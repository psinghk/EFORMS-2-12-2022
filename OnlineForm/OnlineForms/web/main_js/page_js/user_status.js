$(document).ready(function () {
    $('.mt-checkbox').prop('checked', false);
    var table = $("#example").dataTable({
        "order": [[3, "desc"]],
        "bDestroy": true,
        "aoColumnDefs": [{"bSortable": false, "aTargets": [-1]}]
        , language: {searchPlaceholder: "Reg Id, Email, Status, Date"}
    });
    //table.fnPageChange(2,true);


});
function setAction(action)
{
    $('.loader').show();
    console.log('inside setactionb');
    var str = "";
    var strAction = "";
    if (action == 'total')
    {
        str = '<input name="actype" value="total" type="hidden" />';
        strAction = "total";
        $("#action_div").html(str);
    } else if (action == 'new')
    {
        str = '<input name="actype" value="new" type="hidden" />';
        strAction = "new";
        $("#action_div").html(str);
    } else if (action == 'pending')
    {
        str = '<input name="actype" value="pending" type="hidden" />';
        strAction = "pending";
        $("#action_div").html(str);
    } else if (action == 'completed')
    {
        str = '<input name="actype" value="completed" type="hidden" />';
        strAction = "completed";
        $("#action_div").html(str);
    } else if (action == 'filter')
    {
        str = '<input name="actype" value="filter" type="hidden" />';
        strAction = "filter";
        $("#action_div").html(str);
    }
    console.log('before mainform');
    //var frm = $('input[name="frm"]').val();

    var role = $('input[name="role"]').val();
    var reject_click = $('input[name="reject_click"]').val();
    var frm = [];
    var stat = [];
    $.each($("input[name='frm']:checked"), function () {
        frm.push($(this).val());
    });
    $.each($("input[name='stat']:checked"), function () {
        stat.push($(this).val());
    });
    console.log(frm);
    if (frm.length < 1 && stat.length < 1 && strAction == "filter") {
        console.log("Nothing is selected in filter panel")
        setAction('pending');
    } else {
        var frmdata = {frm: frm, actype: strAction, role: role, stat: stat};
        var data = JSON.stringify(frmdata);
        $.ajax({
            type: "POST",
            url: "rawShowUserData",
            data: data,
            datatype: JSON,
            contentType: 'application/json; charset=utf-8',
            success: function (data) {
                $('.loader').hide();
                var d = JSON.stringify(data.listBeanObj.data);
                var d = JSON.parse(d);
                var otherData = JSON.stringify(data.listBeanObj);
                var otherData = JSON.parse(otherData);
                var tblStr = '';

                $(".caption-subject").html(otherData.heading);
                for (i = 0; i < d.length; i++) {
                    tblStr += '<tr class="odd gradeX">\
                                <td class="track_id" id="' + d[i]._stat_reg_no + '" ><a href="#" class="previewedit" id="true">' + d[i]._stat_reg_no + '</a></td>\\n\
                                <input type="hidden" value="" id="form_val">\
                                <input type="hidden" value="" id="ref_no">\
                                <input type="hidden" value="USER" id="panel">\
                                <td>' + d[i]._email + '</td>\
                                <td><span class="label label-sm label-success">' + d[i]._stat_type + '</span></td>';
                    if (d[i]._stat_createdon === null) {
                        tblStr += '<td class="center"></td>';
                    } else {
                        tblStr += '<td class="center">' + d[i]._stat_createdon.split(' ')[0] + '</td>';
                    }
                    tblStr += '<td><div class="btn-group curr_reg_btn" id="' + d[i]._stat_reg_no + '">';

                    if (d[i].showQuery) {
                        tblStr += '<button  class="btn btn-primary btn-xs green dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false" title="Query Raised, Please click on Raise/Respond to Query Link"> Actions \
                                    <i class="fa fa-question-circle" style="color:#FFEB3B;"></i>\
                                    <i class="fa fa-angle-down"></i>\
                                    </button>';
                    } else {
                        tblStr += '<button  class="btn btn-primary btn-xs green dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false" title="action"> Actions  \
                                    <i class="fa fa-angle-down"></i>\
                                    </button>';
                    }
                    if (d[i].showResponse) {
                        tblStr += '<ul class="dropdown-menu dropdown-menu-right action-btn-list" role="menu" style="position:inherit;" >\
                                    <li><a class="previewedit"  id="' + d[i].showAction + '">\
                                    <i class="fa fa-edit"></i> Preview ';
                        if (d[i].showdnsEdit) {
                            tblStr += '/Edit';
                        }
                        tblStr += ' </a>\
                                    </li>\
                                    <li>\
                                    <a class="" id="' + d[i].showAction + '" onclick="approve_ca(\'' + d[i]._stat_reg_no + '\', \'' + d[i]._stat_form_type + '\')">\
                                    <i class="fa fa-edit"></i> Approve \
                                    </a>\
                                    </li>';
                        if (d[i].isReject) {
                            tblStr += ' <li>\
                                        <a class="" id="">\
                                        <i class="fa fa-edit"></i> Resend \
                                        </a>\
                                        </li>';
                        }
                        tblStr += '</ul>';
                    } else {
                        if (d[i]._stat_reg_no.includes("DOREXTRT-FORM")) {
                            tblStr += '<ul class="dropdown-menu dropdown-menu-right action-btn-list" role="menu" style="position:inherit;" >\
                                    <li>\
                                    <a class="previewedit" id="' + d[i].showAction + '">\
                                    <i class="fa fa-edit"></i> Preview ';
                            if (d[i].showAction) {
                                tblStr += ' / Edit ';
                            }
                            tblStr += ' </a>\
                        </li>';
                            if (d[i].isReject) {
                                tblStr += ' <li>\
                                        <a class="resend"><i class="fa fa-edit"></i> Resend </a>\
                                        </li>';
                            }
                            if (d[i].showAction) {
                                tblStr += ' <li>\
                                        <a class="reject">\
                                        <i class="fa fa-comment"></i> Reject </a>\
                                        </li>';
                            }
                            if (d[i].showBulkLink) {
                                tblStr += ' <li>\
                                        <a data-toggle="modal" href="#bulk_view_modal" onclick="prefillViewBulkData(\'' + d[i]._stat_reg_no + '\', \'' + d[i]._stat_form_type + '\')" >\
                                        <i class="fa fa-step-forward"></i> View Bulk User IDS</a>\
                                        </li>\
                                        <li>  \
                                        <a href="sdownload?regNo=' + d[i]._stat_reg_no + '&type=success">Success Document</a>\
                                        </li>\
                                        <li>\
                                        <a href="download?regNo=' + d[i]._stat_reg_no + '&type=error">Error Document</a>\
                                        </li>';
                            }
                            if (d[i]._stat_type != 'Manual') {
                                tblStr += '<li><a class="track" onclick="track_frm(\'' + d[i]._stat_reg_no + '\')"><i class="fa fa-map-marker"></i> Track </a></li>';
                            }
                            if (d[i].pdf_path == 'manual_upload' && d[i]._stat_type != 'Cancelled') {
                                tblStr += ' <li>\
                                        <a class="fupload_1" title="Click here to Upload sealed and signed form in PDF Format ">\
                                        <i class="fa fa-upload"></i> Upload/Change Scanned Form </a>\
                                        </li>';
                            }
                            if (d[i]._stat_type == 'Pending with RO/Nodal/FO' && d[i].sign_cert != null && d[i].rename_sign_cert != null) {
                                tblStr += ' <li>\
                                        <a href="download_4?ref_num=' + d[i]._stat_reg_no + '&stat_form_type=' + d[i]._stat_form_type + '" title=""Click here to Download sealed and signed form in PDF Format">\
                                        <i class="fa fa-download" ></i> Download Scanned Form </a>\
                                        </li>';
                            }
                            tblStr += ' <li>\
                                        <a href="download_1_retired?ref_num=' + d[i]._stat_reg_no + '&cert_fileFileName=download_esigned" title="Click here to Download e-Signed form in PDF Format">\
                                        <i class="fa fa-download"></i> Download esigned document  </a>\
                                        </li>';

                            tblStr += '</ul>';
                        } else {
                            tblStr += '<ul class="dropdown-menu dropdown-menu-right action-btn-list" role="menu" style="position:inherit;" >\
                                    <li>\
                                    <a class="previewedit" id="' + d[i].showAction + '">\
                                    <i class="fa fa-edit"></i> Preview ';
                            if (d[i].showAction) {
                                tblStr += ' / Edit ';
                            }
                            tblStr += ' </a>\
                        </li>';
                            if (d[i].isReject) {
                                tblStr += ' <li>\
                                        <a class="resend"><i class="fa fa-edit"></i> Resend </a>\
                                        </li>';
                            }
                            if (d[i].showAction) {
                                tblStr += ' <li>\
                                        <a class="reject">\
                                        <i class="fa fa-comment"></i> Reject </a>\
                                        </li>';
                            }
                            if (d[i].showBulkLink) {
                                tblStr += ' <li>\
                                        <a data-toggle="modal" href="#bulk_view_modal" onclick="prefillViewBulkData(\'' + d[i]._stat_reg_no + '\', \'' + d[i]._stat_form_type + '\')" >\
                                        <i class="fa fa-step-forward"></i> View Bulk User IDS</a>\
                                        </li>\
                                        <li>  \
                                        <a href="sdownload?regNo=' + d[i]._stat_reg_no + '&type=success">Success Document</a>\
                                        </li>\
                                        <li>\
                                        <a href="download?regNo=' + d[i]._stat_reg_no + '&type=error">Error Document</a>\
                                        </li>';
                            }
                            if (d[i]._stat_type != 'Manual') {
                                tblStr += '<li><a class="track" onclick="track_frm(\'' + d[i]._stat_reg_no + '\')"><i class="fa fa-map-marker"></i> Track </a></li>';
                            }
                            tblStr += ' <li>\
                                    <a href="generatefile?data=' + d[i]._stat_reg_no + '" title="Click here to Download the filled form in PDF">\
                                    <i class="fa fa-download"></i> Generate Form </a>\
                                    </li>';
                            if (d[i].pdf_path == 'manual_upload' && d[i]._stat_type != 'Cancelled') {
                                tblStr += ' <li>\
                                        <a class="fupload_1" title="Click here to Upload sealed and signed form in PDF Format ">\
                                        <i class="fa fa-upload"></i> Upload/Change Scanned Form </a>\
                                        </li>';
                            }
                            if (d[i]._stat_type == 'Pending with RO/Nodal/FO' && d[i].sign_cert != null && d[i].rename_sign_cert != null) {
                                tblStr += ' <li>\
                                        <a href="download_4?ref_num=' + d[i]._stat_reg_no + '&stat_form_type=' + d[i]._stat_form_type + '" title=""Click here to Download sealed and signed form in PDF Format">\
                                        <i class="fa fa-download" ></i> Download Scanned Form </a>\
                                        </li>';
                            }
                            if (d[i]._stat_type == 'Pending with RO/Nodal/FO' && d[i].pdf_path != 'online processing' && d[i].pdf_path != 'manual_upload') {
                                tblStr += ' <li>\
                                        <a href="download_1?ref_num=' + d[i]._stat_reg_no + '&cert_fileFileName=download_esigned" title="Click here to Download e-Signed form in PDF Format">\
                                        <i class="fa fa-download"></i> Download esigned document  </a>\
                                        </li>';
                            }
                            tblStr += ' <li>\
                                    <a class="fuploadmul_file" onclick="fuploadmul(\'' + d[i]._stat_reg_no + '\')"  title="Click here to Upload relevant Documents in PDF Format. Example: Office ID Proof, Letter of consent, etc.">\
                                    <i class="fa fa-upload"></i> Upload Multiple Docs</a>\
                                    </li>\
                                    <li>\
                                    <a class="fdownload" onclick="fdownload(\'' + d[i]._stat_reg_no + '\')" title="Click here to Download relevant Documents uploaded by you in PDF Format">\
                                    <i class="fa fa-download"></i> Download Uploaded Docs</a>\
                                    </li>\
                                    <li>\
                                    <a data-toggle="modal" href="#query_raise" onclick="apply_heading(\'' + d[i]._stat_reg_no + '\', \'' + d[i]._stat_form_type + '\', \'query_raise\');resetRandom();"  >\
                                    <i class="fa fa-comment"></i> Raise/Respond to Query </a>\
                                    </li>\
                                    </ul>';
                        }
                    }
                    tblStr += '</div></td></tr>';
                }
                $('#rawFilterAction').html('');
                $('#example').dataTable().fnDestroy()
                $('#rawFilterAction').html(tblStr);
                $('#example').dataTable({
                    "order": [[3, "desc"]],
                    "bDestroy": true,
                    "aoColumnDefs": [{"bSortable": false, "aTargets": [-1]}]
                    , language: {searchPlaceholder: "Reg Id, Email, Status, Date"}
                });
            },
            error: function () {
                $('.loader').hide();
                console.log('error');
            },
            complete: function () {
                $('.loader').hide();
            }
        });

    }
}
// ******************************* Added By  ************************************************ //                                     
function openAllTooltip() {
    $("[data-toggle='popover']").popover('show');
}
function closeAllTooltip() {
    $("[data-toggle='popover']").popover('hide');
}
$('.fillter-div-max-tabs').bind('contextmenu', function (e) {
    return false;
}); 