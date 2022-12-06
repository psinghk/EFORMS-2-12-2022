//let refreshOnFlag = function () {
//    $.ajax({
//        type: "POST",
//        url: "checkRefreshFlag",
//        success: function (data) {
//            if (data.refreshFlag != undefined && data.refreshFlag) {
//                refreshNow();
//            }
//        },
//        error: function () {
//            console.log('error');
//        }
//    });
//}; 
//let refreshNow = function () {
//    //window.location.reload(true);
//    $.ajax({
//        type: "POST",
//        url: "fetchSwitchUrl",
//        success: function (data) {
//            if (data.switchUrl != undefined && data.switchUrl !== null && data.switchUrl !== '') {
//                window.location = data.switchUrl;
//            }
//        },
//        error: function () {
//            console.log('error');
//        }
//    });
//};
//
//setInterval(refreshOnFlag, 10000);