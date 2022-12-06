/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var err = "";
    var span_val = $(".control-label").children("span").html();
    if (span_val == "*") {
        $(".control-label").children("span").css('font-size', '25px');
    }

    $(document).on('blur', '.act_email1', function () {
        console.log("gggggggg")
        var email_regex = /^[a-zA-Z0-9._%+-]+$/i;
        var email = $(this).val();
        var field_form = $(this).closest('form').attr('id');

        $.ajax({
            type: "POST",
            url: "checkPrefmail",
            data: {data: email},
            datatype: JSON,
            success: function (data) {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                $('#' + field_form + " #act_email1_err").html(jsonvalue.error.errorMsg1)
                if (jsonvalue.error.errorMsg1.indexOf("is available for activation") > -1) {
                    $('#' + field_form + " #act_email1_err").css("color", "green");
                } else {
                    $('#' + field_form + " #act_email1_err").css("color", "red");
                }
            },
            error: function () {
                console.log('error');
                return false;
            }
        });
    });


    $(document).on('blur', '.deact_email1', function () {
        console.log("gggggggg")
        var email_regex = /^[a-zA-Z0-9._%+-]+$/i;
        var email = $(this).val();
        var field_form = $(this).closest('form').attr('id');

        $.ajax({
            type: "POST",
            url: "checkPrefmailForDeactivation",
            data: {data: email},
            datatype: JSON,
            success: function (data) {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                $('#' + field_form + " #deact_email1_err").html(jsonvalue.error.errorMsg1)
                if (jsonvalue.error.errorMsg1.indexOf("is available for deactivation") > -1) {
                    $('#' + field_form + " #deact_email1_err").css("color", "green");
                } else {
                    $('#' + field_form + " #deact_email1_err").css("color", "red");
                }
            },
            error: function () {
                console.log('error');
                return false;
            }
        });
    });

    $(document).on('find keyup', '.page-container-bg-solid input,textarea,select', function (e) {
        var emailregex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        var flag = false;
        var field_name = $(this).attr("name");
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var fVal = $(this).val();
        var fPlace = $(this).attr("placeholder");
        var Serr = $(this).next().find('span').prop('id');
        if (field_name === "hod_email") {

            if (fVal.match(emailregex) && !flag) {
                flag = false;
                //$('.' + field_name).remove();
                $('#' + field_form + " #" + Serr).html("");
                if (fVal === $('#user_email').val()) {
                    $('#' + field_form + " #" + Serr).html("Reporting/Nodal/Forwarding Officer email can not be same as user email address");
                    $('#under_sec_details').addClass("display-hide")
                    $('#under_sec_email').val("");
                    $('#under_sec_mobile').val("");
                    $('#under_sec_name').val("");
                    $('#under_sec_tel').val("");
                    $('#under_sec_desig').val("");
                    $('#hod_mobile').val("");
                    $('#hod_name').val("");
                    $('#hod_tel').val("");
                    $('#ca_design').val("");
                } else {
                    $.ajax({
                        type: "POST",
                        url: "getHODdetails",
                        data: {email: fVal},
                        datatype: JSON,
                        success: function (data) {
                            var myJSON = JSON.stringify(data);
                            var jsonvalue = JSON.parse(myJSON);
                            console.log(jsonvalue.hodDetails.bo)
                            if (jsonvalue.hodDetails.bo == "no bo")
                            {
                                console.log("inside no bo")
                                $('#' + field_form + ' #hod_mobile').val("");
                                $('#' + field_form + ' #hod_name').val("");
                                $('#' + field_form + ' #hod_tel').val("");
                                $('#' + field_form + ' #ca_design').val("");
                                $('#' + field_form + ' #hod_mobile').attr("readonly", false);
                                $('#' + field_form + ' #hod_name').attr("readonly", false);
                                $('#' + field_form + ' #hod_tel').attr("readonly", false);
                                $('#' + field_form + ' #ca_design').attr("readonly", false);
                                $('#' + field_form + ' #under_sec_details').removeClass("display-hide")
                                $('#' + field_form + ' #USEditPreview').removeClass("display-hide")
                                $('#' + field_form + ' #USEditPreview').find('input, textarea, button, select').removeAttr('disabled');
                                $('#' + field_form + ' #REditPreview').find('input, textarea, button, select').removeAttr('disabled');
                            } else {


                                console.log("inside else no bo")
                                $('#' + field_form + ' #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');
                                $('#' + field_form + ' #REditPreview #hod_email').removeAttr('disabled');
                                $('#' + field_form + ' #under_sec_details').addClass("display-hide")
                                $('#' + field_form + ' #USEditPreview').addClass("display-hide")
                                $('#' + field_form + ' #under_sec_email').val("");
                                $('#' + field_form + ' #under_sec_mobile').val("");
                                $('#' + field_form + ' #under_sec_name').val("");
                                $('#' + field_form + ' #under_sec_tel').val("");
                                $('#' + field_form + ' #under_sec_desig').val("");
                                if (jsonvalue.hodDetails.mobile !== "") {

                                    var startMobile = jsonvalue.hodDetails.mobile.substring(0, 3);
                                    var endMobile = jsonvalue.hodDetails.mobile.substring(10, 13);
                                    $('#' + field_form + ' #hod_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                                    $('#' + field_form + ' #hod_mobile').attr("readonly", true);
                                } else {
                                    $('#' + field_form + ' #hod_mobile').val("");
                                    $('#' + field_form + ' #hod_mobile').attr("readonly", false);
                                }
                                if (jsonvalue.hodDetails.cn !== "") {
                                    $('#' + field_form + ' #hod_name').val(jsonvalue.hodDetails.cn);
                                    $('#' + field_form + ' #hod_name').attr("readonly", true);
                                } else {
                                    $('#' + field_form + ' #hod_name').val("");
                                    $('#' + field_form + ' #hod_name').attr("readonly", false);
                                }
                                if (jsonvalue.hodDetails.ophone !== "") {
                                    $('#' + field_form + ' #hod_tel').val(jsonvalue.hodDetails.ophone);
                                    $('#' + field_form + ' #hod_tel').attr("readonly", true);
                                } else {
                                    $('#' + field_form + ' #hod_tel').val("");
                                    $('#' + field_form + ' #hod_tel').attr("readonly", false);
                                }
                                if (jsonvalue.hodDetails.desig !== "") {
                                    $('#' + field_form + ' #ca_design').val(jsonvalue.hodDetails.desig);
                                    $('#' + field_form + ' #ca_design').attr("readonly", true);
                                } else {
                                    $('#' + field_form + ' #ca_design').val("");
                                    $('#' + field_form + ' #ca_design').attr("readonly", false);
                                }


                            }

                        },
                        error: function () {
                            console.log('error');
                        }
                    });
                }

            } else {
                $('#' + field_form + " #" + Serr).html(fPlace);
                //$(this).parent().append('<span class="' + field_name + '" style="color:#f00;">' + fPlace + '</span>');
                return false;
            }
        }



        if (field_name === "under_sec_email") {

            if (fVal.match(emailregex) && !flag) {
                flag = false;
                //$('.' + field_name).remove();
                $('#' + field_form + " #" + Serr).html("");
                if (fVal === $('#hod_email').val()) {
                    $('#' + field_form + " #" + Serr).html("Under Secretary Officer email can not be same as user email address");
                    $('#under_sec_mobile').val("");
                    $('#under_sec_name').val("");
                    $('#under_sec_tel').val("");
                    $('#under_sec_desig').val("");
                } else {
                    $.ajax({
                        type: "POST",
                        url: "getHODdetails",
                        data: {email: fVal},
                        datatype: JSON,
                        success: function (data) {
                            var myJSON = JSON.stringify(data);
                            var jsonvalue = JSON.parse(myJSON);
//                     /console.log(jsonvalue.hodDetails)
                            if (jsonvalue.hodDetails.bo == "no bo")
                            {

                                $('#under_sec_email_error').html("Under secretary should be government employee");
                                $('#under_sec_name').prop("readonly", false);
                                $('#under_sec_desig').prop("readonly", false);
                                $('#under_sec_mobile').prop("readonly", false);
                                $('#under_sec_tel').prop("readonly", false);
                                $('#under_sec_mobile').val("");
                                $('#under_sec_name').val("");
                                $('#under_sec_tel').val("");
                                $('#under_sec_desig').val("");
                            } else {

                                if (jsonvalue.hodDetails.mobile !== "") {
                                    var startMobile = jsonvalue.hodDetails.mobile.substring(0, 3);
                                    var endMobile = jsonvalue.hodDetails.mobile.substring(10, 13);
                                    $('#under_sec_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                                    $('#under_sec_mobile').prop("readonly", true);
                                } else {
                                    $('#under_sec_mobile').val("");
                                    $('#under_sec_mobile').prop("readonly", false);
                                }
                                if (jsonvalue.hodDetails.cn !== "") {
                                    $('#under_sec_name').val(jsonvalue.hodDetails.cn);
                                    $('#under_sec_name').prop("readonly", true);
                                } else {
                                    $('#under_sec_name').val("");
                                    $('#under_sec_name').prop("readonly", false);
                                }
                                if (jsonvalue.hodDetails.ophone !== "") {
                                    $('#under_sec_tel').val(jsonvalue.hodDetails.ophone);
                                    $('#under_sec_tel').prop("readonly", true);
                                } else {
                                    $('#under_sec_tel').val("");
                                    $('#under_sec_tel').prop("readonly", false);
                                }
                                if (jsonvalue.hodDetails.desig !== "") {
                                    $('#under_sec_desig').val(jsonvalue.hodDetails.desig);
                                    $('#under_sec_desig').prop("readonly", true);
                                } else {
                                    $('#under_sec_desig').val("");
                                    $('#under_sec_desig').prop("readonly", false);
                                }
                            }

                        },
                        error: function () {
                            console.log('error');
                        }
                    });
                }

            } else {
                $('#' + field_form + " #" + Serr).html(fPlace);
                return false;
            }
        }

    });


    $(".sms_preview_checks [type='checkbox']").click(function () {
        var field_name = $(this).attr("name");
        var field_form = $(this).closest('form').attr('id');
        var chk_counter = $('.sms_preview_checks input:checkbox:checked').length;

        if (chk_counter < 1) {
            if (field_name === "sms_service") {
                if ($(this).is(":checked"))
                {
                    $(".sms_serv_err").addClass('d-none');
                    $('#' + field_form + ' #sms_serv_err').html("");
                } else {
                    $(".sms_serv_err").removeClass('d-none');
                    $('#' + field_form + ' #sms_serv_err').html("Please check SMS service");
                }
            }
        } else {
            $(".sms_serv_err").addClass('d-none');
            $('#' + field_form + ' #sms_serv_err').html("");
        }
    });
    $(document).on('find blur', '.page-container-bg-solid input,textarea,select', function () {

        var flag = false;
        var field_name = $(this).attr("name");
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var fVal = $(this).val();
        var fPlace = $(this).attr("placeholder");
        var Serr = $(this).next().find('span').prop('id');
//        if (field_name === "sms_service") {
//            if ($(this).is(":checked"))
//            {
//                $('#' + field_form + ' #sms_serv_err').html("");
//            } else {
//                $('#' + field_form + ' #sms_serv_err').html("Please check SMS service");
//            }
//        }
        if (field_name === "chk_box") {
            if ($(this).is(":checked"))
            {
                $('#' + field_form + ' #request_err').html("");
            } else {
                $('#' + field_form + ' input[name=request_' + fVal + ']').val('');
                $('#' + field_form + ' #request_err').html("");
            }
        }


        var emailregex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        var trafficregex = /^[0-9]{4,9}$/;
        if (field_name === "domestic_traf" || field_name === "inter_traf") {
            if (fVal.match(trafficregex)) {
                var a = 1 * fVal;
                if (a === 0 || a === 'NaN') {

                    flag = false;
                    err = true;
                    if (field_name === "domestic_traf") {
                        $('#' + field_form + ' #domestic_traf_yes').text('');
                    } else {
                        $('#' + field_form + ' #inter_traf_yes').text('');
                    }
                } else {
                    flag = false;
                    $("#" + Serr).html('');
                    $('.' + field_name).remove();
                    var checked_value = $('input[name=sender]:checked').val();
                    if (field_name === "domestic_traf" && checked_value === 'Yes') {
                        var msg1 = 34 / 1000 * fVal;
                        msg1 = msg1.toFixed(2);
                        $('#' + field_form + ' #domestic_traf_yes').text('Approx Cost: INR ' + msg1);
                        $('#' + field_form + " #domestic_traf_yes").css("color", "blue");
                    } else if (field_name === "domestic_traf" && checked_value === 'No') {
                        var msg1 = 72 / 1000 * fVal;
                        msg1 = msg1.toFixed(2);
                        $('#' + field_form + ' #domestic_traf_yes').text('Approx Cost: INR ' + msg1);
                        $('#' + field_form + " #domestic_traf_yes").css("color", "blue");
                    } else if (field_name === "inter_traf") {
                        $('#' + field_form + ' #inter_traf_yes').text('SMS rates will vary as per country');
                        $('#' + field_form + " #inter_traf_yes").css("color", "blue");
                    }
                }
            } else {
//                        $(this).parent().append('<span class="' + field_name + '" style="color:#f00;">' + fPlace + '</span>');
//                        $("#" + Serr).html('');
//                        flag = false;
//                        err = true;
                if (field_name === "domestic_traf") {
                    $('#' + field_form + ' #domestic_traf_yes').text(fPlace);
                    $('#' + field_form + " #domestic_traf_yes").css("color", "red");
                } else if (field_name === "inter_traf" && fVal !== '') {
                    $('#' + field_form + ' #inter_traf_yes').text(fPlace);
                    $('#' + field_form + " #inter_traf_yes").css("color", "red");
                }
            }
        }



    });
    $('.page-container-bg-solid').find('select').change(function () {
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');
        if (field_name === 'server_loc') {
            var server_loc = $(this).val();
            if (server_loc === 'Other')
            {
                $('#' + field_form + ' #server_other').show();
                $('#' + field_form + ' #server_other').removeClass('display-hide');
                $('#' + field_form + ' #server_loc_txt').val('');
            } else
            {
                $('#' + field_form + ' #server_other').hide();
                $('#' + field_form + ' #server_other').addClass('display-hide');
                $('#' + field_form + ' #server_loc_txt').val('');
            }
        }
    });
    $('.page-container-bg-solid').find('input[name="cert_file1"]').change(function (e) {
        e.preventDefault();
        var field_form = $(this).closest('form').attr('id');
        var file = $('#' + field_form + ' input[name="cert_file1"]').get(0).files[0];
        console.log(file)
        var fileType = file.type;
        var fileSize = file.size;
        var match = ["application/pdf"]; // for multiple take comma separated values
        if (fileType === match[0]) {
            if (fileSize <= 1000000) {
                $('#file_err').text("");
                var formData = new FormData();
                formData.append('cert_file', file);
                $.ajax({
                    url: 'checkPDF',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);
                        if (jsonvalue.error !== '' && jsonvalue.error !== null) {
                            $('#file_err').text(jsonvalue.error);
                            $('#cert').val('false');
                        } else {
                            $('#cert').val('true');
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log('error');
                    }
                });
            } else {
                $('#file_err').text("PDF file shoulb be less than 1mb");
            }
        } else {
            $('#file_err').text("Upload only PDF file");
        }

    });

    $('#webcast_file').change(function (e) {

        e.preventDefault();
        for (var i = 0; i < $('input[name="webcast_file"]').get(0).files.length; ++i) {
            var file = $('input[name="webcast_file"]').get(0).files[i];
            var fileType = file.type;
            var fileSize = file.size;
            var flag = true;

            var match = ["application/pdf", "image/jpeg", "image/png"]; // for multiple take comma separated values            
            if (fileType === match[0] || fileType === match[1] || fileType === match[2]) {
                if (fileSize <= 5000000) {

                } else {
                    flag = false;
                    $('#file_err').text("PDF/JPG file shoulb be less than 5mb. Filename: " + file.name);
                    break;
                }
            } else {
                flag = false;
                $('#file_err').text("Upload only PDF/JPG file. Filename: " + file.name);
                break;
            }
        }

        if (flag) {
            $('#file_err').text("");
            var formData = new FormData();
            for (var i = 0; i < $('input[name="webcast_file"]').get(0).files.length; ++i) {
                formData.append('userfiles', $('input[name="webcast_file"]').get(0).files[i]);
                console.log($('input[name="webcast_file"]').get(0).files[i].name);
            }
            for (var pair of formData.entries()) {
                console.log(pair[0] + ' - ' + pair[1]);
            }
            console.log(formData)

            $.ajax({
                url: 'checkWebcastFile',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (data) {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    if (jsonvalue.error !== '' && jsonvalue.error !== null) {
                        $('#file_err').text(jsonvalue.error);
                        $('#cert').val('false');
                    } else {
                        $('#cert').val('true');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('error');
                }
            });
        }
    });

    $('#cert_file').change(function (e) {

        e.preventDefault();
        var file = $('input[name="cert_file"]').get(0).files[0];
        var fileType = file.type;
        var fileSize = file.size;
        var match = ["application/pdf"]; // for multiple take comma separated values

        if (fileType === match[0]) {
            if (fileSize <= 1000000) {
                $('#file_err').text("");
                var formData = new FormData();
                formData.append('cert_file', file);
                $.ajax({
                    url: 'checkPDF',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);
                        if (jsonvalue.error !== '' && jsonvalue.error !== null) {
                            $('#file_err').text(jsonvalue.error);
                            $('#cert').val('false');
                        } else {
                            $('#cert').val('true');
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log('error');
                    }
                });
            } else {
                $('#file_err').text("PDF file shoulb be less than 1mb");
            }
        } else {
            $('#file_err').text("Upload only PDF file");
        }
    });
    $('#cert_file1').change(function (e) {

        e.preventDefault();
        var file = $('input[name="cert_file1"]').get(0).files[0];
        var fileType = file.type;
        var fileSize = file.size;
        var match = ["application/pdf"]; // for multiple take comma separated values

        if (fileType === match[0]) {
            if (fileSize <= 1000000) {
                $('#file_err').text("");
                var formData = new FormData();
                formData.append('cert_file', file);
                $.ajax({
                    url: 'checkPDF',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);
                        if (jsonvalue.error !== '' && jsonvalue.error !== null) {
                            $('#file_err').text(jsonvalue.error);
                            $('#cert').val('false');
                        } else {
                            $('#cert').val('true');
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log('error');
                    }
                });
            } else {
                $('#file_err').text("PDF file shoulb be less than 1mb");
            }
        } else {
            $('#file_err').text("Upload only PDF file");
        }
    });
    $('#user_file').change(function (e) {
        e.preventDefault();
        var file = $('input[name="user_file"]').get(0).files[0];
        var fileType = file.type;
        var fileSize = file.size
        var match = ["text/csv", "application/vnd.ms-excel"]; // for multiple take comma separated values
        if (fileType === match[0] || fileType === match[1]) {
            if (fileSize <= 1000000) {
                $('#file_err').text("");
                var formData = new FormData();
                formData.append('cert_file', file);

                $.ajax({
                    url: 'checkCSV',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);
                        if (jsonvalue.error !== '' && jsonvalue.error !== null) {
                            $('#file_err').text(jsonvalue.error);
                            $('#cert').val('false');
                        } else {
                            $('#cert').val('true');
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log('error');
                    }
                });
            } else {
                $('#file_err').text("File should be less than 1mb");
            }
        } else {
            $('#file_err').text("Upload file in .csv format");
        }
    });
    $('#user_file1').change(function (e) {

        e.preventDefault();
        var file = $('input[name="user_file1"]').get(0).files[0];
        var fileType = file.type;
        var fileSize = file.size
        var match = ["text/csv", "application/vnd.ms-excel"]; // for multiple take comma separated values
        if (fileType === match[0] || fileType === match[1]) {

            console.log("matched")
            if (fileSize <= 1000000) {
                $('#file_err1').text("");
                var formData = new FormData();
                formData.append('cert_file', file);
                $.ajax({
                    url: 'checkCSV',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);
                        console.log("jsonvalue.error" + jsonvalue.error)
                        if (jsonvalue.error !== '' && jsonvalue.error !== null) {
                            $('#file_err').text(jsonvalue.error);
                            $('#cert1').val('false');
                        } else {

                            $('#cert1').val('true');
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log('error');
                    }
                });
            } else {
                $('#file_err').text("File should be less than 1mb");
            }
        } else {

            $('#file_err').text("Upload file in .csv format");
        }
    });
    $('#user_file_prvw').change(function (e) {
        console.log("ddsfdsf")
        e.preventDefault();
        var file = $('input[name="user_file_prvw"]').get(0).files[0];
        var fileType = file.type;
        var fileSize = file.size
        console.log('type:: ' + fileType)
        var match = ["text/csv", "application/vnd.ms-excel"]; // for multiple take comma separated values
        if (fileType === match[0] || fileType === match[1]) {
            if (fileSize <= 1000000) {
                $('#file_err1').text("");
                var formData = new FormData();
                formData.append('cert_file', file);
                $.ajax({
                    url: 'checkCSV',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);
                        console.log(jsonvalue.error)
                        if (jsonvalue.error !== '' && jsonvalue.error !== null) {

                            $('#file_err1').text(jsonvalue.error);
                            $('#dns_form2 #cert').val('false');
                            $('#dns_preview #cert').val('false');
                        } else {
                            $('#dns_form2 #cert').val('true');
                            $('#dns_preview #cert').val('true');
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log('error');
                    }
                });
            } else {
                $('#file_err1').text("File should be less than 1mb");
            }
        } else {
            $('#file_err1').text("Upload file in .csv format");
            $('#dns_form2 #cert').val('false');
            $('#dns_preview #cert').val('false');
        }
    });
    $('#form1').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#form1').serializeObject());
        //$('#profile1').attr("disabled",false)
        $.ajax({
            type: "POST",
            url: "PersonalProfile",
            data: {data: data},
            datatype: JSON,
            success: function (data) {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var flag = true;
                //$('#profile1').attr("disabled",true)
                if (jsonvalue.error.name_error !== null && jsonvalue.error.name_error !== "" && jsonvalue.error.name_error !== undefined)
                {
                    //  $('#user_name').focus();
                    $('#username_error').html(jsonvalue.error.name_error);
                    flag = false;
                } else {
                    $('#username_error').html("");
                }

                if (jsonvalue.error.city_error !== null && jsonvalue.error.city_error !== "" && jsonvalue.error.city_error !== undefined)
                {
                    //  $('#user_city').focus();
                    $('#usercity_error').html(jsonvalue.error.city_error);
                    flag = false;
                } else {

                    $('#usercity_error').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    //   $('#user_state').focus();
                    $('#userstate_error').html(jsonvalue.error.state_error);
                    flag = false;
                } else {

                    $('#userstate_error').html("");
                }
                if (jsonvalue.error.mobile_error !== null && jsonvalue.error.mobile_error !== "" && jsonvalue.error.mobile_error !== undefined)
                {
                    //  $('#user_mobile').focus();
                    $('#usermobile_error').html(jsonvalue.error.mobile_error);
                    flag = false;
                } else {

                    $('#usermobile_error').html("");
                }
                if (jsonvalue.error.ophone_error !== null && jsonvalue.error.ophone_error !== "" && jsonvalue.error.ophone_error !== undefined)
                {
                    // $('#user_ophone').focus();
                    $('#userophone_error').html(jsonvalue.error.ophone_error);
                    flag = false;
                } else {
                    $('#userophone_error').html("");
                }
                if (jsonvalue.error.rphone_error !== null && jsonvalue.error.rphone_error !== "" && jsonvalue.error.rphone_error !== undefined)
                {
                    // $('#user_rphone').focus();
                    $('#userrphone_error').html(jsonvalue.error.rphone_error);
                    flag = false;
                } else {
                    $('#userrphone_error').html("");
                }
                if (jsonvalue.error.emp_code_error !== null && jsonvalue.error.emp_code_error !== "" && jsonvalue.error.emp_code_error !== undefined)
                {
                    // $('#user_empcode').focus();
                    $('#userempcode_error').html(jsonvalue.error.emp_code_error);
                    flag = false;
                } else {
                    $('#userempcode_error').html("");
                }
                if (jsonvalue.error.add_error !== null && jsonvalue.error.add_error !== "" && jsonvalue.error.add_error !== undefined)
                {
                    // $('#user_address').focus();
                    $('#useraddress_error').html(jsonvalue.error.add_error);
                    flag = false;
                } else {
                    $('#useraddress_error').html("");
                }
                if (jsonvalue.error.email_error !== null && jsonvalue.error.email_error !== "" && jsonvalue.error.email_error !== undefined)
                {
                    //  $('#user_email').focus();
                    $('#useremail_error').html(jsonvalue.error.email_error);
                    flag = false;
                } else {
                    $('#useremail_error').html("");
                }
                if (jsonvalue.error.desg_error !== null && jsonvalue.error.desg_error !== "" && jsonvalue.error.desg_error !== undefined)
                {
                    //  $('#user_design').focus();
                    $('#userdesign_error').html(jsonvalue.error.desg_error);
                    flag = false;
                } else {

                    $('#userdesign_error').html("");
                }
                if (jsonvalue.error.pin_error !== null && jsonvalue.error.pin_error !== "" && jsonvalue.error.pin_error !== undefined)
                {
                    //  $('#user_pincode').focus();
                    $('#userpincode_error').html(jsonvalue.error.pin_error);
                    flag = false;
                } else {
                    $('#userpincode_error').html("");
                }

                if (jsonvalue.error.bocheck !== null && jsonvalue.error.bocheck !== "" && jsonvalue.error.bocheck !== undefined)
                {
                    //  $('#user_pincode').focus();
                    $('#user_alt_address_err').html(jsonvalue.error.bocheck);
                    flag = false;
                } else {
                    $('#user_alt_address_err').html("");
                }
                if (!flag) {
                    console.log("jzxghjdggasdhgasdgashgdasokkkkkkkkkkkkkk");
                    $('.nav-tabs a[href="#tab_1_1"]').tab('show');
                    $('#tab_1_1').show();
                    $('#tab_1_2').hide();
                    $('#tab2_nav').removeClass('active show');
                    $('#tab1_nav').addClass('active');
                } else {
                    console.log("notttttttttttjzxghjdggasdhgasdgashgdasokkkkkkkkkkkkkk");
                    $('.nav-tabs a[href="#tab_1_2"]').tab('show');
                    $('#tab_1_1').hide();
                    $('#tab_1_2').show();
                    $('#tab1_nav').removeClass('active show');
                    $('#tab2_nav').addClass('active');
                }
            },
            error: function () {
                console.log('error');
                return false;
            }
        });
    });
    $('#form2').submit(function (e) {
        e.preventDefault();
        $('#form2 #hod_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#form2').serializeObject());
        var isIsNicEmployee = $('#isIsNicEmployee').val();
        // $('#profile2').attr("disabled",false)
        if (isIsNicEmployee === 'true')
        {
            $('#form2 #hod_email').prop('disabled', false);
        }
        var email = $('#form1 #user_email').val();
        var mobile = $('#form1 #user_mobile').val();
        var dataObj = {data: data, check_validate: 'true'};
        // var dataObj = {userValues: {email: email, mobile: mobile}, data: data, check_validate: 'true'};

        //var data1 = JSON.stringify(dataObj);
        //console.log("data" + data1);


        $.ajax({
            type: "POST",
            url: "orgProfile",
            data: dataObj,
            datatype: JSON,
            success: function (data) {
                // $('#profile2').attr("disabled",true)
                console.log("data" + data)
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var flag = true;
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#useremployment_error').focus();
                    $('#useremployment_error').html(jsonvalue.error.employment_error);
                    flag = false;
                } else {

                    $('#useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#minerror').focus();
                    $('#minerror').html(jsonvalue.error.ministry_error);
                    flag = false;
                } else {

                    $('#minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#deperror').focus();
                    $('#deperror').html(jsonvalue.error.dept_error);
                    flag = false;
                } else {

                    $('#deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#other_dept').focus();
                    $('#other_dep_error').html(jsonvalue.error.dept_other_error);
                    flag = false;
                } else {
                    $('#other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#smierror').focus();
                    $('#smierror').html(jsonvalue.error.state_dept_error);
                    flag = false;
                } else {

                    $('#smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#state_error').focus();
                    $('#state_error').html(jsonvalue.error.state_error);
                    flag = false;
                } else {

                    $('#state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#org_error').focus();
                    $('#org_error').html(jsonvalue.error.org_error);
                    flag = false;
                } else {

                    $('#org_error').html("");
                }
                if (jsonvalue.error.ca_name_error !== null && jsonvalue.error.ca_name_error !== "" && jsonvalue.error.ca_name_error !== undefined)
                {

                    $('#ca_name').focus();
                    $('#caname_error').html(jsonvalue.error.ca_name_error);
                    flag = false;
                } else {

                    $('#caname_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#ca_design').prop("readonly", false)
                    $('#ca_design').focus();
                    $('#ca_desg_error').html(jsonvalue.error.ca_desg_error);
                    flag = false;
                } else {

                    $('#ca_desg_error').html("");
                }
                if (jsonvalue.error.ca_mobile_error !== null && jsonvalue.error.ca_mobile_error !== "" && jsonvalue.error.ca_mobile_error !== undefined)
                {
                    $('#ca_mobile').focus();
                    $('#camobile_error').html(jsonvalue.error.ca_mobile_error);
                    flag = false;
                } else {

                    $('#camobile_error').html("");
                }
                if (jsonvalue.error.ca_email_error !== null && jsonvalue.error.ca_email_error !== "" && jsonvalue.error.ca_email_error !== undefined)
                {
                    $('#ca_email').focus();
                    $('#caemail_error').html(jsonvalue.error.ca_email_error);
                    flag = false;
                } else {

                    $('#caemail_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {

                    $('#hod_name').prop("readonly", false)
                    $('#hod_name').focus();
                    $('#hodname_error').html(jsonvalue.error.hod_name_error);
                    flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#hod_mobile').focus();
                    $('#hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    flag = false;
                } else {

                    $('#hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#hodemail_error').focus();
                    $('#hodemail_error').html(jsonvalue.error.hod_email_error);
                    flag = false;
                } else {

                    $('#hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#hod_tel').prop("readonly", false)
                    $('#hod_tel').focus();
                    $('#hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    flag = false;
                } else {
                    $('#hodtel_error').html("");
                }




                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#under_sec_name').prop("readonly", false)
                    $('#under_sec_name').focus();
                    $('#under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    flag = false;
                } else {
                    $('#under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#under_sec_mobile').focus();
                    $('#under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    flag = false;
                } else {

                    $('#under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    //$('#hod_email').focus();
                    $('#under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    flag = false;
                } else {

                    $('#under_sec_email_error').html("");
                }
                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {
                    $('#under_sec_tel').prop("readonly", false)
                    $('#under_sec_tel').focus();
                    $('#under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    flag = false;
                } else {
                    $('#under_sec_tel_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#under_sec_desig').prop("readonly", false)
                    $('#under_sec_desig').focus();
                    $('#under_sec_desg_error').html(jsonvalue.error.under_sec_desg_error);
                    flag = false;
                } else {

                    $('#under_sec_desg_error').html("");
                }
                if (!flag) {

                    $('#tab_1_2').show();
                    $('#tab_1_1').hide();
                    $('#tab1_nav').removeClass('active');
                    $('#tab2_nav').addClass('active');
                } else
                {
                    // send otp on mobile
                    var email = $('#form1 #user_email').val();
                    var mobile = $('#form1 #user_mobile').val();
                    var otp_type = $('#otp_type').val();
                    var emailvalidate = $('#emailvalidate').val();
                    if (otp_type === 'mobileOnly' && emailvalidate === 'false')
                    {

                        var dataObj = {userValues: {email: email, mobile: mobile, isEmailValidated: emailvalidate}};
                        var data1 = JSON.stringify(dataObj);
                        // send otp on email
                        $.ajax({
                            type: "POST",
                            url: "OtpGenerate",
                            data: data1,
                            contentType: 'application/json; charset=utf-8',
                            //data: data1,
                            dataType: 'html'
                        })
                                .done(function (data) {
                                    $('#otp_modal').modal({backdrop: 'static', keyboard: false});
                                    $('#emailmod').show();
                                    $('#em_otp').text('Enter One Time Password received on Email: ' + email);
                                })
                                .fail(function () {
                                    console.log('error');
                                });
                    } else if (otp_type === 'emailOnly' && emailvalidate === 'false')
                    {

                        var dataObj = {userValues: {email: email, mobile: mobile, isEmailValidated: emailvalidate}};
                        var data1 = JSON.stringify(dataObj);
                        // send otp on mobile
                        $.ajax({
                            type: "POST",
                            url: "OtpGenerate",
                            data: data1,
                            contentType: 'application/json; charset=utf-8',
                            dataType: 'html'
                        })
                                .done(function (data) {

                                    $('#otp_modal').modal({backdrop: 'static', keyboard: false});
                                    $('#mobilemod').show();
                                    $('#mob_otp').text('Enter One Time Password received on Mobile: ' + mobile);
                                })
                                .fail(function () {
                                    console.log('error');
                                });
                    } else {

                        // both verified
//                        var dataObj = {userValues: {email: email, mobile: mobile, isEmailValidated: emailvalidate}};
//                        var data1 = JSON.stringify(dataObj);
                        var dataObj = {data: data, check_validate: 'false'};
                        $.ajax({
                            type: "POST",
                            url: "orgProfile",
                            data: dataObj,
                            dataType: 'html'
                        })
                                .done(function (data) {
                                    var myJSON = JSON.stringify(data);
                                    var jsonvalue = JSON.parse(myJSON);
                                    window.location.href = 'showUserData';
                                })
                                .fail(function () {
                                    console.log('error');
                                });
                    }


                }
            },
            error: function () {
                console.log('error');
                return false;
            }
        });
    });
    $('#verify_profile_otp').click(function (e) {

        e.preventDefault();
        var otp_mobile = $('#otp_sms_mobile1').val();
        var otp_email = $('#otp_sms_email1').val();
        var otp_type = $('#otp_type').val();
        var new_profile_email = $('#form1 #user_email').val();
        var new_profile_mobile = $('#form1 #user_mobile').val();
        var emailvalidate = $('#emailvalidate').val();
        var isIsNewUser = $('#isIsNewUser').val();
        var flg = false;
        if (otp_mobile === "" && otp_email !== "") {
            var regn = "^[0-9]{6}$";
            if (otp_email.match(regn)) {
                flg = true;
                $('#otp_error_modal').html("");
            } else {
                $('#otp_error_modal').html("Please enter correct OTP");
            }
        } else if (otp_mobile !== "" && otp_email === "") {
            var regn = "^[0-9]{6}$";
            if (otp_mobile.match(regn)) {
                flg = true;
                $('#otp_error_modal').html("");
            } else {
                $('#otp_error_modal').html("Please enter correct OTP");
            }
        } else {
            $('#otp_error_modal').html("Please Enter OTP");
        }

        if (flg) {
            var dataObj = {userValues: {email: new_profile_email, mobile: new_profile_mobile, emailOtp: otp_email, mobileOtp: otp_mobile, isEmailValidated: emailvalidate, isNewUser: isIsNewUser}};
            var data1 = JSON.stringify(dataObj);
            var CSRFRandom = $("#CSRFRandom").val();
            $.ajax({
                type: "POST",
                url: "verifyOtp",
                data: data1,
                datatype: JSON,
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    if (data.userValues.authenticated) {

                        $.ajax({
                            type: "POST",
                            url: "orgProfile",
                            data: {check_validate: 'false'},
                            dataType: 'html'
                        })
                                .done(function (data) {

                                    var myJSON = JSON.stringify(data);
                                    var jsonvalue = JSON.parse(myJSON);
                                    window.location.href = 'showUserData';
                                })
                                .fail(function () {
                                    console.log('error');
                                });
                        //window.location.href = "Forms";

                    } else {
                        console.log("failed")
                    }

                },
                error: function () {
                    console.log('error');
                }
            });
        }
    });
    $('#t2').click(function (e) {
        e.preventDefault();
        $('#form1').submit();
    });
    $('#t1').click(function (e) {
        e.preventDefault();
        $('#tab_1_1').show();
        $('#tab_1_2').hide();
        $('#tab2_nav').removeClass('show active');
    });
    $('#esign_submit').click(function () {
        var formtype = $('#esign_submit').val();
        var esign = $("#esign_div input[type=radio]:checked").val();
        console.log("efign: " + esign);
        if (esign === null) {
            $('#esign_error').text('Please select any process to proceed');
        } else if (esign === 'esign') {
            $('#esign_img3').show();
            $('#esign_submit').hide();
            $.ajax({
                type: "POST",
                url: "consent",
                data: {check: esign, formtype: formtype},
                success: function (check) {

                    window.location.href = "esignPDF";
                },
                error: function () {
                    console.log('error');
                }
            });
        } else {
            if (esign === "manual_upload")
            {
                var msg = '<p>Please note, if you are selecting a manual option while submitting your request, it will remain pending at your end as long as you do not upload duly sealed and signed scanned copy of the application form. For other options, your request will be automatically forwarded to the next level.</p>';
                // msg = +'<p>For other options, your request will be automatically forwarded to next level</p>';

                bootbox.confirm({
                    title: "Discard Campaign",
                    message: msg,
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
                            $('#esign_img3').show();
                            $('#esign_submit').hide();
                            console.log("here after loading")
                            $.ajax({
                                type: "POST",
                                url: "consent",
                                data: {check: esign, formtype: formtype},
                                success: function (check) {
                                    var myJSON = JSON.stringify(check);
                                    var jsonvalue = JSON.parse(myJSON);
                                    if (jsonvalue.consentreturn.checkreturn === 'unsuccess')
                                    {
                                        $('#failed').modal({backdrop: 'static', keyboard: false});
                                    } else {
                                        if (jsonvalue.consentreturn.checkreturn === 'online')
                                        {
                                            $('#reg_num').html(jsonvalue.consentreturn.Ref_string);
                                            $('#status').show();
                                            $('#done').modal({backdrop: 'static', keyboard: false});
                                        } else if (jsonvalue.consentreturn.checkreturn === 'manual_upload') {
                                            $('#reg_num1').html(jsonvalue.consentreturn.Ref_string);
                                            $('#status1').show();
                                            $('#generate').modal({backdrop: 'static', keyboard: false});
                                        }
                                    }
                                },
                                error: function () {
                                    console.log('error');
                                }
                            });
                        }
                    }
                });

            } else {

                $('#esign_img3').show();
                $('#esign_submit').hide();
                console.log("here after loading")
                $.ajax({
                    type: "POST",
                    url: "consent",
                    data: {check: esign, formtype: formtype},
                    success: function (check) {
                        var myJSON = JSON.stringify(check);
                        var jsonvalue = JSON.parse(myJSON);
                        if (jsonvalue.consentreturn.checkreturn === 'unsuccess')
                        {

                            // $('#status').show();
                            $('#failed').modal({backdrop: 'static', keyboard: false});
                        } else {
                            if (jsonvalue.consentreturn.checkreturn === 'online')
                            {
                                $('#reg_num').html(jsonvalue.consentreturn.Ref_string);
                                $('#status').show();
                                $('#done').modal({backdrop: 'static', keyboard: false});
                            } else if (jsonvalue.consentreturn.checkreturn === 'manual_upload') {
                                $('#reg_num1').html(jsonvalue.consentreturn.Ref_string);
                                $('#status1').show();
                                $('#generate').modal({backdrop: 'static', keyboard: false});
                            }
                        }
                    },
                    error: function () {
                        console.log('error');
                    }
                });
            }
        }
    });
    $('#esign_1').click(function () {

        var aadhaar = $('#aadhaar').val();
        if (aadhaar === null || aadhaar === '' || !aadhaar.match(/^[0-9]{12}$/)) {
            $('#aadhaar_error').text('Please enter 12 digit aadhaar Number');
            $('#esign_img1').hide();
            $('#esign_1').show();
        } else {




// start , code added by pr on 12thapr18

            var ref_num = "";
            var formtype = "";
            if ($("#ref_num") != null && $("#ref_num") != 'undefined')
            {
                ref_num = $("#ref_num").val();
            }

            if ($("#formtype") != null && $("#formtype") != 'undefined')
            {
                formtype = $("#formtype").val();
            }

// end , code added by pr on 12thapr18

            console.log(" ref_num value is " + ref_num + " formtype value is " + formtype);
            $('#esign_img1').show();
            $('#esign_1').hide();
            $.ajax({
                type: "POST",
                url: "aadhaarCheck",
                //data: {aadhaar: aadhaar},
                data: {aadhaar: aadhaar, ref_num: ref_num, formtype: formtype}, // above line commented this added by pr on 12thapr18
                success: function (aadhaar) {

                    var myJSON = JSON.stringify(aadhaar);
                    var jsonvalue = JSON.parse(myJSON);
                    if (jsonvalue.esignreturn.aadhaarreturn === 'otp') {
                        $('#aadhaar-div').addClass('display-hide');
                        $('#aadhaar-otp-div').removeClass('display-hide');
                        $('#esign_img2').hide();
                        $('#esign_2').show();
                        $('#otp-aadhaar_error').text('');
                        $('#otp-aadhaar').val('');
                        $('#otp-aadhaar').focus();
                    } else {
                        $('#esign_img1').hide();
                        $('#esign_1').show();
                        $('#aadhaar_error').text(jsonvalue.otpErrMsg);
                    }
                },
                error: function () {
                    console.log('error');
                }
            });
        }
    });
    $('#esign_2').click(function ()
    {
        console.log(" inside esign_2 click ");
        var otp = $('#otp-aadhaar').val();
        console.log(" otp value is " + otp);
        if (otp === null || otp === '' || !otp.match(/^[0-9]{6}$/)) {
            $('#otp-aadhaar_error').text('Enter OTP sent to your mobile number registered with aadhaar');
            $('#esign_img2').hide();
            $('#esign_2').show();
        } else {
            $('#esign_img2').show();
            $('#esign_2').hide();
            $.ajax({
                type: "POST",
                url: "otpAadharCheck",
                data: {otp: otp},
                success: function (aadhaar)
                {
                    console.log(" inside otpadar response ");
                    var myJSON = JSON.stringify(aadhaar);
                    var jsonvalue = JSON.parse(myJSON);
                    console.log(" inside otpadar response  after stringify  " + jsonvalue.esignreturn.aadhaarreturn);
                    //if (jsonvalue.esignreturn.aadhaarreturn === 'done') {
                    if (jsonvalue.esignreturn.aadhaarreturn === 'fail') {
                        $('#esign').toggle();
                        var formtype = $('#esign_submit').val();
                        $.ajax({
                            type: "POST",
                            url: "consent",
                            data: {check: 'esign', formtype: formtype},
                            success: function (check) {
                                var myJSON = JSON.stringify(check);
                                var jsonvalue = JSON.parse(myJSON);
                                if (jsonvalue.consentreturn.checkreturn === 'unsuccess')
                                {

                                    // $('#status').show();
                                    $('#failed').modal({backdrop: 'static', keyboard: false});
                                } else {
                                    if (jsonvalue.consentreturn.checkreturn === 'esign')
                                    {
                                        $('#reg_num').html(jsonvalue.consentreturn.Ref_string);
                                        $('#status').show();
                                        $('#done').modal({backdrop: 'static', keyboard: false});
                                    }
                                }
                            },
                            error: function () {
                                console.log('error');
                            }
                        });
                    } else if (jsonvalue.esignreturn.aadhaarreturn === 'wrongOTP') {
                        $('#otp-aadhaar_error').text(jsonvalue.otpErrMsg);
                        $('#esign_img2').hide();
                        $('#esign_2').show();
                        $('#otp-aadhaar').val('');
                        $('#otp-aadhaar').focus();
                    } else {
                        //$('#otp-aadhaar_error').text(jsonvalue.otpErrMsg);
                        $('#esign').toggle();
                        $('#reg_num').html(jsonvalue.consentreturn.Ref_string);
                        $('#status').show();
                        $('#done').modal({backdrop: 'static', keyboard: false});
                    }
                },
                error: function () {
                    console.log('error');
                }
            });
        }
    });
    // preview modal
    $('#close').click(function () {
        window.location.href = 'index.jsp';
    });
    $('#done_close').click(function () {
        window.location.href = 'showUserData';
    });
    $('#generate_close').click(function () {
        window.location.href = 'showUserData';
    });
    $('#generate_close1').click(function () {

        window.location.href = 'showUserData';
    });
    //TRACK

    $('.reject').click(function (e) {
        console.log("hi############")
        var value = $(this).closest('tr').children('td:first').text();
        $('#reject_click').val(value)
        $('#delModal').modal('show', {backdrop: 'static'});
    });
    $('#delModal_yes').click(function (e) {
// $(document).on('click', '#delModal_yes', function (e) {
        e.preventDefault();
        var value = $('#reject_click').val();
        $.ajax({
            type: "POST",
            url: "CancelRequest",
            data: {data: value},
            success: function (data) {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                if (jsonvalue.dbupdate === "cancel") {


                    $('#delModal').modal('toggle');
                    $('#delModal1').modal('show', {backdrop: 'static'});
                    location.reload(false);
                } else if (jsonvalue.dbupdate === 'notfound') {
                    console.log('not found');
                    $('#delModal').modal('toggle');
                    $('#delModal2').modal('show', {backdrop: 'static'});
                    location.reload(false);
                } else if (jsonvalue.dbupdate === 'fail') {
                    $('#delModal').modal('toggle');
                    $('#delModal3').modal('show', {backdrop: 'static'});
                    location.reload(false);
                }

            },
            error: function () {
                console.log("It seem's there is some problem, please try again later !!!")
            }
        });
    });

    $(document).on('click', '.previewedit', function (e) // line modified by pr on 22ndmay18
    {   //$("#ViewPendReqHOGRec").modal('toggle');
        $('.loader').show();
        e.preventDefault();

        var status_val = $(this).closest('tr').find('td:eq(2)').text();
        var curr_status = current_status(status_val);

        var ref_no = $(this).closest('tr').children('td.track_id1:first').text();
        var approve_btn = "<a href='javascript:void(0)' class='approve-changes' onclick=\"requestHogPendingActionApprove('" +ref_no+ "');\"><i class='fa fa-check'></i> Approve</a>";
        var reject_btn = "<a href='javascript:void(0)' class='reject-changes' onclick=\"requestHogPendingActionReject('" +ref_no+ "');\"><i class='fa fa-ban'></i> Reject</a>";
        var raise_btn = "<a href='javascript:void(0)' class='btn btn-secondary' onclick=\"requestActionPullback('" +ref_no+ "');\"><i class='fa fa-ban'></i> PullBack</a>";
        //var raise_btn = "";
        
//        if (ref_no !== "")
//        {
//            if (!ref_no) {
//                ref_no = $(this).closest('div.curr_reg_btn').attr('id');
//                ref_no = ref_no.replace("actions_", "");
//            }
//        } else {
//            var file = $('input[name="cert_file"]').get(0).files[0];
//            ref_no = $('#ref_no').val();
//        }

        var showEdit = $(this).attr("id");
        var whichform = ref_no.substring(0, ref_no.indexOf('-'));;

        var comingFrom = $("#comingFrom").val();
        var flag = 'hog';
        
        $.ajax({
            type: "POST",
            url: "preview",
            data: {data: ref_no, whichform: whichform, comingFrom: comingFrom, flag:flag},
            dataType: 'json'
        }).done(function (data) {
            resetCSRFRandom(); // line added by pr on 23rdjan18

            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            // line added  by pr on 31stmay18
            if (whichform === 'SMS') {

                $.ajax({
                    type: "POST",
                    url: "sms_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.sms_preview').html(dataResponse);
                                loadFormData()
                                $("#large1 .modal-title").html("Preview for " + ref_no);
                                $("#ref_no_uploadblkdata").val(ref_no);
                                $('#sms_preview .edit').removeClass('display-hide');
                                $('#large1').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#sms_preview :button[type='button']").removeAttr('disabled');
                                $("#sms_preview #tnc").removeAttr('disabled');
                                $("#sms_preview #tnc_div").addClass('display-hide');
                                $('#large1').modal({backdrop: 'static', keyboard: false});
                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#sms_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true')
                                {


                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {

                                        $("#sms_preview .edit").addClass('display-hide');
                                        $('#sms_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#sms_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){ 
                                    $("#sms_preview .edit").addClass('display-hide');
                                    $('#sms_preview .req_pullback').addClass('display-hide');
                                }, 2);

                                if (jsonvalue.sms_service === "quicksms") {
                                    $('#sms_serv6').prop('checked', true);
                                    $('#show_pull_url').hide();
                                    $('#show_note').hide();
                                    $('#quick_hide').hide();
                                    $('#audit_div').hide();
                                } else {

                                    $('#quick_hide').show();
                                    $('#audit_div').show();
                                    var myarray = jsonvalue.sms_service.split(',');
                                    for (var i = 0; i < myarray.length; i++)
                                    {
                                        var service = myarray[i].trim();
                                        if (service === 'quicksms') {
                                            $('#sms_serv6').prop('checked', true);
                                        } else if (service === 'push') {
                                            $('#sms_serv1').prop('checked', true);
                                        } else if (service === 'pull') {
                                            $('#sms_serv1').prop('checked', true);
                                            $('#sms_serv2').prop('checked', true);
                                            $('#show_pull_url').show();
                                            $('#show_note').show();
                                            if (jsonvalue.short_flag === 'y')
                                            {
                                                $('#short_code_input').show();
                                                $('#short_code').val(jsonvalue.short_note)
                                                $('#sy_code').prop('checked', true);
                                            } else if (jsonvalue.prvw_sflag === 'b')
                                            {
                                                $('#short_code_input').show();
                                                $('#short_code').val(jsonvalue.short_note)
                                                $('#sb_code').prop('checked', true);
                                            } else {
                                                $('#sn_code').prop('checked', true);
                                                $('#short_code_input').hide();
                                                $('#short_code').val("")
                                            }
                                        } else if (service === 'obd') {
                                            $('#sms_serv3').prop('checked', true);
                                        } else if (service === 'missedcall') {
                                            $('#sms_serv4').prop('checked', true);
                                        } else if (service === 'otp') {
                                            $('#sms_serv5').prop('checked', true);
                                        }
                                    }
                                }
                                $('#app_name').val(jsonvalue.app_name);
                                $('#app_url').val(jsonvalue.app_url);
                                $('#sms_usage').val(jsonvalue.sms_usage);
                                if (jsonvalue.server_loc === "Other")
                                {
                                    $('#server_other').show();
                                    $('#server_loc_txt').val(jsonvalue.server_loc_other);
                                    $('#server_loc').val(jsonvalue.server_loc);
                                } else {
                                    $('#server_other').hide();
                                    $('#server_loc').val(jsonvalue.server_loc);
                                    $('#server_loc_txt').val("");
                                }
                                $('#base_ip').val(jsonvalue.base_ip);
                                $('#service_ip').val(jsonvalue.service_ip);
                                $('#pull_url').val(jsonvalue.pull_url);
                                $('#pull_keyword').val(jsonvalue.pull_keyword);
                                $('input[name=t_off_name]').val(jsonvalue.tech_name);
                                $('input[name=tdesignation]').val(jsonvalue.tech_desig);
                                $('input[name=temp_code]').val(jsonvalue.tech_emp_code);
                                $('input[name=taddrs]').val(jsonvalue.tech_address);
                                //$('input[name=tcity]').val(jsonvalue.tech_city);
                                $('select[name=tstate]').val(jsonvalue.tech_state);
                                $.get('getDistrict', {
                                    user_state: jsonvalue.tech_state
                                }, function (response) {

                                    var select = $('#tcity');
                                    select.find('option').remove();
                                    $('<option>').val("").text("-SELECT-").appendTo(select);
                                    $.each(response, function (index, value) {

                                        if (jsonvalue.tech_city == value)
                                        {
                                            $('<option selected="selected">').val(value).text(value).appendTo(select);
                                        } else
                                        {
                                            $('<option>').val(value).text(value).appendTo(select);
                                        }

                                    });
                                });
                                $('input[name=tpin]').val(jsonvalue.tech_pin);
                                $('input[name=ttel_ofc]').val(jsonvalue.tech_ophone);
                                $('input[name=ttel_res]').val(jsonvalue.tech_rphone);
                                $('input[name=tmobile]').val(jsonvalue.tech_mobile);
                                $('input[name=tauth_email]').val(jsonvalue.tech_email);
                                $('input[name=bauth_off_name]').val(jsonvalue.bowner_name);
                                $('input[name=bdesignation]').val(jsonvalue.bowner_desig);
                                $('input[name=bemp_code]').val(jsonvalue.bowner_emp_code);
                                $('input[name=baddrs]').val(jsonvalue.bowner_address);
                                $('select[name=bstate]').val(jsonvalue.bowner_state);
                                $.get('getDistrict', {
                                    user_state: jsonvalue.bowner_state
                                }, function (response) {

                                    var select = $('#bcity');
                                    select.find('option').remove();
                                    $('<option>').val("").text("-SELECT-").appendTo(select);
                                    $.each(response, function (index, value) {

                                        if (jsonvalue.bowner_city == value)
                                        {
                                            $('<option selected="selected">').val(value).text(value).appendTo(select);
                                        } else
                                        {
                                            $('<option>').val(value).text(value).appendTo(select);
                                        }

                                    });
                                });
                                // $('input[name=bstate]').val(jsonvalue.bowner_state);
                                $('input[name=bpin]').val(jsonvalue.bowner_pin);
                                $('input[name=btel_ofc]').val(jsonvalue.bowner_ophone);
                                $('input[name=btel_res]').val(jsonvalue.bowner_rphone);
                                $('input[name=bmobile]').val(jsonvalue.bowner_mobile);
                                $('input[name=bauth_email]').val(jsonvalue.bowner_email);
                                $('#staging_ip').val(jsonvalue.staging_ip);
                                if (jsonvalue.flag_sender === 'Yes')
                                {
                                    $('#sender_div').show();
                                    $('#sender_id').val(jsonvalue.sender_id)
                                    $('#sender_y').prop('checked', true);
                                } else {
                                    $('#sender_n').prop('checked', true);
                                    $('#sender_div').hide();
                                    $('#sender_id').val("")
                                }
                                if (jsonvalue.audit === 'Yes')
                                {
                                    $('#audit_date_div').hide();
                                    $('#datepicker1').val("")
                                    $('#audit_y').prop('checked', true);
                                } else {
                                    $('#audit_n').prop('checked', true);
                                    $('#audit_date_div').show();
                                    $('#datepicker1').val(jsonvalue.audit_date)
                                }
                                $('#domestic_traf').val(jsonvalue.domestic_traffic)
                                $('#inter_traf').val(jsonvalue.inter_traffic)

                                $('#sms_preview .edit').removeClass('display-hide'); // line added by pr on 29thjan18

                                //if (curr_status === data.role) {
                                    createdombtn("#sms_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                if (jsonvalue.final_id != null && jsonvalue.final_id != "")
                                {
                                    $("#sms_preview .final_id_span_cls").show();
                                    $('#sms_preview .final_id_cls').html(jsonvalue.final_id); // line added by pr on 18thjul18
                                } else
                                {
                                    $("#sms_preview .final_id_span_cls").hide();
                                }

                                // end, code added by pr on 18thjul18

                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'DNS') {
                if (jsonvalue.oldornew === 'old') {
                    $.ajax({
                        type: "POST",
                        url: "old_dns_preview",
                        dataType: 'html', complete: function () {
                            $('.loader').hide();
                        }
                    }).done(function (dataResponse) {
                        if (dataResponse.trim() != '' && dataResponse != null) {
                            $('.dns_preview').html(dataResponse);
                            loadFormData()
                            $("#dns_preview_form .modal-title").html("Preview for " + ref_no);
                            $('#dns_preview #stat_type').val(jsonvalue.stat_type);
                            if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                            { 
                                $("#dns_preview .edit,.save-changes").hide();
                            } else if (showEdit === 'true') {
                                // start, code added by pr on 25thjan18                            
                                if (comingFrom === 'admin')
                                {
                                    $("#dns_preview .edit").addClass('display-hide');
                                    $('#dns_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                } else
                                {   
                                    $("#dns_preview .edit,.save-changes").hide();
                                }
                                // end, code added by pr on 25thjan18
                            }
                            setTimeout(function(){
                                $("#dns_preview .edit").addClass('display-hide');
                                $('#dns_preview .req_pullback').addClass('display-hide');
                            }, 2);

                            $('#dns_preview #add_dns').html('');
                            $('#dns_preview #add_cname').html('');
                            $('#dns_preview #addoldip').html('');
                            $('#dns_preview #addnewip').html('');
                            // dns url
                            $('#dns_preview #dns_loc_1').val(jsonvalue.server_location);
                            $('#dns_preview #migration_date1').val(jsonvalue.migration_date);
                            if (jsonvalue.domain_url.indexOf(";") > -1) {
                                var myarray = jsonvalue.domain_url.split(';');
                                for (var i = 0; i < myarray.length; i++)
                                {
                                    var service = myarray[i].trim();
                                    if (i === 0) {

                                        $('#dns_preview #dns_url_1').val(service);
                                    } else {

                                        //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 87%;"><span id="domain1" style="position: relative;width: 65px;float: right;margin-right: 52px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + jsonvalue.dns_type + '.in</span>' + '<button type="button" class="rmvbox1" style="position: relative;float: right;margin-top: -49px;font-size: xx-large;background: none;border: none;" title="Remove Row" id="dns_rmv1" name="dns_rmv1"><i class="fa fa-remove"></i></button></div>';
                                        var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1" placeholder="Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 0px; "></div>';
                                        $('#dns_preview #add_dns').append(elmnt);
                                    }
                                }
                            } else {
                                $('#dns_preview #dns_url_1').val(jsonvalue.domain_url);
                            }

                            if (jsonvalue.domain_new_ip.indexOf(";") > -1) {
                                var myarray = jsonvalue.domain_new_ip.split(';');
                                for (var i = 0; i < myarray.length; i++)
                                {
                                    var service = myarray[i].trim();
                                    if (i === 0) {

                                        $('#dns_preview #dns_newip_1').val(service);
                                    } else {

                                        //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 87%;"><span id="domain1" style="position: relative;width: 65px;float: right;margin-right: 52px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + jsonvalue.dns_type + '.in</span>' + '<button type="button" class="rmvbox1" style="position: relative;float: right;margin-top: -49px;font-size: xx-large;background: none;border: none;" title="Remove Row" id="dns_rmv1" name="dns_rmv1"><i class="fa fa-remove"></i></button></div>';
                                        var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1" placeholder="Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" type="text" name="dns_new_ip[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 0px; "></div>';
                                        $('#dns_preview #addnewip').append(elmnt);
                                    }
                                }
                            } else {
                                $('#dns_preview #dns_newip_1').val(jsonvalue.domain_new_ip);
                            }

                            if (jsonvalue.domain_old_ip != null)
                            {
                                if (jsonvalue.domain_old_ip.indexOf(";") > -1) {
                                    var myarray = jsonvalue.domain_old_ip.split(';');
                                    for (var i = 0; i < myarray.length; i++)
                                    {
                                        var service = myarray[i].trim();
                                        if (i === 0) {

                                            $('#dns_preview #dns_oldip_1').val(service);
                                        } else {

                                            //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 87%;"><span id="domain1" style="position: relative;width: 65px;float: right;margin-right: 52px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + jsonvalue.dns_type + '.in</span>' + '<button type="button" class="rmvbox1" style="position: relative;float: right;margin-top: -49px;font-size: xx-large;background: none;border: none;" title="Remove Row" id="dns_rmv1" name="dns_rmv1"><i class="fa fa-remove"></i></button></div>';
                                            var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1" placeholder="Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" type="text" name="dns_old_ip[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 0px; "></div>';
                                            $('#dns_preview #addoldip').append(elmnt);
                                        }
                                    }
                                } else {
                                    $('#dns_preview #dns_oldip_1').val(jsonvalue.domain_old_ip);
                                }
                            }

                            if (jsonvalue.domain_cname.indexOf(";") > -1) {

                                var myarray = jsonvalue.domain_cname.split(';');

                                for (var i = 0; i < myarray.length; i++)
                                {

                                    var cname = myarray[i].trim();
                                    if (i === 0) {
                                        // if (cname.startsWith("www."))
                                        //{
                                        //  $('#dns_preview #dns_cname_1').val(cname);
                                        //$('#dns_preview #dns_cname_1').prop("readonly", true);

                                        //                                            } else {
                                        $('#dns_preview #dns_cname_1').val(cname);
                                        //
                                        //                                            }
                                    } else {
                                        //                                            if (cname.startsWith("www."))
                                        //                                            {
                                        //                                                var elmnt = '<div class="con"><input class="form-control class_name_loc1" placeholder="Enter the DNS server location" value="' + cname + '" name="dns_cname[]" id="dns_loc" maxlength="100" style="margin: 20px 0px 20px 0px;" readonly></div>';
                                        //                                            } else {

                                        var elmnt = '<div class="con"><input class="form-control class_name_loc1" placeholder="Enter the DNS server location" value="' + cname + '" name="dns_cname[]" id="dns_loc" maxlength="100" style="margin: 20px 0px 20px 0px;"></div>';

                                        //}
                                        $('#dns_preview #add_cname').append(elmnt);
                                    }
                                }
                            } else {


                                $('#dns_preview #dns_cname_1').val(jsonvalue.domain_cname);
                            }



                            // RADIO
                            if (jsonvalue.dns_type === 'nic')
                            {
                                $('#dns_domain1_1').prop('checked', true);
                                $('#domain1').text('.nic.in');
                            } else {
                                $('#dns_domain1_2').prop('checked', true);
                                $('#domain1').text('.gov.in');
                            }
                            // checkboxes
                            if (jsonvalue.record_aaaa !== '') {
                                $('#request_aaaa_1').val(jsonvalue.record_aaaa);
                                $('#request_aaaa_1').removeClass('display-hide');
                                $('#aaaa_1').prop('checked', true);
                            } else {
                                $('#request_aaaa_1').val("");
                                $('#request_aaaa_1').addClass('display-hide');
                                $('#aaaa_1').prop('checked', false);
                            }


                            if (jsonvalue.record_mx !== '') {
                                $('#request_mx_1').val(jsonvalue.record_mx);
                                $('#request_mx_1').removeClass('display-hide');
                                $('#mx_1').prop('checked', true);
                            } else {

                                $('#request_mx_1').val("");
                                $('#request_mx_1').addClass('display-hide');
                                $('#mx_1').prop('checked', false);
                            }

                            if (jsonvalue.record_mx1 !== '') {

                                $('#mx_1').prop('checked', true);
                                if (jsonvalue.req_for === "req_modify")
                                {
                                    $('#request_mx_2').val(jsonvalue.record_mx1);
                                    $('#request_mx_2').removeClass('display-hide');
                                }
                            } else {

                                //
                                //                            ('#request_mx_2').val("");
                                //                            $('#request_mx_2').addClass('display-hide');


                                $('#mx_1').prop('checked', false);
                            }
                            if (jsonvalue.record_ptr !== '') {
                                $('#request_ptr_1').val(jsonvalue.record_ptr);
                                $('#request_ptr_1').removeClass('display-hide');
                                $('#ptr_1').prop('checked', true);
                                if (jsonvalue.req_for === "req_modify")
                                {
                                    $('#request_ptr_2').val(jsonvalue.record_ptr1);
                                    $('#request_ptr_2').removeClass('display-hide');
                                }
                            } else {

                                $('#request_ptr_1').val("");
                                $('#request_ptr_1').addClass('display-hide');
                                if (jsonvalue.req_for === "req_modify")
                                {
                                    $('#request_ptr_2').val("");
                                    $('#request_ptr_2').addClass('display-hide');
                                }
                                $('#ptr_1').prop('checked', false);
                            }

                            if (jsonvalue.record_srv !== '') {
                                $('#request_srv_1').val(jsonvalue.record_srv);
                                $('#request_srv_1').removeClass('display-hide');
                                $('#srv_1').prop('checked', true);
                            } else {

                                $('#request_srv_1').val("");
                                $('#request_srv_1').addClass('display-hide');
                                $('#srv_1').prop('checked', false);
                            }
                            if (jsonvalue.record_spf !== '') {
                                $('#request_spf_1').val(jsonvalue.record_spf);
                                $('#request_spf_1').removeClass('display-hide');
                                $('#spf_1').prop('checked', true);
                            } else {

                                $('#request_spf_1').val("");
                                $('#request_spf_1').addClass('display-hide');
                                $('#spf_1').prop('checked', false);
                            }
                            if (jsonvalue.record_txt !== '') {
                                $('#request_txt_1').val(jsonvalue.record_txt);
                                $('#request_txt_1').removeClass('display-hide');
                                $('#txt_1').prop('checked', true);
                            } else {

                                $('#request_txt_1').val("");
                                $('#request_txt_1').addClass('display-hide');
                                $('#txt_1').prop('checked', false);
                            }
                            if (jsonvalue.record_cname !== '') {
                                $('#request_cname1').val(jsonvalue.record_cname);
                                $('#request_cname1').removeClass('display-hide');
                                $('#cname_1').prop('checked', true);
                            } else {
                                $('#request_cname1').val("");
                                $('#request_cname1').addClass('display-hide');
                                $('#cname_1').prop('checked', false);
                            }
                            if (jsonvalue.record_dmarc !== '') {
                                $('#request_dmarc_1').val(jsonvalue.record_dmarc);
                                $('#request_dmarc_1').removeClass('display-hide');
                                $('#dmarc_1').prop('checked', true);
                            } else {

                                $('#request_dmarc_1').val("");
                                $('#request_dmarc_1').addClass('display-hide');
                                $('#dmarc_1').prop('checked', false);
                            }
                            if (jsonvalue.url === "web_url")
                            {
                                $('#dns_preview #url_web').prop('checked', true);
                                //$('#dns_form2 #url_web').val(jsonvalue.form_details.url);
                            } else {
                                $('#dns_preview #url_service').prop('checked', true);
                                //$('#dns_form2 #url_service').val(jsonvalue.form_details.url);
                            }
                            $('dns_preview #empcode').hide();
                            if (jsonvalue.req_for === "req_new")
                            {
                                $('#dns_preview #first_div').removeClass('display-hide');
                                $('#dns_preview #second_div').removeClass('display-hide');
                                $('#dns_preview #third_div').removeClass('display-hide');
                                $('#dns_preview #other_div_req').addClass('display-hide');
                                $('#dns_preview #dns_url_label').html("DNS URL")
                                $('#dns_preview #dns_ip_label').html("Application IP")
                                $('#dns_preview #req_prvw').html("NEW Request: ")
                                $('#dns_preview #req_new_ip').removeClass('display-hide');
                                $('#dns_preview #req_old_ip').addClass('display-hide');
                                $('#dns_preview #req_action1').val(jsonvalue.req_for);
                                $('#dns_preview #ptr_1').prop('checked', false);
                            }
                            if (jsonvalue.req_for === "req_cname")
                            {
                                $('#dns_preview #first_div').removeClass('display-hide');
                                $('#dns_preview #second_div').removeClass('display-hide');
                                $('#dns_preview #third_div').removeClass('display-hide');
                                $('#dns_preview #other_div_req').addClass('display-hide');
                                $('#dns_preview #dns_url_label').html("DNS URL")
                                $('#dns_preview #dns_ip_label').html("Application IP")
                                $('#dns_preview #req_prvw').html("CNAME ADD Request: ")
                                $('#dns_preview #req_new_ip').addClass('display-hide');
                                $('#dns_preview #req_old_ip').addClass('display-hide');
                                $('#dns_preview #req_action1').val(jsonvalue.req_for);
                                $('#dns_preview #ptr_1').prop('checked', false);
                            }
                            if (jsonvalue.req_for === "req_modify")
                            {
                                $('#dns_preview #first_div').removeClass('display-hide');
                                $('#dns_preview #second_div').removeClass('display-hide');
                                $('#dns_preview #third_div').removeClass('display-hide');
                                $('#dns_preview #other_div_req').removeClass('display-hide');
                                $('#dns_preview #dns_url_label').html("DNS URL")
                                $('#dns_preview #dns_ip_label').html("Application IP")
                                $('#dns_preview #other_url_label').html("OLD DNS URL")
                                $('#dns_preview #other_ip_label').html("OLD Application IP")
                                $('#dns_preview #req_prvw').html("Modify Request: ")
                                $('#dns_preview #req_old_ip').removeClass('display-hide');
                                $('#dns_preview #req_new_ip').removeClass('display-hide');
                                $('#dns_preview #req_action1').val(jsonvalue.req_for);
                                $('#dns_preview #ptr_1').prop('checked', false);
                            }
                            if (jsonvalue.req_for === "req_delete")
                            {
                                $('#dns_preview #first_div').removeClass('display-hide');
                                $('#dns_preview #second_div').removeClass('display-hide');
                                $('#dns_preview #third_div').removeClass('display-hide');
                                $('#dns_preview #dns_url_label').html("OLD DNS URL")
                                $('#dns_preview #dns_ip_label').html("OLD Application IP")
                                $('#dns_preview #other_div_req').addClass('display-hide');
                                $('#dns_preview #req_prvw').html("Delete Request: ")
                                $('#dns_preview #req_new_ip').removeClass('display-hide');
                                $('#dns_preview #req_action1').val(jsonvalue.req_for);
                                $('#dns_preview #ptr_1').prop('checked', true);
                            }
                            if (jsonvalue.form_type == "dns_bulk")
                            {
                                $('#dns_preview #first_div').addClass('display-hide');
                                $('#dns_preview #dns_url_label').html("DNS URL")
                                $('#dns_preview #dns_ip_label').html("Application IP")
                                $('#dns_preview #dns_bulk_preview').removeClass("display-hide");
                                $('#dns_preview #uploaded_filename').text(jsonvalue.uploaded_filename);
                                $('#dns_preview #confirm').val(jsonvalue.form_type);
                                $('#dns_preview #upload_dns').removeClass('display-hide');
                            } else {

                                $('#dns_preview #upload_dns').addClass('display-hide');
                                $('#dns_preview #dns_bulk_preview').addClass("display-hide");
                            }
                            $('#dns_preview #dns_user_empcode').val(jsonvalue.prvw_empcode);
                            $('#dns_preview #req_action').val(jsonvalue.req_for);

                            $('#dns_preview .edit').removeClass('display-hide');
                            //$('#dns_preview .edit').show();

                            $('#dns_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                            $("#dns_preview :button[type='button']").removeAttr('disabled');
                            $("#dns_preview #tnc").removeAttr('disabled');
                            $("#dns_preview #tnc_div").addClass('display-hide');
                            //if (curr_status === data.role) {
                                createdombtn("#dns_preview", approve_btn, reject_btn, raise_btn);
                            //}
                            $('#dns_preview_form').modal({backdrop: 'static', keyboard: false});
                        } else {
                            console.log('Preview is not loading');
                        }

                    })
                            .fail(function () {
                                console.log('This isn\'t LIT');
                            });
                } else {
                    $.ajax({
                        type: "POST",
                        url: "dns_preview",
                        dataType: 'html', complete: function () {
                            $('.loader').hide();
                        }
                    }).done(function (dataResponse) {
                        if (dataResponse.trim() != '' && dataResponse != null) {
                            $('.dns_preview').html(dataResponse);
                            loadFormData()
                            $("#dns_preview_form .modal-title").html("Preview for " + ref_no);
                            $('#dns_preview #stat_type').val(jsonvalue.stat_type);
                            if (jsonvalue.stat_type == 'completed') {
                                $('#dns_preview .edit').hide();
                            } else {
                                $('#dns_preview .edit').hide();
                            }
                            setTimeout(function(){ 
                                $('#dns_preview .req_pullback').addClass('display-hide');
                            }, 2);
                            $('#dns_preview .save-changes').addClass('display-hide');

                            $('#dns_preview #add_dns').html('');
                            $('#dns_preview #add_cname').html('');
                            $('#dns_preview #addoldip').html('');
                            $('#dns_preview #addnewip').html('');
                            // dns url
                            $('#dns_preview #dns_loc_1').val(jsonvalue.server_location);
                            $('#dns_preview #migration_date1').val(jsonvalue.migration_date);
                            // RADIO
                            if (jsonvalue.dns_type === 'nic')
                            {
                                $('#dns_domain1_1').prop('checked', true);
                                $('#domain1').text('.nic.in');
                            } else {
                                $('#dns_domain1_2').prop('checked', true);
                                $('#domain1').text('.gov.in');
                            }
                            // checkboxes
                            if (jsonvalue.record_aaaa !== '') {
                                $('#request_aaaa_1').val(jsonvalue.record_aaaa);
                                $('#request_aaaa_1').removeClass('display-hide');
                                $('#aaaa_1').prop('checked', true);
                            } else {
                                $('#request_aaaa_1').val("");
                                $('#request_aaaa_1').addClass('display-hide');
                                $('#aaaa_1').prop('checked', false);
                            }


                            if (jsonvalue.record_mx !== '') {
                                $('#request_mx_1').val(jsonvalue.record_mx);
                                $('#request_mx_1').removeClass('display-hide');
                                $('#mx_1').prop('checked', true);
                            } else {

                                $('#request_mx_1').val("");
                                $('#request_mx_1').addClass('display-hide');
                                $('#mx_1').prop('checked', false);
                            }

                            if (jsonvalue.record_mx1 !== '') {

                                $('#mx_1').prop('checked', true);
                                if (jsonvalue.req_for === "req_modify")
                                {
                                    $('#request_mx_2').val(jsonvalue.record_mx1);
                                    $('#request_mx_2').removeClass('display-hide');
                                }
                            } else {

//
//                            ('#request_mx_2').val("");
//                            $('#request_mx_2').addClass('display-hide');


                                $('#mx_1').prop('checked', false);
                            }
                            if (jsonvalue.record_ptr !== '') {
                                $('#request_ptr_1').val(jsonvalue.record_ptr);
                                $('#request_ptr_1').removeClass('display-hide');
                                $('#ptr_1').prop('checked', true);
                                if (jsonvalue.req_for === "req_modify")
                                {
                                    $('#request_ptr_2').val(jsonvalue.record_ptr1);
                                    $('#request_ptr_2').removeClass('display-hide');
                                }
                            } else {

                                $('#request_ptr_1').val("");
                                $('#request_ptr_1').addClass('display-hide');
                                if (jsonvalue.req_for === "req_modify")
                                {
                                    $('#request_ptr_2').val("");
                                    $('#request_ptr_2').addClass('display-hide');
                                }
                                $('#ptr_1').prop('checked', false);
                            }

                            if (jsonvalue.record_srv !== '') {
                                $('#request_srv_1').val(jsonvalue.record_srv);
                                $('#request_srv_1').removeClass('display-hide');
                                $('#srv_1').prop('checked', true);
                            } else {

                                $('#request_srv_1').val("");
                                $('#request_srv_1').addClass('display-hide');
                                $('#srv_1').prop('checked', false);
                            }
                            if (jsonvalue.record_spf !== '') {
                                $('#request_spf_1').val(jsonvalue.record_spf);
                                $('#request_spf_1').removeClass('display-hide');
                                $('#spf_1').prop('checked', true);
                            } else {

                                $('#request_spf_1').val("");
                                $('#request_spf_1').addClass('display-hide');
                                $('#spf_1').prop('checked', false);
                            }
                            if (jsonvalue.record_txt !== '') {
                                $('#request_txt_1').val(jsonvalue.record_txt);
                                $('#request_txt_1').removeClass('display-hide');
                                $('#txt_1').prop('checked', true);
                            } else {

                                $('#request_txt_1').val("");
                                $('#request_txt_1').addClass('display-hide');
                                $('#txt_1').prop('checked', false);
                            }
                            if (jsonvalue.record_cname !== '') {
                                $('#request_cname1').val(jsonvalue.record_cname);
                                $('#request_cname1').removeClass('display-hide');
                                $('#cname_1').prop('checked', true);
                            } else {
                                $('#request_cname1').val("");
                                $('#request_cname1').addClass('display-hide');
                                $('#cname_1').prop('checked', false);
                            }
                            if (jsonvalue.record_dmarc !== '') {
                                $('#request_dmarc_1').val(jsonvalue.record_dmarc);
                                $('#request_dmarc_1').removeClass('display-hide');
                                $('#dmarc_1').prop('checked', true);
                            } else {

                                $('#request_dmarc_1').val("");
                                $('#request_dmarc_1').addClass('display-hide');
                                $('#dmarc_1').prop('checked', false);
                            }
                            if (jsonvalue.url === "web_url")
                            {
                                $('#dns_preview #url_web').prop('checked', true);
                                //$('#dns_form2 #url_web').val(jsonvalue.form_details.url);
                            } else {
                                $('#dns_preview #url_service').prop('checked', true);
                                //$('#dns_form2 #url_service').val(jsonvalue.form_details.url);
                            }
                            $('dns_preview #empcode').hide();
                            if (jsonvalue.req_for === "req_new")
                            {
                                $('#dns_preview #first_div').removeClass('display-hide');
                                $('#dns_preview #second_div').removeClass('display-hide');
                                $('#dns_preview #third_div').removeClass('display-hide');
                                $('#dns_preview #other_div_req').addClass('display-hide');
                                $('#dns_preview #dns_url_label').html("DNS URL")
                                $('#dns_preview #dns_ip_label').html("Application IP")
                                $('#dns_preview #req_prvw').html("NEW Request: ")
                                $('#dns_preview #req_new_ip').removeClass('display-hide');
                                $('#dns_preview #req_old_ip').addClass('display-hide');
                                $('#dns_preview #req_action1').val(jsonvalue.req_for);
                                $('#dns_preview #ptr_1').prop('checked', false);
                            }
                            if (jsonvalue.req_for === "req_modify")
                            {
                                $('#dns_preview #first_div').removeClass('display-hide');
                                $('#dns_preview #second_div').removeClass('display-hide');
                                $('#dns_preview #third_div').removeClass('display-hide');
                                $('#dns_preview #other_div_req').removeClass('display-hide');
                                $('#dns_preview #dns_url_label').html("DNS URL")
                                $('#dns_preview #dns_ip_label').html("Application IP")
                                $('#dns_preview #other_url_label').html("OLD DNS URL")
                                $('#dns_preview #other_ip_label').html("OLD Application IP")
                                $('#dns_preview #req_prvw').html("Modify Request: ")
                                $('#dns_preview #req_old_ip').removeClass('display-hide');
                                $('#dns_preview #req_new_ip').removeClass('display-hide');
                                $('#dns_preview #req_action1').val(jsonvalue.req_for);
                                $('#dns_preview #ptr_1').prop('checked', false);
                            }
                            if (jsonvalue.req_for === "req_delete")
                            {
                                $('#dns_preview #first_div').removeClass('display-hide');
                                $('#dns_preview #second_div').removeClass('display-hide');
                                $('#dns_preview #third_div').removeClass('display-hide');
                                $('#dns_preview #dns_url_label').html("OLD DNS URL")
                                $('#dns_preview #dns_ip_label').html("OLD Application IP")
                                $('#dns_preview #other_div_req').addClass('display-hide');
                                $('#dns_preview #req_prvw').html("Delete Request: ")
                                $('#dns_preview #req_new_ip').removeClass('display-hide');
                                $('#dns_preview #req_action1').val(jsonvalue.req_for);
                                $('#dns_preview #ptr_1').prop('checked', true);
                            }
                            if (jsonvalue.form_type == "dns_bulk")
                            {
                                $('#dns_preview #first_div').addClass('display-hide');
                                $('#dns_preview #dns_url_label').html("DNS URL")
                                $('#dns_preview #dns_ip_label').html("Application IP")
                                $('#dns_preview #dns_bulk_preview').removeClass("display-hide");
                                $('#dns_preview #uploaded_filename').text(jsonvalue.uploaded_filename);
                                $('#dns_preview #confirm').val(jsonvalue.form_type);
                                $('#dns_preview #upload_dns').removeClass('display-hide');

                                $("#dns_preview #successBulkData thead tr").append("<td style='width: 6% !important;'>S.No</td><td>Domain</td><td>CNAME</td><td>New IP</td>");
                                validDomForReqModify(jsonvalue.validRecord);
                                setTimeout(function () {
                                    $("#dns_preview table#successBulkData button").prop('disabled', true);
                                }, 10);
                            } else {

                                $('#dns_preview #upload_dns').addClass('display-hide');
                                $('#dns_preview #dns_bulk_preview').addClass("display-hide");
                            }
                            $('#dns_preview #dns_user_empcode').val(jsonvalue.prvw_empcode);
                            $('#dns_preview #req_action').val(jsonvalue.req_for);

                            $('#dns_preview .edit').removeClass('display-hide');
                            //$('#dns_preview .edit').show();

                            $('#dns_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                            $("#dns_preview :button[type='button']").removeAttr('disabled');
                            $("#dns_preview #tnc").removeAttr('disabled');
                            $("#dns_preview #tnc_div").addClass('display-hide');
                            console.log(data.role);
                            //if (curr_status === data.role) {
                                createdombtn("#dns_preview", approve_btn, reject_btn, raise_btn);
                            //}
                            $('#dns_preview_form').modal({backdrop: 'static', keyboard: false});
                        } else {
                            console.log('Preview is not loading');
                        }

                    })
                            .fail(function () {
                                console.log('This isn\'t LIT');
                            });
                }
            } else if (whichform === 'WIFI') {
                $.ajax({
                    type: "POST",
                    url: "wifi_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.wifi_preview').html(dataResponse);
                                loadFormData()
                                $("#wifi_preview_form .modal-title").html("Preview for " + ref_no);
                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#wifi_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#wifi_preview .edit").addClass('display-hide');
                                        $('#wifi_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#wifi_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#wifi_preview .edit").addClass('display-hide');
                                    $('#wifi_preview .req_pullback').addClass('display-hide');
                                }, 2);

                                $('#wifi_preview #fwd_ofc_name').val(jsonvalue.prvw_ofc_name);
                                $('#wifi_preview #fwd_ofc_email').val(jsonvalue.prvw_ofc_email);
                                $('#wifi_preview #fwd_ofc_tel').val(jsonvalue.prvw_ofc_tel);
                                $('#wifi_preview #fwd_ofc_mobile').val(jsonvalue.prvw_ofc_mobile);
                                $('#wifi_preview #fwd_ofc_design').val(jsonvalue.prvw_ofc_desig);
                                $('#wifi_preview #fwd_ofc_add').val(jsonvalue.prvw_ofc_add);
                                if (jsonvalue.prvw_wifitype === 'nic') {
                                    $('#wifi_preview #nic_wifi_div').removeClass('display-hide');
                                    $('#wifi_preview #guest_wifi_div').addClass('display-hide');
                                    $('#wifi_preview #wifi_mac1').val(jsonvalue.prvw_wifimac1);
                                    $('#wifi_preview #wifi_os1').val(jsonvalue.prvw_wifios1);
                                    if (jsonvalue.prvw_wifimac2 !== '' && jsonvalue.prvw_wifios2 !== '') {
                                        $('#wifi_preview #wifi_mac2_div').removeClass('display-hide');
                                        $('#wifi_preview #wifi_mac2').val(jsonvalue.prvw_wifimac2);
                                        $('#wifi_preview #wifi_os2').val(jsonvalue.prvw_wifios2);
                                    } else {
                                        $('#wifi_preview #wifi_mac2_div').addClass('display-hide');
                                    }
                                    if (jsonvalue.prvw_wifimac3 !== '' && jsonvalue.prvw_wifios3 !== '') {
                                        $('#wifi_preview #wifi_mac3_div').removeClass('display-hide');
                                        $('#wifi_preview #wifi_mac3').val(jsonvalue.prvw_wifimac3);
                                        $('#wifi_preview #wifi_os3').val(jsonvalue.prvw_wifios3);
                                    } else {
                                        $('#wifi_preview #wifi_mac3_div').addClass('display-hide');
                                    }


                                } else {
                                    $('#wifi_preview #nic_wifi_div').addClass('display-hide');
                                    $('#wifi_preview #guest_wifi_div').removeClass('display-hide');
                                    $("#wifi_preview input[name='wifi_request']").prop('checked', false);
                                    if (jsonvalue.prvw_wifirequest === 'laptop') {
                                        $('#wifi_preview #wifi_request1').prop('checked', true);
                                    } else if (jsonvalue.prvw_wifirequest === 'mobile') {
                                        $('#wifi_preview #wifi_request2').prop('checked', true);
                                    } else {
                                        $('#wifi_preview #wifi_request3').prop('checked', true);
                                    }

                                    $('#wifi_preview #wifi_time').val(jsonvalue.prvw_wifitime);
                                    $("#wifi_preview input[name='wifi_duration']").prop('checked', false);
                                    if (jsonvalue.prvw_wifiduration === 'days') {
                                        $('#wifi_preview #wifi_duration1').prop('checked', true);
                                    } else {
                                        $('#wifi_preview #wifi_duration2').prop('checked', true);
                                    }
                                }

                                if (jsonvalue.wifi_process == "certificate")
                                {

                                    $('#wifi_preview #wifi_req_div').addClass("display-hide");
                                    $('#wifi_preview #wifi_mac2_div').removeClass('display-hide');
                                    $('#wifi_preview #wifi_mac3_div').removeClass('display-hide');
                                    $("#wifi_preview #wifi_cert").prop('checked', true);
                                    $('#wifi_preview #fwd_ofc_name').val(jsonvalue.prvw_ofc_name);
                                    $('#wifi_preview #fwd_ofc_email').val(jsonvalue.prvw_ofc_email);
                                    $('#wifi_preview #fwd_ofc_tel').val(jsonvalue.prvw_ofc_tel);
                                    $('#wifi_preview #fwd_ofc_mobile').val(jsonvalue.prvw_ofc_mobile);
                                    $('#wifi_preview #fwd_ofc_design').val(jsonvalue.prvw_ofc_desig);
                                    $('#wifi_preview #fwd_ofc_add').val(jsonvalue.prvw_ofc_add);
                                } else if (jsonvalue.wifi_process == "req_delete")
                                {
                                    console.log("inside if else" + jsonvalue.mac_os_List)
                                    $("#wifi_preview #wifi_DeletePrev_div").html('');
                                    $.each(jsonvalue.mac_os_List, function (i, l) {
                                        console.log("wifi_mac" + l.wifi_mac1)
                                        $("#wifi_preview #wifi_DeletePrev_div").append('<div class="row" style="padding: 10px;"><div class="col-md-5"><label class="control-label" for="street">MAC Address of the Device <span style="color: red">*</span></label><input class="form-control" readonly placeholder="Enter MAC Address (e.g: AA:AA:AA:AA:AA:AA)" type="text" name="wifi_mac1' + i + '" id="wifi_mac1"  value="' + l.wifi_mac1 + '" maxlength="17"></div><div class="col-md-5"><label class="control-label" for="street">Operating System of the Device<span style="color: red">*</span></label><input class="form-control" readonly placeholder="Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]" type="text" name="wifi_os1' + i + '" id="wifi_os1"  value="' + l.wifi_os1 + '"></div></div>');
                                        i++;
                                    });
                                    $("#wifi_preview #wifi_delete").prop('checked', true);
                                    $('#wifi_preview #wifi_req_div').addClass("display-hide");
                                    $('#wifi_preview #wifi_DeletePrev_div').removeClass("display-hide");
                                } else {
                                    console.log("inside else")
                                    $('#wifi_preview #wifi_req_div').removeClass("display-hide")
                                    $("#wifi_preview #wifi_req").prop('checked', true);
                                    $('#wifi_preview #fwd_ofc_name').val(jsonvalue.prvw_ofc_name);
                                    $('#wifi_preview #fwd_ofc_email').val(jsonvalue.prvw_ofc_email);
                                    $('#wifi_preview #fwd_ofc_tel').val(jsonvalue.prvw_ofc_tel);
                                    $('#wifi_preview #fwd_ofc_mobile').val(jsonvalue.prvw_ofc_mobile);
                                    $('#wifi_preview #fwd_ofc_design').val(jsonvalue.prvw_ofc_desig);
                                    $('#wifi_preview #fwd_ofc_add').val(jsonvalue.prvw_ofc_add);
                                    if (jsonvalue.prvw_wifitype === 'nic') {
                                        // $('#wifi_preview #nic_wifi_div').removeClass('display-hide');
                                        //$('#wifi_preview #guest_wifi_div').addClass('display-hide');
                                        $('#wifi_preview #wifi_mac1').val(jsonvalue.prvw_wifimac1);
                                        $('#wifi_preview #wifi_os1').val(jsonvalue.prvw_wifios1);
                                        if (jsonvalue.prvw_wifimac2 !== '' && jsonvalue.prvw_wifios2 !== '') {
                                            $('#wifi_preview #wifi_mac2_div').removeClass('display-hide');
                                            $('#wifi_preview #wifi_mac2').val(jsonvalue.prvw_wifimac2);
                                            $('#wifi_preview #wifi_os2').val(jsonvalue.prvw_wifios2);
                                        } else {
                                            $('#wifi_preview #wifi_mac2_div').addClass('display-hide');
                                        }
                                        if (jsonvalue.prvw_wifimac3 !== '' && jsonvalue.prvw_wifios3 !== '') {
                                            $('#wifi_preview #wifi_mac3_div').removeClass('display-hide');
                                            $('#wifi_preview #wifi_mac3').val(jsonvalue.prvw_wifimac3);
                                            $('#wifi_preview #wifi_os3').val(jsonvalue.prvw_wifios3);
                                        } else {
                                            $('#wifi_preview #wifi_mac3_div').addClass('display-hide');
                                        }


                                    } else {
                                        // $('#wifi_preview #nic_wifi_div').addClass('display-hide');
                                        // $('#wifi_preview #guest_wifi_div').removeClass('display-hide');
                                        $("#wifi_preview input[name='wifi_request']").prop('checked', false);
                                        if (jsonvalue.prvw_wifirequest === 'laptop') {
                                            $('#wifi_preview #wifi_request1').prop('checked', true);
                                        } else if (jsonvalue.prvw_wifirequest === 'mobile') {
                                            $('#wifi_preview #wifi_request2').prop('checked', true);
                                        } else {
                                            $('#wifi_preview #wifi_request3').prop('checked', true);
                                        }

                                        $('#wifi_preview #wifi_time').val(jsonvalue.prvw_wifitime);
                                        $("#wifi_preview input[name='wifi_duration']").prop('checked', false);
                                        if (jsonvalue.prvw_wifiduration === 'days') {
                                            $('#wifi_preview #wifi_duration1').prop('checked', true);
                                        } else {
                                            $('#wifi_preview #wifi_duration2').prop('checked', true);
                                        }
                                    }
                                }

                                // start, code added by pr on 19thdec19
                                if (jsonvalue.wifi_process !== "req_delete")
                                {
                                    if (jsonvalue.final_id != null && jsonvalue.final_id != "")
                                    {
                                        $("#wifi_preview .final_id_span_cls").show();
                                        $('#wifi_preview .final_id_cls').html(jsonvalue.final_id); // line added by pr on 18thjul18
                                    } else
                                    {
                                        $("#wifi_preview .final_id_span_cls").hide();
                                    }
                                }

                                // end, code added by pr on 19thdec19


                                // start, code added by pr on 2ndjan2020

                                if (jsonvalue.final_id != null && jsonvalue.final_id != "")
                                {
                                    $("#wifi_preview .ldap_cls").html(jsonvalue.ldap_wifi_value);
                                }

                                // end, code added by pr on 2ndjan2020 


                                $('#wifi_preview .edit').removeClass('display-hide');
                                $('#wifi_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#wifi_preview :button[type='button']").removeAttr('disabled');
                                $("#wifi_preview #tnc").removeAttr('disabled');
                                $("#wifi_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#wifi_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#wifi_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'SINGLEUSER') {

                console.log(" inside single user block in preview edit click showEdit value is " + showEdit + " comingfrom value is " + comingFrom);
                $.ajax({
                    type: "POST",
                    url: "single_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.single_preview').html(dataResponse);
                                $("#singleuser_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()


                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#singleuser_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {

                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {

                                        $("#singleuser_preview .edit").addClass('display-hide');
                                        $('#singleuser_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#singleuser_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#singleuser_preview .edit").addClass('display-hide');
                                    $('#singleuser_preview .req_pullback').addClass('display-hide');
                                }, 2);
                                if (jsonvalue.prvw_request_flag == "y")
                                {


                                    $('#singleuser_preview #req_other_type').prop("checked", true);
                                    $('#singleuser_preview #div_for_other').removeClass("display-hide");
                                    $('#singleuser_preview #div_for_self').addClass("display-hide");
                                    $('#singleuser_preview #applicant_name').val(jsonvalue.prvw_applicant_name);
                                    $('#singleuser_preview #applicant_design').val(jsonvalue.prvw_applicant_desig);
                                    $('#singleuser_preview #applicant_empcode').val(jsonvalue.prvw_applicant_empcode);
                                    $('#singleuser_preview #applicant_mobile').val(jsonvalue.prvw_applicant_mobile);
                                    $('#singleuser_preview #applicant_email').val(jsonvalue.prvw_applicant_email);
                                    $('#singleuser_preview #applicant_single_email1').val(jsonvalue.prvw_pemail1);
                                    $('#singleuser_preview #applicant_single_email2').val(jsonvalue.prvw_pemail2);
                                    $('#singleuser_preview #single_dob7').val(jsonvalue.prvw_dob);
                                    $('#singleuser_preview #single_dor7').val(jsonvalue.prvw_dor);
                                    if (jsonvalue.prvw_idtype === 'id_name') {
                                        $("#singleuser_preview #applicant_single_id_type_1").prop('checked', true);
                                    } else {
                                        $("#singleuser_preview #applicant_single_id_type_2").prop('checked', true);
                                    }
                                    $('#singleuser_preview #single_email1').val(jsonvalue.prvw_pemail1);
                                    $('#singleuser_preview #single_email2').val(jsonvalue.prvw_pemail2);
                                    $('#singleuser_preview #applicant_state').val(jsonvalue.prvw_applicant_state);
                                    if (jsonvalue.prvw_emp_type === 'emp_regular') {
                                        $("#singleuser_preview #applicant_single_emp_type1").prop('checked', true);
                                    } else if (jsonvalue.prvw_emp_type === 'consultant') {
                                        $("#singleuser_preview #applicant_single_emp_type2").prop('checked', true);
                                    } else {
                                        $("#singleuser_preview #applicant_single_emp_type3").prop('checked', true);
                                    }
                                    if (jsonvalue.prvw_type === 'mail') {
                                        $("#singleuser_preview #mail").prop('checked', true);
                                    } else if (jsonvalue.prvw_type === 'eoffice') {
                                        $("#singleuser_preview #eoffice").prop('checked', true);
                                    } else {
                                        $("#singleuser_preview #app").prop('checked', true);
                                    }

                                    var applicant_dept = jsonvalue.prvw_applicant_dept.split("~");

                                    $('#singleuser_preview #applicant_employment').val(applicant_dept[0])
                                    if (applicant_dept[0] === 'Central' || applicant_dept[0] === 'UT') {
                                        $('#singleuser_preview #applicant_central_div').show();
                                        $('#singleuser_preview #applicant_state_div').hide();
                                        $('#singleuser_preview #applicant_other_div').hide();
                                        $('#singleuser_preview #applicant_other_text_div').addClass("display-hide");
                                        var select = $('#singleuser_preview #applicant_min');
                                        select.find('option').remove();
                                        $('<option>').val(applicant_dept[1]).text(applicant_dept[1]).appendTo(select);
                                        var select = $('#singleuser_preview #applicant_dept');
                                        select.find('option').remove();
                                        $('<option>').val(applicant_dept[2]).text(applicant_dept[2]).appendTo(select);
                                        if (applicant_dept[2].toString().toLowerCase() === 'other') {
                                            $('#singleuser_preview #applicant_other_text_div').removeClass("display-hide");
                                            $('#singleuser_preview #applicant_other_dept').val(applicant_dept[3]);
                                        }


                                    } else if (applicant_dept[0] === 'State') {
                                        $('#singleuser_preview #applicant_central_div').hide();
                                        $('#singleuser_preview #applicant_state_div').show();
                                        $('#singleuser_preview #applicant_other_div').hide();
                                        $('#singleuser_preview #applicant_other_text_div').addClass("display-hide");
                                        var select = $('#singleuser_preview #applicant_stateCode');
                                        select.find('option').remove();
                                        $('<option>').val(applicant_dept[1]).text(applicant_dept[1]).appendTo(select);
                                        var select = $('#singleuser_preview #applicant_Smi');
                                        select.find('option').remove();

                                        $('<option>').val(applicant_dept[2]).text(applicant_dept[2]).appendTo(select);

                                        if (applicant_dept[2].toString().toLowerCase() === 'other') {
                                            $('#singleuser_preview #other_text_div').removeClass("display-hide");
                                            $('#singleuser_preview #other_dept').val(applicant_dept[3]);
                                        }

                                    } else if (applicant_dept[0] === 'Others' || applicant_dept[0] === "Psu" || applicant_dept[0] === "Const" || applicant_dept[0] === "Nkn" || applicant_dept[0] === "Project") {

                                        $('#singleuser_preview #applicant_central_div').hide();
                                        $('#singleuser_preview #applicant_state_div').hide();
                                        $('#singleuser_preview #applicant_other_div').show();
                                        $('#singleuser_preview #applicant_other_text_div').addClass("display-hide");
                                        var select = $('#singleuser_preview #applicant_Org');

                                        select.find('option').remove();
                                        $('<option>').val(applicant_dept[1]).text(applicant_dept[1]).appendTo(select);
                                        if (applicant_dept[1].toLowerCase() === 'other') {
                                            $('#singleuser_preview #applicant_other_text_div').removeClass("display-hide");
                                            $('#singleuser_preview #applicant_other_dept').val(applicant_dept[2]);
                                        }

                                    } else {
                                        $('#singleuser_preview #applicant_central_div').hide();
                                        $('#singleuser_preview #applicant_state_div').hide();
                                        $('#singleuser_preview #applicant_other_div').hide();
                                    }

                                } else {

                                    $('#singleuser_preview #req_self_type').prop("checked", true);
                                    $('#singleuser_preview #div_for_self').removeClass("display-hide");
                                    $('#singleuser_preview #div_for_other').addClass("display-hide");
                                    $('#singleuser_preview #single_dob1').val(jsonvalue.prvw_dob);
                                    $('#singleuser_preview #single_dor1').val(jsonvalue.prvw_dor);
                                    if (jsonvalue.prvw_idtype === 'id_name') {
                                        $("#singleuser_preview #single_id_type_1").prop('checked', true);
                                    } else {
                                        $("#singleuser_preview #single_id_type_2").prop('checked', true);
                                    }
                                    $('#singleuser_preview #single_email1').val(jsonvalue.prvw_pemail1);
                                    $('#singleuser_preview #single_email2').val(jsonvalue.prvw_pemail2);
                                    if (jsonvalue.prvw_emp_type === 'emp_regular') {
                                        $("#singleuser_preview #single_emp_type1").prop('checked', true);
                                    } else if (jsonvalue.prvw_emp_type === 'consultant') {
                                        $("#singleuser_preview #single_emp_type2").prop('checked', true);
                                    } else {
                                        $("#singleuser_preview #single_emp_type3").prop('checked', true);
                                    }
                                    if (jsonvalue.prvw_type === 'mail') {
                                        $("#singleuser_preview #mail_1").prop('checked', true);
                                    } else if (jsonvalue.prvw_type === 'eoffice') {
                                        $("#singleuser_preview #eoffice_1").prop('checked', true);
                                    } else {
                                        $("#singleuser_preview #app_1").prop('checked', true);
                                    }
                                }


                                // start, code added by pr on 18thjul18

                                if (jsonvalue.final_id != null && jsonvalue.final_id != "")
                                {
                                    $("#singleuser_preview .final_id_span_cls").show();
                                    $('#singleuser_preview .final_id_cls').html(jsonvalue.final_id); // line added by pr on 18thjul18
                                } else
                                {
                                    $("#singleuser_preview .final_id_span_cls").hide();
                                }


                                if (jsonvalue.mailexist == "yes")
                                {
                                    $("#singleuser_preview #single_dup_err").html(jsonvalue.dup_error)
                                    $("#singleuser_preview #single_dup_err").css("color", "blue");
                                    $("#singleuser_preview #single_dup_err").css("font-weight", "bold");
                                } else
                                {
                                    $("#singleuser_preview #single_dup_err").html("")
                                }

                                // end, code added by pr on 18thjul18                        
                                if (jsonvalue.prvw_under_sec_email != "" && jsonvalue.prvw_under_sec_email != null)
                                {

                                    $('#singleuser_preview #USEditPreview').removeClass("display-hide");
                                    $("#singleuser_preview #under_sec_email").val(jsonvalue.prvw_under_sec_email)
                                    $("#singleuser_preview #under_sec_name").val(jsonvalue.prvw_under_sec_name);
                                    $("#singleuser_preview #under_sec_mobile").val(jsonvalue.prvw_under_sec_mobile);
                                    $("#singleuser_preview #under_sec_desig").val(jsonvalue.prvw_under_sec_desig);
                                    $("#singleuser_preview #under_sec_tel").val(jsonvalue.prvw_under_sec_telephone);
                                }

                                $('#singleuser_preview .edit').removeClass('display-hide');
                                $('#single_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#singleuser_preview :button[type='button']").removeAttr('disabled');
                                $("#singleuser_preview #tnc").removeAttr('disabled');
                                $("#singleuser_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#singleuser_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#singleuser_preview_form').modal({backdrop: 'static', keyboard: false});
                                console.log(" inside single user form in onlineform.js value of final_id is " + jsonvalue.final_id);
                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'LDAP') {


                $.ajax({
                    type: "POST",
                    url: "ldap_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.ldap_preview').html(dataResponse);
                                $("#ldap_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()
                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#ldap_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#ldap_preview .edit").addClass('display-hide');
                                        $('#ldap_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#ldap_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#ldap_preview .edit").addClass('display-hide');
                                    $('#ldap_preview .req_pullback').addClass('display-hide');
                                }, 2);
                                $('#ldap_preview #app_name').val(jsonvalue.prvw_appname);
                                $('#ldap_preview #app_url').val(jsonvalue.prvw_appurl);
                                $('#ldap_preview #base_ip').val(jsonvalue.prvw_baseip);
                                $('#ldap_preview #service_ip').val(jsonvalue.prvw_serviceip);
                                $('#ldap_preview #domain').val(jsonvalue.prvw_domain);
                                if (jsonvalue.prvw_server_loc === "Other")
                                {
                                    $('#ldap_preview #server_other').removeClass('display-hide');
                                    $('#ldap_preview #server_loc_txt').val(jsonvalue.prvw_server_loc_other);
                                    $('#ldap_preview #server_loc').val(jsonvalue.prvw_server_loc);
                                } else {
                                    $('#ldap_preview #server_other').addClass('display-hide');
                                    $('#ldap_preview #server_loc_txt').val('');
                                    $('#ldap_preview #server_loc').val(jsonvalue.prvw_server_loc);
                                }

                                if (jsonvalue.prvw_https === 'yes') {
                                    $("#ldap_preview #https_1").prop('checked', true);
                                } else {
                                    $("#ldap_preview #https_2").prop('checked', true);
                                }

                                if (jsonvalue.prvw_audit === 'yes') {
                                    $("#ldap_preview #audit_1").prop('checked', true);
                                    $('#ldap_preview #cert1_div').removeClass('display-hide');
                                    $('#ldap_preview #no_audit_div').addClass('display-hide');
                                    $('#ldap_preview #uploaded_filename').text(jsonvalue.prvw_uploaded_filename);
                                } else {
                                    $("#ldap_preview #audit_2").prop('checked', true);
                                    $('#ldap_preview #cert1_div').addClass('display-hide');
                                    $('#ldap_preview #no_audit_div').removeClass('display-hide');
                                    $('#ldap_preview #ldap_id1').val(jsonvalue.prvw_ldap_id1);
                                    $('#ldap_preview #ldap_id2').val(jsonvalue.prvw_ldap_id2);
                                }

                                $('#ldap_preview .edit').removeClass('display-hide');
                                $('#ldap_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#ldap_preview :button[type='button']").removeAttr('disabled');
                                $("#ldap_preview #tnc").removeAttr('disabled');
                                $("#ldap_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#ldap_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#ldap_preview_form').modal({backdrop: 'static', keyboard: false});
                                $('#upload_ldap').hide();
                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'IMAPPOP') {
                $.ajax({
                    type: "POST",
                    url: "imappop_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {
                            console.log(dataResponse + "ref_no" + ref_no);
                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.imappop_preview').html(dataResponse);
                                $("#imappop_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()

                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {

                                    $("#imapop_preview .edit,.save-changes").hide();
                                    //  $('#imappop_preview .edit').addClass('display-hide'); // line added by pr on 20thdec17

                                } else if (showEdit === 'true')
                                {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#imappop_preview .edit").addClass('display-hide');
                                        $('#imappop_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#imappop_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#imappop_preview .edit").addClass('display-hide');
                                    $('#imappop_preview .req_pullback').addClass('display-hide');
                                }, 2);                                
                                if (jsonvalue.protocol === 'imap')
                                {
                                    $('#imappop_preview #protocol_imap').prop('checked', true);
                                } else {
                                    $('#imappop_preview #protocol_pop').prop('checked', true);
                                }

                                $('#imappop_preview .edit').removeClass('display-hide'); // commented by pr on 20thdec17, uncommented by pr on 29thjan18
                                $('#imappop_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#imappop_preview :button[type='button']").removeAttr('disabled');
                                $("#imappop_preview #tnc").removeAttr('disabled');
                                $("#imappop_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#imappop_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#imappop_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'MOBILE') {

                $.ajax({
                    type: "POST",
                    url: "mobile_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.mobile_preview').html(dataResponse);
                                $("#mobile_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()
                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#mobile_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#mobile_preview .edit").addClass('display-hide');
                                        $('#mobile_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#mobile_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#mobile_preview .edit").addClass('display-hide');
                                    $('#mobile_preview .req_pullback').addClass('display-hide');
                                }, 2);                                
                                
                                $('#mobile_preview #country_code').val(jsonvalue.country_code);
                                $('#mobile_preview #new_mobile').val(jsonvalue.new_mobile);
                                // $('#mobile_preview #remarks').val(jsonvalue.remarks);

                                if (jsonvalue.remarks_flag !== "y")
                                {

                                    $('#mobile_preview #remarks').val(jsonvalue.remarks);
                                    $('#mobile_preview #remarks_div').addClass('display-hide');

                                } else {

                                    $('#mobile_preview #remarks').val("other");
                                    $('#mobile_preview #other_remarks').val(jsonvalue.remarks);
                                    $('#mobile_preview #remarks_div').removeClass('display-hide');

                                }
                                $('#mobile_preview .edit').removeClass('display-hide');
                                $('#mobile_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#mobile_preview :button[type='button']").removeAttr('disabled');
                                $("#mobile_preview #tnc").removeAttr('disabled');
                                $("#mobile_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#mobile_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#mobile_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'NKN') {

                $.ajax({
                    type: "POST",
                    url: "nkn_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.nkn_preview').html(dataResponse);
                                $("#nkn_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()
                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#nkn_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#nkn_preview .edit").addClass('display-hide');
                                        $('#nkn_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#nkn_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#nkn_preview .edit").addClass('display-hide');
                                    $('#nkn_preview .req_pullback').addClass('display-hide');
                                }, 2);     
                                $('#nkn_preview #inst_name').val(jsonvalue.prvw_inst_name);
                                $('#nkn_preview #inst_id').val(jsonvalue.prvw_inst_id);
                                $('#nkn_preview #nkn_project').val(jsonvalue.prvw_nkn_project);
                                if (jsonvalue.prvw_request_type === 'nkn_bulk') {
                                    $('#nkn_preview #nkn_single_preview').addClass('display-hide');
                                    $('#nkn_preview #nkn_bulk_preview').removeClass('display-hide');
                                    $('#nkn_preview #uploaded_filename').text(jsonvalue.prvw_uploaded_filename);
                                } else {
                                    $('#nkn_preview #nkn_single_preview').removeClass('display-hide');
                                    $('#nkn_preview #nkn_bulk_preview').addClass('display-hide');
                                    $('#nkn_preview #single_dob5').val(jsonvalue.prvw_dob);
                                    $('#nkn_preview #single_dor5').val(jsonvalue.prvw_dor);
                                    $('#nkn_preview #single_email1').val(jsonvalue.prvw_pemail1);
                                    $('#nkn_preview #single_email2').val(jsonvalue.prvw_pemail2);
                                }

                                if (jsonvalue.prvw_under_sec_email != "" && jsonvalue.prvw_under_sec_email != null)
                                {

                                    $('#nkn_preview #USEditPreview').removeClass("display-hide");
                                    $("#nkn_preview #under_sec_email").val(jsonvalue.prvw_under_sec_email)
                                    $("#nkn_preview #under_sec_name").val(jsonvalue.prvw_under_sec_name);
                                    $("#nkn_preview #under_sec_mobile").val(jsonvalue.prvw_under_sec_mobile);
                                    $("#nkn_preview #under_sec_desig").val(jsonvalue.prvw_under_sec_desig);
                                    $("#nkn_preview #under_sec_tel").val(jsonvalue.prvw_under_sec_telephone);
                                }

                                // start, code added by pr on 18thjul18

                                if (jsonvalue.final_id != null && jsonvalue.final_id != "")
                                {
                                    $("#nkn_preview .final_id_span_cls").show();
                                    $('#nkn_preview .final_id_cls').html(jsonvalue.final_id); // line added by pr on 18thjul18
                                } else
                                {
                                    $("#nkn_preview .final_id_span_cls").hide();
                                }

                                // end, code added by pr on 18thjul18



                                $('#nkn_preview .edit').removeClass('display-hide');
                                $('#nkn_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#nkn_preview :button[type='button']").removeAttr('disabled');
                                $("#nkn_preview #tnc").removeAttr('disabled');
                                $("#nkn_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#nkn_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#nkn_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'GEM') {
                $.ajax({
                    type: "POST",
                    url: "gem_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.gem_preview').html(dataResponse);
                                $("#gem_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()


                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#gem_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#gem_preview .edit").addClass('display-hide');
                                        $('#gem_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#gem_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#gem_preview .edit").addClass('display-hide');
                                    $('#gem_preview .req_pullback').addClass('display-hide');
                                }, 2); 
                                if (jsonvalue.prvw_pse === 'central_pse') {
                                    $("#gem_preview #central_pse").prop('checked', true);
                                    $('#gem_preview #central_pse_div').removeClass('display-hide');
                                    $('#gem_preview #state_pse_div').addClass('display-hide');
                                    $.get('centralMinistry', {
                                        orgType: 'Central'
                                    }, function (response) {
                                        var select = $('#gem_preview #pse_ministry');
                                        select.find('option').remove();
                                        $('<option>').val("").text("-SELECT-").appendTo(select);
                                        $.each(response, function (index, value) {
                                            $('<option>').val(value).text(value).appendTo(select);
                                        });
                                    });
                                    setTimeout(function () {
                                        $("#gem_preview  #pse_ministry").val(jsonvalue.prvw_pse_ministry);
                                    }, 200);
                                } else {


                                    $('#gem_preview select[name=pse_state]').val(jsonvalue.prvw_pse_state);
                                    $.get('getDistrict', {
                                        user_state: jsonvalue.prvw_pse_state
                                    }, function (response) {

                                        var select = $('#gem_preview #pse_district');
                                        select.find('option').remove();
                                        $('<option>').val("").text("-SELECT-").appendTo(select);
                                        $.each(response, function (index, value) {

                                            if (jsonvalue.prvw_pse_district == value)
                                            {
                                                $('<option selected="selected">').val(value).text(value).appendTo(select);
                                            } else
                                            {
                                                $('<option>').val(value).text(value).appendTo(select);
                                            }

                                        });
                                    });
                                    $("#gem_preview #state_pse").prop('checked', true);
                                    $('#gem_preview #state_pse_div').removeClass('display-hide');
                                    $('#gem_preview #central_pse_div').addClass('display-hide');
//                                            $("#gem_preview #pse_state").val(jsonvalue.prvw_pse_state);
//                                            $("#gem_preview #pse_district").val(jsonvalue.prvw_pse_district);
                                }

                                $('#gem_preview #fwd_ofc_name').val(jsonvalue.prvw_ofc_name);
                                $('#gem_preview #fwd_ofc_email').val(jsonvalue.prvw_ofc_email);
                                $('#gem_preview #fwd_ofc_tel').val(jsonvalue.prvw_ofc_tel);
                                $('#gem_preview #fwd_ofc_mobile').val(jsonvalue.prvw_ofc_mobile);
                                $('#gem_preview #fwd_ofc_design').val(jsonvalue.prvw_ofc_desig);
                                $('#gem_preview #fwd_ofc_add').val(jsonvalue.prvw_ofc_add);
                                $('#gem_preview #single_dob3').val(jsonvalue.prvw_dob);
                                $('#gem_preview #single_dor3').val(jsonvalue.prvw_dor);
                                $('#gem_preview #single_email1').val(jsonvalue.prvw_pemail1);
                                $('#gem_preview #single_email2').val(jsonvalue.prvw_pemail2);
                                $('#gem_preview #domestic_traf').val(jsonvalue.prvw_traffic);
                                $("#gem_preview input:radio[value='" + jsonvalue.primary_user + "']").prop("checked", true);
                                if (jsonvalue.primary_user == "yes")
                                {
                                    $('#gem_preview #primary_text_id').removeClass("display-hide");
                                    $('#gem_preview #primary_user_id').val(jsonvalue.primary_user_id);
                                } else {
                                    $('#gem_preview #primary_text_id').addClass("display-hide");
                                    $('#gem_preview #primary_user_id').val("");
                                }


                                // start, code added by pr on 18thjul18

                                if (jsonvalue.final_id != null && jsonvalue.final_id != "")
                                {
                                    $("#gem_preview .final_id_span_cls").show();
                                    $('#gem_preview .final_id_cls').html(jsonvalue.final_id); // line added by pr on 18thjul18
                                } else
                                {
                                    $("#gem_preview .final_id_span_cls").hide();
                                }

                                // end, code added by pr on 18thjul18




                                $('#gem_preview #role_assign').val(jsonvalue.role_assign);
                                $('#gem_preview .edit').removeClass('display-hide');
                                $('#gem_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#gem_preview :button[type='button']").removeAttr('disabled');
                                $("#gem_preview :input[type='radio']").removeAttr('disabled');
                                $("#gem_preview #tnc").removeAttr('disabled');
                                $("#gem_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#gem_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#gem_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'BULKUSER') {
                $.ajax({
                    type: "POST",
                    url: "bulk_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.bulk_preview').html(dataResponse);
                                $("#bulkuser_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()
                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#bulkuser_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#bulkuser_preview .edit").addClass('display-hide');
                                        $('#bulkuser_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#bulkuser_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#bulkuser_preview .edit").addClass('display-hide');
                                    $('#bulkuser_preview .req_pullback').addClass('display-hide');
                                }, 2); 
                                
                                if (jsonvalue.prvw_type === 'mail') {
                                    $("#bulkuser_preview #bulk_mail_1").prop('checked', true);
                                } else if (jsonvalue.prvw_type === 'eoffice') {
                                    $("#bulkuser_preview #bulk_eoffice_1").prop('checked', true);
                                } else {
                                    $("#bulkuser_preview #bulk_app_1").prop('checked', true);
                                }

                                if (jsonvalue.prvw_id_type === 'id_name') {
                                    $("#bulkuser_preview #bulk_id_type_1").prop('checked', true);
                                } else {
                                    $("#bulkuser_preview #bulk_id_type_2").prop('checked', true);
                                }

                                if (jsonvalue.prvw_emp_type === 'emp_regular') {
                                    $('#bulkuser_preview #bulk_emp_type1').prop('checked', 'checked');
                                } else if (jsonvalue.prvw_emp_type === 'consultant') {
                                    $('#bulkuser_preview #bulk_emp_type2').prop('checked', 'checked');
                                } else {
                                    $('#bulkuser_preview #bulk_emp_type3').prop('checked', 'checked');
                                }

                                $('#bulkuser_preview #uploaded_filename').text(jsonvalue.prvw_uploaded_filename);
                                // start, code added by pr on 12thjun18

                                console.log(" valid_filepath is " + jsonvalue.valid_filepath + " not valid path is " + jsonvalue.notvalid_filepath + " error_filepath is " + jsonvalue.error_filepath);

                                if (jsonvalue.prvw_under_sec_email != "" && jsonvalue.prvw_under_sec_email != null)
                                {

                                    $('#bulkuser_preview #USEditPreview').removeClass("display-hide");
                                    $("#bulkuser_preview #under_sec_email").val(jsonvalue.prvw_under_sec_email)
                                    $("#bulkuser_preview #under_sec_name").val(jsonvalue.prvw_under_sec_name);
                                    $("#bulkuser_preview #under_sec_mobile").val(jsonvalue.prvw_under_sec_mobile);
                                    $("#bulkuser_preview #under_sec_desig").val(jsonvalue.prvw_under_sec_desig);
                                    $("#bulkuser_preview #under_sec_tel").val(jsonvalue.prvw_under_sec_telephone);
                                }

                                $('#bulkuser_preview .edit').removeClass('display-hide');
                                $('#bulk_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#bulkuser_preview :button[type='button']").removeAttr('disabled');
                                $("#bulkuser_preview #tnc").removeAttr('disabled');
                                $("#bulkuser_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#gem_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#bulkuser_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'DLIST') {
                whichform = 'distribution';
                $.ajax({
                    type: "POST",
                    url: "distribution_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {
                                $('.distribution_preview').html(dataResponse);
                                $("#distribution_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData();
                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#distribution_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#distribution_preview .edit").addClass('display-hide');
                                        $('#distribution_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#distribution_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18

                                }
                                setTimeout(function(){
                                    $("#distribution_preview .edit").addClass('display-hide');
                                    $('#distribution_preview .req_pullback').addClass('display-hide');
                                }, 2);                                 
                                
                                $('#distribution_preview #list_name').val(jsonvalue.list_name);
                                $('#distribution_preview #description_list').val(jsonvalue.list_description);
                                $('#distribution_preview #validity_date1').val(jsonvalue.validity_date);
                                if (jsonvalue.list_mod === 'yes') {
                                    $('#distribution_preview #list_mod_yes').prop('checked', true);
                                } else {
                                    $('#distribution_preview #list_mod_no').prop('checked', true);
                                }

                                if (jsonvalue.allowed_member === 'yes') {
                                    $('#distribution_preview #allowed_member_yes').prop('checked', true);
                                } else {
                                    $('#distribution_preview #allowed_member_no').prop('checked', true);
                                }

                                if (jsonvalue.list_temp === 'yes') {
                                    $('#distribution_preview #list_temp_yes').prop('checked', true);
                                    $('#distribution_preview #validity_date_show').show();
                                } else {
                                    $('#distribution_preview #list_temp_no').prop('checked', true);
                                    $('#distribution_preview #validity_date_show').hide();
                                }

                                if (jsonvalue.non_nicnet === 'yes') {
                                    $('#distribution_preview #non_nicnet_yes').prop('checked', true);
                                } else {
                                    $('#distribution_preview #non_nicnet_no').prop('checked', true);
                                }

                                $('#distribution_preview #auth_off_name').val(jsonvalue.t_off_name);
                                $('#distribution_preview #auth_email').val(jsonvalue.tauth_email);
                                $('#distribution_preview #mobile').val(jsonvalue.tmobile);
                                $('#distribution_preview .edit').removeClass('display-hide');
                                $('#dlist_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#distribution_preview :button[type='button']").removeAttr('disabled');
                                $("#distribution_preview #tnc").removeAttr('disabled');
                                $("#distribution_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#distribution_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#distribution_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'RELAY') {
                $.ajax({
                    type: "POST",
                    url: "relay_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.relay_preview').html(dataResponse);
                                $("#relay_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()

                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#relay_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#relay_preview .edit").addClass('display-hide');
                                        $('#relay_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#relay_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#relay_preview .edit").addClass('display-hide');
                                    $('#relay_preview .req_pullback').addClass('display-hide');
                                }, 2);    

                                $('#relay_preview #add_ip').html('');
                                var error_flag = true;
                                if (jsonvalue.app_ip.indexOf(";") > -1) {
                                    var myarray = jsonvalue.app_ip.split(';');
                                    for (var i = 0; i < myarray.length; i++)
                                    {
                                        var service = myarray[i].trim();
                                        if (i === 0) {
                                            $('#relay_preview #relay_ip').val(service);
                                        } else {
                                            var elmnt = '<div class="con"><input class="form-control" placeholder="Enter the IP Address [e.g.: 127.0.0.1]" type="text" name="relay_ip[]" id="relay_ip1" value="' + service + '" maxlength="12" style="margin: 20px 0px 20px 0px; width: 96%;"></div>';
                                            $('#relay_preview #add_ip').append(elmnt);
                                        }
                                    }
                                } else {
                                    $('#relay_preview #relay_ip').val(jsonvalue.app_ip);
                                }


                                $('#relay_preview #relay_app_name').val(jsonvalue.app_name);
                                $('#relay_preview #division').val(jsonvalue.division_name);
                                $('#relay_preview #os').val(jsonvalue.os);
                                if (jsonvalue.server_loc === "Other")
                                {
                                    $('#relay_preview #server_other').removeClass('display-hide');
                                    $('#relay_preview #server_loc_txt').val(jsonvalue.server_loc_other);
                                    $('#relay_preview #server_loc').val(jsonvalue.server_loc);
                                } else {
                                    $('#relay_preview #server_other').addClass('display-hide');
                                    $('#relay_preview #server_loc_txt').val('');
                                    $('#relay_preview #server_loc').val(jsonvalue.server_loc);
                                }
                                if (jsonvalue.staging_ip === "yes")
                                {
                                    $('#relay_preview #cert_div').addClass('display-hide');
                                    $('#relay_preview #cert_div1').removeClass('display-hide');
                                } else {
                                    $('#relay_preview #cert_div').removeClass('display-hide');
                                    $('#relay_preview #cert_div1').addClass('display-hide');
                                    $('#relay_preview #uploaded_filename').text(jsonvalue.uploaded_filename);
                                }

                                $('#relay_preview .edit').removeClass('display-hide');
                                $('#relay_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#relay_preview :button[type='button']").removeAttr('disabled');
                                $("#relay_preview #tnc").removeAttr('disabled');
                                $("#relay_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#relay_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#relay_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'IP') {
                $.ajax({
                    type: "POST",
                    url: "ip_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.ip_preview').html(dataResponse);
                                $("#ip_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()
                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#ip_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#ip_preview .edit").addClass('display-hide');
                                        $('#ip_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#ip_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#ip_preview .edit").addClass('display-hide');
                                    $('#ip_preview .req_pullback').addClass('display-hide');
                                }, 2);    

                                $("#ip_preview input:radio[value='" + jsonvalue.req_for + "']").prop("checked", true);
                                if (jsonvalue.req_for === "sms")
                                {
                                    $('#ip_preview #sms_account').removeClass('display-hide');
                                    $('#ip_preview #account_name').val(jsonvalue.account_name);
                                    $('#ip_preview #relay_app_div').addClass('display-hide');
                                    $('#ip_preview #relay_app_ip').addClass('display-hide');
                                    $('#ip_preview #relay_app').val('');
                                    $('#ip_preview #relay_old_ip').val('');
                                    $('#ip_preview #server_div').addClass('display-hide');
                                } else if (jsonvalue.req_for === "ldap")
                                {
                                    $('#ip_preview #ldap_account').removeClass('display-hide');
                                    $('#ip_preview #ldap_url').removeClass('display-hide');
                                    $('#ip_preview #ldap_id').removeClass('display-hide');
                                    $('#ip_preview #ldap_account_name').val(jsonvalue.account_name);
                                    $('#ip_preview #ldap_url').val(jsonvalue.ldap_url);
                                    $('#ip_preview #ldap_auth_allocate').val(jsonvalue.ldap_auth_allocate);
                                    $('#ip_preview #relay_app_div').addClass('display-hide');
                                    $('#ip_preview #relay_app_ip').addClass('display-hide');
                                    $('#ip_preview #relay_app').val('');
                                    $('#ip_preview #relay_old_ip').val('');
                                    $('#ip_preview #server_div').addClass('display-hide');
                                } else if (jsonvalue.req_for === "relay")
                                {
                                    $('#ip_preview #sms_account').addClass('display-hide');
                                    $('#ip_preview #account_name').val("");
                                    $('#ip_preview #relay_app_div').removeClass('display-hide');
                                    $('#ip_preview #relay_app_ip').removeClass('display-hide');
                                    $('#ip_preview #server_div').removeClass('display-hide');
                                    $('#ip_preview #relay_app').val(jsonvalue.app_name);
                                    $('#ip_preview #relay_old_ip').val(jsonvalue.app_ip);
                                    if (jsonvalue.server_loc === "Other")
                                    {
                                        $('#ip_preview #server_other').removeClass('display-hide');
                                        $('#ip_preview #server_loc_txt').val(jsonvalue.server_loc_other);
                                        $('#ip_preview #server_loc').val(jsonvalue.server_loc);
                                    } else {
                                        $('#ip_preview #server_other').addClass('display-hide');
                                        $('#ip_preview #server_loc_txt').val('');
                                        $('#ip_preview #server_loc').val(jsonvalue.server_loc);
                                    }
                                } else {
                                    $('#ip_preview #sms_account').addClass('display-hide');
                                    $('#ip_preview #account_name').val("");
                                    $('#ip_preview #relay_app_div').addClass('display-hide');
                                    $('#ip_preview #relay_app_ip').addClass('display-hide');
                                    $('#ip_preview #relay_app').val('');
                                    $('#ip_preview #relay_old_ip').val('');
                                    $('#ip_preview #server_div').addClass('display-hide');
                                }

                                if (jsonvalue.ip_type === 'addip') {
                                    //commented by MI on 27thjune19
                                    // $('#ip_preview #relay_app_ip').removeClass('display-hide');
                                    $('#ip_preview #change_ip_div').addClass('display-hide');
                                    $('#ip_preview #add_ip_div').removeClass('display-hide');
                                    $('#ip_preview #add_ip1').val(jsonvalue.add_ip1);
                                    $('#ip_preview #add_ip2').val(jsonvalue.add_ip2);
                                    $('#ip_preview #add_ip3').val(jsonvalue.add_ip3);
                                    $('#ip_preview #add_ip4').val(jsonvalue.add_ip4);
                                    $('#ip_preview #confirm').val(jsonvalue.ip_type);
                                    $('#ip_preview #ip_form_type').html("ADD IP");
                                } else {
                                    $('#ip_preview #relay_app_ip').addClass('display-hide');
                                    $('#ip_preview #relay_app_ip').addClass('display-hide');
                                    $('#ip_preview #add_ip_div').addClass('display-hide');
                                    $('#ip_preview #change_ip_div').removeClass('display-hide');
                                    if (jsonvalue.add_ip1.indexOf(";") > -1) {
                                        var myarray = jsonvalue.add_ip1.split(';');
                                        $('#ip_preview #old_ip1').val(myarray[0].trim());
                                        $('#ip_preview #new_ip1').val(myarray[1].trim());
                                    }

                                    if (jsonvalue.add_ip2 !== "")
                                    {

                                        if (jsonvalue.add_ip2.indexOf(";") > -1) {
                                            var myarray = jsonvalue.add_ip2.split(';');
                                            if (myarray[0].trim() !== "")
                                            {
                                                $('#ip_preview #newdiv1ad1').show();
                                                $('#ip_preview #old_ip2').val(myarray[0].trim());
                                                $('#ip_preview #new_ip2').val(myarray[1].trim());
                                            }

                                        }
                                    } else {
                                        $('#ip_preview #newdiv1ad1').hide();
                                        $('#ip_preview #old_ip2').val("");
                                        $('#ip_preview #new_ip2').val("");
                                    }
                                    if (jsonvalue.add_ip3 !== "")
                                    {

                                        if (jsonvalue.add_ip3.indexOf(";") > -1) {
                                            var myarray = jsonvalue.add_ip3.split(';');
                                            if (myarray[0].trim() !== "")
                                            {
                                                $('#ip_preview #newdiv2ad2').show();
                                                $('#ip_preview #old_ip3').val(myarray[0].trim());
                                                $('#ip_preview #new_ip3').val(myarray[1].trim());
                                            }
                                        } else
                                        {
                                            $('#ip_preview #newdiv2ad2').hide();
                                            $('#ip_preview #old_ip3').val("");
                                            $('#ip_preview #new_ip3').val("");
                                        }
                                    } else {


                                        $('#ip_preview #newdiv2ad2').hide();
                                        $('#ip_preview #old_ip3').val("");
                                        $('#ip_preview #new_ip3').val("");
                                    }
                                    if (jsonvalue.add_ip4 !== "")
                                    {

                                        if (jsonvalue.add_ip4.indexOf(";") > -1) {
                                            var myarray = jsonvalue.add_ip4.split(';');
                                            if (myarray[0].trim() !== "")
                                            {

                                                $('#ip_preview #old_ip4').val(myarray[0].trim());
                                                $('#ip_preview #new_ip4').val(myarray[1].trim());
                                                $('#ip_preview #newdiv3ad3').show();
                                            } else {

                                                $('#ip_preview #newdiv3ad3').hide();
                                                $('#ip_preview #old_ip4').val("");
                                                $('#ip_preview #new_ip4').val("");
                                            }
                                        }
                                    } else {
                                        $('#ip_preview #newdiv3ad3').hide();
                                        $('#ip_preview #old_ip4').val("");
                                        $('#ip_preview #new_ip4').val("");
                                    }
                                    $('#ip_preview #ip_form_type').html("CHANGE IP");
                                }
                                $('#ip_preview #confirm').val(jsonvalue.ip_type);
                                $('#ip_preview .edit').removeClass('display-hide');
                                $('#ip_preview #changeip_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#ip_preview :button[type='button']").removeAttr('disabled');
                                $("#ip_preview #tnc").removeAttr('disabled');
                                $("#ip_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#ip_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#ip_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }

                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'WEBCAST') {

                $.ajax({
                    type: "POST",
                    url: "webcast_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {
                            //
                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.webcast_preview').html(dataResponse);
                                $("#webcast_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData();
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

                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {

                                    $("#webcast_preview .edit,.save-changes").hide();
                                    //  $('#imappop_preview .edit').addClass('display-hide'); // line added by pr on 20thdec17

                                } else if (showEdit === 'true')
                                {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#webcast_preview .edit").addClass('display-hide');
                                        $('#webcast_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#webcast_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#webcast_preview .edit").addClass('display-hide');
                                    $('#webcast_preview .req_pullback').addClass('display-hide');
                                }, 2);  

                                $('#webcast_preview #req_for').val(jsonvalue.req_for);
                                $('#webcast_preview #req_for_span').html(jsonvalue.req_for);
                                if (jsonvalue.req_for === 'live') {

                                    $('#webcast_preview #web_live').removeClass('display-hide');
                                    $('#webcast_preview #web_demand').addClass('display-hide');
                                    $('#webcast_preview #web_demand').find('input, textarea, button, select').attr('disabled', 'true');
                                    $('#webcast_preview #web_live').find('input, textarea, button, select').removeAttr('disabled');

                                    $('#webcast_preview #event_coo_name').val(jsonvalue.event_coo_name);
                                    $('#webcast_preview #event_coo_email').val(jsonvalue.event_coo_email);
                                    $('#webcast_preview #event_coo_design').val(jsonvalue.event_coo_design);
                                    $('#webcast_preview #event_coo_mobile').val(jsonvalue.event_coo_mobile);
                                    $('#webcast_preview #event_coo_address').val(jsonvalue.event_coo_address);

                                    $('#webcast_preview #event_name_eng').val(jsonvalue.event_name_eng);
                                    $('#webcast_preview #event_name_hin').val(jsonvalue.event_name_hin);
                                    $('#webcast_preview #event_start_date1').val(jsonvalue.event_start);
                                    $('#webcast_preview #event_end_date1').val(jsonvalue.event_end);
                                    $('#webcast_preview #event_type').val(jsonvalue.event_type);
                                    if (jsonvalue.telecast === 'yes') {
                                        $("#webcast_preview #telecast_yes").prop('checked', true);
                                        $('#webcast_preview #telecast_yes_div').removeClass('display-hide');
                                        $('#webcast_preview #telecast_no_div').addClass('display-hide');
                                        $('#webcast_preview #vcid_div').addClass('display-hide');
                                        $('#webcast_preview #channel_name').val(jsonvalue.channel_name);
                                    } else {
                                        $("#webcast_preview #telecast_no").prop('checked', true);
                                        $('#webcast_preview #telecast_yes_div').addClass('display-hide');
                                        $('#webcast_preview #telecast_no_div').removeClass('display-hide');
                                        $('#webcast_preview #live_feed').val(jsonvalue.live_feed);

                                        if (jsonvalue.live_feed === 'Through VC') {
                                            $('#webcast_preview #vcid_div').removeClass('display-hide');
                                            $('#webcast_preview #vc_id').val(jsonvalue.vc_id);
                                        } else {
                                            $('#webcast_preview #vcid_div').addClass('display-hide');
                                        }
                                    }
                                    if (jsonvalue.conf_radio === 'conference') {
                                        $("#webcast_preview #conf_radio_yes").prop('checked', true);
                                    } else {
                                        $("#webcast_preview #conf_radio_no").prop('checked', true);
                                    }
                                    $('#webcast_preview #conf_name').val(jsonvalue.conf_name);
                                    $('#webcast_preview #conf_type').val(jsonvalue.conf_type);
                                    $('#webcast_preview #conf_city').val(jsonvalue.conf_city);
                                    $('#webcast_preview #conf_schedule').val(jsonvalue.conf_schedule);
                                    $('#webcast_preview #conf_session').val(jsonvalue.conf_session);
                                    $('#webcast_preview #conf_bw').val(jsonvalue.conf_bw);
                                    $('#webcast_preview #conf_provider').val(jsonvalue.conf_provider);

                                    if (jsonvalue.conf_event_hired === 'yes') {
                                        $("#webcast_preview #conf_event_hired_yes").prop('checked', true);
                                        $('#webcast_preview #flash_media_encoder').removeClass('display-hide');
                                    } else {
                                        $("#webcast_preview #conf_event_hired_no").prop('checked', true);
                                        $('#webcast_preview #flash_media_encoder').addClass('display-hide');
                                    }
                                    if (jsonvalue.conf_flash === 'yes') {
                                        $('#webcast_preview #conf_flash_no_div').addClass('display-hide');
                                        $("#webcast_preview #conf_flash_yes").prop('checked', true);
                                    } else {
                                        $('#webcast_preview #conf_flash_no_div').removeClass('display-hide');
                                        $("#webcast_preview #conf_flash_no").prop('checked', true);
                                        $('#webcast_preview #local_setup').val(jsonvalue.local_setup);
                                    }
                                    if (jsonvalue.conf_video === 'yes') {
                                        $("#webcast_preview #conf_video_yes").prop('checked', true);
                                    } else {
                                        $("#webcast_preview #conf_video_no").prop('checked', true);
                                    }

                                    if (jsonvalue.conf_video === 'yes') {
                                        $('#webcast_preview #conf_contact_div').removeClass('display-hide');
                                        $('#webcast_preview #conf_contact').val(jsonvalue.conf_contact);
                                    } else if (jsonvalue.conf_video === 'no') {
                                        $('#webcast_preview #conf_contact_div').addClass('display-hide');
                                        $('#webcast_preview #conf_contact').val('');
                                    }
                                    if (jsonvalue.hall_type === 'multiple') {
                                        $('#webcast_preview #hall_number').removeClass('display-hide');
                                        $('#webcast_preview #hall_number').css('display', 'inline');
                                        $('#webcast_preview #hall_number').val(jsonvalue.hall_number);
                                        $('#webcast_preview #hall_multiple').prop('checked', true);
                                    } else if (jsonvalue.hall_type === 'single') {
                                        $('#webcast_preview #hall_number').addClass('display-hide');
                                        $('#webcast_preview #hall_number').css('display', 'none');
                                        $('#webcast_preview #hall_number').val("");
                                        $('#webcast_preview #hall_single').prop('checked', true);
                                    }
                                    if (jsonvalue.webcast_uploaded_files !== null && jsonvalue.webcast_uploaded_files !== "" && jsonvalue.webcast_uploaded_files !== undefined) {
                                        $('#webcast_preview #cert_div').removeClass('display-hide');
                                        var fileString = "";
                                        $.each(jsonvalue.webcast_uploaded_files, function (key, value) {
                                            fileString += '<a href="download_webcast?doc_name=' + key + '"<span>' + key + '</span></a><br/>';
                                        });
                                        $("#webcast_preview #webcast_files").append(fileString);
                                    } else {
                                        $('#webcast_preview #cert_div').addClass('display-hide');
                                    }
                                } else if (jsonvalue.req_for === 'demand') {

                                    $('#webcast_preview #web_live').addClass('display-hide');
                                    $('#webcast_preview #web_demand').removeClass('display-hide');
                                    $('#webcast_preview #web_live').find('input, textarea, button, select').attr('disabled', 'true');
                                    $('#webcast_preview #web_demand').find('input, textarea, button, select').removeAttr('disabled');
                                    $('#webcast_preview #event_size').val(jsonvalue.event_size);
                                    $('#webcast_preview #event_no').val(jsonvalue.event_no);
                                    $('#webcast_preview #media_format').val(jsonvalue.media_format);
                                }
                                if (jsonvalue.payment === 'yes') {
                                    $("#webcast_preview #payment_yes").prop('checked', true);
                                    $('#webcast_preview #payment_div').removeClass('display-hide');
                                    $('#webcast_preview #cheque_no').val(jsonvalue.cheque_no);
                                    $('#webcast_preview #cheque_amount').val(jsonvalue.cheque_amount);
                                    $('#webcast_preview #cheque_date1').val(jsonvalue.cheque_date);
                                    $('#webcast_preview #bank_name').val(jsonvalue.bank_name);
                                } else {
                                    $("#webcast_preview #payment_no").prop('checked', true);
                                    $('#webcast_preview #payment_div').addClass('display-hide');
                                }
                                $('#webcast_preview #remarks').val(jsonvalue.remarks);
                                $('#webcast_preview #fwd_ofc_name').val(jsonvalue.fwd_ofc_name);
                                $('#webcast_preview #fwd_ofc_email').val(jsonvalue.fwd_ofc_email);
                                $('#webcast_preview #fwd_ofc_tel').val(jsonvalue.fwd_ofc_tel);
                                $('#webcast_preview #fwd_ofc_mobile').val(jsonvalue.fwd_ofc_mobile);
                                $('#webcast_preview #fwd_ofc_design').val(jsonvalue.fwd_ofc_design);
                                $('#webcast_preview #fwd_ofc_add').val(jsonvalue.fwd_ofc_add);


                                $('#webcast_preview .edit').removeClass('display-hide'); // commented by pr on 20thdec17, uncommented by pr on 29thjan18
                                $('#webcast_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#webcast_preview :button[type='button']").removeAttr('disabled');
                                $("#webcast_preview #tnc").removeAttr('disabled');
                                $("#webcast_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#webcast_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#webcast_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }
                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === 'CENTRALUTM') {

                $.ajax({
                    type: "POST",
                    url: "firewall_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {
                            console.log("dataresponse@@@@@@@" + dataResponse);
                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.centralutm_preview').html(dataResponse);
                                $("#centralutm_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()
                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    console.log("inside firewall showedit false ");
                                    //$("#centralutm_preview .edit,.save-changes").hide();

                                    // above line modified by pr on 1stnov18
                                    $("#centralutm_preview .edit").addClass('display-hide');
                                    $('#centralutm_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            


                                } else if (showEdit === 'true')
                                {

                                    console.log("inside firewall showedit true ");
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {

                                        $("#centralutm_preview .edit").addClass('display-hide');
                                        $('#centralutm_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#centralutm_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#centralutm_preview .edit").addClass('display-hide');
                                    $('#centralutm_preview .req_pullback').addClass('display-hide');
                                }, 2); 



                                $('#centralutm_preview #add_sourceip').html('');
                                $('#centralutm_preview #add_designationip').html('');
                                $('#centralutm_preview #add_service').html('');
                                $('#centralutm_preview #add_port').html('');
                                $('#centralutm_preview #add_action').html('');
                                $('#centralutm_preview #add_time').html('');
                                if (jsonvalue.sourceIP.indexOf(";") > -1) {
                                    var myarray = jsonvalue.sourceIP.split(';');
                                    for (var i = 0; i < myarray.length; i++)
                                    {
                                        var sourceIP = myarray[i].trim();
                                        if (i === 0) {

                                            $('#centralutm_preview #firewall_sourceip_1').val(sourceIP);
                                        } else {

                                            //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 87%;"><span id="domain1" style="position: relative;width: 65px;float: right;margin-right: 52px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + jsonvalue.dns_type + '.in</span>' + '<button type="button" class="rmvbox1" style="position: relative;float: right;margin-top: -49px;font-size: xx-large;background: none;border: none;" title="Remove Row" id="dns_rmv1" name="dns_rmv1"><i class="fa fa-remove"></i></button></div>';
                                            var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1" placeholder="Enter the Source IP [e.g.: demo.nic.in or demo.gov.in]" type="text" name="firewall_sourceip[]" value="' + sourceIP + '" maxlength="100" style="margin: 20px 0px 20px 0px; "></div>';
                                            $('#centralutm_preview #add_sourceip').append(elmnt);
                                        }
                                    }
                                } else {
                                    $('#centralutm_preview #firewall_sourceip_1').val(jsonvalue.sourceIP);
                                }

                                if (jsonvalue.destinationIP.indexOf(";") > -1) {
                                    var myarray = jsonvalue.destinationIP.split(';');
                                    for (var i = 0; i < myarray.length; i++)
                                    {
                                        var destinationIP = myarray[i].trim();
                                        if (i === 0) {

                                            $('#centralutm_preview #firewall_destinationip_1').val(destinationIP);
                                        } else {

                                            //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 87%;"><span id="domain1" style="position: relative;width: 65px;float: right;margin-right: 52px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + jsonvalue.dns_type + '.in</span>' + '<button type="button" class="rmvbox1" style="position: relative;float: right;margin-top: -49px;font-size: xx-large;background: none;border: none;" title="Remove Row" id="dns_rmv1" name="dns_rmv1"><i class="fa fa-remove"></i></button></div>';
                                            var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1" placeholder="Enter the Destination IP [e.g.: demo.nic.in or demo.gov.in]" type="text" name="firewall_destinationip[]" value="' + destinationIP + '" maxlength="100" style="margin: 20px 0px 20px 0px; "></div>';
                                            $('#centralutm_preview #add_designationip').append(elmnt);
                                        }
                                    }
                                } else {
                                    $('#centralutm_preview #firewall_destinationip_1').val(jsonvalue.destinationIP);
                                }

                                if (jsonvalue.service != null)
                                {
                                    if (jsonvalue.service.indexOf(";") > -1) {
                                        var myarray = jsonvalue.service.split(';');
                                        for (var i = 0; i < myarray.length; i++)
                                        {
                                            var service = myarray[i].trim();
                                            if (i === 0) {

                                                $('#centralutm_preview #firewall_service_1').val(service);
                                            } else {
                                                var tcp_selected = "", udp_selected = "", icmp_selected = "";
                                                if (service === "tcp")
                                                {
                                                    tcp_selected = "selected = selected";

                                                }
                                                if (service === "udp")
                                                {
                                                    udp_selected = "selected = selected";

                                                }
                                                if (service === "icmp")
                                                {
                                                    icmp_selected = "selected = selected";

                                                }

                                                var elmnt = '<div class="con"><select class="form-control ser" id="" name="firewall_service[]" style="margin: 20px 0px 20px 0px; ">' +
                                                        "<option value=''>-SELECT-</option><option value='tcp' " + tcp_selected + ">TCP</option>" +
                                                        "<option value='udp' selected ='" + udp_selected + "' " + udp_selected + ">UDP</option>" +
                                                        "<option value='icmp' selected ='" + icmp_selected + "' " + icmp_selected + ">ICMP</option>" +
                                                        "</select></div>";
                                                $('#centralutm_preview #add_service').append(elmnt);
                                            }
                                        }
                                    } else {
                                        $('#centralutm_preview #firewall_service_1').val(jsonvalue.service);
                                    }
                                }

                                if (jsonvalue.ports.indexOf(";") > -1) {

                                    var myarray = jsonvalue.ports.split(';');
                                    for (var i = 0; i < myarray.length; i++)
                                    {
                                        var ports = myarray[i].trim();
                                        if (i === 0) {
                                            $('#centralutm_preview #firewall_port_1').val(ports);
                                        } else {
                                            var elmnt = '<div class="con"><input class="form-control class_name_loc1" placeholder="Enter the firewall port" value="' + ports + '" name="firewall_port[]" id="dns_loc" maxlength="100" style="margin: 20px 0px 20px 0px;"></div>';
                                            $('#centralutm_preview #add_port').append(elmnt);
                                        }
                                    }
                                } else {

                                    $('#centralutm_preview #firewall_port_1').val(jsonvalue.ports);
                                }

                                console.log("jsonvalue.action." + jsonvalue.action)
                                if (jsonvalue.action.indexOf(";") > -1) {

                                    var myarray = jsonvalue.action.split(';');
                                    for (var i = 0; i < myarray.length; i++)
                                    {
                                        var action = myarray[i].trim();
                                        if (i === 0) {
                                            $('#centralutm_preview #firewall_action_1').val(action);
                                        } else {

                                            var permit_selected = "", deny_selected = "";
                                            if (action === "permit")
                                            {
                                                permit_selected = "selected";

                                            }
                                            if (action === "deny")
                                            {
                                                deny_selected = "selected";

                                            }
                                            var elmnt = '<div class="con"><select class="form-control ser" id="" name="firewall_action[]" style="margin: 20px 0px 20px 0px; ">' +
                                                    "<option value=''>-SELECT-</option><option value='permit' " + permit_selected + ">Permit</option>" +
                                                    "<option value='deny' " + deny_selected + ">Deny</option>" +
                                                    "</select></div>";
                                            $('#centralutm_preview #add_action').append(elmnt);

                                        }

                                    }
                                } else {

                                    $('#centralutm_preview #firewall_action_1').val(jsonvalue.action);
                                }

                                if (jsonvalue.timeperiod.indexOf(";") > -1) {

                                    var myarray = jsonvalue.timeperiod.split(';');
                                    for (var i = 0; i < myarray.length; i++)
                                    {
                                        var timeperiod = myarray[i].trim();
                                        if (i === 0) {
                                            $('#centralutm_preview #firewall_timePeriod_1').val(timeperiod);
                                        } else {

                                            var elmnt = '<div class="con"><input class="form-control class_name_loc1" placeholder="Enter the time period" value="' + timeperiod + '" name="firewall_time[]" id="dns_loc" maxlength="100" style="margin: 20px 0px 20px 0px; width:85%"></div>';
                                            $('#centralutm_preview #add_time').append(elmnt);
                                            //$('#centralutm_preview #add_time').append(elmnt);
                                        }
                                    }
                                } else {

                                    $('#centralutm_preview #firewall_timePeriod_1').val(jsonvalue.timeperiod);
                                }



                                $('#centralutm_preview #remarks').val(jsonvalue.prvw_purpose);
                                $('#centralutm_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#centralutm_preview :button[type='button']").removeAttr('disabled');
                                $("#centralutm_preview #tnc").removeAttr('disabled');
                                $("#centralutm_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#centralutm_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#centralutm_preview_form').modal({backdrop: 'static', keyboard: false});
                            }
                        });
            } else if (whichform === 'VPN' || whichform === 'VPNRENEW' || whichform === 'VPNADD' || whichform === 'VPNADD' || whichform === 'VPNSURRENDER' || whichform === 'VPNDELETE') { //added by nikki on 26th jun 2019
                whichform = 'vpn';
                $.ajax({
                    type: "POST",
                    url: "vpn_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.vpn_preview').html(dataResponse);
                                $("#vpn_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()
                                showEdit = "false";
                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#vpn_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#vpn_preview .edit").addClass('display-hide');
                                        $('#vpn_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {
                                        $("#vpn_preview .edit,.save-changes").hide();
                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#vpn_preview .edit").addClass('display-hide');
                                    $('#vpn_preview .req_pullback').addClass('display-hide');
                                }, 2); 


                                if (jsonvalue.user_type === "vpn_renew" || jsonvalue.user_type === "change_add" || jsonvalue.user_type === "vpn_surrender" || jsonvalue.user_type === "vpn_delete") {
                                    $('#vpn_preview #api_output_div').removeClass('display-hide');
                                    $('#vpn_preview #api_output_div').html(jsonvalue.vpn_api_output);
                                } else {
                                    $('#vpn_preview #api_output_div').addClass('display-hide');
                                }

                                if (jsonvalue.prvw_remarks != "")
                                {

                                    $('#vpn_preview #remarks').val(jsonvalue.prvw_remarks);
                                    $('#vpn_preview #remarks_div').removeClass("display-hide");
                                } else {

                                    $('#vpn_preview #remarks_div').addClass("display-hide");
                                }

                                if (jsonvalue.prvw_vpncoord != "na" && jsonvalue.prvw_vpncoord != "")

                                {
                                    $('#vpn_preview #sel_coo1').removeClass("display-hide");
                                    $('#vpn_preview #vpn_coo').val(jsonvalue.prvw_vpncoord);
                                } else {

                                    $('#vpn_preview #sel_coo1').addClass("display-hide");
                                    $('#vpn_preview #vpn_coo').val("");
                                }

                                $('#vpn_preview #vpn_coo').val(jsonvalue.prvw_vpncoord);
                                if (jsonvalue.user_type === "vpn_delete") {
                                    var content = "<table class='table table-bordered table-hover'>"
                                    content += '<th>IP Address</th><th>Application URL</th><th>Destination Port</th><th>Server Location</th>';
                                    $.each(jsonvalue.vpn_data_all, function (key, value) {
                                        //   console.log("== " + key + ": " + value);
                                        content += '<tr>';
                                        $.each(value, function (key1, value1) {
                                            // console.log("++" + key1 + ": " + value1);
                                            content += '<td>' + value1 + '</td>';
                                        });
                                        content += '</tr>';
                                    });
                                    content += "</table>"
                                    //console.log('CONTENT: ' + content);
                                    $('#vpn_preview #vpn_data_filled').html('');
                                    $('#vpn_preview #vpn_data_filled').append(content);
                                } else if (jsonvalue.user_type !== "vpn_renew") {
                                    var content = "<table class='table table-bordered table-hover'>"
                                    content += '<th>IP Type</th><th>IP Address</th><th>Application URL</th><th>Destination Port</th><th>Server Location</th>';
                                    $.each(jsonvalue.vpn_data_all, function (key, value) {
                                        console.log("== " + key + ": " + value);
                                        content += '<tr>';
                                        $.each(value, function (key1, value1) {
                                            console.log("++" + key1 + ": " + value1);
                                            if (key1 === 'ip1' || key1 === 'ip2' || key1 === 'ip3' || key1 === 'server_loc' || key1 === 'server_loc_txt' || key1 === 'deleted_by' || key1 === 'id' || key1 === 'regno') {

                                            } else {
                                                if (key1 === 'deleted_flag') {

                                                } else {
                                                    content += '<td>' + value1 + '</td>';
                                                }
                                            }
                                        });
                                        content += '</tr>';
                                    });
                                    content += "</table>"
                                    console.log('CONTENT: ' + content);
                                    $('#vpn_preview #vpn_data_filled').html('');
                                    $('#vpn_preview #vpn_data_filled').append(content);
                                }
                                $('#vpn_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#vpn_preview :button[type='button']").removeAttr('disabled');
                                $("#vpn_preview #tnc").removeAttr('disabled');
                                $("#vpn_preview #tnc_div").addClass('display-hide');
                                $("#vpn_preview #confirm").addClass('display-hide');
                                $("#vpn_preview #closebtn").removeClass('display-hide').html('Close').show();
                                //if (curr_status === data.role) {
                                    createdombtn("#vpn_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#vpn_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }
                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === "EMAILACTIVATE") { //added by nikki on 26th jun 2019
                whichform = 'email_act';
                $.ajax({
                    type: "POST",
                    url: "email_act_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.email_act_preview').html(dataResponse);
                                $("#email_act_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()

                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#email_act_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18                            
                                    if (comingFrom === 'admin')
                                    {
                                        $("#email_act_preview .edit").addClass('display-hide');
                                        $('#email_act_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {

                                        $("#email_act_preview .edit").removeClass('display-hide');
                                        $('#email_act_preview .save-changes').removeClass('display-hide');
                                        //$("#email_act_preview .edit,.save-changes").removeClass('display-hide');

                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#email_act_preview .edit").addClass('display-hide');
                                    $('#email_act_preview .req_pullback').addClass('display-hide');
                                }, 2);                                 
                                
                                if (jsonvalue.prvw_emp_type === 'emp_regular') {
                                    $('#email_act_preview #single_emp_type1').prop('checked', 'checked');
                                    $('#email_act_preview #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                                    $('#email_act_preview #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                                } else if (jsonvalue.prvw_emp_type === 'consultant') {
                                    $('#email_act_preview #single_emp_type2').prop('checked', 'checked');
                                    $('#email_act_preview #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                                    $('#email_act_preview #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                                } else {
                                    $('#email_act_preview #single_emp_type3').prop('checked', 'checked');
                                    $('#email_act_preview #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                                    $('#email_act_preview #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                                }
                                if (jsonvalue.prvw_emp_type !== "emp_regular")
                                {
                                    if (jsonvalue.prvw_file_name !== "")
                                    {
                                        $('#email_act_preview #cert_div').removeClass("display-hide");
                                        $('#email_act_preview #cert_div1').removeClass("display-hide");
                                        $('#email_act_preview #uploaded_filename').text(jsonvalue.prvw_file_name);
                                    }
                                } else {
                                    $('#email_act_preview #cert_div1').addClass("display-hide");
                                    $('#email_act_preview #cert_div').addClass('display-hide');
                                }

                                $('#email_act_preview #act_email1').val(jsonvalue.prvw_pemail1);
                                $('#email_act_preview #email_single_dor1').val(jsonvalue.prvw_dor);


                                $('#email_act_preview .edit').removeClass('display-hide');
                                $('#email_act_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#email_act_preview :button[type='button']").removeAttr('disabled');
                                $("#email_act_preview #tnc").removeAttr('disabled');
                                $("#email_act_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#email_act_preview", approve_btn, reject_btn, raise_btn);
                                //}
                                $('#email_act_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }
                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            } else if (whichform === "EMAILDEACTIVATE") { //added by nikki on 26th jun 2019
                whichform = 'email_deact';
                $.ajax({
                    type: "POST",
                    url: "email_deact_preview",
                    dataType: 'html', complete: function () {
                        $('.loader').hide();
                    }
                })
                        .done(function (dataResponse) {

                            if (dataResponse.trim() != '' && dataResponse != null) {

                                $('.email_deact_preview').html(dataResponse);
                                $("#email_deact_preview_form .modal-title").html("Preview for " + ref_no);
                                loadFormData()

                                if (showEdit === 'false') // to show /hide edit on preview based on stat_type
                                {
                                    $("#email_deact_preview .edit,.save-changes").hide();
                                } else if (showEdit === 'true') {
                                    // start, code added by pr on 25thjan18    

                                    if (comingFrom === 'admin')
                                    {
                                        $("#email_deact_preview .edit").addClass('display-hide');
                                        $('#email_deact_preview .save-changes').addClass('display-hide'); // line added by pr on 20thdec17                                                            
                                    } else
                                    {

                                        $("#email_deact_preview .edit").removeClass('display-hide');
                                        $('#email_deact_preview .save-changes').removeClass('display-hide');
                                        //$("#email_act_preview .edit,.save-changes").removeClass('display-hide');

                                    }
                                    // end, code added by pr on 25thjan18
                                }
                                setTimeout(function(){
                                    $("#email_deact_preview .edit").addClass('display-hide');
                                    $('#email_deact_preview .req_pullback').addClass('display-hide');
                                }, 2); 


                                $('#email_deact_preview #deact_email1').val(jsonvalue.prvw_pemail1);



                                $('#email_deact_preview .edit').removeClass('display-hide');
                                $('#email_deact_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                $("#email_deact_preview :button[type='button']").removeAttr('disabled');
                                $("#email_deact_preview #tnc").removeAttr('disabled');
                                $("#email_deact_preview #tnc_div").addClass('display-hide');
                                //if (curr_status === data.role) {
                                    createdombtn("#email_deact_preview", approve_btn, reject_btn, raise_btn);
                                //}


                                $('#email_deact_preview_form').modal({backdrop: 'static', keyboard: false});
                            } else {
                                console.log('Preview is not loading');
                            }
                        })
                        .fail(function () {
                            console.log('This isn\'t LIT');
                        });
            }

            function loadFormData() {
                $('#' + whichform.toLowerCase() + '_preview #user_name').val(jsonvalue.prvw_offname);
                $('#' + whichform.toLowerCase() + '_preview #user_design').val(jsonvalue.prvw_desig);
                $('#' + whichform.toLowerCase() + '_preview #user_address').val(jsonvalue.prvw_address);
                $('#' + whichform.toLowerCase() + '_preview #user_city').val(jsonvalue.prvw_city);
                $('#' + whichform.toLowerCase() + '_preview #user_state').val(jsonvalue.prvw_addstate);
                $('#' + whichform.toLowerCase() + '_preview #user_pincode').val(jsonvalue.prvw_pin);
                $('#' + whichform.toLowerCase() + '_preview #user_ophone').val(jsonvalue.prvw_ophone);
                $('#' + whichform.toLowerCase() + '_preview #user_rphone').val(jsonvalue.prvw_rphone);

                // alert("jsonvalue.role"+jsonvalue.role)
                if (jsonvalue.role == "user" || jsonvalue.role == "ca")
                {
                    var startMobile = jsonvalue.prvw_mobile.substring(0, 3);
                    var endMobile = jsonvalue.prvw_mobile.substring(10, 13);
                    $('#' + whichform.toLowerCase() + '_preview #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                } else {
                    $('#' + whichform.toLowerCase() + '_preview #user_mobile').val(jsonvalue.prvw_mobile);

                }

                $('#' + whichform.toLowerCase() + '_preview #user_email').val(jsonvalue.prvw_authemail);
                $('#' + whichform.toLowerCase() + '_preview #user_empcode').val(jsonvalue.prvw_empcode);
                $('#' + whichform.toLowerCase() + '_preview #user_employment').val(jsonvalue.prvw_employment);
                $('#' + whichform.toLowerCase() + '_preview #hod_name').val(jsonvalue.hod_name);
                $('#' + whichform.toLowerCase() + '_preview #hod_email').val(jsonvalue.hod_email);
                $('#' + whichform.toLowerCase() + '_preview #hod_tel').val(jsonvalue.hod_tel);
                $('#' + whichform.toLowerCase() + '_preview #hod_mobile').val(jsonvalue.hod_mobile);
                $('#' + whichform.toLowerCase() + '_preview #ca_design').val(jsonvalue.ca_design);
//                        if (jsonvalue.prvw_department == "Department of Electronics and Information Technology (Deity), MyGov Project")
//                        {
//
//
//                            $('#wifi_preview #forwarding_div').hide();
//                        } else {
//
//                            $('#wifi_preview #forwarding_div').show();
//                        }


                if (jsonvalue.prvw_employment === 'Central' || jsonvalue.prvw_employment === 'UT') {
                    $('#' + whichform.toLowerCase() + '_preview #central_div').show();
                    $('#' + whichform.toLowerCase() + '_preview #state_div').hide();
                    $('#' + whichform.toLowerCase() + '_preview #other_div').hide();
                    $('#' + whichform.toLowerCase() + '_preview #other_text_div').addClass("display-hide");
                    var select = $('#' + whichform.toLowerCase() + '_preview #min');
                    select.find('option').remove();
                    $('<option>').val(jsonvalue.prvw_ministry).text(jsonvalue.prvw_ministry).appendTo(select);
                    var select = $('#' + whichform.toLowerCase() + '_preview #dept');
                    select.find('option').remove();
                    $('<option>').val(jsonvalue.prvw_department).text(jsonvalue.prvw_department).appendTo(select);
                    if (jsonvalue.prvw_department.toString().toLowerCase() === 'other') {
                        $('#' + whichform.toLowerCase() + '_preview #other_text_div').removeClass("display-hide");
                        $('#' + whichform.toLowerCase() + '_preview #other_dept').val(jsonvalue.prvw_state_dept);
                    }
                } else if (jsonvalue.prvw_employment === 'State') {

                    $('#' + whichform.toLowerCase() + '_preview #central_div').hide();
                    $('#' + whichform.toLowerCase() + '_preview #state_div').show();
                    $('#' + whichform.toLowerCase() + '_preview #other_div').hide();
                    $('#' + whichform.toLowerCase() + '_preview #other_text_div').addClass("display-hide");

                    $('#' + whichform.toLowerCase() + '_preview #stateCode').val(jsonvalue.prvw_state);
                    var select = $('#' + whichform.toLowerCase() + '_preview #stateCode');
                    select.find('option').remove();

                    $('<option>').val(jsonvalue.prvw_state).text(jsonvalue.prvw_state).appendTo(select);
                    var select = $('#' + whichform.toLowerCase() + '_preview #Smi');
                    select.find('option').remove();
                    $('<option>').val(jsonvalue.prvw_department).text(jsonvalue.prvw_department).appendTo(select);
                    if (jsonvalue.prvw_department.toString().toLowerCase() === 'other') {
                        $('#' + whichform.toLowerCase() + '_preview #other_text_div').removeClass("display-hide");
                        $('#' + whichform.toLowerCase() + '_preview #other_dept').val(jsonvalue.prvw_state_dept);
                    }

                } else if (jsonvalue.prvw_employment === 'Others' || jsonvalue.prvw_employment === "Psu" || jsonvalue.prvw_employment === "Const" || jsonvalue.prvw_employment === "Nkn" || jsonvalue.prvw_employment === "Project") {
                    $('#' + whichform.toLowerCase() + '_preview #central_div').hide();
                    $('#' + whichform.toLowerCase() + '_preview #state_div').hide();
                    $('#' + whichform.toLowerCase() + '_preview #other_div').show();
                    $('#' + whichform.toLowerCase() + '_preview #other_text_div').addClass("display-hide");
                    var select = $('#' + whichform.toLowerCase() + '_preview #Org');
                    select.find('option').remove();
                    $('<option>').val(jsonvalue.prvw_org).text(jsonvalue.prvw_org).appendTo(select);
                    if (jsonvalue.prvw_org.toString().toLowerCase() === 'other') {
                        $('#' + whichform.toLowerCase() + '_preview #other_text_div').removeClass("display-hide");
                        $('#' + whichform.toLowerCase() + '_preview #other_dept').val(jsonvalue.prvw_state_dept);
                    }

                } else {
                    $('#' + whichform.toLowerCase() + '_preview #central_div').hide();
                    $('#' + whichform.toLowerCase() + '_preview #state_div').hide();
                    $('#' + whichform.toLowerCase() + '_preview #other_div').hide();
                }
            }

        })
                .fail(function () {
                    console.log('error');
                });
    });
    $(document).on('change', '#user_employment', function () {
        //$('#user_employment').change(function () {
        console.log("user employment change select")
        $('#other_text_div').addClass("display-hide");
        $('#other_dept').val("");
        var field_form = $(this).closest('form').attr('id');
        console.log("form" + field_form)
        var orgname = $("#" + field_form + " #user_employment").val();
        //console.log(orgname+"orgname")
        if (orgname === null || orgname === "") {
            $('#useremployment_error').html("Select organization category");
        } else {
            var regn = "^[a-zA-Z]{1,8}$";
            if (orgname.match(regn)) {

                $('#useremployment_error').html("");
                if (orgname === "Central" || orgname === "UT") {
                    $("#" + field_form + " #central_div").show();
                    $("#" + field_form + " #state_div").hide();
                    $("#" + field_form + " #other_div").hide();
                    $("#" + field_form + " #state_div,#other_div").find('input,select').val('');
                    $.get('centralMinistry', {
                        orgType: orgname
                    }, function (response) {
                        var select = $("#" + field_form + " #min");
                        select.find('option').remove();
                        $('<option>').val("").text("-SELECT-").appendTo(select);
                        $.each(response, function (index, value) {
                            $('<option>').val(value).text(value).appendTo(select);
                        });
                    });
                } else if (orgname === "State") {
                    $("#" + field_form + " #central_div").hide();
                    $("#" + field_form + " #state_div").show();
                    $("#" + field_form + " #other_div").hide();
                    $("#" + field_form + " #central_div,#other_div").find('input,select').val('');
                    $("#" + field_form + " #stateCode").val("Delhi");
                    $.get('centralMinistry', {
                        orgType: orgname
                    }, function (response) {
                        var select = $("#" + field_form + " #stateCode");
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
                    $("#" + field_form + " #central_div").hide();
                    $("#" + field_form + " #state_div").hide();
                    $("#" + field_form + " #other_div").show();
                    $("#" + field_form + " #central_div,#state_div").find('input,select').val('');
                    $.get('centralMinistry', {
                        orgType: orgname
                    }, function (response) {
                        var select = $("#" + field_form + " #Org");
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
                    $("#" + field_form + " #central_div").hide();
                    $("#" + field_form + " #state_div").hide();
                    $("#" + field_form + " #other_div").hide();
                }
            } else {
                $("#" + field_form + " #useremployment_error").html("Select organization category in correct format");
            }
        }
    });
    $(document).on('change', '#min', function () {
        var field_form = $(this).closest('form').attr('id')
        console.log("form" + field_form)
        //$('#min').change(function () {

        var orgname = $("#" + field_form + " #min").val();
        $.get('centralDepartment', {
            depType: orgname
        }, function (response) {

            var select = $("#" + field_form + " #dept");
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
    $(document).on('change', '#stateCode', function () {
        //$('#stateCode').change(function () {

        var field_form = $(this).closest('form').attr('id')
        console.log("form" + field_form)
        var orgname = $("#" + field_form + " #stateCode").val();
        $.get('centralDepartment', {
            depType: orgname
        }, function (response) {
            var select = $("#" + field_form + " #Smi");
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
    $(document).on('change', '#dept', function () {

        var field_form = $(this).closest('form').attr('id')
        console.log("field_form" + field_form)
        //$('#dept').change(function () {
        var dept = $("#" + field_form + " #dept").val();

        if (dept != "Department of Electronics and Information Technology (Deity), MyGov Project" && dept != 'other')
        {
            //$('#wifi_preview_tab #forwarding_div').show();
            $("#" + field_form + " #govdept").html("<input type ='hidden' class='form-control' id='dept_mygov' maxlength='50' value='" + dept + "'/>")
            $("#" + field_form + " #other_text_div").addClass("display-hide");
        } else if (dept == "Department of Electronics and Information Technology (Deity), MyGov Project" && dept != 'other')
        {
            // $('#wifi_preview_tab #forwarding_div').hide();
            $("#" + field_form + " #govdept").html("<input type ='hidden' class='form-control' id='dept_mygov' maxlength='50' value='" + dept + "'/>")
            $("#" + field_form + " #other_text_div").addClass("display-hide");
        } else if (dept.toString().toLowerCase() == 'other')
        {

            $("#" + field_form + " #other_text_div").removeClass("display-hide");
        }
    });
    $(document).on('change', '#Smi', function () {

        //$('#Smi').change(function () {
        var field_form = $(this).closest('form').attr('id')
        var Smi = $("#" + field_form + " #Smi").val();
        if (Smi.toString().toLowerCase() == 'other')
        {
            $("#" + field_form + " #other_text_div").removeClass("display-hide");
        } else
        {
            $("#" + field_form + " #other_text_div").addClass("display-hide");
        }
    });
    $(document).on('change', '#Org', function () {
        var field_form = $(this).closest('form').attr('id')
        //$('#Org').change(function () {
        var Org = $("#Org").val();
        if (Org.toString().toLowerCase() == 'other')
        {
            $("#" + field_form + " #other_text_div").removeClass("display-hide");
        } else
        {
            $("#" + field_form + " #other_text_div").addClass("display-hide");
        }

    });
});

function current_status(status_val) {
    var curr_status = "";
    switch (status_val) {
        case "Pending with RO/Nodal/FO":
            curr_status = 'ca';
            break;
        case "Pending with Coordinator":
            curr_status = 'co';
            break;
        case "Pending with Support":
            curr_status = 'sup';
            break;
        case "Pending with Admin":
            curr_status = 'admin';
            break;
        default:
            curr_status = 'a';
            break;
    }
    return curr_status;
}
function checkDuplicates() {
    // get all input elements
    var $elems = $('.class_name');
    // we store the inputs value inside this array
    var values = [];
    // return this
    var isDuplicated = false;
    // loop through elements
    $elems.each(function () {
        //If value is empty then move to the next iteration.
        if (!this.value)
            return true;
        //If the stored array has this value, break from the each method

        if (values.indexOf(this.value) !== -1) {
            isDuplicated = true;
            return false;
        }
        // store the value
        values.push(this.value);
    });
    return isDuplicated;
}
function checkDuplicatesprvw() {

// get all input elements
    var $elems = $('.class_name_prvw');
    // we store the inputs value inside this array
    var values = [];
    // return this
    var isDuplicated = false;
    // loop through elements
    $elems.each(function () {
        //If value is empty then move to the next iteration.
        if (!this.value)
            return true;
        //If the stored array has this value, break from the each method
        if (values.indexOf(this.value) !== -1) {
            isDuplicated = true;
            return false;
        }
        // store the value
        values.push(this.value);
    });
    return isDuplicated;
}
// below function added by pr on 19thjan18
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
function checkDuplicatesDNS(classname) {
// get all input elements
    var $elems = $(classname);
    // we store the inputs value inside this array
    var values = [];
    // return this
    var isDuplicated = false;
    // loop through elements
    $elems.each(function () {
        console.log('IN FUNC: ' + this.value);
        //If value is empty then move to the next iteration.
        if (!this.value)
            return true;
        //If the stored array has this value, break from the each method

        if (values.indexOf(this.value) !== -1) {
            isDuplicated = true;
            return false;
        }
        // store the value
        values.push(this.value);
    });
    return isDuplicated;
}

function checkDupDNSPrvw(classname) {
// get all input elements
    var $elems = $(classname);
    // we store the inputs value inside this array
    var values = [];
    // return this
    var isDuplicated = false;
    // loop through elements
    $elems.each(function () {
        //If value is empty then move to the next iteration.
        if (!this.value)
            return true;
        //If the stored array has this value, break from the each method

        if (values.indexOf(this.value) !== -1) {
            isDuplicated = true;
            return false;
        }
        // store the value
        values.push(this.value);
    });
    return isDuplicated;
}
$(document).on('click', '.fupload_2', function () {
    $('#file_cert_err').text("");
    $('.fileinput-filename').text("");
    //  $('.fileinput-exists').text("select file");
    // $('.fileinput-new').show();

    $('#fileUpload').modal('show', {backdrop: 'static'});
    var ref_no = $(this).closest('tr').children('td:first').text();
    var whichform = ref_no.substring(0, ref_no.indexOf('-'));
    var showEdit = $(this).attr("id"); // to show /hide edit on preview based on stat_type
    $('#form_val').val(whichform)
    $('#ref_no').val(ref_no)
});
$(document).on('click', '.fupload_1', function () {

    $('#file_cert_err').text("");
    $('.fileinput-filename').text("");
    //  $('.fileinput-exists').text("select file");
    // $('.fileinput-new').show();

    $('#fileUpload').modal('show', {backdrop: 'static'});
    var ref_no = $(this).closest('tr').children('td:first').text();
    var whichform = ref_no.substring(0, ref_no.indexOf('-'));
    var showEdit = $(this).attr("id"); // to show /hide edit on preview based on stat_type
    $('#form_val').val(whichform)
    $('#ref_no').val(ref_no)


    $.ajax({
        url: 'getCert',
        type: 'POST',
        data: {ref_no: ref_no, whichform: whichform},
        datatype: JSON,
        success: function (data) {

            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            if (jsonvalue.sign_cert != null && jsonvalue.sign_cert != "")
            {

                $('#update_cert').removeClass("display-hide")
                $('#update_footer_cert').removeClass("display-hide")
                $('#insert_cert').addClass("display-hide")
                $('#insert_footer_cert').addClass("display-hide")
                $('#cert_relay_file').text(jsonvalue.sign_cert);
                $('#regno_cert').val(jsonvalue.registration_no);
            } else {

                $('#insert_footer_cert').removeClass("display-hide")
                $('#insert_cert').removeClass("display-hide")
                $('#update_cert').addClass("display-hide")
                $('#update_footer_cert').addClass("display-hide")
            }
            //console.log("certdetails:::"+jsonvalue.certdetails.sign_cert)
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error');
        }
    });
});
$('.updatecert').click(function () {

    var regno = $("#regno_cert").val();
    var whichform = regno.substring(0, regno.indexOf('-'));
    $.ajax({
        url: 'updatecert',
        type: 'POST',
        data: {ref_no: regno, whichform: whichform},
        datatype: JSON,
        success: function (data) {
            $('#update_cert').removeClass("display-hide")
            $('#update_footer_cert').removeClass("display-hide");
            $('#file_cert_err1').html("File Uploaded Successfully.");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error');
        }
    });
});
//$('.fupload').click(function () {



$(document).on('click', '.work_order', function () {
    $('#file_cert_err').text("");
    $('.fileinput-filename').text("");
    $('#workOrderUpload').modal('show', {backdrop: 'static'});
    var ref_form = $(this).attr("id");
    var arr = ref_form.split("~");
    var ref_no = arr[0];
    var whichform = ref_no.substring(0, ref_no.indexOf('-'));
    $('#form_val').val(whichform)
    $('#ref_no').val(ref_no)
    var showEdit = $(this).attr("id"); // to show /hide edit on preview based on stat_type
});


$('.uploadworder').click(function (e) {

    e.preventDefault();
    var file = $('input[name="workorder_cert_file"]').get(0).files[0];
    if (file != null)
    {
        console.log("file@@@@@@@@" + file)
        var fileType = file.type;
        var fileSize = file.size;
        var match = ["application/pdf"]; // for multiple take comma separated values
        if (fileType === match[0]) {
            if (fileSize <= 1000000) {
                $('#file_err').text("");
                var formData = new FormData();
                formData.append('cert_file', file);
                $.ajax({
                    url: 'checkPDF',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);
                        var ref_no = $('#ref_no').val();
                        if (jsonvalue.error !== '' && jsonvalue.error !== null) {
                            $('#file_err').text(jsonvalue.error);
                            $('#cert').val('false');
                        } else {
                            var whichform = ref_no.substring(0, ref_no.indexOf('-'));

                            $('#cert').val('true');
                            $.ajax({
                                type: "POST",
                                url: "preview",
                                data: {data: ref_no, whichform: whichform, comingFrom: "user", action: "newReqOnprvw"},
                                dataType: 'json'
                            }).done(function (data) {
                                resetCSRFRandom(); // line added by pr on 23rdjan18

                                var myJSON = JSON.stringify(data);
                                var jsonvalue = JSON.parse(myJSON);

                                if (whichform === "EMAILACTIVATE") { //added by nikki on 26th jun 2019
                                    whichform = 'email_act';
                                    $.ajax({
                                        type: "POST",
                                        url: "email_act_preview",
                                        dataType: 'html', complete: function () {
                                            $('.loader').hide();
                                        }
                                    })
                                            .done(function (dataResponse) {

                                                if (dataResponse.trim() != '' && dataResponse != null) {

                                                    $('.email_act_preview').html(dataResponse);
                                                    $(".modal-title").html("Preview for " + ref_no);
                                                    loadFormData();
                                                    if (jsonvalue.prvw_emp_type === 'emp_regular') {
                                                        $('#email_act_preview #single_emp_type1').prop('checked', 'checked');
                                                        $('#email_act_preview #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                                                        $('#email_act_preview #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                                                    } else if (jsonvalue.prvw_emp_type === 'consultant') {
                                                        $('#email_act_preview #single_emp_type2').prop('checked', 'checked');
                                                        $('#email_act_preview #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                                                        $('#email_act_preview #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                                                    } else {
                                                        $('#email_act_preview #single_emp_type3').prop('checked', 'checked');
                                                        $('#email_act_preview #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                                                        $('#email_act_preview #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                                                    }

                                                    if (jsonvalue.prvw_emp_type !== "prvw_file_name")
                                                    {

                                                        if (jsonvalue.prvw_file_name !== "")
                                                        {
                                                            $('#email_act_preview #cert_div').removeClass("display-hide");
                                                            $('#email_act_preview #cert_div1').removeClass("display-hide");
                                                            $('#email_act_preview #uploaded_filename').text(jsonvalue.prvw_file_name);
                                                        }
                                                    } else {
                                                        $('#email_act_preview #cert_div1').addClass("display-hide");
                                                        $('#email_act_preview #cert_div').addClass('display-hide');
                                                    }

                                                    $('#email_act_preview #act_email1').val(jsonvalue.prvw_pemail1);
                                                    $('#email_act_preview #email_single_dor1').val(jsonvalue.prvw_dor);
                                                    $('#email_act_preview .edit').removeClass('display-hide');
                                                    $('#email_act_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                                    $("#email_act_preview #confirm").val('work_order_upload');
                                                    // $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').attr('disabled', 'true');
                                                    $("#email_act_preview :button[type='button']").removeAttr('disabled');
                                                    $("#email_act_preview #tnc").removeAttr('disabled');
                                                    $('#workOrderUpload').modal('toggle');
                                                    $('#email_act_preview_form').modal({backdrop: 'static', keyboard: false});
                                                } else {
                                                    console.log('Preview is not loading');
                                                }
                                            })
                                            .fail(function () {
                                                console.log('This isn\'t LIT');
                                            });
                                } else if (whichform === "EMAILDEACTIVATE") { //added by nikki on 26th jun 2019
                                    whichform = 'email_act';
                                    $.ajax({
                                        type: "POST",
                                        url: "email_act_preview",
                                        dataType: 'html'
                                    })
                                            .done(function (dataResponse) {

                                                if (dataResponse.trim() != '' && dataResponse != null) {

                                                    $('.email_act_preview').html(dataResponse);
                                                    $(".modal-title").html("Preview for " + ref_no);
                                                    loadFormData()



                                                    $("#email_act_preview .edit").removeClass('display-hide');
                                                    $('#email_act_preview .save-changes').removeClass('display-hide');
                                                    $('#email_act_preview #single_email1').val(jsonvalue.prvw_pemail1);
                                                    $('#email_act_preview #divDor').addClass('display-hide');

                                                    $('#email_act_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                                                    $("#email_act_preview :button[type='button']").removeAttr('disabled');
                                                    $("#email_act_preview #tnc").removeAttr('disabled');
                                                    $("#email_act_preview #tnc_div").addClass('display-hide');
                                                    $("#email_act_preview #confirm").addClass('display-hide');

                                                    $("#email_act_preview #closebtn").removeClass('display-hide').html('Close').show();
                                                    $('#email_act_preview_form').modal({backdrop: 'static', keyboard: false});
                                                } else {
                                                    console.log('Preview is not loading');
                                                }
                                            })
                                            .fail(function () {
                                                console.log('This isn\'t LIT');
                                            });
                                }

                                function loadFormData() {
                                    console.log(whichform);
                                    console.log('this is lit ' + jsonvalue.prvw_offname + "whichform" + whichform);
                                    $('#' + whichform.toLowerCase() + '_preview #user_name').val(jsonvalue.prvw_offname);
                                    $('#' + whichform.toLowerCase() + '_preview #user_design').val(jsonvalue.prvw_desig);
                                    $('#' + whichform.toLowerCase() + '_preview #user_address').val(jsonvalue.prvw_address);
                                    $('#' + whichform.toLowerCase() + '_preview #user_city').val(jsonvalue.prvw_city);
                                    $('#' + whichform.toLowerCase() + '_preview #user_state').val(jsonvalue.prvw_addstate);
                                    $('#' + whichform.toLowerCase() + '_preview #user_pincode').val(jsonvalue.prvw_pin);
                                    $('#' + whichform.toLowerCase() + '_preview #user_ophone').val(jsonvalue.prvw_ophone);
                                    $('#' + whichform.toLowerCase() + '_preview #user_rphone').val(jsonvalue.prvw_rphone);


                                    if (jsonvalue.role == "user" || jsonvalue.role == "ca")
                                    {
                                        var startMobile = jsonvalue.prvw_mobile.substring(0, 3);
                                        var endMobile = jsonvalue.prvw_mobile.substring(10, 13);
                                        $('#' + whichform.toLowerCase() + '_preview #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                                    } else {
                                        $('#' + whichform.toLowerCase() + '_preview #user_mobile').val(jsonvalue.prvw_mobile);

                                    }

                                    $('#' + whichform.toLowerCase() + '_preview #user_email').val(jsonvalue.prvw_authemail);
                                    $('#' + whichform.toLowerCase() + '_preview #user_empcode').val(jsonvalue.prvw_empcode);
                                    $('#' + whichform.toLowerCase() + '_preview #user_employment').val(jsonvalue.prvw_employment);
                                    $('#' + whichform.toLowerCase() + '_preview #hod_name').val(jsonvalue.hod_name);
                                    $('#' + whichform.toLowerCase() + '_preview #hod_email').val(jsonvalue.hod_email);
                                    $('#' + whichform.toLowerCase() + '_preview #hod_tel').val(jsonvalue.hod_tel);
                                    if (jsonvalue.role == "user" || jsonvalue.role == "ca")
                                    {
                                        var ROstartMobile = jsonvalue.hod_mobile.substring(0, 3);
                                        var ROendMobile = jsonvalue.hod_mobile.substring(10, 13);
                                        $('#' + whichform.toLowerCase() + '_preview #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
                                    } else {
                                        $('#' + whichform.toLowerCase() + '_preview #hod_mobile').val(jsonvalue.hod_mobile);
                                    }
                                    $('#' + whichform.toLowerCase() + '_preview #ca_design').val(jsonvalue.ca_design);
//                        if (jsonvalue.prvw_department == "Department of Electronics and Information Technology (Deity), MyGov Project")
//                        {
//
//
//                            $('#wifi_preview #forwarding_div').hide();
//                        } else {
//
//                            $('#wifi_preview #forwarding_div').show();
//                        }


                                    if (jsonvalue.prvw_employment === 'Central' || jsonvalue.prvw_employment === 'UT') {
                                        $('#' + whichform.toLowerCase() + '_preview #central_div').show();
                                        $('#' + whichform.toLowerCase() + '_preview #state_div').hide();
                                        $('#' + whichform.toLowerCase() + '_preview #other_div').hide();
                                        $('#' + whichform.toLowerCase() + '_preview #other_text_div').addClass("display-hide");
                                        var select = $('#' + whichform.toLowerCase() + '_preview #min');
                                        select.find('option').remove();
                                        $('<option>').val(jsonvalue.prvw_ministry).text(jsonvalue.prvw_ministry).appendTo(select);
                                        var select = $('#' + whichform.toLowerCase() + '_preview #dept');
                                        select.find('option').remove();
                                        $('<option>').val(jsonvalue.prvw_department).text(jsonvalue.prvw_department).appendTo(select);
                                        if (jsonvalue.prvw_department.toString().toLowerCase() === 'other') {
                                            $('#' + whichform.toLowerCase() + '_preview #other_text_div').removeClass("display-hide");
                                            $('#' + whichform.toLowerCase() + '_preview #other_dept').val(jsonvalue.prvw_state_dept);
                                        }
                                    } else if (jsonvalue.prvw_employment === 'State') {

                                        $('#' + whichform.toLowerCase() + '_preview #central_div').hide();
                                        $('#' + whichform.toLowerCase() + '_preview #state_div').show();
                                        $('#' + whichform.toLowerCase() + '_preview #other_div').hide();
                                        $('#' + whichform.toLowerCase() + '_preview #other_text_div').addClass("display-hide");

                                        $('#' + whichform.toLowerCase() + '_preview #stateCode').val(jsonvalue.prvw_state);
                                        var select = $('#' + whichform.toLowerCase() + '_preview #stateCode');
                                        select.find('option').remove();

                                        $('<option>').val(jsonvalue.prvw_state).text(jsonvalue.prvw_state).appendTo(select);
                                        var select = $('#' + whichform.toLowerCase() + '_preview #Smi');
                                        select.find('option').remove();
                                        $('<option>').val(jsonvalue.prvw_department).text(jsonvalue.prvw_department).appendTo(select);
                                        if (jsonvalue.prvw_department.toString().toLowerCase() === 'other') {
                                            $('#' + whichform.toLowerCase() + '_preview #other_text_div').removeClass("display-hide");
                                            $('#' + whichform.toLowerCase() + '_preview #other_dept').val(jsonvalue.prvw_state_dept);
                                        }

                                    } else if (jsonvalue.prvw_employment === 'Others' || jsonvalue.prvw_employment === "Psu" || jsonvalue.prvw_employment === "Const" || jsonvalue.prvw_employment === "Nkn" || jsonvalue.prvw_employment === "Project") {
                                        $('#' + whichform.toLowerCase() + '_preview #central_div').hide();
                                        $('#' + whichform.toLowerCase() + '_preview #state_div').hide();
                                        $('#' + whichform.toLowerCase() + '_preview #other_div').show();
                                        $('#' + whichform.toLowerCase() + '_preview #other_text_div').addClass("display-hide");
                                        var select = $('#' + whichform.toLowerCase() + '_preview #Org');
                                        select.find('option').remove();
                                        $('<option>').val(jsonvalue.prvw_org).text(jsonvalue.prvw_org).appendTo(select);
                                        if (jsonvalue.prvw_org.toString().toLowerCase() === 'other') {
                                            $('#' + whichform.toLowerCase() + '_preview #other_text_div').removeClass("display-hide");
                                            $('#' + whichform.toLowerCase() + '_preview #other_dept').val(jsonvalue.prvw_state_dept);
                                        }

                                    } else {
                                        $('#' + whichform.toLowerCase() + '_preview #central_div').hide();
                                        $('#' + whichform.toLowerCase() + '_preview #state_div').hide();
                                        $('#' + whichform.toLowerCase() + '_preview #other_div').hide();
                                    }
                                }

                            })
                                    .fail(function () {
                                        console.log('error');
                                    });

                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log('error');
                    }
                });
            } else {

                $('#file_err').text("PDF file should be less than 1mb");

            }
        } else {

            $('#file_err').text("Upload only PDF file");


        }
    } else {

        $('#file_err').text("Please upload pdf file.");
    }
});


$(document).on('click', '.fupload', function () {

    $('#file_cert_err').text("");
    $('.fileinput-filename').text("");
    $('#fileUpload').modal('show', {backdrop: 'static'});
    var ref_form = $(this).attr("id");
    var arr = ref_form.split("~");
    var ref_no = arr[0];
    var whichform = ref_no.substring(0, ref_no.indexOf('-'));
    $('#form_val').val(whichform)
    $('#ref_no').val(ref_no)
    var showEdit = $(this).attr("id"); // to show /hide edit on preview based on stat_type




});
$('.track_upload #cert_file').change(function (e) {

    var file = $('input[name="cert_file"]').get(0).files[0];
    var fileType = file.type;
    var fileSize = file.size;
    var match = ["application/pdf"]; // for multiple take comma separated values
    if (fileType === match[0]) {
        if (fileSize <= 1000000) {
            $('#file_cert_err').text("");
            var formData = new FormData();
            formData.append('cert_file', file);
            $.ajax({
                url: 'checkCert',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (data) {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    if (jsonvalue.error !== '' && jsonvalue.error !== null) {
                        $('#file_cert_err').text(jsonvalue.error);
                        $('#cert').val('false');
                    } else {
                        $('#cert').val('true');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('error');
                }
            });
        } else {
            $('#file_cert_err').text("PDF file shoulb be less than 1mb");
        }
    } else {
        $('#file_cert_err').text("Upload only PDF file");
    }
})
$('.track_upload1 #cert_file1').change(function (e) {

    var file = $('input[name="cert_file1"]').get(0).files[0];
    var fileType = file.type;
    var fileSize = file.size;
    var match = ["application/pdf"]; // for multiple take comma separated values
    if (fileType === match[0]) {
        if (fileSize <= 1000000) {
            $('#file_cert_err').text("");
            var formData = new FormData();
            formData.append('cert_file', file);
            $.ajax({
                url: 'checkCert',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (data) {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    if (jsonvalue.error !== '' && jsonvalue.error !== null) {
                        $('#file_cert_err').text(jsonvalue.error);
                        $('#cert').val('false');
                    } else {
                        $('#cert').val('true');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('error');
                }
            });
        } else {
            $('#file_cert_err').text("PDF file shoulb be less than 1mb");
        }
    } else {
        $('#file_cert_err').text("Upload only PDF file");
    }
})
$(document).on('click', '.upload_cert', function (e) {
    e.preventDefault();
    var ref_no = $('#ref_no').val();
    var whichform = $('#form_val').val();
    var panel = $('#panel').val();
    var file = $('input[name="cert_file"]').get(0).files[0];
    var fileType = file.type;
    var fileSize = file.size;
    var match = ["application/pdf"]; // for multiple take comma separated values
    if (fileType === match[0]) {

        if (fileSize <= 1000000) {
            $('#file_cert_err').text("");
            var formData = new FormData();
            formData.append('cert_file', file);
            $.ajax({
                type: "POST",
                url: "sign_cert",
                data: {data: ref_no, whichform: whichform, panel: panel},
                dataType: 'html',
                success: function (data) {
                    var cert = $('#cert').val();
                    if (cert == "true")
                    {
                        var msg = "File Uploaded Successfully.";
                        if (panel == "CA")
                        {
                            msg = "File Uploaded Successfully. Please click on Approve button under Actions to forward this Request.";
                        }
                    } else {
                        msg = "Please upload pdf file only.";
                    }
                    bootbox.alert({
                        message: msg,
                        callback: function () {
                            location.reload(true);
                        }
                    });
                },
                error: function () {
                    console.log('error');
                }
            });
        } else {
            $('#file_cert_err').text("PDF file shoulb be less than 1mb");
        }
    } else {
        $('#file_cert_err').text("Upload only PDF file");
    }
});

function fuploadmul(ref_no) {
    $("#vpb_added_files").addClass('d-none');
    $("#vpb_added_files_box").html('<tr><td colspan="5" class="text-center"><div class="no-record">No Record Found.</div></td></tr>');
    $("#file_cert_mul_err").text("");
    $('#file_cert_err').text("");
    $('.fileinput-filename').text("");
    $("#fileUploadMultiple .modal-header h4").html("File Upload for:- " + ref_no);
    $('#fileUploadMultiple').modal('show', {backdrop: 'static'});

    var whichform = ref_no.substring(0, ref_no.indexOf('-'));
    var showEdit = $(this).attr("id"); // to show /hide edit on preview based on stat_type
    $('#form_val').val(whichform)
    $('#ref_no').val(ref_no)
}
function fdownload(ref_no) {
    $('.loader').show();
    $('#fileDownload').modal('show', {backdrop: 'static'});
    //var ref_no = $(this).closest('tr').children('td:first').text();
    var whichform = ref_no.substring(0, ref_no.indexOf('-'));
    var showEdit = $(this).attr("id"); // to show /hide edit on preview based on stat_type
    $('#form_val').val(whichform)
    $('#ref_no').val(ref_no)
    var panel = $('#panel').val();

    var formData = new FormData();
    formData.append('ref_no', ref_no);
    formData.append('panel', panel);
    $.ajax({
        url: 'downloadpdf',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            var i = 0;
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            var str = "<table class='table table-sm table-bordered table-striped downloaded-tbl'><thead><tr><th>File Name</th><th>Role</th><th>Action</th></tr></thead>";
            var doc = "", up_role = ""; // line added by pr on 25thapr19
            for (var value in jsonvalue) { // for loop modified by pr on 25thapr19
                console.log(" value in for loop is " + value);
                var arr = value.split("|");
                if (arr != null && arr.length > 0)
                {
                    if (arr[0] != null)
                    {
                        doc = arr[0];
                    }
                    if (arr[1] != null)
                    {
                        up_role = arr[1];
                    }
                }

                value = doc;
                str += "<tr><td><i class='fa fa-file-pdf-o'></i> " + value + "</td>";
                str += "<td id='role'> " + up_role + "</td>"; // line added by pr on 25thapr19
                str += "<td><a href='download_6?doc_name=" + value + "' class='btn btn-info btn-sm' target='_blank'><i class='fa fa-download' ></i> Download</a>&nbsp;<button type='button' class='btn btn-danger btn-sm' id='file_remove' value='" + value + "'><i class='fa fa-remove' ></i> Delete</button></td></tr>";

                i++;
            }
            str += "</table>";
            if (i > 0) {
                $("#fileDowloadResult").html(str);
            } else {
                $("#fileDowloadResult").html("No File to download.");
            }

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error');
        }, complete: function (data) {
            $('.loader').hide();
        }
    });
}
function fuserdownload(ref_no) {
    $('#fileUserDownload').modal('show', {backdrop: 'static'});
    //var ref_no = $(this).closest('tr').children('td:first').text();
    var whichform = ref_no.substring(0, ref_no.indexOf('-'));
    var showEdit = $(this).attr("id"); // to show /hide edit on preview based on stat_type
    $('#form_val').val(whichform)
    $('#ref_no').val(ref_no)
    var panel = 'USER'

    var formData = new FormData();
    formData.append('ref_no', ref_no);
    formData.append('panel', panel);
    $.ajax({
        url: 'downloadpdf',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            var i = 0;
            var str = "<div class='portlet box red'><div class='portlet-title'><div class='caption'><h6><i class='fa fa-file-pdf'></i>&emsp;Document List Uploaded by User</h6></div></div><div class='portlet-body'><div class='table-responsive'><table class='table table-sm table-bordered table-striped downloaded-tbl'><thead><tr><th>File Name</th><th>Action</th></tr></thead>";
            var doc = ""; // line added by pr on 25thapr19
            for (var value in jsonvalue) {
                var arr = value.split("|");
                if (arr != null && arr.length > 0)
                {
                    if (arr[0] != null)
                    {
                        doc = arr[0];
                    }
                }

                value = doc;
                str += "<tr><td><i class='fa fa-file-pdf-o'></i> " + value + "</td>";
                str += "<td><a href='download_6?doc_name=" + value + "' class='btn btn-info btn-sm' target='_blank'><i class='fa fa-download' ></i> Download</a></td></tr>";
                i++;
            }
            str += "</table></div></div></div>";
            if (i > 0) {
                $("#fileDowloadResult1").html(str);
            } else {
                $("#fileDowloadResult1").html("No File to download.");
            }

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error');
        }
    });
}
$(document).on('click', '#file_remove', function () {
    var file = $(this).val();
    var ref_no = $('#ref_no').val();
    var panel = $('#role').val();
    // alert($('#role').val())
    var whichform = ref_no.substring(0, ref_no.indexOf('-'));
    bootbox.confirm({
        title: "Remove File",
        message: "Are you sure? Do you want to remove this file?",
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
                //$('.loader').show();
                $.ajax({
                    type: "POST",
                    url: "fileDelete",
                    data: {data: ref_no, whichform: whichform, panel: panel, filetoDelete: file},
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);

                        if (jsonvalue.returnString == "success")
                        {
                            fdownload(ref_no)
                        }
                    }, complete: function (data) {
                        //$('.loader').hide();
                    }
                });
            }
        }

    });
});
function resend(ref_no) {
    //var ref_no = $(this).closest('tr').children('td:first').text();
    var whichform = ref_no.substring(0, ref_no.indexOf('-'));
    var r = confirm("Your application with  Reference number " + ref_no + " will be processed again as you are choosing to resubmit the request . Do you still wish to proceed ?"); // text modified by pr on 19thfeb18
    if (r === true) {
        $.ajax({
            type: "POST",
            url: "preview",
            data: {data: ref_no, whichform: whichform},
            success: function (data) {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                if (jsonvalue.form_name == "IMAPPOP")
                {
                    window.location = "ImapPop_registration.jsp";
                } else if (jsonvalue.form_name == "SMS")
                {
                    window.location = "Sms_Registration.jsp";
                } else if (jsonvalue.form_name == "LDAP")
                {
                    window.location = "Ldap_registration.jsp";
                } else if (jsonvalue.form_name == "DLIST")
                {
                    window.location = "Distribution_registration.jsp";
                } else if (jsonvalue.form_name == "RELAY")
                {
                    window.location = "Relay_registration.jsp";
                } else if (jsonvalue.form_name == "MOBILE")
                {
                    window.location = "Mobile_registration.jsp";
                } else if (jsonvalue.form_name == "WIFI")
                {
                    window.location = "Wifi_registration.jsp";
                } else if (jsonvalue.form_name == "IP")
                {
                    window.location = "Change_ip.jsp";
                } else if (jsonvalue.form_name == "DNS")
                {
                    window.location = "Dns_registration.jsp";
                } else if (jsonvalue.form_name == "SINGLEUSER")
                {
                    window.location = "Email_registration.jsp";
                } else if (jsonvalue.form_name == "GEM")
                {
                    window.location = "Email_registration.jsp";
                } else if (jsonvalue.form_name == "NKN")
                {
                    window.location = "Email_registration.jsp";
                }

            },
            error: function () {
                console.log("It seem's there is some problem, please try again later !!!")
            }
        });
    }
}
$('#user_design').blur(function () {

    var desig = ($('#user_design').val());
    console.log(" desig is " + desig);
    if ((desig.toLowerCase().indexOf("scientist") != -1 && (desig.toLowerCase().indexOf("f") != -1 || desig.toLowerCase().indexOf("g") != -1 || desig.toLowerCase().indexOf("h") != -1)) || desig.toLowerCase().indexOf("hod") != -1 || desig.toLowerCase().indexOf("secretary") != -1 || desig.toLowerCase().indexOf("head of the department") != -1)
    {

        console.log(" inside show ");
        $("#desigadd").show();
    } else
    {
        console.log(" inside hide ");
        $("#desigadd").hide();
    }
});
$('#user_state').change(function () {
    var user_state = $("#user_state").val();
    $.get('getDistrict', {
        user_state: user_state
    }, function (response) {

        var select = $('#user_city');
        select.find('option').remove();
        $('<option>').val("").text("-SELECT-").appendTo(select);
        $.each(response, function (index, value) {
            $('<option>').val(value).text(value).appendTo(select);
        });
    });
})
$(document).on('click', '.rand', function (e) {
    e.preventDefault();
    var mobile = $('#user_mobile').val();
    console.log("click on generate code")
    $('#succ_err').html("");
    $.ajax({
        type: "POST",
        url: "OtpGenerate_updatemobile",
        data: {mobile: mobile},
        dataType: 'html'
    })
            .done(function (data) {
                $("#old_code").removeClass("display-hide");
                $(".rand").addClass("display-hide");
            })
            .fail(function () {
                console.log('error');
            });
});
$(document).on('blur', '#oldcode', function (e) {
    e.preventDefault();
    var oldcode = $('#oldcode').val();
    var mobile = $('#user_mobile').val();
    console.log("click on verfiy old code")
    $.ajax({
        type: "POST",
        url: "verify_old_code",
        data: {oldcode: oldcode, mobile: mobile},
        datatype: JSON,
        success: function (data)
        {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            $('#old_code_err').html("");
            console.log("return output to verify old code" + jsonvalue.returnString)
            if (jsonvalue.returnString == "old_code_err")
            {
                $('#old_code_err').html("Please Enter OTP in correct format");
            } else if (jsonvalue.returnString == "success")
            {
                $('#right').removeClass("display-hide");
                $('#wrong').addClass("display-hide");
                $("#new-mobile").removeClass("display-hide");
            } else {
                $('#wrong').removeClass("display-hide");
                $('#right').addClass("display-hide");
                $('#old_code_err').html("");
            }
        }, error: function ()
        {
            console.log("error in old code verify")
        }
    })


});
$(document).on('blur', '#newmobile', function (e) {
    e.preventDefault();
    var new_mobile = $('#newmobile').val();
    var mobile = $('#user_mobile').val();
    console.log("on the click of new mobile")
    $.ajax({
        type: "POST",
        url: "OtpGenerate_newmobile",
        data: {new_mobile: new_mobile, mobile: mobile},
        datatype: JSON,
        success: function (data)
        {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            $('#moberr').html("");
            console.log("return output if click new mobile" + jsonvalue.returnString)

            if (jsonvalue.returnString == "moberr")
            {
                $("#new-code").addClass("display-hide");
                $('#moberr').html("Please Enter Mobile in correct format with country code e.g +91XXXXXXXXXX");
            } else if (jsonvalue.returnString == "mobileInLdap")
            {
                $("#new-code").addClass("display-hide");
                $('#moberr').html("There is already an email address associated with this mobile number so the same mobile number can't be updated");// text change 20thmay2020
            } else if (jsonvalue.returnString == "same")
            {
                $('#moberr').html("This is the same number you are updating");
                $("#new-code").addClass("display-hide");
                $('#succ_err').html("");
                $("#newcode").val('');
            } else {
                $("#new-code").removeClass("display-hide");
            }
        }, error: function ()
        {
            console.log("error in old code verify")
        }
    })


})
$(document).on('blur', '#newcode', function (e) {
    e.preventDefault();
    var oldcode = $('#oldcode').val();
    var mobile = $('#user_mobile').val();
    var newcode = $('#newcode').val();
    var new_mobile = $('#newmobile').val();
    var user_alt_address = $('#user_alt_address').val();
    console.log("on the click of new code")
    $.ajax({
        type: "POST",
        url: "verify_new_code",
        data: {oldcode: oldcode, mobile: mobile, newcode: newcode, new_mobile: new_mobile, user_alt_address: user_alt_address},
        datatype: JSON,
        success: function (data)
        {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            if (jsonvalue.returnString == "new_code_err")
            {

                $('#succ_err').html("Please enter new otp code in correct format");
            } else if (jsonvalue.returnString == "newOtpNotMatch")
            {

                $('#succ_err').html("Please enter correct new otp");
            } else if (jsonvalue.returnString == "oldOtpNotMatch")
            {

                $('#succ_err').html("Please enter correct old otp");
            } else if (jsonvalue.returnString == "success")
            {

                $('#succ_err').html("Mobile number updated successfully");
            } else if (jsonvalue.returnString == "mobilesamehod")
            {

                $('#moberr').html("You can not update your mobile number same as of your reporting officer");
                $("#newcode").val('');
            } else if (jsonvalue.returnString == "mobilesameus")
            {

                $('#moberr').html("You can not update your mobile number same as of your Under Secratery");
                $("#newcode").val('');
            } else if (jsonvalue.returnString == "existprofile")
            {
                $('#succ_err').html("Please make your profile first");
                $("#newcode").val('');
            } else {
                $('#succ_err').html("Please Enter correct OTP");
                $("#newcode").val('');
            }
        }, error: function ()
        {
            console.log("error in new code ")
        }
    })


});
$(document).on('click', '#mod_close_mobile', function () {

    window.location.href = 'profile';
});
// below code added by pr on 1stnov18
$(document).on('click', '#scan_mod_close', function () {

    window.location.href = 'showLinkData?adminValue=ca';
});
$(document).on('click', '.process', function () {

    var field_id = $(this).attr('id');
    var field_form = $(this).closest('form').attr('id');
    var field_name = $(this).attr('name');
    if (field_id == "wifi_cert")
    {

        $('#' + field_form + ' #wifi_req_div').addClass("display-hide")

    } else if (field_id == "wifi_req")
    {

        $('#' + field_form + ' #wifi_req_div').removeClass("display-hide")

    }

});
function approve_ca(reg_no, form_name, stat_type)
{


    $.ajax({
        type: "POST",
        url: "approve_ca",
        data: {data: reg_no, whichform: form_name, stat_type: stat_type},
        datatype: JSON,
        success: function (data)
        {

            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            if (jsonvalue.response == "success")
            {

                window.location = "showUserData";
            } else {

                $('#approve_ca_modal').modal('show', {backdrop: 'static'});
            }
        }, error: function ()
        {
            console.log("fdsfdsf")
        }
    })


}

$(document).on('change', '#pse_state', function () {
    var pse_state = $("#pse_state").val();
    $.get('getDistrict', {
        user_state: pse_state
    }, function (response) {

        var select = $('#pse_district');
        select.find('option').remove();
        $('<option>').val("").text("-SELECT-").appendTo(select);
        $.each(response, function (index, value) {
            $('<option>').val(value).text(value).appendTo(select);
        });
    });
})

$(document).on('change', '#gem_preview #pse_state', function () {

    var pse_state = $("#gem_preview #pse_state").val();
    $.get('getDistrict', {
        user_state: pse_state
    }, function (response) {

        var select = $('#gem_preview #pse_district');
        select.find('option').remove();
        $('<option>').val("").text("-SELECT-").appendTo(select);
        $.each(response, function (index, value) {
            $('<option>').val(value).text(value).appendTo(select);
        });
    });
})
$(document).on('click', 'input[type="radio"]', function () {

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

function createdombtn(popup_id, approve_btn, reject_btn, raise_btn) {
    $('.preview-change .modal-footer').prepend(`<div class="special_button_collection">
        <span class="btn btn-primary req_approve">` + approve_btn + `</span>
        <span class="btn btn-danger req_reject ml-1">` + reject_btn + `</span>
        <span class="req_pullback ml-1">` + raise_btn + `</span>
    </div>`);

    $(".special_button_collection span.btn").click(function () {
        $('.modal:visible').removeClass('fade').modal('hide').addClass('fade');
    });
}
$(document).on('change', '.selectReason', function () {
//$('#mobile_preview .selectReason').change(function () {


    var field_form = $(this).closest('form').attr('id');

    var selectReason = $("#" + field_form + " .selectReason").val();

    if (selectReason === "other")
    {
        $("#" + field_form + " #remarks_div").removeClass("display-hide");
    } else {
        $("#" + field_form + " #remarks_div").addClass("display-hide");
    }
})

