
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="da_onboarding_preview_tab">
            <!--Modal body start-->
            <div class="portlet light ">
                <div class="portlet-title">
                    <div class="caption font-red-sunglo">
                        <i class="icon-settings font-red-sunglo"></i>
                        <span class="alert alert-info">DA Onboarding Request Form</span>
                    </div>
                </div>
            </div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">DA Onboarding Details</h3>
                </div>

                <div id="dns_service">
                    <label class="k-radio k-radio--bold k-radio--brand">
                        <input type="radio" name="eligibility" id="eligibility_department"  value="department" disabled> Govt department/institutes/organization
                        <span></span>
                    </label>&emsp;&emsp;
                    <label class="k-radio k-radio--bold k-radio--brand">
                        <input type="radio" name="eligibility" id="eligibility_psu"   value="psu" disabled> PSU
                        <span></span>
                    </label>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <label for="street">VPN IP </label>
                        <input class="form-control" value="" placeholder="Enter VPN IP" type="text" name="da_vpn_reg_no" id="vpn_reg_no" maxlength="50"  aria-required="true" disabled>
                        <font style="color:red"><span id="app_name_err"></span></font>
                    </div>
                    <!--
                    <div class="col-md-6">
                        <label for="street">BO Name <span style="color: red">*</span></label>
                        <input class="form-control" value="" placeholder="Enter BO Name" type="text" name="bo_name" id="bo_name" maxlength="50"  aria-required="true" disabled>
                        <font style="color:red"><span id="app_name_err"></span></font>
                    </div>                    
                    -->
                </div>
                <div class="alert alert-secondary my-4">
                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                        <input type="checkbox" name="daon_mobile" id="daon_mobile"> I will ensure the mobile numbers of all users under this domain are updated.
                        Please note that by selecting this, the users under this domain will not be 
                        allowed to update their mobile numbers on their own and all requests will be 
                        sent to you ONLY.
                        <span></span>
                    </label>
                    </div>




                <!--                <div class="row form-group" id="upload_ldap"> 
                                    <div class="col-md-6" id="cert1_div">
                                        <div class="alert alert-secondary" style="margin-top: 21px;">
                                            <b>Uploaded filename: </b>&ensp;<i class="fa fa-file-download" style="font-size: 17px;margin-right: 6px;color: #5867dd;"></i><a href="download_1" ><span id="uploaded_filename">download</span></a>
                                        </div>  
                                    </div>
                                </div>-->
                <input type="hidden" id="cert" value="false" />

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
            <!--<a href="javascript:void(0);" class="btn btn-warning green edit " >Edit</a>-->
            <button type="submit" class="btn red btn-success btn-outline save-changes" id="confirm">Submit</button>
            <button type="button" class="btn btn-default dark btn-outline" data-dismiss="modal" id="closebtn">Close</button>
        </div>
    </div>
</div>
