/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {


    //for loading page data first time show pending request
    pageloaddata();
    function pageloaddata() {

        $('#chart_load').show();
        $.ajax({
            type: "POST",
            url: "RequestCount",
            datatype: JSON,
            data: {showValue: "pending", dateRange: "", filter: "all"},
            success: function (data) {
                $('#chart_load').hide();
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var lol = jsonvalue['ja']
                var val = jsonvalue['val'];
                var my = JSON.stringify(val);
                var cal = JSON.parse(my);
                $('#rcd').html("pending request : ");
                $('.extra').show();
                $('#11').html(cal.support_pending + "<br><small>SUPPORT PENDING</small>");
                $('#12').html(cal.mailadmin_pending + "<br><small>MAILADMIN PENDING</small>");
                $('#13').html(cal.coordinator_pending + "<br><small>COORDINATOR PENDING</small>");
                $('#14').html(cal.da_pending + "<br><small>DA PENDING</small>");
                $('#15').html(cal.ro_pending + "<br><small>RO PENDING</small>");
                $('#16').html(cal.manual + "<br><small>USER PENDING</small>");
                ChartsAmcharts.init(lol);
            }
            ,
            error: function () {
                console.log('Error, please contact to support !!!');
            }
        });

    }

    $('.date-range-toggle').click(function (e) {
        $('#daterange').val('');
    });

    //for sorting data value by dhirendra joshi 20july2018
    AmCharts.addInitHandler(function (chart) {

        // Check if `orderByField` is set
        if (chart.orderByField === undefined) {
            // Nope - do nothing
            return;
        }

        // Re-order the data provider
        chart.dataProvider.sort(function (a, b) {
            if (a[ chart.orderByField ] > b[ chart.orderByField ]) {
                return -1;
            } else if (a[ chart.orderByField ] == b[ chart.orderByField ]) {
                return 0;
            }
            return 1;
        });

    }, ["serial"]);


    //for  filter change 
    $('.filterforsearch').click(function (e) {
        var t = $(this).attr('id');

        if (t === "checkall") {

            $('#all-req').html('<select class="bs-select form-control" id="select"><option value="select">--Select--</option><option value="total">Total Request</option><option value="pending">Pending Request</option><option value="rejected">Rejected Request</option><option value="completed">Completed Request</option></select><font style="color:red"><span id="select_error"></span></font>');

        } else if (t === "checkform") {
            $('.extra').hide();
            $('#all-req').html('<select class="bs-select form-control" id="select"><option value="select">--Select--</option><option value="single">Single Form Request</option><option value="bulk">Bulk Form Request</option><option value="nkn_single">NKN Single Request</option><option value="nkn_bulk">NKN Bulk Request</option><option value="gem">GEM Form Request</option><option value="mobile">Mobile Update Request</option><option value="imappop">Imap/POP Enable Request</option><option value="dlist">Distribution List Request</option><option value="ip">IP Change/Add Request</option><option value="ldap">Ldap Form Request</option><option value="relay">Relay Form Request</option><option value="sms">SMS Form Request</option><option value="wifi">Wifi Form Request</option><option value="dns">DNS Form Request</option></select><font style="color:red"><span id="select_error"></span></font>');

        }

    });


    // for  toltal/pending/rejected/completed request click
    $('.req_status').click(function (e) {
        var t = $(this).attr('id');
        $('#chart_load').show();
        $.ajax({
            type: "POST",
            url: "RequestCount",
            datatype: JSON,
            data: {showValue: t, filter: "all"},
            success: function (data) {
                $('#chart_load').hide();
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var lol = jsonvalue['ja'];
                var val = jsonvalue['val'];
                var my = JSON.stringify(val);
                var cal = JSON.parse(my);
                $('#1').html(cal.totalrequest);
                $('#2').html(cal.totalpending);
                $('#3').html(cal.totalRejected);
                $('#4').html(cal.totalComplete);

                if (t === "total") {
                    $('#rcd').html("total request :");
                    $('.extra').hide();

                } else if (t === "pending") {
                    $('#rcd').html("pending request :");
                    $('.extra').show();
                    $('#11').html(cal.support_pending + "<br><small>SUPPORT PENDING</small>");
                    $('#12').html(cal.mailadmin_pending + "<br><small>MAILADMIN PENDING</small>");
                    $('#13').html(cal.coordinator_pending + "<br><small>COORDINATOR PENDING</small>");
                    $('#14').html(cal.da_pending + "<br><small>DA PENDING</small>");
                    $('#15').html(cal.ro_pending + "<br><small>RO PENDING</small>");
                    $('#16').html(cal.manual + "<br><small>USER PENDING</small>");


                } else if (t === "rejected") {
                    $('#rcd').html("rejected request :");
                    $('.extra').show();
                    $('#11').html(cal.support_rejected + "<br><small>SUPPORT REJECTED</small>");
                    $('#12').html(cal.mailadmin_rejected + "<br><small>MAILADMIN REJECTED</small>");
                    $('#13').html(cal.coordinator_rejected + "<br><small>COORDINATOR REJECTED</small>");
                    $('#14').html(cal.da_rejected + "<br><small>DA REJECTED</small>");
                    $('#15').html(cal.ro_rejected + "<br><small>RO REJECTED</small>");
                    $('#16').html(cal.manual_rejected + "<br><small>USER REJECTED</small>");

                } else if (t === "completed") {
                    $('#rcd').html("completed request :");
                    $('.extra').hide();

                }
                ChartsAmcharts.init(lol);
            }
            ,
            error: function () {
                console.log('Error, please contact to support !!!');
            }
        });
    });



    //$('.filter').click(function (e) {
    $(document).on('click','.filter',function(e){    
        var d = $('#daterange').val();
        var t = $("#select option:selected").val();
        var r = $("input[name='filter']:checked").val();

        
        $(".req_status").off('click');

        var dateregn = /^[0-9a-zA-z,-\s]{25,50}$/;
        var flg = true;
        console.log("select value :" + t + " :: Date range :" + d + " :: Radio value :" + r);

        if (t === null || t === "" || r === "all") {
            $('#select_error').html("");
        }else if (t === null || t === "" || t === "select") {
            $('#select_error').html("Please select Filter");
            flg = false;
        } else if (t === "total" || t === "pending" || t === "rejected" || t === "completed" || t === "single" || t === "bulk" || t === "nkn_single" || t === "nkn_bulk" || t === "gem" || t === "mobile" || t === "imappop" || t === "dlist" || t === "ip" || t === "ldap" || t === "relay" || t === "sms" || t === "wifi" || t === "dns") {
            $('#select_error').html("");

        } else {
            $('#select_error').html("Select the correct filter value from dropdown list");
            flg = false;
        }

        if (d === null || d === "") {
            $('#date_error').html("Please select Date-Range");
            flg = false;
        } else if (!d.match(dateregn)) {
            $('#date_error').html("Please select the correct Date-Range");
            flg = false;

        } else {
            $('#date_error').html("");
        }

        if (flg) {
            $('#chart_load').show();
            $.ajax({
                type: "POST",
                url: "RequestCount",
                datatype: JSON,
                data: {showValue: t, dateRange: d, filter: r},
                success: function (data) {
                    $('#chart_load').hide();
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    var lol = jsonvalue['ja'];
                    var lolrej = jsonvalue['ja1'];
                    var val = jsonvalue['val'];
                    var my = JSON.stringify(val);
                    var cal = JSON.parse(my);
                    $('#1').html(cal.totalrequest);
                    $('#2').html(cal.totalpending);
                    $('#3').html(cal.totalRejected);
                    $('#4').html(cal.totalComplete);

                    if (t === "total") {
                        if (d === "") {
                            $('#rcd').html("total request :");
                        } else {
                            $('#rcd').html("total request between : " + d);
                        }
                        $('.extra').hide();


                    } else if (t === "pending") {
                        if (d === "") {
                            $('#rcd').html("pending request : ");
                        } else {
                            $('#rcd').html("pending request between : " + d);
                        }
                        $('.extra').show();
                        $('#11').html(cal.support_pending + "<br><small>SUPPORT PENDING</small>");
                        $('#12').html(cal.mailadmin_pending + "<br><small>MAILADMIN PENDING</small>");
                        $('#13').html(cal.coordinator_pending + "<br><small>COORDINATOR PENDING</small>");
                        $('#14').html(cal.da_pending + "<br><small>DA PENDING</small>");
                        $('#15').html(cal.ro_pending + "<br><small>RO PENDING</small>");
                        $('#16').html(cal.manual + "<br><small>USER PENDING</small>");

                    } else if (t === "rejected") {
                        if (d === "") {
                            $('#rcd').html("rejected request : ");
                        } else {
                            $('#rcd').html("rejected request between : " + d);
                        }
                        $('.extra').show();
                        $('#11').html(cal.support_rejected + "<br><small>SUPPORT REJECTED</small>");
                        $('#12').html(cal.mailadmin_rejected + "<br><small>MAILADMIN REJECTED</small>");
                        $('#13').html(cal.coordinator_rejected + "<br><small>COORDINATOR REJECTED</small>");
                        $('#14').html(cal.da_rejected + "<br><small>DA REJECTED</small>");
                        $('#15').html(cal.ro_rejected + "<br><small>RO REJECTED</small>");
                        $('#16').html(cal.manual_rejected + "<br><small>USER REJECTED</small>");

                    } else if (t === "completed") {
                        if (d === "") {
                            $('#rcd').html("completed request : ");
                        } else {
                            $('#rcd').html("completed request between : " + d);
                        }
                        $('.extra').hide();
                        $('#1').html(cal.totalrequest);
                        $('#2').html(cal.totalpending);
                        $('#3').html(cal.totalRejected);
                        $('#4').html(cal.totalComplete);

                    } else {
                        $('.extra').hide();
                        if (d === "") {
                            $('#rcd').html("Pending Request : " + t + " Form");

                        } else {
                            $('#rcd').html("Pending Request : " + t + " Form between :" + d);

                        }
                    }
                    if (r === "all") {
                        $('.req123').removeClass('col-md-6');
                        $('.req123').addClass('col-md-12');
                        $('#test').html('');
                        ChartsAmcharts.init(lol);
                    } else {
                        $('.req123').removeClass('col-md-12');
                        $('.req123').addClass('col-md-6');
                        $('#test').html('<div class="col-md-12 rejhide"><div class="portlet light "><div class="portlet-title"><div class="caption"><i class="icon-bar-chart font-green-haze"></i><span class="caption-subject bold uppercase font-green-haze" id="rcd123"></span><span class="caption-helper"></span></div><div class="tools"></div></div><div class="portlet-body"><div id="chart_6" class="chart" style="height: 400px;"> </div></div></div></div>');
                        ChartsAmcharts.init(lol);
                        ChartsAmcharts123.init(lolrej);
                        if (d === "") {
                            $('#rcd123').html("Rejected Request : " + t + " Form");
                        } else {
                            $('#rcd123').html("Rejected Request: " + t + " Form between :" + d);
                        }
                    }
                }
                ,
                error: function () {
                    console.log('Error, please contact to support !!!');
                }
            });
        }

    });

    var ChartsAmcharts = function () {
        var initChartSample5 = function (data) {
            var chart = AmCharts.makeChart("chart_5", {
                "theme": "light",
                "type": "serial",
                "startDuration": 2,
                "orderByField": "visits",
                "fontFamily": 'Open Sans',
                "color": 'black',
                "dataProvider": data,
                "valueAxes": [{
                        "position": "left",
                        "axisAlpha": 0,
                        "gridAlpha": 0,
                    }],
                "graphs": [{
                        "balloonText": "[[category]]: <b>[[value]]</b>",
                        "colorField": "color",
                        "fillAlphas": 0.85,
                        "lineAlpha": 0.1,
                        "type": "column",
                        "topRadius": 1,
                        "valueField": "visits",
                        "labelText": "[[value]]",
                        "labelPosition": "top",
                        "bullet": "round",
                        "bulletSize": 10,
                    }],
                "depth3D": 0,
                "angle": 0,
                "chartCursor": {
                    "categoryBalloonEnabled": true,
                    "cursorAlpha": 0,
                    "zoomable": false
                },
                "categoryField": "country",
                "categoryAxis": {
                    "gridPosition": "start",
                    "axisAlpha": 0,
                    "gridAlpha": 0,
                    "labelRotation": 30,
                    "bold": true,
                },
                "exportConfig": {
                    "menuTop": "20px",
                    "menuRight": "20px",
                    "menuItems": [{
                            "icon": '/lib/3/images/export.png',
                            "format": 'png'
                        }]
                }
            }, 0);
            jQuery('.chart_5_chart_input').off().on('input change', function () {
                var property = jQuery(this).data('property');
                var target = chart;
                chart.startDuration = 0;
                if (property == 'topRadius') {
                    target = chart.graphs[0];
                }

                target[property] = this.value;
                //chart.validateData();
                chart.validateNow();
            });
            $('#chart_5').closest('.portlet').find('.fullscreen').click(function () {
                chart.invalidateSize();
            });
        }
        return {
            //main function to initiate the module

            init: function (data) {

                initChartSample5(data);
            }

        };

    }();

    var ChartsAmcharts123 = function () {
        var initChartSample5 = function (data) {
            var chart = AmCharts.makeChart("chart_6", {
                "theme": "light",
                "type": "serial",
                "startDuration": 2,
                "orderByField": "visits",
                "fontFamily": 'Open Sans',
                "color": 'black',
                "dataProvider": data,
                "valueAxes": [{
                        "position": "left",
                        "axisAlpha": 0,
                        "gridAlpha": 0,
                    }],
                "graphs": [{
                        "balloonText": "[[category]]: <b>[[value]]</b>",
                        "colorField": "color",
                        "fillAlphas": 0.85,
                        "lineAlpha": 0.1,
                        "type": "column",
                        "topRadius": 1,
                        "valueField": "visits",
                        "labelText": "[[value]]",
                        "labelPosition": "top",
                        "bullet": "round",
                        "bulletSize": 10,
                    }],
                "depth3D": 0,
                "angle": 0,
                "chartCursor": {
                    "categoryBalloonEnabled": true,
                    "cursorAlpha": 0,
                    "zoomable": false
                },
                "categoryField": "country",
                "categoryAxis": {
                    "gridPosition": "start",
                    "axisAlpha": 0,
                    "gridAlpha": 0,
                    "labelRotation": 30,
                    "bold": true,
                },
                "exportConfig": {
                    "menuTop": "20px",
                    "menuRight": "20px",
                    "menuItems": [{
                            "icon": '/lib/3/images/export.png',
                            "format": 'png'
                        }]
                }
            }, 0);
            jQuery('.chart_6_chart_input').off().on('input change', function () {
                var property = jQuery(this).data('property');
                var target = chart;
                chart.startDuration = 0;
                if (property == 'topRadius') {
                    target = chart.graphs[0];
                }

                target[property] = this.value;
                //chart.validateData();
                chart.validateNow();
            });
            $('#chart_6').closest('.portlet').find('.fullscreen').click(function () {
                chart.invalidateSize();
            });
        }
        return {
            //main function to initiate the module

            init: function (data) {

                initChartSample5(data);
            }

        };

    }();
});
