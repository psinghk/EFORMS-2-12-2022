var dataCollection = [];
var tmpdataCollection = [];

function checkForDelete(data, ddt) {
    var dt = data.split('~');
    if($(ddt).is(':checked')) {
        dataCollection.push(dt);
        $("#row-" + dt[0]).addClass('del-row-select');
    } else {
        
        delete dataCollection[dt[0]];
        tmpdataCollection = dataCollection;
        dataCollection = [];
        $.each(tmpdataCollection, function (key, val) {
            if(val) {
                dataCollection.push(val);
            }
        });
        $("#row-" + dt[0]).removeClass('del-row-select');
    }
    console.log(dataCollection)
    $("#row-" + dt[0]).addClass('del-row-select');
    var chk_count = $('input[name=deleted_value]:checked').length;
    if (chk_count > 0) {
        $("#vpn_renew_button button").html('Continue');
    } else {
        $("#vpn_renew_button button").html('Add New');
    }
} 

// add new row 
$(document).on('click', "#vpn_addnew", function () {
    $('#modal_id').modal('toggle');
    $('#vpn_renew_form').html('');
    $('#vpn_renew_button').html('');
    var CSRFRandom = $("#CSRFRandom").val();
    var data = $("#vpn_reg_no").val();
    $.ajax({
        type: "POST",
        url: "vpn_fatch2",
        data: {data: data, CSRFRandom: CSRFRandom},
        dataType: 'text',
        async: false,
        success: function (result)
        {
            var jvalue = JSON.parse(result);
            var api_details = JSON.parse(jvalue.api_response);
            var data = api_details.access_details;
            console.log(api_details.expdate);
            if (data !== undefined) {
                var a = '<h6 class="mb-3"><b style="font-size: 15px;">Existing Entries</b></h6>'
                a += "<table class='table table-striped table-bordered' id='existing_tbl'><thead><tr><th>Server IP</th><th>Server Location</th><th>Destination Port</th><th>Service</th></tr></thead><tbody>";
                $.each(data, function (key, value) {
                    a += ("<tr><td><input type='hidden' name='" + key + "' value ='" + value.serip + "'/> " + value.serip + " </td>" + "<td ><input type='hidden' name='" + key + "' value ='" + value.serloc + "'/> " + value.serloc + " </td><td> <input type='hidden' name='" + key + "' value ='" + value.destport + "'/>" + value.destport + "  </td> <td> <input type='hidden' name='" + key + "' value ='" + value.desc_service + "'/>" + value.desc_service + " </td></tr>");
                });
                a += "</tbody></table>" + "<input type='hidden' id='req_for' name='' value='vpn_renew'/> <input type='hidden'value='" + $("#vpn_reg_no").val() + "' name='vpn_reg_no' id='vpn_reg_no' />";
                $("#vpn_access_details_new").append(a);
                $("#vpn_renew_form_new").html('');
                $("#vpn_renew_form_new").html(a);
                $("#existing_tbl").DataTable({
                    'pageLength': 5
                });
                $("#new_ip1").focus();
                if (dataCollection.length > 0) {
                    createDeleteDom();
                }
                resetCSRFRandom();
            }
        }, error: function (data) {
            console.log(data);
        }
    });
});

function createDeleteDom() {
    $.each(dataCollection, function (key, val) {
        console.log(val)
        if(val) {
            if (key == 0) {
                $.each(val, function (k, v) {
                    console.log(v)
                    $("#delete_this_entry_0 input[type=checkbox]").attr('disabled', false);
                    $("#delete_this_entry_0 input[type=checkbox]").attr('readonly', true);
                    $("#delete_this_entry_0 input[type=checkbox]").attr('checked', true);
                    $("#delete_this_entry_0 input[type=checkbox]").val(1);
                    $("#delete_this_entry_0").removeClass('d-none');
                    if (k === 1) {

                        $('input.vpn_ip_type:not(:checked)').prop('disabled', true);
                        $('#detailContainer_div_radio').attr('readonly', true);

                        if (v.indexOf('-') != -1) {
                            $('#ip_range_' + key).attr('checked', true);
                            var ip_arr = v.split('-');
                            $('#detailContainer_div_radio #ip_range_' + key).attr('checked', true);
                            $("#detailContainer_div_radio #new_ip2").val(ip_arr[0]);
                            $("#detailContainer_div_radio #new_ip2").attr('readonly', true);
                            $("#detailContainer_div_radio #new_ip3").val(ip_arr[1]);
                            $("#detailContainer_div_radio #new_ip3").attr('readonly', true);
                            $("#detailContainer_div_radio #new_ip1").val('');
                            $("#detailContainer_div_radio #ip_range_div").removeClass('display-hide');
                            $("#detailContainer_div_radio #ip_div").addClass('display-hide');
                            $("#detailContainer_div_radio").find('.col-md-4').addClass("col-md-3").removeClass("col-md-4");

                        } else {
                            $("#detailContainer_div_radio #new_ip1").val(v);
                            $("#detailContainer_div_radio #new_ip1").attr('readonly', true);
                        }
                    }
                    if (k === 3) {
                        $("#detailContainer_div_radio #dest_port").val(v);
                        $("#detailContainer_div_radio #dest_port").attr('readonly', true);
                    }
                })
            } else {
                var div = document.createElement('DIV');
                div.setAttribute("class", "row");
                div.innerHTML = getDeleteDynamicTextBox(key, val);
                document.getElementById("detailContainer").appendChild(div);
                $("select.form-control").change(function () {
                    var data = $(this).val();
                    if (data === "Other") {
                        $(this).closest(".detailContainer_div_select").find("#server_other").removeClass('display-hide');
                    } else {
                        $(this).closest(".detailContainer_div_select").find("#server_other").addClass('display-hide');
                    }
                });
            }
        }
    })
}
function getDeleteDynamicTextBox(key, value) {
    lol++;
    var html = '';
    html += '<div id="detailContainer_div_radio" class="detailContainer_div_radio col-md-11 col-10">\n\
 <div class="ip_div_2">\n\
 <div class="row_delete" id="vpn_ip_add_div">\n\
 <div class="col-md-6"> \n\
 <label class="control-label" for="street">IP Address <span style="color: red">*</span></label><br/> \n\
 <div="mt-2">';
    if (value[1].indexOf('-') != -1) {
        html += '<label class="k-radio k-radio--bold k-radio--brand"><input type="radio" name="vpn_ip_type[' + key + ']" id="ip_range" class="vpn_ip_type" value="range" checked> IP Range <span></span></label>';
    } else {
        html += '<label class="k-radio k-radio--bold k-radio--brand"><input type="radio" name="vpn_ip_type[' + key + ']" id="ip_single" class="vpn_ip_type" value="single" checked> Single IP <span></span></label>'
    }
    html += '<font style="color:red"><span id="ip_type_err"></span></font></div> \n\
 <div class="col-md-6"><label class="k-checkbox k-checkbox--bold k-checkbox--danger float-right" id="delete_this_entry_'+key+'"><input type="checkbox" name="delete_this_entry[' + key + ']" value="1" onclick="deleteVpnEntry(this)" checked><span></span>Delete this VPN Entry</label></div></div></div>\n\
 <div class="row ip_div_1">';
    if (value[1].indexOf('-') != -1) {
        var ip_arr = value[1].split('-');
        html += '<div class="col-md-4 ip_div d-none" id="ip_div"> \n\
    <label class="control-label" for="street">Enter IP address <span style="color: red">*</span></label>\n\
    <input class="form-control" placeholder="Enter IP Address [e.g: 10.10.10.10]" type="text" name="vpn_new_ip1" id="new_ip1" maxlength="100" aria-required="true" value="' + value[1] + '" readonly> <font style="color:red"><span id="new_ip1_err"></span></font> \n\
    </div><div class="col-md-6" id="ip_range_div"> \n\
    <div class="row"><div class="col-md-3"> \n\
    <label class="control-label" for="street">Enter IP range (From) <span style="color: red">*</span></label>\n\
    <input class="form-control" placeholder="Enter IP range (From) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip2" id="new_ip2" maxlength="100" value="' + ip_arr[0] + '" aria-required="true" readonly> <font style="color:red"><span id="new_ip2_err"></span></font>\n\
    </div>\n\
    <div class="col-md-3">\n\
    <label class="control-label" for="street">Enter IP range (To) <span style="color: red">*</span></label>\n\
    <input class="form-control" placeholder="Enter IP range (To) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip3" id="new_ip3" value="' + ip_arr[1] + '" maxlength="100" aria-required="true" readonly> <font style="color:red"><span id="new_ip3_err"></span></font>\n\
    </div></div>\n\
    </div><div class="col-md-3">\n\
    <label class="control-label" for="street">Application URL <span style="color: red"></span></label>\n\
    <input class="form-control" placeholder="Enter Application URL [e.g: (abc.com)] " type="text" name="vpn_app_url" id="app_url" value="" maxlength="100" aria-required="true" readonly> <font style="color:red"><span id="app_url_err"></span></font>\n\
    </div>\n\
    <div class="col-md-3">\n\
    <label class="control-label" for="street">Destination Port <span style="color: red">*</span></label>\n\
    <input placeholder="Enter Destination Port [e.g: 80,443] " type="text" name="vpn_dest_port" id="dest_port" value="' + value[3] + '" class="form-control" readonly> <font style="color:red"><span id="dest_port_err"></span></font>\n\
    </div></div>';
    } else {
        html += '<div class="col-md-4 ip_div" id="ip_div"> \n\
    <label class="control-label" for="street">Enter IP address <span style="color: red">*</span></label>\n\
    <input class="form-control" placeholder="Enter IP Address [e.g: 10.10.10.10]" type="text" name="vpn_new_ip1" id="new_ip1" maxlength="100" aria-required="true" value="' + value[1] + '" readonly> <font style="color:red"><span id="new_ip1_err"></span></font> \n\
    </div><div class="col-md-4">\n\
    <label class="control-label" for="street">Application URL <span style="color: red"></span></label>\n\
    <input class="form-control" placeholder="Enter Application URL [e.g: (abc.com)] " type="text" name="vpn_app_url" id="app_url" value="" maxlength="100" aria-required="true" readonly> <font style="color:red"><span id="app_url_err"></span></font>\n\
    </div>\n\
    \n\<div class="display-hide col-md-6" id="ip_range_div"> \n\
     <div class="row"><div class="col-md-6"> \n\
     <label class="control-label" for="street">Enter IP range (From) <span style="color: red">*</span></label>\n\
     <input class="form-control" placeholder="Enter IP range (From) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip2" id="new_ip2" maxlength="100" aria-required="true"> <font style="color:red"><span id="new_ip2_err"></span></font>\n\
     </div>\n\
     <div class="col-md-6 ">\n\
     <label class="control-label" for="street">Enter IP range (To) <span style="color: red">*</span></label>\n\
     <input class="form-control" placeholder="Enter IP range (To) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip3" id="new_ip3" value="" maxlength="100" aria-required="true"> <font style="color:red"><span id="new_ip3_err"></span></font>\n\
     </div></div></div>\n\
    <div class="col-md-4">\n\
    <label class="control-label" for="street">Destination Port <span style="color: red">*</span></label>\n\
    <input placeholder="Enter Destination Port [e.g: 80,443] " type="text" name="vpn_dest_port" id="dest_port" value="' + value[3] + '" class="form-control" readonly> <font style="color:red"><span id="dest_port_err"></span></font>\n\
    </div>';
    }

    html += '</div>\n\
 <div class="row detailContainer_div_select">\n\
 <div class="col-md-6">\n\
 <label class="control-label" for="street">Server Location<span style="color: red">*</span></label>\n\
 <select name="vpn_server_loc" class="form-control" id="server_loc">\n\
 <option value="NDC Delhi">NDC Delhi</option>\n\
 <option value="NDC Pune">NDC Pune</option>\n\
 <option value="NDC Hyderabad">NDC Hyderabad</option>\n\
 <option value="NDC Bhubaneswar">NDC Bhubaneswar</option>\n\
 <option value="Other">Other</option>\n\
 </select>\n\
 <font style="color:red"><span id="server_loc_err"></span></font>\n\
 </div>\n\
 <div class="col-md-6 display-hide" id="server_other">\n\
 <label class="control-label" for="street">Enter server location <span style="color: red">*</span></label>\n\
 <input class="form-control" placeholder="Enter Server Location [characters,dot(.) and whitespace]" type="text" name="vpn_server_loc_txt" id="server_loc_txt" value="" maxlength="100"> <font style="color:red"><span id="server_txt_err"></span></font>\n\
 </div>\n\
 </div>\n\
 </div>\n\
 </div>\n\
 <div class="add-ip-div col-md-1 col-2"><div class="btn-div"><button class="btn btn-primary float-right" type="button" onclick="AddTextBox()"><i class="fa fa-plus"></i></button> <button type="button" value="Remove" onclick="RemoveTextBox(this)" class="btn btn-danger float-right mt-2" title="Remove Row"><i class="fa fa-minus"></i></button></div></div>';
    return html;
}

function deleteVpnEntry(div) {
    bootbox.confirm({
        title: "Delete this Record",
        message: "Are you sure? Do you want to delete this Record?",
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
                $(div).closest('.row').remove();
                $(div).parent().remove();
            } else{
                $(div).prop('checked', true);
            }
        }
    });
}