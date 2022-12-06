/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {
    var kidd;
    $(".formUpdate").click(function (e) {
        $('.loader').show();
        e.preventDefault();

        var field_name = $(this).data("value");
        if (field_name === 'formname') {
            var field_id = $(this).attr('id');
            if (field_id === "") {
                if (kidd !== "") {
                    field_id = kidd;
                    $('#example').DataTable().fnDestroy();
                }
            }
            $.ajax({
                type: "POST",
                url: "updateCO_getdata",
                data: {data: field_id},
                datatype: JSON,
                success: function (data)
                {

                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    if (jsonvalue.list.length > 1) {

                        console.log(" field_id value is " + field_id);

                        if (field_id === 'email') {
                            setTimeout(function () {
                                //$( '#example thead tr th:eq( 2 )' ).after('<th id="example_domain">Domain</th>');

                                $('.tabData').html('<table class="table table-striped table-bordered table-hover table-checkable order-column dt-responsive nowrap" id="example"><thead> <tr> <th>Category</th> <th>Ministry</th> <th>Department</th><th>Domain</th><th>Co-ordinator Name</th> <th>Co-ordinator Email</th> <th>Co-ordinator Mobile</th> <th>Admin Email</th> <th>Allowed Ip</th> <th width="10%">Update Action</th> </tr> </thead> <tbody> </tbody></table>');
                                //$('#example').html('');
                                $("#example").DataTable({
                                    "data": jsonvalue.list,
                                    "responsive": true,
                                    "bDestroy": true,
                                    columns: [
                                        {"data": "empcat"},
                                        {"data": "empmin"},
                                        {"data": "empdep"},
                                        {"data": "dom"},
                                        {"data": "cname"},
                                        {"data": "cemail"},
                                        {"data": "cmobile"},
                                        {"data": "aemail"},
                                        {"data": "ip"},
                                        {"data": "updateButton"}
                                    ]
                                });
                                $('.loader').hide();

                            }, 20);

                            $("#admin_request_heading").html('Co-ordinator list for ' + field_id + ' Form');
                        } else if (field_id === 'vpn') {
                            $('.tabData').html('<table class="table table-striped table-bordered table-hover table-checkable order-column dt-responsive nowrap" id="example"><thead> <tr> <th>Category</th> <th>Ministry</th> <th>Department</th><th>Co-ordinator Name</th> <th>Co-ordinator Email</th> <th>Co-ordinator Mobile</th> <th>Admin Email</th> <th>Allowed Ip</th> <th width="10%">Update Action</th><th width="10%">Delete Action</th> </tr> </thead> <tbody> </tbody></table>');
                            setTimeout(function () {
                                $("#example").DataTable({
                                    "data": jsonvalue.list,
                                    "responsive": true,
                                    "bDestroy": true,
                                    columns: [
                                        {"data": "empcat"},
                                        {"data": "empmin"},
                                        {"data": "empdep"},
                                        {"data": "cname"},
                                        {"data": "cemail"},
                                        {"data": "cmobile"},
                                        {"data": "aemail"},
                                        {"data": "ip"},
                                        {"data": "updateButton"},
                                        {"data": "deleteButton"}
                                    ]
                                });
                                $('.loader').hide();
                            }, 20);
                            $("#admin_request_heading").html('Co-ordinator list for ' + field_id + ' Form');
                        } else {
                            $('.tabData').html('<table class="table table-striped table-bordered table-hover table-checkable order-column dt-responsive nowrap" id="example"><thead> <tr> <th>Category</th> <th>Ministry</th> <th>Department</th><th>Co-ordinator Name</th> <th>Co-ordinator Email</th> <th>Co-ordinator Mobile</th> <th>Admin Email</th> <th>Allowed Ip</th> <th width="10%">Update Action</th> </tr> </thead> <tbody> </tbody></table>');
                            setTimeout(function () {
                                $("#example").DataTable({
                                    "data": jsonvalue.list,
                                    "responsive": true,
                                    "bDestroy": true,
                                    columns: [
                                        {"data": "empcat"},
                                        {"data": "empmin"},
                                        {"data": "empdep"},
                                        {"data": "cname"},
                                        {"data": "cemail"},
                                        {"data": "cmobile"},
                                        {"data": "aemail"},
                                        {"data": "ip"},
                                        {"data": "updateButton"}
                                    ]
                                });
                                $('.loader').hide();


                            }, 20);
                            $("#admin_request_heading").html('Co-ordinator list for ' + field_id + ' Form');
                        }
                        $("#Co-data").removeClass('display-hide');
                        $("#formname").val(field_id);
                    } else {
                        $("#Co-data").addClass('display-hide');
                    }
                }, error: function ()
                {
                    $('.loader').hide();
                }
            });
        } else if (field_name === "update_ministry") {
            $('.loader').hide();
            $('#update_min_form #new_min_dept').val('');
            $('#update_min_form #new_min_dept_error').html('');
            $.get('centralMinistry', {
                orgType: 'Central'
            }, function (response) {
                var select = $('#update_min_form #min');
                select.find('option').remove();
                $('<option>').val("").text("-SELECT-").appendTo(select);
                $.each(response, function (index, value) {
                    $('<option>').val(value).text(value).appendTo(select);
                });
            });
            $("#update_min_modal").modal('show');
        } else if (field_name === "update_dept") {
            $('.loader').hide();
            $('#update_dept_form #new_min_dept').val('');
            $('#update_dept_form #new_min_dept_error').html('');
            $("#update_dept_modal").modal('show');
        }
    });

    $("#add_reqs").click(function () {
        var add_data = $('#formname').val();
        if (add_data === 'email') {
            $("#domain_div").removeClass('display-hide');
        } else {
            $("#domain_div").addClass('display-hide');
        }

        if (add_data === 'vpn') {
            $("#hod_email").val('vpnsupport@nic.in').attr('readonly', true);
            $("#select_dept_mandate").addClass('display-hide');
        } else {
            $("#select_dept_mandate").removeClass('display-hide');
        }
        $("#add_reqs_modal").modal('show');
    });

    $(document).on('change', '#user_employment', function () {
        var field_form = $(this).closest('form').attr('id');
        $('#' + field_form + ' #other_text_div').addClass("display-hide");
        $('#' + field_form + ' #other_dept').val("");
        var orgname = $('#' + field_form + ' #user_employment').val();
        if (orgname === null || orgname === "") {
            $('#' + field_form + ' #useremployment_error').html("Select organization category");
        } else {
            var regn = "^[a-zA-Z]{1,8}$";
            if (orgname.match(regn)) {
                $('#' + field_form + ' #useremployment_error').html("");
                if (orgname === "Central" || orgname === "UT") {
                    $('#' + field_form + ' #min_state_org_label').html("Ministry/Organization");
                    $('#' + field_form + ' #other_text_div').addClass("display-hide");
                    $('#' + field_form + ' #dept_div').removeClass("display-hide");
                    $.get('centralMinistry', {
                        orgType: orgname
                    }, function (response) {
                        var select = $('#' + field_form + ' #min');
                        select.find('option').remove();
                        $('<option>').val("").text("-SELECT-").appendTo(select);
                        $.each(response, function (index, value) {
                            $('<option>').val(value).text(value).appendTo(select);
                        });
                    });
                } else if (orgname === "State") {
                    $('#' + field_form + ' #min_state_org_label').html("State");
                    $('#' + field_form + ' #other_text_div').addClass("display-hide");
                    $('#' + field_form + ' #dept_div').removeClass("display-hide");
                    $('#' + field_form + ' #min').val("Delhi");
                    $.get('centralMinistry', {
                        orgType: orgname
                    }, function (response) {
                        var select = $('#' + field_form + ' #min');
                        select.find('option').remove();
                        $('<option>').val("").text("-SELECT-").appendTo(select);
                        $.each(response, function (index, value) {
                            $('<option>').val(value).text(value).appendTo(select);
                        });
                        if (field_form !== 'update_dept_form') {
                            if (response.toString().toLowerCase().indexOf("other") == -1)
                            {
                                if (response.toString().toLowerCase().indexOf("other") > -1) {
                                } else {
                                    $('<option>').val("other").text("other").appendTo(select);
                                }
                            }
                        }
                    });
                } else if (orgname === "Others" || orgname === "Psu" || orgname === "Const" || orgname === "Nkn" || orgname === "Project") {
                    $('#' + field_form + ' min_state_org_label').html("Organization Name");
                    $('#' + field_form + ' #other_text_div').addClass("display-hide");
                    $('#' + field_form + ' #dept_div').addClass("display-hide");
                    $.get('centralMinistry', {
                        orgType: orgname
                    }, function (response) {
                        var select = $('#' + field_form + ' #min');
                        select.find('option').remove();
                        $('<option>').val("").text("-SELECT-").appendTo(select);
                        $.each(response, function (index, value) {
                            $('<option>').val(value).text(value).appendTo(select);
                        });
                        if (field_form !== 'update_dept_form') {
                            if (response.toString().toLowerCase().indexOf("other") == -1)
                            {
                                if (response.toString().toLowerCase().indexOf("other") > -1) {
                                } else {
                                    $('<option>').val("other").text("other").appendTo(select);
                                }
                            }
                        }
                    });
                }
            } else {
                $('#' + field_form + ' #useremployment_error').html("Select organization category in correct format");
            }
        }
    });

    $(document).on('change', '#min', function () {
        var field_form = $(this).closest('form').attr('id');
        var catname = $('#' + field_form + ' #user_employment').val();
        var orgname = $('#' + field_form + ' #min').val();
        if (catname === "Central" || catname === "UT") {
            $.get('centralDepartment', {
                depType: orgname
            }, function (response) {
                var select = $('#' + field_form + ' #dept');
                select.find('option').remove();
                $('<option>').val("").text("-SELECT-").appendTo(select);
                $.each(response, function (index, value) {
                    $('<option>').val(value).text(value).appendTo(select);
                });
                if (field_form !== 'update_dept_form') {
                    if (response.toString().toLowerCase().indexOf("other") == -1)
                    {
                        if (response.toString().toLowerCase().indexOf("other") > -1) {
                        } else {
                            $('<option>').val("other").text("other").appendTo(select);
                        }
                    }
                }
            });
        } else if (catname === "State") {
            $.get('centralDepartment', {
                depType: orgname
            }, function (response) {
                var select = $('#' + field_form + ' #dept');
                select.find('option').remove();
                $('<option>').val("").text("-SELECT-").appendTo(select);
                $.each(response, function (index, value) {
                    $('<option>').val(value).text(value).appendTo(select);
                });
                if (field_form !== 'update_dept_form') {
                    if (response.toString().toLowerCase().indexOf("other") == -1)
                    {
                        if (response.toString().toLowerCase().indexOf("other") > -1) {
                        } else {
                            $('<option>').val("other").text("other").appendTo(select);
                        }
                    }
                }
            });
        } else if (catname === "Others" || catname === "Psu" || catname === "Const" || catname === "Nkn" || catname === "Project") {
            if (orgname.toString().toLowerCase() === 'other')
            {
                $('#' + field_form + ' #other_text_div').removeClass("display-hide");
            } else {
                $('#' + field_form + ' #other_text_div').addClass("display-hide");
            }
        }
    });

    $(document).on('change', '#dept', function () {
        var field_form = $(this).closest('form').attr('id');
        var catname = $('#' + field_form + ' #user_employment').val();
        var orgname = $('#' + field_form + ' #min').val();
        var add_data = $('#formname').val();
        var dept = $('#' + field_form + ' #dept').val();
        console.log('DEPT: '+ dept)
        if (dept === 'Other' || dept === 'other')
        {
            $.ajax({
                type: "POST",
                url: "check_otherdept",
                data: {data: catname, test: orgname, add_data: add_data,dept: dept},
                datatype: JSON,
                success: function (data) {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    if (jsonvalue.returnString === "NA") {
                        $('#' + field_form + ' #other_text_div').removeClass("display-hide");
                    } else {
                        $('#' + field_form + ' #deperror').html("Co-ordinator already exists for this selection");
                        $('#' + field_form + ' #other_text_div').addClass("display-hide");
                    }
                }, error: function () {

                }
            });
        } else {
            $('#' + field_form + ' #other_text_div').addClass("display-hide");
        }
    });

    $(document).on('blur', '#ca_email', function () {
        var field_form = $(this).closest('form').attr('id');
        var ca_email = $('#' + field_form + ' #ca_email').val();
        $.ajax({
            type: "POST",
            url: "getCaValues",
            data: {data: ca_email},
            datatype: JSON,
            success: function (data)
            {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                if (jsonvalue.map.source !== null) {
                    if (jsonvalue.map.source === 'ldap') {
                        $('#' + field_form + ' #ca_email_error').html('');
                        if (jsonvalue.map.cn !== "") {
                            $('#' + field_form + ' #ca_name').val(jsonvalue.map.cn);
                            $('#' + field_form + ' #ca_name').attr("readonly", true);
                        } else {
                            $('#' + field_form + ' #ca_name').val("");
                            $('#' + field_form + ' #ca_name').attr("readonly", false);
                        }
                        if (jsonvalue.map.mobile !== "") {
                            $('#' + field_form + ' #ca_mobile').val(jsonvalue.map.mobile);
                            $('#' + field_form + ' #ca_mobile').attr("readonly", true);
                        } else {
                            $('#' + field_form + ' #ca_mobile').val("");
                            $('#' + field_form + ' #ca_mobile').attr("readonly", false);
                        }
                    } else {
                        $('#' + field_form + ' #ca_email_error').html('Email address not present in LDAP');
                    }
                } else {
                    $('#' + field_form + ' #ca_email_error').html('Email address not present in LDAP');
                }
            }, error: function ()
            {

            }
        });
    });

    $('#add_data').click(function (e) {
        e.preventDefault();
        $('.loader').show();
        var data = JSON.stringify($('#add_co_form').serializeObject());
        var add_data = $('#formname').val();
        $.ajax({
            type: "POST",
            url: "add_coordinator",
            data: {data: data, add_data: add_data},
            datatype: JSON,
            success: function (data) {
                $('.loader').hide();
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var flag = true;
                if (jsonvalue.error.useremployment_error !== null && jsonvalue.error.useremployment_error !== "" && jsonvalue.error.useremployment_error !== undefined)
                {
                    $('#add_co_form #useremployment_error').html(jsonvalue.error.useremployment_error);
                    flag = false;
                } else {
                    $('#add_co_form #useremployment_error').html("");
                }
                if (jsonvalue.error.minerror !== null && jsonvalue.error.minerror !== "" && jsonvalue.error.minerror !== undefined)
                {
                    $('#add_co_form #minerror').html(jsonvalue.error.minerror);
                    flag = false;
                } else {
                    $('#add_co_form #minerror').html("");
                }
                if (jsonvalue.error.deperror !== null && jsonvalue.error.deperror !== "" && jsonvalue.error.deperror !== undefined)
                {
                    $('#add_co_form #deperror').html(jsonvalue.error.deperror);
                    flag = false;
                } else {
                    $('#add_co_form #deperror').html("");
                }
                if (jsonvalue.error.other_dep_error !== null && jsonvalue.error.other_dep_error !== "" && jsonvalue.error.other_dep_error !== undefined)
                {
                    $('#add_co_form #other_dep_error').html(jsonvalue.error.other_dep_error);
                    flag = false;
                } else {
                    $('#add_co_form #other_dep_error').html("");
                }
                if (jsonvalue.error.domain_error !== null && jsonvalue.error.domain_error !== "" && jsonvalue.error.domain_error !== undefined)
                {
                    $('#add_co_form #domain_error').html(jsonvalue.error.domain_error);
                    flag = false;
                } else {
                    $('#add_co_form #domain_error').html("");
                }
                if (jsonvalue.error.ca_email_error !== null && jsonvalue.error.ca_email_error !== "" && jsonvalue.error.ca_email_error !== undefined)
                {
                    $('#add_co_form #ca_email_error').html(jsonvalue.error.ca_email_error);
                    flag = false;
                } else {
                    $('#add_co_form #ca_email_error').html("");
                }
                if (jsonvalue.error.ca_name_error !== null && jsonvalue.error.ca_name_error !== "" && jsonvalue.error.ca_name_error !== undefined)
                {
                    $('#add_co_form #ca_name_error').html(jsonvalue.error.ca_name_error);
                    flag = false;
                } else {
                    $('#add_co_form #ca_name_error').html("");
                }
                if (jsonvalue.error.ca_mobile_error !== null && jsonvalue.error.ca_mobile_error !== "" && jsonvalue.error.ca_mobile_error !== undefined)
                {
                    $('#add_co_form #ca_mobile_error').html(jsonvalue.error.ca_mobile_error);
                    flag = false;
                } else {
                    $('#add_co_form #ca_mobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#add_co_form #hod_email_error').html(jsonvalue.error.hod_email_error);
                    flag = false;
                } else {
                    $('#add_co_form #hod_email_error').html("");
                }
                if (jsonvalue.error.base_ip_error !== null && jsonvalue.error.base_ip_error !== "" && jsonvalue.error.base_ip_error !== undefined)
                {
                    $('#add_co_form #base_ip_error').html(jsonvalue.error.base_ip_error);
                    flag = false;
                } else {
                    $('#add_co_form #base_ip_error').html("");
                }
                if (!flag) {

                } else {
                    kidd = add_data;
                    $('a[id="' + add_data + '"]').trigger('click');
                    $('#add_reqs_modal').modal('toggle');
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 18px;'>Co-ordinator successfully added.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });
                }
            },
            error: function () {
                $('.loader').hide();
                console.log('error');
                return false;
            }
        });
    });

    $(document).on('click', '.updateEmp', function (e) {
        e.preventDefault();
        var id = $(this).attr('id');
        var add_data = $('#formname').val();
        $.ajax({
            type: "POST",
            url: "updateCO_getCOdata",
            data: {data: id, add_data: add_data},
            datatype: JSON,
            success: function (data)
            {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                $('#update_co_form select[name="user_employment"] option[value=' + jsonvalue.co_map.user_employment + ']').attr('selected', 'selected');                               

                $.get('centralMinistry', {
                    orgType: $.trim(jsonvalue.co_map.user_employment)
                }, function (response) {
                    var select = $("#update_co_form #min");
                    select.find('option').remove();
                    $('<option>').val(jsonvalue.co_map.min).text(jsonvalue.co_map.min).appendTo(select);
                    $.each(response, function (index, value) {
                        $('<option>').val(value).text(value).appendTo(select);
                    });
                });
                //jsonvalue.co_map.user_employment = "Central";
                if ($.trim(jsonvalue.co_map.user_employment) === "Central" || $.trim(jsonvalue.co_map.user_employment) === "UT") {
                    $("#update_co_form #min_state_org_label").html("Ministry/Organization");
                    $('#update_co_form #dept_div').removeClass("display-hide");
                    $.get('centralDepartment', {
                        depType: jsonvalue.co_map.min
                    }, function (response) {
                        //  if (add_data === 'email') {
                        $('#update_co_form #dept_div').removeClass("display-hide");
                        var select = $("#update_co_form #dept");
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.co_map.dept).text(jsonvalue.co_map.dept).appendTo(select);
                        $.each(response, function (index, value) {
                            $('<option>').val(value).text(value).appendTo(select);
                        });
                        if (response.toString().toLowerCase().indexOf("other") == -1)
                        {
                            if (response.toString().toLowerCase().indexOf("other") > -1) {
                            } else {
                                $('<option>').val("other").text("other").appendTo(select);
                            }
                        }
                        //  } 
                    });
                    $('#update_co_form #user_employment').prop('disabled', 'true');
                    if (add_data === 'vpn') {
//                        $('#update_co_form #min').prop('disabled', 'true');
//                        $('#update_co_form #dept').prop('disabled', 'true');
                    }else{
                        $('#update_co_form #min').prop('disabled', 'true');
                        $('#update_co_form #dept').prop('disabled', 'true');
                    }
                    if (jsonvalue.co_map.dept === 'Other') {
                        $('#update_co_form #other_text_div').removeClass("display-hide");
                        $('#update_co_form #other_dept').val(jsonvalue.co_map.other_dept);
                        $('#update_co_form #user_employment').prop('disabled', 'true');
                    } else {
                        $('#update_co_form #other_text_div').addClass("display-hide");
                    }


                } else if (jsonvalue.co_map.user_employment === "State") {
                    $("#update_co_form #min_state_org_label").html("State");
                    $('#update_co_form #dept_div').removeClass("display-hide");
                    $.get('centralDepartment', {
                        depType: jsonvalue.co_map.min
                    }, function (response) {
                        // if (add_data === 'email') {
                        $('#update_co_form #dept_div').removeClass("display-hide");
                        var select = $("#update_co_form #dept");
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.co_map.dept).text(jsonvalue.co_map.dept).appendTo(select);
                        $.each(response, function (index, value) {
                            $('<option>').val(value).text(value).appendTo(select);
                        });
                        if (response.toString().toLowerCase().indexOf("other") == -1)
                        {
                            if (response.toString().toLowerCase().indexOf("other") > -1) {
                            } else {
                                $('<option>').val("other").text("other").appendTo(select);
                            }
                        }
                        // } 
                    });
                    $('#update_co_form #user_employment').prop('disabled', 'true');
                    if (add_data === 'vpn') {
//                        $('#update_co_form #min').prop('disabled', 'true');
//                        $('#update_co_form #dept').prop('disabled', 'true');
                    }else{
                        $('#update_co_form #min').prop('disabled', 'true');
                        $('#update_co_form #dept').prop('disabled', 'true');
                    }
                    if (jsonvalue.co_map.dept === 'Other') {
                        $('#update_co_form #other_text_div').removeClass("display-hide");
                        $('#update_co_form #other_dept').val(jsonvalue.co_map.other_dept);
                        $('#update_co_form #user_employment').prop('disabled', 'true');
                    } else {
                        $('#update_co_form #other_text_div').addClass("display-hide");
                    }
                } else if (jsonvalue.co_map.user_employment === "Others" || jsonvalue.co_map.user_employment === "Psu" || jsonvalue.co_map.user_employment === "Const" || jsonvalue.co_map.user_employment === "Nkn" || jsonvalue.co_map.user_employment === "Project") {
                    $("#update_co_form #min_state_org_label").html("Organization Name");
                    $('#update_co_form #user_employment').prop('disabled', 'true');
                    $('#update_co_form #min').prop('disabled', 'true');
                    $('#update_co_form #dept_div').addClass("display-hide");
                    if (jsonvalue.co_map.min === 'Other') {
                        $('#update_co_form #other_text_div').removeClass("display-hide");
                        $('#update_co_form #other_dept').val(jsonvalue.co_map.other_dept);
                        $('#update_co_form #user_employment').prop('disabled', 'true');
                    } else {
                        $('#update_co_form #other_text_div').addClass("display-hide");
                    }
                }
                if (add_data === 'email') {
                    $('#update_co_form #domain_div').removeClass("display-hide");
                    $('#update_co_form #domain').val(jsonvalue.co_map.domain);
                } else {
                    $('#update_co_form #domain_div').addClass("display-hide");
                }
                $('#update_co_form #ca_email').val(jsonvalue.co_map.ca_email);
                $('#update_co_form #ca_name').val(jsonvalue.co_map.ca_name);
                $('#update_co_form #ca_mobile').val(jsonvalue.co_map.ca_mobile);
                if (add_data === 'vpn') {
                    $('#update_co_form #hod_email').val('vpnsupport@nic.in').attr('readonly', true);
                    $("#update_co_form #select_dept_mandate").addClass('display-hide');
                } else {
                    $("#update_co_form #select_dept_mandate").removeClass('display-hide');
                }
                $('#update_co_form #base_ip').val(jsonvalue.co_map.base_ip);
                $('#update_co_form #update_data_id').val(id);
                $("#update_reqs_modal").modal('show');

            }, error: function ()
            {

            }
        });
    });

    $('#update_data').click(function (e) {
        $('.loader').show();
        e.preventDefault();
        $('#update_reqs_modal').find('input,select').removeAttr('disabled');
        var data = JSON.stringify($('#update_co_form').serializeObject());
        $('#update_reqs_modal').prop('disabled', 'true');
        var add_data = $('#formname').val();
        var emp_id = $("#update_co_form #update_data_id").val();
        $.ajax({
            type: "POST",
            url: "update_coordinator",
            data: {data: data, add_data: add_data, emp_id: emp_id},
            datatype: JSON,
            success: function (data) {
                $('.loader').hide();
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var flag = true;
                if (jsonvalue.error.domain_error !== null && jsonvalue.error.domain_error !== "" && jsonvalue.error.domain_error !== undefined)
                {
                    $('#update_co_form #domain_error').html(jsonvalue.error.domain_error);
                    flag = false;
                } else {
                    $('#update_co_form #domain_error').html("");
                }
                if (jsonvalue.error.ca_email_error !== null && jsonvalue.error.ca_email_error !== "" && jsonvalue.error.ca_email_error !== undefined)
                {
                    $('#update_co_form #ca_email_error').html(jsonvalue.error.ca_email_error);
                    flag = false;
                } else {
                    $('#update_co_form #ca_email_error').html("");
                }
                if (jsonvalue.error.ca_name_error !== null && jsonvalue.error.ca_name_error !== "" && jsonvalue.error.ca_name_error !== undefined)
                {
                    $('#update_co_form #ca_name_error').html(jsonvalue.error.ca_name_error);
                    flag = false;
                } else {
                    $('#update_co_form #ca_name_error').html("");
                }
                if (jsonvalue.error.ca_mobile_error !== null && jsonvalue.error.ca_mobile_error !== "" && jsonvalue.error.ca_mobile_error !== undefined)
                {
                    $('#update_co_form #ca_mobile_error').html(jsonvalue.error.ca_mobile_error);
                    flag = false;
                } else {
                    $('#update_co_form #ca_mobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#update_co_form #hod_email_error').html(jsonvalue.error.hod_email_error);
                    flag = false;
                } else {
                    $('#update_co_form #hod_email_error').html("");
                }
                if (jsonvalue.error.base_ip_error !== null && jsonvalue.error.base_ip_error !== "" && jsonvalue.error.base_ip_error !== undefined)
                {
                    $('#update_co_form #base_ip_error').html(jsonvalue.error.base_ip_error);
                    flag = false;
                } else {
                    $('#update_co_form #base_ip_error').html("");
                }
                if (!flag) {

                } else {
                    kidd = add_data;
                    $('a[id="' + add_data + '"]').trigger('click');
                    $('#update_reqs_modal').modal('toggle');
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 18px;'>Co-ordinator successfully updated.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });
                }
            },
            error: function () {
                $('.loader').hide();
                console.log('error');
                return false;
            }
        });
    });

    $('#min_update').click(function (e) {
        e.preventDefault();
        $('.loader').show();
        var data = JSON.stringify($('#update_min_form').serializeObject());
        $.ajax({
            type: "POST",
            url: "update_ministry",
            data: {data: data},
            datatype: JSON,
            success: function (data) {
                $('.loader').hide();
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var flag = true;
                if (jsonvalue.error.minerror !== null && jsonvalue.error.minerror !== "" && jsonvalue.error.minerror !== undefined)
                {
                    $('#update_min_form #minerror').html(jsonvalue.error.minerror);
                    flag = false;
                } else {
                    $('#update_min_form #minerror').html("");
                }
                if (jsonvalue.error.other_dep_error !== null && jsonvalue.error.other_dep_error !== "" && jsonvalue.error.other_dep_error !== undefined)
                {
                    $('#update_min_form #other_dep_error').html(jsonvalue.error.other_dep_error);
                    flag = false;
                } else {
                    $('#update_min_form #other_dep_error').html("");
                }
                if (!flag) {

                } else {
                    $('#update_min_modal').modal('toggle');
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 18px;'>Ministry name successfully updated.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });
                }
            },
            error: function () {
                $('.loader').hide();
                console.log('error');
                return false;
            }
        });
    });

    $('#dept_update').click(function (e) {
        e.preventDefault();
        $('.loader').show();
        var data = JSON.stringify($('#update_dept_form').serializeObject());
        $.ajax({
            type: "POST",
            url: "update_dept",
            data: {data: data},
            datatype: JSON,
            success: function (data) {
                $('.loader').hide();
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var flag = true;
                if (jsonvalue.error.useremployment_error !== null && jsonvalue.error.useremployment_error !== "" && jsonvalue.error.useremployment_error !== undefined)
                {
                    $('#update_dept_form #useremployment_error').html(jsonvalue.error.useremployment_error);
                    flag = false;
                } else {
                    $('#update_dept_form #useremployment_error').html("");
                }
                if (jsonvalue.error.minerror !== null && jsonvalue.error.minerror !== "" && jsonvalue.error.minerror !== undefined)
                {
                    $('#update_dept_form #minerror').html(jsonvalue.error.minerror);
                    flag = false;
                } else {
                    $('#update_dept_form #minerror').html("");
                }
                if (jsonvalue.error.deperror !== null && jsonvalue.error.deperror !== "" && jsonvalue.error.deperror !== undefined)
                {
                    $('#update_dept_form #deperror').html(jsonvalue.error.deperror);
                    flag = false;
                } else {
                    $('#update_dept_form #deperror').html("");
                }
                if (jsonvalue.error.new_min_dept_error !== null && jsonvalue.error.new_min_dept_error !== "" && jsonvalue.error.new_min_dept_error !== undefined)
                {
                    $('#update_dept_form #new_min_dept_error').html(jsonvalue.error.other_dep_error);
                    flag = false;
                } else {
                    $('#update_dept_form #new_min_dept_error').html("");
                }
                if (!flag) {

                } else {
                    $('#update_dept_modal').modal('toggle');
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 18px;'>Department name successfully updated.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });
                }
            },
            error: function () {
                $('.loader').hide();
                console.log('error');
                return false;
            }
        });
    });

    $(document).on('click', '.deleteEmp', function (e) {
        e.preventDefault();
        var id = $(this).attr('id');
        var add_data = $('#formname').val();
        bootbox.confirm({
            message: "Do you want to delete this coordinator?",
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
                console.log('This was logged in the callback: ' + result); // true for yes .. false for no
                if (result === true) {
                    $.ajax({
                        type: "POST",
                        url: "updateCO_deleteCO",
                        data: {data: id, add_data: add_data},
                        datatype: JSON,
                        success: function (data)
                        {
                            kidd = add_data;
                            $('a[id="' + add_data + '"]').trigger('click');
                            bootbox.dialog({
                                message: "<p style='text-align: center; font-size: 18px;'>Co-ordinator deleted successfully. All pending requests moved to support.</p>",
                                title: "",
                                buttons: {
                                    cancel: {
                                        label: "OK",
                                        className: 'btn-info'
                                    }
                                }
                            });
                        }, error: function ()
                        {

                        }
                    });
                }
            }
        });
    });

});