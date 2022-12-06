var icount = 0;
$(function () {
    $("#dns_url_err").hide();
    $("#dns_cname_error").hide();
    $("#dns_old_ip_error").hide();
    $("#dns_new_ip_error").hide();
    $("#dns_other_old_error").hide();
    $("#dns_other_new_error").hide();
    $("#dns_loc_error").hide();

    var error_domain = false;
    var error_cname = false;
    var error_old_ip = false;
    var error_new_ip = false;
    var error_new_other = false;
    var error_old_other = false;
    var error_dns_loc = false;

    $("#dns_url_1").focusout(function () {
        check_url_domain();
    });
    $("#dns_cname_1").focusout(function () {
        check_cname();
    });
    $("#dns_new_ip").focusout(function () {
        check_newip();
    });
    $("#dns_oldip_1").focusout(function () {
        check_old_ip();
    });

    $("#dns_loc").focusout(function () {
        check_loaction();
    });

    $("#new_other_txt").focusout(function () {
        new_other_txt();
    });


    function new_other_txt() {
        $("#dns_other_new_error").html('');
        var old_other_txt = $("#old_other_txt").val();
        var new_other_txt = $("#new_other_txt").val();
        var domain = $("#dns_url_1").val();
        if ($("#new_other_txt").val().length < 1) {
            var err_msg = $("#new_other_txt").attr('placeholder')
            $("#dns_other_new_error").html("Please " + err_msg);
            $("#dns_other_new_error").show();
            error_new_other = true;
        } else if (old_other_txt === new_other_txt) {
            //var err_msg = $("#new_other_txt").attr('placeholder')
//            $("#dns_other_new_error").html("Old and New Entry can not be same.");
//            $("#dns_other_new_error").show();
//            error_new_other = true;
        } else {
            $("#dns_other_new_error").hide();
            error_new_other = false;
        }
    }

    function check_url_domain() {
        var pattern = new RegExp(/[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)?/gi);
        if (!pattern.test($("#dns_url_1").val())) {
            $("#dns_url_err").html("Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]");
            $("#dns_url_err").show();
            error_domain = true;
        } else {
            $("#dns_url_err").hide();
            error_domain = false;
        }
    }
    function check_newip() {
        var pattern = new RegExp(/((^\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\s*$)|(^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$))/);
        if (!pattern.test($("#dns_new_ip").val())) {
            $("#dns_new_ip_error").html("Please Enter a Valid New IP.");
            $("#dns_new_ip_error").show();
            error_old_ip = true;
        } else {
            $("#dns_new_ip_error").hide();
            error_old_ip = false;
        }
    }
    function check_cname() {
        var cname = $("#dns_cname_1").val();
        var domain = $("#dns_url_1").val();
        var pattern = new RegExp(/[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)?/gi);
        if (cname.length > 1 && !pattern.test($("#dns_cname_1").val())) {
            $("#dns_cname_error").html("Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]");
            $("#dns_cname_error").show();
            error_cname = true;
        } else {
            $("#dns_cname_error").hide();
            error_cname = false;
        }
    }

    function check_old_ip() {
        var dns_oldip_1 = $("#dns_oldip_1").val().length;
        var pattern = new RegExp(/((^\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\s*$)|(^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$))/);
        if (dns_oldip_1 > 1 && !pattern.test($("#dns_oldip_1").val())) {
            $("#dns_old_ip_error").html("Please Enter a Valid Old IP.");
            $("#dns_old_ip_error").show();
            error_new_ip = true;
        } else {
            $("#dns_old_ip_error").hide();
            error_new_ip = false;
        }
    }

    function check_loaction() {
        var pattern = new RegExp(/^[ A-Za-z0-9_@./,#&+-]*$/);
        if ($("#dns_loc").val().length < 2) {
            $("#dns_loc_error").html("Server Location cannot be empty");
            $("#dns_loc_error").show();
            error_dns_loc = true;
        } else if (!pattern.test($("#dns_loc").val())) {
            $("#dns_loc_error").html("Enter the DNS server location,[Alphanumeric,(.,-/) and whitespace] allowed");
            $("#dns_loc_error").show();
            error_dns_loc = true;
        } else {
            $("#dns_loc_error").hide();
            error_dns_loc = false;
        }
    }

    $("#addiprecord").click(function () {

        error_domain = false;
        error_cname = false;
        error_old_ip = false;
        error_new_ip = false;
        error_new_other = false;
        //error_old_other = false;
        error_dns_loc = false;

        check_url_domain();
        check_cname();
        //check_old_ip();
        check_loaction()
        if ($('input[name="req_"]').is(':checked') && $('input[name="req_other_add"]').is(':checked')) {
            var chk_val = $('input[name="req_"]:checked').val();
            if (chk_val === 'req_modify') {
                new_other_txt();
                //old_other_txt()
            } else {
                new_other_txt();
            }
        } else {
            if ($('input[name="req_"]').is(':checked')) {
                var chk_val = $('input[name="req_"]:checked').val();
                if (chk_val !== 'req_delete') {
                    check_newip();
                }
            }

        }
        if (error_domain == false && error_cname == false && error_old_ip == false && error_new_ip == false && error_dns_loc == false) {
            icount++;
            $("#recd_id").val(icount);
            var dataObj = {dnsEditData: $('#sigle_form').serializeObject()};
            var data = JSON.stringify(dataObj);
            $.ajax({
                type: 'post',
                url: 'singleDnsDataPost',
                data: data,
                datatype: JSON,
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    if (!jQuery.isEmptyObject(data.dnsEditData)) {
                        $(".singleDataErr").html("");
                        $(".singleDataErr").removeClass('alert alert-danger')
                        $("#dns_service input[type='radio']").prop('disabled', true);
                        $('input:radio[name="req_other_add"]:not(:checked)').prop('disabled', true);
                        var req_other_add = $('input[name="req_other_add"]:checked').val();
                        var req_ = $('input[name="req_"]:checked').val();
                        if (data.dnsEditData.errorMessage) {
                            $(".singleDataErr").html(data.dnsEditData.errorMessage);
                            $(".singleDataErr").addClass('alert alert-danger')
                        } else {
                            if (!jQuery.isEmptyObject(data.bulkData)) {
                                $('.dns_owner_error').removeClass('alert alert-danger');
                                $('.dns_owner_error').html("");
                                $('.card-holder').removeClass('err-holder');
                                $("#dnsip_collection").removeClass("d-none");
                                $("#sigle_form input:text").val("");
                                var thead = "";
                                thead = createThead(req_, req_other_add);
                                $("#dnsip_collection_tbl thead").html("");
                                $("#dnsip_collection_tbl thead").html(thead);
                                singleRecordTbl(data.bulkData.dnsSingleData, req_, req_other_add);
                            }
                        }

                    }
                },
                complete: function () {
                    $('.loader').hide();
                }
            });
        } else {
            return true;
        }
    });
});

function createThead(req_, req_other_add) {
    var thead = "";
    if (req_other_add !== undefined && req_other_add !== "") {
        if (req_ !== undefined && req_ === "req_modify") {
            thead += '<tr><td width="5%">S.No</td><td>Domain</td><td>' + req_other_add.toUpperCase() + '</td><td>OLD ' + req_other_add.toUpperCase() + '</td><td>Web Server Location</td><td>Migration Date</td><td class="text-center">Action</td></tr>';
        } else {
            thead += '<tr><td width="5%">S.No</td><td>Domain</td><td>' + req_other_add.toUpperCase() + '</td><td>Web Server Location</td><td>Migration Date</td><td class="text-center">Action</td></tr>';
        }
    } else {
        if (req_ == 'req_modify') {
            thead += '<tr><td class="text-center" width="8%">S.No</td><td>Domain</td><td>CNAME</td><td>OLD IP</td><td>NEW IP</td><td>Web Server Location</td><td>Migration Date</td> <td class="text-center">Action</td></tr>';
        } else {
            thead += '<tr><td class="text-center" width="8%">S.No</td><td>Domain</td><td>CNAME</td><td>IP</td><td>Web Server Location</td><td>Migration Date</td> <td class="text-center">Action</td></tr>';
        }
    }
    return thead;
}

function singleDnsDataDeletePost(i) {
    bootbox.confirm({
        title: "Discard Campaign",
        message: "Are you sure? Do you want to discard this Record?",
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
            if(result) {
                $.ajax({
                    type: 'post',
                    url: 'singleDnsDataDeletePost',
                    data: {bulkDataId: i},
                    success: function (data) {
                        singleRecordTbl(data.bulkData.dnsSingleData);
                    }
                });
            }
        }
    });
}

$("#singleDataEditBtnFetch").click(function () {
    $('.loader').show();
    var dataObj = {dnsEditData: $('#singleDomEdit').serializeObject()};
    var data = JSON.stringify(dataObj);
    $.ajax({
        type: 'post',
        url: 'singleDnsDataEditPost',
        data: data,
        datatype: JSON,
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            if (data.bulkData.dnsSingleData.length > 0) {
                singleRecordTbl(data.bulkData.dnsSingleData);
                if(data.dnsEditData.errorMessage) {
                    $(".dns_sigerrorMessage").html(data.dnsEditData.errorMessage);
                    $(".dns_sigerrorMessage").addClass('alert alert-danger');
                } else{
                    $(".dns_sigerrorMessage").html("");
                    $(".dns_sigerrorMessage").removeClass('alert alert-danger');
                    $("#dynamicUploadEditSingle").modal('hide');
                }
            }
        },
        complete: function () {
            $('.loader').hide();
        }
    })
});

function singleRecordTbl(data) {
    var req_other_add = $('input[name="req_other_add"]:checked').val();
    var req_ = $('input[name="req_"]:checked').val();
    var add_data = "";
    $("#dnsip_collection").removeClass("d-none")
    if (data.length > 0) {
        if (req_other_add !== undefined && req_other_add !== '') {
            $("#dnsip_collection_tbl tbody").html("");
            $.each(data, function (k, val) {
                $("#dnsip_collection_tbl tbody").append(dynamic_append_tbl(val, k));
            });
        } else {
            $.each(data, function (k, val) {
                add_data += fetchSingleRecords(val, req_, req_other_add, k);
            })

            $("#dnsip_collection_tbl tbody").html("");
            $("#dnsip_collection_tbl tbody").html(add_data);
        }
    } else {
        $("#dnsip_collection").addClass("d-none")
        $("#dns_service input[type='radio']").prop('disabled', false);
        $('input[name="req_other_add"]:not(:checked)').prop('disabled', false);
        //$(".four_div").addClass('d-none');
        $("#dnsip_collection_tbl thead").html("");
        $("#dnsip_collection_tbl tbody").html("");
    }
}
function fetchSingleRecords(data, req_, req_other_add, i) {
    i++;
    var add_data = "";
    if (req_other_add !== undefined && req_other_add !== "") {
        
    } else {
        if (req_ == 'req_modify') {
            add_data += `<tr><td class='text-center'>` + i + `</td><td>` + data.domain + `</td><td>` + data.cname + `</td><td>` + data.old_ip + `</td><td>` + data.new_ip + `</td><td>` + data.dns_loc + `</td><td>` + data.migration_date + `</td>`;
            add_data += `<td class='text-center' width="15%">
                <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
                <ul class="dropdown-menu">
                    <li><a href="javascript:void(0);" onclick="singleDnsDataEditFetch(` + data.id + `)">Edit</a></li>
                    <li><a href="javascript:void(0);" onclick="singleDnsDataDeletePost(` + data.id + `)">Delete</a></li>
                </ul>
            </td></tr>`;
        } else {
            add_data += `<tr><td class='text-center'>` + i + `</td><td>` + data.domain + `</td><td>` + data.cname + `</td><td>` + data.new_ip + `</td><td>` + data.dns_loc + `</td><td>` + data.migration_date + `</td>`;
            add_data += `<td class='text-center' width="15%">
                <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
                <ul class="dropdown-menu">
                    <li><a href="javascript:void(0);" onclick="singleDnsDataEditFetch(` + data.id + `)">Edit</a></li>
                    <li><a href="javascript:void(0);" onclick="singleDnsDataDeletePost(` + data.id + `)">Delete</a></li>
                </ul>
            </td></tr>`;
        }
    }
    return add_data;
}
$('input[name="req_"]').click(function () {
    $('input[name="req_other_add"]').prop('checked', false);
    $("#first_div input:text").val("");
});
$('input[name="req_other_add"]').click(function () {
    $('font span').html('');
    $("#first_div input:text").val("");
    var req_ = $('input[name="req_"]:checked').val();
    if (this.previous) {
        $('.not-other').removeClass('d-none')
        $("#other_data_entry").addClass('d-none');
        $('#addiprecord').closest('div').attr('class', 'col-md-12 pt-2 text-center');
        this.checked = false;
    } else {
        var val_data = $(this).val();
        $('.not-other').addClass('d-none')

        $("#other_data_entry").removeClass('d-none');
        $('#addiprecord').closest('div').attr('class', 'col-md-6 pt-2');
        $('#addiprecord').addClass('mt-4');

        if (req_ == 'req_modify') {
            $("#other_old input:text").attr('name', 'old_' + val_data + '_txt');
            $("#other_old input:text").attr('placeholder', 'Enter Old ' + val_data.toUpperCase() + ' Value.');
            $("#other_old label").html('Old ' + val_data.toUpperCase());
            $("#other_new input:text").attr('name', val_data + '_txt');
            $("#other_new input:text").attr('placeholder', 'Enter New ' + val_data.toUpperCase() + ' Value.');
            $("#other_new label").html('New ' + val_data.toUpperCase());
            $("#other_old").removeClass('d-none');
            $("#other_new").attr('class', 'col-md-6');
        } else {
            $("#other_new input:text").attr('name', val_data + '_txt');
            $("#other_new input:text").attr('placeholder', 'Enter ' + val_data.toUpperCase() + ' Value.');
            $("#other_new label").html(val_data.toUpperCase());
            $("#other_new").attr('class', 'col-md-12');
            $("#other_old").addClass('d-none');
        }
        this.checked = true;
    }
    this.previous = this.checked;
});
var old_val = '';
function dynamic_append_tbl(data, count) {
    count++;
    var add_data = "";
    var req_ = $('input[name="req_"]:checked').val();
    var txt_val = false;
    add_data += `<tr><td class='text-center'>` + count + `</td><td>` + data.domain + `</td>`;
    $.each(data, function (k, val) {
        
        if (k.indexOf('_txt') != -1 && val !== '') {
            old_val = 'old_' + k;
        }
    })
    $.each(data, function (k, val) {
        if (k.indexOf('_txt') != -1 && val !== '') {
            add_data += `<td>` + checkNullval(val) + `</td>`;
        }
        if (k == old_val && val == '' && req_ === 'req_modify') {
            txt_val = true;
        }
    })
    if (txt_val) {
        add_data += `<td></td>`;
    }
    add_data += `<td>` + checkNullval(data.dns_loc) + `</td><td>` + checkNullval(data.migration_date) + `</td><td class='text-center' width="15%">
        <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
        <ul class="dropdown-menu">
            <li><a href="javascript:void(0);" onclick="singleDnsDataEditFetch(` + data.id + `)">Edit</a></li>
            <li><a href="javascript:void(0);" onclick="singleDnsDataDeletePost(` + data.id + `)">Delete</a></li>
        </ul>
    </td></tr>`;
    return add_data;
}
var old_val_txt = '';
var cname_val = '';
var frm_val = true;
function singleDnsDataEditFetch(i) {
    $.ajax({
        type: 'post',
        url: 'singleDnsDataFetch',
        data: {bulkDataId: i},
        success: function (data) {
            var form_dom = "";
            $("#domid").val(i);
            form_dom = singleEditPopup(data.dnsEditData);
            if (frm_val) { 
                if(old_val_txt == 'old_ip') {
                    form_dom += `<div class="form-group">
                        <label for="">` + namingConvention(old_val_txt) + `</label>
                        <input type="text" name="old_ip" class="form-control" placeholder="Enter ` + namingConvention(old_val_txt) + `" value="">
                    </div>`;
                    
                    if(cname_val == 'cname') {
                        form_dom += `<div class="form-group">
                            <label for="">` + namingConvention(cname_val) + `</label>
                            <input type="text" name="cname" class="form-control" placeholder="Enter ` + namingConvention(cname_val) + `" value="">
                        </div>`;
                    }
                } else if(cname_val == 'cname') {
                    form_dom += `<div class="form-group">
                        <label for="">` + namingConvention(cname_val) + `</label>
                        <input type="text" name="cname" class="form-control" placeholder="Enter ` + namingConvention(cname_val) + `" value="">
                    </div>`;
                } else {
                    form_dom += `<div class="form-group">
                        <label for="">` + namingConvention(old_val_txt) + `</label>
                        <input type="text" name="old_` + old_val_txt + `_txt" class="form-control" placeholder="Enter ` + namingConvention(old_val_txt) + `" value="">
                    </div>`;
                }
                
            }
            $("#migrate_pop1_sig").val(data.dnsEditData.migration_date);
            $("#dynamic-form").html("");
            $("#dynamic-form").html(form_dom);
            $("#dynamicUploadEditSingle").modal('show');
        }
    });
}

function singleEditPopup(data) {
    var req_other_add = $('input[name="req_other_add"]:checked').val();
    var req_ = $('input[name="req_"]:checked').val();
    var form_dom = "";
    frm_val = false;
    $.each(data, function (k, val) {
        if (k.indexOf('_txt') != -1 && val !== '') {
            old_val_txt = 'old_' + k;
        }
    })
    $.each(data, function (k, val) {
        if (val !== '' && k != 'id' && k != 'migration_date' && k != 'campaignId' && k != 'req_' && k != 'req_other_add') {
            form_dom += `<div class="form-group">
                 <label for="">` + namingConvention(k) + `</label>
                 <input type="text" name="` + k + `" id="` + k + `" class="form-control" placeholder="Enter ` + namingConvention(k) + `" value="` + val + `">
             </div>`;
        }
        if (k == old_val && val == '' && req_ === 'req_modify') {
            frm_val = true;
        }
        if (k == 'old_ip' && val == '' && req_ === 'req_modify') {
            old_val_txt = 'old_ip';
            frm_val = true;
        }
        if (k == 'cname' && val == '' && req_ !== ''  && req_other_add == undefined || req_other_add == '') {
            cname_val = "cname"
            frm_val = true;
        }
    });
    return form_dom;
}