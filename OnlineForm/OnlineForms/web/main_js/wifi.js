$(document).ready(function () {
    var processVal = $('input[name="wifi_process"]:checked').val();
    if (processVal == "request")
    {
        fetchApplicantPendingData();
        console.log("111111111111111111111")
        $('#wifi_req_div').removeClass("display-hide")
        $('#wifi_Deletereq_div').addClass("display-hide")
        $('#wifi_req_div').find("input, button, submit, textarea, select").attr("disabled", false);
        //$("#wifi_Pendingreq_div").children("div").remove();

    }

    $('.page-wrapper').find('input').click(function () {
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');
        if (field_id == "wifi_cert")
        {
            $('#' + field_form + ' #wifi_req_div').addClass("display-hide")
            $('#' + field_form + ' #wifi_Deletereq_div').addClass("display-hide");
            $('#' + field_form + ' #wifi_req_div').find("input, button, submit, textarea, select").attr("disabled", true);
            bootbox.alert("VPN Certificate is not required for NIC WIFI connectivity.Do you really wish to proceed."); // line added by pr on 24thapr19

        } else if (field_id == "wifi_req")
        {
            console.log("222222222222222")
            $('#' + field_form + ' #wifi_req_div').removeClass("display-hide")
            $('#' + field_form + ' #wifi_Deletereq_div').addClass("display-hide")
            // $('#' + field_form + ' #wifi_Pendingreq_div').removeClass("display-hide")
            $('#' + field_form + ' #wifi_req_div').find("input, button, submit, textarea, select").attr("disabled", false);
            $("#wifi_Pendingreq_div").children("div").remove();
            fetchApplicantPendingData();
            // $('#' + field_form + ' #wifi_req_div').addClass("display-hide")
            //$('#' + field_form + ' #wifi_req_div').find("input, button, submit, textarea, select").attr("disabled", true);
            $('#' + field_form + ' #wifi_Deletereq_div').addClass("display-hide")

        } else if (field_id == "wifi_deleteReq")
        {
            $("#wifi_Deletereq_div").children("div").remove();
            $.ajax({
                type: "GET",
                url: "fetchApplicantData",
                //data: {email: hod_email},
                //datatype: JSON,
                success: function (data) {
                    console.log(data);
                    var myJSON = JSON.stringify(data);
                    //var i = 1;
                    var jsonvalue = JSON.parse(myJSON);
                    console.log("array length" + jsonvalue.length)

                    if (jsonvalue.length == 0)
                    {
                        bootbox.alert("No Mac Address to Delete.");
                    } else {
                        for (var i = 0; i < jsonvalue.length; i++)
                        {
                            $("#wifi_Deletereq_div").append('<div class="row" style="padding: 10px;"><div class="col-md-5"><label class="control-label" for="street">MAC Address of the Device <span style="color: red">*</span></label><input class="form-control" readonly placeholder="Enter MAC Address (e.g: AA:AA:AA:AA:AA:AA)" type="text" name="wifi_mac1' + i + '" id="wifi_mac1"  value="' + jsonvalue[i].wifi_mac1 + '" maxlength="17"></div><div class="col-md-5"><label class="control-label" for="street">Operating System of the Device<span style="color: red">*</span></label><input class="form-control" readonly placeholder="Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]" type="text" name="wifi_os1' + i + '" id="wifi_os1"  value="' + jsonvalue[i].wifi_os1 + '"></div><div class="col-md-2" style="margin-top: 10px;"><label>Delete</label><br><input type="checkbox" name="delete_mac_id"  id="delete_mac_id" value="' + jsonvalue[i].wifi_mac1 + "," + jsonvalue[i].wifi_os1 + "," + jsonvalue[i].registration_no + '"></div></div>');
                        }
                    }

                },
                error: function () {
                    console.log('error');
                }
            });
            $('#' + field_form + ' #wifi_req_div').addClass("display-hide")
            //$('#' + field_form + ' #wifi_req_div').find("input, button, submit, textarea, select").attr("disabled", true);
            $('#' + field_form + ' #wifi_Deletereq_div').removeClass("display-hide")

        }

    });
//    $(document).on('find click', '.page-wrapper button', function () {
//        console.log("sadsada")
//        var field_id = $(this).attr('id');
//        var field_form = $(this).closest('form').attr('id');
//        var field_name = $(this).attr('name');
//        console.log(field_name)
//        var form_tab;
//        // wifi form 
////        if (field_form === "wifi_form1") {
////            if (field_name === 'guest_wifi') {
////                $('#wifi_type_div').addClass('display-hide');
////                $('#nic_wifi_div').addClass('display-hide');
////                $('#guest_wifi_div').removeClass('display-hide');
////                $('.nic_wifi_div').find('input').val('');
////            } else if (field_name === 'nic_wifi') {
////                $('#wifi_type_div').addClass('display-hide');
////                $('#nic_wifi_div').removeClass('display-hide');
////                $('#guest_wifi_div').addClass('display-hide');
////                $('.guest_wifi_div').find('input').val('');
////            }
////        }
//
//
//    });
    var emailregex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    var nameregex = /^[a-zA-Z .,]{1,50}$/;
    var mobileregex = /^[+0-9]{10,13}$/;
    var phoneregex1 = /^[0-9]{3,5}[-]([0-9]{6,15})$/;
    var addregex = /^[a-zA-Z0-9 .,-\\#\\/\\(\\)]{2,100}$/;
    var desigregex = /^[a-zA-Z0-9 \.\,\-\&]{2,50}$/;
    $('#wifi_form2 #fwd_ofc_email').blur(function () {
        var hod_email = $('#wifi_form2 #fwd_ofc_email').val();
        console.log("hod_email" + hod_email)
        var flag = false;
        if (hod_email.match(emailregex) && !flag) {
            $.ajax({
                type: "POST",
                url: "checkbo",
                data: {email: hod_email},
                datatype: JSON,
                success: function (data) {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    var flg = false;
                    if (jsonvalue.error.bocheck === 'false') {
                        $('#wifi_form2 #fwd_ofc_mobile').prop('readonly', false);
                        $('#wifi_form2 #fwd_ofc_mobile').val("");
                        $('#wifi_form2 #fwd_ofc_name').prop('readonly', false);
                        $('#wifi_form2 #fwd_ofc_name').val("");
                        $('#wifi_form2 #fwd_ofc_tel').prop('readonly', false);
                        $('#wifi_form2 #fwd_ofc_tel').val("");
                        $('#wifi_form2 #fwd_ofc_desig').prop('readonly', false);
                        $('#wifi_form2 #fwd_ofc_desig').val("");
                        $('#wifi_form2 #fwd_ofc_add').prop('readonly', false);
                        $('#wifi_form2 #fwd_ofc_add').val("");
                        // $('#wifi_form2 #fwd_email_err1').html('Forwarding officer can only be an NIC Employee.')
                        return false;
                    } else {
                        $('#wifi_form2 #fwd_email_err1').html('.')
                        if (jsonvalue.userValues.email !== "") {
                            $('#wifi_form2 #fwd_ofc_email').val(jsonvalue.userValues.email);
                            if (jsonvalue.userValues.email.match(emailregex))
                            {
                                flg = true;
                            } else {
                                $('#wifi_form2 #fwd_email_err').html("Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                            }
                        } else {
                            $('#wifi_form2 #fwd_email_err').html("Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                            $('#hod_email').val("");
                        }
                        if (jsonvalue.userValues.mobile !== "") {
                            $('#wifi_form2 #fwd_ofc_mobile').val(jsonvalue.userValues.mobile);
                            if (jsonvalue.userValues.mobile.match(mobileregex))
                            {
                                flg = true;
                                $('#wifi_form2 #fwd_ofc_mobile').prop('readonly', true);
                            } else {
                                $('#wifi_form2 #fwd_mobile_err').html("Enter Mobile Number [e.g: +919999999999]");
                                $('#wifi_form2 #fwd_ofc_mobile').prop('readonly', false);
                            }
                        } else {
                            $('#wifi_form2 #fwd_ofc_mobile').val("");
                            $('#wifi_form2 #fwd_ofc_mobile').prop('readonly', false);
                        }
                        if (jsonvalue.userValues.cn !== "") {
                            $('#wifi_form2 #fwd_ofc_name').val(jsonvalue.userValues.cn);
                            if (jsonvalue.userValues.cn.match(nameregex))
                            {
                                flg = true;
                                $('#wifi_form2 #fwd_ofc_name').prop('readonly', true);
                            } else {
                                $('#wifi_form2 #fwd_name_err').html("Enter Name [characters,dot(.) and whitespaces]");
                                $('#wifi_form2 #fwd_ofc_name').prop('readonly', false);
                            }
                        } else {
                            $('#wifi_form2 #fwd_ofc_name').val("");
                            $('#wifi_form2 #fwd_ofc_name').prop('readonly', false);
                        }
                        if (jsonvalue.userValues.ophone !== "") {
                            $('#wifi_form2 #fwd_ofc_tel').val(jsonvalue.userValues.ophone);
                            if (jsonvalue.userValues.ophone.match(phoneregex1))
                            {
                                flg = true;
                                $('#wifi_form2 #fwd_ofc_tel').prop('readonly', true);
                            } else {
                                $('#wifi_form2 #fwd_tel_err').html("Enter Reporting/Nodal/Forwarding Officer Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                                $('#wifi_form2 #fwd_ofc_tel').prop('readonly', false);
                            }
                        } else {
                            $('#wifi_form2 #fwd_ofc_tel').val("");
                            $('#wifi_form2 #fwd_ofc_tel').prop('readonly', false);
                        }
                        if (jsonvalue.userValues.desig !== "" && jsonvalue.userValues.desig !== "null") {

                            $('#wifi_form2 #fwd_ofc_desig').val(jsonvalue.userValues.desig);
                            if (jsonvalue.userValues.desig.match(desigregex))
                            {
                                flg = true;
                                $('#wifi_form2 #fwd_ofc_desig').prop('readonly', true);
                            } else {
                                $('#wifi_form2 #fwd_desig_err').html("Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespaces and [. , - &]]]");
                                $('#wifi_form2 #fwd_ofc_desig').prop('readonly', false);
                            }
                        } else {
                            $('#wifi_form2 #fwd_ofc_desig').val("");
                            $('#wifi_form2 #fwd_ofc_desig').prop('readonly', false);
                        }
                        if (flg) {
                            $('#rodetails').removeClass("display-hide");
                        }
                        //alert("hi")
                        $('#wifi_form2 #fwd_email_err1').html('')
                    }

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
    $('#wifi_form4 #fwd_ofc_email').blur(function () {
        var hod_email = $('#wifi_form4 #fwd_ofc_email').val();
        console.log("hod_email" + hod_email)
        var flag = false;
        if (hod_email.match(emailregex) && !flag) {
            $.ajax({
                type: "POST",
                url: "checkbo",
                data: {email: hod_email},
                datatype: JSON,
                success: function (data) {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    var flg = false;
                    if (jsonvalue.error.bocheck === 'false') {
                        $('#wifi_form4 #fwd_ofc_mobile').prop('readonly', false);
                        $('#wifi_form4 #fwd_ofc_mobile').val("");
                        $('#wifi_form4 #fwd_ofc_name').prop('readonly', false);
                        $('#wifi_form4 #fwd_ofc_name').val("");
                        $('#wifi_form4 #fwd_ofc_tel').prop('readonly', false);
                        $('#wifi_form4 #fwd_ofc_tel').val("");
                        $('#wifi_form4 #fwd_ofc_desig').prop('readonly', false);
                        $('#wifi_form4 #fwd_ofc_desig').val("");
                        $('#wifi_form4 #fwd_ofc_add').prop('readonly', false);
                        $('#wifi_form4 #fwd_ofc_add').val("");
                        //$('#wifi_form4 #fwd_email_err1').html('Forwarding officer can only be an NIC Employee.')

                    } else {
                        $('#wifi_form4 #fwd_email_err1').html('.')
                        if (jsonvalue.userValues.email !== "") {
                            $('#wifi_form4 #fwd_ofc_email').val(jsonvalue.userValues.email);
                            if (jsonvalue.userValues.email.match(emailregex))
                            {
                                flg = true;
                            } else {
                                $('#wifi_form4 #fwd_email_err').html("Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                            }
                        } else {
                            $('#wifi_form4 #fwd_email_err').html("Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                            $('#hod_email').val("");
                        }
                        if (jsonvalue.userValues.mobile !== "") {
                            $('#wifi_form4 #fwd_ofc_mobile').val(jsonvalue.userValues.mobile);
                            if (jsonvalue.userValues.mobile.match(mobileregex))
                            {
                                flg = true;
                                $('#wifi_form4 #fwd_ofc_mobile').prop('readonly', true);
                            } else {
                                $('#wifi_form4 #fwd_mobile_err').html("Enter Mobile Number [e.g: +919999999999]");
                                $('#wifi_form4 #fwd_ofc_mobile').prop('readonly', false);
                            }
                        } else {
                            $('#wifi_form4 #fwd_ofc_mobile').val("");
                            $('#wifi_form4 #fwd_ofc_mobile').prop('readonly', false);
                        }
                        if (jsonvalue.userValues.cn !== "") {
                            $('#wifi_form4 #fwd_ofc_name').val(jsonvalue.userValues.cn);
                            if (jsonvalue.userValues.cn.match(nameregex))
                            {
                                flg = true;
                                $('#wifi_form4 #fwd_ofc_name').prop('readonly', true);
                            } else {
                                $('#wifi_form4 #fwd_name_err').html("Enter Name [characters,dot(.) and whitespaces]");
                                $('#wifi_form4 #fwd_ofc_name').prop('readonly', false);
                            }
                        } else {
                            $('#wifi_form4 #fwd_ofc_name').val("");
                            $('#wifi_form4 #fwd_ofc_name').prop('readonly', false);
                        }
                        if (jsonvalue.userValues.ophone !== "") {
                            $('#wifi_form4 #fwd_ofc_tel').val(jsonvalue.userValues.ophone);
                            if (jsonvalue.userValues.ophone.match(phoneregex1))
                            {
                                flg = true;
                                $('#wifi_form4 #fwd_ofc_tel').prop('readonly', true);
                            } else {
                                $('#wifi_form4 #fwd_tel_err').html("Enter Reporting/Nodal/Forwarding Officer Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                                $('#wifi_form4 #fwd_ofc_tel').prop('readonly', false);
                            }
                        } else {
                            $('#wifi_form4 #fwd_ofc_tel').val("");
                            $('#wifi_form4 #fwd_ofc_tel').prop('readonly', false);
                        }
                        if (jsonvalue.userValues.desig !== "" && jsonvalue.userValues.desig !== "null") {

                            $('#wifi_form4 #fwd_ofc_desig').val(jsonvalue.userValues.desig);
                            if (jsonvalue.userValues.desig.match(desigregex))
                            {
                                flg = true;
                                $('#wifi_form4 #fwd_ofc_desig').prop('readonly', true);
                            } else {
                                $('#wifi_form4 #fwd_desig_err').html("Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespaces and [. , - &]]]");
                                $('#wifi_form4 #fwd_ofc_desig').prop('readonly', false);
                            }
                        } else {
                            $('#wifi_form4 #fwd_ofc_desig').val("");
                            $('#wifi_form4 #fwd_ofc_desig').prop('readonly', false);
                        }
                        if (flg) {
                            $('#rodetails').removeClass("display-hide");
                        }
                        $('#wifi_form2 #fwd_email_err1').html('')
                    }
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
    // wifi form submission

    $('#wifi_form2').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#wifi_form2').serializeObject());
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18
        console.log("data::::::::::" + data)
        $.ajax({
            type: "POST",
            url: "wifi_tab1",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom(); // line added by pr on 23rdjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                $("#wifi_form4 #wifi_DeletePrev_div").html('');
                console.log("AAAAAAABBBBBBBBS" + jsonvalue.mac_list2);
                var error_flag = true;
                if (jsonvalue.error.mac1_error !== null && jsonvalue.error.mac1_error !== "" && jsonvalue.error.mac1_error !== undefined)
                {
                    $('#wifi_mac1_err').html(jsonvalue.error.mac1_error)
                    $('#wifi_form2 #wifi_mac1').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#wifi_mac1_err').html("")
                }
                if (jsonvalue.error.os1_error !== null && jsonvalue.error.os1_error !== "" && jsonvalue.error.os1_error !== undefined)
                {
                    $('#wifi_os1_err').html(jsonvalue.error.os1_error)
                    $('#wifi_form2 #wifi_os1').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#wifi_os1_err').html("")
                }

                // added on 2019-12-19

                if (jsonvalue.error.device1_error !== null && jsonvalue.error.device1_error !== "" && jsonvalue.error.device1_error !== undefined)
                {
                    $('#device_type_err1').html(jsonvalue.error.device1_error)
                    $('#wifi_form2 #device_type1').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#device_type_err1').html("")
                }

                if (jsonvalue.error.device2_error !== null && jsonvalue.error.device2_error !== "" && jsonvalue.error.device2_error !== undefined)
                {
                    $('#device_type_err2').html(jsonvalue.error.device2_error)
                    $('#wifi_form2 #device_type2').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#device_type_err2').html("")
                }

                if (jsonvalue.error.device3_error !== null && jsonvalue.error.device3_error !== "" && jsonvalue.error.device3_error !== undefined)
                {
                    $('#device_type_err3').html(jsonvalue.error.device3_error)
                    $('#wifi_form2 #device_type3').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#device_type_err3').html("")
                }

                // added on 2019-12-19

                if (jsonvalue.error.mac2_error !== null && jsonvalue.error.mac2_error !== "" && jsonvalue.error.mac2_error !== undefined)
                {
                    $('#wifi_mac2_err').html(jsonvalue.error.mac2_error)
                    $('#wifi_form2 #wifi_mac2').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#wifi_mac2_err').html("")
                }

                if (jsonvalue.error.os2_error !== null && jsonvalue.error.os2_error !== "" && jsonvalue.error.os2_error !== undefined)
                {
                    $('#wifi_form2 #wifi_os2_err').html(jsonvalue.error.os2_error)
                    $('#wifi_form2 #wifi_os2').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#wifi_os2_err').html("")
                }
                if (jsonvalue.error.mac3_error !== null && jsonvalue.error.mac3_error !== "" && jsonvalue.error.mac3_error !== undefined)
                {
                    $('#wifi_mac3_err').html(jsonvalue.error.mac3_error)
                    $('#wifi_form2 #wifi_mac3').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#wifi_mac3_err').html("")
                }
                if (jsonvalue.error.os3_error !== null && jsonvalue.error.os3_error !== "" && jsonvalue.error.os3_error !== undefined)
                {
                    $('#wifi_os3_err').html(jsonvalue.error.os3_error)
                    $('#wifi_form2 #wifi_os3').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#wifi_os3_err').html("")
                }

                if (jsonvalue.error.mac4_error !== null && jsonvalue.error.mac4_error !== "" && jsonvalue.error.mac4_error !== undefined)
                {
                    $('#wifi_form2 #wifi_mac4_err').html(jsonvalue.error.mac4_error)
                    $('#wifi_form2 #wifi_mac4').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form2 #wifi_mac4_err').html("")
                }
                if (jsonvalue.error.os4_error !== null && jsonvalue.error.os4_error !== "" && jsonvalue.error.os4_error !== undefined)
                {
                    $('#wifi_form2 #wifi_os4_err').html(jsonvalue.error.os4_error)
                    $('#wifi_form2 #wifi_os4').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form2 #wifi_os4_err').html("")
                }
                if (jsonvalue.error.fwd_mobile_err !== null && jsonvalue.error.fwd_mobile_err !== "" && jsonvalue.error.fwd_mobile_err !== undefined)
                {
                    $('#wifi_form2 #fwd_mobile_err').html(jsonvalue.error.fwd_mobile_err);
                    $('#wifi_form2 #fwd_ofc_mobile').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form2 #imgtxt2').val("");
                } else {
                    $('#wifi_form2 #fwd_mobile_err').html("");
                }
                if (jsonvalue.error.fwd_email_err !== null && jsonvalue.error.fwd_email_err !== "" && jsonvalue.error.fwd_email_err !== undefined)
                {
                    $('#wifi_form2 #fwd_email_err').html(jsonvalue.error.fwd_email_err);
                    $('#wifi_form2 #fwd_ofc_email').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form2 #imgtxt2').val("");
                } else {
                    $('#wifi_form2 #fwd_email_err').html("");
                }

                if (jsonvalue.error.fwd_email_err1 !== null && jsonvalue.error.fwd_email_err1 !== "" && jsonvalue.error.fwd_email_err1 !== undefined)
                {
                    $('#wifi_form2 #fwd_email_err1').html(jsonvalue.error.fwd_email_err1);
                    $('#wifi_form2 #fwd_ofc_email').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form2 #imgtxt2').val("");
                } else {
                    $('#wifi_form2 #fwd_email_err1').html("");
                }


                if (jsonvalue.error.fwd_name_err !== null && jsonvalue.error.fwd_name_err !== "" && jsonvalue.error.fwd_name_err !== undefined)
                {

                    $('#wifi_form2 #fwd_name_err').html(jsonvalue.error.fwd_name_err);
                    $('#wifi_form2 #fwd_ofc_name').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form2 #imgtxt2').val("");
                } else {
                    $('#wifi_form2 #fwd_name_err').html("");
                }
                if (jsonvalue.error.fwd_desig_err !== null && jsonvalue.error.fwd_desig_err !== "" && jsonvalue.error.fwd_desig_err !== undefined)
                {
                    $('#wifi_form2 #fwd_desig_err').html(jsonvalue.error.fwd_desig_err);
                    $('#wifi_form2 #fwd_ofc_desig').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form2 #imgtxt2').val("");
                } else {
                    $('#wifi_form2 #fwd_desig_err').html("");
                }
                if (jsonvalue.error.fwd_add_err !== null && jsonvalue.error.fwd_add_err !== "" && jsonvalue.error.fwd_add_err !== undefined)
                {
                    $('#wifi_form2 #fwd_add_err').html(jsonvalue.error.fwd_add_err);
                    $('#wifi_form2 #fwd_ofc_add').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form2 #imgtxt2').val("");
                } else {
                    $('#wifi_form2 #fwd_add_err').html("");
                }
                if (jsonvalue.error.fwd_tel_err !== null && jsonvalue.error.fwd_tel_err !== "" && jsonvalue.error.fwd_tel_err !== undefined)
                {
                    $('#wifi_form2 #fwd_tel_err').html(jsonvalue.error.fwd_tel_err);
                    $('#wifi_form2 #fwd_ofc_tel').focus();
                    error_flag = false;
                    $('#wifi_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form2 #imgtxt2').val("");
                } else {
                    $('#wifi_form2 #fwd_tel_err').html("");
                }
                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#captchaerror').html("")
                }

                if (jsonvalue.error.deleteMacId_error !== null && jsonvalue.error.deleteMacId_error !== "" && jsonvalue.error.deleteMacId_error !== undefined)
                {
                    $('#deleteMacId_error').html(jsonvalue.error.deleteMacId_error)
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#deleteMacId_error').html("")
                }

                if (checkDuplicates())
                {
                    $('#wifi_form2 #macduperr').html("You entered duplicate MAC address please correct and enter diffrent MAC addresses");
                    error_flag = false;
                    $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else
                {
                    $('#wifi_form2 #macduperr').html("");
                }

                // start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#wifi_form2 #captchaerror').html(jsonvalue.error.csrf_error);
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

                    $('#wifi_form4 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#wifi_form4 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#wifi_form4 #user_name').val(jsonvalue.profile_values.cn);
                    $('#wifi_form4 #user_design').val(jsonvalue.profile_values.desig);
                    $('#wifi_form4 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#wifi_form4 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#wifi_form4 #user_state').val(jsonvalue.profile_values.state);
                    $('#wifi_form4 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#wifi_form4 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#wifi_form4 #user_rphone').val(jsonvalue.profile_values.rphone);
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#wifi_form4 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    $('#wifi_form4 #user_email').val(jsonvalue.profile_values.email);
                    $('#wifi_form4 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#wifi_form4 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#wifi_form4 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#wifi_form4 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#wifi_form4 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    // $('#wifi_form4 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    $('#wifi_form4 #ca_design').val(jsonvalue.profile_values.ca_design);
//                    if (jsonvalue.profile_values.dept == "Department of Electronics and Information Technology (Deity), MyGov Project")
//                    {
//
//
//                        $('#forwarding_div').hide();
//
//                    } else {
//
//                        $('#forwarding_div').show();
//                    }
                    $('#wifi_form4 #fwd_ofc_name').val(jsonvalue.form_details.fwd_ofc_name);
                    $('#wifi_form4 #fwd_ofc_email').val(jsonvalue.form_details.fwd_ofc_email);
                    $('#wifi_form4 #fwd_ofc_tel').val(jsonvalue.form_details.fwd_ofc_tel);
                    $('#wifi_form4 #fwd_ofc_mobile').val(jsonvalue.form_details.fwd_ofc_mobile);
                    $('#wifi_form4 #fwd_ofc_design').val(jsonvalue.form_details.fwd_ofc_design);
                    $('#wifi_form4 #fwd_ofc_add').val(jsonvalue.form_details.fwd_ofc_add);
                    $('#wifi_form4 #nic_wifi_div').removeClass('display-hide');
                    $('#wifi_form4 #guest_wifi_div').addClass('display-hide');
                    $('#wifi_form4 #wifi_mac1').val(jsonvalue.form_details.wifi_mac1);
                    $('#wifi_form4 #wifi_os1').val(jsonvalue.form_details.wifi_os1);
                    if (jsonvalue.form_details.wifi_process == "certificate")
                    {

                        $("#wifi_form4 #wifi_cert").prop('checked', true);
                        $('#wifi_form4 #wifi_req_div').addClass("display-hide")

                    } else if (jsonvalue.form_details.wifi_process == "req_delete")
                    {
                        console.log(jsonvalue.mac_list2)
                        $.each(jsonvalue.mac_list2, function (i, l) {
                            console.log("wifi_mac1" + l.wifi_mac1)
                            $("#wifi_form4 #wifi_DeletePrev_div").append('<div class="row" style="padding: 10px;"><div class="col-md-5"><label class="control-label" for="street">MAC Address of the Device <span style="color: red">*</span></label><input class="form-control" readonly placeholder="Enter MAC Address (e.g: AA:AA:AA:AA:AA:AA)" type="text" name="wifi_mac1' + i + '" id="wifi_mac1"  value="' + l.wifi_mac1 + '" maxlength="17"></div><div class="col-md-5"><label class="control-label" for="street">Operating System of the Device<span style="color: red">*</span></label><input class="form-control" readonly placeholder="Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]" type="text" name="wifi_os1' + i + '" id="wifi_os1"  value="' + l.wifi_os1 + '"></div></div>');
                            i++;
                        });
                        // $("#wifi_form4 #wifi_DeletePrev_div").html('');
                        $("#wifi_form4 #wifi_delete").prop('checked', true);
                        $('#wifi_form4 #wifi_req_div').addClass("display-hide");
                        $('#wifi_form4 #wifi_DeletePrev_div').removeClass("display-hide")
                        $('#large').modal({backdrop: 'static', keyboard: false});
                    } else {

                        $("#wifi_form4 #wifi_req").prop('checked', true);
                        $('#wifi_form4 #wifi_DeletePrev_div').addClass("display-hide");
                        $('#wifi_form4 #wifi_req_div').removeClass("display-hide");
                        console.log("device 1:" + jsonvalue.form_details.device_type1)
                        console.log("device 2:" + jsonvalue.form_details.device_type2)
                        console.log("device 3:" + jsonvalue.form_details.device_type3)
                        console.log("device 4:" + jsonvalue.form_details.device_type4)
                        if (jsonvalue.form_details.device_type1 == "Bio-Metric device") {
                            $("#wifi_form4 #wifi_req_div #device_type1").val(jsonvalue.form_details.device_type1).attr("selected", "selected");
                        } else {
                            $("#wifi_form4 #wifi_req_div #device_type1").val(jsonvalue.form_details.device_type1).attr("selected", "selected");
                        }

                        if (jsonvalue.form_details.wifi_mac2 !== '' && jsonvalue.form_details.wifi_os2 !== '') {
                            $('#wifi_form4 #wifi_mac2_div').removeClass('display-hide');
                            $('#wifi_form4 #wifi_mac2').val(jsonvalue.form_details.wifi_mac2);
                            $('#wifi_form4 #wifi_os2').val(jsonvalue.form_details.wifi_os2);
                            if (jsonvalue.form_details.device_type2 == "Bio-Metric device") {
                                $("#wifi_form4 #wifi_req_div #device_type2").val(jsonvalue.form_details.device_type2).attr("selected", "selected");
                            } else {
                                $("#wifi_form4 #wifi_req_div #device_type2").val(jsonvalue.form_details.device_type2).attr("selected", "selected");
                            }

                        } else {
                            $('#wifi_form4 #wifi_mac2_div').addClass('display-hide');
                        }
                        if (jsonvalue.form_details.wifi_mac3 !== '' && jsonvalue.form_details.wifi_os3 !== '') {
                            $('#wifi_form4 #wifi_mac3_div').removeClass('display-hide');
                            $('#wifi_form4 #wifi_mac3').val(jsonvalue.form_details.wifi_mac3);
                            $('#wifi_form4 #wifi_os3').val(jsonvalue.form_details.wifi_os3);
                            if (jsonvalue.form_details.device_type3 == "Bio-Metric device") {
                                $("#wifi_form4 #wifi_req_div #device_type3").val(jsonvalue.form_details.device_type3).attr("selected", "selected");
                            } else {
                                $("#wifi_form4 #wifi_req_div #device_type3").val(jsonvalue.form_details.device_type3).attr("selected", "selected");
                            }
                        } else {
                            $('#wifi_form4 #wifi_mac3_div').addClass('display-hide');
                        }
                        //************  add by mohit sharma

                        if (jsonvalue.form_details.wifi_mac4 !== '' && jsonvalue.form_details.wifi_os4 !== '') {
                            $('#wifi_form4 #wifi_mac4_div').removeClass('display-hide');
                            $('#wifi_form4 #wifi_mac4').val(jsonvalue.form_details.wifi_mac4);
                            $('#wifi_form4 #wifi_os4').val(jsonvalue.form_details.wifi_os4);
                            if (jsonvalue.form_details.device_type4 == "Bio-Metric device") {
                                $("#wifi_form4 #wifi_req_div #device_type4").val(jsonvalue.form_details.device_type4).attr("selected", "selected");
                            } else {
                                $("#wifi_form4 #wifi_req_div #device_type4").val(jsonvalue.form_details.device_type4).attr("selected", "selected");
                            }

                        } else {
                            $('#wifi_form4 #wifi_mac4_div').addClass('display-hide');
                        }



                    }
                    $('#wifi_form4 #confirm').val("nic");
                    $('.edit').removeClass('display-hide');
                    $('#wifi_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#wifi_form4 :button[type='button']").removeAttr('disabled');
                    $("#wifi_form4 #tnc").removeAttr('disabled');
                    $("#stack2 #nic_tnc").removeClass('display-hide');
                    $("#stack2 #guest_tnc").addClass('display-hide');
                    //alert(jsonvalue.form_details.wifi_bo)

                    //$("#wifi_form4 #wifi_DeletePrev_div").html('');

                    // $('#large').modal({backdrop: 'static', keyboard: false});
                    if (jsonvalue.form_details.wifi_process == "request")
                    {
                        if ((jsonvalue.form_details.wifi_os1.toLocaleString().toLocaleLowerCase() === "ios" && jsonvalue.form_details.wifi_bo == "2") || (jsonvalue.form_details.wifi_os1.toLocaleString().toLocaleLowerCase() === "apple" && jsonvalue.form_details.wifi_bo == "2") || (jsonvalue.form_details.wifi_os2.toLocaleString().toLocaleLowerCase() === "ios" && jsonvalue.form_details.wifi_bo == "2") || (jsonvalue.form_details.wifi_os2.toLocaleString().toLocaleLowerCase() === "apple" && jsonvalue.form_details.wifi_bo == "2") || (jsonvalue.form_details.wifi_os3.toLocaleString().toLocaleLowerCase() === "ios" && jsonvalue.form_details.wifi_bo == "2") || (jsonvalue.form_details.wifi_os3.toLocaleString().toLocaleLowerCase() === "apple" && jsonvalue.form_details.wifi_bo == "2"))
                        {

                            bootbox.dialog({
                                message: "<p style='text-align: center; font-size: 18px;'>Since one of your devices are iOS/Apple, so Wi-Fi Certificate needs to be generated or if Certificate for  Wi-Fi has already been created then same can be used.New Certificate creation will take some time. Thanks for your patience.</p>",
                                title: "Information",
                                buttons: {
                                    cancel: {
                                        label: "OK",
                                        className: 'btn-info',
                                        callback: function () {

                                            $('#large').modal({backdrop: 'static', keyboard: false});
                                        }
                                    }
                                }
                            });
                        } else {

                            $('#large').modal({backdrop: 'static', keyboard: false});
                        }
                    } else {

                        $('#large').modal({backdrop: 'static', keyboard: false});
                    }
                }

            }, error: function ()
            {
                console.log("inside error");
            }
        });
    });
    $('#wifi_form3').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#wifi_form3').serializeObject());
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18
        $.ajax({
            type: "POST",
            url: "wifi_tab2",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom(); // line added by pr on 23rdjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                // profile

                if (jsonvalue.error.tduration_error !== null && jsonvalue.error.tduration_error !== "" && jsonvalue.error.tduration_error !== undefined)
                {
                    $('#wifi_form3 #wifi_duration_err').html(jsonvalue.error.tduration_error)
                    //$('#wifi_form3 #wifi_time').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form3 #wifi_duration_err').html("")
                }
                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#wifi_form3 #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                } else {
                    $('#wifi_form3 #captchaerror').html("")
                }

                // start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#wifi_form3 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 23rdjan18

                if (!error_flag)
                {

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
                    $('#wifi_form4 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#wifi_form4 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#wifi_form4 #user_name').val(jsonvalue.profile_values.cn);
                    $('#wifi_form4 #user_design').val(jsonvalue.profile_values.desig);
                    $('#wifi_form4 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#wifi_form4 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#wifi_form4 #user_state').val(jsonvalue.profile_values.state);
                    $('#wifi_form4 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#wifi_form4 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#wifi_form4 #user_rphone').val(jsonvalue.profile_values.rphone);
                    $('#wifi_form4 #user_mobile').val(jsonvalue.profile_values.mobile);
                    $('#wifi_form4 #user_email').val(jsonvalue.profile_values.email);
                    $('#wifi_form4 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#wifi_form4 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#wifi_form4 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#wifi_form4 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    $('#wifi_form4 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    $('#wifi_form4 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $('#wifi_form4 #nic_wifi_div').addClass('display-hide');
                    $('#wifi_form4 #guest_wifi_div').removeClass('display-hide');
                    $("#wifi_form4 input[name='wifi_request']").prop('checked', false);
                    if (jsonvalue.form_details.wifi_request === 'laptop') {
                        $('#wifi_form4 #wifi_request1').prop('checked', true);
                    } else if (jsonvalue.form_details.wifi_request === 'mobile') {
                        $('#wifi_form4 #wifi_request2').prop('checked', true);
                    } else {
                        $('#wifi_form4 #wifi_request3').prop('checked', true);
                    }
                    $('#wifi_form4 #wifi_time').val(jsonvalue.form_details.wifi_time);
                    $("#wifi_form4 input[name='wifi_duration']").prop('checked', false);
                    if (jsonvalue.form_details.wifi_duration === 'days') {
                        $('#wifi_form4 #wifi_duration1').prop('checked', true);
                    } else {
                        $('#wifi_form4 #wifi_duration2').prop('checked', true);
                    }
                    $('#wifi_form4 #confirm').val("guest");
                    $('.edit').removeClass('display-hide');
                    $('#wifi_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#wifi_form4 :button[type='button']").removeAttr('disabled');
                    $("#wifi_form4 #tnc").removeAttr('disabled');
                    $("#stack2 #guest_tnc").removeClass('display-hide');
                    $("#stack2 #nic_tnc").addClass('display-hide');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                }
            }, error: function ()
            {
                console.log("inside error");
            }
        });
    });
    $('#wifi_form4').submit(function (e) {
        e.preventDefault();
        $("#wifi_form4 :disabled").removeAttr('disabled');
        $('#wifi_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifi_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifi_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifi_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#wifi_form4').serializeObject());
        $('#wifi_preview_tab #user_email').prop('disabled', 'true'); // 20th march


        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "wifi_tab3",
            data: {data: data, action_type: "confirm", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom(); // line added by pr on 23rdjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.mac1_error !== null && jsonvalue.error.mac1_error !== "" && jsonvalue.error.mac1_error !== undefined)
                {
                    $('#wifi_form4 #wifi_mac1_err').html(jsonvalue.error.mac1_error)
                    $('#wifi_form4 #wifi_mac1').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_mac1_err').html("")
                }
                if (jsonvalue.error.os1_error !== null && jsonvalue.error.os1_error !== "" && jsonvalue.error.os1_error !== undefined)
                {
                    $('#wifi_form4 #wifi_os1_err').html(jsonvalue.error.os1_error)
                    $('#wifi_form4 #wifi_os1').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_os1_err').html("")
                }
                if (jsonvalue.error.mac2_error !== null && jsonvalue.error.mac2_error !== "" && jsonvalue.error.mac2_error !== undefined)
                {
                    $('#wifi_form4 #wifi_mac2_err').html(jsonvalue.error.mac2_error)
                    $('#wifi_form4 #wifi_mac2').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_mac2_err').html("")
                }
                if (jsonvalue.error.wifi_os2 !== null && jsonvalue.error.wifi_os2 !== "" && jsonvalue.error.wifi_os2 !== undefined)
                {
                    $('#wifi_form4 #wifi_os2_err').html(jsonvalue.error.os2_error)
                    $('#wifi_form4 #wifi_os2').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_os2_err').html("")
                }
                if (jsonvalue.error.mac3_error !== null && jsonvalue.error.mac3_error !== "" && jsonvalue.error.mac3_error !== undefined)
                {
                    $('#wifi_form4 #wifi_mac3_err').html(jsonvalue.error.mac3_error)
                    $('#wifi_form4 #wifi_mac3').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_mac3_err').html("")
                }
                if (jsonvalue.error.os3_error !== null && jsonvalue.error.os3_error !== "" && jsonvalue.error.os3_error !== undefined)
                {
                    $('#wifi_form4 #wifi_os3_err').html(jsonvalue.error.os3_error)
                    $('#wifi_form4 #wifi_os3').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_os3_err').html("")
                }

                // ****************added by mohit sharma*****************

                if (jsonvalue.error.mac4_error !== null && jsonvalue.error.mac4_error !== "" && jsonvalue.error.mac4_error !== undefined)
                {
                    $('#wifi_form4 #wifi_mac4_err').html(jsonvalue.error.mac4_error)
                    $('#wifi_form4 #wifi_mac4').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_mac4_err').html("")
                }
                if (jsonvalue.error.os4_error !== null && jsonvalue.error.os4_error !== "" && jsonvalue.error.os4_error !== undefined)
                {
                    $('#wifi_form4 #wifi_os4_err').html(jsonvalue.error.os4_error)
                    $('#wifi_form4 #wifi_os4').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_os4_err').html("")
                }


                if (jsonvalue.error.tduration_error !== null && jsonvalue.error.tduration_error !== "" && jsonvalue.error.tduration_error !== undefined)
                {
                    $('#wifi_form4 #wifi_duration_err').html(jsonvalue.error.tduration_error)
                    $('#wifi_form4 #wifi_time').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_duration_err').html("")
                }

                if (jsonvalue.error.fwd_email_err !== null && jsonvalue.error.fwd_email_err !== "" && jsonvalue.error.fwd_email_err !== undefined)
                {
                    $('#wifi_form2 #fwd_email_err').html(jsonvalue.error.fwd_email_err);
                    $('#wifi_form2 #fwd_ofc_email').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form2 #fwd_email_err').html("");
                }

                if (jsonvalue.error.fwd_email_err1 !== null && jsonvalue.error.fwd_email_err1 !== "" && jsonvalue.error.fwd_email_err1 !== undefined)
                {
                    $('#wifi_form2 #fwd_email_err1').html(jsonvalue.error.fwd_email_err1);
                    $('#wifi_form2 #fwd_ofc_email').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form2 #fwd_email_err1').html("");
                }


                if (jsonvalue.error.fwd_name_err !== null && jsonvalue.error.fwd_name_err !== "" && jsonvalue.error.fwd_name_err !== undefined)
                {

                    $('#wifi_form2 #fwd_name_err').html(jsonvalue.error.fwd_name_err);
                    $('#wifi_form2 #fwd_ofc_name').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form2 #fwd_name_err').html("");
                }
                if (jsonvalue.error.fwd_desig_err !== null && jsonvalue.error.fwd_desig_err !== "" && jsonvalue.error.fwd_desig_err !== undefined)
                {
                    $('#wifi_form2 #fwd_desig_err').html(jsonvalue.error.fwd_desig_err);
                    $('#wifi_form2 #fwd_ofc_desig').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form2 #fwd_desig_err').html("");
                }
                if (jsonvalue.error.fwd_add_err !== null && jsonvalue.error.fwd_add_err !== "" && jsonvalue.error.fwd_add_err !== undefined)
                {
                    $('#wifi_form2 #fwd_add_err').html(jsonvalue.error.fwd_add_err);
                    $('#wifi_form2 #fwd_ofc_add').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form2 #fwd_add_err').html("");
                }
                if (jsonvalue.error.fwd_tel_err !== null && jsonvalue.error.fwd_tel_err !== "" && jsonvalue.error.fwd_tel_err !== undefined)
                {
                    $('#wifi_form2 #fwd_tel_err').html(jsonvalue.error.fwd_tel_err);
                    $('#wifi_form2 #fwd_ofc_tel').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form2 #fwd_tel_err').html("");
                }
                if (jsonvalue.error.fwd_mobile_err !== null && jsonvalue.error.fwd_mobile_err !== "" && jsonvalue.error.fwd_mobile_err !== undefined)
                {
                    $('#wifi_form4 #fwd_mobile_err').html(jsonvalue.error.fwd_mobile_err);
                    $('#wifi_form4 #fwd_mobile_err').focus();
                    error_flag = false;
                    $('#wifi_form4 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form4 #imgtxt2').val("");
                } else {
                    $('#wifi_form4 #fwd_mobile_err').html("");
                }


                if (checkDuplicatesprvw())
                {
                    $('#wifi_form4 #macduperr').html("You entered duplicate MAC address please correct and enter diffrent MAC addresses");
                    error_flag = false;
                } else
                {
                    $('#wifi_form4 #macduperr').html("");
                }

                // start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#wifi_form4 #tnc').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#wifi_form4 #useremployment_error').focus();
                    $('#wifi_form4 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#wifi_form4 #minerror').focus();
                    $('#wifi_form4 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#wifi_form4 #deperror').focus();
                    $('#wifi_form4 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#wifi_form4 #other_dept').focus();
                    $('#wifi_form4 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#wifi_form4 #smierror').focus();
                    $('#wifi_form4 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#wifi_form4 #state_error').focus();
                    $('#wifi_form4 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#wifi_form4 #org_error').focus();
                    $('#wifi_form4 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#wifi_form4 #ca_design').focus();
                    $('#wifi_form4 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#wifi_form4 #hod_name').focus();
                    $('#wifi_form4 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#wifi_form4 #hod_mobile').focus();
                    $('#wifi_form4 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#wifi_form4 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#wifi_form4 #hod_tel').focus();
                    $('#wifi_form4 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #hodtel_error').html("");
                }
                // profile on 20th march

                if (error_flag) {
                    // show modal
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'wifi'},
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
    $('#wifi_form4 .edit').click(function () {
        var employment = $('#wifi_preview_tab #user_employment').val();
        var min = $('#wifi_preview_tab #min').val();
        var dept = $('#wifi_preview_tab #dept').val();
        var statecode = $('#wifi_preview_tab #stateCode').val();
        var Smi = $('#wifi_preview_tab #Smi').val();
        var Org = $('#wifi_preview_tab #Org').val();
        var wifi_process = $('#wifi_preview_tab input[name=wifi_process]:checked').val();
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifi_preview_tab #min');
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
                var select = $('#wifi_preview_tab #dept');
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
                $('#wifi_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifi_preview_tab #stateCode');
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
                var select = $('#wifi_preview_tab #Smi');
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
                $('#wifi_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifi_preview_tab #Org');
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
                $('#wifi_preview_tab #other_text_div').removeClass("display-hide");
                $('#wifi_preview_tab #other_dept').val(dept);
            }


        } else {
            $('#wifi_preview_tab #central_div').hide();
            $('#wifi_preview_tab #state_div').hide();
            $('#wifi_preview_tab #other_div').hide();
        }




        $('#wifi_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifi_preview_tab #wifi_process_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifi_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#wifi_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifi_preview_tab #REditPreview #hod_email').removeAttr('disabled');
        $(this).addClass('display-hide');
        // $(this).hide();
    });
    $('#wifi_form4 #confirm').click(function () {
        $("#wifi_form4 :disabled").removeAttr('disabled');
        $('#wifi_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifi_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifi_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifi_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#wifi_form4').serializeObject());
        $('#wifi_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "wifi_tab3",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom(); // line added by pr on 23rdjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.mac1_error !== null && jsonvalue.error.mac1_error !== "" && jsonvalue.error.mac1_error !== undefined)
                {
                    $('#wifi_form4 #wifi_mac1_err').html(jsonvalue.error.mac1_error)
                    $('#wifi_form4 #wifi_mac1').focus();
                    error_flag = false;
                } else {

                    $('#wifi_form4 #wifi_mac1_err').html("")
                }
                if (jsonvalue.error.os1_error !== null && jsonvalue.error.os1_error !== "" && jsonvalue.error.os1_error !== undefined)
                {

                    $('#wifi_form4 #wifi_os1_err').html(jsonvalue.error.os1_error)
                    $('#wifi_form4 #wifi_os1').focus();
                    error_flag = false;
                } else {

                    $('#wifi_form4 #wifi_os1_err').html("")
                }
                if (jsonvalue.error.mac2_error !== null && jsonvalue.error.mac2_error !== "" && jsonvalue.error.mac2_error !== undefined)
                {
                    $('#wifi_form4 #wifi_mac2_err').html(jsonvalue.error.mac2_error)
                    $('#wifi_form4 #wifi_mac2').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_mac2_err').html("")
                }
                if (jsonvalue.error.wifi_os2 !== null && jsonvalue.error.wifi_os2 !== "" && jsonvalue.error.wifi_os2 !== undefined)
                {
                    $('#wifi_form4 #wifi_os2_err').html(jsonvalue.error.os2_error)
                    $('#wifi_form4 #wifi_os2').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_os2_err').html("")
                }
                if (jsonvalue.error.mac3_error !== null && jsonvalue.error.mac3_error !== "" && jsonvalue.error.mac3_error !== undefined)
                {
                    $('#wifi_form4 #wifi_mac3_err').html(jsonvalue.error.mac3_error)
                    $('#wifi_form4 #wifi_mac3').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_mac3_err').html("")
                }
                if (jsonvalue.error.os3_error !== null && jsonvalue.error.os3_error !== "" && jsonvalue.error.os3_error !== undefined)
                {
                    $('#wifi_form4 #wifi_os3_err').html(jsonvalue.error.os3_error)
                    $('#wifi_form4 #wifi_os3').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_os3_err').html("")
                }
                if (jsonvalue.error.os4_error !== null && jsonvalue.error.os4_error !== "" && jsonvalue.error.os4_error !== undefined)
                {
                    $('#wifi_form4 #wifi_os4_err').html(jsonvalue.error.os4_error)
                    $('#wifi_form4 #wifi_os4').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_os4_err').html("")
                }
                if (jsonvalue.error.tduration_error !== null && jsonvalue.error.tduration_error !== "" && jsonvalue.error.tduration_error !== undefined)
                {
                    $('#wifi_form4 #wifi_duration_err').html(jsonvalue.error.tduration_error)
                    $('#wifi_form4 #wifi_time').focus();
                    error_flag = false;
                } else {
                    $('#wifi_form4 #wifi_duration_err').html("")
                }

                if (jsonvalue.error.fwd_mobile_err !== null && jsonvalue.error.fwd_mobile_err !== "" && jsonvalue.error.fwd_mobile_err !== undefined)
                {
                    $('#wifi_form4 #fwd_mobile_err').html(jsonvalue.error.fwd_mobile_err);
                    $('#wifi_form4 #fwd_mobile_err').focus();
                    error_flag = false;
                    $('#wifi_form4 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form4 #imgtxt2').val("");
                } else {
                    $('#wifi_form4 #fwd_mobile_err').html("");
                }
                if (jsonvalue.error.fwd_email_err !== null && jsonvalue.error.fwd_email_err !== "" && jsonvalue.error.fwd_email_err !== undefined)
                {
                    $('#wifi_form4 #fwd_email_err').html(jsonvalue.error.fwd_email_err);
                    $('#wifi_form4 #fwd_email_err').focus();
                    error_flag = false;
                    $('#wifi_form4 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form4 #imgtxt2').val("");
                } else {
                    $('#wifi_form4 #fwd_email_err').html("");
                }
                if (jsonvalue.error.fwd_email_err1 !== null && jsonvalue.error.fwd_email_err1 !== "" && jsonvalue.error.fwd_email_err1 !== undefined)
                {
                    $('#wifi_form4 #fwd_email_err1').html(jsonvalue.error.fwd_email_err1);
                    $('#wifi_form4 #fwd_email_err1').focus();
                    error_flag = false;
                    $('#wifi_form4 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form4 #imgtxt2').val("");
                } else {
                    $('#wifi_form4 #fwd_email_err1').html("");
                }
                if (jsonvalue.error.fwd_name_err !== null && jsonvalue.error.fwd_name_err !== "" && jsonvalue.error.fwd_name_err !== undefined)
                {

                    $('#wifi_form4 #fwd_name_err').html(jsonvalue.error.fwd_name_err);
                    $('#wifi_form4 #fwd_name_err').focus();
                    error_flag = false;
                    $('#wifi_form4 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form4 #imgtxt2').val("");
                } else {
                    $('#wifi_form4 #fwd_name_err').html("");
                }
                if (jsonvalue.error.fwd_desig_err !== null && jsonvalue.error.fwd_desig_err !== "" && jsonvalue.error.fwd_desig_err !== undefined)
                {
                    $('#wifi_form4 #fwd_desig_err').html(jsonvalue.error.fwd_desig_err);
                    $('#wifi_form4 #fwd_desig_err').focus();
                    error_flag = false;
                    $('#wifi_form4 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form4 #imgtxt2').val("");
                } else {
                    $('#wifi_form4 #fwd_desig_err').html("");
                }
                if (jsonvalue.error.fwd_add_err !== null && jsonvalue.error.fwd_add_err !== "" && jsonvalue.error.fwd_add_err !== undefined)
                {
                    $('#wifi_form4 #fwd_add_err').html(jsonvalue.error.fwd_add_err);
                    $('#wifi_form4 #fwd_add_err').focus();
                    error_flag = false;
                    $('#wifi_form4 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form4 #imgtxt2').val("");
                } else {
                    $('#wifi_form4 #fwd_add_err').html("");
                }
                if (jsonvalue.error.fwd_tel_err !== null && jsonvalue.error.fwd_tel_err !== "" && jsonvalue.error.fwd_tel_err !== undefined)
                {
                    $('#wifi_form4 #fwd_tel_err').html(jsonvalue.error.fwd_tel_err);
                    $('#wifi_form4 #fwd_tel_err').focus();
                    error_flag = false;
                    $('#wifi_form4 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_form4 #imgtxt2').val("");
                } else {
                    $('#wifi_form4 #fwd_tel_err').html("");
                }
                if (checkDuplicatesprvw())
                {
                    $('#wifi_form4 #macduperr').html("You entered duplicate MAC address please correct and enter diffrent MAC addresses");
                    error_flag = false;
                } else
                {
                    $('#wifi_form4 #macduperr').html("");
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
                    $('#wifi_form4 #useremployment_error').focus();
                    $('#wifi_form4 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#wifi_form4 #minerror').focus();
                    $('#wifi_form4 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#wifi_form4 #deperror').focus();
                    $('#wifi_form4 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#wifi_form4 #other_dept').focus();
                    $('#wifi_form4 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#wifi_form4 #smierror').focus();
                    $('#wifi_form4 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#wifi_form4 #state_error').focus();
                    $('#wifi_form4 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#wifi_form4 #org_error').focus();
                    $('#wifi_form4 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#wifi_form4 #ca_design').focus();
                    $('#wifi_form4 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#wifi_form4 #hod_name').focus();
                    $('#wifi_form4 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#wifi_form4 #hod_mobile').focus();
                    $('#wifi_form4 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#wifi_form4 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#wifi_form4 #hod_tel').focus();
                    $('#wifi_form4 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#wifi_form4 #hodtel_error').html("");
                }
                // profile on 20th march
                if (!error_flag) {
                    $("#wifi_form4 :disabled").removeAttr('disabled');
                    $('#wifi_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#wifi_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#wifi_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {
                    if ($('#wifi_form4 #tnc').is(":checked"))
                    {

                        if (jsonvalue.form_details.wifi_process == "certificate")
                        {

                            $('#wifi_form_confirm #wifi_details1').addClass("display-hide");
                            $('#wifi_form_confirm #wifi_details2').removeClass("display-hide");
                        } else if (jsonvalue.form_details.wifi_process == "request") {

                            $('#wifi_form_confirm #wifi_details2').addClass("display-hide");
                            $('#wifi_form_confirm #wifi_details1').removeClass("display-hide");
                        }
                        // alert(jsonvalue.form_details.wifi_process)

                        $('#wifi_form4 #tnc_error').html("");
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                        console.log(jsonvalue.form_details)
                        if (jsonvalue.form_details.dept == "Department of Electronics and Information Technology (Deity), MyGov Project")
                        {
                            $('#wifi_form_confirm #details_ro').html("Reporting/Nodal/Forwarding Officer Details")
                            $('#wifi_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#wifi_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#wifi_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                        } 
                        
//                        if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
//                        {
//                             $('#wifi_form_confirm #details_ro').html("Forwarding Officer Details")
//                            $('#wifi_form_confirm #fill_hod_name').html(jsonvalue.profile_values.fwd_ofc_name)
//                            $('#wifi_form_confirm #fill_hod_email').html(jsonvalue.profile_values.fwd_ofc_email)
//                            var startMobile = jsonvalue.profile_values.fwd_ofc_mobile.substring(0, 3);
//                            var endMobile = jsonvalue.profile_values.fwd_ofc_mobile.substring(10, 13);
//                            $('#wifi_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile)
//
//
//                        }
//                        
//                        else if (jsonvalue.form_details.min == "External Affairs")
//                        {
//                            $('#wifi_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#wifi_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#wifi_form_confirm #fill_hod_mobile').html("+919384664224");
//                        }
                       // else {



//                            if (jsonvalue.form_details.fwd_ofc_email != "")
//                            {
//                                $('#wifi_form_confirm #details_ro').html("Forwarding Officer Details")
//                                $('#wifi_form_confirm #fill_hod_name').html(jsonvalue.form_details.fwd_ofc_name)
//                                $('#wifi_form_confirm #fill_hod_email').html(jsonvalue.form_details.fwd_ofc_email)
//                                $('#wifi_form_confirm #fill_hod_mobile').html(jsonvalue.form_details.fwd_ofc_mobile)
//
//
//                            } else {
                            $('#wifi_form_confirm #details_ro').html("Forwarding Officer Details")
                            $('#wifi_form_confirm #fill_hod_name').html(jsonvalue.profile_values.fwd_ofc_name)
                            $('#wifi_form_confirm #fill_hod_email').html(jsonvalue.profile_values.fwd_ofc_email)
                            var startMobile = jsonvalue.profile_values.fwd_ofc_mobile.substring(0, 3);
                            var endMobile = jsonvalue.profile_values.fwd_ofc_mobile.substring(10, 13);
                            $('#wifi_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile)
                            // }

                     //   }
                        $('#wifi_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    } else {
                        $('#wifi_form4 #tnc_error').html("Please agree to Terms and Conditions");
                        $("#wifi_form4 #tnc").removeAttr('disabled');
                        $("#wifi_form4 :disabled").removeAttr('disabled');
                        $('#wifi_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#wifi_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        $('#wifi_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    }
                }
            }
        });
    });
    $('#wifi_form_confirm #confirmYes').click(function () {
        $('#wifi_form4').submit();
        $('#stack3').modal('toggle');
    });
    $(document).on('click', '#wifi_preview .edit', function () {
        //$('#wifi_preview .edit').click(function () {
        var employment = $('#wifi_preview_tab #user_employment').val();
        var min = $('#wifi_preview_tab #min').val();
        var dept = $('#wifi_preview_tab #dept').val();
        var statecode = $('#wifi_preview_tab #stateCode').val();
        var Smi = $('#wifi_preview_tab #Smi').val();
        var Org = $('#wifi_preview_tab #Org').val();
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifi_preview_tab #min');
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
                var select = $('#wifi_preview_tab #dept');
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
                $('#wifi_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifi_preview_tab #stateCode');
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
                var select = $('#wifi_preview_tab #Smi');
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
                if (response.toString().toLowerCase().indexOf("Other") == -1)
                {

                    if (response.toString().toLowerCase().indexOf("other") == -1 && Smi == "other") {
                        $('<option selected="selected">').val("other").text("other").appendTo(select);
                    } else {
                        $('<option>').val("other").text("other").appendTo(select);
                    }
                }

            });
            if (dept === 'other') {
                $('#wifi_preview_tab #other_text_div').removeClass("display-hide");
                $('#wifi_preview_tab #other_dept').val(dept);
            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifi_preview_tab #Org');
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
                $('#wifi_preview_tab #other_text_div').removeClass("display-hide");
                $('#wifi_preview_tab #other_dept').val(dept);
            }


        } else {
            $('#wifi_preview_tab #central_div').hide();
            $('#wifi_preview_tab #state_div').hide();
            $('#wifi_preview_tab #other_div').hide();
        }
        $('#wifi_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#wifi_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#wifi_preview_tab #wifi_process_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifi_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifi_preview_tab #forwarding_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifi_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            

        if ($("#comingFrom").val("admin"))
        {
            $("#wifi_preview .save-changes").removeClass('display-hide'); // line added by pr on 25thjan18
            $("#wifi_preview .save-changes").html("Update");
        }
        // code end
    });
    $(document).on('click', '#wifi_preview #confirm', function () {
        // $('#wifi_preview #confirm').click(function () {
        $('#wifi_preview').submit();
        // $('#wifi_preview_form').modal('toggle');
    });
    $('#wifi_preview').submit(function (e) {

        e.preventDefault();
        $("#wifi_preview :disabled").removeAttr('disabled');
        $('#wifi_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifi_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
        $('#wifi_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifi_preview_tab #user_email').removeAttr('disabled'); // 20th march  
        var data = JSON.stringify($('#wifi_preview').serializeObject());
        $('#wifi_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        $('#wifi_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "wifi_tab3",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom(); // line added by pr on 23rdjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                console.log("MOhit--sahil :::::::::::" + jsonvalue.formDetails.wifi_mac4);
                if (jsonvalue.error.mac1_error !== null && jsonvalue.error.mac1_error !== "" && jsonvalue.error.mac1_error !== undefined)
                {
                    $('#wifi_preview #wifi_mac1_err').html(jsonvalue.error.mac1_error)
                    $('#wifi_preview #wifi_mac1').focus();
                    error_flag = false;
                } else {
                    $('#wifi_preview #wifi_mac1_err').html("")
                }
                if (jsonvalue.error.os1_error !== null && jsonvalue.error.os1_error !== "" && jsonvalue.error.os1_error !== undefined)
                {
                    $('#wifi_preview #wifi_os1_err').html(jsonvalue.error.os1_error)
                    $('#wifi_preview #wifi_os1').focus();
                    error_flag = false;
                } else {
                    $('#wifi_preview #wifi_os1_err').html("")
                }
                if (jsonvalue.error.mac2_error !== null && jsonvalue.error.mac2_error !== "" && jsonvalue.error.mac2_error !== undefined)
                {
                    $('#wifi_preview #wifi_mac2_err').html(jsonvalue.error.mac2_error)
                    $('#wifi_preview #wifi_mac2').focus();
                    error_flag = false;
                } else {
                    $('#wifi_preview #wifi_mac2_err').html("")
                }
                if (jsonvalue.error.wifi_os2 !== null && jsonvalue.error.wifi_os2 !== "" && jsonvalue.error.wifi_os2 !== undefined)
                {
                    $('#wifi_preview #wifi_os2_err').html(jsonvalue.error.os2_error)
                    $('#wifi_preview #wifi_os2').focus();
                    error_flag = false;
                } else {
                    $('#wifi_preview #wifi_os2_err').html("")
                }
                if (jsonvalue.error.mac3_error !== null && jsonvalue.error.mac3_error !== "" && jsonvalue.error.mac3_error !== undefined)
                {
                    $('#wifi_preview #wifi_mac3_err').html(jsonvalue.error.mac3_error)
                    $('#wifi_preview #wifi_mac3').focus();
                    error_flag = false;
                } else {
                    $('#wifi_preview #wifi_mac3_err').html("")
                }
                if (jsonvalue.error.os3_error !== null && jsonvalue.error.os3_error !== "" && jsonvalue.error.os3_error !== undefined)
                {
                    $('#wifi_preview #wifi_os3_err').html(jsonvalue.error.os3_error)
                    $('#wifi_preview #wifi_os3').focus();
                    error_flag = false;
                } else {
                    $('#wifi_preview #wifi_os3_err').html("")
                }


                //------------added by mohit sharma -----------------------------

                if (jsonvalue.error.mac4_error !== null && jsonvalue.error.mac4_error !== "" && jsonvalue.error.mac4_error !== undefined)
                {
                    $('#wifi_preview #wifi_mac4_err').html(jsonvalue.error.mac4_error)
                    $('#wifi_preview #wifi_mac4').focus();
                    error_flag = false;
                } else {
                    $('#wifi_preview #wifi_mac4_err').html("")
                }
                if (jsonvalue.error.os4_error !== null && jsonvalue.error.os4_error !== "" && jsonvalue.error.os4_error !== undefined)
                {
                    $('#wifi_preview #wifi_os4_err').html(jsonvalue.error.os4_error)
                    $('#wifi_preview #wifi_os4').focus();
                    error_flag = false;
                } else {
                    $('#wifi_preview #wifi_os4_err').html("")
                }



                if (jsonvalue.error.tduration_error !== null && jsonvalue.error.tduration_error !== "" && jsonvalue.error.tduration_error !== undefined)
                {
                    $('#wifi_preview #wifi_duration_err').html(jsonvalue.error.tduration_error)
                    $('#wifi_preview #wifi_time').focus();
                    error_flag = false;
                } else {
                    $('#wifi_preview #wifi_duration_err').html("")
                }

                if (jsonvalue.error.fwd_mobile_err !== null && jsonvalue.error.fwd_mobile_err !== "" && jsonvalue.error.fwd_mobile_err !== undefined)
                {
                    $('#wifi_preview #fwd_mobile_err').html(jsonvalue.error.fwd_mobile_err);
                    $('#wifi_preview #fwd_mobile_err').focus();
                    error_flag = false;
                    $('#wifi_preview #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_preview #imgtxt2').val("");
                } else {
                    $('#wifi_preview #fwd_mobile_err').html("");
                }
                if (jsonvalue.error.fwd_email_err !== null && jsonvalue.error.fwd_email_err !== "" && jsonvalue.error.fwd_email_err !== undefined)
                {
                    $('#wifi_preview #fwd_email_err').html(jsonvalue.error.fwd_email_err);
                    $('#wifi_preview #fwd_email_err').focus();
                    error_flag = false;
                    $('#wifi_preview #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_preview #imgtxt2').val("");
                } else {
                    $('#wifi_preview #fwd_email_err').html("");
                }
                if (jsonvalue.error.fwd_name_err !== null && jsonvalue.error.fwd_name_err !== "" && jsonvalue.error.fwd_name_err !== undefined)
                {

                    $('#wifi_preview #fwd_name_err').html(jsonvalue.error.fwd_name_err);
                    $('#wifi_preview #fwd_name_err').focus();
                    error_flag = false;
                    $('#wifi_preview #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_preview #imgtxt2').val("");
                } else {
                    $('#wifi_preview #fwd_name_err').html("");
                }
                if (jsonvalue.error.fwd_desig_err !== null && jsonvalue.error.fwd_desig_err !== "" && jsonvalue.error.fwd_desig_err !== undefined)
                {
                    $('#wifi_preview #fwd_desig_err').html(jsonvalue.error.fwd_desig_err);
                    $('#wifi_preview #fwd_desig_err').focus();
                    error_flag = false;
                    $('#wifi_preview #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_preview #imgtxt2').val("");
                } else {
                    $('#wifi_preview #fwd_desig_err').html("");
                }
                if (jsonvalue.error.fwd_add_err !== null && jsonvalue.error.fwd_add_err !== "" && jsonvalue.error.fwd_add_err !== undefined)
                {
                    $('#wifi_preview #fwd_add_err').html(jsonvalue.error.fwd_add_err);
                    $('#wifi_preview #fwd_add_err').focus();
                    error_flag = false;
                    $('#wifi_preview #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_preview #imgtxt2').val("");
                } else {
                    $('#wifi_preview #fwd_add_err').html("");
                }
                if (jsonvalue.error.fwd_tel_err !== null && jsonvalue.error.fwd_tel_err !== "" && jsonvalue.error.fwd_tel_err !== undefined)
                {
                    $('#wifi_preview #fwd_tel_err').html(jsonvalue.error.fwd_tel_err);
                    $('#wifi_preview #fwd_tel_err').focus();
                    error_flag = false;
                    $('#wifi_preview #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifi_preview #imgtxt2').val("");
                } else {
                    $('#wifi_preview #fwd_tel_err').html("");
                }
                if (checkDuplicatesprvw())
                {
                    $('#wifi_preview #macduperr').html("You entered duplicate MAC address please correct and enter diffrent MAC addresses");
                    error_flag = false;
                } else
                {
                    $('#wifi_preview #macduperr').html("");
                }

                // start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#wifi_preview #tnc').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // profile 20th march 
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#wifi_preview #useremployment_error').focus();
                    $('#wifi_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#wifi_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#wifi_preview #minerror').focus();
                    $('#wifi_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#wifi_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#wifi_preview #deperror').focus();
                    $('#wifi_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#wifi_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#wifi_preview #other_dept').focus();
                    $('#wifi_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#wifi_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#wifi_preview #smierror').focus();
                    $('#wifi_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#wifi_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#wifi_preview #state_error').focus();
                    $('#wifi_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#wifi_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#wifi_preview #org_error').focus();
                    $('#wifi_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#wifi_preview #org_error').html("");
                }
                // profile 20th march 
                if (!error_flag)
                {

                    $("#wifi_preview :disabled").removeAttr('disabled');
                    $('#wifi_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#wifi_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {

                    $('#wifi_preview_form').modal('toggle');
                }

            }, error: function (request, error)
            {
                console.log('error');
            }
        });
    });
    $(document).on('click', '.cancelmac', function () {
        var value = $(this).val();
        bootbox.confirm("Are you sure? Do You want to cancel this Request?", function (result) {
            if (result) {
                $.ajax({
                    type: "POST",
                    url: "CancelRequest",
                    data: {data: value},
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);
                        if (jsonvalue.dbupdate === "cancel") {
                            $('#delModal1').modal('show', {backdrop: 'static'});
                        } else if (jsonvalue.dbupdate === 'notfound') {
                            $('#delModal2').modal('show', {backdrop: 'static'});
                        } else if (jsonvalue.dbupdate === 'fail') {
                            $('#delModal3').modal('show', {backdrop: 'static'});
                        }
                        $("#wifi_Pendingreq_data").addClass('d-none');
                        fetchApplicantPendingData();
                    },
                    error: function () {
                        console.log("It seem's there is some problem, please try again later !!!")
                        //alert("It seem's there is some problem, please try again later !!!");
                    }
                });
            }
        });
    });
});

function fetchApplicantPendingData() {
    $.ajax({
        type: "POST",
        url: "fetchApplicantPendingData",
        success: function (data) {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            var str_data = "";
            var i = 1;
            if (jsonvalue.length > 0) {
                $("#wifi_Pendingreq_data").removeClass('d-none');
            }
            $.each(jsonvalue, function (j, jval) {
                str_data += "<tr><td class='text-center'><b>" + i + "</b></td><td>" + jval.wifi_mac1 + "</td><td>" + jval.wifi_os1 + "</td><td><button type='button' class='btn btn-primary btn-sm cancelmac' value='" + jval.registration_no + "' >Cancel</button></td></tr>";
                i++;
            });
            $("#wifi_pending_tbody").html(str_data);
        },
        error: function () {
            console.log('error');
        }
    });
}