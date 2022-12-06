
<script>
    $(document).ready(function () {

        $('#migration_date1').datetimepicker({
            todayHighlight: true,
            autoclose: true,
            pickerPosition: 'bottom-right',
            format: 'mm/dd/yyyy hh:ii'
        });
    });
</script>

<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="dns_preview_tab">
            <!--Modal body start-->
            <div class="portlet light ">
                <span class="alert alert-info">DNS Request Form</span>
            </div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup"><span id="req_prvw">New Request:</span> DNS Entry Details</h3>
                </div>
                <div id="dns_tab_preview2" >
                    <input type="hidden" id="req_action" value="">


                    <div class="row display-hide form-group" id="first_div">
                        <div class="col-md-3" style="padding-right:0px;">
                            <label for="street" id="dns_url_label">DNS URL  </label><span style="color: red;">*</span>
                            <input class="form-control class_name_url1" placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="dns_url[]" id="dns_url_1"  value="" maxlength="100" >
                            <div class=""><div id="add_dns"></div></div>
                            <font style="color:red"><span id="dns_url_err"></span></font>
                        </div>
                        <div class="col-md-3" style="padding-right:0px;">
                            <label for="street" id="dns_cname">CNAME  </label>
                            <input class="form-control class_name_dns class_name_url" placeholder="Enter CNAME e.g : www.demo.nic.in or demo.gov.in" type="text" name="dns_cname[]" id="dns_cname_1"  value="" maxlength="100" style="display:inline;" >
                            <div class=""><div id="add_cname"></div></div>
                            <font style="color:red"><span id="dns_cname_error"></span></font>
                        </div>
                        <div class="col-md-3 display-hide" id="req_old_ip" style="padding-right:0px;">
                            <label for="street" id="old_ip_label"> Old Application IP/A/AAAA  </label>
                            <input class="form-control class_name_dns class_name_url" placeholder="Enter Old IP e.g : 164.100.X.X or IPV6 Address" type="text" name="dns_old_ip[]" id="dns_oldip_1"  value="" maxlength="100" style="display:inline;" >
                            <div class=""><div id="addoldip"></div></div>
                            <font style="color:red"><span id="dns_old_ip_error"></span></font>
                        </div>
                        <div class="col-md-3 display-hide" id="req_new_ip">
                            <label for="street" id="new_ip_label">New Application IP/A/AAAA </label>
                            <input class="form-control class_name_loc" placeholder="Enter New IP e.g : 164.100.X.X or IPV6 Address" type="text" name="dns_new_ip[]" id="dns_newip_1"  value="" maxlength="100" style="display:inline;">
                            <div class=""><div id="addnewip"></div></div>
                            <font style="color:red"><span id="dns_new_ip_error"></span></font>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-6">
                            <label for="street">Migration Date & Time</label>
                            <div class="row form-group">
                                <div class='col-md-6'>
                                    <div class="input-group date">
                                        <input type="text" class="form-control" placeholder="Start Date & Time" id="migration_date1" name="migration_date" readonly>
                                        <div class="input-group-append">
                                            <span class="input-group-text">
                                                <i class="la la-clock-o glyphicon-th"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <font style="color:red"><span id="event_date_err"></span></font>
                        </div>

                    </div>

                    <div class="row form-group">
                        <div class="col-md-4">
                            <label for="street">WEB Server Location  </label><span style="color: red;">*</span>
                            <input class="form-control class_name_loc1" placeholder="Alphanumeric,whitespace and [. , - # / ( ) ]" type="text" name="dns_loc" id="dns_loc_1"  value="" maxlength="100" >
                            <font style="color:red"><span id="dns_loc_error"></span></font>
                        </div>
                        <div class="col-md-4">
                            <input type="hidden" id="stat_type" value="">
                            <label for="street">Employee Code </label><span style="color: red;">*</span>
                            <input class="form-control" placeholder="[Only characters and digits allowed]" type="text" name="user_empcode" id="user_empcode"  value="" maxlength="12">
                            <font style="color:red"><span id="userempcode_error"></span></font>
                        </div>




                    </div>

                    <input type="hidden" name="url" value="web_url" /> <!--  above code commented this added by pr on 20thmar19 -->
                    <div class="row display-hide form-group" id="second_div">
                        <div class="col-md-12">
                            <label for="street"><strong>Additional Services: </strong></label>
                        </div>
                        <div class="col-md-4">
                            <div class="mt-checkbox-list">
                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                    <input type="checkbox"  name="request_mx_chk" value="mx" id="mx_1" readonly="readonly"> MX
                                    <span></span>
                                </label>
                                <input type="text" name="request_mx" id="request_mx_1" class="form-control addons display-hide" placeholder="[Alphanumeric,[ . , - / ] and whitespace] allowed">
                                <font style="color:red"><span id="request_mx_err"></span></font>
                                <br>
                                <input type="text" name="request_mx1" id="request_mx_2" class="form-control addons1 display-hide" placeholder="[Alphanumeric,[ . , - / ] and whitespace] allowed">
                                <font style="color:red"><span id="request_mx_err1"></span></font>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="mt-checkbox-list">
                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                    <input type="checkbox" name="request_ptr_chk" value="ptr" id="ptr_1"> PTR
                                    <span></span>
                                </label>
                                <input type="text" name="request_ptr" id="request_ptr_1" class="form-control addons display-hide" placeholder="Enter the IPV4/IPV6 Address">
                                <font style="color:red"><span id="request_ptr_err"></span></font>
                                <br>
                                <input type="text" name="request_ptr1" id="request_ptr_2" class="form-control addons1 display-hide" placeholder="Enter the IPV4/IPV6 Address">
                                <font style="color:red"><span id="request_ptr_err1"></span></font>
                            </div>
                        </div>
                        <input type="hidden" id="req_action1" value="">
                        <div class="col-md-4">
                            <div class="mt-checkbox-list">
                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                    <input type="checkbox" name="request_srv_chk" value="srv" id="srv_1" readonly="readonly"> SRV
                                    <span></span>
                                </label>
                                <input type="text"  name="request_srv" id="request_srv_1"  class="form-control addons display-hide" placeholder="Enter SRV value">
                                <font style="color:red"><span id="request_srv_err"></span></font>
                            </div>
                        </div>
                    </div>
                    <div class="form-group display-hide" id="third_div">
                        <div class="row form-group">
                            <div class="col-md-4">
                                <div class="mt-checkbox-list">
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                        <input type="checkbox" name="request_spf_chk" value="spf" id="spf_1" readonly="readonly"> SRF
                                        <span></span>
                                    </label>
                                    <input type="text" name="request_spf" id="request_spf_1" class="form-control addons display-hide" placeholder="Enter SPF value">
                                    <font style="color:red"><span id="request_spf_err"></span></font>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                    <input type="checkbox" name="request_txt_chk" value='txt' id="txt_1" readonly="readonly"> TXT
                                    <span></span>
                                </label>
                                <input type="text" name="request_txt" id="request_txt_1" class="form-control addons display-hide" placeholder="Enter TXT value">
                                <font style="color:red"><span id="request_txt_err"></span></font>
                            </div>

                            <div class="col-md-4">
                                <div class="mt-checkbox-list">
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                        <input type="checkbox" name="request_dmarc_chk" value='dmarc' id="dmarc_1" readonly="readonly"> DMARC
                                        <span></span>
                                    </label>
                                    <input type="text" name="request_dmarc" id="request_dmarc_1" class="form-control addons display-hide" placeholder="Enter DMARC value">
                                    <font style="color:red"><span id="request_dmarc_err"></span></font>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" id="upload_dns"> 
                <div class="col-md-6">
                    <label>Please Select Your File</label>
                    <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                        <input type="file" class="custom-file-input" name="user_file" id="user_file">
                        <label class="custom-file-label text-left" for="cert_file">Select File</label>
                        <span class="fileinput-filename"> </span> &nbsp;
                        <font style="color:red"><span id="file_err"> </span></font>
                    </div>
                    <font style="color:red"><span id="file_err"> </span></font>
                    <input type="hidden" id="cert" value="true">
                </div>
                <div id="dns_bulk_preview" class="col-md-6 display-hide">
                    <label for="street"></label>
                    <div class="alert alert-info">
                        <b>Uploaded filename: </b>&emsp;<a href="download_1" style="color: #fbc34cd9;font-weight: 700;text-decoration: underline;"><span id="uploaded_filename">Download</span></a>
                    </div>
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
        <div class="modal-footer">
            <button type="button" class="btn btn-default btn-outline" data-dismiss="modal" id="closebtn">Close</button>
            <a href="javascript:void(0);" class="btn btn-warning edit " >Edit</a>
            <button type="button" class="btn red btn-success save-changes" id="confirm">Submit</button>
        </div> 
    </div>
</div>