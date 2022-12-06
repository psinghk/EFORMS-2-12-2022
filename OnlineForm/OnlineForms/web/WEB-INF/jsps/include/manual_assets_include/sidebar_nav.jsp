
<!DOCTYPE html>

<html lang="en">

    <!-- begin::Head -->
    <head>
        <meta charset="utf-8" />
        <title>e-Forms | Support,Component Authority,Mail-Admin | @Gov.in</title>
        <meta name="description" content="e-Forms | Support,Component Authority,Mail-Admin | @Gov.in">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <link rel="shortcut icon" href="assets/media/logos/favicon.ico" />
        <link href="assets/demo/default/base/style.bundle.css" rel="stylesheet" type="text/css" rel="preload" />
        <link href="assets/css/fonts/webfonts.css" rel="stylesheet" type="text/css" rel="preload" />  
        <link href="assets/demo/default/skins/header/base/light.css" rel="stylesheet" type="text/css" rel="preload" />
        <link href="assets/demo/default/skins/brand/brand.css" rel="stylesheet" type="text/css" rel="preload" />
        <link href="assets/demo/default/skins/aside/brand.css" rel="stylesheet" type="text/css" rel="preload" />
        <link rel="stylesheet" href="assets/custom/custom.css" rel="preload" />
        <link href="assets/old_assets/css/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <link href="assets/vendors/general/perfect-scrollbar/css/perfect-scrollbar.css" rel="stylesheet" type="text/css"/>
        <link href="assets/vendors/custom/datatables/datatables.bundle.css" rel="stylesheet" type="text/css"/>
        <link href="assets/vendors/custom/vendors/fontawesome5/css/all.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/demo/bootstrap-datetime-picker/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" href="assets/custom/datatable/css/responsive.bootstrap.min.css" />

    </head>

    <!-- end::Head -->

    <!-- begin::Body -->
    <body class="k-header--fixed k-header-mobile--fixed k-aside--enabled k-aside--fixed page-wrapper page-container-bg-solid">

        <!-- begin:: Page -->

        <!-- begin:: Header Mobile -->
        <div id="k_header_mobile" class="k-header-mobile  k-header-mobile--fixed ">
            <div class="k-header-mobile__logo">
                <a href="showUserData">
                    <img alt="Logo" src="assets/media/logos/logo-1.png" />
                </a>
            </div>
            <div class="k-header-mobile__toolbar">
                <button class="k-header-mobile__toolbar-toggler k-header-mobile__toolbar-toggler--left" id="k_aside_mobile_toggler"><span></span></button>
                <button class="k-header-mobile__toolbar-toggler" id="k_header_mobile_toggler"><span></span></button>
                <button class="k-header-mobile__toolbar-topbar-toggler" id="k_header_mobile_topbar_toggler"><i class="fa fa-th"></i></button>
            </div>
        </div>
        <!-- end:: Header Mobile -->
        <div class="k-grid k-grid--hor k-grid--root">
            <div class="k-grid__item k-grid__item--fluid k-grid k-grid--ver k-page">

                <!-- begin:: Aside -->
                <button class="k-aside-close " id="k_aside_close_btn"><i class="fa fa-angle-left"></i></button>
                <div class="k-aside  k-aside--fixed k-grid__item k-grid k-grid--desktop k-grid--hor-desktop" id="k_aside">

                    <!-- begin:: Aside -->
                    <div class="k-aside__brand	k-grid__item " id="k_aside_brand">
                        <div class="k-aside__brand-logo">
                            <a href="showUserData">
                                <img alt="Logo" src="assets/media/logos/logo-1.png" />
                            </a>
                        </div>
                        <div class="k-aside__brand-tools">
                            <button class="k-aside__brand-aside-toggler k-aside__brand-aside-toggler--left" id="k_aside_toggler"><span></span></button>
                        </div>
                    </div>

                    <!-- end:: Aside -->

                    <!-- begin:: Aside Menu -->
                    <div class="k-aside-menu-wrapper	k-grid__item k-grid__item--fluid" id="k_aside_menu_wrapper">
                        <div id="k_aside_menu" class="k-aside-menu " data-kmenu-vertical="1" data-kmenu-scroll="1" data-kmenu-dropdown-timeout="500">
                            <ul class="k-menu__nav">
                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#introduction" data-value="eformsintro" class="k-menu__link">
                                        <i class="k-menu__link-icon 	fa fa-bars"></i>
                                        <span class="k-menu__link-text">Introduction</span>
                                    </a>
                                </li>
                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#purpose" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fa fa-window-restore"></i>
                                        <span class="k-menu__link-text">Purpose</span>
                                    </a>
                                </li>

                                <li class="k-menu__item k-menu__item--submenu k-menu__item--here  k-menu__item--hover" aria-haspopup="true" data-kmenu-submenu-toggle="hover">
                                    <a href="javascript:;" class="k-menu__link k-menu__toggle">
                                        <i class="k-menu__link-icon fa fa-flag"></i>
                                        <span class="k-menu__link-text">Scope</span>
                                        <i class="k-menu__ver-arrow fa fa-angle-right"></i>
                                    </a>  
                                    <div class="k-menu__submenu "><span class="k-menu__arrow"></span>
                                        <ul class="k-menu__subnav">
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#scope" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Authorship</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </li>

                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#features" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fab fa-foursquare"></i>
                                        <span class="k-menu__link-text">Features</span>
                                    </a>
                                </li>
                                <!--<li class="k-menu__item " aria-haspopup="true">
                                        <a href="#portal_workflow" data-value="ldap" class="k-menu__link">
                                                <i class="k-menu__link-icon fa fa-edit"></i>
                                                <span class="k-menu__link-text">Portal Workflow</span>
                                        </a>
                                </li>-->

                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#service_tab" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fas fa-server"></i>
                                        <span class="k-menu__link-text">Services Tab</span>
                                    </a>
                                </li>

                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#in_focus" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fa fa-edit"></i>
                                        <span class="k-menu__link-text">In-Focus</span>
                                    </a>
                                </li>
                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#contact_us" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fa fa-address-book"></i>
                                        <span class="k-menu__link-text">Contact Us</span>
                                    </a>
                                </li>
                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#faqs" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fa fa-question-circle"></i>
                                        <span class="k-menu__link-text">FAQS</span>
                                    </a>
                                </li>
                                <!--<li class="k-menu__item " aria-haspopup="true">
                                        <a href="#feedback" data-value="ldap" class="k-menu__link">
                                                <i class="k-menu__link-icon fa fa-envelope-open"></i>
                                                <span class="k-menu__link-text">Feedback</span>
                                        </a>
                                </li>-->

                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#register" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fa fa-receipt"></i>
                                        <span class="k-menu__link-text">HOW TO REGISTER?</span>
                                    </a>
                                </li>

                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#login_console_gov" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fa fa-lock"></i>
                                        <span class="k-menu__link-text">LOGIN (Government)</span>
                                    </a>
                                </li>

                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#login_console_non_gov" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fas fa-sign-in-alt"></i>
                                        <span class="k-menu__link-text">LOGIN (Non-Government)</span>
                                    </a>
                                </li>

                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#first_time_user" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fa fa-user"></i>
                                        <span class="k-menu__link-text">First time User</span>
                                    </a>
                                </li>

                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#existing_user" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fa fa-users"></i>
                                        <span class="k-menu__link-text">EXISTING USER</span>
                                    </a>
                                </li>

                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#home_page" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fa fa-home"></i>
                                        <span class="k-menu__link-text">Home Page</span>
                                    </a>
                                </li>

                                <li class="k-menu__item " aria-haspopup="true">
                                    <a href="#dashboard" data-value="ldap" class="k-menu__link">
                                        <i class="k-menu__link-icon fas fa-tv"></i>
                                        <span class="k-menu__link-text">Dashboard</span>
                                    </a>
                                </li>

                                <li class="k-menu__item k-menu__item--submenu k-menu__item--here  k-menu__item--hover" aria-haspopup="true" data-kmenu-submenu-toggle="hover">
                                    <a href="#dashboard_types" class="k-menu__link k-menu__toggle">
                                        <i class="k-menu__link-icon fa fa-chalkboard-teacher"></i>
                                        <span class="k-menu__link-text">Types of Dashboard</span>
                                        <i class="k-menu__ver-arrow fa fa-angle-right"></i>
                                    </a>
                                    <div class="k-menu__submenu ">
                                        <span class="k-menu__arrow"></span>
                                        <ul class="k-menu__subnav">
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#role_applicant" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Applicant</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#role_secretary" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Undersecretary/JS/Secretary</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#role_reporting_officer" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Reporting officer</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#role_coordinator" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">NIC Coordinator</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#role_support" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Support</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#role_admin" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Admin</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#delegated_admin" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Delegated Admin</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </li>

                                <li class="k-menu__item k-menu__item--submenu k-menu__item--here  k-menu__item--hover" aria-haspopup="true" data-kmenu-submenu-toggle="hover">
                                    <a href="javascript:;" class="k-menu__link k-menu__toggle">
                                        <i class="k-menu__link-icon fa fa-file"></i>
                                        <span class="k-menu__link-text">About Manual</span>
                                        <i class="k-menu__ver-arrow fa fa-angle-right"></i>
                                    </a>  
                                    <div class="k-menu__submenu "><span class="k-menu__arrow"></span>
                                        <ul class="k-menu__subnav">
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#about_process" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Manual process</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#preq_new_user" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">New users</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#preq_ex_user" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Existing users</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </li>

                                <li class="k-menu__item k-menu__item--submenu k-menu__item--here  k-menu__item--hover" aria-haspopup="true" data-kmenu-submenu-toggle="hover">
                                    <a href="javascript:;" class="k-menu__link k-menu__toggle">
                                        <i class="k-menu__link-icon fa fa-cogs"></i>
                                        <span class="k-menu__link-text">Online Process</span>
                                        <i class="k-menu__ver-arrow fa fa-angle-right"></i>
                                    </a>  
                                    <div class="k-menu__submenu "><span class="k-menu__arrow"></span>
                                        <ul class="k-menu__subnav">
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#online_process" class="k-menu__link">
                                                    <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Manual Process</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </li>

                                <li class="k-menu__item k-menu__item--submenu k-menu__item--here  k-menu__item--hover" aria-haspopup="true" data-kmenu-submenu-toggle="hover">
                                    <a href="javascript:;" class="k-menu__link k-menu__toggle">
                                        <i class="k-menu__link-icon fas fa-clone"></i>
                                        <span class="k-menu__link-text">Our Services Tab</span>
                                        <i class="k-menu__ver-arrow fa fa-angle-right"></i>
                                    </a>  
                                    <div class="k-menu__submenu "><span class="k-menu__arrow"></span>
                                        <ul class="k-menu__subnav">
                                            <li class="mb-3 k-menu__item " aria-haspopup="true">
                                                <a href="#our_service_tab" class="k-menu__link">

                                                    <i class="k-menu__link-icon fad fas fa-file-signature">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Authentication Services (LDAP)</span>
                                                </a>
                                            </li>


                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#distribution_service_list" class="k-menu__link">
                                                    <i class="k-menu__link-icon fa fa-user-lock">
                                                        <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Distribution List Services</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item k-menu__item--submenu k-menu__item--here  k-menu__item--hover" aria-haspopup="true" data-kmenu-submenu-toggle="hover">
                                                <a href="javascript:;" class="k-menu__link k-menu__toggle">
                                                    <i class="k-menu__link-icon fa fa-sort-amount-up"></i>
                                                    <span class="k-menu__link-text">DNS Services</span>
                                                    <i class="k-menu__ver-arrow fa fa-angle-right"></i>
                                                </a>  
                                                <div class="k-menu__submenu "><span class="k-menu__arrow"></span>
                                                    <ul class="k-menu__subnav">
                                                        <li class="mb-3 k-menu__item " aria-haspopup="true">
                                                            <a href="#dns_service" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">DNS User Subscription (File upload)</span>
                                                            </a>
                                                        </li>
                                                        <li class="mb-3 k-menu__item " aria-haspopup="true">
                                                            <a href="#dns_manual" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">DNS User Subscription (Manual Entries)</span>
                                                            </a>
                                                        </li>



                                                    </ul>
                                                </div>
                                            </li>
                                            <li class="k-menu__item k-menu__item--submenu k-menu__item--here  k-menu__item--hover" aria-haspopup="true" data-kmenu-submenu-toggle="hover">
                                                <a href="#email_tab" class="k-menu__link k-menu__toggle">
                                                    <i class="k-menu__link-icon fa fa-mail-bulk"></i>
                                                    <span class="k-menu__link-text">Email (@gov)</span>
                                                    <i class="k-menu__ver-arrow fa fa-angle-right"></i>
                                                </a>  
                                                <div class="k-menu__submenu "><span class="k-menu__arrow"></span>
                                                    <ul class="k-menu__subnav">
                                                        <li class="k-menu__item " aria-haspopup="true">
                                                            <a href="#single_user_subs" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">Single User Subscription</span>
                                                            </a>
                                                        </li>
                                                        <li class="k-menu__item " aria-haspopup="true">
                                                            <a href="#bulk_subs" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">Bulk Subscription</span>
                                                            </a>
                                                        </li>
                                                        <li class="k-menu__item " aria-haspopup="true">
                                                            <a href="#nkn_single_subs" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">NKN Single Subscription</span>
                                                            </a>
                                                        </li>
                                                        <li class="k-menu__item " aria-haspopup="true">
                                                            <a href="#nkn_bulk_subs" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">NKN BULK Subscription</span>
                                                            </a>
                                                        </li>
                                                        <li class="k-menu__item k-menu__item--submenu k-menu__item--here  k-menu__item--hover" aria-haspopup="true" data-kmenu-submenu-toggle="hover">
                                                            <a href="javascript:;" class="k-menu__link k-menu__toggle">
                                                                <i class="k-menu__link-icon fa fa-chalkboard-teacher"></i>
                                                                <span class="k-menu__link-text">GEM Subscription</span>
                                                                <i class="k-menu__ver-arrow fa fa-angle-right"></i>
                                                            </a>  
                                                            <div class="k-menu__submenu "><span class="k-menu__arrow"></span>
                                                                <ul class="k-menu__subnav">
                                                                    <li class="k-menu__item " aria-haspopup="true">
                                                                        <a href="#gem_subs" class="k-menu__link">
                                                                            <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                                <span></span>
                                                                            </i>
                                                                            <span class="k-menu__link-text">Primary Users</span>
                                                                        </a>
                                                                    </li>
                                                                    <li class="k-menu__item " aria-haspopup="true">
                                                                        <a href="#sec_subs" class="k-menu__link">
                                                                            <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                                <span></span>
                                                                            </i>
                                                                            <span class="k-menu__link-text">Secondary Users</span>
                                                                        </a>
                                                                    </li>
                                                                </ul>
                                                            </div>
                                                        </li>
                                                        <li class="k-menu__item " aria-haspopup="true">
                                                            <a href="#email_act" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">Email Active</span>
                                                            </a>
                                                        </li>
                                                        <li class="k-menu__item " aria-haspopup="true">
                                                            <a href="#email1_deact" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">Email De-Activate</span>
                                                            </a>
                                                        </li>

                                                    </ul>

                                                </div>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#central_utm_service" class="k-menu__link">
                                                    <i class="k-menu__link-icon fa fa-user-shield"></i>
                                                    <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Extend the Validity of Account</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#imap_pop" class="k-menu__link">
                                                    <i class="k-menu__link-icon fa fa-recycle"></i>
                                                    <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">IMAP/POP</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#ip_change_request" class="k-menu__link">
                                                    <i class="k-menu__link-icon fa fa-signal"></i>
                                                    <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">IP Change Request</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#sms_service" class="k-menu__link">
                                                    <i class="k-menu__link-icon fa fa-envelope-open"></i>
                                                    <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">SMS Services</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#smtp_gateway" class="k-menu__link">
                                                    <i class="k-menu__link-icon fa fa-server"></i>
                                                    <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">SMTP Gateway</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item k-menu__item--submenu k-menu__item--here  k-menu__item--hover" aria-haspopup="true" data-kmenu-submenu-toggle="hover">
                                                <a href="#update_profile" class="k-menu__link k-menu__toggle">
                                                    <i class="mb-3 k-menu__link-icon fa fa-mobile-alt"></i>
                                                    <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Update Profile (@gov)</span>
                                                    <i class="k-menu__ver-arrow fa fa-angle-right"></i>
                                                </a>
                                                <div class="k-menu__submenu "><span class="k-menu__arrow"></span>
                                                    <ul class="k-menu__subnav">
                                                        <li class="k-menu__item " aria-haspopup="true">
                                                            <a href="#mobile_user" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">Mobile & Profile</span>
                                                            </a>
                                                        </li>
                                                    <li class="k-menu__item " aria-haspopup="true">
                                                            <a href="#profile_user" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">Profile</span>
                                                            </a>
                                                        </li>
                                                        <li class="mb-3 mt-3 k-menu__item " aria-haspopup="true">
                                                            <a href="#kavach_user" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">Kavach Registered User</span>
                                                            </a>
                                                        </li>
                                                        <li class="k-menu__item " aria-haspopup="true">
                                                            <a href="#nokavach_user" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">Kavach Non-Registered User</span>
                                                            </a>
                                                        </li>

                                                    </ul>
                                                </div>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#vpn_service" class="k-menu__link">
                                                    <i class="k-menu__link-icon fa fa-sitemap"></i>
                                                    <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">VPN Service</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#wifi_service" class="k-menu__link">
                                                    <i class="k-menu__link-icon fa fa-wifi"></i>
                                                    <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Wifi Service</span>
                                                </a>
                                            </li>
                                            <li class="k-menu__item " aria-haspopup="true">
                                                <a href="#wifi_port_service" class="k-menu__link">
                                                    <i class="k-menu__link-icon fab fa-megaport"></i>
                                                    <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">Wifi Porting Services</span>
                                                </a>
                                            </li>
                                            <!--                                <li class="k-menu__item " aria-haspopup="true">
                                                                                <a href="#da_onboard" class="k-menu__link">
                                                                                    <i class="k-menu__link-icon fa fa-wifi"></i>
                                                                                        <span></span>
                                                                                    </i>
                                                                                    <span class="k-menu__link-text">DA Onboarding</span>
                                                                                </a>
                                                                            </li>-->
                                            
                                            <li class="k-menu__item k-menu__item--submenu k-menu__item--here  k-menu__item--hover" aria-haspopup="true" data-kmenu-submenu-toggle="hover">
                                                <a href="#da_onboard" class="k-menu__link k-menu__toggle">
                                                    <i class="k-menu__link-icon fa fa-address-card"></i>
                                                    <span></span>
                                                    </i>
                                                    <span class="k-menu__link-text">DA Onboarding</span>
                                                    <i class="k-menu__ver-arrow fa fa-angle-right"></i>
                                                </a>
                                                <div class="k-menu__submenu"><span class="k-menu__arrow"></span>
                                                    <ul class="k-menu__subnav">
                                                        <li class="k-menu__item " aria-haspopup="true">
                                                            <a href="#vpn01_ip" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">Users with VPN IP</span>
                                                            </a>
                                                        </li>
                                                        <li class="k-menu__item " aria-haspopup="true">
                                                            <a href="#vpn1_ip" class="k-menu__link">
                                                                <i class="k-menu__link-bullet k-menu__link-bullet--dot">
                                                                    <span></span>
                                                                </i>
                                                                <span class="k-menu__link-text">Users without VPN IP</span>
                                                            </a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </li>



















                                <!--<li class="k-menu__item " aria-haspopup="true">
                                    <a href="#vpn_service" class="k-menu__link">
                                        <i class="k-menu__link-icon fa fa-sitemap"></i>
                                            <span></span>
                                        </i>
                                        <span class="k-menu__link-text">VPN Service</span>
                                    </a>
                                </li>-->

                                <!--<li class="k-menu__item " aria-haspopup="true">
                                    <a href="#wifi_service" class="k-menu__link">
                                        <i class="k-menu__link-icon fa fa-wifi"></i>
                                            <span></span>
                                        </i>
                                        <span class="k-menu__link-text">Wifi Service</span>
                                    </a>
                                </li>-->
                            </ul>
                        </div>
                    </div>
                    <!-- end:: Aside Menu -->
                </div>
                <div class="k-grid__item k-grid__item--fluid k-grid k-grid--hor k-wrapper" id="k_wrapper">

                    <style>

                    </style>