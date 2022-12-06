<%--
    Document   : VPN
    Created on : Oct 27, 2017, 10:31:57 AM
    Author     : nikki
--%>
<%@page import="com.org.bean.UserData"%>
<%@page import="com.google.gson.Gson"%>
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
    UserData userdata = (UserData) session.getAttribute("uservalues");
    if (!session.getAttribute("update_without_oldmobile").equals("no")) {

        if (!userdata.isIsNewUser()) {
            response.sendRedirect("Mobile_registration");
        }
        response.sendRedirect("index.jsp");
    }
    String random = entities.Random.csrf_random();
    session.setAttribute("rand", random);
    // below code added by pr on 23rdjan18
    String CSRFRandom = entities.Random.csrf_random();
    session.setAttribute("CSRFRandom", CSRFRandom);
    System.out.println("CSRF session: " + CSRFRandom);
%>
<style type="text/css">
    table.table.table-bordered.table-hover tr th {
        background: #eee;
    }ul.vpn-listing li {
        font-family: monospace;
        font-size: 15px;
    }
</style>
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
                <a href="" class="k-content__head-breadcrumb-link">Vpn Services</a>
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile">
            <div class="portlet light " id="form_wizard_1" style="display:block;">
                <div class="portlet-title">
                    <div class="k-portlet__head">
                        <div class="k-portlet__head-label">
                            <h3 class="k-portlet__head-title">VPN Details</h3>
                        </div>
                    </div>
                </div>
                <div class="portlet-body form">

                    <div class="col-md-12 form-wizard">
                        <div class="form-body">
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab1">
                                    <form action="" method="post" id="vpn_form1">
                                        <div class="row mb-4">
                                            <div class="col-md-12 mt-4">
                                                <label for="street">Type of User <span style="color: red">*</span></label><br>
                                                <div class="row">
                                                    <div class="col-3 mt-2 mb-3">
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="req_for" id="vpn_single" value="single" checked> New Request
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-3 mt-2 mb-3">
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <!--<input type="radio" name="req_for" id="vpn_change" value="change_add" > Add / Delete to existing-->
                                                            <input type="radio" name="req_for" id="vpn_change" value="change_add" > Add/Delete IP address to existing
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-3 mt-2 mb-3">
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="req_for" id="vpn_change_renew" value="change_add" > Renew
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-3 mt-2 mb-3">
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="req_for" id="vpn_surrender" value="Surrender" > Surrender
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                </div>
                                                <font style="color:red"><span id="req_for_err"></span></font>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-12 mb-4" id="vpn_renew_form_new"></div>
                                            <div class="col-md-6 mt-4 display-hide" id="coord_div">
                                                <label for="street">Choose Coordinator <span style="color: red">*</span></label><br>
                                                <div class="mt-2 mb-3">
                                                    <span class="display-hide" id="co_chk1">
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="req_co" id="central_co" value="central_co" > Organization Coordinator
                                                            <span></span>
                                                        </label>
                                                        &emsp;&emsp;&emsp;&emsp;&emsp;
                                                    </span>
                                                    <span>
                                                        <label class="k-radio k-radio--bold k-radio--brand display-hide" id="co_chk2">
                                                            <input type="radio" name="req_co" id="state_co" value="state_co" > State Coordinator
                                                            <span></span>
                                                        </label>&emsp;&emsp;&emsp;&emsp;&emsp;
                                                    </span>
                                                    <font style="color:red"><span id="req_co_err"></span></font>
                                                </div>
                                            </div>

                                            <div class="col-md-3 mt-3 display-hide" id="sel_coo">
                                                <label for="street">Choose Co-ordinator<span style="color: red">*</span></label>                                                                            
                                                <select name="vpn_coo" class="form-control" id="vpn_coo">
                                                    <option value="">-SELECT-</option>
                                                </select>
                                                <font style="color:red"><span id="vpn_coo_error"></span></font>
                                            </div> 

                                            <div class="col-md-3 mt-3 display-hide" id="state_coo" style="max-width: 24%;">
                                                <label for="street">Choose State Co-ordinator<span style="color: red">*</span></label>                                                                            
                                                <select name="vpn_coo1" class="form-control" id="vpn_state_coo">
                                                    <option value="">-SELECT-</option>
                                                </select>
                                                <font style="color:red"><span id="vpn_coo_error1"></span></font>
                                            </div>
                                        </div>
                                        <div class="col-md-12" id="detailContainer">
                                            <div class="row">
                                                <div id="detailContainer_div_radio" class="detailContainer_div_radio col-md-11 col-10">
                                                    <div class="ip_div_2">
                                                        <div class="row_delete" id="vpn_ip_add_div">
                                                            <div class="col-md-6">
                                                                <label for="street">IP Address <span style="color: red">*</span></label>
                                                                <div class="mt-2">
                                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                                        <input type="radio" name="vpn_ip_type[0]" class="vpn_ip_type" id="ip_single_0" value="single" checked> Single IP
                                                                        <span></span>
                                                                    </label>&emsp;&emsp;&emsp;
                                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                                        <input type="radio" name="vpn_ip_type[0]" class="vpn_ip_type" id="ip_range_0" value="range" > IP Range
                                                                        <span></span>
                                                                    </label>&emsp;&emsp;&emsp;
                                                                    <font style="color:red"><span id="ip_type_err"></span></font>
                                                                </div>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--danger d-none float-right" id="delete_this_entry_0">
                                                                    <input type="checkbox" name="delete_this_entry[0]" value="0" disabled onclick="deleteVpnEntry(this)">
                                                                    <span></span>
                                                                    Delete this VPN Entry
                                                                </label>
                                                            </div>
                                                        </div>
                                                        <div class="row ip_div_1">
                                                            <div class="col-md-4 ip_div" id="ip_div">
                                                                <label for="street">Enter Server IP address <span style="color: red">*</span></label>
                                                                <input class="form-control" placeholder="Enter IP Address [e.g: 10.10.10.10]" type="text" name="vpn_new_ip1" id="new_ip1" maxlength="100"  aria-required="true">
                                                                <font style="color:red"><span id="new_ip1_err"></span></font>
                                                            </div>                                                                            
                                                            <div class="display-hide col-md-6" id="ip_range_div">
                                                                <div class="row">
                                                                    <div class="col-md-6">
                                                                        <label for="street">Enter IP range (From) <span style="color: red">*</span></label>
                                                                        <input class="form-control" placeholder="Enter IP range (From) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip2" id="new_ip2" maxlength="100"  aria-required="true">
                                                                        <font style="color:red"><span id="new_ip2_err"></span></font>
                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <label for="street">Enter IP range (To)  <span style="color: red">*</span></label>
                                                                        <input class="form-control" placeholder="Enter IP range (To) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip3" id="new_ip3" value="" maxlength="100"  aria-required="true">                                                                                        
                                                                        <font style="color:red"><span id="new_ip3_err"></span></font>
                                                                    </div>
                                                                </div>
                                                            </div>

                                                            <div class="col-md-4">
                                                                <label for="street">Application URL </label>
                                                                <input class="form-control" placeholder="Enter Application URL [e.g: (abc.com)] " type="text" name="vpn_app_url" id="app_url" value="" maxlength="100"  aria-required="true">                                                                                        
                                                                <font style="color:red"><span id="app_url_err"></span></font>
                                                            </div>
                                                            <div class="col-md-4">
                                                                <label for="street">Destination Port <span style="color: red">*</span></label>
                                                                <input placeholder="Enter Destination Port [e.g: 80,443] " type="text" name="vpn_dest_port" id="dest_port"  class="form-control" >
                                                                <font style="color:red"><span id="dest_port_err"></span></font>
                                                            </div>
                                                        </div>

                                                        <div class="row detailContainer_div_select">
                                                            <div class="col-md-6">
                                                                <label for="street">Server Location<span style="color: red">*</span></label>
                                                                <select name="vpn_server_loc" class="form-control" id="server_loc">
                                                                    <option value="NDC Delhi">NDC Delhi</option>
                                                                    <option value="NDC Pune">NDC Pune</option>
                                                                    <option value="NDC Hyderabad">NDC Hyderabad</option>
                                                                    <option value="NDC Bhubaneswar">NDC Bhubaneswar</option>
                                                                    <option value="Other">Other</option>
                                                                </select>
                                                                <font style="color:red"><span id="server_loc_err"></span></font>
                                                            </div>
                                                            <div class="col-md-6 display-hide" id="server_other">
                                                                <label for="street">Enter server location <span style="color: red">*</span></label>
                                                                <input class="form-control" placeholder="Enter Server Location [characters,dot(.) and whitespace]" type="text" name="vpn_server_loc_txt" id="server_loc_txt"  value="" maxlength="100">
                                                                <font style="color:red"><span id="server_txt_err"></span></font>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="add-ip-div col-md-1 col-2">
                                                    <div class="btn-div">
                                                        <button class="btn btn-primary float-right" type="button" onClick="AddTextBox()"><i class="fa fa-plus"></i></button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">

                                            <div class="col-md-12">
                                                <label for="street">Remarks</label>
                                                <input class="form-control" value="" placeholder="Remarks" type="text" name="remarks" id="remarks" maxlength="100"  aria-required="true">
                                                <font style="color:red"><span id="remarks_error"></span></font>
                                            </div>                                                                                
                                        </div>
                                        <div class="row mt-5">
                                            <div class="col-md-6" style="text-align: right;">
                                                <br/><label for="street">Captcha</label>
                                                <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                                <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                            </div>
                                            <div class="col-md-2">
                                                <div class="form-group">
                                                    <label for="street">Enter Captcha<span style="color: red">*</span></label>
                                                    <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="imgtxt" maxlength="6" value=""> 
                                                    <font style="color:red"><span id="captchaerror"></span></font>
                                                </div>
                                            </div>
                                        </div>
                                        <font style="color:red"><span id="request_err"></span></font>
                                        <div class="row">
                                            <div class="col-md-12 text-center mt-5">
                                                <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                                <button name="submit"  value="preview" class="btn purple btn-primary sbold" > Preview and Submit </button>                                                                
                                            </div>
                                        </div>
                                    </form>
                                </div>                                                          
                            </div>
                        </div>
                    </div>

                </div>
            </div>                                            
        </div>
    </div>
</div>
<jsp:include page="include/new_include/footer.jsp" />
<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="vpn_form2" method="post">
        <jsp:include page="include/vpn_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade" id="modal_id" role="dialog">

    <div class="modal-dialog modal-lg">
        <!-- Modal content-->
        <div class="modal-content" id="renew_vpn">
            <div class="modal-header">
                <!--<h4 class="modal-title">Add/Delete Request</h4>-->
                <h4 class="modal-title">Add/Delete IP Address</h4>
                <button type="button" class="close" id="mod1"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">   
                <!--<form id="vpn_fatch_data" method="post">-->
                <div class="row">
                    <div class="col-md-6 mb-4">
                        <label>VPN REGISTRATION NO</label>
                        <!--                        <input type="text" value="" id="vpn_reg_no" name="vpn_reg_no" class="form-control" placeholder="Enter VPN Registration Number"/>-->
                        <select class="form-control" id="vpn_reg_no" name="vpn_reg_no">
                            <option value="-select-">
                                -Select-
                            </option>
                        </select>
                    </div>
                    <div class="col-md-2 mt-2">
                        <!--                        <button id="vpn_fatch_data_submit"  class="btn btn-primary mt-4">Search</button>-->
                    </div> 
                </div>

                <div id="vpn_access_details" >
                    <form id="vpn_renew_form" >
                    </form>
                    <form id="vpn_renew_form2" method="post">
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <!--                <button class="btn btn-danger" onclick="window.location.reload();" data-dismiss="modal" aria-hidden="true">Close</button>-->

                <div id="vpn_renew_button" style="display: inline">

                </div>
                <div id="vpn_edit_button" style="display: inline">
                </div>
                <div id="vpn_ipadd_button" style="display: inline">

                </div>
            </div>
        </div>

    </div>

</div>


<div class="modal fade" id="modal_id_renew" role="dialog">
    <div class="modal-dialog modal-lg">
        <!-- Modal content-->
        <div class="modal-content" id="renew_vpn">
            <div class="modal-header">
                <h4 class="modal-title">Renew Request</h4>
                <button type="button" class="close" id="mod2"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">   
                <!--<form id="vpn_fatch_data" method="post">-->
                <div class="row">
                    <div class="alert alert-success d-none" id="vpn_renew_msg_renew"></div>
                    <div class="col-md-6 mb-4">
                        <label>SELECT VPN REGISTRATION NO</label>
                        <!--<input type="text" value="" id="vpn_reg_no_renew" name="vpn_reg_no" class="form-control" placeholder="Enter VPN Registration Number"/>-->
                        <select class="form-control" id="vpn_reg_no_renew" name="vpn_reg_no">
                            <option value="-select-">
                                -Select-
                            </option>
                        </select>
                    </div>
                    <div class="col-md-2 mt-2">
                        <!--                        <button id="vpn_fatch_data_submit_renew"  class="btn btn-primary mt-4">Search</button>-->
                    </div> 
                </div>
                <div id="vpn_access_details_renew" >
                    <form id="vpn_renew_form_renew" >
                    </form>
                    <form id="vpn_renew_form2_renew" method="post">
                    </form>
                </div>
            </div>
            <div class="modal-footer">

                <!--                <button class="btn btn-danger" onclick="window.location.reload();" data-dismiss="modal" aria-hidden="true">Close</button>-->
                <div id="vpn_renew_button_renew" style="display: inline">
                </div>
                <div id="vpn_ipadd_button_renew" style="display: inline">
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="modal_id_surrender" role="dialog">
    <div class="modal-dialog modal-lg">
        <!-- Modal content-->
        <div class="modal-content" id="surrender_vpn">
            <div class="modal-header">
                <h4 class="modal-title">Surrender Request</h4>
                <button type="button" class="close" id="mod3"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">   
                <!--<form id="vpn_fatch_data" method="post">-->
                <div class="row">
                    <div class="alert alert-success d-none" id="vpn_renew_msg_surrender"></div>
                    <div class="col-md-6 mb-4">
                        <label>SELECT VPN REGISTRATION NO</label>
                        <!--<input type="text" value="" id="vpn_reg_no_renew" name="vpn_reg_no" class="form-control" placeholder="Enter VPN Registration Number"/>-->
                        <select class="form-control" id="vpn_reg_no_surrender" name="vpn_reg_no">
                            <option value="-select-">
                                -Select-
                            </option>
                        </select>
                    </div>
                    <div class="col-md-2 mt-2">
                        <!--                        <button id="vpn_fatch_data_submit_surrender"  class="btn btn-primary mt-4">Search</button>-->
                    </div> 
                </div>
                <div id="vpn_access_details_surrender" >
                    <form id="vpn_renew_form_surrender" >
                    </form>
                    <form id="vpn_renew_form2_surrender" method="post">
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <!--                <button class="btn btn-danger" data-dismiss="modal" aria-hidden="true">Close</button>-->
                <div id="vpn_renew_button_surrender" style="display: inline">
                </div>
                <div id="vpn_ipadd_button_surrender" style="display: inline">
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="modal_id_vpn_single" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content" id="renew_vpn">
            <div class="modal-header">
                <h4 class="modal-title">Existed VPN Number</h4>
                <button type="button" class="close" data-dismiss="modal"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">   
                <!--<form id="vpn_fatch_data" method="post">-->
                <div class="row">
                    <div class="alert alert-success d-none" id="vpn_renew_msg_renew"></div>
                    <div class="col-md-6 mb-4">
                        <div  id="vpn_reg_no_single"> 
                        </div>
                    </div>
                </div>
                <div id="vpn_access_details_renew" >
                    <form id="vpn_renew_form_renew" >
                    </form>
                    <form id="vpn_renew_form2_renew" method="post">
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-danger" data-dismiss="modal" aria-hidden="true">OK</button>
                <div id="vpn_renew_button_renew" style="display: inline">
                </div>
                <div id="vpn_ipadd_button_renew" style="display: inline">
                </div>
            </div>
        </div>
    </div>
</div>
<!-- /.modal -->

<!--Nested Modal -->
<div class="modal fade" id="stack2" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Terms and conditions</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">                           
                <b>NOTE: Please read all instructions carefully and select the required services.</b>
                <br/>
                <br />
                <ul>
                    <li>Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to <b>servicedesk@nic.in</b></li>
                    <li>NIC does not capture any aadhaar related information.</li>
                </ul>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- Nested Modal-->
<!-- Modal for last submission -->
<div class="modal fade" id="stack3" tabindex="-1">
    <form id="vpn_form_confirm">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
</div>
<div class="modal fade" id="stack4" tabindex="-1">
    <form id="vpn_renew_confirm">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
</div>
<div class="modal fade" id="stack5" tabindex="-1">
    <form id="vpn_surrender_confirm">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
</div>
<div class="modal fade" id="stack6" tabindex="-1">
    <form id="vpn_delete_confirm">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
</div>

<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/vpn_newfile.js" type="text/javascript"></script>   
<script src="main_js/vpn.js" type="text/javascript"></script>   
<script type="text/javascript">
    $(document).ready(function () {
        $("#refresh").on("click", function () {
            $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#closebtn").on("click", function () {

            $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#imgtxt').val("");
        });
    });
</script><%
    String json = new Gson().toJson(session.getAttribute("vpn_coo_select"));
    String json1 = new Gson().toJson(session.getAttribute("vpn_state_coo_select"));
%>

<script type="text/javascript">
    var cordinatorArray = '<%=json%>';
    cordinatorArray = JSON.parse(cordinatorArray);

    var statecordinatorArray = '<%=json1%>';
    statecordinatorArray = JSON.parse(statecordinatorArray);
    $(document).ready(function () {
    
        if (cordinatorArray !== null && statecordinatorArray !== null)
        {
         
         
       
            if (cordinatorArray.length > 0 && statecordinatorArray.length > 0)
            {
                $('#central_co').prop('checked', true);
                $('#coord_div').removeClass("display-hide");
                $('#co_chk1').removeClass("display-hide");
                $('#co_chk2').removeClass("display-hide");
                $('#sel_coo').removeClass("display-hide");
                $('#state_coo').addClass("display-hide");
                $.each(cordinatorArray, function (index, value) {
                    var coordval = /\((.*?)\)/g.exec(value);
                    $('#vpn_coo').append($('<option>', {
                        value: coordval[1],
                        text: value
                    }));
                });
            }
            if (cordinatorArray.length > 0)
            {
                console.log("111111111")
                $('#central_co').prop('checked', true);
                $('#coord_div').removeClass("display-hide");
                $('#co_chk1').removeClass("display-hide");
                $('#sel_coo').removeClass("display-hide");
                $('#state_coo').addClass("display-hide");
                // alert(cordinatorArray)
                $.each(cordinatorArray, function (index, value) {
                    var coordval = /\((.*?)\)/g.exec(value);

                    $('#vpn_coo').append($('<option>', {
                        value: coordval[1],
                        text: value
                    }));
                });

            } else {
                $('#coord_div').addClass("display-hide");
                $('#co_chk1').addClass("display-hide");
            }

            if (statecordinatorArray.length > 0)
            {
                $('#state_co').prop('checked', true);
                $('#coord_div').removeClass("display-hide");
                $('#co_chk2').removeClass("display-hide");
                $('#sel_coo').addClass("display-hide");
                $('#state_coo').removeClass("display-hide");
                $.each(statecordinatorArray, function (index, value) {
                    var coordval = /\((.*?)\)/g.exec(value);
                    $('#vpn_state_coo').append($('<option>', {
                        value: coordval[1],
                        text: value
                    }));
                });
            } else {
                $('#co_chk2').addClass("display-hide");
            }
        } else if (cordinatorArray !== null)
        {
            console.log("2122222222222222")
        }
    });
    $(document).on('click', 'input[name="req_co"]', function () {
        $('#vpn_state_coo').html("");
        $('#vpn_coo').html("");
        $('#vpn_state_coo').html("<option value='na'>-SELECT-</option>");
        $('#vpn_coo').html("<option value='na'>-SELECT-</option>");
        var a = $("input[name='req_co']:checked").val();

        if (a === "central_co")
        {
            if (cordinatorArray.length === 0)
            {
                $('#sel_coo').addClass("display-hide");
                $('#vpn_coo').append($('<option>', {
                    value: "na",
                    text: "select",
                    selected: "selected"
                }));
            } else {
                $('#sel_coo').removeClass("display-hide");
                $('#state_coo').addClass("display-hide");
                $.each(cordinatorArray, function (index, value) {
                    var coordval = /\((.*?)\)/g.exec(value);
                    $('#vpn_coo').append($('<option>', {
                        value: coordval[1],
                        text: value
                    }));
                });
            }
        } else {
            if (statecordinatorArray.length === 0)
            {
                $('#state_coo').addClass("display-hide");
                $('#vpn_state_coo').append($('<option>', {
                    value: "na",
                    text: "select",
                    selected: "selected"
                }));
            } else {
                $('#sel_coo').addClass("display-hide");
                $('#state_coo').removeClass("display-hide");
                $.each(statecordinatorArray, function (index, value) {
                    var coordval = /\((.*?)\)/g.exec(value);
                    $('#vpn_state_coo').append($('<option>', {
                        value: coordval[1],
                        text: value
                    }));
                });
            }
        }
    })
</script>

