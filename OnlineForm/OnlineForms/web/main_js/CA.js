var dateFormat = function () {
    var token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
            timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
            timezoneClip = /[^-+\dA-Z]/g,
            pad = function (val, len) {
                val = String(val);
                len = len || 2;
                while (val.length < len)
                    val = "0" + val;
                return val;
            };
    // Regexes and supporting functions are cached through closure
    return function (date, mask, utc) {
        var dF = dateFormat;
        // You can't provide utc if you skip other args (use the "UTC:" mask prefix)
        if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
            mask = date;
            date = undefined;
        }
        // Passing date through Date applies Date.parse, if necessary
        date = date ? new Date(date) : new Date;
        if (isNaN(date))
            throw SyntaxError("invalid date");
        mask = String(dF.masks[mask] || mask || dF.masks["default"]);
        // Allow setting the utc argument via the mask
        if (mask.slice(0, 4) == "UTC:") {
            mask = mask.slice(4);
            utc = true;
        }
        var _ = utc ? "getUTC" : "get",
                d = date[_ + "Date"](),
                D = date[_ + "Day"](),
                m = date[_ + "Month"](),
                y = date[_ + "FullYear"](),
                H = date[_ + "Hours"](),
                M = date[_ + "Minutes"](),
                s = date[_ + "Seconds"](),
                L = date[_ + "Milliseconds"](),
                o = utc ? 0 : date.getTimezoneOffset(),
                flags = {
                    d: d,
                    dd: pad(d),
                    ddd: dF.i18n.dayNames[D],
                    dddd: dF.i18n.dayNames[D + 7],
                    m: m + 1,
                    mm: pad(m + 1),
                    mmm: dF.i18n.monthNames[m],
                    mmmm: dF.i18n.monthNames[m + 12],
                    yy: String(y).slice(2),
                    yyyy: y,
                    h: H % 12 || 12,
                    hh: pad(H % 12 || 12),
                    H: H,
                    HH: pad(H),
                    M: M,
                    MM: pad(M),
                    s: s,
                    ss: pad(s),
                    l: pad(L, 3),
                    L: pad(L > 99 ? Math.round(L / 10) : L),
                    t: H < 12 ? "a" : "p",
                    tt: H < 12 ? "am" : "pm",
                    T: H < 12 ? "A" : "P",
                    TT: H < 12 ? "AM" : "PM",
                    Z: utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
                    o: (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
                    S: ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
                };
        return mask.replace(token, function ($0) {
            return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
        });
    };
}();
// Some common format strings
dateFormat.masks = {
    "default": "ddd mmm dd yyyy HH:MM:ss",
    shortDate: "m/d/yy",
    mediumDate: "mmm d, yyyy",
    longDate: "mmmm d, yyyy",
    fullDate: "dddd, mmmm d, yyyy",
    shortTime: "h:MM TT",
    mediumTime: "h:MM:ss TT",
    longTime: "h:MM:ss TT Z",
    isoDate: "yyyy-mm-dd",
    isoTime: "HH:MM:ss",
    isoDateTime: "yyyy-mm-dd'T'HH:MM:ss",
    isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};
// Internationalization strings
dateFormat.i18n = {
    dayNames: [
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    ],
    monthNames: [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    ]
};
// For convenience...
Date.prototype.format = function (mask, utc) {
    return dateFormat(this, mask, utc);
};
$(document).ready(function () {

    // above code commented and below added by pr on 12thapr18
    $(document).on("click", "#fwd_wo_ad", function (e) {
        e.preventDefault();
        var esignVal = $("input[name='esign_type']:checked").val();
        var role = $("#role").val();
        var value = $(this).attr("value");
        var random = $("#random").val();
        var app_type = $("#app_type").val();
        var statRemarks = $("#statRemarksApp").val();
        if (esignVal == null) {
            $('#esign_error').text('Please select any process to proceed');
        } else if (esignVal == "online" || app_type == "manual_upload") // if added by pr on 9thfeb18 , manual condition added by pr on 22ndmar18
        {
            var app_ca_type = "online";
            if (app_type == "manual_upload") {
                app_ca_type = "manual_upload";
            }
            $("#forward_modal").modal("hide");
            $("#statRemarksApp").val(""); // line added by pr on 21stjun18
            $("#approve_modal").modal(); // above lines commented this added by pr on 4thjun18
        } else if (esignVal == 'esign') // opening the modal to enter aadhaar number
        {
            var value = $(this).attr("value");
            var reg_no_arr = value.split("~");
            var reg_no = "",
                    form_name = "";
            if (reg_no_arr[0] != null) {
                reg_no = reg_no_arr[0];
            }
            if (reg_no_arr[1] != null) {
                form_name = reg_no_arr[1];
            }
            $("#ref_num").val(reg_no);
            $("#formtype").val(form_name);
            $.ajax({
                type: "POST",
                url: "ConsentAdmin",
                data: {
                    check: esignVal,
                    formtype: form_name,
                    ref_num: reg_no
                },
                success: function (check) {
                    window.location.href = "esignPDF";
                },
                error: function () {
                    console.log('error');
                }
            });
        }
    });

    // approve_btn code modified by pr on 27thnov19
    $(document).on("click", "#approve_btn", function (e) {
        e.preventDefault();
//        var esignVal = $("input[name='esign_type']:checked").val();
//        var role = $("#role").val();
        var value = $(this).attr("value");
//        var random = $("#random").val();
//        var statRemarks = $("#statRemarksApp").val();

        // start, code added by pr on 27thnov19

        // ajax call to an action class to check for any previous rejected requests for this applicant email and mobile combination by the admin for this service

        //console.log(" inside approve btn function click regNo value is "+value);

        var arr = value.split("~");

        var reg_no = arr[0];

        var form_name = arr[1];

        var emailMobile = $("#app_" + reg_no).attr("class");

        var emmob_arr = emailMobile.split("~");

        var email = emmob_arr[0];

        var mobile = emmob_arr[1];

        //console.log(" reg_no value is "+reg_no+" emailMobile value is "+emailMobile);

        var newvalue = emailMobile + "~" + form_name; // now the value is replaced with email~mobile~form_name

        //var email_mobile = $(emailMobileElement).attr("class");

        $.post('checkPreviousReject', {
            regNo: newvalue
        },
                function (jsonResponse) {

                    //console.log(" inside checkPreviousReject response prevrejectdata value is "+jsonResponse.prevrejectdata);

                    var table = "";

                    if (jsonResponse.prevrejectdata != null)
                    {
                        table = getPrevRejectTable(jsonResponse.prevrejectdata, email, mobile);
                    }

                    approveByRole(table, value);

                }, 'json');

        // the code which was here has been taken out into a new function  approveByRole( msg, value ) below                                  

        // if there were some requests found which were rejected then add it in the msg string of confirmation        

        // end, code added by pr on 27thnov19      

    });



    // below function formed from the above code by pr on 27thnov19
    function approveByRole(msg, value)
    {
        //var msg = "";

        var reg_no = value.split('~')[0];
        console.log(reg_no)
        var esignVal = $("input[name='esign_type']:checked").val();
        var role = $("#role").val();
        //var value = $(this).attr("value");
        var random = $("#random").val();
        var statRemarks = $("#statRemarksApp").val();
        var csrf = $('#CSRFRandom').val();
        var title = "";
        //console.log("esignVal is "+esignVal+" role is "+role+" value is "+value+" random is "+random+" statRemarks "+statRemarks );        

        if (role == 'ca') {
            var app_type = $("#app_type").val();
            if (esignVal == null) {
                $('#esign_error').text('Please select any process to proceed');
            } else if (esignVal == "online" || app_type == "manual_upload" || app_type.indexOf("Manual") >= 0) {
                var app_ca_type = "online";
                if (app_type == "manual_upload" || app_type.indexOf("Manual") >= 0) {
                    app_ca_type = "manual_upload";
                }
                if (app_type == "online") {
                    //$('.bootbox .modal-footer').prepend("Do you really wish to proceed without eSigning ("+reg_no+")?");
                    msg += "Note: Please verify credentials of the user before approving eSigning Request (" + reg_no + ")."; // var removed , cancat + added by pr on 27thnov19
                    title = "Do you really wish to proceed without eSigning (" + reg_no + ") ?"; // var removed , cancat + added by pr on 27thnov19
                } else {
                    //$('.bootbox .modal-footer').prepend("Do you really wish to approve and forward this Request ("+reg_no+")?");
                    msg += "Note: Please verify credentials of the user before approving Request (" + reg_no + ").";
                    title = "Do you really wish to approve and forward this Request (" + reg_no + ") ?"; // var removed , cancat + added by pr on 27thnov19
                }

                console.log(" inside RO block msg is " + msg);

                bootbox.confirm({
                    title: title,
                    message: msg,
                    size: 'large',
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
                            $("#fwd_wo_ad").hide();
                            $(".loadergif").show();
                            $(".loader").show();
                            $.ajax({url: 'action', type: 'post', data: {
                                    regNo: value,
                                    random: random,
                                    app_ca_type: app_ca_type, // line added by pr on 22ndmar18                                
                                    app_ca_path: "",
                                    CSRFRandom: csrf,
                                    statRemarks: statRemarks // line added by pr on 10thmay18
                                },
                                success: function(jsonResponse) {

                                    if (jsonResponse.csrf_error) {
                                        alert(jsonResponse.csrf_error);
                                    }
                                    resetCSRFRandom();

                                    // start, code added by pr on 20thapr18
                                    if (app_type != "manual_upload") // if around added by pr on 9thmay18
                                    {
                                        $("#fwd_wo_ad").show();
                                    }
                                    $(".loadergif").hide();
                                    $(".loader").hide();
                                    if (jsonResponse.isSuccess == true) {
                                        resetRandom();
                                        $(".alert-danger").show();
                                        $(".alert-danger").html(jsonResponse.msg);
                                        showAlertBox(jsonResponse.msg)
                                        $("#forward_modal").modal("hide");
                                        $("#approve_modal").modal("hide");
                                        var action = $("#action_hidden").val();
                                        var table = $('#example').DataTable();
                                        table.ajax.reload(null, false);
                                    } else if (jsonResponse.isError == true) {
                                        resetRandom();
                                        $(".alert-danger").hide();
                                        showAlertBox(jsonResponse.msg)
                                    }
                                }, error: function (jsonResponse) {
                                    var json2 = JSON.stringify(jsonResponse);
                                                var jsonvalue = JSON.parse(json2);
                                                var error = JSON.parse(jsonvalue.responseText.substring(0, jsonvalue.responseText.indexOf("}") + 1)).error;
                                                $('#new_alert .modal-body').html(error);
                                                $('#new_alert').modal('show');
                                                $('.loader').hide();
                                },
                                datatype: 'json'});

                        }
                    }
                });
            } else if (esignVal == 'esign') {
                var app_ca_path = $("#app_ca_path").val();
                $.ajax({url: 'action', type: 'post', data: {
                        regNo: value,
                        random: random,
                        app_ca_type: "esign", // line added by pr on 22ndmar18 
                        app_ca_path: app_ca_path,
                        statRemarks: statRemarks // line added by pr on 11thmay18
                    },
                    success: function(jsonResponse) {
                        if (jsonResponse.isSuccess == true) {
                            resetRandom();
                            $(".alert-danger").show();
                            $(".alert-danger").html(jsonResponse.msg);
                            showAlertBox(jsonResponse.msg)
                            $("#forward_modal").modal("hide");
                            $("#approve_modal").modal("hide");
                            var action = $("#action_hidden").val();
                            var table = $('#example').DataTable();
                            table.ajax.reload(null, false);
                        } else if (jsonResponse.isError == true) {
                            resetRandom();
                            $(".alert-danger").hide();
                            showAlertBox(jsonResponse.msg)
                            $("#fwd_coord_error").html(jsonResponse.msg);
                        }
                    }, error: function (jsonResponse) {
                        var json2 = JSON.stringify(jsonResponse);
                                                var jsonvalue = JSON.parse(json2);
                                                var error = JSON.parse(jsonvalue.responseText.substring(0, jsonvalue.responseText.indexOf("}") + 1)).error;
                                                $('#new_alert .modal-body').html(error);
                                                $('#new_alert').modal('show');
                                                $('.loader').hide();
                    },
                    datatype: 'json'});
            }
        } else if (role == 'sup') {
            var simply_checked = $("input[name='forwardtoda']:checked").val();

            //console.log(" inside Sup block backlog msg is "+msg);

            if (simply_checked == 'simplyforward') {
                var fwd_coord = $("#enter_da_co").val();
                var fwd_coord_btn = $("#fwd_coord_btn").val();
                var choose_da_type = $("#choose_da_type").val();

                bootbox.confirm({
                    message: msg + "Do you really wish to approve and forward this request to the entered Coordinator/DA ?", // msg added by pr on 27thnov19
                    size: 'large',
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
                            // start, code added by pr on 20thapr18
                            $("#fwd_coord_btn").hide();
                            $(".loadergif").show();
                            $(".loader").show();
                            // end, code added by pr on 20thapr18
                            //alert("conf ="+conf);
                            $.post('fwdToCoordMadmin', {
                                fwd_coord: fwd_coord,
                                choose_da_type: choose_da_type, // line added by pr on 22ndfeb18
                                regNo: fwd_coord_btn,
                                random: random,
                                statRemarks: statRemarks // line added by pr on 10thmay18
                            },
                                    function (jsonResponse) {
                                        // start, code added by pr on 20thapr18
                                        $("#fwd_coord_btn").show();
                                        $(".loadergif").hide();
                                        $(".loader").hide();
//                                    console.log(" inside suport block jsonResponse.prevrejected value is "+jsonResponse.prevrejected);
//                                    
//                                   if(  jsonResponse.prevrejected != null &&  jsonResponse.prevrejected != "" )
//                                   {
//                                       console.log(" inside suport block jsonResponse.prevrejected value not null ");
//                                       
//                                       showAlertBox(jsonResponse.prevrejected);
//                                   }                                    
                                        // end, code added by pr on 20thapr18
                                        if (jsonResponse.isSuccess == true) {
                                            resetRandom(); // line added by pr on 4thjan18
                                            //$(".alert-danger").show();
                                            //$(".alert-danger").html(jsonResponse.msg);
                                            //alert(jsonResponse.msg);
                                            showAlertBox(jsonResponse.msg)
                                            //                                                        $("#forward_modal").modal("toggle");
                                            //
                                            //                                                        location.reload();
                                            // above code commented below added     
                                            // start, code added by pr on 5thjun18
                                            $("#forward_modal").modal("hide");
                                            $("#approve_modal").modal("hide");
                                            var action = $("#action_hidden").val();
                                            //setAction(action) // line commented by pr on 23rdoct18
                                            // start, code added by pr on 23rdoct18
                                            var table = $('#example').DataTable();
                                            table.ajax.reload(null, false);
                                            // end, code added by pr on 23rdoct18
                                            // end, code added by pr on 5thjun18
                                        } else if (jsonResponse.isError == true) {
                                            resetRandom(); // line added by pr on 4thjan18
                                            //$(".alert-danger").hide();
                                            //$("#fwd_coord_error").html(jsonResponse.msg);
                                            //alert(jsonResponse.msg);
                                            showAlertBox(jsonResponse.msg)
                                        }
                                    }, 'json');
                        }
                    }
                });
            } else {
                var selectedValues = [];
                $("#fwd_coord :selected").each(function () {
                    selectedValues.push($(this).val());
                });
                //alert(selectedValues);
                fwd_coord = selectedValues.join();
                // end, code added by pr on 22ndfeb18
                var fwd_coord_btn = $("#fwd_coord_btn").val();
                // start, code added by pr on 22ndfeb18
                var choose_da_type = $("#choose_da_type").val();
                bootbox.confirm({
                    message: msg + "Do you really wish to approve and forward this request to the selected Coordinator/DA ?", // msg added by pr on 27thnov19
                    size: 'large',
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
                            // start, code added by pr on 20thapr18
                            $("#fwd_coord_btn").hide();
                            $(".loadergif").show();
                            $(".loader").show();
                            // end, code added by pr on 20thapr18
                            $.post('fwdToCoordMadmin', {
                                fwd_coord: fwd_coord,
                                choose_da_type: choose_da_type, // line added by pr on 22ndfeb18
                                regNo: fwd_coord_btn,
                                random: random,
                                statRemarks: statRemarks // line added by pr on 10thmay18
                            },
                                    function (jsonResponse) {
                                        // start, code added by pr on 20thapr18
                                        $("#fwd_coord_btn").show();
                                        $(".loadergif").hide();
                                        $(".loader").hide();
                                        // end, code added by pr on 20thapr18
                                        if (jsonResponse.isSuccess == true) {
                                            resetRandom(); // line added by pr on 4thjan18
                                            //$(".alert-danger").show();
                                            //$(".alert-danger").html(jsonResponse.msg);
                                            //alert(jsonResponse.msg);
                                            showAlertBox(jsonResponse.msg)
                                            //                                                        $("#forward_modal").modal("toggle");
                                            //
                                            //                                                        location.reload();
                                            // above code commented below added     
                                            // start, code added by pr on 5thjun18
                                            $("#forward_modal").modal("hide");
                                            $("#approve_modal").modal("hide");
                                            var action = $("#action_hidden").val();
                                            //setAction(action) // line commented by pr on 23rdoct18
                                            // start, code added by pr on 23rdoct18
                                            var table = $('#example').DataTable();
                                            table.ajax.reload(null, false);
                                            // end, code added by pr on 23rdoct18
                                            // end, code added by pr on 5thjun18
                                        } else if (jsonResponse.isError == true) {
                                            resetRandom(); // line added by pr on 4thjan18
                                            //$(".alert-danger").hide();
                                            //$("#fwd_coord_error").html(jsonResponse.msg);
                                            //alert(jsonResponse.msg);
                                            showAlertBox(jsonResponse.msg)
                                        }
                                    }, 'json');
                        }
                    }
                });
            }
        } else if (role == 'co') {
            //if( esign_type == "online" ) // if added by pr on 9thfeb18
            {
                // below code added on 28thnov17 for dns form
                var reg_no_arr = value.split("~");
                var reg_no = "",
                        form_name = "";
                if (reg_no_arr[0] != null) {
                    var reg_no = reg_no_arr[0];
                }
                if (reg_no_arr[1] != null) {
                    var form_name = reg_no_arr[1];
                }

                //console.log(" inside Coordinator block backlog msg is "+msg);

                bootbox.confirm({
                    message: msg + "Do you really wish to approve and forward this request.", // msg added by pr on 27thnov19
                    size: 'large', // added by pr on 27thnov19 in all the above places too
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
                            $(".loadergif").show();
                            $(".loader").show();
                            $.post('fwdToCoordMadmin', {
                                regNo: value,
                                random: random,
                                statRemarks: statRemarks
                            },
                                    function (jsonResponse) {
                                        $(".loadergif").hide();
                                        $(".loader").hide();
                                        resetRandom(); // line added by pr on 4thjan18
                                        if (jsonResponse.isSuccess == true) {
                                            $(".alert-danger").show();
                                            $(".alert-danger").html(jsonResponse.msg);
                                            showAlertBox(jsonResponse.msg)
                                            $("#approve_modal").modal("hide");
                                            var action = $("#action_hidden").val();
                                            var table = $('#example').DataTable();
                                            table.ajax.reload(null, false);
                                        } else if (jsonResponse.isError == true) {
                                            $(".alert-danger").hide();
                                            showAlertBox(jsonResponse.msg)
                                            $("#fwd_coord_error").html(jsonResponse.msg);
                                        }
                                    }, 'json');
                        }
                    }
                });
            }
        }
    }
    $('#reject_btn').click(function () {
        var random = $("#random").val();
        var csrf = $('#CSRFRandom').val();
        var value = $(this).attr("value");
        var statRemarks = $("#statRemarks").val();
        var rej = "rej";
        if (statRemarks != "") {
            var conf = "Do you really wish to Reject this Application ?";
            if (conf) {
                $("#reject_btn").hide();
                $(".loadergif").show();
                $(".loader").show();
                $.ajax({url: 'action', type: 'post', data: {
                        regNo: value,
                        statRemarks: statRemarks,
                        random: random,
                        CSRFRandom: csrf,
                        app_ca_type: rej
                    },
                    success: function(jsonResponse) {
                        if (jsonResponse.csrf_error) {
                            alert(jsonResponse.csrf_error);
                        }
                        resetCSRFRandom();
                        $("#reject_btn").show();
                        $(".loadergif").hide();
                        $(".loader").hide();
                        if (jsonResponse.isSuccess == true) {
                            resetRandom();
                            showAlertBox(jsonResponse.msg)
                            $("#reject_modal").modal("toggle");
                            var action = $("#action_hidden").val();
                            var table = $('#example').DataTable();
                            table.ajax.reload(null, false);
                        } else if (jsonResponse.isError == true) {
                            resetRandom();
                            $(".alert-danger").hide();
                            $("#statRemarksError").html(jsonResponse.msg);
                        }
                    }, error: function (jsonResponse) {
                        var json2 = JSON.stringify(jsonResponse);
                                                var jsonvalue = JSON.parse(json2);
                                                var error = JSON.parse(jsonvalue.responseText.substring(0, jsonvalue.responseText.indexOf("}") + 1)).error;
                                                $('#new_alert .modal-body').html(error);
                                                $('#new_alert').modal('show');
                                                $('.loader').hide();
                    },
                    datatype: 'json'});
            }
        } else {
            showAlertBox("Please enter remarks")
        }
    });
    $("#one").on("click", function () {
        prefillData();
        clearData(); // clear previously filled data
    });
    $("#two").on("click", function () {
        clearData(); // clear previously filled data
    });
    $("#oneda").on("click", function () {
        prefillDAData();
        clearData(); // clear previously filled data
    });
    $("#twoda").on("click", function () {
        prefillDAData();
        clearData(); // clear previously filled data
    });
    $("#fwd_coord_btn").on("click", function () {
        var random = $("#random").val();
        var simply_checked = $("input[name='forwardtoda']:checked").val();
        if (simply_checked == 'simplyforward') {
            var fwd_coord = $("#enter_da_co").val();
            var fwd_coord_btn = $("#fwd_coord_btn").val();
            var choose_da_type = $("#choose_da_type").val();
            var errmsg = "";
            var isValid = true;
            if (fwd_coord == "") {
                errmsg = " Please Enter a coordinator to forward the request to.";
                isValid = false;
            }
            if (choose_da_type == "") {
                errmsg = " Please Choose DA/Coordinator.";
                isValid = false;
            }
            if (isValid) {
                $("#forward_modal").modal("hide");
                $("#statRemarksApp").val(""); // line added by pr on 21stjun18
                $("#approve_modal").modal(); // code replaced with this line by pr on 5thjun18    
            } else {
                showAlertBox(errmsg)
            }
        } else {
            var selectedValues = [];
            $("#fwd_coord :selected").each(function () {
                selectedValues.push($(this).val());
            });
            //alert(selectedValues);
            fwd_coord = selectedValues.join();
            // end, code added by pr on 22ndfeb18
            var fwd_coord_btn = $("#fwd_coord_btn").val();
            // start, code added by pr on 22ndfeb18
            var choose_da_type = $("#choose_da_type").val();
            var errmsg = "";
            var isValid = true;
            if (fwd_coord == "") {
                errmsg = " Please Select a coordinator to forward the request to.";
                isValid = false;
            }
            if (choose_da_type == "") {
                errmsg = " Please Choose DA/Coordinator.";
                isValid = false;
            }
            if (isValid) {
                $("#forward_modal").modal("hide");
                $("#statRemarksApp").val(""); // line added by pr on 21stjun18
                $("#approve_modal").modal(); // code replaced with this line by pr on 5thjun18    
            } else {
                showAlertBox(errmsg)
            }
        }
    });
    $("#add_coord_btn").on("click", function () {
        var random = $("#random").val();
        var add_coord = $("#add_coord").val();
        var isValid = true;
        var errmsg = "";
        var choose_da_type_add = $("#choose_da_type_add").val();
        var type = "";
        if (choose_da_type_add == 'da') {
            type = "DA-Admin";
        } else if (choose_da_type_add == 'c') {
            type = "Coordinator";
        }
        if (choose_da_type_add == "") {
            isValid = false;
            errmsg += " Please choose DA/Coordinator";
        } else if (!(choose_da_type_add == "da" || choose_da_type_add == "c")) {
            isValid = false;
            errmsg += " Please choose a valid DA/Coordinator";
        }
        if (add_coord == "") {
            isValid = false;
            errmsg += " Please add a DA/Coordinator";
        }
        if (isValid) {
            var add_coord_btn = $("#add_coord_btn").val();
            bootbox.confirm({
                message: "Do you really wish to Add " + type,
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
                        $("#add_coord_btn").hide();
                        $(".loadergif").show();
                        $(".loader").show();
                        $.post('addCoord', {
                            add_coord: add_coord,
                            regNo: add_coord_btn,
                            random: random,
                            choose_da_type: choose_da_type_add
                        },
                                function (jsonResponse) {
                                    $("#add_coord_btn").show();
                                    $(".loadergif").hide();
                                    $(".loader").hide();
                                    if (jsonResponse.isSuccess == true) {
                                        resetRandom();
                                        $(".alert-danger").show();
                                        $(".alert-danger").html(jsonResponse.msg);
                                        setTimeout(function () {
                                            $(".alert-danger").hide();
                                            $(".alert-danger").html("");
                                        }, 10000); // code added by pr on 20thdec17
                                    } else if (jsonResponse.isError == true) {
                                        resetRandom(); // line added by pr on 4thjan18
                                        $(".alert-danger").hide();
                                        $("#add_coord_error").html(jsonResponse.msg);
                                        setTimeout(function () {
                                            $("#add_coord_error").html("");
                                        }, 10000); // code added by pr on 20thdec17
                                    }
                                }, 'json');
                    }
                }
            });
        } else {
            showAlertBox(errmsg)
        }
    });
    $("#fwd_madmin_btn").on("click", function () { // modified by pr on 28thnov19
        var random = $("#random").val();
        var fwd_madmin = new Array();
        var selectedValues = [];
        $("#fwd_madmin :selected").each(function () {
            selectedValues.push($(this).val());
        });
        fwd_madmin = selectedValues.join();
        var fwd_madmin_btn = $("#fwd_madmin_btn").val();


        // start, code added by pr on 28thnov19

        // ajax call to an action class to check for any previous rejected requests for this applicant email and mobile combination by the admin for this service

        //console.log(" inside approve btn function click regNo value is "+fwd_madmin_btn);

        var arr = fwd_madmin_btn.split("~");

        var reg_no = arr[0];

        var form_name = arr[1];

        var emailMobile = $("#app_" + reg_no).attr("class");

        var emmob_arr = emailMobile.split("~");

        var email = emmob_arr[0];

        var mobile = emmob_arr[1];

        //console.log(" create_email_btn reg_no value is "+reg_no+" emailMobile value is "+emailMobile);

        var newvalue = emailMobile + "~" + form_name; // now the value is replaced with email~mobile~form_name

        //var email_mobile = $(emailMobileElement).attr("class");

        $.post('checkPreviousReject', {
            regNo: newvalue
        },
                function (jsonResponse) {

                    //console.log(" create_email_btn inside checkPreviousReject response prevrejectdata value is "+jsonResponse.prevrejectdata);

                    var table = "";

                    if (jsonResponse.prevrejectdata != null)
                    {
                        table = getPrevRejectTable(jsonResponse.prevrejectdata, email, mobile);
                    }


                    // end, code added by pr on 28thnov19      

                    bootbox.confirm({
                        message: table + "Do you really wish to approve and forward this request to the selected Admin", // table added by pr on 28thnov19
                        size: 'large', // line added by pr on 28thnov19
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
                                $.post('fwdToCoordMadmin', {
                                    fwd_madmin: fwd_madmin,
                                    regNo: fwd_madmin_btn,
                                    random: random
                                },
                                        function (jsonResponse) {
                                            if (jsonResponse.isSuccess == true) {
                                                resetRandom(); // line added by pr on 4thjan18
                                                $(".alert-danger").show();
                                                $(".alert-danger").html(jsonResponse.msg);
                                                showAlertBox(jsonResponse.msg)
                                                $("#forward_modal").modal("toggle");
                                                var action = $("#action_hidden").val();
                                                var table = $('#example').DataTable();
                                                table.ajax.reload(null, false);
                                            } else if (jsonResponse.isError == true) {
                                                resetRandom(); // line added by pr on 4thjan18
                                                $(".alert-danger").hide();
                                                $("#fwd_coord_error").html(jsonResponse.msg);
                                                showAlertBox(jsonResponse.msg)
                                            }
                                        }, 'json');
                            }
                        }
                    });

                }, 'json');  // line added by pr on 28thnov19	

    });
    $("#create_sms_btn").on("click", function () {
        var random = $("#random").val();
        var csrf = $('#CSRFRandom').val();
        var final_sms_id = $("#final_sms_id").val();
        var create_sms_btn = $("#create_sms_btn").val();
        var flag = true,
                errMsg = "";
        var description = "";
        var email_desc1 = $("#sms_desc1").is(":checked");
        var email_desc2 = $("#sms_desc2").is(":checked");
        if (email_desc1) {
            email_desc1 = $("#sms_desc1").val();
            description = email_desc1;
        }
        if (email_desc2) {
            email_desc2 = $("#sms_desc2").val();
            description = email_desc2;
        }
        if ((email_desc1 == null || email_desc1 == "") && (email_desc2 == null || email_desc2 == "")) // if added by pr on 13thmar18
        {
            flag = false;
            errMsg += " Please choose Account Type FREE/PAID ";
        } else if (!(email_desc1 == "free" || email_desc1 == "paid" || email_desc2 == "free" || email_desc2 == "paid")) {
            flag = false;
            errMsg += " Please choose VALID Account Type FREE/PAID ";
        }
        if (final_sms_id == "") {
            flag = false;
            errMsg += " Please enter Final UID";
        }
        if (flag) {

            // start, code added by pr on 28thnov19

            // ajax call to an action class to check for any previous rejected requests for this applicant email and mobile combination by the admin for this service

            //console.log(" inside approve btn function click regNo value is "+create_email_btn);

            var arr = create_sms_btn.split("~");

            var reg_no = arr[0];

            var form_name = arr[1];

            var emailMobile = $("#app_" + reg_no).attr("class");

            var emmob_arr = emailMobile.split("~");

            var email = emmob_arr[0];

            var mobile = emmob_arr[1];

            //console.log(" create_email_btn reg_no value is "+reg_no+" emailMobile value is "+emailMobile);

            var newvalue = emailMobile + "~" + form_name; // now the value is replaced with email~mobile~form_name

            //var email_mobile = $(emailMobileElement).attr("class");

            $.post('checkPreviousReject', {
                regNo: newvalue
            },
                    function (jsonResponse) {

                        //console.log(" create_sms_btn inside checkPreviousReject response prevrejectdata value is "+jsonResponse.prevrejectdata);

                        var table = "";

                        if (jsonResponse.prevrejectdata != null)
                        {
                            table = getPrevRejectTable(jsonResponse.prevrejectdata, email, mobile);
                        }

                        // end, code added by pr on 28thnov19          

                        bootbox.confirm({
                            message: table + "Do you really wish to Create this SMS Auth ID", // table added by pr on 28thnov19
                            size: 'large', // line added by pr on 28thnov19
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
                                    $("#create_sms_btn").hide();
                                    $(".loadergif").show();
                                    $(".loader").show();
                                    $.ajax({url: 'createID', type: 'post', data: {
                                            final_sms_id: final_sms_id,
                                            regNo: create_sms_btn,
                                            description: description,
                                            random: random,
                                            CSRFRandom: csrf
                                        },
                                        success: function (jsonResponse) {

                                            $("#create_sms_btn").show();
                                            $(".loadergif").hide();
                                            $(".loader").hide();

                                            if (jsonResponse.isSuccess == true) {
                                                resetRandom();
                                                showAlertBox(" SMS Auth ID Created Successfully !")

                                                $("#create_modal").modal("toggle");
                                                var action = $("#action_hidden").val();
                                                var table = $('#example').DataTable();
                                                table.ajax.reload(null, false);
                                            } else if (jsonResponse.isError == true) {
                                                alert("1")
                                                resetRandom();
                                                showAlertBox(" SMS Auth ID couldnot be Created " + jsonResponse.msg)
                                                $(".alert-danger").hide();
                                                $("#fwd_coord_error").html(jsonResponse.msg);
                                            }
                                        }, error: function (jsonResponse) {
                                            var json2 = JSON.stringify(jsonResponse);
                                                var jsonvalue = JSON.parse(json2);
                                                var error = JSON.parse(jsonvalue.responseText.substring(0, jsonvalue.responseText.indexOf("}") + 1)).error;
                                                $('#new_alert .modal-body').html(error);
                                                $('#new_alert').modal('show');
                                                $('.loader').hide();
                                        },
                                        datatype: 'json'});
                                }
                            }
                        });

                    }, 'json');  // line added by pr on 28thnov19	


        } else {
            
            showAlertBox(errMsg)
            
        }
    });
    $("#mark_done_btn").on("click", function () {
        var random = $("#random").val();
        var csrf = $('#CSRFRandom').val();
        console.log(" r value is " + random);
        var doneRemarks = $("#doneRemarks").val();
        var mark_done_btn = $("#mark_done_btn").val();
        var wifi_value = "";
        if ($("#wifi_value").length) {
            wifi_value = $("#wifi_value").val();
        }
        {
            var msg = "Do you really wish to Mark as Done";
            var flag = true;
            var isWifiDelete = false;
            if (mark_done_btn.indexOf("wifiport") < -1 && mark_done_btn.indexOf("wifi") > -1) {
                //if (mark_done_btn.indexOf("wifi") > -1) {
                if (mark_done_btn.indexOf("delete") > -1) {
                    isWifiDelete = true;
                    wifi_value = ""; // line added by pr on 18thfeb2020
                } else {
                    if (wifi_value != "") {
                        msg += " and update the Wifi Value to " + wifi_value + "";
                    } else {
                        showAlertBox("Please enter a valid wifi value")
                        flag = false;
                    }
                }
            }
            var imap_value = "";
            if (mark_done_btn.indexOf("imappop") > -1) {
                var isChecked = $("#imap_update_ldap").is(":checked");
                if (isChecked) {
                    imap_value = "n";
                    msg += " and NOT UPDATE in the LDAP.";
                } else {
                    imap_value = "y";
                    msg += " and UPDATE in the LDAP.";
                }
            }
            var location = "";

            if (mark_done_btn.indexOf("centralutm") > -1) {
                var location_arr = [];

                $.each($("input[name='location']:checked"), function () {
                    location_arr.push($(this).val());
                });

                if (location_arr.length == 0) {
                    showAlertBox("Please choose atleast one location")
                    flag = false;
                } else {
                    location = location_arr.join(",");
                }
            }
//         if (mark_done_btn.indexOf("wifiport") > -1) {
//                var location_arr = [];
//
//                $.each($("input[name='location']:checked"), function () {
//                    location_arr.push($(this).val());
//                });
//
//                if (location_arr.length == 0) {
//                    showAlertBox("Please choose atleast one location")
//                    flag = false;
//                } else {
//                    location = location_arr.join(",");
//                }
//            }
            // start, code added by pr on 23rdapr2020
            var dor = "";

            if (mark_done_btn.indexOf("email_act") > -1) {

                dor = $("#single_dor7").val();

                var emp_type = $("#emp_type_hid").val();

                console.log(" emp type is " + emp_type);

                if (emp_type != "emp_regular")
                {
                    if (dor == null || dor == "" || dor == "00-00-0000") {
                        showAlertBox("Please select Valid DOR")
                        flag = false;
                    }
                }
            }
            // end, code added by pr on 23rdapr2020
            //var conf = false;
            if (flag) {
//                conf = confirm(msg);
//            }

//            var conf = confirm(msg);
//
//            if (conf) {
//                $("#mark_done_btn").hide();
//                $(".loadergif").show();
//                $(".loader").show();
//
//                $.post('createID', {
//                        statRemarks: doneRemarks,
//                        regNo: mark_done_btn,
//                        wifi_value: wifi_value,
//                        imap_value: imap_value,
//                        random: random,
//                        isWifiDelete: isWifiDelete,
//                        location: location
//                    },
//                    function(jsonResponse) {
//
//                        $("#mark_done_btn").show();
//                        $(".loadergif").hide();
//                        $(".loader").hide();
//
//                        if (jsonResponse.isSuccess == true) {
//                            resetRandom();
//                            showAlertBox(jsonResponse.msg);
//                            $("#create_modal").modal("toggle");
//                            var action = $("#action_hidden").val();
//                            var table = $('#example').DataTable();
//                            console.log(" just before the datatable reload call ");
//                            table.ajax.reload(null, false);
//                        } else if (jsonResponse.isError == true) {
//                            resetRandom();
//                            $(".alert-danger").hide();
//                            showAlertBox(jsonResponse.msg)
//                            $("#doneRemarks_error").html(jsonResponse.msg);
//                        }
//                    }, 'json');
//            }
                // above code commented below added by pr on 28thnov19


                // start, code added by pr on 28thnov19

                // ajax call to an action class to check for any previous rejected requests for this applicant email and mobile combination by the admin for this service

                //console.log(" inside approve btn function click regNo value is "+mark_done_btn);

                var arr = mark_done_btn.split("~");

                var reg_no = arr[0];

                var form_name = arr[1];

                var emailMobile = $("#app_" + reg_no).attr("class");

                var emmob_arr = emailMobile.split("~");

                var email = emmob_arr[0];

                var mobile = emmob_arr[1];

                //console.log(" mark_done_btn reg_no value is "+reg_no+" emailMobile value is "+emailMobile);

                var newvalue = emailMobile + "~" + form_name; // now the value is replaced with email~mobile~form_name

                //var email_mobile = $(emailMobileElement).attr("class");

                $.post('checkPreviousReject', {
                    regNo: newvalue
                },
                        function (jsonResponse) {

                            //console.log(" create_email_btn inside checkPreviousReject response prevrejectdata value is "+jsonResponse.prevrejectdata);

                            var table = "";

                            if (jsonResponse.prevrejectdata != null)
                            {
                                table = getPrevRejectTable(jsonResponse.prevrejectdata, email, mobile);
                            }


                            // end, code added by pr on 28thnov19

                            bootbox.confirm({
                                message: table + msg, // table added aby pr on 28thnov19
                                size: 'large', // line added by pr on 28thnov19
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

                                        $("#mark_done_btn").hide();
                                        $(".loadergif").show();
                                        $(".loader").show();

                                        $.ajax({url: 'createID', type: 'post', data: {
                                                statRemarks: doneRemarks,
                                                regNo: mark_done_btn,
                                                wifi_value: wifi_value,
                                                imap_value: imap_value,
                                                random: random,
                                                CSRFRandom: csrf,
                                                isWifiDelete: isWifiDelete,
                                                location: location,
                                                dor: dor // line added  by pr on 23rdapr2020
                                            },
                                            success: function(jsonResponse) {

                                                $("#mark_done_btn").show();
                                                $(".loadergif").hide();
                                                $(".loader").hide();

                                                if (jsonResponse.isSuccess == true) {
                                                    resetRandom();
                                                    showAlertBox(jsonResponse.msg);
                                                    $("#create_modal").modal("toggle");
                                                    var action = $("#action_hidden").val();
                                                    var table = $('#example').DataTable();
                                                    //console.log(" just before the datatable reload call ");
                                                    table.ajax.reload(null, false);
                                                } else if (jsonResponse.isError == true) {

                                                    resetRandom();

                                                    $(".alert-danger").hide();
                                                    showAlertBox(jsonResponse.msg)
                                                    $("#doneRemarks_error").html(jsonResponse.msg);
                                                }
                                            }, error: function (jsonResponse) {
                                                var json2 = JSON.stringify(jsonResponse);
                                                var jsonvalue = JSON.parse(json2);
                                                var error = JSON.parse(jsonvalue.responseText.substring(0, jsonvalue.responseText.indexOf("}") + 1)).error;
                                                $('#new_alert .modal-body').html(error);
                                                $('#new_alert').modal('show');
                                                $('.loader').hide();
                                            },
                                            datatype: 'json'});
                                    }
                                }
                            });


                        }, 'json');  // line added by pr on 28thnov19	


            } // flag if close added by pr on 28thnov19

        }
    });
    $("select[name='po']").on("change", function () {
        var form_name = "";
        if ($("#create_email_btn") != null) {
            var value = $("#create_email_btn").val();
            var arr = value.split("~");
            form_name = arr[1];
        }
        var po = $(this).val();
        $.getJSON('fetchBOs', {
            po: po
        },
                function (jsonResponse) {
                    var select = $("select[name='bo']");
                    select.find('option').remove();
                    $('<option>').val("").text("Select BO").appendTo(select);
                    $.each(jsonResponse.bodata, function (index, value) {
                        if (form_name == "nkn_single") {
                            if ((po.trim() == "NIC Support" && value.trim() == "nationalknowledgenetwork") || (po.trim() == "nkn-mailmigration")) {
                                $('<option>').val(value).text(value).appendTo(select);
                            }
                        } else {
                            $('<option>').val(value).text(value).appendTo(select);
                        }
                    });
                });
    });
    $('select[name="bo"]').on("change", function () {
        var po = $("select[name='po']").val();
        var bo = $("select[name='bo']").val();
        $.getJSON('fetchBODomain', {
            po: po,
            bo: bo
        },
                function (jsonResponse) {

                    console.log(" inside fetchdomain response " + jsonResponse);

                    var select = $("select[name='domain']");
                    select.find('option').remove();
                    $('<option>').val("").text("Select Domain").appendTo(select);
                    var emp_type_span = $(".emp_type_span").html();
                    $.each(jsonResponse.domaindata, function (index, value) {
                        if (emp_type_span != "fms_support" && emp_type_span != "emp_contract") // line modified by pr on 6thmay19 
                        {
                            $('<option>').val(value).text(value).appendTo(select);
                        } else {
//                            if (emp_type_span == "fms_support") {
//                                $('<option>').val("supportgov.in").text(value).appendTo(select);
//                            } else if (emp_type_span == "emp_contract") {
//                                $('<option>').val("govcontractor.in").text(value).appendTo(select);
//                            }

                            // above code commented below added by pr on 19thmay2020   
                            if (value == "supportgov.in" || value == "govcontractor.in" || value == "nic.in")
                            {
                                $('<option>').val(value).text(value).appendTo(select);
                            }
                        }
                    });
                });
    });
    $("select[name='bulkpo']").on("change", function () {
        //console.log(" inside bulkpo change function ");
        var form_name = "";
        if ($("#bulk_create_btn") != null) {
            var value = $("#bulk_create_btn").val();
            var arr = value.split("~");
            form_name = arr[1];
            //console.log(" create_email_btn not null  inside bulkpo change create_email_btn btn click " + form_name);
        }
        var po = $(this).val();
        $.getJSON('fetchBOs', {
            po: po
        },
                function (jsonResponse) {
                    var select = $("select[name='bulkbo']");
                    select.find('option').remove();
                    $('<option>').val("").text("Select BO").appendTo(select);
                    $.each(jsonResponse.bodata, function (index, value) {
                        if (form_name == "nkn_bulk") {
                            if ((po.trim() == "NIC Support" && value.trim() == "nationalknowledgenetwork") || (po.trim() == "nkn-mailmigration")) {
                                $('<option>').val(value).text(value).appendTo(select);
                            }
                        } else // if and else around added by pr on 30thoct18
                        {
                            $('<option>').val(value).text(value).appendTo(select);
                        }
                    });
                });
    });
    $('select[name="bulkbo"]').on("change", function () {
        var po = $("select[name='bulkpo']").val();
        var bo = $("select[name='bulkbo']").val();
        $.getJSON('fetchBODomain', {
            po: po,
            bo: bo
        },
                function (jsonResponse) {
                    var select = $("select[name='bulkdomain']");
                    select.find('option').remove();
                    $('<option>').val("").text("Select Domain").appendTo(select);
                    var emp_type_span = $(".emp_type_span").html();
                    $.each(jsonResponse.domaindata, function (index, value) {
                        if (emp_type_span != "fms_support" && emp_type_span != "emp_contract") // line modified by pr on 6thmay19
                        {
                            $('<option>').val(value).text(value).appendTo(select);
                        } else {
                            //if (value == "nic.in") {
                            if (value == "supportgov.in" || value == "govcontractor.in" || value == "nic.in")// line modified by pr on 24thfeb2020 
                            {
                                $('<option>').val(value).text(value).appendTo(select);
                            }
                        }
                    });
                });
    });
    $("#create_email_btn").on("click", function () { // modified by pr on 28thnov19
        var random = $("#random").val();
        var csrf = $('#CSRFRandom').val();

        var po = $("#po").val();
        var bo = $("#bo").val();
        var domain = $("#domain").val();
        var final_sms_id = $("#final_email_id").val();
        var create_email_btn = $("#create_email_btn").val();
        var flag = true;
        var errMsg = "";
        var description = "";
        var email_desc1 = $("#email_desc1").is(":checked");
        var email_desc2 = $("#email_desc2").is(":checked");
        if (email_desc1) {
            email_desc1 = $("#email_desc1").val();
            description = email_desc1;
        }
        if (email_desc2) {
            email_desc2 = $("#email_desc2").val();
            description = email_desc2;
        }
        if ((email_desc1 == null || email_desc1 == "") && (email_desc2 == null || email_desc2 == "")) // if added by pr on 13thmar18
        {
            flag = false;
            errMsg += " Please choose Account Type FREE/PAID ";
        } else if (!(email_desc1 == "free" || email_desc1 == "paid" || email_desc2 == "free" || email_desc2 == "paid")) {
            flag = false;
            errMsg += " Please choose VALID Account Type FREE/PAID ";
        }
        if (po == null || po == "") {
            flag = false;
            errMsg += " Please select PO ";
        }
        if (bo == null || bo == "") {
            flag = false;
            errMsg += " Please select BO ";
        }
        if (domain == null || domain == "") {
            flag = false;
            errMsg += " Please select Domain ";
        }
        if (final_sms_id == null || final_sms_id == "") {
            flag = false;
            errMsg += " Please enter Final ID ";
        }
        var primary_id = "";
        var alias_id = "";


        // start, code added by pr on 3rdoct19

        var statRemarks = $(".statRemarks").val();

        //console.log(" inside create email id button clicked statremarks are "+statRemarks);

        // end, code added by pr on 3rdoct19


        if ($("#primary_id") != null && $("#primary_id").val() == "") {
            flag = false;
            errMsg += " Please enter Prmary ID ";
        } else {
            primary_id = $("#primary_id").val();
        }
        if (flag) {

            // start, code added by pr on 28thnov19

            // ajax call to an action class to check for any previous rejected requests for this applicant email and mobile combination by the admin for this service

            //console.log(" inside approve btn function click regNo value is "+create_email_btn);

            var arr = create_email_btn.split("~");

            var reg_no = arr[0];

            var form_name = arr[1];

            var emailMobile = $("#app_" + reg_no).attr("class");

            var emmob_arr = emailMobile.split("~");

            var email = emmob_arr[0];

            var mobile = emmob_arr[1];

            //console.log(" create_email_btn reg_no value is "+reg_no+" emailMobile value is "+emailMobile);

            var newvalue = emailMobile + "~" + form_name; // now the value is replaced with email~mobile~form_name

            //var email_mobile = $(emailMobileElement).attr("class");

            $.post('checkPreviousReject', {
                regNo: newvalue
            },
                    function (jsonResponse) {

                        //console.log(" create_email_btn inside checkPreviousReject response prevrejectdata value is "+jsonResponse.prevrejectdata);

                        var table = "";

                        if (jsonResponse.prevrejectdata != null)
                        {
                            table = getPrevRejectTable(jsonResponse.prevrejectdata, email, mobile);
                        }


                        // end, code added by pr on 28thnov19

                        bootbox.confirm({
                            message: table + "Do you really wish to Create Email ID", // table added by pr on 28thnov19
                            size: 'large', // line added by pr on 28thnov19
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
                                    $("#create_email_btn").hide();
                                    $(".loadergif").show();
                                    $(".loader").show();

                                    $.ajax({url: 'createID', type: 'post', data: {
                                            po: po,
                                            bo: bo,
                                            domain: domain,
                                            final_sms_id: final_sms_id,
                                            primary_id: primary_id, // this and below line added by pr on 19thfeb18
                                            regNo: create_email_btn,
                                            description: description, // line added by pr on 13thmar18
                                            random: random,
                                            CSRFRandom: csrf,
                                            statRemarks: statRemarks // line added by pr on 3rdoct19
                                        },
                                       success: function(jsonResponse) {

                                            $("#create_email_btn").show();
                                            $(".loadergif").hide();
                                            $(".loader").hide();

                                            if (jsonResponse.isSuccess == true) {
                                                resetRandom();
                                                showAlertBox(" Email ID created Successfully");
                                                $("#create_modal").modal("toggle");
                                                var action = $("#action_hidden").val();
                                                var table = $('#example').DataTable();
                                                //console.log(" just before the datatable reload call ");
                                                table.ajax.reload(null, false);
                                            } else if (jsonResponse.isError == true) {
                                                resetRandom(); // line added by pr on 4thjan18
                                                $(".alert-danger").hide();
                                                showAlertBox(jsonResponse.msg)
                                            }
                                        }, error: function (jsonResponse) {
                                            var json2 = JSON.stringify(jsonResponse);
                                                var jsonvalue = JSON.parse(json2);
                                                var error = JSON.parse(jsonvalue.responseText.substring(0, jsonvalue.responseText.indexOf("}") + 1)).error;
                                                $('#new_alert .modal-body').html(error);
                                                $('#new_alert').modal('show');
                                                $('.loader').hide();
                                        },
                                        datatype: 'json'});
                                }
                            }
                        });

                    }, 'json');  // line added by pr on 28thnov19

        } else {
            
            showAlertBox(errMsg)
            
        }
    });
    // check all for bulk users, below code modified by pr on 19thjul19
    $(document).on('click', '#check_all , .check_all', function ()
    {

        // console.log(" inside .checkall click ");

        var count = 1;  // line added by pr on 18thapr18

        $("input[name='bulk_id']").each(function ()
        {
            var element = $(this);

            //if ($("#check_all").is(":checked"))
            if ($('#check_all , .check_all').is(":checked")) // line modified by pr on 19thjul19
            {
                if (count < 100) // if added by pr on 18thapr18
                {
                    element.prop("checked", true);
                }

                count++; // line added by pr on 18thapr18

            } else
            {
                element.prop("checked", false);
            }



        });

        checkNumChecked();

    });


    function checkNumChecked() // function to check the number of checkboxes clicked to create ids of in case of bulk users
    {
        var checkCount = 0;
        $("input[name='bulk_id']").each(function () {
            var element = $(this);
            if ($(this).is(":checked")) {
                checkCount++;
            }
        });
        var limit = 100; // line modified by pr on 27thfeb18
        if (checkCount > limit) {
            //alert(" Please check " + limit + " or less checkboxes to create IDs for. ");
            showAlertBox(" Please check " + limit + " or less checkboxes to create IDs for. ")
            $("#bulk_create_btn").hide();
        } else {
            $("#bulk_create_btn").show();
        }
    }
    $("#bulk_create_btn").on("click", function () {
        var random = $("#random").val();
        var csrf = $('#CSRFRandom').val();
        var regNo = $(this).val();
        var bulk_id = new Array();
        i = 0;
        $("input[name='bulk_id']").each(function () {
            if ($(this).is(":checked")) {
                bulk_id[i] = $(this).val();
                i++;
            }
        });
        bulk_id = bulk_id.join();
        var flag = true;
        var errMsg = "";
        var po = $("select[name='bulkpo']").val();
        var bo = $("select[name='bulkbo']").val();
        var domain = $("select[name='bulkdomain']").val();
        var description = "";
        var email_desc1 = $("#bulk_desc1").is(":checked");
        var email_desc2 = $("#bulk_desc2").is(":checked");
        if (email_desc1) {
            email_desc1 = $("#bulk_desc1").val();
            description = email_desc1;
        }
        if (email_desc2) {
            email_desc2 = $("#bulk_desc2").val();
            description = email_desc2;
        }
        if ((email_desc1 == null || email_desc1 == "") && (email_desc2 == null || email_desc2 == "")) // if added by pr on 13thmar18
        {
            flag = false;
            errMsg += " Please choose Account Type FREE/PAID ";
        } else if (!(email_desc1 == "free" || email_desc1 == "paid" || email_desc2 == "free" || email_desc2 == "paid")) {
            flag = false;
            errMsg += " Please choose VALID Account Type FREE/PAID ";
        }
        if (po == null || po == "") {
            flag = false;
            errMsg += " Please select PO ";
        }
        if (bo == null || bo == "") {
            flag = false;
            errMsg += " Please select BO ";
        }
        if (domain == null || domain == "") {
            flag = false;
            errMsg += " Please select Domain ";
        }
        if (bulk_id == "" || bulk_id == null) {
            flag = false;
            errMsg += " Please check atleast one checkbox";
        }

        // start, code added by pr on 3rdoct19

        var statRemarks = $(".statRemarksBulk").val();

        //console.log(" inside create email id button clicked statremarks are "+statRemarks);

        // end, code added by pr on 3rdoct19


        if (flag) {
            bootbox.confirm({
                message: "Do you really wish to Create Bulk IDs ",
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

                        $("#bulk_create_btn").hide();
                        $(".loadergif").show();
                        $(".loader").show();

                        $.post('createBulkID', {
                            bulk_id: bulk_id,
                            po: po,
                            bo: bo,
                            domain: domain,
                            regNo: regNo,
                            description: description,
                            random: random,
                            CSRFRandom: csrf,
                            statRemarks: statRemarks // line added by pr on 3rdoct19
                        },
                                function (jsonResponse) {
                                    $("#bulk_create_btn").show();
                                    $(".loadergif").hide();
                                    $(".loader").hide();
                                    resetRandom();
                                    showAlertBox(" Bulk ID Creation Request Processed Successfully ! ")
                                    var bulkUpdateData = jsonResponse.bulkUpdateData;
                                    var value_arr = "",
                                            remarks = "",
                                            showCheckBox = "";
                                    var sub = "",
                                            showMore = "";
                                    var toggleBulk = jsonResponse.toggleBulk;
                                    if (!toggleBulk) // if around and else added by pr on 26thdec18
                                    {
                                        // console.log(" toggleBulk value is " + toggleBulk);
                                        $.each(jsonResponse.bulkUpdateData, function (index, value) {
                                            // console.log(" index is " + index + " value is " + value);
                                            value_arr = value.split("~");
                                            if (value_arr != null && value_arr.length == 2) {
                                                //console.log(" inside value arr not null ");
                                                remarks = value_arr[0];
                                                showCheckBox = value_arr[1];
                                                if (remarks.trim().length >= 10) {
                                                    sub = remarks.substring(0, 9);
                                                    showMore = '<span class="moreb" id="moreb_' + index + '" style="color:blue;cursor:pointer;"> ' + sub + ' more...</span><span id="complete_' + index + '" class="complete" style="display:none;">' + remarks + '</span>';
                                                } else if (remarks.trim() == "none") {
                                                    sub = "";
                                                    showMore = sub;
                                                } else {
                                                    sub = remarks;
                                                    showMore = sub;
                                                }
                                                $("#tr_" + index + " .reject_remarks").html(showMore); // get the class inside this particular tr id
                                                if (showCheckBox != "") {
                                                    // console.log(" inside show check box not null ");
                                                    if (showCheckBox == "n") {
                                                        //$("#tr_"+index).hide(); 
                                                        $("#tr_" + index).remove();
                                                    }
                                                }
                                            }
                                        });
                                        // end, added by pr on 24thdec18
                                        var action = $("#action_hidden").val();
                                        if (jsonResponse.mapQuota !== undefined && jsonResponse.mapQuota) {
                                            $.each(jsonResponse.mapQuota, function (index, value) {
                                                $("#tr_" + index + " .reject_remarks").html('<text class="text-danger">' + value + '</text>');
                                            });
                                        }
                                        //setAction(action)  // line commented by pr on 23rdoct18
                                        // end, code added by pr on 25thmay18

                                    } else {
                                        $("#bulk_modal").modal("toggle");
                                    }
                                    // start, code added by pr on 23rdoct18
                                    var table = $('#example').DataTable();
                                    // console.log(" just before the datatable reload call ");
                                    table.ajax.reload(null, false);
                                    // end, code added by pr on 23rdoct18
                                }, 'json');
                    }
                }
            });
        } else {
            //alert(errMsg);
            showAlertBox(errMsg)
        }
    });
});
$(document).on('change', '#choose_da_type', function () {
    var select = $('#fwd_coord');
    select.find('option').remove();
    var val = $("#choose_da_type").val();
    var value = "";
    if ($("#fwd_coord_btn") != null && $("#fwd_coord_btn") != "undefined" && $("#fwd_coord_btn").val() != "") // if added by pr on 16thmar18
    {
        value = $("#fwd_coord_btn").val();
    }
    if (val == "da") {
        $.getJSON('fetchDAs', {
            regNo: value
        },
                function (jsonResponse) {
                    var select = $('#fwd_coord');
                    select.find('option').remove();
                    $('<option>').val("").text("Select DA-Admin").appendTo(select);
                    $.each(jsonResponse.dadata, function (index, value) {
                        //console.log(" da data loop "+value+index);
                        $('<option>').val(value).text(value).appendTo(select);
                    });
                    var empDet = "";
                    $.each(jsonResponse.empdata, function (index, value) {
                        if (value != "" && index == "employment") {
                            empDet += " Employment: " + value + "<br>";
                        } else if (value != "" && index == "ministry") {
                            empDet += " Ministry: " + value + "<br>";
                        } else if (value != "" && index == "department") {
                            empDet += " Department: " + value + "<br>";
                        } else if (value != "" && index == "state") {
                            empDet += " State: " + value + "<br>";
                        } else if (value != "" && index == "organization") {
                            empDet += " Organization: " + value + "<br>";
                        } else if (value != "" && index == "other_dept") {
                            empDet += " Other: " + value + "<br>";
                        }
                    });
                    if (empDet != "") {
                        //console.log(empDet)
                        //$("#ajaxEmpDetails").html(empDet);
                        $(".ajaxEmpDetails").html(empDet); // line modified by pr on 20thmar18
                    }
                });
    } else if (val == "c") {
        // console.log(" inside coordinator on change of dat type ")
        $.getJSON('fetchCoordinator', {
            regNo: value
        },
                function (jsonResponse) {
                    console.log(jsonResponse)

                    //  console.log(" inside json response for coorsds  jsonResponse.coorddata  "+jsonResponse.coorddata);
                    var select = $('#fwd_coord');
                    select.find('option').remove();
                    //$('<option>').val("").text("Select Coordinator").appendTo(select); // commented by pr on 23rdmar18
                    $.each(jsonResponse.coorddata, function (index, value) {
                        //  console.log("  coorddata loop "+value+index);
                        $('<option>').val(value).text(value).appendTo(select);
                    });
                    var empDet = "";
                    $.each(jsonResponse.empdata, function (index, value) {
                        if (value != "" && index == "employment") {
                            empDet += " Employment: " + value + "<br>";
                        } else if (value != "" && index == "ministry") {
                            empDet += " Ministry: " + value + "<br>";
                        } else if (value != "" && index == "department") {
                            empDet += " Department: " + value + "<br>";
                        } else if (value != "" && index == "state") {
                            empDet += " State: " + value + "<br>";
                        } else if (value != "" && index == "organization") {
                            empDet += " Organization: " + value + "<br>";
                        } else if (value != "" && index == "other_dept") {
                            empDet += " Other: " + value + "<br>";
                        }
                    });
                    if (empDet != "") {
                        // console.log(empDet)
                        $(".ajaxEmpDetails").html(empDet); // line modified by pr on 20thmar18
                    }
                });
    }
});

function clearData() {
    var element = $("#add_coord");
    if (element != null) {
        element.val("");
    }
    element = $("#fwd_coord");
    if (element != null) {
        element.val("");
    }
    // below code added by pr on 7thmar18
    element = $("#add_da");
    if (element != null) {
        element.val("");
    }
    element = $("#fwd_da");
    if (element != null) {
        element.val("");
    }
    // start, code added by pr on 12thmar18
    element = $("#choose_da_type");
    if (element != null) {
        element.val("");
    }
    element = $("#choose_da_type_add");
    if (element != null) {
        element.val("");
    }
    element = $("#fwd_da");
    if (element != null) {
        element.val("");
    }
    element = $("#da_from_admin");
    if (element != null) {
        element.val("");
    }
    // end, code added by pr on 6thjun18
}
// below function added by pr on 9thapr18
function prefillQuery() {
    $('.loader').show();
    var csrf = $('#CSRFRandom').val();
    // clear the previous values
    $("#choose_recp").val("");
    $("#query").val("");
    var role = $("#role").val();
    //console.log(" role value is "+role);
    var value = $("#query_btn").val();
    // console.log(" query_btn value is  "+value+" role value is "+role);
    var reg_no = "",
            form_name = "";
    if (value != "") {
        var reg_no_arr = value.split("~");
        if (reg_no_arr[0] != null) {
            reg_no = reg_no_arr[0];
        }
        if (reg_no_arr[1] != null) {
            form_name = reg_no_arr[1];
        }
    }
    //console.log('calling 3 times.');
    $.ajax({
        type: "POST",
        url: "fetchRecps",
        data: {
            regNo: value, CSRFRandom: csrf
        },
        datatype: JSON,
        success: function (jsonResponse) {
            if (jsonResponse.csrf_error) {
                alert(jsonResponse.csrf_error);
            }
            var select = $('#choose_recp');
            select.find('option').remove();
            $('<option>').val("").text("Select Recipient").appendTo(select);
            if (role != "user") {
                var app_email = $("#" + reg_no + "_email").html();
                $('<option>').val("u").text("Applicant(" + app_email + ")").appendTo(select);
            }
            $.each(jsonResponse.recpdata, function (index, value) {
                var val = "";
                if (value == "ca" && role != "ca") {
                    val = "Reporting/Nodal/Forwarding Officer";
                } else if (value == "c" && role != "co") {
                    val = "Coordinator";
                } else if (value == "s" && role != "sup") {
                    val = "Support";
                } else if (value == "d") {
                    val = "DA-Admin";
                } else if (value == "m" && role != "admin") {
                    val = "Admin";
                }
                if (val != "") {
                    $('<option>').val(value).text(val).appendTo(select);
                }
            });
            // below code shows the table showing the history
            var str_head = "",
                    str = "";
            str_head = ` <tr>
                        <th> S.No </th>
                        <th> By </th>
                        <th> To </th>
                        <th> Message </th>
                        <th> Date </th>
                        </tr>`;
            var i = 1;
            $.each(jsonResponse.querydata, function (index, value) {
                // console.log(value);
                var forwarded_by_str = "",
                        forwarded_to_str = "";
                if (value.qr_forwarded_by == "ca" || value.qr_forwarded_to == "ca") {
                    if (value.qr_forwarded_by == "ca") {
                        forwarded_by_str = "Reporting/Nodal/Forwarding Officer";
                    }
                    if (value.qr_forwarded_to == "ca") {
                        forwarded_to_str = "Reporting/Nodal/Forwarding Officer";
                    }
                }
                if (value.qr_forwarded_by == "s" || value.qr_forwarded_to == "s") {
                    if (value.qr_forwarded_by == "s") {
                        forwarded_by_str = "Support";
                    }
                    if (value.qr_forwarded_to == "s") {
                        forwarded_to_str = "Support";
                    }
                }
                if (value.qr_forwarded_by == "c" || value.qr_forwarded_to == "c") {
                    // console.log(" inside 3 ");
                    if (value.qr_forwarded_by == "c") {
                        forwarded_by_str = "Coordinator";
                    }
                    if (value.qr_forwarded_to == "c") {
                        forwarded_to_str = "Coordinator";
                    }
                }
                if (value.qr_forwarded_by == "d" || value.qr_forwarded_to == "d") {
                    // console.log(" inside 4 ");
                    if (value.qr_forwarded_by == "d") {
                        forwarded_by_str = "DA-Admin";
                    }
                    if (value.qr_forwarded_to == "d") {
                        forwarded_to_str = "DA-Admin";
                    }
                }
                if (value.qr_forwarded_by == "m" || value.qr_forwarded_to == "m") {
                    //  console.log(" inside 5 ");
                    if (value.qr_forwarded_by == "m") {
                        forwarded_by_str = "Admin";
                    }
                    if (value.qr_forwarded_to == "m") {
                        forwarded_to_str = "Admin";
                    }
                }
                if (value.qr_forwarded_by == "u" || value.qr_forwarded_to == "u") {
                    if (value.qr_forwarded_by == "u") {
                        forwarded_by_str = "Applicant";
                    }
                    if (value.qr_forwarded_to == "u") {
                        forwarded_to_str = "Applicant";
                    }
                }
                if (value.qr_forwarded_by_user != "") {
                    forwarded_by_str += "( " + value.qr_forwarded_by_user + " )";
                }
                if (value.qr_forwarded_to_user != "") {
                    forwarded_to_str += "( " + value.qr_forwarded_to_user + " )";
                }
                var sub = value.qr_message.substring(0, 10);
                var showMore = '<span class="more" style="color:blue;cursor:pointer;"> ' + sub + ' more...</span><span class="complete" style="display:none;">' + value.qr_message + '</span>';
                var time_format = dateFormat(value.qr_createdon, "h:MM TT | mmm dd, yyyy");
                var div_reply = 'my-side';
                var div_color = '';
                if (!value.qr_accountholder) {
                    div_reply = "div-reply";
                    div_color = value.qr_forwarded_by + "-div";
                }
                var role = jsonResponse.role;
                if (role == "user") {
                    role = "u";
                } else if (role == "admin") {
                    role = "m";
                } else if (role == "support") {
                    role = "s";
                } else if (role == "coordinator") {
                    role = "c";
                } else if (role == "ca") {
                    role = "ca";
                }

                if ((jsonResponse.statusdata == "rejected") && (value.qr_forwarded_by_user == jsonResponse.fetchSessionEmail) && (value.qr_forwarded_by == role)) {
                    //alert("*****1*********");
                    str += '<div class="row chat-view ' + div_reply + '"><div class="img-div col-md-1 col-sm-2 col-2"><img src="assets/media/users/300_21.jpg"/></div><div class="col-8 ' + div_color + '"><span class="detail-data-div-by"><b>By: </b>' + forwarded_by_str + '<span class="time-data">' + time_format + '</span></span>' + value.qr_message + '<span class="detail-data-div"><b>To: </b>' + forwarded_to_str + '</span></div></div>';
                    //alert("*****2*********");
                } else if ((jsonResponse.statusdata == "rejected") && (value.qr_forwarded_to_user == jsonResponse.fetchSessionEmail) && (value.qr_forwarded_to == role)) {
                    //alert("*****3*********"); 
                    str += '<div class="row chat-view ' + div_reply + '"><div class="img-div col-md-1 col-sm-2 col-2"><img src="assets/media/users/300_21.jpg"/></div><div class="col-8 ' + div_color + '"><span class="detail-data-div-by"><b>By: </b>' + forwarded_by_str + '<span class="time-data">' + time_format + '</span></span>' + value.qr_message + '<span class="detail-data-div"><b>To: </b>' + forwarded_to_str + '</span></div></div>';
                    //str +='';
                    //alert("*****4*********");
                } else if ((jsonResponse.statusdata == "rejected") && (value.qr_forwarded_by_user != jsonResponse.fetchSessionEmail) && (value.qr_forwarded_by != role)) {
                    //alert("*****9*********");
                    str += '';
                    //  str += '<div class="row chat-view ' + div_reply + '"><div class="img-div col-md-1 col-sm-2 col-2"><img src="assets/media/users/300_21.jpg"/></div><div class="col-8 ' + div_color + '"><span class="detail-data-div-by"><b>By: </b>' + forwarded_by_str + '<span class="time-data">' + time_format + '</span></span>' + value.qr_message + '<span class="detail-data-div"><b>To: </b>' + forwarded_to_str + '</span></div></div>';
                    //alert("*****10*********");
                } else if ((jsonResponse.statusdata == "rejected") && (value.qr_forwarded_to_user != jsonResponse.fetchSessionEmail) && (value.qr_forwarded_to != role)) {
                    //alert("*****5*********");
                    str += '<div class="row chat-view ' + div_reply + '"><div class="img-div col-md-1 col-sm-2 col-2"><img src="assets/media/users/300_21.jpg"/></div><div class="col-8 ' + div_color + '"><span class="detail-data-div-by"><b>By: </b>' + forwarded_by_str + '<span class="time-data">' + time_format + '</span></span>' + value.qr_message + '<span class="detail-data-div"><b>To: </b>' + forwarded_to_str + '</span></div></div>';
                    //alert("*****6*********");
                } else {
                    //alert("*****7*********");
                    str += '<div class="row chat-view ' + div_reply + '"><div class="img-div col-md-1 col-sm-2 col-2"><img src="assets/media/users/300_21.jpg"/></div><div class="col-8 ' + div_color + '"><span class="detail-data-div-by"><b>By: </b>' + forwarded_by_str + '<span class="time-data">' + time_format + '</span></span>' + value.qr_message + '<span class="detail-data-div"><b>To: </b>' + forwarded_to_str + '</span></div></div>';
                    //alert("*****8*********");
                }
                // str += '<div class="row chat-view ' + div_reply + '"><div class="img-div col-md-1 col-sm-2 col-2"><img src="assets/media/users/300_21.jpg"/></div><div class="col-8 ' + div_color + '"><span class="detail-data-div-by"><b>By: </b>' + forwarded_by_str + '<span class="time-data">' + time_format + '</span></span>' + value.qr_message + '<span class="detail-data-div"><b>To: </b>' + forwarded_to_str + '</span></div></div>';
                i++;
            });
            if (i > 1) {
                $('.color-indicator').show();
            } else {
                $('.color-indicator').hide();
            }
            $("#query_table").html(str);
            $("#example_query").dataTable();
            // console.log(" raiseQueryBtn value is "+jsonResponse.raiseQueryBtn);
            if (jsonResponse.raiseQueryBtn) // show raise/respond options
            {
                $("#showQueryAdd").show();
            } else // hide raise/respond options
            {
                $("#showQueryAdd").hide();
            }
        },
        error: function () {
            console.log("error")
        },
        complete: function () {
            $('.loader').hide();
        }
    });
}
// below code added by pr on 10thapr18
$(document).on('click', '.more', function () {
    $parent_box = $(this).closest('.togl');
    $parent_box.siblings().find('.complete').hide();
    $parent_box.find('.complete').toggle();
});
// below function added by pr on 29thmay18
$(document).on('click', '.moreb', function () {
    var more_id = $(this).attr("id");
    var bulk_id = more_id.replace("moreb_", "");
    var complete_id = "complete_" + bulk_id;
    $parent_box = $(this).closest('.togl');
    // console.log($parent_box);
    $("[id*='complete_']").hide();
    $("[id*='moreb_']").show();
    $.each($("[id*='moreb_']"), function () {
        var id = $(this).attr("id");
        var bul_id = id.replace("moreb_", "");
        if ($("#complete_" + bul_id).html() == "undefined" || $("#complete_" + bul_id).html() == undefined || $("#complete_" + bul_id).html() == "") {
            $("#moreb_" + bul_id).html(""); // make it empty                
        }
    });
    $(this).hide();
    $parent_box.find("#" + complete_id).toggle();
});

function prefillData() {
    var role = $("#role").val();
    $.each($("input[name='location']"), function () {
        $(this).prop("checked", false);
    });
    //console.log("ROLE : " + role);
    var value = "";
    if ($("#fwd_wo_ad").val() != "") {
        value = $("#fwd_wo_ad").val();
    } else if ($("#create_email_btn").val() != "") {
        value = $("#create_email_btn").val();
    } else if ($("#mark_done_btn").val() != "") {
        value = $("#mark_done_btn").val();
    }
    //console.log(" inside prefilldata function regno + formname + action value is " + value);
    var reg_no = "",
            form_name = "";
    if (value != "") {
        var reg_no_arr = value.split("~");
        if (reg_no_arr[0] != null) {
            reg_no = reg_no_arr[0];
        }
        if (reg_no_arr[1] != null) {
            form_name = reg_no_arr[1];
        }
    }
    //console.log(" inside prefildata form_name value is " + form_name);
    if (role == 'sup') // then set forward to coordinator dropdown and employment details to show to add coordinator
    {
        $("#empty-coordlist").hide();
        $("#empty-coordlist").html('');
        // start, code added by pr on 23rdmar18
        if (form_name == 'sms') {
            // remove the choose_da_type da type 
            var select = $('#choose_da_type');
            select.find('option').remove(); // uncommented by pr on 26thmar18
            $('<option>').val("").text("--Select--").appendTo(select); // line added by pr on 26thmar18		
            $('<option>').val("c").text("Coordinator").appendTo(select);
        } else {
            var select = $('#choose_da_type');
            select.find('option').remove(); // uncommented by pr on 26thmar18
            $('<option>').val("").text("--Select--").appendTo(select); // line added by pr on 26thmar18
            $('<option>').val("da").text("DA-Admin").appendTo(select);
            $('<option>').val("c").text("Coordinator").appendTo(select);
        }
        // end, code added by pr on 23rdmar18
        $.getJSON('fetchCoordinator', {
            regNo: value
        },
                function (jsonResponse) {
                    if (jsonResponse.isEmptyCoordList) {
                        $("#empty-coordlist").show();
                        $("#empty-coordlist").html('There are no explicit coordinators for this department, you may use any one from below:-');
                    }
                    var select = $('#fwd_coord');
                    select.find('option').remove();
                    //$('<option>').val("").text("Select Coordinator").appendTo(select); // commented by pr on 23rdmar18
                    $.each(jsonResponse.coorddata, function (index, value) {
                        $('<option>').val(value).text(value).appendTo(select);
                    });
                    var empDet = "";
                    $.each(jsonResponse.empdata, function (index, value) {
                        if (value != "" && index == "employment") {
                            empDet += " Employment: " + value + "<br>";
                        } else if (value != "" && index == "ministry") {
                            empDet += " Ministry: " + value + "<br>";
                        } else if (value != "" && index == "department") {
                            empDet += " Department: " + value + "<br>";
                        } else if (value != "" && index == "state") {
                            empDet += " State: " + value + "<br>";
                        } else if (value != "" && index == "organization") {
                            empDet += " Organization: " + value + "<br>";
                        } else if (value != "" && index == "other_dept") {
                            empDet += " Other: " + value + "<br>";
                        }
                    });
                    if (empDet != "") {
                        //console.log(empDet)
                        $(".ajaxEmpDetails").html(empDet); // line modified by pr on 20thmar18
                    }
                });
    } else if (role == 'co') // if coordinator then prefill the mailadmin dropdown
    {
        if (form_name != "dns") // for dns and vpn form no need to show dropdwon for mailadmins 
        {
            $.getJSON('fetchAdmins', {
                regNo: value
            },
                    function (jsonResponse) {
                        var select = $('#fwd_madmin');
                        select.find('option').remove();
                        $('<option>').val("").text("Select Admin").appendTo(select);
                        $.each(jsonResponse.madmindata, function (index, value) {
                            $('<option>').val(value).text(value).appendTo(select);
                        });
                    });
        }
    } else if (role == 'admin') {
        // start, code added by pr on 21stjun19
        $("#wifi_mad").hide();
        $("#imap_mad").hide();
        $("#firewall_mad").hide();
        $("#wifiport_mad").hide();
        $("#single_emp_type").hide();
        // start, code added by pr on 15thoct19
        $("#imap_mad").hide();
        $("#wifi_select").hide();
        $("#emailact_mad").hide(); // line added by pr on 23rdapr2020
        // end, code added by pr on 15thoct19
        if (form_name == "nkn_single") {
            $.getJSON('fetchPOs', {},
                    function (jsonResponse) {
                        //console.log("JSON RESPONSE : " + jsonResponse)
                        var select = $('#po');
                        select.find('option').remove();
                        $('<option>').val("").text("Select PO").appendTo(select);
                        $.each(jsonResponse.podata, function (index, value) {
                            if (value.trim() == "NIC Support" || value.trim() == "nkn-mailmigration") {
                                $('<option>').val(value).text(value).appendTo(select);
                            }
                        });
                        var select = $('#bo');
                        select.find('option').remove();
                        var select = $('#domain');
                        select.find('option').remove();
                    });
            $.post('fetchEmpType', {
                regNo: value
            },
                    function (jsonResponse) {
                        // console.log("JSON RESPONSE : " + jsonResponse)
                        var empType = jsonResponse.emp_type;
                        var prefEmail = jsonResponse.prefEmail;
                        //console.log(" inside getemptype response emptype is " + empType + " prefEmail value is " + prefEmail);
                        $("#emp_type_hid").val(empType);
                        $(".emp_type_span").html(empType);
                        $("#pref_email_span").html(prefEmail);
                    }, 'json');
        } else if (form_name.trim() == "wifi") {
            var showVPNBtn = false;
            $.getJSON('fetchwifivalue', {
                regNo: value
            },
                    function (jsonResponse) {
                        //console.log(" inside json response " + jsonResponse.wifi_value);
                        // start, code added by pr on 29thnov18 for wifi                       
                        var wifi_value = jsonResponse.wifi_value;

                        var ldap_wifi_value = jsonResponse.ldap_wifi_value; // line added by pr on 2ndjan2020

                        var wifi_process = "";
                        var cert_mail_sent = "";
                        var isIOS = "No"; // line added by pr on 4thdec18
                        if (jsonResponse.wifi_value.indexOf("~") >= 0) {
                            var arr = jsonResponse.wifi_value.split("~");
                            if (arr.length >= 1 && arr[0] != null) {
                                wifi_value = arr[0];
                            }
                            if (arr.length >= 2 && arr[1] != null) {
                                if (arr[1] == "certificate") {
                                    showVPNBtn = true;
                                }
                                wifi_process = arr[1];
                            }
                            if (arr.length >= 3 && arr[2] != null) // isIOS
                            {
                                if (arr[1] == "request" && arr[2] == "y") {
                                    showVPNBtn = true;
                                }
                                if (arr[2] == "y") {
                                    arr[2] = "Yes";
                                    isIOS = "Yes"; // line added by pr on 4thdec18
                                } else {
                                    arr[2] = "No";
                                    isIOS = "No"; // line added by pr on 4thdec18
                                }
                            }
                            if (arr.length >= 4 && arr[3] != null) // isMailSent
                            {
                                cert_mail_sent = arr[3];
                                if (cert_mail_sent == "y") {
                                    cert_mail_sent = "Yes";
                                    cert_mail_sent = "<span style='color:green;font-weight:bold;'>Yes</span>";
                                } else {
                                    cert_mail_sent = "No";
                                    cert_mail_sent = "<span style='color:red;font-weight:bold;'>No</span>";
                                }
                            }
                        }
                        if (wifi_process != "req_delete") {
                            //console.log(" inside wifi process not delete ");
                            var iosStr = "";
                            if (wifi_process == "request") {
                                iosStr = "<br><br>IS OS AN IOS: <span style='font-weight:bold;'>" + isIOS + "</span>";
                            }
                            $("#wifi_process").html("Type of Request: <span style='font-weight:bold;'>" + wifi_process.toUpperCase() + "</span>" + iosStr); // line modified by pr on 4thdec18
                            $("#cert_mail_sent").html("Mail sent to VPN Team: " + cert_mail_sent);
                            //$("#wifi_att").html("WIFI Attribute value : <span style='font-weight:bold;'>" + wifi_value + "</span>"); // line modified by pr on 29thnov18 for wifi

                            $("#wifi_att").html("<strong>eForms</strong> WIFI Attribute : <span style='font-weight:bold;'>" + wifi_value + "</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>LDAP</strong> WIFI Attribute : <span style='font-weight:bold;'>" + ldap_wifi_value + "</span>"); // line modified by pr on 2ndjan2020

                            $("#wifi_select").show();
                            $('[name=wifi_select] option').filter(function () {
                                //console.log(" text is " + $(this).val());
                                return ($(this).val() == wifi_value);
                            }).prop('selected', true);
                        } else {
                            var new_value = reg_no + "~" + form_name + "~delete";
                            $("#mark_done_btn").val(new_value);
                            //console.log(" inside wifi process DELETE ");
                        }
                        // start, code added by pr on 21stjun19
                        $("#wifi_mad").show();
                        $("#firewall_mad").hide();
                        $("#wifiport_mad").hide();
                        // end, code added by pr on 21stjun19
                        $("#imap_mad").hide();
                    });
            //.error(function() {}); // line commented by pr on 3rddec19
        } else if (form_name.trim() == "imappop") {
            $.getJSON('fetchimapvalue', {
                regNo: value
            },
                    function (jsonResponse) {
                        //console.log(" value got from getimapvalue is " + jsonResponse.imap_value);
                        var val = jsonResponse.imap_value;
                        if (val.indexOf("~") >= 0) {
                            var arr = val.split("~");
                            var str = "The protocols already enabled for this Applicant are : ";
                            if (arr[1].indexOf("-imaps") < 0) // it doesnt contain this string and hence imap is enabled
                            {
                                str += "[IMAP] ";
                            }
                            if (arr[1].indexOf("-pops") < 0) // it doesnt contain this string and hence imap is enabled
                            {
                                str += "[POP] ";
                            }
                            $("#imap_att").html(str);
                            $("#protocol_value").html(arr[0].toUpperCase());
                            $("#wifi_mad").hide();
                            $("#firewall_mad").hide();
                            $("#wifiport_mad").hide();
                            $("#imap_mad").show();
                        }
                    });
            //.error(function() {});// line commented by pr on 3rddec19
        } else if (form_name.trim() == "ip") {
            // console.log("in else... prefilldata ip" + role);
        } else if (form_name.trim() == "email_act") {// else if added by pr on 23rdapr2020       

            $.getJSON('fetchdorpdf', {
                regNo: value
            },
                    function (jsonResponse) {
                        console.log(" value got from getimapvalue is " + jsonResponse.imap_value);
                        //imap_value -> emp_type + "~" + dor+ "~" + work_order;  
                        var val = jsonResponse.imap_value;
                        if (val.indexOf("~") >= 0) {
                            var arr = val.split("~");

                            var emp_type = "", dor = "", work_order = "";

                            if (arr.length == 3)
                            {
                                if (arr[0] != null)
                                {
                                    emp_type = arr[0];
                                }

                                if (arr[1] != null)
                                {
                                    dor = arr[1];
                                }

                                if (arr[2] != null)
                                {
                                    work_order = arr[2];
                                }
                            }
                            //console.log(" work order is "+work_order+" dor "+dor+" emp_type "+emp_type);

                            $("#emailact_mad").show();

                            $(".emp_type").html(emp_type);

                            $("#emp_type_hid").val(emp_type);

                            console.log(" emp type in hidden is " + $("#emp_type_hid").val());

                            if (emp_type != "emp_regular")// show the pdf download link and dor prefilled in calendar 
                            {
                                $("#show_dor").show();

                                $("#uploaded_filename").text(work_order);

                                //$("#emailact_dor").val(dor);
                                $("#single_dor7").val(dor);
                                var str = `<p><ul><li>At the time of Email Id creation for Single User Subscription, check the Date of Expiry mentioned in the relevant Work Order provided by the user.</li><li>For entering/changing Date of Expiry manually, a calendar is provided through which you can choose the Date of Expiry as per the Work Order.</li></p><label for="street" ><span>Work Order Uploaded</span></label>: <span id="test_id"><a href="download_1">` + work_order + `</a></span>`;

                                //console.log(" work_order is "+work_order);
                                $("#show_dor #emailact_pdf").html(str);
                            } else
                            {
                                $("#show_dor").hide();
                            }

                        }
                    });
        } else {
            if (form_name.trim() == "single") // if added by pr on 12thjun2020
            {
                //console.log("in else... prefilldata " + role);
                $("#single_emp_type").show();
                $.getJSON('fetchPOs', {},
                        function (jsonResponse) {
                            //console.log("JSON RESPONSE : " + jsonResponse)
                            var select = $('#po');
                            select.find('option').remove();
                            $('<option>').val("").text("Select PO").appendTo(select);
                            $.each(jsonResponse.podata, function (index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                            var select = $('#bo');
                            select.find('option').remove();
                            var select = $('#domain');
                            select.find('option').remove();
                        });
                var value = reg_no + "~" + form_name + "~create";
                $.post('fetchEmpType', {
                    regNo: value
                },
                        function (jsonResponse) {
                            //console.log("JSON RESPONSE : " + jsonResponse)
                            var empType = jsonResponse.emp_type;
                            var prefEmail = jsonResponse.prefEmail;
                            //console.log(" inside getemptype response emptype is " + empType + " prefEmail value is " + prefEmail);
                            $("#emp_type_hid").val(empType);
                            $(".emp_type_span").html(empType);
                            $("#pref_email_span").html(prefEmail);

                            // start, code added by pr on 4thdec19

                            var domain = "";

                            if (prefEmail.indexOf(",") > -1)
                            {
                                var prefArr = prefEmail.split(",");

                                var prefEmail = prefArr[0];

                                var arr = prefEmail.split("@");

                                domain = arr[1];
                            }

                            var flag = false;

                            //console.log(" jsonResponse.domaindata value is "+jsonResponse.domaindata);

                            $.each(jsonResponse.domaindata, function (index, value) {

                                //console.log(" domaindata value is "+value);

                                if (value.trim() == "nic.in @nic.in")
                                {
                                    value = "nic.in";
                                }

                                if (value.trim() == domain)
                                {
                                    flag = true;
                                }

                            });

                            //console.log(" domain exists "+flag);

                            if (!flag)
                            {
                                var str = "<font style='color:red;'>Please add the domain first as it doesnt exist !!</font>";

                                showAlertBox(str);

                                $("#dom_err").html(str);
                            } else
                            {
                                $("#dom_err").html("");
                            }

                            // end, code added by pr on 4thdec19

                        }, 'json');
            } else if (form_name.trim() == "centralutm") // if added by pr on 29thjul19
            {
                $("#firewall_mad").show();
            } else if (form_name.trim() == "wifiport") // if added by pr on 29thjul19
            {
                $("#wifiport_mad").show();
            }

        }
    }
}

function prefillDAData() {
    //console.log(" inside prefillDAData ");
    //var value = $("#add_da_btn").val();
    var value = $("#fwd_da_btn").val(); // line modified by pr on 6thjun18
    //console.log(" inside prefillDAData regno value is "+value);
    // prefill the data in the modal
    $.getJSON('fetchDAs', {
        regNo: value
    },
            function (jsonResponse) {
                var select = $('#fwd_da');
                select.find('option').remove();
                $('<option>').val("").text("Select DA-Admin").appendTo(select);
                /*$.each(jsonResponse.coorddata, function (index, value)
                 {
                 $('<option>').val(value).text(value).appendTo(select);
                 });*/
                // above line commented below added by pr on 9thmar18
                $.each(jsonResponse.dadata, function (index, value) {
                    $('<option>').val(value).text(value).appendTo(select);
                });
                var empDet = "";
                $.each(jsonResponse.empdata, function (index, value) {
                    if (value != "" && index == "employment") {
                        empDet += " Employment: " + value + "<br>";
                    } else if (value != "" && index == "ministry") {
                        empDet += " Ministry: " + value + "<br>";
                    } else if (value != "" && index == "department") {
                        empDet += " Department: " + value + "<br>";
                    } else if (value != "" && index == "state") {
                        empDet += " State: " + value + "<br>";
                    } else if (value != "" && index == "organization") {
                        empDet += " Organization: " + value + "<br>";
                    } else if (value != "" && index == "other_dept") {
                        empDet += " Other: " + value + "<br>";
                    }
                });
                if (empDet != "") {
                    //$("#ajaxEmpDetailsMAdmin").html(empDet);
                    $(".ajaxEmpDetailsMAdmin").html(empDet); // above line commentde this added by pr on 23rdapr18
                }
            });
}
// below function modified by pr on 1stjun18
function prefillBulkData(reg_no, form_name) // function modified by pr on 27thdec17
{
    $("#showRejectData").hide(); // line added by pr on 28thnov19

    //console.log(" inside prefillBulkData func value of form_type is " + form_name);
    if (form_name != "nkn_bulk") // if else added by pr on 27thdec17
    {
        $.getJSON('fetchPOs', {},
                function (jsonResponse) {
                    //console.log(" inside getPOs jsonResponse ");
                    //var select = $('#po');
                    var select = $('select[name="bulkpo"]');
                    select.find('option').remove();
                    $('<option>').val("").text("Select PO").appendTo(select);
                    $.each(jsonResponse.podata, function (index, value) {
                        // console.log(" inside jsonresponse each loop index " + index + " value is " + value);
                        $('<option>').val(value).text(value).appendTo(select);
                        //console.log(" after value has been set " + $('<option>').val());
                    });
                    var select = $('select[name="bulkbo"]');
                    select.find('option').remove();
                    var select = $('select[name="bulkdomain"]');
                    select.find('option').remove();
                });
        // below code added by pr on 1stmay19
        var value = reg_no + "~" + form_name + "~create";
        $.post('fetchEmpType', {
            regNo: value
        },
                function (jsonResponse) {
                    var empType = jsonResponse.emp_type;
                    var prefEmail = jsonResponse.prefEmail;
                    //console.log(" inside getemptype response emptype is " + empType + " prefEmail value is " + prefEmail);
                    $("#emp_type_hid").val(empType);
                    $(".emp_type_span").html(empType);
                    $("#pref_email_span").html(prefEmail);
                }, 'json');
    } else // this is for nkn bulk form po bo domain popups
    {
        /*var select = $('select[name="bulkpo"]');
         select.find('option').remove();
         
         $('<option>').val("NIC Support").text("NIC Support").appendTo(select);
         
         select = $('select[name="bulkbo"]');
         select.find('option').remove();
         
         $('<option>').val("nationalknowledgenetwork").text("nationalknowledgenetwork").appendTo(select);
         
         select = $('select[name="bulkdomain"]');
         select.find('option').remove();
         
         $('<option>').val("nkn.in").text("nkn.in").appendTo(select);*/
        // above code commented below added by pr on 30thoct18
        $.getJSON('fetchPOs', {},
                function (jsonResponse) {
                    //console.log(" inside getPOs jsonResponse ");
                    //var select = $('#po');
                    var select = $('select[name="bulkpo"]');
                    select.find('option').remove();
                    $('<option>').val("").text("Select PO").appendTo(select);
                    $.each(jsonResponse.podata, function (index, value) {
                        // console.log(" inside jsonresponse each loop index " + index + " value is " + value);
                        if (value.trim() == "NIC Support" || value.trim() == "nkn-mailmigration") {
                            $('<option>').val(value).text(value).appendTo(select);
                        }
                        //console.log(" after value has been set " + $('<option>').val());
                    });
                    var select = $('select[name="bulkbo"]');
                    select.find('option').remove();
                    var select = $('select[name="bulkdomain"]');
                    select.find('option').remove();
                });
        // below code added by pr on 1stmay19
        var value = reg_no + "~" + form_name + "~create";
        $.post('fetchEmpType', {
            regNo: value
        },
                function (jsonResponse) {
                    var empType = jsonResponse.emp_type;
                    var prefEmail = jsonResponse.prefEmail;
                    //console.log(" inside getemptype response emptype is " + empType + " prefEmail value is " + prefEmail);
                    $("#emp_type_hid").val(empType);
                    $(".emp_type_span").html(empType);
                    $("#pref_email_span").html(prefEmail);
                }, 'json');
    }
    $("#bulk_users_tbody").hide(); // line added by pr on 30thmay18
    $.getJSON('fetchBulkUsers', {
        reg_no: reg_no
    },
            function (jsonResponse) {
                $("#check_all").prop("checked", false); // line added by pr on 1stjun18
                //  console.log(" insinde getbulkusers jsonresponse ");
                // start, code added by pr on 29thmay18
                var sub = "";
                var showMore = "";
                var str = "";
                var checkboxBulkId = ""; // line added by pr on 29thmay18
                var k = 1; // line added by pr on 18thapr18
                $.each(jsonResponse.bulkUsersdata, function (index, value) {
                    showMore = "";
                    if (value.reject_remarks != "" && value.reject_remarks.trim() != "") {
                        if (value.reject_remarks.trim().length >= 10) {
                            sub = value.reject_remarks.substring(0, 9);
                            showMore = '<span class="moreb" id="moreb_' + value.bulk_id + '" style="color:blue;cursor:pointer;"> ' + sub + ' more...</span><span id="complete_' + value.bulk_id + '" class="complete" style="display:none;">' + value.reject_remarks + '</span>';
                        } else {
                            sub = value.reject_remarks;
                            showMore = sub;
                        }
                    }
                    checkboxBulkId = value.bulk_id;
                    //console.log(" inside getbulkusers function call value of mobile_ldap_email is " + value.mobile_ldap_email.trim());
                    if (value.mobile_ldap_email.trim() != "") {
                        //checkboxBulkId = checkboxBulkId + "~y";
                        checkboxBulkId = checkboxBulkId + "~" + value.mobile_ldap_email.trim(); // above line modified by pr on 24thoct18                        
                    } else {
                        checkboxBulkId = checkboxBulkId + "~n";
                    }
                    // id added for tr by pr on 24thdec18
                    str += ` <tr class = "odd gradeX togl" role = "row" id = "tr_` + value.bulk_id + `" >
                                        <td> ` + k + ` </td>
                                        <td> ` + value.bulk_id + ` </td>
                                        <td contenteditable = "true" class = "uid" id = "` + value.bulk_id + `" > ` + value.uid + ` </td>
                                        <td contenteditable = "true" class = "mail" id = "` + value.bulk_id + `" > ` + value.email + ` </td>
                                        <td> ` + value.mobile + ` </td>
                                        <td><span id = "uid_exist_` + value.bulk_id + `" > ` + value.mobile_ldap_email + ` </span></td >
                                        <td contenteditable = "true" class = "reject_remarks" id = "` + value.bulk_id + `" > ` + showMore + ` </td>
                                        <td class = "center" > ` + value.createdon + ` </td>
                                        <td>
                                        <input type = "checkbox" name = "bulk_id"  onclick = "checkNumChecked();" id = "check_` + value.bulk_id + `"   value = "` + checkboxBulkId + `" />
                                        </td>
                                        </tr>`;
                    k++; // line added by pr on 18thapr18 and above str modified
                });
                $("#bulk_users_tbody").html(str);
                $("#bulk_users_tbody").show(); // line added by pr on 30thmay18 , so that when the data is updated then only it should get displayed
            });

    // start, code added by pr on 28thnov19

    // start, code added by pr on 28thnov19

    // ajax call to an action class to check for any previous rejected requests for this applicant email and mobile combination by the admin for this service

    //console.log(" inside approve btn function click regNo value is "+create_email_btn);

    var emailMobile = $("#app_" + reg_no).attr("class");

    var emmob_arr = emailMobile.split("~");

    var email = emmob_arr[0];

    var mobile = emmob_arr[1];

    //console.log(" prefillbulkdata reg_no value is "+reg_no+" emailMobile value is "+emailMobile);

    var newvalue = emailMobile + "~" + form_name; // now the value is replaced with email~mobile~form_name

    //var email_mobile = $(emailMobileElement).attr("class");

    $.post('checkPreviousReject', {
        regNo: newvalue
    },
            function (jsonResponse) {

                // console.log(" prefillbulkdata inside checkPreviousReject response prevrejectdata value is "+jsonResponse.prevrejectdata);

                var table = "";

                if (jsonResponse.prevrejectdata != null)
                {
                    table = getPrevRejectTable(jsonResponse.prevrejectdata, email, mobile);
                }

                // console.log(" bulk table tos how is "+table);

                // end, code added by pr on 28thnov19

                $("#showRejectData").html(table);

                $("#showRejectData").show();

            }, 'json');  // line added by pr on 28thnov19	

    // end, code added by pr on 28thnov19        
}
// below function added by pr on 4thapr18
$(document).on('click', '.modalcall', function () {
    var name = $(this).attr("name");
    var newName = "";
    // console.log(" name of this attribute is "+name);
    name = name.split("|").join("<br>");
    // console.log(" name value is "+name);
    //name = name.replaceAll("|", "<br>" );
    $("#mobile_ldap_email").html(name);
    $('#bulk_mobile').modal();
});
// below function added by pr on 5thapr18
$(document).on('click', '.bulk_mobile', function () {

    //console.log(" bulk_mobile clicked ");

    var value = "";
    if ($("#create_email_btn") != null && $("#create_email_btn") != "undefined" && $("#create_email_btn") != undefined) {
        value = $("#create_email_btn").val();
    }
    //console.log(" inside bulk_mobile click func value of create btn is "+value);
    if (value != "") {
        // send the request to servger side to fetch the mobile duplicates for this reg no auth_email and show them in the modal
        var reg_no = "",
                formName = "";
        var regNoArr = value.split("~");
        reg_no = regNoArr[0];
        formName = regNoArr[1];
        // console.log(" reg_no value is "+reg_no);
        $.getJSON('fetchMobileDuplicates', {
            regNo: reg_no,
            formName: formName
        },
                function (jsonResponse) {
                    // console.log(" inside jsoinresponse from getMobileDuplicates func ");
                    var name = jsonResponse.mobile_ldap_email;
                    name = name.split("|").join("<br>");
                    if (name != "")
                    {
                        $("#mobile_ldap_email").html(name);
                    } else
                    {
                        $("#mobile_ldap_email").html("There are no emails linked with this Mobile");
                    }
                    $('#bulk_mobile').modal();
                });
        /*.error(function() {
         // console.log(" inside error ");
         });*/ // line commented by pr on 3rddec19
    }
});
// below function modified by pr on 29thmay18 
$(document).on('blur', 'td[contenteditable]', function () {
    var bulk_id = $(this).attr('id');
    var field_name = $(this).attr('class');
    //var field_value = $(this).text();
    var field_value = $(this).html();
    var uid_exist = "";
    //            if( field_name == "reject_remarks" ) // if the field is reject_remarks then just fetch teh content inside the span inside the td of contenteditable and with class as complete
    //            {
    //                field_value = $(".complete").html();
    //            }
    if (field_value.indexOf("<span") >= 0) {
        //console.log(" contains spane field value is "+field_value);
        var comp_id = "complete_" + bulk_id;
        //console.log( " comp_id value is "+comp_id );
        field_value = $("#" + comp_id).html();
        //console.log( " value inside comp_id value is "+comp_id+" is "+field_value );
    } else {
        //console.log(" DOESNT contains spane field value is "+field_value);
    }
    if (typeof field_value !== 'undefined' && field_value != "" && field_value.trim() != "") {
        var find = '<br>';
        var re = new RegExp(find, 'g');
        field_value = field_value.replace(re, '');
        var find = '&nbsp;';
        var re = new RegExp(find, 'g');
        field_value = field_value.replace(re, '');
    }
    //console.log(" after replace field value is " + field_value);
    if (field_name == "reject_remarks") // only in case of reject remarks uid_exist value has a significance
    {
        var span = "uid_exist_" + bulk_id;
        //console.log(" span id is "+span);
        var id = $("#" + span);
        //console.log(" id is "+id);
        var id_value = id.html();
        //console.log(" id value is "+id_value);
        if ($("#uid_exist_" + bulk_id).html().trim() != "") {
            uid_exist = "y";
        } else {
            uid_exist = "n";
        }
    }
    //console.log(" field name is "+field_name+" field value is "+field_value+" complete class value is "+field_value+" uid_exist value is "+uid_exist);
    $.post('updateBulk', {
        bulk_id: bulk_id,
        field_name: field_name,
        field_value: field_value,
        uid_exist: uid_exist // line added by pr on 30thmay18
    },
            function (jsonResponse) {
                if (jsonResponse.isSuccess == true) {
                    $("#bulkUpdateMessage").html(jsonResponse.msg);
                    $("#bulkUpdateMessage").show();
                } else if (jsonResponse.isError == true) {
                    $("#bulkUpdateMessage").html(jsonResponse.msg);
                    $("#bulkUpdateMessage").show();
                }
                setTimeout(function () {
                    // console.log(" inside set timeout func ");
                    $("#bulkUpdateMessage").hide();
                    $('#bulkUpdateMessage').html('');
                }, 10000);
            }, 'json');
});

// above function modified by pr on 24thsep19
function prefillViewBulkData(reg_no, form_name)// function added by pr on 27thdec17 to show created and not created records
{
    //console.log(" inside prefillViewBulkData function reg no is " + reg_no + " form_name is " + form_name);

    $("#bulk_view_heading").html("Bulk Email IDs Details for Registration no - " + reg_no);

    reg_no = reg_no + "~" + form_name;// line added by pr on 8thjan18

    $("#bulk_reject_btn").val(reg_no); // line added by pr on 23rdjul19

    var role = $("#role").val();

    var showRemarksEditor = false; // line added by pr on 24thjul19

    var showRejectCheckbox = false; // line added by pr on 6thdec19
    var csrf = $('#CSRFRandom').val();
    //console.log(" inside prefillviewbulkdata func role value is "+role+" bulk_reject_btn value is "+$("#bulk_reject_btn").val());

    $.getJSON('fetchAllBulkUsers',
            {
                reg_no: reg_no,
                CSRFRandom: csrf

            },
            function (jsonResponse)
            {
                if (jsonResponse.csrf_error) {
                    alert(jsonResponse.csrf_error);
                }
                resetCSRFRandom();
                //console.log(" insinde getAllBulkUsers jsonresponse ");

                var str_head = "", str = "";

                $("#bulk_users_view_thead").html("");// line added by pr on 8thjan18

                $("#bulk_users_view_tbody").html("");


                $.each(jsonResponse.bulkAllUsersdata, function (index, value)
                {
                    showRejectCheckbox = false; // line added by pr on 6thdec19

//                    console.log(" inside prefillViewBulkData response each index is " + index + " bulk_id is " + value.bulk_id+ 
//                            " rejected_by is " + value.rejected_by+" reject_remarks is "+value.reject_remarks);

                    // start, code added by pr on 24thjul19

                    var stat_type = value._stat_type;// line added by pr on 24thdec19    

                    console.log(" stat type is " + stat_type + " role is " + role);

                    //if (value.is_created == "n" && value.is_rejected == "n")
                    if (value.is_created == "n" && value.is_rejected == "n" && ((stat_type == "ca_pending" && role == "ca")
                            || (stat_type == "coordinator_pending" && role == "co"))) // line modified by pr on 24thdec19
                    {
                        showRemarksEditor = true;

                        showRejectCheckbox = true; // line added by pr on 6thdec19
                    }

                    // end, code added by pr on 24thjul19

                    var is_created = "No";

                    var is_rejected = "No";

                    if (value.is_created == 'y')
                    {
                        is_created = "Yes";
                    }

                    if (value.is_rejected == 'y')
                    {
                        is_rejected = "Yes";
                    }

                    /*if (form_name == "vpn_bulk")
                     {
                     str += `<tr class="odd gradeX" role="row">
                     
                     <td>` + value.bulk_id + `</td>
                     <td>` + value.uid + `</td>
                     <td>` + value.email + `</td>
                     <td>` + value.mobile + `</td>
                     <td class="center">` + value.createdon + `</td>
                     
                     </tr>`;
                     } else*/
                    {
                        // mobile_ldap_email added by pr on 17thdec19
                        console.log(" for mobile " + value.mobile + " emails linked are " + value.mobile_ldap_email);
                        str += `<tr class="odd gradeX" role="row">

                                                                                <td>` + value.bulk_id + `</td>
                                                                                <td>` + value.uid + `</td>
                                                                                <td>` + value.email + ' ' + value.mobile + `</td>
                                                                                <td>` + value.mobile_ldap_email + `</td>
                                                                                <td class="center">` + is_created + `</td>
                                                                                <td class="center">` + is_rejected + `</td>
                                                                                <td title="` + value.reject_remarks + `" class="center">` + value.rejected_by + `</td>`;

                        str += `<td> ` + value.reject_remarks + `</td>`;

                        if (role == "co" || role == "ca") // if block added by pr on 19thjul19, ca added by pr on 6thdec19
                        {
                            if (showRemarksEditor && showRejectCheckbox) // && showRejectCheckbox added by pr on 6thdec19
                            {
                                str += `<td> <input type="checkbox" name="bulk_id"  onclick="" id="check_` + value.bulk_id + `" value="` + value.bulk_id + `" /></td>`;
                            } else
                            {
                                str += `<td> &nbsp;</td>`;
                            }
                        }

                        str += `</tr>`; // rejected_by added by pr on 19thjul19
                    }

                });

                // below code added by pr on 24thjul19
                {
                    str_head = `<tr>
                                        <th> Bulk Id </th>
                                        <th> UID </th>
                                        <th> Email & Mobile </th>
                                        <th> Emails Linked </th>
                                        <th> Is Created </th>                                        
                                        <th> Is Rejected </th>
                                        <th> Rejected By</th>`; // emails linked added  by pr on 17thdec19

                    str_head += `<th> Reject Remarks</th>`;

                    if (role == "co" || role == "ca") // if block added by pr on 19thjul19, ca added by pr on 6thdec19
                    {
                        if (showRemarksEditor)
                        {
                            str_head += `<th> <input type="checkbox" id="check_all_view" class="check_all" /></th>`;
                        } else
                        {
                            str_head += `<th> &nbsp;</th>`;
                        }
                    }

                    str_head += `</tr>`;// line added by pr on 8thjan18, rejected by added by pr on 19thjul19
                }


                //$("#bulk_users_view_thead").html(str_head);// line added by pr on 8thjan18

                /*var table = `<table class="table table-striped table-bordered table-hover table-checkable order-column" id="example1">
                 
                 <thead id="bulk_users_view_thead" >` + str_head +
                 `</thead>
                 <tbody id="bulk_users_view_tbody">` +
                 str
                 
                 
                 + `</tbody>
                 </table>`;*/

                var table = `<table id="example1" class="table table-striped table-bordered table-hover table-checkable order-column dt-responsive dataTable no-footer dtr-inline">

                                <thead id="bulk_users_view_thead" >` + str_head +
                        `</thead>
                                <tbody id="bulk_users_view_tbody">` +
                        str


                        + `</tbody>
                            </table>`;

                // start, code added by pr on 23rdjul19

                if (role == "co" || role == "ca") // if there are any requests needs to be processed then only show the remarks and reject button, ca added by pr on 6thdec19
                {
                    if (showRemarksEditor)
                    {
                        table += '<div class="form-body"><div class="form-group"><label>Add Reject Remarks</label><textarea class="form-control" maxlength="500" id="statRemarksView" name="statRemarks"';

                        table += 'placeholder="Enter Reject Remarks" rows="3"></textarea></div><font style="color:red"><span id="statRemarksError"></span></font></div>';

                        table += '<div><button type="button" value="' + reg_no + '" id="bulk_reject_btn" class="btn dark btn-danger bulk_popup">Reject</button></div>';
                    }
                    /* else  // if all are rejected and the status is coordinator_pending then give an option to reject right away
                     {                        
                     var status = $(reg_no+"_status").text();
                     
                     if( status.equalsIgnoreCase("Pending with Coordinator") )
                     {                        
                     table += '<a data-toggle="modal" href="#reject_modal" onclick="apply_heading('+reg_no+', '+form_name+', "reject", "");resetRandom();"><i class="fa fa-remove"></i> Reject </a>';
                     }
                     } */

                }

                // end, code added by pr on 23rdjul19


                $("#bulk_view_modal").modal();

                /*$("#bulk_users_view_thead").append(str_head);// line added by pr on 8thjan18
                 
                 $("#bulk_users_view_tbody").append(str);*/

                $("#table_id").html(table);
                setTimeout(function () {
                    $('#example1').dataTable({
                        "ordering": false,
                        responsive: true
                    });
                }, 150);

            });
    /*.error(function ()
     {
     //alert("error");
     
     showAlertBox("error") // line modified by pr on 22ndmar19
     });*/ // line commented by pr on 3rddec19
}

// below function added by pr on 17thdec19
function clearFileUpload()
{
    $("#csv_file_error").html("");

    $(".custom-file-label").html("Select File");
}

function resetRandom() {
    $.getJSON('resetCSRFRandom', {}, function (jsonResponse) {
        // console.log(jsonResponse);
        $("#CSRFRandom").val(jsonResponse.random);
    })
}
// below code added by pr on 7thmar18
$("#add_da_btn").on("click", function () {
    // start, code added by pr on 3rdjan18
    //resetRandom(); 
    var random = $("#random").val();
    // end, code added by pr on 3rdjan18
    var add_coord = $("#add_da").val();
    if (add_coord != "") // if else added by pr on 20thdec17
    {
        var add_coord_btn = $("#add_da_btn").val();

        // above code commented below added by pr on 22ndmar19
        bootbox.confirm({
            message: "Do you really wish to Add DA-Admin",
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
                    // start, code added by pr on 20thapr18
                    $("#add_da_btn").hide();
                    $(".loadergif").show();
                    $(".loader").show();
                    // end, code added by pr on 20thapr18
                    $.post('addDA', {
                        add_coord: add_coord,
                        regNo: add_coord_btn,
                        random: random
                    },
                            function (jsonResponse) {
                                // start, code added by pr on 20thapr18
                                $("#add_da_btn").show();
                                $(".loadergif").hide();
                                $(".loader").hide();
                                // end, code added by pr on 20thapr18
                                if (jsonResponse.isSuccess == true) {
                                    resetRandom(); // line added by pr on 4thjan18
                                    $(".alert-danger").show();
                                    $(".alert-danger").html(jsonResponse.msg);
                                    setTimeout(function () {
                                        $(".alert-danger").hide();
                                        $(".alert-danger").html("");
                                    }, 10000); // code added by pr on 20thdec17
                                } else if (jsonResponse.isError == true) {
                                    resetRandom(); // line added by pr on 4thjan18
                                    $(".alert-danger").hide();
                                    $("#add_da_error").html(jsonResponse.msg); // add_da_error modified by pr on 23rdapr18
                                    setTimeout(function () {
                                        $("#add_da_error").html(""); // add_da_error modified by pr on 23rdapr18 
                                    }, 10000); // code added by pr on 20thdec17
                                }
                            }, 'json');
                }
            }
        });
    } else {
        //alert("Please enter DA to Add.");
        showAlertBox("Please enter DA to Add.")
    }
});
// below code added by pr on 7thmar18
$("#fwd_da_btn").on("click", function () {
    // start, code added by pr on 3rdjan18
    //resetRandom(); 
    var random = $("#random").val();
    // end, code added by pr on 3rdjan18
    //var fwd_coord = $("#fwd_coord").val();
    // start, code added by pr on 22ndfeb18
    var fwd_coord = new Array();
    var selectedValues = [];
    $("#fwd_da :selected").each(function () {
        selectedValues.push($(this).val());
    });
    fwd_coord = selectedValues.join();
    // end, code added by pr on 22ndfeb18
    var fwd_coord_btn = $("#fwd_da_btn").val();
    // start, code added by pr on 22ndfeb18
    var errmsg = "";
    var isValid = true;
    if (fwd_coord == "") {
        errmsg = " Please Select a DA-Admin to forward the request to.";
        isValid = false;
    }
    // end, code added by pr on 22ndfeb18
    //if (fwd_coord != "") // if else added by pr on 20thdec17
    if (isValid) {

        // above code commented below added by pr on 22ndmar19
        bootbox.confirm({
            message: "Do you really wish to forward this request to the selected DA-Admin ?",
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
                    $("#madmin_forward_modal").modal("hide");
                    $("#statRemarksRev").val(""); // line added by pr on 7thjun18
                    $("#revert_modal").modal(); // content replaced with this line by pr on 6thjun18
                }
            }
        });
    } else {
        //alert(errmsg);
        showAlertBox(errmsg)
    }
});
// below code added by pr on 23rdapr18
$("#fwd_m_da_btn").on("click", function () {
    var random = $("#random").val();
    var fwd_m_da_btn = $("#fwd_m_da_btn").val();
    var errmsg = "";
    var isValid = true;
    var da_from_admin = $("#da_from_admin").val();
    if (da_from_admin == "") {
        errmsg = " Please Enter a DA-Admin to forward request to.";
        isValid = false;
    }
    // end, code added by pr on 22ndfeb18
    // console.log(" fwd_coord value is "+da_from_admin+" regNo "+fwd_m_da_btn);
    if (isValid) {

        // above code commented below added by pr on 22ndmar19
        bootbox.confirm({
            message: "Do you really wish to forward this request to the selected DA-Admin ?",
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
                    $("#madmin_forward_modal").modal("hide");
                    $("#statRemarksRev").val(""); // line added by pr on 7thjun18
                    $("#revert_modal").modal(); // content replaced with this line by pr on 6thjun18  
                }
            }
        });
    } else {
        $("#da_from_admin_error").html(errmsg);
    }
});
// below code added by pr on 6thjun18
$(document).on("click", "#revert_btn", function (e) {
    var role = $("#role").val();
    var value = $(this).attr("value");
    var random = $("#random").val();
    var statRemarks = $("#statRemarksRev").val();
    var loggedin_email = $("#loggedin_email").val();
    //console.log(" inside revert btn click statRemarks value is " + statRemarks);

    $("#revert_btn").hide(); // line added by pr on 1stnov19

    if (role == 'co') {
        $.post('revoke', {
            regNo: value,
            random: random,
            statRemarks: statRemarks // line added by pr on 6thjun18
        },
                function (jsonResponse) {
                    if (jsonResponse.isSuccess == true) {
                        resetRandom(); // line added by pr on 4thjan18
                        //$(".alert-danger").show();
                        //$(".alert-danger").html(jsonResponse.msg);
                        //alert(jsonResponse.msg);
                        showAlertBox(jsonResponse.msg)

                        $("#madmin_forward_modal").modal("hide");
                        $("#revert_modal").modal("hide");
                        var action = $("#action_hidden").val();
                        //setAction(action) // line commented by pr on 23rdoct18
                        // start, code added by pr on 23rdoct18
                        var table = $('#example').DataTable();
                        //console.log(" just before the datatable reload call ");
                        table.ajax.reload(null, false);
                        // end, code added by pr on 23rdoct18
                        // end, code added by pr on 25thmay18

                    } else if (jsonResponse.isError == true) {

                        $("#revert_btn").show(); // line added by pr on 1stnov19

                        resetRandom(); // line added by pr on 4thjan18
                        //$(".alert-danger").hide();
                        //$("#fwd_coord_error").html(jsonResponse.msg);
                        //alert(jsonResponse.msg);
                        showAlertBox(jsonResponse.msg)
                        /*$("#madmin_forward_modal").modal("hide");
                         
                         $("#revert_modal").modal("toggle");*/
                    }
                }, 'json');
    } else if (role == 'admin') {
        //console.log(" inside mailadmin block statRemarks value is " + statRemarks);
        if (loggedin_email == "support@nic.in" || loggedin_email == "support@gov.in" || loggedin_email == "support@dummy.nic.in") {
            //fwd_da_btn
            var fwd_coord_select = "",
                    da_from_admin = "",
                    fwd_coord = "";
            if ($("#fwd_da") != "undefined") {
                //console.log(" inside fwd_da not undefined ");
                fwd_coord_select = new Array();
                var selectedValues = [];
                $("#fwd_da :selected").each(function () {
                    selectedValues.push($(this).val());
                });
                fwd_coord_select = selectedValues.join();
            }
            //fwd_m_da_btn
            if ($("#da_from_admin") != "undefined") {
                //console.log(" inside da_from_admin not undefined ");
                da_from_admin = $("#da_from_admin").val();
            }
            if (fwd_coord_select != "") {
                fwd_coord = fwd_coord_select;
            }
            if (da_from_admin != "") {
                fwd_coord = da_from_admin;
            }
            // console.log(" fwd_coord  value is " + fwd_coord + " regNo value is " + value + " statRemarks value is " + statRemarks);
            if (fwd_coord != "") {
                $.post('revoke', {
                    fwd_coord: fwd_coord,
                    regNo: value,
                    random: random,
                    statRemarks: statRemarks // line added by pr on 6thjun18
                },
                        function (jsonResponse) {
                            // start, code added by pr on 20thapr18
                            $("#fwd_da_btn").show();
                            $(".loadergif").hide();
                            $(".loader").hide();
                            // end, code added by pr on 20thapr18
                            if (jsonResponse.isSuccess == true) {
                                resetRandom(); // line added by pr on 4thjan18
                                //$(".alert-danger").show();
                                //$(".alert-danger").html(jsonResponse.msg);
                                //alert(jsonResponse.msg);
                                showAlertBox(jsonResponse.msg)

                                $("#madmin_forward_modal").modal("hide");
                                $("#revert_modal").modal("hide");
                                var action = $("#action_hidden").val();
                                //setAction(action) // line commented by pr on 23rdoct18
                                // start, code added by pr on 23rdoct18
                                var table = $('#example').DataTable();
                                //console.log(" just before the datatable reload call ");
                                table.ajax.reload(null, false);
                                // end, code added by pr on 23rdoct18
                                // end, code added by pr on 25thmay18
                                //location.reload();
                            } else if (jsonResponse.isError == true) {

                                $("#revert_btn").show(); // line added by pr on 1stnov19

                                resetRandom(); // line added by pr on 4thjan18
                                // start, code added by pr on 23rdapr18
                                //                                $(".alert-danger").hide();
                                //
                                //                                $("#add_da_error").html(jsonResponse.msg); // add_da_error modified by pr on 23rdapr18
                                //
                                //                                setTimeout(function () {
                                //                                    $("#add_da_error").html(""); // add_da_error modified by pr on 23rdapr18 
                                //                                }, 10000); // code added by pr on 20thdec17
                                //                                
                                // end, code added by pr on 23rdapr18 
                                //alert(jsonResponse.msg); // commented by pr on 23rdapr18
                                showAlertBox(jsonResponse.msg)
                                $("#madmin_forward_modal").modal("toggle");
                                $("#revert_modal").modal("toggle");
                            }
                        }, 'json');
            } else {
                //alert(" No DA selected to forward the request to.");
                showAlertBox(" No DA selected to forward the request to.")
            }
        } else {
            $.post('revoke', {
                regNo: value,
                random: random,
                statRemarks: statRemarks // line added by pr on 6thjun18
            },
                    function (jsonResponse) {
                        if (jsonResponse.isSuccess == true) {
                            resetRandom(); // line added by pr on 4thjan18
                            //$(".alert-danger").show();
                            //$(".alert-danger").html(jsonResponse.msg);
                            //alert(jsonResponse.msg);
                            showAlertBox(jsonResponse.msg)

                            //$("#madmin_forward_modal").modal("toggle");
                            $("#madmin_forward_modal").modal("hide");
                            $("#revert_modal").modal("hide");
                            var action = $("#action_hidden").val();
                            // setAction(action)// line commented by pr on 23rdoct18
                            // start, code added by pr on 23rdoct18
                            var table = $('#example').DataTable();
                            //console.log(" just before the datatable reload call ");
                            table.ajax.reload(null, false);
                            // end, code added by pr on 23rdoct18
                            // end, code added by pr on 25thmay18

                        } else if (jsonResponse.isError == true) {

                            $("#revert_btn").show(); // line added by pr on 1stnov19

                            resetRandom(); // line added by pr on 4thjan18
                            //$(".alert-danger").hide();
                            //$("#fwd_coord_error").html(jsonResponse.msg);
                            //alert(jsonResponse.msg);
                            showAlertBox(jsonResponse.msg)
                        }
                    }, 'json');
        }
    }
});
// below function added by pr on 6thmar18
function revoke(reg_no, form_type) {
    // check if role is co or mailadmin
    resetRandom();
    var role = $("#role").val();
    var loggedin_email = $("#loggedin_email").val();
    var random = $("#random").val();
    var value = reg_no + "~" + form_type + "~revoke";
    $("#revert_btn").show(); // line added by pr on 1stnov19
    $("#revert_btn").val(value); // line added by pr on 6thjun18
    if (role == 'co') {

        // above code commented below added by pr on 22ndmar19
        bootbox.confirm({
            message: "If this request has landed to you wrongly and you think, you are not the right person to process this request, you choose this option and this Request will go back to support for further processing.Do you really wish to perform this action?",
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
                    $("#statRemarksRev").val(""); // line added by pr on 7thjun18
                    $("#revert_modal").modal("show");
                }
            }
        });
    } else if (role == 'admin') {
        // check if the logged in user is support , if yes then show the da related popup
        // if not support then send directly to he maiadmin as support
        //loggedin_email = "support@nic.in"; // done only for testing  on 23rdapr18
        // show the popup to select the DA-admin
        if (loggedin_email == "support@nic.in" || loggedin_email == "support@gov.in" || loggedin_email == "support@dummy.nic.in") {
            //console.log(" isnide revoke function suport loggedin email ");
            var heading = "Forward Action";
            $(".modal-title").text(heading + " for Reg No. - " + reg_no);
            $("#add_da_btn").val(value);
            $("#fwd_da_btn").val(value);
            $("#fwd_m_da_btn").val(value); // line added by pr on 23rdapr18
            // prefill the data in the modal
            $.getJSON('fetchDAs', {
                regNo: value
            },
                    function (jsonResponse) {
                        // console.log(" isnide revoke function suport loggedin email get das response ");
                        var select = $('#fwd_da');
                        select.find('option').remove();
                        $('<option>').val("").text("Select DA-Admin").appendTo(select);
                        /* $.each(jsonResponse.coorddata, function (index, value)
                         {
                         $('<option>').val(value).text(value).appendTo(select);
                         });*/
                        // above line commented below added by pr on 9thmar18
                        $.each(jsonResponse.dadata, function (index, value) {
                            //console.log(" inside get dadata loop ");
                            $('<option>').val(value).text(value).appendTo(select);
                        });
                        var empDet = "";
                        $.each(jsonResponse.empdata, function (index, value) {
                            if (value != "" && index == "employment") {
                                empDet += " Employment: " + value + "<br>";
                            } else if (value != "" && index == "ministry") {
                                empDet += " Ministry: " + value + "<br>";
                            } else if (value != "" && index == "department") {
                                empDet += " Department: " + value + "<br>";
                            } else if (value != "" && index == "state") {
                                empDet += " State: " + value + "<br>";
                            } else if (value != "" && index == "organization") {
                                empDet += " Organization: " + value + "<br>";
                            } else if (value != "" && index == "other_dept") {
                                empDet += " Other: " + value + "<br>";
                            }
                        });
                        if (empDet != "") {
                            //$("#ajaxEmpDetailsMAdmin").html(empDet);
                            $(".ajaxEmpDetailsMAdmin").html(empDet); // above line commentde this added by pr on 23rdapr18
                        }
                    });
            $("#madmin_forward_modal").modal();
        } else {

            // above code commented below added by pr on 22ndmar19, support@nic.in added by pr on 1stnov19
            bootbox.confirm({
                message: "Do you really wish to forward this request to Admin (Support@nic.in) ?",
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
                        $("#statRemarksRev").val(""); // line added by pr on 7thjun18
                        $("#revert_modal").modal("show"); // content replaced with this by pr on 6thjun18     
                    }
                }
            });
        }
    }
}
// below function added by pr on 12thmar18
$(document).on('change', '#choose_da_type_add', function () {
    var choose_da_type_add = $("#choose_da_type_add").val();
    if (choose_da_type_add != "") {
        if (choose_da_type_add == 'da') {
            $("#coordType").html("DA-Admin");
        } else if (choose_da_type_add == 'c') {
            $("#coordType").html("Coordinator");
        }
    }
});
//added by sanjeev kumar
$(document).on('click', '#search_btn_of_email', function () {
    var searchedEmail = $("#sreg_no_byemail").val();
    //
    var isValid = true;
    var errmsg = "";
    if (searchedEmail == "") {
        isValid = false;
        errmsg += "Please enter email";
    }
    if (isValid) {

        $.getJSON('searchStatusByRegNo', {
            searchedEmail: searchedEmail
        },
                function (jsonResponse) {
                    // start, code added by pr on 2ndmay18      
                    console.log("json respones:::" + jsonResponse);
                    var empDet = "";
                    $.each(jsonResponse.empDetailsHM, function (index, value) {
                        // console.log(" searchStatusByRegNo response index is "+index+" value is "+value);
                        if (value.trim() != "" && index == "employment") {
                            empDet += "<tr><td>Employment</td><td>" + value + "</td></tr>";
                        } else if (value != null && value.trim() != "" && index == "ministry") {
                            empDet += "<tr><td>Ministry</td><td>" + value + "</td></tr>";
                        } else if (value != null && value.trim() != "" && index == "department") {
                            empDet += "<tr><td>Department</td><td>" + value + "</td></tr>";
                        } else if (value != null && value.trim() != "" && index == "state") {
                            empDet += "<tr><td>State</td><td>" + value + "</td></tr>";
                        } else if (value != null && value.trim() != "" && index == "organization") {
                            empDet += "<tr><td>Organization</td><td>" + value + "</td></tr>";
                        } else if (value != null && value.trim() != "" && index == "other_dept") {
                            empDet += "<tr><td>Other</td><td>" + value + "</td></tr>";
                        }
                        if (index == "auth_off_name" && value.trim() != "") {
                            empDet += "<tr><td>Name of the Applicant</td><td>" + value + "</td></tr>";
                        }
                        if (index == "designation" && value.trim() != "") {
                            empDet += "<tr><td>Designation</td><td>" + value + "</td></tr>";
                        }
                        if (index == "emp_code" && value.trim() != "") {
                            empDet += "<tr><td>Employee Code</td><td>" + value + "</td></tr>";
                        }
                        if (index == "address" && value.trim() != "") {
                            empDet += "<tr><td>Postal Address</td><td>" + value + "</td></tr>";
                        }
                        if (index == "add_state" && value.trim() != "") {
                            empDet += "<tr><td>State of Posting</td><td>" + value + "</td></tr>";
                        }
                        if (index == "pin" && value.trim() != "") {
                            empDet += "<tr><td>Pincode</td><td>" + value + "</td></tr>";
                        }
                        if (index == "ophone" && value.trim() != "") {
                            empDet += "<tr><td>Telephone Office</td><td>" + value + "</td></tr>";
                        }
                        if (index == "rphone" && value.trim() != "") {
                            empDet += "<tr><td>Telephone Residence</td><td>" + value + "</td></tr>";
                        }
                        if (index == "mobile" && value.trim() != "") {
                            empDet += "<tr><td>Mobile</td><td>" + value + "</td></tr>";
                        }
                        if (index == "auth_email" && value.trim() != "") {
                            empDet += "<tr><td>Email</td><td>" + value + "</td></tr>";
                        }
//                        if (index == "hod_email" && value.trim() != "") {
//                            empDet += "<tr><td>Reporting/Nodal/Forwarding Officer Email</td><td>" + value + "</td></tr>";
//                        }
//                        if (index == "hod_name" && value.trim() != "") {
//                            empDet += "<tr><td>Reporting/Nodal/Forwarding Officer Name</td><td>" + value + "</td></tr>";
//                        }
//                        if (index == "hod_mobile" && value.trim() != "") {
//                            empDet += "<tr><td>Reporting/Nodal/Forwarding Officer Mobile</td><td>" + value + "</td></tr>";
//                        }
//                        if (index == "hod_telephone" && value.trim() != "") {
//                            empDet += "<tr><td>Reporting/Nodal/Forwarding Officer Telephone</td><td>" + value + "</td></tr>";
//                        }
//                        if (index == "ca_desig" && value.trim() != "") {
//                            empDet += "<tr><td>Reporting/Nodal/Forwarding Officer Designation</td><td>" + value + "</td></tr>";
//                        }

                        // start, code added by pr on 10thjan2020

                        value = value.trim();

                        var val = "";

                        if (index == "type" && value.trim() != "") {

                            if (value == "mail")
                            {
                                val = "Mail User (with mailbox)";
                            } else if (value == "app")
                            {
                                val = "Application User (without mail box(Eoffice-auth))";// line modified by pr on 5thmay2020
                            } else if (value == "eoffice")
                            {
                                val = "e-office-srilanka";
                            }

                            empDet += "<tr><td>Type of Account</td><td>" + val + "</td></tr>";
                        }

                        if (index == "emp_type" && value.trim() != "") {

                            if (value == "emp_regular")
                            {
                                val = "Govt/Psu Official";
                            } else if (value == "consultant")
                            {
                                val = "Consultant";
                            } else if (value == "emp_contract")
                            {
                                val = "FMS Support Staffs";
                            }

                            empDet += "<tr><td>Employment Type</td><td>" + val + "</td></tr>";
                        }

                        // end, code added by pr on 10thjan2020

                    });
                    if (empDet != "") {
                        // start code added by pr on 3rdjan19
                        empDet = empDet + "<br><br><a onClick='generatePdfs('" + searchedEmail + "')' title='Click here to Download the filled form in PDF'><i class='fa fa-download'></i> Generate Form </a><br><br>";
                        // end code added by pr on 3rdjan19
                        //console.log(empDet)
                        $(".ajaxEmpDetails").html(empDet); // line modified by pr on 20thmar18
                    }
                    // end, code added by pr on 2ndmay18
                    var str_head = "",
                            str = "";
                    str_head = ` <tr>
                        <th> S.No. </th>
                        <th> Registration Number </th>
                        <th> Applicant Email</th>
                        <th> Applicant Mobile number </th>
                        <th> Status</th>
                        <th>Date </th>                                                                                    
                        </tr>`;
                    var i = 1;

                    $.each(jsonResponse.searchdata, function (index, value) {
                        var arr = value.split("~");
                        str += ` <tr class = "odd gradeX" role = "row" >
                                <td> ` + i + ` </td>
                                <td> ` + arr[0] + ` </td>
                                <td> ` + arr[1] + ` </td>
                                <td> ` + arr[2] + ` </td>
                                <td class = "center" > ` + arr[3] + ` </td>
                                <td class = "center" > ` + arr[4] + ` </td>
                               
                                </tr>`;
                        i++;
                    });
                    var table = ` <table class = "table table-striped table-bordered table-hover table-checkable order-column" id = "searchtab" >
                        <thead> ` + str_head +
                            ` </thead>
                        <tbody> ` +
                            str +
                            ` </tbody>
                        </table>`;
                    if (jsonResponse.showPullBack == true) // if added by pr on 15thmar18
                    {
                        table += "<br><input type='button' id='pull_btn' name='" + reg_no + "'  value='Pull Back Request' />";
                    }
                    //$("#search_heading").html("Status Heirarchy");            
                    $("#search_heading").html("Status Hierarchy"); // line modified by pr on 14thaug18           
                    $("#search_table_id").html(table);
                    $("#searchtab").dataTable();
                });
    } else {
        //alert(errmsg);
        showAlertBox(errmsg)
    }
});



// below function added by pr on 13thmar18
$(document).on('click', '#search_btn', function () {
    var reg_no = $("#sreg_no").val();
    var isValid = true;
    var errmsg = "";
    if (reg_no == "") {
        isValid = false;
        errmsg += "Please enter Registration Number to Search";
    }
    if (isValid) {
        $.getJSON('searchStatusByRegNo', {
            reg_no: reg_no
        },
                function (jsonResponse) {
                    // start, code added by pr on 2ndmay18                    
                    var empDet = "";
                    $.each(jsonResponse.empDetailsHM, function (index, value) {
                        // console.log(" searchStatusByRegNo response index is "+index+" value is "+value);
                        if (value.trim() != "" && index == "employment") {
                            empDet += "<tr><td>Employment</td><td>" + value + "</td></tr>";
                        } else if (value != null && value.trim() != "" && index == "ministry") {
                            empDet += "<tr><td>Ministry</td><td>" + value + "</td></tr>";
                        } else if (value != null && value.trim() != "" && index == "department") {
                            empDet += "<tr><td>Department</td><td>" + value + "</td></tr>";
                        } else if (value != null && value.trim() != "" && index == "state") {
                            empDet += "<tr><td>State</td><td>" + value + "</td></tr>";
                        } else if (value != null && value.trim() != "" && index == "organization") {
                            empDet += "<tr><td>Organization</td><td>" + value + "</td></tr>";
                        } else if (value != null && value.trim() != "" && index == "other_dept") {
                            empDet += "<tr><td>Other</td><td>" + value + "</td></tr>";
                        }
                        if (index == "auth_off_name" && value.trim() != "") {
                            empDet += "<tr><td>Name of the Applicant</td><td>" + value + "</td></tr>";
                        }
                        if (index == "designation" && value.trim() != "") {
                            empDet += "<tr><td>Designation</td><td>" + value + "</td></tr>";
                        }
                        if (index == "emp_code" && value.trim() != "") {
                            empDet += "<tr><td>Employee Code</td><td>" + value + "</td></tr>";
                        }
                        if (index == "address" && value.trim() != "") {
                            empDet += "<tr><td>Postal Address</td><td>" + value + "</td></tr>";
                        }
                        if (index == "add_state" && value.trim() != "") {
                            empDet += "<tr><td>State of Posting</td><td>" + value + "</td></tr>";
                        }
                        if (index == "pin" && value.trim() != "") {
                            empDet += "<tr><td>Pincode</td><td>" + value + "</td></tr>";
                        }
                        if (index == "ophone" && value.trim() != "") {
                            empDet += "<tr><td>Telephone Office</td><td>" + value + "</td></tr>";
                        }
                        if (index == "rphone" && value.trim() != "") {
                            empDet += "<tr><td>Telephone Residence</td><td>" + value + "</td></tr>";
                        }
                        if (index == "mobile" && value.trim() != "") {
                            empDet += "<tr><td>Mobile</td><td>" + value + "</td></tr>";
                        }
                        if (index == "auth_email" && value.trim() != "") {
                            empDet += "<tr><td>Email</td><td>" + value + "</td></tr>";
                        }
                        if (index == "hod_email" && value.trim() != "") {
                            empDet += "<tr><td>Reporting/Nodal/Forwarding Officer Email</td><td>" + value + "</td></tr>";
                        }
                        if (index == "hod_name" && value.trim() != "") {
                            empDet += "<tr><td>Reporting/Nodal/Forwarding Officer Name</td><td>" + value + "</td></tr>";
                        }
                        if (index == "hod_mobile" && value.trim() != "") {
                            empDet += "<tr><td>Reporting/Nodal/Forwarding Officer Mobile</td><td>" + value + "</td></tr>";
                        }
                        if (index == "hod_telephone" && value.trim() != "") {
                            empDet += "<tr><td>Reporting/Nodal/Forwarding Officer Telephone</td><td>" + value + "</td></tr>";
                        }
                        if (index == "ca_desig" && value.trim() != "") {
                            empDet += "<tr><td>Reporting/Nodal/Forwarding Officer Designation</td><td>" + value + "</td></tr>";
                        }

                        // start, code added by pr on 10thjan2020

                        value = value.trim();

                        var val = "";

                        if (index == "type" && value.trim() != "") {

                            if (value == "mail")
                            {
                                val = "Mail User (with mailbox)";
                            } else if (value == "app")
                            {
                                val = "Application User (without mail box(Eoffice-auth))";// line modified by pr on 5thmay2020
                            } else if (value == "eoffice")
                            {
                                val = "e-office-srilanka";
                            }

                            empDet += "<tr><td>Type of Account</td><td>" + val + "</td></tr>";
                        }

                        if (index == "emp_type" && value.trim() != "") {

                            if (value == "emp_regular")
                            {
                                val = "Govt/Psu Official";
                            } else if (value == "consultant")
                            {
                                val = "Consultant";
                            } else if (value == "emp_contract")
                            {
                                val = "FMS Support Staffs";
                            }

                            empDet += "<tr><td>Employment Type</td><td>" + val + "</td></tr>";
                        }

                        // end, code added by pr on 10thjan2020

                    });
                    if (empDet != "") {
                        // start code added by pr on 3rdjan19
                        empDet = empDet + "<br><br><a onClick='generatePdfs('" + reg_no + "')' title='Click here to Download the filled form in PDF'><i class='fa fa-download'></i> Generate Form </a><br><br>";
                        // end code added by pr on 3rdjan19
                        //console.log(empDet)
                        $(".ajaxEmpDetails").html(empDet); // line modified by pr on 20thmar18
                    }
                    // end, code added by pr on 2ndmay18
                    var str_head = "",
                            str = "";
                    str_head = ` <tr>
                        <th> S.No. </th>
                        <th> Status </th>
                        <th> Forwarded By </th>
                        <th> Forwarded To </th>
                        <th> Date </th>
                        <th> Remarks (IF ANY) </th>                                                                                    
                        </tr>`;
                    var i = 1;
                    $.each(jsonResponse.searchdata, function (index, value) {
                        var arr = value.split("~");
                        str += ` <tr class = "odd gradeX" role = "row" >
                                <td> ` + i + ` </td>
                                <td> ` + getStatType(arr[0]) + ` </td>
                                <td> ` + getForwardedToBy(arr[1], arr[2]) + ` </td>
                                <td> ` + getForwardedToBy(arr[3], arr[4]) + ` </td>
                                <td class = "center" > ` + arr[6] + ` </td>
                                <td class = "center" > ` + arr[5] + ` </td>
                                </tr>`;
                        i++;
                    });
                    var table = ` <table class = "table table-striped table-bordered table-hover table-checkable order-column" id = "searchtab" >
                        <thead> ` + str_head +
                            ` </thead>
                        <tbody> ` +
                            str +
                            ` </tbody>
                        </table>`;
                    if (jsonResponse.showPullBack == true) // if added by pr on 15thmar18
                    {
                        table += "<br><input type='button' id='pull_btn' name='" + reg_no + "'  value='Pull Back Request' />";
                    }
                    //$("#search_heading").html("Status Heirarchy");            
                    $("#search_heading").html("Status Hierarchy"); // line modified by pr on 14thaug18           
                    $("#search_table_id").html(table);
                    $("#searchtab").dataTable();
                });
    } else {
        //alert(errmsg);
        showAlertBox(errmsg)
    }
});
// below function added by pr on 14thaug18
$(document).on('click', '#search_frm_btn', function () {
    var skeyword = $("#skeyword").val();
    var srole = $("#srole").val();
    var isValid = true;
    var errmsg = "";
    if (skeyword == "") {
        isValid = false;
        errmsg += "Please enter Keyword to Search";
    }
    if (srole == "") {
        isValid = false;
        errmsg += "Please enter Role to Search";
    }
    if (isValid) {
        $.getJSON('searchStatusByKeyword', {
            skeyword: skeyword,
            srole: srole,
        },
                function (jsonResponse) {
                    //console.log(" jsonResponse.msg value is " + jsonResponse.msg);
                    $("#search_key_heading").html("");
                    $("#key_error").html("");
                    if (jsonResponse.msg == "") {
                        var str_head = "",
                                str = "";
                        str_head = ` <tr>
                        <th> S.No. </th>
                        <th> Registration Number </th>
                        <th> View Status Hierarchy </th>                                            
                        </tr>`;
                        var i = 1;
                        $.each(jsonResponse.searchformdata, function (index, value) {
                            str += ` <tr class = "odd gradeX" role = "row" >
                                <td> ` + i + ` </td>
                                <td> ` + value + ` </td>
                                <td> < a href = "javascript:void(0)" onclick = "showStatus('` + value + `')" > View Status Hierarchy < /a></td >
                                </tr>`;
                            i++;
                        });
                        if (i > 1) // if results are fetched then show the table else show an error message
                        {
                            var table = ` <table class = "table table-striped table-bordered table-hover table-checkable order-column" id = "searchkeytab" >
                        <thead> ` + str_head +
                                    ` </thead>
                        <tbody> ` +
                                    str +
                                    ` </tbody>
                        </table>`;
                            //$("#search_heading").html("Status Heirarchy");            
                            $("#search_key_heading").html("Forms Found !"); // line modified by pr on 14thaug18           
                            $("#keysearch_table_id").html(table);
                            $("#searchkeytab").dataTable();
                        } else {
                            $("#search_key_heading").html("No Forms Found !");
                            $("#search_key_heading").css("color", "red");
                        }
                    } else {
                        $("#key_error").html(jsonResponse.msg);
                        $("#search_key_heading").html("");
                        $("#keysearch_table_id").html("");
                    }
                });
    } else {
        //alert(errmsg);
        showAlertBox(errmsg)
    }
});
$(document).on('click', '#pull_btn', function () {
    // console.log("pull_btn clicked");
    var name = $(this).attr("name");
    if (name != "") // if added by pr on 20thmar18
    {
        // above code commented below added by pr on 22ndmar19
        bootbox.confirm({
            message: "Do you really wish to pull the Request from DA/Coordinator/Admin",
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
                    // console.log(" name of btn element is "+name);    
                    $.getJSON('pullBack', {
                        regNo: name
                    },
                            function (jsonResponse) {
                                // console.log(" inside response from pull back func ");
                                if (jsonResponse.isSuccess == true) {
                                    //alert(" Request Successfully Pulled Back from DA/Coordinator/Admin");  // Admin added by pr on 15thnov18
                                    showAlertBox(" Request Successfully Pulled Back from DA/Coordinator/Admin")
                                    $.getJSON('searchStatusByRegNo', {
                                        reg_no: name
                                    },
                                            function (jsonResponse) {
                                                var str_head = "",
                                                        str = "";
                                                str_head = ` <tr>
                                                <th> S.No. </th>
                                                <th> Status </th>
                                                <th> Forwarded By </th>
                                                <th> Forwarded To </th>
                                                <th> Date </th>
                                                <th> Remarks (IF ANY) </th>                                                                                    
                                                </tr>`;
                                                var i = 1;
                                                $.each(jsonResponse.searchdata, function (index, value) {
                                                    var arr = value.split("~");
                                                    str += ` <tr class = "odd gradeX" role = "row" >
                                                        <td> ` + i + ` </td>
                                                        <td> ` + getStatType(arr[0]) + ` </td>
                                                        <td> ` + getForwardedToBy(arr[1], arr[2]) + ` </td>
                                                        <td> ` + getForwardedToBy(arr[3], arr[4]) + ` </td>
                                                        <td class = "center" > ` + arr[6] + ` </td>
                                                        <td class = "center" > ` + arr[5] + ` </td>
                                                        </tr>`;
                                                    i++;
                                                });
                                                var table = ` <table class = "table table-striped table-bordered table-hover table-checkable order-column" id = "searchtab" >
                                                <thead> ` + str_head +
                                                        ` </thead>
                                                <tbody> ` +
                                                        str +
                                                        ` </tbody>
                                                </table>`;
                                                if (jsonResponse.showPullBack == true) // if added by pr on 15thmar18
                                                {
                                                    table += "<br><input type='button' id='pull_btn' name='" + reg_no + "'  value='Pull Back Request' />";
                                                }
                                                //$("#search_heading").html("Status Heirarchy");            
                                                $("#search_heading").html("Status Hierarchy"); // line modified by pr on 14thaug18           
                                                $("#search_table_id").html(table);
                                                $("#searchtab").dataTable();
                                            });
                                } else {
                                }
                            });
                }
            }
        });
    }
});
// bellow code added  by pr on 16thmar18
$(document).on('click', '#search_close_btn', function () {
    // console.log("search_close_btn clicked");

    //$("#search_regno_modal").modal("toggle"); // line commented by pr on 27thfeb18
    $("#search_regno_modal").modal("hide");
    var action = $("#action_hidden").val();
    //setAction(action)  // line commented by pr on 23rdoct18
    // start, code added by pr on 23rdoct18
    var table = $('#example').DataTable();
    //console.log(" just before the datatable reload call ");
    table.ajax.reload(null, false);
    // end, code added by pr on 23rdoct18
    // end, code added by pr on 25thmay18

});
// bellow code added  by pr on 18thjun18
$(document).on('click', '#query_close_btn', function () {
    //console.log(" inside query close btn click function ");
    //$("#query_raise_modal").modal("hide");
    var action = $("#action_hidden").val();
    //setAction(action) // line commented by pr on 23rdoct18
    // start, code added by pr on 23rdoct18
    var table = $('#example').DataTable();
    //console.log(" just before the datatable reload call ");
    table.ajax.reload(null, false);
    // end, code added by pr on 23rdoct18
});

function clearSearch() {
    $("#sreg_no").val("");
    $("#search_heading").html("");
    $("#search_table_id").html("");
    $(".ajaxEmpDetails").html(""); // line added by pr on 14thaug18 
    $(".modal-title").html("Search by Registration Number OR Keyword"); // line added  by pr on 28thsep18
    // start, code added by pr on 17thaug18
    $("#search_key_heading").html("");
    $("#keysearch_table_id").html("");
    $("#key_error").html("");
    $("#skeyword").val("");
    $("#srole").find("option").remove();
    // end, code added by pr on 17thaug18
}
// below function added by p r on 13thmar18
function getStatType(value) {
    var stat_type = "";
    if (value == 'ca_pending') {
        stat_type = "Pending with RO/Nodal/FO";
    } else if (value == 'ca_rejected') {
        stat_type = "Rejected by RO/Nodal/FO";
    } else if (value == 'support_pending') {
        stat_type = "Pending with Support";
    } else if (value == 'support_rejected') {
        stat_type = "Rejected by Support";
    } else if (value == 'coordinator_pending') {
        stat_type = "Pending with Coordinator";
    } else if (value == 'coordinator_rejected') {
        stat_type = "Rejected by Coordinator";
    } else if (value == 'mail-admin_pending') {
        stat_type = "Pending with Admin";
    } else if (value == 'mail-admin_rejected') {
        stat_type = "Rejected by Admin";
    } else if (value == 'da_pending') {
        stat_type = "Pending with DA-Admin";
    } else if (value == 'da_rejected') // else if added by pr on 27thjun18
    {
        stat_type = "Rejected by DA-Admin";
    } else if (value == 'manual') {
        stat_type = "Manual";
    } else if (value == 'manual_upload') {
        stat_type = "Pending with User";
    } else if (value == 'completed') {
        stat_type = "Completed";
    } else if (value == 'cancel') {
        stat_type = "Cancelled";
    } else if (value == 'api') // else if added  by pr on 24thaug18
    {
        stat_type = "API";
    } else if (value == 'domainapi') // else if added  by pr on 24thaug18
    {
        stat_type = "API";
    } else if (value == 'us_pending') // start,code added  by pr on 8thjan19, 14thjan19
    {
        stat_type = "Pending with Under Secretary and above";
    } else if (value == 'us_rejected') {
        stat_type = "Rejected by Under Secretary and above";
    } else if (value == 'us_expired') {
        stat_type = "Under Secretary and above Link Timeout";
    } // end, code added  by pr on 8thjan19, 14thjan19
    return stat_type;
}
// below function added by p r on 13thmar18
function getForwardedToBy(value, email) {
    var stat_type = "-";
    if (value != null && value != "") {
        if (value == 'ca') {
            stat_type = "Reporting/Nodal/Forwarding Officer";
        } else if (value == 's') {
            stat_type = "Support";
        } else if (value == 'c') {
            stat_type = "Coordinator";
        } else if (value == 'm') {
            stat_type = "Admin";
        } else if (value == 'd') {
            stat_type = "DA-Admin";
        } else if (value == 'us') // else if added by pr on 8thjan19
        {
            stat_type = "Under Secretary";
        } else if (value == 'a') // else if added by pr on 1stfeb19
        {
            stat_type = "User";
        }
        //console.log(" email value is "+email);
        if (email != null && email != "" && email != "null") // email not null string added by pr on 8thfeb19
        {
            stat_type += "(" + email + ")";
        }
    }
    return stat_type;
}
// below function added by pr on 2ndapr18
$(document).on('click', '#bulk_upload', function () {

    // start, code added  by pr on 17thdec19
    $(".loadergif").show();
    $("#bulk_upload").hide();
    // end, code added  by pr on 17thdec19

    // console.log(" file uploaded ");
    var val = $("#csv_file").val();
    // console.log(" val is "+val);
    if (val != "") {
        var file = $('input[name="csv_file"]').get(0).files[0];
        var fileType = file.type;
        var fileSize = file.size;
        var fileName = file.name;
        var filenameArr = fileName.split(".");
        var ext = filenameArr[1];
        var csrf = $('#CSRFRandom').val();
        //  console.log(" filetype value is "+fileType+" file name is "+fileName);
        //var match = ["text/csv", "application/vnd.ms-excel"]; // for multiple take comma separated values
        var match = ["text/csv", "application/vnd.ms-excel"]; // for multiple take comma separated values
        if ((fileType === match[0] || fileType === match[1]) && ext == "csv") {
            //  console.log(" inside flitype matches ");
            if (fileSize <= 1000000) {
                var formData = new FormData();
                formData.append('csv_file', file);
                formData.append('CSRFRandom', csrf);
                $.ajax({
                    url: 'updateBulkUpload',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);
                        if (jsonvalue.csrf_error) {
                            alert(jsonvalue.csrf_error);
                        }
                        resetCSRFRandom();
                        // console.log(" isSuccess "+jsonvalue.isSuccess+" isError "+jsonvalue.isError+" msg "+jsonvalue.msg);
                        $("#bulk_errmsg").html("");
                        if (jsonvalue.isError == true) {
                            var str = "<table><tr><th>S No.</th><th>Bulk ID , Error Message</th></tr>";
                            var i = 1;
                            $.each(jsonvalue.errorArr, function (index, value) {
                                //  console.log(" inside arraylist response index is "+index+" value is "+value);
                                str += ` <tr> <td> ` + i + ` </td><td>` + value + `</td > </tr>`
                                i++;
                            });
                            str += ` </table>`;
                            $("#bulk_errmsg").html(str);
                        }
                        if (jsonvalue.msg != "") {
                            $("#csv_file_error").html(jsonvalue.msg);
                        }

                        // start, code added  by pr on 17thdec19
                        $("#bulk_upload").show();
                        $(".loadergif").hide();
                        // end, code added  by pr on 17thdec19

                    },
                    error: function (jqXHR, textStatus, errorThrown, data) {
                       var json2 = JSON.stringify(jsonResponse);
                                                var jsonvalue = JSON.parse(json2);
                                                var error = JSON.parse(jsonvalue.responseText.substring(0, jsonvalue.responseText.indexOf("}") + 1)).error;
                                                $('#new_alert .modal-body').html(error);
                                                $('#new_alert').modal('show');
                                                $('.loader').hide();
                        console.log('error');

                        // start, code added  by pr on 17thdec19
                        $("#bulk_upload").show();
                        $(".loadergif").hide();
                        // end, code added  by pr on 17thdec19

                    }
                });
            } else {
                //alert(" File should be less than 1 MB ");
                showAlertBox(" File should be less than 1 MB ");
                // start, code added  by pr on 17thdec19
                $("#bulk_upload").show();
                $(".loadergif").hide();
                // end, code added  by pr on 17thdec19
            }
        } else {
            // console.log(" inside flitype  DOESNT  match ");
            //alert(" Please upload a file in CSV Format ");
            showAlertBox(" Please upload a file in CSV Format ");
            // start, code added  by pr on 17thdec19
            $("#bulk_upload").show();
            $(".loadergif").hide();
            // end, code added  by pr on 17thdec19
        }
    } else {
        //alert(" Please upload a CSV file ");
        showAlertBox(" Please upload a CSV file ");
        // start, code added  by pr on 17thdec19
        $("#bulk_upload").show();
        $(".loadergif").hide();
        // end, code added  by pr on 17thdec19
    }

    // start, code added  by pr on 17thdec19
//    $("#bulk_upload").show();
//    $(".loadergif").hide();
    // end, code added  by pr on 17thdec19

});

// below function modified by pr on 1stjun18 for bulk users, modified by pr on 25thjul19, replaced by pr on 24thsep19
$(document).on('click', '#bulk_reject_btn', function ()
{
    var regNo = $(this).val();

    var reg_no = "";

    var reg_no_arr = regNo.split("~");

    if (reg_no_arr[0] != null)
    {
        reg_no = reg_no_arr[0];
    }

    /*if (reg_no_arr[1] != null)
     {
     form_name = reg_no_arr[1];
     }*/


    var bulk_id = new Array();

    var i = 0;

    var id = ""; // line added by pr on 30thmay18

    var id_arr = ""; // line added by pr on 30thmay18

    $("input[name='bulk_id']").each(function ()
    {
        if ($(this).is(":checked"))
        {
            id_arr = $(this).val().split("~");

            id = id_arr[0];

            //bulk_id[i] = $(this).val();

            bulk_id[i] = id;

            i++;
        }

    });

    // start, code added by pr on 23rdjul19

    var role = $("#role").val();

    //console.log(" role value inside bulk_reject_btn click functionality is "+role);

    var statRemarks = "";

    if (role == "co" || role == "ca") // ca added by pr on 6thdec19
    {
        statRemarks = $("#statRemarksView").val();

        //console.log(" statRemarks inside role co is "+statRemarks);        
    }

    var form_name = "";

    if (reg_no_arr != null && reg_no_arr[1] != null)
    {
        form_name = reg_no_arr[1];
    }

    //console.log(" statRemarks value is "+statRemarks+" for role as "+role+" form_name value is "+form_name);

    // end, code added by pr on 23rdjul19

    // console.log(" bulk_ids value is "+bulk_id+" bulk_id length is "+bulk_id.length);  

    if (bulk_id != null && bulk_id.length > 0)
    {
        if (((role == "co" || role == "ca") && statRemarks != "") || role == "admin") // role ca added by pr on 6thdec19
        {
            // start, code added by pr on 25thjul19

            var msg = "";

            if ((role == "co" || role == "ca") && statRemarks != "") // ca added by pr on 6thdec19
            {
                msg = "If you reject all the records then the request would be rejected altogether. Do you wish to proceed ?";
            }

            // end, code added by pr on 25thjul19

            bulk_id = bulk_id.join();

            // above code commented below added by pr on 22ndmar19
            bootbox.confirm({
                message: "Do you really wish to Reject the selected records." + msg, // line modified by pr on 25thjul19
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

                    // console.log(" inside rejectbtn callback result is "+result);

                    if (result)
                    {
                        $("#bulk_reject_btn").hide(); // hide create button and show loader img

                        $(".loadergif").show();
                        $(".loader").show(); // show this image

                        // end, code added by pr on 19thapr18

                        $.post('rejectBulkID',
                                {
                                    bulk_id: bulk_id,
                                    regNo: reg_no,
                                    statRemarks: statRemarks // line added by pr on 23rdjul19
                                },
                                function (jsonResponse)
                                {
                                    $("#bulk_reject_btn").show();

                                    $(".loadergif").hide();
                                    $(".loader").hide();

                                    if (role == "admin") // if around added by pr on 23rdjul19
                                    {
                                        //console.log(" inside bulk_reject_btn click functionality admin role is "+role);

                                        var sub = "";

                                        var showMore = "";

                                        // end, code added by pr on 29thmay18

                                        if (jsonResponse.isSuccess == true)
                                        {
                                            //alert(jsonResponse.msg);

                                            showAlertBox(jsonResponse.msg) // line modified by pr on 22ndmar19

                                            $("#bulk_users_tbody").hide(); // line added by pr on 30thmay18

                                            $.getJSON('fetchBulkUsers',
                                                    {
                                                        reg_no: reg_no
                                                    },
                                                    function (jsonResponse)
                                                    {
                                                        $("#check_all").prop("checked", false); // line added by pr on 1stjun18

                                                        var flag = true;

                                                        //  console.log(" insinde getbulkusers jsonresponse ");

                                                        // start, code added by pr on 29thmay18

                                                        var sub = "";

                                                        var showMore = "";

                                                        var str = "";

                                                        var checkboxBulkId = ""; // line added by pr on 29thmay18


                                                        var k = 1; // line added by pr on 18thapr18
                                                        $.each(jsonResponse.bulkUsersdata, function (index, value)
                                                        {
                                                            //console.log(" inside bulkusers data each record ");

                                                            showMore = "";

                                                            if (value.reject_remarks != "" && value.reject_remarks.trim() != "")
                                                            {
                                                                if (value.reject_remarks.trim().length >= 10)
                                                                {
                                                                    sub = value.reject_remarks.substring(0, 9);

                                                                    showMore = '<span class="moreb" id="moreb_' + value.bulk_id + '" style="color:blue;cursor:pointer;"> ' + sub + ' more...</span><span id="complete_' + value.bulk_id + '" class="complete" style="display:none;">' + value.reject_remarks + '</span>';
                                                                } else
                                                                {
                                                                    sub = value.reject_remarks;

                                                                    showMore = sub;
                                                                }

                                                            }

                                                            checkboxBulkId = value.bulk_id;

                                                            if (value.mobile_ldap_email != "" && value.mobile_ldap_email.trim() != "")
                                                            {
                                                                checkboxBulkId = checkboxBulkId + "~y";
                                                            } else
                                                            {
                                                                checkboxBulkId = checkboxBulkId + "~n";
                                                            }

                                                            // id added for tr by pr on 24thdec18
                                                            str += `<tr class="odd gradeX togl" role="row" id = "tr_` + value.bulk_id + `">

                                                                                <td >` + k + `</td>
                                                                                <td >` + value.bulk_id + `</td>
                                                                                <td contenteditable="true" class="uid" id="` + value.bulk_id + `">` + value.uid + `</td>
                                                                                <td contenteditable="true" class="mail" id="` + value.bulk_id + `">` + value.email + `</td>
                                                                                <td>` + value.mobile + `</td>
    
                                                                                <td><span id="uid_exist_` + value.bulk_id + `">` + value.mobile_ldap_email + `</span></td>
                    
                                                                                <td contenteditable="true" class="reject_remarks" id="` + value.bulk_id + `">` + showMore + `</td>
    
                                                                                <td class="center">` + value.createdon + `</td>
                                                                                <td>
                                                                                    <input type="checkbox" name="bulk_id"  onclick="checkNumChecked();" id="check_` + value.bulk_id + `"   value="` + checkboxBulkId + `" />
                                                                                </td>
                                                                            </tr>`; // email duplicates added by pr on 3rdapr18, id added for checkbox on 24thdec18


                                                            k++; // line added by pr on 18thapr18 and above str modified

                                                            flag = false;

                                                        });

                                                        //console.log(" inside rejectbtn function flag value to show the modal or not is "+flag);

                                                        if (flag) // if there are no records left to be processed then reload the page
                                                        {
                                                            $("#bulk_modal").modal("toggle");

                                                            $("#bulk_modal").modal("hide");

                                                            var action = $("#action_hidden").val();

                                                            //setAction(action); // line commented by pr on 23rdoct18

                                                            // start, code added by pr on 23rdoct18

                                                            var table = $('#example').DataTable();

                                                            // console.log(" just before the datatable reload call ");

                                                            table.ajax.reload(null, false);

                                                            // end, code added by pr on 23rdoct18

                                                        } else
                                                        {
                                                            $("#bulk_users_tbody").html(str);

                                                            $("#bulk_users_tbody").show(); // line added by pr on 30thmay18 , so that when the data is updated then only it should get displayed
                                                        }


                                                    });

                                        } else if (jsonResponse.isError == true)
                                        {
                                            //alert(jsonResponse.msg);

                                            showAlertBox(jsonResponse.msg) // line modified by pr on 22ndmar19
                                        }

                                    } // code separated based on role as admin or co
                                    else
                                    {
                                        showAlertBox(jsonResponse.msg)

                                        //console.log(" inside bulk_reject_btn click functionality co role is "+role);

                                        prefillViewBulkData(reg_no, form_name);

                                        var table = $('#example').DataTable();

                                        //console.log(" just before the datatable reload call ");

                                        table.ajax.reload(null, false);

                                    }

                                }, 'json');
                    }

                }
            });

        } // if around and below else added by pr on 23rdjul19
        else
        {
            showAlertBox(" Please enter Reject Remarks ") // line modified by pr on 22ndmar19
        }

    } else
    {
        //alert(" Please select atleast one record to Reject ");

        showAlertBox(" Please select atleast one record to Reject ") // line modified by pr on 22ndmar19
    }

});



// below function added by pr on 6thapr18
$(document).on('click', '#query_btn', function () {
    var CSRFRandom = $("#CSRFRandom").val();  // audit by nikki


    var flag = true;
    var errmsg = "";
    var query = $("#query").val();
    var choose_recp = $("#choose_recp").val();
    var choose_recp_str = "";
    if (query == "") {
        flag = false;
        errmsg = " Please enter a Query Message.";
    }
    if (choose_recp == "") {
        flag = false;
        errmsg = " Please Select a Recipient.";
    } else {
        if (choose_recp == "u") {
            choose_recp_str = "Applicant";
        } else if (choose_recp == "ca") {
            choose_recp_str = "Reporting/Nodal/Forwarding Officer";
        } else if (choose_recp == "sup") {
            choose_recp_str = "Support";
        } else if (choose_recp == "co") {
            choose_recp_str = "Coordinator";
        } else if (choose_recp == "m") {
            choose_recp_str = "Admin";
        }
    }
    var value = $("#query_btn").val();
    // console.log(" query btn value is "+value);
    if (value == "") {
        flag = false;
        errmsg = " Reg No is not valid. ";
    }
    if (flag) {
        // above code commented below added by pr on 22ndmar19
        bootbox.confirm({
            message: "Do you really wish to Raise/Respond a Query to the " + choose_recp_str,
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
                    $("#query_btn").hide();
                    $(".loadergif").show();
                    $(".loader").show();

                    $.ajax({url: 'raiseQuery', type:'post', data: {
                        regNo: value,
                        choose_recp: choose_recp,
                        statRemarks: query,
                        CSRFRandom: CSRFRandom
                    },
                            success: function (jsonResponse) {
                                if (jsonResponse.isSuccess == true) {
                                    showAlertBox(jsonResponse.msg)
                                    prefillQuery();
                                    $("#query_btn").show();
                                    $(".loadergif").hide();
                                    $(".loader").hide();
                                } else if (jsonResponse.isError == true) {
                                    showAlertBox(jsonResponse.msg)
                                    $("#query_btn").show();
                                    $(".loadergif").hide();
                                    $(".loader").hide();
                                }
                                resetRandom();
                            }, error: function(jsonResponse){
                                          var json2 = JSON.stringify(jsonResponse);
                                                var jsonvalue = JSON.parse(json2);
                    var error = JSON.parse(jsonvalue.responseText.substring(0,jsonvalue.responseText.indexOf("}")+1)).error;
                    $('#new_alert .modal-body').html(error);
                    $('#new_alert').modal('show');
                    $('.loader').hide();} ,
datatype: 'json'});
                }
            }
        });
    } else {
        showAlertBox(errmsg)
    }
});
// below code added by pr on 12thapr18
$('#esign_2admin').click(function () {
    // console.log(" inside esign_2admin click block ");
    var otp = $('#otp-aadhaar').val();
    // console.log(" inside esign_2admin click block otp entered value is "+otp);
    if (otp === null || otp === '' || !otp.match(/^[0-9]{6}$/)) {
        $('#otp-aadhaar_error').text('Enter OTP sent to your mobile number registered with aadhaar');
        $('#esign_img2').hide();
        $('#esign_2admin').show();
    } else {
        $('#esign_img2').show();
        $('#esign_2admin').hide();
        $.ajax({
            type: "POST",
            url: "OtpAadhaarAdmin",
            data: {
                otp: otp
            },
            //datatype: JSON,
            success: function (aadhaar) {
                var myJSON = JSON.stringify(aadhaar);
                var jsonvalue = JSON.parse(myJSON);
                if (jsonvalue.esignreturn.aadhaarreturn === 'fail') {
                    showAlertBox("Please try again Later !")
                } else if (jsonvalue.esignreturn.aadhaarreturn === 'wrongOTP') {
                    $('#otp-aadhaar_error').text(jsonvalue.otpErrMsg);
                    $('#esign_img2').hide();
                    $('#esign_2admin').show(); // line modified by pr on 13thapr18
                } else if (jsonvalue.esignreturn.aadhaarreturn === 'done') {
                    // $('#esign').toggle();  
                    // above line commented below two added by pr on 6thjun18
                    $("#esign").modal("hide");
                    $("#forward_modal").modal("hide");
                    var role = $("#role").val();
                    var value = $("#fwd_wo_ad").attr("value");
                    var random = $("#random").val();
                    if (role == 'ca') {
                        // below code modified by pr on 4thjun18
                        //console.log(" jsonvalue.esignreturn.esignedFileName value is " + jsonvalue.esignreturn.esignedFileName);
                        $("#app_ca_path").val(jsonvalue.esignreturn.esignedFileName);
                        $("#statRemarksApp").val(""); // line added by pr on 21stjun18
                        $("#approve_modal").modal(); // code eliminated and modal is opened 
                    }
                } else {
                    showAlertBox("Please try again!! response is " + jsonvalue.esignreturn.aadhaarreturn)
                }
            },
            error: function () {
                console.log(" inside OtpAadhaarAdmin error as response ");
                //alert("details are wrong!!")
                showAlertBox("details are wrong!!")
            }
        });
    }
});
// below code added by pr on 12thapr18
$(".closecls").on("click", function () {
    //alert(" close btn clicked ");
    //$("#esign").modal(toggle);
    $('#esign').modal('hide');
    //$("#forward_modal").modal('toggle'); // to close this modal
    $("#forward_modal").modal('hide'); // to close this modal
});
// below function added by pr on 16thaug18
function showStatus(reg_no) {
    //console.log(" inside show status function reg no value is " + reg_no);
    $("#sreg_no").val(reg_no);
    $("#search_btn").click();
    $("#sreg_no").focus();
}

function showRole(keyword) {
    //console.log(" keyword entered is " + keyword);
    var expr = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
    var phoneno = /^\d{1,12}$/;
    var select = $("#srole");
    var opt = "";
    select.find('option').remove();
    if (expr.test(keyword)) {
        //console.log(" inside email valid ");
        opt = "<option value=''>Select</option><option value='user'>Applicant</option><option value='ca'>Reporting Officer</option><option value='co'>Coordinator</option><option value='admin'>Admin</option>";
        select.append(opt);
    } else if (keyword.match(phoneno)) {
        // console.log(" mobile valid ");
        opt = "<option value=''>Select</option><option value='user'>Applicant</option><option value='ca'>Reporting Officer</option>";
        select.append(opt);
    } else {
        // console.log(" inside email NOT valid ");
        // below line modified by pr on 17thaug18
        opt = "<option value=''>Select</option><option value='user'>Applicant</option><option value='ca'>Reporting Officer</option><option value='co'>Coordinator</option><option value='da'>DA-Admin</option><option value='admin'>Admin</option>";
        select.append(opt);
    }
}
// below function added by pr on 20thmar19
function showAlertBox(text) {
    //console.log(" inside show alert box function ");
    bootbox.alert(text, function () {
        //console.log("Alert Callback");
    });
}
// below code added by pr on 7thdec18 for wifi
$(document).on("click", "#update_wifi_att", function (e) {
    var wifi_value = $("#wifi_value").val();
    var mark_done_btn = $("#mark_done_btn").val();
    var arr = mark_done_btn.split("~");
    var reg_no = arr[0];
    if (arr[0] != null) {
        reg_no = arr[0];
    }
//    console.log(" inside update_wifi_att button clicked  wifi_value  " + wifi_value + " reg_no " + reg_no);
//    console.log(" regNo " + reg_no + " wifi_value " + wifi_value);
    bootbox.confirm({
        message: "Are you sure you want to Update WIFI Attribute in LDAP and/or Generate Certificate & Send Mail to VPN Team",
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
                    type: "POST",
                    url: "fetchwifivalue",
                    data: {
                        regNo: reg_no + "~wifi~create",
                        wifi_value: wifi_value
                    },
                    success: function (jsonResponse) {
                        var showVPNBtn = false;
                        // start, code added by pr on 29thnov18 for wifi                       
                        var wifi_value = jsonResponse.wifi_value;
                        var wifi_process = "";
                        var cert_mail_sent = "";
                        var isIOS = "No"; // line added by pr on 4thdec18
                        if (jsonResponse.wifi_value.indexOf("~") >= 0) {
                            var arr = jsonResponse.wifi_value.split("~");
                            if (arr.length >= 1 && arr[0] != null) {
                                wifi_value = arr[0];
                            }
                            if (arr.length >= 2 && arr[1] != null) {
                                if (arr[1] == "certificate") {
                                    showVPNBtn = true;
                                }
                                wifi_process = arr[1];
                            }
                            if (arr.length >= 3 && arr[2] != null) // isIOS
                            {
                                if (arr[1] == "request" && arr[2] == "y") {
                                    //console.log(" inside request and isIOS y ");
                                    showVPNBtn = true;
                                }
                                if (arr[2] == "y") {
                                    arr[2] = "Yes";
                                    isIOS = "Yes"; // line added by pr on 4thdec18
                                } else {
                                    arr[2] = "No";
                                    isIOS = "No"; // line added by pr on 4thdec18
                                }
                            }
                            if (arr.length >= 4 && arr[3] != null) // isMailSent
                            {
                                cert_mail_sent = arr[3];
                                if (cert_mail_sent == "y") {
                                    cert_mail_sent = "Yes";
                                    cert_mail_sent = "<span style='color:green;font-weight:bold;'>Yes</span>";
                                } else {
                                    cert_mail_sent = "No";
                                    cert_mail_sent = "<span style='color:red;font-weight:bold;'>No</span>";
                                }
                            }
                        }
                        var iosStr = ""; // line added by pr on 4thdec18
                        if (wifi_process == "request") // if block added by pr on 4thdec18
                        {
                            iosStr = "<br><br>IS OS AN IOS: <span style='font-weight:bold;'>" + isIOS + "</span>";
                        }
                        $("#wifi_process").html("Type of Request: <span style='font-weight:bold;'>" + wifi_process.toUpperCase() + "</span>" + iosStr); // line modified by pr on 4thdec18
                        $("#cert_mail_sent").html("Mail sent to VPN Team: " + cert_mail_sent);
                        $("#wifi_att").html("WIFI Attribute value : <span style='font-weight:bold;'>" + wifi_value + "</span>");
                        $("#wifi_select").show();
                        $("#wifi_value option[value='" + wifi_value + "']").attr("selected", "selected");
                        $("#imap_mad").hide();
                    },
                    error: function () {
                        showAlertBox("Attribute couldnot be updated in the LDAP")
                    }
                });
            }
        }
    });
});
$('.queryRaise').on('click', function (e) {
    //$(document).on('click', '.queryRaise', function () {
    var action_type = "query_raise";
    $('#queryRaiseModel').modal('show', {
        backdrop: 'static'
    });
    var ref_no = $(this).closest('tr').children('td:first').text();
    var whichform = ref_no.substring(0, ref_no.indexOf('-'));
    var showEdit = $(this).attr("id"); // to show /hide edit on preview based on stat_type
    //
    var str = ref_no + "~" + whichform + "~" + action_type; // first reg_no then form type then action type(reject or forward)
    var heading = "Action";
    var role = $("#role").val(); // line added by pr on 22ndmar18
    //console.log(" app_type value is "+app_type+" role is "+role);
    $("#fwd_wo_ad").val(str);
    // start, code added by pr on 4thjun18
    $("#approve_btn").val(str);
    $("#fwd_coord_btn").val(str);
    $("#add_coord_btn").val(str);
    // end, code added by pr on 4thjun18
    $("#approve_modal .modal-title").text("Approve Action for Reg No. - " + ref_no); //line added by pr on 29thjun18
    if (action_type.trim() == 'query_raise') // else if added by pr on 9thapr18
    {
        heading = "Query Raise";
        $("#query_btn").val(str);
        //console.log(" inside apply heading func query btn value is "+$("#query_btn").val() );
        prefillQuery(); // populate the drop down with the options like  the admins involved for this registration no.
        $("#query_raise .modal-title").text(heading + " for Reg No. - " + ref_no); //line added by pr on 31stmay18
    }
    //    $('#form_val').val(whichform)
    //    $('#ref_no').val(ref_no)
    //    $('#showQueryAdd').show();
});

function apply_heading(reg_no, stat_form_type, action_type, app_type) {
    var str = reg_no + "~" + stat_form_type + "~" + action_type; // first reg_no then form type then action type(reject or forward)
    var heading = "Action";
    var role = $("#role").val();
    $("#fwd_wo_ad").val(str);
    $("#approve_btn").val(str);
    $("#fwd_coord_btn").val(str);
    $("#add_coord_btn").val(str);
    $("#approve_modal .modal-title").text("Approve Action for Reg No. - " + reg_no); //line added by pr on 29thjun18
    $("#app_type").val(app_type);
    if (action_type == 'forward') {

        if (role == "ca") {
            if (app_type.indexOf("Manual") < 0) // in case of esign or online
            {
                $("#forward_modal").modal();
            } else {
                $("#app_ca_type").val("manual_upload");
                $("#statRemarksApp").val(""); // line added by pr on 21stjun18
                $("#approve_modal").modal();
            }
        } else if (role == "sup") {
            $("#forward_modal").modal();
            $("#forward_esign_div").hide();
            $("#forward_from_support_div").show();
        } else {
            document.getElementById('fwd_wo_ad').click();
        }
    } else if (action_type == 'reject') {
        heading = "Reject Action";
        $("#statRemarksError").text('')
        $("#statRemarks").val("");
        $("#reject_btn").val(str);
        $("#reject_modal .modal-title").text(heading + " for Reg No. - " + reg_no);
    } else if (action_type == 'create') {
        heading = "Create ID/Mark as Done";
        $(".statRemarks").val("");// line added by pr on 9thjan2020
        if (stat_form_type == 'single' || stat_form_type == 'nkn_single' || stat_form_type == 'gem') {
            $("#create_email_div").show();
            $("#create_sms_div").hide();
            $("#mark_done_div").hide();
            $("#create_email_btn").val(str);
            $("#final_email_id").val("");
            $("#primary_id").val("");
            $("#alias_id").val("");
            $("input[name='description']").prop("checked", false);
            $(".emp_type_span").html("");
            $("#pref_email_span").html("");// two lines added by pr on 4thjun2020
            prefillData();
        } else if (stat_form_type == 'sms') {
            $("#create_sms_div").show();
            $("#create_email_div").hide();
            $("#mark_done_div").hide();
            $("#create_sms_btn").val(str);
            $("#final_sms_id").val("");
            $("input[name='description']").prop("checked", false);
        } else if (stat_form_type == 'bulk') {
            $("#create_modal").modal("toggle");
            window.location.href = "showLinkData";
        } else {
            $("#mark_done_div").show();
            $("#create_sms_div").hide();
            $("#create_email_div").hide();
            $("#mark_done_btn").val(str);
            $("#doneRemarks").val("");
            $("#wifi_att").html("");
            $("#wifi_select").val("");
            $("#wifi_select").hide();
            prefillData();
        }
        $("#create_modal .modal-title").text(heading + " for Reg No. - " + reg_no); //line added by pr on 31stmay18
    } else if (action_type.trim() == 'bulk') {
        heading = "Bulk Users";
        // start, code added by pr on 28thmay18
        $("#bulkUpdateMessage").html("");
        $("#bulkUpdateMessage").hide();
        // end, code added by pr on 28thmay18
        $("input[name='description']").prop("checked", false); // line added by pr on 1stjun18
        $(".statRemarksBulk").val("");// line added by pr on 9thjan2020
        prefillBulkData(reg_no, stat_form_type); // second param added by pr on 27thdec17
        $("#bulk_create_btn").val(str);
        $("#bulk_reject_btn").val(str); // line added by pr on 19thapr18
        $("#bulk_modal .modal-title").text(heading + " for Reg No. - " + reg_no); //line added by pr on 31stmay18 
    } else if (action_type.trim() == 'query_raise') // else if added by pr on 9thapr18
    {
        $('#query_raise').modal();
        heading = "Query Raise";
        $("#query_btn").val(str);
        prefillQuery();
        $("#query_raise .modal-title").text(heading + " for Reg No. - " + reg_no);
    }
    $('#basic_track').modal('hide');
}

function putOnHold() {
    var type = $("#hold_type").val();
    var regNo = $("#hold_reg_no").val();
    var csrf = $('#CSRFRandom').val();
    var statRemarks = "";
    var msg = "";
    if (type == "true") {
        msg = "Are you sure you want to put the request On hold";
        statRemarks = $("#hold_statRemarks").val();
    } else {
        msg = "Are you sure you want to put the request Off hold";
    }
    bootbox.confirm({
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


                $("#onhold_modal").modal('hide');
                $.ajax({
                    type: "POST",
                    url: "putOnHold",
                    data: {
                        regNo: regNo,
                        type: type,
                        CSRFRandom: csrf,
                        statRemarks: statRemarks
                    },
                    success: function (data) {

                        if (data.csrf_error) {
                            alert(data.csrf_error);
                        }
                        resetCSRFRandom();
                        if (data.isSuccess) {
                            showAlertBox(data.msg);
                        } else {
                            showAlertBox(data.msg);
                        }
                        fetchCounts();
                        var table = $('#example').DataTable();
                        table.ajax.reload(null, false);
                    },
                    error: function (data) {
                        var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    var error = JSON.parse(jsonvalue.responseText.substring(0,jsonvalue.responseText.indexOf("}")+1)).error;
                    $('#new_alert .modal-body').html(error);
                    $('#new_alert').modal('show');
                    $('.loader').hide();
                    console.log('error');
                    }
                });
            }
        }
    });
}
// below function added by pr on 23rdaug19
function showonhold(regNo, type) {
    $("#onhold_modal").modal();
    $("#hold_type").val(type);
    $("#hold_reg_no").val(regNo);
    if (type == "false") // just show the information
    {
        $.ajax({
            type: "POST",
            url: "fetchRemarks",
            data: {
                regNo: regNo
            },
            datatype: JSON,
            success: function (jsonResponse) {
                var remarks = jsonResponse.statRemarks;
                $("#show_remark").html(remarks);
            },
            error: function () {
                console.log("error")
            }
        });

        $("#onhold_title").html("Put Off Hold Action for Reg No. - " + regNo);

        $("#onhold_btn").html("Put Off Hold");
        $("#onhold_show_remark").show();
        $("#onhold_add_remark").hide();
    } else // show the textarea to add remarks
    {
        $("#onhold_title").html("Put On Hold Action for Reg No. - " + regNo + " ( Add Remarks ) ");
        $("#onhold_btn").html("Put On Hold");

        $("#hold_statRemarks").val("");
        $("#onhold_add_remark").show();
        $("#onhold_show_remark").hide();
        //console.log(" inside type as true ");
    }
}

// below function added by pr on 6thjan2020
function showonhold(regNo, type)
{
    console.log(" inside putOnHold func  regNo value is " + regNo + " type value is " + type);

    // show onhold_modal and based on  type true or false show the textarea and button or show only info

    $("#onhold_modal").modal();

    $("#hold_type").val(type);

    $("#hold_reg_no").val(regNo);

    if (type == "false") // just show the information
    {
        //$("#onhold_modal .statRemarks").prop('readonly',true); // just show the information 

        //$('#onhold_modal').find('.statRemarks').prop('readonly',true);      

        // get the remarks from the final_audit_track table       

        $.ajax({

            type: "POST",
            url: "fetchRemarks",
            data: {regNo: regNo},
            datatype: JSON,
            success: function (jsonResponse)
            {
                console.log(" inside fetchremarks success response jsonResponse is " + jsonResponse);

                var remarks = jsonResponse.statRemarks;

                console.log(" inside fetchremarks success response remarks " + remarks);

                $("#show_remark").html(remarks);

            }, error: function ()
            {
                console.log("error")
            }

        });

        $("#onhold_title").html("Put Off Hold Action for Reg No. - " + regNo);


        $("#onhold_btn").html("Put Off Hold");

        $("#onhold_show_remark").show();

        $("#onhold_add_remark").hide();

        console.log(" inside type as false ");
    } else // show the textarea to add remarks
    {
        //$("#onhold_modal .statRemarks").prop('readonly',false);// show the texarea that is editable

        //$('#onhold_modal').find('.statRemarks').prop('readonly',false); 

        $("#onhold_title").html("Put On Hold Action for Reg No. - " + regNo + " ( Add Remarks ) ");

        $("#onhold_btn").html("Put On Hold");

        $("#hold_statRemarks").val("");

        $("#onhold_add_remark").show();

        $("#onhold_show_remark").hide();

        console.log(" inside type as true ");
    }

}

//added by nikki on 26th jun 2019
function prefillVpnData(reg_no, form_name, showApprove, appType, role) {
    $("#vpn_action_heading").html("VPN entries for Registration no - " + reg_no);
    $.ajax({
        type: "POST",
        url: "getVpnData",
        data: {
            reg_no: reg_no
        },
        dataType: 'json',
        success: function (data) {
            var content = "<table class='table table-bordered table-hover'>"
            content += '<th>IP Type</th><th>IP Address</th><th>Application URL</th><th>Destination Port</th><th>Server Location</th><th>Action</th>';
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            var flag = false;
            $.each(jsonvalue.vpn_data, function (key, value) {
                content += '<tr>';
                $.each(value, function (key1, value1) {
                    if (key1 === 'ip1' || key1 === 'ip2' || key1 === 'ip3' || key1 === 'server_loc' || key1 === 'server_loc_txt' || key1 === 'deleted_by' || key1 === 'id' || key1 === 'regno') {
                    } else {
                        if (key1 === 'deleted_flag' && value1 === 'N') {
                            flag = true;
                            content += '<td><button type="button" id="disable_vpnentry" data="' + value.id + ',' + value.regno + '" class="btn red vpnentry ' + value.id + '">Disable</button></td>';
                        } else if (key1 === 'deleted_flag' && value1 === 'Y') {
                            content += '<td><button type="button" id="enable_vpnentry" data="' + value.id + ',' + value.regno + '" class="btn blue vpnentry ' + value.id + '">Enable</button></td>';
                        } else {
                            content += '<td>' + value1 + '</td>';
                        }
                    }
                });
                content += '</tr>';
            });
            content += "</table>"
            if (showApprove) {
                $('#vpn_approve_btn').attr("onclick", "apply_heading('" + reg_no + "','" + form_name + "','forward_vpn','" + appType + "');prefillData();resetRandom();");
            } else {
                if (role === 'ca') {
                    $('#vpn_approve_btn').attr("onclick", "apply_heading('','','vpn_appmodal','')");
                }
            }
            if (flag === false) {
                $("#vpn_approve_btn").prop("disabled", "true");
                $("#vpn_action_form #statRemarksError").html('You have no entries to Approve. Please enable atleast one entry to Approve the request.');
            } else {
                $("#vpn_approve_btn").removeAttr('disabled');
                $('#vpn_action_form #statRemarksError').html('');
            }
            $('#vpn_action_form #vpn_action_data_filled').html('');
            $('#vpn_action_form #vpn_action_data_filled').append(content);
            $("#vpn_reject_btn").attr("onclick", "apply_heading('" + reg_no + "','" + form_name + "','reject_vpn','');resetRandom();");
            if (role === 'admin') {
                $('#vpn_approve_btn').addClass('.display-hide');
                $('#vpn_approve_btn').addClass('.display-hide');
            }
            if (role === 'admin') {
                $('#vpn_approve_btn').remove();
                $('#vpn_reject_btn').remove();
            }
            $("#vpn_action_modal").modal();
        },
        error: function () {
            showAlertBox("error");
        }
    });
}

// below function added by pr on 27thnov19
function getPrevRejectTable(prevrejectdata, email, mobile)
{
    var str = "", table = "";
    var i = 1;

    var by = "";

    $.each(prevrejectdata, function (index, value)
    {
        //console.log(" index is " + index + " value is " + value);

        //console.log(" stat_reg_no "+value._stat_reg_no+" stat_type "+value._stat_type+" setEmail "+value.email
        //        +" datetime "+value.createdon+" setReject_remarks "+value.reject_remarks);

        by = value._stat_type;

        if (by.toLowerCase().indexOf("ca") >= 0)
        {
            by = "Reporting Officer(" + value.email + ")";
        } else if (by.toLowerCase().indexOf("support") >= 0)
        {
            by = "Support(" + value.email + ")";
        } else if (by.toLowerCase().indexOf("coordinator") >= 0)
        {
            by = "Coordinator(" + value.email + ")";
        } else if (by.toLowerCase().indexOf("us") >= 0)
        {
            by = "Under Secretary(" + value.email + ")";
        } else if (by.toLowerCase().indexOf("mail-admin") >= 0)
        {
            by = "Admin(" + value.email + ")";
        } else if (by.toLowerCase().indexOf("da") >= 0)
        {
            by = "DA(" + value.email + ")";
        }

        str += `<tr>
        <td> ` + i + ` </td>
        <td> ` + value._stat_reg_no + ` </td>
        <td> ` + by + ` </td>
        <td> ` + value.createdon + ` </td>
        <td> ` + value.reject_remarks + ` </td>
        </tr>`;
        i++;

    });

    if (str != "")
    {
        var str_head = `<tr>
        <th> S.No </th>
        <th> Registration No. </th>
        <th> Rejected By </th>
        <th> Date </th>
        <th> Remarks </th>
        </tr>`;
        table = "<p class='mb-3' style='font-size: 14px;font-weight: 700;color: red;'>This Applicant's (<span class='list-email'>" + email + "</span>, <span class='list-email'>" + mobile + "</span>) requests have already been rejected as shown below , Please verify the credentials and authenticity of the applicant. It seems suspicious, so be careful before Approving/Completing the request.</p>";
        table += `<table id="example1" class="table table-striped table-bordered table-hover table-checkable order-column dt-responsive dataTable no-footer dtr-inline">
        <thead id="bulk_users_view_thead" >` + str_head + `</thead><tbody id="bulk_users_view_tbody">` + str + `</tbody></table>`;
    }

    return table;
}