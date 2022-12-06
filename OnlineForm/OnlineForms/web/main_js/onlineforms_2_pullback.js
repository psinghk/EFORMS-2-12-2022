
    $(document).on('click', '.previewedit1', function (e) // line modified by pr on 22ndmay18
    {   $("#ViewPendReqRec").modal('toggle');
        $('.loader').show();
        e.preventDefault();

        var status_val = $(this).closest('tr').find('td:eq(2)').text();
        var curr_status = current_status(status_val);

        var ref_no = $(this).closest('tr').children('td.track_id_single:first').text();
        var approve_btn = "<a href='javascript:void(0)' class='approve-changes' onclick=\"requestActionApprove('" +ref_no+ "');\"><i class='fa fa-check'></i> Approve</a>";
        var reject_btn = "<a href='javascript:void(0)' class='reject-changes' onclick=\"requestActionReject('" +ref_no+ "');\"><i class='fa fa-ban'></i> Reject</a>";
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
//        }.

        var showEdit = $(this).attr("id");
        var whichform = ref_no.substring(0, ref_no.indexOf('-'));;

        var comingFrom = 'admin';
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#sms_preview .req_approve').addClass('display-hide');
                                    $('#sms_preview .req_reject ').addClass('display-hide');
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
                            $(".modal-title").html("Preview for " + ref_no);
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
                                $('#dns_preview .req_approve').addClass('display-hide');
                                $('#dns_preview .req_reject ').addClass('display-hide');
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
                            $(".modal-title").html("Preview for " + ref_no);
                            $('#dns_preview #stat_type').val(jsonvalue.stat_type);
                            if (jsonvalue.stat_type == 'completed') {
                                $('#dns_preview .edit').hide();
                            } else {
                                $('#dns_preview .edit').hide();
                            }
                            setTimeout(function(){ 
                                $('#dns_preview .req_approve').addClass('display-hide');
                                $('#dns_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#wifi_preview .req_approve').addClass('display-hide');
                                    $('#wifi_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#singleuser_preview .req_approve').addClass('display-hide');
                                    $('#singleuser_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#ldap_preview .req_approve').addClass('display-hide');
                                    $('#ldap_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#imappop_preview .req_approve').addClass('display-hide');
                                    $('#imappop_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#mobile_preview .req_approve').addClass('display-hide');
                                    $('#mobile_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#nkn_preview .req_approve').addClass('display-hide');
                                    $('#nkn_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#gem_preview .req_approve').addClass('display-hide');
                                    $('#gem_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#bulkuser_preview .req_approve').addClass('display-hide');
                                    $('#bulkuser_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#distribution_preview .req_approve').addClass('display-hide');
                                    $('#distribution_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#relay_preview .req_approve').addClass('display-hide');
                                    $('#relay_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#ip_preview .req_approve').addClass('display-hide');
                                    $('#ip_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#webcast_preview .req_approve').addClass('display-hide');
                                    $('#webcast_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#centralutm_preview .req_approve').addClass('display-hide');
                                    $('#centralutm_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#vpn_preview .req_approve').addClass('display-hide');
                                    $('#vpn_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#email_act_preview .req_approve').addClass('display-hide');
                                    $('#email_act_preview .req_reject ').addClass('display-hide');
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
                                $(".modal-title").html("Preview for " + ref_no);
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
                                    $('#email_deact_preview .req_approve').addClass('display-hide');
                                    $('#email_deact_preview .req_reject ').addClass('display-hide');
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

