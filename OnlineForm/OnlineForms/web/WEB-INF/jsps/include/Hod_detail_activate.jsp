<%-- 
    Document   : Hod_detail
    Created on : Jan 9, 2018, 1:56:45 PM
    Author     : nikki
--%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="modal-dialog" id="rep-ofc">
    <div class="modal-content">
        <div class="modal-header">
            <h4 class="modal-title" id="details_ro">Reporting/Nodal/Forwarding Officer Details</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        </div>
        <div class="modal-body">    
             <p style="font-weight: bold">We are sending your request for approval to your reporting officer</p>
           
            <p style="font-weight: bold">Are you sure, you want to proceed?
            
        </div>
        <div class="modal-footer">
            <button type="button" class="btn dark btn-outline btn-primary" data-dismiss="modal">No</button>                                
            <button type="button" class="btn red btn-outline save-changes btn-success" id="confirmYes">Yes</button>                                
        </div>
    </div>
</div>
} %>