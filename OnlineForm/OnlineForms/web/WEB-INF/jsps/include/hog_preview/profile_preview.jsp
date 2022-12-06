<%@page import="com.org.bean.UserData"%>
<%@page import="com.org.bean.ImportantData"%>
<%@page import="java.util.ArrayList"%>
<div class="k-portlet__head">
    <h4 class="theme-heading-h3">Personal Information</h4>
</div>
<div class="form-group row">
    <div class="col-md-4">
        <label>Name of Applicant <span style="color: red">*</span></label>
        <input class="form-control" placeholder="[Only characters,dot(.) and whitespace allowed]" type="text" name="user_name" id="user_name"  value="" maxlength="50">
        <font style="color:red"><span id="username_error"></span></font>
    </div>
    <div class="col-md-4">
        <label for="street">Designation <span style="color: red">*</span></label>
        <input class="form-control" placeholder="[Only characters,digits,whitespace and [. , - &] allowed]" type="text" name="user_design" id="user_design"  value="" maxlength="50">
        <font style="color:red"><span id="userdesign_error"></span></font>
    </div>
    <div class="col-md-4" id="empcode">
        <label for="street">Employee Code </label>
        <input class="form-control" placeholder="[Only characters and digits allowed]" type="text" name="user_empcode" id="user_empcode"  value="" maxlength="12">
        <font style="color:red"><span id="userempcode_error"></span></font>
    </div>
</div>
<div class="k-portlet__head">
    <h3 class="theme-heading-h3-popup">Office Address</h3>
</div>
<div class="form-group row">
    <div class="col-md-12">
        <label for="street">Postal Address <span style="color: red">*</span></label>
        <input class="form-control " placeholder="Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" type="text" name="user_address" id="user_address"  maxlength="100"  value="" aria-required="true">
        <font style="color:red"><span id="useraddress_error"></span></font>
    </div>
</div>
<div class="form-group row">
    <div class="col-md-4">
        <label>State where you are posted <span style="color: red">*</span></label>
        <%
            String statename = "";
            UserData userdata = (UserData) session.getAttribute("uservalues");
            ImportantData impdata = (ImportantData) userdata.getImpData();
            ArrayList state_name = (ArrayList) impdata.getStates();
        %>
        <select id="user_state" name="user_state" theme="simple" class="form-control">
            <%            for (int i = 0; i < state_name.size(); i++) {
                    statename = state_name.get(i).toString();
            %>
            <option value="<%=statename%>"><%=statename%></option>
            <% }%>
        </select>
        <font style="color:red"><span id="userstate_error"></span></font>
    </div>
    <div class="col-md-4">
        <label for="street">District <span style="color: red">*</span></label>
        <input class="form-control" placeholder="[Only characters and whitespace allowed]" type="text" name="user_city" id="user_city" maxlength="50"  value="" >
        <font style="color:red"><span id="usercity_error"></span></font>
    </div>
    <div class="col-md-4">
        <label for="street">Pin Code <span style="color: red">*</span></label>
        <input class="form-control" placeholder="[Only digits(6) allowed]" type="text" name="user_pincode" id="user_pincode"  maxlength="6"  value="" aria-required="true">
        <font style="color:red"><span id="userpincode_error"></span></font>
    </div>
</div>
<div class="form-group row">
    <div class="col-md-6">
        <label for="street">Telephone Number :(O)</label>
        <input class="form-control" placeholder="Enter Telephone Number(O) [STD CODE-TELEPHONE]" type="text" name="user_ophone" id="user_ophone"  maxlength="15"  value="" aria-required="true">
        <font style="color:red"><span id="userophone_error"></span></font>
    </div>
    <div class="col-md-6">
        <label for="street">Telephone Number :(R)<span style="color: red"></span></label>
        <input class="form-control" placeholder="Enter Telephone Number(R) [STD CODE-TELEPHONE]" type="text" name="user_rphone" id="user_rphone"  maxlength="15" value="">
        <font style="color:red"><span id="userrphone_error"></span></font>
    </div>
</div>
<div class="form-group">
    <div class="form-group row">
        <div class="col-md-6">
            <label for="street">Mobile <span style="color: red">*</span></label>
            <input class="form-control" placeholder="Enter Mobile [e.g: +919999999999]" type="text" name="user_mobile" id="user_mobile"  value="" maxlength="13">
            <font style="color:red"><span id="usermobile_error"></span></font>
        </div>
        <div class="col-md-6">
            <label for="street">E-mail Address <span style="color: red">*</span></label>
            <input class="form-control" style="text-transform:lowercase;" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" type="text" name="user_email" id="user_email"  value="" maxlength="100">
            <font style="color:red"><span id="useremail_error"></span></font>
        </div>
        <span id="nicemp"></span>
    </div>
</div>
<div id="REditPreview">
    <div class="k-portlet__head">
        <h3 class="theme-heading-h3-popup">Reporting/Nodal/Forwarding Officer Details</h3>
    </div>
    <div class="form-group row">
        <div class="col-md-6">
            <label for="street">Reporting/Nodal/Forwarding Officer Email <span style="color: red">*</span></label>
            <input class="form-control" style="text-transform:lowercase;" placeholder="Enter E-Mail Address [e.g: abc.xyz@zxc.com]" type="text" name="hod_email" id="hod_email" value="" maxlength="50">
            <font style="color:red"><span id="hodemail_error"></span></font>
            <font style="color:red"><span id="hodemail_error1"></span></font>
        </div>
        <div class="col-md-6">
            <label for="street">Reporting/Nodal/Forwarding Officer Name  <span style="color: red">*</span></label>
            <input class="form-control" placeholder="Enter Name [characters,dot(.) and whitespace]" type="text" name="hod_name" id="hod_name" value="" maxlength="50">
            <font style="color:red"><span id="hodname_error"></span></font>
        </div>
    </div>
    <div class="form-group">
        <div class="form-group row">
            <div class="col-md-6">
                <label for="street">Reporting/Nodal/Forwarding Officer Mobile <span style="color: red">*</span></label>
                <input class="form-control" placeholder="Enter Mobile Number [e.g: +919999999999]" type="text" name="hod_mobile" id="hod_mobile" value="" maxlength="15">
                <font style="color:red"><span id="hodmobile_error"></span></font>
            </div>
            <div class="col-md-6">
                <label for="street">Reporting/Nodal/Forwarding Officer Telephone <span style="color: red">*</span></label>
                <input class="form-control" style="text-transform:lowercase;" type="text" id="hod_tel" name="hod_tel" placeholder="Enter Reporting/Nodal/Forwarding OfficerTelephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]" maxlength="15">
                <font style="color:red"><span id="hodtel_error"></span></font>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <label>Reporting/Nodal/Forwarding Officer Designation <span style="color: red">*</span></label>
                <input type="text" id="ca_design" name="ca_design" placeholder="Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespace and [. , - &]]" maxlength="50" class="form-control" value="" /> 
                <font style="color:red"><span id="cadesign_error"></span></font>
            </div>
        </div>
    </div>
</div>
<div id="EditPreview"> 
    <div class="k-portlet__head">
        <h3 class="theme-heading-h3-popup">Organization Details</h3>
    </div>
    <div class="row form-group">
        <div class="col-md-6">
            <label>Organization Category</label>                                                                               
            <select class="form-control" id="user_employment" name="user_employment">
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
            <font style="color:red"><span id="useremployment_error"></span></font>
        </div>
        <div class="col-md-6" id="central_div" style="display:none">
            <label for="street">Ministry/Organization <span style="color: red">*</span></label>
            <select class='form-control' name='min' id='min'>
                <option value=''>-SELECT-</option>
            </select>
            <font style="color:red"><span id="minerror"></span></font>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12" id="central_div" style="display:none">
            <div class="form-group">
                <label for="street">Department/Division/Domain <span style="color: red">*</span></label>
                <select class='form-control' name='dept' id='dept' >
                    <option value=''>-SELECT-</option>
                </select>
                <font style="color:red"><span id="deperror"></span></font>
            </div>
        </div>
    </div>
    <div class="row form-group">
        <div class="col-md-12" id="state_div" style="display:none">
            <div class="row">
                <div class="col-md-6">
                    <label for="street">State <span style="color: red">*</span></label>
                    <select class='form-control' id="stateCode" name="stateCode">
                        <option value=''>-SELECT-</option>
                    </select>
                    <font style="color:red"><span id="state_error"></span></font>
                </div>
                <div class="col-md-6">
                    <label for="street">Department <span style="color: red">*</span></label>
                    <select class='form-control' name='state_dept' id='Smi'>
                        <option value=''>-SELECT-</option>
                    </select>
                    <font style="color:red"><span id="smierror"></span></font>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div  id="other_div" class="col-md-12" style="display:none">
            <label class='control-label' for='street'>Organization Name <span style='color: red'>*</span></label>
            <select class='form-control' name='org' id='Org'>
                <option value=''>-SELECT-</option>
            </select>
            <font style='color:red'><span id='body_error'></span></font>
        </div>
    </div>
    <div class="row">
        <div id="other_text_div"  class="display-hide col-md-12">
            <label>Other<span style='color: red'>*</span></label>
            <input type="text" id="other_dept" name="other_dept" placeholder="Enter Department Name [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),underscore(_),ampersand(&) allowed]" maxlength="100" class="form-control" /> 
            <font style="color:red"><span id="other_dep_error"></span></font>
        </div>
    </div>

</div>

