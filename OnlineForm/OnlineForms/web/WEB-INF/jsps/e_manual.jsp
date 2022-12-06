<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="include/manual_assets_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<jsp:include page="include/manual_assets_include/header.jsp" />
<link rel="stylesheet" href="assets/home-page/css/usermanual.css" />
<div class="k-content	k-grid__item k-grid__item--fluid k-grid k-grid--hor" id="k_content">
    <!-- begin:: Content Head -->
    <div class="k-content__head	k-grid__item">
        <div class="k-content__head-main">
            <h3 class="k-content__head-title">eForms</h3>
            <div class="k-content__head-breadcrumbs">
                <a href="#" class="k-content__head-breadcrumb-home"><i class="flaticon2-shelter"></i></a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link">Dashboard</a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link">Manual</a>

                <!-- <span class="k-content__head-breadcrumb-link k-content__head-breadcrumb-link--active">Active link</span> -->
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet" style="height: 100%;">
            <!-- BEGIN PAGE CONTENT INNER -->
            <div class="portlet light " id="form_wizard_1" style="display:block;">
                <div class="portlet-title">
                    <div class="k-portlet__head">
                        <div class="k-portlet__head-label">
                            <h3 class="k-portlet__head-title">eForms Manual</h3>
                        </div>
                    </div>
                </div>
                <div class="portlet-body form manual-page">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab2" >
                                    <div class="col-md-12 mt-5">
                                        <div id="introduction" class="paragraph-sty">
                                            <h2>Introduction</h2>
                                            <p>
                                                Previously, the process of getting enrolled in NIC services depended on paperwork, however, with rapid
                                                technological changes, the process of filling forms online came into existence. Earlier with paperwork,
                                                applicants had to fill manual forms that pass through different levels of processing eventually making it a
                                                time-consuming process and increasing the difficulty for the applicants to track the status of their own
                                                application.
                                            </p>
                                            <p>
                                                With the eForms, this entire process, right from filling forms till availing services; it has become
                                                completely automated and manageable.
                                            </p>
                                        </div>
                                        <div id="purpose" class="paragraph-sty">
                                            <h2>Purpose</h2>
                                            <p>
                                                The purpose of this document is to provide step by step instructions to the users for filling the forms to
                                                avail several services available under the eForms.
                                            </p>
                                        </div>
                                        <div id="scope" class="paragraph-sty">
                                            <h2>Scope</h2>
                                            <p>
                                                This manual is meant for Ministries/Departments and States/UT’s applicants who are willing to avail NIC services.
                                            </p>
                                            <p><b>The services which are offered by NIC are as follows:</b></p>
                                            <table class="table table-bordered table-striped">
                                                <tr class="table-primary">
                                                    <th>S.No</th>
                                                    <th>Name of the Services</th>
                                                    <!--                                                    <th>S.No</th>
                                                                                                        <th>Name of the Services</th>-->
                                                </tr>
                                                <tr>
                                                    <td>1</td>
                                                    <td>Email (@gov.in)</td>
                                                    <!--                                                    <td>2</td>
                                                                                                        <td>Authentication Service (LDAP)</td>-->
                                                </tr>

                                                <tr>
                                                    <td>2</td>
                                                    <td>Distribution List Service</td>
                                                </tr>
                                                <tr>
                                                    <td>3</td>
                                                    <td>IMAP/POP</td>
                                                </tr>
                                                <tr>
                                                    <td>4</td>
                                                    <td>SMS Service</td>
                                                </tr>
                                                <tr>
                                                    <td>5</td>
                                                    <td>SMTP Gateway Service</td>
                                                </tr>
                                                <tr>
                                                    <td>6</td>
                                                    <td>Update Mobile in (@gov)</td>
                                                </tr>
                                                <tr>
                                                    <td>7</td>
                                                    <td>WIFI Service</td>
                                                </tr>
                                                <tr>
                                                    <td>8</td>
                                                    <td>WIFI Port Service</td>
                                                </tr>
                                                <tr>
                                                    <td>9</td>
                                                    <td>DNS Services</td>
                                                </tr>
                                                <tr>
                                                    <td>10</td>
                                                    <td>VPN Service</td>
                                                </tr>
                                                <tr>
                                                    <td>11</td>
                                                    <td>DA Onboarding</td>
                                                </tr>
                                            </table>
                                        </div>
                                        <div id="authorship" class="highlight-div paragraph-sty">
                                            <h3>Authorship</h3>
                                            <p>This manual has been written by Messaging and SMS division, National Informatics Centre, Ministry of Electronics and Information Technology, Government of India</p>
                                        </div>
                                        <div id="features" class="paragraph-sty">
                                            <h2>Features</h2>
                                            <ul class="features-ul mt-4 mb-5">
                                                <li>SMS and Email notification to all the stakeholders on any movement of the request or any modification of request</li>
                                                <li>Track facility to track the current status and past movements of the request</li>
                                                <li>Applicant and Reporting Officers can digitally sign the requests.</li>
                                                <li>Intelligence to alert approving authority while approving any suspicious request</li>
                                                <li>Intelligence to stop suspicious users</li>
                                                <li>Custom Workflow for different services</li>
                                                <li>Multi-department integration through web-services for seamless data sharing.</li>
                                                <li>Single click for ID creations and closure of the different services.</li>
                                                <li>Dashboard to all stakeholders to view approved, forwarded, submitted, rejected, pending and completed requests</li>
                                                <li>Filter and Search facility to search and filter the requests on the basis of service, applicant&rsquo;s email and status of the requests</li>
                                                <li>Raise/Respond to query to all the stakeholders for them to interact among each other</li>
                                                <li>Generate PDF facility to generate PDF of the request dynamically</li>
                                                <li>Download/Upload multiple documents facility to upload/download ID proofs or any other related documents.</li>
                                                <li>Preview facility to preview the form before approving/submitting it.</li>
                                            </ul>
                                        </div>
                                        <!--                                        <div id="portal_workflow" class="paragraph-sty">
                                                                                    <h2>Portal Workflow</h2>
                                                                                    <p>Portal Workflow is an introductory video about the eForms</p>
                                                                                    <img src="assets/home-page/img/manual_image/1.png" alt="" class="img-responsive" />
                                                                                </div>-->
                                        <div id="service_tab" class="paragraph-sty">
                                            <h2>Services Tab</h2>
                                            <p>Provides brief introduction of the on-boarded services as shown below: -</p>
                                            <img src="assets/home-page/img/manual_image/2.png" alt="2.png" class="img-responsive" />
                                        </div>
                                        <div id="in_focus" class="paragraph-sty">
                                            <h2>In-Focus</h2>
                                            <img src="assets/home-page/img/manual_image/03.png" alt="03.png" class="img-responsive" />
                                        </div>
                                        <div id="contact_us" class="paragraph-sty">
                                            <h2>Contact Us</h2>
                                            <p>You can use this feature to contact NIC Servicedesk Team to register your queries /complaints. By clicking on this button, you will be redirected to<a href="https://servicedesk.nic.in/"> https://servicedesk.nic.in/</a> where you will have to provide your contact details and raise a ticket against your query or issue. </p>
                                            <p>A unique ticket number will be generated which can be used for future reference. </p>
                                        </div>
                                        <div id="faqs" class="paragraph-sty">
                                            <h2>FAQS</h2>
                                            <p>All your queries related to eForms (in the form of question-answer) are listed under this tab such as- Login issues, Registration issues, issues faced while filling the Form. Also, applicants with any of the Roles (RO, Coordinator, Delegated Admin and Support) can refer this section to know the answers of their queries.  </p>
                                        </div>
                                        <!--                                        <div id="feedback" class="paragraph-sty">
                                                                                    <h2>Feedback</h2>
                                                                                    <p>Users can provide us with feedback/queries about the eForms.</p>
                                                                                    <img src="assets/home-page/img/manual_image/04.png" alt="04.png" class="img-responsive" />
                                                                                </div>-->
                                        <div id="register" class="paragraph-sty">
                                            <h2>HOW TO REGISTER?</h2>
                                            <ol class="features-ul self-ol mt-4">
                                                <li>You can open eForms from the URL <a href="https://eForms.nic.in">https://eForms.nic.in</a></li>
                                                <li>You will now see the home page of eForms. </li>
                                                <li>Click on login button given on the top right corner of the home page, as shown below.</li>
                                            </ol>
                                            <img src="assets/home-page/img/manual_image/05.png" alt="05.png" class="img-responsive" />
                                            <ol start="4" class="features-ul mt-4 mb-5">
                                                <li>You can choose either of the options (marked in red color box) such as:</li>
                                                <p>i. Login with Parichay (SSO)</p>
                                                <p>ii. Login with eForms</p>
                                            </ol>                                         
                                        </div>
                                        <div id="login_console_gov" class="paragraph-sty">
                                            <h2 class="mt-2">LOGIN CONSOLE (Government User)</h2>
                                            <h4>Login with Parichay (SSO)</h4>
                                            <p>This link will direct the user to the Login console of Parichay (SSO) portal where the government user possessing government/NIC Email ID will only be allowed to login.</p>
                                            <ol class="features-ul mt-4">
                                                <li>
                                                    User will login to Parichay (SSO) with his/her credentials by entering registered Email ID and password.
                                                </li>
                                                <li>User has to verify the Two –Step Authentication by choosing any of the option given on the “Two- Step Authentication” console.</li>
                                            </ol>
                                            <p>Shown below are 2 screenshots of how a login for a Government email looks like. </p>
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <img src="assets/home-page/img/manual_image/parichay_01.png" alt="parichay_01.png" class="img-responsive mb-5" style="height:350px; width:500px" />
                                                </div>
                                                <div class="col-md-4">
                                                    <img src="assets/home-page/img/manual_image/parichay_02.png" alt="parichay_02.png" class="img-responsive mb-5" style="height:350px; width:500px"  />
                                                </div>
                                            </div>
                                            <ol start="3">
                                                <li>For instance, user selects option: OTP on Mobile and clicks on Next button.</li>
                                                <li>In next step, enter OTP provided on the registered mobile no. and click on Next button.</li>
                                            </ol>
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <img src="assets/home-page/img/manual_image/parichay_04.png" alt="parichay_04.png" class="img-responsive  mb-5" style="height:350px; width:500px"/>
                                                </div>
                                                <div class="col-md-4">
                                                    <img src="assets/home-page/img/manual_image/parichay_03.png" alt="parichay_03.png" class="img-responsive  mb-5" style="height:350px; width:500px" />
                                                </div>
                                            </div>
                                            <ol start="5">
                                                <li>User will be logged in to the eForms portal.</li>
                                            </ol>
                                            <div class="col-md-8">
                                                <img src="assets/home-page/img/manual_image/31.png" alt="31.png" class="img-responsive  mb-5" />
                                            </div>
                                            <h4><b>Login with eForms</b></h4>
                                            <p>Users selecting this option must have Government/NIC Email ID which exists in LDAP. Hence, the entire login process remains the same as explained above.</p>
                                            <!--                                            <div class="row">
                                                                                            <div class="col-md-4">
                                                                                                <p><b>Step 1: Email Input Text Area</b></p>
                                                                                                <img src="assets/home-page/img/manual_image/06.png" alt="06.png" class="img-responsive img-manual-size mb-5" />   
                                                                                            </div>
                                                                                            <div class="col-md-4">
                                                                                                <p><b>Step 2: Password Input Text Area</b></p>
                                                                                                <img src="assets/home-page/img/manual_image/06_1.png" alt="06_1.png" class="img-responsive img-manual-size" />        
                                                                                            </div>
                                                                                            <div class="col-md-4">
                                                                                                <p><b>Step 3: OTP Input Text Area</b></p>
                                                                                                <img src="assets/home-page/img/manual_image/06_2.png" alt="06_2.png" class="img-responsive img-manual-size" />
                                                                                            </div>
                                                                                        </div>-->
                                        </div>
                                        <div id="login_console_non_gov" class="paragraph-sty">
                                            <h2>LOGIN CONSOLE (Non-Government Login)</h2>
                                            <!--                                            <ul class="features-ul mt-4">
                                                                                            <li>
                                                                                                Login Type: Non-Government Login Account
                                                                                            </li>
                                                                                        </ul>-->
                                            <p>Shown below are screenshots of how a login for a Non-Government email looks like.</p>
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <p><b>Step 1: Email Input Text Area</b></p>
                                                    <img src="assets/home-page/img/manual_image/06_3.png" alt="06.png" class="img-responsive img-manual-size mb-5" />      
                                                </div>
                                                <div class="col-md-4">  
                                                    <p><b>Step 2: Enter OTP and verify the “Captcha” code.</b></p>
                                                    <img src="assets/home-page/img/manual_image/06_4.png" alt="04.png" class="img-responsive img-manual-size" />        
                                                </div>
                                                <div class="col-md-4">
                                                    <p><b>Step 3: Read Instruction carefully.</b></p>
                                                    <img src="assets/home-page/img/manual_image/06_5.png" alt="04.png" class="img-responsive img-manual-size" />        
                                                </div>
                                            </div>
                                        </div>
                                        <div id="first_time_user" class="paragraph-sty">
                                            <h2>First time User:</h2>
                                            <ol class="features-ul ol-inner mt-4">
                                                <li>If you are an applicant who is using the portal for the 1st time, you will be prompted with a window that would ask for your email address and you can click on submit. A new window will appear which prompts to enter the applicant’s mobile number. The OTP will be sent to the given email address and mobile number both. The user can use either of them or both, to login to the portal.</li>
                                                <li>After submission, a new profile page will appear. The applicant will have to fill the complete personal as well as organizational information on the profile page to proceed further.</li>
                                                <li>The personal information includes fields like:
                                                    <ol class="features-ul ol-inner ">
                                                        <li>User name</li>
                                                        <li>Employee code</li>
                                                        <li>Mobile number (which will be auto-filled)</li>
                                                        <li>Email address</li>
                                                        <li>Telephone number (O/R) in the format mentioned</li>
                                                        <li>Designation</li>
                                                        <li>Official address</li>
                                                        <li>The state posted (select from the drop-down)</li>
                                                        <li>District name</li>
                                                        <li>Postal address</li>
                                                    </ol></li>
                                                <li>Click on continue to proceed. Enter your organizational information to register your profile in the eForms portal. The details to be submitted include fields like: -
                                                    <ol class="features-ul ol-inner">
                                                        <li>Organization category</li>
                                                        <li>Ministry/Organization</li>
                                                        <li>Department/Division/Domain</li>
                                                    </ol>
                                                </li>
                                                <li>Reporting/Nodal/forwarding officer email (If the applicant is a NIC employee the details of the reporting officer can be edited by sending a request to NIC OAD division)
                                                    <ol class="features-ul ol-inner">
                                                        <li>Reporting/Nodal/forwarding officer Name</li>
                                                        <li>Reporting/Nodal/forwarding officer Mobile</li>
                                                        <li>Reporting/Nodal/forwarding officer Telephone</li>
                                                        <li>Reporting/Nodal/forwarding officer Designation</li>
                                                    </ol>
                                                </li>
                                            </ol>
                                        </div>
                                        <div id="existing_user" class="paragraph-sty">
                                            <h2>EXISTING USER</h2>
                                            <ol class="features-ul ol-inner mt-4">
                                                <li>The applicant will login using the credentials (NIC/Gov email address or any alternate email address). The email address from which the applicant logins to the portal, already exists in our database. Hence, it will display the registered number on which the OTP will be sent for login.
                                                    <ol class="features-ul ol-inner">
                                                        <li>nter the OTP received on the mobile number and click on continue. In any case, if you haven’t received the OTP you can click on “Resend OTP”, you will receive another OTP, which you can enter and click on continue to proceed further.</li>
                                                    </ol>
                                                    <p class="highlight-div paragraph-sty mt-2"><b>NOTE:</b>
                                                        The domain of the email address of the reporting officer should have @nic.in/@gov.in or any other government sub-domains like @cbi.gov.in, @csir.res.in, etc.). If the reporting officer’s email address is a non-government domain (e.g. @gmail.com/@yahoo.com etc.) the process will become manual (which is explained further in this manual) for that particular case.   
                                                    </p>
                                                </li>
                                                <li>The eForms portal has made it mandatory for applicants to have a permanent profile to be created and saved so it is pre-filled in the registration form of NIC services.</li>
                                                <li>The reporting officer’s details will be saved and if you are a NIC employee the details of the reporting officer will not be edited. You will have to send an email to eForms@nic.in to update your reporting officer’s details. Please refer the screenshots given below for reference.</li>
                                            </ol>
                                        </div>
                                        <div id="home_page" class="paragraph-sty">
                                            <h2>Home Page</h2>
                                            <p>Once the applicant log’s in, he/she will be able to view a page where there will be many options available. Here are the options mentioned below: -</p>
                                        </div>
                                        <div id="dashboard" class="paragraph-sty">
                                            <h2>Dashboard</h2>
                                            <p>Dashboard provides user a glimpse of the type of information one can see depending on the role and rights of the user. Apart from this feature, Dashboard provides information about all the services available (Discussed below) as well as total user request, pending requests in any, total pending request, Total request completed.</p>
                                            <p><b>Dashboard Panel: </b>Once you will login in to dashboard, you can easily see the type of role one is assigned on the left on the panel, as shown in the diagram. </p>
                                            <img src="assets/home-page/img/manual_image/09.png" alt="09.png" class="img-responsive" />        
                                        </div>
                                        <div id="dashboard_types" class="paragraph-sty">
                                            <h2>Types of Dashboard</h2>
                                            <img src="assets/home-page/img/manual_image/09_3.png" alt="09_3.png" class="img-responsive" />        
                                            <ol class="features-ul ol-inner mt-4">
                                                <li><strong><u>User Dashboard:</u></strong> Also known as &ldquo;My Request&rdquo; shows the number of forms that you have filled with the status. You can anytime take the following actions on your request like Preview/Edit, Reject or track. The applicant can also track his application status by the SMS or email received. There is a tracking link that is sent via both email and SMS to the applicant&rsquo;s registered email address or mobile number. </li>
                                                <p>For other Types of dashboard, all the details are mentioned below.</p>
                                                <li>RO Dashboard</li>
                                                <li>CO Dashboard</li>
                                                <li>Support</li>
                                                <li>Admin</li>
                                                <li>Delegated Admin</li>
                                            </ol>
                                            <p>To understand the difference between each dashboard and how positions within the eForms operate it&rsquo;s important to know role and objectives of each.</p>
                                            <ol class="features-ul ol-inner mt-4">
                                                <li id="role_applicant"><b>Role of Applicant: </b>
                                                    <p>The applicant will fill the form using eForms portal. If the email address of the applicant through whom he is trying to login to the portal is of a non-government domain, it will be prompted to enter the mobile number on which OTP will be received. Also, OTP will be sent to both email addresses and mobile numbers. </p>
                                                    <ol class="features-ul ol-inner mt-4">
                                                        <li>The new user can authenticate using either the OTP&rsquo;s or any one of them. However, after the final submission of the profile, it will be prompted to authenticate using the OTP sent on the mobile number/email address.</li>
                                                        <li>The applicant will fill the profile information on eForms portal which will include the details like personal and organizational information. In the organizational information, if the applicant&rsquo;s reporting officer&rsquo;s email address is a government domain, in this case, the process becomes online. The applicant will fill the form and after submission will be asked for three options:
                                                            <ol class="features-ul ol-inner mt-4">
                                                                <li>E-sign the document with Aadhar.</li>
                                                                <li>Proceed online without Aadhar.</li>
                                                                <li>Proceed manually by uploading the scanned copy</li>
                                                            </ol>
                                                            <p class="highlight-div paragraph-sty mt-2">The online process of eforms portal depends on the email address of the reporting officer. If the reporting officer of the applicant is a government employee whose email address ends with a government domain and exists in our database, in this case, the process of submission of online forms becomes online. This is irrespective of the applicant’s email address.</p>
                                                        </li>
                                                    </ol>
                                                </li>
                                                <li id="role_secretary"><b>Role of Undersecretary/JS/Secretary: </b>
                                                    <p>If the email address of the reporting officer is a non-government domain (eg: @gmail.com, @yahoo.com, etc.), the requirement to fill the details of Undersecretary/JS/Secretary becomes mandatory.</p>
                                                    <ol class="features-ul ol-inner mt-4">
                                                        <li>Once the applicant fills the registration form, the same will be forwarded to Undersecretary/JS/Secretary for approval. A link to accept or decline requests along with the details of the applicant will be sent to the undersecretary/JS/secretary email address (as mentioned in the profile of the applicant) and mobile number.</li>
                                                        <li>This link is valid until 7 days, after which it will expire and the application form will be rejected automatically. After the confirmation of undersecretary/JS/secretary, the form will be forwarded to the concerned NIC coordinator/Delegated administrator for further action.</li>
                                                    </ol>
                                                </li>
                                                <li id="role_reporting_officer"><b>Role of Reporting officer: </b>
                                                    <p>If the reporting officer’s email address is a government domain (exists in our database), then the application filed by the applicant will be forwarded to the concerned reporting officer. Once the application form is submitted by the applicant, an email confirmation sent is to the reporting officer’s email address stating to take necessary action against the request. The reporting officer will login to eForms portal, using the credentials as mentioned in the email (i.e. login id), enter the OTP sent to your registered mobile number and proceed.</p>
                                                    <ol class="features-ul ol-inner mt-4">
                                                        <li>After login a dashboard will appear, in which all the requests pending or completed by the reporting officer will be visible. Apply filter on the listed service and click on the action button in front of the registration number. The following actions can be performed by the reporting officer:
                                                            <ol class="features-ul ol-inner mt-4">
                                                                <li>Preview/Edit</li>
                                                                <li>Approve</li>
                                                                <li>Reject</li>
                                                                <li>Track</li>
                                                                <li>Generate Form</li>
                                                                <li>Upload scanned form</li>
                                                                <li>Upload multiple docs</li>
                                                                <li>Download multiple docs</li>
                                                                <li>Download docs uploaded by the user</li>
                                                                <li>Raise/ Respond to query</li>
                                                            </ol>
                                                        </li>
                                                    </ol>
                                                    <p class="highlight-div paragraph-sty mt-2">After the action by the reporting officer, the request will go the concerned delegated administrator/NIC coordinator of the applicant’s Ministry/Department/State.</p>
                                                </li>
                                                <li id="role_coordinator"><b>Role of NIC Coordinator:  </b>
                                                    <p>In the manual process, after the approval by undersecretary/ JS/secretary, the form will be forwarded to the concerned NIC Coordinator/Delegated Administrator for approval. The NIC coordinator can download the form uploaded by the applicant. The coordinator can also respond to various queries </p>
                                                    <ol class="features-ul ol-inner mt-4">
                                                        <li>Preview/Edit</li>
                                                        <li>Reject</li>
                                                        <li>Generate Form</li>
                                                        <li>Upload/Change Scanned form</li>
                                                        <li>Download uploaded form</li>
                                                        <li>Raise/ Respond to query</li>
                                                    </ol>
                                                </li>
                                                <li id="role_support"><b>Role of Support:</b>
                                                    <ol class="features-ul ol-inner mt-4">
                                                        <li>Support team plays one of the most vital roles in the process of approval and rejection of an application. The role of a support also comes with a privilege where they can choose or add a DA, Coordinator and even an Admin.</li>
                                                        <li>Support with the help of App ID (Registration #) can easily track the status of the application rigt from the beginning.</li>
                                                        <li>It also has a special role where Support can use Search Functionality based on keyword and role of a person on the bases of their name.   Once the search is complete, Support team can see all the forms which are filled by the searched name.</li>
                                                    </ol>
                                                </li>
                                                <li id="role_admin"><b>Role of Admin:</b>
                                                    <ol class="features-ul ol-inner mt-4">
                                                        <li>Admin Panel is more like the closing panel where the final stating of the eForms takes place.</li>
                                                        <li>The user in the last process once closed receives a username and password which is created by the admin panel.</li>

                                                    </ol>
                                                </li>
                                                <li id="delegated_admin"><b>Delegated Admin:</b>
                                                    <p>The states that have been given the delegated administrator console will be responsible of creation of email accounts after the approval from reporting officer. </p>
                                                    <ol class="features-ul ol-inner mt-4">
                                                        <li>Any modification or deletion in the email address of the applicant will be done by the DA&rsquo;s using the DA console given to them. The DA console is accessible only through VPN.</li>
                                                        <li>If the user has any query regarding the email account creation he/she may contact the delegated administrator directly.</li>
                                                        <li>Administrators which have been delegated to manage accounts of a particular department/organization.</li>
                                                        <li>It gives independence of managing their respective accounts under their own business organizations (BO&rsquo;s).</li>
                                                        <li>These administrators can create, delete, activate, deactivate accounts, can manage the size of mailboxes of a particular user of their department, can enable/disable IMAP and POP, can change the password etc.</li>
                                                        <li>The delegated administrative console is given to organizations which are using e-mail services of NIC under free/paid categories.</li>
                                                    </ol>
                                                </li>
                                            </ol>
                                        </div>
                                        <div id="about_process" class="paragraph-sty">
                                            <h2>About Manual and Online Process</h2>
                                            <p>There are 2 possibilities in which the user can fill the form:</p>
                                            <ol class="features-ul ol-inner mt-4">
                                                <li><b>Manual process: </b>
                                                    <p> If the applicant has created a profile on eForms and has given the email address of the reporting officer as a non-government domain (e.g. @gmail.com/yahoo.com etc.), in this case, the process becomes manual for the applicant. </p>
                                                </li>
                                            </ol>
                                            <label id="preq_new_user" class="lab-styl pl-5">Prerequisites for new users </label>
                                            <ul class="features-ul mb-5 ml-5">
                                                <li>The form consists of personal as well as organizational information. The applicant will have to fill all the fields marked with a * (mandatory) sign.</li>
                                                <li>If the email address of the reporting officer is of a non-government domain, the applicant will have to provide the details of undersecretary/JS/secretary (Name, email address, mobile number, landline number, and designation). A link will be sent to undersecretary and above for approval or rejection of the application form&rsquo;s</li>
                                                <li>This link will be sent to the email address mentioned in the profile information and will be valid for 7 days. If the link expires the application form will be automatically rejected and the applicant will have to apply again using eForms portal.</li>
                                            </ul>
                                            <label id="preq_ex_user" class="lab-styl pl-5">Prerequisites for Existing users</label>
                                            <ul class="features-ul mb-5 ml-5">
                                                <li>If the applicant has already created a profile on eforms portal, and the email address of the reporting officer is a non-government domain, the applicant will have to provide the details of undersecretary/JS/secretary in the profile.</li>
                                                <li>The registration form will be filled by the applicant, it will be sent to the concerned undersecretary/JS/secretary for approval (a link and SMS will be sent to the registered email address and mobile number of undersecretary/JS/Secretary).</li>
                                                <li>Once the application form is approved by the official it will be forwarded to the concerned NIC Coordinator/Delegated Administrator for necessary action. The final action is taken by the admin for the request submitted by the applicant and approved by the NIC coordinator/DA. The applicant will receive a message once the request is completed/ rejected by the admin.</li>
                                            </ul>
                                            <ol class="features-ul ol-inner mt-4">
                                                <li>The applicant will fill the registration form; after the final submission of the form the applicant will be directed to download the form (PDF file). The downloaded PDF form can be uploaded using the dashboard of the user module.</li>
                                                <li>Click on “My Request” option on the left pane of the dashboard, you will be able to view the request submitted by you.</li>
                                                <li>Select the service for which you wish to upload your documents and apply the required filter.</li>
                                                <li>Click on the request for which you want to upload the form and then click on “Action” button, you will see a drop-down menu in which multiple options are present, click on upload/change scanned forms and select the downloaded form from your PC/Laptop.</li>
                                            </ol>
                                        </div>
                                        <div id="online_process" class="paragraph-sty">
                                            <h2>The Online Process of eForms    </h2>
                                            <p>In the online process of eforms user has three options namely:</p>
                                            <ol class="features-ul ol-inner mt-4">
                                                <li>E-sign the document with Aadhar Card</li>
                                                <li>Proceed online without Aadhar Card</li>
                                                <li>Proceed manually by uploading the scanned copy</li>
                                            </ol>
                                            <p>The applicant can use any one of the options as per the convenience. </p>
                                            <ul class="features-ul mb-5 ml-5">
                                                <li>In the online process, user’s request will be forwarded to the Reporting Officer for approval.</li>
                                                <li>After the approval from the Reporting Officer, the request will be forwarded to the NIC Coordinator/Delegated Administrator.</li>
                                                <li>Once NIC Coordinator approves the request, the request will be forwarded to the Admin/Support Team for approval and closure of the request.</li>
                                            </ul>
                                            <p class="highlight-div paragraph-sty mt-2">If an applicant is a NIC employee, the details of the reporting officer remains non-editable. In this case, the NIC employee will send a request to eforms@nic.in for change of reporting officer details.</p>
                                            <div class="col-md-12"><img src="assets/home-page/img/manual_image/online.png" alt="online.png" class="img-responsive"></div>
                                            <!--                                            <label style="color: #5578eb;">The Manual Process of eForms</label>
                                                                                        <ul class="features-ul mt-1 mb-3">
                                                                                            <li>The applicant will fill the registration form; after the final submission of the form the applicant will be directed to download the form (PDF file). The downloaded PDF form can be uploaded using the dashboard of the user module.</li>
                                                                                            <li>Click on &ldquo;My Request&rdquo; option on the left pane of the dashboard, you will be able to view the request submitted by you.</li>
                                                                                            <li>Select the service for which you wish to upload your documents and apply the required filter.</li>
                                                                                            <li>Click on the request for which you want to upload the form and then click on &ldquo;Action&rdquo; button, you will see a drop-down menu in which multiple options are present, click on upload/change scanned forms and select the downloaded form from your PC/Laptop.</li>
                                                                                        </ul>-->
                                            <!--<div class="row">-->
                                            <!--<div class="col-md-6"><img src="assets/home-page/img/manual_image/09_1.png" alt="09_1.png" class="img-responsive img-manual-size" /></div>-->
                                            <!--<div class="col-md-12"><img src="assets/home-page/img/manual_image/09_2.png" alt="09_2.png" class="img-responsive" />        </div>-->
                                            <!--</div>-->
                                            <ul class="features-ul mt-1 mb-4 mt-5">
                                                <li>The file size of the (.pdf) file should be less than 1 MB in size. Click on upload once you have chosen the file. Your form will be uploaded successfully. You can also upload the supporting documents along with the form, just click on “upload multiple docs” option, browse the document from your PC/Laptop and click on upload, the documents will be uploaded successfully. However, if the applicant wants to verify the documents, there is an option to download the documents as well.</li>
                                            </ul>
                                            <p class="highlight-div paragraph-sty mt-2"><b>NOTE:</b> The form can be edited any time by the applicant till the form finally reaches the next level, i.e. reporting officer.</p>
                                            <p class="mt-4">An applicant can also track the status of the application form by switching to the dashboard, click on “My Request”, select the service from the list and then click on the action button in front of the registration form number which you want to track. The pop-up window will display the details like application reference number, applicant’s name, email, mobile, applied date. It also displays, whether the applicant is an online/manual user and to which step the application form has been reached along with the full timestamp. The action button displays the following options for the applicant:</p>
                                            <ul class="features-ul mt-1 mb-3">
                                                <li>Preview/Edit</li>
                                                <li>Reject</li>
                                                <li>Track</li>
                                                <li>Generate Form</li>
                                                <li>Download scanned form</li>
                                                <li>Upload multiple docs</li>
                                                <li>Upload/Change scanned form</li>
                                                <li>Download uploaded docs</li>
                                                <li>Raise/Respond to query</li>
                                            </ul>        
                                        </div>
                                        <div id="our_service_tab" class="paragraph-sty ">
                                            <h2>Our Services Tab</h2>
                                            <p>This option displays the list of online forms available in eForms portal. The applicant may choose any of the services as per the requirement.</p>

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <!--<h5>Step 1: LDAP Form</h5>-->


                                                    <!--                                                    <h5 class="mt-4">Step 2.3: LDAP Preview Form (Organizational and LDAP Request Details)</h5>
                                                                                                        <img src="assets/home-page/img/manual_image/08_3.png" alt="08.png" class="img-responsive img-manual-size mt-4"/>-->
                                                </div>
                                                <!--                                                <div class="col-md-6">
                                                                                                    <h5>Step 2.1: LDAP Preview Form (Personal Information)</h5>
                                                                                                    <img src="assets/home-page/img/manual_image/08_1.png" alt="08.png" class="img-responsive img-manual-size mt-4"/>
                                                                                                    
                                                                                                    <h5 class="mt-5">Step 2.2: LDAP Preview Form (Reporting/Nodal/Forwarding Officer Details)</h5>
                                                                                                    <img src="assets/home-page/img/manual_image/08_2.png" alt="08.png" class="img-responsive img-manual-size mt-4"/>
                                                                                                </div>
                                                                                            </div>
                                                                                            <div class="row">
                                                                                                <div class="col-md-6">
                                                                                                    <h5 class="mt-4">Step 3: Alert Box regarding Reporting Officer.</h5>
                                                                                                    <img src="assets/home-page/img/manual_image/08_4.png" alt="08_4.png" style="width:400px !Important;" class="img-responsive img-manual-size mt-4"/>
                                                                                                    
                                                                                                    <h5 class="mt-4">Step 5: Final Step to send request to RO/Nodal/Forwarding Officer</h5>
                                                                                                    <img src="assets/home-page/img/manual_image/8_06.png" alt="08.png" class="img-responsive img-manual-size mt-4"/>
                                                                                                </div>
                                                                                                <div class="col-md-6">
                                                                                                    <h5 class="mt-4">Step 4: Pre-Request for integration and Checking</h5>
                                                                                                    <img src="assets/home-page/img/manual_image/08_5.png" alt="08.png" class="img-responsive img-manual-size mt-4"/>
                                                                                                </div>
                                                                                            </div>-->
                                            </div>
                                            <div id="distribution_service_list" class="paragraph-sty ">
                                                <div class="row">
                                                    <div class="col-md-2">
                                                        <img style="width: auto!important;" src="assets/home-page/img/manual_image/15.png" alt="15.png" class="img-responsive mt-4"/>
                                                    </div>
                                                    <div class="col-md-10 mt-4">
                                                        <h2>Distribution List Services</h2>
                                                        <p>After login into eForms portal, you will see the list of services on the left panel.</p>
                                                        <ol class="features-ul">
                                                            <li>Click on the Distribution List form to proceed with your request.</li>
                                                            <li>Read the instructions carefully given while filling the form.</li>
                                                            <!--                                                        <li>Enter the name of the list which you want to keep. Please note append @lsmgr.nic.in after the list name. Now enter the description of the list.</li>
                                                                                                                    <li>You can also assign a moderator to the list who will be responsible for any action taken on the list or click on No if you want the list to be open for all the list members.</li>-->
                                                        </ol>
                                                    </div>
                                                </div>
                                                <div class="single_request">
                                                    <h5 class="mt-5 mb-4">Single Request</h5>
                                                    <ol class="features-ul">
                                                        <li>Enter the name of the list which you want to keep. Please note append @lsmgr.nic.in after the list name. Now enter the description of the list.</li>
                                                        <li>You can also assign a moderator to the list who will be responsible for any action taken on the list or click on No if you want the list to be open for all the list members.</li>

                                                        <div class=""col-md-12">
                                                             <img class="col-md-12 mt-4" src="assets/home-page/img/manual_image/single_01.png" alt="single_01.png">
                                                            <img class="col-md-12" src="assets/home-page/img/manual_image/single_02.png" alt="single_02.png">
                                                        </div>

                                                        <li>Now if you are the moderator of the list, then enter the moderator name, email address, and mobile number. Enter the correct Captcha and click on the preview and submit button.</li>
                                                        <img  src="assets/home-page/img/manual_image/19.png" alt="19.png" class="img-responsive mt-2 mb-2"/>
                                                        <li>The form will be submitted and will be sent to the reporting officer for necessary action.</li>
                                                        <li>The application can be processed in two ways: -
                                                            <ol class="features-ul ol-inner">
                                                                <li>Proceed online without Aadhaar (In this process the form will be sent to the concerned reporting officer for approval, after which the form will be forwarded to the respective NIC coordinator/Delegated Administrator for necessary action. The admin will then process the request and a confirmation SMS and email will be sent to the applicant with the credentials or with information that Wi-Fi has been enabled on your device.</li>
                                                                <li>Proceed Manually (In this process you will have to download the form and proceed. The process in mentioned in the section (“<text style="color:red;">About Manual and Online Process of eForms Portal</text>”.)</li>
                                                            </ol>
                                                        </li>

                                                    </ol>
                                                    <!--                                            <div class="row">
                                                                                                    <div class="col-md-6"><img  src="assets/home-page/img/manual_image/16.png" alt="16.png" class="img-responsive mt-2 mb-2"/></div>
                                                                                                    <div class="col-md-6">
                                                                                                        <ol start="5" >
                                                                                                            <li>Also select whether the list is temporary, if yes mention the validity date.</li>
                                                                                                        </ol>
                                                                                                        <img  src="assets/home-page/img/manual_image/17.png" alt="17.png" class="img-responsive  mb-2"/>
                                                                                                        <ol start="6" >
                                                                                                            <li>Also, specify whether only the members are allowed to send mails to the list or not. Make the selection appropriately as per your choice. 
                                                                                                                <ol type="i">
                                                                                                                    <li>
                                                                                                                        Your list will be created and the applicant will be notified by SMS and email which will be sent to the respective email address and mobile number.
                                                                                                                    </li>
                                                                                                                </ol>
                                                                                                            </li>
                                                                                                        </ol>
                                                                                                        <img  src="assets/home-page/img/manual_image/18.png" alt="18.png" class="img-responsive mt-2 mb-2"/>
                                                                                                    </div>
                                                                                                </div>-->
                                                    <!--                                            <div class="row">
                                                                                                    <div class="col-md-6">
                                                                                                        <ol start="7">
                                                                                                            <li>Now if you are the moderator of the list, then enter the moderator name, email address, and mobile number. Enter the Captcha to preview and submit the form.</li>
                                                                                                        </ol>
                                                                                                        <img  src="assets/home-page/img/manual_image/19.png" alt="19.png" class="img-responsive mt-2 mb-2"/>
                                                                                                    </div>
                                                                                                    <div class="col-md-6">
                                                                                                        <ol start="8">
                                                                                                            <li>The form will be submitted and will be sent to the reporting officer for necessary action.</li>
                                                                                                            <li>The application can be processed in two ways: -
                                                                                                                <ol type="i">
                                                                                                                    <li>Proceed online without Aadhar (In this process the form will be sent to the concerned reporting officer for approval, after which the form will be forwarded to the respective NIC coordinator/Delegated Administrator for necessary action. The admin will then process the request and a confirmation SMS and email will be sent to the applicant with the credentials or with information that Wi-Fi has been enabled on your device.</li>
                                                                                                                    <li>Proceed Manually (In this process you will have to download the form and proceed. The process in mentioned in the section (“<span style="color: red;">About Manual and Online Process of eForms Portal</span>”.)</li>
                                                                                                                </ol>
                                                                                                            </li>
                                                                                                        </ol>  
                                                                                                    </div>
                                                                                                </div>-->
                                                </div>
                                                <div id="bulk_request" class="paragraph-sty">
                                                    <h2>Bulk Request</h2>
                                                    <ol class="features-ul">
                                                        <li>For bulk request, select the Excel file in correct format and upload the same in the application form</li>
                                                        <img class="mt-2" src="assets/home-page/img/manual_image/bulk_01.png" alt="bulk_01.png">
                                                        <li>Enter the correct Captcha value and submit the form. For the remaining process refer<text style="color:red"> “Distribution List (Single Request)”</text>.</li>
                                                        <img class="mt-2" src="assets/home-page/img/manual_image/bulk_02.png" alt="bulk_02.png">
                                                    </ol>
                                                </div>
                                                <div id="dns_service" class="paragraph-sty">
                                                    <h2>DNS Services</h2>
                                                    <div class="row">
                                                        <div class="col-md-2">
                                                            <img  style="width: auto!important;" src="assets/home-page/img/manual_image/dns_01.png" alt="dns_01.png" />
                                                        </div>
                                                        <div class="col-md-10">
                                                            <ol class="features-ul">
                                                                <label><b>DNS User Subscription (File Upload)</b></label>
                                                                <p>Steps to avail DNS services as given below:</p>
                                                                <li>Open the URL <a href="https://eForms.nic.in">https://eForms.nic.in.</a></li>
                                                                <li>Enter the credentials and login to the portal.</li>
                                                                <li>On the left pane of the page click on DNS service option.</li>
                                                                <li>You will see two options on the dashboard:</li>
                                                                <ol class="features-ul ol-inner">
                                                                    <li>DNS User Subscription (Manual Entries)</li>
                                                                    <li>DNS Bulk Subscription through (File Upload)</li>
                                                                </ol>
                                                                <li>Read the instructions carefully before filling the form. Fill all the mandatory fields marked with (*).</li>
                                                                <li>User can follow below process for any of the options as mentioned in point no. 4.</li>
                                                        </div>
                                                        <div class="col-md-10">
                                                            <label id="dns_manual" class="mt-4"><b>DNS User Subscription (Manual Entries): </b></label>
                                                            <ol class="features-ul">
                                                                <li>Make your selections, if your request is for New, Modify or Delete DN Sentry.</li>
                                                                <li>Enter the domain name, CName (Canonical Name) and IP Address. A user can enter multiple IP addresses by clicking on the &ldquo;+&rdquo; sign.</li>
                                                                <li>Enter the web server location (only alphanumeric, white space and., #/ () are allowed).</li>
                                                                <li>Select record addition and check the checkbox for MX, PTR, TXT, SRV, SPF, and DMARC.</li>

                                                                <div class="col-md-12">
                                                                    <img  src="assets/home-page/img/manual_image/dns_02.png" alt="dns_02.png" class="img-responsive mt-2 mb-2" />
                                                                    <img  src="assets/home-page/img/manual_image/dns_03.png" alt="dns_03.png" class="img-responsive mt-2 mb-2" />
                                                                </div>


                                                                <!--                                                <div class="row">
                                                                                                                    <div class="col-md-6">
                                                                                                                        <img  src="assets/home-page/img/manual_image/21.png" alt="21.png" class="img-responsive mt-2 mb-2" />
                                                                                                                    </div>
                                                                                                                    <div class="col-md-6">
                                                                                                                        <img  src="assets/home-page/img/manual_image/22.png" alt="22.png" class="img-responsive mt-2 mb-2" />
                                                                                                                    </div>
                                                                                                                </div>-->

                                                                <li>If you have made the selection as MX, then the request will be sent to MR. Rajesh Singh (rajesh.singh@nic.in)/Mrs. Rajeswari (rajp@nic.in) for approval.</li>
                                                                <li>For other additions, the request will be sent to the Admin for necessary action.</li>
                                                                <li>Enter the Captcha and click on preview and submit.</li>
                                                                <li>You will view the preview of the form. You have the option to edit the form, only the organizational detail and new request DNS entry details are editable. Click on &ldquo;agree the terms and conditions&rdquo; and submit the form.</li>
                                                                <li>You will see a confirmation window, which will display the details of your reporting officer, click on Yes to proceed or click on No if you are unsure about the submission of the form.</li>
                                                                <li>If you have clicked &ldquo;YES&rdquo;, select the form submission type from the option shown and proceed further.</li>
                                                                <li>You can select the following options to proceed:
                                                                    <ol style="list-style-type: circle;">
                                                                        <li>E-Sign the document with Aadhar- Enter your Aadhar details to e-sign the document for verification.</li>
                                                                        <li>Proceed online- This will automatically submit the form.</li>
                                                                        <li>Proceed manually by uploading the scanned Copy- If you have opted for a manual process please upload the scanned copy of the form in the user module and then proceed further with the submission.</li>
                                                                    </ol>
                                                                </li>


                                                                <!--<p>Click on continue for final submission of the form. You will receive a registration number of the form filled by you. </p>-->
                                                                <!--<div class="row">-->
                                                                <div class="col-md-6">
                                                                    <img  src="assets/home-page/img/manual_image/23.png" alt="23.png" class="img-responsive mt-2 mb-2" />    
                                                                </div>

                                                                <!--                                                <div class="col-md-6">
                                                                                                                    <img style="width: 309px;" src="assets/home-page/img/manual_image/24.png" alt="24.png" class="img-responsive mt-2 mb-2" />
                                                                                                                </div>-->
                                                                <!--</div>-->
                                                                <li>Click on continue for final submission of the form. A pop-up will be displayed indicating the approval of your form by your Reporting Officer. If you wish to proceed, click on “YES” otherwise click on “NO”.</li>
                                                                <img style="width: 509px;" src="assets/home-page/img/manual_image/24.png" alt="24.png" class="img-responsive mt-2 mb-2" />
                                                                <li>For instance, you clicked on “YES”, your form will be submitted and a unique Registration Number will be generated for your request. You can also, track your request by using “TRACK” button available on the notification box.</li>
                                                                <img src="assets/home-page/img/manual_image/dns_04.png" alt="dns_04.png" class="img-responsive mt-2 mb-2" />
                                                            </ol>
                                                            <p class="highlight-div paragraph-sty mt-2"><b>NOTE:</b> The form can be edited any time by the applicant till the form finally reaches the next level, i.e. reporting officer.</p>
                                                        </div>
                                                    </div>
                                                    <!--                                            <label>The flow of the form will remain the same, please refer the point (“<label style="color:red;">About Manual and Online Process of eForms Portal</label>”)</label>-->
                                                    <h2>DNS User Subscription through(File upload): -</h2>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <img  src="assets/home-page/img/manual_image/dns_05.png" alt="dns_05.png" class="img-responsive mt-2 mb-2" />

                                                        </div>
                                                        <div class="col-md-6">
                                                            <img  src="assets/home-page/img/manual_image/dns_05.png" alt="dns_05.png" class="img-responsive mt-2 mb-2" />
                                                        </div>
                                                    </div>
                                                    <p><b>NOTE: </b>Please read all instructions before uploading the file: -</p>
                                                    <ul class="features-ul mt-1 mb-3">
                                                        <li>All the columns heading are mandatory in CSV file</li>
                                                        <li>Download the sample file then do the entries.</li>
                                                        <li>DNS URL is a mandatory field.</li>
                                                        <li>A maximum number of rows accepted at a time are 3000. Please upload CSV file with maximum 3000 rows only.</li>
                                                        <li>You can upload a file for new requests, modify the request or delete request.</li>
                                                        <li>You can also download the sample .csv file for all the three cases</li>
                                                        <li>Select your file from your desktop. Click on browse to select your file.</li>
                                                        <p>User can follow below given steps for requesting DNS User Subscription through File Upload</p>
                                                    </ul>

                                                    <ol class="features-ul">

                                                        <li>Select the request type</li>
                                                        <li>Choose the records addition from the provided list</li>
                                                        <li>Click on “Browse” to select the file from your desktop and upload the same.</li>
                                                        <li>Enter the correct Captcha value and click on “preview and submit” button.</li>
                                                        <li>You will be shown the preview of the request form, click on “agree to terms and conditions” followed by clicking on “Submit” button.</li>
                                                        <li>A pop-up window will be displayed on the screen indicating the approval of your request from Reporting Officer, click on “YES” to proceed.</li>
                                                        <li>Select any of the three options from below for submitting your request:</li>
                                                        <ol class="features-ul">
                                                            <li>E-Sign the document with Aadhaar</li>
                                                            <li>Proceed online</li>
                                                            <li>Proceed manually by uploading the scanned Copy</li>
                                                        </ol>
                                                        <li>After choosing the submission type, click on “Final Submit”. You request will be submitted for approval and a unique registration number will be generated. With this registration number you can track the status of your request by using “TRACK” button.</li>
                                                    </ol>


                                                    <!--                                            <p>You will receive a confirmation along with a registration number of the form submitted. Use the registration number to track your application form.</p>-->
                                                </div>
                                                <div id="email_tab" class="paragraph-sty">
                                                    <h2>Email (@gov)</h2>
                                                    <div class="row">
                                                        <div class="col-md-2"><img src="assets/home-page/img/manual_image/email_01.png" alt="email_01.png" class="img-responsive img-manual-size" /></div>
                                                        <div class="col-md-10">
                                                            <p>This registration form is designed for the applicants who require an email address in the government domain.</p>
                                                            <p>Users who wish to avail this service can follow below given steps for filling the request:</p>
                                                            <ol class="features-ul ol-inner">
                                                                <li>Login to eforms portal >> Select the option “Email (@gov)” on the left pane of the dashboard.</li>
                                                                <li>Read the instruction given on window pop-up and click on the OK button to proceed.</li>
                                                                <li>Select any of the email requests as per your requirements (as shown in the screenshot below).</li>
                                                                <li>Choose from the given options for single subscription details:
                                                                    <ol class="features-ul ol-inner mt-4">
                                                                        <li>For Self</li>
                                                                        <li>For Other User (where you are posted)</li>            
                                                                    </ol>
                                                                </li>
                                                                <li>From the options given below for the type of email id, click on any one of the radio button as per your requirements:                                                       
                                                                    <ol class="features-ul ol-inner mt-4">
                                                                        <li>Mail User (with mailbox)</li>
                                                                        <li>Application User (without mailbox (E-office auth))</li>
                                                                        <li>e-office-srilanka</li>
                                                                    </ol>
                                                                </li>
                                                                <li>Enter rest of the details in correct format</li>
                                                                <li>DesFor different type of email requests, steps for filling the requests are mentioned in the subsequent sections. (Refer images attached along with the steps).</li>
                                                        </div>
                                                    </div>
                                                    <div id="email_sub" class="paragraph-sty">
                                                        <h3 id="single_user_subs">Single User Subscription Form:</h3>
                                                        <ol class="features-ul">
                                                            <li>Select Single Subscription and details from the list of options provided on the request form</li>
                                                            <img src="assets/home-page/img/manual_image/email_02.png" alt="email_02.png" class="img-responsive mt-2" />
                                                            <li>Choose the type of mail id. If you wish to know the details of mail ids, click on “Know More” link before choosing the options.</li>
                                                            <img src="assets/home-page/img/manual_image/email_03.png" alt="email_03.png" class="img-responsive mt-2" />
                                                            <li>Enter date of birth and date of retirement/date of expiry in correct format.</li>
                                                            <img src="assets/home-page/img/manual_image/email_04.png" alt="email_04.png" class="img-responsive mt-2" />
                                                            <li>Select your Email address preference and Employee Description.</li>
                                                            <img src="assets/home-page/img/manual_image/email_05.png" alt="email_05.png" class="img-responsive mt-2" />
                                                            <li>Enter your preferred email address 1 & 2. Please read the email address guidelines carefully before proceeding further.</li>
                                                            <img src="assets/home-page/img/manual_image/email_06.png" alt="email_06.png" class="img-responsive mt-2" />
                                                            <li>Enter the correct Captcha value and click on Preview and Submit button.</li>
                                                            <img src="assets/home-page/img/manual_image/email_07.png" alt="email_07.png" class="img-responsive mt-2" style="height:200px" />
                                                            <li>The preview of the filled form will be shown to the applicant. The applicant can edit the official details in the form before the final submission. If the applicant wants to change any details in the personal as well as organizational information, it can be changed by using “My Profile” option given on the top right corner of the page.</li>
                                                            <p class="highlight-div paragraph-sty mt-2"><b>NOTE:</b> For the flow of after submission of the form refer: <b>About Manual and Online Process of eforms Portal</b></p>
                                                            <li>After clicking on the Submit button, a pop-up message will be displayed for confirming of approval of the request by the Reporting Officer. If the user agrees with the approval, s/he can click on YES to proceed further.</li>
                                                            <li>On the next screen, the user will be shown three options for the submission of the request and s/he can click on any of the radio buttons to finally submit the request.
                                                                <ol class="features-ul ol-inner mt-4">
                                                                    <li>e-sign the document with Aadhaar</li>
                                                                    <li>Proceed online</li>
                                                                    <li>Proceed manually by uploading the scanned copy</li>
                                                                </ol>
                                                            </li>
                                                            <li>After clicking on Final Submit button, registration number of your request will be generated, this registration number can be used to track the status of your application by using “TRACK USER” button.</li> 
                                                        </ol>
                                                        <!--                                                            <img src="assets/home-page/img/manual_image/Screenshot_2.png" alt="Screenshot_2.png" class="img-responsive img-manual-size" />                                                
                                                                                                                <li><img src="assets/home-page/img/manual_image/Screenshot_3.png" alt="Screenshot_3.png" class="img-responsive img-manual-size" /></li>
                                                                                                                <li><img src="assets/home-page/img/manual_image/Screenshot_4.png" alt="Screenshot_4.png" class="img-responsive img-manual-size" /></li>
                                                                                                                <li>Select your Employee Description<img src="assets/home-page/img/manual_image/Screenshot_5.png" alt="Screenshot_5.png" class="img-responsive img-manual-size" /></li>
                                                                                                                <li>Enter your preferred email address 1 &amp; 2. Please read the email address guidelines carefully before proceeding further.</li>
                                                                                                                <li>Enter the Captcha value and proceed.</li>
                                                                                                                <li>The form preview will be shown to the applicant. The applicant can edit the official details in the form before the final submission. If the applicant wants to change any details in the personal as well as organizational information, it can be changed by going to the top right corner of the page, click on &ldquo;My Profile&rdquo; and then make the necessary changes.</li>               
                                                                                                            <p class="highlight-div paragraph-sty mt-2">For the flow of after submission of the form refer: <b>About Manual and Online Process of eforms Portal</b></p>-->
                                                    </div>

                                                    <h2 id="bulk_subs">Bulk Subscription Form:</h2>
                                                    <p>This registration form is designed for applicants who require an email address in bulk in the government domain. Login to the eForms portal remains the same.</p>
                                                    <p>Steps for filling the bulk email request is as follows:</p>
                                                    <ol class="features-ul">
                                                        <li>Select “Bulk Subscription”, bulk user subscription details and email address preference from the list of the options provided in the form.</li>
                                                        <img src="assets/home-page/img/manual_image/email_08.png" alt="email_08.png" class="img-responsive" />
                                                        <li>If you wish to check the sample file for bulk subscription in CSV format, then click on the link to download and refer the same to upload the bulk data for the form.
                                                            <p class="mt-4">The input file should be in the format as given below:</p>
                                                            <ol class="features-ul ol-inner mt-4">
                                                                <li>First Name and Last Name</li>
                                                                <li>Designation: Department/ Ministry: State</li>
                                                                <li>Country Code without (+): Mobile</li>
                                                                <li>Date of Retirement (dd-mm-yyyy)</li>
                                                                <li>Login UID</li>
                                                                <li>Complete Email address</li>
                                                                <li>Date of Birth (dd-mm-yyyy)</li>
                                                                <li>Employee Code</li>
                                                            </ol>
                                                        </li>
                                                        <img src="assets/home-page/img/manual_image/email_09.png" alt="email_09.png" class="img-responsive" />
                                                        <li class="mt-4">Select employee description and upload the CSV file by using “Browse” option & selecting the file from your desktop/laptop.</li>
                                                        <li>Enter the correct Captcha value and click on Submit button.</li>
                                                        <li>Rest of the process remains the same as mentioned in <text style="color:red">“Single User Subscription Form” section.</text></li>
                                                        <img src="assets/home-page/img/manual_image/email_10.png" alt="email_10.png" class="img-responsive mt-2" />
                                                    </ol>
                                                    <!--                                            <div class="row">
                                                                                                    <div class="col-md-6">
                                                                                                        <ol class="features-ul ol-inner mt-4">
                                                                                                            <li>Read the instruction window pop up and click on the OK button to proceed.</li>
                                                                                                            <li>Select &ldquo;Bulk User Subscription&rdquo;</li>
                                                                                                            <li>Enter the bulk user subscription details such as :-
                                                                                                                <ol class="features-ul ol-inner mt-4">
                                                                                                                    <li>Type of Mail (With Mailbox)</li>
                                                                                                                    <li>Application User (without mail box(Eoffice-auth))</li>  line modified by pr on 5thmay2020 
                                                                                                                    <li>E-office Srilanka</li>
                                                                                                                </ol>
                                                                                                            </li>
                                                                                                            <li>The applicant can download a sample CSV file which can be used as a reference to upload the bulk data for the bulk user subscription form.</li>
                                                                                                            <li>The format for input file should be:
                                                                                                                <ol class="features-ul ol-inner mt-4">
                                                                                                                    <li>First Name and Last Name</li>
                                                                                                                    <li>Designation: Department/ Ministry: State</li>
                                                                                                                    <li>Country Code without(+): Mobile</li>
                                                                                                                    <li>Date of Retirement(dd-mm-yyyy)</li>
                                                                                                                    <li>Login UID</li>
                                                                                                                    <li>Complete Email address</li>
                                                                                                                    <li>Date of Birth(dd-mm-yyyy)</li>
                                                                                                                    <li>Employee Code</li>
                                                                                                                </ol>
                                                                                                            </li>
                                                                                                            <li>Enter the “Employee Description”:-
                                                                                                                <ol class="features-ul ol-inner mt-4">
                                                                                                                    <li>Govt/PSU Official</li>
                                                                                                                    <li>Consultant</li>
                                                                                                                    <li>FMS Support Staffs</li>
                                                                                                                </ol>
                                                                                                            </li>
                                                                                                        </ol>
                                                                                                    </div>-->
                                                    <div class="col-md-6">
                                                        <p class="highlight-div paragraph-sty mt-2"><span style="color:red;font-weight: 600;">Note:</span>  for the flow of after submission of the form refer the point:<text style="color:red"> About Manual and Online Process of eForms Portal</text></p>
                                                        <p class="highlight-div paragraph-sty mt-2">The maximum number of rows accepted at a time is 3000. Please upload CSV file with maximum 3000 rows only.</p>
                                                        <p class="highlight-div paragraph-sty mt-2">All Fields are mandatory (except Date of Birth and Employee code) for account creation</p>
                                                        <p class="highlight-div paragraph-sty mt-2">We have allowed ID Creation facility for International mobile numbers as well. To handle this, now excel file will have one more column to accommodate country codes for their country.
                                                            Country Code (allowed 1-5 digits), Mobile Number (allowed 8-14 digits) * For for (91) Country Code Only a 10-digit mobile number is allowed.</p>

                                                        <img src="assets/home-page/img/manual_image/Screenshot_6.png" alt="Screenshot_6.png" class="img-responsive mb-3 mt-2" />
                                                    </div>
                                                </div>

                                                <h2 id="nkn_single_subs">NKN Single Subscription</h2>
                                                <p>This registration form is designed for applicants who require an email address in the government domain for NKN connected institutes.</p>
                                                <p>Login to the eForms portal remains the same as mentioned in previous sections. The applicant has to follow below steps for filling NKN single subscription request.</p>
                                                <ol class="features-ul">
                                                    <li>Click on Email (@gov) service available on the left pane of the dashboard.</li>
                                                    <li>Read the instructions on the pop-up window and click on OK button to proceed.</li>
                                                    <li>Select “NKN Single Subscription” from the options provided</li>
                                                    <li>Enter the NKN user subscription details:
                                                        <ol class="features-ul">
                                                            <li>Institute Name</li>
                                                            <li>Institute ID</li>
                                                            <li>Name of Project NKN</li>
                                                        </ol>
                                                    </li>
                                                    <li>Select the date of birth and date of retirement from the calendar.</li>
                                                    <li>Select the preferred email address 1 and 2. Refer to the email address guidelines while entering the preferred email address.</li>
                                                    <li>Click on the preview and submit button to proceed further.</li>
                                                </ol>
                                                <p class="highlight-div paragraph-sty mt-2"><span style="color:red;font-weight: 600;">Note:</span>  For the flow of after submission of the form refer the point:<text style="color:red"> About Manual and Online Process eForms Portal</text></p>
                                                <img src="assets/home-page/img/manual_image/nkn_01.png" alt="nkn_01.png" class="img-responsive mb-3" />  
                                                <h2 id="nkn_bulk_subs">NKN BULK Subscription</h2>
                                                <p>This registration form is designed for the applicants who require bulk email address in the government domain for NKN connected</p>
                                                <p>The applicant must follow below given steps to avail this service:</p>
                                                <ol class="features-ul">
                                                    <li>Login to eForms portal >> Select the option “Email (@gov)” on the left pane of the dashboard.</li>
                                                    <li>Read the instructions on the pop-up window and click on the OK button to proceed.</li>
                                                    <li>Select “NKN Bulk Subscription”</li>
                                                    <li>Enter the NKN Bulk User Subscription details:
                                                        <ol class="features-ul">
                                                            <li>Institute name</li>
                                                            <li>Institute ID</li>
                                                            <li>Name of Project NKN</li>
                                                        </ol>
                                                    </li>
                                                    <li>The applicant can download the sample CSV file which can be used as a reference to upload the data for the creation of email accounts for NKN Institutes. The email address will be created in the institute's own registered domain.</li>
                                                    <li>The format of input file should be:
                                                        <ol class="features-ul">
                                                            <li>First Name</li>
                                                            <li>Last Name</li>
                                                            <li>Designation</li>
                                                            <li>Department/ Ministry</li>
                                                            <li>State</li>
                                                            <li>Country Code without (+) Mobile</li>
                                                            <li>Date of Retirement (dd-mm-yyyy)</li>
                                                            <li>Login UID</li>
                                                            <li>Complete Email address</li>
                                                            <li>Date of Birth (dd-mm-yyyy)</li>
                                                            <li>Employee Code</li>                                                       
                                                        </ol>
                                                    </li>
                                                    <li>Click to browse and select the file from your desktop to upload the same in the form.</li>
                                                    <li>Enter the correct Captcha value and click on Submit button.</li>
                                                    <p class="highlight-div paragraph-sty mt-2"><span style="color:red;font-weight: 600;">Note:</span>  For the flow of after submission of the form refer the point:<text style="color:red"> About Manual and Online Process eForms Portal</text></p>
                                                    <img src="assets/home-page/img/manual_image/nkn_02.png" alt="nkn_02.png" class="img-responsive mb-3" />  
                                                    <img src="assets/home-page/img/manual_image/nkn_03.png" alt="nkn_03.png" class="img-responsive mb-3" />  
                                                </ol>
                                                <!--                                            <div class="row">
                                                                                                <div class="col-md-6">
                                                                                                    <ol class="features-ul ol-inner mt-4">
                                                                                                        <li>Read the instruction window pop up and click on OK button to proceed.</li>
                                                                                                        <li>Select &ldquo;NKN Single Subscription&rdquo;</li>
                                                                                                        <li>Enter the NKN user subscription details:
                                                                                                            <ol class="features-ul ol-inner mt-4">
                                                                                                                <li>Institute Name</li>
                                                                                                                <li>Institute ID</li>
                                                                                                                <li>Name of Project NKN</li>
                                                                                                            </ol>
                                                                                                        </li>
                                                                                                        <li>Select the date of birth and date of retirement from the calendar.</li>
                                                                                                        <li>Select the preferred email address 1 and 2. Refer to the email address guidelines while entering the preferred email address.</li>
                                                                                                        <li>Click on the preview and submit button to proceed further.</li>
                                                                                                    </ol>
                                                                                                </div>
                                                                                                <div class="col-md-6">
                                                                                                    <p class="highlight-div paragraph-sty mt-2"><span style="color:red;font-weight: 600;">Note:</span>  for the flow of after submission of the form refer the point: about Manual and Online Process of eForms Portal</p>
                                                                                                    <img src="assets/home-page/img/manual_image/Screenshot_7.png" alt="Screenshot_7.png" class="img-responsive mb-3" />
                                                                                                </div>
                                                                                            </div>-->

                                                <!--                                            <p>This registration form is designed for the applicants who require an email address in the government domain. Login to eForms portal>> Select the option “Email” on the left pane of the dashboard.</p>
                                                                                            <div class="row">
                                                                                                <div class="col-md-6">
                                                                                                    <ol class="features-ul ol-inner mt-4">
                                                                                                        <li>Read the instruction window pop up and click on the OK button to proceed.</li>
                                                                                                        <li>Select &ldquo;NKN Bulk Subscription&rdquo;</li>
                                                                                                        <li>Enter the NKN Bulk User Subscription details:
                                                                                                            <ol class="features-ul ol-inner mt-4">
                                                                                                                <li>Institute name</li>
                                                                                                                <li>Institute ID</li>
                                                                                                                <li>Name of Project NKN</li>
                                                                                                            </ol>
                                                                                                        </li>
                                                                                                        <li>The applicant can download the sample CSV file which can be used as a reference to upload the data for the creation of email accounts for NKN Institutes. The email address will be created in the institute's own registered domain.</li>
                                                                                                        <li>The format of input file should be:
                                                                                                            <ol class="features-ul ol-inner mt-2">
                                                                                                                <li>First Name</li>
                                                                                                                <li>Last Name</li>
                                                                                                                <li>Designation</li>
                                                                                                                <li>Department/ Ministry</li>
                                                                                                                <li>State</li>
                                                                                                                <li>Country Code without (+) Mobile</li>
                                                                                                                <li>Date of Retirement (dd-mm-yyyy)</li>
                                                                                                                <li>Login UID</li>
                                                                                                                <li>Complete Email address</li>
                                                                                                                <li>Date of Birth (dd-mm-yyyy)</li>
                                                                                                                <li>Employee Code</li>
                                                                                                            </ol>
                                                                                                        </li>
                                                                                                    </ol>
                                                                                                </div>
                                                                                                <div class="col-md-6">
                                                                                                    <p class="highlight-div paragraph-sty mt-2"><span style="color:red;font-weight: 600;">Note:</span>   for the flow of after submission of the form refer the point: about Manual and Online Process of eForms Portal</p>
                                                                                                    <img src="assets/home-page/img/manual_image/Screenshot_8.png" alt="Screenshot_8.png" class="img-responsive img-manual-size mb-3" />
                                                                                                </div>-->
                                                <!--</div>-->
                                                <h2 id="gem_subs">GEM Subscription</h2>
                                                <p>For the process of GeM User subscription refer the URL: <a href="https://gem.gov.in/userFaqs">https://gem.gov.in/userFaqs</a></p>
                                                <h2>For Primary Users: </h2>
                                                <p>Please find the GeM Subscription Procedure mentioned below for Primary applicant.</p>
                                                <p>Send the endorsed letter signed by the Deputy Secretary or Undersecretary level of the ministry under which the organization belongs to and forward the same to gemapplicant@gem.gov.in. After the approval, the applicant will get an email id with the domain @gembuyer.in.</p>
                                                <img src="assets/home-page/img/manual_image/gem_01.png" alt="gem_01.png" class="img-responsive"/>
                                                <img src="assets/home-page/img/manual_image/gem_02.png" alt="gem_02.png" class="img-responsive " />
                                                <p>Once the primary email id is created, send a CSV file of the accounts which are to be created by GeM Subscription in the given format which is mentioned in the trailing mail.</p>
                                                <ol class="features-ul mt-4 mb-5">
                                                    <li>S No.</li>
                                                    <li>First Name</li>
                                                    <li>Last Name</li>
                                                    <li>Designation</li>
                                                    <li>Role (HOD/Buyer/ Consignee /both/PAO/DDO)</li>
                                                    <li>Name of Ministry/ Department/ Organization</li>
                                                    <li>State/City</li>
                                                    <li>Mobile No (10 digits)</li>
                                                    <li>Complete Office Address</li>
                                                </ol>
                                                <h2 id="sec_subs">For Secondary Users: </h2>
                                                <p>Fill the GeM User Subscription form given on eForms portal. </p>
                                                <div class="row">
                                                    <div class="text">
                                                        <ol class="features-ul ol-inner mt-4">
                                                            <li>Read the instruction window pop up and click on OK button to proceed.</li>
                                                            <li>Select &ldquo;GeM Subscription&rdquo;</li>
                                                            <li>Enter the GeM User Subscription details:</li>
                                                            <li>In the organization category:

                                                            </li>


                                                            <h4>For Central PSE</h4>
                                                            <p>Select the controlling ministry from the drop down</p>
                                                            <img src="assets/home-page/img/manual_image/gem_03.png" alt="gem_03.png" class="img-responsive  mb-3" />


                                                            <h4>For State PSE</h4>
                                                            <p>Select the state of posting from the drop-down and select the district name (where the applicant is posted) from the drop-down.</p>
                                                            <img src="assets/home-page/img/manual_image/gem_04.png" alt="gem_04.png" class="img-responsive  mb-3" />
                                                            <li>Forwarding Officer details will be auto filled when you select the organization category</li>
                                                            <p class="col-md-12 highlight-div paragraph-sty mt-2"><span style="color:red;font-weight: 600;">Note:</span>  Your application needs to be forwarded by an officer at the level of Under Secretary or above and having a government email address.<br><br><text>For example: @nic.in/@gov.in. Once approved by the Forwarding Officer, your request will be forwarded to gemapplicant@gem.gov.in. Please contact GeM support (gemapplicant@gem.gov.in) for any queries.<text></p>
                                                            <p>If you are a primary user/HOD on GeM Portal click on YES or click NO.</p>
                                                            <li>Enter the personal details:</li>
                                                            <li>Date of retirement</li>
                                                            <li>Select the role to be assigned from the drop-down</li>
                                                            <li>Enter the preferred email address 1&2(refer the email address guidelines for the creation of email address)</li>
                                                            <li>Enter your projected monthly traffic.</li>
                                                            <li>Enter the Captcha value and click on Preview and Submit button.</li>
                                                            <li>Rest of the process remains the same as mentioned in <text style="color:red">“Single User Subscription Form”</text>.</li>
                                                        </ol>
                                                    </div>                                                  
                                                </div>
                                                <h3 id="email_act">Email Active</h3>
                                                <ol class="features-ul">
                                                    <li>Select the “Email Activate” option in the form.</li>
                                                    <li>Enter the email id in the correct format as shown in the image below</li>
                                                    <li>Choose the employee description such as:
                                                        <ol class="features-ol">
                                                            <li>Govt/PSU Official</li>
                                                            <li>Consultant/Contractual Staff</li>
                                                            <li>FMS Support Staff</li>

                                                        </ol>
                                                    <li>Select the Date of Retirement from the calendar</li>
                                                    <li>Enter the correct Captcha value and click on Preview and Submit button</li>
                                                    <li>For rest of the process, follow the steps as mentioned in<text style="color:red"> “Single User Subscription form”</text>.</li>
                                                    <img src="assets/home-page/img/manual_image/email_11.png" alt="email_11.png" class="img-responsive mt-2 mb-3" />
                                                    </li>
                                                </ol>
                                                <h3 id="email1_deact">Email De-Activate</h3>
                                                <ol class="features-ul">
                                                    <li>Select the “Email De-Activate” option in the Email Subscription Forms under Email (@gov) service</li>
                                                    <li>Enter the email id in the correct format as shown in the image below</li>


                                                    <li>Enter the correct Captcha value and click on Preview and Submit button</li>
                                                    <li>For rest of the process, follow the steps as mentioned in<text style="color:red"> “Single User Subscription form”</text>.</li>
                                                    <img src="assets/home-page/img/manual_image/email_12.png" alt="email_12.png" class="img-responsive mt-2 mb-3" />
                                                    </li>
                                                </ol>
                                                <!--                                                <div class="row">
                                                                                                    <div class="col-md-6">
                                                                                                        <ol class="features-ul  mt-4">
                                                                                                            <li><b>Enter the forwarding officer details like: </b>
                                                                                                                <ul style="list-style-type: circle;" >
                                                                                                                    <li>Email address</li>
                                                                                                                    <li>Name</li>
                                                                                                                    <li>Mobile number</li>
                                                                                                                    <li>Telephone number</li>
                                                                                                                    <li>Designation</li>
                                                                                                                    <li>Address</li>
                                                                                                                </ul>
                                                                                                                <p><b>If you are a primary user/HOD on GeM Portal click on YES or click NO. </b></p>    
                                                                                                            </li>
                                                                                                            <li>Enter the personal details:</li>
                                                                                                            <li>Date of retirement</li>
                                                                                                            <li>Select the role to be assigned from the drop-down</li>
                                                                                                            <li>Enter the preferred email address 1&amp;2(refer the email address guidelines for the creation of email address)</li>
                                                                                                            <li>Enter your projected monthly traffic.</li>
                                                                                                            <li>Enter the Captcha value and proceed further.</li>
                                                                                                        </ol>
                                                                                                    </div>
                                                                                                    <div class="col-md-6">
                                                                                                        <img src="assets/home-page/img/manual_image/Screenshot_25.png" alt="Screenshot_25.png" class="img-responsive  mb-3" />
                                                                                                    </div>
                                                                                                </div>-->
                                                <!--</div>-->
                                                <div id="central_utm_service" class="paragraph-sty">
                                                    <h2>Extend the Validity of Account</h2>
                                                    <ol class="features-ul">
                                                        <li>Select Extend the Validity of Account option.</li>
                                                        <li>Choose Email address preference from the provided options.</li>
                                                        <li>Select Employee Description as shown in the screenshot below.</li>
                                                        <li>Your email address, date of birth and previous date of account expiry will be prefilled. </li>
                                                        <li>Choose the date of account expiry rom the calendar that you want to extend.</li>
                                                        <li>Fill in correct Captcha value and click on Preview and Submit button. </li>
                                                        <li>For rest of the process, follow the steps as mentioned in<text style="color:red" “Single User Subscription form”.</text></li>
                                                    </ol>
                                                    <img src="assets/home-page/img/manual_image/email_13.png" alt="email_13.png" class="img-responsive  mb-3" />
                                                </div>
                                                <div id="imap_pop" class="paragraph-sty">
                                                    <h2>IMAP/POP</h2>
                                                    <p>The users, who wish to apply for NIC IMAP/POP services, shall follow below given procedure to fill in the request.</p>


                                                    <div class="row">
                                                        <div class="col-md-12">
                                                            <ol class="features-ul ol-inner mt-4">
                                                                <li>Enter the eForms portal address<a href="https://eforms.nic.in/ browser"> (https://eforms.nic.in/)</a> on the web browser.</li>
                                                                <li>Enter your credentials to log in to the portal.</li>
                                                                <li>Click on the IMAP/POP service from the left-hand panel of the dashboard.</li>
                                                                <li>Read the given instructions carefully while filling the form.</li>
                                                                <li>Check the protocol which is to be enabled on your device i.e. IMAP/POP. Click on any one of  the options as per your requirements.</li>
                                                                <li>Enter the correct Captcha value and click on “Preview and Submit” button.</li>
                                                                <img src="assets/home-page/img/manual_image/imap_01.png" alt="imap_01.png" class="img-responsive" />
                                                                <li>You can now preview the form and edit also. Accept the terms and conditions to submit the form.</li>
                                                                <img src="assets/home-page/img/manual_image/imap_02.png" alt="imap_02.png" class="img-responsive" />
                                                                <li>The applicant will be shown three types of submission process, select any one of the   options to finally submit the request.</li>
                                                                <img src="assets/home-page/img/manual_image/imap_03.png" alt="imap_03.png" class="img-responsive" />
                                                                <li>After clicking on “Continue” button, your form will be finally submitted and a registration number will be generated for your request. This registration number can be further used to track the status of your request anytime by using “TRACK USER” button.</li>
                                                                <img src="assets/home-page/img/manual_image/imap_04.png" alt="imap_04.png" class="img-responsive" />
                                                            </ol>

                                                        </div>
                                                        <!--                                                    <div class="col-md-6">
                                                                                                                <img src="assets/home-page/img/manual_image/Screenshot_11.png" alt="Screenshot_11.png" class="img-responsive" />
                                                                                                                <img src="assets/home-page/img/manual_image/Screenshot_27.png" alt="Screenshot_27.png" class="img-responsive mt-4" />
                                                                                                            </div>-->
                                                    </div>
                                                </div>

                                                <div id="sms_service" class="paragraph-sty">
                                                    <h2>SMS Services</h2>
                                                    <p>SMS service allows you to register for the following services: PUSH / PULL / OBD / MISSED CALL / OTP SERVICE / QUICK SMS SERVICE.</p>
                                                    <p>Users have to follow below given steps to fill any of the SMS services as mentioned above.</p>
                                                    <ol class="features-ul">
                                                        <li>Login to the eForms portal <a href="https://eforms.nic.in/">(https://eforms.nic.in/)</a>  with your valid credentials and OTP validation.</li>
                                                        <li>Click on SMS Service option available under OUR SERVICES tab on the left-hand panel in the Dashboard.</li>
                                                        <li>Read the instructions/notice carefully before proceeding for the filling the form.</li>
                                                        <li>Select the SMS service from the options provided (as shown in screenshot below). Enter the application name, application URL, purpose of application, server location, IP address 1 and IP address 2. Click on Continue button.</li>
                                                        <img src="assets/home-page/img/manual_image/sms_01.png" alt="sms_01.png" class="img-responsive mt-2" />
                                                        <img src="assets/home-page/img/manual_image/sms_02.png" alt="sms_02.png" class="img-responsive" />
                                                        <li>Enter contact details of the Technical Admin in the Step 2.</li>
                                                        <img src="assets/home-page/img/manual_image/sms_03.png" alt="sms_03.png" class="img-responsive mt-2" />
                                                        <li>Enter the contact details of the Billing Owner in Step 3.</li>
                                                        <img src="assets/home-page/img/manual_image/sms_04.png" alt="sms_04.png" class="img-responsive mt-2" />
                                                        <li>Enter other mandatory details such as: application security audit, IP of staging server for testing, monthly expected SMS traffic, sender ID, projected domestic monthly SMS traffic and projected international SMS traffic.
                                                            Enter the correct Captcha value and click on Preview and Submit button to proceed further.</li>
                                                        <img src="assets/home-page/img/manual_image/sms_05.png" alt="sms_05.png" class="img-responsive mt-2" />
                                                        <li>For rest of the process after Preview and Submit, refer <text style="color:red">“Email (@gov) service section “</text>.</li>
                                                    </ol>


                                                </div>



                                                <div id="ip_change_request" class="paragraph-sty">
                                                    <h2>IP Change Request</h2>
                                                    <p>For IP change request, user has to follow step by step process to fill in the application form on the eForms portal. Here are the steps for the same:</p>
                                                    <div class="row">
                                                        <div class="col-md-12">
                                                            <ol class="features-ul ol-inner mt-4">
                                                                <li>Enter <a href="https://eforms.nic.in/">https://eforms.nic.in/</a>on your browser.</li>
                                                                <li>Enter your valid credentials to log in to the eForms portal.</li>
                                                                <li>Click on “IP Change Request” form under OUR SERVICES tab given on the left panel of the dashboard.</li>
                                                                <li>Read the instructions carefully given while filling the form</li>

                                                                <li>Select your preference:
                                                                    <ol class="features-ul ol-inner mt-4">
                                                                        <li>Add IP</li>
                                                                        <li>Change IP</li>
                                                                    </ol>
                                                                </li>
                                                                <img src="assets/home-page/img/manual_image/imap_05.png" alt="imap_05.png" class="img-responsive" />
                                                                <li>When you click on change IP and proceed, you will get two options to make your choice i.e. change IP for LDAP Auth and SMS Service.
                                                                    <ol class="features-ul ol-inner mt-4">
                                                                        <li>If you have made the choice as LDAP auth you can change up to 4 IPs and fill in other details also as shown in the image below. Enter the correct Captcha and click on Preview and Submit button.</li>
                                                                        <img src="assets/home-page/img/manual_image/ip_01.png" alt="ip_01.png" class="img-responsive" />
                                                                        <li>For a change of IP in case of SMS service enter the account name along with the IP address. You can change up to 4 IP’s in this case also. Enter the correct Captcha and click on Preview and Submit button.</li>
                                                                        <img src="assets/home-page/img/manual_image/ip_02.png" alt="ip_02.png" class="img-responsive" />
                                                                    </ol>
                                                                </li>
                                                                <li>You will see the preview of your form. If you wish to edit the form you can edit the organization details and service specific details by using “Edit” button or else click on accept terms and conditions and submit the form.</li>
                                                                <li>A confirmation message will be displayed on the screen indicating the approval of your request by your Reporting Officer. Click on “YES” to proceed further.</li>
                                                                <li>Your application can be processed in three ways as shown in the screenshot below:
                                                                    <ol class="features-ul ol-inner mt-4">
                                                                        <li>e-sign the document with Aadhaar</li>
                                                                        <li>Proceed online</li>
                                                                        <li>Proceed manually by uploading the scanned copy</li>
                                                                    </ol>
                                                                </li>
                                                                <img src="assets/home-page/img/manual_image/ip_03.png" alt="ip_03.png" class="img-responsive" />
                                                                <li>After clicking on Continue button, your request will be finally submitted and a registration number will be generated for your request. Also, a confirmation message will be sent to your registered mobile number and email regarding the submission of your request.</li>
                                                                <img src="assets/home-page/img/manual_image/ip_04.png" alt="ip_04.png" class="img-responsive" />
                                                            </ol>
                                                        </div>

                                                    </div>
                                                    <!--                                                <div class="row">
                                                                                                        <div class="col-md-12 mt-3">
                                                                                                            <ul >
                                                                                                                <li>When you click on change IP and proceed, you will get three options to make your choice i.e. change IP for LDAP Auth, relay and SMS Service.</li>
                                                                                                                <li>If you have made the choice as LDAP auth you can change up to 4 IPs. Enter the Captcha and proceed.</li>
                                                                                                                <li>Now, if the preference is to change the IP for relay service, you will have to enter the application name along with the old IP address, also select the server location from the drop-down menu.</li>
                                                                                                                <li>Now, you can change up to 4 IP&rsquo;s. Enter the <em>Captcha</em> and proceed.</li>
                                                                                                                <li>For a change of IP in case of SMS service enter the account name along with the old IPs. You can change up to 4 IP&rsquo;s in this case also. Enter the <em>Captcha</em> and proceed.</li>
                                                                                                                <li>After clicking on the submit button you can view the preview of the form. Click on accept terms and conditions and submit the form. The form will be sent to the reporting officer for necessary action.</li>
                                                                                                                <li>You can preview and submit the form. The application can be processed in two ways:
                                                                                                                    <ol style="list-style-type: circle;">
                                                                                                                        <li>Proceed online without Aadhar *
                                                                                                                            <p class="highlight-div paragraph-sty mt-2">*In this process the form will be sent to the concerned reporting officer for approval, after which the form will be forwarded to the respective NIC coordinator/Delegated Administrator for necessary action. The admin will then process the request and a confirmation SMS and email will be sent to the applicant with the credentials or with information that Wi-Fi has been enabled on your device.</p>
                                                                                                                        </li>
                                                                                                                        <li>Manually (In this process you will have to download the form and proceed. The process mentioned below.)</li>
                                                                                                                    </ol>
                                                                                                                </li>
                                                                                                                <li>You can preview and submit the form. The application can be processed in two ways   
                                                                                                                    <ol style="list-style-type: circle;">
                                                                                                                        <li>Proceed online without Aadhar which goes through a process (About Manual and Online Process of eForms Portal) and a confirmation SMS and email will be sent to the applicant with the credentials or with information that Wi-Fi has been enabled on your device.</li>
                                                                                                                        <li>Proceed Manually</li>
                                                                                                                    </ol>
                                                                                                                </li>
                                                                                                                <li>Similarly, in case of Add IP there are three options to select namely LDAP auth, relay service, and SMS</li>
                                                                                                                <li>You will have to enter the IP address which you want to add with all the service. The maximum limits of adding IP&rsquo;s are up to 4.
                                                                                                                    <img src="assets/home-page/img/manual_image/Screenshot_29.png" alt="Screenshot_29.png" class="img-responsive mt-3 mb-3" />
                                                                                                                    <ol style="list-style-type: circle;">
                                                                                                                        <li>For LDAP Auth just add provide the IP address which you want to add.</li>
                                                                                                                        <li>For RELAY service mention the application name, old IP address and server location along with the IP address which is to be added.</li>
                                                                                                                        <li>For SMS Service mention the account name for which IP has to add. The maximum limit of adding IP address is 4.</li>
                                                                                                                    </ol>
                                                                                                                </li>                                                            <li>Enter the <em>Captcha</em> value and proceed with the preview and submission of the form.</li>
                                                                                                                <li>You will see the preview of the form which you can submit or edit after accepting the terms and conditions.</li>
                                                                                                                <li>The form will be submitted and it will be forwarded to the reporting officer for necessary action.</li>
                                                                                                            </ul>
                                                                                                        </div>
                                                                                                    </div>-->
                                                    <!--</div>-->
                                                    <div id="smtp_gateway" class="paragraph-sty">
                                                        <h2>SMTP Gateway</h2>
                                                        <p>SMTP Gateway service allows you to register for relay service to send emails from applications (only outgoing mails).</p>
                                                        <p>Users can follow below steps to fill in the application form for SMTP Gateway service:</p>
                                                        <ol class="features-ul">
                                                            <li>Login to the eForms portal with valid credentials and OTP validation.</li>
                                                            <li>Click on SMTP Gateway under OUR SERVICE tab in the Dashboard.</li>
                                                            <li>Select the request type: NEW, ADD, MODIFY, DELETE.</li>
                                                            <li>Enter the valid Application IP which is mandatory field to be filled.</li>
                                                            <img src="assets/home-page/img/manual_image/sms_06.png" alt="sms_06.png" class="img-responsive mt-2 mb-2" />
                                                            <li>Choose any one of the options for Security Audit:
                                                                <ol class="features-ol">
                                                                    <li>Hardware</li>
                                                                    <li>Software</li>
                                                                    <ul class="features-ul">
                                                                        <li>If you have chosen Hardware, then upload the exemption certificate/letter in PDF format in the pace provided (refer image below).</li>
                                                                        <img src="assets/home-page/img/manual_image/smtp_01.png" alt="smtp_01.png" class="img-responsive" />
                                                                        <li>If you have chosen Software, then select the security audit date of expiry from the calendar and upload the exemption certificate/letter in PDF format.</li>
                                                                        <img src="assets/home-page/img/manual_image/smtp_02.png" alt="smtp_02.png" class="img-responsive" />
                                                                    </ul>
                                                                </ol>
                                                            </li>
                                                            <li>Enter following details in the next step:</li>
                                                            <ol class="features-ul">
                                                                <li>Application Name</li>
                                                                <li>Application URL</li>
                                                                <li>Name of Division</li>
                                                                <li>Operating System</li>
                                                                <li>Server Location from drop-down list</li>
                                                                <li>Port (select either of the options)</li>
                                                                <li>Sender ID</li>
                                                                <li>MX of the domain</li>
                                                                <li>Total no. of emails to be sent</li>
                                                                <li>Mail Type (select from the options provided)</li>
                                                            </ol>
                                                            <img src="assets/home-page/img/manual_image/smtp_03.png" alt="smtp_03.png" class="img-responsive mt-2 mb-2" />
                                                            <li>Select the point of contact of the application as YES or NO. Fill in the name, email, mobile number and landline no. (Not mandatory) fields. Then, enter the correct Captcha value and click on Preview and Submit button to proceed for further step.</li>
                                                            <img src="assets/home-page/img/manual_image/smtp_04.png" alt="smtp_04.png" class="img-responsive mt-2" />
                                                            <li>For the further procedure, refer<text style="color:red"> “Email (@gov) service section”</text>.</li>
                                                        </ol>

                                                    </div>
                                                    <div id="update_profile" class="paragraph-sty">
                                                        <h2>Update Profile in (@gov)</h2>
                                                        <p>Update Mobile & Profile service is one of the crucial services available on the eForms portal for all the users whose email addresses are registered in NIC as the profile of the user is the most important attribute with which the user’s authenticity is verified in the eForms. In addition to this, also multi- factor authentication through OTP validation is done on user’s valid mobile number by using this service. </p>
                                                        <p>Hence, any user who wants to update his/her mobile as well as profile in NIC central repository can simply go to the eForms portal and follow the procedure (as explained in further section) for the same.</p>
                                                        <h4><b>Steps to Avail the Service</b> </h4>
                                                        <p>All the users have to follow below given procedure in order to avail the update mobile & profile  </p>
                                                        <ol class="features-ul">

                                                            <li>Enter<a href="https://eforms.nic.in"> https://eforms.nic.in</a> or <a href="https://eforms.nic.in/update-mobile">https://eforms.nic.in/update-mobile</a> on any web browser.</li>
                                                            <li> Click on the “Update Profile” link available on the eForms.</li>
                                                            <img src="assets/home-page/img/manual_image/user_01.png" alt="users_01.png" class="img-responsive mt-2 mb-2" />
                                                            <li>Read all the instructions carefully given on the left side panel. Click on the confirmation box (marked in red color). Enter your Gov/NIC email address and then click on Next button.  </li>
                                                            <img src="assets/home-page/img/manual_image/users_02.png" alt="user_02.png" class="img-responsive" />
                                                            <br><br>
                                                            <img src="assets/home-page/img/manual_image/users_03.png" alt="user_03.png" class="img-responsive mt-2 mb-2" />
                                                            <li>On the Verify Password Console, enter your email password as well as the correct captcha value and move ahead by clicking on Next button.</li>
                                                            <img src="assets/home-page/img/manual_image/users_04.png" alt="user_04.png" class="img-responsive mt-2 mb-2" />
                                                            <li>On the next screen, you will be shown your current mobile number. Now, enter your new mobile number in the given field and click on Next button</li>
                                                            <img src="assets/home-page/img/manual_image/users_13.PNG" alt="user_02.png" class="img-responsive" />
                                                            <li>Now enter the OTP which you have received on your new mobile number and subsequently click 
                                                                on Next button.</li>
                                                            <p>If you haven’t received OTP, then you can use Resend OTP button to get another OTP.</p

                                                        </ol>
                                                        <img src="assets/home-page/img/manual_image/users_20.PNG" alt="user_02.png" class="img-responsive" />
                                                        <h4 id="kavach_user" class="mt-4 mb-4">For Kavach Registered Users </h4>
                                                        <ol start="7">
                                                            <li>As you are already user authenticated through Kavach, you will be redirected to the Update Profile in (@gov) form in eForms. Here, your new mobile number, date of birth, date of retirement and designation will be auto-filled. Enter your display name and select the reason from the drop down. Fill in the correct captcha value and click on Preview and Submit button.</li>
                                                            <!--<p class="highlight-div paragraph-sty mt-2"><b>NOTE:</b> Mobile Number, Date of Birth, Date of Retirement and Designation are editable fields. If you wish to change any of these attributes, you can do the same by entering the details on the respective fields.</p>-->
                                                            <img src="assets/home-page/img/manual_image/user_07.png" alt="user_07.png" class="img-responsive mt-2 mb-2" />
                                                            <li>You will be shown the Preview of your application. Here you can check all the form details thoroughly and click on “I agree to terms and conditions” box followed by Submit button.</li>
                                                            <img src="assets/home-page/img/manual_image/user_08.png" alt="user_08.png" class="img-responsive mt-2 mb-2" /><br>
                                                            <img src="assets/home-page/img/manual_image/user_09.png" alt="user_09.png" class="img-responsive mt-2 mb-2" />
                                                            <li>Your request will be submitted to your Reporting Officer. </li>
                                                            <img src="assets/home-page/img/manual_image/user_10.png" alt="user_10.png" class="img-responsive mt-2 mb-2" style="height:200px; width:300px" />
                                                            <li>Select the submission type from the provided options and click on Continue button.</li>
                                                            <img src="assets/home-page/img/manual_image/user_11.png" alt="user_11.png" class="img-responsive mt-2 mb-2" />
                                                            <li>You will get the notification message of submission of the request. You can use TRACK link to know the status of your application. In case of Mobile & Profile, you will get registration no. as MOBILE-FORMXXXXXXXXXXXX and for Profile; you will get registration no. as PROFILE-FORMXXXXXXXXXXXX</li>
                                                            <img src="assets/home-page/img/manual_image/user_12.png" alt="user_12.png" class="img-responsive mt-2 mb-2" />
                                                            <br>
                                                            <img src="assets/home-page/img/manual_image/users_10.png" alt="users_10.png" class="img-responsive mt-2 mb-2" />
                                                            <li>Once your request will be approved by the Reporting Officer, it will move to the DA-Admin or else, it will move to the NIC Coordinator and then to the INOC Support Admin<a href="support@nic.in"> (support@nic.in)</a> who will update your mobile and profile details (as per your requirements) in NIC central repository. </li> 
                                                        </ol>

                                                        <h4 id="nokavach_user" class="mt-4 mb-4">For Kavach Non-Registered Users</h4>
                                                        <ol start="7">
                                                            <li>Your email address and new mobile number will be prefilled. Select the title from the drop down, enter your first name, middle name (if applicable) and last name & rest of the details will be pre-filled (if you wish to edit any of details, you can do the same). </li>
                                                            <p>Choose either of the option such as – Self/On Behalf and select the reason for updating mobile number from the drop down and then click on Submit button. </p>
                                                            <img src="assets/home-page/img/manual_image/users_11.png" alt="users_11.png" class="img-responsive mt-2 mb-2" />
                                                            <br>
                                                            <img src="assets/home-page/img/manual_image/users_12.png" alt="users_12.png" class="img-responsive mt-2 mb-2" />
                                                            <li>A confirmation box will appear on the next page. Click on Yes button to move further.  </li>
                                                            <img src="assets/home-page/img/manual_image/user_14.png" alt="user_14.png" class="img-responsive mt-2 mb-2" style="height:600px; width:400px" />
                                                            <li>You will be redirected to CDAC portal for eSign through Aadhaar. Enter your valid Aadhaar Number and click on Get OTP button.</li>
                                                            <img src="assets/home-page/img/manual_image/user_15.png" alt="user_15.png" class="img-responsive mt-2 mb-2" />
                                                            <li>On the next page, enter OTP received on your mobile number, click on the consent box and then on Submit button.</li>
                                                            <img src="assets/home-page/img/manual_image/user_16.png" alt="user_16.png" class="img-responsive mt-2 mb-2" />

                                                        </ol>


                                                    </div>
                                                    <div id="vpn_service" class="paragraph-sty">
                                                        <h2>VPN Service</h2>
                                                        <p>This registration form is designed for the applicants who require a Virtual Private Network to access 
                                                            Intranet for NIC services</p>
                                                        <p>Users can select from the request options provided for the Type of User such as:</p>
                                                        <ol>
                                                            <li>new</li>
                                                            <li>Add/Delete IP Address to Existing</li>
                                                            <li>Renew</li>
                                                            <li>Surrender</li>
                                                        </ol>
                                                        <h4>New Request</h4>
                                                        <p>Under this request, users can fill in new request for getting VPN IP to access the intranet on their
                                                            workstations. As shown in the screenshot below, all the mandatory information is required to be 
                                                            filled by the user. After which the preview of the application will be shown to the user and the final submission will be done to the Reporting Officer.
                                                        </p>
                                                        <h4>For Single IP</h4>
                                                        <ol class="feature-ul">
                                                            <li>For single IP request, the user has to enter IP address of the server, application URL,
                                                                destination port and choose the server location from the drop-down list.
                                                            </li>
                                                            <li>Enter the correct Captcha value and click on Preview and Submit button.</li>
                                                            <li>The remaining process from the preview of the application till final submission remainsthe 
                                                                same as mentioned in “Email (@gov) service section”. The registration number for this
                                                                service will be unique every time the user fills in new request.</li>

                                                        </ol>
                                                        <img src="assets/home-page/img/manual_image/single_ip_01.PNG" alt="user_16.png" class="img-responsive mt-2 mb-2" />
                                                        <h4>For IP Range</h4>
                                                        <ol class="feature-ul">
                                                            <li>For IP range request, the user has to enter IP range (from), IP range (to), application URL,
destination port and choose the server location from the drop-down list.</li>
                                                            <li>Enter the correct Captcha value and click on Preview and Submit button.</li>
                                                            <li>Rest of the process from the preview of the application till final submission remains the
same as mentioned in “Email (@gov) service section”. The user will receive a unique
registration number for this service also that can be used to track the request.
</li>
<img src="assets/home-page/img/manual_image/single_ip_02.PNG" alt="user_16.png" class="img-responsive mt-2 mb-2" />
                                                        </ol>
                                                        <h4>Add/Delete IP Address to Existing</h4>
                                                        <p>Users can add or delete the IP address to the already existing VPN IP by using this option wherein 
after clicking on radio button for this request type, a pop up window will appear as shown in the
screenshot below.</p>
                                                        <p>The user can follow below given steps:
</p>
<ol class="feature-ul">
    <li>In the pop-up window, select the already existing VPN Registration Number from the drop
down list for which user wants to add/delete IP address to existing.
</li>
 <img src="assets/home-page/img/manual_image/single_ip_03.PNG" alt="user_16.png" class="img-responsive mt-2 mb-2" />
 <li>If you want to add IP address on same VPN registration number, click on “Add New” button
and you will be redirected to the screen as shown in point i.
</li>
 <img src="assets/home-page/img/manual_image/single_ip_04.PNG" alt="user_16.png" class="img-responsive mt-2 mb-2" />
 <li> If you want to delete IP address for the same VPN registration number, then choose any of
the check box and click on Continue button. The selected entry will be displayed on the VPN
form. Add remarks if required.
</li>
<p>Enter the correct Captcha value and click on Preview and Submit button. The remaining
process till final submission remains the same as mentioned in “Email (@gov) service section”.</p>
<img src="assets/home-page/img/manual_image/single_ip_05.PNG" alt="user_16.png" class="img-responsive mt-2 mb-2" />
<img src="assets/home-page/img/manual_image/single_ip_06.PNG" alt="user_16.png" class="img-responsive mt-2 mb-2" />

</ol>
<h4>Renew</h4>
<p>Similarly, users can choose this option provided in the VPN Service Form. After clicking on this
option, a pop-up window will appear as shown in given picture below. User will choose the VPN 
Registration No. from drop down list which will be displayed on the screen, against which Renew
Request needs to be submitted, followed by clicking on Renew button.</p>
<img src="assets/home-page/img/manual_image/single_ip_07.PNG" alt="user_16.png" class="img-responsive mt-2 mb-2" />
<img src="assets/home-page/img/manual_image/single_ip_08.PNG" alt="user_16.png" class="img-responsive mt-2 mb-2" />
<h4>Surrender</h4>
<p>This VPN request is for the users who wish to surrender their existing VPN IP. Users have to
choose this option first as provided in the VPN form, followed by selecting VPN Registration No. 
from the drop down list displayed on the screen. After which, verifying and clicking on Surrender
button, the request will be submitted.</p>
            <img src="assets/home-page/img/manual_image/single_ip_09.PNG" alt="user_16.png" class="img-responsive mt-2 mb-2" />
<img src="assets/home-page/img/manual_image/single_ip_10.PNG" alt="user_16.png" class="img-responsive mt-2 mb-2" />                                            
                                                    </div>
                                                    <div id="wifi_service" class="paragraph-sty">
                                                        <h2>Wifi Service</h2>
                                                        <p>This registration form is designed to access NIC WIFI service to use internet. For every user maximum 4 devices are allowed for enabling WIFI services.</p>
                                                        <p>To avail this service the user will have to follow below steps:</p>
                                                        <ol class="features-ul">
                                                            <li>Login to the eForms portal with valid credentials and OTP validation.</li>
                                                            <li>Click on WIFI Service under OUR SERVICES tab in the dashboard.</li>
                                                            <li>You will see WIFI registration form on the screen.</li>
                                                            <li>Select any of the options from WIFI request details as per your requirements:
                                                                <ol class="features-ul">
                                                                    <li>WIFI Request</li>
                                                                    <li>Delete</li>  
                                                                    <li>WIFI Certificate</li>
                                                                </ol>
                                                            </li>
                                                            <p>If you wish to add new WIFI request, then click on WIFI Request radio button, enter maximum 
of 4 MAC addresses. Enter the correct Captcha value and click on Preview andSubmit button.
</p>
                                                            <img src="assets/home-page/img/manual_image/email1_01.png" alt="email1_01.png" class="img-responsive mt-3" />
                                                            <img src="assets/home-page/img/manual_image/email1_02.png" alt="email1_02.png" class="img-responsive mt-3" />
                                                            <ul class="features-ul">
                                                                <li>If you want to delete any of the MAC addresses, choose Delete radio button, your MAC address along with the operating system will be auto filled. Subsequently, check in DELETE check box, enter the correct Captcha value and click on Preview and Submit button.</li>
                                                                <img src="assets/home-page/img/manual_image/email1_03.png" alt="email1_03.png" class="img-responsive mt-3" />
                                                                <li>If you require WIFI certificate, click on the desired radio button followed by entering the correct Captcha value and clicking on Preview and Submit button.</li>
                                                                <img src="assets/home-page/img/manual_image/email1_04.png" alt="email1_04.png" class="img-responsive mt-3" />
                                                            </ul>
                                                            <li>The process from preview of the application form till final submission will remain the same (refer “Email (@gov) service section). A unique registration number for WIFI request will be generated and the user can use that number to track the request whenever he/she wants.</li>

                                                        </ol>

                                                    </div>
                                                    <div id="wifi_port_service" class="paragraph-sty">
                                                        <h2>Wifi Porting Service</h2>
                                                        <p>This registration form is designed for the users to access NIC Wifi Porting Service, where users can manually add the following information and submit the request:</p>
                                                        <ol class="features-ul">
                                                            <li>Source IP Address</li> 
                                                            <li>Destination IP Address</li>
                                                            <li>Port</li>
                                                            <li>Select the service type such as – TCP, UDP, ICMP</li>
                                                            <li>Select Action as Permit/Deny</li>
                                                            <li>Select Time Period</li>
                                                            <li>Add the request in a row</li>
                                                        </ol>
                                                        <p>Users can mention the Purpose of the request. Check the declaration box and Click on Preview and Submit button as shown in the screenshot below.</p>
                                                        <p>For remaining process till final submission <text style="color:red">“refer Email (@gov) service section”</text>.</p>
                                                        <img src="assets/home-page/img/manual_image/email1_05.png" alt="email1_05.png" class="img-responsive mt-3" />
                                                    </div>
                                                    <div id="da_onboard" class="paragraph-sty">
                                                        <h2>DA Onboarding</h2>
                                                        <p>This registration form is specifically designed for the users who wish to become the Delegated Administrator of their organization (Govt department/institutes/organization/PSU). In order to have access to DA console, user has to fill and submit this request from eForms.</p>
                                                        <div class="row col-md-10">
                                                            <div class="col-md-4 ml-5">
                                                                <img src="assets/home-page/img/manual_image/da_01.png" alt="da_01.png" class="img-responsive mt-3" style="height:400px; width:200px" /> 
                                                            </div>
                                                            <div class="col-md-6">
                                                                <p>Steps to fill DA Onboarding request: </p>
                                                                <ol class="features-ul">
                                                                    <li>Login with Parichay (SSO) on eforms portal with valid UserID and Password.</li>
                                                                    <li>You will be redirected to the home page of the eForms.</li>
                                                                    <li>From left side panel, select DA Onboarding form available under “Our Services” tab.</li>
                                                                    <p class="col-md-12 highlight-div paragraph-sty mt-2"><span style="color:red;font-weight: 600;">Note:</span>  Users must have VPN IP in order to fill in DA Onboarding request.</p>   
                                                                    <p class="col-md-12 highlight-div paragraph-sty mt-2"><span style="color:red;font-weight: 600;">Note:</span> Users not having NIC Coordinators, their request will move to the NIC Support Team first and then to their NIC Coordinator.</p> 
                                                                </ol>
                                                            </div>
                                                        </div>
                                                        <ol class="features-ul" start="4">
                                                            <li>Read all the instructions carefully before filling the form.</li>
                                                            <li>Choose any of given Eligibility options such as-
                                                                <ul class="features-ul">
                                                                    <li>Govt department/institutions/organization</li>
                                                                    <li>PSU</li>
                                                                </ul>
                                                            </li>
                                                        </ol>
                                                        <h4 id="vpn01_ip"><b>Users with VPN IP</b></h4>
                                                        <ol class="features-ul">
                                                            <li>Read all the instructions carefully before filling the form.</li>
                                                            <li> Choose any of given Eligibility options such as-</li>
                                                            <p>Govt department/institutions/organization</p>
                                                            <p>PSU</p>
                                                           
                                                            <li> Your VPN IP will be prefilled. Also, check in the box marked in red color displayed on the screen.</li>
                                                            <li>Enter the correct Captcha value and click on Preview and Submit button.</li>
                                                             <img src="assets/home-page/img/manual_image/single_ip_11.PNG" alt="da_03.png" class="img-responsive mt-3" />
                                                             <li> You will be shown the preview of your application form. Check all the details and click on 
“I agree to terms and conditions”. 
</li>
                                                            <img src="assets/home-page/img/manual_image/da_03.png" alt="da_03.png" class="img-responsive mt-3" />
                                                            <img src="assets/home-page/img/manual_image/da_04.png" alt="da_04.png" class="img-responsive mt-3" />
                                                            <li>Click on YES button to submit your request to your Reporting Officer. </li>
                                                            <img src="assets/home-page/img/manual_image/da_05.png" alt="da_05.png" class="img-responsive mt-3" style="heigth:600px; width:400px"  /> 
                                                            <li>Click on Continue button and then YES button to proceed further with manual uploading of your form.</li>
                                                            <img src="assets/home-page/img/manual_image/da_06.png" alt="da_06.png" class="img-responsive mt-3" /> 
                                                            <img src="assets/home-page/img/manual_image/da_07.png" alt="da_07.png" class="img-responsive mt-3" style="heigth:600px; width:400px" /> 
                                                            <li>Your form will be submitted and a unique registration number will be generated. Now you have to 
                                                                click on Download PDF button to get it sealed and signed by your Competent Authority and then 
                                                                Upload the same PDF of the form on the eForms again.</li>
                                                            <img src="assets/home-page/img/manual_image/da_08.png" alt="da_08.png" class="img-responsive mt-3" /> 
                                                        </ol>
                                                       
                                                    </div>    
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <jsp:include page="include/manual_assets_include/footer.jsp" />

            <style>
                html, body {
                    scroll-padding-top: 100px; /* height of sticky header */
                }
            </style>