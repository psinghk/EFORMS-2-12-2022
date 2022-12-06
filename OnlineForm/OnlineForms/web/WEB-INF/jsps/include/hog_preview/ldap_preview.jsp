
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="ldap_preview_tab">
            <!--Modal body start-->
            <div class="portlet light ">
                <div class="portlet-title">
                    <div class="caption font-red-sunglo">
                        <i class="icon-settings font-red-sunglo"></i>
                        <span class="alert alert-info">LDAP Request Form</span>
                    </div>
                </div>
            </div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Ldap Request Details</h3>
                </div>
                <div class="row form-group">
                    <div class="col-md-6">
                        <label for="street">Name of the Application <span style="color: red">*</span></label>
                        <input class="form-control" value="" placeholder="Enter Name of the Applicaion [characters,dot(.) and whitespace]" type="text" name="app_name" id="app_name" maxlength="50"  aria-required="true">
                        <font style="color:red"><span id="app_name_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">Application URL <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Application URL [e.g: (https://abc.com)] " type="text" name="app_url" id="app_url" value="" maxlength="100"  aria-required="true">                                                                                        
                        <font style="color:red"><span id="app_url_err"></span></font>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-md-6">
                        <label for="street"><strong>IP1</strong> from which you will access LDAP Server <span style="color: red">*</span>
                            <a href="#myModal1" data-toggle="modal"  style="color:blue">(Know Your IP<i class="entypo-help"></i>)</a></label>
                        <input class="form-control" placeholder="Enter Application IP1 [e.g: 10.10.10.10]" type="text" name="base_ip" id="base_ip" value="" maxlength="20"  aria-required="true">
                        <font style="color:red"><span id="base_ip_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street"><strong>IP2</strong> from which you will access LDAP Server</label>
                        <input class="form-control" placeholder="Enter Application IP2 [e.g: 10.10.10.10]" type="text" name="service_ip" id="service_ip" maxlength="20">
                        <font style="color:red"><span id="service_ip_err"></span></font>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-md-6">
                        <label for="street">Domain/Group Of People who will access this application<span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Only [Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" type="text" name="domain" id="domain" maxlength="50">
                        <font style="color:red"><span id="domain_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">Server Location<span style="color: red">*</span></label>
                        <select name="server_loc" class="form-control" id="server_loc">
                            <option value="NDC Delhi">NDC Delhi</option>
                            <option value="NDC Pune">NDC Pune</option>
                            <option value="NDC Hyderabad">NDC Hyderabad</option>
                            <option value="NDC Bhubaneswar">NDC Bhubaneswar</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                </div>
                <div class="row display-hide" id="server_other">
                    <div class="col-md-6">
                    </div>
                    <div class="col-md-6">
                        <label for="street">Enter server location <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Server Location [Alphanumeric,whitespace and [. , - # / ( ) ] allowed]" type="text" name="server_loc_txt" id="server_loc_txt"  value="" maxlength="100">
                        <font style="color:red"><span id="server_txt_err"></span></font>
                    </div>
                </div>
                <div class="row" >
                    <div class="col-md-6">
                        <label for="street">Is the application enabled over https: <span style="color: red">*</span></label>
                        <div id="ldap_https">
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="https" id="https_1"  value="yes" checked=""> Yes
                                <span></span>
                            </label>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="https" id="https_2"  value="no" > No
                                <span></span>
                            </label>
                        </div>
                        <font style="color:red"><span id="https_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">Is the application security audit cleared: <span style="color: red">*</span></label>
                        <div id="ldap_audit">
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="audit" id="audit_1"  value="yes" checked=""> Yes
                                <span></span>
                            </label>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="audit" id="audit_2"  value="no"> No
                                <span></span>
                            </label>
                        </div>
                        <font style="color:red"><span id="audit_err"></span></font>
                    </div>
                </div>
                <!--                <div class="row"  id="cert1_div">
                                    <div class="col-md-12">
                                        <label for="street">Application should have Security audit clearance certificate: <span style="color: red">*</span></label>
                                        <div class="alert alert-info" style="width: 96%;margin: 15px;background-color: #f5f5f5">
                                            <b>Uploaded filename: </b><a href="download_1"><span id="uploaded_filename">download</span></a>
                                        </div>                                                                                    
                                    </div>                                                                            
                                </div>-->
                <div class="row form-group" id="upload_ldap"> 
                    <div class="col-md-6 form-body">
                        <label>Security audit clearance certificate:</label>
                        <div class="custom-file">
                            <input type="file" class="custom-file-input" name="cert_file1" id="cert_file1">
                            <label class="custom-file-label text-left" for="cert_file">Select File</label>
                            <span class="fileinput-filename"> </span>
                            <font style="color:red"><span id="file_err"> </span></font>
                        </div>
                        <input type="hidden" id="cert" value="true">
                    </div>
                    <div class="col-md-6" id="cert1_div">
                        <div class="alert alert-secondary" style="margin-top: 21px;">
                            <b>Uploaded filename: </b>&ensp;<i class="fa fa-file-download" style="font-size: 17px;margin-right: 6px;color: #5867dd;"></i><a href="download_1" ><span id="uploaded_filename">download</span></a>
                        </div>  
                    </div>
                </div>
                <input type="hidden" id="cert" value="false" />
                <div class="row display-hide" id="no_audit_div">
                    <div class="col-md-6">
                        <label for="street">Enter Email id which will access ldap server<span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" type="text" name="ldap_id1" id="ldap_id1" value="" maxlength="50"  aria-required="true">
                        <font style="color:red"><span id="specific_id1_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">Enter Email id which will access ldap server</label>
                        <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" type="text" name="ldap_id2" id="ldap_id2" maxlength="50">
                        <font style="color:red"><span id="specific_id2_err"></span></font>
                    </div>
                </div>
                <div class="form-group" id='tnc_div'>
                    <div class="mt-checkbox-list">
                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                            <input type="checkbox"  name="tnc" id="tnc"> <b>I agree to <a data-toggle="modal" href="#stack2" style="color:blue">Terms and Conditions</a></b>
                            <span></span>
                        </label>
                        <font style="color:red"><span id="tnc_error"></span></font>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="javascript:void(0);" class="btn btn-warning green edit " >Edit</a>
            <button type="button" class="btn red btn-success btn-outline save-changes" id="confirm">Submit</button>
            <button type="button" class="btn btn-default dark btn-outline" data-dismiss="modal" id="closebtn">Close</button>
        </div>
    </div>
</div>
