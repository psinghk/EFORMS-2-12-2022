<%-- 
    Document   : dashboard
    Created on : 28 May, 2018, 11:49:44 AM
    Author     : DJ
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib  uri="/struts-tags" prefix="s"%>
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
<jsp:include page="include/new_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<jsp:include page="include/new_include/header.jsp" />
<link href="assets/old_assets/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet" type="text/css" />
<!-- begin:: Content -->
<div class="k-content	k-grid__item k-grid__item--fluid k-grid k-grid--hor" id="k_content">

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
                <div class="col-md-9 m-auto">

                    <div class="row mt-5 mb-5">
                        <div class="col-md-3 mt-3">
                            <div class="col-sm-offset-1">
                                <div class="radio filterforsearch" id="checkall" style="display: inline-block;">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="filter" id="all"   value="all" checked> All
                                        <span></span>
                                    </label>&emsp;&emsp;&emsp;
                                </div>
                                <div class="radio filterforsearch" id="checkform" style="display: inline-block;">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="filter" id="formwise"  value="form" > Form-Wise
                                        <span></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3" id="all-req">
                            <select class="bs-select form-control sel" id="select">
                                <option value="select">--Select--</option>
                                <option value="total">Total Request</option>
                                <option value="pending">Pending Request</option>
                                <option value="rejected">Rejected Request</option>
                                <option value="completed">Completed Request</option>
                            </select>
                            <font style="color:red"><span id="select_error"></span></font>
                        </div>    
                        <div class="col-md-4">
                            <div class="input-group" id="defaultrange">
                                <input type="text" id="daterange" class="form-control" placeholder="Please select any date range (Optional)" readonly="">
                                <span class="input-group-btn">
                                    <button class="btn default date-range-toggle" type="button">
                                        <i class="fa fa-calendar"></i>
                                    </button>
                                </span>
                            </div>
                            <font style="color:red"><span id="date_error"></span></font>
                        </div>
                        <div class="col-md-1">
                            <button type="submit" class="btn btn-primary red filter"><i class="fa fa-filter"></i> Filter</button>
                        </div>
                    </div>
                </div>
                <div class="col-md-12 mt-5 mb-5">

                    <div class="row">
                        <div class="col-3">
                            <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                                <div class="k-portlet__body k-portlet__body--fluid">
                                    <div class="k-widget-3 k-widget-3--info" style="background: #304a9e;">
                                        <div class="k-widget-3__content dashtab">
                                            <div class="k-widget-3__content-info">
                                                <div class="k-widget-3__content-section req_status" id="total" style="cursor: pointer;">
                                                    <div class="k-widget-3__content-title">Total Requests</div>
                                                    <div class="k-widget-3__content-desc">TOTAL USER REQUESTS</div>
                                                </div>
                                                <div class="k-widget-3__content-section">
                                                    <span class="k-widget-3__content-number" data-counter="counterup" id="1" data-value="<s:property value="listBeanObj.totalRequest"/>">0</span>
                                                    <span  id="totalCount"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--begin::Portlet-->

                            <!--end::Portlet-->
                        </div>

                        <div class="col-3">

                            <!--begin::Portlet-->
                            <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                                <div class="k-portlet__body k-portlet__body--fluid">
                                    <div class="k-widget-3 k-widget-3--danger">
                                        <div class="k-widget-3__content dashtab">
                                            <div class="k-widget-3__content-info req_status" id="pending" style="cursor: pointer;">
                                                <div class="k-widget-3__content-section">
                                                    <div class="k-widget-3__content-title">Pending Request</div>
                                                    <div class="k-widget-3__content-desc">TOTAL PENDING REQUEST</div>
                                                </div>
                                                <div class="k-widget-3__content-section">
                                                    <span class="k-widget-3__content-number" data-counter="counterup" id="2" data-value="<s:property value="listBeanObj.pendingRequest"/>">0</span>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!--end::Portlet-->
                        </div>

                        <div class="col-3">

                            <!--begin::Portlet-->
                            <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                                <div class="k-portlet__body k-portlet__body--fluid">
                                    <div class="k-widget-3 k-widget-3--warning">
                                        <div class="k-widget-3__content dashtab">
                                            <div class="k-widget-3__content-info req_status" id="rejected" style="cursor: pointer;">
                                                <div class="k-widget-3__content-section">
                                                    <div class="k-widget-3__content-title">Rejected Request</div>
                                                    <div class="k-widget-3__content-desc">TOTAL REJECTED REQUEST</div>
                                                </div>
                                                <div class="k-widget-3__content-section">
                                                    <span class="k-widget-3__content-number" data-counter="counterup" id="3" data-value="<s:property value="listBeanObj.rejectedRequest"/>"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!--end::Portlet-->
                        </div>

                        <div class="col-3">

                            <!--begin::Portlet-->
                            <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                                <div class="k-portlet__body k-portlet__body--fluid">
                                    <div class="k-widget-3 k-widget-3--success">
                                        <div class="k-widget-3__content dashtab">
                                            <div class="k-widget-3__content-info req_status" id="completed" style="cursor: pointer;">
                                                <div class="k-widget-3__content-section">
                                                    <div class="k-widget-3__content-title">Completed Request</div>
                                                    <div class="k-widget-3__content-desc">TOTAL COMPLETES REQUESTS</div>
                                                </div>
                                                <div class="k-widget-3__content-section">
                                                    <span class="k-widget-3__content-number" data-counter="counterup" id="4" data-value="<s:property value="listBeanObj.completeRequest"/>">0</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!--end::Portlet-->
                        </div>
                    </div>
                </div>
                <div class="col-md-12 mb-5">
                    <div class="row extra" style="display: none">
                        <div class="col-2"><div class="portlet light "><div class="portlet-title"><div class="number"><h5 class="font-red-haze"><span  id="11"></span></h5></div></div></div></div>
                        <div class="col-2"><div class="portlet light "><div class="portlet-title"><div class="number"><h5 class="font-red-haze"><span  id="12"></span></h5></div></div></div></div>
                        <div class="col-2"><div class="portlet light "><div class="portlet-title"><div class="number"><h5 class="font-red-haze"><span  id="13"></span></h5></div></div></div></div>
                        <div class="col-2"><div class="portlet light "><div class="portlet-title"><div class="number"><h5 class="font-red-haze"><span  id="14"></span></h5></div></div></div></div>
                        <div class="col-2"><div class="portlet light "><div class="portlet-title"><div class="number"><h5 class="font-red-haze"><span  id="15"></span></h5></div></div></div></div>
                        <div class="col-2"><div class="portlet light "><div class="portlet-title"><div class="number"><h5 class="font-red-haze"><span  id="16"></span></h5></div></div></div></div>
                    </div>
                </div>
                <center> <img src="assets/old_assets/images/loadings.gif" id="chart_load" style="height:15px; display: none"> </center>
                <div class="col-md-12 mt-5 mb-5">
                    <div class="row">
                        <div class="col-md-12 req123">
                            <!-- BEGIN CHART PORTLET-->
                            <div class="portlet light ">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="icon-bar-chart font-green-haze"></i>
                                        <span class="caption-subject bold uppercase font-green-haze" id="rcd"></span>
                                        <span class="caption-helper"></span>
                                    </div>
                                    <div class="tools"></div>
                                </div>
                                <div class="portlet-body"><div id="chart_5" class="chart" style="height: 400px;"> </div></div>
                            </div>
                            <!-- END CHART PORTLET-->
                        </div>
                        <div id="test" class="col-md-6" style="padding: 0;"></div>
                    </div>
                </div>   
            </div>
        </div>
    </div>
</div>
<!-- Start footer-->
<jsp:include page="include/new_include/footer.jsp" />
<!-- End footer-->
<!-- END QUICK NAV -->
<div class="modal fade" id="modal_id" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">Ã—</button>
                <h4 class="modal-title"><b>Modal Title goes here</b></h4>
            </div>
            <div class="modal-body">                                                                                               
                <!--- content goes here -->
                Modal Content goes here.
            </div>
            <div class="modal-footer">
                <!-- footer goes here -->   
                Modal footer or any action goes here.                             
            </div>
        </div>
    </div>
</div>


<script src="assets/old_assets/plugins/jquery.min.js" type="text/javascript"></script>
<script src="assets/old_assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="assets/old_assets/plugins/moment.min.js" type="text/javascript"></script>
<script src="assets/old_assets/scripts/datatable.js" type="text/javascript"></script>
<script src="assets/old_assets/plugins/datatables/datatables.min.js" type="text/javascript"></script>
<script src="assets/old_assets/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js" type="text/javascript"></script>
<script src="assets/old_assets/plugins/bootstrap-daterangepicker/daterangepicker.min.js" type="text/javascript"></script>
<script src="assets/old_assets/plugins/amcharts/amcharts/amcharts.js" type="text/javascript"></script>
<script src="assets/old_assets/plugins/amcharts/amcharts/serial.js" type="text/javascript"></script>
<!-- For Bar chart loader -->
<script src="assets/old_assets/plugins/amcharts/amstockcharts/plugins/dataloader/dataloader.min.js" type="text/javascript"></script>
<script src="main_js/dashboard.js" type="text/javascript"></script>
<script src="assets/old_assets/plugins/counterup/jquery.waypoints.min.js" type="text/javascript"></script>
<script src="assets/old_assets/plugins/counterup/jquery.counterup.min.js" type="text/javascript"></script>
<script src="assets/old_assets/scripts/app.min.js" type="text/javascript"></script>
<script src="assets/old_assets/pages/scripts/components-date-time-pickers.min.js" type="text/javascript"></script>
<script src="assets/old_assets/scripts/table-datatables-managed.min.js" type="text/javascript"></script>
<script>
    $(document).ready(function () {
        $(document).on('click', '.caction', function () {
            $('#modal_id').modal();
        });
    });
</script>
</body>
</html>
