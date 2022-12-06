$(document).ready(function () {


    var flag = false;

    $(document).on("keyup", "td[contenteditable]", function (e) {
        var table = $("#example1 tbody");
        table.find('tr').each(function (i) {
            var $tds = $(this).find('td');
            var arr_source = [];
            var arr_dest = [];
            let sourceip = $tds.eq(0).text();
            if (sourceip.indexOf('-') > -1)
            {
                arr_source = sourceip.split('-');
            } else {
                arr_source = sourceip.split('/');
            }
            let destinationip = $tds.eq(1).text();
            if (destinationip.indexOf('-') > -1)
            {
                arr_dest = destinationip.split('-');

            } else {

                arr_dest = destinationip.split('/');
            }
            //var service = $('.ser').val();
            let tdObject = $(this).find('td:eq(2)'); //locate the <td> holding select;
            let selectObject = tdObject.find(".ser option:selected"); //grab the <select> tag assuming that there will be only single select box within that <td> 
            let service = selectObject.val();
            console.log("service 11111111111" + service);
            let ports = $tds.eq(3).text();
            let action = $('.act').val();
            let timeperiod = $tds.eq(5).text();
            var ipregx = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
            var serviceregex = /^[a-zA-Z#0-9\\,.\\_\\-\\/\\(\\)]{2,100}$/;
            var portregex = /^[0-9]{1,10}$/;
            var timeregex = /^[0-9]{0,3}$/
            var $tds1 = $(this).find('td');
            let row1 = $tds1.eq(0);
            let row5 = $tds1.eq(5);
            row5.attr('placeholder', "Default")
            if (arr_source[1] != null && sourceip.indexOf('-') > -1 || sourceip.indexOf('/') > -1)
            {
                if ((arr_source[1].match(ipregx)) && (arr_source[0].match(ipregx) && (sourceip.indexOf('/') == -1))) {
                    $('#firewall_form1 #source_ip_error').html("")
                    $(".sip").removeClass("invalid");
                    flag = true;
                } else if ((arr_source[0].match(ipregx)) && (arr_source[1].match(portregex) && (sourceip.indexOf('/') == -1))) {
                    $('#firewall_form1 #source_ip_error').html("")
                    $(".sip").removeClass("invalid");
                    flag = true;
                } else if ((arr_source[0].match(ipregx)) && ((arr_source[1] <= 32)) && (sourceip.indexOf('/') > -1)) {
                    $('#firewall_form1 #source_ip_error').html("")
                    $(".sip").removeClass("invalid");
                    flag = true;
                } else {
                    $('#firewall_form1 #source_ip_error').html("Enter the Source IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address")
                    var $tds = $(this).find('td');
                    $($tds.eq(0)).addClass("invalid");
                    flag = false;

                }
            }
            if (arr_source[1] == null)
            {
                if ((arr_source[1] == "" || arr_source[1] == null || arr_source[1] == undefined) && arr_source[0].match(ipregx)) {
                    $('#firewall_form1 #source_ip_error').html("")
                    $(".sip").removeClass("invalid");
                    flag = true;
                } else if (sourceip == "all" || sourceip == "any") {
                    $('#firewall_form1 #source_ip_error').html("")
                    $(".sip").removeClass("invalid");
                    flag = true;
                } else {
                    var $tds = $(this).find('td');
                    $('#firewall_form1 #source_ip_error').html("Enter the Source IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address")
                    $($tds.eq(0)).addClass("invalid");
                    flag = false;
                }
            }
            if (arr_dest[1] != null && ((destinationip.indexOf('-') > -1) || (destinationip.indexOf('/') > -1)))
            {
                if ((arr_dest[1].match(ipregx)) && (arr_dest[0].match(ipregx) && (destinationip.indexOf('/') == -1))) {
                    $('#firewall_form1 #destination_ip_error').html("")
                    $(".dip").removeClass("invalid");
                    flag = true;
                } else if ((arr_dest[0].match(ipregx)) && (arr_dest[1].match(portregex) && (destinationip.indexOf('/') == -1))) {
                    $('#firewall_form1 #destination_ip_error').html("")
                    $(".dip").removeClass("invalid");
                    flag = true;
                } else if ((arr_dest[0].match(ipregx)) && ((arr_dest[1] <= 32)) && (destinationip.indexOf('/') > -1)) {
                    $('#firewall_form1 #destination_ip_error').html("")
                    $(".dip").removeClass("invalid");
                    flag = true;
                } else {
                    $('#firewall_form1 #destination_ip_error').html("Enter the Destination IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address")
                    var $tds = $(this).find('td');
                    $($tds.eq(1)).addClass("invalid");
                    flag = false;

                }
            }
            if (arr_dest[1] == null)
            {

                if ((arr_dest[1] == "" || arr_dest[1] == null) && arr_dest[0].match(ipregx)) {
                    $('#firewall_form1 #destination_ip_error').html("")
                    $(".dip").removeClass("invalid");
                    flag = true;
                } else if (destinationip == "all" || destinationip == "any") {
                    $('#firewall_form1 #destination_ip_error').html("")
                    $(".dip").removeClass("invalid");
                    flag = true;
                } else {

                    var $tds = $(this).find('td');
                    $('#firewall_form1 #destination_ip_error').html("Enter the Destination IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address")
                    flag = false;

                }
            }


            if (action.match(serviceregex)) {

                $('#firewall_form1 #action_error').html("")
                $(".act").removeClass("invalid");
                flag = true;
            } else {

                var $tds = $(this).find('td');
                $('#firewall_form1 #action_error').html("Enter the Action in correct format")
                $($tds.eq(2)).addClass("invalid");
                flag = false;

            }

            if (service.match(serviceregex)) {

                $('#firewall_form1 #service_error').html("")
                $(".ser").removeClass("invalid");
                flag = true;
            } else {

                var $tds = $(this).find('td');
                $('#firewall_form1 #service_error').html("Enter the service in correct format")
                $($tds.eq(3)).addClass("invalid");
                flag = false;

            }
            if (service != "icmp")
            {
                //$($tds.eq(4)).prop("disabled", false);
                //if(ports.indexOf("-")>0)

                if (ports.indexOf("-") > -1) {
                    var portArray = ports.split('-');
                    var part1 = portArray[0];
                    var part2 = portArray[1]
                    if (part1.match(portregex) && part2.match(portregex)) {
                        $('#firewall_form1 #port_error').html("")
                        $(".port").removeClass("invalid");
                        flag = true;
                    } else if (ports == "all" || ports == "any") {
                        $('#firewall_form1 #port_error').html("")
                        $(".port").removeClass("invalid");
                        flag = true;
                    } else {
                        $('#firewall_form1 #port_error').html("Enter the Port in correct format")
                        var $tds = $(this).find('td');
                        $($tds.eq(4)).addClass("invalid");
                        flag = false;

                    }
                } else {
                    if (ports.match(portregex)) {
                        $('#firewall_form1 #port_error').html("")
                        $(".port").removeClass("invalid");
                        flag = true;
                    } else if (ports == "all" || ports == "any") {
                        $('#firewall_form1 #port_error').html("")
                        $(".port").removeClass("invalid");
                        flag = true;
                    } else {
                        $('#firewall_form1 #port_error').html("Enter the Port in correct format")
                        var $tds = $(this).find('td');
                        $($tds.eq(4)).addClass("invalid");
                        flag = false;

                    }
                }
            } else {
                flag = true;
                console.log("here@@@@@@@@@@@@@@@@@@@")
//                var $tds = $(this).find('td');
//                console.log($($tds.eq(4)))
//                $(".port").attr("disabled", true);
//                $($tds.eq(4)).attr("disabled", true);
//                $($tds.eq(4)).attr("disabled", true);
//                 $('.port').attr('contenteditable','false');

            }

            if ($.trim(timeperiod) !== '') {
                var $tds = $(this).find('td');
                $tds.removeAttr('placeholder');
                if (timeperiod.match(timeregex)) {
                    $('#firewall_form1 #time_error').html("")
                    $(".time").removeClass("invalid");
                    flag = true;
                } else {
                    var $tds = $(this).find('td');
                    $('#firewall_form1 #time_error').html("Enter the Time/Period [Numeric only], days should be greater than 1 less than 730)")
                    $($tds.eq(5)).addClass("invalid");
                    flag = false;

                }
            }
            if (flag)
            {
                $('.adderrorRow').addClass("d-none");
            } else {
                $('.adderrorRow').removeClass("d-none");
            }
        });

    });

    $(document).on("change", "td[contenteditable]", function (e) {
        var table = $("#example1 tbody");
        table.find('tr').each(function (i) {
            var $tds = $(this).find('td');
            let tdObject = $(this).find('td:eq(2)');
            let selectObject = tdObject.find(".ser option:selected"); //grab the <select> tag assuming that there will be only single select box within that <td> 
            let service = selectObject.val();
            console.log("on change service")
            if (service === "icmp")
            {
                console.log("in icmp")
                $(this).closest('tr').find('.port').attr('contenteditable', 'false');
                $(this).closest('tr').find('.port').text("");
            } else {
                $(this).closest('tr').find('.port').attr('contenteditable', 'true');
                // $('.port').attr('contenteditable', 'true');

            }
            console.log("on change" + service)
        });

    });

//    $(document).on("change", "td[contenteditable]", function (e) {
//        var table = $("#example1 tbody");
//        table.find('tr').each(function (i) {
//            var $tds = $(this).find('td');
//            let tdObject = $(this).find('td:eq(2)');
//            let selectObject = tdObject.find(".ser option:selected"); //grab the <select> tag assuming that there will be only single select box within that <td> 
//            let service = selectObject.val();
//            console.log("on change service")
//            if (service === "icmp")
//            {
//                console.log("in icmp")
//                //tdObject.attr("contenteditable",false)
//                $('.port').attr('contenteditable', 'false');
//            } else {
//                $('.port').attr('contenteditable', 'true');
//
//            }
//            console.log("on change" + service)
//        });
//
//    });
//    var table = "";
    $('#firewall_form1').submit(function (e) {
        e.preventDefault();

        if (flag)
        {
            jsonObj = [];
            var table = $("#example1 tbody");
            table.find('tr').each(function (i) {
                var $tds = $(this).find('td');
                let tdObject = $(this).find('td:eq(2)'); //locate the <td> holding select;
                let selectObject = tdObject.find(".ser option:selected"); //grab the <select> tag assuming that there will be only single select box within that <td> 
                let service = selectObject.val();
                let sourceip = $tds.eq(0).text();
                let destinationip = $tds.eq(1).text();
                let ports = $tds.eq(3).text();
                let tdObject1 = $(this).find('td:eq(4)'); //locate the <td> holding select;
                let selectObject1 = tdObject1.find(".act option:selected"); //grab the <select> tag assuming that there will be only single select box within that <td> 
                var action = selectObject1.val();
                let timeperiod = $tds.eq(5).text();
                item = {};
                item ["sourceip"] = sourceip.trim();
                item ["destinationip"] = destinationip.trim();
                item ["service"] = service.trim();
                item ["ports"] = ports.trim();
                item ["action"] = action.trim();
                item ["timeperiod"] = timeperiod.trim();
                jsonObj.push(item);
            });

            var purposeVal = $('#remarks').val();
            console.log("Purpose@@@@@@@@@@" + purposeVal)
            var dataObj = {
                "valueObj": jsonObj,
                "purpose":purposeVal
            };
            var data = JSON.stringify(dataObj);
            console.log("val object data" + data)
            $.ajax({
                url: "Firewall_tab1",
                data: data,
                dataType: 'json',
                contentType: 'application/json',
                type: 'POST',
                async: true,
                success: function (data)
                {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    var error_flag = true;
                   
                    if (jsonvalue.error.source_ip_error !== null && jsonvalue.error.source_ip_error !== "" && jsonvalue.error.source_ip_error !== undefined)
                    {
                        $('#firewall_form1 #source_ip_error').html(jsonvalue.error.source_ip_error)
                        $('#firewall_form1 #source_ip_error').focus();
                        error_flag = false;
                        $('#firewall_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        $('#source_ip_error').html("")

                    }
                    
                    if (jsonvalue.error.destination_ip_error !== null && jsonvalue.error.destination_ip_error !== "" && jsonvalue.error.destination_ip_error !== undefined)
                    {
                        $('#firewall_form1 #destination_ip_error').html(jsonvalue.error.destination_ip_error)
                        $('#firewall_form1 #destination_ip_error').focus();
                        error_flag = false;
                        $('#firewall_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        $('#destination_ip_error').html("")

                    }
                    if (jsonvalue.error.service_error !== null && jsonvalue.error.service_error !== "" && jsonvalue.error.service_error !== undefined)
                    {
                        $('#firewall_form1 #service_ip_error').html(jsonvalue.error.service_error)
                        $('#firewall_form1 #service_ip_error').focus();
                        error_flag = false;
                        $('#firewall_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        $('#service_ip_error').html("")

                    }
                    if (jsonvalue.error.ports_error !== null && jsonvalue.error.ports_error !== "" && jsonvalue.error.ports_error !== undefined)
                    {
                        $('#firewall_form1 #port_error').html(jsonvalue.error.ports_error)
                        $('#firewall_form1 #port_error').focus();
                        error_flag = false;
                        $('#firewall_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        $('#port_error').html("")

                    }
                    if (jsonvalue.error.action_error !== null && jsonvalue.error.action_error !== "" && jsonvalue.error.action_error !== undefined)
                    {
                        $('#firewall_form1 #action_error').html(jsonvalue.error.action_error)
                        $('#firewall_form1 #action_error').focus();
                        error_flag = false;
                        $('#firewall_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        $('#action_error').html("")

                    }
                    if (jsonvalue.error.timeperiod_error !== null && jsonvalue.error.timeperiod_error !== "" && jsonvalue.error.timeperiod_error !== undefined)
                    {
                        $('#firewall_form1 #time_error').html(jsonvalue.error.timeperiod_error)
                        $('#firewall_form1 #time_error').focus();
                        error_flag = false;
                        $('#firewall_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        $('#time_error').html("")

                    }
                    if (jsonvalue.error.purpose_error !== null && jsonvalue.error.purpose_error !== "" && jsonvalue.error.purpose_error !== undefined)
                    {
                        $('#firewall_form1 #purpose_error').html(jsonvalue.error.purpose_error)
                        $('#firewall_form1 #purpose_error').focus();
                        error_flag = false;
                        $('#firewall_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        $('#firewall_form1 #purpose_error').html("")

                    }
                    

                    console.log("error_flag###########" + error_flag)
                    if (!error_flag)
                    {

                        $('.adderrorRow').removeClass("d-none");

                    } else {
                        $('.adderrorRow').addClass("d-none");
                        if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                            $('#firewall_form2 #central_div').show();
                            $('#firewall_form2 #state_div').hide();
                            $('#firewall_form2 #other_div').hide();
                            $('#firewall_form2 #other_text_div').addClass("display-hide");
                            var select = $('#firewall_form2 #min');
                            select.find('option').remove();
                            $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                            var select = $('#firewall_form2 #dept');
                            select.find('option').remove();
                            $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                            if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                                $('#firewall_form2 #other_text_div').removeClass("display-hide");
                                $('#firewall_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                            }


                        } else if (jsonvalue.profile_values.user_employment === 'State') {
                            $('#firewall_form2 #central_div').hide();
                            $('#firewall_form2 #state_div').show();
                            $('#firewall_form2 #other_div').hide();
                            $('#firewall_form2 #other_text_div').addClass("display-hide");
                            var select = $('#firewall_form2 #stateCode');
                            select.find('option').remove();
                            $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                            var select = $('#firewall_form2 #Smi');
                            select.find('option').remove();
                            $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                            if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                                $('#firewall_form2 #other_text_div').removeClass("display-hide");
                                $('#firewall_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                            }

                        } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn") {
                            $('#firewall_form2 #central_div').hide();
                            $('#firewall_form2 #state_div').hide();
                            $('#firewall_form2 #other_div').show();
                            $('#firewall_form2 #other_text_div').addClass("display-hide");
                            var select = $('#firewall_form2 #Org');
                            select.find('option').remove();
                            $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);

                            if (jsonvalue.profile_values.Org === 'other') {
                                $('#firewall_form2 #other_text_div').removeClass("display-hide");
                                $('#firewall_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                            }

                        } else {
                            $('#firewall_form2 #central_div').hide();
                            $('#firewall_form2 #state_div').hide();
                            $('#firewall_form2 #other_div').hide();
                        }

                        $('#firewall_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                        $('#firewall_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                        $('#firewall_form2 #user_name').val(jsonvalue.profile_values.cn);
                        $('#firewall_form2 #user_design').val(jsonvalue.profile_values.desig);
                        $('#firewall_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                        $('#firewall_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                        $('#firewall_form2 #user_state').val(jsonvalue.profile_values.state);
                        $('#firewall_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                        $('#firewall_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                        $('#firewall_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
                        $('#firewall_form2 #user_mobile').val(jsonvalue.profile_values.mobile);
                        $('#firewall_form2 #user_email').val(jsonvalue.profile_values.email);
                        $('#firewall_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                        $('#firewall_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                        $('#firewall_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                        $('#firewall_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                        $('#firewall_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                        $('#firewall_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                        $('#firewall_form2 #remarks').val(jsonvalue.purpose);
                        var myarray = jsonvalue.valueObj;
                        $('#firewall_form2 #add_sourceip').html('');
                        $('#firewall_form2 #add_designationip').html('');
                        $('#firewall_form2 #add_service').html('');
                        $('#firewall_form2 #add_port').html('');
                        $('#firewall_form2 #add_action').html('');
                        $('#firewall_form2 #add_time').html('');
                        for (var i = 0; i < myarray.length; i++)
                        {
                            var sourceip = myarray[i]["sourceip"].trim();

                            if (i === 0) {
                                $('#firewall_form2 #firewall_sourceip_1').val(sourceip);
                            } else {
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
                                var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="firewall_sourceip[]" value="' + sourceip + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
                                $('#firewall_form2 #add_sourceip').append(elmnt);
                            }
                        }

                        for (var i = 0; i < myarray.length; i++)
                        {
                            var destinationip = myarray[i]["destinationip"].trim();

                            if (i === 0) {
                                $('#firewall_form2 #firewall_destinationip_1').val(destinationip);
                            } else {
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
                                var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="firewall_destinationip[]" value="' + destinationip + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
                                $('#firewall_form2 #add_designationip').append(elmnt);
                            }
                        }

                        for (var i = 0; i < myarray.length; i++)
                        {
                            var service = myarray[i]["service"].trim();

                            if (i === 0) {
                                $('#firewall_form2 #firewall_service_1').val(service);
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
                                    $('.class_name_port').prop('disabled', 'true');
                                } else {
                                    $('.class_name_port').prop('disabled', 'false');

                                }

                                var elmnt = '<div class="con"><select class="form-control ser" id="" name="firewall_service[]" style="margin: 20px 0px 20px 15px; width: 90%;">' +
                                        "<option value=''>-SELECT-</option><option value='tcp' " + tcp_selected + ">TCP</option>" +
                                        "<option value='udp' " + udp_selected + ">UDP</option>" +
                                        "<option value='icmp' " + icmp_selected + ">ICMP</option>" +
                                        "</select></div>";
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
                                // var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="firewall_service[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
                                $('#firewall_form2 #add_service').append(elmnt);
                            }
                        }
                        for (var i = 0; i < myarray.length; i++)
                        {
                            var port = myarray[i]["ports"].trim();

                            if (i === 0) {
                                $('#firewall_form2 #firewall_port_1').val(port);
                            } else {
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
                                var elmnt = '<div class="con"><input class="form-control dns_name class_name_port' + i + '"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="firewall_port[]" value="' + port + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
                                $('#firewall_form2 #add_port').append(elmnt);
                            }
                        }
                        for (var i = 0; i < myarray.length; i++)
                        {
                            var action = myarray[i]["action"].trim();
                            var permit_selected = "", deny_selected = "";
                            if (action === "permit")
                            {
                                permit_selected = "selected";

                            }
                            if (action === "deny")
                            {
                                deny_selected = "selected";

                            }


                            if (i === 0) {
                                $('#firewall_form2 #firewall_action_1').val(action);
                            } else {

                                var elmnt = '<div class="con"><select class="form-control ser" id="" name="firewall_action[]" style="margin: 20px 0px 20px 15px; width: 90%;">' +
                                        "<option value=''>-SELECT-</option><option value='permit' " + permit_selected + ">Permit</option>" +
                                        "<option value='deny' " + deny_selected + ">Deny</option>" +
                                        "</select></div>";
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
                                // var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="firewall_service[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
                                $('#firewall_form2 #add_action').append(elmnt);

                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
//                                var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="firewall_action[]" value="' + action + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
//                                $('#firewall_form2 #add_action').append(elmnt);
                            }
                        }
                        for (var i = 0; i < myarray.length; i++)
                        {
                            var timeperiod = myarray[i]["timeperiod"].trim();

                            if (i === 0) {
                                if (timeperiod !== "")
                                {

                                    $('#firewall_form2 #firewall_timePeriod_1').val(timeperiod);
                                } else {

                                    $('#firewall_form2 #firewall_timePeriod_1').val("Default");
                                }
                            } else {

                                if (timeperiod !== "")
                                {
                                    var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="firewall_time[]" value="' + timeperiod + '" maxlength="100" style="margin: 20px 0px 15px 0px; width:85%"></div>';
                                    $('#firewall_form2 #add_time').append(elmnt);
                                } else {

                                    var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="firewall_time[]" value="Default" maxlength="100" style="margin: 20px 0px 15px 0px; width:85%"></div>';
                                    $('#firewall_form2 #add_time').append(elmnt);
                                }
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';


                            }
                        }
                        //$('#firewall_form2 #firewall_timeperiod_1').val("1234");
                        $('.edit').removeClass('display-hide');
                        $('#centralutm_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                        $("#firewall_form2 :button[type='button']").removeAttr('disabled');
                        $("#firewall_form2 #tnc").removeAttr('disabled');
                        $('#large').modal({backdrop: 'static', keyboard: false});
                    }
                }, error: function ()
                {
                    $('#tab1').show();
                }
            });
        } else {

            $('#tab1').show();
        }

    });
//
    $('#firewall_form2').submit(function (e) {
        e.preventDefault();
        $("#firewall_form2 :disabled").removeAttr('disabled');
        $('#centralutm_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#centralutm_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#centralutm_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#centralutm_preview_tab #user_email').removeAttr('disabled'); // 20th march
        $('#centralutm_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var data = JSON.stringify($('#firewall_form2').serializeObject());
        var firewall_sourceip = $("#firewall_form2 input[name='firewall_sourceip[]']").val();
        if (firewall_sourceip !== "")
        {
            firewall_sourceip = $("#firewall_form2 input[name='firewall_sourceip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        } else {
            firewall_sourceip = $("#firewall_form2 input[name='firewall_sourceip[]']").val();
        }
        var firewall_destinationip = $("#firewall_form2 input[name='firewall_destinationip[]']").val();
        if (firewall_destinationip !== "")
        {
            firewall_destinationip = $("#firewall_form2 input[name='firewall_destinationip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var firewall_service = $("#firewall_form2 select[name='firewall_service[]']").val();
        if (firewall_service !== "")
        {
            firewall_service = $("#firewall_form2 select[name='firewall_service[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        console.log("service:::::::::" + firewall_service)
        var firewall_port = $("#firewall_form2 input[name='firewall_port[]']").val();
        if (firewall_port !== "")
        {
            firewall_port = $("#firewall_form2 input[name='firewall_port[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var firewall_action = $("#firewall_form2 input[name='firewall_port[]']").val();
        if (firewall_action !== "")
        {
            firewall_action = $("#firewall_form2 select[name='firewall_action[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }


        var firewall_time = $("#firewall_form2 input[name='firewall_time[]']").val();
        if (firewall_time !== "")
        {
            firewall_time = $("#firewall_form2 input[name='firewall_time[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

//        var cert = $('#dns_form2 #cert').val();
//
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        var submit = $('#firewall_form2 #confirm').val();

//        $('#dns_form2 .edit').removeClass('display-hide');

        $.ajax({
            type: 'POST',
            url: "Firewall_tab2",
            data: {data: data, firewall_sourceip: firewall_sourceip, firewall_destinationip: firewall_destinationip, firewall_service: firewall_service, firewall_port: firewall_port, firewall_action: firewall_action, firewall_time: firewall_time, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.source_ip_error !== null && jsonvalue.error.source_ip_error !== "" && jsonvalue.error.source_ip_error !== undefined)
                {
                    $('#firewall_form2 #source_ip_error').html(jsonvalue.error.source_ip_error)
                    $('#firewall_form2 #source_ip_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #source_ip_error').html("")

                }
                if (jsonvalue.error.destination_ip_error !== null && jsonvalue.error.destination_ip_error !== "" && jsonvalue.error.destination_ip_error !== undefined)
                {
                    $('#firewall_form2 #destination_ip_error').html(jsonvalue.error.destination_ip_error)
                    $('#firewall_form2 #destination_ip_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #destination_ip_error').html("")

                }
                if (jsonvalue.error.service_error !== null && jsonvalue.error.service_error !== "" && jsonvalue.error.service_error !== undefined)
                {
                    $('#firewall_form2 #service_ip_error').html(jsonvalue.error.service_error)
                    $('#firewall_form2 #service_ip_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #service_ip_error').html("")

                }
                if (jsonvalue.error.ports_error !== null && jsonvalue.error.ports_error !== "" && jsonvalue.error.ports_error !== undefined)
                {
                    $('#firewall_form2 #port_error').html(jsonvalue.error.ports_error)
                    $('#firewall_form2 #port_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #port_error').html("")

                }

                if (jsonvalue.error.action_error !== null && jsonvalue.error.action_error !== "" && jsonvalue.error.action_error !== undefined)
                {
                    $('#firewall_form2 #action_error').html(jsonvalue.error.action_error)
                    $('#firewall_form2 #action_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #action_error').html("")

                }
                if (jsonvalue.error.timeperiod_error !== null && jsonvalue.error.timeperiod_error !== "" && jsonvalue.error.timeperiod_error !== undefined)
                {
                    $('#firewall_form2 #time_error').html(jsonvalue.error.timeperiod_error)
                    $('#firewall_form2 #time_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #time_error').html("")

                }

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#firewall_form2 #useremployment_error').focus();
                    $('#firewall_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#firewall_form2 #minerror').focus();
                    $('#firewall_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#firewall_form2 #deperror').focus();
                    $('#firewall_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #deperror').html("");
                }

                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#firewall_form2 #other_dept').focus();
                    $('#firewall_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#firewall_form2 #smierror').focus();
                    $('#firewall_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#firewall_form2 #state_error').focus();
                    $('#firewall_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#firewall_form2 #org_error').focus();
                    $('#firewall_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#firewall_form2 #ca_design').focus();
                    $('#firewall_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#firewall_form2 #hod_name').focus();
                    $('#firewall_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#firewall_form2 #hod_mobile').focus();
                    $('#firewall_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#firewall_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#firewall_form2 #hod_tel').focus();
                    $('#firewall_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #hodtel_error').html("");
                }
                 if (jsonvalue.error.purpose_error !== null && jsonvalue.error.purpose_error !== "" && jsonvalue.error.purpose_error !== undefined)
                    {
                        $('#firewall_form2 #purpose_error').html(jsonvalue.error.purpose_error)
                        $('#firewall_form2 #purpose_error').focus();
                        error_flag = false;
                        $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        $('firewall_form2 #purpose_error').html("")

                    }
                if (error_flag) {
                    // show modal
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'centralutm'},
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

    $('#firewall_form2 #confirm').click(function (e) {

        e.preventDefault();
        $("#firewall_form2 :disabled").removeAttr('disabled');
        $('#centralutm_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#centralutm_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#centralutm_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#centralutm_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#firewall_form2').serializeObject());
        $('#centralutm_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 19thjan18



        $('#firewall_form2 #user_email').prop('disabled', 'true'); // 20th march

        var firewall_sourceip = $("#firewall_form2 input[name='firewall_sourceip[]']").val();

        if (firewall_sourceip !== "")
        {
            firewall_sourceip = $("#firewall_form2 input[name='firewall_sourceip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        } else {
            firewall_sourceip = $("#firewall_form2 input[name='firewall_sourceip[]']").val();
        }

        var firewall_destinationip = $("#firewall_form2 input[name='firewall_destinationip[]']").val();
        if (firewall_destinationip !== "")
        {
            firewall_destinationip = $("#firewall_form2 input[name='firewall_destinationip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var firewall_service = $("#firewall_form2 select[name='firewall_service[]']").val();
        if (firewall_service !== "")
        {
            firewall_service = $("#firewall_form2 select[name='firewall_service[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        console.log("service:::::::::" + firewall_service)
        var firewall_port = $("#firewall_form2 input[name='firewall_port[]']").val();
        if (firewall_port !== "")
        {
            firewall_port = $("#firewall_form2 input[name='firewall_port[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var firewall_action = $("#firewall_form2 input[name='firewall_port[]']").val();
        if (firewall_action !== "")
        {
            firewall_action = $("#firewall_form2 select[name='firewall_action[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }


        var firewall_time = $("#firewall_form2 input[name='firewall_time[]']").val();
        if (firewall_time !== "")
        {
            firewall_time = $("#firewall_form2 input[name='firewall_time[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        var submit = $('#firewall_form2 #confirm').val();

        $('#firewall_form2 .edit').removeClass('display-hide');
        $.ajax({
            type: "POST",
            url: "Firewall_tab2",
            data: {data: data, firewall_sourceip: firewall_sourceip, firewall_destinationip: firewall_destinationip, firewall_service: firewall_service, firewall_port: firewall_port, firewall_action: firewall_action, firewall_time: firewall_time, action_type: "validate", CSRFRandom: CSRFRandom, request_type: submit},
            datatype: JSON,
            success: function (data)
            {

                // resetCSRFRandom();// line added by pr on 10thjan18
                var error_flag = true;
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                if (jsonvalue.error.source_ip_error !== null && jsonvalue.error.source_ip_error !== "" && jsonvalue.error.source_ip_error !== undefined)
                {
                    $('#firewall_form2 #source_ip_error').html(jsonvalue.error.source_ip_error)
                    $('#firewall_form2 #source_ip_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #source_ip_error').html("")

                }
                if (jsonvalue.error.destination_ip_error !== null && jsonvalue.error.destination_ip_error !== "" && jsonvalue.error.destination_ip_error !== undefined)
                {
                    $('#firewall_form2 #destination_ip_error').html(jsonvalue.error.destination_ip_error)
                    $('#firewall_form2 #destination_ip_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #destination_ip_error').html("")

                }
                if (jsonvalue.error.service_error !== null && jsonvalue.error.service_error !== "" && jsonvalue.error.service_error !== undefined)
                {
                    $('#firewall_form2 #service_ip_error').html(jsonvalue.error.service_error)
                    $('#firewall_form2 #service_ip_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #service_ip_error').html("")

                }
                if (jsonvalue.error.ports_error !== null && jsonvalue.error.ports_error !== "" && jsonvalue.error.ports_error !== undefined)
                {
                    $('#firewall_form2 #port_error').html(jsonvalue.error.ports_error)
                    $('#firewall_form2 #port_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #port_error').html("")

                }

                if (jsonvalue.error.action_error !== null && jsonvalue.error.action_error !== "" && jsonvalue.error.action_error !== undefined)
                {
                    $('#firewall_form2 #action_error').html(jsonvalue.error.action_error)
                    $('#firewall_form2 #action_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #action_error').html("")

                }
                if (jsonvalue.error.timeperiod_error !== null && jsonvalue.error.timeperiod_error !== "" && jsonvalue.error.timeperiod_error !== undefined)
                {
                    $('#firewall_form2 #time_error').html(jsonvalue.error.timeperiod_error)
                    $('#firewall_form2 #time_error').focus();
                    error_flag = false;
                    $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#firewall_form2 #imgtxt').val("");
                } else {
                    $('#firewall_form2 #time_error').html("")

                }

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#firewall_form2 #useremployment_error').focus();
                    $('#firewall_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#firewall_form2 #minerror').focus();
                    $('#firewall_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#firewall_form2 #deperror').focus();
                    $('#firewall_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #deperror').html("");
                }

                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#firewall_form2 #other_dept').focus();
                    $('#firewall_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#firewall_form2 #smierror').focus();
                    $('#firewall_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#firewall_form2 #state_error').focus();
                    $('#firewall_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#firewall_form2 #org_error').focus();
                    $('#firewall_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#firewall_form2 #ca_design').focus();
                    $('#firewall_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#firewall_form2 #hod_name').focus();
                    $('#firewall_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#firewall_form2 #hod_mobile').focus();
                    $('#firewall_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#firewall_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#firewall_form2 #hod_tel').focus();
                    $('#firewall_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#firewall_form2 #hodtel_error').html("");
                }   
                  if (jsonvalue.error.purpose_error !== null && jsonvalue.error.purpose_error !== "" && jsonvalue.error.purpose_error !== undefined)
                    {
                        $('#firewall_form2 #purpose_error').html(jsonvalue.error.purpose_error)
                        $('#firewall_form2 #purpose_error').focus();
                        error_flag = false;
                        $('#firewall_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        $('firewall_form2 #purpose_error').html("")

                    }

                if (!error_flag)
                {
                    $("#firewall_form2 :disabled").removeAttr('disabled');
                    $('#firewall_form2 #centralutm_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#firewall_form2 #centralutm_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#firewall_form2 #centralutm_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march


                } else {

//                if (!error_flag) {
//                    alert("inside if")
//
//                    $("#firewall_form2 :disabled").removeAttr('disabled');
//                    $('#firewall_form2 #centralutm_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
//                    $('#firewall_form2 #centralutm_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
//                    $('#firewall_form2 #centralutm_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
//                } else {

                    if ($('#firewall_form2 #tnc').is(":checked"))
                    {


                        $('#firewall_form2 #tnc_error').html("");
                        $('#centralutm_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                        $('#firewall_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                        $('#firewall_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                        $('#firewall_form_confirm #fill_hod_mobile').html(jsonvalue.form_details.hod_mobile);

                    } else {
                        $('#firewall_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        $('#firewall_form2 #centralutm_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#firewall_form2 #centralutm_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //$('#firewall_form2 #centralutm_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        $("#firewall_form2 #tnc").removeAttr('disabled');
                    }
                }

            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });

    $('#firewall_form_confirm #confirmYes').click(function () {

        $('#firewall_form2').submit();
        $('#stack3').modal('toggle');
    });

    $('#firewall_form2 .edit').click(function () {

        var firewall_service = $("#firewall_form2 select[name='firewall_service[]']").val();
        if (firewall_service !== "")
        {
            firewall_service = $("#firewall_form2 select[name='firewall_service[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        var res = firewall_service.split(";");

        var employment = $('#centralutm_preview_tab #user_employment').val();
        var min = $('#centralutm_preview_tab #min').val();
        var dept = $('#centralutm_preview_tab #dept').val();
        var statecode = $('#centralutm_preview_tab #stateCode').val();
        var Smi = $('#centralutm_preview_tab #Smi').val();
        var Org = $('#centralutm_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#centralutm_preview_tab #min');
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
                var select = $('#centralutm_preview_tab #dept');
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
                $('#centralutm_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#centralutm_preview_tab #stateCode');
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
                var select = $('#centralutm_preview_tab #Smi');
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
                $('#centralutm_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#centralutm_preview_tab #Org');
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
                $('#centralutm_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#centralutm_preview_tab #central_div').hide();
            $('#centralutm_preview_tab #state_div').hide();
            $('#centralutm_preview_tab #other_div').hide();
        }



        $('#firewall_form2').find('input, textarea, button, select').prop('disabled', 'false');
        $('#firewall_form2').find('input, textarea, button, select').removeAttr('disabled');
        $('#firewall_form2 #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#firewall_form2 #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        $('#firewall_form2 #REditPreview #hod_email').removeAttr('disabled');
        for (var i = 0; i < res.length; i++)
        {
            console.log("length:::" + res.length)
            if (i == 0)
            {
                console.log("inisde if length 1")
                if (res[i] === "icmp")
                {
                    console.log("inisde if length 1 and service is icmp")
                    $(".class_name_port").prop("disabled", true);
                } else {
                    console.log("inisde if length 1 and service is not icmp")
                    $(".class_name_port").prop("disabled", false);
                }
            } else {
                console.log("inside else")
                if (res[i] === "icmp")
                {
                    console.log("inside else icmp")
                    $(".class_name_port" + i).prop("disabled", true);
                } else {
                    console.log("inside else not icmp")
                    $(".class_name_port" + i).prop("disabled", false);
                }
            }

        }
        $(this).addClass('display-hide');




        //$(this).hide();
    });

    $(document).on('click', '#centralutm_preview .edit', function () {

        // $('#centralutm_preview .edit').click(function () {

        var employment = $('#centralutm_preview_tab #user_employment').val();
        var min = $('#centralutm_preview_tab #min').val();
        var dept = $('#centralutm_preview_tab #dept').val();
        var statecode = $('#centralutm_preview_tab #stateCode').val();
        var Smi = $('#centralutm_preview_tab #Smi').val();
        var Org = $('#centralutm_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#centralutm_preview_tab #min');
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
                var select = $('#centralutm_preview_tab #dept');
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
                $('#centralutm_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#centralutm_preview_tab #stateCode');
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
                var select = $('#centralutm_preview_tab #Smi');
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
                $('#centralutm_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#centralutm_preview_tab #Org');
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
                $('#centralutm_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#centralutm_preview_tab #central_div').hide();
            $('#centralutm_preview_tab #state_div').hide();
            $('#centralutm_preview_tab #other_div').hide();
        }




        $('#centralutm_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#centralutm_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#centralutm_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#centralutm_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#centralutm_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        //console.log($(this))
        $(this).addClass('display-hide');


        if ($("#comingFrom").val("admin"))
        {
            $("#centralutm_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#centralutm_preview .save-changes").html("Update");
        }


        //$(this).hide();
    });

    $(document).on('click', '#centralutm_preview #confirm', function () {
        //$('#centralutm_preview #confirm').click(function (e) {

        $('#centralutm_preview').submit();

    });

    $('#centralutm_preview').submit(function (e) {


        e.preventDefault();
        $("#centralutm_preview :disabled").removeAttr('disabled');
        $('#centralutm_preview #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#centralutm_preview #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#centralutm_preview #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#centralutm_preview #user_email').removeAttr('disabled'); // 20th march
        $('#centralutm_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var data = JSON.stringify($('#centralutm_preview').serializeObject());
        var firewall_sourceip = $("#centralutm_preview input[name='firewall_sourceip[]']").val();
        if (firewall_sourceip !== "")
        {
            firewall_sourceip = $("#centralutm_preview input[name='firewall_sourceip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        } else {
            firewall_sourceip = $("#centralutm_preview input[name='firewall_sourceip[]']").val();
        }
        console.log()
        var firewall_destinationip = $("#centralutm_preview input[name='firewall_destinationip[]']").val();
        if (firewall_destinationip !== "")
        {
            firewall_destinationip = $("#centralutm_preview input[name='firewall_destinationip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var firewall_service = $("#centralutm_preview input[name='firewall_service[]']").val();
        if (firewall_service !== "")
        {
            firewall_service = $("#centralutm_preview input[name='firewall_service[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var firewall_port = $("#centralutm_preview input[name='firewall_port[]']").val();
        if (firewall_port !== "")
        {
            firewall_port = $("#centralutm_preview input[name='firewall_port[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var firewall_action = $("#centralutm_preview input[name='firewall_port[]']").val();
        if (firewall_action !== "")
        {
            firewall_action = $("#centralutm_preview input[name='firewall_action[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }


        var firewall_time = $("#centralutm_preview input[name='firewall_time[]']").val();
        if (firewall_time !== "")
        {
            firewall_time = $("#centralutm_preview input[name='firewall_time[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        console.log("firewall_sourceip" + firewall_sourceip)
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: 'POST',
            url: "Firewall_tab2",
            data: {data: data, firewall_sourceip: firewall_sourceip, firewall_destinationip: firewall_destinationip, firewall_service: firewall_service, firewall_port: firewall_port, firewall_action: firewall_action, firewall_time: firewall_time, action_type: "update", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                //resetCSRFRandom();// line added by pr on 10thjan18
                var error_flag = true;
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                if (jsonvalue.error.source_ip_error !== null && jsonvalue.error.source_ip_error !== "" && jsonvalue.error.source_ip_error !== undefined)
                {
                    $('#centralutm_preview #source_ip_error').html(jsonvalue.error.source_ip_error)
                    $('#centralutm_preview #source_ip_error').focus();
                    error_flag = false;
                    $('#centralutm_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#centralutm_preview #imgtxt').val("");
                } else {
                    $('#centralutm_preview #source_ip_error').html("")

                }
                if (jsonvalue.error.destination_ip_error !== null && jsonvalue.error.destination_ip_error !== "" && jsonvalue.error.destination_ip_error !== undefined)
                {
                    $('#centralutm_preview #destination_ip_error').html(jsonvalue.error.destination_ip_error)
                    $('#centralutm_preview #destination_ip_error').focus();
                    error_flag = false;
                    $('#centralutm_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#centralutm_preview #imgtxt').val("");
                } else {
                    $('#centralutm_preview #destination_ip_error').html("")

                }
                if (jsonvalue.error.service_error !== null && jsonvalue.error.service_error !== "" && jsonvalue.error.service_error !== undefined)
                {
                    $('#centralutm_preview #service_ip_error').html(jsonvalue.error.service_error)
                    $('#centralutm_preview #service_ip_error').focus();
                    error_flag = false;
                    $('#centralutm_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#centralutm_preview #imgtxt').val("");
                } else {
                    $('#centralutm_preview #service_ip_error').html("")

                }
                if (jsonvalue.error.ports_error !== null && jsonvalue.error.ports_error !== "" && jsonvalue.error.ports_error !== undefined)
                {
                    $('#centralutm_preview #port_error').html(jsonvalue.error.ports_error)
                    $('#centralutm_preview #port_error').focus();
                    error_flag = false;
                    $('#centralutm_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#centralutm_preview #imgtxt').val("");
                } else {
                    $('#centralutm_preview #port_error').html("")

                }
                if (jsonvalue.error.action_error !== null && jsonvalue.error.action_error !== "" && jsonvalue.error.action_error !== undefined)
                {
                    $('#centralutm_preview #action_error').html(jsonvalue.error.action_error)
                    $('#centralutm_preview #action_error').focus();
                    error_flag = false;
                    $('#centralutm_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#centralutm_preview #imgtxt').val("");
                } else {
                    $('#centralutm_preview #action_error').html("")

                }
                if (jsonvalue.error.timeperiod_error !== null && jsonvalue.error.timeperiod_error !== "" && jsonvalue.error.timeperiod_error !== undefined)
                {
                    $('#centralutm_preview #time_error').html(jsonvalue.error.timeperiod_error)
                    $('#centralutm_preview #time_error').focus();
                    error_flag = false;
                    $('#centralutm_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#centralutm_preview #imgtxt').val("");
                } else {
                    $('#centralutm_preview #time_error').html("")

                }

                if (!error_flag)
                {

                    $("#centralutm_preview :disabled").removeAttr('disabled');
                    $('#centralutm_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#centralutm_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {

                    $('#centralutm_preview_form').modal('toggle');
                }


            }, error: function ()
            {
                console.log('error');
            }
        });
    });



});

