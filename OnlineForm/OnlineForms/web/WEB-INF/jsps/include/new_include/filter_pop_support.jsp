<%@page import="com.org.utility.Constants"%>
<%@ taglib  uri="/struts-tags" prefix="s"%>
<!-- begin::Quick Panel -->
<div id="k_quick_panel" class="k-quick-panel">
    <div class="col-12 mainpage">
        <div id="close-filter" class="col-md-12 float-right mt-3 mb-3 pr-0"><i class="fa fa-window-close fa-2x float-right" id="close-filter-i"></i></div>
        <div class="filter row mt-3 mb-3">
            <div class="col-6 pr-1 mt-2 mb-2">
                <a class="filter-a btn" href="javascript:void(0);"  onclick="setAction('total');" style="background: #303F9F;width: 100%;">
                    <div class="row">
                        <div class="col-9">
                            <h6>Total Requests</h6>
                        </div>
                        <div class="col-3">
                            <h5><span class="k-widget-3__content-number totalCount" id="totalCount" />">0</span></h5>
                        </div>
                    </div>
                </a>
            </div>

            <div class="col-6 pl-1 mt-2 mb-2">
                <a class="filter-a btn" href="javascript:void(0);"  onclick="setAction('new');" style="background: #ffb822;width: 100%;">
                    <div class="row">
                        <div class="col-9">
                            <h6>Today Pending</h6>
                        </div>
                        <div class="col-3">
                            <h5><span class="k-widget-3__content-number newCount" id="newCount">0</span></h5>
                        </div>
                    </div>
                </a>
            </div>

            <div class="col-6 pr-1 mt-2 mb-2">
                <a class="filter-a btn" href="javascript:void(0);"  onclick="setAction('pending');" style="background: #d32f2f;width: 100%;">
                    <div class="row">
                        <div class="col-9">
                            <h6>Total Pending</h6>
                        </div>
                        <div class="col-3">
                            <h5><span class="k-widget-3__content-number counterup" id="counterup" />">0</span></h5>
                        </div>
                    </div>
                </a>
            </div>

            <div class="col-6 col-6 pl-1 mt-2 mb-2">
                <a class="filter-a btn" href="javascript:void(0);"  onclick="setAction('completed');" style="background: #00796B;width: 100%;">
                    <div class="row">
                        <div class="col-9">
                            <h6>Total Completed</h6>
                        </div>
                        <div class="col-3">
                            <h5><span class="k-widget-3__content-number completeCount" id="completeCount">0</span></h5>
                        </div>
                    </div>
                </a>
            </div>
        </div>
        <!--begin::Portlet-->
        <div class="k-portlet k-portlet--height-fluid stack-eform">
            <div class="k-portlet__head">
                <div class="k-portlet__head-label">
                    <h3 class="k-portlet__head-title">Filters</h3>
                </div>

            </div>
            <div class="k-portlet__body">
                <div class="k-widget-18">
                    <div class="search-filter ">

                        <div class="panel-group">
                            <div class="panel panel-default" style="border:none;">

                                <h3 class="theme-heading-h3">Application</h3>    
                                <div class="error_cnts"></div><!-- line added by pr on 26thmar19 -->

                                <div id="collapse_3_1" class="panel-collapse ">
                                    <div class="panel-body">
                                        <div class="form-group">
                                            <div style="font-weight: bold;" class="mt-radio-list" id="filter_div"> <!--  div id added by pr on 21stmay18  -->
                                                <!-- title added and form name changed by pr on 17thjan19 -->
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.SINGLE_FORM_KEYWORD%>_form" id="<%=Constants.SINGLE_FORM_KEYWORD%>_form" style="display:none;" title="Check this checkbox, if you want to see Single email ID creation requests"> Single Email Creation Request
                                                    <input type="checkbox" value="<%=Constants.SINGLE_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.BULK_FORM_KEYWORD%>_form" id="<%=Constants.BULK_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Bulk email ID creation requests"> Bulk Email Creation Request
                                                    <input type="checkbox" value="<%=Constants.BULK_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.SMS_FORM_KEYWORD%>_form" id="<%=Constants.SMS_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see SMS Services Requests"> SMS Services
                                                    <input type="checkbox" value="<%=Constants.SMS_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.IP_FORM_KEYWORD%>_form" id="<%=Constants.IP_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see IP Change Requests requests"> IP Change Requests
                                                    <input type="checkbox" value="<%=Constants.IP_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.DIST_FORM_KEYWORD%>_form" id="<%=Constants.DIST_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Distribution List requests"> Distribution List Services
                                                    <input type="checkbox" value="<%=Constants.DIST_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.GEM_FORM_KEYWORD%>_form" id="<%=Constants.GEM_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see GEM email ID creation  requests"> GEM Email Creation Request
                                                    <input type="checkbox" value="<%=Constants.GEM_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.MOB_FORM_KEYWORD%>_form" id="<%=Constants.MOB_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Update Mobile requests"> Update Your Mobile Number
                                                    <input type="checkbox" value="<%=Constants.MOB_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.IMAP_FORM_KEYWORD%>_form" id="<%=Constants.IMAP_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see IMAP/POP requests"> Enable or Disable IMAP/POP
                                                    <input type="checkbox" value="<%=Constants.IMAP_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.LDAP_FORM_KEYWORD%>_form" id="<%=Constants.LDAP_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see LDAP requests"> Authentication Services (LDAP)
                                                    <input type="checkbox" value="<%=Constants.LDAP_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.NKN_SINGLE_FORM_KEYWORD%>_form" id="<%=Constants.NKN_SINGLE_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see NKN email ID creation requests"> NKN Email Creation Request
                                                    <input type="checkbox"  value="<%=Constants.NKN_SINGLE_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.RELAY_FORM_KEYWORD%>_form" id="<%=Constants.RELAY_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Relay requests"> SMTP Gateway Services (Relay)
                                                    <input type="checkbox"  value="<%=Constants.RELAY_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.DNS_FORM_KEYWORD%>_form" id="<%=Constants.DNS_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see DNS requests"> DNS Services Registration
                                                    <input type="checkbox" value="<%=Constants.DNS_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label> 
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.WIFI_FORM_KEYWORD%>_form" id="<%=Constants.WIFI_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see WIFI requests"> Wi-Fi Services Registration
                                                    <input type="checkbox" value="<%=Constants.WIFI_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.VPN_SINGLE_FORM_KEYWORD%>_form" id="<%=Constants.VPN_SINGLE_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see WIFI requests"> VPN Single Registration
                                                    <input type="checkbox" value="<%=Constants.VPN_SINGLE_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.VPN_ADD_FORM_KEYWORD%>_form" id="<%=Constants.VPN_ADD_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see WIFI requests"> VPN Add Registration (VPN id)
                                                    <input type="checkbox" value="<%=Constants.VPN_ADD_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.VPN_RENEW_FORM_KEYWORD%>_form" id="<%=Constants.VPN_RENEW_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see WIFI requests"> VPN Renew Registration (VPN id)
                                                    <input type="checkbox" value="<%=Constants.VPN_RENEW_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="panel-group " >
                            <div class="panel panel-default" style="border:none;">
                                <h3 class="theme-heading-h3">Status</h3>
                                <div id="collapse_3_2" class="panel-collapse ">
                                    <div class="panel-body">
                                        <div class="form-group">
                                            <!-- title added by pr on 17thjan19 -->
                                            <div class="mt-radio-list">
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand" title="Check this checkbox, when you want to see only those requests which are Forwarded by you."> Forwarded Request
                                                    <input type="radio" value="forwarded" class="stat" name="stat">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand" title="Check this checkbox, when you want to see only those requests which are Pending with you."> Pending Request
                                                    <input type="radio" value="pending" class="stat" name="stat">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand" title="Check this checkbox, when you want to see only those requests which are Rejected by you."> Rejected Request
                                                    <input type="radio" value="rejected" class="stat" name="stat">
                                                    <span></span>
                                                </label>
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand" title="Check this checkbox, when you want to see only those requests which are Completed by you."> Completed Request
                                                    <input type="radio" value="completed" class="stat" name="stat">
                                                    <span></span>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- start, code added by pr on 30thjul19  --> 

                        <div class="search-filter-divider bg-grey-steel"></div>

                        <div class="panel-group " >
                            <div class="panel panel-default" style="border:none;">
                                <h3 class="theme-heading-h3">Query Raise</h3>
                                <div id="collapse_3_2" class="panel-collapse ">
                                    <div class="panel-body">
                                        <div class="form-group">

                                            <div class="mt-radio-list">
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input type="radio" value="query" class="queryfilter" name="queryfilter"> Query Raised
                                                    <span></span>
                                                </label>&emsp;&emsp;
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input type="radio" value="query_ans" class="queryfilter" name="queryfilter"> Query Answered
                                                    <span></span>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <input type="text" name="querydate" placeholder="Search Query in a Date Range" id="querydate" autocomplete="off" class="form-control daterange" />


                        <div class="search-filter-divider bg-grey-steel"></div>
                    </div>
                </div>
            </div>
        </div>
        <!--</form>--><!-- form tag deleted by pr on 2ndjan18 -->
    </div>
</div>

<!-- end::Quick Panel -->