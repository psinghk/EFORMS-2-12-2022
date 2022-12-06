<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="webcast_preview_tab">
            <div class="alert alert-primary"><span style="font-size: 16px;">Webcast Services Form</span></div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div id="forwarding_div">
                    <div class="k-portlet__head">
                        <h3 class="theme-heading-h3-popup">Forwarding Officer Details</h3>
                    </div>
                    <div class="row form-group">

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
                        <div class="row form-group">
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
                        <div class="row form-group">

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
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Webcast Request Details</h3>
                </div>

                <div class="row form-group">
                    <div class="col-md-12">
                        <input type="hidden" name="req_for" id="req_for" />
                        <label for="street">Type of Request: <span style="color: red">*</span> <span class="uppercase" id="req_for_span" style="font-weight: bold; font-size: 15px;"></span></label>
                    </div>
                </div>
                <!-- WEBCAST LIVE START -->
                <div id="web_live" >
                    <div class="row form-group">
                        <div class="col-md-6">
                            <label for="street">Event Name/Description (English)<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter Event name and description" type="text" name="event_name_eng" id="event_name_eng" maxlength="100"  aria-required="true">
                            <font style="color:red"><span id="event_name_eng_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street">Event Name/Description (Hindi)<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter Event name and description " type="text" name="event_name_hin" id="event_name_hin" maxlength="100"  aria-required="true">
                            <font style="color:red"><span id="event_name_hin_err"></span></font>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-6">
                            <label for="street">Date and timings of Event<span style="color: red">*</span></label>
                            <div class="row form-group">
                                <div class='col-md-6'>
                                    <div class="input-group date">
                                        <input type="text" class="form-control" placeholder="Start Date & Time" id="event_start_date1" name="event_start_date" readonly>
                                        <div class="input-group-append">
                                            <span class="input-group-text">
                                                <i class="la la-clock-o glyphicon-th"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div class='col-md-6'>
                                    <div class="input-group date">
                                        <input type="text" class="form-control" placeholder="End Date & Time" name="event_end_date" id="event_end_date1" readonly>
                                        <div class="input-group-append">
                                            <span class="input-group-text">
                                                <i class="la la-clock-o glyphicon-th"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <font style="color:red"><span id="event_date_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street">Type of Event<span style="color: red">*</span></label>
                            <select name="event_type" class="form-control" id="event_type">
                                <option value="International">International</option>
                                <option value="National">National</option>
                                <option value="State Government">State Government</option>                                                                                        
                                <option value="Others">Others</option>
                            </select>
                            <font style="color:red"><span id="event_type_err"></span></font>
                        </div>
                    </div>

                    <div class="form-group row"> 
                        <div class="col-md-6">
                            <label for="street">Event Coordinator Name <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Name [characters,dot(.) and whitespace]" type="text" name="event_coo_name" id="event_coo_name"  maxlength="50" value="">
                            <font style="color:red"><span id="event_coo_name_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street">Event Coordinator Email <span style="color: red">*</span></label>
                            <input class="form-control" style="text-transform:lowercase;" placeholder="Enter E-Mail Address [e.g: abc.xyz@zxc.com]" type="text" name="event_coo_email" id="event_coo_email"   maxlength="50"  value=""  >
                            <font style="color:red"><span id="event_coo_email_err"></span></font>
                        </div>                                                                                                        
                    </div>
                    <div class="form-group row">
                        <div class="col-md-6">
                            <label>Event Coordinator Designation <span style="color: red">*</span></label>
                            <input type="text" id="event_coo_design" name="event_coo_design" placeholder="Enter Designation [characters,digits,whitespace and [. , - &]]" maxlength="50" class="form-control"  value="" /> 
                            <font style="color:red"><span id="event_coo_desig_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street">Event Coordinator Mobile <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Mobile Number [e.g: +919999999999]" type="text" name="event_coo_mobile" id="event_coo_mobile"  maxlength="15" value="" >
                            <font style="color:red"><span id="event_coo_mobile_err"></span></font>
                        </div>
                    </div>
                    <div class="form-group row">                                                   
                        <div class="col-md-12">
                            <label for="street">Event Coordinator Address <span style="color: red">*</span></label>
                            <input type="text" id="event_coo_address" name="event_coo_address" placeholder="Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" maxlength="50" class="form-control"  /> 
                            <font style="color:red"><span id="event_coo_address_err"></span></font>
                        </div>
                    </div>
                    <div class="row form-group" >
                        <div class="col-md-6">
                            <label for="street">Is live telecast will be available on DD (If Yes then please specify the channel name)<span style="color: red">*</span></label>
                            <div id="event_telecast">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="telecast" id="telecast_yes"  value="yes" checked=""> Yes
                                    <span></span>
                                </label>&emsp;&emsp;
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="telecast" id="telecast_no"  value="no" > No
                                    <span></span>
                                </label>
                            </div>
                            <font style="color:red"><span id="telecast_err"></span></font>
                        </div>
                        <div class="col-md-6" id="telecast_yes_div">
                            <label for="street">Channel Name<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter Channel name " type="text" name="channel_name" id="channel_name" maxlength="50"  aria-required="true">
                            <font style="color:red"><span id="channel_name_err"></span></font>
                        </div>
                        <div class="col-md-6 display-hide" id="telecast_no_div">
                            <label for="street">How Are you planning to get live audio/video feed ?<span style="color: red">*</span></label>
                            <select name="live_feed" class="form-control" id="live_feed">
                                <option value="Own Live encoding">Own Live encoding</option>
                                <option value="Through VC">Through VC</option>                                                                                        
                                <option value="Through Hired Agency">Through Hired Agency</option>                                                                                        
                                <option value="By other means">By other means</option>
                            </select>
                            <font style="color:red"><span id="live_feed_err"></span></font>
                        </div>
                    </div>
                    <div class="row display-hide" id="vcid_div">                                                                                
                        <div class="col-md-6">
                            <label for="street">VC ID<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter VC ID " type="text" name="vc_id" id="vc_id" maxlength="50"  aria-required="true">
                            <font style="color:red"><span id="vc_id_err"></span></font>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-6">
                            <label for="street">Is it a Conference/Workshop ?<span style="color: red">*</span></label>
                            <div id="conf_workshop">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="conf_radio" id="conf_radio_yes"  value="yes" checked=""> Conference
                                    <span></span>
                                </label>&emsp;&emsp;
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="conf_radio" id="conf_radio_no"  value="no" > Workshop
                                    <span></span>
                                </label>
                            </div>
                            <font style="color:red"><span id="conf_radio_err"></span></font>
                        </div>
                    </div>
                    <!-- WEBCAST CONFERENCE START -->                    
                    <div class="row form-group">
                        <div class="col-md-4">
                            <label for="street">Name of Conference/Workshop<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter Name of Conference/Workshop" type="text" name="conf_name" id="conf_name" maxlength="100"  aria-required="true">
                            <font style="color:red"><span id="conf_name_err"></span></font>
                        </div>
                        <div class="col-md-4">
                            <label for="street">Type of Conference/Workshop<span style="color: red">*</span></label>
                            <select name="conf_type" class="form-control" id="conf_type">
                                <option value="International">International </option>
                                <option value="National">National</option>
                                <option value="State Government">State Government</option>                                                                                        
                                <option value="Others">Others</option>
                            </select>
                            <font style="color:red"><span id="conf_type_err"></span></font>
                        </div>
                        <div class="col-md-4">
                            <label for="street">City and Venue of Conference/Workshop<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter City and Venue of Conference/Workshop" type="text" name="conf_city" id="conf_city" maxlength="50"  aria-required="true">
                            <font style="color:red"><span id="conf_city_err"></span></font>
                        </div>
                    </div>
                    <div class="row form-group">                        
                        <div class="col-md-6">
                            <label for="street">Conference/Workshop schedule /Program details with no. of days<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Conference/Workshop schedule /Program details with no. of days" type="text" name="conf_schedule" id="conf_schedule" maxlength="100"  aria-required="true">
                            <font style="color:red"><span id="conf_schedule_err"></span></font>
                        </div>
                        <div id="cert_div" class="col-md-6">                        
                            <label for="street">Uploaded Letter/Schedule/Agenda name: <span style="color: red">*</span></label>
                            <div class="alert alert-secondary" >
                                <b>Uploaded filename: </b>&emsp;
                                <div id="webcast_files"></div>
                            </div>                                                                                    
                        </div>
                    </div>
                    <div class="row form-group" >
                        <div class="col-md-6">
                            <label for="street">Number of parallel sessions<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter Number of parallel sessions [Only digits]" type="text" name="conf_session" id="conf_session" maxlength="2"  aria-required="true">
                            <font style="color:red"><span id="conf_session_err"></span></font>
                        </div>
                        <div class="col-md-6" id="hall_div">
                            <label for="street">Number of Hall<span style="color: red">*</span></label>
                            <div class="row form-group">
                                <div class="col-md-6 mt-2">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="hall_type" id="hall_multiple"  value="multiple" checked=""> Multiple
                                        <span></span>
                                    </label>&emsp;&emsp;
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="hall_type" id="hall_single"  value="single" > Single
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-6">
                                    <input class="form-control" value="" placeholder="Enter Number of Halls e.g. 1/2/3" type="text" name="hall_number" id="hall_number" maxlength="3"  aria-required="true">
                                </div>
                            </div>
                            <font style="color:red"><span id="hall_type_err"></span></font>
                        </div>                                                                                    
                    </div>
                    <div class="row form-group" >                                                                                    
                        <div class="col-md-6" >
                            <label for="street">Internet Connectivity /Leased line (Minimum 2 Mbps for each live stream/session)<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Type and capacity in terms of bandwidth " type="text" name="conf_bw" id="conf_bw" maxlength="50"  aria-required="true">
                            <font style="color:red"><span id="conf_bw_err"></span></font>
                        </div>      
                        <div class="col-md-6">
                            <label for="street">Internet/Leased line Service provider Name<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter Internet/Leased line Service provider Name" type="text" name="conf_provider" id="conf_provider" maxlength="100"  aria-required="true">
                            <font style="color:red"><span id="conf_provider_err"></span></font>
                        </div>
                    </div>
                    <div class="row form-group" >                                                                                    
                        <div class="col-md-6">
                            <label for="street">Event Management company hired ?<span style="color: red">*</span></label>
                            <div>
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="conf_event_hired" id="conf_event_hired_yes"  value="yes" checked=""> Yes
                                    <span></span>
                                </label>&emsp;&emsp;
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="conf_event_hired" id="conf_event_hired_no"  value="no" > No
                                    <span></span>
                                </label>
                            </div>
                            <font style="color:red"><span id="conf_event_hired_err"></span></font>
                        </div>
                        <div class="col-md-6" id="flash_media_encoder">
                            <label for="street">Do this agency able to stream live using Flash Live Media Encoder<span style="color: red">*</span></label>
                            <div id="conf_flash_div">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="conf_flash" id="conf_flash_yes"  value="yes" checked=""> Yes
                                    <span></span>
                                </label>&emsp;&emsp;
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="conf_flash" id="conf_flash_no"  value="no" > No
                                    <span></span>
                                </label>
                                <div id="conf_flash_no_div" class="display-hide">
                                    <input class="form-control " value="" placeholder="Enter local setup" type="text" name="local_setup" id="local_setup" maxlength="50"  aria-required="true">
                                </div>
                            </div>
                            <font style="color:red"><span id="conf_flash_err"></span></font>
                        </div>
                    </div>
                    <div class="row form-group">                                                                                    
                        <div class="col-md-6">
                            <label for="street">Video coverage agency hired or not<span style="color: red">*</span></label>
                            <div  id="conf_video_div">
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="conf_video" id="conf_video_yes"  value="yes" checked=""> Yes
                                    <span></span>
                                </label>&emsp;&emsp;
                                <label class="k-radio k-radio--bold k-radio--brand">
                                    <input type="radio" name="conf_video" id="conf_video_no"  value="no" > No
                                    <span></span>
                                </label>
                            </div>
                            <font style="color:red"><span id="conf_video_err"></span></font>
                        </div>
                        <div class="col-md-6" id="conf_contact_div">
                            <label for="street">Contact details of video agency<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter contact details of video agency" type="text" name="conf_contact" id="conf_contact" maxlength="100"  aria-required="true">
                            <font style="color:red"><span id="conf_contact_err"></span></font>
                        </div>  
                    </div>
                    <div class="row alert alert-secondary" style="border: 2px solid #ddd;">
                        <b>Duration of on-demand availability : NIC shall host the VoD/Live webcast recordings for 01 year and thereafter will be removed or handed over to the owner/Ministry /Department on request.</b>
                    </div>

                    <!-- WEBCAST CONFERENCE END -->
                </div>
                <!-- WEBCAST LIVE END -->
                <!-- WEBCAST DEMAND START -->
                <div id="web_demand" class="display-hide">
                    <div class="row form-group">
                        <div class="col-md-4">
                            <label for="street">Total number of video clips <span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter Total number of video clips" type="text" name="event_no" id="event_no" maxlength="50"  aria-required="true">
                            <font style="color:red"><span id="event_no_err"></span></font>
                        </div>
                        <div class="col-md-4">
                            <label for="street">Total size in GB<span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter Total size in GB" type="text" name="event_size" id="event_size" maxlength="50"  aria-required="true">
                            <font style="color:red"><span id="event_size_err"></span></font>
                        </div>
                        <div class="col-md-4">
                            <label for="street">Media Format provided<span style="color: red">*</span></label>
                            <select name="media_format" class="form-control" id="media_format">
                                <option value="MP4">MP4</option>
                                <option value="MPEG">MPEG</option>
                                <option value="FLV">FLV</option>                                                                                        
                                <option value="F4V">F4V</option>
                            </select>
                            <font style="color:red"><span id="media_format_err"></span></font>
                        </div>
                    </div>
                    <div class="row alert alert-secondary" style="border: 2px solid #ddd;">
                        <b>Duration of on-demand availability : NIC shall host the VoD/Live webcast recordings for 01 year and thereafter will be removed or handed over to the owner/Ministry /Department on request.</b>
                    </div>
                </div>
                <!-- WEBCAST DEMAND END -->
                <div class="row form-group">
                    <div class="col-md-12">
                        <label for="street">Payment details applicable or not? <span style="color: red">*</span></label>
                        <div id="payment_applicable_div">
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="payment" id="payment_yes"  value="yes" checked=""> Yes
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="payment" id="payment_no"  value="no" > No
                                <span></span>
                            </label>
                        </div>
                        <font style="color:red"><span id="payment_err"></span></font>
                    </div>
                </div>
                <div id="payment_div">
                    <div class="row form-group">
                        <div class="col-md-4">
                            <label for="street">Cheque/DD No/NEFT No/RTGS No</label>
                            <input class="form-control" value="" placeholder="Enter Cheque/DD No/NEFT No/RTGS No" type="text" name="cheque_no" id="cheque_no" maxlength="30"  aria-required="true">
                            <font style="color:red"><span id="cheque_no_err"></span></font>
                        </div>
                        <div class="col-md-4">
                            <label for="street">Amount </label>
                            <input class="form-control" value="" placeholder="Enter Amount" type="text" name="cheque_amount" id="cheque_amount" maxlength="50"  aria-required="true">
                            <font style="color:red"><span id="amount_err"></span></font>
                        </div>
                        <div class="col-md-4">
                            <label for="street">Date </label>
                            <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="cheque_date" id="cheque_date1" placeholder="Enter Date [DD-MM-YYYY]" readonly/>
                            <font style="color:red"><span id="cheque_date_err"></span></font>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-12">
                            <label for="street">Bank & Branch </label>
                            <input class="form-control" value="" placeholder="Enter Bank & Branch" type="text" name="bank_name" id="bank_name" maxlength="100"  aria-required="true">
                            <font style="color:red"><span id="bank_name_err"></span></font>
                        </div>                                                                                
                    </div>
                </div>

                <div class="row form-group">
                    <div class="col-md-12">
                        <label for="street">Any other details or remarks</label>
                        <input class="form-control" value="" placeholder="Enter any other details or remarks" type="text" name="remarks" id="remarks" maxlength="100"  aria-required="true">
                        <font style="color:red"><span id="remarks_err"></span></font>
                    </div>                

                </div>
                <div class="form-group mt-4" id='tnc_div'>
                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand mt-5">
                        <input type="checkbox"  name="tnc" id="tnc"> <b>I agree to <a data-toggle="modal" href="#stack2" style="color:blue">Terms and Conditions</a></b>
                        <span></span>
                    </label>
                    <font style="color:red"><span id="tnc_error"></span></font>
                </div>

            </div>
        </div>
        <div class="modal-footer">
            <font style="color:red"><span id="webcast_error"></span></font>
            <button type="button" class="btn dark btn-default" data-dismiss="modal" id="closebtn">Close</button>
            <a href="javascript:void(0);" class="btn btn-warning edit " >Edit</a>
            <button type="button" class="btn red btn-primary save-changes" id="confirm">Submit</button>
        </div> 
    </div>
</div>