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
            
           
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        </div>
        <div class="modal-body">            
            <p style="font-weight: bold">We are sending your request for approval to our Admin team.</p>
            <p style="font-weight: bold">Are you sure, you want to proceed?
            <p style="font-weight: bold" id="wifi_details2" class="display-hide">Since, you have requested for Wi-Fi Certificate so it will take some time for creation.
Thanks for your patience.</p>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn dark btn-danger mr-2" data-dismiss="modal">No</button>                                
            <button type="button" class="btn red btn-info save-changes" id="confirmYes">Yes</button>                                
        </div>
    </div>
</div>
