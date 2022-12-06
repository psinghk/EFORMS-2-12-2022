<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="bulk_preview_tab">
            <!--Modal body start-->
            <div class="alert alert-primary"><span style="font-size: 16px;">Bulk User Subscription Form</span></div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div id="under_sec_div">
                    <jsp:include page="underSecretary_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Bulk User Subscription Details</h3>
                </div>
                <div class="row form-group">
                    <div class="col-md-12">
                        <label class="control-label" for="street">Type of Mail ID: <span style="color: red">*</span></label>
                        <div>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="req_for" id="bulk_mail_1"  value="mail" > Mail user (with mailbox)
                                <span></span>
                            </label>&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="req_for" id="bulk_app_1"  value="app" > Application user (without mail box(Eoffice-auth)) <!-- line modified by pr on 5thmay2020  -->
                                <span></span>
                            </label>&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="req_for" id="bulk_eoffice_1"  value="eoffice"> e-office-srilanka
                                <span></span>
                            </label>&emsp;
                        </div>
                        <font style="color:red"><span id="req_for_err"></span></font>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-md-6">
                        <label class="control-label" for="street">Email address preference: <span style="color: red">*</span></label>
                        <div>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="single_id_type" id="bulk_id_type_1"  value="id_name" > Name Based
                                <span></span>
                            </label>&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="single_id_type" id="bulk_id_type_2"  value="id_desig" > Designation/Office based id
                                <span></span>
                            </label>&emsp;
                        </div>
                        <font style="color:red"><span id="bulk_id_type_err"></span></font>
                    </div>

                    <div class="col-md-6">
                        <label class="control-label" for="street">Employee Description: <span style="color: red">*</span></label>
                        <div id="emp_type">
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="single_emp_type" id="bulk_emp_type1"  value="emp_regular" > Govt/Psu Official
                                <span></span>
                            </label>&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="single_emp_type" id="bulk_emp_type2"  value="consultant" > Consultant
                                <span></span>
                            </label>&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="single_emp_type" id="bulk_emp_type3"  value="emp_contract" > FMS Support Staffs
                                <span></span>
                            </label>
                        </div>
                        <font style="color:red"><span id="single_emp_type_err"></span></font>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                      
                        <div class="alert alert-secondary">
                            <b>Uploaded filename: </b>&ensp;<a href="download_1"><span id="uploaded_filename">Download</span></a>
                        </div>                                                                                    
                    </div> 
                    <div class="col-md-6">
                       
                        <div class="alert alert-secondary">
                            <b>Valid File: </b>&ensp;<a href="download_valid"><span id="valid_filepath">Download Valid Records File</span></a>
                        </div>                                                                                    
                    </div>  
                </div>
                <div class="row">
                    <div class="col-md-6">
                      
                        <div class="alert alert-secondary">
                            <b>Invalid File: </b>&ensp;<a href="download_notvalid"><span id="notvalid_filepath">Download InValid Records File</span></a>
                        </div>                                                                                      
                    </div> 
                    <div class="col-md-6">
                       
                        <div class="alert alert-secondary">
                            <b>Error File: </b>&ensp;<a href="download_error"><span id="error_filepath">Download Error File</span></a>
                        </div>                                                                                    
                    </div>  
                </div>
                
                <div class="form-group" id='tnc_div'>
                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                        <input type="checkbox"  name="tnc" id="tnc"> <b>I agree to <a data-toggle="modal" href="#bulk-stack2" style="color:blue">Terms and Conditions</a></b>
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
