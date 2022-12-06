var storedFiles = [];

document.addEventListener("DOMContentLoaded", init, false);
$(".vpb_browse_file_box").on('click', function () {
    $("#vasplus_multiple_files").focus().trigger("click");
});
function init() {
    document.querySelector('#vasplus_multiple_files').addEventListener('change', handleFileSelect, false);
    document.querySelector('#vasplus_form_id').addEventListener('submit', handleForm, false);
}

function handleFileSelect(e) {
    //to check that even single file is selected or not
    if (!e.target.files)
        return;

    //get the array of file object in files variable
    var files = e.target.files;
    var filesArr = Array.prototype.slice.call(files);

    //print if any file is selected previosly 
    for (var i = 0; i < storedFiles.length; i++) {
        console.log("Select Multiple")
        domCreator(storedFiles);
    }
    filesArr.forEach(function (f) {
        console.log("Select Single")
        storedFiles.push(f);
        domCreator(storedFiles);
    });
 $("#file_cert_mul_err").html("");
    //store the array of file in our element this is send to other page by form submit
    $("input[name=vasplus_multiple_files]").val(storedFiles);
    fetchUploadedFileCounts();
}

function handleForm(e) {
    e.preventDefault();
    console.log('handleForm');
    var ref_no = $('#ref_no').val();
    var whichform = $('#form_val').val();
    var panel = $('#panel').val();
    var dataString = new FormData();
    var ins = storedFiles.length;
    var csrf = $('#CSRFRandom').val();
    for (var y = 0; y < ins; y++)
    {
        var files_name_without_extensions = storedFiles[y].name.substr(0, storedFiles[y].name.lastIndexOf('.')) || storedFiles[y].name;
        var ids = files_name_without_extensions.replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '');
        dataString.append("upload_file", storedFiles[y]);
        dataString.append('upload_file_ids', ids);
    }
    dataString.append('ref_no', ref_no);
    dataString.append('whichform', whichform);
    dataString.append('panel', panel);
    dataString.append('CSRFRandom', csrf);
    $.ajax({
        type: "POST",
        url: 'vpb_uploader',
        data: dataString,
        cache: false,
        contentType: false,
        processData: false,
        beforeSend: function ()
        {
            for (var y = 0; y < ins; y++)
            {
                var files_name_without_extensions = storedFiles[y].name.substr(0, storedFiles[y].name.lastIndexOf('.')) || storedFiles[y].name;
                var ids = files_name_without_extensions.replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '');
                $("#uploading_" + ids).html('<div align="left"><img src="assets/old_assets/images/loadings.gif" width="80" align="absmiddle" title="Upload...."/></div>');
                $("#remove" + ids).html('<div align="center" style="font-family:Verdana, Geneva, sans-serif;font-size:11px;color:blue;">Uploading...</div>');
                $(".loader").show();
            }
        },
        success: function (response)
        {
            console.log(response);
            if (response.csrf_error) {
                alert(response.csrf_error);
            }
            resetCSRFRandom();
            var res_arr = response.split(';');
            $.each(res_arr, function (key, val) {
                var val_arr = val.split("=>");
                if (val_arr[0] == "file_uploaded") {
                    $("#uploading_" + val_arr[1]).html('<div align="left" style="font-family:Verdana, Geneva, sans-serif;font-size:11px;color:green;">File Uploaded</div>');
                } else if (val_arr[0] == "exceed_limit") {
                    $("#file_cert_mul_err").html("You have already uploaded 5 files, Please remove previous files then Upload again.")
                    $("#uploading_" + val_arr[1]).html('<div align="left" style="font-family:Verdana, Geneva, sans-serif;font-size:11px;color:red;">Exceed Limit</div>');
                } else if (val_arr[0] == "syserr") {
                    //$("#file_cert_mul_err").html("You have already uploaded 5 files, Please remove previous files then Upload again.")
                    $("#uploading_" + val_arr[1]).html('<div align="left" style="font-family:Verdana, Geneva, sans-serif;font-size:11px;color:red;">Invalid File</div>');
                }
            });
        }, error: function (data) {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            var error = JSON.parse(jsonvalue.responseText.substring(0, jsonvalue.responseText.indexOf("}") + 1)).error;
            $('#new_alert .modal-body').html(error);
            $('#new_alert').modal('show');
            $('.loader').hide();
            console.log('error');
            $("#file_cert_mul_err").html("Please upload pdf file only. (Note: Encrypted, Compressed and Password Protected file will not be valid.")
            storedFiles = [];
            $('#vasplus_form_id').trigger("reset");
            $(".upload_status").html('<div align="left" style="font-family:Verdana, Geneva, sans-serif;font-size:11px;color:red;">Failed</div>');
        }, complete: function () {
            $(".loader").hide();
            storedFiles = [];
            $('#vasplus_form_id').trigger("reset");
        }
    });

}

function fetchUploadedFileCounts() {
    var ref_no = $('#ref_no').val();
    var ins = storedFiles.length;
    $.ajax({
        type: 'post',
        url: 'fetchDocCount',
        data: {ref_no: ref_no},
        success: function (data) {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            let count = 0;
            count = 5 - jsonvalue.count;
           
             if (count <= 0) {
                error_msg = "You have already uploaded <b>" + jsonvalue.count + "</b> files, Please remove previous files then Upload again.";
                $("#file_cert_mul_err").html(error_msg);
                $(".filesUpload").addClass("display-hide");
            } else if (count < ins) {
           
                error_msg = "You have already uploaded <b>" + jsonvalue.count + "</b> files, Now you can upload only <b>" + count + "</b> files."
                $("#file_cert_mul_err").html(error_msg);
                $(".filesUpload").addClass("display-hide");
            } else {
                $(".filesUpload").removeClass("display-hide");
            }

        }
    });
}
;


function domCreator(vpb_value)
{
   
    $("#vpb_added_files_box").html("");

    // $("#added_class").val("");
    this.vpb_files = vpb_value;

    if (this.vpb_files.length > 0)
    {
        $("#vpb_added_files").removeClass('d-none')
        this.vpb_browsed_files = []
        $(".vpb_white").html('');
        var vpb_added_files_displayer = vpb_file_id = "";
        for (var i = 0; i < this.vpb_files.length; i++)
        {
            //Use the names of the files without their extensions as their ids
            var files_name_without_extensions = this.vpb_files[i].name.substr(0, this.vpb_files[i].name.lastIndexOf('.')) || this.vpb_files[i].name;
            vpb_file_id = files_name_without_extensions.replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '');
            var fileType = this.vpb_files[i].type;

            var vpb_file_to_add = vpb_file_ext(this.vpb_files[i].name);
            var vpb_class = $("#added_class").val();
            var vpb_file_icon;
            for (var i = 0; i < this.vpb_files.length; i++)
            {

                if (this.vpb_files.length > 5) {
                    $('#file_cert_mul_err').text("You can upload only 5 files");
                    storedFiles = [];
                    $('#vasplus_form_id').trigger("reset");
                    return false;
                } else {


                    $('#file_cert_mul_err').text("");
                }
                var fileType = this.vpb_files[i].type;

                var fileSize = this.vpb_files[i].size;
                var match = ["application/pdf"]; // for multiple take comma separated values
                if (fileType === match[0]) {
                    if (fileSize <= 1000000) {

                    } else {
                        $('#file_cert_mul_err').text("PDF file shoulb be less than 1mb");
                        storedFiles = [];
                        $('#vasplus_form_id').trigger("reset");
                        return;
                    }
                } else {

                    $('#file_cert_mul_err').text("Upload only PDF file");
                    storedFiles = [];
                    $('#vasplus_form_id').trigger("reset");
                    return false;
                }
                //Use the names of the files without their extensions as their ids
                var files_name_without_extensions = this.vpb_files[i].name.substr(0, this.vpb_files[i].name.lastIndexOf('.')) || this.vpb_files[i].name;
                vpb_file_id = files_name_without_extensions.replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '');
                var vpb_file_to_add = vpb_file_ext(this.vpb_files[i].name);

                var vpb_class = $("#added_class").val();
                var vpb_file_icon;
                //Check and display File Size
                var vpb_fileSize = (this.vpb_files[i].size / 1024);
                if (vpb_fileSize / 1024 > 1)
                {
                    if (((vpb_fileSize / 1024) / 1024) > 1)
                    {
                        vpb_fileSize = (Math.round(((vpb_fileSize / 1024) / 1024) * 100) / 100);
                        var vpb_actual_fileSize = vpb_fileSize + " GB";
                    } else
                    {
                        vpb_fileSize = (Math.round((vpb_fileSize / 1024) * 100) / 100)
                        var vpb_actual_fileSize = vpb_fileSize + " MB";
                    }
                } else
                {
                    vpb_fileSize = (Math.round(vpb_fileSize * 100) / 100)
                    var vpb_actual_fileSize = vpb_fileSize + " KB";
                }

                //Check and display File Size
                var vpb_fileSize = (this.vpb_files[i].size / 1024);
                if (vpb_fileSize / 1024 > 1)
                {
                    if (((vpb_fileSize / 1024) / 1024) > 1)
                    {
                        vpb_fileSize = (Math.round(((vpb_fileSize / 1024) / 1024) * 100) / 100);
                        var vpb_actual_fileSize = vpb_fileSize + " GB";
                    } else
                    {
                        vpb_fileSize = (Math.round((vpb_fileSize / 1024) * 100) / 100)
                        var vpb_actual_fileSize = vpb_fileSize + " MB";
                    }
                } else
                {
                    vpb_fileSize = (Math.round(vpb_fileSize * 100) / 100)
                    var vpb_actual_fileSize = vpb_fileSize + " KB";
                }

                //Check and display the date that files were last modified
                var vpb_date_last_modified = new Date(this.vpb_files[i].lastModifiedDate);
                var dd = vpb_date_last_modified.getDate();
                var mm = vpb_date_last_modified.getMonth() + 1;
                var yyyy = vpb_date_last_modified.getFullYear();
                var vpb_date_last_modified_file = dd + '/' + mm + '/' + yyyy;

                //File Display Classes
                if (vpb_class == 'vpb_blue') {
                    var new_classc = 'vpb_white';
                } else {
                    //var new_classc = 'vpb_blue';
                }


                if (typeof this.vpb_files[i] != undefined && this.vpb_files[i].name != "")
                {
                    if (vpb_file_to_add == "pdf" || vpb_file_to_add == "PDF")
                    {
                        vpb_file_icon = '<img src="assets/old_assets/images/pdf.gif" align="absmiddle" border="0" alt="" />';
                    } else {
                        vpb_file_icon = '<img src="assets/old_assets/images/general.png" align="absmiddle" border="0" alt="" />';
                    }

                    //Assign browsed files to a variable so as to later display them below
                    vpb_added_files_displayer += '<tr id="add_fileID' + vpb_file_id + '"><td>' + vpb_file_icon + ' ' + this.vpb_files[i].name.substring(0, 40) + '</td><td>Ready</td><td>' + vpb_actual_fileSize + '</td><td>' + vpb_date_last_modified_file + '</td><td><span class="upload_status" id="uploading_' + vpb_file_id + '"><span id="remove' + vpb_file_id + '"><span class="vpb_files_remove_left_inner" onclick="vpb_remove_this_file(\'' + i + '\',\'' + this.vpb_files[i].name + '\');">Remove</span></span></td></tr>';

                }
            }
        }
        //Display browsed files on the screen to the user who wants to upload them
        $("#vpb_added_files_box").append(vpb_added_files_displayer);
        $("#added_class").val(new_classc);
    }
}

function vpb_file_ext(file) {
    return (/[.]/.exec(file)) ? /[^.]+$/.exec(file.toLowerCase()) : '';
}

function vpb_remove_this_file(id, filename){

  $("#file_cert_mul_err").html("");
    bootbox.confirm({
        title: "Remove File",
        message: "Are you sure? Do you want to remove this file?",
        buttons: {
            confirm: {
                label: 'Yes',
                className: 'btn-success'
            },
            cancel: {
                label: 'No',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if (result) {
                const index = storedFiles.indexOf(storedFiles[id])
                if (index > -1) {
                    storedFiles.splice(index, 1);
                }
                $("input[name=vasplus_multiple_files]").val(storedFiles);
                domCreator(storedFiles);
 fetchUploadedFileCounts();
            }
        }
    });  
}