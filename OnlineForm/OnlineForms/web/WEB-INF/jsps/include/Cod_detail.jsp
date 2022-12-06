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
            <p style="font-weight: bold">We are sending your request for approval to support@nic.in.</p>
            <p style="font-weight: bold">Are you sure, you want to proceed?
             <div class="modal-footer">
                <button type="button" class="btn dark btn-outline" data-dismiss="modal">No</button>                                
                <button type="button" class="btn red btn-outline save-changes" id="confirmYes">Yes</button>                                
            </div>   
        </div>
    </div>
</div>
