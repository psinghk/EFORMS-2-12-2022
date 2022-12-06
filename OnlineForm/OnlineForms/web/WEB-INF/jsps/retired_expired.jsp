<head>
    <meta charset="utf-8">
    <title>@Gov.in | e-Forms</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">
    <link href="assets/home-page/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/home-page/lib/bootstrap/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/home-page/update-mobile/css/style.css">
    <link rel="stylesheet" href="assets/home-page/update-mobile/css/navigation.css">
    <style>
        .circle,
        .circle-border {
            width: 80px;
            height: 80px;
            border-radius: 50%;
        }
        .circle {
            z-index: 1;
            position: relative;
            background: white;
            transform: scale(1);
            animation: success-anim 700ms ease;
        }
        .circle-border {
            z-index: 0;
            position: absolute;
            transform: scale(1.1);
            animation: circle-anim 400ms ease;
            background: #f86;	
        }
        @keyframes success-anim {
            0% {
                transform: scale(0);
            }
            30% {
                transform: scale(0);
            }
            100% {
                transform: scale(1);
            }
        }
        @keyframes circle-anim {
            from {
                transform: scale(0);
            }
            to {
                transform: scale(1.1);
            }
        }
        .error::before,
        .error::after {
            content: "";
            display: block;
            height: 7px;
            background: #f86;
            position: absolute;
        }
        .error::before {
            width: 50px;
            top: 48%;
            left: 20%;
            transform: rotateZ(50deg);
        }
        .error::after {
            width: 50px;
            top: 48%;
            left: 20%;
            transform: rotateZ(-50deg);
        }
        #wizard {
            padding: 25px !important;
            min-height: 350px !important;
            margin-right: 0px;
            font-size: 16px;
             margin-bottom: 150px;
        }
        #clients {
            padding: 30px 0 30px !important;
        }
    </style>
</head>
<body>
    <div class="loader"><img src="assets/images/loader-1.gif" alt="" /></div>
    <header id="header" class="fixed-top">
        <div class="container">
            <div class="logo float-left">
                <a class="scrollto" href="https://eforms.nic.in/"><img src="assets/home-page/img/eforms-logo.png" alt="" class="img-fluid"></a>
            </div>
        </div>
    </header>
    <div class="wrapper display-show" id="retired_basic">
        <div class="col-md-6 m-auto">
            <div id="wizard">
                <div style="text-align: center; padding: 30px 0px 0px 265px;">
                    <div class="circle-border"></div>
                    <div class="circle">
                        <div class="error"></div>
                    </div>
                </div>
                <h5 class="text-center mt-5 mb-4 text-danger">Your account has already been expired. To extend, please contact https://servicedesk.nic.in or call at Toll Free number 1800111555</h5>
                <div style="overflow:auto;" class="mt-2">
                    <div class="col-md-12 p-0" style="text-align: center;">
                        <button type="button" class="btn btn-primary btn-action" id="submit"><a href="https://eforms.nic.in/" style="color: #ffff;">Go To Homepage</a></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!--==========================
Clients Section
============================-->
<section id="clients" class="section-bg section-bg-logo">
    <div class="container">
        <ul class="list-unstyled partners-icon row">
            <li class="col-md-2 col-4">
                <a href="https://india.gov.in/" target="_blank"><img src="assets/img/footer/new/india_gov.png"></a>
            </li>
            <li class="col-md-2 col-4 border-left border-right">
                <a href="http://nkn.gov.in/" target="_blank"><img src="assets/img/footer/new/nkn.png"></a>
            </li>
            <li class="col-md-2 col-4 border-right">
                <a href="http://meity.gov.in/" target="_blank"><img src="assets/img/footer/new/meity.png"></a>
            </li>
            <li class="col-md-2 col-4 border-right mt-md-0 mt-3">
                <a href="http://www.digitalindia.gov.in/" target="_blank"><img src="assets/img/footer/new/digital_india.png"></a>
            </li>
            <li class="col-md-2 col-4 border-right mt-md-0 mt-3">
                <a href="https://email.gov.in/" target="_blank"><img src="assets/img/footer/new/gov.png"></a>
            </li>
            <li class="col-md-2 col-4 mt-md-0 mt-3">
                <a href="https://mygov.in/" target="_blank"><img src="assets/img/footer/new/mygov.png"></a>
            </li>
        </ul>
    </div>
</section>
</main>

<!--==========================
Footer
============================-->
<footer id="footer">
    <div class="container">
        <div class="copyright">
            Designed and Developed by Messaging Division <br />
            <span id="year"></span>&nbsp;&copy; Copyright <a href="https://www.nic.in/" style="color:#fff;" target="_blank" title="External Link"><strong>National Informatics Centre</strong></a>.
        </div>
        <div class="credits">
            All Rights Reserved
        </div>
    </div>
</footer>
</body>
</html>


