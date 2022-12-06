$("a.filter-a").click(function () {
    $("#filter-div-checked .filter-span").remove();
    $(".k-checkbox.k-checkbox--bold.k-checkbox--brand input:checkbox").prop('checked', false);
})
$(".search-filter  .k-checkbox input:checkbox").click(function (event) {
    event.stopImmediatePropagation();
    var class_txt;
    if($(this).prop('checked')){
        
        console.log("if part of fileter")
        var txt = $(this).closest('.k-checkbox').text().trim();
        class_txt = txt.replace(/"/g, "").replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '-').toLowerCase();
        $(this).addClass(class_txt + '-checked');
        $("#filter-div-checked").append("<span class='filter-span " + class_txt + "'>" + txt + "<span class='close-filter'>X</span></span>");
        
        $("#example").DataTable().clear().destroy();
        initDatatable('filter');
    } else {
        console.log("else part of fileter")
        var txt = $(this).closest('.k-checkbox').text().trim();
        class_txt = txt.replace(/"/g, "").replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '-').toLowerCase();
        $('.' + class_txt).remove();
        $(this).removeClass(class_txt + '-checked');
        setAction("filter");
    }
    
    $('span.close-filter').click(function (event) {
        event.stopImmediatePropagation();
        $(this).closest('.filter-span').remove();
        $('.'+class_txt + '-checked').prop('checked', false);
        $("#example").DataTable().clear().destroy();
        initDatatable('filter');
    });
});
$(".mainpage .k-checkbox input:checkbox").click(function () {
    console.log("Data Checked:::::::::::::");
    var class_txt;
    if ($(this).prop('checked')) {
        var txt = $(this).closest('.k-checkbox').text().trim();
        class_txt = txt.replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '-').toLowerCase();
        $(this).addClass(class_txt + '-checked');
        $("#filter-div-checked").append("<span class='filter-span " + class_txt + "'>" + txt + "<span class='close-filter'>X</span></span>");
        setAction("filter");
    } else {
        var txt = $(this).closest('.k-checkbox').text().trim();
        class_txt = txt.replace(/"/g, "").replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '-').toLowerCase();
        $('.' + class_txt).remove();
        $(this).removeClass(class_txt + '-checked');
        setAction("filter");
    }
    $('span.close-filter').click(function (event) {
        event.stopImmediatePropagation();
        $(this).closest('.filter-span').remove();
        console.log("class_txt"+class_txt);
        $('.'+class_txt + '-checked').prop('checked', false);
        setAction("filter");
    });
});

$(window).resize(function() {
    if ($(window).width() < 1400) {
        $('body').addClass('k-aside--minimize');
    }
    else {
       $('body').removeClass('k-aside--minimize');
    }
});
$(document).ready(function(){
    if ($(window).width() < 1400) {
        $('body').addClass('k-aside--minimize');
    }
    else {
        $('body').removeClass('k-aside--minimize');
    }
})
$('.queryfilter').click(function() {
    if($(this).is(':checked')) {
        $('.apply_filter').removeClass('d-none')
    } else{
        $('.apply_filter').addClass('d-none')
    }
});
$("#querydate_range").focusout(function(){
    var val = $(this).val();
    if(val) {
        $('.apply_filter').removeClass('d-none')
    } else{
        $('.apply_filter').addClass('d-none')
    }
});

function closeForm() {
    $(".footer-msg").css("right", "-275px");
    $("span.footer-msg-btn.open-button.btn-warning").css("right", "0px");
}
function openForm() {
    $(".footer-msg").css("right", "0px");
    $("span.footer-msg-btn.open-button.btn-warning").css("right", "-50px");
}

$('#filter-btn').click(function() {
    $("#k_quick_panel").css("right", "0px");
});

$("#close-filter-i").click(function(){
    $("#k_quick_panel").css("right", "-500px");
});

var refreshes = parseInt(sessionStorage.getItem('refreshes'), 10) || 0;
sessionStorage.setItem('refreshes', ++refreshes);
if (refreshes > 2) {
    closeForm();
}
$("#log_signout").click(function() {
    sessionStorage.setItem('refreshes', 0);
})
function fetchUrCoord() {
    $.ajax({
        type: 'post',
        url: 'fetchUrCoord',
        success: function(data) {
            console.log()
            var re_datatable_data = "";
            var cnt =  0;
            $('#knowUrCoTbl').DataTable().destroy();
            $('#knowUrCoBody').empty();
            $.each(data.val.coordinator_list, function (index, value) {
                cnt =  cnt + 1;
                re_datatable_data += '<tr><td>' + cnt + '</td><td>' + value[3] + '</td><td>' + (value[5] !== null && value[5] !== 'null' ? value[5] : '') + '</td><td>' + value[6] + '</td></tr>';
            }); 
            $("#knowUrCoBody").append(re_datatable_data);
            $('#knowUrCoTbl').DataTable()
            $("#knowUrCo").modal('show');
        }
    })
}