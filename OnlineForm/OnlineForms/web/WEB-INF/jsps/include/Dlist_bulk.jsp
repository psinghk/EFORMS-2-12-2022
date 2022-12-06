<html>
    <body>
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="dlist_preview_tab">
            <!--Modal body start-->
            <div class="portlet light ">
                <span class="alert alert-info">Bulk Distribution List Request Form</span>
            </div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Bulk Distribution List Details</h3>
                </div>

               

<!--                <div class="col-md-6" id="cert1_div">
                         <div class="alert alert-secondary">
                            <b>Uploaded filename: </b>&ensp;<a href="download_1"><span id="uploaded_filename">Download</span></a>
                        </div>    
                    </div>-->





            <div class="row">
                    <div class="col-md-6">
                      
                        <div class="alert alert-secondary">
                            <b>Uploaded filename: </b>&ensp;<a href="download_1"><span id="uploaded_filename">Download</span></a>
                        </div>                                                                                    
                    </div> 
                    <div class="col-md-6">
                       
                        <div class="alert alert-secondary">
                            <b>Valid File: </b>&ensp;<a href="download_valid"><span id="valid_filepath">Download Valid Records File</span></a>
                        </div>                                                                                    
                    </div>  
                </div>
                <div class="row">
                    <div class="col-md-6">
                      
                        <div class="alert alert-secondary">
                            <b>Invalid File: </b>&ensp;<a href="download_notvalid"><span id="notvalid_filepath">Download InValid Records File</span></a>
                        </div>                                                                                      
                    </div> 
                    <div class="col-md-6">
                       
                        <div class="alert alert-secondary">
                            <b>Error File: </b>&ensp;<a href="download_error"><span id="error_filepath">Download Error File</span></a>
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
         </body>       
    </html>