$(document).ready(function () {
    var pathname = window.location.pathname;
    pathname = pathname.toLowerCase();
    if (pathname.indexOf('dns') > -1) {
        fetchOpenCampaigns();
    }
    $(function () {
        $("body").delegate(".migrationdatepicker", "focusin", function () {
            //var today = new Date();
            //var lastDate = new Date(today.getFullYear() +1, 11, 31);
            $(this).datepicker({
                dateFormat: 'dd/mm/yy', minDate: '1',
                maxDate: 30, //set the lastDate as maxDate
                beforeShow: function (input, inst) {
                    var rect = input.getBoundingClientRect();
                    setTimeout(function () {
                        inst.dpDiv.css({top: rect.top + 40, left: rect.left + 0});
                    }, 0);
                }
            });
        });
    });
    $(function () {
        $("body").delegate(".migrationdatepicker1", "focusin", function () {
            $(this).datepicker({
                dateFormat: 'dd/mm/yy', minDate: '1',
                maxDate: 30
            });
        });
    });

    $('input[name="dns_single_form"]').click(function () {
        $("font span").html('');
        show_divs();
    })

    $('#dns_single_form #req_new_ip').removeClass('display-hide');
    $('#dns_single_form #add_ip').html('');
    $("input[name='req_']").click(function () {
        icount = 0;
        $("#first_div input:text").val("");
        var field_form = $(this).closest('form').attr('id')

        var reqValue = $("input[name='req_']:checked", '#' + field_form).val();
        $('#req_mod').val(reqValue);
        req_radio_func(field_form, reqValue)
    });
    var count = 0;
    var count1 = 0;
    var count2 = 0;
    var count_ip = 0;
    var count_ip_form = 0;
    var count_dns = 0;
    var count_srl_form = 0;
    var count_srl = 0;
    var flag1;
    $('input[name="req_"]').click(function () {
        if ($(this).is(':checked')) {
            $("font span").html('');
            count_ip_form = 0;
            $('.dns_owner_error').removeClass('alert alert-danger');
            $('.dns_owner_error').html("");
        }
    });
    $(document).on('find click', '.page-wrapper button', function () {
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');
        var form_tab;
        if (field_name === 'dns_single_form') {
            $('#tab2').show();
            $('#tab1').hide();
            $('#dns_single_form').removeClass('display-hide');
            $('#dns_bulk_form').addClass('display-hide');
        } else if (field_name === 'dns_bulk_form') {
            $('#tab2').show();
            $('#tab1').hide();
            $('#dns_bulk_form').removeClass('display-hide');
            //$('#dns_single_form').addClass('display-hide');
        } else if (field_name === 'download') {
            var data = $(this).val();
            window.location = "download_2?uploaded_filename=" + data;
        }
        if (field_form === "dns_single_form") {
            form_tab = "1";
            var reqValue = $("input[name='req_']:checked", '#' + field_form).val();
            if (reqValue == "req_modify")
            {
                if (field_name == "addip") {
                    $("button.rmvbox1").remove();
                    if (count_ip_form < 49) {
                        var err_id = count_ip_form + 2
                        var elmnt = '<div class="row"><div class="col-md-3" style="padding-right:0px;"><input class="form-control dns_name class_name_url class_name_domain" placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="dns_url[]" id="dns_url" value="" maxlength="100" style="margin: 5px 0px 0px 0px;"><font style="color:red"><span class="dns_url_err dns_url_error' + err_id + '" id="dns_url_err"></span></font></div>';
                        elmnt += '<div class="col-md-3 pr-0"><input class="form-control class_name_ip" placeholder="Enter CNAME e.g : www.demo.nic.in or demo.gov.in" type="text" name="dns_cname[]" id="dns_ip"  value="" style="margin: 5px 0px 0px 0px;"><font style="color:red"><span id="dns_cname_error" class="dns_cname_error' + err_id + '"></span></font></div>';
                        elmnt += '<div class="col-md-3 pr-0"><input class="form-control class_name_dns req_old_ip1" placeholder="Enter Old IP e.g : 164.100.X.X or IPV6 Address" type="text" name="dns_old_ip[]" id=""  value=""  style="margin: 5px 0px 0px 0px;"><font style="color:red"><span class="dns_old_ip_error' + err_id + '" id="dns_old_ip_error"></span></font></div>';
                        elmnt += '<div class="col-md-2 pr-0"><input class="form-control class_name_loc req_new_ip1 mt-1" placeholder="Enter New IP e.g : 164.100.X.X or IPV6 Address allowed" value="" name="dns_new_ip[]" id="dns_loc" maxlength="100"><font style="color:red"><span id="dns_new_ip_error" class="dns_new_ip_error' + err_id + '"></span></font></div>' + '<div class="col-md-1"><button type="button" class="btn btn-danger btn-circle rmvbox1 mt-1" name="dns_rmv1" title="Remove Row" id="rmv"><i class="fa fa-minus fa-2x"></i></button></div></div>';
                        $('#' + field_form + ' #add_ip').append(elmnt);
                        $('.iploc_Err').css('display', 'block');
                    } else if (flag1) {
                        $('#' + field_form + ' #add_ip').fadeOut('slow', function () {
                            $('#' + field_form + ' #add_ip').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only Fifty(50) entries allowed.</div>');
                            $('#' + field_form + ' .alert_msg').fadeOut(8000);
                        });
                        flag1 = false;
                    }
                    if (count2 === 1) {
                        count_ip_form += 1;
                        count2++;
                    } else {
                        if (count1 < 49) {
                            count_ip_form++; //= count;
                            count2++;
                        }
                    }
                    if (count_ip_form == "49") {
                        $('#' + field_form + ' #add_ip').fadeOut('slow', function () {
                            $('#' + field_form + ' #add_ip').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only Fifty(50) entries allowed.</div>');
                            $('#' + field_form + ' .alert_msg').fadeOut(2000);
                        });
                    }
                    if (count_srl_form < 49) {
                        //   $('#' + field_form + ' #add_srl').append(elmnt);
                    } else if (flag1) {
                        $('#' + field_form + ' #add_srl').fadeOut('slow', function () {
                            $('#' + field_form + ' #add_srl').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only Fifty(50) entries allowed.</div>');
                            $('#' + field_form + ' .alert_msg').fadeOut(8000);
                        });
                        flag1 = false;
                    }
                    if (count1 === 1)
                    {
                        count_srl_form += 1;
                        count1++;
                    } else {
                        if (count1 < 49) {
                            count_srl_form++; //= count;
                            count1++;
                        }
                    }
                    if (count_srl_form == "49")
                    {
                        $('#' + field_form + ' #add_srl').fadeOut('slow', function () {
                            $('#' + field_form + ' #add_srl').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only five(5) entries allowed.</div>');
                            $('#' + field_form + ' .alert_msg').fadeOut(2000);
                        });
                    }
                }
            }
            if (reqValue == "req_new" || reqValue == "req_delete")
            {
                if (field_name == "addip") {
                    $("button.rmvbox1").remove();
                    if (count_ip_form < 49) {
                        var err_id = count_ip_form + 2
                        var elmnt = '<div class="row"><div class="col-md-3" style="padding-right:0px;"><input class="form-control dns_name class_name_url class_name_domain" placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="dns_url[]" id="dns_url" value="" maxlength="100" style="margin: 5px 0px 0px 0px;"><font style="color:red"><span class="dns_url_err dns_url_error' + err_id + '" id="dns_url_err"></span></font></div>';
                        elmnt += '<div class="col-md-3" style="padding-right:0px;"><input class="form-control class_name_ip" placeholder="Enter CNAME e.g : demo.nic.in or demo.gov.in" type="text" name="dns_cname[]" id="dns_cname"  value="" style="margin: 5px 0px 0px 0px;"><font style="color:red"><span id="dns_cname_error" class="dns_cname_error' + err_id + '"></span></font></div>';
                        elmnt += '<div class="col-md-2 pr-0"><input class="form-control class_name_loc req_new_ip1 mt-1" placeholder="Enter New IP e.g : 164.100.X.X or IPV6 Address allowed" value="" name="dns_new_ip[]" id="dns_loc" maxlength="100"><font style="color:red"><span id="dns_new_ip_error" class="text-danger dns_new_ip_error' + err_id + '"></span></font></div>' + '<div class="col-md-1"><button type="button" class="btn btn-danger btn-circle rmvbox1 mt-1" name="dns_rmv1" title="Remove Row" id="rmv"><i class="fa fa-minus fa-2x"></i></button></div></div>';
                        $('#' + field_form + ' #add_ip').append(elmnt);
                        $('.iploc_Err').css('display', 'block');
                    } else if (flag1) {
                        $('#' + field_form + ' #add_ip').fadeOut('slow', function () {
                            $('#' + field_form + ' #add_ip').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only Fifty(50) entries allowed.</div>');
                            $('#' + field_form + ' .alert_msg').fadeOut(8000);
                        });
                        flag1 = false;
                    }
                    if (count2 === 1) {
                        count_ip_form += 1;
                        count2++;
                    } else {
                        if (count1 < 49) {
                            count_ip_form++; //= count;
                            count2++;
                        }
                    }
                    if (count_ip_form == "49") {
                        $('#' + field_form + ' #add_ip').fadeOut('slow', function () {
                            $('#' + field_form + ' #add_ip').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only Fifty(50) entries allowed.</div>');
                            $('#' + field_form + ' .alert_msg').fadeOut(2000);
                        });
                    }
                    if (count_srl_form < 49) {
                        //   $('#' + field_form + ' #add_srl').append(elmnt);
                    } else if (flag1) {
                        $('#' + field_form + ' #add_srl').fadeOut('slow', function () {
                            $('#' + field_form + ' #add_srl').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only Fifty(50) entries allowed.</div>');
                            $('#' + field_form + ' .alert_msg').fadeOut(8000);
                        });
                        flag1 = false;
                    }
                    if (count1 === 1)
                    {
                        count_srl_form += 1;
                        count1++;
                    } else {
                        if (count1 < 49) {
                            count_srl_form++; //= count;
                            count1++;
                        }
                    }
                    if (count_srl_form == "49")
                    {
                        $('#' + field_form + ' #add_srl').fadeOut('slow', function () {
                            $('#' + field_form + ' #add_srl').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only five(5) entries allowed.</div>');
                            $('#' + field_form + ' .alert_msg').fadeOut(2000);
                        });
                    }
                }
            }
            if (field_name == "dns_rmv") {
                count_dns--;
                count--;
                $(this).parent('div .con').remove();
            }
            if (field_name == "dns_rmv1") {
                count_srl_form--;
                count1--;
                $(this).closest('div#add_ip').find('.row:nth-last-child(2)').find('div:nth-last-child(1)').html('<button type="button" class="btn btn-danger btn-circle rmvbox1 mt-1" name="dns_rmv1" title="Remove Row" id="rmv"><i class="fa fa-minus fa-2x"></i></button>');
                $(this).closest('div[class=row]').remove();
                count_ip_form--;
                count2--;
                $(this).parent('div .con').remove();
                //$('#' + field_form +  ' #add_ip').parent('div .add_ip').remove();
            }
            if (field_name == "dns_rmv2") {
                count_ip_form--;
                count2--;
                $(this).parent('div .con').remove();
            }
        }
    });
    $('input[type="checkbox"]').change(function () {
        var field_form = $(this).closest('form').attr('id')
        var reqValue = $("input[name='req_']:checked", '#' + field_form).val();
        var reqValue1 = $("#" + field_form + " #req_action1").val();
        if (this.checked)
        {
            $(".new_ip_chk").html("");
            if (reqValue == "req_modify" || reqValue1 == "req_modify")
            {
                $(this).parents('.mt-checkbox-list').children('.addons').removeClass('display-hide');
                $(this).parents('.mt-checkbox-list').children('.addons1').removeClass('display-hide');
            } else if (reqValue == "req_new")
            {
                $(this).parents('.mt-checkbox-list').children('.addons').removeClass('display-hide');
                $(this).parents('.mt-checkbox-list').children('.addons1').addClass('display-hide');
                //$(this).parents('.mt-checkbox-list').children('.addons1').toggleClass('display-hide');
            } else {
                $(this).parents('.mt-checkbox-list').children('.addons').removeClass('display-hide');
                $(this).parents('.mt-checkbox-list').children('.addons1').addClass('display-hide');
            }
        } else
        {
            $(".new_ip_chk").html("*");
            console.log("not checked")
            $(this).parents('.mt-checkbox-list').children('.addons').toggleClass('display-hide');
            $(this).parents('.mt-checkbox-list').children('.addons1').addClass('display-hide');
            var a = $(this).val();
            $('#request_' + a).val('');
        }
    });
    $("input[name='dns_type']").click(function () {
        var radioValue = $("input[name='dns_type']:checked").val();
        if (radioValue) {
            if (radioValue === 'modify' || radioValue === 'delete') {
                $('.dns_url').removeClass('display-hide');
                $(".control-label").children("span").html();
            } else
            {
                $('.dns_url').addClass('display-hide');
            }
        }
    });
    // dns form submission
    $('#dns_single_form').submit(function (e) {
        e.preventDefault();
        $('.dns_owner_error').removeClass('alert alert-danger');
        $('.dns_owner_error').html("");
        $("font span").html('');
        $(".loader").show();
        var req_ = $("#dns_single_form_div input[name='req_']:checked").val();
        var req_other_add = $("input[name='req_other_add']:checked").val();
        $("#_req").val(req_);
        $("#req_other_additional").val(req_other_add);

        var data = JSON.stringify($('#dns_single_form').serializeObject());
        console.log(data)
        var CSRFRandom = $("#CSRFRandom").val();
        $.ajax({
            type: "POST",
            url: "dns_tab3",
            data: {data: data, CSRFRandom: CSRFRandom, cert: null},
            datatype: JSON,
            success: function (data)
            {
                console.log(data)
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (!jQuery.isEmptyObject(jsonvalue.error)) {
                    fetchErrorFunc(jsonvalue.error);
                    error_flag = false;
                    fetchErrorFunc(jsonvalue.error);
                }
                if (!error_flag) {
                    $('#tab2').show();
                    $('#dns_single_form_div').removeClass('display-hide');
                    $('#dns_single_form').removeClass('display-hide');
                    $('#dns_bulk_form').addClass('display-hide');
                    $("#dnsbulk-2").addClass('display-hide')
                } else {
                    $('#tab2').show();
                    $('#dns_single_form_div').addClass('display-hide');
                    $('#dns_bulk_form').addClass('display-hide');
                    $("#dnsbulk-2").removeClass('display-hide')
                }
                if (jsonvalue.iCampaignId) {
                    showCapaign(jsonvalue.iCampaignId)
                }
                if (!jQuery.isEmptyObject(jsonvalue.bulkData)) {
                    $('.success-head-notify').html(msgValidHead(jsonvalue.bulkData.formDetails));
                    $('.err-head-notify').html(msgInValidHead(jsonvalue.bulkData.formDetails));
//                    setTimeout(function() {
//                        bulkDomCreate(jsonvalue.bulkData);
//                    }, 100);
                }

            }, error: function ()
            {
                $(".loader").hide();
                $('#tab1').show();
            }
        });
    });



    $('#dns_bulk_form').submit(function (e) {
        $('.loader').show();
        e.preventDefault();
        var data = JSON.stringify($('#dns_bulk_form').serializeObject());
        var user_file = $("#user_file_dns").val();
        var cert = $('#cert').val();
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        $.ajax({
            type: "POST",
            url: "dns_tab3",
            data: {data: data, cert: cert, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                console.log(data)
                //$('.loader').hide();
                resetCSRFRandom();// line added by pr on 22ndjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                console.log(jsonvalue.bulkData)
                if (!jQuery.isEmptyObject(jsonvalue.bulkData)) {
                    setTimeout(function () {
                        bulkDomCreate(jsonvalue.bulkData);
                    }, 100);
                }
                if (jsonvalue.error.file_err)
                {
                    $('#dns_bulk_form #file_err').html(jsonvalue.error.file_err)
                    error_flag = false;
                    $('#dns_bulk_form #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_bulk_form #captcha1').val("");
                } else {
                    $('#dns_bulk_form #file_err').html("")
                }
                if (jsonvalue.error.dns_loc_error !== undefined && jsonvalue.error.dns_loc_error !== null && jsonvalue.error.dns_loc_error !== "")
                {
                    $('#dns_bulk_form #dns_loc_error').html(jsonvalue.error.dns_loc_error)
                    $('#dns_bulk_form #dns_loc').focus();
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_bulk_form #imgtxt').val("");
                } else {
                    $('#dns_loc_error').html("")
                }
                if (jsonvalue.error.request_cname_err !== undefined && jsonvalue.error.request_cname_err !== null && jsonvalue.error.request_cname_err !== "")
                {
                    $('#dns_bulk_form #request_cname_err').html(jsonvalue.error.request_cname_err)
                    $('#dns_bulk_form #request_cname').focus();
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dns_bulk_form #request_cname_err').html("")
                }
                if (jsonvalue.error.request_aaa_err !== undefined && jsonvalue.error.request_aaa_err !== null && jsonvalue.error.request_aaa_err !== "")
                {
                    $('#dns_bulk_form #request_aaa_err').html(jsonvalue.error.request_aaa_err)
                    $('#dns_bulk_form #request_aaaa').focus();
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_bulk_form #imgtxt').val("");
                } else {
                    $('#dns_bulk_form #request_aaa_err').html("")
                }
                if (jsonvalue.error.request_mx_err !== undefined && jsonvalue.error.request_mx_err !== null && jsonvalue.error.request_mx_err !== "")
                {
                    $('#dns_bulk_form #request_mx_err').html(jsonvalue.error.request_mx_err)
                    $('#dns_bulk_form #request_mx').focus();
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_bulk_form #imgtxt').val("");
                } else {
                    $('#dns_bulk_form #request_mx_err').html("")
                }
                if (jsonvalue.error.request_mx_err1 !== undefined && jsonvalue.error.request_mx_err1 !== null && jsonvalue.error.request_mx_err1 !== "")
                {
                    $('#dns_bulk_form #request_mx_err1').html(jsonvalue.error.request_mx_err1)
                    $('#dns_bulk_form #request_mx_err1').focus();
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_bulk_form #imgtxt').val("");
                } else {
                    $('#dns_bulk_form #request_mx_err1').html("")
                }
                if (jsonvalue.error.request_ptr_err !== undefined && jsonvalue.error.request_ptr_err !== null && jsonvalue.error.request_ptr_err !== "")
                {
                    $('#dns_bulk_form #request_ptr_err').html(jsonvalue.error.request_ptr_err)
                    $('#dns_bulk_form #request_ptr').focus();
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_bulk_form #imgtxt').val("");
                } else {
                    $('#dns_bulk_form #request_ptr_err').html("")
                }
                if (jsonvalue.error.request_ptr_err1 !== undefined && jsonvalue.error.request_ptr_err1 !== null && jsonvalue.error.request_ptr_err1 !== "")
                {
                    $('#dns_bulk_form #request_ptr_err1').html(jsonvalue.error.request_ptr_err1)
                    $('#dns_bulk_form #request_ptr').focus();
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_bulk_form #imgtxt').val("");
                } else {
                    $('#dns_bulk_form #request_ptr_err1').html("")
                }
                if (jsonvalue.error.request_srv_err !== undefined && jsonvalue.error.request_srv_err !== null && jsonvalue.error.request_srv_err !== "")
                {
                    $('#dns_bulk_form #request_srv_err').html(jsonvalue.error.request_srv_err)
                    $('#dns_bulk_form #request_srv').focus();
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_bulk_form #imgtxt').val("");
                } else {
                    $('#request_srv_err').html("")
                }
                if (jsonvalue.error.request_spf_err !== undefined && jsonvalue.error.request_spf_err !== null && jsonvalue.error.request_spf_err !== "")
                {
                    $('#dns_bulk_form #request_spf_err').html(jsonvalue.error.request_spf_err)
                    $('#dns_bulk_form #dns_bulk_form #request_spf').focus();
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dns_bulk_form #request_spf_err').html("")
                }
                if (jsonvalue.error.request_txt_err !== undefined && jsonvalue.error.request_txt_err !== null && jsonvalue.error.request_txt_err !== "")
                {
                    $('#dns_bulk_form #request_txt_err').html(jsonvalue.error.request_txt_err)
                    $('#dns_bulk_form #request_txt_err').focus();
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dns_bulk_form #request_txt_err').html("")
                }
                if (jsonvalue.error.request_dmarc_err !== undefined && jsonvalue.error.request_dmarc_err !== null && jsonvalue.error.request_dmarc_err !== "")
                {
                    $('#dns_bulk_form #request_dmarc_err').html(jsonvalue.error.request_dmarc_err)
                    $('#dns_bulk_form #request_dmarc_err').focus();
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dns_bulk_form #request_dmarc_err').html("")
                }
                if (jsonvalue.error.cap_error !== undefined && jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "")
                {
                    $('#dns_bulk_form #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#dns_bulk_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_bulk_form #imgtxt').val("");
                } else {
                    $('#dns_bulk_form #captchaerror').html("")
                }
                // start, code added by pr on 22ndjan18
                if (jsonvalue.error.csrf_error !== undefined && jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "")
                {
                    $('#dns_bulk_form #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // end, code added by pr on 22ndjan18
                if (!error_flag) {
                    $('#dnsbulk-1').removeClass('display-hide');
                    $('#dnsbulk-2').addClass('display-hide');
                } else {
                    $('#dnsbulk-1').addClass('display-hide');
                    $('#dnsbulk-2').removeClass('display-hide');
                    //console.log('jsonvalue.form_details.request_cname: ' + jsonvalue.form_details.request_cname);
                }



            }, error: function ()
            {
                $(".loader").hide();
                $('#tab1').show();
            }, complete: function () {
                $(".loader").hide();
            }
        });
    });

    $('#dns_bulk_form2').submit(function (e) {
        $('.loader').show();
        e.preventDefault();
        $('#dns_bulk_form2 .edit').removeClass('display-hide');
        $.ajax({
            type: "POST",
            url: "dns_bulk_tab2",
            datatype: JSON,
            success: function (data)
            {
                $('.loader').hide();
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.error_file !== null && jsonvalue.error.error_file !== "" && jsonvalue.error.error_file !== undefined)
                {
                    $('#dns_bulk_form2 #error_file1').html(jsonvalue.error.error_file)
                    error_flag = false;
                    $('#dns_bulk_form2 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_bulk_form2 #captcha1').val("");
                } else {
                    $('#dns_bulk_form2 #error_file1').html("")
                }

                if (!jQuery.isEmptyObject(jsonvalue.error)) {
                    error_flag = false;
                    fetchErrorFunc(jsonvalue.error);
                }
                if (error_flag) {

                    $('#dns_form2 #dns_loc_1').val(jsonvalue.form_details.dns_loc);
                    if (jsonvalue.form_details.req_ === "req_new")
                    {
                        // $('#dns_form2 #first_div').removeClass('display-hide');
                        $('#dns_form2 #second_div').removeClass('display-hide');
                        $('#dns_form2 #third_div').removeClass('display-hide');
                        $('#dns_form2 #other_div_req').addClass('display-hide');
                        $('#dns_form2 #domain_label').html("DNS URL")
                        $('#dns_form2 #dns_ip_label').html("Application IP")
                        $('#dns_form2 #req_new_ip').addClass('display-hide');
                        $('#dns_form2 #req_action1').val(jsonvalue.form_details.req_);
                        $('#dns_form2 #ptr_1').prop('checked', false);
                    }
                    if (jsonvalue.form_details.req_ === "req_modify")
                    {
                        // $('#dns_form2 #first_div').removeClass('display-hide');
                        $('#dns_form2 #second_div').removeClass('display-hide');
                        $('#dns_form2 #third_div').removeClass('display-hide');
                        $('#dns_form2 #other_div_req').removeClass('display-hide');
                        $('#dns_form2 #domain_label').html("Domain Name")
                        $('#dns_form2 #dns_cname').html("CNAME")
                        $('#dns_form2 #old_ip_label').html("OLD DNS IP/A/AAAA ")
                        $('#dns_form2 #new_ip_label').html("New Application IP/A/AAAA")
                        $('#dns_form2 #req_prvw').html("Modify Request: ")
                        $('#dns_form2 #req_action1').val(jsonvalue.form_details.req_);
                        $('#dns_form2 #ptr_1').prop('checked', false);
                    }
                    if (jsonvalue.form_details.req_ === "req_delete")
                    {
                        //$('#dns_form2 #first_div').removeClass('display-hide');
                        $('#dns_form2 #second_div').removeClass('display-hide');
                        $('#dns_form2 #third_div').removeClass('display-hide');
                        $('#dns_form2 #domain_label').html("Domain Name")
                        $('#dns_form2 #dns_cname').html("CNAME")
                        $('#dns_form2 #old_ip_label').html("OLD DNS IP/A/AAAA ")
                        $('#dns_form2 #other_div_req').addClass('display-hide');
                        $('#dns_form2 #req_prvw').html("Delete Request: ")
                        $('#dns_form2 #req_action1').val(jsonvalue.form_details.req_);
                        $('#dns_form2 #ptr_1').prop('checked', true);
                    }
                    if (jsonvalue.form_details.req_ === "req_other")
                    {
                        //$('#dns_form2 #first_div').removeClass('display-hide');
                        $('#dns_form2 #second_div').removeClass('display-hide');
                        $('#dns_form2 #third_div').removeClass('display-hide');
                        $('#dns_form2 #domain_label').html("Domain Name")
                        $('#dns_form2 #dns_cname').html("CNAME")
                        $('#dns_form2 #old_ip_label').html("OLD DNS IP/A/AAAA ")
                        $('#dns_form2 #other_div_req').addClass('display-hide');
                        $('#dns_form2 #req_prvw').html("Delete Request: ")
                        $('#dns_form2 #req_action1').val(jsonvalue.form_details.req_);
                        $('#dns_form2 #ptr_1').prop('checked', true);
                    }
                    // RADIO
                    if (jsonvalue.form_details.dns_domain === 'nic')
                    {
                        $('#dns_form2 #dns_domain1_1').prop('checked', true);
                        $('#dns_form2 #domain1').text('.nic.in');
                    } else {
                        $('#dns_form2 #dns_domain1_2').prop('checked', true);
                        $('#dns_form2 #domain1').text('.gov.in');
                    }
                    // checkboxes
                    if (jsonvalue.form_details.request_mx !== '') {
                        $('#dns_form2 #request_mx_1').val(jsonvalue.form_details.request_mx);
                        $('#dns_form2 #request_mx_1').removeClass('display-hide');
                        $('#dns_form2 #mx_1').prop('checked', true);
                        if (jsonvalue.form_details.req_ === "req_modify")
                        {
                            $('#dns_form2 #request_mx_2').val(jsonvalue.form_details.request_mx1);
                            $('#dns_form2 #request_mx_2').removeClass('display-hide');
                        }
                    }
                    if (jsonvalue.form_details.request_ptr !== '') {
                        $('#dns_form2 #request_ptr_1').val(jsonvalue.form_details.request_ptr);
                        $('#dns_form2 #request_ptr_1').removeClass('display-hide');
                        $('#dns_form2 #ptr_1').prop('checked', true);
                        if (jsonvalue.form_details.req_ === "req_modify")
                        {
                            $('#dns_form2 #request_ptr_2').val(jsonvalue.form_details.request_ptr1);
                            $('#dns_form2 #request_ptr_2').removeClass('display-hide');
                        }
                    }
                    if (jsonvalue.form_details.request_srv !== '') {
                        $('#dns_form2 #request_srv_1').val(jsonvalue.form_details.request_srv);
                        $('#dns_form2 #request_srv_1').removeClass('display-hide');
                        $('#dns_form2 #srv_1').prop('checked', true);
                    }
                    if (jsonvalue.form_details.request_spf !== '') {
                        $('#dns_form2 #request_spf_1').val(jsonvalue.form_details.request_spf);
                        $('#dns_form2 #request_spf_1').removeClass('display-hide');
                        $('#dns_form2 #spf_1').prop('checked', true);
                    }
                    if (jsonvalue.form_details.request_txt !== '') {
                        $('#dns_form2 #request_txt_1').val(jsonvalue.form_details.request_txt);
                        $('#dns_form2 #request_txt_1').removeClass('display-hide');
                        $('#dns_form2 #txt_1').prop('checked', true);
                    }
                    if (jsonvalue.form_details.request_dmarc !== '') {
                        $('#dns_form2 #request_dmarc_1').val(jsonvalue.form_details.request_dmarc);
                        $('#dns_form2 #request_dmarc_1').removeClass('display-hide');
                        $('#dns_form2 #dmarc_1').prop('checked', true);
                    }
                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#dns_form2 #central_div').show();
                        $('#dns_form2 #state_div').hide();
                        $('#dns_form2 #other_div').hide();
                        $('#dns_form2 #other_text_div').addClass("display-hide");
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
                        }, 2000);
                        setTimeout(function () {
                            if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                                $('#other_text_div').removeClass("display-hide");
                                $('#other_dept').val(jsonvalue.profile_values.other_dept);
                            }
                        }, 1000);
                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#dns_form2 #central_div').hide();
                        $('#dns_form2 #state_div').show();
                        $('#dns_form2 #other_div').hide();
                        $('#dns_form2 #other_text_div').addClass("display-hide");
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
                        $('#dns_form2 #central_div').hide();
                        $('#dns_form2 #state_div').hide();
                        $('#dns_form2 #other_div').show();
                        $('#dns_form2 #other_text_div').addClass("display-hide");
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
                        $('#dns_form2 #central_div').hide();
                        $('#dns_form2 #state_div').hide();
                        $('#dns_form2 #other_div').hide();
                    }
                    $('#dns_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#dns_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#dns_form2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#dns_form2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#dns_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#dns_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#dns_form2 #user_state').val(jsonvalue.profile_values.state);
                    $('#dns_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#dns_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#dns_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#dns_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);

                    $('#dns_form2 #user_email').val(jsonvalue.profile_values.email);

                    $('#dns_form2 #dns_user_empcode').val(jsonvalue.profile_values.emp_code);



                    $('#dns_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#dns_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#dns_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);

                    $('#dns_form2 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    $('#dns_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $('#dns_form2 #dns_bulk_preview').removeClass("display-hide");
                    $("#dns_form2 #successBulkData thead tr").html("");
                    $("#dns_form2 #successBulkData tbody").html("");
                    if (!jQuery.isEmptyObject(jsonvalue.bulkData.validRecord)) {
                        validDomForReqModify2(jsonvalue.bulkData)
                    }
                    setTimeout(function () {
                        if (jsonvalue.profile_values.emp_code) {
                            $('#dns_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                        } else {
                            $('#dns_form2 #user_empcode').prop('disabled', false);
                        }
                        $("#dns_form2 table#successBulkData button").prop('disabled', true);
                    }, 10);
                    $('#dns_form2 #confirm').val('dns_bulk');
                    $('.edit').removeClass('display-hide');
                    $('#dns_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#dns_form2 :button[type='button']").removeAttr('disabled');

                    $("#dns_form2 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                } else {
                }
            }, error: function ()
            {
                $('.loader').hide();
                $('#tab1').show();
            }
        });
    });
    $('#dns_form2').submit(function (e) {
        $('.loader').show();
        e.preventDefault();
        $("#dns_form2 :disabled").removeAttr('disabled');
        $('#dns_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dns_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#dns_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#dns_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#dns_form2').serializeObject());
        console.log(data + "on submit")
        var stat_type = "";
        $('#dns_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        var dns_cname = $("#dns_form2 input[name='dns_cname[]']").val();
        if (dns_cname !== "")
        {
            dns_cname = $("#dns_form2 input[name='dns_cname[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        } else {
            dns_cname = $("#dns_form2 input[name='dns_cname[]']").val();
        }
        var dns_oldip = $("#dns_form2 input[name='dns_old_ip[]']").val();
        if (dns_oldip !== "")
        {
            dns_oldip = $("#dns_form2 input[name='dns_old_ip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        var dns_url = $("#dns_form2 input[name='dns_url[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var dns_newip = $("#dns_form2 input[name='dns_new_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var cert = $('#dns_form2 #cert').val();
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        var submit = $('#dns_form2 #confirm').val();
        $('#dns_form2 .edit').removeClass('display-hide');
        $.ajax({
            type: "POST",
            url: "dns_tab2",
            data: {data: data, dns_url: dns_url, dns_oldip: dns_oldip, dns_newip: dns_newip, dns_cname: dns_cname, action_type: "confirm", CSRFRandom: CSRFRandom, request_type: submit, cert: cert, stat_type: stat_type},
            datatype: JSON,
            success: function (data)
            {
                $('.loader').hide();
                resetCSRFRandom();// line added by pr on 10thjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.file_err !== undefined && jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "")
                {
                    $('#dns_form2 #file_err').html(jsonvalue.error.file_err)
                    error_flag = false;
                    $('#dns_form2 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_form2 #captcha1').val("");
                } else {
                    $('#dns_form2 #file_err').html("")
                }
                if (jsonvalue.error.old_url_error !== undefined && jsonvalue.error.old_url_error !== null && jsonvalue.error.old_url_error !== "")
                {
                    $('#dns_form2 #old_url_error').html(jsonvalue.error.old_url_error)
                    $('#dns_form2 #old_url_error').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #old_url_error').html("")
                }
                if (jsonvalue.error.old_ip_error !== undefined && jsonvalue.error.old_ip_error !== null && jsonvalue.error.old_ip_error !== "")
                {
                    $('#dns_form2 #old_ip_error').html(jsonvalue.error.old_ip_error)
                    $('#dns_form2 #old_ip_error').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #old_ip_error').html("")
                }
                if (jsonvalue.error.dns_url_error !== undefined && jsonvalue.error.dns_url_error !== null && jsonvalue.error.dns_url_error !== "")
                {
                    $('#dns_form2 #dns_url_err').html(jsonvalue.error.dns_url_error)
                    $('#dns_form2 #dns_url_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #dns_url_err').html("")
                    $("#dns_form2 .iploc_Err").css("display", "block");
                }
                if (jsonvalue.error.dns_ip_error !== undefined && jsonvalue.error.dns_ip_error !== null && jsonvalue.error.dns_ip_error !== "")
                {
                    $('#dns_form2 #dns_ip_err').html(jsonvalue.error.dns_ip_error)
                    $('#dns_form2 #dns_ip').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #dns_ip_err').html("")
                }
                if (jsonvalue.error.dns_loc_error !== null && jsonvalue.error.dns_loc_error !== "" && jsonvalue.error.dns_loc_error !== undefined)
                {
                    $('#dns_form2 #dns_loc_error').html(jsonvalue.error.dns_loc_error)
                    $('#dns_form2 #dns_loc').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #dns_loc_error').html("")
                }
                if (jsonvalue.error.request_mx_err !== undefined && jsonvalue.error.request_mx_err !== null && jsonvalue.error.request_mx_err !== "")
                {
                    $('#dns_form2 #request_mx_err').html(jsonvalue.error.request_mx_err)
                    $('#dns_form2 #request_mx_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_mx_err').html("")
                }
                if (jsonvalue.error.request_mx_err1 !== undefined && jsonvalue.error.request_mx_err1 !== null && jsonvalue.error.request_mx_err1 !== "")
                {
                    $('#dns_form2 #request_mx_err1').html(jsonvalue.error.request_mx_err1)
                    $('#dns_form2 #request_mx_2').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_mx_err1').html("")
                }
                if (jsonvalue.error.request_ptr_err !== undefined && jsonvalue.error.request_ptr_err !== null && jsonvalue.error.request_ptr_err !== "")
                {
                    $('#dns_form2 #request_ptr_err').html(jsonvalue.error.request_ptr_err)
                    $('#dns_form2 #request_ptr_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_ptr_err').html("")
                }
                if (jsonvalue.error.request_ptr_err1 !== undefined && jsonvalue.error.request_ptr_err1 !== null && jsonvalue.error.request_ptr_err1 !== "")
                {
                    $('#dns_form2 #request_ptr_err1').html(jsonvalue.error.request_ptr_err1)
                    $('#dns_form2 #request_ptr_2').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_ptr_err1').html("")
                }
                if (jsonvalue.error.request_srv_err !== undefined && jsonvalue.error.request_srv_err !== null && jsonvalue.error.request_srv_err !== "")
                {
                    $('#dns_form2 #request_srv_err').html(jsonvalue.error.request_srv_err)
                    $('#dns_form2 #request_srv_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_srv_err').html("")
                }
                if (jsonvalue.error.request_spf_err !== undefined && jsonvalue.error.request_spf_err !== null && jsonvalue.error.request_spf_err !== "")
                {
                    $('#dns_form2 #request_spf_err').html(jsonvalue.error.request_spf_err)
                    $('#dns_form2 #request_spf_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_spf_err').html("")
                }
                if (jsonvalue.error.request_dmarc_err !== undefined && jsonvalue.error.request_dmarc_err !== null && jsonvalue.error.request_dmarc_err !== "")
                {
                    $('#dns_form2 #request_dmarc_err').html(jsonvalue.error.request_dmarc_err)
                    $('#dns_form2 #request_dmarc').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_dmarc_err').html(jsonvalue.error.request_dmarc_err)
                }
                if (jsonvalue.error.request_txt_err !== undefined && jsonvalue.error.request_txt_err !== null && jsonvalue.error.request_txt_err !== "")
                {
                    $('#dns_form2 #request_txt_err').html(jsonvalue.error.request_txt_err)
                    $('#dns_form2 #request_txt1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #dns_loc_err').html("")
                }
                var a = checkDupDNSPrvw('.class_name_url1');
                var b = checkDupDNSPrvw('.class_name_ip1');
                var c = checkDupDNSPrvw('.class_name_loc1');
                if (a === true && b === true && c === true) {
                    $('#dns_form2 #dns_url_err').html("You entered duplicate values");
                    error_flag = false;
                    $('#dns_single_form #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else
                {
                    // $('#dns_single_form #dns_url_err').html("");
                }
                // start, code added by pr on 22ndjan18
                if (jsonvalue.error.csrf_error !== undefined && jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "")
                {
                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // profile on 20th march
                if (jsonvalue.error.employment_error !== undefined && jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "")
                {
                    $('#dns_form2 #useremployment_error').focus();
                    $('#dns_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== undefined && jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "")
                {
                    $('#dns_form2 #minerror').focus();
                    $('#dns_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== undefined && jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "")
                {
                    $('#dns_form2 #deperror').focus();
                    $('#dns_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== undefined && jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "")
                {
                    $('#dns_form2 #other_dept').focus();
                    $('#dns_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== undefined && jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "")
                {
                    $('#dns_form2 #smierror').focus();
                    $('#dns_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== undefined && jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "")
                {
                    $('#dns_form2 #state_error').focus();
                    $('#dns_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== undefined && jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "")
                {
                    $('#dns_form2 #org_error').focus();
                    $('#dns_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== undefined && jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "")
                {
                    $('#dns_form2 #ca_design').focus();
                    $('#dns_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== undefined && jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "")
                {
                    $('#dns_form2 #hod_name').focus();
                    $('#dns_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== undefined && jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "")
                {
                    $('#dns_form2 #hod_mobile').focus();
                    $('#dns_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== undefined && jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "")
                {
                    $('#dns_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== undefined && jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "")
                {
                    $('#dns_form2 #hod_tel').focus();
                    $('#dns_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #hodtel_error').html("");
                }
                if (jsonvalue.error.emp_code_error !== undefined && jsonvalue.error.emp_code_error !== null && jsonvalue.error.emp_code_error !== "")
                {
                    $('#dns_form2 #userempcode_error').focus();
                    $('#dns_form2 #userempcode_error').html(jsonvalue.error.emp_code_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #userempcode_error').html("");
                }
                if (!jQuery.isEmptyObject(jsonvalue.error)) {
                    fetchErrorFunc(jsonvalue.error);
                    error_flag = false;
                }
                // profile on 20th march
                if (error_flag) {
                    // show modal
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'dns'},
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
                $('.loader').hide();
                $('#tab1').show();
            }
        });
    });
    $('#dns_form2 .edit').click(function () {
        var employment = $('#dns_preview_tab #user_employment').val();
        var min = $('#dns_preview_tab #min').val();
        var dept = $('#dns_preview_tab #dept').val();
        var statecode = $('#dns_preview_tab #stateCode').val();
        var Smi = $('#dns_preview_tab #Smi').val();
        var Org = $('#dns_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#min');
                select.find('option').remove();
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
            $('#dns_form2 #central_div').hide();
            $('#dns_form2 #state_div').hide();
            $('#dns_form2 #other_div').show();
            $('#dns_form2 #other_text_div').addClass("display-hide");
            $("#dns_form2 table#successBulkData button").prop('disabled', false);
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
            $('#dns_form2 #central_div').hide();
            $('#dns_form2 #state_div').hide();
            $('#dns_form2 #other_div').hide();
        }
        $('#dns_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#dns_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#dns_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dns_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#dns_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#single_preview_tab #REditPreview #hod_email').removeAttr('disabled');
        $(this).addClass('display-hide');
        var reqValue1 = $("#dns_form2 #req_action1").val();
        if (reqValue1 == "req_delete")
        {
            $('#dns_form2 #ptr_1').prop({
                disabled: true
            });
        }
        //$(this).hide();
    });
    $('#dns_form2 #confirm').click(function () {
        $(".loader").show();
        $("#dns_form2 :disabled").removeAttr('disabled');
        $('#dns_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dns_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#dns_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#dns_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var dns_url = $("#dns_form2 input[name='dns_url[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var dns_cname = $("#dns_form2 input[name='dns_cname[]']").val();
        if (dns_cname !== "")
        {
            dns_cname = $("#dns_form2 input[name='dns_cname[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        } else {
            dns_cname = $("#dns_form2 input[name='dns_cname[]']").val();
        }
        var dns_oldip = $("#dns_form2 input[name='dns_old_ip[]']").val();
        if (dns_oldip !== "")
        {
            dns_oldip = $("#dns_form2 input[name='dns_old_ip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        var dns_newip = $("#dns_form2 input[name='dns_new_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var data = JSON.stringify($('#dns_form2').serializeObject());
        $('#dns_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        var submit = $('#dns_form2 #confirm').val();
        var cert = $('#dns_form2 #cert').val();
        var stat_type = "";
        $.ajax({
            type: "POST",
            url: "dns_tab2",
            data: {data: data, dns_url: dns_url, dns_oldip: dns_oldip, dns_newip: dns_newip, dns_cname: dns_cname, action_type: "validate", CSRFRandom: CSRFRandom, request_type: submit, cert: cert, stat_type: stat_type},
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 22ndjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.file_err !== undefined && jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "")
                {
                    $('#dns_form2 #file_err').html(jsonvalue.error.file_err)
                    error_flag = false;
                    $('#dns_form2 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_form2 #captcha1').val("");
                } else {
                    $('#dns_form2 #file_err').html("")
                }
                if (jsonvalue.error.dns_cname_error !== undefined && jsonvalue.error.dns_cname_error !== null && jsonvalue.error.dns_cname_error !== "")
                {
                    $('#dns_form2 #dns_cname_error').html(jsonvalue.error.dns_cname_error)
                    $('#dns_form2 #dns_cname_error').focus();
                    error_flag = false;
                    $('#dns_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dns_form2 #dns_cname_error').html("")
                }
                if (jsonvalue.error.dns_new_ip_error !== undefined && jsonvalue.error.dns_new_ip_error !== null && jsonvalue.error.dns_new_ip_error !== "")
                {
                    $('#dns_form2 #dns_new_ip_error').html(jsonvalue.error.dns_new_ip_error)
                    $('#dns_form2 #dns_new_ip_error').focus();
                    error_flag = false;
                    $('#dns_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dns_form2 #dns_new_ip_error').html("")
                    $('#dns_form2 #old_ip_err').addClass('display-hide');
                }
                if (jsonvalue.error.dns_old_ip_error !== undefined && jsonvalue.error.dns_old_ip_error !== null && jsonvalue.error.dns_old_ip_error !== "")
                {
                    // $('#dns_form2 #dns_old_ip_error').removeClass("display-hide");
                    $('#dns_form2 #dns_old_ip_error').html(jsonvalue.error.dns_old_ip_error)
                    $('#dns_form2 #dns_old_ip_error').focus();
                    error_flag = false;
                    $('#dns_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dns_form2 #dns_old_ip_error').html("")
                }
                if (jsonvalue.error.dns_url_error !== undefined && jsonvalue.error.dns_url_error !== null && jsonvalue.error.dns_url_error !== "")
                {
                    $('#dns_form2 #dns_url_err').html(jsonvalue.error.dns_url_error)
                    $('#dns_form2 #dns_url_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #dns_url_err').html("")
                    $("#dns_form2 .iploc_Err").css("display", "block");
                }
                if (jsonvalue.error.dns_ip_error !== undefined && jsonvalue.error.dns_ip_error !== null && jsonvalue.error.dns_ip_error !== "")
                {
                    $('#dns_form2 #dns_ip_err').html(jsonvalue.error.dns_ip_error)
                    $('#dns_form2 #dns_ip').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #dns_ip_err').html("")
                }
                if (jsonvalue.error.dns_loc_error !== undefined && jsonvalue.error.dns_loc_error !== null && jsonvalue.error.dns_loc_error !== "")
                {
                    $('#dns_form2 #dns_loc_error').html(jsonvalue.error.dns_loc_error)
                    $('#dns_form2 #dns_loc').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #dns_loc_error').html("")
                }
                if (jsonvalue.error.request_mx_err !== undefined && jsonvalue.error.request_mx_err !== null && jsonvalue.error.request_mx_err !== "")
                {
                    $('#dns_form2 #request_mx_err').html(jsonvalue.error.request_mx_err)
                    $('#dns_form2 #request_mx_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_mx_err').html("")
                }
                if (jsonvalue.error.request_mx_err1 !== undefined && jsonvalue.error.request_mx_err1 !== null && jsonvalue.error.request_mx_err1 !== "")
                {
                    $('#dns_form2 #request_mx_err1').html(jsonvalue.error.request_mx_err1)
                    $('#dns_form2 #request_mx_2').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_mx_err1').html("")
                }
                if (jsonvalue.error.request_ptr_err !== undefined && jsonvalue.error.request_ptr_err !== null && jsonvalue.error.request_ptr_err !== "")
                {
                    $('#dns_form2 #request_ptr_err').html(jsonvalue.error.request_ptr_err)
                    $('#dns_form2 #request_ptr_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_ptr_err').html("")
                }
                if (jsonvalue.error.request_ptr_err1 !== undefined && jsonvalue.error.request_ptr_err1 !== null && jsonvalue.error.request_ptr_err1 !== "")
                {
                    $('#dns_form2 #request_ptr_err1').html(jsonvalue.error.request_ptr_err1)
                    $('#dns_form2 #request_ptr_2').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_ptr_err1').html("")
                }
                if (jsonvalue.error.request_srv_err !== undefined && jsonvalue.error.request_srv_err !== null && jsonvalue.error.request_srv_err !== "")
                {
                    $('#dns_form2 #request_srv_err').html(jsonvalue.error.request_srv_err)
                    $('#dns_form2 #request_srv_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_srv_err').html("")
                }
                if (jsonvalue.error.request_spf_err !== undefined && jsonvalue.error.request_spf_err !== null && jsonvalue.error.request_spf_err !== "")
                {
                    $('#dns_form2 #request_spf_err').html(jsonvalue.error.request_spf_err)
                    $('#dns_form2 #request_spf_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_spf_err').html("")
                }
                if (jsonvalue.error.request_txt_err !== undefined && jsonvalue.error.request_txt_err !== null && jsonvalue.error.request_txt_err !== "")
                {
                    $('#dns_form2 #request_txt_err').html(jsonvalue.error.request_txt_err)
                    $('#dns_form2 #request_txt1').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #dns_loc_err').html("")
                }
                if (jsonvalue.error.request_dmarc_err !== undefined && jsonvalue.error.request_dmarc_err !== null && jsonvalue.error.request_dmarc_err !== "")
                {
                    $('#dns_form2 #request_dmarc_err').html(jsonvalue.error.request_dmarc_err)
                    $('#dns_form2 #request_dmarc').focus();
                    error_flag = false;
                } else {
                    $('#dns_form2 #request_dmarc_err').html(jsonvalue.error.request_dmarc_err)
                }
                var a = checkDupDNSPrvw('.class_name_url1');
                var b = checkDupDNSPrvw('.class_name_ip1');
                var c = checkDupDNSPrvw('.class_name_loc1');
                if (a === true && b === true && c === true) {
                    $('#dns_form2 #dns_url_err').html("You entered duplicate values");
                    error_flag = false;
                    $('#dns_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_form2 #imgtxt').val("");
                } else
                {
                    //$('#dns_form2 #dns_url_err').html("");
                }
                // start, code added by pr on 22ndjan18
                if (jsonvalue.error.csrf_error !== undefined && jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "")
                {
                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // end, code added by pr on 22ndjan18
                // profile on 20th march
                if (jsonvalue.error.employment_error !== undefined && jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "")
                {
                    $('#dns_form2 #useremployment_error').focus();
                    $('#dns_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== undefined && jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "")
                {
                    $('#dns_form2 #minerror').focus();
                    $('#dns_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== undefined && jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "")
                {
                    $('#dns_form2 #deperror').focus();
                    $('#dns_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== undefined && jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "")
                {
                    $('#dns_form2 #other_dept').focus();
                    $('#dns_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== undefined && jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "")
                {
                    $('#dns_form2 #smierror').focus();
                    $('#dns_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== undefined && jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "")
                {
                    $('#dns_form2 #state_error').focus();
                    $('#dns_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== undefined && jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "")
                {
                    $('#dns_form2 #org_error').focus();
                    $('#dns_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== undefined && jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "")
                {
                    $('#dns_form2 #ca_design').focus();
                    $('#dns_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== undefined && jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "")
                {
                    $('#dns_form2 #hod_name').focus();
                    $('#dns_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== undefined && jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "")
                {
                    $('#dns_form2 #hod_mobile').focus();
                    $('#dns_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== undefined && jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "")
                {
                    $('#dns_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== undefined && jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "")
                {
                    $('#dns_form2 #hod_tel').focus();
                    $('#dns_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #hodtel_error').html("");
                }
                if (jsonvalue.error.emp_code_error !== undefined && jsonvalue.error.emp_code_error !== null && jsonvalue.error.emp_code_error !== "")
                {
                    $('#dns_form2 #userempcode_error').focus();
                    $('#dns_form2 #userempcode_error').html(jsonvalue.error.emp_code_error);
                    error_flag = false;
                } else {
                    $('#dns_form2 #userempcode_error').html("");
                }
                if (!jQuery.isEmptyObject(jsonvalue.error)) {
                    fetchErrorFunc(jsonvalue.error);
                    error_flag = false;
                }
                // profile on 20th march
                if (!error_flag) {
                    $("#dns_form2 :disabled").removeAttr('disabled');
                    $('#dns_form2 #dns_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#dns_form2 #dns_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#dns_form2 #dns_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {
                    if ($('#dns_form2 #tnc').is(":checked"))
                    {

                        $('#dns_form2 #tnc_error').html("");
                        $('#dns_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#stack3').modal({backdrop: 'static', keyboard: false});

                        if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
                        {
                            $('#dns_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#dns_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#dns_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);


                        } else if (jsonvalue.form_details.min == "External Affairs")
                        {
                            $('#dns_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
                            $('#dns_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
                            $('#dns_form_confirm #fill_hod_mobile').html("+919384664224");
                        } else {
                            $('#dns_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#dns_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#dns_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                        }

                    } else {
                        $('#dns_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        //$('#dns_form2 #dns_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        //$('#dns_form2 #dns_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //below line commented by MI on 25thjan19
                        //$('#dns_form2 #dns_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        $("#dns_form2 #tnc").removeAttr('disabled');
                    }
                }
            },
            complete: function () {
                $('.loader').hide();
            }
        });
    });
    $('#dns_form_confirm #confirmYes').click(function () {
        $('#dns_form2').submit();
        $('#stack3').modal('toggle');
    });
    $('#request_close').click(function () {
        window.location = 'Dns_registration.jsp';
    });
    $(document).on('click', '#dns_preview .edit', function () {
        // $('#dns_preview .edit').click(function () {
        var employment = $('#dns_preview_tab #user_employment').val();
        var min = $('#dns_preview_tab #min').val();
        var dept = $('#dns_preview_tab #dept').val();

        var statecode = $('#dns_preview_tab #stateCode').val();
        var Smi = $('#dns_preview_tab #Smi').val();
        var Org = $('#dns_preview_tab #Org').val();
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dns_preview_tab #min');
                select.find('option').remove();
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
                var select = $('#dns_preview_tab #dept');
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
                $('#dns_preview_tab #other_text_div').removeClass("display-hide");
            }
        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dns_preview_tab #stateCode');
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
                var select = $('#dns_preview_tab #Smi');
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
            $('#dns_form2 #central_div').hide();
            $('#dns_form2 #state_div').hide();
            $('#dns_form2 #other_div').show();
            $('#dns_form2 #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dns_preview_tab #Org');
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
            $('#dns_preview_tab #central_div').hide();
            $('#dns_preview_tab #state_div').hide();
            $('#dns_preview_tab #other_div').hide();
        }
        $('#dns_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#dns_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#dns_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dns_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            
        if ($("#comingFrom").val("admin"))
        {
            $("#dns_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#dns_preview .save-changes").show();//26-04-2022 added by RAJESH
            $("#dns_preview .save-changes").html("Update");
        }
        var reqValue1 = $("#dns_preview #req_action1").val();
        if (reqValue1 == "req_delete")
        {
            $('#dns_preview #ptr_1').prop({
                disabled: true
            });
        }
        // code end
    });
    //$('#dns_preview #confirm').click(function (e) {
    $(document).on('click', '#dns_preview #confirm', function () {
        var submit = $('#dns_preview #confirm').val();
        var req_action = $('#dns_preview #req_action').val();
        if (submit == "dns_bulk") {
            //e.preventDefault();
            $('.loader').show();
            var data = JSON.stringify($('#dns_preview').serializeObject());
            var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
            var cert = $('#dns_preview #cert').val();
            $.ajax({
                type: "POST",
                url: "dns_tab4",
                data: {data: data, CSRFRandom: CSRFRandom, request_type: req_action, cert: cert},
                datatype: JSON,
                success: function (data)
                {
                    resetCSRFRandom();// line added by pr on 22ndjan18
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    var error_flag = true;
                    if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined)
                    {
                        $('#dns_preview #file_err1').html(jsonvalue.error.file_err)
                        error_flag = false;
                        $('#dns_preview #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#dns_preview #captcha1').val("");
                    } else {
                        $('#dns_preview #file_err').html("")
                    }
                    if (jsonvalue.error.request_mx_err !== undefined && jsonvalue.error.request_mx_err !== null && jsonvalue.error.request_mx_err !== "")
                    {
                        $('#dns_preview #request_mx_err').html(jsonvalue.error.request_mx_err)
                        $('#dns_preview #request_mx_1').focus();
                        error_flag = false;
                    } else {
                        $('#dns_preview #request_mx_err').html("")
                    }
                    if (jsonvalue.error.request_mx_err1 !== undefined && jsonvalue.error.request_mx_err1 !== null && jsonvalue.error.request_mx_err1 !== "")
                    {
                        $('#dns_preview #request_mx_err1').html(jsonvalue.error.request_mx_err1)
                        $('#dns_preview #request_mx_2').focus();
                        error_flag = false;
                    } else {
                        $('#dns_preview #request_mx_err1').html("")
                    }
                    if (jsonvalue.error.request_ptr_err !== undefined && jsonvalue.error.request_ptr_err !== null && jsonvalue.error.request_ptr_err !== "")
                    {
                        $('#dns_preview #request_ptr_err').html(jsonvalue.error.request_ptr_err)
                        $('#dns_preview #request_ptr_1').focus();
                        error_flag = false;
                    } else {
                        $('#dns_preview #request_ptr_err').html("")
                    }
                    if (jsonvalue.error.request_ptr_err1 !== undefined && jsonvalue.error.request_ptr_err1 !== null && jsonvalue.error.request_ptr_err1 !== "")
                    {
                        $('#dns_preview #request_ptr_err1').html(jsonvalue.error.request_ptr_err1)
                        $('#dns_preview #request_ptr_2').focus();
                        error_flag = false;
                    } else {
                        $('#dns_preview #request_ptr_err1').html("")
                    }
                    if (jsonvalue.error.request_srv_err !== undefined && jsonvalue.error.request_srv_err !== null && jsonvalue.error.request_srv_err !== "")
                    {
                        $('#dns_preview #request_srv_err').html(jsonvalue.error.request_srv_err)
                        $('#dns_preview #request_srv_1').focus();
                        error_flag = false;
                    } else {
                        $('#dns_preview #request_srv_err').html("")
                    }
                    if (jsonvalue.error.request_spf_err !== undefined && jsonvalue.error.request_spf_err !== null && jsonvalue.error.request_spf_err !== "")
                    {
                        $('#dns_preview #request_spf_err').html(jsonvalue.error.request_spf_err)
                        $('#dns_preview #request_spf_1').focus();
                        error_flag = false;
                    } else {
                        $('#dns_preview #request_spf_err').html("")
                    }
                    if (jsonvalue.error.request_dmarc_err !== undefined && jsonvalue.error.request_dmarc_err !== null && jsonvalue.error.request_dmarc_err !== "")
                    {
                        $('#dns_preview #request_dmarc_err').html(jsonvalue.error.request_dmarc_err)
                        $('#dns_preview #request_dmarc').focus();
                        error_flag = false;
                    } else {
                        $('#dns_preview #request_dmarc_err').html(jsonvalue.error.request_dmarc_err)
                    }
                    if (jsonvalue.error.request_txt_err !== undefined && jsonvalue.error.request_txt_err !== null && jsonvalue.error.request_txt_err !== "")
                    {
                        $('#dns_preview #request_txt_err').html(jsonvalue.error.request_txt_err)
                        $('#dns_preview #request_txt1').focus();
                        error_flag = false;
                    } else {
                        $('#dns_preview #dns_loc_err').html("")
                    }
                    if (jsonvalue.error.emp_code_error !== undefined && jsonvalue.error.emp_code_error !== null && jsonvalue.error.emp_code_error !== "")
                    {
                        $('#dns_preview #userempcode_error').focus();
                        $('#dns_preview #userempcode_error').html(jsonvalue.error.emp_code_error);
                        error_flag = false;
                    } else {
                        $('#dns_preview #userempcode_error').html("");
                    }
                    if (jsonvalue.error.csrf_error !== undefined && jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "")
                    {
                        $('#captchaerror').html(jsonvalue.error.csrf_error);
                        error_flag = false;
                    }
                    if (!jQuery.isEmptyObject(jsonvalue.error)) {
                        fetchErrorFunc(jsonvalue.error);
                        error_flag = false;
                    }
                    if (!error_flag) {
                    } else {
                        $('#dns_preview_form').modal('hide');
                        $('#dnsbulk-22').removeClass('display-hide');
                        $('#mainpage').addClass('display-hide');
                        //console.log('jsonvalue.form_details.request_cname: ' + jsonvalue.form_details.request_cname);
                    }
                }, error: function ()
                {
                    $('#tab1').show();
                },
                complete: function () {
                    $('.loader').hide();
                }
            });
        } else {
            $('#dns_preview').submit();
        }
    });
    $('#dns_preview').submit(function (e) {
        $('.loader').show();
        e.preventDefault();
        $("#dns_preview :disabled").removeAttr('disabled');
        var dns_cname = $("#dns_preview input[name='dns_cname[]']").val();
        if (dns_cname !== "")
        {
            dns_cname = $("#dns_preview input[name='dns_cname[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        } else {
            dns_cname = $("#dns_preview input[name='dns_cname[]']").val();
        }
        var dns_oldip = $("#dns_preview input[name='dns_old_ip[]']").val();
        if (dns_oldip !== "")
        {
            dns_oldip = $("#dns_preview input[name='dns_old_ip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        var dns_url = $("#dns_preview input[name='dns_url[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var dns_newip = $("#dns_preview input[name='dns_new_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        $('#dns_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dns_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march  
        $('#dns_preview_tab #user_email').removeAttr('disabled');// 20th march                  
        var data = JSON.stringify($('#dns_preview').serializeObject());
        $('#dns_preview_tab #user_email').prop('disabled', 'true'); // 20th march        
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18
        var cert = $('#dns_preview #cert').val();
        var stat_type = $('#dns_preview #stat_type').val();

        $.ajax({
            type: "POST",
            url: "dns_tab2",
            data: {data: data, dns_url: dns_url, dns_oldip: dns_oldip, dns_newip: dns_newip, dns_cname: dns_cname, action_type: "update", CSRFRandom: CSRFRandom, cert: cert, stat_type: stat_type}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 22ndjan18                
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.file_err !== undefined && jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "")
                {
                    $('#dns_preview #file_err').html(jsonvalue.error.file_err)
                    error_flag = false;
                    $('#dns_preview #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_preview #captcha1').val("");
                } else {
                    $('#dns_preview #file_err').html("")
                }
                if (jsonvalue.error.dns_url_error !== undefined && jsonvalue.error.dns_url_error !== null && jsonvalue.error.dns_url_error !== "")
                {
                    $('#dns_preview #dns_url_err').html(jsonvalue.error.dns_url_error)
                    $('#dns_preview #dns_url_1').focus();
                    error_flag = false;
                    $('#dns_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dns_preview #dns_url_err').html("")
                    $("#dns_preview .iploc_Err").css("display", "block");
                }
                if (jsonvalue.error.dns_cname_error !== undefined && jsonvalue.error.dns_cname_error !== null && jsonvalue.error.dns_cname_error !== "")
                {
                    $('#dns_preview #dns_cname_error').html(jsonvalue.error.dns_cname_error)
                    $('#dns_preview #dns_cname_error').focus();
                    error_flag = false;
                    $('#dns_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dns_preview #dns_cname_error').html("")
                }
                if (jsonvalue.error.dns_new_ip_error !== undefined && jsonvalue.error.dns_new_ip_error !== null && jsonvalue.error.dns_new_ip_error !== "")
                {
                    $('#dns_preview #dns_new_ip_error').html(jsonvalue.error.dns_new_ip_error)
                    $('#dns_preview #dns_new_ip_error').focus();
                    error_flag = false;
                    $('#dns_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dns_preview #dns_new_ip_error').html("")
                    $('#dns_preview #old_ip_err').addClass('display-hide');
                }
                if (jsonvalue.error.dns_old_ip_error !== undefined && jsonvalue.error.dns_old_ip_error !== null && jsonvalue.error.dns_old_ip_error !== "")
                {
                    $('#dns_preview #dns_old_ip_error').removeClass("display-hide");
                    $('#dns_preview #dns_old_ip_error').html(jsonvalue.error.dns_old_ip_error)
                    $('#dns_preview #dns_old_ip_error').focus();
                    error_flag = false;
                    $('#dns_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dns_preview #dns_old_ip_error').html("")
                }
                if (jsonvalue.error.dns_loc_error !== undefined && jsonvalue.error.dns_loc_error !== null && jsonvalue.error.dns_loc_error !== "")
                {
                    $('#dns_preview #dns_loc_error').html(jsonvalue.error.dns_loc_error)
                    $('#dns_preview #dns_loc').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #dns_loc_error').html("")
                }
                if (jsonvalue.error.request_mx_err !== undefined && jsonvalue.error.request_mx_err !== null && jsonvalue.error.request_mx_err !== "")
                {
                    $('#dns_preview #request_mx_err').html(jsonvalue.error.request_mx_err)
                    $('#dns_preview #request_mx_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_mx_err').html("")
                }
                if (jsonvalue.error.request_mx_err1 !== undefined && jsonvalue.error.request_mx_err1 !== null && jsonvalue.error.request_mx_err1 !== "")
                {
                    $('#dns_preview #request_mx_err1').html(jsonvalue.error.request_mx_err1)
                    $('#dns_preview #request_mx_2').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_mx_err1').html("")
                }
                if (jsonvalue.error.request_ptr_err !== undefined && jsonvalue.error.request_ptr_err !== null && jsonvalue.error.request_ptr_err !== "")
                {
                    $('#dns_preview #request_ptr_err').html(jsonvalue.error.request_ptr_err)
                    $('#dns_preview #request_ptr_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_ptr_err').html("")
                }
                if (jsonvalue.error.request_ptr_err1 !== undefined && jsonvalue.error.request_ptr_err1 !== null && jsonvalue.error.request_ptr_err1 !== "")
                {
                    $('#dns_preview #request_ptr_err1').html(jsonvalue.error.request_ptr_err1)
                    $('#dns_preview #request_ptr_2').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_ptr_err1').html("")
                }
                if (jsonvalue.error.request_srv_err !== undefined && jsonvalue.error.request_srv_err !== null && jsonvalue.error.request_srv_err !== "")
                {
                    $('#dns_preview #request_srv_err').html(jsonvalue.error.request_srv_err)
                    $('#dns_preview #request_srv_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_srv_err').html("")
                }
                if (jsonvalue.error.request_spf_err !== undefined && jsonvalue.error.request_spf_err !== null && jsonvalue.error.request_spf_err !== "")
                {
                    $('#dns_preview #request_spf_err').html(jsonvalue.error.request_spf_err)
                    $('#dns_preview #request_spf_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_spf_err').html("")
                }
                if (jsonvalue.error.request_dmarc_err !== undefined && jsonvalue.error.request_dmarc_err !== null && jsonvalue.error.request_dmarc_err !== "")
                {
                    $('#dns_preview #request_dmarc_err').html(jsonvalue.error.request_dmarc_err)
                    $('#dns_preview #request_dmarc').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_dmarc_err').html(jsonvalue.error.request_dmarc_err)
                }
                if (jsonvalue.error.request_txt_err !== undefined && jsonvalue.error.request_txt_err !== null && jsonvalue.error.request_txt_err !== "")
                {
                    $('#dns_preview #request_txt_err').html(jsonvalue.error.request_txt_err)
                    $('#dns_preview #request_txt1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #dns_loc_err').html("")
                }
                var a = checkDupDNSPrvw('.class_name_url1');
                var b = checkDupDNSPrvw('.class_name_ip1');
                var c = checkDupDNSPrvw('.class_name_loc1');
                if (a === true && b === true && c === true) {
                    $('#dns_preview #dns_url_err').html("You entered duplicate values");
                    error_flag = false;
                    $('#dns_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_preview #imgtxt').val("");
                } else
                {
                    // $('#dns_preview #dns_url_err').html("");
                }
                // start, code added by pr on 22ndjan18
                if (jsonvalue.error.csrf_error !== undefined && jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "")
                {
                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // end, code added by pr on 22ndjan18
                // profile 20th march 
                if (jsonvalue.error.employment_error !== undefined && jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "")
                {
                    $('#dns_preview #useremployment_error').focus();
                    $('#dns_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#dns_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== undefined && jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "")
                {
                    $('#dns_preview #minerror').focus();
                    $('#dns_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#dns_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== undefined && jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "")
                {
                    $('#dns_preview #deperror').focus();
                    $('#dns_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#dns_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== undefined && jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "")
                {
                    $('#dns_preview #other_dept').focus();
                    $('#dns_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#dns_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== undefined && jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "")
                {
                    $('#dns_preview #smierror').focus();
                    $('#dns_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#dns_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== undefined && jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "")
                {
                    $('#dns_preview #state_error').focus();
                    $('#dns_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#dns_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== undefined && jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "")
                {
                    $('#dns_preview #org_error').focus();
                    $('#dns_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#dns_preview #org_error').html("");
                }
                // profile 20th march 
                if (jsonvalue.error.emp_code_error !== undefined && jsonvalue.error.emp_code_error !== null && jsonvalue.error.emp_code_error !== "")
                {
                    $('#dns_preview #userempcode_error').focus();
                    $('#dns_preview #userempcode_error').html(jsonvalue.error.emp_code_error);
                    error_flag = false;
                } else {
                    $('#dns_preview #userempcode_error').html("");
                }

                if (!jQuery.isEmptyObject(jsonvalue.error)) {
                    fetchErrorFunc(jsonvalue.error);
                    error_flag = false;
                }
                if (!error_flag)
                {
                    $("#dns_preview :disabled").removeAttr('disabled');
                    $('#dns_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#dns_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {
                    $('#dns_preview_form').modal('toggle');
                }
            }, error: function (request, error)
            {
                console.log('error is : ' + error);
            }, complete: function () {
                $('.loader').hide();
            }
        });
    });
    $('#dns_bulk_prvw_form3').submit(function (e) {
        e.preventDefault();
        $("#dns_preview :disabled").removeAttr('disabled');
        var dns_url = $("#dns_preview input[name='dns_url[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var dns_ip = $("#dns_preview input[name='dns_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var dns_loc = $("#dns_preview input[name='dns_loc[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        $('#dns_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dns_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march  
        $('#dns_preview_tab #user_email').removeAttr('disabled');// 20th march                  
        var data = JSON.stringify($('#dns_preview').serializeObject());
        $('#dns_preview_tab #user_email').prop('disabled', 'true'); // 20th march        
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18
        var cert = $('#dns_preview #cert').val();
        var stat_type = "";
        $.ajax({
            type: "POST",
            url: "dns_tab2",
            data: {data: data, dns_url: dns_url, dns_ip: dns_ip, dns_loc: dns_loc, action_type: "update", CSRFRandom: CSRFRandom, cert: cert, stat_type: stat_type}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 22ndjan18                
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.old_url_error !== null && jsonvalue.error.old_url_error !== "" && jsonvalue.error.old_url_error !== undefined)
                {
                    $('#dns_preview #old_url_error').html(jsonvalue.error.old_url_error)
                    $('#dns_preview #old_url_error').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #old_url_error').html("")
                }
                if (jsonvalue.error.old_ip_error !== null && jsonvalue.error.old_ip_error !== "" && jsonvalue.error.old_ip_error !== undefined)
                {
                    $('#dns_preview #old_ip_error').html(jsonvalue.error.old_ip_error)
                    $('#dns_preview #old_ip_error').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #old_ip_error').html("")
                }
                if (jsonvalue.error.dns_url_error !== null && jsonvalue.error.dns_url_error !== "" && jsonvalue.error.dns_url_error !== undefined)
                {
                    $('#dns_preview #dns_url_err').html(jsonvalue.error.dns_url_error)
                    $('#dns_preview #dns_url_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #dns_url_err').html("")
                }
                if (jsonvalue.error.dns_ip_error !== null && jsonvalue.error.dns_ip_error !== "" && jsonvalue.error.dns_ip_error !== undefined)
                {
                    $('#dns_preview #dns_ip_err').html(jsonvalue.error.dns_ip_error)
                    $('#dns_preview #dns_ip').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #dns_ip_err').html("")
                }
                if (jsonvalue.error.dns_loc_error !== null && jsonvalue.error.dns_loc_error !== "" && jsonvalue.error.dns_loc_error !== undefined)
                {
                    $('#dns_preview #dns_loc_error').html(jsonvalue.error.dns_loc_error)
                    $('#dns_preview #dns_loc').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #dns_loc_error').html("")
                }
                if (jsonvalue.error.request_cname_err !== null && jsonvalue.error.request_cname_err !== "" && jsonvalue.error.request_cname_err !== undefined)
                {
                    $('#dns_preview #request_cname_err').html(jsonvalue.error.request_cname_err)
                    $('#dns_preview #request_cname1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_cname_err').html("")
                }
                if (jsonvalue.error.request_aaa_err !== null && jsonvalue.error.request_aaa_err !== "" && jsonvalue.error.request_aaa_err !== undefined)
                {
                    $('#dns_preview #request_aaa_err').html(jsonvalue.error.request_aaa_err)
                    $('#dns_preview #request_aaaa_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_aaa_err').html("")
                }
                if (jsonvalue.error.request_mx_err !== null && jsonvalue.error.request_mx_err !== "" && jsonvalue.error.request_mx_err !== undefined)
                {
                    $('#dns_preview #request_mx_err').html(jsonvalue.error.request_mx_err)
                    $('#dns_preview #request_mx_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_mx_err').html("")
                }
                if (jsonvalue.error.request_ptr_err !== null && jsonvalue.error.request_ptr_err !== "" && jsonvalue.error.request_ptr_err !== undefined)
                {
                    $('#dns_preview #request_ptr_err').html(jsonvalue.error.request_ptr_err)
                    $('#dns_preview #request_ptr_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_ptr_err').html("")
                }
                if (jsonvalue.error.request_srv_err !== null && jsonvalue.error.request_srv_err !== "" && jsonvalue.error.request_srv_err !== undefined)
                {
                    $('#dns_preview #request_srv_err').html(jsonvalue.error.request_srv_err)
                    $('#dns_preview #request_srv_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_srv_err').html("")
                }
                if (jsonvalue.error.request_spf_err !== null && jsonvalue.error.request_spf_err !== "" && jsonvalue.error.request_spf_err !== undefined)
                {
                    $('#dns_preview #request_spf_err').html(jsonvalue.error.request_spf_err)
                    $('#dns_preview #request_spf_1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_spf_err').html("")
                }
                if (jsonvalue.error.request_dmarc_err !== null && jsonvalue.error.request_dmarc_err !== "" && jsonvalue.error.request_dmarc_err !== undefined)
                {
                    $('#dns_preview #request_dmarc_err').html(jsonvalue.error.request_dmarc_err)
                    $('#dns_preview #request_dmarc').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #request_dmarc_err').html(jsonvalue.error.request_dmarc_err)
                }
                if (jsonvalue.error.request_txt_err !== null && jsonvalue.error.request_txt_err !== "" && jsonvalue.error.request_txt_err !== undefined)
                {
                    $('#dns_preview #request_txt_err').html(jsonvalue.error.request_txt_err)
                    $('#dns_preview #request_txt1').focus();
                    error_flag = false;
                } else {
                    $('#dns_preview #dns_loc_err').html("")
                }
                var a = checkDupDNSPrvw('.class_name_url1');
                var b = checkDupDNSPrvw('.class_name_ip1');
                var c = checkDupDNSPrvw('.class_name_loc1');
                if (a === true && b === true && c === true) {
                    $('#dns_preview #dns_url_err').html("You entered duplicate values");
                    error_flag = false;
                    $('#dns_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dns_preview #imgtxt').val("");
                } else
                {
                    $('#dns_preview #dns_url_err').html("");
                }
                // start, code added by pr on 22ndjan18
                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // end, code added by pr on 22ndjan18
                if (!error_flag)
                {
                } else {
                    window.location.href = "showUserData";
                }
            }, error: function (request, error)
            {
                console.log('error is : ' + error);
            }
        });
    });
});

function deleteRecordByBulkUserId_dns(i, comming_from, campid) {
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
                url: 'bulkDnsDataDelete',
                data: {bulkDataId: i, iCampaignId: campid},
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
                                    validDomForReqModify(data.bulkData)
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

$("#singleBulkEditBtn").click(function () {
    $('.loader').show();
    var ref_no_uploadblkdata = $("#ref_no_uploadblkdata").val();
    var iCampaignId = $("#iCampaignIdDt").val();
    var dataObj = {dnsEditData: $('#singleBulkEdit').serializeObject(), iCampaignId: iCampaignId};
    var comming_from = $("#comming_from").val();
    var data = JSON.stringify(dataObj);
    $.ajax({
        type: 'post',
        url: 'bulkDnsDataEditPost',
        data: data,
        datatype: JSON,
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            if (data.hasOwnProperty('errorMessage') || data.dnsEditData.errorMessage) {
                $('.dns_errorMessage').html("");
                $('.dns_errorMessage').fadeIn('fast').html('<div class="alert alert-danger alert_msg">' + data.dnsEditData.errorMessage + '</div>');
                $('.dns_errorMessage').fadeOut(5000);
            } else {
                $("#bulkUploadEditSingle").modal('hide');
                if (comming_from !== 'mainpage') {
                    validDomForReqModify2(data.bulkData)
                    setTimeout(function () {
                        bulkDomCreate(data.bulkData);
                    }, 50);
                } else {
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
    })
})

function checkNullval(data) {
    if (data === undefined || data === null || data === 'null' || data === 'N/A') {
        return "";
    }
    return data;
}


function fetchErrorFunc(data) {
    console.log(data)
    if (data !== null && data !== "" && data !== undefined)
    {
        console.log(data)
        $.each(data, function (key, val) {
            console.log("key:: " + key)
            $('.' + key).html(val);
        });
    }
    if (data.dns_url_error) {
        $(".dns_url_error1").html(data.dns_url_error)
    }
    if (data.dns_owner_error) {
        $('.dns_owner_error').addClass('alert alert-danger');
        $('.dns_owner_error').html("<b>" + data.dns_owner_error + "</b>");
    } else {
        $('.dns_owner_error').removeClass('alert alert-danger');
        $('.dns_owner_error').html("");
    }
    if (data.file_err) {
        var dns_single_form = $('input[name="dns_single_form"]:checked').val();
        if (dns_single_form == 'single') {
            $(".dns_owner_error").addClass('alert alert-danger');
            $(".dns_owner_error").html('<b>Important: Please fill the form and click to Add Record button before submiting the form.</b>');
            $('.card-holder').addClass('err-holder');
        } else {
            $('.dns_owner_error').addClass('alert alert-danger');
            $('.dns_owner_error').html("<b>" + data.file_err + "</b>");
        }
    } else {
        $('.dns_owner_error').removeClass('alert alert-danger');
        $('.dns_owner_error').html("");
        $('.card-holder').removeClass('err-holder');
    }
    if (data.dns_old_ip_error) {
        $(".dns_old_ip_error1").html(data.dns_old_ip_error)
    }
    if (data.dns_new_ip_error) {
        $(".dns_new_ip_error1").html(data.dns_new_ip_error)
    }
    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
}

function req_radio_func(field_form, reqValue) {
    if (reqValue == "req_new")
    {
        $('#dns_new_ip_error').html("");
        $('#' + field_form + ' #old_ip_err').addClass('display-hide');
        $('#' + field_form + ' #add_ip').html('');
        $('#' + field_form + ' #first_div').removeClass('display-hide');
        $('#' + field_form + ' #second_div').removeClass('display-hide');
        $('#' + field_form + ' #third_div').removeClass('display-hide');
        $('#' + field_form + ' #four_div').removeClass('display-hide');
        $('#' + field_form + ' #other_div_req').addClass('display-hide');
        $('#' + field_form + ' #dns_url_label').html("Domain Name")
        $('#' + field_form + ' #dns_cname').html("CNAME")
        $('#' + field_form + ' #new_ip_label').html("IP Address A OR AAAA <span class='text-danger new_ip_chk'>*</span>")
        $('#' + field_form + ' #req_new_ip').removeClass('display-hide');
        $('#' + field_form + ' #req_old_ip').addClass('display-hide');
        $('#' + field_form + ' #ptr').prop('checked', false);
        $('#' + field_form + ' #ptr_1').prop('checked', false);
        $('#' + field_form + ' #mx').prop('checked', false);
        $('#' + field_form + ' #spf').prop('checked', false);
        $('#' + field_form + ' #srv').prop('checked', false);
        $('#' + field_form + ' #txt').prop('checked', false);
        $('#' + field_form + ' #dmarc').prop('checked', false);
        $('#' + field_form + ' #addiprecord').closest('div').attr('class', 'col-md-12 pt-2 text-center');
        $('#' + field_form + ' #addiprecord').removeClass('mt-4');
//        $('#' + field_form + ' #ptr').prop({
//            disabled: false
//        });
        $('#' + field_form + ' #new_ip_star').removeClass('display-hide');
        $('#req_new_label').show();
        $('#req_modify_label').hide();

        $("#other_data_entry").addClass('d-none');
        $("#cname_entry").removeClass('d-none');
        $('.not-other').removeClass('d-none')
    } else if (reqValue == "req_modify")
    {
        $('#' + field_form + ' #old_ip_err').removeClass('display-hide');
        $('#' + field_form + ' #add_ip').html('');
        $('#' + field_form + ' #first_div').removeClass('display-hide');
        $('#' + field_form + ' #second_div').removeClass('display-hide');
        $('#' + field_form + ' #third_div').removeClass('display-hide');
        $('#' + field_form + ' #four_div').removeClass('display-hide');
        $('#' + field_form + ' #req_old_ip').removeClass('display-hide');
        $('#' + field_form + ' #req_new_ip').removeClass('display-hide');
        $('#' + field_form + ' #domain_label').html("Domain Name")
        $('#' + field_form + ' #dns_cname').html("CNAME")
        $('#' + field_form + ' #req_mod_ip').html("OLD IP Address(A OR AAAA)")
        $('#' + field_form + ' #new_ip_label').html("New IP Address <span class='text-danger new_ip_chk'>*</span>")
        $('#' + field_form + ' #old_ip_label').html("OLD IP Address(A OR AAAA)")
        $('#' + field_form + ' #ptr').prop('checked', false);
        $('#' + field_form + ' #ptr_1').prop('checked', false);
        $('#' + field_form + ' #mx').prop('checked', false);
        $('#' + field_form + ' #spf').prop('checked', false);
        $('#' + field_form + ' #srv').prop('checked', false);
        $('#' + field_form + ' #txt').prop('checked', false);
        $('#' + field_form + ' #addiprecord').closest('div').attr('class', 'col-md-6 pt-2');
        $('#' + field_form + ' #addiprecord').addClass('mt-4');

        $('#' + field_form + ' #dmarc').prop('checked', false);
//        $('#' + field_form + ' #ptr').prop({
//            disabled: false
//        });
        $('#' + field_form + ' #new_ip_star').removeClass('display-hide');
        $('#req_modify_label').show();
        $('#req_new_label').hide();
        $("#other_data_entry").addClass('d-none');
        $("#cname_entry").removeClass('d-none');
        $('.not-other').removeClass('d-none')
    } else if (reqValue == "req_delete")
    {
        $('#dns_new_ip_error').html("");
        $('#' + field_form + ' #add_ip').html('');
        $('#' + field_form + ' #first_div').removeClass('display-hide');
        $('#' + field_form + ' #second_div').removeClass('display-hide');
        $('#' + field_form + ' #third_div').removeClass('display-hide');
        $('#' + field_form + ' #four_div').removeClass('display-hide');
        $('#' + field_form + ' #dns_url_label').html("Domain Name")
        $('#' + field_form + ' #new_ip_label').html("IP Address A OR AAAA")
        $('#' + field_form + ' #dns_cname').html("CNAME")
        $('#' + field_form + ' #req_new_ip').removeClass('display-hide');
        $('#' + field_form + ' #req_old_ip').addClass('display-hide');
//        $('#' + field_form + ' #ptr').prop('checked', true);
//        $('#' + field_form + ' #ptr_1').prop('checked', true);
        $('#' + field_form + ' #mx').prop('checked', false);
        $('#' + field_form + ' #spf').prop('checked', false);
        $('#' + field_form + ' #srv').prop('checked', false);
        $('#' + field_form + ' #txt').prop('checked', false);
        $('#' + field_form + ' #dmarc').prop('checked', false);
        $('#' + field_form + ' #addiprecord').closest('div').attr('class', 'col-md-12 text-center');
        $('#' + field_form + ' #addiprecord').removeClass('mt-4');
        //$('#' + field_form + ' #ptr').attr('readonly', 'readonly');
//        $('#' + field_form + ' #ptr').prop({
//            disabled: true
//        });
        $('#' + field_form + ' #request_ptr1').addClass('display-hide');
        $('#' + field_form + ' #request_ptr').addClass('display-hide');
        $('#' + field_form + ' #new_ip_star').addClass('display-hide');
        $('#req_new_label').show();
        $('#req_modify_label').hide();
        $("#other_data_entry").addClass('d-none');
        $("#cname_entry").removeClass('d-none');
        $('.not-other').removeClass('d-none')
    }
}