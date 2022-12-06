<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="wifiport_preview_tab">
            <!--Modal body start-->
            <div class="alert alert-primary"><span style="font-size: 16px;">WiFi Port Services Form</span></div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">WiFi Port Services Entry Details</h3>
                </div>
                <div class="row" id="first_div">
                    <div class="col-md-2" style="padding-right:0px;">
                        <label for="street" id="dns_url_label">Source IP </label><span style="color: red;">*</span>
                        <input class="form-control class_name_url1" placeholder="Enter Source IP e.g : 164.100.X.X or IPV6 Address" type="text" name="wifiport_sourceip[]" id="wifiport_sourceip_1"  value="" maxlength="100" >
                        <div class=""><div id="add_sourceip"></div></div>
                        <font style="color:red"><span id="source_ip_error"></span></font>

                    </div>
                    <div class="col-md-2" style="padding-right:0px;">
                        <label for="street" id="dns_url_label">Destination IP </label><span style="color: red;">*</span>
                        <input class="form-control class_name_dns class_name_url" placeholder="Enter Destination IP e.g : 164.100.X.X or IPV6 Address" type="text" name="wifiport_destinationip[]" id="wifiport_destinationip_1"  value="" maxlength="100"  >
                        <div class=""><div id="add_designationip"></div></div>
                        <font style="color:red"><span id="destination_ip_error"></span></font>
                    </div>
                    <div class="col-md-2" id="req_old_ip" style="padding-right:0px;">
                        <label for="street" id="dns_url_label">Service </label><span style="color: red;">*</span>
                        <select class='form-control' id="wifiport_service_1" name="wifiport_service[]">
                            <option value=''>-SELECT-</option>
                            <option value='tcp'>TCP</option>
                            <option value='udp'>UDP</option>
                            <option value='icmp'>ICMP</option>
                        </select>
                        <!--                        <input class="form-control class_name_dns class_name_url" placeholder="Enter Service e.g : " type="text" name="firewall_service[]" id="firewall_service_1"  value="" maxlength="100"  >
                        -->
                        <div class=""><div id="add_service"></div></div>
                        <font style="color:red"><span id="service_ip_error"></span></font>

                    </div>
                    <div class="col-md-2" id="req_new_ip" style="padding-right:0px;">
                        <label for="street" id="dns_url_label">Ports </label><span style="color: red;">*</span>
                        <input class="form-control class_name_loc" placeholder="EnterPorts e.g : 443" type="text" name="wifiport_port[]" id="wifiport_port_1"  value="" maxlength="100" >
                        <div class=""><div id="add_port"></div></div>
                        <font style="color:red"><span id="port_error"></span></font>

                    </div>
                    <div class="col-md-2" id="req_new_ip" style="padding-right:0px;">
                        <label for="street" id="dns_url_label">Action </label><span style="color: red;">*</span>
                        <select class='form-control' id="wifiport_action_1" name="wifiport_action[]">
                            <option value=''>-SELECT-</option>
                            <option value='permit'>Permit</option>
                            <option value='deny'>Deny</option>
                        </select>
                        <!--                        <input class="form-control class_name_loc" placeholder="Enter Action e.g :" type="text" name="firewall_action[]" id="firewall_action_1"  value="" maxlength="100" >
                        -->

                        <div class=""><div id="add_action"></div></div>
                        <font style="color:red"><span id="action_error"></span></font>

                    </div>
                      <div class="col-md-2" id="req_new_ip" style="padding-right:0px;">
                        <label for="street" id="dns_url_label">Time period</label><span style="color: red;">*</span>
                        <select class='form-control' id="wifiport_timePeriod_1" name="wifiport_time[]">
                            <option value=''>-SELECT-</option>
                            <option value='1day'>1 day</option>
                            <option value='3days'>3 days</option>
                             <option value='1week'>1 week</option>
                            <option value='1months'>1 month</option>
                            <option value='3months'>3 months</option>
                        </select>
                        <!--                        <input class="form-control class_name_loc" placeholder="Enter Action e.g :" type="text" name="firewall_action[]" id="firewall_action_1"  value="" maxlength="100" >
                        -->

                        <div class=""><div id="add_time"></div></div>
                        <font style="color:red"><span id="action_error"></span></font>

                    </div>
<!--                    <div class="col-md-2" id="req_new_ip" style="padding-right:0px;">
                        <label for="street" id="dns_url_label">Time period </label><span style="color: red;">*</span>
                        <input class="form-control class_name_loc" placeholder="Enter Time Period e.g : " type="text" name="wifiport_time[]" id="wifiport_timePeriod_1"  value="" maxlength="100" style="width:85%;display:inline;">
                        <div class=""><div id="add_time"></div></div>
                        <font style="color:red"><span id="time_error"></span></font>
                    </div>-->
                </div>
                 <div class="row">
                    <div class="col-md-6" id="" style="padding-right:0px;">
                        <label for="street" id="dns_url_label">Purpose</label><span style="color: red;">*</span>
                        <input class="form-control" value="" placeholder="Purpose" type="text" name="remarks" id="remarks" maxlength="100"  aria-required="true">
                        <font style="color:red"><span id="purpose_error"></span></font>
                    </div>
                </div>
                <div class="row display-hide" id="submit">
                    <div class="col-md-offset-5 col-md-7">
                        <button name="submit"  value="preview" class="btn purple btn-outline sbold" > Preview and Submit </button>                                                                
                    </div>
                </div>
                <div class="form-group mt-4" id='tnc_div'>
                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand mt-5">
                        <input type="checkbox"  name="tnc" id="tnc"> <b>I agree to <a data-toggle="modal" href="#stack2" style="color:blue">Terms and Conditions</a></b>
                        <span></span>
                    </label>
                    <font style="color:red"><span id="tnc_error"></span></font>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn dark btn-default" data-dismiss="modal" id="closebtn">Close</button>
            <a href="javascript:void(0);" class="btn btn-warning edit " >Edit</a>
            <button type="button" class="btn red btn-primary save-changes" id="confirm">Submit</button>
        </div> 
    </div>
</div>