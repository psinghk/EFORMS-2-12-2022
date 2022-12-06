                                          <%-- 
    Document   : Coordinator_pannel
    Created on : 12 Sep, 2020, 10:47:34 AM
    Author     : Gaurav Jha
--%>

<%@page import="com.org.bean.UserData"%>
<%@page import="com.org.dao.CoordinatorPanelDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%

    
if(session.getAttribute("uservalues")==null|| session.getAttribute("uservalues").equals("")){
    response.sendRedirect("index.jsp");
}
else{
%>

<jsp:include page="include/new_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<link rel="stylesheet" href="assets/custom/tracker-sty.css" />
<jsp:include page="include/new_include/header.jsp" />
<jsp:include page="include/new_include/filter_pop.jsp" />
<style>
    table.dataTable .btn-copannel-datatable {
        padding: 5px 10px;
        font-size: 12px;
        cursor: pointer;
    }
    .btn-co-request{
     padding: 1px 3px 1px 3px;
     cursor: pointer;
    }
</style>

<div class="k-content	k-grid__item k-grid__item--fluid k-grid k-grid--hor" id="k_content">

    <!-- begin:: Content Head -->
<%

CoordinatorPanelDao cpaneldao=new CoordinatorPanelDao();
 UserData userdata = (UserData) session.getAttribute("uservalues");
 
String loggedinuser=userdata.getEmail();
%>

    <div class="row fillter-div-max-tabs">
            <div class="col-3">
                <a class="filter-a" href="javascript:void(0);" >
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--info" style="background: #303F9F;">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">
                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title">Total<br>Completed Requests</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>
                                        <div class="k-widget-3__content-section">
                                            <span class="k-widget-3__content-number"><%=cpaneldao.getTotalCompletedRequestCountOfCoordinator(loggedinuser)%></span>
<!--                                            <span class="k-widget-3__content-number" data-counter="counterup" id="t_completed">0</span>-->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
            <div class="col-3">
                <!--begin::Portlet-->
                <a class="filter-a" href="javascript:void(0);"  >
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--warning">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">
                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title">Total<br>Pending Request</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>
                                        <div class="k-widget-3__content-section">
                                            
                                            <span class="k-widget-3__content-number"><%=cpaneldao.getTotalPendingRequestCountOfCoordinator(loggedinuser)%></span>
<!--                                            <br> <span class="k-widget-3__content-number" data-counter="counterup"  id="t_pending">0</span>-->
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
            <div class="col-3">
                <a class="filter-a" href="javascript:void(0);"  >
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--danger" style="background: #d32f2f;">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">
                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title">Total<br>Forwaded Requests</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>
                                        <div class="k-widget-3__content-section">
                                             <span class="k-widget-3__content-number"><%=cpaneldao.getTotalForwardedRequestCountOfCoordinator(loggedinuser)%></span>
<!--                                            <span class="k-widget-3__content-number" data-counter="counterup" id="t_forwarded">0</span>-->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>

            <div class="col-3">
                <a class="filter-a" href="javascript:void(0);" >
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--success" style="background: #00796B;">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">
                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title">Total<br>Rejected Requests</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>
                                        <div class="k-widget-3__content-section">
                                            <span class="k-widget-3__content-number"><%=cpaneldao.getTotalRejectedRequestCountOfCoordinator(loggedinuser)%></span>
<!--                                            <span class="k-widget-3__content-number" data-counter="counterup" id="t_rejected">0</span>-->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile" style="height: 100%;">
            <div class="portlet light " id="form_wizard_1" style="display:block;">
                <div class="k-portlet__head">
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-6 p-0">
                                <div class="k-portlet__head-label mt-4">
                                    <h3 class="k-portlet__head-title" >HOG Pannel  </h3>
                                </div>
                            </div>
                            <div class="col-md-6 p-0">
                                <span class="btn btn-danger btn-sm mt-3 float-right" id="pend-req-hog-rcd">Pending Request (HOG)</span>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-12 mt-4 mb-4">
                    <h4 class="k-portlet__head-title" >For managing Co-ordinator <span id="getMinistryInfoHog"></span><span class="boxover" id="boxover"></span></h4>

                </div>
                
                <div class="col-md-12 mt-4 mb-4">
                    <div class="msg-alert mb-3"></div>
                    <span class="btn btn-primary btn-sm mb-3" id="add-coo-rcd"><i class="fa fa-plus"></i>Add Coordinator</span>
                    
                    <div class="btn-group">
                            <span class="btn btn-primary btn-sm green dropdown-toggle mb-3" type="button" data-toggle="dropdown" aria-expanded="true" title="Click to take appropriate actions">Manage Department<i class="fa fa-angle-down pl-2"></i>
                            </span>
                            <ul class="dropdown-menu dropdown-menu-right action-btn-list" role="menu" style="position:inherit;">
                                    <li title="Add Department">
                                        <a href="javascript:void(0)" id="add-dept-rcd"><i class='fa fa-plus'></i><b> Add New Department</b></a>
                                    </li>
                                    <li title="Change Department">
                                        <a href="javascript:void(0)" id="change-dept-req-hog-rcd"><i class='fa fa-check'></i><b> Change Existing Department</b></a>
                                    </li>
                            </ul>
                    </div>

                    <span class="btn btn-primary btn-sm mb-3" id="pend-dept-rcd"><i class="fa fa-check"></i>Pending Department</span>
                    
                    <!--ccombo box-->
                    <div class="col-mg-12" style="padding: 10px;border-width: 10px;background-color: #d8d8d8;border-radius: 20px;">
                        <h4>Register yourself as Hog</h4>
                            <br>
                            
                        
                    <form id="addHogSelf"  autocomplete="off" > 
                    
                        <div class="row">
                            
            <div class="col-md-4">
               <div class="form-group">
                  <label for="">Category <span style="color: red">*</span></label>                                           
                    <select class="form-control" id="emp_category_hog" name="emp_category_hog">
                        <option value="">--Select--</option>
                    </select>                                    
                  <span class="text-danger hog_cat_error"></span>
              </div> 
            </div>
              
            <div id="central_div_hog" class="col-md-8">
                <div class="row">
                    <div class="col-md-8">
                     <div class="form-group">
                          <label for="">Ministry/Organization <span style="color: red">*</span></label>
                            <select class="form-control" id="emp_min_state_org_hog" name="emp_min_state_org_hog">
                                <option value="">--Select--</option>
                            </select>  
                          <span class="text-danger hog_min_error"></span>
                      </div> 
                         
                    </div>
                     
                
                </div>
            </div>
              
            <div id="state_div_hog" class="col-md-8" style="display:none">
               <div class="row">
                   <div class="col-md-8">
                       <label for="street">State <span style="color: red">*</span></label>
                       <select class='form-control' id="stateCode_hog" name="stateCode_hog">
                           <option value=''>-SELECT-</option>
                       </select>
                       <span class="text-danger hog_stateCode"></span>   
                        
                   </div>
                    
              
               </div>
           </div>             

           <div id="other_div_hog" class="col-md-8" style="display:none">
               <div class="row">
                   <div class="col-md-6">
                       <label for="street">Organization Name <span style="color: red">*</span></label>
                       <select class='form-control' id="org_hog" name="org_hog">
                           <option value=''>-SELECT-</option>
                       </select>
                       <span class="text-danger hog_org"></span>      
                     
                   </div>
                     
               </div>
           </div>  
           
          </div>
                        
                        
          <button class="btn btn-primary" id="add_hog_self">Register</button>
        
 
        
              
          </form>
                    </div>
                    <br>  
                    
                    
                    <!--End of combo box-->
                    
                    
                    
                    
                    
                    <table id="CoordinatorData" class="table table-bordered table-striped display dt-responsive" style="width:100%" cellspacing="0">
                        <thead>
                            <tr>
                                <th class="text-center">Sr. No.</th>
                                <th>Department</th>
                                <th>Coordinator Email</th>
                                <th>Mobile</th>
                                <th>Completed</th>
                                <th>Pending</th>
                                <th>Forwaded</th>
                                <th data-priority="2">Rejected</th>
                                <th data-priority="1" class="text-center">Action</th>
                            </tr>
                        </thead>
                        <tbody id="CoordinatorDataBody"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- The Modal -->
<div class="modal" id="cooAddMod">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title" id="modalName">Add Coordinator</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
          <form id="cooAddFrm" autocomplete="off">
                    
          <div class="row">
            <div class="col-md-4">
               <div class="form-group">
                  <label for="">Category <span style="color: red">*</span></label>                                           
                    <select class="form-control" id="emp_category" name="emp_category">
                        <option value="">--Select--</option>
                    </select>                                    
                  <span class="text-danger emp_category"></span>
              </div> 
            </div>
              
            <div id="central_div" class="col-md-8">
                <div class="row">
                    <div class="col-md-6">
                     <div class="form-group">
                          <label for="">Ministry/Organization <span style="color: red">*</span></label>
                            <select class="form-control" id="emp_min_state_org" name="emp_min_state_org">
                                <option value="">--Select--</option>
                            </select>  
                          <span class="text-danger emp_min_state_org"></span>
                      </div> 
                    </div>
                    <div class="col-md-6" >
                      <div class="form-group">
                          <label for="">Department/Division/Domain <span style="color: red">*</span></label>
                            <select class="form-control" id="emp_dept" name="emp_dept">
                                <option value="">--Select--</option>
                            </select>  
                          <span class="text-danger emp_dept"></span>
                      </div> 
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
                       <span class="text-danger stateCode"></span>                                                                                                                                                                     
                   </div>
                   <div class="col-md-6">
                       <label for="street">Department <span style="color: red">*</span></label>
                       <select class='form-control' name="state_dept" id="state_dept">
                           <option value=''>-SELECT-</option>
                       </select>       
                       <span class="text-danger state_dept"></span>                                                                                        
                   </div>
               </div>
           </div>             

           <div id="other_div" class="col-md-8" style="display:none">
               <div class="row">
                   <div class="col-md-6">
                       <label for="street">Organization Name <span style="color: red">*</span></label>
                       <select class='form-control' id="org" name="org">
                           <option value=''>-SELECT-</option>
                       </select>
                       <span class="text-danger org"></span>                                                                                                                                                                     
                   </div>
               </div>
           </div>           
          </div>
          
        <div class="row">
            <div class="col-md-4">
              <div class="form-group">
                  <label for="">Sub Department</label>
                  <input type="text" name="emp_sub_dept" id="emp_sub_dept" class="form-control" placeholder="Enter Coordinator Sub-Department">
                  <span class="text-danger emp_sub_dept"></span>
              </div> 
            </div>
            <div class="col-md-4">
                <div class="form-group">
                  <label for="">Name <span style="color: red">*</span></label>
                  <input type="text" name="emp_coord_name" id="emp_coord_name" class="form-control" placeholder="Enter Coordinator Name">
                  <span class="text-danger emp_coord_name"></span>
                </div> 
            </div>
            <div class="col-md-4">
                <div class="form-group">
                  <label for="">Email <span style="color: red">*</span></label>
                  <input type="text" name="emp_coord_email" id="emp_coord_email" class="form-control" placeholder="Enter Coordinator Email">
                  <span class="text-danger emp_coord_email"></span>
                </div> 
            </div>
        </div>

        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                  <label for="">Mobile <span style="color: red">*</span></label>
                  <input type="text" name="emp_coord_mobile" id="emp_coord_mobile" class="form-control" placeholder="Enter Coordinator Mobile">
                  <span class="text-danger emp_coord_mobile"></span>
                </div> 
            </div>
            <div class="col-md-4">
                <div class="form-group">
                  <label for="">IP <span style="color: red">*</span></label>
                  <input type="text" name="emp_ip" id="emp_ip" class="form-control" placeholder="Enter Coordinator State Organization">
                  <span class="text-danger emp_ip"></span>
                </div> 
            </div>
            <div class="col-md-4">
              <div class="form-group" >
                  <input type="hidden" class="form-control" id="emp_id" name="emp_id">
              </div> 
            </div>
        </div>
              
          </form>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
          <button class="btn btn-primary" id="addCooRecBtn">Add Coordinator</button>
          <button class="btn btn-primary" id="updCooRecBtn">Update Coordinator</button>
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>

<!-- Begin::View Coordinator Modal -->
<div class="modal" id="ViewCoordinatorRec">
  <div class="modal-dialog">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">View Coordinator</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
          <table class="table table-bordered table-striped">
              <tbody id="ViewEmpBody"></tbody>
          </table>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- End::View Coordinator Modal -->

<!-- Begin::View Completed Request Modal -->
<div class="modal" id="ViewCompReqRec">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">View Completed Request</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
          <table class="table table-bordered table-striped display dt-responsive" style="width:100%" cellspacing="0" id="ViewCompReqData">
            <thead>
              <tr>
                  <th>Registration No</th>
                  <th>Applicant Name</th>
                  <th>Applicant Email</th>
                  <th>Applicant Mobile</th>
                  <th data-priority="2">Ca Email</th>
                  <th data-priority="1">Status</th>
              </tr>
            </thead>
            <tbody id="ViewCompReqBody">

            </tbody>
          </table>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- End::View Completed Request Modal -->

<!-- Begin::View Pending Request Modal -->
<div class="modal" id="ViewPendReqRec">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">View Pending Request</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
          <div class="row">
              <div class="col-md-12"><div class="msg-mod-alert mb-3"></div></div>
              <div class="col-md-12">
                <table class="table table-bordered table-striped display dt-responsive"  style="width:100%" cellspacing="0" id="ViewPendReqData">
                    <thead>
                      <tr>
                          <th class="active">
                                <input type="checkbox" id="masterCheck" name="select-all" />
                          </th>
                          <th>Registration No</th>
                          <th>Applicant Name</th>
                          <th>Applicant Email</th>
                          <th >Applicant Mobile</th>
                          <th data-priority="2">Ca Email</th>
                          <th data-priority="1">Action</th>
                      </tr>
                    </thead>
                    <tbody id="ViewPendReqBody">

                    </tbody>
                </table>
              </div>
          </div>

      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
          <button type="button" class="btn btn-success" id="BulkPullbackRequest">PullBack</button>
        <button type="button" class="btn btn-default dark btn-outline" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- End::View Pending Request Modal -->

<!-- Begin::Add Department Modal -->
<div class="modal" id="AddDepartRec">
  <div class="modal-dialog">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">Add New Department</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
        <form id="AddDeptRec" autocomplete="off">

            <div class="form-group">
               <label for="">Category <span style="color: red">*</span></label>                                           
                 <select class="form-control" id="add_dept_emp_category" name="add_dept_emp_category">
                     <option value="">--Select--</option>
                 </select>                                    
               <span class="text-danger add_dept_emp_category"></span>
            </div> 

            <div id="add_dept_min_div" >
            <div class="form-group" >
                 <label for="">Ministry/Organization <span style="color: red">*</span></label>
                   <select class="form-control" id="add_dept_emp_min_state_org" name="add_dept_emp_min_state_org">
                       <option value="">--Select--</option>
                   </select>  
                 <span class="text-danger add_dept_emp_min_state_org"></span>
             </div>
            
             <div class="form-group">
                 <label for="">Department/Division/Domain <span style="color: red">*</span></label>
                    <input type="text" name="add_dept_emp_dept" id="add_dept_emp_dept" class="form-control" placeholder="Enter Department">
                 <span class="text-danger add_dept_emp_dept"></span>
             </div> 
            </div>
            
            <div id="add_dept_state_div" style="display:none">
            <div class="form-group" >
                 <label for="">State <span style="color: red">*</span></label>
                   <select class="form-control" id="add_dept_emp_state_code" name="add_dept_emp_state_code">
                       <option value="">--Select--</option>
                   </select>  
                 <span class="text-danger add_dept_emp_state_code"></span>
             </div> 
            
             <div class="form-group">
                 <label for="">Department <span style="color: red">*</span></label>
                    <input type="text" name="add_dept_emp_state_dept" id="add_dept_emp_state_dept" class="form-control" placeholder="Enter Department">
                 <span class="text-danger add_dept_emp_state_dept"></span>
             </div> 
            </div>
            
             <div class="form-group">
                 <label for="">Email <span style="color: red">*</span></label>
                    <input type="text" name="add_dept_emp_coord_email" id="add_dept_emp_coord_email" class="form-control" placeholder="Enter Email">
                 <span class="text-danger add_dept_emp_coord_email"></span>
             </div>
            
            <div class="form-group">
                 <label for="">Mobile <span style="color: red">*</span></label>
                    <input type="text" name="add_dept_emp_coord_mobile" id="add_dept_emp_coord_mobile" class="form-control" placeholder="Enter Mobile">
                 <span class="text-danger add_dept_emp_coord_mobile"></span>
             </div>
            
             <div class="form-group">
                 <label for="">IP <span style="color: red">*</span></label>
                    <input type="text" name="add_dept_emp_ip" id="add_dept_emp_ip" class="form-control" placeholder="Enter IP">
                 <span class="text-danger add_dept_emp_ip"></span>
             </div>
            
        </form>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
          <button class="btn btn-primary" id="addDept">Submit</button>
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- End::Add Department Modal -->


<!-- Begin::View Pending Department Request Modal -->
<div class="modal" id="ViewPendDeptRec">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">Pending Departments for Approval</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
          <div class="row">
              <div class="col-md-12">
                <table class="table table-bordered table-striped display dt-responsive"  style="width:100%" cellspacing="0" id="ViewDeptPendData">
                  <thead>
                    <tr>
                        <th>Category</th>
                        <th>Ministry</th>
                        <th>Department</th>
                        <th>Other Department</th>
                        <th data-priority="2">Requested By</th>
                        <th data-priority="1">Action</th>
                    </tr>
                  </thead>
                  <tbody id="ViewDeptPendBody"></tbody>
                </table>                  
              </div>
          </div>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- End::View Pending Department Request Modal -->

<!-- Begin::Edit Department Request Modal -->
<div class="modal" id="editDepartRec">
  <div class="modal-dialog">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">Update Department</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
        <form id="updateDeptRec" autocomplete="off">

            <div class="form-group">
               <label for="">Category <span style="color: red">*</span></label>                                           
                 <select class="form-control" id="edit_dept_emp_category" name="edit_dept_emp_category">
                     <option value="">--Select--</option>
                 </select>                                    
               <span class="text-danger edit_dept_emp_category"></span>
            </div> 

            <div id="edit_dept_emp_central_div">
            <div class="form-group">
                 <label for="">Ministry/Organization <span style="color: red">*</span></label>
                   <select class="form-control" id="edit_dept_emp_min_state_org" name="edit_dept_emp_min_state_org">
                       <option value="">--Select--</option>
                   </select>  
                 <span class="text-danger"></span>
             </div> 

             <div class="form-group">
                 <label for="">Department/Division/Domain <span style="color: red">*</span></label>
                    <input type="text" name="edit_dept_emp_dept" id="edit_dept_emp_dept" class="form-control" placeholder="Enter Department">
                 <span class="text-danger edit_dept_emp_dept"></span>
             </div> 
            </div>
            
            <div id="edit_dept_emp_state_div">
            <div class="form-group">
                 <label for="">State <span style="color: red">*</span></label>
                   <select class="form-control" id="edit_dept_emp_state_code" name="edit_dept_emp_state_code">
                       <option value="">--Select--</option>
                   </select>  
                 <span class="text-danger"></span>
             </div> 

             <div class="form-group">
                 <label for="">Department <span style="color: red">*</span></label>
                    <input type="text" name="edit_dept_emp_state_dept" id="edit_dept_emp_state_dept" class="form-control" placeholder="Enter Department">
                 <span class="text-danger edit_dept_emp_state_dept"></span>
             </div> 
            </div>
            
             <div class="form-group">
                 <label for="">Email <span style="color: red">*</span></label>
                    <input type="text" name="edit_dept_emp_coord_email" id="edit_dept_emp_coord_email" class="form-control" placeholder="Enter Email">
                 <span class="text-danger edit_dept_emp_coord_email"></span>
             </div>
            <input type="hidden" name="edit_dept_id" id="edit_dept_id">   
        </form>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
          <button class="btn btn-primary" id="updateDept">Update Department</button>
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- End::Edit Department Request Modal -->

<!-- Begin::Approve Department Modal -->
<div class="modal" id="ApproveDepart">
  <div class="modal-dialog">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">Approve Department Request</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
          <div class="row">
              <div class="col-md-12"><div class="msg-mod-alert"></div></div>
              <div class="col-md-12 mb-2"><span>Are you sure? Do you want to Approve this Request?</span></div>
              <div class="col-md-12">
                <form id="ApproveDeptRec" autocomplete="off" onsubmit="event.preventDefault()">
                     <div class="form-group">
                         <label for="">Email <span style="color: red">*</span></label>
                            <input type="text" name="approve_dept_email" id="approve_dept_email" class="form-control" placeholder="Enter Email">
                         <span class="text-danger approve_dept_email"></span>
                     </div>
                    <input type="hidden" name="approve_dept_emp_id" id="approve_dept_emp_id">
                </form>                  
              </div>
          </div>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
          <button class="btn btn-primary" id="approveDept" >Confirm</button>
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- End::Approve Department Modal -->




<!-- Begin::View Pending Request HOG Modal -->
<div class="modal" id="ViewPendReqHOGRec">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">View Pending Request HOG</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
          <div class="row">
              <div class="col-md-12"><div class="msg-mod-alert mb-3"></div></div>              
              <div class="col-md-12">  
                <table class="table table-bordered table-striped display dt-responsive"  style="width:100%" cellspacing="0" id="ViewPendReqHOGData">
                    <thead>
                      <tr>
                          <th class="active">
                                <input type="checkbox"  id="headcheck" class="select-all checkbox" name="headcheck" />
                          </th>
                          <th>Registration No</th>
                          <th>Applicant Name</th>
                          <th>Applicant Email</th>
                          <th >Applicant Mobile</th>
                          <th data-priority="2">Ca Email</th>
                          <th data-priority="1">Action</th>
                      </tr>
                    </thead>
                    <tbody id="ViewPendReqHOGBody">

                    </tbody>
                </table>
              </div>
          </div>

      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
          <span class="btn btn-default" id="statsHOG">Statistics</span>
          <button type="button" class="btn btn-info" id="BulkApproveRequest">Approve All</button>
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- End::View Pending Request HOG Modal -->

<!-- Begin::View HOG Statistics Modal -->
<div class="modal" id="ViewHOGStatsRec">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">View Statistics</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
            <div class="row fillter-div-max-tabs">
                        <div class="col-3">
                            <a class="filter-a" href="javascript:void(0);" >
                                <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                                    <div class="k-portlet__body k-portlet__body--fluid">
                                        <div class="k-widget-3 k-widget-3--info" style="background: #303F9F;">
                                            <div class="k-widget-3__content dashtab">
                                                <div class="k-widget-3__content-info">
                                                    <div class="k-widget-3__content-section">
                                                        <div class="k-widget-3__content-title">Total<br>Completed Requests</div>
                                                        <div class="k-widget-3__content-desc"></div>
                                                    </div>
                                                    <div class="k-widget-3__content-section">
                                                        <span class="k-widget-3__content-number"><%=cpaneldao.getTotalCompletedRequestCountOfHOG(loggedinuser)%></span>
            <!--                                            <span class="k-widget-3__content-number" data-counter="counterup" id="t_completed">0</span>-->
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </div>
                        <div class="col-3">
                            <!--begin::Portlet-->
                            <a class="filter-a" href="javascript:void(0);"  >
                                <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                                    <div class="k-portlet__body k-portlet__body--fluid">
                                        <div class="k-widget-3 k-widget-3--warning">
                                            <div class="k-widget-3__content dashtab">
                                                <div class="k-widget-3__content-info">
                                                    <div class="k-widget-3__content-section">
                                                        <div class="k-widget-3__content-title">Total<br>Pending Request</div>
                                                        <div class="k-widget-3__content-desc"></div>
                                                    </div>
                                                    <div class="k-widget-3__content-section">

                                                        <span class="k-widget-3__content-number"><%=cpaneldao.getTotalPendingRequestCountOfHOG(loggedinuser)%></span>
            <!--                                            <br> <span class="k-widget-3__content-number" data-counter="counterup"  id="t_pending">0</span>-->
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </div>
                        <div class="col-3">
                            <a class="filter-a" href="javascript:void(0);"  >
                                <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                                    <div class="k-portlet__body k-portlet__body--fluid">
                                        <div class="k-widget-3 k-widget-3--danger" style="background: #d32f2f;">
                                            <div class="k-widget-3__content dashtab">
                                                <div class="k-widget-3__content-info">
                                                    <div class="k-widget-3__content-section">
                                                        <div class="k-widget-3__content-title">Total<br>Forwaded Requests</div>
                                                        <div class="k-widget-3__content-desc"></div>
                                                    </div>
                                                    <div class="k-widget-3__content-section">
                                                         <span class="k-widget-3__content-number"><%=cpaneldao.getTotalForwardedRequestCountOfHOG(loggedinuser)%></span>
            <!--                                            <span class="k-widget-3__content-number" data-counter="counterup" id="t_forwarded">0</span>-->
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </div>

                        <div class="col-3">
                            <a class="filter-a" href="javascript:void(0);" >
                                <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                                    <div class="k-portlet__body k-portlet__body--fluid">
                                        <div class="k-widget-3 k-widget-3--success" style="background: #00796B;">
                                            <div class="k-widget-3__content dashtab">
                                                <div class="k-widget-3__content-info">
                                                    <div class="k-widget-3__content-section">
                                                        <div class="k-widget-3__content-title">Total<br>Rejected Requests</div>
                                                        <div class="k-widget-3__content-desc"></div>
                                                    </div>
                                                    <div class="k-widget-3__content-section">
                                                        <span class="k-widget-3__content-number"><%=cpaneldao.getTotalRejectedRequestCountOfHog(loggedinuser)%></span>
            <!--                                            <span class="k-widget-3__content-number" data-counter="counterup" id="t_rejected">0</span>-->
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </div>
                </div>          
      </div>
      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- End::View HOG Statistics Modal -->

<!-- Begin::Change Department HOG Request Modal -->
<div class="modal" id="ChangeDepartHOGRec">
  <div class="modal-dialog">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">Change Existing Department</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
        <form id="ChangeDeptHOGRec" autocomplete="off" onsubmit="event.preventDefault()">

            <div class="form-group">
               <label for="">Category <span style="color: red">*</span></label>                                           
                 <select class="form-control" id="change_dept_emp_category" name="change_dept_emp_category">
                     <option value="">--Select--</option>
                 </select>                                    
               <span class="text-danger change_dept_emp_category"></span>
            </div> 

            <div id="change_dept_emp_min_div">
            <div class="form-group">
                 <label for="">Ministry/Organization <span style="color: red">*</span></label>
                   <select class="form-control" id="change_dept_emp_min_state_org" name="change_dept_emp_min_state_org">
                       <option value="">--Select--</option>
                   </select>  
                 <span class="text-danger change_dept_emp_min_state_org"></span>
             </div> 

             <div class="form-group">
                 <label for="">Department/Division/Domain <span style="color: red">*</span></label>
                   <select class="form-control" id="change_dept_emp_dept" name="change_dept_emp_dept">
                       <option value="">--Select--</option>
                   </select>  
                 <span class="text-danger change_dept_emp_dept"></span>
             </div> 
            </div>
            
            <div id="change_dept_emp_state_div" style="display:none">
            <div class="form-group">
                 <label for="">State <span style="color: red">*</span></label>
                   <select class="form-control" id="change_dept_state_code" name="change_dept_state_code">
                       <option value="">--Select--</option>
                   </select>  
                 <span class="text-danger change_dept_state_code"></span>
             </div> 

             <div class="form-group">
                 <label for="">Department <span style="color: red">*</span></label>
                   <select class="form-control" id="change_dept_state_dept" name="change_dept_state_dept">
                       <option value="">--Select--</option>
                   </select>  
                 <span class="text-danger change_dept_state_dept"></span>
             </div> 
            </div>
            
            
             <div class="form-group">
                 <label for="">New Department <span style="color: red">*</span></label>
                    <input type="text" name="change_dept_new_depts" id="change_dept_new_depts" class="form-control" placeholder="Enter New Department">
                 <span class="text-danger change_dept_new_depts"></span>
             </div>

        </form>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
          <button class="btn btn-primary" id="changeDeptbtnsubmit">Submit</button>
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- End::Change Department HOG Request Modal -->

<!----------------------------------- Begin :: Track ----------------------------------------->

<div class="modal fade bd-modal-lg" id="basic_track" tabindex="-1"  aria-hidden="true" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title" id="refnumbertrack"></h3>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" ><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body clearfix">
                <div class="col-md-12">
                    <h6 class="mb-3"><b>User Detail</b></h6>
                    <table class="table table-bordered table-striped track-tbl" id="track_user_tbl">
                        <thead>
                            <tr class="table-info">
                                <td><b>Name</b></td>
                                <td><b>Email</b></td>
                                <td><b>Mobile</b></td>
                                <td><b>Date</b></td>
                            </tr>
                        </thead>
                        <tbody id="user_data_tbody"></tbody>
                    </table>

                </div>
                <div class="col-md-12">
                    <form class="form-horizontal" action="#" id="submit_form" method="POST">
                        <div class="form-wizard">
                            <div class="form-body">
                                <div class="tracker-div clearfix">
                                    <ul class="order-tracker">

                                    </ul>
                                </div>
                                <div class="track-msg" id="track-msg">
                                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy.
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
<!--                <button type="button" class="btn btn-primary" id="query_raise_hyperlink">Raised/Responded Query</button>-->
                <button type="button" class="btn btn-default" data-dismiss="modal" >Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!----------------------------------- End :: Track ----------------------------------------->


<div class="modal fade bs-modal-lg preview-change" id="large1" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="sms_preview">
        <div class="sms_preview"></div>  
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="dns_preview_form"  tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="dns_preview">
        <div class="dns_preview"></div>    
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="wifi_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="wifi_preview">
        <div class="wifi_preview"></div>   
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="singleuser_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="singleuser_preview">
        <div class="single_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="ldap_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="ldap_preview">
        <div class="ldap_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="imappop_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="imappop_preview">
        <div class="imappop_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="mobile_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="mobile_preview">
        <div class="mobile_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="nkn_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="nkn_preview">
        <div class="nkn_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="gem_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="gem_preview">
        <div class="gem_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="bulkuser_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="bulkuser_preview">
        <div class="bulk_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg  preview-change" id="distribution_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="distribution_preview" method="post">
        <div class="distribution_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>           
<div class="modal fade bs-modal-lg  preview-change" id="relay_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="relay_preview" method="post">
        <div class="relay_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg  preview-change" id="ip_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="ip_preview" method="post">
        <div class="ip_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg  preview-change" id="webcast_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="webcast_preview" method="post">
        <div class="webcast_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg  preview-change" id="centralutm_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="centralutm_preview" method="post">
        <div class="centralutm_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg  preview-change" id="vpn_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="vpn_preview" method="post">
        <div class="vpn_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="email_act_preview_form" tabindex="-1" role="dialog" aria-hidden="true">           
    <form role="form" id="email_act_preview" method="post">    
        <div class="email_act_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="email_deact_preview_form" tabindex="-1" role="dialog" aria-hidden="true">           
    <form role="form" id="email_deact_preview" method="post">    
        <div class="email_deact_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>


<!-- END PAGE CONTENT INNER -->
<style>
.box{

}
.boxover{
    color: #000000cf;
    position: absolute;
    width: 500px;
    bottom: 30px;
    left: 130px;
    border-radius: 10px;
    padding: 20px 20px 20px 20px;
    z-index: 100;
    background: #c5bfbf;
    opacity: 0;
}
</style>

<input type="hidden" name="comingFrom" id="comingFrom" value="user" />
<jsp:include page="include/new_include/footer.jsp" />
<script src="main_js/onlineforms_1_hog_pending.js" type="text/javascript"></script>
<script src="main_js/onlineforms_2_pullback.js" type="text/javascript"></script>
<script src="main_js/dns.js" type="text/javascript"></script>
<script src="main_js/dns_bulk.js" type="text/javascript"></script>
<script src="assets/custom/Coordinator_pannel.js"></script>
<%
}
%>