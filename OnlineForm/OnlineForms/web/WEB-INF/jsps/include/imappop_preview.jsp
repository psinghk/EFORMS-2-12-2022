<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="imappop_preview_tab">
            <!--Modal body start-->
            <div class="alert alert-primary"><span style="font-size: 16px;">Imap Pop Request Form</span></div>
            
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Imap Pop Protocol Enable Details</h3>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <label class="control-label" for="street">Please check the Protocol to be enabled : <span style="color: red">*</span></label>
                        <div class="col-md-12 p-0 mb-3">
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="protocol" id="protocol_imap"  value="imap" > IMAP
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="protocol" id="protocol_pop"   value="pop" > POP
                                <span></span>
                            </label>
                        </div>
                        <font style="color:red"><span id="protocol_err"></span></font>
                        <font style="color:red"><span id="service_pop_err"></span></font>
                        <font style="color:red"><span id="service_imap_err"></span></font>
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
        </div>
        <div class="modal-footer">
            <button type="button" class="btn dark btn-default" data-dismiss="modal" id="closebtn">Close</button>
            <a href="javascript:void(0);" class="btn btn-warning edit " >Edit</a>
            <button type="button" class="btn red btn-success save-changes" id="confirm">Submit</button>
        </div> 
    </div>
</div>
