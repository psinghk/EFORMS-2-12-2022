<%@page import="com.org.bean.HodData"%>
<%@page import="com.org.service.ProfileService"%>
<%@page import="com.org.bean.ImportantData"%>
<%@page import="com.org.bean.UserData"%>
<%@page import="java.util.ArrayList"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page import="org.owasp.esapi.ESAPI"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Expires", "0");
    response.setDateHeader("Expires", -1);
    response.setHeader("X-Frame-Options", "DENY");
    response.addHeader("X-Content-Type-Options", "nosniff");
    response.addHeader("X-XSS-Protection", "1; mode=block");
%>
<%  ProfileService profileService = new ProfileService();
    Map hodDetails = new HashMap();
    System.out.println("hod bo" + hodDetails.get("bo"));
    UserData userdata = (UserData) session.getAttribute("uservalues");
    HodData hoddata = (HodData) userdata.getHodData();
    String otp_type = userdata.getVerifiedOtp();
    boolean nic_employee = userdata.isIsNICEmployee();
    boolean ldap_employee = userdata.isIsEmailValidated();
    boolean isNewUser = userdata.isIsNewUser();
    hodDetails = (HashMap) profileService.getHODdetails(hoddata.getEmail());
%>
<jsp:include page="include/new_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<jsp:include page="include/new_include/header.jsp" />
<link href="assets/old_assets/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet" type="text/css" />
<!-- Start Add Datatable button css -->
<link  href="assets/demo/default/base/assets_datatable/css/buttons.dataTables.min.css" rel="stylesheet"  />
<!-- End Datatable button css -->
<style type="text/css">
.dataTables_length, .dataTables_filter {
    margin-left: 10px;
    float: right;
}
</style>
<!-- begin:: Content -->
<div class="k-content	k-grid__item k-grid__item--fluid k-grid k-grid--hor" id="k_content">
    <!--${ val.coordinator_list }-->
    <!-- begin:: Content Head -->
    <div class="k-content__head	k-grid__item">
        <div class="k-content__head-main">
            <h3 class="k-content__head-title">eForms</h3>
            <div class="k-content__head-breadcrumbs">
                <a href="#" class="k-content__head-breadcrumb-home"><i class="flaticon2-shelter"></i></a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link">Dashboards</a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link">Dashboard Stats</a>

                <!-- <span class="k-content__head-breadcrumb-link k-content__head-breadcrumb-link--active">Active link</span> -->
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile graph-dashboard">
            <!-- END PAGE BREADCRUMBS -->
            <!-- BEGIN PAGE CONTENT INNER -->
            <div class="page-content-inner graph-page">
                <div class="k-portlet__head">
                    <div class="k-portlet__head-label">
                        <h3 class="k-portlet__head-title">Filter Statistics</h3>
                    </div>
                </div>

                <div class="col-md-12 mt-5 mb-5">
                    <div class="row form-group">
                        <div class="col-md-4">
                            <label>Organization Category <span style="color: red">*</span></label>                                                                                    
                                <select class="form-control" id="user_employment" name="user_employment">
                                    <option value="" disabled="" selected="">-SELECT-</option>                
                                    <option value="Central">Central</option>
                                    <option value="Const">Const</option>
                                    <option value="Nkn">Nkn</option>
                                    <option value="Other">Other</option>
                                    <option value="Others">Others</option>
                                    <option value="Project">Project</option>
                                    <option value="Psu">Psu</option>
                                    <option value="State">State</option>
                                    <option value="UT">UT</option>
                                </select>
                                <font style="color:red"><span id="useremployment_error"></span></font>
                            </div>
                            <div id="central_div" class="col-md-8">
                                <div class="row">
                                    <div class="col-md-6">
                                        <label for="street">Ministry/Organization <span style="color: red">*</span></label>
                                        <select class='form-control' name='min' id='minitry'>
                                            <option value=''>-SELECT-</option>
                                        </select>
                                        <font style="color:red"><span id="minerror"></span></font>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="street">Department/Division/Domain <span style="color: red">*</span></label>
                                        <select class='form-control' name='dept' id='dept' >
                                            <option value=''>-SELECT-</option>
                                        </select>   
                                        <font style="color:red"><span id="deperror"></span></font>                                                                                        
                                    </div>
                                </div>
                            </div>
                            <div id="state_div" class="col-md-8" style="display:none">
                                <div class="row">
                                    <div class="col-md-6">
                                        <label for="street">State <span style="color: red">*</span></label>
                                        <select class='form-control' id="stateCode" name="stateCode">
                                            <option value=''>-SELECT-</option>
                                        </select>
                                        <font style="color:red"><span id="state_error"></span></font>                                                                                                                                                                     
                                    </div>
                                    <div class="col-md-6">
                                        <label for="street">Department <span style="color: red">*</span></label>
                                        <select class='form-control' name="state_dept" id="state_dept">
                                            <option value=''>-SELECT-</option>
                                        </select>       
                                        <font style="color:red"><span id="smierror"></span></font>                                                                                        
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row mt-5 mb-5">
                            <div class="col-md-12">
                                <table class="table table-striped table-bordered table-hover" id="example1">
                                    <thead>
                                        <tr>
                                            <th> S.NO. </th>
                                            <th> Category </th>
                                            <th id="repMinistry"> Ministry </th>
                                            <th> Department </th>
                                            <th> Name </th>
                                            <th> Email </th>
                                        </tr>
                                    </thead>
                                    <tbody id="bulk_users_view_tbody">
                                        <s:iterator value="val.coordinator_list" var="eachUser" status="incr">
                                            <tr>
                                                <td><s:property  value="%{#incr.index+1}"/></td>
                                                <td><s:if test="%{#eachUser[1] != 'null'}"><s:property  value="#eachUser[1]"/></s:if></td>
                                                <td><s:if test="%{#eachUser[2] != 'null'}"><s:property  value="#eachUser[2]"/></s:if></td>
                                                <td><s:if test="%{#eachUser[3] != 'null'}"><s:property  value="#eachUser[3]"/></s:if></td>
                                                <td><s:if test="%{#eachUser[5] != 'null'}"><s:property  value="#eachUser[5]"/></s:if></td>
                                                <td><s:if test="%{#eachUser[6] != 'null'}"><s:property  value="#eachUser[6]"/></s:if></td>
                                            </tr>
                                        </s:iterator>                          
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>   
                </div>
            </div>
        </div>
    </div>
<!-- Start footer-->
<jsp:include page="include/new_include/footer.jsp" />
<!-- Start Add datable button JS -->
<script src="assets/demo/default/base/assets_datatable/js/dataTables.buttons.min.js" type="text/javascript"></script>
<script src="assets/demo/default/base/assets_datatable/js/buttons.flash.min.js" type="text/javascript"></script>
<script src="assets/demo/default/base/assets_datatable/js/jszip.min.js" type="text/javascript"></script>
<script src="assets/demo/default/base/assets_datatable/js/pdfmake.min.js" type="text/javascript"></script>
<script src="assets/demo/default/base/assets_datatable/js/buttons.html5.min.js" type="text/javascript"></script>
<script src="assets/demo/default/base/assets_datatable/js/buttons.print.min.js" type="text/javascript"></script>
<script src="assets/demo/default/base/assets_datatable/js/vfs_fonts.js" type="text/javascript"></script>
<!-- End Add datable button JS -->


<script>
    //Begin::DataTable
    $(document).ready(function () {
        var table = $('#example1').DataTable({
            dom: 'Blfrtip',
            aLengthMenu: [
                [25, 50, 100, 200, -1],
                [25, 50, 100, 200, "All"]
            ],
            'iDisplayLength': 100,
            buttons: ['excel', {extend: 'pdf',footer: 'true',text: 'pdf',orientation: 'landscape',pageSize: 'A4',title:'CoordinatorList'  }]
        });
    });
    //End::DataTable

    //Begin::DropDown Hide and Show
    $("#user_employment").change(function () {
        if (this.value === 'State') {
            $("#repMinistry").empty();
            $("#central_div").hide();
            $("#state_div").show();
            $("#repMinistry").append('<th>State</th>');
        } else {
            $("#repMinistry").empty();
            $("#state_div").hide();
            $("#central_div").show();
            $("#repMinistry").append('<th>Ministry</th>');
        }
    });
    //End::DropDown Hide and Show
    
    $("#user_employment").change(function () {
        var orgTypeVal = $(this).val();
        if (orgTypeVal === 'State') {
            $.ajax({
                type: 'post',
                url: 'centralMinistry',
                data: {orgType: orgTypeVal},
                success: function (data) {
                    var ministry_data = "";
                    $("#stateCode").html('');
                    $("#stateCode").html('<option value="">-Select State-</option>');
                    $.each(data, function (index, value) {
                        ministry_data += '<option value="' + value + '">' + value + '</option>';
                    });
                    $("#stateCode").append(ministry_data);
                    $.ajax({
                       type: 'post',
                       url: 'fetchCoordinator-list',
                       data: {orgType: orgTypeVal},
                       success: function(data) {
                            var itemData = data.val.coordinator_list;
                            filter_category(itemData);
                       }
                    });
                }
            });
        } else {
            $.ajax({
                type: 'post',
                url: 'centralMinistry',
                data: {orgType: orgTypeVal},
                success: function (data) {
                    var ministry_data = "";
                    $("#minitry").html('');
                    $("#minitry").html('<option value="">-Select Central Ministry-</option>');
                    $.each(data, function (index, value) {
                        ministry_data += '<option value="' + value + '">' + value + '</option>';
                    });
                    $("#minitry").append(ministry_data);
                    $.ajax({
                       type: 'post',
                       url: 'fetchCoordinator-list',
                       data: {orgType: orgTypeVal},
                       success: function(data) {
                            var itemData = data.val.coordinator_list;
                            filter_category(itemData);
                       }
                    });
                }
            });
        }

    });
    
    //Begin::Filter by Category (Cenral OR State)
    function filter_category(itemData){
        var re_datatable_data = "";
        var cnt =  0;
        $('#example1').DataTable().destroy();
        $('#bulk_users_view_tbody').empty();
        $.each(itemData, function (index, value) {
            cnt =  cnt + 1;
            re_datatable_data += '<tr><td>' + cnt + '</td><td>' + value[1] + '</td><td>' + value[2] + '</td><td>' + value[3] + '</td><td>' + (value[5] !== null && value[5] !== 'null' ? value[5] : '') + '</td><td>' + value[6] + '</td></tr>';
        }); 
        $("#bulk_users_view_tbody").append(re_datatable_data);
        $('#example1').DataTable({
            dom: 'Blfrtip',
            aLengthMenu: [
                [25, 50, 100, 200, -1],
                [25, 50, 100, 200, "All"]
            ],
            'iDisplayLength': 100,
            buttons: ['excel', {extend: 'pdf',footer: 'true',text: 'pdf',orientation: 'landscape',pageSize: 'A4',title:'CoordinatorList'  }]
        }).draw();
    }
    //End::Filter by Category (Cenral OR State)
    
    //Begin::On change of Ministry/Organization Provide Cetral Department AND Data Filter
    $("#minitry").change(function () {
        var orgTypeVal = $(this).val();
        var orgType = $("#user_employment").val();
        $.ajax({
            type: 'post',
            url: 'centralDepartment',
            data: {depType: orgTypeVal},
            success: function (data) {
                var ministry_data = "";
                $("#dept").html('');
                $("#dept").html('<option value="">-Select Central Department-</option>');
                $.each(data, function (index, value) {
                    ministry_data += '<option value="' + value + '">' + value + '</option>';
                });
                $("#dept").append(ministry_data);
                $.ajax({
                    type: 'post',
                    url: 'fetchCoordinator-list',
                    data: {orgType: orgType, depType: orgTypeVal},
                    success: function(data) {
                        var itemData = data.val.coordinator_list;
                        filter_category(itemData);
                    }
                });
            }
        });
    });
    //End::On change of Ministry/Organization Provide Cetral Department AND Data Filter

    //Begin::On change of State Provide State Department AND Data Filter
    $("#stateCode").change(function () {
        var orgTypeVal = $(this).val();
        var orgType = $("#user_employment").val();
        $.ajax({
            type: 'post',
            url: 'centralDepartment',
            data: {depType: orgTypeVal},
            success: function (data) {
                var state_data = "";
                $("#state_dept").html('');
                $("#state_dept").html('<option value="">-Select State Department-</option>');
                $.each(data, function (index, value) {
                    state_data += '<option value="' + value + '">' + value + '</option>';
                });
                $("#state_dept").append(state_data);
                $.ajax({
                    type: 'post',
                    url: 'fetchCoordinator-list',
                    data: {orgType: orgType, depType: orgTypeVal},
                    success: function(data) {
                        var itemData = data.val.coordinator_list;
                        filter_category(itemData);
                    }
                });
            }
        });
    });
    //End::On change of State Provide State Department AND Data Filter
    
    //Begin::On Change of State Provide Data Filter
    $("#state_dept").change(function () {
        var orgTypeVal = $(this).val();
        var orgType = $("#user_employment").val();
        var orgDeptVal = $("#stateCode").val();
        $.ajax({
            type: 'post',
            url: 'fetchCoordinator-list',
            data: {orgType: orgType, depType: orgTypeVal, orgDept:orgDeptVal},
            success: function(data) {
                var itemData = data.val.coordinator_list;
                filter_category(itemData);
            }
        });
    });
    //End::On Change of State Provide Data Filter
    
    //Begin::On Change of Central Department Provide Data Filter
    $("#dept").change(function () {
        var orgTypeVal = $(this).val();
        var orgType = $("#user_employment").val();
        var orgDeptVal = $("#minitry").val();
        console.log(orgTypeVal+''+orgType+''+orgDeptVal);
        $.ajax({
            type: 'post',
            url: 'fetchCoordinator-list',
            data: {orgType: orgType, depType: orgTypeVal, orgDept:orgDeptVal},
            success: function(data) {
                var itemData = data.val.coordinator_list;
                filter_category(itemData);
            }
        });
    });
    //End::On Change of Central Department Provide Data Filter
</script>

    </body>
</html>

