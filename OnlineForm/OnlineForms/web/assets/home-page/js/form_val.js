$(function() {
    
    $(window).load(function() {
        $('#myModal').modal({
            backdrop: 'static',
            show: true
        });
    });
    $("#first_error_message").hide();
    $("#last_error_message").hide();
    $("#email_error_message").hide();
    $("#msg_error_message").hide();
    $("#captcha_error_message").hide();
    var error_username = false;
    var error_email = false;
    var error_msg = false;
    var captcha_msg = false;

    $("#form_first").focusout(function() {
        check_username();
    });
    $("#form_email").focusout(function() {
        check_email();
    });
    $("#msg_txt_message").focusout(function() {
        check_txtmsg();
    });
    $("#imgtxt_feedback").focusout(function() {
        check_captcha()
    });

    function check_username() {
        var username_length = $("#form_first").val().length;
        if (username_length < 5 || username_length > 20) {
            $("#first_error_message").html("Please Enter a Valid First Name.");
            $("#first_error_message").show();
            error_username = true;
        } else {
            $("#first_error_message").hide();
            error_username = false;
        }
    }

    function check_txtmsg() {
        var username_length = $("#msg_txt_message").val().length;
        if (username_length < 2) {
            $("#msg_error_message").html("Please Enter a Valid Message.");
            $("#msg_error_message").show();
            error_msg = true;
        } else {
            $("#msg_error_message").hide();
            error_msg = false;
        }
    }

    function check_email() {
        var pattern = new RegExp(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
        if (pattern.test($("#form_email").val())) {
            $("#email_error_message").hide();
            error_email = false;
        } else {
            $("#email_error_message").html("Invalid email address");
            $("#email_error_message").show();
            error_email = true;
        }
    }

    function check_captcha() {
        var imgtxt_feedback = $("#imgtxt_feedback").val().length;
        if (imgtxt_feedback < 5 || imgtxt_feedback > 20) {
            $("#captcha_error_message").html("Please Enter a Valid Captcha.");
            $("#captcha_error_message").show();
            captcha_msg = true;
        } else {
            $("#captcha_error_message").hide();
            captcha_msg = false;
        }
    }

    $("#registration_form").submit(function() {

        error_username = false;
        error_email = false;
        error_msg = false;
        captcha_msg = false;

        check_username();
        check_email();
        check_txtmsg();
        check_captcha();

        if (error_username == false && error_email == false && error_msg == false && captcha_msg == false) {
            if (confirm('Do you want to submit feedback.')) {
                var fname_txt = $("#form_first").val() + ' ' + $("#form_last").val();
                var email_txt = $("#form_email").val();
                var msg_txt = $("#msg_txt_message").val();
                var imgtxt_feedback = $("#imgtxt_feedback").val();
                var data1 = JSON.stringify({ name: fname_txt, email: email_txt, msg: msg_txt, imgtxt_feedback: imgtxt_feedback });
                $.ajax({
                    type: 'post',
                    url: 'feedback_us',
                    data: data1,
                    datatype: JSON,
                    contentType: 'application/json; charset=utf-8',
                    success: function(data) {
                        if (data.res_msg.cap_error) {
                            $("#captcha_error_message").show();
                            $("#captcha_error_message").html(data.res_msg.cap_error);
                        }
                        if (data.res_msg.cap_succ) {
                            $('.msg_div').addClass('alert alert-success');
                            $(".msg_div").text(data.res_msg.cap_succ);
                            $(".msg_div").delay(10000).fadeOut();
                            $("#registration_form input").val('');
                            $("#registration_form textarea").val('');
                        }
                        feedback_captcha_reset()
                    }
                })
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    });

});

$('#myModallogin').modal({
    backdrop: 'static',
    keyboard: false,
    show: false
});
addEventListener("load", function() {
    setTimeout(hideURLbar, 0);
}, false);

function hideURLbar() {
    window.scrollTo(0, 1);
}
document.getElementById("year").innerHTML = new Date().getFullYear();


$(document).ready(function() {
    $.ajax({
        type: 'post',
        url: 'RequestCounter',
        success: function(data) {
            console.log(data)
            $(".usercount").text(addCommas(data.val.usercount));
            $(".totalcount").text(addCommas(data.val.totalrequest));
            $(".pendingcount").text(addCommas(data.val.totalpending));
            $(".completedcount").text(addCommas(data.val.totalComplete));
        }
    });

    $("#refresh").on("click", function() {
        $('#captcha2').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
    });
});
$('.modal').on('shown.bs.modal', function() {
    $("#header").removeAttr("style");
    $("body").removeAttr("style");
    $(".modal").css({ paddingRight: "0px" });
    $("body").css({ 'overflow': "auto" });
});
$(".c-btn--action").click(function() {
    $("body").removeAttr("style");
})

var refreshes = parseInt(sessionStorage.getItem('refreshes'), 10) || 0;

sessionStorage.setItem('refreshes', ++refreshes);


function loginEform() {
    navToggle();
    bootbox.confirm({
        title: "Login Notice",
        message: `<ul class="login-ul">
            <li>For ease of user onboarding , eForms has now been integrated with NIC Single Sign-On Platform (Parichay). Now, users will be authenticated through Parichay(SSO).</li>
            <li>If you are a Non Gov user, Then Login from eForms Portal.</li>
        </ul>`,
        buttons: {
            confirm: {
                label: 'Login with EForms',
                className: 'btn-primary loginbtn'
            },
            cancel: {
                label: 'Login with Parichay (SSO)',
                className: 'btn-success loginbtn'
            }
        },
        callback: function(result) {
            if (result) {
                $('#myModallogin').modal({
                    backdrop: 'static',
                    keyboard: false
                })
            } else {

                window.location.href = 'https://parichay.nic.in/Accounts/NIC/index.html?service=eforms';

            }
        }
    });
}


$("#mod_close_mobile").click(function() {

    $("#myModallogin").modal('show');
})

function addCommas(nStr) {
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    return x1 + x2;
}
var mob_prefix = [
    { "prefix" : "+213", "country" : "Algeria" },
    { "prefix" : "+376", "country" : "Andorra" },
    { "prefix" : "+244", "country" : "Angola" },
    { "prefix" : "+1264", "country" : "Anguilla" },
    { "prefix" : "+1268", "country" : "Antigua and Barbuda" },
    { "prefix" : "+599", "country" : "Antilles (Dutch)" },
    { "prefix" : "+54", "country" : "Argentina" },
    { "prefix" : "+374", "country" : "Armenia" },
    { "prefix" : "+297", "country" : "Aruba" },
    { "prefix" : "+247", "country" : "Ascension Island" },
    { "prefix" : "+61", "country" : "Australia" },
    { "prefix" : "+43", "country" : "Austria" },
    { "prefix" : "+994", "country" : "Azerbaijan" },
    { "prefix" : "+1242", "country" : "Bahamas" },
    { "prefix" : "+973", "country" : "Bahrain" },
    { "prefix" : "+880", "country" : "Bangladesh" },
    { "prefix" : "+1246", "country" : "Barbados" },
    { "prefix" : "+375", "country" : "Belarus" },
    { "prefix" : "+32", "country" : "Belgium" },
    { "prefix" : "+501", "country" : "Belize" },
    { "prefix" : "+229", "country" : "Benin" },
    { "prefix" : "+1441", "country" : "Bermuda" },
    { "prefix" : "+975", "country" : "Bhutan" },
    { "prefix" : "+591", "country" : "Bolivia" },
    { "prefix" : "+387", "country" : "Bosnia Herzegovina" },
    { "prefix" : "+267", "country" : "Botswana" },
    { "prefix" : "+55", "country" : "Brazil" },
    { "prefix" : "+673", "country" : "Brunei" },
    { "prefix" : "+359", "country" : "Bulgaria" },
    { "prefix" : "+226", "country" : "Burkina Faso" },
    { "prefix" : "+257", "country" : "Burundi" },
    { "prefix" : "+855", "country" : "Cambodia" },
    { "prefix" : "+237", "country" : "Cameroon" },
    { "prefix" : "+1", "country" : "Canada" },
    { "prefix" : "+238", "country" : "Cape Verde Islands" },
    { "prefix" : "+1345", "country" : "Cayman Islands" },
    { "prefix" : "+236", "country" : "Central African Republic" },
    { "prefix" : "+56", "country" : "Chile" },
    { "prefix" : "+86", "country" : "China" },
    { "prefix" : "+57", "country" : "Colombia" },
    { "prefix" : "+269", "country" : "Comoros" },
    { "prefix" : "+242", "country" : "Congo" },
    { "prefix" : "+682", "country" : "Cook Islands" },
    { "prefix" : "+506", "country" : "Costa Rica" },
    { "prefix" : "+385", "country" : "Croatia" },
    { "prefix" : "+53", "country" : "Cuba" },
    { "prefix" : "+90392", "country" : "Cyprus North" },
    { "prefix" : "+357", "country" : "Cyprus South" },
    { "prefix" : "+42", "country" : "Czech Republic" },
    { "prefix" : "+45", "country" : "Denmark" },
    { "prefix" : "+2463", "country" : "Diego Garcia" },
    { "prefix" : "+253", "country" : "Djibouti" },
    { "prefix" : "+1809", "country" : "Dominica" },
    { "prefix" : "+1809", "country" : "Dominican Republic" },
    { "prefix" : "+593", "country" : "Ecuador" },
    { "prefix" : "+20", "country" : "Egypt" },
    { "prefix" : "+353", "country" : "Eire" },
    { "prefix" : "+503", "country" : "El Salvador" },
    { "prefix" : "+240", "country" : "Equatorial Guinea" },
    { "prefix" : "+291", "country" : "Eritrea" },
    { "prefix" : "+372", "country" : "Estonia" },
    { "prefix" : "+251", "country" : "Ethiopia" },
    { "prefix" : "+500", "country" : "Falkland Islands" },
    { "prefix" : "+298", "country" : "Faroe Islands" },
    { "prefix" : "+679", "country" : "Fiji" },
    { "prefix" : "+358", "country" : "Finland" },
    { "prefix" : "+33", "country" : "France" },
    { "prefix" : "+594", "country" : "French Guiana" },
    { "prefix" : "+689", "country" : "French Polynesia" },
    { "prefix" : "+241", "country" : "Gabon" },
    { "prefix" : "+220", "country" : "Gambia" },
    { "prefix" : "+7880", "country" : "Georgia" },
    { "prefix" : "+49", "country" : "Germany" },
    { "prefix" : "+233", "country" : "Ghana" },
    { "prefix" : "+350", "country" : "Gibraltar" },
    { "prefix" : "+30", "country" : "Greece" },
    { "prefix" : "+299", "country" : "Greenland" },
    { "prefix" : "+1473", "country" : "Grenada" },
    { "prefix" : "+590", "country" : "Guadeloupe" },
    { "prefix" : "+671", "country" : "Guam" },
    { "prefix" : "+502", "country" : "Guatemala" },
    { "prefix" : "+224", "country" : "Guinea" },
    { "prefix" : "+245", "country" : "Guinea - Bissau" },
    { "prefix" : "+592", "country" : "Guyana" },
    { "prefix" : "+509", "country" : "Haiti" },
    { "prefix" : "+504", "country" : "Honduras" },
    { "prefix" : "+852", "country" : "Hong Kong" },
    { "prefix" : "+36", "country" : "Hungary" },
    { "prefix" : "+354", "country" : "Iceland" },
    { "prefix" : "+91", "country" : "India" },
    { "prefix" : "+62", "country" : "Indonesia" },
    { "prefix" : "+98", "country" : "Iran" },
    { "prefix" : "+964", "country" : "Iraq" },
    { "prefix" : "+972", "country" : "Israel" },
    { "prefix" : "+39", "country" : "Italy" },
    { "prefix" : "+225", "country" : "Ivory Coast" },
    { "prefix" : "+1876", "country" : "Jamaica" },
    { "prefix" : "+81", "country" : "Japan" },
    { "prefix" : "+962", "country" : "Jordan" },
    { "prefix" : "+7", "country" : "Kazakhstan" },
    { "prefix" : "+254", "country" : "Kenya" },
    { "prefix" : "+686", "country" : "Kiribati" },
    { "prefix" : "+850", "country" : "Korea North" },
    { "prefix" : "+82", "country" : "Korea South" },
    { "prefix" : "+965", "country" : "Kuwait" },
    { "prefix" : "+996", "country" : "Kyrgyzstan" },
    { "prefix" : "+856", "country" : "Laos" },
    { "prefix" : "+371", "country" : "Latvia" },
    { "prefix" : "+961", "country" : "Lebanon" },
    { "prefix" : "+266", "country" : "Lesotho" },
    { "prefix" : "+231", "country" : "Liberia" },
    { "prefix" : "+218", "country" : "Libya" },
    { "prefix" : "+417", "country" : "Liechtenstein" },
    { "prefix" : "+370", "country" : "Lithuania" },
    { "prefix" : "+352", "country" : "Luxembourg" },
    { "prefix" : "+853", "country" : "Macao" },
    { "prefix" : "+389", "country" : "Macedonia" },
    { "prefix" : "+261", "country" : "Madagascar" },
    { "prefix" : "+265", "country" : "Malawi" },
    { "prefix" : "+60", "country" : "Malaysia" },
    { "prefix" : "+960", "country" : "Maldives" },
    { "prefix" : "+223", "country" : "Mali" },
    { "prefix" : "+356", "country" : "Malta" },
    { "prefix" : "+692", "country" : "Marshall Islands" },
    { "prefix" : "+596", "country" : "Martinique" },
    { "prefix" : "+222", "country" : "Mauritania" },
    { "prefix" : "+230", "country" : "Mauritius" },
    { "prefix" : "+269", "country" : "Mayotte" },
    { "prefix" : "+52", "country" : "Mexico" },
    { "prefix" : "+691", "country" : "Micronesia" },
    { "prefix" : "+373", "country" : "Moldova" },
    { "prefix" : "+377", "country" : "Monaco" },
    { "prefix" : "+976", "country" : "Mongolia" },
    { "prefix" : "+1664", "country" : "Montserrat" },
    { "prefix" : "+212", "country" : "Morocco" },
    { "prefix" : "+258", "country" : "Mozambique" },
    { "prefix" : "+95", "country" : "Myanmar" },
    { "prefix" : "+264", "country" : "Namibia" },
    { "prefix" : "+674", "country" : "Nauru" },
    { "prefix" : "+977", "country" : "Nepal" },
    { "prefix" : "+31", "country" : "Netherlands" },
    { "prefix" : "+687", "country" : "New Caledonia" },
    { "prefix" : "+64", "country" : "New Zealand" },
    { "prefix" : "+505", "country" : "Nicaragua" },
    { "prefix" : "+227", "country" : "Niger" },
    { "prefix" : "+234", "country" : "Nigeria" },
    { "prefix" : "+683", "country" : "Niue" },
    { "prefix" : "+672", "country" : "Norfolk Islands" },
    { "prefix" : "+670", "country" : "Northern Marianas" },
    { "prefix" : "+47", "country" : "Norway" },
    { "prefix" : "+968", "country" : "Oman" },
    { "prefix" : "+92", "country" : "Pakistan" },
    { "prefix" : "+680", "country" : "Palau" },
    { "prefix" : "+507", "country" : "Panama" },
    { "prefix" : "+675", "country" : "Papua New Guinea" },
    { "prefix" : "+595", "country" : "Paraguay" },
    { "prefix" : "+51", "country" : "Peru" },
    { "prefix" : "+63", "country" : "Philippines" },
    { "prefix" : "+48", "country" : "Poland" },
    { "prefix" : "+351", "country" : "Portugal" },
    { "prefix" : "+1787", "country" : "Puerto Rico" },
    { "prefix" : "+974", "country" : "Qatar" },
    { "prefix" : "+262", "country" : "Reunion" },
    { "prefix" : "+40", "country" : "Romania" },
    { "prefix" : "+7", "country" : "Russia" },
    { "prefix" : "+250", "country" : "Rwanda" },
    { "prefix" : "+378", "country" : "San Marino" },
    { "prefix" : "+239", "country" : "Sao Tome and Principe" },
    { "prefix" : "+966", "country" : "Saudi Arabia" },
    { "prefix" : "+221", "country" : "Senegal" },
    { "prefix" : "+381", "country" : "Serbia" },
    { "prefix" : "+248", "country" : "Seychelles" },
    { "prefix" : "+232", "country" : "Sierra Leone" },
    { "prefix" : "+65", "country" : "Singapore" },
    { "prefix" : "+421", "country" : "Slovak Republic" },
    { "prefix" : "+386", "country" : "Slovenia" },
    { "prefix" : "+677", "country" : "Solomon Islands" },
    { "prefix" : "+252", "country" : "Somalia" },
    { "prefix" : "+27", "country" : "South Africa" },
    { "prefix" : "+34", "country" : "Spain" },
    { "prefix" : "+94", "country" : "Sri Lanka" },
    { "prefix" : "+290", "country" : "St. Helena" },
    { "prefix" : "+1869", "country" : "St. Kitts" },
    { "prefix" : "+1758", "country" : "St. Lucia" },
    { "prefix" : "+249", "country" : "Sudan" },
    { "prefix" : "+597", "country" : "Suriname" },
    { "prefix" : "+268", "country" : "Swaziland" },
    { "prefix" : "+46", "country" : "Sweden" },
    { "prefix" : "+41", "country" : "Switzerland" },
    { "prefix" : "+963", "country" : "Syria" },
    { "prefix" : "+886", "country" : "Taiwan" },
    { "prefix" : "+7", "country" : "Tajikstan" },
    { "prefix" : "+66", "country" : "Thailand" },
    { "prefix" : "+228", "country" : "Togo" },
    { "prefix" : "+676", "country" : "Tonga" },
    { "prefix" : "+1868", "country" : "Trinidad and Tobago" },
    { "prefix" : "+216", "country" : "Tunisia" },
    { "prefix" : "+90", "country" : "Turkey" },
    { "prefix" : "+7", "country" : "Turkmenistan" },
    { "prefix" : "+993", "country" : "Turkmenistan" },
    { "prefix" : "+1649", "country" : "Turks and Caicos Islands" },
    { "prefix" : "+688", "country" : "Tuvalu" },
    { "prefix" : "+256", "country" : "Uganda" },
    { "prefix" : "+44", "country" : "UK" },
    { "prefix" : "+380", "country" : "Ukraine" },
    { "prefix" : "+971", "country" : "United Arab Emirates" },
    { "prefix" : "+598", "country" : "Uruguay" },
    { "prefix" : "+1", "country" : "USA" },
    { "prefix" : "+7", "country" : "Uzbekistan" },
    { "prefix" : "+678", "country" : "Vanuatu" },
    { "prefix" : "+379", "country" : "Vatican City" },
    { "prefix" : "+58", "country" : "Venezuela" },
    { "prefix" : "+84", "country" : "Vietnam" },
    { "prefix" : "+84", "country" : "Virgin Islands - British" },
    { "prefix" : "+84", "country" : "Virgin Islands - US" },
    { "prefix" : "+681", "country" : "Wallis and Futuna" },
    { "prefix" : "+969", "country" : "Yemen (North)" },
    { "prefix" : "+967", "country" : "Yemen (South)" },
    { "prefix" : "+381", "country" : "Yugoslavia" },
    { "prefix" : "+243", "country" : "Zaire" },
    { "prefix" : "+260", "country" : "Zambia" },
    { "prefix" : "+263", "country" : "Zimbabwe" }
];
function updateMobile() {
    var dt = '';
    $("#country_code_update_mob").html('');
    dt = '<option value="">Select Ext</option>';
    dt += '<option value="+91" selected>+91 (India)</option>';
    $.each(mob_prefix, function(i, v) {
        dt += '<option value="'+v.prefix+'" title="'+v.country+'">'+v.prefix+' ('+v.country+')</option>';
    })
    $("#country_code_update_mob").html(dt);
    $("#updateMobile").modal('show');
    $(this).click(function(){
        $(this).data('clicked', true);
    });
    if($(this).data('clicked')) {
        navToggle(true);
    }
}

$("#tnc").click(function() {
    if($(this).is(":checked")) {
        $("#continueId").removeClass('d-none');
    } else {
        $("#continueId").addClass('d-none');
    }
})

function navToggle(i) {
    $('body').toggleClass('mobile-nav-active');
    $('.mobile-nav-toggle i').toggleClass('fa-times fa-bars');
    $('.mobile-nav-overly').toggle();
}