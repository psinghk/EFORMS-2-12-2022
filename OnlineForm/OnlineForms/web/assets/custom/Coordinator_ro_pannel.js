//Begin::Show CooPannelRO list in data table by Gaurav
$(document).ready(function() {
    initializeCoPannelRoTable();
});

function initializeCoPannelRoTable(){
    $('.loader').show();
     $.ajax({
         type: 'GET',
         url: "viewAllRo",
         dataType: 'json', 
         success: function (data) {
            $('.loader').hide();
            var strRo = "";
            var RoViewAllData = JSON.parse(data.jsonStr);
            $.each(RoViewAllData, function (i, val) {
                $.each(val, function (i, value) {
                    var RoTabserialNo = i+1;
                    console.log(value)
                    strRo += "<tr><td class='text-center'>" + RoTabserialNo + "</td><td>" + value.ca_name + "</td><td>" + value.ca_email + "</td><td>" + value.ca_mobile + "</td><td class='text-center'><span class='btn-copannel-datatable btn-warning mr-1' onclick=\"editRoRecordByUserId('" + value.ca_id + "');\" data-toggle='modal' data-target='#edit' type='button'><i class='fa fa-edit'></i></span><span class='btn-copannel-datatable btn-danger btn-xs mr-1' onclick=\"deleteRoRecordByUserId('" + value.ca_id + "');\" ><i class='fa fa-trash'></i></span><span class='btn-copannel-datatable btn-info btn-xs' onclick=\"viewRoRecordByUserId('" + value.ca_id + "');\" ><i class='fa fa-eye'></i></span></td></tr>";
                });
            });
            $("#CooPannelRoDataBody").html("");
            $("#RoData").dataTable().fnDestroy();
            $("#CooPannelRoDataBody").html(strRo);
            $('#RoData').dataTable({
                "pageLength": 10,
                "searching" : true,
                "autoWidth":false,
                "columnDefs":[
                    {width:'10%', targets:0},
                    {width:'25%', targets:1},
                    {width:'25%', targets:2},
                    {width:'25%', targets:3},
                    {width:'15%', targets:4}
                ]
            });
         },
         error: function () {
        	 console.log("This is error!!");
         }
     });
}
//End::Show CooPannelRO list in data table by Gaurav

function deleteRoRecordByUserId(id){
    bootbox.confirm({
        title: "Delete RO",
        message: "Are you sure? Do you want to delete this RO?",
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
                $(".loader").show();
                $.ajax({
                    type: 'post',
                    url: 'deleteRoById',
                    data: {emp_id:id},
                    success: function(data) {
                    console.log(data);
                    var fnldata =  JSON.parse(data.jsonStr);
                    $.each(fnldata, function (i, val) {
                        if(val === 'deleted') {
                            initializeCoPannelRoTable();
                            console.log(data);
                            $(".msg-alert").addClass('alert alert-success');
                            $(".msg-alert").html("<b>RO Deleted</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-success');
                                $(".msg-alert").html("");
                            }, 15000);
                        }else {
                            $(".msg-alert").addClass('alert alert-danger');
                            $(".msg-alert").html("<b>"+val+"</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-danger');
                                $(".msg-alert").html("");
                            }, 15000);
                        }
                    });
                    }, 
                    complete: function() {
                        $(".loader").hide();
                    }
                })
            }
        }
    });
}

$("#add-ro-rcd").click(function() {
    $('.text-danger').html('');
    $("#RoAddFrm input").val('');
    $("#updRoBtnRec").hide();
    $("#addRoBtnRec").show();
    $("#modalName").html("Add RO");
    $("#addCooRecBtn").html("Add Record");
    $("#RoAddMod").modal({backdrop: 'static', keyboard: false});
});

function check_name() {
    var cname = $("#ca_name").val();
    if (cname.length < 1) {
        $(".ca_name").html("Please enter your name.");
        $(".ca_name").show();
        ca_name = true;
    } else {
        $(".ca_name").hide();
        ca_name = false;
    }
}

function check_email() {
    var pattern = new RegExp(/^\b[A-Z0-9._%-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b$/i);
    if (!pattern.test($("#ca_email").val())) {
        $(".ca_email").html("Please Enter valid Email.");
        $(".ca_email").show();
        ca_email = true;
    } else {
        $(".ca_email").hide();
        ca_email = false;
    }
}

function check_mobile() {
    var pattern = new RegExp(/^[0-9-+]+$/);
    if (!pattern.test($("#ca_mobile").val())) {
        $(".ca_mobile").html("Please Enter valid Contact Number. Do not Use (+91)");
        $(".ca_mobile").show();
        ca_mobile = true;
    } else {
        $(".ca_mobile").hide();
        ca_mobile = false;
    }
}


$('#addRoBtnRec').click(function (e) {
    $('.text-danger').html('');
    
    ca_name = false;
    ca_email = false;
    ca_mobile = false;
    
    check_name();
    check_email();
    check_mobile ();
    
    e.preventDefault();
    if (ca_name == false && ca_email == false && ca_mobile == false) {
            var data = JSON.stringify($('#RoAddFrm').serializeObject());
            console.log("Form data Add::::" + data);
            $.ajax({
                type: 'POST',
                url: "insertRoTable",
                data: {data: data},
                datatype: JSON,
                success: function (data) {
                    console.log(data);
                        var addfnlData = JSON.parse(data.jsonStr);
                        $.each(addfnlData, function (i, val) {
                            if(val === 'inserted'){
                                initializeCoPannelRoTable();
                                $(".msg-alert").addClass('alert alert-success');
                                $(".msg-alert").html("<b>RO Added Successfully.</b>");
                                $("#RoAddMod").modal('hide');
                                setTimeout(function(){ 
                                    $(".msg-alert").removeClass('alert alert-success');
                                    $(".msg-alert").html("");
                                }, 15000);
                            }
                        });
                },
                error: function () {
                    console.log("This is error Case Add !!");
                }

            });
    }else{
        return false;
    }           
});


function editRoRecordByUserId(id){
    $('.loader').show();
    $('#modalName').html('');
    $('#modalName').append('Update RO');
    $("#addRoBtnRec").hide();
    $("#updRoBtnRec").show();
    $('.text-danger').html('');
     $.ajax({
         type: 'GET',
         url: "viewRoId",
         data: {emp_id:id}, 
         success: function (data) {
             console.log(data);
            $('.loader').hide();
            var viewfnlData = JSON.parse(data.jsonStr);
            $.each(viewfnlData, function (i, val) {
                $('#ca_name').val(val.ca_name);
                $('#ca_email').val(val.ca_email);
                $('#ca_mobile').val(val.ca_mobile);
                $('#ca_id').val(val.ca_id);
                console.log(val);
            });
            $("#RoAddMod").modal({backdrop: 'static', keyboard: false});
         },
         error: function () {
        	 console.log("This is error!!");
         }
     });
}

//Begin::Update RO
$('#updRoBtnRec').click(function (e) {
    $('.text-danger').html('');
    
    ca_name = false;
    ca_email = false;
    ca_mobile = false;
    
    check_name();
    check_email();
    check_mobile ();
    
    e.preventDefault();
    if (ca_name == false && ca_email == false && ca_mobile == false) {
            var empId = $('#emp_id').val();
            var data = JSON.stringify($('#RoAddFrm').serializeObject());
            console.log("Form data Update Case::::" + data);
            $.ajax({
                type: 'POST',
                url: "updateRo",
                data: {data:data},
                datatype: JSON,
                success: function (data) {
                    var updfnlData = JSON.parse(data.jsonStr);
                    $.each(updfnlData, function (i, val) {
                        if(val === 'updated'){
                            initializeCoPannelRoTable();
                            $(".msg-alert").addClass('alert alert-success');
                            $(".msg-alert").html("<b>RO Updated Successfully.</b>");
                            $("#RoAddMod").modal('hide');
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-success');
                                $(".msg-alert").html("");
                            }, 15000);
                        }
                    });
                },
                error: function () {
                    console.log("This is error Case Update!!");
                }
            });
    }else{
        return false;
    }           
});
//End::Update RO

function viewRoRecordByUserId(id){
    $('.loader').show();
    $('.text-danger').html('');
     $.ajax({
         type: 'GET',
         url: "viewRoId",
         data: {emp_id:id}, 
         success: function (data) {
            $('.loader').hide();
            var viewfnlData = JSON.parse(data.jsonStr);
            var ViewData = '';
            $.each(viewfnlData, function (i, val) {
                    ViewData += '<tr><th>Name</th><td>'+ val.ca_name +'</td></tr><tr><th>Email</th><td>'+ val.ca_email +'</td></tr><tr><th>Mobile</th><td>'+ val.ca_mobile +'</td></tr>';
                $("#ViewCooPannelRoBody").html('');
                $("#ViewCooPannelRoBody").html(ViewData);
                console.log(val);
            });
            $("#ViewRORec").modal({backdrop: 'static', keyboard: false});
         },
         error: function () {
        	 console.log("This is error!!");
         }
     
     });
}