<%--
    Document   : Admin Request
    Created on : Oct 17, 2019, 12:31:57 AM
    Author     : Avinash
--%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<jsp:include page="include/new_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<jsp:include page="include/new_include/header.jsp" />
<div class="k-content	k-grid__item k-grid__item--fluid k-grid k-grid--hor" id="k_content">
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="row fillter-div-max-tabs">
            <s:iterator value="%{#session.adminFormCo}">
                <div class="col-3">
                    <a class="filter-a formUpdate" href="javascript:void(0);" id="<s:property />" data-value="formname" >
                        <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                            <div class="k-portlet__body k-portlet__body--fluid">
                                <div class="k-widget-3 k-widget-3--info" style="background: #303F9F;">
                                    <div class="k-widget-3__content dashtab">
                                        <div class="k-widget-3__content-info">
                                            <div class="k-widget-3__content-section">
                                                <div class="k-widget-3__content-title" style="text-transform: capitalize"><s:property /> Form Co-ordinators</div>
                                                <div class="k-widget-3__content-desc"></div>
                                            </div>
                                            <div class="k-widget-3__content-section">

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
            </s:iterator>
            <div class="col-3 display-hide">
                <!--begin::Portlet-->
                <a class="filter-a formUpdate" href="javascript:void(0);" data-value="update_ministry">
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                            <div class="k-portlet__body k-portlet__body--fluid">
                                <div class="k-widget-3 k-widget-3--warning">
                                    <div class="k-widget-3__content dashtab">
                                        <div class="k-widget-3__content-info">
                                            <div class="k-widget-3__content-section">
                                                <div class="k-widget-3__content-title" style="text-transform: capitalize">Update Ministry</div>
                                                <div class="k-widget-3__content-desc"></div>
                                            </div>
                                            <div class="k-widget-3__content-section">

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                </a>
            </div>
            <div class="col-3 display-hide">
                <a class="filter-a formUpdate" href="javascript:void(0);" data-value="update_dept">
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--danger" style="background: #d32f2f;">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">
                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title" style="text-transform: capitalize">Update Department</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>
                                        <div class="k-widget-3__content-section">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>

        </div>
        <div id="Co-data" class="row display-hide">
            <div class="col-lg-12 col-xl-12 order-lg-2 order-xl-1">
                <input type="hidden" id="formname" />
                <!--begin::Portlet-->
                <div class="k-portlet k-portlet--height-fluid k-widget-17">
                    <div class="k-portlet__head">
                        <div class="k-portlet__head-label">
                            <h3 class="k-portlet__head-title" id="admin_request_heading">Co-ordinator list</h3>
                        </div>

                    </div>
                    <div class="k-portlet__body">
                        <div class="col-12 d-table pl-0"><span class="btn btn-success mb-4" id="add_reqs">Add Requests</span></div>
                        <div class="tabData">
<!--                        <table class="table table-striped table-bordered table-hover table-checkable order-column dt-responsive nowrap" id="example">
                            <thead>
                                <tr>                                                                                                  
                                    <th>Category</th>
                                    <th>Ministry</th>
                                    <th>Department</th>
                                    <th id="example_domain">Domain</th>
                                    <th>Co-ordinator Name</th>
                                    <th>Co-ordinator Email</th>
                                    <th>Co-ordinator Mobile</th>
                                    <th>Admin Email</th>
                                    <th>Allowed Ip</th>
                                    <th width="10%">Update Action</th>
                                </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>-->
                        </div>
                    </div>
                </div>
                <!-- END EXAMPLE TABLE PORTLET-->
            </div>
        </div>
    </div>

</div>
<jsp:include page="include/new_include/footer.jsp" />
<script src="main_js/updateCO.js" type="text/javascript"></script>
<!-- ADD Modal -->
<div id="add_reqs_modal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-lg">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Add Coordinator</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="add_co_form">
                    <div class="col-md-12">
                        <div class="row form-group">
                            <div class="col-md-4">
                                <label for="select_cat">Organization Category<span style="color: red">*</span></label>
                                <select name="user_employment" id="user_employment" class="form-control">
                                    <option value="">--Select--</option>
                                    <option value="Central">Central</option>
                                    <option value="State">State</option>
                                    <option value="Psu">PSU</option>
                                    <option value="Const">Constitutional Bodies</option>
                                    <option value="Nkn">Nkn Institutes</option>
                                    <option value="Project">Project</option>
                                     <option value="UT">Union Territory</option>
                                    <option value="Others">Others</option>
                                </select>
                                <font style="color:red"><span id="useremployment_error"></span></font>
                            </div>
                            <div id="min_state_org_div" class="col-md-8">
                                <label id="min_state_org_label" for="select_ministry">Ministry/Organization<span style="color: red">*</span></label>
                                <select name="min" id="min" class="form-control">
                                    <option value=''>-SELECT-</option>
                                </select>
                                <font style="color:red"><span id="minerror"></span></font>
                            </div>                        
                        </div>
                        <div id="dept_div"  class="row form-group">
                            <div class="col-md-12">
                                <label for="select_dept">Department/Division/Domain<span id="select_dept_mandate" style="color: red">*</span></label>
                                <select name="dept" id="dept" class="form-control">
                                    <option value=''>-SELECT-</option>
                                </select>
                                <font style="color:red"><span id="deperror"></span></font>         
                            </div>                        
                        </div>
                        <div id="other_text_div" class="row form-group display-hide">
                            <div class="col-md-12 ">
                                <div>
                                    <label>Other <span style="color: red">*</span></label>
                                    <input type="text" id="other_dept" name="other_dept" placeholder="Enter Department Name [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]" maxlength="100" class="form-control" value="" /> 
                                    <font style="color:red"><span id="other_dep_error"></span></font>
                                </div> 
                            </div>
                        </div>
                        <div class="row form-group">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="">Co-ordinator Email<span style="color: red">*</span></label>
                                    <input type="text" name="ca_email" id="ca_email" class="form-control">
                                    <font style="color:red"><span id="ca_email_error"></span></font>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="">Co-ordinator Name<span style="color: red">*</span></label>
                                    <input type="text" name="ca_name" id="ca_name" class="form-control">
                                    <font style="color:red"><span id="ca_name_error"></span></font>
                                </div>
                            </div>
                        </div>
                        <div class="row form-group">                        
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="">Co-ordinator Mobile<span style="color: red">*</span></label>
                                    <input type="text" name="ca_mobile" id="ca_mobile" class="form-control">
                                    <font style="color:red"><span id="ca_mobile_error"></span></font>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group" id="domain_div">
                                    <label for="">Domain</label>
                                    <input type="text" name="domain" id="domain" class="form-control">
                                    <font style="color:red"><span id="domain_error"></span></font>
                                </div>
                            </div>
                        </div>
                        <div class="row form-group">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="">Admin email<span style="color: red">*</span></label>
                                    <input type="text" name="hod_email" id="hod_email" class="form-control">
                                    <font style="color:red"><span id="hod_email_error"></span></font>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="">IP address (comma separated for multiple ips)</label>
                                    <input type="text" name="base_ip" id="base_ip" class="form-control">
                                    <font style="color:red"><span id="base_ip_error"></span></font>
                                </div>
                            </div>
                        </div>
                    </div>                    
                </form>
            </div>
            <div class="modal-footer">
                <span class="btn btn-success" data="" id="add_data">Add</span>
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>
<!-- ADD Modal -->

<!-- UPDATE Modal -->
<div id="update_reqs_modal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-lg">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Update Coordinator</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="update_co_form">
                    <input type="hidden" id="update_data_id" />
                    <div class="col-md-12">
                        <div class="row form-group">
                            <div class="col-md-4">
                                <label for="select_cat">Organization Category<span style="color: red">*</span></label>
                                <select name="user_employment" id="user_employment" class="form-control">
                                    <option value="">--Select--</option>
                                    <option value="Central">Central</option>
                                    <option value="State">State</option>
                                    <option value="Psu">PSU</option>
                                    <option value="Const">Constitutional Bodies</option>
                                    <option value="Nkn">Nkn Institutes</option>
                                    <option value="Project">Project</option>
                                     <option value="UT">Union Territory</option>
                                    <option value="Others">Others</option>
                                </select>
                                <font style="color:red"><span id="useremployment_error"></span></font>
                            </div>
                            <div class="col-md-8">
                                <label id="min_state_org_label" for="select_ministry">Ministry/Organization<span style="color: red">*</span></label>
                                <select name="min" id="min" class="form-control">
                                    <option value=''>-SELECT-</option>
                                </select>
                                <font style="color:red"><span id="minerror"></span></font>
                            </div>                        
                        </div>
                        <div id="dept_div"  class="row form-group">
                            <div class="col-md-12">
                                <label for="select_dept">Department/Division/Domain<span  id="select_dept_mandate" style="color: red">*</span></label>
                                <select name="dept" id="dept" class="form-control">
                                    <option value=''>-SELECT-</option>
                                </select>
                                <font style="color:red"><span id="deperror"></span></font>         
                            </div>                        
                        </div>
                        <div id="other_text_div" class="row form-group display-hide">
                            <div class="col-md-12 ">
                                <div>
                                    <label>Other <span style="color: red">*</span></label>
                                    <input type="text" id="other_dept" name="other_dept" placeholder="Enter Department Name [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]" maxlength="100" class="form-control" value="" /> 
                                    <font style="color:red"><span id="other_dep_error"></span></font>
                                </div> 
                            </div>
                        </div>
                        <div class="row form-group">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="">Co-ordinator Email<span style="color: red">*</span></label>
                                    <input type="text" name="ca_email" id="ca_email" class="form-control">
                                    <font style="color:red"><span id="ca_email_error"></span></font>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="">Co-ordinator Name<span style="color: red">*</span></label>
                                    <input type="text" name="ca_name" id="ca_name" class="form-control">
                                    <font style="color:red"><span id="ca_name_error"></span></font>
                                </div>
                            </div>
                        </div>
                        <div class="row form-group">                        
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="">Co-ordinator Mobile<span style="color: red">*</span></label>
                                    <input type="text" name="ca_mobile" id="ca_mobile" class="form-control">
                                    <font style="color:red"><span id="ca_mobile_error"></span></font>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group"  id="domain_div">
                                    <label for="">Domain</label>
                                    <input type="text" name="domain" id="domain" class="form-control">
                                    <font style="color:red"><span id="domain_error"></span></font>
                                </div>
                            </div>
                        </div>
                        <div class="row form-group">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="">Admin email<span style="color: red">*</span></label>
                                    <input type="text" name="hod_email" id="hod_email" class="form-control">
                                    <font style="color:red"><span id="hod_email_error"></span></font>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="">IP address (comma separated for multiple ips)</label>
                                    <input type="text" name="base_ip" id="base_ip" class="form-control">
                                    <font style="color:red"><span id="base_ip_error"></span></font>
                                </div>
                            </div>
                        </div>
                    </div>                    
                </form>
            </div>
            <div class="modal-footer">
                <span class="btn btn-success" data="" id="update_data">Update</span>
                
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>
<!-- UPDATE Modal -->

<!-- Update ministry Modal -->
<div id="update_min_modal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-lg">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Update Ministry</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="update_min_form">
                    <div class="col-md-12">
                        <div class="row form-group">
                            <div id="min_state_org_div" class="col-md-12">
                                <label id="min_state_org_label" for="select_ministry">Ministry<span style="color: red">*</span></label>
                                <select name="min" id="min" class="form-control">
                                    <option value=''>-SELECT-</option>
                                </select>
                                <font style="color:red"><span id="minerror"></span></font>
                            </div>                        
                        </div>
                        <div class="row form-group">
                            <div class="col-md-12 ">
                                <div>
                                    <label>New Ministry Name<span style="color: red">*</span></label>
                                    <input type="text" id="new_min_dept" name="new_min_dept" placeholder="Enter Ministry Name [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]" maxlength="100" class="form-control" value="" /> 
                                    <font style="color:red"><span id="new_min_dept_error"></span></font>
                                </div> 
                            </div>
                        </div>                       
                    </div>                    
                </form>
            </div>
            <div class="modal-footer">
                <span class="btn btn-success" data="" id="min_update">Update</span>
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>
<!-- Update ministry Modal -->

<!-- Update Department Modal -->
<div id="update_dept_modal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-lg">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Update Department</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="update_dept_form">
                    <div class="col-md-12">
                        <div class="row form-group">
                            <div class="col-md-4">
                                <label for="select_cat">Organization Category<span style="color: red">*</span></label>
                                <select name="user_employment" id="user_employment" class="form-control">
                                    <option value="">--Select--</option>
                                    <option value="Central">Central</option>
                                    <option value="State">State</option>
                                    <option value="Psu">PSU</option>
                                    <option value="Const">Constitutional Bodies</option>
                                    <option value="Nkn">Nkn Institutes</option>
                                    <option value="Project">Project</option>
                                    <option value="Others">Others</option>
                                </select>
                                <font style="color:red"><span id="useremployment_error"></span></font>
                            </div>
                            <div class="col-md-8">
                                <label id="min_state_org_label" for="select_ministry">Ministry/Organization<span style="color: red">*</span></label>
                                <select name="min" id="min" class="form-control">
                                    <option value=''>-SELECT-</option>
                                </select>
                                <font style="color:red"><span id="minerror"></span></font>
                            </div>                        
                        </div>
                        <div id="dept_div"  class="row form-group">
                            <div class="col-md-12">
                                <label for="select_dept">Department/Division/Domain<span style="color: red">*</span></label>
                                <select name="dept" id="dept" class="form-control">
                                    <option value=''>-SELECT-</option>
                                </select>
                                <font style="color:red"><span id="deperror"></span></font>         
                            </div>                        
                        </div>
                        <div class="row form-group">
                            <div class="col-md-12 ">
                                <div>
                                    <label>New Department/Division/Domain Name <span style="color: red">*</span></label>
                                    <input type="text" id="new_min_dept" name="new_min_dept" placeholder="Enter Department Name [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]" maxlength="100" class="form-control" value="" /> 
                                    <font style="color:red"><span id="new_min_dept_error"></span></font>
                                </div> 
                            </div>
                        </div>                        
                    </div>                    
                </form>
            </div>
            <div class="modal-footer">
                <span class="btn btn-success" data="" id="dept_update">Update</span>
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>
<!-- Update Department Modal -->