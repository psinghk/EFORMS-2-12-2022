<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="email_act_preview_tab">
            <!--Modal body start-->
            <span class="alert alert-info" style="font-size: 16px;font-weight: 500;"><span>Email Activate/Deactivate User Subscription Form</span></span>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div id="under_sec_div">
                    <jsp:include page="underSecretary_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Email Activate/Deactivate Subscription Details</h3>
                </div>




                <div class="row">
                    <div class="col-md-6">
                        <label class="control-label" for="street">Preferred Email Address 1 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                        <input class="form-control act_email1" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com OR abc.xyz@nic.in]" type="text" name="act_email1" id="act_email1" value="">
                        <font style="color:red"><span id="single_email1_err"></span></font>
                    </div>

                    <div class="col-md-6" id="divDor"> <label class="control-label" for="street">Date Of Retirement <span style="color: red">*</span></label>
                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dor" id="email_single_dor1" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly />
                        <font style="color:red"><span id="single_dor_err"></span></font>
                    </div>
                </div>  
                <div class="row">
                    <div class="col-md-6">
                        <label class="control-label" for="street">Employee Description: <span style="color: red">*</span></label>
                        <div id="emp_type" class="row">
                            <div class="col-md-5">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="single_emp_type" id="single_emp_type1"  value="emp_regular" >Govt/Psu Official
                                    <span></span>
                                </label>
                            </div>
                            <div class="col-md-7">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="single_emp_type" id="single_emp_type2"  value="consultant" >Consultant/Contractual Staff
                                    <span></span>
                                </label>
                            </div>
                            <div class="col-md-6">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="single_emp_type" id="single_emp_type3"  value="emp_contract" >FMS Support Staffs
                                    <span></span>
                                </label>
                            </div>
                        </div>
                        <font style="color:red"><span id="single_emp_type_err"></span></font>
                    </div>
                    <div class="col-md-12 display-hide" id="cert_div">
                        <div class="col-md-6">
                            <label for="street">Application should have work order of the contractual employee, Upload certificate in <b>PDF</b> format (less than 1mb)<span style="color: red">*</span></label>

                            <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                                <input type="file" class="custom-file-input" id="cert_file1" name="cert_file1">
                                <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                <span class="fileinput-filename"> </span> &nbsp;
                                <font style="color:red"><span id="file_err"> </span></font>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="street">Download Certificate <span style="color: red">*</span></label>
                            <div class="alert alert-secondary" >
                                <b>Uploaded filename: </b>&emsp;<a href="Download_EmaiAct_pdfFile"><span id="uploaded_filename">download</span></a>
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