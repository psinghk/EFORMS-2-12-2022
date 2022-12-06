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
            
            let selectObject2 = tdObject.find(".tym option:selected");
             let timeperiod = selectObject2.val();
            //let timeperiod = $tds.eq(5).text();
            
            var ipregx = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
            var serviceregex = /^[a-zA-Z#0-9\\,.\\_\\-\\/\\(\\)]{2,100}$/;
            var portregex = /^[0-9]{1,10}$/;
           // var timeregex = /^[0-9]{0,3}$/
            var timeregex = "";
            var $tds1 = $(this).find('td');
            let row1 = $tds1.eq(0);
            let row5 = $tds1.eq(5);
            row5.attr('placeholder', "Default")
            if (arr_source[1] != null && sourceip.indexOf('-') > -1 || sourceip.indexOf('/') > -1)
            {
                if ((arr_source[1].match(ipregx)) && (arr_source[0].match(ipregx) && (sourceip.indexOf('/') == -1))) {
                    $('#wifiport_form1 #source_ip_error').html("")
                    $(".sip").removeClass("invalid");
                    flag = true;
                } else if ((arr_source[0].match(ipregx)) && (arr_source[1].match(portregex) && (sourceip.indexOf('/') == -1))) {
                    $('#wifiport_form1 #source_ip_error').html("")
                    $(".sip").removeClass("invalid");
                    flag = true;
                } else if ((arr_source[0].match(ipregx)) && ((arr_source[1] <= 32)) && (sourceip.indexOf('/') > -1)) {
                    $('#wifiport_form1 #source_ip_error').html("")
                    $(".sip").removeClass("invalid");
                    flag = true;
                } else {
                    $('#wifiport_form1 #source_ip_error').html("Enter the Source IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address")
                    var $tds = $(this).find('td');
                    $($tds.eq(0)).addClass("invalid");
                    flag = false;

                }
            }
            if (arr_source[1] == null)
            {
                if ((arr_source[1] == "" || arr_source[1] == null || arr_source[1] == undefined) && arr_source[0].match(ipregx)) {
                    $('#wifiport_form1 #source_ip_error').html("")
                    $(".sip").removeClass("invalid");
                    flag = true;
                } else if (sourceip == "all" || sourceip == "any") {
                    $('#wifiport_form1 #source_ip_error').html("")
                    $(".sip").removeClass("invalid");
                    flag = true;
                } else {
                    var $tds = $(this).find('td');
                    $('#wifiport_form1 #source_ip_error').html("Enter the Source IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address")
                    $($tds.eq(0)).addClass("invalid");
                    flag = false;
                }
            }
            if (arr_dest[1] != null && ((destinationip.indexOf('-') > -1) || (destinationip.indexOf('/') > -1)))
            {
                if ((arr_dest[1].match(ipregx)) && (arr_dest[0].match(ipregx) && (destinationip.indexOf('/') == -1))) {
                    $('#wifiport_form1 #destination_ip_error').html("")
                    $(".dip").removeClass("invalid");
                    flag = true;
                } else if ((arr_dest[0].match(ipregx)) && (arr_dest[1].match(portregex) && (destinationip.indexOf('/') == -1))) {
                    $('#wifiport_form1 #destination_ip_error').html("")
                    $(".dip").removeClass("invalid");
                    flag = true;
                } else if ((arr_dest[0].match(ipregx)) && ((arr_dest[1] <= 32)) && (destinationip.indexOf('/') > -1)) {
                    $('#wifiport_form1 #destination_ip_error').html("")
                    $(".dip").removeClass("invalid");
                    flag = true;
                } else {
                    $('#wifiport_form1 #destination_ip_error').html("Enter the Destination IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address")
                    var $tds = $(this).find('td');
                    $($tds.eq(1)).addClass("invalid");
                    flag = false;

                }
            }
            if (arr_dest[1] == null)
            {

                if ((arr_dest[1] == "" || arr_dest[1] == null) && arr_dest[0].match(ipregx)) {
                    $('#wifiport_form1 #destination_ip_error').html("")
                    $(".dip").removeClass("invalid");
                    flag = true;
                } else if (destinationip == "all" || destinationip == "any") {
                    $('#wifiport_form1 #destination_ip_error').html("")
                    $(".dip").removeClass("invalid");
                    flag = true;
                } else {

                    var $tds = $(this).find('td');
                    $('#wifiport_form1 #destination_ip_error').html("Enter the Destination IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address")
                    flag = false;

                }
            }


            if (action.match(serviceregex)) {

                $('#wifiport_form1 #action_error').html("")
                $(".act").removeClass("invalid");
                flag = true;
            } else {

                var $tds = $(this).find('td');
                $('#wifiport_form1 #action_error').html("Enter the Action in correct format")
                $($tds.eq(2)).addClass("invalid");
                flag = false;

            }

            if (service.match(serviceregex)) {

                $('#wifiport_form1 #service_error').html("")
                $(".ser").removeClass("invalid");
                flag = true;
            } else {

                var $tds = $(this).find('td');
                $('#wifiport_form1 #service_error').html("Enter the service in correct format")
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
                        $('#wifiport_form1 #port_error').html("")
                        $(".port").removeClass("invalid");
                        flag = true;
                    } else if (ports == "all" || ports == "any") {
                        $('#wifiport_form1 #port_error').html("")
                        $(".port").removeClass("invalid");
                        flag = true;
                    } else {
                        $('#wifiport_form1 #port_error').html("Enter the Port in correct format")
                        var $tds = $(this).find('td');
                        $($tds.eq(4)).addClass("invalid");
                        flag = false;

                    }
                } else {
                    if (ports.match(portregex)) {
                        $('#wifiport_form1 #port_error').html("")
                        $(".port").removeClass("invalid");
                        flag = true;
                    } else if (ports == "all" || ports == "any") {
                        $('#wifiport_form1 #port_error').html("")
                        $(".port").removeClass("invalid");
                        flag = true;
                    } else {
                        $('#wifiport_form1 #port_error').html("Enter the Port in correct format")
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

//            if ($.trim(timeperiod) !== '') {
//                var $tds = $(this).find('td');
//                $tds.removeAttr('placeholder');
//                if (timeperiod.match(timeregex)) {
//                    $('#wifiport_form1 #time_error').html("")
//                    $(".time").removeClass("invalid");
//                    flag = true;
//                } else {
//                    var $tds = $(this).find('td');
//                    $('#wifiport_form1 #time_error').html("Enter the Time/Period [Numeric only], days should be greater than 1 less than 730)")
//                    $($tds.eq(5)).addClass("invalid");
//                    flag = false;
//
//                }
//            }

              if (timeperiod!=="") {

                $('#wifiport_form1 #time_error').html("")
                $(".tym").removeClass("invalid");
                flag = true;
            } else {

                var $tds = $(this).find('td');
                $('#wifiport_form1 #time_error').html("Enter the Time Period in correct format")
                $($tds.eq(3)).addClass("invalid");
                flag = false;

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
    $('#wifiport_form1').submit(function (e) {
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
                //let timeperiod = $tds.eq(5).text();
                let tdObject2 = $(this).find('td:eq(5)');
               let selectObject2 = tdObject2.find(".tym option:selected"); //grab the <select> tag assuming that there will be only single select box within that <td> 
                var timeperiod = selectObject2.val();
                
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
                url: "wifiport_tab1",
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
                    //alert(jsonvalue.error.source_ip_error)
                    if (jsonvalue.error.source_ip_error !== null && jsonvalue.error.source_ip_error !== "" && jsonvalue.error.source_ip_error !== undefined)
                    {
                       
                        $('#wifiport_form1 #source_ip_error').html(jsonvalue.error.source_ip_error)
                        $('#wifiport_form1 #source_ip_error').focus();
                        error_flag = false;
                        $('#wifiport_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                          
                        $('#source_ip_error').html("")

                    }
                    
                    if (jsonvalue.error.destination_ip_error !== null && jsonvalue.error.destination_ip_error !== "" && jsonvalue.error.destination_ip_error !== undefined)
                    {
                        
                        $('#wifiport_form1 #destination_ip_error').html(jsonvalue.error.destination_ip_error)
                        $('#wifiport_form1 #destination_ip_error').focus();
                        error_flag = false;
                        $('#wifiport_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        
                        $('#destination_ip_error').html("")

                    }
                    if (jsonvalue.error.service_error !== null && jsonvalue.error.service_error !== "" && jsonvalue.error.service_error !== undefined)
                    {
                        
                        $('#wifiport_form1 #service_ip_error').html(jsonvalue.error.service_error)
                        $('#wifiport_form1 #service_ip_error').focus();
                        error_flag = false;
                        $('#wifiport_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                       
                        $('#service_ip_error').html("")

                    }
                    if (jsonvalue.error.ports_error !== null && jsonvalue.error.ports_error !== "" && jsonvalue.error.ports_error !== undefined)
                    {
                        
                        $('#wifiport_form1 #port_error').html(jsonvalue.error.ports_error)
                        $('#wifiport_form1 #port_error').focus();
                        error_flag = false;
                        $('#wifiport_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                         
                        $('#port_error').html("")

                    }
                    if (jsonvalue.error.action_error !== null && jsonvalue.error.action_error !== "" && jsonvalue.error.action_error !== undefined)
                    {
                         
                        $('#wifiport_form1 #action_error').html(jsonvalue.error.action_error)
                        $('#wifiport_form1 #action_error').focus();
                        error_flag = false;
                        $('#wifiport_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                         
                        $('#action_error').html("")

                    }
                    if (jsonvalue.error.timeperiod_error !== null && jsonvalue.error.timeperiod_error !== "" && jsonvalue.error.timeperiod_error !== undefined)
                    {
                         
                        $('#wifiport_form1 #time_error').html(jsonvalue.error.timeperiod_error)
                        $('#wifiport_form1 #time_error').focus();
                        error_flag = false;
                        $('#wifiport_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        
                        $('#time_error').html("")

                    }
                    if (jsonvalue.error.purpose_error !== null && jsonvalue.error.purpose_error !== "" && jsonvalue.error.purpose_error !== undefined)
                    {
                         
                        $('#wifiport_form1 #purpose_error').html(jsonvalue.error.purpose_error)
                        $('#wifiport_form1 #purpose_error').focus();
                        error_flag = false;
                        $('#wifiport_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                         
                        $('#wifiport_form1 #purpose_error').html("")

                    }
                    
                    
                    console.log("error_flag###########" + error_flag)
                    if (!error_flag)
                    {

                        $('.adderrorRow').removeClass("d-none");

                    } else {
                        $('.adderrorRow').addClass("d-none");
                        if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                            $('#wifiport_form2 #central_div').show();
                            $('#wifiport_form2 #state_div').hide();
                            $('#wifiport_form2 #other_div').hide();
                            $('#wifiport_form2 #other_text_div').addClass("display-hide");
                            var select = $('#wifiport_form2 #min');
                            select.find('option').remove();
                            $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                            var select = $('#wifiport_form2 #dept');
                            select.find('option').remove();
                            $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                            if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                                $('#wifiport_form2 #other_text_div').removeClass("display-hide");
                                $('#wifiport_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                            }


                        } else if (jsonvalue.profile_values.user_employment === 'State') {
                            $('#wifiport_form2 #central_div').hide();
                            $('#wifiport_form2 #state_div').show();
                            $('#wifiport_form2 #other_div').hide();
                            $('#wifiport_form2 #other_text_div').addClass("display-hide");
                            var select = $('#wifiport_form2 #stateCode');
                            select.find('option').remove();
                            $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                            var select = $('#wifiport_form2 #Smi');
                            select.find('option').remove();
                            $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                            if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                                $('#wifiport_form2 #other_text_div').removeClass("display-hide");
                                $('#wifiport_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                            }

                        } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn") {
                            $('#wifiport_form2 #central_div').hide();
                            $('#wifiport_form2 #state_div').hide();
                            $('#wifiport_form2 #other_div').show();
                            $('#wifiport_form2 #other_text_div').addClass("display-hide");
                            var select = $('#wifiport_form2 #Org');
                            select.find('option').remove();
                            $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);

                            if (jsonvalue.profile_values.Org === 'other') {
                                $('#wifiport_form2 #other_text_div').removeClass("display-hide");
                                $('#wifiport_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                            }

                        } else {
                            $('#wifiport_form2 #central_div').hide();
                            $('#wifiport_form2 #state_div').hide();
                            $('#wifiport_form2 #other_div').hide();
                        }

                        $('#wifiport_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                        $('#wifiport_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                        $('#wifiport_form2 #user_name').val(jsonvalue.profile_values.cn);
                        $('#wifiport_form2 #user_design').val(jsonvalue.profile_values.desig);
                        $('#wifiport_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                        $('#wifiport_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                        $('#wifiport_form2 #user_state').val(jsonvalue.profile_values.state);
                        $('#wifiport_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                        $('#wifiport_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                        $('#wifiport_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
                        $('#wifiport_form2 #user_mobile').val(jsonvalue.profile_values.mobile);
                        $('#wifiport_form2 #user_email').val(jsonvalue.profile_values.email);
                        $('#wifiport_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                        $('#wifiport_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                        $('#wifiport_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                        $('#wifiport_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                        $('#wifiport_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                        $('#wifiport_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                        $('#wifiport_form2 #remarks').val(jsonvalue.purpose);
                        var myarray = jsonvalue.valueObj;
                        $('#wifiport_form2 #add_sourceip').html('');
                        $('#wifiport_form2 #add_designationip').html('');
                        $('#wifiport_form2 #add_service').html('');
                        $('#wifiport_form2 #add_port').html('');
                        $('#wifiport_form2 #add_action').html('');
                       
                        $('#wifiport_form2 #add_time').html('');
                        
                        for (var i = 0; i < myarray.length; i++)
                        {
                            var sourceip = myarray[i]["sourceip"].trim();

                            if (i === 0) {
                                $('#wifiport_form2 #wifiport_sourceip_1').val(sourceip);
                            } else {
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
                                var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="wifiport_sourceip[]" value="' + sourceip + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
                                $('#wifiport_form2 #add_sourceip').append(elmnt);
                            }
                        }

                        for (var i = 0; i < myarray.length; i++)
                        {
                            var destinationip = myarray[i]["destinationip"].trim();

                            if (i === 0) {
                                $('#wifiport_form2 #wifiport_destinationip_1').val(destinationip);
                            } else {
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
                                var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="wifiport_destinationip[]" value="' + destinationip + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
                                $('#wifiport_form2 #add_designationip').append(elmnt);
                            }
                        }

                        for (var i = 0; i < myarray.length; i++)
                        {
                            var service = myarray[i]["service"].trim();

                            if (i === 0) {
                                $('#wifiport_form2 #wifiport_service_1').val(service);
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

                                var elmnt = '<div class="con"><select class="form-control ser" id="" name="wifiport_service[]" style="margin: 20px 0px 20px 15px; width: 90%;">' +
                                        "<option value=''>-SELECT-</option><option value='tcp' " + tcp_selected + ">TCP</option>" +
                                        "<option value='udp' " + udp_selected + ">UDP</option>" +
                                        "<option value='icmp' " + icmp_selected + ">ICMP</option>" +
                                        "</select></div>";
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
                                // var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="wifiport_service[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
                                $('#wifiport_form2 #add_service').append(elmnt);
                            }
                        }
                        for (var i = 0; i < myarray.length; i++)
                        {
                            var port = myarray[i]["ports"].trim();

                            if (i === 0) {
                                $('#wifiport_form2 #wifiport_port_1').val(port);
                            } else {
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
                                var elmnt = '<div class="con"><input class="form-control dns_name class_name_port' + i + '"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="wifiport_port[]" value="' + port + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
                                $('#wifiport_form2 #add_port').append(elmnt);
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
                                $('#wifiport_form2 #wifiport_action_1').val(action);
                            } else {

                                var elmnt = '<div class="con"><select class="form-control ser" id="" name="wifiport_action[]" style="margin: 20px 0px 20px 15px; width: 90%;">' +
                                        "<option value=''>-SELECT-</option><option value='permit' " + permit_selected + ">Permit</option>" +
                                        "<option value='deny' " + deny_selected + ">Deny</option>" +
                                        "</select></div>";
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
                                // var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="wifiport_service[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
                                $('#wifiport_form2 #add_action').append(elmnt);

                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
//                                var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="wifiport_action[]" value="' + action + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
//                                $('#wifiport_form2 #add_action').append(elmnt);
                            }
                        }
//                        for (var i = 0; i < myarray.length; i++)
//                        {
//                            var timeperiod = myarray[i]["timeperiod"].trim();
//
//                            if (i === 0) {
//                                if (timeperiod !== "")
//                                {
//
//                                    $('#wifiport_form2 #wifiport_timePeriod_1').val(timeperiod);
//                                } else {
//
//                                    $('#wifiport_form2 #wifiport_timePeriod_1').val("Default");
//                                }
//                            } else {
//
//                                if (timeperiod !== "")
//                                {
//                                    var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="wifiport_time[]" value="' + timeperiod + '" maxlength="100" style="margin: 20px 0px 15px 0px; width:85%"></div>';
//                                    $('#wifiport_form2 #add_time').append(elmnt);
//                                } else {
//
//                                    var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="wifiport_time[]" value="Default" maxlength="100" style="margin: 20px 0px 15px 0px; width:85%"></div>';
//                                    $('#wifiport_form2 #add_time').append(elmnt);
//                                }
//                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
//
//
//                            }
//                        }
//                        

                         for (var i = 0; i < myarray.length; i++)
                        {
                            var timeperiod = myarray[i]["timeperiod"].trim();
                            var oneday_selected = "", threedays_selected = "", oneweek_selected = "", onemonth_selected = "", threemonths_selected = "";
                            if (timeperiod === "1day")
                            {
                                oneday_selected = "selected = selected";

                            }
                            if (timeperiod === "3days")
                            {
                                threedays_selected = "selected = selected";

                            }
                            
                             if (timeperiod === "1week")
                            {
                                oneweek_selected = "selected = selected";

                            }
                             if (timeperiod === "1month")
                            {
                                onemonth_selected = "selected = selected";

                            }
                             if (timeperiod === "3months")
                            {
                                threemonths_selected = "selected = selected";

                            }
                           
                            if (i === 0) {
                                $('#wifiport_form2 #wifiport_timePeriod_1').val(timeperiod);
                            } else {

                                var elmnt = '<div class="con"><select class="form-control tym" id="" name="wifiport_time[]" style="margin: 20px 0px 20px 15px; width: 90%;">' +
                                        "<option value=''>-SELECT-</option><option value='1day' " + oneday_selected + ">1 day</option>" +
                                        "<option value='3days' " + threedays_selected + ">3 days</option>" +
                                        "<option value='1week' " + oneweek_selected + ">1 week</option>" +
                                        "<option value='1month' " + onemonth_selected + ">1 month</option>" +
                                        "<option value='3months' " + threemonths_selected + ">3 months</option>" +
                                        "</select></div>";
                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
                                // var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="wifiport_service[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
                                $('#wifiport_form2 #add_time').append(elmnt);

                                //var elmnt = '<div class="con"><input class="form-control dns_name" placeholder="Enter the DNS URL [e.g.: https://demo.nic.in or https://demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" maxlength="100" style="margin: 20px 0px 20px 15px; width: 90%;"><span id="domain1" style="position: relative;width: 70px;float: right;margin-right: 15px;margin-top: -54px;height: 34px;padding: 6px 12px;border: 1px solid #c2cad8;">.' + radioValue + '.in</span></div>';
//                                var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1"  placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="wifiport_action[]" value="' + action + '" maxlength="100" style="margin: 20px 0px 15px 0px;"></div>';
//                                $('#wifiport_form2 #add_action').append(elmnt);
                            }
                            
                             
                        }
                        
                        
                        //$('#wifiport_form2 #wifiport_timeperiod_1').val("1234");
                        $('.edit').removeClass('display-hide');
                        $('#wifiport_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                        $("#wifiport_form2 :button[type='button']").removeAttr('disabled');
                        $("#wifiport_form2 #tnc").removeAttr('disabled');
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
    $('#wifiport_form2').submit(function (e) {
        e.preventDefault();
        $("#wifiport_form2 :disabled").removeAttr('disabled');
        $('#wifiport_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifiport_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifiport_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifiport_preview_tab #user_email').removeAttr('disabled'); // 20th march
        $('#wifiport_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var data = JSON.stringify($('#wifiport_form2').serializeObject());
        var wifiport_sourceip = $("#wifiport_form2 input[name='wifiport_sourceip[]']").val();
        if (wifiport_sourceip !== "")
        {
            wifiport_sourceip = $("#wifiport_form2 input[name='wifiport_sourceip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        } else {
            wifiport_sourceip = $("#wifiport_form2 input[name='wifiport_sourceip[]']").val();
        }
        var wifiport_destinationip = $("#wifiport_form2 input[name='wifiport_destinationip[]']").val();
        if (wifiport_destinationip !== "")
        {
            wifiport_destinationip = $("#wifiport_form2 input[name='wifiport_destinationip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var wifiport_service = $("#wifiport_form2 select[name='wifiport_service[]']").val();
        if (wifiport_service !== "")
        {
            wifiport_service = $("#wifiport_form2 select[name='wifiport_service[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        console.log("service:::::::::" + wifiport_service)
        var wifiport_port = $("#wifiport_form2 input[name='wifiport_port[]']").val();
        if (wifiport_port !== "")
        {
            wifiport_port = $("#wifiport_form2 input[name='wifiport_port[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var wifiport_action = $("#wifiport_form2 select[name='wifiport_action[]']").val();
        if (wifiport_action !== "")
        {
            wifiport_action = $("#wifiport_form2 select[name='wifiport_action[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var wifiport_time = $("#wifiport_form2 select[name='wifiport_time[]']").val();
        if (wifiport_time !== "")
        {
            wifiport_time = $("#wifiport_form2 select[name='wifiport_time[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

//        var cert = $('#dns_form2 #cert').val();
//
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        var submit = $('#wifiport_form2 #confirm').val();

//        $('#dns_form2 .edit').removeClass('display-hide');

        $.ajax({
            type: 'POST',
            url: "wifiport_tab2",
            data: {data: data, wifiport_sourceip: wifiport_sourceip, wifiport_destinationip: wifiport_destinationip, wifiport_service: wifiport_service, wifiport_port: wifiport_port, wifiport_action: wifiport_action, wifiport_time: wifiport_time, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.source_ip_error !== null && jsonvalue.error.source_ip_error !== "" && jsonvalue.error.source_ip_error !== undefined)
                {
                    $('#wifiport_form2 #source_ip_error').html(jsonvalue.error.source_ip_error)
                    $('#wifiport_form2 #source_ip_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                    $('#wifiport_form2 #source_ip_error').html("")

                }
                if (jsonvalue.error.destination_ip_error !== null && jsonvalue.error.destination_ip_error !== "" && jsonvalue.error.destination_ip_error !== undefined)
                {
                    $('#wifiport_form2 #destination_ip_error').html(jsonvalue.error.destination_ip_error)
                    $('#wifiport_form2 #destination_ip_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                    $('#wifiport_form2 #destination_ip_error').html("")

                }
                if (jsonvalue.error.service_error !== null && jsonvalue.error.service_error !== "" && jsonvalue.error.service_error !== undefined)
                {
                    $('#wifiport_form2 #service_ip_error').html(jsonvalue.error.service_error)
                    $('#wifiport_form2 #service_ip_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                    $('#wifiport_form2 #service_ip_error').html("")

                }
                if (jsonvalue.error.ports_error !== null && jsonvalue.error.ports_error !== "" && jsonvalue.error.ports_error !== undefined)
                {
                    $('#wifiport_form2 #port_error').html(jsonvalue.error.ports_error)
                    $('#wifiport_form2 #port_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                    $('#wifiport_form2 #port_error').html("")

                }

                if (jsonvalue.error.action_error !== null && jsonvalue.error.action_error !== "" && jsonvalue.error.action_error !== undefined)
                {
                    $('#wifiport_form2 #action_error').html(jsonvalue.error.action_error)
                    $('#wifiport_form2 #action_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                    $('#wifiport_form2 #action_error').html("")

                }
                if (jsonvalue.error.timeperiod_error !== null && jsonvalue.error.timeperiod_error !== "" && jsonvalue.error.timeperiod_error !== undefined)
                {
                    $('#wifiport_form2 #time_error').html(jsonvalue.error.timeperiod_error)
                    $('#wifiport_form2 #time_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                    $('#wifiport_form2 #time_error').html("")

                }

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#wifiport_form2 #useremployment_error').focus();
                    $('#wifiport_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#wifiport_form2 #minerror').focus();
                    $('#wifiport_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#wifiport_form2 #deperror').focus();
                    $('#wifiport_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #deperror').html("");
                }

                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#wifiport_form2 #other_dept').focus();
                    $('#wifiport_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#wifiport_form2 #smierror').focus();
                    $('#wifiport_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#wifiport_form2 #state_error').focus();
                    $('#wifiport_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#wifiport_form2 #org_error').focus();
                    $('#wifiport_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#wifiport_form2 #ca_design').focus();
                    $('#wifiport_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#wifiport_form2 #hod_name').focus();
                    $('#wifiport_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#wifiport_form2 #hod_mobile').focus();
                    $('#wifiport_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#wifiport_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#wifiport_form2 #hod_tel').focus();
                    $('#wifiport_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #hodtel_error').html("");
                }
                 if (jsonvalue.error.purpose_error !== null && jsonvalue.error.purpose_error !== "" && jsonvalue.error.purpose_error !== undefined)
                    {
                        $('#wifiport_form2 #purpose_error').html(jsonvalue.error.purpose_error)
                        $('#wifiport_form2 #purpose_error').focus();
                        error_flag = false;
                        $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        $('wifiport_form2 #purpose_error').html("")

                    }
                if (error_flag) {
                    // show modal
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'wifiport'},
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

    $('#wifiport_form2 #confirm').click(function (e) {

        e.preventDefault();
        $("#wifiport_form2 :disabled").removeAttr('disabled');
        $('#wifiport_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifiport_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifiport_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifiport_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#wifiport_form2').serializeObject());
        $('#wifiport_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 19thjan18



        $('#wifiport_form2 #user_email').prop('disabled', 'true'); // 20th march

        var wifiport_sourceip = $("#wifiport_form2 input[name='wifiport_sourceip[]']").val();

        if (wifiport_sourceip !== "")
        {
            wifiport_sourceip = $("#wifiport_form2 input[name='wifiport_sourceip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        } else {
            wifiport_sourceip = $("#wifiport_form2 input[name='wifiport_sourceip[]']").val();
        }

        var wifiport_destinationip = $("#wifiport_form2 input[name='wifiport_destinationip[]']").val();
        if (wifiport_destinationip !== "")
        {
            wifiport_destinationip = $("#wifiport_form2 input[name='wifiport_destinationip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var wifiport_service = $("#wifiport_form2 select[name='wifiport_service[]']").val();
        if (wifiport_service !== "")
        {
            wifiport_service = $("#wifiport_form2 select[name='wifiport_service[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        console.log("service:::::::::" + wifiport_service)
        var wifiport_port = $("#wifiport_form2 input[name='wifiport_port[]']").val();
        if (wifiport_port !== "")
        {
            wifiport_port = $("#wifiport_form2 input[name='wifiport_port[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var wifiport_action = $("#wifiport_form2 select[name='wifiport_action[]']").val();
        if (wifiport_action !== "")
        {
            wifiport_action = $("#wifiport_form2 select[name='wifiport_action[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }


        var wifiport_time = $("#wifiport_form2 select[name='wifiport_time[]']").val();
        if (wifiport_time !== "")
        {
            wifiport_time = $("#wifiport_form2 select[name='wifiport_time[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        
         var remarks = $("#wifiport_form2 input[name='remarks[]']").val();
        if (remarks !== "")
        {
            remarks = $("#wifiport_form2 input[name='remarks[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        
         var purpose = $("#wifiport_form2 input[name='purpose[]']").val();
        if (purpose !== "")
        {
            purpose = $("#wifiport_form2 input[name='purpose[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
   
//alert("purpose:::"+purpose);
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        var submit = $('#wifiport_form2 #confirm').val();
        
        console.log("submit"+submit);

        $('#wifiport_form2 .edit').removeClass('display-hide');
        $.ajax({
            type: "POST",
            url: "wifiport_tab2",
            data: {data: data, wifiport_sourceip: wifiport_sourceip, wifiport_destinationip: wifiport_destinationip, wifiport_service: wifiport_service, wifiport_port: wifiport_port, wifiport_action: wifiport_action, wifiport_time: wifiport_time, action_type: "validate", CSRFRandom: CSRFRandom, request_type: submit},
            datatype: JSON,
            success: function (data)
            {

                // resetCSRFRandom();// line added by pr on 10thjan18
                var error_flag = true;
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                if (jsonvalue.error.source_ip_error !== null && jsonvalue.error.source_ip_error !== "" && jsonvalue.error.source_ip_error !== undefined)
                {
                   
                    $('#wifiport_form2 #source_ip_error').html(jsonvalue.error.source_ip_error)
                    $('#wifiport_form2 #source_ip_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                     
                    $('#wifiport_form2 #source_ip_error').html("")

                }
                if (jsonvalue.error.destination_ip_error !== null && jsonvalue.error.destination_ip_error !== "" && jsonvalue.error.destination_ip_error !== undefined)
                {
                     
                    $('#wifiport_form2 #destination_ip_error').html(jsonvalue.error.destination_ip_error)
                    $('#wifiport_form2 #destination_ip_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                    
                    $('#wifiport_form2 #destination_ip_error').html("")

                }
                if (jsonvalue.error.service_error !== null && jsonvalue.error.service_error !== "" && jsonvalue.error.service_error !== undefined)
                {
                     
                    $('#wifiport_form2 #service_ip_error').html(jsonvalue.error.service_error)
                    $('#wifiport_form2 #service_ip_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                    
                    $('#wifiport_form2 #service_ip_error').html("")

                }
                if (jsonvalue.error.ports_error !== null && jsonvalue.error.ports_error !== "" && jsonvalue.error.ports_error !== undefined)
                {
                    
                    $('#wifiport_form2 #port_error').html(jsonvalue.error.ports_error)
                    $('#wifiport_form2 #port_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                     
                    $('#wifiport_form2 #port_error').html("")

                }

                if (jsonvalue.error.action_error !== null && jsonvalue.error.action_error !== "" && jsonvalue.error.action_error !== undefined)
                {
                     
                    $('#wifiport_form2 #action_error').html(jsonvalue.error.action_error)
                    $('#wifiport_form2 #action_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                     
                    $('#wifiport_form2 #action_error').html("")

                }
                if (jsonvalue.error.timeperiod_error !== null && jsonvalue.error.timeperiod_error !== "" && jsonvalue.error.timeperiod_error !== undefined)
                {
                    
                    $('#wifiport_form2 #time_error').html(jsonvalue.error.timeperiod_error)
                    $('#wifiport_form2 #time_error').focus();
                    error_flag = false;
                    $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_form2 #imgtxt').val("");
                } else {
                    
                    $('#wifiport_form2 #time_error').html("")

                }

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#wifiport_form2 #useremployment_error').focus();
                    $('#wifiport_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#wifiport_form2 #minerror').focus();
                    $('#wifiport_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#wifiport_form2 #deperror').focus();
                    $('#wifiport_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #deperror').html("");
                }

                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#wifiport_form2 #other_dept').focus();
                    $('#wifiport_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#wifiport_form2 #smierror').focus();
                    $('#wifiport_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#wifiport_form2 #state_error').focus();
                    $('#wifiport_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#wifiport_form2 #org_error').focus();
                    $('#wifiport_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#wifiport_form2 #ca_design').focus();
                    $('#wifiport_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#wifiport_form2 #hod_name').focus();
                    $('#wifiport_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#wifiport_form2 #hod_mobile').focus();
                    $('#wifiport_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#wifiport_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#wifiport_form2 #hod_tel').focus();
                    $('#wifiport_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#wifiport_form2 #hodtel_error').html("");
                }   
                  if (jsonvalue.error.purpose_error !== null && jsonvalue.error.purpose_error !== "" && jsonvalue.error.purpose_error !== undefined)
                    {
                        $('#wifiport_form2 #purpose_error').html(jsonvalue.error.purpose_error)
                        $('#wifiport_form2 #purpose_error').focus();
                        error_flag = false;
                        $('#wifiport_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#imgtxt').val("");
                    } else {
                        $('wifiport_form2 #purpose_error').html("")

                    }

                if (!error_flag)
                {
                    $("#wifiport_form2 :disabled").removeAttr('disabled');
                    $('#wifiport_form2 #wifiport_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#wifiport_form2 #wifiport_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#wifiport_form2 #wifiport_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march


                } else {

//                if (!error_flag) {
//                    alert("inside if")
//
//                    $("#wifiport_form2 :disabled").removeAttr('disabled');
//                    $('#wifiport_form2 #wifiport_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
//                    $('#wifiport_form2 #wifiport_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
//                    $('#wifiport_form2 #wifiport_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
//                } else {

                    if ($('#wifiport_form2 #tnc').is(":checked"))
                    {
                        
//                        if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
//                        {
//                            $('#wifiport_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                            $('#wifiport_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                            $('#wifiport_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
//
//
//                        } else if (jsonvalue.form_details.min == "External Affairs")
//                        {
//                            $('#wifiport_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#wifiport_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#wifiport_form_confirm #fill_hod_mobile').html("+919384664224");
//                        } 
                        
                    //    else {



//                            if (jsonvalue.form_details.fwd_ofc_email != "")
//                            {
//                                $('#wifi_form_confirm #details_ro').html("Forwarding Officer Details")
//                                $('#wifi_form_confirm #fill_hod_name').html(jsonvalue.form_details.fwd_ofc_name)
//                                $('#wifi_form_confirm #fill_hod_email').html(jsonvalue.form_details.fwd_ofc_email)
//                                $('#wifi_form_confirm #fill_hod_mobile').html(jsonvalue.form_details.fwd_ofc_mobile)
//
//
//                            } else {
                            $('#wifiport_form_confirm #details_ro').html("Forwarding Officer Details")
                            $('#wifiport_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name)
                            $('#wifiport_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email)
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#wifiport_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile)
                            // }

                     //   }

                        $('#wifiport_form2 #tnc_error').html("");
                        $('#wifiport_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                        

                    } else {
                        $('#wifiport_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        $('#wifiport_form2 #wifiport_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#wifiport_form2 #wifiport_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //$('#wifiport_form2 #wifiport_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        $("#wifiport_form2 #tnc").removeAttr('disabled');
                    }
                }

            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });

    $('#wifiport_form_confirm #confirmYes').click(function () {
        $('#wifiport_form2').submit();
        $('#stack3').modal('toggle');
    });

//Edit prewiew before submission//
    $('#wifiport_form2 .edit').click(function () {
//alert("EDIT");
        var wifiport_service = $("#wifiport_form2 select[name='wifiport_service[]']").val();
        if (wifiport_service !== "")
        {
            wifiport_service = $("#wifiport_form2 select[name='wifiport_service[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        var res = wifiport_service.split(";");

        var employment = $('#wifiport_preview_tab #user_employment').val();
        var min = $('#wifiport_preview_tab #min').val();
        var dept = $('#wifiport_preview_tab #dept').val();
        var statecode = $('#wifiport_preview_tab #stateCode').val();
        var Smi = $('#wifiport_preview_tab #Smi').val();
        var Org = $('#wifiport_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifiport_preview_tab #min');
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
                var select = $('#wifiport_preview_tab #dept');
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
                $('#wifiport_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifiport_preview_tab #stateCode');
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
                var select = $('#wifiport_preview_tab #Smi');
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
                $('#wifiport_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifiport_preview_tab #Org');
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
                $('#wifiport_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#wifiport_preview_tab #central_div').hide();
            $('#wifiport_preview_tab #state_div').hide();
            $('#wifiport_preview_tab #other_div').hide();
        }



        $('#wifiport_form2').find('input, textarea, button, select').prop('disabled', 'false');
        $('#wifiport_form2').find('input, textarea, button, select').removeAttr('disabled');
        $('#wifiport_form2 #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifiport_form2 #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        $('#wifiport_form2 #REditPreview #hod_email').removeAttr('disabled');
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

//preview after submission
    $(document).on('click', '#wifiport_preview .edit', function () {
       
        // $('#wifiport_preview .edit').click(function () {
 // alert('edit wifiport_preview');
        var employment = $('#wifiport_preview_tab #user_employment').val();
        var min = $('#wifiport_preview_tab #min').val();
        var dept = $('#wifiport_preview_tab #dept').val();
        var statecode = $('#wifiport_preview_tab #stateCode').val();
        var Smi = $('#wifiport_preview_tab #Smi').val();
        var Org = $('#wifiport_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifiport_preview_tab #min');
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
                var select = $('#wifiport_preview_tab #dept');
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
                $('#wifiport_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifiport_preview_tab #stateCode');
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
                var select = $('#wifiport_preview_tab #Smi');
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
                $('#wifiport_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#wifiport_preview_tab #Org');
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
                $('#wifiport_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#wifiport_preview_tab #central_div').hide();
            $('#wifiport_preview_tab #state_div').hide();
            $('#wifiport_preview_tab #other_div').hide();
        }




        $('#wifiport_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#wifiport_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#wifiport_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifiport_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifiport_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        //console.log($(this))
        $(this).addClass('display-hide');


        if ($("#comingFrom").val("admin"))
        {
            $("#wifiport_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#wifiport_preview .save-changes").html("Update");
        }


        //$(this).hide();
    });

//preview update after submission
    $(document).on('click', '#wifiport_preview #confirm', function () {
        //$('#wifiport_preview #confirm').click(function (e) {
 //alert('update wifiport_preview');
        $('#wifiport_preview').submit();

    });

    $('#wifiport_preview').submit(function (e) {


        e.preventDefault();
        $("#wifiport_preview :disabled").removeAttr('disabled');
        $('#wifiport_preview #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#wifiport_preview #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifiport_preview #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#wifiport_preview #user_email').removeAttr('disabled'); // 20th march
        $('#wifiport_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var data = JSON.stringify($('#wifiport_preview').serializeObject());
        var wifiport_sourceip = $("#wifiport_preview input[name='wifiport_sourceip[]']").val();
        if (wifiport_sourceip !== "")
        {
            wifiport_sourceip = $("#wifiport_preview input[name='wifiport_sourceip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        } else {
            wifiport_sourceip = $("#wifiport_preview input[name='wifiport_sourceip[]']").val();
        }
        console.log();
        var wifiport_destinationip = $("#wifiport_preview input[name='wifiport_destinationip[]']").val();
        if (wifiport_destinationip !== "")
        {
            wifiport_destinationip = $("#wifiport_preview input[name='wifiport_destinationip[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var wifiport_service = $("#wifiport_preview select[name='wifiport_service[]'] option:selected").val();
       // alert("wifiport_service"+wifiport_service);
        if (wifiport_service !== ""){
            wifiport_service = $("#wifiport_preview select[name='wifiport_service[]'] option:selected").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var wifiport_port = $("#wifiport_preview input[name='wifiport_port[]']").val();
        if (wifiport_port !== "")
        {
            wifiport_port = $("#wifiport_preview input[name='wifiport_port[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }

        var wifiport_action = $("#wifiport_preview select[name='wifiport_action[]'] option:selected").val();
        if (wifiport_action !== "")
        {
            wifiport_action = $("#wifiport_preview select[name='wifiport_action[]'] option:selected").map(function () {
                return $(this).val();
            }).get().join(';');
        }


        var wifiport_time = $("#wifiport_preview select[name='wifiport_time[]'] option:selected").val();
        if (wifiport_time !== "")
        {
            wifiport_time = $("#wifiport_preview select[name='wifiport_time[]'] option:selected").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        
         var purpose = $("#wifiport_preview input[name='purpose[]']").val();
        if (purpose !== "")
        {
            purpose = $("#wifiport_preview input[name='purpose[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        
          var remarks = $("#wifiport_preview input[name='remarks[]']").val();
        if (remarks !== "")
        {
            remarks = $("#wifiport_preview input[name='remarks[]']").map(function () {
                return $(this).val();
            }).get().join(';');
        }
        
        console.log("wifiport_sourceip:::" + wifiport_sourceip);
         console.log("wifiport_destinationip:::" + wifiport_destinationip);
          console.log("wifiport_service:::" + wifiport_service);
           console.log("wifiport_port:::" + wifiport_port);
         console.log("wifiport_action:::" + wifiport_action);
          console.log("wifiport_time:::" + wifiport_time);
           console.log("purpose:::" + purpose);
           console.log("remarks:::" + remarks);
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: 'POST',
            url: "wifiport_tab2",
            data: {data: data, wifiport_sourceip: wifiport_sourceip, wifiport_destinationip: wifiport_destinationip, wifiport_service: wifiport_service, wifiport_port: wifiport_port, wifiport_action: wifiport_action, wifiport_time: wifiport_time,purpose: purpose, action_type: "update", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                //resetCSRFRandom();// line added by pr on 10thjan18
                var error_flag = true;
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                if (jsonvalue.error.source_ip_error !== null && jsonvalue.error.source_ip_error !== "" && jsonvalue.error.source_ip_error !== undefined)
                {
                    $('#wifiport_preview #source_ip_error').html(jsonvalue.error.source_ip_error)
                    $('#wifiport_preview #source_ip_error').focus();
                    error_flag = false;
                    $('#wifiport_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_preview #imgtxt').val("");
                } else {
                    $('#wifiport_preview #source_ip_error').html("")

                }
                if (jsonvalue.error.destination_ip_error !== null && jsonvalue.error.destination_ip_error !== "" && jsonvalue.error.destination_ip_error !== undefined)
                {
                    $('#wifiport_preview #destination_ip_error').html(jsonvalue.error.destination_ip_error)
                    $('#wifiport_preview #destination_ip_error').focus();
                    error_flag = false;
                    $('#wifiport_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_preview #imgtxt').val("");
                } else {
                    $('#wifiport_preview #destination_ip_error').html("")

                }
                if (jsonvalue.error.service_error !== null && jsonvalue.error.service_error !== "" && jsonvalue.error.service_error !== undefined)
                {
                    $('#wifiport_preview #service_ip_error').html(jsonvalue.error.service_error)
                    $('#wifiport_preview #service_ip_error').focus();
                    error_flag = false;
                    $('#wifiport_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_preview #imgtxt').val("");
                } else {
                    $('#wifiport_preview #service_ip_error').html("")

                }
                if (jsonvalue.error.ports_error !== null && jsonvalue.error.ports_error !== "" && jsonvalue.error.ports_error !== undefined)
                {
                    $('#wifiport_preview #port_error').html(jsonvalue.error.ports_error)
                    $('#wifiport_preview #port_error').focus();
                    error_flag = false;
                    $('#wifiport_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_preview #imgtxt').val("");
                } else {
                    $('#wifiport_preview #port_error').html("")

                }
                if (jsonvalue.error.action_error !== null && jsonvalue.error.action_error !== "" && jsonvalue.error.action_error !== undefined)
                {
                    $('#wifiport_preview #action_error').html(jsonvalue.error.action_error)
                    $('#wifiport_preview #action_error').focus();
                    error_flag = false;
                    $('#wifiport_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_preview #imgtxt').val("");
                } else {
                    $('#wifiport_preview #action_error').html("")

                }
                if (jsonvalue.error.timeperiod_error !== null && jsonvalue.error.timeperiod_error !== "" && jsonvalue.error.timeperiod_error !== undefined)
                {
                    $('#wifiport_preview #time_error').html(jsonvalue.error.timeperiod_error)
                    $('#wifiport_preview #time_error').focus();
                    error_flag = false;
                    $('#wifiport_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#wifiport_preview #imgtxt').val("");
                } else {
                    $('#wifiport_preview #time_error').html("")

                }

                if (!error_flag)
                {

//                    $("#wifiport_preview :disabled").removeAttr('disabled');
//                    $('#wifiport_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
//                    $('#wifiport_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march$("#wifiport_preview :disabled").removeAttr('disabled');
                   $("#wifiport_preview :disabled").removeAttr('disabled');
                    $('#wifiport_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#wifiport_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); 

                } else {

                    $('#wifiport_preview_form').modal('toggle');
                }


            }, error: function ()
            {
                console.log('error');
            }
        });
    });



});

