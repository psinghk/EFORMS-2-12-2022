<%
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Expires", "0");
    response.setDateHeader("Expires", -1);
    response.setHeader("X-Frame-Options", "DENY");
    response.setStatus(HttpServletResponse.SC_FOUND);
    response.addHeader("X-Content-Type-Options", "nosniff");
    response.addHeader("X-XSS-Protection", "1; mode=block");
    String uri = request.getRequestURI();
    String url = request.getRequestURL().toString();
    String ctxPath = request.getContextPath();
    url = url.replaceFirst(uri, "");
    url = url + ctxPath;

%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1"><title>404 HTML Template by Colorlib</title>
        <link href="<%= url%>/assets/css/login.min.css" rel="stylesheet" type="text/css" />
        <style type="text/css">
            * {
                -webkit-box-sizing: border-box;
                box-sizing: border-box;
            }

            body {
                padding: 0;
                margin: 0;
            }

            #notfound {
                position: relative;
                height: 100vh;
            }

            #notfound .notfound {
                position: absolute;
                left: 50%;
                top: 45%;
                -webkit-transform: translate(-50%, -50%);
                -ms-transform: translate(-50%, -50%);
                transform: translate(-50%, -50%);
            }

            .notfound {
                max-width: 767px;
                width: 100%;
                line-height: 1.4;
                padding: 0px 15px;
            }

            .notfound .notfound-404 {
                position: relative;
                height: 150px;
                line-height: 150px;
                margin-bottom: 25px;
            }

            .notfound .notfound-404 h1 {
                font-family: 'Titillium Web', sans-serif;
                font-size: 186px;
                font-weight: 900;
                margin: 0px;
                text-transform: uppercase;
                background: url('<%= url%>/assets/img/text.png');
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                background-size: cover;
                background-position: center;
            }

            .notfound h2 {
                font-family: 'Titillium Web', sans-serif;
                font-size: 26px;
                font-weight: 700;
                margin: 0;
            }

            .notfound p {
                font-family: 'Montserrat', sans-serif;
                font-size: 14px;
                font-weight: 500;
                margin-bottom: 0px;
                text-transform: uppercase;
            }

            .notfound a {
                font-family: 'Titillium Web', sans-serif;
                display: inline-block;
                text-transform: uppercase;
                color: #fff;
                text-decoration: none;
                border: none;
                background: #5c91fe;
                padding: 10px 40px;
                font-size: 14px;
                font-weight: 700;
                border-radius: 1px;
                margin-top: 15px;
                -webkit-transition: 0.2s all;
                transition: 0.2s all;
            }

            .notfound a:hover {
                opacity: 0.8;
            }

            @media only screen and (max-width: 767px) {
                .notfound .notfound-404 {
                    height: 110px;
                    line-height: 110px;
                }
                .notfound .notfound-404 h1 {
                    font-size: 120px;
                }
            }

        </style>

    </head>
    <body>
        <div id="notfound">
            <div class="notfound">
                <div class="notfound-404">
                    <h1>404</h1>
                </div>
                <h2>Oops! This Page Could Not Be Found</h2>
                <p>Sorry but the page you are looking for does not exist, have been removed. name changed or is temporarily unavailable</p>
                <a href="<%= url%>">Go To Homepage</a>
            </div>
        </div>
        <footer class="login-footer" style="position: fixed; width: 100%; background: #000000; padding: 14px; bottom: 0px; right: 0px; margin-bottom: 0px;">
            <section class="container foot">
                <div class="row">
                    <div class="col-lg-12 col-md-12 col-xs-12" style="text-align:center">
                        <a href="https://india.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/india_gov.png"></a>
                        <a href="https://www.pmindia.gov.in/en/" target="_blank" title="External Link"><img src="assets/img/footer/new/pm_india.png"></a>
                        <a href="https://email.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/gov.png"></a>
                        <a href="http://meity.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/meity.png"></a>
                        <a href="https://egreetings.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/egreeting.png"></a>
                        <a href="https://mygov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/mygov.png"></a>
                        <a href="https://sampark.gov.in/" target="_blank" title="External Link"><img src="assets/img/footer/new/esampark.png"></a>
                    </div>
                </div>
            </section>
        </footer>
    </body>
</html>