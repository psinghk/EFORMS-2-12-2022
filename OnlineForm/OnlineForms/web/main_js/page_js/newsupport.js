
function showAlias()
{
    var domain = $("#domain").val();
    if (domain == "nic.in") // if domain is nic.in then no need to create mailequiv with nic.in as primary itself contains that
    {
        $("#alias_id").val("");
    } else // else create a mailequivalent with nic.in
    {
        var final_email_id = $("#final_email_id").val();
        $("#alias_id").val(final_email_id + "@nic.in");
    }
}
function cleartestdata()
{
    $("#choose_da_type").val(""); // line added by pr on 1stnov19    
    $("#choose_fwd_div_detailedData").hide();
    $("#simplyforward").show();
    // below lines added by pr on 20thapr18
    var select = $('#choose_da_type');
    $('<option>').val("m").text("Admin").appendTo(select);
}
function cleartestdatahide()
{
    $("#choose_da_type").val(""); // line added by pr on 1stnov19    
    $("#choose_da_type option[value='m']").remove(); // line added by pr on 20thapr18
    $("#choose_fwd_div_detailedData").show();
    $("#simplyforward").hide();
}

// below function added by pr on 5thapr19                              
function clearCountsOnLoad()
{
    $(".totalCount").html(0);
    $(".newCount").html(0);
    $(".counterup").html(0);
    $(".completeCount").html(0);
}

function openAllTooltip() {
    $("[data-toggle='popover']").popover('show');
}
function closeAllTooltip() {
    $("[data-toggle='popover']").popover('hide');
}