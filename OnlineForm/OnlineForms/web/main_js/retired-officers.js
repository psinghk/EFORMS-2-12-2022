function submitByRetiredOfficers() {
    var captcha = $("#captcha").val();
    var CSRFRandom = $("#CSRFRandom").val();
    $.ajax({
        type: "POST",
        url: "dor_ext_retired_tab1_new",
        data: {captcha: captcha, CSRFRandom: CSRFRandom},
        datatype: JSON,
        success: function (data) {
            resetCSRFRandom();
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            var error_flag = true;
            if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined) {
                $('#dor_ext1 #captchaerror').html(jsonvalue.error.cap_error)
                $('#dor_ext1 #captcha1').focus();
                error_flag = false;
                $('#dor_ext1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#dor_ext1 #imgtxt').val("");
            } else {
                $('#dor_ext1 #captchaerror').html("")
            }

            if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined) {
                $('#dor_ext1 #commonErr').html(jsonvalue.error.csrf_error)
                error_flag = false;
            } else {
                $('#dor_ext1 #commonErr').html("")
            }

            if (error_flag && jsonvalue.error != "" && jsonvalue.error != null && jsonvalue.error != undefined) {
                $('#dor_ext2 #dor_email').val($('#dor_ext1 #dor_email').val());
                $('#dor_ext2 #p_email_single_dor').val($('#dor_ext1 #p_email_single_dor').val());
                $('#dor_ext2 #p_email_single_name').val($('#dor_ext1 #p_email_single_name').val());
                $('#dor_ext2 #p_email_single_mobile').val($('#dor_ext1 #p_email_single_mobile').val());
                $('#dor_ext2 #p_email_single_newdor').val($('#dor_ext1 #p_email_single_newdor').val());
                $('#retired_basic').removeClass('display-show');
                $('#retired_basic').addClass('display-hide');
                $('#retired_preview').removeClass('display-hide');
                $('#retired_preview').addClass('display-show');
            }
        }, error: function () {
            console.log('error');
        }
    });
}
;

//$('#dor_ext_confirm #confirmYes').click(function () {
//    dor_ext_submit();
//    $('#stack3').modal('toggle');
//});

$('#dor_ext2').submit(dor_ext_submit);
var dor_ext_submit = function (e) {

}

function submitPeviewByRetiredOfficers() {
    if (document.getElementById("tnc_retired").checked === false) {
        $("#dor_ext2 #tnc_error").html('Please check terms and conditions.');
        return false;
    }
    var csrf = $('#CSRFRandom').val();
    $.ajax({
        type: "POST",
        url: "consentRetired",
        data: {check: 'esign', formtype: 'dor_ext_retired', CSRFRandom: csrf, retired_no_yes: 'yes'},
        success: function (check) {
            window.location.href = "esignPdfRetired";
        },
        error: function () {
            console.log('error');
        }
    });
}

function resetCSRFRandom() {
    $.ajax({
        type: "POST",
        url: "resetCSRFRandom",
        datatype: JSON,
        success: function (jsonResponse)
        {
            $("#CSRFRandom").val(jsonResponse.random);
        }
        , error: function ()
        {
        }
    });
}

$("#dor_ext1 #captcha11").keyup(function () {
    var passVal = $("#dor_ext1 #password").val();
    console.log(passVal);
    var captchaVal = $("#dor_ext1 #captcha11").val();
    if (passVal.length > 2 && captchaVal.length > 5) {
        $("#continueId").prop('disabled', false);
    } else {
        $("#continueId").prop('disabled', true);
    }
});

$('#dor_ext1 #refresh').click(function () {
    console.log(" refrsh " + Date.parse(new Date().toString()));
    $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
});

function refreshCaptcha() {
    console.log(" refrsh " + Date.parse(new Date().toString()));
    $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
}
