<%-- 
    Document   : Mobile_registration
    Created on : 4 Dec, 2017, 1:42:40 PM
    Author     : dhiren
--%>
<%@page import="java.util.Map"%>
<%@page import="com.org.dao.Ldap"%>
<%@page import="com.org.bean.UserData"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.org.utility.Random"%>

<!DOCTYPE html>
<%
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Expires", "0");
    response.setDateHeader("Expires", -1);
    response.setHeader("X-Frame-Options", "DENY");
    response.addHeader("X-Content-Type-Options", "nosniff");
    response.addHeader("X-XSS-Protection", "1; mode=block");
%>
<%
    Ldap ldap = new Ldap();
    String random = new Random().csrf_random();
    session.setAttribute("rand", random);
    String CSRFRandom = new Random().csrf_random();
    session.setAttribute("CSRFRandom", CSRFRandom);
    UserData userdata = (UserData) session.getAttribute("uservalues");
    boolean nic_employee = userdata.isIsNICEmployee();
    boolean ldap_employee = userdata.isIsEmailValidated();
    String mobile = ldap.fetchMobileFromLdap(userdata.getEmail());
    
    Map<String,Object> map=ldap.fetchAttrUpdateMobile(userdata.getEmail());
    String nicDateOfBirth=map.get("nicDateOfBirth").toString();
    String nicDateOfRetirement=map.get("nicDateOfRetirement").toString();
    String designation=map.get("designation").toString();
    String displayName=map.get("displayName").toString();
    
    
    String newMobile = "";
    if (session.getAttribute("update_without_oldmobile").equals("yes")) {
        newMobile = session.getAttribute("mobileNumber").toString();

    }


%>
<script>
    var mobVal = '';
</script>
<jsp:include page="include/new_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<jsp:include page="include/new_include/header.jsp" />
<div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
    <div class="k-portlet k-portlet--mobile" style="height: 100%;">
        <div class="form-wizard portlet light-graph">
            <div class="form-body">
                <div class="tab-content">
                    <div class="tab-pane active" id="tab2" >
                        <form action="" method="post" id="mobile_form1" class="form_val" >
                            <div class="k-portlet__head">
                                <div class="k-portlet__head-label">
                                    <h3 class="k-portlet__head-title">Mobile Update</h3>
                                </div>
                            </div>
                            <div class="col-md-12 mt-5 mb-4" style="padding-left: 17px;margin-top: 17px;">
                                <div class="alert alert-secondary">
                                    <div class="col-md-12">
                                        <b>NOTE: Please read all instructions carefully.</b>
                                        <ul style="padding-left: 14px;margin-top: 22px;">                                                                                        
                                            <li><b>You need to have a valid email address on NIC platform to proceed.</b></li>
                                            <li>Users are requested to ensure they are coming from secured devices and network with all recommended Antivirus & latest patches installed</li>
                                            <li>NIC ensures to provide a secured environment for all the users with utmost priority to prevent any data breach or loss , However NIC does not hold any responsibility in case of any data loss.</li>
                                            <li>User hold complete responsibility of his/her data , incase of any accidental loss NIC will not be able to retrieve the same.</li>
                                            <li>Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to <b>servicedesk@nic.in</b></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-12">

                                <p style="font-weight: bold;">Your mobile number in our portal is: <u id="mobile_txt_val"><%= mobile%></u></p>

                            </div>
                            <div class="col-md-12">
                                <div id="profileError"></div>
                                <div class="row form-group">

                                    <% if (session.getAttribute("update_without_oldmobile").equals("no")) {%>


                                    <div class="col-md-3">
                                        <label  for="street">Country Code  <span style="color: red">*</span></label>
                                        <select class="form-control" name="country_code" id="country_code"  >
                                            <option value="+213">Algeria (+213)</option> 
                                            <option value="+376">Andorra (+376)</option> 
                                            <option value="+244">Angola (+244)</option> 
                                            <option value="+1264">Anguilla (+1264)</option> 
                                            <option value="+1268">Antigua and Barbuda (+1268)</option> 
                                            <option value="+599">Antilles (Dutch) (+599)</option> 
                                            <option value="+54">Argentina (+54)</option> 
                                            <option value="+374">Armenia (+374)</option> 
                                            <option value="+297">Aruba (+297)</option> 
                                            <option value="+247">Ascension Island (+247)</option> 
                                            <option value="+61">Australia (+61)</option> 
                                            <option value="+43">Austria (+43)</option> 
                                            <option value="+994">Azerbaijan (+994)</option> 
                                            <option value="+1242">Bahamas (+1242)</option> 
                                            <option value="+973">Bahrain (+973)</option> 
                                            <option value="+880">Bangladesh (+880)</option> 
                                            <option value="+1246">Barbados (+1246)</option> 
                                            <option value="+375">Belarus (+375)</option> 
                                            <option value="+32">Belgium (+32)</option> 
                                            <option value="+501">Belize (+501)</option> 
                                            <option value="+229">Benin (+229)</option> 
                                            <option value="+1441">Bermuda (+1441)</option> 
                                            <option value="+975">Bhutan (+975)</option> 
                                            <option value="+591">Bolivia (+591)</option> 
                                            <option value="+387">Bosnia Herzegovina (+387)</option> 
                                            <option value="+267">Botswana (+267)</option> 
                                            <option value="+55">Brazil (+55)</option> 
                                            <option value="+673">Brunei (+673)</option> 
                                            <option value="+359">Bulgaria (+359)</option> 
                                            <option value="+226">Burkina Faso (+226)</option> 
                                            <option value="+257">Burundi (+257)</option> 
                                            <option value="+855">Cambodia (+855)</option> 
                                            <option value="+237">Cameroon (+237)</option> 
                                            <option value="+1">Canada (+1)</option> 
                                            <option value="+238">Cape Verde Islands (+238)</option> 
                                            <option value="+1345">Cayman Islands (+1345)</option> 
                                            <option value="+236">Central African Republic (+236)</option> 
                                            <option value="+56">Chile (+56)</option> 
                                            <option value="+86">China (+86)</option> 
                                            <option value="+57">Colombia (+57)</option> 
                                            <option value="+269">Comoros (+269)</option> 
                                            <option value="+242">Congo (+242)</option> 
                                            <option value="+682">Cook Islands (+682)</option> 
                                            <option value="+506">Costa Rica (+506)</option> 
                                            <option value="+385">Croatia (+385)</option> 
                                            <option value="+53">Cuba (+53)</option> 
                                            <option value="+90392">Cyprus North (+90392)</option> 
                                            <option value="+357">Cyprus South (+357)</option> 
                                            <option value="+42">Czech Republic (+42)</option> 
                                            <option value="+45">Denmark (+45)</option> 
                                            <option value="+2463">Diego Garcia (+2463)</option> 
                                            <option value="+253">Djibouti (+253)</option> 
                                            <option value="+1809">Dominica (+1809)</option> 
                                            <option value="+1809">Dominican Republic (+1809)</option> 
                                            <option value="+593">Ecuador (+593)</option> 
                                            <option value="+20">Egypt (+20)</option> 
                                            <option value="+353">Eire (+353)</option> 
                                            <option value="+503">El Salvador (+503)</option> 
                                            <option value="+240">Equatorial Guinea (+240)</option> 
                                            <option value="+291">Eritrea (+291)</option> 
                                            <option value="+372">Estonia (+372)</option> 
                                            <option value="+251">Ethiopia (+251)</option> 
                                            <option value="+500">Falkland Islands (+500)</option> 
                                            <option value="+298">Faroe Islands (+298)</option> 
                                            <option value="+679">Fiji (+679)</option> 
                                            <option value="+358">Finland (+358)</option> 
                                            <option value="+33">France (+33)</option> 
                                            <option value="+594">French Guiana (+594)</option> 
                                            <option value="+689">French Polynesia (+689)</option> 
                                            <option value="+241">Gabon (+241)</option> 
                                            <option value="+220">Gambia (+220)</option> 
                                            <option value="+7880">Georgia (+7880)</option> 
                                            <option value="+49">Germany (+49)</option> 
                                            <option value="+233">Ghana (+233)</option> 
                                            <option value="+350">Gibraltar (+350)</option> 
                                            <option value="+30">Greece (+30)</option> 
                                            <option value="+299">Greenland (+299)</option> 
                                            <option value="+1473">Grenada (+1473)</option> 
                                            <option value="+590">Guadeloupe (+590)</option> 
                                            <option value="+671">Guam (+671)</option> 
                                            <option value="+502">Guatemala (+502)</option> 
                                            <option value="+224">Guinea (+224)</option> 
                                            <option value="+245">Guinea - Bissau (+245)</option> 
                                            <option value="+592">Guyana (+592)</option> 
                                            <option value="+509">Haiti (+509)</option> 
                                            <option value="+504">Honduras (+504)</option> 
                                            <option value="+852">Hong Kong (+852)</option> 
                                            <option value="+36">Hungary (+36)</option> 
                                            <option value="+354">Iceland (+354)</option> 
                                            <option value="+91" selected>India (+91)</option> 
                                            <option value="+62">Indonesia (+62)</option> 
                                            <option value="+98">Iran (+98)</option> 
                                            <option value="+964">Iraq (+964)</option> 
                                            <option value="+972">Israel (+972)</option> 
                                            <option value="+39">Italy (+39)</option> 
                                            <option value="+225">Ivory Coast (+225)</option> 
                                            <option value="+1876">Jamaica (+1876)</option> 
                                            <option value="+81">Japan (+81)</option> 
                                            <option value="+962">Jordan (+962)</option> 
                                            <option value="+7">Kazakhstan (+7)</option> 
                                            <option value="+254">Kenya (+254)</option> 
                                            <option value="+686">Kiribati (+686)</option> 
                                            <option value="+850">Korea North (+850)</option> 
                                            <option value="+82">Korea South (+82)</option> 
                                            <option value="+965">Kuwait (+965)</option> 
                                            <option value="+996">Kyrgyzstan (+996)</option> 
                                            <option value="+856">Laos (+856)</option> 
                                            <option value="+371">Latvia (+371)</option> 
                                            <option value="+961">Lebanon (+961)</option> 
                                            <option value="+266">Lesotho (+266)</option> 
                                            <option value="+231">Liberia (+231)</option> 
                                            <option value="+218">Libya (+218)</option> 
                                            <option value="+417">Liechtenstein (+417)</option> 
                                            <option value="+370">Lithuania (+370)</option> 
                                            <option value="+352">Luxembourg (+352)</option> 
                                            <option value="+853">Macao (+853)</option> 
                                            <option value="+389">Macedonia (+389)</option> 
                                            <option value="+261">Madagascar (+261)</option> 
                                            <option value="+265">Malawi (+265)</option> 
                                            <option value="+60">Malaysia (+60)</option> 
                                            <option value="+960">Maldives (+960)</option> 
                                            <option value="+223">Mali (+223)</option> 
                                            <option value="+356">Malta (+356)</option> 
                                            <option value="+692">Marshall Islands (+692)</option> 
                                            <option value="+596">Martinique (+596)</option> 
                                            <option value="+222">Mauritania (+222)</option>
                                            <option value="+230">Mauritius (+230)</option> 
                                            <option value="+269">Mayotte (+269)</option> 
                                            <option value="+52">Mexico (+52)</option> 
                                            <option value="+691">Micronesia (+691)</option> 
                                            <option value="+373">Moldova (+373)</option> 
                                            <option value="+377">Monaco (+377)</option> 
                                            <option value="+976">Mongolia (+976)</option> 
                                            <option value="+1664">Montserrat (+1664)</option> 
                                            <option value="+212">Morocco (+212)</option> 
                                            <option value="+258">Mozambique (+258)</option> 
                                            <option value="+95">Myanmar (+95)</option> 
                                            <option value="+264">Namibia (+264)</option> 
                                            <option value="+674">Nauru (+674)</option> 
                                            <option value="+977">Nepal (+977)</option> 
                                            <option value="+31">Netherlands (+31)</option> 
                                            <option value="+687">New Caledonia (+687)</option> 
                                            <option value="+64">New Zealand (+64)</option> 
                                            <option value="+505">Nicaragua (+505)</option> 
                                            <option value="+227">Niger (+227)</option> 
                                            <option value="+234">Nigeria (+234)</option> 
                                            <option value="+683">Niue (+683)</option> 
                                            <option value="+672">Norfolk Islands (+672)</option> 
                                            <option value="+670">Northern Marianas (+670)</option> 
                                            <option value="+47">Norway (+47)</option> 
                                            <option value="+968">Oman (+968)</option> 
                                            <option value="+92">Pakistan (+92)</option> 
                                            <option value="+680">Palau (+680)</option> 
                                            <option value="+507">Panama (+507)</option> 
                                            <option value="+675">Papua New Guinea (+675)</option> 
                                            <option value="+595">Paraguay (+595)</option> 
                                            <option value="+51">Peru (+51)</option> 
                                            <option value="+63">Philippines (+63)</option> 
                                            <option value="+48">Poland (+48)</option> 
                                            <option value="+351">Portugal (+351)</option> 
                                            <option value="+1787">Puerto Rico (+1787)</option> 
                                            <option value="+974">Qatar (+974)</option> 
                                            <option value="+262">Reunion (+262)</option> 
                                            <option value="+40">Romania (+40)</option> 
                                            <option value="+7">Russia (+7)</option> 
                                            <option value="+250">Rwanda (+250)</option> 
                                            <option value="+378">San Marino (+378)</option> 
                                            <option value="+239">Sao Tome and Principe (+239)</option> 
                                            <option value="+966">Saudi Arabia (+966)</option> 
                                            <option value="+221">Senegal (+221)</option> 
                                            <option value="+381">Serbia (+381)</option> 
                                            <option value="+248">Seychelles (+248)</option> 
                                            <option value="+232">Sierra Leone (+232)</option> 
                                            <option value="+65">Singapore (+65)</option> 
                                            <option value="+421">Slovak Republic (+421)</option> 
                                            <option value="+386">Slovenia (+386)</option> 
                                            <option value="+677">Solomon Islands (+677)</option> 
                                            <option value="+252">Somalia (+252)</option> 
                                            <option value="+27">South Africa (+27)</option> 
                                            <option value="+34">Spain (+34)</option> 
                                            <option value="+94">Sri Lanka (+94)</option> 
                                            <option value="+290">St. Helena (+290)</option> 
                                            <option value="+1869">St. Kitts (+1869)</option> 
                                            <option value="+1758">St. Lucia (+1758)</option> 
                                            <option value="+249">Sudan (+249)</option> 
                                            <option value="+597">Suriname (+597)</option> 
                                            <option value="+268">Swaziland (+268)</option> 
                                            <option value="+46">Sweden (+46)</option> 
                                            <option value="+41">Switzerland (+41)</option> 
                                            <option value="+963">Syria (+963)</option> 
                                            <option value="+886">Taiwan (+886)</option> 
                                            <option value="+7">Tajikstan (+7)</option> 
                                            <option value="+66">Thailand (+66)</option> 
                                            <option value="+228">Togo (+228)</option> 
                                            <option value="+676">Tonga (+676)</option> 
                                            <option value="+1868">Trinidad and Tobago (+1868)</option> 
                                            <option value="+216">Tunisia (+216)</option> 
                                            <option value="+90">Turkey (+90)</option> 
                                            <option value="+7">Turkmenistan (+7)</option> 
                                            <option value="+993">Turkmenistan (+993)</option> 
                                            <option value="+1649">Turks and Caicos Islands (+1649)</option> 
                                            <option value="+688">Tuvalu (+688)</option> 
                                            <option value="+256">Uganda (+256)</option> 
                                            <option value="+44" >UK (+44)</option> 
                                            <option value="+380">Ukraine (+380)</option> 
                                            <option value="+971">United Arab Emirates (+971)</option> 
                                            <option value="+598">Uruguay (+598)</option> 
                                            <option value="+1">USA (+1)</option> 
                                            <option value="+7">Uzbekistan (+7)</option> 
                                            <option value="+678">Vanuatu (+678)</option> 
                                            <option value="+379">Vatican City (+379)</option> 
                                            <option value="+58">Venezuela (+58)</option> 
                                            <option value="+84">Vietnam (+84)</option> 
                                            <option value="+84">Virgin Islands - British (+1284)</option> 
                                            <option value="+84">Virgin Islands - US (+1340)</option> 
                                            <option value="+681">Wallis and Futuna (+681)</option> 
                                            <option value="+969">Yemen (North)(+969)</option> 
                                            <option value="+967">Yemen (South)(+967)</option> 
                                            <option value="+381">Yugoslavia (+381)</option> 
                                            <option value="+243">Zaire (+243)</option> 
                                            <option value="+260">Zambia (+260)</option> 
                                            <option value="+263">Zimbabwe (+263)</option>
                                        </select>
                                        <font style="color:red"><span id="country_code_err"></span></font>
                                    </div>

                                    <% }%>

                                    <div class="col-md-3">
                                        <label  for="street">New Mobile Number<span style="color: red">*</span></label>
                                        <% if (session.getAttribute("update_without_oldmobile").equals("no")) {%>
                                        <input class="form-control" placeholder="Enter the Mobile Number 10 digits for india [e.g.: 9999999999 ] OR [8-12] digits for international" type="text" name="new_mobile" id="new_mobile"  value="" maxlength="15">
                                        <% } else {%>

                                        <input class="form-control" placeholder="Enter the Mobile Number 10 digits for india [e.g.: 9999999999 ] OR [8-12] digits for international" type="text" name="new_mobile" id="new_mobile"  value="<%= newMobile%>" maxlength="15" readonly="readonly">
                                       

                                        <% } %>       <font style="color:red"><span id="mobile_err"></span></font><font style="color:red"><span id="mobile_request_error"></span></font>



                                        <font style="color:red"><span id="mobile_err"></span></font>
                                        <font style="color:red"><span id="mobile_req_pending_error"></span></font>

                                    </div>
                                    <div class="col-md-2">
                                        <label  for="street">Date of Birth<span style="color: red">*</span></label>
                                        <% if (session.getAttribute("update_without_oldmobile").equals("no")) {%>
                                         <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="nicDateOfBirth" id="nicDateOfBirth" value="<%= nicDateOfBirth%>" placeholder="Enter Date Of Birth [DD-MM-YYYY]"/>
                                   
                                        <% } else {%>

                                     
                                       <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="nicDateOfBirth" id="nicDateOfBirth" value="<%= nicDateOfBirth%>" placeholder="Enter Date Of Birth [DD-MM-YYYY]"/>


                                        <% } %>       <font style="color:red"><span id="nicDateOfBirth_err"></span></font><font style="color:red"><span id="nicDateOfBirth_error"></span></font>



                                        <font style="color:red"><span id="mobile_err"></span></font>
                                        <font style="color:red"><span id="mobile_req_pending_error"></span></font>

                                    </div>
                                    <div class="col-md-2">
                                        <label  for="street">Date of Retirement<span style="color: red">*</span></label>
                                        <% if (session.getAttribute("update_without_oldmobile").equals("no")) {%>
                                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="nicDateOfRetirement" id="nicDateOfRetirement" value="<%= nicDateOfRetirement%>" placeholder="Enter Date Of Retirement [DD-MM-YYYY]"/>
                                      
                                        <% } else {%>

                                        
                                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="nicDateOfRetirement" id="nicDateOfRetirement" value="<%= nicDateOfRetirement%>" placeholder="Enter Date Of Retirement [DD-MM-YYYY]"/>
                                        

                                        <% } %>       <font style="color:red"><span id="nicDateOfRetirement_err"></span></font><font style="color:red"><span id="nicDateOfRetirement_error"></span></font>



                                        <font style="color:red"><span id="nicDateOfRetirement_err"></span></font>
                                       

                                    </div>
                                    <div class="col-md-2">
                                        <label  for="street">Designation<span style="color: red">*</span></label>
                                        <% if (session.getAttribute("update_without_oldmobile").equals("no")) {%>
                                       <input class="form-control" placeholder="Enter Your Designation" type="text" name="designation" id="designation"  value="<%= designation%>">
                                        <% } else {%>

                                        <input class="form-control" placeholder="Enter Your Designation" type="text" name="designation" id="designation"  value="<%= designation%>">

                                        <% } %>       <font style="color:red"><span id="designation_err"></span></font><font style="color:red"><span id="designation_error"></span></font>



                                       

                                    </div>
                                    <div class="col-md-3">
                                        <label  for="street">Display name<span style="color: red">*</span></label>
                                        <% if (session.getAttribute("update_without_oldmobile").equals("no")) {%>
                                         <input class="form-control" placeholder="Enter Your Name" type="text" name="displayName" id="displayName"  value="<%= displayName%>">
                                        <% } else {%>

                                        
                                        <input class="form-control" placeholder="Enter Your Name" type="text" name="displayName" id="displayName"  value="<%= displayName%>">

                                        <% } %>       <font style="color:red"><span id="displayName_err"></span></font><font style="color:red"><span id="displayName_error"></span></font>



                                        <font style="color:red"><span id="displayName_err"></span></font>
                                        

                                    </div>
                                    <% if (session.getAttribute("update_without_oldmobile").equals("no")) {%>
                                    <div class="col-md-4 display-hide" id="new_otp_div">
                                        <label  for="street">Enter Otp code which is sent on Update mobile number.<span style="color: red">*</span></label>
                                        <input class="form-control" style="text-transform:initial;" id="opt_detector" name="newcode" placeholder="Enter OTP" type="text" maxlength="6" value=""> 
                                        <font style="color:red"><span id="otperror"></span></font>
                                    </div>
                                    <% }%>
                                </div>
                                <div class="row form-group">
                                    <div class="col-md-6">
                                        <label  for="street">Reason  <span style="color: red">*</span></label>
                                        <select  name="remarks" theme="simple" class="form-control selectReason">
                                            <option value="" selected>-- Select Reason --</option> 
                                            <option value="I have forgotten my old number">I have forgotten my old number</option>
                                            <option value="I am not using the old number any more">I am not using the old number any more</option>
                                            <option value="I have lost my old number">I have lost my old number</option>
                                            <option value="I want to discard lost number">I want to discard lost number</option>
                                            <option value="other">Other</option>
                                        </select>
                                        <font style="color:red"><span id="remarks_error"></span></font>
                                    </div>
                                    <div class="col-md-6 display-hide" id="remarks_div">
                                        <label  for="street">Remarks<span style="color: red">*</span></label>
                                        <input class="form-control" style="text-transform:initial;" id="reason" name="other_remarks" placeholder="Enter Reason to update mobile" type="text" value=""> 
                                        <font style="color:red"><span id="remarks_other_error"></span></font>
                                    </div>
                                </div>

                                <!--row end-->
                                <div class="col-md-12 mt-4">
                                    <div class="row">
                                        <div class="col-md-6 mt-4" style="text-align: right;">
                                            <br/><label  for="street">Captcha</label>
                                            <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                            <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                        </div>



                                        <div class="col-md-2 mt-4">
                                            <div class="form-group">
                                                <label  for="street">Enter Captcha<span style="color: red">*</span></label>
                                                <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="imgtxt" maxlength="6" value=""> 
                                                <font style="color:red"><span id="captchaerror"></span></font>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row" style="padding:10px;">
                                    <div class="col-md-offset-11 col-md-1">
                                    </div>
                                </div>
                                <font style="color:red"><span id="request_err"></span></font>
                                <div class="col-md-12 text-center mt-5">
                                    <!-- below line added by pr on 22ndjan18 to implement CSRF  -->  
                                    <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                    <!--<input type="hidden" name="CSRFRandom" value="1" id="CSRFRandom" />-->
                                    <button name="submit" value="preview" class="btn purple btn-success sbold" > Preview and Submit </button>                                                                
                                </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<jsp:include page="include/new_include/footer.jsp" />
<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="mobile_form2" method="post">
        <jsp:include page="include/mobile_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<!-- /.modal added by sahil parasher -->

<!--Nested Modal -->
<div class="modal fade" id="stack2" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Terms and conditions</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">                           
                <b>NOTE: Please read all instructions carefully.</b>
                <br/>
                <ol>                                                                                        
                    <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>
                    <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                    <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                    <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>
                    <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.</li>
                    <li>In case of bulk Mobile update please provide the list of email accounts in excel sheet with fields - Email Address, Mobile Number.</li>
                    <li>NIC does not capture any aadhaar related information.</li>
                    <li>Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to <b>servicedesk@nic.in</b></li>
                </ol>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!--Nested Modal -->
<div class="modal fade" id="stack2" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Terms and conditions</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">                           
                <b>NOTE: Please read all instructions carefully.</b>
                <br/>
                <ol>                                                                                        
                    <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>
                    <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                    <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                    <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>
                    <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.</li>
                    <li>In case of bulk Mobile update please provide the list of email accounts in excel sheet with fields - Email Address, Mobile Number.</li>
                    <li>NIC does not capture any aadhaar related information.</li>
                    <li>Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to <b>servicedesk@nic.in</b></li>
                </ol>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- Nested Modal-->
<!-- Modal for last submission -->
<div class="modal fade" id="stack3" tabindex="-1">
    <form id="mobile_form_confirm">
        <%

            if (!nic_employee) {%>
        <jsp:include page="include/Hod_detail.jsp" />
        <% } else {%>
        <jsp:include page="include/wifi_detail.jsp" />
        <%}%>
    </form>
</div>


<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/mobile.js" type="text/javascript"></script>
<script type="text/javascript">
    jQuery(document).ready(function () {
        $("#refresh").on("click", function () {
            $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#closebtn").on("click", function () {
            $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#imgtxt').val("");
        });
    });
</script>
<script>
    $(document).ready(function () {
        if ('<%=nic_employee%>' !== null) {

            if ('<%=nic_employee%>' === 'true') {
                var hod_email = '<s:property value="#session['profile-values'].hod_email" />';
                if (hod_email === null || hod_email === "") {

                } else {
                    $("input[name='hod_email']").prop('readonly', true);
                }
            }
        }
    });
</script>
<script>
    $(document).ready(function () {

        if ('<%=session.getAttribute("resend_request")%>' == "true") {
            var form_name = '<s:property value="#session['prvwdetails'].form_name" />';
            if (form_name == "MOBILE")
            {
                var country_code = '<s:property value="#session['prvwdetails'].country_code" />';
                var new_mobile = '<s:property value="#session['prvwdetails'].new_mobile" />';
                $('#country_code').val(country_code);
                $('#new_mobile').val(new_mobile);
            }

        }

    });
    
    
     $(document).on("focusin", "#nicDateOfRetirement", function () {

        console.log("sahil dateee");
       

        $("#nicDateOfRetirement").datepicker({
            // start, code added by pr on 21stjan19
            beforeShow: function (input, inst) {
                $(document).off("focusin.bs.modal");
            },
            onClose: function () {
                $(document).on("focusin.bs.modal");
            },
            // end, code added by pr on 21stjan19
            dateFormat: "dd-mm-yy",
            changeMonth: true,
            changeYear: true,
            //yearRange: "+0:+48", 
            yearRange: "+0:+49", // line modified by pr on 2ndaug18 to have dor as 67
            minDate: "+1D",
            maxDate: "+50Y"
        });
    });
    
    
    $(document).on("focusin", "#nicDateOfBirth", function () {
          var dt = new Date;
    //var yrg = dt.getFullYear() - 66;

    var yrg = dt.getFullYear() - 67; // line modified by pr on 2ndaug18

    var yrgf = dt.getFullYear() - 18;
    var initDate = new Date();
    initDate.setFullYear(initDate.getFullYear() - 18);
    initDate.setMonth(1 - 1);
    initDate.setDate(1);

            $("#nicDateOfBirth").datepicker({
                yearRange: yrg + ":" + yrgf,
                changeMonth: true,
                changeYear: true,
                dateFormat: "dd-mm-yy",
                defaultDate: initDate,
                onSelect: function (selected, evnt) {

                    var yrgr = dt.getFullYear();

                    //var yrgfr = dt.getFullYear() + 48;

                    var yrgfr = dt.getFullYear() + 49; // line modified by pr on 2ndaug18 to add in the current year to make it 67

                    var initrDate = new Date();
                    initrDate.setFullYear(initrDate.getFullYear());
                    initrDate.setMonth(1 + 1);
                    initrDate.setDate(1);


                    var date = selected.substring(0, 2);
                    var month = selected.substring(3, 5);
                    var year = selected.substring(6, 10);
                    var dateToCompare = new Date(year, month - 1, date);
                    var yrgr = dt.getFullYear();
                    //var yrgfr = dateToCompare.getFullYear() + 66;

                    var yrgfr = dateToCompare.getFullYear() + 67; // line modified by pr on 2ndaug18

                    $("#single_dor").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor1").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor1").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor2").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor2").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor3").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor3").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor4").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor4").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor5").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor5").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor6").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor6").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor7").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor7").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});

                }});
        });
</script>