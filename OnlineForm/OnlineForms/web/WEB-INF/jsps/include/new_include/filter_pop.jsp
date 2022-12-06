<%@page import="java.util.ArrayList"%>
<%@page import="com.org.utility.Constants"%>
<%@ taglib  uri="/struts-tags" prefix="s"%>
<%
ArrayList frmSession = null;
ArrayList statSession = null;
if (session.getAttribute("frmArrList") != null) {
    frmSession = (ArrayList) session.getAttribute("frmArrList");
    session.removeAttribute("frmArrList");
}
if (session.getAttribute("statArrList") != null) {
    statSession = (ArrayList) session.getAttribute("statArrList");
    session.removeAttribute("statArrList");
}
%>
<!-- begin::Quick Panel -->
<div id="k_quick_panel" class="k-quick-panel">
    <div class="col-12 mainpage">
        <div id="close-filter" class="col-md-12 float-right mt-3 pr-0"><i class="fa fa-window-close fa-2x float-right" id="close-filter-i"></i></div>
        <div class="filter row mt-3 mb-3">
            <div class="col-6 pr-1 mt-2 mb-2">
                <a class="filter-a btn" href="javascript:void(0);"  onclick="setAction('total');" style="background: #303F9F;width: 100%;">
                    <div class="row">
                        <div class="col-9 pr-2">
                            <h6>Total Requests</h6>
                        </div>
                        <div class="col-3 pl-0">
                            <h5><span class="k-widget-3__content-number" data-counter="counterup" data-value="<s:property value="listBeanObj.totalRequest" />">0</span></h5>
                        </div>
                    </div>
                </a>
            </div>

            <div class="col-6 pl-1 mt-2 mb-2">
                <a class="filter-a btn" href="javascript:void(0);"  onclick="setAction('new');" style="background: #ffb822;width: 100%;">
                    <div class="row">
                        <div class="col-9 pr-2">
                            <h6>Today Pending</h6>
                        </div>
                        <div class="col-3 pl-0">
                            <h5><span class="k-widget-3__content-number" data-counter="counterup" data-value="<s:property value="listBeanObj.newRequest" />">0</span></h5>
                        </div>
                    </div>
                </a>
            </div>

            <div class="col-6 pr-1 mt-2 mb-2">
                <a class="filter-a btn" href="javascript:void(0);"  onclick="setAction('pending');" style="background: #d32f2f;width: 100%;">
                    <div class="row">
                        <div class="col-9 pr-2">
                            <h6>Total Pending</h6>
                        </div>
                        <div class="col-3 pl-0">
                            <h5><span class="k-widget-3__content-number" data-counter="counterup" data-value="<s:property value="listBeanObj.pendingRequest" />">0</span></h5>
                        </div>
                    </div>
                </a>
            </div>

            <div class="col-6 col-6 pl-1 mt-2 mb-2">
                <a class="filter-a btn" href="javascript:void(0);"  onclick="setAction('completed');" style="background: #00796B;width: 100%;">
                    <div class="row">
                        <div class="col-9 pr-2">
                            <h6>Total Completed</h6>
                        </div>
                        <div class="col-3 pl-0">
                            <h5><span class="k-widget-3__content-number" data-counter="counterup" data-value="<s:property value="listBeanObj.completeRequest" />">0</span></h5>
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
            <div id="collapse_3_1" class="panel-collapse ">
                <div class="k-portlet__body">
                    <div class="k-widget-18">
                        <h3 class="theme-heading-h3">Application</h3>
                        <div class="mt-checkbox-list">
                            <!-- start, below code added and modified by pr on 9thjan18  -->
                            <s:iterator value="listBeanObj.filterForms" var="formString">
                                <s:if test="%{#formString == 'single'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Single User
                                        <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.SINGLE_FORM_KEYWORD)) { %> checked <%}%> value="<%=Constants.SINGLE_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>
                                <s:if test="%{#formString == 'bulk'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Bulk User
                                        <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.BULK_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.BULK_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>
                                <s:if test="%{#formString == 'sms'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> SMS User
                                        <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.SMS_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.SMS_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>
                                <s:if test="%{#formString == 'ip'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Change IP
                                        <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.IP_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.IP_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>
                                <s:if test="%{#formString == 'dlist'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Distribution List
                                        <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.DIST_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.DIST_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>
                                <s:if test="%{#formString == 'gem'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> GEM User
                                        <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.GEM_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.GEM_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>
                                <s:if test="%{#formString == 'mobile'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Mobile change
                                        <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.MOB_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.MOB_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>
                                <s:if test="%{#formString == 'imappop'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> IMAP POP
                                        <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.IMAP_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.IMAP_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>
                                <s:if test="%{#formString == 'ldap'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> LDAP Authentication
                                        <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.LDAP_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.LDAP_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>
                                <s:if test="%{#formString == 'nkn_single'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> NKN User
                                        <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.NKN_SINGLE_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.NKN_SINGLE_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>
                                <s:if test="%{#formString == 'relay'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Relay Server
                                        <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.RELAY_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.RELAY_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>
                                <s:if test="%{#formString == 'dns'}">    
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> DNS Registration
                                        <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.DNS_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.DNS_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label> 
                                </s:if>
                                <s:if test="%{#formString == 'wifi'}">        
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> WIFI Registration
                                        <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.WIFI_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.WIFI_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>    
                                <s:if test="%{#formString == 'vpn_single'}">           
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> VPN Registration
                                        <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.VPN_SINGLE_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.VPN_SINGLE_FORM_KEYWORD%>" name="frm">
                                        <span></span>
                                    </label>
                                </s:if>    
                            </s:iterator>
                            <!-- end, below code added and modified by pr on 9thjan18  -->
                        </div>
                        <h3 class="theme-heading-h3">Status</h3>
                        <div class="mt-checkbox-list">
                            <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Pending Request
                                <input type="checkbox" value="pending"    name="stat">
                                <span></span>
                            </label>
                            <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Rejected Request
                                <input type="checkbox" value="rejected" name="stat">
                                <span></span>
                            </label>
                            <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Completed Request
                                <input type="checkbox" value="completed" name="stat">
                                <span></span>
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--</form>--><!-- form tag deleted by pr on 2ndjan18 -->
    </div>
</div>

<!-- end::Quick Panel -->