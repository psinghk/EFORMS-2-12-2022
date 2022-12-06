$(document).ready(function() {
    initializeDnsTable()
});

function initializeDnsTable() {
    $('.loader').show();
    $('#dnsTeamData').DataTable({
        "jQueryUI" : true,
        "pagingType" : "full_numbers",
        "pageLength": 50,
        "processing": true,
        "bDestroy": true,
        "bServerSide": true,
        "sAjaxSource" : "dns-domaintracker-json",
        "sServerMethod": "GET",
        "columnDefs": [
            {
                "targets": [0, 6], //first and last not sortable
                "orderable": false,
                className: "text-center", "targets": [ 0, 6 ]
            },
        ],
        "aoColumns": [
            { "sWidth": "8%" }
        ],
        "fnRowCallback" : function(nRow, aData, iDisplayIndex){
            $("td:first", nRow).html(iDisplayIndex +1);
           return nRow;
        },
        "fnDrawCallback": function (data) {
            $('.loader').hide();
        }
    });
}

$("#add-dns-rcd").click(function() {
    $('.text-danger').html('');
    $("#dnsTrckerFrm input").val('');
    $("#addDnsTracker").html("Add Record");
    $("#domain").attr("readonly", false);
    $("#dnsTrckerFrm #dnsteamid").val(0);
    $("#dnsTrackerForm").modal({backdrop: 'static', keyboard: false});
});

$('.domain_err').hide();
$('.ip_err').hide();
$('.name_err').hide();
$('.email_err').hide();
$('.mobile_err').hide();

var domain_err = false;
var ip_err = false;
var name_err = false;
var email_err = false;
var mobile_err = false;

$("#name").focusout(function () {
    check_name();
});
$("#email").focusout(function () {
    check_email();
});
$("#ip").focusout(function () {
    check_ip();
});
$("#domain").focusout(function () {
    check_domain();
});
$("#mobile").focusout(function () {
    check_mobile();
});

function check_mobile() {
    var pattern = new RegExp(/^[0-9-+]+$/);
    if (!pattern.test($("#mobile").val())) {
        $(".mobile_err").html("Please Enter valid Contact Number. Do not Use (+91)");
        $(".mobile_err").show();
        mobile_err = true;
    } else {
        $(".mobile_err").hide();
        mobile_err = false;
    }
}

function check_domain() {
    var pattern = new RegExp(/[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)?/gi);
    if (!pattern.test($("#domain").val())) {
        $(".domain_err").html("Please Enter valid Domain.");
        $(".domain_err").show();
        domain_err = true;
    } else {
        $(".domain_err").hide();
        domain_err = false;
    }
}

function check_email() {
    var pattern = new RegExp(/^\b[A-Z0-9._%-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b$/i);
    if (!pattern.test($("#email").val())) {
        $(".email_err").html("Please Enter valid Email.");
        $(".email_err").show();
        email_err = true;
    } else {
        $(".email_err").hide();
        email_err = false;
    }
}
function check_ip() {
    var cname_pattern = new RegExp(/[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)?/gi);
    var pattern = new RegExp(/((^\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\s*$)|(^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$))/);
    if (!pattern.test($("#ip").val())) {
        if(!cname_pattern.test($("#ip").val())) {
            $(".ip_err").html("Please Enter a Valid CNAME or IP.");
            $(".ip_err").show();
            ip_err = true;
        } else {
            $(".ip_err").hide();
            ip_err = false;
        }
    } else {
        $(".ip_err").hide();
        ip_err = false;
    }
}
function check_name() {
    var cname = $("#name").val();
    if (cname.length < 1) {
        $(".name_err").html("Please enter your name.");
        $(".name_err").show();
        name_err = true;
    } else {
        $(".name_err").hide();
        name_err = false;
    }
}

$("#addDnsTracker").click(function() {
    $('.text-danger').html('');
    name_err = false;
    ip_err = false;
    email_err = false;
    domain_err = false;
    mobile_err = false;

    check_email();
    check_name();
    check_ip();
    check_domain();
    check_mobile();
    if (email_err == false && name_err == false && ip_err == false && domain_err == false && mobile_err == false) {
        $(".loader").show();
        var dataObj = {dnsTrckerFrmData: $('#dnsTrckerFrm').serializeObject()};
        var newdata = JSON.stringify(dataObj);
        $.ajax({
            type: 'post',
            url: 'dnsTrackerFrmData-post',
            data: newdata,
            datatype: JSON,
            contentType: 'application/json; charset=utf-8',
            success: function (data) {
                if(data.dnsTrckerFrmData.errorMessage) {
                    $('.domain_err').show();
                    $('.ip_err').show();
                    $('.name_err').show();
                    $('.email_err').show();
                    $('.mobile_err').show();
                    console.log(data.dnsTrckerFrmData.errorMessage)
                    $.each(data.dnsTrckerFrmData.errorMessage, function(key, val) {
                        $('.'+key).html(val);
                    })
                }
                if(data.rcd_inserted) {
                    if(data.rcd_inserted.indexOf("does not exists")!= -1) {
                        $(".msg-alert").addClass('alert alert-danger');
                        $(".msg-alert").html("<b>"+data.rcd_inserted+"</b>");
                        setTimeout(function(){ 
                            $(".msg-alert").removeClass('alert alert-danger');
                            $(".msg-alert").html("");
                        }, 15000);
                    } else {
                        initializeDnsTable();
                        $(".msg-alert").addClass('alert alert-success');
                        $(".msg-alert").html("<b>DNS Record "+data.rcd_inserted+" Successfully.</b>");
                        $("#dnsTrackerForm").modal('hide');
                        setTimeout(function(){ 
                            $(".msg-alert").removeClass('alert alert-success');
                            $(".msg-alert").html("");
                        }, 15000);
                    }
                }
            }, 
            complete: function() {
                $(".loader").hide();
            }
        })
        return false;
    } else {
        return false;
    }
});
    
function dsnRecordEdit(i) {
    $("#domain").attr("readonly", true);
    $.ajax({
        type:  'post',
        url: 'dnsTrackerFrmData-post-fetch',
        data: {dnsTeamId: i},
        success: function(data) {
            $.each(data.dnsTractFetchData[0], function(key, val) {
                $("#"+key).val(val);
                $("#addDnsTracker").html("Update Record")
            });
            $("#dnsTrackerForm").modal({backdrop: 'static', keyboard: false});
        }
    });
}

function dsnRecordDelete(i) {
    bootbox.confirm({
        title: "Delete Domain",
        message: "Are you sure? Do you want to delete this Domain?",
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
            if(result) {
                $(".loader").show();
                $.ajax({
                    type: 'post',
                    url: 'dnsTrackerFrmData-post-delete',
                    data: {dnsTeamId: i},
                    success: function(data) {
                        if(data.rcd_inserted) {
                            if(data.rcd_inserted.indexOf("deleted Successfully") != -1) {
                                initializeDnsTable();
                                $(".msg-alert").addClass('alert alert-success');
                                $(".msg-alert").html("<b>"+data.rcd_inserted+"</b>");
                                setTimeout(function(){ 
                                    $(".msg-alert").removeClass('alert alert-success');
                                    $(".msg-alert").html("");
                                }, 15000);
                            } else {
                                $(".msg-alert").addClass('alert alert-danger');
                                $(".msg-alert").html("<b>"+data.rcd_inserted+"</b>");
                                setTimeout(function(){ 
                                    $(".msg-alert").removeClass('alert alert-danger');
                                    $(".msg-alert").html("");
                                }, 15000);
                            }
                        }
                    }, 
                    complete: function() {
                        $(".loader").hide();
                    }
                })
            }
        }
    });
}