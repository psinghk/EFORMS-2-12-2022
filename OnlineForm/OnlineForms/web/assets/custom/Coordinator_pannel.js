//--------------------------------------Section 1 Begin::Coordinator Add,Delete,Edit,Update------------------------------------------

$(document).ready(function() {
    initializeCoordinatorTable();
    getMinistryInfoOfHOG();
});
$("#emp_category_hog").change(function () {
    if (this.value === 'State') {   
        $("#central_div_hog").hide();
        $("#other_div_hog").hide();
        $("#state_div_hog").show();
    }
    else if(this.value === 'Psu'){
        $("#central_div_hog").hide();
        $("#state_div_hog").hide();
        $("#other_div_hog").show();
    }  
    else if(this.value === 'Const'){
        $("#central_div_hog").hide();
        $("#state_div_hog").hide();
        $("#other_div_hog").show();
    }  
    else if(this.value === 'Nkn'){
        $("#central_div_hog").hide();
        $("#state_div_hog").hide();
        $("#other_div_hog").show();
    }
    else if(this.value === 'Project'){
        $("#central_div_hog").hide();
        $("#state_div_hog").hide();
        $("#other_div_hog").show();
    }
    else if(this.value === 'UT'){
        $("#state_div_hog").hide();
        $("#other_div_hog").hide();
        $("#central_div_hog").show();
    }
    else if(this.value === 'Others'){
        $("#central_div_hog").hide();
        $("#state_div_hog").hide();
        $("#other_div_hog").show();
    }
    else {
        $("#state_div_hog").hide();
        $("#other_div_hog").hide();
        $("#central_div_hog").show();
    }
});


$("#emp_category_hog").change(function () {
    //alert("changed");
    var orgTypeVal = $(this).val();
    if (orgTypeVal === 'State') {
        $.ajax({
            type: 'post',
            url: 'getAllCategoryMinistry',
            data: {emp_category: orgTypeVal},
            success: function (data) {
               
                console.log(data);
                var ChangeDeptMinistryRes = JSON.parse(data.jsonStr_new_ministries);
                var ministry_data = "";
                $("#stateCode_hog").html('');
                $("#stateCode_hog").html('<option value="">-Select-</option>');
                $.each(ChangeDeptMinistryRes, function (i, val) {
                    $.each(val, function (i, value) {
                        ministry_data += '<option value="' + value + '">' + value + '</option>';
                    });
                });
                
                $("#stateCode_hog").append(ministry_data);
            }
        });
    }
    else if(orgTypeVal === 'UT' || orgTypeVal === 'Central') {
        $.ajax({
            type: 'post',
            url: 'getAllCategoryMinistry',
            data: {emp_category: orgTypeVal},
            success: function (data) {
                 console.log(data);
                var ChangeDeptMinistryRes = JSON.parse(data.jsonStr_new_ministries);
                var ministry_data = "";
                $("#emp_min_state_org_hog").html('');
               $("#emp_min_state_org_hog").html('<option value="">-Select-</option>');
//                $.each(ChangeDeptMinistryRes, function (i, val) {
//                    $.each(val, function (i, value) {
//                        ministry_data += '<option value="' + value + '">' + value + '</option>';
//                    });
//                });
                
                
                      $.each(ChangeDeptMinistryRes,function(i,val){
                  $.each(val, function (i, value) {
                      console.log(value);
                   $('#emp_min_state_org_hog').append('<option value="'+value+'">'+value+'</option>').val(value);
                   
                      
                  });
              });
             
                //$("#emp_min_state_org_hog").append(ministry_data);
            }
        });
    }
    else  {
        $.ajax({
            type: 'post',
            url: 'getAllCategoryMinistry',
            data: {emp_category: orgTypeVal},
            success: function (data) {
                 console.log(data);
                var ChangeDeptMinistryRes = JSON.parse(data.jsonStr_new_ministries);
                var ministry_data = "";
                $("#org_hog").html('');
               $("#org_hog").html('<option value="">-Select-</option>');
//                $.each(ChangeDeptMinistryRes, function (i, val) {
//                    $.each(val, function (i, value) {
//                        ministry_data += '<option value="' + value + '">' + value + '</option>';
//                    });
//                });
                
                
                      $.each(ChangeDeptMinistryRes,function(i,val){
                  $.each(val, function (i, value) {
                      console.log(value);
                   $('#org_hog').append('<option value="'+value+'">'+value+'</option>').val(value);
                   
                      
                  });
              });
             
                //$("#emp_min_state_org_hog").append(ministry_data);
            }
        });
    }
});
$('#add_hog_self').click(function (e) {
    alert("register clicked");
    $('.text-danger').html('');
    e.preventDefault();
    var data = JSON.stringify($('#addHogSelf').serializeObject());
    $.ajax({
        type: 'POST',
        url: "registerHOG",
        data: {data: data},
        datatype: JSON,
        success: function (data) {
           //alert("hello");
        
            var addfnlData = JSON.parse(data.jsonStr);
            
           
             if(addfnlData.emp_category_hog === 'invalid'){
                 
                $(".hog_cat_error").html("<b>Please Select Category.</b>");
                setTimeout(function(){ 
                    $(".hog_cat_error").html("");
                }, 7000);                
            }
            if(addfnlData.emp_min_state_org_hog === 'invalid'){
                 alert("invalid ministry");
                $(".hog_min_error").html("<b>Please Select Ministry/Organization.</b>");
                setTimeout(function(){ 
                    $(".hog_min_error").html("");
                }, 5000);                
            }
            
             if(addfnlData.state_code_hog === 'invalid'){
                 alert("invalid stateCode_hog");
                $(".hog_stateCode").html("<b>Please Select Ministry/Organization.</b>");
                setTimeout(function(){ 
                    $(".hog_stateCode").html("");
                }, 7000);                
            }
            
             if(addfnlData.org_hog === 'invalid'){
                  alert("invalid org_hog");
                $(".hog_org").html("<b>Please Select Organization.</b>");
                setTimeout(function(){ 
                    $(".hog_org").html("");
                }, 7000);                
            }
           // var addfnlDataStatus = addfnlData.status;   
           // alert(addfnlData.status);
            
//            if(addfnlData.status === 'success'){
//                $(".emp_category").html("<b>Please Select Category.</b>");
//                setTimeout(function(){ 
//                    $(".emp_category").html("");
//                }, 2000);                
//            }
            
            //Add Coordinator (Case A::Central)
          
            if(addfnlData.status === 'success'){
              //alert("success");
                        //initializeCoordinatorTable();
                        $(".msg-alert").addClass('alert alert-success');
                        $(".msg-alert").html("<b>Hog Added Successfully.</b>");
                        //$("#cooAddMod").modal('hide');
                        setTimeout(function(){ 
                            $(".msg-alert").removeClass('alert alert-success');
                            $(".msg-alert").html("");
                        }, 7000);
                    
         
                
                
//                $.each(addfnlData, function (i, val) {
//                    if(val === 'inserted'){
//                        initializeCoordinatorTable();
//                        $(".msg-alert").addClass('alert alert-success');
//                        $(".msg-alert").html("<b>Coordinator Added Successfully.</b>");
//                        //$("#cooAddMod").modal('hide');
//                        setTimeout(function(){ 
//                            $(".msg-alert").removeClass('alert alert-success');
//                            $(".msg-alert").html("");
//                        }, 1500);
//                    }
//                });                
            }
            else{
                console.log("This is error Case Add !!")
            }               
        },
        error: function () {
            console.log("Something Went Wrong !!");
        }
    });          
});
//Begin::fetch Coordinator list in data table
function initializeCoordinatorTable(){
    $('.loader').show();
     $.ajax({
         type: 'GET',
         url: "viewAllCoordinators",
         dataType: 'json', 
         success: function (data) {
             console.log("data===="+data.jsonStr_new_categories);
             var dataItemCategories=JSON.parse(data.jsonStr_new_categories);
              $.each(dataItemCategories,function(i,val){
                  $.each(val, function (i, value) {
                      console.log(value);
                   $('#emp_category_hog').append('<option value="'+value+'">'+value+'</option>').val(value);
                   
                      
                  });
              });
             
             $("#emp_category_hog").prop("selectedIndex", 0).val(); 
             
             $('.loader').hide();
            var testpending = "";
            var testcompleted = "";
            var testrejected = "";
            var testforwarded = "";
            var str = "";
            var itemData = JSON.parse(data.jsonStr);
            $.each(itemData, function (i, val) {
                $.each(val, function (i, value) {
                    var serialNo = i+1;

                    testpending = value.total_count_pending;
                    testcompleted = value.total_count_completed;
                    testrejected = value.total_count_rejected;
                    testforwarded = value.total_count_forwarded;
                    
                    if(value.pending_request_count === 0){
                        pendingViewLink = '';
                    }else{
                        pendingViewLink = '<a href="javascript:void(0)" class="ml-5" onclick=\"viewPendingReqByUserId(' + value.emp_id + ');\"> View</a>';
                    }

                    if(value.completed_request_count === 0){
                        CompletedViewLink = '';
                    }else{
                        CompletedViewLink = '<a href="javascript:void(0)" class="ml-5" onclick=\"viewCompletedReqByUserId(' + value.emp_id + ');\"> View</a>';
                    }
                    str += "<tr><td class='text-center'>" + serialNo + "</td><td>" + checkUndefined(value.emp_dept) + "</td><td>" + value.emp_coord_email + "</a></td><td>" + checkUndefined(value.emp_coord_mobile) + "</td><td>" + value.completed_request_count + CompletedViewLink +"</td><td>" + value.pending_request_count + pendingViewLink +"</td><td>" + value.forwarded_request_count + "</td><td>" + value.rejected_request_count + "</td><td><div class='btn-group'><button class='btn btn-primary btn-xs green dropdown-toggle' type='button' data-toggle='dropdown' aria-expanded='true' title='Click to take appropriate actions'>Actions<i class='fa fa-angle-down'></i></button><ul class='dropdown-menu dropdown-menu-right action-btn-list' role='menu' style='position:inherit;'><li title='Edit the coordinator details'><a href='javascript:void(0)' onclick=\"editCoordinatorRecordByUserId('" + value.emp_id + "');\"><i class='fa fa-edit'></i> Edit</a></li><li title='Preview the coordinator details'><a href='javascript:void(0)' onclick=\"viewCoordinatorRecordByUserId('" + value.emp_id + "');\"><i class='fa fa-eye'></i> Preview</a></li><li title='Delete the coordinator'><a href='javascript:void(0)' onclick=\"deleteCoordinatorRecordByUserId('" + value.emp_id + "');\"><i class='fa fa-trash'></i> Delete</a></li></ul></div></td></tr>";
                });
            });

//            $('#t_pending').attr("data-value",testpending);
//            $('#t_completed').attr("data-value",testcompleted);
//            $('#t_rejected').attr("data-value",testrejected);
//            $('#t_forwarded').attr("data-value",testforwarded);          
            
             $('#t_pending').html(testpending);
            $('#t_completed').html(testcompleted);
            $('#t_rejected').html(testrejected);
            $('#t_forwarded').html(testforwarded);
            
            $("#CoordinatorDataBody").html("");
            $("#CoordinatorData").dataTable().fnDestroy();
            $("#CoordinatorDataBody").html(str);
            $('#CoordinatorData').dataTable({
                "pageLength": 10,
                "searching" : true,
                "autoWidth":false,
                    "columnDefs": [
                                { responsivePriority: 1, targets: 0 },
                                { responsivePriority: 2, targets: 7 }
                            ],
                fnDrawCallback : function() {
                   $('[data-toggle="popover"]').popover(); 
                }
            });            
            $('.box').mouseover(function(){
                $(this).children('.boxover').stop().animate({opacity:1},300);
            });
            $('.box').mouseleave(function(){
                $(this).children('.boxover').stop().animate({opacity:0},500);
            });
         },
         error: function () {
        	 console.log("Something Went Wrong!!");
         }
     });
}
//End::fetch Coordinator list in data table

//Begin :: Show Ministry on top of Coordinator List
function getMinistryInfoOfHOG(){
    $.ajax({
        type: 'GET',
        url: 'getMinistryInfoOfHOG',
        dataType: 'json',
        success: function (data) {
            var gettMinistryRes = JSON.parse(data.jsonStr);
            var length = (gettMinistryRes['min']).length;
            var getHOGMinistrydata = "";
            var beforehoverfnlDataGetMinHog = "";
            $("#getMinistryInfoHog").html('');
            $.each(gettMinistryRes, function (i, val) {
                beforehoverfnlDataGetMinHog += val[0].emp_min_state_org + '<a href="javascript:void(0)" class="box"> ....</a>';
                $.each(val, function (i, value) {
                    if ( value !== '' ) {
                        if(i === length-1) {
                            getHOGMinistrydata += value.emp_min_state_org;
                        } else {
                            getHOGMinistrydata += value.emp_min_state_org+', ';
                        } 
                    }                              
                });
            });
            $("#getMinistryInfoHog").append('('+beforehoverfnlDataGetMinHog+')');
            $("#boxover").append(getHOGMinistrydata);
                    $('.box').mouseover(function(){
                        $('.boxover').stop().animate({opacity:1},300);
                    });
                    $('.box').mouseleave(function(){
                        $('.boxover').stop().animate({opacity:0},500);
                    });
        },
        error: function () {
            console.log("Something Went Wrong!!");
        }
    });
}
//End :: Show Ministry on top of Coordinator List

//Begin::check for undefined values
function checkUndefined(val){
	if(typeof(val) === "undefined"){
		var filterValue = '';
	}else{
		var filterValue = val;
	}
	return filterValue;
}
//End::check for undefined values

//Begin :: Delete Coordinator by userId
function deleteCoordinatorRecordByUserId(id){
    bootbox.confirm({
        title: "Delete Coordinator",
        message: "Are you sure? Do you want to delete this Coordinator?",
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
                    url: 'deleteCoordinatorById',
                    data: {emp_id:id},
                    success: function(data) {
                    var fnldata =  JSON.parse(data.jsonStr);
                    $.each(fnldata, function (i, val) {
                        if(val === 'deleted') {
                            initializeCoordinatorTable();
                            $(".msg-alert").addClass('alert alert-success');
                            $(".msg-alert").html("<b>Coordinator Deleted</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-success');
                                $(".msg-alert").html("");
                            }, 1500); 
                        }else {
                            $(".msg-alert").addClass('alert alert-danger');
                            $(".msg-alert").html("<b>Something Went Wrong !!</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-danger');
                                $(".msg-alert").html("");
                            }, 1500);
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
//End :: Delete Coordinator by userId

//Begin:: View Coordinator by userId
function viewCoordinatorRecordByUserId(id){
    $('.loader').show();
    $('.text-danger').html('');
     $.ajax({
         type: 'GET',
         url: "viewCoordinatorsById",
         data: {emp_id:id}, 
         success: function (data) {
            $('.loader').hide();
            $(".modal-title").html("View Coordinator");
            $("#ViewCoordinatorRec").modal({backdrop: 'static', keyboard: false});
            var viewfnlData = JSON.parse(data.jsonStr);
            var ViewData = '';
            $.each(viewfnlData, function (i, val) {
                if(val.pending_request_count === 0){
                    pendingViewLink = '';
                }else{
                    pendingViewLink = '<a href="javascript:void(0)" class="ml-5" onclick=\"viewPendingReqByUserId(' + val.emp_id + ');\"> View</a>';
                }
                
                if(val.completed_request_count === 0){
                    CompletedViewLink = '';
                }else{
                    CompletedViewLink = '<a href="javascript:void(0)" class="ml-5" onclick=\"viewCompletedReqByUserId(' + val.emp_id + ');\"> View</a>';
                }
                
                if(val.emp_category === 'Central' || val.emp_category === 'UT'){
                    ViewData += '<tr><th>Category</th><td>'+ val.emp_category +'</td></tr><tr><th>Ministry/Organization</th><td>'+ val.emp_min_state_org +'</td></tr><tr><th>Department/Division/Domain</th><td>'+ checkUndefined(val.emp_dept) +'</td></tr><tr><th>Name</th><td>'+ checkUndefined(val.emp_coord_name) +'</td></tr><tr><th>Email</th><td>'+ val.emp_coord_email +'</td></tr><tr><th>Mobile</th><td>'+ checkUndefined(val.emp_coord_mobile) +'</td></tr><tr><th>IP</th><td>'+ checkUndefined(val.emp_ip) +'</td></tr><tr><th>Completed Request</th><td>'+ val.completed_request_count +'</td></tr><tr><th>Pending Request</th><td>'+ val.pending_request_count +'</td></tr><tr><th>Forwaded Request</th><td>'+ val.forwarded_request_count +'</td></tr><tr><th>Rejected Request</th><td>'+ val.rejected_request_count +'</td></tr>';
                }
                else if(val.emp_category === 'State'){
                    ViewData += '<tr><th>Category</th><td>'+ val.emp_category +'</td></tr><tr><th>State</th><td>'+ val.emp_min_state_org +'</td></tr><tr><th>Department</th><td>'+ checkUndefined(val.emp_dept) +'</td></tr><tr><th>Name</th><td>'+ checkUndefined(val.emp_coord_name) +'</td></tr><tr><th>Email</th><td>'+ val.emp_coord_email +'</td></tr><tr><th>Mobile</th><td>'+ checkUndefined(val.emp_coord_mobile) +'</td></tr><tr><th>IP</th><td>'+ checkUndefined(val.emp_ip) +'</td></tr><tr><th>Completed Request</th><td>'+ val.completed_request_count +'</td></tr><tr><th>Pending Request</th><td>'+ val.pending_request_count +'</td></tr><tr><th>Forwaded Request</th><td>'+ val.forwarded_request_count +'</td></tr><tr><th>Rejected Request</th><td>'+ val.rejected_request_count +'</td></tr>';
                }
               else if (val.emp_category === 'Others' || val.emp_category === "Psu" || val.emp_category === "Const" || val.emp_category === "Nkn" || val.emp_category === "Project") {
                    ViewData += '<tr><th>Category</th><td>'+ val.emp_category +'</td></tr><tr><th>Organization</th><td>'+ val.emp_min_state_org +'</td></tr><tr><th>Name</th><td>'+ checkUndefined(val.emp_coord_name) +'</td></tr><tr><th>Email</th><td>'+ val.emp_coord_email +'</td></tr><tr><th>Mobile</th><td>'+ checkUndefined(val.emp_coord_mobile) +'</td></tr><tr><th>IP</th><td>'+ checkUndefined(val.emp_ip) +'</td></tr><tr><th>Completed Request</th><td>'+ val.completed_request_count +'</td></tr><tr><th>Pending Request</th><td>'+ val.pending_request_count +'</td></tr><tr><th>Forwaded Request</th><td>'+ val.forwarded_request_count +'</td></tr><tr><th>Rejected Request</th><td>'+ val.rejected_request_count +'</td></tr>';
                }
            });
            $("#ViewEmpBody").html('');
            $("#ViewEmpBody").html(ViewData);

         },
         error: function () {
            console.log("Something Went Wrong!!");
         }
     
     });
}
//End:: View Coordinator by userId

$("#add-coo-rcd").click(function() {
    $('.text-danger').html('');
    $("#cooAddFrm input").val('');
    $('#cooAddFrm select').val('');
    $('#emp_min_state_org').find('option').remove().end().append('<option value="">--Select--</option>');
    $('#emp_dept').find('option').remove().end().append('<option value="">--Select--</option>');
    $("#updCooRecBtn").hide();
    $("#addCooRecBtn").show();
    $("#central_div").show();
    $("#other_div").hide();
    $("#state_div").hide();
    $("#modalName").html("Add Coordinator");
    $("#addCooRecBtn").html("Add Coordinator");
    $("#cooAddMod").modal({backdrop: 'static', keyboard: false});
});

//Begin:: Coordinator Add
$('#addCooRecBtn').click(function (e) {
    $('.text-danger').html('');
    e.preventDefault();
    var data = JSON.stringify($('#cooAddFrm').serializeObject());
    $.ajax({
        type: 'POST',
        url: "insertCoordinatorTable",
        data: {data: data},
        datatype: JSON,
        success: function (data) {
            var addfnlData = JSON.parse(data.jsonStr);
            var addfnlDataStatus = addfnlData.status;   
            if(addfnlData.emp_category === 'invalid'){
                $(".emp_category").html("<b>Please Select Category.</b>");
                setTimeout(function(){ 
                    $(".emp_category").html("");
                }, 2000);                
            }
            
            //Add Coordinator (Case A::Central)
            if(addfnlData.emp_min_state_org === 'invalid'){
                $(".emp_min_state_org").html("<b>Please Select Ministry/Organization.</b>");
                setTimeout(function(){ 
                    $(".emp_min_state_org").html("");
                }, 2000);                
            }
            if(addfnlData.emp_dept === 'invalid'){
                $(".emp_dept").html("<b>Please Select Department/Division/Domain.</b>");
                setTimeout(function(){ 
                    $(".emp_dept").html("");
                }, 2000);                
            }
            
            //Add Coordinator (Case A::State)
            if(addfnlData.state_code === 'invalid'){
                $(".stateCode").html("<b>Please Select State.</b>");
                setTimeout(function(){ 
                    $(".stateCode").html("");
                }, 2000);                
            }
            if(addfnlData.state_dept === 'invalid'){
                $(".state_dept").html("<b>Please Select Department.</b>");
                setTimeout(function(){ 
                    $(".state_dept").html("");
                }, 2000);                
            }
            
            //Add Coordinator (Case A::State)
            if(addfnlData.org === 'invalid'){
                $(".org").html("<b>Please Select Organization.</b>");
                setTimeout(function(){ 
                    $(".org").html("");
                }, 2000);                
            }           
            
            if(addfnlData.emp_coord_name === 'invalid'){
                $(".emp_coord_name").html("<b>Please Enter Name.</b>");
                setTimeout(function(){ 
                    $(".emp_coord_name").html("");
                }, 2000);                
            }
            if(addfnlData.emp_coord_email === 'invalid'){
                $(".emp_coord_email").html("<b>Please Enter Valid Email.</b>");
                setTimeout(function(){ 
                    $(".emp_coord_email").html("");
                }, 2000);                
            }
            if(addfnlData.emp_coord_mobile === 'invalid'){
                $(".emp_coord_mobile").html("<b>Please Enter Valid Mobile.</b>");
                setTimeout(function(){ 
                    $(".emp_coord_mobile").html("");
                }, 2000);                
            }  
            if(addfnlData.emp_ip === 'invalid'){
                $(".emp_ip").html("<b>Please Enter Valid IP.</b>");
                setTimeout(function(){ 
                    $(".emp_ip").html("");
                }, 2000);                
            }
            if(addfnlDataStatus === 'inserted'){
                $.each(addfnlData, function (i, val) {
                    if(val === 'inserted'){
                        initializeCoordinatorTable();
                        $(".msg-alert").addClass('alert alert-success');
                        $(".msg-alert").html("<b>Coordinator Added Successfully.</b>");
                        $("#cooAddMod").modal('hide');
                        setTimeout(function(){ 
                            $(".msg-alert").removeClass('alert alert-success');
                            $(".msg-alert").html("");
                        }, 1500);
                    }
                });                
            }
            else{
                console.log("This is error Case Add !!")
            }               
        },
        error: function () {
            console.log("Something Went Wrong !!");
        }
    });          
});
//End:: Coordinator Add

//Begin :: Coordinator Edit
function editCoordinatorRecordByUserId(id){
    $('.loader').show();
    $('#cooAddFrm select').val('');
    $('#modalName').html('');
    $('#modalName').append('Update Coordinator');
    $("#addCooRecBtn").hide();
    $("#updCooRecBtn").show();
    $('.text-danger').html('');
     $.ajax({
         type: 'GET',
         url: "viewCoordinatorsById",
         data: {emp_id:id}, 
         success: function (data) {
            $('.loader').hide();
            var viewfnlData = JSON.parse(data.jsonStr);
            $.each(viewfnlData, function (i, val) {
                $('#emp_coord_name').val(val.emp_coord_name);
                $('#emp_coord_email').val(val.emp_coord_email);
                $('#emp_coord_mobile').val(val.emp_coord_mobile);
                $('#emp_bo_id').val(val.emp_bo_id);
                $('#emp_domain').val(val.emp_domain);
                $('#emp_category').val(val.emp_category);
                $('#emp_admin_email').val(val.emp_admin_email);
                $('#emp_ip').val(val.emp_ip);
                $('#emp_id').val(val.emp_id);
                if(val.emp_category === 'State'){
                    $("#central_div").hide();
                    $("#other_div").hide();
                    $("#state_div").show();
                    $('#stateCode').find('option').remove().end().append('<option value="'+val.emp_min_state_org+'">'+val.emp_min_state_org+'</option>').val(val.emp_min_state_org);
                    $('#state_dept').find('option').remove().end().append('<option value="'+val.emp_dept+'">'+val.emp_dept+'</option>').val(val.emp_dept);
                }
                else if(val.emp_category === 'Central' || val.emp_category === 'UT'){
                    $("#state_div").hide();
                    $("#other_div").hide();
                    $("#central_div").show(); 
                    $('#emp_min_state_org').find('option').remove().end().append('<option value="'+val.emp_min_state_org+'">'+val.emp_min_state_org+'</option>').val(val.emp_min_state_org);
                    $('#emp_dept').find('option').remove().end().append('<option value="'+val.emp_dept+'">'+val.emp_dept+'</option>').val(val.emp_dept);
                }
                else if (val.emp_category === 'Others' || val.emp_category === "Psu" || val.emp_category === "Const" || val.emp_category === "Nkn" || val.emp_category === "Project") {
                    $("#central_div").hide();
                    $("#state_div").hide();
                    $("#other_div").show();
                    $('#org').find('option').remove().end().append('<option value="'+val.emp_min_state_org+'">'+val.emp_min_state_org+'</option>').val(val.emp_min_state_org);
                }
            });
            $("#cooAddMod").modal({backdrop: 'static', keyboard: false});
         },
         error: function () {
            console.log("Something Went Wrong!!");
         }
     
     });
}
//End :: Coordinator Edit

//Begin::Update coordinator
$('#updCooRecBtn').click(function (e) {
    $('.text-danger').html('');
    e.preventDefault();
    var empId = $('#emp_id').val();
    var data = JSON.stringify($('#cooAddFrm').serializeObject());
    //console.log("Form data Update Case::::" + data);
    $.ajax({
        type: 'POST',
        url: "updateCoordinator",
        data: {data:data},
        datatype: JSON,
        success: function (data) {
            var updfnlData = JSON.parse(data.jsonStr);
            var updfnlDataStatus = updfnlData.status;
            if(updfnlData.emp_category === 'invalid'){
                $(".emp_category").html("<b>Please Select Category.</b>");
                setTimeout(function(){ 
                    $(".emp_category").html("");
                }, 2000);                
            }
            
            //Add Coordinator (Case A::Central)
            if(updfnlData.emp_min_state_org === 'invalid'){
                $(".emp_min_state_org").html("<b>Please Select Ministry/Organization.</b>");
                setTimeout(function(){ 
                    $(".emp_min_state_org").html("");
                }, 2000);                
            }
            if(updfnlData.emp_dept === 'invalid'){
                $(".emp_dept").html("<b>Please Select Department/Division/Domain.</b>");
                setTimeout(function(){ 
                    $(".emp_dept").html("");
                }, 2000);                
            }
            
            //Add Coordinator (Case A::State)
            if(updfnlData.state_code === 'invalid'){
                $(".stateCode").html("<b>Please Select State.</b>");
                setTimeout(function(){ 
                    $(".stateCode").html("");
                }, 2000);                
            }
            if(updfnlData.state_dept === 'invalid'){
                $(".state_dept").html("<b>Please Select Department.</b>");
                setTimeout(function(){ 
                    $(".state_dept").html("");
                }, 2000);                
            }
            
            //Add Coordinator (Case A::State)
            if(updfnlData.org === 'invalid'){
                $(".org").html("<b>Please Select Organization.</b>");
                setTimeout(function(){ 
                    $(".org").html("");
                }, 2000);                
            }           
            
            if(updfnlData.emp_coord_name === 'invalid'){
                $(".emp_coord_name").html("<b>Please Enter Name.</b>");
                setTimeout(function(){ 
                    $(".emp_coord_name").html("");
                }, 2000);                
            }
            if(updfnlData.emp_coord_email === 'invalid'){
                $(".emp_coord_email").html("<b>Please Enter Valid Email.</b>");
                setTimeout(function(){ 
                    $(".emp_coord_email").html("");
                }, 2000);                
            }
            if(updfnlData.emp_coord_mobile === 'invalid'){
                $(".emp_coord_mobile").html("<b>Please Enter Valid Mobile.</b>");
                setTimeout(function(){ 
                    $(".emp_coord_mobile").html("");
                }, 2000);                
            }  
            if(updfnlData.emp_ip === 'invalid'){
                $(".emp_ip").html("<b>Please Enter Valid IP.</b>");
                setTimeout(function(){ 
                    $(".emp_ip").html("");
                }, 2000);                
            }
            if(updfnlDataStatus === 'updated'){
                $.each(updfnlData, function (i, val) {
                    if(val === 'updated'){
                        initializeCoordinatorTable();
                        $(".msg-alert").addClass('alert alert-success');
                        $(".msg-alert").html("<b>Coordinator Updated Successfully.</b>");
                        $("#cooAddMod").modal('hide');
                        setTimeout(function(){ 
                            $(".msg-alert").removeClass('alert alert-success');
                            $(".msg-alert").html("");
                        }, 1500);
                    }
                });               
            }
            else{
                console.log("This is error Case Updated !!")
            }               
        },
        error: function () {
            console.log("Something Went Wrong!!");
        }
    });        
});
//End::Update coordinator



//--------------------------------------Section 1 End::Coordinator Add,Delete,Edit,Update------------------------------------------



function viewCompletedReqByUserId(id){
    $('.loader').show();
    $('.text-danger').html('');
     $.ajax({
         type: 'GET',
         url: "getCompletedRequestOfCoordinator",
         data: {emp_id:id}, 
         success: function (data) {
            $('.loader').hide();
            var viewfnlData = JSON.parse(data.jsonStr);
            var ViewData = '';
            $.each(viewfnlData, function (i, val) {
                $.each(val, function (i, value) {
                ViewData += '<tr><td>'+ value.registration_no +'</td><td>'+ value.applicant_name +'</td><td>'+ value.applicant_email +'</td><td>'+ value.applicant_mobile +'</td><td>'+ value.ca_email +'</td><td>'+ value.status +'</td></tr>';
                });
            });
            $("#ViewCompReqBody").html('');
            $("#ViewCompReqData").dataTable().fnDestroy();
            $("#ViewCompReqBody").html(ViewData);
            setTimeout(function(){
                $('#ViewCompReqData').dataTable({
                    "pageLength": 10,
                    "searching" : true,
                    "responsive": true,
                    "columnDefs": [
                                { responsivePriority: 1, targets: 0 },
                                { responsivePriority: 2, targets: 5 }
                            ],
                });
            },2);
            $(".modal-title").html("View Completed Request");
            $("#ViewCompReqRec").modal({backdrop: 'static', keyboard: false});
         },
         error: function () {
            console.log("Something Went Wrong!!");
         }
     
     });
}

function viewPendingReqByUserId(id){
    $('.loader').show();
    $('.text-danger').html('');
     $.ajax({
         type: 'GET',
         url: "getPendingRequestOfCoordinator",
         data: {emp_id:id}, 
         success: function (data) {
            $('.loader').hide();
            var viewfnlData = JSON.parse(data.jsonStr);
            var viewfnlPendingData = viewfnlData['status'];
            var ViewData = '';
            $.each(viewfnlPendingData, function (i, val) {
                ViewData += "<tr><td ><input type='checkbox' name='row-check' onclick=\"childCheck();\" /></td><td class='track_id_single'><a href='javascript:void(0)' class='previewedit1' id='false'>"+ val.registration_no +"</a></td><td>"+ val.applicant_name +"</td><td>"+ val.applicant_email +"</td><td>"+ val.applicant_mobile +"</td><td>"+ val.ca_email +"</td><td><div class='btn-group'><button class='btn btn-primary btn-xs green dropdown-toggle' type='button' data-toggle='dropdown' aria-expanded='true' title='Click to take appropriate actions'>Actions<i class='fa fa-angle-down'></i></button><ul class='dropdown-menu dropdown-menu-right action-btn-list' role='menu' style='position:inherit;'><li title='Track the request'><a href='javascript:void(0)' onclick=\"track_frm('" + val.registration_no + "');\"><i class='fa fa-map-marker'></i> Track</a></li><li title='Preview the request'><a href='javascript:void(0)' class='previewedit1' id='false'><i class='fa fa-eye'></i> Preview</a></li></ul></div></td></tr>";
            });
            $("#ViewPendReqBody").html('');
            $("#ViewPendReqData").dataTable().fnDestroy(); 
            $("#ViewPendReqBody").html(ViewData);
            setTimeout(function(){
                $('#ViewPendReqData').dataTable({
                    "pageLength": 10,
                    "searching" : true,
                    "responsive": true,
                    "columnDefs": [
                                { responsivePriority: 1, targets: 0 },
                                { responsivePriority: 2, targets: 6 },
                            ]

                });
            },2);
            $(".modal-title").html("View Pending Request");
            $("#ViewPendReqRec").modal({backdrop: 'static', keyboard: false});        
         },
         error: function () {
            console.log("Something Went Wrong!!");
         }
     
     });
}

//----------------------------  Begin::Checkbox for View Pending Request  --------------------

// Header Master Checkbox Event
$("#masterCheck").on("click", function () {
    if($("input:checkbox[name='select-all']").prop("checked")) {
        $("input:checkbox[name='row-check']").prop("checked", true);
    }else {
        $("input:checkbox[name='row-check']").prop("checked", false);
    }
});

// Check event on each table row checkbox            
function childCheck(){
    var total_check_boxes = $("input:checkbox[name='row-check']").length;
    var total_checked_boxes = $("input:checkbox[name='row-check']:checked").length;
    if (total_check_boxes === total_checked_boxes) {
        $("#masterCheck").prop("checked", true);
    }else {
        $("#masterCheck").prop("checked", false);
    }
}

// Get data for checked box on BulkPullBack        
$('#BulkPullbackRequest').click(function(){
    var checkboxAllfnlData = $('#ViewPendReqData').find('input[name="row-check"]:checked').map(function(){
          return $(this).closest('tr').find('td:nth-child(2)').text();
    }).get().join(',');
    if(checkboxAllfnlData.length > 1){
        $('.loader').show();
        $("#ViewPendReqRec").modal('toggle');
        $('.text-danger').html('');
        $.ajax({
            type: 'POST',
            url: "pullbackAllRequestHog",
            data: {registration_no:checkboxAllfnlData},
            datatype: JSON,
            success: function (data) {
                $('.loader').hide();
                var bulkPullbackfnlData = JSON.parse(data.jsonStr);
                $.each(bulkPullbackfnlData, function (i, val) {
                    if(val === 'Pulled Successfully  '){
                        initializeCoordinatorTable();
                        viewPendingReqHOG();
                        $(".msg-alert").addClass('alert alert-success');
                        $(".msg-alert").html("<b>Bulk Pulled Successfully.</b>");
                        setTimeout(function(){ 
                            $(".msg-alert").removeClass('alert alert-success');
                            $(".msg-alert").html("");
                        }, 1500);
                    }
                });
            },
            error: function () {
                console.log("Something Went Wrong!!");
            }
        });
    }
    else{
        $(".msg-mod-alert").addClass('alert alert-danger');
        $(".msg-mod-alert").html("<b>Please select atleast one request.</b>");
        setTimeout(function(){ 
            $(".msg-mod-alert").removeClass('alert alert-danger');
            $(".msg-mod-alert").html("");
        }, 1500);
    }
});
//-------------------------- End::CheckBox for View Pending Request ----------------------------- 

function requestActionPullback(regNo){
        bootbox.confirm({
        title: "Request Pullback",
        message: "Are you sure? Do you want to pullback this Request?",
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
            if(result === true) {
                $(".loader").show();
                $.ajax({
                    type: 'post',
                    url: 'pullbackRequestHog',
                    data: {registration_no:regNo},
                    success: function(data) {
                    var pullbackReqfnldata =  JSON.parse(data.jsonStr);
                        if(pullbackReqfnldata.status === 'Pulled Successfully  ') {
                            initializeCoordinatorTable();
                            viewPendingReqHOG();
                            $(".req_approve").removeClass('display-hide');
                            $(".req_reject").removeClass('display-hide');
                            $(".req_pullback").addClass('display-hide');
                            $(".msg-alert").addClass('alert alert-success');
                            $(".msg-alert").html("<b>Pulled Request Successfully</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-success');
                                $(".msg-alert").html("");
                            }, 1500);
                        }else {
                            $(".msg-alert").addClass('alert alert-danger');
                            $(".msg-alert").html("<b>Something Went Wrong !!</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-danger');
                                $(".msg-alert").html("");
                            }, 1500);
                        }
                    }, 
                    complete: function() {
                        $(".loader").hide();
                    }
                });
            }
            else if(result===false){
                //$("#RequestActionPreviewMod").modal({backdrop: 'static', keyboard: false});
            }
        }
    });
}

function requestActionApprove(regNo){
        bootbox.confirm({
        title: "Approve Request",
        message: "Are you sure? Do you want to Approve this Request?",
        buttons: {
            confirm: {
                label: 'Confirm',
                className: 'btn-success'
            },
            cancel: {
                label: 'Cancel',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if(result === true) {
                $(".loader").show();
                $.ajax({
                    type: 'post',
                    url: 'approveRequestHog',
                    data: {registration_no:regNo},
                    success: function(data) {
                    var approveReqfnldata =  JSON.parse(data.jsonStr);
                        if(approveReqfnldata.status === 'Approved Successfully and send to admin ') {
                            $(".msg-alert").addClass('alert alert-success');
                            $(".msg-alert").html("<b>"+ approveReqfnldata.status + approveReqfnldata.to_email +"</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-success');
                                $(".msg-alert").html("");
                            }, 1500);
                        }else {
                            $(".msg-alert").addClass('alert alert-danger');
                            $(".msg-alert").html("<b>Something Went Wrong !!</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-danger');
                                $(".msg-alert").html("");
                            }, 1500);
                        }
                    }, 
                    complete: function() {
                        $(".loader").hide();
                    }
                });
            }
            else if(result===false){
                //$("#ViewPendReqRec").modal({backdrop: 'static', keyboard: false});
            }
        }
    });
}

function requestActionReject(regNo){
        bootbox.confirm({
        title: "Reject Request",
        message: "Are you sure? Do you want to reject this Request?",
        buttons: {
            confirm: {
                label: 'Confirm',
                className: 'btn-success'
            },
            cancel: {
                label: 'Cancel',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if(result === true) {
                $(".loader").show();
                $.ajax({
                    type: 'post',
                    url: 'rejectRequestHog',
                    data: {registration_no:regNo},
                    success: function(data) {
                    var rejectReqfnldata =  JSON.parse(data.jsonStr);
                    $.each(rejectReqfnldata, function (i, val) {
                        if(val === 'Rejected_Successfully') {
                            $(".msg-alert").addClass('alert alert-success');
                            $(".msg-alert").html("<b>Request Reject Successfully</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-success');
                                $(".msg-alert").html("");
                            }, 1500);
                        }else {
                            $(".msg-alert").addClass('alert alert-danger');
                            $(".msg-alert").html("<b>Something Went Wrong !!</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-danger');
                                $(".msg-alert").html("");
                            }, 1500);
                        }
                    });
                    }, 
                    complete: function() {
                        $(".loader").hide();
                    }
                });
            }
            else if(result===false){
                //$("#ViewPendReqRec").modal({backdrop: 'static', keyboard: false});
            }
        }
    });
}

$("#reqpreviewbackbtn").click(function() {
    $("#RequestActionPreviewMod").modal('hide');
    $("#ViewPendReqRec").modal({backdrop: 'static', keyboard: false});
});


//------- Section 2 Begin:: PedingRequestHOG, ApprovePendingRequestHOG, RejectPendingRequestHog, BulkApprovePendingRequestHog----------------

$("#pend-req-hog-rcd").click(function() {
    $('.text-danger').html('');
    $(".modal-title").html("View Pending Request HOG");
    viewPendingReqHOG();
    $("#ViewPendReqHOGRec").modal({backdrop: 'static', keyboard: false});  
});

$(document).ready(function() {
        viewPendingReqHOG(); 
});

function viewPendingReqHOG(){
    $('.loader').show();
    $('.text-danger').html('');
     $.ajax({
         type: 'GET',
         url: "getPendingRequestOfHog",
         dataType: 'json',
         success: function (data) {
            $('.loader').hide();
            var fnlreqHOGData = JSON.parse(data.jsonStr);
            var fnlreqHOGDataStatus = fnlreqHOGData['status'];
            var fnlreqHOGDataCount = fnlreqHOGData['totalcount'];
            if(fnlreqHOGDataCount > 0){
                $("#pend-req-hog-rcd").show();
                var ViewData = '';
                $.each(fnlreqHOGDataStatus, function (i, val) {
                    ViewData += "<tr><td ><input type='checkbox' name='approvebulkchild-check' onclick=\"bulkApproveAllChildCheck();\"/></td><td class='track_id1'><a href='javascript:void(0);' class='previewedit' id='false'>"+ val.registration_no +"</a></td><td>"+ val.applicant_name +"</td><td>"+ val.applicant_email +"</td><td>"+ val.applicant_mobile +"</td><td>"+ val.ca_email +"</td><td><div class='btn-group'><button class='btn btn-primary btn-xs green dropdown-toggle' type='button' data-toggle='dropdown' aria-expanded='true' title='Click to take appropriate actions'>Actions<i class='fa fa-angle-down'></i></button><ul class='dropdown-menu dropdown-menu-right action-btn-list' role='menu' style='position:inherit;'><li title='Track the request'><a href='javascript:void(0)' onclick=\"track_frm('" + val.registration_no + "');\"><i class='fa fa-map-marker'></i> Track</a></li><li title='Preview the request'><a href='javascript:void(0)' class='previewedit' id='false'><i class='fa fa-eye' ></i> Preview</a></li></ul></div></td></tr>";
                });
                $("#ViewPendReqHOGBody").html('');
                $("#ViewPendReqHOGData").dataTable().fnDestroy(); 
                $("#ViewPendReqHOGBody").html(ViewData);
                setTimeout(function(){ 
                    $('#ViewPendReqHOGData').dataTable({
                        "pageLength": 10,
                        "searching" : true,
                        "responsive": true,
                        "columnDefs": [
                                    { responsivePriority: 1, targets: 0 },
                                    { responsivePriority: 2, targets: 6 },
                                ]

                    });
                },1000);                
            }else{
                $("#pend-req-hog-rcd").hide();
            }

            //$("#ViewPendReqRec").modal({backdrop: 'static', keyboard: false});
         },
         error: function () {
        	 console.log("This is error!!");
         }
     
     });
}

//----------------------------  Begin::Checkbox for View HOG Pending Request  --------------------

// Header Master Checkbox Event
$("#headcheck").on("click", function () {
    if($("input:checkbox[name='headcheck']").prop("checked")) {
        $("input:checkbox[name='approvebulkchild-check']").prop("checked", true);
    }else {
        $("input:checkbox[name='approvebulkchild-check']").prop("checked", false);
    }
});

// Check event on each table row checkbox            
function bulkApproveAllChildCheck(){
    var total_check_boxes = $("input:checkbox[name='approvebulkchild-check']").length;
    var total_checked_boxes = $("input:checkbox[name='approvebulkchild-check']:checked").length;
    if (total_check_boxes === total_checked_boxes) {
        $("#headcheck").prop("checked", true);
    }else {
        $("#headcheck").prop("checked", false);
    }
}

// Get data for checked box on BulkPullBack        
$('#BulkApproveRequest').click(function(){
    var approveAllfnlData = $('#ViewPendReqHOGData').find('input[name="approvebulkchild-check"]:checked').map(function(){
          return $(this).closest('tr').find('td:nth-child(2)').text();
    }).get().join(',');
    if(approveAllfnlData.length > 1){
        $("#ViewPendReqHOGRec").modal('toggle');
        $('.text-danger').html('');
        bootbox.confirm({
            title: "Approve Request",
            message: "Are you sure? Do you want to Approve all Request without previrew?",
            buttons: {
                confirm: {
                    label: 'Confirm',
                    className: 'btn-success'
                },
                cancel: {
                    label: 'Cancel',
                    className: 'btn-danger'
                }
            },
            callback: function (result) {
                if(result === true) {
                    $('.loader').show();
                    $.ajax({
                        type: 'POST',
                        url: "approveAllRequestHog",
                        data: {registration_no:approveAllfnlData},
                        datatype: JSON,
                        success: function (data) {
                            $('.loader').hide();
                            var bulkApprovefnlData = JSON.parse(data.jsonStr);
                            $.each(bulkApprovefnlData, function (i, val) {
                                if(val === 'Approved Successfully and send to admin '){
                                    initializeCoordinatorTable();
                                    viewPendingReqHOG();
                                    $(".msg-alert").addClass('alert alert-success');
                                    $(".msg-alert").html("<b>Bulk Aprpoved Successfully.</b>");
                                    setTimeout(function(){ 
                                        $(".msg-alert").removeClass('alert alert-success');
                                        $(".msg-alert").html("");
                                    }, 1500);
                                }
                            });
                        },
                        error: function () {
                            console.log("This is error Case Update!!");
                        }
                    });
                }
                else if(result===false){
                    $(".modal-title").html("View Pending Request HOG");
                    $("#ViewPendReqHOGRec").modal({backdrop: 'static', keyboard: false});
                }
            }
        });
    }
    else{
        $(".msg-mod-alert").addClass('alert alert-danger');
        $(".msg-mod-alert").html("<b>Please select atleast one request.</b>");
        setTimeout(function(){ 
            $(".msg-mod-alert").removeClass('alert alert-danger');
            $(".msg-mod-alert").html("");
        }, 1500);
    }
});
//-------------------------- End::CheckBox for View HOG Pending Request ----------------------------- 

function requestHogPendingActionApprove(regNo){
        bootbox.confirm({
        title: "Approve Request",
        message: "Are you sure? Do you want to Approve this Request?",
        buttons: {
            confirm: {
                label: 'Confirm',
                className: 'btn-success'
            },
            cancel: {
                label: 'Cancel',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if(result === true) {
                $(".loader").show();
                $.ajax({
                    type: 'post',
                    url: 'approveRequestHog',
                    data: {registration_no:regNo},
                    success: function(data) {
                    var approveReqfnldata =  JSON.parse(data.jsonStr);
                        if(approveReqfnldata.status === 'Approved Successfully and send to admin ') {
                            viewPendingReqHOG();
                            $(".msg-alert").addClass('alert alert-success');
                            $(".msg-alert").html("<b>"+ approveReqfnldata.status + approveReqfnldata.to_email +"</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-success');
                                $(".msg-alert").html("");
                            }, 1500);
                        }else {
                            $(".msg-alert").addClass('alert alert-danger');
                            $(".msg-alert").html("<b>Please select atleast one request.</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-danger');
                                $(".msg-alert").html("");
                            }, 1500);
                        }
                    }, 
                    complete: function() {
                        $(".loader").hide();
                    }
                });
            }
            else if(result===false){
                $(".modal-title").html("View Pending Request HOG");
                $("#ViewPendReqHOGRec").modal({backdrop: 'static', keyboard: false});
            }
        }
    });
}

function requestHogPendingActionReject(regNo){
        bootbox.confirm({
        title: "Reject Request",
        message: "Are you sure? Do you want to reject this Request?",
        buttons: {
            confirm: {
                label: 'Confirm',
                className: 'btn-success'
            },
            cancel: {
                label: 'Cancel',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if(result === true) {
                $(".loader").show();
                $.ajax({
                    type: 'post',
                    url: 'rejectRequestHog',
                    data: {registration_no:regNo},
                    success: function(data) {
                    var rejectReqfnldata =  JSON.parse(data.jsonStr);
                    $.each(rejectReqfnldata, function (i, val) {
                        if(val === 'Rejected_Successfully') {
                            viewPendingReqHOG();
                            $(".msg-alert").addClass('alert alert-success');
                            $(".msg-alert").html("<b>Request Reject Successfully</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-success');
                                $(".msg-alert").html("");
                            }, 1500);
                        }else {
                            $(".msg-alert").addClass('alert alert-danger');
                            $(".msg-alert").html("<b>Something Went Wrong !!</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-danger');
                                $(".msg-alert").html("");
                            }, 1500);
                        }
                    });
                    }, 
                    complete: function() {
                        $(".loader").hide();
                    }
                });
            }
            else if(result===false){
                $(".modal-title").html("View Pending Request HOG");
                $("#ViewPendReqHOGRec").modal({backdrop: 'static', keyboard: false});
            }
        }
    });    
}


$("#statsHOG").click(function() {
    $('.text-danger').html('');
    $("#ViewHOGStatsRec .modal-title").html("View Statistics");
    $("#ViewHOGStatsRec").modal({backdrop: 'static', keyboard: false});  
});
//----------------------- Section 2 End:: peding request HOG ----------------------------------
    


//----------------------- Section 4 Begin : Change Department HOG ----------------------------

$("#change-dept-req-hog-rcd").click(function() {
    $('.text-danger').html('');
    $("#ChangeDeptHOGRec input").val('');
    $('#ChangeDeptHOGRec select').val('');
    $(".modal-title").html("Change Existing Department");
    $("#ChangeDepartHOGRec").modal({backdrop: 'static', keyboard: false});
});

$("#change_dept_emp_category").change(function () {
    if (this.value === 'State') {   
        $("#change_dept_emp_min_div").hide();
        $("#change_dept_emp_state_div").show();
    }
    else if(this.value === 'UT' || this.value === 'Central'){
        $("#change_dept_emp_state_div").hide();
        $("#change_dept_emp_min_div").show();

    }
});

//Begin::Get Category for Add Coordinator
$(document).ready(function() {
    $.ajax({
        type: 'GET',
        url: 'getCategoryHOG',
        dataType: 'json',
        success: function (data) {
                var ChangeDeptCatData = JSON.parse(data.jsonStr);
                var chnage_dept_category = "";
                $("#change_dept_emp_category").html('');
                $("#change_dept_emp_category").html('<option value="">--Select--</option>');
                $.each(ChangeDeptCatData, function (i, val) {
                    $.each(val, function (i, value) {
                        chnage_dept_category += '<option value="' + value + '">' + value + '</option>';
                    });
                });
                $("#change_dept_emp_category").append(chnage_dept_category);
        }
    });
});
//End::Get Category for Add Coordinator

$("#change_dept_emp_category").change(function () {
    var orgTypeVal = $(this).val();
    if (orgTypeVal === 'State') {
        $.ajax({
            type: 'post',
            url: 'getCategoryMinistryHOG',
            data: {emp_category: orgTypeVal},
            success: function (data) {
                var ChangeDeptMinistryRes = JSON.parse(data.jsonStr);
                var ministry_data = "";
                $("#change_dept_state_code").html('');
                $("#change_dept_state_code").html('<option value="">-Select-</option>');
                $.each(ChangeDeptMinistryRes, function (i, val) {
                    $.each(val, function (i, value) {
                        ministry_data += '<option value="' + value + '">' + value + '</option>';
                    });
                });
                
                $("#change_dept_state_code").append(ministry_data);
            }
        });
    }
    else if(orgTypeVal === 'UT' || orgTypeVal === 'Central') {
        $.ajax({
            type: 'post',
            url: 'getCategoryMinistryHOG',
            data: {emp_category: orgTypeVal},
            success: function (data) {
                var ChangeDeptMinistryRes = JSON.parse(data.jsonStr);
                var ministry_data = "";
                $("#change_dept_emp_min_state_org").html('');
                $("#change_dept_emp_min_state_org").html('<option value="">-Select-</option>');
                $.each(ChangeDeptMinistryRes, function (i, val) {
                    $.each(val, function (i, value) {
                        ministry_data += '<option value="' + value + '">' + value + '</option>';
                    });
                });
                $("#change_dept_emp_min_state_org").append(ministry_data);
            }
        });
    }
});

//Begin::On change of Ministry/Organization Provide Cetral Department AND Data Filter
$("#change_dept_emp_min_state_org").change(function () {
    var orgTypeVal = $(this).val();
    var orgType = $("#change_dept_emp_category").val();
    $.ajax({
        type: 'post',
        url: 'getCategoryMinistryDepartmentHOG',
        data: {emp_category: orgType,emp_min_state_org: orgTypeVal},
        success: function (data) {
            var ChangeDeptCentralDepartRes = JSON.parse(data.jsonStr);
            var ministry_dept_data = "";
            $("#change_dept_emp_dept").html('');
            $("#change_dept_emp_dept").html('<option value="">-Select-</option>');
            $.each(ChangeDeptCentralDepartRes, function (i, val) {
                $.each(val, function (i, value) {
                    ministry_dept_data += '<option value="' + value + '">' + value + '</option>';
                });
            });
            $("#change_dept_emp_dept").append(ministry_dept_data);
        }
    });
});
//End::On change of Ministry/Organization Provide Cetral Department AND Data Filter


//Begin::On change of State Provide State Department AND Data Filter
$("#change_dept_state_code").change(function () {
    var orgTypeVal = $(this).val();
    var orgType = $("#change_dept_emp_category").val();
    $.ajax({
        type: 'post',
        url: 'getCategoryMinistryDepartmentHOG',
        data: {emp_category: orgType,emp_min_state_org: orgTypeVal},
        success: function (data) {
            var ChangeDeptCentralDepartRes = JSON.parse(data.jsonStr);
            var state_data = "";
            $("#change_dept_state_dept").html('');
            $("#change_dept_state_dept").html('<option value="">-Select-</option>');
            $.each(ChangeDeptCentralDepartRes, function (i, val) {
                $.each(val, function (i, value) {
                    state_data += '<option value="' + value + '">' + value + '</option>';
                });
            });
            
            $("#change_dept_state_dept").append(state_data);
        }
    });
});
//End::On change of State Provide State Department AND Data Filter

$('#changeDeptbtnsubmit').click(function (e) {
        $('.text-danger').html('');
        e.preventDefault();
        $('.loader').show();
        var data = JSON.stringify($('#ChangeDeptHOGRec').serializeObject());
        var change_dept_emp_category = $("#change_dept_emp_category").val();
        var change_dept_emp_min_state_org = $("#change_dept_emp_min_state_org").val();
        var change_dept_emp_dept = $("#change_dept_emp_dept").val();
        
        var change_dept_state_code = $("#change_dept_state_code").val();
        var change_dept_state_dept = $("#change_dept_state_dept").val();          
        
        var change_dept_new_dept = $("#change_dept_new_depts").val();
        
        //console.log("Form data Add Department::::" + data);
            $.ajax({
                type: 'POST',
                url: "updateDepartmentHOG",
                data: {emp_category: change_dept_emp_category,emp_min_state_org:change_dept_emp_min_state_org,emp_dept:change_dept_emp_dept,state_code:change_dept_state_code,state_dept:change_dept_state_dept,new_dept:change_dept_new_dept},
                datatype: JSON,
                success: function (data) {
                        $('.loader').hide();
                        var changeDeptfnlData = JSON.parse(data.jsonStr);
                        var changeDeptfnlDataStatus = changeDeptfnlData.status;
                        if(changeDeptfnlData.emp_category === 'invalid'){
                            $(".change_dept_emp_category").html("<b>Please Select Category.</b>");
                            setTimeout(function(){ 
                                $(".change_dept_emp_category").html("");
                            }, 2000);                                 
                        }
                        
                        //Change department (Case :: Central)
                        if(changeDeptfnlData.add_dept_emp_min_state_org === 'invalid'){
                            $(".change_dept_emp_min_state_org").html("<b>Please Select Ministry/Organization.</b>");
                            setTimeout(function(){ 
                                $(".change_dept_emp_min_state_org").html("");
                            }, 2000);                               
                        }
                        if(changeDeptfnlData.emp_dept === 'invalid'){
                            $(".change_dept_emp_dept").html("<b>Please Select Department/Division/Domain.</b>");
                            setTimeout(function(){ 
                                $(".change_dept_emp_dept").html("");
                            }, 2000);                               
                        }
                        
                        //Change department (Case :: State)
                        if(changeDeptfnlData.add_dept_state_code === 'invalid'){
                            $(".change_dept_state_code").html("<b>Please Select State</b>");
                            setTimeout(function(){ 
                                $(".change_dept_state_code").html("");
                            }, 2000);                               
                        }
                        if(changeDeptfnlData.emp_dept === 'invalid'){
                            $(".change_dept_state_dept").html("<b>Please Select Department.</b>");
                            setTimeout(function(){ 
                                $(".change_dept_state_dept").html("");
                            }, 2000);                               
                        }
                        
                        if(changeDeptfnlData.new_dept === 'invalid'){
                            $(".change_dept_new_depts").html("<b>Please Enter New Department.</b>");
                            setTimeout(function(){ 
                                $(".change_dept_new_depts").html("");
                            }, 2000);                               
                        }
                        if(changeDeptfnlDataStatus === 'updated'){
                            $.each(changeDeptfnlData, function (i, val) {
                                if(val === 'updated'){
                                    initializeCoordinatorTable();
                                    $(".msg-alert").addClass('alert alert-success');
                                    $(".msg-alert").html("<b>Department Changed Successfully.</b>");
                                    $("#ChangeDepartHOGRec").modal('hide');
                                    setTimeout(function(){ 
                                        $(".msg-alert").removeClass('alert alert-success');
                                        $(".msg-alert").html("");
                                    }, 1500);
                                }
                            });                            
                        }
                        else{
                            console.log("This is error Case change Department !!");
                             
                        }

                },
                error: function () {
                    console.log("Something Went Wrong!!");
                }

            });
});  


//----------------------- Section 4 End : Change Department HOG ----------------------------




    
//------------------------  Section 3 Begin:: Add Department,Pending ALL Department Request, ApproveRequest, RejectRequest   -------------------------  

$("#add_dept_emp_category").change(function () {
    if (this.value === 'State') {   
        $("#add_dept_min_div").hide();
        $("#add_dept_state_div").show();
    }
    else if(this.value === 'UT' || this.value === 'Central'){
        $("#add_dept_state_div").hide();
        $("#add_dept_min_div").show();

    }
});

$(document).ready(function() {
    $.ajax({
        type: 'GET',
        url: 'getCategoryHOG',
        dataType: 'json',
        success: function (data) {
                var AddDeptCatData = JSON.parse(data.jsonStr);
                var add_dept_category = "";
                $("#add_dept_emp_category").html('');
                $("#add_dept_emp_category").html('<option value="">--Select--</option>');
                $.each(AddDeptCatData, function (i, val) {
                    $.each(val, function (i, value) {
                        add_dept_category += '<option value="' + value + '">' + value + '</option>';
                    });
                });
                $("#add_dept_emp_category").append(add_dept_category);
        }
    });
});    
    
$("#add_dept_emp_category").change(function () {
    var addDepartCategoryType = $("#add_dept_emp_category").val();
    if (addDepartCategoryType === 'State') {
        $.ajax({
            type: 'post',
            url: 'getCategoryMinistryHOG',
            data: {emp_category: addDepartCategoryType},
            success: function (data) {
                var AddDeptMinistryRes = JSON.parse(data.jsonStr);
                var add_dept_ministry = "";
                $("#add_dept_emp_state_code").html('');
                $("#add_dept_emp_state_code").html('<option value="">--Select--</option>');
                $.each(AddDeptMinistryRes, function (i, val) {
                    $.each(val, function (i, value) {
                        add_dept_ministry += '<option value="' + value + '">' + value + '</option>';
                    });
                });
                $("#add_dept_emp_state_code").append(add_dept_ministry);
            }
        });
    }
    else if(addDepartCategoryType === 'Central' || addDepartCategoryType === 'UT'){
        $.ajax({
            type: 'post',
            url: 'getCategoryMinistryHOG',
            data: {emp_category: addDepartCategoryType},
            success: function (data) {
                var AddDeptMinistryRes = JSON.parse(data.jsonStr);
                var add_dept_ministry = "";
                $("#add_dept_emp_min_state_org").html('');
                $("#add_dept_emp_min_state_org").html('<option value="">--Select--</option>');
                $.each(AddDeptMinistryRes, function (i, val) {
                    $.each(val, function (i, value) {
                        add_dept_ministry += '<option value="' + value + '">' + value + '</option>';
                    });
                });
                $("#add_dept_emp_min_state_org").append(add_dept_ministry);
            }
        });        
    }
});

$("#add-dept-rcd").click(function() {
    $('.text-danger').html('');
    $("#AddDeptRec input").val('');
    $('#AddDeptRec select').val('');
    $(".modal-title").html("Add New Department");
    $("#AddDepartRec").modal({backdrop: 'static', keyboard: false});
});

$('#addDept').click(function (e) {
    $('.text-danger').html('');
    e.preventDefault();

        $('.loader').show();
        var data = JSON.stringify($('#AddDeptRec').serializeObject());
        var add_dept_emp_category = $("#add_dept_emp_category").val();
        var add_dept_emp_min_state_org = $("#add_dept_emp_min_state_org").val();
        
        var add_dept_state_code = $("#add_dept_emp_state_code").val();
        var add_dept_state_dept = $("#add_dept_emp_state_dept").val();
        
        var add_dept_emp_dept = $("#add_dept_emp_dept").val();
        var emp_coord_email = $("#add_dept_emp_coord_email").val();
        var emp_coord_mobile = $("#add_dept_emp_coord_mobile").val();
        var emp_ip = $("#add_dept_emp_ip").val();
        //console.log("Form data Add Department::::" + data);
            $.ajax({
                type: 'POST',
                url: "addDepartmentHOG",
                data: {add_dept_emp_category: add_dept_emp_category,add_dept_emp_min_state_org:add_dept_emp_min_state_org,add_dept_emp_dept:add_dept_emp_dept,add_dept_state_code:add_dept_state_code,add_dept_state_dept:add_dept_state_dept,emp_coord_email:emp_coord_email,emp_coord_mobile:emp_coord_mobile,emp_ip:emp_ip},
                datatype: JSON,
                success: function (data) {
                        $('.loader').hide();
                        var addDeptfnlData = JSON.parse(data.jsonStr);
                        var addDeptfnlDataStatus = addDeptfnlData.status;
                        if(addDeptfnlData.emp_category === 'invalid'){
                            $(".add_dept_emp_category").html("<b>Please Select Category.</b>");
                            setTimeout(function(){ 
                                $(".add_dept_emp_category").html("");
                            }, 1500);                            
                        }
                        //Check Validation for add department (Case :: Central)
                        if(addDeptfnlData.add_dept_emp_min_state_org === 'invalid'){
                            $(".add_dept_emp_min_state_org").html("<b>Please Select Ministry/Organization.</b>");
                            setTimeout(function(){ 
                                $(".add_dept_emp_min_state_org").html("");
                            }, 1500);                                
                        }
                        if(addDeptfnlData.emp_dept === 'invalid'){
                            $(".add_dept_emp_dept").html("<b>Please Select Department/Division/Domain.</b>");
                            setTimeout(function(){ 
                                $(".add_dept_emp_dept").html("");
                            }, 1500);                                
                        }
                        //Check Validation for add department (Case :: State)
                        if(addDeptfnlData.add_dept_state_code === 'invalid'){
                            $(".add_dept_emp_state_code").html("<b>Please Select State.</b>");
                            setTimeout(function(){ 
                                $(".add_dept_emp_state_code").html("");
                            }, 1500);                                
                        }
                        if(addDeptfnlData.emp_dept === 'invalid'){
                            $(".add_dept_emp_state_dept").html("<b>Please Select Department.</b>");
                            setTimeout(function(){ 
                                $(".add_dept_emp_state_dept").html("");
                            }, 1500);                                
                        }
                       
                        if(addDeptfnlData.add_dept_emp_coord_email === 'invalid'){
                            $(".add_dept_emp_coord_email").html("<b>Please Enter Valid Email.</b>");
                            setTimeout(function(){ 
                                $(".add_dept_emp_coord_email").html("");
                            }, 1500);                                
                        }
                        if(addDeptfnlData.add_dept_emp_ip === 'invalid'){
                            $(".add_dept_emp_ip").html("<b>Please Enter Valid IP.</b>");
                            setTimeout(function(){ 
                                $(".add_dept_emp_ip").html("");
                            }, 1500);                                
                        }
                        if(addDeptfnlDataStatus === 'department inserted'){
                            $.each(addDeptfnlData, function (i, val) {
                                if(val === 'department inserted'){
                                    initializeCoordinatorTable();
                                    $(".msg-alert").addClass('alert alert-success');
                                    $(".msg-alert").html("<b>Department Added Successfully.</b>");
                                    $("#AddDepartRec").modal('hide');
                                    setTimeout(function(){ 
                                        $(".msg-alert").removeClass('alert alert-success');
                                        $(".msg-alert").html("");
                                    }, 1500);
                                }
                                else{
                                    console.log("This is error Case Add !!");
                                }
                            });                            
                        }
                },
                error: function () {
                     console.log("Something Went Wrong!!");
                }

            });
});    

$("#pend-dept-rcd").click(function() {
    $('.text-danger').html('');
    $(".modal-title").html("Pending Departments for Approval");
    $("#ViewPendDeptRec").modal({backdrop: 'static', keyboard: false});
});

$(document).ready(function() {
    fetchPendingDepartmentList();   
});    

function fetchPendingDepartmentList(){
        $.ajax({
        type: 'GET',
        url: 'showDepartmentPendingRequest',
        dataType: 'json',
        success: function (data) {
                var getpendingdeptData = JSON.parse(data.jsonStr);
                var getpendingdeptDataCount = getpendingdeptData['totalcount'];
                var getpendingdeptDataStatus = getpendingdeptData['status'];
                if(getpendingdeptDataCount > 0){
                    $("#pend-dept-rcd").show();
                    var peding_dept = "";
                    $.each(getpendingdeptDataStatus, function (i, val) {
                        peding_dept += '<tr><td>'+ val.emp_category +'</td><td>'+ val.emp_min_state_org +'</td><td>'+ val.emp_dept +'</td><td>'+ val.other_dept +'</td><td>'+ val.requested_by +'</td><td><div class="btn-group"><button class="btn btn-primary btn-xs green dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="true" title="Click to take appropriate actions">Actions<i class="fa fa-angle-down"></i></button><ul class="dropdown-menu dropdown-menu-right action-btn-list" role="menu" style="position:inherit;"><li title="Approve the request"><a href="javascript:void(0)" onclick=\"approveDepartmentByUserId(' + val.emp_id + ');\" ><i class="fa fa-check"></i> Approve</a></li><li title="Reject the request"><a href="javascript:void(0)" onclick=\"rejectDepartmentByUserId(' + val.emp_id + ');\"><i class="fa fa-ban"></i> Reject</a></li><li title="Reject the request"><a href="javascript:void(0)" onclick=\"editDepartmentByUserId(' + val.dept_id + ');\"><i class="fa fa-edit"></i> Edit</a></li></ul></div></td></tr>';
                    });
                    $("#ViewDeptPendBody").html('');
                    $("#ViewDeptPendData").dataTable().fnDestroy(); 
                    $("#ViewDeptPendBody").html(peding_dept);
                    setTimeout(function(){ 
                        $('#ViewDeptPendData').dataTable({
                            "pageLength": 10,
                            "searching" : true,
                            "responsive": true,
                            "columnDefs": [
                                        { responsivePriority: 1, targets: 0 },
                                        { responsivePriority: 2, targets: 5 },
                                    ]

                        });
                    }, 3000); 
                }else{
                   $("#pend-dept-rcd").hide(); 
                }
        }
    });
}

function rejectDepartmentByUserId(emp_id){
    $("#ViewPendDeptRec").modal('hide');
        bootbox.confirm({
        title: "Reject Department Request",
        message: "Are you sure? Do you want to reject this Request?",
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
            if(result === true) {
                $(".loader").show();
                $.ajax({
                    type: 'post',
                    url: 'rejectDepartment',
                    data: {id:emp_id},
                    success: function(data) {
                    var deptRejectReqfnldata =  JSON.parse(data.jsonStr);
                    $.each(deptRejectReqfnldata, function (i, val) {
                        if(val === 'Rejected Successfully') {
                            fetchPendingDepartmentList();
                            $(".msg-alert").addClass('alert alert-success');
                            $(".msg-alert").html("<b>Department Request Reject Successfully</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-success');
                                $(".msg-alert").html("");
                            }, 1500);
                        }else {
                            $(".msg-alert").addClass('alert alert-danger');
                            $(".msg-alert").html("<b>Something Went Wrong !!</b>");
                            setTimeout(function(){ 
                                $(".msg-alert").removeClass('alert alert-danger');
                                $(".msg-alert").html("");
                            }, 1500);
                        }
                    });
                    }, 
                    complete: function() {
                        $(".loader").hide();
                    }
                });
            }
            else if(result===false){
                $("#ViewPendDeptRec").modal({backdrop: 'static', keyboard: false});
            }
        }
    });    
}


function approveDepartmentByUserId(emp_id){
    $('.text-danger').html('');
    $("#ApproveDeptRec input").val('');
    //$("#ViewPendDeptRec").modal('hide');
    $('#approve_dept_emp_id').val(emp_id);
    $("#ApproveDepart .modal-title").html("Approve Department Request");
    $("#ApproveDepart").modal({backdrop: 'static', keyboard: false});
}


$('#approveDept').click(function (e) {
            $('.text-danger').html('');
            e.preventDefault()
            var data = JSON.stringify($('#ApproveDeptRec').serializeObject());
            var coordinator_email = $("#approve_dept_email").val();
            var emp_id = $("#approve_dept_emp_id").val();
            //console.log("Form data Add::::" + data);
            $.ajax({
                type: 'POST',
                url: "approveDepartment",
                data: {id: emp_id,coordinator_email:coordinator_email},
                datatype: JSON,
                success: function (data) {
                        var approvedfnlData = JSON.parse(data.jsonStr);
                        var approvedfnlDataStatus = approvedfnlData.status;
                        if(approvedfnlData.coordinator_email === 'invalid'){                            
                            $(".approve_dept_email").html("<b>Coordinator Email Not Valid.</b>");
                            setTimeout(function(){ 
                                $(".approve_dept_email").html("");
                            }, 2000);                              
                        }    
                        else if(approvedfnlDataStatus === 'notValid'){
                            $(".msg-mod-alert").addClass('alert alert-danger');
                            $(".msg-mod-alert").html("<b>Coordinator Email Not Valid.</b>");
                            setTimeout(function(){ 
                                $(".msg-mod-alert").removeClass('alert alert-danger');
                                $(".msg-mod-alert").html("");
                            }, 2000);                        
                        }
                        else{
                            $.each(approvedfnlData, function (i, val) {
                                if(val === 'Approved'){
                                    fetchPendingDepartmentList();
                                    $(".msg-alert").addClass('alert alert-success');
                                    $(".msg-alert").html("<b>Department Approved Successfully.</b>");
                                    $("#ApproveDepart").modal('hide');
                                    setTimeout(function(){ 
                                        $(".msg-alert").removeClass('alert alert-success');
                                        $(".msg-alert").html("");
                                    }, 1500);
                                }
                                else {
                                    $(".msg-alert").addClass('alert alert-danger');
                                    $(".msg-alert").html("<b>Something Went Wrong !!</b>");
                                    setTimeout(function(){ 
                                        $(".msg-alert").removeClass('alert alert-danger');
                                        $(".msg-alert").html("");
                                    }, 1500);
                                }
                            });                            
                        }
                },
                error: function () {
                     console.log("Something Went Wrong!!");
                }
            });           
});

function editDepartmentByUserId(dept_id){
    $('.loader').show();
    $('.text-danger').html('');
     $.ajax({
         type: 'GET',
         url: "viewDepartmentPendingRequestById",
         data: {dept_id:dept_id}, 
         success: function (data) {
            $('.loader').hide();
            //$("#ViewPendDeptRec").modal('hide');
            var viewDeptfnlData = JSON.parse(data.jsonStr);
            $.each(viewDeptfnlData, function (i, val) {
                $.each(val, function (i, value) {
                    $('#edit_dept_emp_coord_email').val(value.requested_by).prop('disabled', true);;
                    $('#edit_dept_id').val(value.dept_id);
                    if(value.emp_category === 'Central' || value.emp_category === 'UT'){
                        $('#edit_dept_emp_state_div').hide();
                        $('#edit_dept_emp_central_div').show();
                        $('#edit_dept_emp_dept').val(value.other_dept);
                        $('#edit_dept_emp_category').find('option').remove().end().append('<option value="'+value.emp_category+'">'+value.emp_category+'</option>').val(value.emp_category).prop('disabled', true);;
                        $('#edit_dept_emp_min_state_org').find('option').remove().end().append('<option value="'+value.emp_min_state_org+'">'+value.emp_min_state_org+'</option>').val(value.emp_min_state_org).prop('disabled', true);;
                    }
                    else if(value.emp_category === 'State'){
                        $('#edit_dept_emp_central_div').hide();
                        $('#edit_dept_emp_state_div').show();
                        $('#edit_dept_emp_state_dept').val(value.other_dept);
                        $('#edit_dept_emp_category').find('option').remove().end().append('<option value="'+value.emp_category+'">'+value.emp_category+'</option>').val(value.emp_category).prop('disabled', true);;
                        $('#edit_dept_emp_state_code').find('option').remove().end().append('<option value="'+value.emp_min_state_org+'">'+value.emp_min_state_org+'</option>').val(value.emp_min_state_org).prop('disabled', true);;
                    }
                });       
            });
            $("#editDepartRec .modal-title").html("Update Department");
            $("#editDepartRec").modal({backdrop: 'static', keyboard: false});
         },
         error: function () {
        	  console.log("Something Went Wrong!!");
         }
     
     });
}


$('#updateDept').click(function (e) {
        $('.text-danger').html('');
        e.preventDefault();
        $('.loader').show();
        var data = JSON.stringify($('#updateDeptRec').serializeObject());
        var edit_data_central = $("#edit_dept_emp_dept").val();
        var edit_data_state = $("#edit_dept_emp_state_dept").val();        
        var edit_data_category = $("#edit_dept_emp_category").val();
        
        var edit_dept_emp_dept = '';
        if(edit_data_category == 'Central'){
            edit_dept_emp_dept += edit_data_central;
        }
        else if(edit_data_category == 'State') {
            edit_dept_emp_dept += edit_data_state;
        }
        var dept_id = $("#edit_dept_id").val();
        //console.log("Form data Add Department::::" + data);
            $.ajax({
                type: 'POST',
                url: "updateDepartmentPendingRequestById",
                data: {other_dept: edit_dept_emp_dept,dept_id:dept_id},
                datatype: JSON,
                success: function (data) {
                        $('.loader').hide();
                        var updateDeptfnlData = JSON.parse(data.jsonStr);
                        if(updateDeptfnlData.other_dept === 'invalid'){
                            $(".edit_dept_emp_state_dept").html("<b>Please Enter Department.</b>");
                            setTimeout(function(){ 
                                $(".edit_dept_emp_state_dept").html("");
                            }, 1500);                                
                        }
                        else{
                            $.each(updateDeptfnlData, function (i, val) {
                                if(val === 'success'){
                                    fetchPendingDepartmentList();
                                    $(".msg-alert").addClass('alert alert-success');
                                    $(".msg-alert").html("<b>Department Updated Successfully.</b>");
                                    $("#editDepartRec").modal('hide');
                                    setTimeout(function(){ 
                                        $(".msg-alert").removeClass('alert alert-success');
                                        $(".msg-alert").html("");
                                    }, 1500);
                                }
                                else{
                                    console.log("This is error Case Update!!")
                                }
                            });                            
                        }

                },
                error: function () {
                     console.log("Something Went Wrong!!");
                }

            });
});    
//-------------------------------------------------Section 3 End--------------------------------------------------------------


//-----------------Begin :: Section A - Sub Part for Category,Ministry,Department change Category wise -----------------------

//Begin::DropDown Hide and Show
$("#emp_category").change(function () {
    if (this.value === 'State') {   
        $("#central_div").hide();
        $("#other_div").hide();
        $("#state_div").show();
    }
    else if(this.value === 'Psu'){
        $("#central_div").hide();
        $("#state_div").hide();
        $("#other_div").show();
    }  
    else if(this.value === 'Const'){
        $("#central_div").hide();
        $("#state_div").hide();
        $("#other_div").show();
    }  
    else if(this.value === 'Nkn'){
        $("#central_div").hide();
        $("#state_div").hide();
        $("#other_div").show();
    }
    else if(this.value === 'Project'){
        $("#central_div").hide();
        $("#state_div").hide();
        $("#other_div").show();
    }
    else if(this.value === 'UT'){
        $("#state_div").hide();
        $("#other_div").hide();
        $("#central_div").show();
    }
    else if(this.value === 'Others'){
        $("#central_div").hide();
        $("#state_div").hide();
        $("#other_div").show();
    }
    else {
        $("#state_div").hide();
        $("#other_div").hide();
        $("#central_div").show();
    }
});
//End::DropDown Hide and Show

//Begin::Get Category for Add Coordinator
$(document).ready(function() {
    $.ajax({
        type: 'GET',
        url: 'getCategoryHOG',
        dataType: 'json',
        success: function (data) {
                var AddCoordCatData = JSON.parse(data.jsonStr);
                var add_Coord_category = "";
                $("#emp_category").html('');
                $("#emp_category").html('<option value="">--Select--</option>');
                $.each(AddCoordCatData, function (i, val) {
                    $.each(val, function (i, value) {
                        add_Coord_category += '<option value="' + value + '">' + value + '</option>';
                    });
                });
                $("#emp_category").append(add_Coord_category);
        }
    });
});
//End::Get Category for Add Coordinator

$("#emp_category").change(function () {
    var orgTypeVal = $(this).val();
    if (orgTypeVal === 'State') {
        $.ajax({
            type: 'post',
            url: 'getCategoryMinistryHOG',
            data: {emp_category: orgTypeVal},
            success: function (data) {
                var AddCoordMinistryRes = JSON.parse(data.jsonStr);
                var ministry_data = "";
                $("#stateCode").html('');
                $("#stateCode").html('<option value="">-Select-</option>');
                $.each(AddCoordMinistryRes, function (i, val) {
                    $.each(val, function (i, value) {
                        ministry_data += '<option value="' + value + '">' + value + '</option>';
                    });
                });                
                $("#stateCode").append(ministry_data);
            }
        });
    }
    else if(orgTypeVal === 'Psu' || orgTypeVal === 'Const' || orgTypeVal === 'Nkn' || orgTypeVal === 'Project' || orgTypeVal === 'Others') {
        $.ajax({
            type: 'post',
            url: 'getCategoryMinistryHOG',
            data: {emp_category: orgTypeVal},
            success: function (data) {
                var AddCoordMinistryRes = JSON.parse(data.jsonStr);
                var ministry_data = "";
                $("#org").html('');
                $("#org").html('<option value="">-Select-</option>');
                $.each(AddCoordMinistryRes, function (i, val) {
                    $.each(val, function (i, value) {
                        ministry_data += '<option value="' + value + '">' + value + '</option>';
                    });
                });
                $("#org").append(ministry_data);
            }
        });
    } 
    else if(orgTypeVal === 'UT' || orgTypeVal === 'Central') {
        $.ajax({
            type: 'post',
            url: 'getCategoryMinistryHOG',
            data: {emp_category: orgTypeVal},
            success: function (data) {
                var AddCoordMinistryRes = JSON.parse(data.jsonStr);
                var ministry_data = "";
                $("#emp_min_state_org").html('');
                $("#emp_min_state_org").html('<option value="">-Select-</option>');
                $.each(AddCoordMinistryRes, function (i, val) {
                    $.each(val, function (i, value) {
                        ministry_data += '<option value="' + value + '">' + value + '</option>';
                    });
                });
                $("#emp_min_state_org").append(ministry_data);
            }
        });
    }
});

//Begin::On change of Ministry/Organization Provide Cetral Department AND Data Filter
$("#emp_min_state_org").change(function () {
    var orgTypeVal = $(this).val();
    var orgType = $("#emp_category").val();
    $.ajax({
        type: 'post',
        url: 'getCategoryMinistryDepartmentHOG',
        data: {emp_category: orgType,emp_min_state_org: orgTypeVal},
        success: function (data) {
            var AddCoordCentralDepartRes = JSON.parse(data.jsonStr);
            var ministry_dept_data = "";
            $("#emp_dept").html('');
            $("#emp_dept").html('<option value="">-Select-</option>');
            $.each(AddCoordCentralDepartRes, function (i, val) {
                $.each(val, function (i, value) {
                    ministry_dept_data += '<option value="' + value + '">' + value + '</option>';
                });
            });
            $("#emp_dept").append(ministry_dept_data);
        }
    });
});
//End::On change of Ministry/Organization Provide Cetral Department AND Data Filter


//Begin::On change of State Provide State Department AND Data Filter
$("#stateCode").change(function () {
    var orgTypeVal = $(this).val();
    var orgType = $("#emp_category").val();
    $.ajax({
        type: 'post',
        url: 'getCategoryMinistryDepartmentHOG',
        data: {emp_category: orgType,emp_min_state_org: orgTypeVal},
        success: function (data) {
            var AddCoordStateDepartRes = JSON.parse(data.jsonStr);
            var state_data = "";
            $("#state_dept").html('');
            $("#state_dept").html('<option value="">-Select-</option>');
            $.each(AddCoordStateDepartRes, function (i, val) {
                $.each(val, function (i, value) {
                    state_data += '<option value="' + value + '">' + value + '</option>';
                });
            });            
            
            $("#state_dept").append(state_data);
        }
    });
});
//End::On change of State Provide State Department AND Data Filter

//-----------------End :: Section A - Sub Part for Category,Ministry,Department change Category wise -----------------------



//--------------------------------- Begin :: Track Request ---------------------------------------

function track_frm(ref_no)
{
    $('.loader').show();
    //$("#ViewPendReqRec").modal('hide');
    //$("#ViewPendReqHOGRec").modal('hide');
    $('.order-tracker li').removeClass('active-track');
    $('.order-tracker li').removeClass('pending-track');
    $('.order-tracker li').removeClass('reject-track');
    $(".fa-caret-down").hide();
    $.ajax({
        url: "fetchTrackAll",
        type: "POST",
        data: {reg_no: ref_no},
        success: function (data) {
            $("#track-msg").html("");
            //console.log(data)
            var str = "";
            var ul_str = "";
            var value = data.hmTrack.msg.split('=>');
            $("#refnumbertrack").text(data.hmTrack.reg_no)
            $.each(value, function (i, val) {
                trc_list = val.split("->");
                if (trc_list[1]) {
                    str += "<tr><td><b>" + trc_list[0] + ":</b></td><td>" + trc_list[1] + "</td></tr>";
                }
            });
            $('#query_raise_hyperlink').attr('onclick', 'apply_heading("'+data.hmTrack.reg_no+'", "'+data.hmTrack.form_name+'", "query_raise", "");resetRandom();');
            //var counter=data.hmTrack.roles.length;
            var roles = data.hmTrack.roles.filter(function(v){return v!==''});
            //console.log(roles)
            //console.log(data.hmTrack.status)
             var oldvalue;
             var old_forward_by;
            $.each(roles, function (i, val) {
                var newval = val.split('=>');
                var forword_to = newval[1];
                //console.log(val)
                val = newval[0];
                if(i == 0){
                    old_forward_by = "";
                }else{
                    old_forward_by = oldvalue[0];
                }
                
                console.log("Old Sender: "+old_forward_by)
                
                if (val === 'a') {
                    ul_str += "<li class=\"a-track\"><div class=\"li-track-desc\">User</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 'c') {
                    ul_str += "<li class=\"c-track\"><div class=\"li-track-desc\">CO</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 'ca') {
                    ul_str += "<li class=\"ca-track\"><div class=\"li-track-desc\">RO/FO/Nodal</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 'h') {
                    ul_str += "<li class=\"h-track\"><div class=\"li-track-desc\">HOG</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 's') {
                    ul_str += "<li class=\"s-track\"><div class=\"li-track-desc\">Support</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 'm') {
                    ul_str += "<li class=\"m-track\"><div class=\"li-track-desc\">Admin</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 'd') {
                    ul_str += "<li class=\"d-track\"><div class=\"li-track-desc\">DA Admin</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 'us') {
                    ul_str += "<li class=\"us-track\"><div class=\"li-track-desc\">US/JS/AS/S</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                $('ul.order-tracker').html(ul_str);
                setTimeout(function () {
                    var width = (100 / roles.length);
                    $('ul.order-tracker li').css('width', width + "%");
                    $('.' + val + '-track span.cercle-order');
                    $("ul.order-tracker li:last-child").addClass(data.hmTrack.status + '-track curr-track');
                    $("ul.order-tracker li:not(:last-child)").addClass('active-track');
                    
                    $(".cercle-order").click(function () {
                        $(".fa-caret-down").hide();
                        $(this).closest('.li-tracker').find('.fa-caret-down').show();
                    });
                }, 100)
                oldvalue = newval;
            });

            $("#track-msg").html("<table class='table table-bordered'>" + str + "</table>")
            $("#user_data_tbody").html('<tr class="table-success"><td>' + data.hmTrack.name + '</td><td>' + data.hmTrack.email + '</td><td>' + data.hmTrack.mob + '</td><td>' + data.hmTrack.forword_date + '</td></tr>')
            $('#basic_track').modal({backdrop: 'static', keyboard: false});
        },
        error: function (data) {
            var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    var error = JSON.parse(jsonvalue.responseText.substring(0,jsonvalue.responseText.indexOf("}")+1)).error;
                    $('#new_alert .modal-body').html(error);
                    $('#new_alert').modal('show');
            $('.loader').hide();
//            alert('Error: coming from fetchTrackAll Error from server side');
        },
        complete: function(){
            $('.loader').hide();
        }
    }); 
}

function fetchTrackByRole(forward_by, state, trole_txt, regno) {
    //$('.loader').hide();
    $.ajax({
        url: "fetchTrackByRole",
        type: "POST",
        data: {srole: state, reg_no: regno, trole: trole_txt, forward: forward_by},
        success: function (data) {
            console.log(data)
            $("#track-msg").html("");
            var str = "";
            var value = data.hmTrack.msg.split('=>');
            $.each(value, function (i, val) {
                trc_list = val.split("->");
                if (trc_list[1]) {
                    console.log(trc_list[1])
                    str += "<tr><td><b>" + trc_list[0] + ":</b></td><td>" + trc_list[1] + "</td></tr>";
                }
            });

            $("#track-msg").html("<table class='table table-bordered'>" + str + "</table>")
        },
        error: function (data) 
        {
        var jsonvalue = JSON.parse(myJSON);
                    var error = JSON.parse(jsonvalue.responseText.substring(0,jsonvalue.responseText.indexOf("}")+1)).error;
                    $('#new_alert .modal-body').html(error);
                    $('#new_alert').modal('show');
         $('.loader').hide();
//            alert('Error: coming from fetchTracbyrole Error from server side');
        },
        complete: function() {
            $('.loader').hide();
        }
    });

}

//setTimeout(function () {
    $('[data-counter="counterup"]').each(function() {
      var $this = $(this),
      countTo = $this.attr('data-value');
      $({ countNum: $this.text()}).animate({
        countNum: countTo
      },
      {
        duration: 1000,
        easing:'linear',
        step: function() {
          $this.text(Math.floor(this.countNum));
        },
        complete: function() {
          $this.text(this.countNum);
        }
      });  
    });
//}, 1500);


//--------------------------------- End :: Track Request ---------------------------------------