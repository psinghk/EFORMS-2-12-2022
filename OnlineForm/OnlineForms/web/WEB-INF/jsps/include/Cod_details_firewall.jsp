<%-- 
    Document   : Hod_detail
    Created on : Jan 9, 2018, 1:56:45 PM
    Author     : nikki
--%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <h4 class="modal-title">Coordinator Details:</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        </div>
        <div class="modal-body"> 
            <p><i class="entypo-info-circled"></i> We are sending your request for approval to Coordinators (ashish@nic.in,hasan@gov.in,sseca2.sp-dl@nkn.in)<br/> </p>            
            <p style="font-weight: bold">Are you sure you want to submit the form?</p>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-danger" data-dismiss="modal">No</button>                                
                <button type="button" class="btn red btn-primary save-changes" id="confirmYes">Yes</button>                                
            </div>
        </div>
    </div>
</div>
