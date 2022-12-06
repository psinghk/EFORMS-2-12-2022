$(document).on('find click', '.page-wrapper button', function () {
    var field_name = $(this).attr('name');
    if (field_name === 'download') {
        var data = $(this).val();
        window.location = "download_2?uploaded_filename=" + data;
    }
});
$("input[name='dlist_single_form']").click(function () {
    var distType = $(this).val();
    if (distType === 'bulk') {
        $("#dlist-single-div").addClass('d-none');
        $("#dlist-double-div").addClass('d-none');
        $("#dlist-bulk-div").removeClass('d-none');

    } else {
        $("#dlist-single-div").removeClass('d-none');
        $("#dlist-double-div").removeClass('d-none');
        $("#dlist-bulk-div").addClass('d-none');
    }
});
$("#dlist_bulkform1").submit(function (e) {
    $(".loader").show();
    e.preventDefault();
    var data = JSON.stringify($('#dlist_bulkform1').serializeObject());
    console.log("----bulk data====" + data);
    var cert = $('#cert').val();
    var user_file = $('#user_file_dlist').val();
    var CSRFRandom = $("#CSRFRandom").val();
    $.ajax({
        type: "POST",
        url: "dlistBulkSubmission",
        data: {data: data, cert: cert, CSRFRandom: CSRFRandom, user_file: user_file},
        datatype: JSON,
        success: function (data)
        {
            console.log(data);
            $(".loader").hide();
            resetCSRFRandom();
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            var error_flag = true;
            if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined)
            {
                $('#dlist_bulkform1 #file_err').html(jsonvalue.error.file_err);
                $('#dlist_bulkform1 #file_err').focus();
                error_flag = false;
                $('#dlist_bulkform1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#dlist_bulkform1 #captcha').val("");
                $('#dlist_bulkform1 #imgtxt').val("");

            } else {
                $('#dlist_bulkform1 #file_err').html("");
            }
            if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
            {
                $('#dlist_bulkform1 #captchaerror').html(jsonvalue.error.cap_error);
                error_flag = false;
                $('#dlist_bulkform1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("");
            } else {
                $('#captchaerror').html("");
            }
            if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
            {
                $('.captchaerror').html(jsonvalue.error.csrf_error);
                error_flag = false;
                $('#dlist_bulkform1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#dlist_bulkform1 #imgtxt').val("");
            }
            // alert("error_flag"+error_flag)
            if (error_flag) {
                $("#bulk-2").removeClass('display-hide');
                $("#bulk-1").addClass('display-hide');
            } else {
                $("#bulk-2").addClass('display-hide');
                $("#bulk-1").removeClass('display-hide');
            }
        }, error: function ()
        {
            $(".loader").hide();
            console.log('error');
        }
    });
});
$("#dlist_bulkform2").submit(function (e) {
    //  alert("Inside Dlist bulk:");
    $(".loader").show();
    e.preventDefault();
    var data = JSON.stringify($('#dlist_bulkform2').serializeObject());
    console.log("----bulk data====" + data);
    var cert = $('#cert').val();
    var user_file = $('#user_file_dlist').val();
    var CSRFRandom = $("#CSRFRandom").val();

    $.ajax({
        type: "POST",
        url: "dlistBulkSubmission2",
        data: {data: data, cert: cert, CSRFRandom: CSRFRandom, user_file: user_file},
        datatype: JSON,
        success: function (data)
        {
            console.log(data);
            $(".loader").hide();
            resetCSRFRandom();
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            var error_flag = true;
            if (jsonvalue.error.error_file !== null && jsonvalue.error.error_file !== "" && jsonvalue.error.error_file !== undefined)
            {
                $('#dlist_bulkform2 #error_file').html(jsonvalue.error.error_file)
                error_flag = false;
            } else {
                $('#dlist_bulkform2 #error_file').html("")
            }
            if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined)
            {
                $('#dlist_bulkform2 #file_err').html(jsonvalue.error.file_err);
                error_flag = false;
                $('#dlist_bulkform #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#dlist_bulkform #captcha1').val("");

            } else {
                $('#dlist_bulkform #file_err').html("");
            }
            if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
            {
                $('#captchaerror').html(jsonvalue.error.cap_error);
                error_flag = false;
                $('#captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("");
            } else {
                $('#dlist_bulkform .captchaerror').html("");
            }

            if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
            {
                $('.captchaerror').html(jsonvalue.error.csrf_error);
                error_flag = false;
                $('#dlist_bulkform2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#dlist_bulkform2 #imgtxt2').val("");
            }
            if (jsonvalue.error.email_info_error !== null && jsonvalue.error.email_info_error !== "" && jsonvalue.error.email_info_error !== undefined)
            {
                error_flag = false;
                bootbox.dialog({
                    message: "<p style='text-align: center; font-size: 15px;font-weight:bold;'> You may register only for the following services :-</p><ul><li> Dlist Service</li> <li>VPN Service</li> <li>Security Audit Service</li> <li>e-Sampark Service</li><li>Cloud Service</li><li>Domain Registration Service</li><li>Firewall Service</li><li>Reservation for video conferencing Service</li> <li>Web Application Firewall services</li></ul>"
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
                console.log("INSIDE IF");
                if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {

                    $('#dlist_bulkform #central_div').show();
                    $('#dlist_bulkform #state_div').hide();
                    $('#dlist_bulkform #other_div').hide();
                    $('#dlist_bulkform #other_text_div').addClass("display-hide");
                    var select = $('#dlist_bulkform #min');
                    select.find('option').remove();
                    $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                    var select = $('#dlist_bulkform #dept');
                    select.find('option').remove();
                    $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                    if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                        $('#dlist_bulkform #other_text_div').removeClass("display-hide");
                        $('#dlist_bulkform #other_dept').val(jsonvalue.profile_values.other_dept);
                    }
                } else if (jsonvalue.profile_values.user_employment === 'State') {
                    $('#dlist_bulkform #central_div').hide();
                    $('#dlist_bulkform #state_div').show();
                    $('#dlist_bulkform #other_div').hide();
                    $('#dlist_bulkform #other_text_div').addClass("display-hide");
                    var select = $('#dlist_bulkform #stateCode');
                    select.find('option').remove();
                    $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                    var select = $('#dlist_bulkform  #Smi');
                    select.find('option').remove();
                    $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                    if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                        $('#dlist_bulkform  #other_text_div').removeClass("display-hide");
                        $('#dlist_bulkform #other_dept').val(jsonvalue.profile_values.other_dept);
                    }

                } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                    $('#dlist_bulkform #central_div').hide();
                    $('#dlist_bulkform #state_div').hide();
                    $('#dlist_bulkform #other_div').show();
                    $('#dlist_bulkform #other_text_div').addClass("display-hide");
                    var select = $('#dlist_bulkform #Org');
                    select.find('option').remove();
                    $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                    if (jsonvalue.profile_values.Org === 'other') {
                        $('#dlist_bulkform #other_text_div').removeClass("display-hide");
                        $('#dlist_bulkform #other_dept').val(jsonvalue.profile_values.other_dept);
                    }

                } else {
                    $('#dlist_bulkform #central_div').hide();
                    $('#dlist_bulkform #state_div').hide();
                    $('#dlist_bulkform #other_div').hide();
                }
                var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                $('#dlist_bulkform #user_employment').val(jsonvalue.profile_values.user_employment);
                $('#dlist_bulkform #stateCode').val(jsonvalue.profile_values.stateCode);
                $('#dlist_bulkform #user_name').val(jsonvalue.profile_values.cn);
                $('#dlist_bulkform #user_design').val(jsonvalue.profile_values.desig);
                $('#dlist_bulkform #user_address').val(jsonvalue.profile_values.postalAddress);
                $('#dlist_bulkform #user_city').val(jsonvalue.profile_values.nicCity);
                $('#dlist_bulkform #user_state').val(jsonvalue.profile_values.state);
                $('#dlist_bulkform #user_pincode').val(jsonvalue.profile_values.pin);
                $('#dlist_bulkform #user_ophone').val(jsonvalue.profile_values.ophone);
                $('#dlist_bulkform #user_rphone').val(jsonvalue.profile_values.rphone);
                //$('#dlist_bulkform #user_mobile').val(jsonvalue.profile_values.mobile);
                $('#dlist_bulkform #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                $('#dlist_bulkform #user_email').val(jsonvalue.profile_values.email);
                $('#dlist_bulkform #user_empcode').val(jsonvalue.profile_values.emp_code);
                $('#dlist_bulkform #hod_name').val(jsonvalue.profile_values.hod_name);
                $('#dlist_bulkform #hod_email').val(jsonvalue.profile_values.hod_email);
                $('#dlist_bulkform #hod_tel').val(jsonvalue.profile_values.hod_tel);
                //$('#dlist_bulkform #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                $('#dlist_bulkform #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                $('#dlist_bulkform #ca_design').val(jsonvalue.profile_values.ca_design);


                $('#dlist_bulkform #list_name').val(jsonvalue.form_details.list_name);
                $('#dlist_bulkform #description_list').val(jsonvalue.form_details.description_list);
                $('#dlist_bulkform #validity_date1').val(jsonvalue.form_details.validity_date);
                $('#dlist_bulkform #uploaded_filename').text(jsonvalue.form_details.uploaded_filename);
                if (jsonvalue.form_details.list_mod === 'yes') {
                    $('#dlist_bulkform #list_mod_yes').prop('checked', true);
                } else {
                    $('#dlist_bulkform #list_mod_no').prop('checked', true);
                }

                if (jsonvalue.form_details.allowed_member === 'yes') {
                    $('#dlist_bulkform #allowed_member_yes').prop('checked', true);
                } else {
                    $('#dlist_bulkform #allowed_member_no').prop('checked', true);
                }

                if (jsonvalue.form_details.list_temp === 'yes') {
                    $('#dlist_bulkform #list_temp_yes').prop('checked', true);
                    $('#dlist_bulkform #validity_date_show').show();

                } else {
                    $('#dlist_bulkform #list_temp_no').prop('checked', true);
                    $('#dlist_bulkform #validity_date_show').hide();
                }

                if (jsonvalue.form_details.non_nicnet === 'yes') {
                    $('#dlist_bulkform #non_nicnet_yes').prop('checked', true);

                } else {
                    $('#dlist_bulkform #non_nicnet_no').prop('checked', true);
                }
                $('#dlist_bulkform #memberCount').val(jsonvalue.form_details.memberCount);


                if (jsonvalue.bulkData.moderatorData == null) {
                    $('#dlist_bulkform #moderator_collection').addClass("d-none");
                    $('#dlist_bulkform #mod_heading').addClass("d-none");
                }
                if (jsonvalue.bulkData.ownerData == null) {
                    $('#dlist_bulkform #owner_collection').addClass("d-none");
                    $('#dlist_bulkform #own_heading').addClass("d-none");
                }
                $('#dlist_bulkform #auth_off_name').val(jsonvalue.form_details.t_off_name);
                $('#dlist_bulkform #auth_email').val(jsonvalue.form_details.tauth_email);
                $('#dlist_bulkform #mobile').val(jsonvalue.form_details.tmobile);

                $('.edit').removeClass('display-hide');
                $('#dlist_bulkform #dlist_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                $("#dlist_bulkform :button[type='button']").removeAttr('disabled');
                $("#dlist_bulkform #tnc").removeAttr('disabled');
                $('#large1').modal({backdrop: 'static', keyboard: false});
                $('#email_head1').addClass('display-hide');
                $('#email_head2').addClass('display-hide');
                $('#bulk-1').addClass('display-hide');

            } else {
                console.log("INSIDE ELSE");
                $('#email_head1').removeClass('display-hide');
                $('#email_head2').removeClass('display-hide');
                $('#bulk-1').addClass('display-hide');
                //$('#bulk-2').addClass('display-hide');
//                if (error_flag) {
////                    $("#bulk-2").addClass('d-none');
//                    $('#large1').modal({backdrop: 'static', keyboard: false});
//                }
            }

            var renamedPath = jsonvalue.form_details.renamed_filepath;

            var pathName = renamedPath.replace(".xls", "");

            pathName = pathName.trim();

            var valid_filepath = pathName + "-valid.xls";

            var notvalid_filepath = pathName + "-notvalid.xls";

            var error_filepath = pathName + "-error.xls";


        }, error: function ()
        {
            $(".loader").hide();
            console.log('error');
        }
    });
});
$('#dlist_bulkform #confirm').click(function (e) {
    e.preventDefault();
    $("#dlist_bulkform :disabled").removeAttr('disabled');
    $('#dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
    $('#dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
    $('#dlist_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
    $('#dlist_preview_tab #user_email').removeAttr('disabled');
    var data = JSON.stringify($('#dlist_bulkform').serializeObject());
    $('#dlist_preview_tab #user_email').prop('disabled', 'true');

    var CSRFRandom = $("#CSRFRandom").val();

    $.ajax({
        type: "POST",
        url: "dLIST_Bulktab",
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
                $('#dlist_bulkform #list_name_err').html(jsonvalue.error.list_name_error);
                $('#dlist_bulkform #list_name_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #list_name_err').html("");
            }
            if (jsonvalue.error.list_desc_error !== null && jsonvalue.error.list_desc_error !== "" && jsonvalue.error.list_desc_error !== undefined)
            {
                $('#dlist_bulkform #description_list_err').html(jsonvalue.error.list_desc_error);
                $('#dlist_bulkform #description_list_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #description_list_err').html("");
            }
            if (jsonvalue.error.list_mod_error !== null && jsonvalue.error.list_mod_error !== "" && jsonvalue.error.list_mod_error !== undefined)
            {
                $('#dlist_bulkform #list_mod_err').html(jsonvalue.error.list_mod_error);
                $('#dlist_bulkform #list_mod_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #list_mod_err').html("");
            }
            if (jsonvalue.error.list_allowed_error !== null && jsonvalue.error.list_allowed_error !== "" && jsonvalue.error.list_allowed_error !== undefined)
            {
                $('#dlist_bulkform #allowed_member_err').html(jsonvalue.error.list_allowed_error);
                $('#dlist_bulkform #allowed_member_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #allowed_member_err').html("");
            }
            if (jsonvalue.error.list_temp_error !== null && jsonvalue.error.list_temp_error !== "" && jsonvalue.error.list_temp_error !== undefined)
            {
                $('#dlist_bulkform #list_temp_err').html(jsonvalue.error.list_temp_error);
                $('#dlist_bulkform #list_temp_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #list_temp_err').html("");
            }
            if (jsonvalue.error.list_validdate_error !== null && jsonvalue.error.list_validdate_error !== "" && jsonvalue.error.list_validdate_error !== undefined)
            {
                $('#dlist_bulkform #validity_date_err').html(jsonvalue.error.list_validdate_error);
                $('#dlist_bulkform #validity_date_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #validity_date_err').html("");
            }

            if (jsonvalue.error.list_nonnicnet_error !== null && jsonvalue.error.list_nonnicnet_error !== "" && jsonvalue.error.list_nonnicnet_error !== undefined)
            {
                $('#dlist_bulkform #non_nicnet_err').html(jsonvalue.error.list_nonnicnet_error);
                $('#dlist_bulkform #non_nicnet_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #non_nicnet_err').html("");
            }

            if (jsonvalue.error.mod_name_error !== null && jsonvalue.error.mod_name_error !== "" && jsonvalue.error.mod_name_error !== undefined)
            {
                $('#dlist_bulkform #tauth_off_name_err').html(jsonvalue.error.mod_name_error);
                $('#dlist_bulkform #tauth_off_name_err').focus();

                error_flag = false;
            } else {
                $('#dlist_bulkform #tauth_off_name_err').html("");
            }
            if (jsonvalue.error.mod_email_error !== null && jsonvalue.error.mod_email_error !== "" && jsonvalue.error.mod_email_error !== undefined)
            {
                $('#dlist_bulkform #tauth_email_err').html(jsonvalue.error.mod_email_error);
                $('#dlist_bulkform #tauth_email_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #tauth_email_err').html("");
            }

            if (jsonvalue.error.mod_mobile_error !== null && jsonvalue.error.mod_mobile_error !== "" && jsonvalue.error.mod_mobile_error !== undefined)
            {
                $('#dlist_bulkform #tmobile_err').html(jsonvalue.error.mod_mobile_error);
                $('#dlist_bulkform #tmobile_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #tmobile_err').html("");
            }
            if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
            {
                $('#dlist_bulkform #useremployment_error').focus();
                $('#dlist_bulkform #useremployment_error').html(jsonvalue.error.employment_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #useremployment_error').html("");
            }
            if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
            {
                $('#dlist_bulkform #minerror').focus();
                $('#dlist_bulkform #minerror').html(jsonvalue.error.ministry_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #minerror').html("");
            }
            if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
            {
                $('#dlist_bulkform #deperror').focus();
                $('#dlist_bulkform #deperror').html(jsonvalue.error.dept_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #deperror').html("");
            }
            if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
            {
                $('#dlist_bulkform #other_dept').focus();
                $('#dlist_bulkform #other_dep_error').html(jsonvalue.error.dept_other_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #other_dep_error').html("");
            }
            if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
            {
                $('#dlist_bulkform #smierror').focus();
                $('#dlist_bulkform #smierror').html(jsonvalue.error.state_dept_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #smierror').html("");
            }
            if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
            {
                $('#dlist_bulkform #state_error').focus();
                $('#dlist_bulkform #state_error').html(jsonvalue.error.state_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #state_error').html("");
            }
            if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
            {
                $('#dlist_bulkform #org_error').focus();
                $('#dlist_bulkform #org_error').html(jsonvalue.error.org_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #org_error').html("");
            }
            if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
            {
                $('#dlist_bulkform #ca_design').prop("readonly", false);
                $('#dlist_bulkform #ca_design').focus();
                $('#dlist_bulkform #cadesign_error').html(jsonvalue.error.ca_desg_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #cadesign_error').html("");
            }
            if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
            {
                $('#dlist_bulkform #hod_name').prop("readonly", false);
                $('#dlist_bulkform #hod_name').focus();
                $('#dlist_bulkform #hodname_error').html(jsonvalue.error.hod_name_error);
                error_flag = false;
            } else {
                $('#hodname_error').html("");
            }
            if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
            {
                $('#dlist_bulkform #hod_mobile').focus();
                $('#dlist_bulkform #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #hodmobile_error').html("");
            }
            if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
            {
                $('#dlist_bulkform #hodemail_error').html(jsonvalue.error.hod_email_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #hodemail_error').html("");
            }
            if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
            {
                $('#dlist_bulkform #hod_tel').prop("readonly", false);
                $('#dlist_bulkform #hod_tel').focus();
                $('#dlist_bulkform #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #hodtel_error').html("");
            }
            if (!error_flag) {
                $("#dlist_bulkform :disabled").removeAttr('disabled');
                $('#dlist_bulkform #dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                $('#dlist_bulkform #dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                $('#dlist_bulkform #dlist_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
            } else {

                if ($('#dlist_bulkform #tnc').is(":checked"))
                {
                    // alert("Inside BULK Ro details");
                    $('#dlist_bulkform #tnc_error').html("");
                    $('#stack777').modal({backdrop: 'static', keyboard: false});

                    if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
                    {
                        $('#dlist_form_confirm1 #fill_hod_name').html(jsonvalue.form_details.hod_name);
                        $('#dlist_form_confirm1 #fill_hod_email').html(jsonvalue.form_details.hod_email);
                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                        $('#dlist_form_confirm1 #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);


                    } else if (jsonvalue.form_details.min == "External Affairs")
                    {
                        $('#dlist_form_confirm1 #fill_hod_name').html("Dr S JANAKIRAMAN");
                        $('#dlist_form_confirm1 #fill_hod_email').html("jsegit@mea.gov.in");
                        $('#dlist_form_confirm1 #fill_hod_mobile').html("+919384664224");
                    } else {
                        $('#dlist_form_confirm1 #fill_hod_name').html(jsonvalue.form_details.hod_name);
                        $('#dlist_form_confirm1 #fill_hod_email').html(jsonvalue.form_details.hod_email);
                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                        $('#dlist_form_confirm1 #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                    }
                    $('#dlist_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                } else {
                    $('#dlist_bulkform #dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#dlist_bulkform #tnc_error').html("Please agree to Terms and Conditions");
                    $('#dlist_bulkform #dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $("#dlist_bulkform #tnc").removeAttr('disabled');
                }
            }
        }, error: function ()
        {
            $('#tab1').show();
        }
    });
});

$('#dlist_bulkform .edit').click(function () {
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
        $('#dlist_preview_tab #central_div').hide();
        $('#dlist_preview_tab #state_div').hide();
        $('#dlist_preview_tab #other_div').show();
        $('#dlist_preview_tab #other_text_div').addClass("display-hide");
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
            $('#dlist_preview_tab #other_text_div').removeClass("display-hide");

        }
    } else {
//            $('#dlist_preview_tab #central_div').hide();
//            $('#dlist_preview_tab #state_div').hide();
//            $('#dlist_preview_tab #other_div').hide();
    }
    $('#dlist_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
    $('#dlist_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
    $('#dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
    $('#dlist_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');
    $('#dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
    //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
    //$('#bulk_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
    $('#dlist_preview_tab #REditPreview #hod_email').removeAttr('disabled');
    //$(this).addClass('display-hide');
});

$('#dlist_form_confirm1 #confirmYes').click(function () {
    //alert("Inside Dlist BULK Final submit");
    $('#dlist_bulkform').submit();
    $('#stack777').modal('toggle');
});
$('#dlist_bulkform').submit(function (e) {
    e.preventDefault();
    $("#dlist_bulkform :disabled").removeAttr('disabled');
    $('#dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
    $('#dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');
    $('#dlist_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled');
    $('#dlist_preview_tab #user_email').removeAttr('disabled');
    var data = JSON.stringify($('#dlist_bulkform').serializeObject());
    $('#dlist_preview_tab #user_email').prop('disabled', 'true');

    var CSRFRandom = $("#CSRFRandom").val();

    $.ajax({
        type: "POST",
        url: "dLIST_Bulktab",
        data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom, type: "dl_bulk"},
        datatype: JSON,
        success: function (data)
        {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            var error_flag = true;
            if (jsonvalue.error.list_name_error !== null && jsonvalue.error.list_name_error !== "" && jsonvalue.error.list_name_error !== undefined)
            {
                $('#dlist_bulkform #list_name_err').html(jsonvalue.error.list_name_error);
                $('#dlist_bulkform #list_name_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #list_name_err').html("");
            }
            if (jsonvalue.error.list_desc_error !== null && jsonvalue.error.list_desc_error !== "" && jsonvalue.error.list_desc_error !== undefined)
            {
                $('#dlist_bulkform #description_list_err').html(jsonvalue.error.list_desc_error);
                $('#dlist_bulkform #description_list_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #description_list_err').html("");
            }
            if (jsonvalue.error.list_mod_error !== null && jsonvalue.error.list_mod_error !== "" && jsonvalue.error.list_mod_error !== undefined)
            {
                $('#dlist_bulkform #list_mod_err').html(jsonvalue.error.list_mod_error);
                $('#dlist_bulkform #list_mod_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #list_mod_err').html("");
            }
            if (jsonvalue.error.list_allowed_error !== null && jsonvalue.error.list_allowed_error !== "" && jsonvalue.error.list_allowed_error !== undefined)
            {
                $('#dlist_bulkform #allowed_member_err').html(jsonvalue.error.list_allowed_error);
                $('#dlist_bulkform #allowed_member_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #allowed_member_err').html("");
            }

            if (jsonvalue.error.list_temp_error !== null && jsonvalue.error.list_temp_error !== "" && jsonvalue.error.list_temp_error !== undefined)
            {
                $('#dlist_bulkform #list_temp_err').html(jsonvalue.error.list_temp_error);
                $('#dlist_bulkform #list_temp_err').focus();

                error_flag = false;
            } else {
                $('#dlist_bulkform #list_temp_err').html("");
            }
            if (jsonvalue.error.list_validdate_error !== null && jsonvalue.error.list_validdate_error !== "" && jsonvalue.error.list_validdate_error !== undefined)
            {
                $('#dlist_bulkform #validity_date_err').html(jsonvalue.error.list_validdate_error);
                $('#dlist_bulkform #validity_date_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #validity_date_err').html("");
            }

            if (jsonvalue.error.list_nonnicnet_error !== null && jsonvalue.error.list_nonnicnet_error !== "" && jsonvalue.error.list_nonnicnet_error !== undefined)
            {
                $('#dlist_bulkform #non_nicnet_err').html(jsonvalue.error.list_nonnicnet_error);
                $('#dlist_bulkform #non_nicnet_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #non_nicnet_err').html("");
            }

            if (jsonvalue.error.mod_name_error !== null && jsonvalue.error.mod_name_error !== "" && jsonvalue.error.mod_name_error !== undefined)
            {
                $('#dlist_bulkform #tauth_off_name_err').html(jsonvalue.error.mod_name_error);
                $('#dlist_bulkform #tauth_off_name_err').focus();

                error_flag = false;
            } else {
                $('#dlist_bulkform #tauth_off_name_err').html("");
            }

            if (jsonvalue.error.mod_email_error !== null && jsonvalue.error.mod_email_error !== "" && jsonvalue.error.mod_email_error !== undefined)
            {
                $('#dlist_bulkform #tauth_email_err').html(jsonvalue.error.mod_email_error);
                $('#dlist_bulkform #tauth_email_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #tauth_email_err').html("");
            }

            if (jsonvalue.error.mod_mobile_error !== null && jsonvalue.error.mod_mobile_error !== "" && jsonvalue.error.mod_mobile_error !== undefined)
            {
                $('#dlist_bulkform #tmobile_err').html(jsonvalue.error.mod_mobile_error);
                $('#dlist_bulkform #tmobile_err').focus();
                error_flag = false;
            } else {
                $('#dlist_bulkform #tmobile_err').html("");
            }

            if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
            {
                $('#dlist_bulkform #useremployment_error').focus();
                $('#dlist_bulkform #useremployment_error').html(jsonvalue.error.employment_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #useremployment_error').html("");
            }
            if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
            {
                $('#dlist_bulkform #minerror').focus();
                $('#dlist_bulkform #minerror').html(jsonvalue.error.ministry_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #minerror').html("");
            }
            if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
            {
                $('#dlist_bulkform #deperror').focus();
                $('#dlist_bulkform #deperror').html(jsonvalue.error.dept_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #deperror').html("");
            }
            if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
            {
                $('#dlist_bulkform #other_dept').focus();
                $('#dlist_bulkform #other_dep_error').html(jsonvalue.error.dept_other_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #other_dep_error').html("");
            }
            if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
            {
                $('#dlist_bulkform #smierror').focus();
                $('#dlist_bulkform #smierror').html(jsonvalue.error.state_dept_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #smierror').html("");
            }
            if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
            {
                $('#dlist_bulkform #state_error').focus();
                $('#dlist_bulkform #state_error').html(jsonvalue.error.state_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #state_error').html("");
            }
            if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
            {
                $('#dlist_bulkform #org_error').focus();
                $('#dlist_bulkform #org_error').html(jsonvalue.error.org_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #org_error').html("");
            }
            if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
            {
                $('#dlist_bulkform #ca_design').prop("readonly", false);
                $('#dlist_bulkform #ca_design').focus();
                $('#dlist_bulkform #cadesign_error').html(jsonvalue.error.ca_desg_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #cadesign_error').html("");
            }
            if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
            {
                $('#dlist_bulkform #hod_name').prop("readonly", false);
                $('#dlist_bulkform #hod_name').focus();
                $('#dlist_bulkform #hodname_error').html(jsonvalue.error.hod_name_error);
                error_flag = false;
            } else {
                $('#hodname_error').html("");
            }
            if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
            {
                $('#dlist_bulkform #hod_mobile').focus();
                $('#dlist_bulkform #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #hodmobile_error').html("");
            }
            if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
            {
                $('#dlist_bulkform #hodemail_error').html(jsonvalue.error.hod_email_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #hodemail_error').html("");
            }
            if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
            {
                $('#dlist_bulkform #hod_tel').prop("readonly", false);
                $('#dlist_bulkform #hod_tel').focus();
                $('#dlist_bulkform #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                error_flag = false;
            } else {
                $('#dlist_bulkform #hodtel_error').html("");
            }
            if (error_flag) {
                $('#large1').modal('toggle');
                $.ajax({
                    type: "POST",
                    url: "esign",
                    data: {formtype: 'bulkdistributionlist'},
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
