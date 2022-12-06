<%-- 
    Document   : Coordinator_pannel_ro.jsp
    Created on : 14 Sep, 2020, 11:29:42 AM
    Author     : Gaurav Jha
--%>

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
    
if(session.getAttribute("uservalues")==null|| session.getAttribute("uservalues").equals("")){
    response.sendRedirect("index.jsp");
}
else{
%>
<jsp:include page="include/new_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<jsp:include page="include/new_include/header.jsp" />
<style>
table.dataTable .btn-copannel-datatable {
    padding: 5px 10px;
    font-size: 12px;
    cursor: pointer;
}
</style>
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
                <a href="cooPannel" class="k-content__head-breadcrumb-link">Coordinator Pannel</a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link">RO</a>
            </div>
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
                                    <h3 class="k-portlet__head-title">RO</h3>
                                </div>
                            </div>
                            <div class="col-md-6 p-0">
                                <div class="float-right">
                                    <span id="dns_history_page" class="dns_history_page "></span>&emsp;
                                    <a href="cooPannel" class="ml-2 mt-3 domain_tracker">Coordinator</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-12 mt-4 mb-4">
                    <div class="msg-alert mb-3"></div>
                    <span class="btn btn-primary btn-sm mb-3" id="add-ro-rcd"><i class="fa fa-plus"></i>Add Record</span>
                    <table id="RoData" class="table table-bordered table-striped dnsTeamData">
                        <thead>
                            <tr>
                                <th class="text-center">Sr. No.</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Mobile</th>
                                <th class="text-center">Action</th>
                            </tr>
                        </thead>
                        <tbody id="CooPannelRoDataBody"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- The Modal -->
<div class="modal" id="RoAddMod">
  <div class="modal-dialog">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
          <h4 class="modal-title" id="modalName">Add RO</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
          <div class="msg-alert mb-3"></div>
          <form id="RoAddFrm" autocomplete="off">
              <div class="form-group">
                  <label for="">Enter Name</label>
                  <input type="text" name="ca_name" id="ca_name" class="form-control" placeholder="Enter Name">
                  <span class="text-danger ca_name"></span>
              </div>
              <div class="form-group">
                  <label for="">Enter Email</label>
                  <input type="text" name="ca_email" id="ca_email" class="form-control" placeholder="Enter Email">
                  <span class="text-danger ca_email"></span>
              </div>
              <div class="form-group">
                  <label for="">Enter Mobile</label>
                  <input type="text" name="ca_mobile" id="ca_mobile" class="form-control" placeholder="Enter Mobile">
                  <span class="text-danger ca_mobile"></span>
              </div>
              <input type="hidden" class="form-control" name="emp_id" id="ca_id">
          </form>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
          <button class="btn btn-primary" id="addRoBtnRec">Add Record</button>
          <button class="btn btn-primary" id="updRoBtnRec">Update Record</button>
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>

<!-- Begin::View RO Modal -->
<div class="modal" id="ViewRORec">
  <div class="modal-dialog">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">View RO</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
          <table class="table table-bordered table-striped">
              <tbody id="ViewCooPannelRoBody"></tbody>
          </table>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- End::View RO Modal -->

<!-- END PAGE CONTENT INNER -->
<jsp:include page="include/new_include/footer.jsp" />
<script src="assets/custom/Coordinator_ro_pannel.js"></script>
<%
}
%>