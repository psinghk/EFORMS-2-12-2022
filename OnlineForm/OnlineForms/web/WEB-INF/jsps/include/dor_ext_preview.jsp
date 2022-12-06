<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="dor_ext_preview_tab">
            <!--Modal body start-->
            <span class="alert alert-info" style="font-size: 16px;font-weight: 500;"><span>Extend the Validity of Account Subscription Form</span></span>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div id="under_sec_div">
                    <jsp:include page="underSecretary_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Extend the Validity of Account Details</h3>
                </div>
                <div class="col-md-6">
                    <label for="street">Email address preference: <span style="color: red">*</span></label>
                    <div id="id_based">
                        <label class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="single_id_type" id="single_id_type_dor_1"  value="id_name" checked> Name Based
                            <span></span>
                        </label>&emsp;&emsp;
                        <label class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="single_id_type" id="single_id_type_dor_2"  value="id_desig" > Designation/Office based id
                            <span></span>
                        </label>&emsp;&emsp;
                    </div>
                    <font style="color:red"><span id="single_id_type_err"></span></font>
                </div>
                <div class="col-md-12" id="dor_desc">
                    <label for="street">Employee Description: <span style="color: red">*</span></label>
                    <div id="emp_type">
                        <label class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="single_emp_type" id="single_emp_type_dor1"  value="emp_regular" checked="" readonly> Govt/Psu Official
                            <span></span>
                        </label>&emsp;&emsp;
                        <label class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="single_emp_type" id="single_emp_type_dor2"  value="consultant" > Consultant/Contractual Staff
                            <span></span>
                        </label>&emsp;&emsp;
                        <label class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="single_emp_type" id="single_emp_type_dor3"  value="emp_contract"> FMS Support Staffs
                            <span></span>
                        </label>&emsp;&emsp;
                    </div>
                    <font style="color:red"><span id="single_emp_type_err"></span></font>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <label class="control-label" for="street">Email Address  (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                        <input class="form-control act_email1" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com OR abc.xyz@nic.in]" type="text" name="dor_email" id="dor_email" value="" readonly>
                        <font style="color:red"><span id="single_email1_err"></span></font>
                    </div>

                    <div class="col-md-6" id="divDor"> <label class="control-label" for="street">Previous Date Of Account Expiry  <span style="color: red">*</span></label>
                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="p_single_dor" id="p_email_single_dor" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly />
                        <font style="color:red"><span id="single_dor_err"></span></font>
                    </div>
                    
                     <div class="col-md-6" id="divDor"> <label class="control-label" for="street">Date Of Account Expiry  <span style="color: red">*</span></label>
                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dor" id="email_single_dor2" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly />
                        <font style="color:red"><span id="single_dor_err"></span></font>
                    </div>
                     <div class="col-md-6" id="divDor"> <label class="control-label" for="street">Date Of Birth  <span style="color: red">*</span></label>
                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dob" id="email_single_dob2" placeholder="Enter Date Of Birth [DD-MM-YYYY]" readonly />
                        <font style="color:red"><span id="single_dob_err"></span></font>
                    </div>
                    
                    <!--19-july-2022 anmol-->
<!--                    <label class="ml-3 my-3" for="street">Is retired: <span style="color: red">*</span></label>
                    <div class="row ml-3 my-3" id="emp_type">                              
                    <div class="col-md-4"><label class="k-radio k-radio--bold k-radio--brand">
                    <input type="radio" name="retired_no_yes" id="retired_yes1" class="retired_no_yes" value="yes" >Yes
                    <span></span>
                    </label>
                    </div>
                    <div class="col-md-4"><label class="k-radio k-radio--bold k-radio--brand">
                    <input type="radio" name="retired_no_yes" id="retired_no1" class="retired_no_yes" value="no" > NO
                    <span></span>
                    </label>
                    </div>
                    </div>-->
                </div>  
                <div class="row">
                  
                    <div>
                        <div class="col-md-6" id = "fileUploadDiv">
                            <label for="street">Application should have Security audit clearance certificate, Upload certificate in <b>PDF</b> format (less than 1mb)<span style="color: red">*</span></label>

                            <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                                <input type="file" class="custom-file-input" id="hardware_cert_file" name="hardware_cert_file">
                                <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                <span class="fileinput-filename"> </span> &nbsp;
                                <font style="color:red"><span id="hardware_file_err"> </span></font>
                            </div>
                             <input type="hidden" id="cert" value="">
                        </div>
                        <br>

                        <div class="col-md-6" id = "fileDownloadDiv">
                            <label for="street">Download Certificate <span style="color: red">*</span></label>
                            <div class="alert alert-secondary" >
                                <b>Uploaded filename: </b>&emsp;<a href="download_1"><span id="hardware_cert_file3">download</span></a>
                            </div>                                                                                    
                        </div>        


                    </div>

                </div>

                <!--  below div added by pr on 18thjul18  -->

                <div class="form-group mt-4" id='tnc_div'>
                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                        <input type="checkbox"  name="tnc" id="tnc"> <b>I agree to <a data-toggle="modal" href="#stack2" style="color:blue">Terms and Conditions</a></b>
                        <span></span>
                    </label>
                    <font style="color:red"><span id="tnc_error"></span></font>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-success edit" >Edit</button>
            <button type="button" class="btn red btn-warning save-changes" id="confirm" value="">Submit</button>
            <button type="button" class="btn dark btn-default" data-dismiss="modal" id="closebtn">Close</button>
        </div> 
    </div>
</div>
                
<!--<script src="main_js/onlineforms.js" type="text/javascript"></script>               -->


