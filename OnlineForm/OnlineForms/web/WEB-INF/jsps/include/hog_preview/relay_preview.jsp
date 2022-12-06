
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="relay_preview_tab">
            <!--Modal body start-->
            <div class="portlet light ">
                <span class="alert alert-info">Relay Request Form</span>
            </div>
            
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="panel-heading" style="background-color: grey;margin-top: 15px;">
                    <div class="panel-title" style="color:white;font-weight:bold;"></div>
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Relay Entry Details</h3>
                </div>
                <div id="relay_tab_preview2" >
                    <div class="row" id="app_ip_prvw">
                        <div class="col-md-12">
                            <label for="street">Application IP  <span style="color: red">*</span></label>
                            <input class="form-control class_name_prvw" placeholder="Enter the IP Address [e.g.: 164.100.X.X]" type="text" name="relay_ip[]" id="relay_ip"  value="" maxlength="15">
                            <div id="add_ip"></div>
                            <font style="color:red"><span id="relay_ip_err"></span></font>
                            <font style="color:red"><span id="relay_dup_ip_err"></span></font>   
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label for="street">Application Name <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Application Name, characters only ,character limit (5-50), dot(,),comma(,) whitespace allowed" type="text" name="relay_app_name" id="relay_app_name"  value="" maxlength="100" required/>
                            <font style="color:red"><span id="app_name_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="street">Name of Division <span style="color: red">*</span></label>
                                <input class="form-control" placeholder="Enter Name of Division, characters only ,character limit (5-50), dot(,),comma(,) whitespace allowed" type="text" name="division" id="division" value="" maxlength="100" required />
                                <font style="color:red"><span id="division_error"></span></font>
                            </div>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4">
                            <label for="street">Operating System (Name, Version) <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Operating System (Name, Version), [Only characters,whitespace,comma(,),hypen(-) allowed]" type="text" name="os" id="os"  value="" maxlength="50" required/>
                            <font style="color:red"><span id="os_error"></span></font>
                        </div>
                        <div class="col-md-4">
                            <label for="street">Server Location<span style="color: red">*</span></label>
                            <select name="server_loc" class="form-control" id="server_loc">
                                <option value="NDC Delhi">NDC Delhi</option>
                                <option value="NDC Pune">NDC Pune</option>
                                <option value="NDC Hyderabad">NDC Hyderabad</option>
                                <option value="NDC Bhubaneswar">NDC Bhubaneswar</option>
                                <option value="Other">Other</option>
                            </select>
                        </div>
                        <div class="col-md-4 display-hide" id="server_other">
                            <label for="street">Enter server location <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Server Location [Alphanumeric,whitespace and [. , - # / ( ) ] allowed]" type="text" name="server_loc_txt" id="server_loc_txt"  value="" maxlength="100">
                            <font style="color:red"><span id="server_txt_err"></span></font>
                        </div>
                    </div>
                   
                    <div class="row form-group">
                        <div class="col-md-12" id="cert_div">
                            <label for="street">Download Certificate <span style="color: red">*</span></label>
                            <div class="alert alert-secondary" >
                                <b>Uploaded filename: </b>&emsp;<a href="download_1"><span id="uploaded_filename">download</span></a>
                            </div>                                                                                    
                        </div>        
                        <div class="col-md-12" id="cert_div1">
                            <label for="street">Staging Server? ( IP will be allowed maximum for 15 days ): <span style="color: red">*</span></label>
                            <span id="s_server">YES</span>                                                                                   
                        </div>  
                    </div>
                    <div class="row" id="cert_div">
                        <div class="col-md-12">
                            <label for="street">Application should have Security audit clearance certificate, Upload certificate in <b>PDF</b> format (less than 1mb)<span style="color: red">*</span></label>
                            
                            <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                                <input type="file" class="custom-file-input" id="cert_file1" name="cert_file1" >
                                <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                <span class="fileinput-filename"> </span> &nbsp;
                                <font style="color:red"><span id="file_err"> </span></font>
                            </div>
                        </div>                                                                            
                    </div>
                </div>
                <div class="form-group" id='tnc_div'>
                    <div class="mt-checkbox-list">
                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                            <input type="checkbox" name="tnc" id="tnc"> <b>I agree to <a data-toggle="modal" href="#stack2" style="color:blue">Terms and Conditions</a></b>
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
