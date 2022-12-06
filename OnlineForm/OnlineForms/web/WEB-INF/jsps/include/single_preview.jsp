
<%@page import="java.util.ArrayList"%>
<%@page import="com.org.bean.ImportantData"%>
<%@page import="com.org.bean.UserData"%>
<%
    UserData userdata = (UserData) session.getAttribute("uservalues");
%>
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="single_preview_tab">
            <!--Modal body start-->
            <span class="alert alert-info" style="font-size: 16px;font-weight: 500;"><span>Single User Subscription Form</span></span>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div id="under_sec_div">
                    <jsp:include page="underSecretary_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Single User Subscription Details</h3>
                </div>
                 
                <div class="col-md-12">
                    
                    <div class="e-office-div mt-3 mb-3">
                        <label class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="req_user_type" id="req_self_type"  value="self" checked=""> For Self
                            <span></span>
                        </label>&emsp;&emsp;
                        <label class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="req_user_type" id="req_other_type"  value="other_user" > For Other User(Where you are posted)
                            <span></span>
                        </label>&emsp;&emsp;


                    </div>
                    <font style="color:red"><span id="req_for_err"></span></font>
                </div>
                 
                <div id="div_for_self" class="display-hide">
                    <div class="row form-group">
                        <div class="col-md-12">
                            <label class="control-label" for="street">Type of Mail ID: <span style="color: red">*</span></label>
                            <div class="row">
                                <div class="col-md-4">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="req_for" id="mail_1"  value="mail" >Mail user (with mailbox)
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-4">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="req_for" id="app_1"  value="app" >Application user (without mail box(Eoffice-auth)) <!-- line modified by pr on 5thmay2020  -->
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-4">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="req_for" id="eoffice_1"  value="eoffice" >e-office-srilanka
                                        <span></span>
                                    </label>
                                </div>
                            </div>
                            <font style="color:red"><span id="req_for_err"></span></font>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-6">
                            <label class="control-label" for="street">Date Of Birth <span style="color: red">*</span></label>
                            <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dob" id="single_dob1" placeholder="Enter Date Of Birth" />
                            <font style="color:red"><span id="single_dob_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label class="control-label" for="street">Date Of Retirement/Date of expiry <span style="color: red">*</span></label>
                            <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dor" id="single_dor1" placeholder="Enter Date Of Retirement" />
                            <font style="color:red"><span id="single_dor_err"></span></font>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-6">
                            <label class="control-label" for="street">Email address preference: <span style="color: red">*</span></label>
                            <div class="row" id="id_based">
                                <div class="col-md-6">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="single_id_type" id="single_id_type_1"  value="id_name" > Name Based
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-6">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="single_id_type" id="single_id_type_2"  value="id_desig" >Designation/Office based id
                                        <span></span>
                                    </label>
                                </div>
                            </div>
                            <font style="color:red"><span id="single_id_type_err"></span></font>


                    </div>
                    <div class="col-md-6">
                        <label class="control-label" for="street">Employee Description: <span style="color: red">*</span></label>
                        <div id="emp_type" class="row">
                            <div class="col-md-5">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="single_emp_type" id="single_emp_type1"  value="emp_regular" >Govt/Psu Official
                                    <span></span>
                                </label>
                            </div>
                            <div class="col-md-7">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="single_emp_type" id="single_emp_type2"  value="consultant" >Consultant/Contractual Staff
                                    <span></span>
                                </label>
                            </div>
                            <div class="col-md-6">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="single_emp_type" id="single_emp_type3"  value="emp_contract" >FMS Support Staffs
                                    <span></span>
                                </label>
                            </div>
                        </div>
                        <font style="color:red"><span id="single_emp_type_err"></span></font>
                    </div>

                    </div>
                    <div class="row emailcheck">
                        <div class="col-md-6">
                            <label class="control-label" for="street">Preferred Email Address 1 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com OR abc.xyz@nic.in]" type="text" name="single_email1" id="single_email1" value="">
                            <font style="color:red"><span id="single_email1_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label class="control-label" for="street">Preferred Email Address 2 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com OR abc.xyz@nic.in]" type="text" name="single_email2" id="single_email2" value="">
                            <font style="color:red"><span id="single_email2_err"></span></font>
                        </div>
                        <div class="col-md-12">  <font style="color:red"><span id="single_dup_err"></span></font></div>
                    </div>  
                </div>

                <div id="div_for_other" class="display-hide"> 
                    <div class="col-md-12">
                        <label for="street">Type of Mail ID: <span style="color: red">*</span> <a href="" id="mD2" data-toggle="modal" data-target="#myModal1" style="color:blue">(Know More<i class="entypo-help"></i>)</a></label>
                        <div class="e-office-div mt-3 mb-3">
                            <% if (userdata.getUserOfficialData() != null && userdata.getUserOfficialData().getDepartment() != null && !userdata.getUserOfficialData().getDepartment().contains("E-office")) { %>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="applicant_req_for" id="mail"  value="mail" checked=""> Mail user (with mailbox)
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="applicant_req_for" id="app"  value="app" > Application user (without mail box(Eoffice-auth)) <!-- line modified by pr on 5thmay2020 -->
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="applicant_req_for" id="eoffice"  value="eoffice" > e-office-srilanka
                                <span></span>
                            </label>&emsp;&emsp;
                            <% } else { %>
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="applicant_req_for" id="app"  value="app" checked> Application user (without mail box(Eoffice-auth)) <!-- line modified by pr on 5thmay2020 -->
                                <span></span>
                            </label>&emsp;&emsp;
                            <% }%>
                        </div>
                        <font style="color:red"><span id="applicant_req_for_err"></span></font>
                    </div>
                    <div class="col-md-12">
                        <div class="row form-group">
                            <div class="col-md-6">
                                <label>User Name <span style="color: red">*</span></label>
                                <input type="text" id="applicant_name" name="applicant_name" placeholder="Enter Full Name [Only characters,dot(.) and whitespace allowed]" class="form-control" maxlength="50" value=""  /> 
                                <font style="color:red"><span id="applicant_name_error"></span></font>
                            </div>
                            <div class="col-md-6">
                                <label>Employee Code</label>
                                <input type="text" id="applicant_empcode" name="applicant_empcode" placeholder="Enter Employee Code [Only characters and digits allowed]" maxlength="12" class="form-control" value="" /> 
                                <font style="color:red"><span id="applicant_emp_code_error"></span></font>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="row form-group">
                            <div class="col-md-6">
                                <label>Mobile<span style="color: red">*</span></label>

                                <input type="text" id="applicant_mobile" name="applicant_mobile" placeholder="Enter Mobile Number [e.g:+919999999999]" class="form-control" maxlength="13" value=""/> 

                                <font style="color:red"><span id="applicant_mobile_error"></span></font>
                            </div>
                            <div class="col-md-6">
                                <label>Email Address <span style="color: red">*</span></label>
                                <input type="text" id="applicant_email" name="applicant_email" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" class="form-control" maxlength="100" value="" /> 
                                <font style="color:red"><span id="applicant_email_error"></span></font>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="row form-group">
                            <div class="col-md-6">
                                <label>Designation <span style="color: red">*</span></label>
                                <input type="text" id="applicant_design" name="applicant_design" placeholder="Enter Designation [Only characters,digits,whitespace and [. , - &] allowed]"  maxlength="50" class="form-control" value="" /> 
                                <font style="color:red"><span id="applicant_desg_error"></span></font>
                            </div>

                            <div class="col-md-6">
                                <label>State where you are posted <span style="color: red">*</span></label>
                                <%
                                    ImportantData impdata = (ImportantData) userdata.getImpData();
                                    ArrayList state_name = (ArrayList) impdata.getStates();
                                    System.out.println("state_name" + state_name);
                                    String statename = "";
                                %>
                                <select id="applicant_state" name="applicant_state" theme="simple" class="form-control">
                                    <option value="" selected>select</option>
                                    <%            for (int i = 0; i < state_name.size(); i++) {
                                            statename = state_name.get(i).toString();
                                    %>
                                    <option value="<%=statename%>"><%=statename%></option>
                                    <%}
                                    %>
                                </select>
                                <font style="color:red"><span id="applicant_state_error"></span></font>
                            </div>



                        </div>


                    </div>
                    <div class="col-md-12">
                        <div class="row form-group">
                            <div class="col-md-4">
                                <label>Organization Category <span style="color: red">*</span></label>                                                                                    
                                <select class="form-control" id="applicant_employment" name="applicant_employment">
                                    <option value="">--Select--</option>
                                    <option value="Central">Central</option>
                                    <option value="State">State</option>
                                    <option value="Psu">PSU</option>
                                    <option value="Const">Constitutional Bodies</option>
                                    <option value="Nkn">Nkn Institutes</option>
                                    <option value="Project">Project</option>
                                    <option value="UT">Union Territory</option>
                                    <option value="Others">Others</option>
                                </select>
                                <font style="color:red"><span id="applicantemployment_error"></span></font>
                            </div>
                            <div id="applicant_central_div" class="col-md-8" style="display:none">
                                <div class="row">
                                    <div class="col-md-6">
                                        <label for="street">Ministry/Organization <span style="color: red">*</span></label>
                                        <select class='form-control' name='applicant_min' id='applicant_min'>
                                            <option value=''>-SELECT-</option>
                                        </select>
                                        <font style="color:red"><span id="minerror"></span></font>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="street">Department/Division/Domain <span style="color: red">*</span></label>
                                        <select class='form-control' name='applicant_dept' id='applicant_dept' >
                                            <option value=''>-SELECT-</option>
                                        </select>   
                                        <font style="color:red"><span id="deperror"></span></font>                                                                                        
                                    </div>
                                </div>
                            </div>
                            <div id="applicant_state_div" class="col-md-8" style="display:none">
                                <div class="row">
                                    <div class="col-md-6">
                                        <label for="street">State <span style="color: red">*</span></label>
                                        <select class='form-control' id="applicant_stateCode" name="applicant_stateCode">
                                            <option value=''>-SELECT-</option>
                                        </select>
                                        <font style="color:red"><span id="state_error"></span></font>                                                                                                                                                                     
                                    </div>
                                    <div class="col-md-6">
                                        <label for="street">Department <span style="color: red">*</span></label>
                                        <select class='form-control' name='applicant_Smi' id='applicant_Smi'>
                                            <option value=''>-SELECT-</option>
                                        </select>       
                                        <font style="color:red"><span id="smierror"></span></font>                                                                                        
                                    </div>
                                </div>
                            </div>
                            <div id="applicant_other_div" class="col-md-8" style="display:none">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label for='street'>Organization Name <span style='color: red'>*</span></label>
                                            <select class='form-control' name='applicant_Org' id='applicant_Org'>
                                                <option value=''>-SELECT-</option>
                                            </select>
                                        </div>
                                        <font style='color:red'><span id='applicant_org_error'></span></font> 
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="row mt-3 mb-3">
                            <div class="col-md-6 ">
                                <div id="applicant_other_text_div" class="display-hide" >
                                    <label>Other <span style="color: red">*</span></label>
                                    <input type="text" id="applicant_other_dept" name="applicant_other_dept" placeholder="Enter Department Name [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]" maxlength="100" class="form-control" value="" /> 
                                    <font style="color:red"><span id="applicant_other_dep_error"></span></font>
                                </div> 
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="row form-group">
                            <div class="col-md-6">
                                <label for="street">Date Of Birth <span style="color: red">*</span></label>
                                <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="applicant_single_dob" id="single_dob7" placeholder="Enter Date Of Birth [DD-MM-YYYY]" readonly />
                                <font style="color:red"><span id="single_dob_err"></span></font>
                            </div>
                            <div class="col-md-6">
                                <label for="street">Date Of Retirement/Date of expiry<span style="color: red">*</span></label>
                                <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="applicant_single_dor" id="single_dor7" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly/>
                                <font style="color:red"><span id="single_dor_err"></span></font>
                            </div>
                        </div>
                    </div>


                    <div class="col-md-12">
                        <div class="row form-group" >
                            <div class="col-md-6">
                                <label for="street">Email address preference: <span style="color: red">*</span></label>
                                <div id="id_based">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="applicant_single_id_type" id="applicant_single_id_type_1"  value="id_name" checked=""> Name Based
                                        <span></span>
                                    </label>&emsp;&emsp;
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="applicant_single_id_type" id="applicant_single_id_type_2"  value="id_desig" > Designation/Office based id
                                        <span></span>
                                    </label>&emsp;&emsp;
                                </div>
                                <font style="color:red"><span id="single_id_type_err"></span></font>
                            </div>
                            <div class="col-md-6">
                                <label for="street">Employee Description: <span style="color: red">*</span></label>
                                <div id="emp_type">
                                    <div class="row">
                                        <div class="col-md-5">
                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                <input type="radio" name="applicant_single_emp_type" id="applicant_single_emp_type1"  value="emp_regular" checked=""> Govt/Psu Official
                                                <span></span>
                                            </label>
                                        </div>
                                        <div class="col-md-7">
                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                <input type="radio" name="applicant_single_emp_type" id="applicant_single_emp_type2"  value="consultant" > Consultant/Contractual Staff
                                                <span></span>
                                            </label>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                <input type="radio" name="applicant_single_emp_type" id="applicant_single_emp_type3"  value="emp_contract" > FMS Support Staffs
                                                <span></span>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <font style="color:red"><span id="single_emp_type_err"></span></font>
                            </div>
                        </div>       
                    </div>        


                    <div class="col-md-12">

                        <div class="row applicantemailcheck">
                            <div class="col-md-6">
                                <label class="control-label" for="street">Preferred Email Address 1 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                                <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com OR abc.xyz@nic.in]" type="text" name="applicant_single_email1" id="applicant_single_email1" value="">
                                <font style="color:red"><span id="single_email1_err"></span></font>
                            </div>
                            <div class="col-md-6">
                                <label class="control-label" for="street">Preferred Email Address 2 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                                <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com OR abc.xyz@nic.in]" type="text" name="applicant_single_email2" id="applicant_single_email2" value="">
                                <font style="color:red"><span id="single_email2_err"></span></font>
                            </div>
                            <div class="col-md-12">  <font style="color:red"><span id="single_dup_err"></span></font></div>
                        </div>  

                    </div>
                </div>

                <!--  below div added by pr on 18thjul18  -->
                <div class="row emailcheck final_id_span_cls display-hide">
                    <div class="col-md-12">
                        <label class="control-label" for="street"><strong>ID Assigned : </strong></label>
                        <span class="final_id_cls"></span>
                    </div>
                </div>  
                <div class="form-group mt-4" id='tnc_div'>
                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                        <input type="checkbox"  name="tnc" id="tnc"> <b>I agree to <a data-toggle="modal" href="#stack2" style="color:blue">Terms and Conditions</a></b>
                        <span></span>
                    </label>
                    <font style="color:red"><span id="tnc_error"></span></font>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-success edit" >Edit</button>
            <button type="button" class="btn red btn-warning save-changes" id="confirm">Submit</button>
            <button type="button" class="btn dark btn-default" data-dismiss="modal" id="closebtn">Close</button>
        </div> 
    </div>
</div>