
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="changeip_preview_tab">
            <!--Modal body start-->
            <div class="portlet light ">
                <span class="alert alert-info">IP - Add/Change Request Form</span>
            </div>

            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">IP - Add/Change Request Details</h3>
                </div>

                <div class="row mt-2 mb-3">
                    <div class="col-md-12">
                        <label for="street"><b>REQUEST:</b> Service request for <span style="color: red">*</span></label>
                        <div class="col-sm-offset-1">
                            <!-- 28-apr-2022
                            <div class="radio" style="display: inline-block;">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio"  name="req_for" id="req_for_ldapauth" value="ldap" autofocus /> LDAP AUTH
                                    <span></span>
                                </label>&emsp;&emsp;
                            </div>
                            <div class="radio" style="display: inline-block;">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio"  name="req_for" id="req_for_relay" value="relay" /> RELAY
                                    <span></span>
                                </label>&emsp;&emsp;
                            </div>
                            -->
                            <div class="radio" style="display: inline-block;">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio"   name="req_for" id="req_for_sms" value="sms"  /> SMS
                                    <span></span>
                                </label>&emsp;&emsp;
                            </div>
                            <font style="color:red"><span id="show_req_for"></span></font>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 display-hide" id="sms_account" >
                        <div class="form-group">
                            <label for="street">Account Name <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="account_name" id="account_name" value="" maxlength="15" />
                            <font style="color:red"><span id="account_name_error"></span></font>
                        </div>
                    </div>
                    <div class="col-md-6 display-hide" id="relay_app_div">
                        <div class="form-group">
                            <label for="street">Application Name <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Application Name, [characters only limit[50],dot(,),comma(,) whitespace allowed]" type="text" name="relay_app" id="relay_app"  value="" maxlength="50" />
                            <font style="color:red"><span id="app_name_error"></span></font>
                        </div>
                    </div>
                    <div class="col-md-6 display-hide" id="relay_app_ip">
                        <div class="form-group">
                            <label for="street">Old IP Address </label>
                            <input class="form-control class_name_prvw" placeholder="Enter IP Address [e.g. 10.1.1.1]" type="text" name="relay_old_ip" id="relay_old_ip"  value="" maxlength="15" />
                            <font style="color:red"><span id="relay_app_ip_error"></span></font>
                        </div>
                    </div>
                    <div class="col-md-3 display-hide" id="ldap_account">
                        <div class="form-group">
                            <label class="control-label" for="street">Account Name <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="ldap_account_name" id="ldap_account_name"  value="" autocomplete="off" />
                            <font style="color:red"><span id="ldap_account_name_error"></span></font>
                        </div>

                    </div>
                    <div class="col-md-3 display-hide" id="ldap_url">
                        <div class="form-group">
                            <label class="control-label" for="street"> URL of the application<span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="ldap_url" id="ldap_url"  value=""  autocomplete="off" />
                            <font style="color:red"><span id="ldap_url_error"></span></font>
                        </div>
                    </div>
                    <div class="col-md-3 display-hide" id="ldap_id">
                        <div class="form-group">
                            <label class="control-label" for="street"> LDAP auth id allocated:<span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="ldap_auth_allocate" id="ldap_auth_allocate"  value=""  autocomplete="off" />
                            <font style="color:red"><span id="ldap_alocate_error"></span></font>
                        </div>
                    </div>
                </div>
                <div class="row display-hide form-group" id="server_div">
                    <div class="col-md-6">
                        <label for="street">Server Location<span style="color: red">*</span></label>
                        <select name="server_loc" class="form-control" id="server_loc">
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
                        <input class="form-control" placeholder="Enter Server Location [Alphanumeric,whitespace and [. , - # / ( ) ] allowed]" type="text" name="server_loc_txt" id="server_loc_txt"  value="" maxlength="100">
                        <font style="color:red"><span id="server_txt_err"></span></font>
                    </div>
                </div>
                <div id="change_ip_div" class="display-hide" >
                    <div class="row  form-group">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="street">OLD IP Address 1 <span style="color: red">*</span></label>
                                <input class="form-control class_name_prvw" placeholder="OLD IP Address 1 [e.g. 10.1.X.X]" type="text" name="old_ip1" id="old_ip1" value="" maxlength="15" required />
                                <font style="color:red"><span id="old_ip1_error"></span></font>
                                <font style="color:red"><span id="old_ipp1_error"></span></font>    
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="street">NEW IP Address 1 <span style="color: red">*</span></label>
                                <input class="form-control class_name_prvw" placeholder="NEW IP Address 1 [e.g. 10.1.X.X]" type="text" name="new_ip1" id="new_ip1"  value="" maxlength="15" required />
                                <font style="color:red"><span id="new_ip1_error"></span></font>
                                <font style="color:red"><span id="new_ipp1_error"></span></font>
                            </div>
                        </div>
<!--                        <div class="col-md-2"  style="margin-top: 30px;" id="addremdi1" style="display: none;">
                            <button type="button" class="btn btn-primary btn-sm" value="abadd1" id="newbadd1" name="newbadd1" title="Add IP" onclick="show2rowchild();"><i class="fa fa-plus fa-2x"></i></button>
                        </div>-->
                    </div>
                    <div class="row  form-group" id="newdiv1ad1" style="display: none;">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="street">OLD IP Address 2 <span style="color: red"></span></label>
                                <input class="form-control class_name_prvw" placeholder="OLD IP Address 2 [e.g. 10.1.X.X]" type="text" name="old_ip2" id="old_ip2"  value="" maxlength="15"/>
                                <font style="color:red"><span id="old_ip2_error"></span></font>
                                <font style="color:red"><span id="old_ipp2_error"></span></font>  
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="street">NEW IP Address 2 <span style="color: red"></span></label>
                                <input class="form-control class_name_prvw" placeholder="NEW IP Address 2 [e.g. 10.1.X.X]" type="text" name="new_ip2" id="new_ip2" value="" maxlength="15" />
                                <font style="color:red"><span id="new_ip2_error"></span></font>
                                <font style="color:red"><span id="new_ipp2_error"></span></font>
                            </div>
                        </div>
<!--                        <div class="col-md-2" id="addremdi2" style="display: none;" style="margin-top: 30px;">
                            <button type="button" class="btn btn-primary btn-sm" value="abadd2" id="newbadd2" name="newbadd2" title="Add IP" onclick="show3rowchild();"><i class="fa fa-plus fa-2x"></i></button>
                            <button type="button" class="btn btn-warning btn-sm" value="abadd2" id="newbremo2" name="newbremo2" title="Remove IP" onclick="hide2rowchild();"><i class="fa fa-minus fa-2x"></i></button>
                        </div>-->
                    </div>

                    <div class="row" id="newdiv2ad2" style="display: none;">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="street">OLD IP Address 3 <span style="color: red"></span></label>
                                <input class="form-control class_name_prvw" placeholder="OLD IP Address 3 [e.g. 10.1.X.X]" type="text" name="old_ip3" id="old_ip3" value="" maxlength="15"  />
                                <font style="color:red"><span id="old_ip3_error"></span></font>
                                <font style="color:red"><span id="old_ipp3_error"></span></font>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="street">NEW IP Address 3 <span style="color: red"></span></label>
                                <input class="form-control class_name_prvw" placeholder="NEW IP Address 3 [e.g. 10.1.X.X]" type="text" name="new_ip3" id="new_ip3"  value="" maxlength="15" />
                                <font style="color:red"><span id="new_ip3_error"></span></font>
                                <font style="color:red"><span id="new_ipp3_error"></span></font>
                            </div>
                        </div>
<!--                        <div class="col-md-2" id="addremdi3" style="display: none;" style="margin-top: 30px;">
                            <button type="button" class="btn btn-primary btn-sm" value="abadd3" id="newbadd3" name="newbadd3" title="Add IP" onclick="show4rowchild();"><i class="fa fa-plus fa-2x"></i></button>
                            <button type="button" class="btn btn-warning btn-sm" value="abadd3" id="newbremo3" name="newbremo3" title="Remove IP" onclick="hide3rowchild();"><i class="fa fa-minus fa-2x"></i></button>
                        </div>-->
                    </div>

                    <div class="row" id="newdiv3ad3" style="display: none;">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="street">OLD IP Address 4 <span style="color: red"></span></label>
                                <input class="form-control class_name_prvw" placeholder="OLD IP Address 4 [e.g. 10.1.X.X]" type="text" name="old_ip4" id="old_ip4"  value="" maxlength="15" />
                                <font style="color:red"><span id="old_ip4_error"></span></font>
                                <font style="color:red"><span id="old_ipp4_error"></span></font>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="street">NEW IP Address 4 <span style="color: red"></span></label>
                                <input class="form-control class_name_prvw" placeholder="NEW IP Address 4 [e.g. 10.1.X.X]" type="text" name="new_ip4" id="new_ip4" value="" maxlength="15" />
                                <font style="color:red"><span id="new_ip4_error"></span></font>
                                <font style="color:red"><span id="new_ipp4_error"></span></font>
                            </div>
                        </div>
<!--                        <div class="col-md-2" id="addremdi4" style="display: none;margin-top: 30px;">
                            <button type="button" value="abadd4" id="newbremo4" name="newbremo4" title="Remove IP" onclick="hide4rowchild();" class="btn btn-warning btn-sm"><i class="fa fa-minus fa-2x"></i></button>
                        </div>-->
                    </div>

                    <font style="color:red"><span id="ipduperr"></span></font> 
                </div>
                <div id="add_ip_div" class="display-hide">
                    <div class="row">
                        <div class="col-md-3">
                            <label for="street">IP Address 1 <span style="color: red">*</span></label>
                            <input class="form-control class_name_prvw" placeholder="IP Address 1 [e.g. 10.1.X.X]" type="text" name="add_ip1" id="add_ip1" value="" maxlength="15" required />
                            <font style="color:red"><span id="add_ip1_error"></span></font>
                        </div>
                        <div class="col-md-3">
                            <label for="street">IP Address 2 <span style="color: red"></span></label>
                            <input class="form-control class_name_prvw" placeholder="IP Address 2 [e.g. 10.1.X.X]" type="text" name="add_ip2" id="add_ip2" value="" maxlength="15" />
                            <font style="color:red"><span id="add_ip2_error"></span></font>
                        </div>
                        <div class="col-md-3">
                            <label for="street">IP Address 3 <span style="color: red"></span></label>
                            <input class="form-control class_name_prvw" placeholder="IP Address 3 [e.g. 10.1.X.X]" type="text" name="add_ip3" id="add_ip3" value="" maxlength="15" />
                            <font style="color:red"><span id="add_ip3_error"></span></font>
                        </div>
                        <div class="col-md-3">
                            <label for="street">IP Address 4 <span style="color: red"></span></label>
                            <input class="form-control class_name_prvw" placeholder="IP Address 4 [e.g. 10.1.X.X]" type="text" name="add_ip4" id="add_ip4"  value="" maxlength="15" />
                            <font style="color:red"><span id="add_ip4_error"></span></font>
                        </div>
                    </div>
                    <font style="color:red"><span id="ipduperr"></span></font>  
                </div>
                <div class="form-group mt-5 mb-3" id='tnc_div'>
                    <div class="mt-checkbox-list">
                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                            <input type="checkbox" name="tnc" id="tnc"> <b>I agree to <a data-toggle="modal" href="#stack2" style="color:blue">Terms and Conditions</a></b>
                            <span></span>
                        </label>
                        <font style="color:red"><span id="tnc_error"></span></font>
                    </div>
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