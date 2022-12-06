
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="relay_preview_tab">
            <!--Modal body start-->
            <div class="portlet light ">
                <span class="alert alert-info">Relay Request Form</span>
            </div>

            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="panel-heading" style="background-color: grey;margin-top: 15px;">
                    <div class="panel-title" style="color:white;font-weight:bold;"></div>
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Relay Entry Details</h3>
                </div>
                <div id="relay_tab_preview2" >
                    <div class="mt-2 mb-3">
                        <label for="street"><strong>Request For:<span id="req_for_span"></span></strong></label>
                        <input type="hidden" name="req_" id="req_" value="">
                    </div>
                    <div class="row form-group" id="app_ip_prvw">
                        <div id="oldip" class="col-md-6 d-none">
                            <label for="street">Old Application IP  <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter the IP Address [e.g.: 164.100.X.X ]" type="text" name="old_relay_ip[]" id="old_relay_ip"  value="" maxlength="15">
                            <font style="color:red"><span id="relay_dup_ip_err1"></span></font>  
                            <div id="old_add_ip"></div>
                        </div>
                        <div class="col-md-6">
                            <label for="street">Application IP  <span style="color: red">*</span></label>
                            <input class="form-control class_name_prvw" placeholder="Enter the IP Address [e.g.: 164.100.X.X]" type="text" name="relay_ip[]" id="relay_ip"  value="" maxlength="15">
                            <font style="color:red"><span id="relay_ip_err"></span></font>
                             
                            <div id="add_ip"></div>
                            <font style="color:red"><span id="relay_dup_ip_err"></span></font>
                        </div>

                    </div>

                    <div class="row form-group">
                        <div class="col-md-4">
                            <label for="street">Application Name <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Application Name, [characters limit[50] only,dot(,),comma(,) whitespace allowed] " type="text" name="relay_app_name" id="relay_app_name"  value="" maxlength="50" />
                            <font style="color:red"><span id="app_name_err"></span></font>
                        </div>
                        <div class="col-md-4">
                            <label for="street">Application URL </label>
                            <input class="form-control" placeholder="Enter Application URL [e.g: (https://abc.com)]" type="text" name="relay_app_url" id="relay_app_url"  value="" maxlength="50" />
                            <font style="color:red"><span id="app_name_err"></span></font>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label for="street">Name of Division <span style="color: red">*</span></label>
                                <input class="form-control" placeholder="Enter Name of Division, [characters limit[50]  only, dot(,),comma(,) whitespace allowed]" type="text" name="division" id="division" value="" maxlength="50"  />
                                <font style="color:red"><span id="division_err"></span></font>
                            </div>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-6">
                            <label for="street">Operating System (Name, Version) <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Operating System (Name, Version), [Only characters,whitespace,comma(,),hypen(-) allowed]" type="text" name="os" id="os"  value="" maxlength="50" required/>
                            <font style="color:red"><span id="os_error"></span></font>
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
                        <div class="col-md-12 display-hide" id="server_other">
                            <label for="street">Enter server location <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Server Location [Alphanumeric,whitespace and [. , - # / ( ) ] allowed]" type="text" name="server_loc_txt" id="server_loc_txt"  value="" maxlength="100">
                            <font style="color:red"><span id="server_txt_err"></span></font>
                        </div>
                    </div>
                    <div id="not-prompt-in-caseofdelete">
                    <div class="row form-group">
                        <div class="col-md-6">
                            <div class="row form-group">
                                <div class="col-6">
                                    <label for="street">Port <span style="color: red">*</span></label>
                                    <div class="mt-2">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" value="25" name="smtp_port"  id="port_25" > Port 25
                                            <span></span>
                                        </label>&emsp;&emsp;
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" value="465" name="smtp_port" id="port_465"> Port 465
                                            <span></span>
                                        </label>

                                    </div>
                                    <font style="color:red"><span id="port_err"></span></font>
                                </div>
                                <div class="col-6 d-none" id="auth_id_div">
                                    <label for="">Auth ID <span style="color: red">*</span></label>
                                    <input type="text" name="relay_auth_id" id="auth_id" class="form-control" placeholder="Enter Auth ID">
                                    <font style="color:red"><span id="auth_id_err"></span></font>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label for="street">Sender ID <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Sender ID. Ex: no-reply@xyz.gov.in" type="text" name="relay_sender_id" id="relay_sender_id"  value="" maxlength="100" />
                            <font style="color:red"><span id="sender_id_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street">MX of the Domain </label>
                            <input class="form-control" placeholder="MX of the Domain" type="text" name="domain_mx" id="domain_mx" value="" maxlength="100" disabled="disabled">
                            <font style="color:red"><span id="domain_mx_err"></span></font>
                        </div>
                    </div>
                    <div class="row form-group mt-2">
                        <div class="col-md-6">
                            <label for="street">Total Number of mails to be sent daily (approx)</label>
                            <input class="form-control" placeholder="Total Number of mails to be sent daily" type="text" name="mailsent" id="mailsent"  value="" maxlength="100" />
                            <font style="color:red"><span id="total_mail_err"></span></font>
                        </div>
                        <div class="col-md-6 d-none" id="other-record-addition">
                            <label class="mb-3" for="street"><strong>Other Record Addition: </strong></label>
                            <div class="row">
                                <div class="col-md-4">
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                        <input type="checkbox" value="spf" name="spf" id="spftest"> SPF
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-4">
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                        <input type="checkbox" value="dkim" name="dkim" id="dkimtest"> DKIM
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-4">
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                        <input type="checkbox" value="dmarc" name="dmarc" id="dmarctest"> DMARC
                                        <span></span>
                                    </label>
                                </div>
                                <font style="color:red"><span id="record_err"></span></font>
                            </div>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-12 mt-2">
                            <div class="form-group">
                                <label for=""><b>Mail Type</b></label>
                                <div class="mt-2">
                                   <div class="row">
                                                    <div class="col-md-2 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="OTP Service through Email" name="otp_mail_service" id="otp_mail_service"
                                                                   > OTP Service through Email
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="Transactional Mails" name="mail_type_trans_mail" id="mail_type_trans_mail"> Transactional Mails
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="Registration Mails" name="mail_type_reg_mail" id="mail_type_reg_mail"> Registration Mails
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="Forgot id/password" name="mail_type_forgotpass" id="mail_type_forgotpass"> Forgot id/password
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-1 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="Alerts" name="mail_type_alert" id="mail_type_alert"> Alerts
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-1 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="Others" name="mail_type_other" id="mail_type_other"> Others
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 col-sm-6">
                                                        <input type="text" name="other_mail_type" id="other_mail_type" class="form-control d-none" placeholder="Other Mail type">
                                                    </div>
                                                </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <label for="" class='mb-3'><strong>Are you The point of contact of the application?</strong></label>
                            <div class="row form-group">
                                <div class="col-md-3">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" value="yes" name="point_contact" id="point_contact_1"> Yes
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-3">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" value="no" name="point_contact" id="point_contact_2"> No
                                        <span></span>
                                    </label>
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-3">
                                    <label for="street">Name  <span style="color: red">*</span></label>
                                    <input class="form-control " placeholder="Enter Name" type="text" name="point_name" id="point_name"  value="">
                                    <font style="color:red"><span id="point_name_error"></span></font>  
                                </div>
                                <div class="col-md-3">
                                    <label for="street">Email  <span style="color: red">*</span></label>
                                    <input class="form-control " placeholder="Enter Email Address" type="text" name="point_email" id="point_email"  value="">
                                    <font style="color:red"><span id="point_email_error"></span></font>
                                </div>
                                <div class="col-md-3">
                                    <label for="street">Mobile Number  <span style="color: red">*</span></label>
                                    <input class="form-control " placeholder="Enter Mobile Number" type="text" name="mobile_number" id="mobile_number"  value="">
                                    <font style="color:red"><span id="point_mobile_error"></span></font>
                                </div>
                                <div class="col-md-3">
                                    <label for="street">Landline Number</label>
                                    <input class="form-control " placeholder="Enter Landline Number (Optional)" type="text" name="landline_number" id="landline_number"  value="">
                                    <font style="color:red"><span id="point_tel_error"></span></font>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12 d-none">
                        <label for="" class='mb-3' id="domain-hosted-with-nic"><strong>Is domain hosted with nic</strong></label>
                        <div class="row form-group">
                            <div class="col-md-2">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" value="yes" name="is_hosted" id="is_hosted_1"> Yes
                                    <span></span>
                                </label>
                            </div>
                            <div class="col-md-2">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" value="no" name="is_hosted" id="is_hosted_2" checked="checked"> No
                                    <span></span>
                                </label>
                            </div>
                        </div>
                    </div>
                    </div>
                    

                
                    <div class="row form-group">
                        <div class="col-md-12">
                            <label for="">
                                Security Audit: Whether mail will be sent through any application or Hardware Device. <b>(If mail is sent through Hardware Device, Security Audit may be exempted.)</b>
                            </label>
                            <div class="row form-group">
                                <div class="col-md-4">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" value="Hardware" name="security_audit" id="security_audit_1"> Hardware
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-4">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" value="Software" name="security_audit" id="security_audit_2"> Software Application
                                        <span></span>
                                    </label>
                                </div>
                                
                                <div class="col-md-4">
                                    <input class="form-control security_exp_date" type="text" name="security_exp_date" id="security_exp_date" placeholder="Security Audit Expiry Date (To check validity)*" readonly="">
                                    <font style="color:red"><span id="audit_date_err"></span></font>
                                </div>
                                <font style="color:red"><span id="audit_error"></span></font>
                            </div>

                        </div>
                        <div class="col-md-12" id="hardware_cert_div">
<!--                            <label for="street">Download Certificate <span style="color: red">*</span></label>-->
                            <div class="alert alert-secondary" >
                                <b>Exempted Certificate: </b>&emsp;<a href="hardware_download_1"><span id="hardware_uploaded_filename">download</span></a>
                            </div>                                                                                    
                        </div> 
                    </div>
                    <div class="row form-group">
                        <div class="col-md-12" id="cert_div">
<!--                            <label for="street">Download Certificate <span style="color: red">*</span></label>-->
                            <div class="alert alert-secondary" >
                                <b>Uploaded filename: </b>&emsp;<a href="download_1"><span id="uploaded_filename">download</span></a>
                            </div>                                                                                    
                        </div>        
                        <div class="col-md-12" id="cert_div1">
                            <label for="street">Staging Server? ( IP will be allowed maximum for 15 days ): <span style="color: red">*</span></label>
                            <span id="s_server">YES</span>                                                                                   
                        </div>  
                    </div>
                    <div class="row d-none" >
                        <div class="col-md-12">
                            <label for="street">Application should have Security audit clearance certificate, Upload certificate in <b>PDF</b> format (less than 1mb)<span style="color: red">*</span></label>

                            <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                                <input type="file" class="custom-file-input" id="cert_file1" name="cert_file1" >
                                <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                <span class="fileinput-filename"> </span> &nbsp;
                                <font style="color:red"><span id="file_err"> </span></font>
                            </div>
                        </div>                                                                            
                    </div>
                    </div>
                </div>
                <div class="form-group" id='tnc_div'>
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
