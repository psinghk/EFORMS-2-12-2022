<!-- File Upload Modal Start -->
<div class="modal fade bd-modal-lg" id="fileUploadMultiple" tabindex="-1"  aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4>File Upload</h4> 
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="portlet-body">
                    <div class="row">
                        <div class="col-md-12">

                            <form name="vasplus_form_id" id="vasplus_form_id" action="javascript:void(0);" enctype="multipart/form-data">
                                <div class="alert alert-info p-0">
                                    <ul class="pt-3">
                                        <li>Only PDF files are allowed.</li>
                                        <li>File should be less than 1 MB.</li>
                                    </ul>
                                </div>
                                <div class="filename"></div>
                                <div class="row" align="center">
                                    <div class="col-12">
                                        <div class="vpb_browse_file_box mt-3">
                                            <i class="fa fa-cloud-upload-alt"></i>
                                            <p class="text-center" style="font-weight: 600;color: #5578eb;">Click here to select File</p>
                                        </div>
                                        <div style="color:red" class="mt-4"><span id="file_cert_mul_err"></span></div>
                                        <input type="file" name="vasplus_multiple_files[]" id="vasplus_multiple_files" accept="application/pdf" multiple style="display: none;" />
                                    </div>
                                    <div class="col-md-12 mt-3">
                                        <table id="vpb_added_files" class="table table-striped">
                                            <thead>
                                                <tr class="table-info">
                                                    <th scope="col">File Name</th>
                                                    <th scope="col">Status</th>
                                                    <th scope="col">Size</th>
                                                    <th scope="col">Time</th>
                                                    <th scope="col">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody id="vpb_added_files_box">
                                                <tr>
                                                    <td colspan="5" class="text-center"><div class="no-record">No File Selected.</div></td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="col-md-12 mt-2 mb-3 filesUpload display-hide">
                                        <div class="col-md-12 text-center p-3" style="border: 1px solid #ccc;border-radius: 10px;">
                                            <input type="submit" value="Click to Upload" class="btn btn-primary">
                                        </div>
                                    </div>
                                </div>
                            </form>

                            <!-- Uploaded Files Displayer Area -->
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
                <!--                        <button type="button" class="btn dark btn-default mod_close" data-dismiss="modal">Submit</button>-->
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- File Upload Modal End -->

<!-- File Upload Modal Start -->
<div class="modal fade bd-modal-lg" id="fileDownload" tabindex="-1"  aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">File Download</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="portlet-body">
                    <div class="row">
                        <div class="col-md-12">
                            <span id="fileDowloadResult"></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- File Upload Modal End -->

<div class="modal fade bd-modal-lg" id="fileUserDownload" tabindex="-1"  aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h2>File Download</h2>
            </div>
            <div class="modal-body">
                <div class="portlet-body">
                    <div class="row">
                        <div class="col-md-12" id="fileDowloadResult1"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>