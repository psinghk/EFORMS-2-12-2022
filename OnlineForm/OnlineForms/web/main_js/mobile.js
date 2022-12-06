/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {

// Mobile form submission
    $('#mobile_form1').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#mobile_form1').serializeObject());
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        $('.loader').show();
        $.ajax({
            type: "POST",
            url: "mobile_tab1",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $('#profileError').html('');
                $('#profileError').removeClass('alert alert-danger');
                
                resetCSRFRandom();// line added by pr on 22ndjan18
                console.log("data" + data)
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                console.log("jsonvalue" + jsonvalue)
                var error_flag = true;
                if(isEmpty(jsonvalue.profile_values) && isEmpty(jsonvalue.error)) {
                    error_flag = false;
                    $('#profileError').addClass('alert alert-danger');
                    $('#profileError').html("Dear Sir/Ma'am, Your Profile is not in our database so please Enter your details in&ensp;<a href='profile' class='urlDsg'>Profile Section</a>");
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                }
                if (jsonvalue.error.mobile_request_error !== null && jsonvalue.error.mobile_request_error !== "" && jsonvalue.error.mobile_request_error !== undefined)
                {

                    error_flag = false;
                    $('#mobile_request_error').html(jsonvalue.error.mobile_request_error);
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");

                } else {
                    $('#mobile_request_error').html("");
                }
                
                if (jsonvalue.error.mobile_req_pending_error !== null && jsonvalue.error.mobile_req_pending_error !== "" && jsonvalue.error.mobile_req_pending_error !== undefined)
                {

                    error_flag = false;
                    $('#mobile_req_pending_error').html(jsonvalue.error.mobile_req_pending_error);
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");

                } else {
                    $('#mobile_req_pending_error').html("");
                }

                if (jsonvalue.error.mobile_error !== undefined && jsonvalue.error.mobile_error !== null && jsonvalue.error.mobile_error !== "") {
                    error_flag = false;
                    $('#mobile_err').html(jsonvalue.error.mobile_error);
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#mobile_err').html("");
                }
                if (jsonvalue.error.otp_error !== null && jsonvalue.error.otp_error !== "" && jsonvalue.error.otp_error !== undefined) {
                    error_flag = false;
                    $('#otperror').html(jsonvalue.error.otp_error);
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#otperror').html("");
                }
                if (jsonvalue.error.remarks_error !== null && jsonvalue.error.remarks_error !== "" && jsonvalue.error.remarks_error !== undefined) {
                    error_flag = false;
                    $('#remarks_error').html(jsonvalue.error.remarks_error);
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#remarks_error').html("");
                }
                if (jsonvalue.error.remarks_other_error !== null && jsonvalue.error.remarks_other_error !== "" && jsonvalue.error.remarks_other_error !== undefined) {
                    error_flag = false;
                    $('#remarks_other_error').html(jsonvalue.error.remarks_other_error);
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#remarks_other_error').html("");
                }

                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#captchaerror').html(jsonvalue.error.cap_error);
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#captchaerror').html("");
                }


                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                
                
                
                if (jsonvalue.error.nicDateOfBirth_err !== null && jsonvalue.error.nicDateOfBirth_err !== "" && jsonvalue.error.nicDateOfBirth_err !== undefined)
                {

                    error_flag = false;
                    $('#nicDateOfBirth_err').html(jsonvalue.error.nicDateOfBirth_err);
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");

                } else {
                    $('#nicDateOfBirth_err').html("");
                }
                
                
                if (jsonvalue.error.nicDateOfRetirement_err !== null && jsonvalue.error.nicDateOfRetirement_err !== "" && jsonvalue.error.nicDateOfRetirement_err !== undefined)
                {

                    error_flag = false;
                    $('#nicDateOfRetirement_err').html(jsonvalue.error.nicDateOfRetirement_err);
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");

                } else {
                    $('#nicDateOfRetirement_err').html("");
                }
                
                
                if (jsonvalue.error.designation_err !== null && jsonvalue.error.designation_err !== "" && jsonvalue.error.designation_err !== undefined)
                {

                    error_flag = false;
                    $('#designation_err').html(jsonvalue.error.designation_err);
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");

                } else {
                    $('#designation_err').html("");
                }
                
                
                if (jsonvalue.error.displayName_err !== null && jsonvalue.error.displayName_err !== "" && jsonvalue.error.displayName_err !== undefined)
                {

                    error_flag = false;
                    $('#displayName_err').html(jsonvalue.error.displayName_err);
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");

                } else {
                    $('#displayName_err').html("");
                }

                // end, code added by pr on 22ndjan18




                if (!error_flag) {


                } else {
                    //added by sanjeev 03-08-2022
                    $('#profile').html(jsonvalue.profile_values.Formtype.charAt(0).toUpperCase()+jsonvalue.profile_values.Formtype.toString().slice(1) + " Update Form");
                    $('#profile1').html(jsonvalue.profile_values.Formtype.charAt(0).toUpperCase()+jsonvalue.profile_values.Formtype.toString().slice(1) + " Update New Details");
                    $('#profile2').html(jsonvalue.profile_values.Formtype.charAt(0).toUpperCase()+jsonvalue.profile_values.Formtype.toString().slice(1) + " Update Old Details");



                      if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#mobile_form2 #central_div').show();
                        $('#mobile_form2 #state_div').hide();
                        $('#mobile_form2 #other_div').hide();
                        $('#mobile_form2 #other_text_div').addClass("display-hide");
                        var select = $('#mobile_form2 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#mobile_form2 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#mobile_form2 #other_text_div').removeClass("display-hide");
                            $('#mobile_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#mobile_form2 #central_div').hide();
                        $('#mobile_form2 #state_div').show();
                        $('#mobile_form2 #other_div').hide();
                        $('#mobile_form2 #other_text_div').addClass("display-hide");
                        var select = $('#mobile_form2 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#mobile_form2 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#mobile_form2 #other_text_div').removeClass("display-hide");
                            $('#mobile_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#mobile_form2 #central_div').hide();
                        $('#mobile_form2 #state_div').hide();
                        $('#mobile_form2 #other_div').show();
                        $('#mobile_form2 #other_text_div').addClass("display-hide");
                        var select = $('#mobile_form2 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        // alert(jsonvalue.profile_values.Org.toString().toLowerCase())

                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
                            $('#mobile_form2 #other_text_div').removeClass("display-hide");
                            $('#mobile_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#mobile_form2 #central_div').hide();
                        $('#mobile_form2 #state_div').hide();
                        $('#mobile_form2 #other_div').hide();
                    }

                    $('#mobile_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#mobile_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#mobile_form2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#mobile_form2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#mobile_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#mobile_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#mobile_form2 #user_state').val(jsonvalue.profile_values.state);
                    $('#mobile_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#mobile_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#mobile_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#mobile_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    //$('#mobile_form2 #user_mobile').val(jsonvalue.profile_values.mobile);
                    $('#mobile_form2 #user_email').val(jsonvalue.profile_values.email);
                    $('#mobile_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#mobile_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#mobile_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#mobile_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    //$('#mobile_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#mobile_form2 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    $('#mobile_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    //added by sanjeev 3-08-2022
                    
                    $('#mobile_form2 #designationOld').val(jsonvalue.profile_values.designationOld);
                    $('#mobile_form2 #displayNameOld').val(jsonvalue.profile_values.displayNameOld);
                    $('#mobile_form2 #nicDateOfBirthOld').val(jsonvalue.profile_values.nicDateOfBirthOld);
                    $('#mobile_form2 #mobileOld').val(jsonvalue.profile_values.mobileOld);
                    $('#mobile_form2 #nicDateOfRetirementOld').val(jsonvalue.profile_values.nicDateOfRetirementOld);
                    console.log(jsonvalue.profile_values.designationOld);

                    if (jsonvalue.form_details.country_code == null)
                    {

                        $('#mobile_form2 #country_code').val(jsonvalue.form_details.country_code);
                        $('#mobile_form2 #country_code_tab').addClass('display-hide');
                    } else {
                        $('#mobile_form2 #country_code_tab').removeClass('display-hide');

                    }
                    $('#mobile_form2 #new_mobile').val(jsonvalue.form_details.new_mobile);


                    if (jsonvalue.form_details.remarks === "other")
                    {
                        $('#mobile_form2 #other_remarks').val(jsonvalue.form_details.other_remarks);
                        $('#mobile_form2 #remarks').val(jsonvalue.form_details.remarks);
                        $('#mobile_form2 #remarks_div').removeClass('display-hide');
                    } else {
                        $('#mobile_form2 #remarks').val(jsonvalue.form_details.remarks);
                        $('#mobile_form2 #remarks_div').addClass('display-hide');
                    }
                    $('#mobile_form2 #nicDateOfBirth').val(jsonvalue.form_details.nicDateOfBirth);
                    $('#mobile_form2 #nicDateOfRetirement').val(jsonvalue.form_details.nicDateOfRetirement);
                    $('#mobile_form2 #designation').val(jsonvalue.form_details.designation);
                    $('#mobile_form2 #displayName').val(jsonvalue.form_details.displayName);
                    
                    $('.edit').removeClass('display-hide');
                    $('#mobile_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#mobile_form2 :button[type='button']").removeAttr('disabled');
                    $("#mobile_form2 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                }

            }, error: function ()
            {
                $('#tab1').show();
            }, complete() {
                $('.loader').hide();
            }
        });
    });
    $('#mobile_form2').submit(function (e) {
        e.preventDefault();
        $('.loader').show();
        $("#mobile_form2 :disabled").removeAttr('disabled');
        $('#mobile_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#mobile_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#mobile_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#mobile_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#mobile_form2').serializeObject());
        $('#mobile_preview_tab #user_email').prop('disabled', 'true'); // 20th march


        $('#mobile_form2 .edit').removeClass('display-hide');


        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        $.ajax({
            type: "POST",
            url: "mobile_tab2",
            data: {data: data, action_type: "confirm", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 22ndjan18 var myJSON = JSON.stringify(data);
                console.log("data" + data)
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                console.log("jsonvalue" + jsonvalue)
                var error_flag = true;

                if (jsonvalue.error.mobile_request_error !== null && jsonvalue.error.mobile_request_error !== "" && jsonvalue.error.mobile_request_error !== undefined)
                {

                    error_flag = false;
                    $('#mobile_form2 #mobile_request_error').html(jsonvalue.error.mobile_request_error);
                    $('#mobile_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_form2 #imgtxt').val("");

                } else {
                    $('#mobile_form2 #mobile_request_error').html("");
                }
                
                if (jsonvalue.error.mobile_req_pending_error !== null && jsonvalue.error.mobile_req_pending_error !== "" && jsonvalue.error.mobile_req_pending_error !== undefined)
                {

                    error_flag = false;
                    $('#mobile_form2 #mobile_req_pending_error').html(jsonvalue.error.mobile_req_pending_error);
                    $('#mobile_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_form2 #imgtxt').val("");

                } else {
                    $('#mobile_form2 #mobile_req_pending_error').html("");
                }
                
                
                
                
                if (jsonvalue.error.mobile_error !== null && jsonvalue.error.mobile_error !== "" && jsonvalue.error.mobile_error !== undefined) {
                    error_flag = false;
                    $('#mobile_form2 #mobile_error').html(jsonvalue.error.mobile_error)
                } else {
                    $('#mobile_form2 #mobile_error').html("")
                }
                if (jsonvalue.error.remarks_error !== null && jsonvalue.error.remarks_error !== "" && jsonvalue.error.remarks_error !== undefined) {
                    error_flag = false;
                    $('#mobile_form2 #remarks_error').html(jsonvalue.error.remarks_error);
                    $('#mobile_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_form2 #imgtxt').val("");
                } else {
                    $('#mobile_form2 #remarks_error').html("");
                }
                if (jsonvalue.error.remarks_other_error !== null && jsonvalue.error.remarks_other_error !== "" && jsonvalue.error.remarks_other_error !== undefined) {
                    error_flag = false;
                    $('#mobile_form2 #remarks_other_error').html(jsonvalue.error.remarks_other_error);
                    $('#mobile_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_form2 #imgtxt').val("");
                } else {
                    $('#mobile_form2 #remarks_other_error').html("");
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
                    $('#mobile_form2 #useremployment_error').focus();
                    $('#mobile_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#mobile_form2 #minerror').focus();
                    $('#mobile_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#mobile_form2 #deperror').focus();
                    $('#mobile_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#mobile_form2 #other_dept').focus();
                    $('#mobile_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#mobile_form2 #smierror').focus();
                    $('#mobile_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#mobile_form2 #state_error').focus();
                    $('#mobile_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#mobile_form2 #org_error').focus();
                    $('#mobile_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#mobile_form2 #ca_design').focus();
                    $('#mobile_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#mobile_form2 #hod_name').focus();
                    $('#mobile_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#mobile_form2 #hod_mobile').focus();
                    $('#mobile_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#mobile_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#mobile_form2 #hod_tel').focus();
                    $('#mobile_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #hodtel_error').html("");
                }
                // profile on 20th march



                if (error_flag) {
                    // show modal
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'mobile'},
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
            }, complete() {
                $('.loader').hide();
            }
        });
    });
    $('#mobile_form2 .edit').click(function () {
        var employment = $('#mobile_preview_tab #user_employment').val();
        var min = $('#mobile_preview_tab #min').val();
        var dept = $('#mobile_preview_tab #dept').val();
        var statecode = $('#mobile_preview_tab #stateCode').val();
        var Smi = $('#mobile_preview_tab #Smi').val();
        var Org = $('#mobile_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#mobile_preview_tab #min');
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
                var select = $('#mobile_preview_tab #dept');
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
                $('#mobile_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#mobile_preview_tab #stateCode');
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
                var select = $('#mobile_preview_tab #Smi');
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
                $('#mobile_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#mobile_preview_tab #central_div').hide();
            $('#mobile_preview_tab #state_div').hide();
            $('#mobile_preview_tab #other_div').show();
            $('#mobile_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#mobile_preview_tab #Org');
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
                $('#mobile_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#mobile_preview_tab #central_div').hide();
            $('#mobile_preview_tab #state_div').hide();
            $('#mobile_preview_tab #other_div').hide();
        }

        $('#mobile_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');

        $('#mobile_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#mobile_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#mobile_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#mobile_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        //added by sanjeev 03-08-2022
        $('#mobile_preview #designation').val(jsonvalue.designation);
        $('#mobile_preview #displayName').val(jsonvalue.displayName);
        $('#mobile_preview #designationOld').val(jsonvalue.designationOld);
        $('#mobile_preview #displayNameOld').val(jsonvalue.displayNameOld);
        $('#mobile_preview #nicDateOfBirthOld').val(jsonvalue.nicDateOfBirthOld);
        $('#mobile_preview #mobileOld').val(jsonvalue.mobileOld);
        $('#mobile_preview #nicDateOfRetirementOld').val(jsonvalue.nicDateOfRetirementOld)

        $('#mobile_preview_tab #REditPreview #hod_email').removeAttr('disabled');
        $('#mobile_preview_tab #new_mobile').prop('disabled', 'true');
        $('#mobile_preview_tab #nicDateOfBirth').prop('disabled', 'true');
        $('#mobile_preview_tab #nicDateOfRetirement').prop('disabled', 'true');
        $('#mobile_preview_tab #designation').prop('disabled', 'true');
        $('#mobile_preview_tab #displayName').prop('disabled', 'true');
        
        $(this).addClass('display-hide');
        //$(this).hide();
    });
    $('#mobile_form2 #confirm').click(function () {
        $('.loader').show();
        $("#mobile_form2 :disabled").removeAttr('disabled');
        $('#mobile_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#mobile_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#mobile_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#mobile_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#mobile_form2').serializeObject());
        $('#mobile_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18


        $.ajax({
            type: "POST",
            url: "mobile_tab2",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                console.log("data" + data)
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                console.log("jsonvalue" + jsonvalue)
                var error_flag = true;

                if (jsonvalue.error.mobile_error !== null && jsonvalue.error.mobile_error !== "" && jsonvalue.error.mobile_error !== undefined) {
                    error_flag = false;
                    $('#mobile_form2 #mobile_error').html(jsonvalue.error.mobile_error)
                } else {
                    $('#mobile_form2 #mobile_error').html("")
                }
                 if (jsonvalue.error.mobile_req_pending_error !== null && jsonvalue.error.mobile_req_pending_error !== "" && jsonvalue.error.mobile_req_pending_error !== undefined)
                {

                    error_flag = false;
                    $('#mobile_form2 #mobile_req_pending_error').html(jsonvalue.error.mobile_req_pending_error);
                    $('#mobile_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_form2 #imgtxt').val("");

                } else {
                    $('#mobile_form2 #mobile_req_pending_error').html("");
                }
                 if (jsonvalue.error.mobile_request_error !== null && jsonvalue.error.mobile_request_error !== "" && jsonvalue.error.mobile_request_error !== undefined)
                {

                    error_flag = false;
                    $('#mobile_form2 #mobile_request_error').html(jsonvalue.error.mobile_request_error);
                    $('#mobile_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_form2 #imgtxt').val("");

                } else {
                    $('#mobile_form2 #mobile_request_error').html("");
                }
                
                
                if (jsonvalue.error.remarks_error !== null && jsonvalue.error.remarks_error !== "" && jsonvalue.error.remarks_error !== undefined) {
                    error_flag = false;
                    $('#mobile_form2 #remarks_error').html(jsonvalue.error.remarks_error);
                    $('#mobile_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_form2 #imgtxt').val("");
                } else {
                    $('#mobile_form2 #remarks_error').html("");
                }
                if (jsonvalue.error.remarks_other_error !== null && jsonvalue.error.remarks_other_error !== "" && jsonvalue.error.remarks_other_error !== undefined) {
                    error_flag = false;
                    $('#mobile_form2 #remarks_other_error').html(jsonvalue.error.remarks_other_error);
                    $('#mobile_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_form2 #imgtxt').val("");
                } else {
                    $('#mobile_form2 #remarks_other_error').html("");
                }
                if (jsonvalue.error.remarks_error !== null && jsonvalue.error.remarks_error !== "" && jsonvalue.error.remarks_error !== undefined) {
                    error_flag = false;
                    $('#mobile_form2 #remarks_error').html(jsonvalue.error.remarks_error);
                    $('#mobile_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_form2 #imgtxt').val("");
                } else {
                    $('#mobile_form2 #remarks_error').html("");
                }
                if (jsonvalue.error.remarks_other_error !== null && jsonvalue.error.remarks_other_error !== "" && jsonvalue.error.remarks_other_error !== undefined) {
                    error_flag = false;
                    $('#mobile_form2 #remarks_other_error').html(jsonvalue.error.remarks_other_error);
                    $('#mobile_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_form2 #imgtxt').val("");
                } else {
                    $('#mobile_form2 #remarks_other_error').html("");
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
                    $('#mobile_form2 #useremployment_error').focus();
                    $('#mobile_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#mobile_form2 #minerror').focus();
                    $('#mobile_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#mobile_form2 #deperror').focus();
                    $('#mobile_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#mobile_form2 #other_dept').focus();
                    $('#mobile_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#mobile_form2 #smierror').focus();
                    $('#mobile_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#mobile_form2 #state_error').focus();
                    $('#mobile_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#mobile_form2 #org_error').focus();
                    $('#mobile_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#mobile_form2 #ca_design').focus();
                    $('#mobile_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#mobile_form2 #hod_name').focus();
                    $('#mobile_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#mobile_form2 #hod_mobile').focus();
                    $('#mobile_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#mobile_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#mobile_form2 #hod_tel').focus();
                    $('#mobile_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#mobile_form2 #hodtel_error').html("");
                }
                // profile on 20th march
                if (!error_flag) {
                    $("#mobile_form2 :disabled").removeAttr('disabled');
                    $('#mobile_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#mobile_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#mobile_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {


                    if ($('#mobile_form2 #tnc').is(":checked"))
                    {

                        $('#mobile_form2 #tnc_error').html("");
                        //$('#mobile_form2').submit();
                        //$('#stack3').modal({backdrop: 'static', keyboard: false});


//                        if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
//                        {
//                            $('#mobile_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                            $('#mobile_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                            $('#mobile_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
//
//
//                        } else if (jsonvalue.form_details.min == "External Affairs")
//                        {
//                            $('#mobile_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#mobile_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#mobile_form_confirm #fill_hod_mobile').html("+919384664224");
//                        } 
                       // else {
                            $('#mobile_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#mobile_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#mobile_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                       // }
                        $('#mobile_form2 #tnc_error').html("");
//                        if(mobVal) {
//                            $('#mobile_form2').submit();
//                            //window.location.href = 'e_sign';
//                        } else {
//                            //resetCSRFRandom();// line added by pr on 22ndjan18
//                            $('#stack3').modal({backdrop: 'static', keyboard: false});
//                        }
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                        $('#mobile_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                        $('#mobile_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                        $('#mobile_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                        $('#mobile_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    } else {
                        $('#mobile_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        $("#mobile_form2 #tnc").removeAttr('disabled');
                        $('#mobile_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#mobile_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //line commented by MI on25thjan19
                        // $('#mobile_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    }
                }
            }, error: function ()
            {
                $('#tab2').show();
            }, complete() {
                $('.loader').hide();
            }

        });
    });
    $('#mobile_form_confirm #confirmYes').click(function () {
        $('#mobile_form2').submit();
        $('#stack3').modal('toggle');
    });

    $(document).on('click', '#mobile_preview .edit', function () {
        // $('#mobile_preview .edit').click(function () {

        var employment = $('#mobile_preview_tab #user_employment').val();
        var min = $('#mobile_preview_tab #min').val();
        var dept = $('#mobile_preview_tab #dept').val();
        var statecode = $('#mobile_preview_tab #stateCode').val();
        var Smi = $('#mobile_preview_tab #Smi').val();
        var Org = $('#mobile_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#mobile_preview_tab #min');
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
                var select = $('#mobile_preview_tab #dept');
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
                $('#mobile_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#mobile_preview_tab #stateCode');
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
                var select = $('#mobile_preview_tab #Smi');
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
                $('#mobile_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#mobile_preview_tab #central_div').hide();
            $('#mobile_preview_tab #state_div').hide();
            $('#mobile_preview_tab #other_div').show();
            $('#mobile_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#mobile_preview_tab #Org');
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
                $('#mobile_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#mobile_preview_tab #central_div').hide();
            $('#mobile_preview_tab #state_div').hide();
            $('#mobile_preview_tab #other_div').hide();
        }

        $('#mobile_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#mobile_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#mobile_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#mobile_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#mobile_preview_tab #new_mobile').prop('disabled', 'true');
        $('#mobile_preview_tab #nicDateOfBirth').prop('disabled', 'true');
        $('#mobile_preview_tab #nicDateOfRetirement').prop('disabled', 'true');
        $('#mobile_preview_tab #designation').prop('disabled', 'true');
        $('#mobile_preview_tab #displayName').prop('disabled', 'true');
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            

        if ($("#comingFrom").val("admin"))
        {
            $("#mobile_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#mobile_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#mobile_preview #confirm', function () {

        //$('#mobile_preview #confirm').click(function () {
        $('#mobile_preview').submit();
        //  $('#mobile_preview_form').modal('toggle');
    });
    $('#mobile_preview').submit(function (e) {
        e.preventDefault();
        $('.loader').show();
        $("#mobile_preview :disabled").removeAttr('disabled');
        $('#mobile_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#mobile_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#mobile_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#mobile_preview_tab #user_email').removeAttr('disabled');// 20th march                  
        var data = JSON.stringify($('#mobile_preview').serializeObject());
        $('#mobile_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        $('#mobile_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');


        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "mobile_tab2",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 23rdjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.mobile_error !== null && jsonvalue.error.mobile_error !== "" && jsonvalue.error.mobile_error !== undefined) {
                    error_flag = false;
                    $('#mobile_preview #mobile_error').html(jsonvalue.error.mobile_error)
                } else {
                    $('#mobile_preview #mobile_error').html("")
                }


                if (jsonvalue.error.country_code_err !== null && jsonvalue.error.country_code_err !== "" && jsonvalue.error.country_code_err !== undefined) {
                    error_flag = false;
                    $('#mobile_preview #country_code_err').html(jsonvalue.error.country_code_err)
                } else {
                    $('#mobile_preview #country_code_err').html("")
                }
                if (jsonvalue.error.mobile_req_pending_error !== null && jsonvalue.error.mobile_req_pending_error !== "" && jsonvalue.error.mobile_req_pending_error !== undefined)
                {

                    error_flag = false;
                    $('#mobile_preview #mobile_req_pending_error').html(jsonvalue.error.mobile_req_pending_error);
                    $('#mobile_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_preview #imgtxt').val("");

                } else {
                    $('#mobile_preview #mobile_req_pending_error').html("");
                }
                 if (jsonvalue.error.mobile_request_error !== null && jsonvalue.error.mobile_request_error !== "" && jsonvalue.error.mobile_request_error !== undefined)
                {

                    error_flag = false;
                    $('#mobile_preview #mobile_request_error').html(jsonvalue.error.mobile_request_error);
                    $('#mobile_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#mobile_preview #imgtxt').val("");

                } else {
                    $('#mobile_preview #mobile_request_error').html("");
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
                    $('#mobile_preview #useremployment_error').focus();
                    $('#mobile_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#mobile_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#mobile_preview #minerror').focus();
                    $('#mobile_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#mobile_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#mobile_preview #deperror').focus();
                    $('#mobile_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#mobile_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#mobile_preview #other_dept').focus();
                    $('#mobile_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#mobile_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#mobile_preview #smierror').focus();
                    $('#mobile_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#mobile_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#mobile_preview #state_error').focus();
                    $('#mobile_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#mobile_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#mobile_preview #org_error').focus();
                    $('#mobile_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#mobile_preview #org_error').html("");
                }
                // profile 20th march 
                if (!error_flag)
                {

                    $("#mobile_preview :disabled").removeAttr('disabled');
                    $('#mobile_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#mobile_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {
                    $('#mobile_preview_form').modal('toggle');
                }
            }, error: function (request, error)
            {
                console.log('error');
            }, complete() {
                $('.loader').hide();
            }
        });
    });
});

$(document).on('blur', '#new_mobile', function (e) {
    e.preventDefault();
    var new_mobile = $("#new_mobile").val();
    var country_code = $("#country_code").val();
    var mobile = $("#mobile_txt_val").text();
    $('.loader').show();
    // write ajax code
    $.ajax({
        type: "POST",
        url: "OtpGenerate_newmobile",
        data: {new_mobile: new_mobile, mobile: mobile, country_code: country_code},
        datatype: JSON,
        success: function (data) {

            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            console.log("jsonvalue.returnString" + jsonvalue.returnString);


            if (jsonvalue.returnString === "moberr")
            {

                //$("#new-code").addClass("display-hide");
                $('#mobile_err').html("Please Enter Mobile number in correct format.");
                $("#new_otp_div").addClass('display-hide');
            } else if (jsonvalue.returnString != null && jsonvalue.returnString.indexOf("There is already an email address") > -1)
            {

                $("#new-code").addClass("display-hide");
                $('#mobile_err').html(jsonvalue.returnString);// text change 20thmay2020
            } else {

                $('#mobile_err').html("");
                $("#new_otp_div").removeClass('display-hide');

            }
        }, complete() {
            $('.loader').hide();
        }
    });


});
function isEmpty(obj) {
    return Object.keys(obj).length === 0;
}
$('.selectReason').change(function () {


    var field_form = $(this).closest('form').attr('id');

    var selectReason = $("#" + field_form + " .selectReason").val();

    if (selectReason === "other")
    {
        $("#" + field_form + " #remarks_div").removeClass("display-hide");
    } else {
        $("#" + field_form + " #remarks_div").addClass("display-hide");
    }
})

