<%@page import="com.org.bean.UserData"%>
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="wifi_preview_tab">
            <!--Modal body start-->
            <div class="alert alert-primary"><span style="font-size: 16px;">WIFI Request Form</span></div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <%
                    UserData userdata = (UserData) session.getAttribute("uservalues");
                    String otp_type = userdata.getVerifiedOtp();
                    boolean nic_employee = userdata.isIsNICEmployee();
                    if (!nic_employee) {%>
                <div id="forwarding_div" class="display-hide">
                    <div class="k-portlet__head">
                        <h3 class="theme-heading-h3-popup">Forwarding Officer Details</h3>
                    </div>
                    <span id="govdept"></span>
                    <div class="row"> 
                        <div class="col-md-6">
                            <label for="street">Email <span style="color: red">*</span></label>
                            <input class="form-control" style="text-transform:lowercase;" placeholder="Enter E-Mail Address [e.g: abc.xyz@zxc.com]" type="text" name="fwd_ofc_email" id="fwd_ofc_email" value=""  maxlength="50"  >
                            <font style="color:red"><span id="fwd_email_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street">Name  <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Name [characters,dot(.) and whitespace]" type="text" name="fwd_ofc_name" id="fwd_ofc_name" value="" maxlength="50">
                            <font style="color:red"><span id="fwd_name_err"></span></font>
                        </div>
                        <span id="nicemp"></span>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label for="street"> Mobile <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Mobile Number [e.g: +919999999999]" type="text" name="fwd_ofc_mobile" id="fwd_ofc_mobile" value="" maxlength="15">
                            <font style="color:red"><span id="fwd_mobile_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street">Telephone <span style="color: red">*</span></label>
                            <input class="form-control" style="text-transform:lowercase;" type="text" id="fwd_ofc_tel" name="fwd_ofc_tel" placeholder="Enter Reporting/Nodal/Forwarding Officer Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]" value="" maxlength="15">
                            <font style="color:red"><span id="fwd_tel_err"></span></font>
                        </div>
                        <span id="nicemp"></span>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Designation <span style="color: red">*</span></label>
                            <input type="text" id="fwd_ofc_design" name="fwd_ofc_design" placeholder="Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespace and [. , - &]]" maxlength="50" class="form-control"  value="" /> 
                            <font style="color:red"><span id="fwd_desig_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label>Address <span style="color: red">*</span></label>
                            <input type="text" id="fwd_ofc_add" name="fwd_ofc_add" placeholder="Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" maxlength="50" class="form-control" value="" /> 
                            <font style="color:red"><span id="fwd_add_err"></span></font>
                        </div>
                    </div>
                </div>
                <% }%>
                <div></div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">WIFI Request Details</h3>
                </div>
                <div id="nic_wifi_div">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="alert alert-secondary">
                                <b>NOTE:</b><br>
                                <ul style="margin-top: 15px;padding-left: 17px;">
                                    <li>Entries marked with asterisk  (<span style="color: red">*</span>) are mandatory</li>
                                    <li>Only three devices allowed per user ID.</li>                                                                                            
                                    <li>For iPhone/iPad/MAC, write <b>ios(version)</b> in Operating System.</li>
                                </ul>
                            </div>                                                                   
                        </div>
                    </div>

                    <div class="row" id="wifi_process_div"> 
                        <div class="col-md-6">
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input class="process" style="text-transform:lowercase;" placeholder="wifi request" type="radio" name="wifi_process" id="wifi_req" value="request" > Wifi Request
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input class="process" placeholder="wifi certificate" type="radio" name="wifi_process" id="wifi_cert" value="certificate"> Wifi Certificate
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input class="process" placeholder="wifi certificate" type="radio" name="wifi_process" id="wifi_delete" readonly value="req_delete"> Wifi Delete
                                <span></span>
                            </label>&emsp;&emsp;
                        </div>

                    </div>
                    <div id="wifi_req_div" class="">
                        <div class="row">
                            <div class="col-md-4">
                                <label for="street">MAC Address of the Device <span style="color: red">*</span></label>
                                <input class="form-control class_name_prvw" placeholder="Enter MAC Address (e.g: AA:AA:AA:AA:AA:AA)" type="text" autocomplete="off" name="wifi_mac1" id="wifi_mac1"  value="" maxlength="17">
                                <font style="color:red"><span id="wifi_mac1_err"></span></font>
                            </div>
                            <div class="col-md-4">
                                <label for="street">Operating System of the Device<span style="color: red">*</span></label>
                                <input class="form-control " placeholder="Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]" type="text" autocomplete="off" name="wifi_os1" id="wifi_os1"  value="">
                                <font style="color:red"><span id="wifi_os1_err"></span></font>
                            </div>
                            <div class="col-md-4">
                                <label for="street">Device Type<span style="color: red">*</span></label>
                                <select name="device_type1" class="form-control" id="device_type1">
                                    <option value="">select</option>
                                    <option id="Bio-Metric" value="Bio-Metric device">Bio-Metric device</option>
                                    <option id="Laptop" value="Laptop">Laptop</option>
                                    <option id="Phone" value="Phone">Phone</option>
                                    <option id="Windows" value="Windows">Windows</option>
                                    <option id="Other" value="Other">Other</option>
                                </select>
                                <font style="color:red"><span id="device_type_err1"></span></font>
                            </div>
                        </div>
                        <div class="row" id="wifi_mac2_div">
                            <div class="col-md-4">
                                <label for="street">MAC Address of the Device</label>
                                <input class="form-control class_name_prvw" placeholder="Enter MAC Address (e.g: AA:AA:AA:AA:AA:AA)" type="text" autocomplete="off" name="wifi_mac2" id="wifi_mac2"  value="" maxlength="17">
                                <font style="color:red"><span id="wifi_mac2_err"></span></font>
                            </div>
                            <div class="col-md-4">
                                <label for="street">Operating System of the Device</label>
                                <input class="form-control " placeholder="Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]" type="text" autocomplete="off" name="wifi_os2" id="wifi_os2"  value="">
                                <font style="color:red"><span id="wifi_os2_err"></span></font>
                            </div>
                            <div class="col-md-4">
                                <label for="street">Device Type</label>
                                <select name="device_type2" class="form-control" id="device_type2">
                                    <option value="">select</option>
                                    <option id="Bio-Metric" value="Bio-Metric device">Bio-Metric device</option>
                                    <option id="Laptop" value="Laptop">Laptop</option>
                                    <option id="Phone" value="Phone">Phone</option>
                                    <option id="Windows" value="Windows">Windows</option>
                                    <option id="Other" value="Other">Other</option>
                                </select>
                                <font style="color:red"><span id="device_type_err2"></span></font>
                            </div>
                        </div>
                        <div class="row" id="wifi_mac3_div">
                            <div class="col-md-4">
                                <label for="street">MAC Address of the Device</label>
                                <input class="form-control class_name_prvw" placeholder="Enter MAC Address (e.g: AA:AA:AA:AA:AA:AA)" type="text" autocomplete="off" name="wifi_mac3" id="wifi_mac3"  value="" maxlength="17">
                                <font style="color:red"><span id="wifi_mac3_err"></span></font>
                            </div>
                            <div class="col-md-4">
                                <label for="street">Operating System of the Device</label>
                                <input class="form-control " placeholder="Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]" type="text" autocomplete="off" name="wifi_os3" id="wifi_os3"  value="">
                                <font style="color:red"><span id="wifi_os3_err"></span></font>
                            </div>
                            <div class="col-md-4">
                                <label for="street">Device Type</label>
                                <select name="device_type3" class="form-control" id="device_type3">
                                    <option value="">select</option>
                                    <option id="Bio-Metric" value="Bio-Metric device">Bio-Metric device</option>
                                    <option id="Laptop" value="Laptop">Laptop</option>
                                    <option id="Phone" value="Phone">Phone</option>
                                    <option id="Windows" value="Windows">Windows</option>
                                    <option id="Other" value="Other">Other</option>
                                </select>
                                <font style="color:red"><span id="device_type_err3"></span></font>
                            </div>
                        </div>   
                        
                         <div class="row" id="wifi_mac4_div">
                            <div class="col-md-4">
                                <label for="street">MAC Address of the Device</label>
                                <input class="form-control class_name_prvw" placeholder="Enter MAC Address (e.g: AA:AA:AA:AA:AA:AA)" type="text" autocomplete="off" name="wifi_mac4" id="wifi_mac4"  value="" maxlength="17">
                                <font style="color:red"><span id="wifi_mac4_err"></span></font>
                            </div>
                            <div class="col-md-4">
                                <label for="street">Operating System of the Device</label>
                                <input class="form-control " placeholder="Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]" type="text" autocomplete="off" name="wifi_os4" id="wifi_os4"  value="">
                                <font style="color:red"><span id="wifi_os4_err"></span></font>
                            </div>
                            <div class="col-md-4">
                                <label for="street">Device Type</label>
                                <select name="device_type4" class="form-control" id="device_type4">
                                    <option value="">select</option>
                                    <option id="Bio-Metric" value="Bio-Metric device">Bio-Metric device</option>
                                    <option id="Laptop" value="Laptop">Laptop</option>
                                    <option id="Phone" value="Phone">Phone</option>
                                    <option id="Windows" value="Windows">Windows</option>
                                    <option id="Other" value="Other">Other</option>
                                </select>
                                <font style="color:red"><span id="device_type_err4"></span></font>
                            </div>
                        </div>  
                    </div>

                    <div id="wifi_DeletePrev_div" class="display-hide">


                    </div>
                </div>
                
                  <!--  below div added by pr on 19thdec19  -->
                        <div class="row final_id_span_cls display-hide">
                            <div class="col-md-12">
                                <label class="control-label" for="street"><strong>Wifi Value Assigned : </strong></label>
                                <span class="final_id_cls"></span>
                            </div>
                        </div>  
                
                 <div class="row"><!-- div added by pr on 2ndjan2020 -->
                            <div class="col-md-12">
                                <label class="control-label" for="street"><strong>Current LDAP Wifi Value  : </strong></label>
                                <span class="ldap_cls"></span>
                            </div>
                        </div>   
                  
                <font style="color:red"><span id="macduperr"></span></font>
                <div class="mt-checkbox-list mt-5" id="tnc_div">
                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                        <input type="checkbox"  name="tnc" id="tnc">&emsp;<b>I agree to <a data-toggle="modal" href="#stack2" style="color:blue">Terms and Conditions & Declarations</a></b>
                        <span></span>
                    </label>
                    <font style="color:red"><span id="tnc_error"></span></font>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn dark btn-default" data-dismiss="modal" id="closebtn">Close</button>
            <a href="javascript:void(0);" class="btn btn-warning edit " >Edit</a>
            <button type="button" class="btn red btn-success save-changes" id="confirm">Submit</button>
        </div> 
    </div>
</div>
