function track_frm(ref_no)
{
    $('.loader').show();
    $('.order-tracker li').removeClass('active-track');
    $('.order-tracker li').removeClass('pending-track');
    $('.order-tracker li').removeClass('reject-track');
    $(".fa-caret-down").hide();
    var csrf = $('#CSRFRandom').val();
    $.ajax({
        url: "fetchTrackAll",
        type: "POST",
        data: {reg_no: ref_no, CSRFRandom: csrf},
        success: function (data) {
            if(data.csrf_error) {
                alert(data.csrf_error)
            }
            $("#track-msg").html("");
            console.log(data)
            var str = "";
            var ul_str = "";
            var value = data.hmTrack.msg.split('=>');
            $("#refnumbertrack").text(data.hmTrack.reg_no)
            $.each(value, function (i, val) {
                trc_list = val.split("->");
                if (trc_list[1]) {
                    str += "<tr><td><b>" + trc_list[0] + ":</b></td><td>" + trc_list[1] + "</td></tr>";
                }
            });
            $('#query_raise_hyperlink').attr('onclick', 'apply_heading("'+data.hmTrack.reg_no+'", "'+data.hmTrack.form_name+'", "query_raise", "");resetRandom();');
            //var counter=data.hmTrack.roles.length;
            var roles = data.hmTrack.roles.filter(function(v){return v!==''});
            console.log(roles)
            console.log(data.hmTrack.status)
             var oldvalue;
             var old_forward_by;
            $.each(roles, function (i, val) {
                var newval = val.split('=>');
                var forword_to = newval[1];
                console.log(val)
                val = newval[0];
                if(i == 0){
                    old_forward_by = "";
                }else{
                    old_forward_by = oldvalue[0];
                }
                
                console.log("Old Sender: "+old_forward_by)
                
                if (val === 'a') {
                    ul_str += "<li class=\"a-track\"><div class=\"li-track-desc\">User</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"','"+ i +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 'c') {
                    ul_str += "<li class=\"c-track\"><div class=\"li-track-desc\">CO</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"','"+ i +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 'ca') {
                    ul_str += "<li class=\"ca-track\"><div class=\"li-track-desc\">RO/FO/Nodal</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"','"+ i +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 's') {
                    ul_str += "<li class=\"s-track\"><div class=\"li-track-desc\">Support</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"','"+ i +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 'm') {
                    ul_str += "<li class=\"m-track\"><div class=\"li-track-desc\">Admin</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"','"+ i +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 'd') {
                    ul_str += "<li class=\"d-track\"><div class=\"li-track-desc\">DA Admin</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"','"+ i +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                if (val === 'us') {
                    ul_str += "<li class=\"us-track\"><div class=\"li-track-desc\">US/JS/AS/S</div><div class=\"li-tracker\"><span class=\"cercle-order\" onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"','"+ i +"');\" title='Show Details'><i class=\"fa fa-caret-down\"></i></span></div></li>";
                }
                $('ul.order-tracker').html(ul_str);
                setTimeout(function () {
                    var width = (100 / roles.length);
                    $('ul.order-tracker li').css('width', width + "%");
                    $('.' + val + '-track span.cercle-order');
                    $("ul.order-tracker li:last-child").addClass(data.hmTrack.status + '-track curr-track');
                    $("ul.order-tracker li:not(:last-child)").addClass('active-track');
                    
                    $(".cercle-order").click(function () {
                        $(".fa-caret-down").hide();
                        $(this).closest('.li-tracker').find('.fa-caret-down').show();
                    });
                }, 100)
                oldvalue = newval;
            });

            $("#track-msg").html("<table class='table table-bordered'>" + str + "</table>")
            $("#user_data_tbody").html('<tr class="table-success"><td>' + data.hmTrack.name + '</td><td>' + data.hmTrack.email + '</td><td>' + data.hmTrack.mob + '</td><td>' + data.hmTrack.forword_date + '</td></tr>')
            $('#basic_track').modal('show', {backdrop: 'static'});
        },
        error: function (data) {
             var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    var error = JSON.parse(jsonvalue.responseText.substring(0,jsonvalue.responseText.indexOf("}")+1)).error;
                    $('#new_alert .modal-body').html(error);
                    $('#new_alert').modal('show');
                    $('.loader').hide();
                    console.log('error');
//            alert('Error: coming from fetchTrackAll Error from server side');
        },
        complete: function(){
            $('.loader').hide();
            resetCSRFRandomToken();
        }
    });

    // end, code added by pr on 22ndaug19    
}

function fetchTrackByRole(forward_by, state, trole_txt, regno,position) {
   var csrf = $('#CSRFRandom').val();
    //$('.loader').hide();
    $.ajax({
        url: "fetchTrackByRole",
        type: "POST",
        data: {srole: state, reg_no: regno, trole: trole_txt, forward: forward_by, position:position, CSRFRandom: csrf},
        success: function (data) {
            console.log(data)
            $("#track-msg").html("");
            var str = "";
            var value = data.hmTrack.msg.split('=>');
            $.each(value, function (i, val) {
                trc_list = val.split("->");
                if (trc_list[1]) {
                    console.log(trc_list[1])
                    str += "<tr><td><b>" + trc_list[0] + ":</b></td><td>" + trc_list[1] + "</td></tr>";
                }
            });

            $("#track-msg").html("<table class='table table-bordered'>" + str + "</table>")
        },
        error: function () {
            alert('Error: coming from fetchTracbyrole Error from server side');
        },
        complete: function() {
            $('.loader').hide();
             resetCSRFRandomToken();
        }
    });

}

$(".on_hold").click(function(){
    console.log($(this))
    if ($(this).prop('checked')) {
        $('input.stat').attr("disabled", true);
        $('input.stat').closest('label').css('color','#ccc');
        $('input.stat').closest('label').css('color','#ccc');
        $('input.stat.pending_chk').attr("disabled", false);
        $('input.stat.on_hold').attr("disabled", false);
        $('input.stat.on_hold').closest('label').css('color','#333');
        $('input.stat.pending_chk').closest('label').css('color','#333');
    } else {
        $('input.stat').attr("disabled", false);
        $('input.stat').closest('label').css('color','#333');
    }
});
$(".query_raised").click(function(){
    if ($(this).prop('checked')) {
        $('input.on_hold').attr("disabled", true);
        $('input.on_hold').closest('label').css('color','#ccc');
        $('input.query_answered').attr("disabled", true);
        $('input.query_answered').closest('label').css('color','#ccc');
        $('input.stat.query_raised').attr("disabled", false);
    } else {
        $('input.stat').attr("disabled", false);
        $('input.stat').closest('label').css('color','#333');
    }
});
$(".query_answered").click(function(){
    if ($(this).prop('checked')) {
        $('input.on_hold').attr("disabled", true);
        $('input.on_hold').closest('label').css('color','#ccc');
        $('input.query_answered').attr("disabled", false);
        $('input.stat.query_raised').attr("disabled", true);
        $('input.stat.query_raised').closest('label').css('color','#ccc');
    } else {
        $('input.stat').attr("disabled", false);
        $('input.stat').closest('label').css('color','#333');
    }
});

function resetCSRFRandomToken()
{
    $.ajax({
        type: "POST",
        url: "resetCSRFRandom",
        datatype: JSON,
        success: function (jsonResponse)
        {
            console.log(jsonResponse.random)
            $("#CSRFRandom").val(jsonResponse.random);
        }
    });
}