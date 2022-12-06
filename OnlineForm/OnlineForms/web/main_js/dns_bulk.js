$('input[name="dns_single_form"]').click(function () {
    if ($(this).is(':checked')) {
        fetchOpenCampaigns()
    }
});

function fetchOpenCampaigns() {
    $.ajax({
        type: 'post',
        url: 'fetchOpenCampaigns',
        success: function (data) {
            $('input:radio[name="dns_single_form"]').prop('disabled', false);
            show_divs();
            if (data.campaigns.length > 0) {
                $('#tab2').show();
                $('#dns_single_form_div').addClass('display-hide');
                $('#dns_bulk_form').addClass('display-hide');
                $("#dnsbulk-2").addClass('display-hide')
                var campaign_tbl = "";
                $("#campaign_tbl tbody").html("");
                $.each(data.campaigns, function (key, val) {
                    key++;
                    campaign_tbl += "<tr><td>" + key + "</td><td>" + val.id + "</td><td>" + namingConvention(val.request_type) + "</td><td>" + namingConvention(val.req_other_add) + "</td><td>" + val.submitted_at + "</td><td class='text-center'><span class='btn btn-primary btn-sm' onclick='showCapaign(" + val.id + ")'>Continue</span></td><td class='text-center'><span class='btn btn-danger btn-sm' onclick='discardCampaign(" + val.id + ")'>Discard</span></td></tr>"
                });
                $("#campaign_tbl tbody").html(campaign_tbl);
                $("#campaign_modaltbl").modal('show');
            } else {
                $("#campaign_modaltbl").modal('hide');
                $('#tab2').show();
                $("#dnsbulk-2").addClass('display-hide')
                var dns_single_form = $('input[name="dns_single_form"]:checked').val();
                if(dns_single_form == "single") {
                    $('#dns_single_form_div').removeClass('display-hide');
                } else {
                    $('#dns_bulk_form').removeClass('display-hide');
                }
                $("#dns_history_page").text("DNS History");
                $("#history-page").addClass("d-none");
                $("#tab1").show();
                $("#tab2").show();
            }
        }
    })
}
function showCapaign(i) {
    $.ajax({
        type: 'post',
        url: 'fetchCampaignData',
        data: {bulkDataId: i},
        success: function (data) {
            $("#_req").val(data.bulkData.formDetails.request_type);
            $("#dns_history_page").text("DNS History");
            $("#history-page").addClass("d-none");
            $("#tab1").show();
            $("#tab2").show();
            $("#campaign_modaltbl").modal('hide');
            $('#dnsbulk-1').addClass('display-hide');
            $('#dns_single_form_div').addClass('display-hide');
            $('#dnsbulk-2').removeClass('display-hide');
            $('.success-head-notify').html(msgValidHead(data.bulkData.formDetails));
            $('.err-head-notify').html(msgInValidHead(data.bulkData.formDetails));
            bulkDomCreate(data.bulkData)
        }
    })
}
function msgValidHead(obj) {
    if(obj.request_type) {
        if(obj.request_type === 'req_modify') {
            if(obj.request_other_add) {
                return "Following Record's are valid for modify to "+namingConvention(obj.request_other_add)+" requests";
            }
            return "Following Domain's are valid for modify request";
        } else if(obj.request_type === 'req_delete') {
            if(obj.request_other_add) {
                return "Following Record's are valid for delete to "+namingConvention(obj.request_other_add)+" requests";
            }
            return "Following Domain's are valid for delete request";
        } else {
            if(obj.request_other_add) {
                return "Following Record's are valid for new to "+namingConvention(obj.request_other_add)+" requests";
            }
            return "Following Domain's are valid for new request";
        }
    } else {
        if(obj.request_other_add) {
            return "Following Record's are valid for new to "+namingConvention(obj.request_other_add)+" requests";
        }
        return "Following Domain's are valid for new request";
    }
}

function msgInValidHead(obj) {
    if(obj.request_type) {
        if(obj.request_type === 'req_modify') {
            if(obj.request_other_add) {
                return "Following Record's are invalid for modify to "+namingConvention(obj.request_other_add)+" requests";
            }
            return "Following Domain's are invalid for modify request";
        } else if(obj.request_type === 'req_delete') {
            if(obj.request_other_add) {
                return "Following Record's are invalid for delete to "+namingConvention(obj.request_other_add)+" requests";
            }
            return "Following Domain's are invalid for delete request";
        } else {
            if(obj.request_other_add) {
                return "Following Record's are invalid for new to "+namingConvention(obj.request_other_add)+" requests";
            }
            return "Following Domain's are invalid for new request";
        }
    } else {
        if(obj.request_other_add) {
            return "Following Record's are invalid for new to "+namingConvention(obj.request_other_add)+" requests";
        }
        return "Following Domain's are invalid for new request";
    }
}

function discardCampaign(i) {
    bootbox.confirm({
        title: "Discard Campaign",
        message: "Are you sure? Do you want to discard this Campaign?",
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
                    url: 'discardCampaign',
                    data: {bulkDataId: i},
                    success: function (data) {
                        fetchOpenCampaigns();
                    }
                })
            }
        }

    });
}

function discardRelatedCampaign(i) {
    $.ajax({
        type: 'post',
        url: 'discardCampaign',
        data: {bulkDataId: i},
        success: function (data) {
            //fetchOpenCampaigns();
        }
    })
}

function validDomForReqModify2(jsonvalue) {
//    alert("2")
    var validData = "";
    var thead = "";
    thead += `<tr><td class='text-center not-export-col'>S.No</td><td>Domain Value</td>`
    $.each(jsonvalue.validRecord[0], function (k, val) {
        if (k == 'req_other_add' && val == '') {
            validcname_col = true;
        }
        if (k != 'id' && k != 'cname' && k != 'domain' && k != 'errorMessage' && k != 'campaignId' && k != 'req_other_add' && val != '' || (k == 'migration_date' && val === '')) {
            thead += `<td>` + namingConvention(k) + `</td>`
        }
    });
    if (validcname_col) {
        thead += `<td>CNAME</td>`
    }
    thead += `<td class='text-center not-export-col'>Action</td></tr>`;
    $("#successBulkData thead").html(thead);
    $.each(jsonvalue.validRecord, function (key, val) {
        validData += dynamic_append_final_validtb1_dns(val, key);
    });
    $("#successBulkData").DataTable().clear().destroy();
    $("#successBulkData tbody").html(validData);
    setTimeout(function () {
        $("#successBulkData").DataTable();
    }, 5);
}

$('#user_file_dns').change(function (e) {
    e.preventDefault();
    var file = $('input[name="user_file"]').get(0).files[0];
    var fileType = file.type;
    var fileSize = file.size
    var req_type = $('#dnsbulk-1 input[name="req_"]:checked').val();
    var req_other_type = $('#dnsbulk-1 input[name="req_other_add"]:checked').val();
    
    var match = ["text/csv", "application/vnd.ms-excel"]; // for multiple take comma separated values
    if (fileType === match[0] || fileType === match[1]) {
        if (fileSize <= 1000000) {
            $('#file_err').text("");
            var formData = new FormData();
            formData.append('cert_file', file);
            formData.append('req_type', chk_undefined(req_type));
            formData.append('req_other_type', chk_undefined(req_other_type));
            $.ajax({
                url: 'checkCSVForDNS',
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

var validcname_col = false;
var cname_col = false;
function bulkDomCreate(jsonvalue) {
    $("#success_bulk_record thead").html("");
    $("#success_bulk_record tbody").html("");
    $("#error_bulk_record tbody").html("");
    $("#error_bulk_record thead").html("");
    var invalidData = "";
    var validData = "";
    if(jsonvalue.formDetails.renamed_file) {
        $("input[name='dns_single_form'][value='bulk']").prop('checked', true);
        $("input[name='dns_single_form'][value='single']").prop('checked', false);
    } else {
        $("input[name='dns_single_form'][value='bulk']").prop('checked', false);
        $("input[name='dns_single_form'][value='single']").prop('checked', true);
    }
    if(jsonvalue.invalidRecord.length > 0 || jsonvalue.validRecord.length > 0) {
        $('input:radio[name="dns_single_form"]:not(:checked)').prop('disabled', true);
    } else {
        $('input:radio[name="dns_single_form"]').prop('disabled', false);
    }
    $('span.err-count').html(jsonvalue.invalidRecord.length);
    if (jsonvalue.invalidRecord.length > 0) {
        var thead = "";
        thead += `<tr><td class='text-center not-export-col'>S.No</td><td>Domain Value</td>`;
        $.each(jsonvalue.invalidRecord[0], function (k, val) {
            if (k == 'req_other_add' && val == '') {
                cname_col = true;
            }
            if (k != 'id' && k != 'cname' && k != 'domain' && k != 'errorMessage' && k != 'campaignId' && k != 'req_other_add' && val != '' || (k == 'migration_date' && val === '')) {
                thead += `<td>` + namingConvention(k) + `</td>`
            }
        });
        if (cname_col) {
            thead += `<td>CNAME</td>`
        }
        thead += `<td>Error Message</td><td class='text-center not-export-col'>Action</td></tr>`;
        $("#error_bulk_record thead").html(thead);
        $.each(jsonvalue.invalidRecord, function (key, val) {
            invalidData += dynamic_append_bulk_invalidtbl(val, key);
        });

        $('#error_bulk_record').DataTable().clear().destroy();
        $("#error_bulk_record tbody").html(invalidData);

        setTimeout(function () {
            $("#error_bulk_record").DataTable({
                dom: 'Bfrtip',
                buttons: [
                    'csv'
                ], exportOptions: {
                    columns: ':visible(:not(.not-export-col))'
                }
            });
        }, 5);
    } else {
        $(".err-head-notify").html("No Record Found.")
    }
    $('span.succ-count').html(jsonvalue.validRecord.length);
    if (jsonvalue.validRecord.length > 0) {
        $("button[value=preview]").prop('disabled', false);
        var thead = "";
        thead += `<tr><td class='text-center not-export-col'>S.No</td><td>Domain Value</td>`
        $.each(jsonvalue.validRecord[0], function (k, val) {
            if (k == 'req_other_add' && val == '') {
                validcname_col = true;
            }
            if (k != 'id' && k != 'cname' && k != 'domain' && k != 'errorMessage' && k != 'campaignId' && k != 'req_other_add' && val != '' || (k == 'migration_date' && val === '')) {
                thead += `<td>` + namingConvention(k) + `</td>`
            }
        });
        if (validcname_col) {
            thead += `<td>CNAME</td>`
        }
        thead += `<td class='text-center not-export-col'>Action</td></tr>`;
        $("#success_bulk_record thead").html(thead);
        $.each(jsonvalue.validRecord, function (key, val) {
            validData += dynamic_append_bulk_validtbl(val, key);
        });
        $("#success_bulk_record").DataTable().clear().destroy();
        $("#success_bulk_record tbody").html(validData);
        setTimeout(function () {
            var numCols = $('#example thead th').length;
            $("#success_bulk_record").DataTable({
                dom: 'Bfrtip',
                buttons: [
                    {
                        extend: 'csv',
                        exportOptions: {
                          columns: ':not(td.not-export-col)'
                        }
                    },
                ]
            });
        }, 5);
    } else {
        $("button[value=preview]").prop('disabled', true);
        $(".success-head-notify").html("No Record Found");
        if (jsonvalue.invalidRecord.length > 0) {
            $('.nav-tabs a[href="#profile"]').tab('show');
        }
    }
}

function dynamic_append_bulk_invalidtbl(data, id_data) {
    id_data++;
    var add_data = "";
    add_data += `<tr><td class='text-center'>` + id_data + `</td><td>` + checkNullval(data.domain) + `</td>`;

    $.each(data, function (k, val) {
        if (k != 'id' && k != 'cname' && k != 'domain' && k != 'errorMessage' && k != 'campaignId' && k != 'req_other_add' && val != '' || (k == 'migration_date' && val === '')) {
            add_data += `<td>` + checkNullval(val) + `</td>`;
        }
    })
    if (cname_col) {
        add_data += `<td>` + checkNullval(data.cname) + `</td>`;
    }
    add_data += `<td>` + data.errorMessage + `</td>`;
    add_data += `<td class='text-center' width="15%">
        <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
        <ul class="dropdown-menu">
            <li><a href="javascript:void(0);" onclick="editRecordByBulkUserId_dns(` + data.id + `, 'mainpage', ` + data.campaign_id + `)">Edit</a></li>
            <li><a href="javascript:void(0);" onclick="deleteRecordByBulkUserId_dns(` + data.id + `, 'mainpage', ` + data.campaign_id + `)">Delete</a></li>
        </ul>
    </td></tr>`;
    return add_data;
}

function dynamic_append_bulk_validtbl(data, id_data) {
    id_data++;
    var add_data = "";
    add_data += `<tr><td class='text-center'>` + id_data + `</td><td>` + data.domain + `</td>`;
    $.each(data, function (k, val) {
        if (k != 'id' && k != 'cname' && k != 'domain' && k != 'errorMessage' && k != 'campaignId' && k != 'req_other_add' && val != '' || (k == 'migration_date' && val === '')) {
            add_data += `<td>` + checkNullval(val) + `</td>`;
        }
    })
    if (validcname_col) {
        add_data += `<td>` + checkNullval(data.cname) + `</td>`;
    }
    add_data += `<td class='text-center' width="15%">
        <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
        <ul class="dropdown-menu">
            <li><a href="javascript:void(0);" onclick="editRecordByBulkUserId_dns(` + data.id + `, 'mainpage', ` + data.campaign_id + `)">Edit</a></li>
            <li><a href="javascript:void(0);" onclick="deleteRecordByBulkUserId_dns(` + data.id + `, 'mainpage', ` + data.campaign_id + `)">Delete</a></li>
        </ul>
    </td></tr>`;
    return add_data;
}

function dynamic_append_final_validtb1_dns(data, id_data) {
//alert("6")
    id_data++;
    var add_data = "";
    add_data += `<tr><td class='text-center'>` + id_data + `</td><td>` + data.domain + `</td>`;
    $.each(data, function (k, val) {
        if (k != 'id' && k != 'cname' && k != 'domain' && k != 'errorMessage' && k != 'campaignId' && k != 'req_other_add' && val != '' || (k == 'migration_date' && val === '')) {
            add_data += `<td>` + checkNullval(val) + `</td>`;
        }
    })
    if (validcname_col) {
        add_data += `<td>` + checkNullval(data.cname) + `</td>`;
    }
    add_data += `<td class='text-center' width="15%">
        <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
        <ul class="dropdown-menu">
            <li><a href="javascript:void(0);" onclick="editRecordByBulkUserId_dns(` + data.id + `, 'finalpopup', ` + data.campaign_id + `)">Edit</a></li>
            <li><a href="javascript:void(0);" onclick="deleteRecordByBulkUserId_dns(` + data.id + `, 'finalpopup', ` + data.campaign_id + `)">Delete</a></li>
        </ul>
    </td></tr>`;
    return add_data;
}

function namingConvention(lbl) {
    var req_type = $("#_req").val();
    if (lbl.indexOf('mx') != -1) {
        if (lbl.indexOf('old') != -1) {
            return 'OLD MX Value';
        }
        return 'MX Value *';
    }
    if (lbl.indexOf('cname_txt') != -1) {
        if (lbl.indexOf('old') != -1) {
            return 'OLD CNAME Value';
        }
        return 'CNAME Value *';
    }
    if (lbl.indexOf('cname') != -1) {
        
        return 'CNAME Value';
    }
    if (lbl.indexOf('loc') != -1) {
        return 'Server Location *';
    }
    if (lbl.indexOf('doma') != -1) {
        return 'Domain Value *';
    }
    if (lbl.indexOf('ptr') != -1) {
        if (lbl.indexOf('old') != -1) {
            return 'OLD PTR Value';
        }
        return 'PTR Value *';
    }
    if (lbl.indexOf('txt_') != -1) {
        if (lbl.indexOf('old') != -1) {
            return 'OLD TXT Value';
        }
        return 'TXT Value *';
    }
    if (lbl.indexOf('srv') != -1) {
        if (lbl.indexOf('old') != -1) {
            return 'OLD SRV Value';
        }
        return 'SRV Value *';
    }
    if (lbl.indexOf('spf') != -1) {
        if (lbl.indexOf('old') != -1) {
            return 'OLD SPF Value';
        }
        return 'SPF Value *';
    }
    if (lbl.indexOf('dmarc') != -1) {
        if (lbl.indexOf('old') != -1) {
            return 'OLD DMARC Value';
        }
        return 'DMARC Value *';
    }
    if (lbl.indexOf('old_ip') != -1) {
        return 'OLD IP';
    }
    if (lbl.indexOf('new_ip') != -1) {
        if(req_type == 'req_delete') {
            return 'IP';
        }
        return 'NEW IP *';
    }
    if (lbl.indexOf('migration_date') != -1) {
        return 'Migration Date';
    }
    if(lbl.indexOf('req_') != -1) {
        return lbl.split('_')[1].toUpperCase();
    }
    return lbl;
}

function editRecordByBulkUserId_dns(i, comming_from, campid) {
    $(".loader").show();
    $.ajax({
        type: 'post',
        url: 'bulkDnsDataEdit',
        data: {bulkDataId: i, iCampaignId: campid},
        success: function (data) {
            $("#comming_from").val(comming_from)
            var form_dom = "";
            $("#sinId").val(data.dnsData.id);
            $("#iCampaignIdDt").val(data.dnsData.campaignId);
            form_dom = bulkEditPopup(data.dnsData)
            $("#migrate_pop1").val(checkNullval(data.dnsData.migration_date));
            $("#bulkdynamic-form").html("");
            $("#bulkdynamic-form").html(form_dom);
            $("#bulkUploadEditSingle").modal('show');
        }, complete: function (data) {
            $(".loader").hide();
        }
    })
}

function bulkEditPopup(data) {
    var form_dom = "";
    $.each(data, function (k, val) {
        if (val !== '' && k != 'id' && k != 'migration_date' && k != 'req_' && k != "campaignId" && k != "req_other_add" && k != "errorMessage" && k.indexOf("registration") == -1) {
            form_dom += `<div class="form-group">
                 <label for="">` + namingConvention(k) + `</label>
                 <input type="text" name="` + k + `" id="` + k + `" class="form-control migration_date_pop" placeholder="Enter ` + namingConvention(k) + `" value="` + checkNullval(val) + `">
             </div>`;
        }
    });
    return form_dom;
}

$("#dns_history_page").click(function () {
    if ($(this).text() == "DNS History") {
        $(this).text("Back to Main View")
        fetchCampDnsHistory();
        $("#history-page").removeClass("d-none");
        $("#tab1").hide();
        $("#tab2").hide();
    } else {
        $(this).text("DNS History");
        $("#history-page").addClass("d-none");
        $("#tab1").show();
        $("#tab2").show();
        fetchOpenCampaigns();
    }

});

function fetchCampDnsHistory() {
    $.ajax({
        type: 'post',
        url: 'fetchCompleteCampaignData',
        success: function (data) {
            if (data.bulkData.complete.length > 0) {
                var campaign_pendingtbl = "";
                $("#campaign_completetbl tbody").html("");
                $.each(data.bulkData.complete, function (key, val) {
                    key++;
                    campaign_pendingtbl += "<tr><td>" + key + "</td><td>" + val.id + "</td><td>" + chkSubmissiontype(val.uploaded_file) + "</td><td>" + val.submitted_at + "</td><td class='text-center'></td><td class='text-center'></td></tr>"
                });
                $("#campaign_completetbl").DataTable().clear().destroy();
                $("#campaign_completetbl tbody").html(campaign_pendingtbl);
                setTimeout(function () {
                    $("#campaign_completetbl").DataTable({
                        "columnDefs": [
                            {"width": "30%", "targets": 2}
                        ]
                    });
                }, 5);
            } else {
                var campaign_pendingtbl = "";
                $("#campaign_completetbl tbody").html("");
                campaign_pendingtbl += "<tr><td class='text-center' colspan='6'>No Result Found</td></tr>"
                $("#campaign_completetbl tbody").html(campaign_pendingtbl);
            }

            if (data.bulkData.discard.length > 0) {
                var campaign_pendingtbl = "";
                $("#campaign_discardtbl tbody").html("");
                $.each(data.bulkData.discard, function (key, val) {
                    key++;
                    campaign_pendingtbl += "<tr><td>" + key + "</td><td>" + val.id + "</td><td>" + chkSubmissiontype(val.uploaded_file) + "</td><td>" + val.submitted_at + "</td><td class='text-center'></td><td class='text-center'></td></tr>"
                });
                $("#campaign_discardtbl").DataTable().clear().destroy();
                $("#campaign_discardtbl tbody").html(campaign_pendingtbl);
                setTimeout(function () {
                    $("#campaign_discardtbl").DataTable({
                        "columnDefs": [
                            {"width": "30%", "targets": 2}
                        ]
                    });
                }, 5);
            } else {
                var campaign_pendingtbl = "";
                $("#campaign_discardtbl tbody").html("");
                campaign_pendingtbl += "<tr><td class='text-center' colspan='6'>No Result Found</td></tr>"
                $("#campaign_discardtbl tbody").html(campaign_pendingtbl);
            }

            if (data.bulkData.pending.length > 0) {
                var campaign_pendingtbl = "";
                $("#campaign_pendingtbl tbody").html("");
                $.each(data.bulkData.pending, function (key, val) {
                    key++;
                    campaign_pendingtbl += "<tr><td>" + key + "</td><td>" + val.id + "</td><td>" + chkSubmissiontype(val.uploaded_file) + "</td><td>" + val.submitted_at + "</td><td class='text-center'><span class='btn btn-primary btn-sm' onclick='showCapaign(" + val.id + ")'>Continue</span></td><td class='text-center'><span class='btn btn-danger btn-sm' onclick='discardCampaign(" + val.id + ")'>Discard</span></td></tr>"
                });
                $("#campaign_pendingtbl").DataTable().clear().destroy();
                $("#campaign_pendingtbl tbody").html(campaign_pendingtbl);
                setTimeout(function () {
                    $("#campaign_pendingtbl").DataTable({
                        "columnDefs": [
                            {"width": "30%", "targets": 2}
                        ]
                    });
                }, 5);
            } else {
                var campaign_pendingtbl = "";
                $("#campaign_pendingtbl tbody").html("");
                campaign_pendingtbl += "<tr><td class='text-center' colspan='6'>No Result Found</td></tr>"
                $("#campaign_pendingtbl tbody").html(campaign_pendingtbl);
            }
        }
    })
}

function chkSubmissiontype(data) {
    if (data === null || data === "") {
        return "You have selected DNS User Subscription Through (Manual Entries)";
    }
    return "You have selected DNS User Subscription Through (File Upload)";
}

$('#dnsbulk-1 input[name="req_"]').click(function() {
    $("#download_bulk_file").attr("href", "assets/downloads/"+filegeneraterlink());
});

$('#dnsbulk-1 input[name="req_other_add"]').click(function() {
    $("#download_bulk_file").attr("href", "assets/downloads/"+filegeneraterlink());
});
function filegeneraterlink() {
    $('#user_file_dns').val("");
    $(".custom-file-label").html("Select File");
    var file_name = "";
    var req_ = $('#dnsbulk-1 input[name="req_"]:checked').val();
    var req_other_add = $('#dnsbulk-1 input[name="req_other_add"]:checked').val();
    if(req_ == 'req_modify') {
       if(req_other_add !== undefined) {
           return file_name = req_+"_"+req_other_add+".csv";
       }
       return file_name = req_+".csv";
    } else {
        if(req_other_add !== undefined) {
           return file_name = "req_new_"+req_other_add+".csv";
       }
       return file_name = "req_new.csv";
    }
}

function chk_undefined(val) {
    if(val === undefined) {
        return "";
    }
    return val;
}

function show_divs() {
    $('.dns_owner_error').removeClass('alert alert-danger');
    $('.dns_owner_error').html("");
    $('.card-holder').removeClass('err-holder');
    var sele_data = $('input[name="dns_single_form"]:checked').val();
    if (sele_data === 'single') {
        $('#tab2').show();
        $('#dns_single_form_div').removeClass('display-hide');
        $('#dns_single_form').removeClass('display-hide');
        $('#dns_bulk_form').addClass('display-hide');
        $("#dnsbulk-1").addClass('display-hide')
        $("#dnsbulk-2").addClass('display-hide')
    } else {
        $('#tab2').show();
        $("#dnsbulk-1").removeClass('display-hide')
        $('#dns_single_form_div').addClass('display-hide');
        $('#dns_bulk_form').removeClass('display-hide');
    }
}