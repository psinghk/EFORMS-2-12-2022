<%@page import="com.org.bean.UserData"%>
<div class="row pt-3" style="margin: 0;background: #fff;box-shadow: 0px -2px 7px 4px #eee;">

    <div class="col-12">
        <ul class="footer-ul mt-2 mr-5">
            <li><a href="https://india.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/india_gov.png"></a></li>
            <li><a href="https://www.pmindia.gov.in/en/" target="_blank" title="External Link"><img src="assets/img/footer/new/pm_india.png"></a></li>
            <li><a href="https://email.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/gov.png"></a></li>
            <!--            <li><a href="http://nkn.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/nkn.png"></a></li>-->
            <!--            <li><a href="http://meity.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/meity.png"></a></li>
            -->            <li><a href="http://www.digitalindia.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/digital_india.png"></a></li>
            <li><a href="https://mygov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/mygov.png"></a></li>
            <li><a href="https://egreetings.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/egreeting.png"></a></li>
            <li><a href="https://sampark.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/esampark.png"></a></li>
        </ul>
    </div>
    <div class="col-12 text-center bottom-footer">
        <div class="p-2">
            <a href="https://eforms.nic.in/docs/policy.html" target="_blank" title="External Link">POLICIES UNDER THE E-MAIL SERVICE OF THE GOVERNMENT</a><br />
            Designed and Developed by Messaging Division NIC <span id="year">2019</span><b>&nbsp;&copy;&nbsp;</b><a href="https://eforms.nic.in/" target="_blank" class="k-link">eForms</a>
        </div>
    </div>
    <span class="footer-msg-btn open-button btn-warning" onclick="openForm()"><i class="fa fa-info-circle"></i></span>
    <div class="footer-msg">
        <div class="chat-popup" id="myForm">
            <div class="form-container">
                <div class="col-md-12 mb-3"> <a href="javascript:;" onclick="closeForm()" class="btn btn-icon-only red closeN float-right"><i class="fa fa-times mt-2 pt-2"></i></a></div>
                <% if(session.getAttribute("employment_data")!=null)
                {%>
                <h3 class="mb-3">Information</h3>  
                <% System.out.println("AAA: "+session.getAttribute("employment_data").toString()); %>
                <p>For any query, you may contact <%=session.getAttribute("employment_data")%> </p>
                <% } %>
            </div>
        </div>
    </div>
</div>
<!--<div class="k-header__topbar-item k-header__topbar-item--quick-panel filter-btn" data-toggle="k-tooltip" title="" data-placement="right" data-original-title="Filter Panel">
    <span class="k-header__topbar-icon" id="k_quick_panel_toggler_btn">
        <i class="fa fa-filter fa-2x"></i>
    </span>
</div>-->
<!-- end:: Page -->
</div>
</div>
</div>
</body>
</html>
<!-- begin::Scrolltop -->
<div id="k_scrolltop" class="k-scrolltop">
    <i class="fa fa-arrow-up"></i>
</div>

<!-- Modal -->
<!-- The Modal -->
<div class="modal" id="knowUrCo">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">Related Coordinator's</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
          <table class="table table-bordered table-striped table-sm" id="knowUrCoTbl">
              <thead>
                  <tr>
                      <td>S.No.</td>
                      <td>Department</td>
                      <td>Name</td>
                      <td>Email</td>
                  </tr>
              </thead>
              <tbody id="knowUrCoBody"></tbody>
          </table>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>


<!-- end::Global Config -->

<!--begin:: Global Mandatory Vendors -->
<script src="assets/vendors/general/jquery/dist/jquery.js?v=1.0" type="text/javascript"></script>
<script src="assets/old_assets/plugins/jquery-ui.js?v=1.0"></script>
<script src="assets/vendors/general/popper.js/dist/umd/popper.js?v=1.0" type="text/javascript"></script>
<script src="assets/vendors/general/bootstrap/dist/js/bootstrap.min.js?v=1.0" type="text/javascript"></script>
<script src="assets/vendors/general/perfect-scrollbar/dist/perfect-scrollbar.js?v=1.0" type="text/javascript"></script>
<!--end:: Global Mandatory Vendors -->
<!--begin::Global Theme Bundle -->
<script src="assets/demo/default/base/scripts.bundle.js?v=1.0" type="text/javascript"></script>
<!--end::Page Vendors -->

<!--begin::Global App Bundle -->
<script src="assets/demo/default/base/jquery.dataTables.min.js?v=1.0" type="text/javascript"></script>
<script src="assets/demo/default/base/dataTables.bootstrap4.min.js?v=1.0" type="text/javascript"></script>
<script src="assets/old_assets/plugins/jquery.serializeObject.js?v=1.0"></script>
<script src="assets/old_assets/plugins/bootbox.min.js?v=1.0"></script>
<script src="main_js/register.js?v=1.0" type="text/javascript"></script>
<script src="assets/custom/custom.js?v=1.0" type="text/javascript"></script>
<script src="assets/demo/bootstrap-datetime-picker/js/bootstrap-datetimepicker.min.js?v=1.0" type="text/javascript"></script>
<script type="text/javascript" src="assets/custom/datatable/js/dataTables.responsive.min.js?v=1.0"></script>
<script type="text/javascript" src="assets/custom/datatable/js/responsive.bootstrap.min.js?v=1.0"></script>
<%
    UserData userdata = (UserData) session.getAttribute("uservalues");
%>
<script type="text/javascript">
                    $(document).ready(function () {
                        $('#k_table_1').DataTable();
                    });
                    $(document).ready(function () {
                        $('[data-toggle="tooltip"]').tooltip();
                    });

                    $('ul.k-menu__nav li.k-menu__item a.k-menu__link').each(function () {
                        var $this = $(this);
                        if (this.href == window.location.href) {
                            $this.closest('li.k-menu__item--submenu').addClass('k-menu__item--open');
                            $this.closest('li.k-menu__item').addClass('k-menu__item--active');
                        }
                    });
</script>
<script>
//    $(".formclass").click(function () {
//        if ($(this).attr('value') === 'imap') {
//            setTimeout(function () {
//                window.location.href = "ImapPop_registration";
//            }, 500);
//        }
//    });

    // modifed by MI on 18thapr18
</script>
<% if (userdata.isIsEmailValidated()) {

        if (userdata.isIsNewUser()) {%>
<script>
    $('.upmobile').click(function () {
        bootbox.dialog({
            message: "<p style='font-size: 15px;'> You need to update your profile first.</p>",
            buttons: {
                cancel: {
                    label: "OK",
                    className: 'btn-info'
                }
            }
        });
    });</script>

<%  } else if (session.getAttribute("update_without_oldmobile").equals("yes")) {%>
<script>
    $('.upmobile').click(function () {
        bootbox.dialog({
            message: "<p style='font-size: 15px;'>Since, You have requested for updating mobile number and your mobile number has not been updated yet in our Central Repositary. You can not access any of the services unless your mobile number gets updated. If you want to track the status, please go to My Request and coordinate with the Approving Authority for early resolution.</p>",
            buttons: {
                cancel: {
                    label: "OK",
                    className: 'btn-info'
                }
            }
        });
    });

    $('.nongovuser').click(function () {
        bootbox.dialog({
            message: "<p style='font-size: 15px;'>Since, You have requested for updating mobile number and your mobile number has not been updated yet in our Central Repositary. You can not access any of the services unless your mobile number gets updated. If you want to track the status, please go to My Request and coordinate with the Approving Authority for early resolution.</p>",
            buttons: {
                cancel: {
                    label: "OK",
                    className: 'btn-info'
                }
            }
        });
    });
</script>
<% }
    }%>
<script>

    $(document).ready(function () {
       // console.clear();
        if (<%=!userdata.isIsEmailValidated()%>)
    <%
        System.out.println("userdata.isIsNewUser()"+userdata.isIsNewUser());
        if (userdata.isIsNewUser()) {%>
        $('.email_invalid').click(function () {
            bootbox.dialog({
                message: "<p style='font-size: 16px;font-weight:bold;'> You may register only for the following services :-</p><ul><li> Email Service</li><li> Sms Service</li> <li>VPN Service</li> <li>Security Audit Service</li> <li>e-Sampark Service</li><li>Cloud Service</li><li>Domain Registration Service</li><li>Firewall Service</li><li>Reservation for video conferencing Service</li> <li>Web Application Firewall services</li></ul>"
                        + "<p style='font-size: 14px;'>To register for other services, please log in with your government email service(NIC) email address.</p>",
                title: "",
                buttons: {
                    cancel: {
                        label: "OK",
                        className: 'btn-info'
                    }
                }
            });
        });
        $('.nongovuser').click(function () {
            bootbox.dialog({
                message: "<p style='font-size: 15px;'> You need to update your profile first.</p>",
                buttons: {
                    cancel: {
                        label: "OK",
                        className: 'btn-info'
                    }
                }
            });
        });

    <% } else {%>
        $('.email_invalid').click(function () {
            bootbox.dialog({
                message: "<p style='font-size: 16px;font-weight:bold;'> You may register only for the following services :-</p><ul><li> Email Service</li> <li>VPN Service</li><li> Sms Service</li> <li>Security Audit Service</li> <li>e-Sampark Service</li><li>Cloud Service</li><li>Domain Registration Service</li><li>Firewall Service</li><li>Reservation for video conferencing Service</li> <li>Web Application Firewall services</li></ul>"
                        + "<p style='font-size: 14px;'>To register for other services, please log in with your government email service(NIC) email address.</p>",
                title: "",
                buttons: {
                    cancel: {
                        label: "OK",
                        className: 'btn-info'
                    }
                }
            });
        });

    <%}%>

        document.getElementById("year").innerHTML = new Date().getFullYear();
    })




</script>
<script>
    var KAppOptions = {
        "colors": {
            "state": {
                "brand": "#5d78ff",
                "metal": "#c4c5d6",
                "light": "#ffffff",
                "accent": "#00c5dc",
                "primary": "#5867dd",
                "success": "#34bfa3",
                "info": "#36a3f7",
                "warning": "#ffb822",
                "danger": "#fd3995",
                "focus": "#9816f4"
            },
            "base": {
                "label": ["#c5cbe3", "#a1a8c3", "#3d4465", "#3e4466"],
                "shape": ["#f0f3ff", "#d9dffa", "#afb4d4", "#646c9a"]
            }
        }
    };


    $('input[type="file"]').ready(function(e){ 
        $('.custom-file-input').on('change', function() {
            var fileName = $(this).val();
            var trim_fileName = fileName.replace(/^.*\\/, "");
            console.log(trim_fileName);
            $(this).next('.custom-file-label').addClass("selected").html(trim_fileName);
        });
    });

</script>
<script src="main_js/refresh.js" type="text/javascript"></script>
<!--end::Global App Bundle -->
<!-- Modal -->