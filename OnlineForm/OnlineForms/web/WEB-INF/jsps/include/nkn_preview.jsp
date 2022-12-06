<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="nkn_preview_tab">
            <!--Modal body start-->
            <div class="alert alert-primary"><span style="font-size: 16px;">NKN User Subscription Form</span></div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div id="under_sec_div">
                    <jsp:include page="underSecretary_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">NKN User Subscription Details</h3>
                </div>             
                <div class="row form-group">
                    <div class="col-md-6">
                        <label for="street">Institute Name <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Institute Name [Only characters,whitespace,comma(,) allowed]" type="text" name="inst_name" id="inst_name" value="">
                        <font style="color:red"><span id="inst_name_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">Institute ID </label>
                        <input class="form-control" placeholder="Enter Institute ID [Alphanumeric,dot(.),comma(,),hypen(-) allowed]" type="text" name="inst_id" id="inst_id" value="">
                        <font style="color:red"><span id="inst_name_err"></span></font>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-md-12">
                        <label for="street">Name of Project NKN <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Name of Project NKN [Only characters,whitespace,comma(,)allowed]" type="text" name="nkn_project" id="nkn_project" value="">
                        <font style="color:red"><span id="nkn_project_err"></span></font>
                    </div>                                                                                
                </div>
                <div id="nkn_single_preview">
                    <div class="row form-group">
                        <div class="col-md-6">
                            <label for="street">Date Of Birth <span style="color: red">*</span></label>
                            <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dob" id="single_dob5" placeholder="Enter Date Of Birth [DD-MM-YYYY]" readonly/>
                            <font style="color:red"><span id="single_dob_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street">Date Of Retirement/Date of expiry <span style="color: red">*</span></label>
                            <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dor" id="single_dor5" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly />
                            <font style="color:red"><span id="single_dor_err"></span></font>
                        </div>
                    </div>
                    <div class="col-md-12 mt-3 mb-3 p-0">
                        <label for="street">Type of Mail ID: <span style="color: red">*</span> <a href="" id="mD2" data-toggle="modal" data-target="#myModal1" style="color:blue">(Know More<i class="entypo-help"></i>)</a></label>
                        <div>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="nkn_req_for" id="nkn_req_for"  value="mail" checked=""> Mail user (with mailbox)
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="nkn_req_for" id="nkn_req_for"  value="app" > Application user (without mail box(Eoffice-auth))<!-- text changed by pr on 5thmay2020 -->
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="nkn_req_for" id="nkn_req_for"  value="eoffice" > e-office-srilanka
                                <span></span>
                            </label>&emsp;&emsp;
                        </div>
                        <font style="color:red"><span id="nkn_req_for_err"></span></font>
                    </div>
                    <div class="col-md-12 mt-3 mb-3 p-0">
                        <label for="street">Email address preference: <span style="color: red">*</span></label>
                        <div>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="nkn_id_type" id="nkn_id_type"  value="" checked=""> Name Based
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="nkn_id_type" id="nkn_id_type"  value="" > Designation/Office based id
                                <span></span>
                            </label>&emsp;&emsp;
                        </div>
                        <font style="color:red"><span id="nkn_id_type_err"></span></font>
                    </div>
                    <div class="row emailcheck">
                        <div class="col-md-6">
                            <label for="street">Preferred Email Address 1 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" type="text" name="single_email1" id="single_email1" value="">
                            <font style="color:red"><span id="single_email1_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street">Preferred Email Address 2 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" type="text" name="single_email2" id="single_email2" value="">
                            <font style="color:red"><span id="single_email2_err"></span></font>
                        </div>
                    </div>
                </div>
                <div id="nkn_bulk_preview">
                          <div class="col-md-12 mt-3 mb-3 p-0">
                        <label for="street">Type of Mail ID: <span style="color: red">*</span> <a href="" id="mD2" data-toggle="modal" data-target="#myModal1" style="color:blue">(Know More<i class="entypo-help"></i>)</a></label>
                        <div>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="nkn_req_for1" id="nkn_req_for"  value="mail" checked=""> Mail user (with mailbox)
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="nkn_req_for1" id="nkn_req_for"  value="app" > Application user (without mail box(Eoffice-auth))<!-- text changed by pr on 5thmay2020 -->
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="nkn_req_for1" id="nkn_req_for"  value="eoffice" > e-office-srilanka
                                <span></span>
                            </label>&emsp;&emsp;
                        </div>
                        <font style="color:red"><span id="nkn_req_for_err"></span></font>
                    </div>
                    <div class="col-md-12 mt-3 mb-3 p-0">
                        <label for="street">Email address preference: <span style="color: red">*</span></label>
                        <div>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="nkn_id_type1" id="nkn_id_type"  value="id_name" checked=""> Name Based
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="nkn_id_type1" id="nkn_id_type"  value="id_desig" > Designation/Office based id
                                <span></span>
                            </label>&emsp;&emsp;
                        </div>
                        <font style="color:red"><span id="nkn_id_type_err"></span></font>
                    </div>
                    <label for="street"></label>
                    <div class="alert alert-info">
                        <b>Uploaded filename: </b>&emsp;<a href="download_1" style="color: #fff;text-decoration: underline;"><span id="uploaded_filename">download</span></a>
                    </div>
                </div>


                <!--  below div added by pr on 18thjul18  -->
                <div class="row emailcheck final_id_span_cls display-hide">
                    <div class="col-md-12">
                        <label for="street"><strong>ID Assigned : </strong></label>
                        <span class="final_id_cls"></span>
                    </div>
                </div>
                <div class="form-group mt-5" id='tnc_div'>
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
            <button type="button" class="btn dark btn-default" data-dismiss="modal" id="closebtn">Close</button>
            <a href="javascript:void(0);" class="btn btn-warning edit" >Edit</a>
            <button type="button" value="" class="btn red btn-success save-changes" id="confirm">Submit</button>
        </div> 
    </div>
</div>
