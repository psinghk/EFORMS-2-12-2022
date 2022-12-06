<%@page import="com.org.bean.UserData"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Expires", "0");
    response.setDateHeader("Expires", -1);
    response.setHeader("X-Frame-Options", "DENY");
    response.addHeader("X-Content-Type-Options", "nosniff");
    response.addHeader("X-XSS-Protection", "1; mode=block");
%>
<%
    
    String random = entities.Random.csrf_random();
    session.setAttribute("rand", random);
    // below code added by pr on 10thjan18
    String CSRFRandom = entities.Random.csrf_random();
    session.setAttribute("CSRFRandom", CSRFRandom);
    UserData userdata = (UserData) session.getAttribute("uservalues");
    boolean nic_employee = userdata.isIsNICEmployee();
    boolean ldap_employee = userdata.isIsEmailValidated();
    String employment = userdata.getUserOfficialData().getEmployment();
    if (!session.getAttribute("update_without_oldmobile").equals("no")) {

        if (!userdata.isIsNewUser()) {
            response.sendRedirect("Mobile_registration");
        }
        response.sendRedirect("index.jsp");
    }
%>
<style type="text/css">
    td {
        border: 1px solid #eee !important;
    }
</style>
<jsp:include page="include/new_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<jsp:include page="include/new_include/header.jsp" />
<div class="k-content	k-grid__item k-grid__item--fluid k-grid k-grid--hor" id="k_content">

    <!-- begin:: Content Head -->
    <div class="k-content__head	k-grid__item">
        <div class="k-content__head-main">
            <h3 class="k-content__head-title">eForms</h3>
            <div class="k-content__head-breadcrumbs">
                <a href="#" class="k-content__head-breadcrumb-home"><i class="flaticon2-shelter"></i></a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link">Dashboards</a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link">Short Messaging Services</a>

                <!-- <span class="k-content__head-breadcrumb-link k-content__head-breadcrumb-link--active">Active link</span> -->
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile-firewal">
            <div class="portlet light" style="display:block;">
                <div class="portlet-title">
                    <div class="k-portlet__head">
                        <div class="k-portlet__head-label">
                            <h3 class="k-portlet__head-title">Form Details - Step 1 of 2</h3>
                        </div>
                    </div>
                </div>
                <div class="portlet-body form">

                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="tab-content">

                                <div class="tab-pane active" id="tab2" >

                                    <table id="example1" class="table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer display" width="100%" cellspacing="0">
                                        <thead>
                                            <tr>
                                                <!--                                                                        <th>S.No. </th>-->
                                                <th>Source IP / Range</th>
                                                <th>Destination IP /Range</th>
                                                <th>Service</th>
                                                <th>Ports</th>
                                                <th>Action(Permit/Deny)</th>
                                                <th>Time Period</th>
                                                <th>Add/Remove Rows</th>
                                            </tr>
                                        </thead>
                                    </table>

                                    <form action="" method="post" id="firewall_form1" class="form_val">

                                        <table id="newRow" style="display:none;">
                                            <tbody>
                                                <tr>
                                                    <td contenteditable="true" placeholder="[164.100.X.X]" class="attrName sip borderheading pHolder"></td>
                                                    <td contenteditable="true" placeholder="[164.100.X.X]" class="attrName dip borderheading pHolder"></td>
                                                    <td contenteditable="false" placeholder="[TCP/UDP/ICMP]" class="attrName borderheading pHolder"> <select class='form-control ser' id="" name="">
                                                            <option value=''>-SELECT-</option>
                                                            <option value='tcp'>TCP</option>
                                                            <option value='udp'>UDP</option>
                                                            <option value='icmp'>ICMP</option>
                                                        </select></td>
                                                    <td contenteditable="true" placeholder="[8080]" class="attrName port borderheading pHolder"></td>
                                                    <td contenteditable="false" placeholder="[Permit/Deny]" class="attrName borderheading pHolder"><select class='form-control act' id="" name="">
                                                            <option value=''>-SELECT-</option>
                                                            <option value='permit'>Permit</option>
                                                            <option value='deny'>Deny</option>
                                                        </select></td>
                                                    <td contenteditable="true" class="attrName time borderheading pHolder" placeholder="Default"></td>
                                                    <td><button type="button" class="btn btn-block btn-info plusRow"><i class="fa fa-plus-square" aria-hidden="true"></i> Add</button></td>
                                                </tr>
                                            <font color="red"><span id="add_row_msg"></span></font>

                                            </tbody>
                                        </table>

                                        <table id="newRow1" style="display:none;">
                                            <tbody>
                                                <tr>

                                                    <td contenteditable="true" placeholder="[164.100.X.X]" class="attrName sip borderheading pHolder"></td>
                                                    <td contenteditable="true" placeholder="[164.100.X.X]" class="attrName dip borderheading pHolder"></td>
                                                    <td contenteditable="false" placeholder="[TCP/UDP/ICMP]" class="attrName borderheading pHolder"> <select class='form-control ser' id="" name="">
                                                            <option value=''>-SELECT-</option>
                                                            <option value='tcp'>TCP</option>
                                                            <option value='udp'>UDP</option>
                                                            <option value='icmp'>ICMP</option>
                                                        </select></td>
                                                    <td contenteditable="true" placeholder="[8080]" class="attrName port borderheading pHolder"></td>
                                                    <td contenteditable="false" placeholder="[Permit/Deny]" class="attrName borderheading pHolder"><select class='form-control act' id="" name="">
                                                            <option value=''>-SELECT-</option>
                                                            <option value='permit'>Permit</option>
                                                            <option value='deny'>Deny</option>
                                                        </select></td>
                                                    <td contenteditable="true" class="attrName time borderheading pHolder" placeholder="Default"></td>
                                                    <td> 
                                                        <button type="button" class="btn btn-block btn-danger minusRow"><i class="fa fa-minus-square" aria-hidden="true"></i> Remove</button>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                         <div class="form-group row">
                                                    <div class="col-md-12">
                                                        <label for="street">Purpose</label>
                                                        <input class="form-control" value="" placeholder="Purpose" type="text" name="remarks" id="remarks" maxlength="100"  aria-required="true">
                                                        <font style="color:red"><span id="purpose_error"></span></font>
                                                    </div>                                                                                
                                                </div>
                                        <div class="mt-checkbox-list inTheEnd mt-5 mb-5">
                                            <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                <input type="checkbox" name="tnc1" id="tnc1" required> <b style="font-weight: 500;">I Declare that i have verified ip segment details for that particular location.</b>
                                                <span></span>
                                            </label>
                                            <font style="color:red"><span id="tnc1_error"></span></font>
                                        </div>
                                        <font color="red"><span id="add_row_msg"></span></font>
                                        <div class="row" id="submit">
                                            <div class="col-md-12 text-center">
                                                <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                                <button name="submit"  value="preview" class="btn btn-primary purple btn-outline sbold" > Preview and Submit </button>                                                                
                                            </div>
                                        </div>
                                    </form>
                                </div>

                            </div>
                        </div>

                    </div>
                </div>

            </div>
        </div>
    </div>
    <!-- END PAGE CONTENT INNER -->
</div>
<jsp:include page="include/new_include/footer.jsp" />


<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="firewall_form2" method="post">
        <jsp:include page="include/firewall_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<!--Nested Modal -->
<div class="modal fade" id="stack2" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">Terms and conditions</h4>
            </div>
            <div class="modal-body">                           
                <b>NOTE: Please read all instructions carefully.</b>
                <br/>
                <ol>
                    <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>
                    <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                    <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                    <li>NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.</li>
                    <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>
                    <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.
                        Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as follows:
                        Trash - 7 days
                        ProbablySpam – 7 days</li>
                    <li>NIC account will be deactivated, if not used for 90 days.</li>
                    <li>Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.</li>
                    <li>Contact our 24x7 support if you have any problems. Phone 1800-111-555 or you can send mail to servicedesk@nic.in</li>
                    <li>Please note that advance payment is a must for paid users.</li>
                    <li>NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.</li>
                    <li>NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.</li>
                    <li>NIC does not capture any aadhaar related information.</li>
                </ol>
                <div class="modal-footer">
                    <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Nested Modal-->
<!-- Modal for last submission -->
<div class="modal fade" id="stack3" tabindex="-1">
    <form id="firewall_form_confirm">

        <jsp:include page="include/Cod_details_firewall.jsp" />

    </form>
</div>
<!-- end -->


<!--[if lt IE 9]>
<script src="assets/plugins/respond.min.js"></script>
<script src="assets/plugins/excanvas.min.js"></script>
<script src="assets/plugins/ie8.fix.min.js"></script>
<![endif]-->

<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/firewall.js" type="text/javascript"></script>
<script src="assets/old_assets/plugins/jquery.serializeObject.js"></script>
<!--        <script src="js/test.js" type="text/javascript"></script>-->

<script>
    $(document).ready(function () {


        if ('<%=nic_employee%>' !== null) {
            if ('<%=nic_employee%>' === 'true') {
                var hod_email = '<s:property value="#session['uservalues'].hodData.email" />';
                if (hod_email === null || hod_email === "") {
                } else {
                    $("input[name='hod_email']").prop('readonly', true);
                }
            }
        }
        $("#refresh").on("click", function () {
            $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#closebtn").on("click", function () {

            $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#imgtxt').val("");
        });

        console.log("load")
        var flag = false;
        var i = 0;
        var table;

        $("#example1").on("mousedown", "td .minusRow", function (e) {
            table.row($(this).closest("tr")).remove().draw();
            if (i == 4) {
                $('#add_row_msg').html("");

            }
            //table = "";
            i--
            flag = true;

        });
        table = $('#example1').DataTable();
        $('#example1').css('border-bottom', 'none');
        $('<div class="adderrorRow d-none"><font style="color:red;font-size:11px"><span id="source_ip_error"></span></font></br>'
                + '<font style="color:red;font-size:11px"><span id="destination_ip_error"></span></font></br>'
                + '<font style="color:red;font-size:11px"><span id="service_error"></span></font></br>'
                + '<font style="color:red;font-size:11px"><span id="port_error"></span></font></br>'
                + '<font style="color:red;font-size:11px"><span id="action_error"></span></font></br>'
                + '<font style="color:red;font-size:11px"><span id="time_error"></span></font></div>').insertAfter('.inTheEnd');

        if ($("#newRow").find("tr")[0] != null)
            var rowHtml = $("#newRow").find("tr")[0].outerHTML
        table.row.add($(rowHtml)).draw();
        var table1 = $("#example1 tbody");
        table1.find('tr').each(function (i) {
            var $tds1 = $(this).find('td');
            let
            row1 = $tds1.eq(0);
            let
            row5 = $tds1.eq(5);
            //row1.focus();

        });
        $("#example1").on("mousedown", "td .plusRow", function (e) {
            var table1 = $("#newRow1 tbody");
            table1.find('tr').each(function (i) {
                var $tds1 = $(this).find('td');
                let
                row1 = $tds1.eq(0);
                let
                row5 = $tds1.eq(5);
                row1.focus();
                row5.attr('placeholder', "Default");
            });
            if (i <= 3)
            {
                i++;
                var rowHtml = $("#newRow1").find("tr")[0].outerHTML

                table.row.add($(rowHtml)).draw();


            } else {

                $('#add_row_msg').html("Max rows 5 only")
            }
            $('#submit').removeClass("display-hide");

        })
        $('.addRow').click(function () {
            $('#add_row').addClass("display-hide")
            if (i <= 4)
            {
                i++;
                // t.row.add( [1,2,3] ).draw();
                var rowHtml = $("#newRow").find("tr")[0].outerHTML
                console.log(rowHtml);
                table.row.add($(rowHtml)).draw();
                var table1 = $("#example1 tbody");
                table1.find('tr').each(function (i) {
                    var $tds1 = $(this).find('td');
                    let
                    row1 = $tds1.eq(0);
                    let
                    row5 = $tds1.eq(5);
                    row1.focus();

                });


            } else {

                $('#add_row_msg').html("Max rows 5 only")
            }
            $('#submit').removeClass("display-hide");

        })
        if ('<%=session.getAttribute("nic_employee")%>' !== null) {
            var nic_employee = '<s:property value="#session['user_bo']" />';
            if (nic_employee === 'NIC Employees') {
                var hod_email = '<s:property value="#session['profile-values'].hod_email" />';

                if (hod_email === null || hod_email === "") {

                } else {
                    $("input[name='hod_email']").prop('readonly', true);
                }
            }
        }
    });

</script>