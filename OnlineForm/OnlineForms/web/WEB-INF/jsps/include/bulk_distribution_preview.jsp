
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
                

                <div id="dlist_tab_preview2">
                   
                    <div class="col-md-12 mt-4 owner_details" id="own_heading">
                        <h3 class="theme-heading-h3-popup" id="test">Excel File Details</h3>
                    </div>

                    <div class="row">
                        <div class="col-md-12 mt-4 owner_details" id="owner_collection">
                                     
                            <table class="table table-bordered table-responsive table-striped table-sm mb-4" id="own_heading_tbl">
                                <thead class="thead-light">
                                    <tr>
                                        <td class="text-center" width="8%">S.No</td>
                                        <td>List Name</td>
                                        <td>List Description</td>
                                        <td>List Mod</td>
                                        <td>Allow Member</td>
                                        <td>List Temp</td>
                                        <td>Mail Acceptance From Non Nicnet Id/other domains</td>
                                        <td>Owner Name</td>
                                        <td>Owner Email</td>
                                        <td>Owner Mobile</td>
                                        <td>Moderator Name</td>
                                        <td>Moderator Email</td>
                                        <td>Moderator Mobile</td>
                                        <td>Owner Admin</td>
                                        <td id="test">Moderator Admin</td>
                                    </tr>
                                </thead>
                                <tbody id="qwerty"></tbody>
                            </table>
                           
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
    
                       
                    
                     
