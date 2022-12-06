var showDepartment2 = function (e) {
    e.preventDefault();
    var inputCurrentCoordinatorEmail = $('#inputCurrentCoordinatorEmail').val();
    var inputPendingStatus = $('#inputPendingStatus').val();
    data = JSON.stringify({
        inputCurrentCoordinatorEmail: inputCurrentCoordinatorEmail,
        inputPendingStatus: inputPendingStatus
    });
    $.ajax({
            type: 'post',
            url: fetchEmploymentDetailsOfCoord,
            data: data,
            datatype: JSON,
            contentType: 'application/json; charset=utf-8',
            success: function (data) {
                var error_flag = true;
                var success_flag = false;
                alert(data);
                
                if (error_flag && !success_flag) {
                    if (data.listEmpCategoryMinistryDept != null && data.listEmpCategoryMinistryDept.length > 0) {
                        $("#showEmpCategoryMinDept").empty();
                        $("#showEmpCategoryMinDept").append("<thead><th>Select</th><th>Alias</th><th>Category</th><th>Ministry</th><th>Department</th></thead>");
                        for (i = 0; i < data.listEmpCategoryMinistryDept.length; i = i + 1) {
                            var json = JSON.stringify(data.listEmpCategoryMinistryDept[i]);
                            var json2 = JSON.stringify(radioEmpCatMinDept);
                            var checked = json === json2;
                            if (checked)
                                checked = 'checked=checked';
                            else
                                checked = '';
                            $("#showEmpCategoryMinDept").append("<tr><td><input type='radio' name='radioEmpCatMinDept' value='" + json + "' " + checked + "/></td><td>" + data.listEmpCategoryMinistryDept[i].alias + "</td><td>" + data.listEmpCategoryMinistryDept[i].emp_category + "</td><td>" + data.listEmpCategoryMinistryDept[i].emp_min_state_org + "</td><td>" + data.listEmpCategoryMinistryDept[i].emp_dept + "</td></tr>");
                        }
                        $('#showEmpCategoryMinDept').append("<div><input type='button' value='Show pending requests' onclick='showDepartment(event)' class='btn btn-outline btn-primary mt-4 ml-3 buttonShowDepartment'></div>");
                    } else {
                        $("#showEmpCategoryMinDept").empty();
                        $("#showEmpCategoryMinDept").append("<thead><th>No department found</th></thead>");
                    }
                    var list = data.pendingCoordinatorRequests;
                    var lePendingCoordinatorRequests = $("#lePendingCoordinatorRequests");
                    var leCoordinatorEmails = $('#leCoordinatorEmails');
                    if (list != null) {
                        if (list.length > 0) {
                            lePendingCoordinatorRequests.empty();
                            lePendingCoordinatorRequests.show();
                            lePendingCoordinatorRequests.append("<thead><th colspan='4'><strong>Showing for " + radioEmpCatMinDept.emp_min_state_org + " and " + radioEmpCatMinDept.emp_dept + "</strong></th></thead>");
                            lePendingCoordinatorRequests.append("<thead><th>Reg No</th><th>Form</th><th>Status</th><th>To email</th></thead>");
                            for (i = 0; i < list.length; i = i + 1) {
                                lePendingCoordinatorRequests.append("<tr><td>" + list[i].registrationNumber + "</td><td>" + list[i].formName + "</td><td>" + list[i].status + "</td><td>" + list[i].toEmail + "</td></tr>");
                            }

                            leCoordinatorEmails.empty();
                            leCoordinatorEmails.show();
                            leCoordinatorEmails.append("<thead><th>Select</th><th>Coordinator/DA Email</th></thead>");
                            var coordinatorEmailsFromCoordinatorEmail = data.coordinatorEmailsFromCoordinatorEmail;
                            for (i = 0; i < coordinatorEmailsFromCoordinatorEmail.length; i = i + 1) {
                                leCoordinatorEmails.append("<tr><td><input type='radio' name='inputNewCoordinatorEmail' value='" + coordinatorEmailsFromCoordinatorEmail[i] + "'/></td><td>" + coordinatorEmailsFromCoordinatorEmail[i] + "</td></tr>");
                            }
                            leCoordinatorEmails.append("<div><input type='button' value='Move requests' onclick='showDepartment(event)' class='btn btn-outline btn-primary mt-4 ml-3 buttonShowDepartment'></div>");
                        } else {
                            lePendingCoordinatorRequests.empty();
                            lePendingCoordinatorRequests.show();
                            lePendingCoordinatorRequests.append("<thead><th>No requests found for " + radioEmpCatMinDept.emp_min_state_org + " and " + radioEmpCatMinDept.emp_dept + "</th></thead>");

                            leCoordinatorEmails.empty();
                            leCoordinatorEmails.show();
                            leCoordinatorEmails.append("<thead><th>No other coordinator/DA found for " + radioEmpCatMinDept.emp_min_state_org + " and " + radioEmpCatMinDept.emp_dept + "</th></thead>");
                        }
                    } else {
                        $("#lePendingCoordinatorRequests").hide();
                        $('#leCoordinatorEmails').hide();
                    }
                } else {
                }
            },
            error: function () {
                console.log("This is error Case  !!");
            }
        });
    return false;
}

var showDepartment = function (e) {
    e.preventDefault();
    var inputCurrentCoordinatorEmail = $('#inputCurrentCoordinatorEmail').val();
    var inputPendingStatus = $('#inputPendingStatus').val();
    
    if ($('[name="radioEmpCatMinDept"]:checked').val() != undefined) {
        var radioEmpCatMinDept = JSON.parse($('[name="radioEmpCatMinDept"]:checked').val());
    }
//    alert($('[name="inputNewCoordinatorEmail"]:checked').val());
    var move_flag = false;
    if ($('[name="inputNewCoordinatorEmail"]:checked').val() != undefined) {
        var inputNewCoordinatorEmail = $('[name="inputNewCoordinatorEmail"]:checked').val();
        move_flag = true;
    }
    if(!window.ldata)
    window.ldata = {};
    data = JSON.stringify({
        radioEmpCatMinDept: radioEmpCatMinDept,
        inputCurrentCoordinatorEmail: inputCurrentCoordinatorEmail,
        inputPendingStatus: inputPendingStatus,
        inputNewCoordinatorEmail: inputNewCoordinatorEmail,
        pendingCoordinatorRequests: window.ldata.pendingCoordinatorRequests,
    });
    if (!move_flag || move_flag && confirm('Do you Want to Move All Request?')) {
        if (move_flag)
            url = 'moveRequestsToSelectedCoord'; // prabhat  21-june-2022
        else if(radioEmpCatMinDept!==undefined) 
            url = 'fetchRequestsOfCoordAndShowValidCoords';
        else
            url = 'fetchEmploymentDetailsOfCoord';
        $.ajax({
            type: 'post',
            url: url,
            data: data,
            datatype: JSON,
            contentType: 'application/json; charset=utf-8',
            success: function (data) {
                window.ldata = data;
                var error_flag = true;
                var success_flag = false;
                if (data.success != undefined)
                    success_flag = data.success.success === "Requests moved succesFully" ? true : false;
                if (data.error != null) {
                    if (data.error.currentCoordinatorEmail !== null && data.error.currentCoordinatorEmail !== "" && data.error.currentCoordinatorEmail !== undefined) {
                        error_flag = false;
                        $('#errorCurrentCoordinatorEmail').html(data.error.currentCoordinatorEmail)
                    } else {
                        $('#errorCurrentCoordinatorEmail').html("")
                    }
                    if (data.error.pendingStatus !== null && data.error.pendingStatus !== "" && data.error.pendingStatus !== undefined) {
                        error_flag = false;
                        $('#errorPendingStatus').html(data.error.pendingStatus)
                    } else {
                        $('#errorPendingStatus').html("")
                    }
                }
                if (success_flag) {
                    alert("Requests Moved successfully");
                    $("#showEmpCategoryMinDept").empty();
                    $("#lePendingCoordinatorRequests").empty();
                    $("#leCoordinatorEmails").empty();
                    $('#inputCurrentCoordinatorEmail').val("");
                    $("#inputPendingStatus").val("");
                }
                if (error_flag && !success_flag) {
                    if(data.pendingCoordinatorRequests==undefined){
                    if (data.listEmpCategoryMinistryDept != null && data.listEmpCategoryMinistryDept.length > 0) {
                        $("#showEmpCategoryMinDept").empty();
                        $("#showEmpCategoryMinDept").append("<thead><th>Select</th><th>Alias</th><th>Category</th><th>Ministry</th><th>Department</th></thead>");
                        for (i = 0; i < data.listEmpCategoryMinistryDept.length; i = i + 1) {
                            var json = JSON.stringify(data.listEmpCategoryMinistryDept[i]);
                            var json2 = JSON.stringify(radioEmpCatMinDept);
                            var checked = json === json2;
                            if (checked)
                                checked = 'checked=checked';
                            else
                                checked = '';
                            $("#showEmpCategoryMinDept").append("<tr><td><input type='radio' name='radioEmpCatMinDept' value='" + json + "' " + checked + "/></td><td>" + data.listEmpCategoryMinistryDept[i].alias + "</td><td>" + data.listEmpCategoryMinistryDept[i].emp_category + "</td><td>" + data.listEmpCategoryMinistryDept[i].emp_min_state_org + "</td><td>" + data.listEmpCategoryMinistryDept[i].emp_dept + "</td></tr>");
                        }
                        $('#showEmpCategoryMinDept').append("<div><input type='button' value='Show pending requests' onclick='showDepartment(event)' class='btn btn-outline btn-primary mt-4 ml-3 buttonShowDepartment'></div>");
                    } else {
                        $("#showEmpCategoryMinDept").empty();
                        $("#showEmpCategoryMinDept").append("<thead><th>No department found</th></thead>");
                    }
                }
                    var list = data.pendingCoordinatorRequests;
                    var lePendingCoordinatorRequests = $("#lePendingCoordinatorRequests");
                    var leCoordinatorEmails = $('#leCoordinatorEmails');
                    if (list != null) {
                        if (list.length > 0) {
                            lePendingCoordinatorRequests.empty();
                            lePendingCoordinatorRequests.show();
                            lePendingCoordinatorRequests.append("<thead><th colspan='4'><strong>Showing for " + radioEmpCatMinDept.emp_min_state_org + " and " + radioEmpCatMinDept.emp_dept + "</strong></th></thead>");
                            lePendingCoordinatorRequests.append("<thead><th>Reg No</th><th>Form</th><th>Status</th><th>To email</th></thead>");
                            for (i = 0; i < list.length; i = i + 1) {
                                lePendingCoordinatorRequests.append("<tr><td>" + list[i].registrationNumber + "</td><td>" + list[i].formName + "</td><td>" + list[i].status + "</td><td>" + list[i].toEmail + "</td></tr>");
                            }

                            leCoordinatorEmails.empty();
                            leCoordinatorEmails.show();
                            leCoordinatorEmails.append("<thead><th>Select</th><th>Coordinator/DA Email</th></thead>");
                            var coordinatorEmailsFromCoordinatorEmail = data.coordinatorEmailsFromCoordinatorEmail;
                            for (i = 0; i < coordinatorEmailsFromCoordinatorEmail.length; i = i + 1) {
                                leCoordinatorEmails.append("<tr><td><input type='radio' name='inputNewCoordinatorEmail' value='" + coordinatorEmailsFromCoordinatorEmail[i] + "'/></td><td>" + coordinatorEmailsFromCoordinatorEmail[i] + "</td></tr>");
                            }
                            leCoordinatorEmails.append("<div><input type='button' value='Move requests' onclick='showDepartment(event)' class='btn btn-outline btn-primary mt-4 ml-3 buttonShowDepartment'></div>");
                        } else {
                            lePendingCoordinatorRequests.empty();
                            lePendingCoordinatorRequests.show();
                            lePendingCoordinatorRequests.append("<thead><th>No requests found for " + radioEmpCatMinDept.emp_min_state_org + " and " + radioEmpCatMinDept.emp_dept + "</th></thead>");

                            leCoordinatorEmails.empty();
                            leCoordinatorEmails.show();
                            leCoordinatorEmails.append("<thead><th>No other coordinator/DA found for " + radioEmpCatMinDept.emp_min_state_org + " and " + radioEmpCatMinDept.emp_dept + "</th></thead>");
                        }
                    } else {
                        $("#lePendingCoordinatorRequests").hide();
                        $('#leCoordinatorEmails').hide();
                    }
                } else {
                }
            },
            error: function () {
                console.log("This is error Case  !!");
            }
        });
    }
    return false;
}

var showDepartment1 = function (e) {
    e.preventDefault();
    var inputCurrentCoordinatorEmail = $('#inputCurrentCoordinatorEmail').val();
    var inputPendingStatus = $('#inputPendingStatus').val();
    
    
    
    
    
    if ($('[name="radioEmpCatMinDept"]:checked').val() != undefined) {
        var radioEmpCatMinDept = JSON.parse($('[name="radioEmpCatMinDept"]:checked').val());
    }
//    alert($('[name="inputNewCoordinatorEmail"]:checked').val());
    var move_flag = false;
    if ($('[name="inputNewCoordinatorEmail"]:checked').val() != undefined) {
        var inputNewCoordinatorEmail = $('[name="inputNewCoordinatorEmail"]:checked').val();
        move_flag = true;
    }
    data = JSON.stringify({
        radioEmpCatMinDept: radioEmpCatMinDept,
        inputCurrentCoordinatorEmail: inputCurrentCoordinatorEmail,
        inputPendingStatus: inputPendingStatus,
        inputNewCoordinatorEmail: inputNewCoordinatorEmail
    });
    if (!move_flag || move_flag && confirm('Do you Want to Move All Request?')) {
        if (move_flag)
            url = 'realMoveRequest'; // prabhat  21-june-2022
        else
            url = 'moveRequestShowCatMinDeptAndRequests';
        $.ajax({
            type: 'post',
            url: url,
            data: data,
            datatype: JSON,
            contentType: 'application/json; charset=utf-8',
            success: function (data) {
                var error_flag = true;
                var success_flag = false;
                if (data.success != undefined)
                    success_flag = data.success.success === "Requests moved succesFully" ? true : false;
                if (data.error != null) {
                    if (data.error.currentCoordinatorEmail !== null && data.error.currentCoordinatorEmail !== "" && data.error.currentCoordinatorEmail !== undefined) {
                        error_flag = false;
                        $('#errorCurrentCoordinatorEmail').html(data.error.currentCoordinatorEmail)
                    } else {
                        $('#errorCurrentCoordinatorEmail').html("")
                    }
                    if (data.error.pendingStatus !== null && data.error.pendingStatus !== "" && data.error.pendingStatus !== undefined) {
                        error_flag = false;
                        $('#errorPendingStatus').html(data.error.pendingStatus)
                    } else {
                        $('#errorPendingStatus').html("")
                    }
                }
                if (success_flag) {
                    alert("Requests Moved successfully");
                    $("#showEmpCategoryMinDept").empty();
                    $("#lePendingCoordinatorRequests").empty();
                    $("#leCoordinatorEmails").empty();
                    $('#inputCurrentCoordinatorEmail').val("");
                    $("#inputPendingStatus").val("");
                }
                if (error_flag && !success_flag) {
                    if (data.listEmpCategoryMinistryDept != null && data.listEmpCategoryMinistryDept.length > 0) {
                        $("#showEmpCategoryMinDept").empty();
                        $("#showEmpCategoryMinDept").append("<thead><th>Select</th><th>Alias</th><th>Category</th><th>Ministry</th><th>Department</th></thead>");
                        for (i = 0; i < data.listEmpCategoryMinistryDept.length; i = i + 1) {
                            var json = JSON.stringify(data.listEmpCategoryMinistryDept[i]);
                            var json2 = JSON.stringify(radioEmpCatMinDept);
                            var checked = json === json2;
                            if (checked)
                                checked = 'checked=checked';
                            else
                                checked = '';
                            $("#showEmpCategoryMinDept").append("<tr><td><input type='radio' name='radioEmpCatMinDept' value='" + json + "' " + checked + "/></td><td>" + data.listEmpCategoryMinistryDept[i].alias + "</td><td>" + data.listEmpCategoryMinistryDept[i].emp_category + "</td><td>" + data.listEmpCategoryMinistryDept[i].emp_min_state_org + "</td><td>" + data.listEmpCategoryMinistryDept[i].emp_dept + "</td></tr>");
                        }
                        $('#showEmpCategoryMinDept').append("<div><input type='button' value='Show pending requests' onclick='showDepartment(event)' class='btn btn-outline btn-primary mt-4 ml-3 buttonShowDepartment'></div>");
                    } else {
                        $("#showEmpCategoryMinDept").empty();
                        $("#showEmpCategoryMinDept").append("<thead><th>No department found</th></thead>");
                    }
                    var list = data.pendingCoordinatorRequests;
                    var lePendingCoordinatorRequests = $("#lePendingCoordinatorRequests");
                    var leCoordinatorEmails = $('#leCoordinatorEmails');
                    if (list != null) {
                        if (list.length > 0) {
                            lePendingCoordinatorRequests.empty();
                            lePendingCoordinatorRequests.show();
                            lePendingCoordinatorRequests.append("<thead><th colspan='4'><strong>Showing for " + radioEmpCatMinDept.emp_min_state_org + " and " + radioEmpCatMinDept.emp_dept + "</strong></th></thead>");
                            lePendingCoordinatorRequests.append("<thead><th>Reg No</th><th>Form</th><th>Status</th><th>To email</th></thead>");
                            for (i = 0; i < list.length; i = i + 1) {
                                lePendingCoordinatorRequests.append("<tr><td>" + list[i].registrationNumber + "</td><td>" + list[i].formName + "</td><td>" + list[i].status + "</td><td>" + list[i].toEmail + "</td></tr>");
                            }

                            leCoordinatorEmails.empty();
                            leCoordinatorEmails.show();
                            leCoordinatorEmails.append("<thead><th>Select</th><th>Coordinator/DA Email</th></thead>");
                            var coordinatorEmailsFromCoordinatorEmail = data.coordinatorEmailsFromCoordinatorEmail;
                            for (i = 0; i < coordinatorEmailsFromCoordinatorEmail.length; i = i + 1) {
                                leCoordinatorEmails.append("<tr><td><input type='radio' name='inputNewCoordinatorEmail' value='" + coordinatorEmailsFromCoordinatorEmail[i] + "'/></td><td>" + coordinatorEmailsFromCoordinatorEmail[i] + "</td></tr>");
                            }
                            leCoordinatorEmails.append("<div><input type='button' value='Move requests' onclick='showDepartment(event)' class='btn btn-outline btn-primary mt-4 ml-3 buttonShowDepartment'></div>");
                        } else {
                            lePendingCoordinatorRequests.empty();
                            lePendingCoordinatorRequests.show();
                            lePendingCoordinatorRequests.append("<thead><th>No requests found for " + radioEmpCatMinDept.emp_min_state_org + " and " + radioEmpCatMinDept.emp_dept + "</th></thead>");

                            leCoordinatorEmails.empty();
                            leCoordinatorEmails.show();
                            leCoordinatorEmails.append("<thead><th>No other coordinator/DA found for " + radioEmpCatMinDept.emp_min_state_org + " and " + radioEmpCatMinDept.emp_dept + "</th></thead>");
                        }
                    } else {
                        $("#lePendingCoordinatorRequests").hide();
                        $('#leCoordinatorEmails').hide();
                    }
                } else {
                }
            },
            error: function () {
                console.log("This is error Case  !!");
            }
        });
    }
    return false;
}

$(".buttonShowDepartment").click(showDepartment);

//                if (data.rolls.length > 0) {
//                    var role_data = "";
////                    $("#roles").html('');
////                    $("#roles").html('<option value="">-Select Roles-</option>');
////                    $.each(data.rolls, function (index, value) {
////                        console.log(value);
////                        role_data += '<option value="' + value + '">' + value + '</option>';
////                    });
//                    $("#roles").append(role_data);
//                    $("#fetch_tab_submit").hide();
//                    $("#move_tab_submit").show();
//                    $("#cemailSection").show();
//                    $("#rolesSection").show();
//                } else {
//                    $("#fetch_tab_submit").show();
//                    $("#move_tab_submit").hide();
//                    $("#cemailSection").hide();
//                    $("#rolesSection").hide();
//                    console.log("please enter fetch request email!");
//                    $('#msg').addClass('alert alert-danger').text("No role found !!");
//                    setTimeout(function () {
//                        $("#msg").removeClass('alert alert-danger');
//                        $("#msg").html('');
//                    }, 2000);
//                }


$('#move_tab_submit').click(function (e) {
    e.preventDefault();
    //alert("here")
    var pending_user_email = $("#pending_user_email").val();
    var roles = $("#roles").val();
    var to_email = $("#to_email").val();

    $.ajax({
        type: 'post',
        url: 'changeOfficer',
        data: {pending_user_email: pending_user_email, to_email: to_email, roles: roles},
        datatype: 'html',
        success: function (data) {

            var error_flag = true;
            if (data.error.pending_email_error !== null && data.error.pending_email_error !== "" && data.error.pending_email_error !== undefined)
            {
                error_flag = false;
                $('#emailerror').html(data.error.pending_email_error)

            } else {
                $('#emailerror').html("")

            }
            if (data.error.email_error !== null && data.error.email_error !== "" && data.error.email_error !== undefined)
            {
                error_flag = false;
                $('#toemailerror').html(data.error.email_error)

            } else {
                $('#toemailerror').html("")

            }
            if (data.error.role_error !== null && data.error.role_error !== "" && data.error.role_error !== undefined)
            {
                error_flag = false;
                $('#roleerror').html(data.error.role_error)

            } else {
                $('#roleerror').html("")

            }

            if (error_flag)
            {

                $('#msg').addClass('alert alert-danger').text("Request Moved Successfully");
                setTimeout(function () {
                    $("#msg").removeClass('alert alert-danger');
                    $("#msg").html('');
                }, 3000);
                $("#rolesSection").hide();
                $("#cemailSection").hide();
                $("#move_tab_submit").hide();
                $("#fetch_tab_submit").show();
                $('#femail').val('');
                $('#showPending').empty();
            } else {
                $("#rolesSection").show();
                $("#cemailSection").show();
                $("#move_tab_submit").show();
                $("#fetch_tab_submit").hide();

            }
        },
        error: function () {
            console.log("This is error Case Add !!");
            alert("Please Select Correct Email-ID");
        }

    });
    //return false;

});


$(".moveRequest #roles").on("change", function () {
    var pending_user_email = $("#pending_user_email").val();
    var roles = $("#roles").val();
    var to_email = $("#to_email").val();
    if (roles.includes("pending")) {
        $.ajax({
            type: 'post',
            url: 'getRoll2',
            data: {pending_user_email: pending_user_email, roles: roles, to_email: to_email},
            datatype: 'html',
            success: function (data) {

                var error_flag = true;
                if (data.error.pending_email_error !== null && data.error.pending_email_error !== "" && data.error.pending_email_error !== undefined)
                {
                    error_flag = false;
                    $('#emailerror').html(data.error.pending_email_error)

                } else {
                    $('#emailerror').html("")

                }

                if (data.error.role_error !== null && data.error.role_error !== "" && data.error.role_error !== undefined)
                {
                    error_flag = false;
                    $('#roleerror').html(data.error.role_error)

                } else {
                    $('#roleerror').html("")

                }

                if (error_flag)
                {
                    if (data.use_data != null) {
                        $("#showPending").empty();
                        $("#showPending").append("<thead><th>Reg No</th><th>Form</th><th>Status</th><th>To email</th></thead>");
                        for (i = 0; i < data.use_data.length; i = i + 1) {
                            $("#showPending").append("<tr><td>" + data.use_data[i].registrationNumber + "</td><td>" + data.use_data[i].formName + "</td><td>" + data.use_data[i].status + "</td><td>" + data.use_data[i].toEmail + "</td></tr>");
                        }
                    } else {
                        $("#showPending").empty();
                        $("#showPending").append("<thead><th>Not found</th></thead>");
                    }

                } else {

                }
            },
            error: function () {
                console.log("This is error Case Add !!");
                alert("Please Select Correct option");
            }

        });
    }
})
