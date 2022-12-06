
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="dlist_preview_tab">
            <!--Modal body start-->
            <div class="portlet light ">
                <span class="alert alert-info">Distribution List Request Form</span>
            </div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Distribution List Details</h3>
                </div>
                <div id="dlist_tab_preview2">
                    <div class="row from-group pt-1 pb-1">
                        <div class="col-md-6">
                            <label class="control-label" for="street">Name of the list (append @lsmgr.nic.in after list name)<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="E.g: abc.def@lsmgr.nic.in [dot(.) or hyphen(-) with 6-20 characters in list name]" type="text" name="list_name" id="list_name" maxlength="50"  aria-required="true">
                            <font style="color:red"><span id="list_name_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label class="control-label" for="street">Description of List  <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Description of List  [characters,dot(.) and whitespace] " type="text" name="description_list" id="description_list" value="" maxlength="100"  aria-required="true">                                                                                        
                            <font style="color:red"><span id="description_list_err"></span></font>
                        </div>
                    </div>
                    <div class="row from-group pt-1 pb-1">
                        <div class="col-md-6" id="dlist1">
                            <label class="control-label" for="street">Will the List be moderated ? <span style="color: red">*</span></label><br>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="list_mod" id="list_mod_yes" value="yes" checked> Yes(recommended)
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="list_mod" id="list_mod_no" value="no" > No
                                <span></span>
                            </label>

                            <font style="color:red"><span id="list_mod_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label class="control-label" for="street">Are only members allowed to send mails to the list  ? <span style="color: red">*</span></label><br>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="allowed_member" id="allowed_member_yes" value="yes" checked> Yes(recommended)
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="allowed_member" id="allowed_member_no" value="no" > No
                                <span></span>
                            </label>
                            <font style="color:red"><span id="allowed_member_err"></span></font>
                        </div>
                    </div>
                    <div class="row from-group pt-1 pb-1">
                        <div class="col-md-6">
                            <label class="control-label" for="street">Is list temporary (if yes, indicate validity date) ? <span style="color: red">*</span></label><br>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="list_temp" id="list_temp_yes" value="yes"> Yes
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="list_temp" id="list_temp_no" value="no" checked> No
                                <span></span>
                            </label>
                            <font style="color:red"><span id="list_temp_err"></span></font>
                        </div>
                        <div class="col-md-6" id="validity_date_show" style="display:none;">
                            <label class="control-label" for="street">Validity date <span style="color: red">*</span></label><br>
                            <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="validity_date" id="validity_date1" placeholder="Enter Validaty Date [DD-MM-YYYY]" readonly/>
                            <font style="color:red"><span id="validity_date_err"></span></font>
                        </div>
                    </div>
                    <div class="row from-group pt-1 pb-1">
                        <div class="col-md-12">
                            <label class="control-label" for="street">Will list accept mail from a non-NICNET email address (from internet like gmail, yahoo etc)   ? <span style="color: red">*</span></label><br>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="non_nicnet" id="non_nicnet_yes" value="yes"> Yes
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="non_nicnet" id="non_nicnet_no" value="no" checked> No(recommended)
                                <span></span>
                            </label>
                            <font style="color:red"><span id="non_nicnet_err"></span></font>
                        </div>
                    </div>
                    <div class="k-portlet__head">
                        <h3 class="theme-heading-h3-popup">Moderator Details</h3>
                    </div>
                    <div class="row from-group pt-1 pb-1">
                        <div class="col-md-6">
                            <label class="control-label" for="street">Moderator Name  <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Name of The Admin [characters,dot(.) and whitespace]" type="text" name="t_off_name" id="auth_off_name"  value="" maxlength="100">
                            <font style="color:red"><span id="tauth_off_name_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label class="control-label" for="street">Moderator E-mail Address <span style="color: red">*</span></label>
                            <input class="form-control" style="text-transform:lowercase;" placeholder="Enter Moderator Email Address [e.g: abc.xyz@gov.in]" type="text" name="tauth_email" id="auth_email"  value="" maxlength="50">
                            <font style="color:red"><span id="tauth_email_err"></span></font>
                        </div>
                    </div>
                    <div class="row from-group pt-1 pb-1">
                        <div class="col-md-6">
                            <label class="control-label" for="street">Moderator Mobile <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Mobile [e.g: +919999999999]" type="text" name="tmobile" id="mobile"  value="" maxlength="15">
                            <font style="color:red"><span id="tmobile_err"></span></font>
                        </div>
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
            <button type="button" class="btn btn-default dark btn-outline" data-dismiss="modal" id="closebtn">Close</button>
            <a href="javascript:void(0);" class="btn btn-warning green edit" >Edit</a>
            <button type="button" class="btn red btn-success btn-outline save-changes" id="confirm">Submit</button>
        </div> 
    </div>
</div>
