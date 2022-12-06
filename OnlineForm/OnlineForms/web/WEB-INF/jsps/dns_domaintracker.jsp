<%@page import="com.org.bean.UserData"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
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
<%
    String random = entities.Random.csrf_random();
    session.setAttribute("rand", random);
    String CSRFRandom = entities.Random.csrf_random();
    session.setAttribute("CSRFRandom", CSRFRandom);
    UserData userdata = (UserData) session.getAttribute("uservalues");
    boolean nic_employee = userdata.isIsNICEmployee();
    boolean ldap_employee = userdata.isIsEmailValidated();
    if (!session.getAttribute("update_without_oldmobile").equals("no")) {
        if (!userdata.isIsNewUser()) {
            response.sendRedirect("Mobile_registration");
        }
        response.sendRedirect("index.jsp");
    }
    session.removeAttribute("populatedList");
%>
<jsp:include page="include/new_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<jsp:include page="include/new_include/header.jsp" />
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
                <a href="Dns_registration" class="k-content__head-breadcrumb-link">Domain Name System Services</a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link">Domain Record Tracker</a>
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
                                    <h3 class="k-portlet__head-title">Domain Record Tracker</h3>
                                </div>
                            </div>
                            <div class="col-md-6 p-0">
                                <div class="float-right">
                                    <span id="dns_history_page" class="dns_history_page "></span>&emsp;
                                    <a href="Dns_registration" class="ml-2 mt-3 domain_tracker">DNS Service</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-12 mt-4 mb-4">
                    <div class="msg-alert mb-3"></div>
                    <span class="btn btn-primary btn-sm mb-3" id="add-dns-rcd"><i class="fa fa-plus"></i>Add Record</span>
                    <table id="dnsTeamData" class="table table-bordered table-striped dnsTeamData">
                        <thead>
                            <tr>
                                <th class="text-center">Sr. No.</th>
                                <th>Domain</th>
                                <th>IP/CNAME</th>
                                <th>Applicant Name</th>
                                <th>Applicant Email</th>
                                <th>Applicant Mobile</th>
                                <th class="text-center">Action</th>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- The Modal -->
<div class="modal" id="dnsTrackerForm">
  <div class="modal-dialog">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">Modal Heading</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
          <div class="msg-alert mb-3"></div>
          <form id="dnsTrckerFrm" autocomplete="off">
              <input type="hidden" name="id" id="dnsteamid">
              <div class="form-group">
                  <label for="">Enter Domain</label>
                  <input type="text" name="domain" id="domain" class="form-control" placeholder="Enter Domain">
                  <span class="text-danger domain_err"></span>
              </div>
              <div class="form-group">
                  <label for="">Enter IP</label>
                  <input type="text" name="ip" id="ip" class="form-control" placeholder="Enter IP">
                  <span class="text-danger ip_err"></span>
              </div>
              <div class="form-group">
                  <label for="">Owner Name</label>
                  <input type="text" name="name" id="name" class="form-control" placeholder="Enter Owner Name">
                  <span class="text-danger name_err"></span>
              </div>
              <div class="form-group">
                  <label for="">Owner Email</label>
                  <input type="text" name="email" id="email" class="form-control" placeholder="Enter Owner Email">
                  <span class="text-danger email_err"></span>
              </div>
              <div class="form-group">
                  <label for="">Owner Mobile</label>
                  <input type="text" name="mobile" id="mobile" class="form-control" placeholder="Enter Owner Mobile">
                  <span class="text-danger mobile_err"></span>
              </div>
          </form>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
          <button class="btn btn-primary" id="addDnsTracker">Add Record</button>
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- END PAGE CONTENT INNER -->
<jsp:include page="include/new_include/footer.jsp" />
<script src="assets/custom/dns_team_data.js"></script>
