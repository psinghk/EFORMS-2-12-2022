
<%@page import="com.org.bean.UserData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.org.bean.ImportantData"%>
<%

    UserData userdata = (UserData) session.getAttribute("uservalues");

%>
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="gem_preview_tab">
            <!--Modal body start-->
            <div class="alert alert-primary"><span style="font-size: 16px;">GEM User Subscription Form</span></div>
            
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div id="forwarding_div">
                    <div class="k-portlet__head">
                        <h3 class="theme-heading-h3-popup">Forwarding Officer Details</h3>
                    </div>
                    <label for="street"><strong>Your application need to be forwarded by an officer at the level of Under Secretary or above and having government email address, for example @nic.in/@gov.in. 
                            Once approved by the Forwarding Officer, your request will be forwarded to gemapplicant@gem.gov.in. 
                            Please contact GEM support (gemapplicant@gem.gov.in) for any queries.</strong></label>
                    <div class="row">
                        <div class="col-md-6">
                            <label for="street">Email <span style="color: red">*</span></label>
                            <input class="form-control" style="text-transform:lowercase;" placeholder="Enter E-Mail Address [e.g: abc.xyz@zxc.com]" type="text" name="fwd_ofc_email" id="fwd_ofc_email" value="" maxlength="50">
                            <font style="color:red"><span id="fwd_email_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street"> Name  <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Name [characters,dot(.) and whitespace]" type="text" name="fwd_ofc_name" id="fwd_ofc_name" value="" maxlength="50">
                            <font style="color:red"><span id="fwd_name_err"></span></font>
                        </div>
                        <span id="nicemp"></span>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-6">
                                <label for="street">Mobile <span style="color: red">*</span></label>
                                <input class="form-control" placeholder="Enter Mobile Number [e.g: +919999999999]" type="text" name="fwd_ofc_mobile" id="fwd_ofc_mobile" value="" maxlength="15">
                                <font style="color:red"><span id="fwd_mobile_err"></span></font>
                            </div>
                            <div class="col-md-6">
                                <label for="street">Telephone <span style="color: red">*</span></label>
                                <input class="form-control" style="text-transform:lowercase;" type="text" id="fwd_ofc_tel" name="fwd_ofc_tel" placeholder="Enter Reporting/Nodal/Forwarding OfficerTelephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]" maxlength="15">
                                <font style="color:red"><span id="fwd_tel_err"></span></font>
                            </div>
                            <span id="nicemp"></span>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label>Designation <span style="color: red">*</span></label>
                                <input type="text" id="fwd_ofc_design" name="fwd_ofc_design" placeholder="Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespace and [. , - &]]" maxlength="50" class="form-control" value="" /> 
                                <font style="color:red"><span id="fwd_desig_err"></span></font>
                            </div>
                            <div class="col-md-6">
                                <label>Address <span style="color: red">*</span></label>
                                <input type="text" id="fwd_ofc_add" name="fwd_ofc_add" placeholder="Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" maxlength="50" class="form-control" value="" /> 
                                <font style="color:red"><span id="fwd_add_err"></span></font>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="primary_div">
                    <div class="row"  >
                        <div class="col-md-6">
                            <label>Are you primary user/HOD on GeM portal    <span style="color: red">*</span></label>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" id="primary_user_yes" name="primary_user"  value="yes" /> Yes
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" id="primary_user_no" name="primary_user"  value="no" /> No
                                <span></span>
                            </label>
                            <font style="color:red"><span id="primary_user_err"></span></font>
                        </div>
                        <div class="col-md-6 display-hide" id="primary_text_id">
                            <label>Enter User Id <span style="color: red">*</span></label>
                            <input type="text" id="primary_user_id" name="primary_user_id" placeholder="Enter User Id [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" maxlength="50" class="form-control" value="" /> 
                            <font style="color:red"><span id="primary_id_err"></span></font>
                        </div>
                    </div>

                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">GEM User Subscription Details</h3>
                </div>
                <div class="row"  >
                    <div class="col-md-12">
                        <label for="street">Organization Category <span style="color: red">*</span></label>
                        <div id="pse_service">
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="pse" id="central_pse"  value="central_pse" checked=""> Central PSE (Which are controlled by Central Ministry)
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="pse" id="state_pse"  value="state_pse" > State PSE (Which are controlled by State)
                                <span></span>
                            </label>
                        </div>
                        <font style="color:red"><span id="pse_err"></span></font>
                    </div>
                </div>
                <div class="row" id="central_pse_div"  >
                    <div class="col-md-12">
                        <label class='control-label' for='street'>Controlling Ministry <span style='color: red'>*</span></label>
                        <div class="form-group">
                            <select class='form-control' name='pse_ministry' id='pse_ministry'>
                                <option value=''>-SELECT-</option>
                            </select>
                        </div>
                        <font style='color:red'><span id='central_pse_err'></span></font>                                                                                        
                    </div>
                </div>
                <div class="row display-hide" id="state_pse_div"  >
                    <div class="col-md-6">
                        <label class='control-label' for='street'>State (Where PSE is located) <span style='color: red'>*</span></label>

                        <%                              ImportantData impdata = (ImportantData) userdata.getImpData();
                            ArrayList state_name = (ArrayList) impdata.getStates();
                            String statename = "";
                        %>
                        <div class="form-group">
                            <select id="pse_state" name="pse_state" theme="simple" class="form-control">
                                <option value="" selected>select</option>
                                <%            for (int i = 0; i < state_name.size(); i++) {
                                        statename = state_name.get(i).toString();
                                %>
                                <option value="<%=statename%>"><%=statename%></option>
                                <%}
                                %>
                            </select>
                        </div>

                        <font style='color:red'><span id='pse_state_err'></span></font>                                                                                        
                    </div>
                    <div class="col-md-6">
                        <label for="street">District Name (Where applicant is posted) <span style="color: red">*</span></label>
                        <!--                        <input class="form-control" placeholder="Enter Your District Name [Only characters,dot(.) amp(&) and whitespace allowed]" type="text" name="pse_district" id="pse_district" value="">
                        -->
                        <select class='form-control' name='pse_district' id='pse_district'>
                            <option value=''>-SELECT-</option>
                        </select> 
                        <font style="color:red"><span id="pse_district_err"></span></font>
                    </div>  
                </div>
                <div class="row"  >
                    <!--                <div class="col-md-6">
                                        <label for="street">Date Of Birth <span style="color: red">*</span></label>
                                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dob" id="single_dob3" placeholder="Enter Date Of Birth" readonly />
                                        <font style="color:red"><span id="gem_dob_err"></span></font>
                                    </div>-->
                    <div class="col-md-6">
                        <label for="street">Date Of Retirement/Date of expiry<span style="color: red">*</span></label>
                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dor" id="single_dor3" placeholder="Enter Date Of Retirement" readonly/>
                        <font style="color:red"><span id="gem_dor_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">Role to be assign <span style="color: red">*</span></label>
                        <select name="role_assign" class="form-control" id="role_assign">
                            <option value="">--Select--</option>
                            <option value="HOD">HOD</option>
                            <option value="Buyer">Buyer</option>
                            <option value="Consignee">Consignee</option>
                            <option value="BuyerCon">Buyer & consignee</option>
                            <option value="DDO">DDO</option>
                        </select>
                        <font style="color:red"><span id="gem_dor_err"></span></font>
                    </div>
                </div>                
                <div class="row emailcheck"  >
                    <div class="col-md-6">
                        <label for="street">Preferred Email Address 1 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@gembuyer.in or abc.xyz@gem.gov.in]" type="text" name="single_email1" id="single_email1" value="">
                        <font style="color:red"><span id="gem_email1_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">Preferred Email Address 2 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@gembuyer.in or abc.xyz@gem.gov.in]" type="text" name="single_email2" id="single_email2" value="">
                        <font style="color:red"><span id="gem_email2_err"></span></font>
                    </div>
                </div>
                <div class="row"  >
                    <div class="col-md-6">
                        <label for="street">Enter Your Projected Monthly Traffic <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Your Projected Monthly Traffic" type="text" name="domestic_traf" id="domestic_traf" value="" maxlength="9">
                        <font style="color:red"><span id="gem_traffic_err"></span></font>
                    </div>                                                                                
                </div>


                <!--  below div added by pr on 18thjul18  -->
                <div class="row emailcheck final_id_span_cls display-hide"  >
                    <div class="col-md-12">
                        <label for="street"><strong>ID Assigned : </strong></label>

                        <span class="final_id_cls"></span>

                    </div>

                </div>      

                <div class="form-group mt-4" id='tnc_div'>
                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                        <input type="checkbox"  name="tnc" id="tnc"> <b>I agree to <a data-toggle="modal" href="#gem-stack2" style="color:blue">Terms and Conditions</a></b>
                        <span></span>
                    </label>
                    <font style="color:red"><span id="tnc_error"></span></font>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn dark btn-default" data-dismiss="modal" id="closebtn">Close</button>
            <a href="javascript:void(0);" class="btn btn-warning edit " >Edit</a>
            <button type="button" class="btn red btn-success save-changes" id="confirm">Submit</button>
        </div> 
    </div>
</div>