<!DOCTYPE html>
<html lang="en">
	<!-- begin::Head -->
	<head>
		<meta charset="utf-8" />
		<title>Maintenance | Work in Progress</title>
		<meta name="description" content="Page not found page examples">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<!--begin::Web font -->
		<link rel="stylesheet" href="assets/home-page/maintenance_sty/assets/css/webfonts.css">
		<link href="assets/home-page/maintenance_sty/assets/demo/default/base/style.bundle.css" rel="stylesheet" type="text/css" />
		<link href="assets/home-page/maintenance_sty/assets/custom/error/404-v1.css" rel="stylesheet" type="text/css" />
	</head>
	<body class="k-bg-light k-header--fixed k-header-mobile--fixed k-aside--enabled k-aside--fixed">
		<div class="k-grid k-grid--ver k-grid--root k-page">
                    <% String url = request.getRequestURI();
                        String[] url_arr = url.split("/");
                        if(url_arr.length == 3 && !url_arr[2].contains("index.jsp")) {
                            response.sendRedirect("index.jsp");
                        }
                    %>
                    
                    <div class="k-error404-v1">
                            <div class="k-error404-v1__content">
                                    <div class="k-error404-v1__title">Work in Progress</div>
                                    <div class="k-error404-v1__desc"><strong>Coming Soon!</strong> Site is Under Maintenance, Sorry for Inconvenience.</div>
                            </div>
                            <div class="k-error404-v1__image">
                                    <img src="assets/media/misc/404-bg1.jpg" class="k-error404-v1__image-content" alt="" title="" />
                            </div>
                    </div>
		</div>
	</body>
</html>