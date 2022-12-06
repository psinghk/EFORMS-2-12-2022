
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
                    <div class="dns_owner_error"></div>
                    
                    <div class="row form-group">
                        <div class="col-md-4">
                            <input type="hidden" id="stat_type" value="">
                            <label for="street">Employee Code </label><span style="color: red;">*</span>
                            <input class="form-control" placeholder="[Only characters and digits allowed]" type="text" name="user_empcode" id="user_empcode"  value="" maxlength="12">
                            <font style="color:red"><span id="userempcode_error"></span></font>
                        </div>
                    </div>
                    <input type="hidden" name="url" value="web_url" /> <!--  above code commented this added by pr on 20thmar19 -->
                </div>
            </div>
            <div class="row" id="upload_dns"> 
                <div class="col-md-12 mb-4">
                    <label for="street"><strong>Success Domain List</strong></label>
                    <table class="table table-bordered table-sm dt-responsive" id="successBulkData">
                        <thead>
                            <tr></tr>
                        </thead>
                        <tbody></tbody>
                    </table>
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
            <a href="javascript:void(0);" class="btn btn-warning edit" >Edit</a>
            <button type="button" class="btn red btn-success save-changes" id="confirm">Submit</button>
        </div> 
    </div>
</div>