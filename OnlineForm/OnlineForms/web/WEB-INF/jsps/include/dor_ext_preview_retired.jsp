 <link rel="stylesheet" href="assets/home-page/update-mobile/css/style.css">
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="dor_ext_preview_tab">
            <!--Modal body start-->
         
            <div class="portlet-body form">

                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Extend the Validity of Account Details</h3>
                </div>
                <div class="row">
                    <div class="col-md-12 mb-3">
                        <label class="control-label" for="street">Email Address  (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                        <input class="form-control act_email1" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com OR abc.xyz@nic.in]" type="text" name="dor_email" id="dor_email" value="" readonly>
                        <font style="color:red"><span id="single_email1_err"></span></font>
                    </div>

                    <div class="col-md-12 mb-3" id="divDor"> <label class="control-label" for="street">Previous Date Of Account Expiry  <span style="color: red">*</span></label>
                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="p_single_dor" id="p_email_single_dor" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly />
                        <font style="color:red"><span id="single_dor_err"></span></font>
                    </div>

                    <div class="col-md-12 mb-3" id="divDor"> <label class="control-label" for="street">Date Of Account Expiry  <span style="color: red">*</span></label>
                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dor" id="p_email_single_newdor" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly />
                        <font style="color:red"><span id="single_dor_err"></span></font>
                    </div>
                   
                    <div class = "col-md-12 mb-3">
                        <label class="control-label" for="street" >Name <span style="color: red">*</span></label>
                        <input class="form-control " type="text" name="p_name_dor" id="p_email_single_name" placeholder="Enter name of the user" readonly/>
                        <font style="color:red"><span id="prev_dor_err"></span></font>
                    </div>
<!--                    <div class = "col-md-12 mb-3">
                        <label class="control-label" for="street" >Mobile<span style="color: red">*</span></label>
                        <input class="form-control" type="" name="p_mobile_dor" id="p_email_single_mobile" placeholder="Enter Mobile of the user" readonly/>
                        <font style="color:red"><span id="prev_dor_err"></span></font>
                    </div>-->
                    
                </div>  
<!--                   <div class="form-group" id='tnc_div' style="display: flex;">
                    <div class="mt-checkbox-list">
                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                            <input type="checkbox" name="tnc" id="tnc" class="ml-2" style="width: 10px;"> <b>I agree to <a data-toggle="modal" id="tnc_retired_officers" class="mt-2" href="#stack2" style="color:blue">Terms and Conditions</a></b>
                            <span></span>
                        </label>
                        <font style="color:red"><span id="tnc_error"></span></font>
                    </div>
                </div>-->
            </div>
        </div>
        <div class="modal-footer">
            <!--            <button type="button" class="btn btn-success edit" >Edit</button>-->
            <button type="button" class="btn red btn-warning save-changes" id="confirm" value="" onclick="submitByRetiredOfficersPreview()">Submit</button>
            <button type="button" class="btn dark btn-secondary" data-dismiss="modal" id="closebtn">Close</button>
        </div> 
    </div>
</div>

