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
            <p style="font-weight: bold">We are sending your request for approval to email address (<span style="color: blue;" id="fill_hod_email"></span>)</p>
            <table class="table table-bordered">
                <tr>
                    <td><b>Name:</b></td>
                    <td><span id="fill_hod_name"></span></td>
                </tr>
                <tr>
                    <td><b>Email:</b></td>
                    <td><span id="fill_hod_email"></span></td>
                </tr>
                <tr>
                    <td><b>Mobile:</b></td>
                    <td><span id="fill_hod_mobile"></span></td>
                </tr>
            </table>
            <p style="font-weight: bold">Are you sure, you want to proceed?
            <p style="font-weight: bold" id="wifi_details2" class="display-hide">Since, you have requested for Wi-Fi Certificate so it will take some time for creation.
                Thanks for your patience.</p>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn dark btn-outline btn-primary" data-dismiss="modal">No</button>                                
            <button type="button" class="btn red btn-outline save-changes btn-success" id="confirmYes">Yes</button>                                
        </div>
    </div>
</div>
} %>