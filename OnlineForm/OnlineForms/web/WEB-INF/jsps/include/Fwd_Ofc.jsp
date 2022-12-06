<%-- 
    Document   : Hod_detail
    Created on : Jan 9, 2018, 1:56:45 PM
    Author     : nikki
--%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <h4 class="modal-title" id="details_ro">Forwarding Officer Details</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        </div>
        
         <div class="modal-body">    
            <p style="font-weight: bold">We are sending your request for approval to email address (<span style="color: blue;" id="fwd_ofc_email"></span>)</p>
            <p><i class="entypo-info-circled"></i> <label><b>Name:</b> </label> <span id="fill_hod_name"></span></p>
            <p><i class="entypo-info-circled"></i> <label> <b>Email:</b>  </label><span id="fill_hod_email"></span></p>
            <p><i class="entypo-info-circled"></i><label><b>Mobile:</b> </label> <span id="fill_hod_mobile"></span></p>
            <p style="font-weight: bold">Are you sure, you want to proceed?
            <div class="modal-footer">
                <button type="button" class="btn dark btn-outline" data-dismiss="modal">No</button>                                
                <button type="button" class="btn red btn-outline save-changes" id="confirmYes">Yes</button>                                
            </div>
        </div>
        
    </div>
</div>






