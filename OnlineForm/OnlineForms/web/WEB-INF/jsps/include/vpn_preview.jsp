
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="vpn_preview_tab">
            <div class="alert alert-primary"><span style="font-size: 16px;">VPN Request Form</span></div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">VPN Details</h3>
                </div>
                <div id="vpn_details_preview_div">
                    <input type="hidden" id="req_for" name="req_for" />
                </div>
                <div class="row display-hide" style="padding:20px;" id="api_output_div">
                </div>

                <div class="row">
                    <div class="col-md-12" id="vpn_data_filled">
                    </div>
                </div>
                <div class="row">
                    
                    <div class="col-md-12" id="remarks_div"> 
                        <label for="street">Remarks</label>
                        <input class="form-control" value="" placeholder="Remarks" type="text" name="remarks" id="remarks" maxlength="100"  aria-required="true">
                        <font style="color:red"><span id="remarks_error"></span></font>
                    </div>                                                                                
                </div>
                 <div class="row">
                <div class="col-md-6" id="sel_coo1">
                        <label for="street">Co-ordinator email</label>
                        <input class="form-control" value="" placeholder="Co-ordinator email address" type="text" name="vpn_coo" id="vpn_coo" maxlength="100"  aria-required="true">
                        <font style="color:red"><span id="vpn_coo_error"></span></font>
                    </div> 
                 </div>
                <div class="form-group mt-4" id='tnc_div'>
                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand mt-5">
                        <input type="checkbox"  name="tnc" id="tnc"> <b>I agree to <a data-toggle="modal" href="#stack2" style="color:blue">Terms and Conditions</a></b>
                        <span></span>
                    </label>
                    <font style="color:red"><span id="tnc_error"></span></font>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal"  id="closebtn">Close</button>
<!--            <a href="javascript:void(0);" class="btn btn-warning edit " >Edit</a>-->
            <button type="button" class="btn red btn-success save-changes" id="confirm">Submit</button>
        </div> 
    </div>
</div>