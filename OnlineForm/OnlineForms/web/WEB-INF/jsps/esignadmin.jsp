<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<html>
    <head>
        <meta charset="utf-8" />
        <title>@Gov.in | Support,Reporting/Nodal/Forwarding Officer,Admin | e-Forms</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">        
        <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE"/>
        <META HTTP-EQUIV="EXPIRES" CONTENT="0"/>
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"/>
        <meta content="width=device-width, initial-scale=1" name="viewport" />
        <meta content="" name="description" />
        <meta content="" name="author" />
        <link href="assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/plugins/bootstrap-fileinput/bootstrap-fileinput.css" rel="stylesheet" type="text/css" />
        <link href="assets/plugins/datatables/datatables.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css" rel="stylesheet" type="text/css" />
        <link href="assets/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
        <link href="assets/css/search.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/css/blog.css" rel="stylesheet" type="text/css" />
        <link href="assets/css/layout.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/css/default.min.css" rel="stylesheet" type="text/css" id="style_color" />
        <script src="assets/plugins/jquery.min.js" type="text/javascript"></script>
        <script src="assets/plugins/jquery-ui.js"></script>
        <link href="assets/css/jquery-ui.css" rel="stylesheet" type="text/css" />
        <link href="assets/css/bootstrap-tagsinput.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="assets/css/vpb_uploader.css" rel="stylesheet" />
    
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Insert title here</title>
        <%
            String moduleEsign = session.getAttribute("moduleEsign").toString();
            String ref_num = session.getAttribute("ref_num").toString()+"~"+session.getAttribute("form_type").toString()+"~forward";
            String app_ca_type = "esign";
            String statRemarks = "esign";
            String app_type="esign";
           
            System.out.println("moduleEsign:::::" + moduleEsign);
        %>
           <script type="text/javascript">
            function proceed() {

                var moduleEsign = '<%=moduleEsign%>';
                var ref_num = '<%=ref_num%>';
                var app_ca_type = '<%=app_ca_type%>';
                var statRemarks = '<%=statRemarks%>'
                var app_type = '<%=app_type%>'
                alert(moduleEsign)
                bootbox.confirm({
                    message: "dsadsadsa",
                    buttons: {
                        confirm: {
                            label: 'Yes',
                            className: 'btn-success'
                        },
                        cancel: {
                            label: 'No',
                            className: 'btn-danger'
                        }
                    },
                    callback: function (result) {

                        if (result)
                        {
                            // start, code added by pr on 20thapr18

                            $("#fwd_wo_ad").hide(); // hide create button and show loader img

                            $(".loadergif").show(); // show this image

                            // end, code added by pr on 20thapr18

                            $.post('action',
                                    {
                                        regNo: ref_num,
                                        random: "", // line added by pr on 3rdjan18
                                        //app_user_type:app_type, // line added by pr on 22ndmar18 , not required because it will already be there
                                        app_ca_type: app_ca_type, // line added by pr on 22ndmar18                                
                                        app_ca_path: "",
                                        statRemarks: statRemarks // line added by pr on 10thmay18
                                    },
                                    function (jsonResponse)
                                    {
                                       
                                        // start, code added by pr on 20thapr18

                                        if (app_type != "manual_upload") // if around added by pr on 9thmay18
                                        {

                                            $("#fwd_wo_ad").show(); // hide create button and show loader img
                                        }

                                        $(".loadergif").hide(); // show this image

                                        // end, code added by pr on 20thapr18

                                        if (jsonResponse.isSuccess == true)
                                        {
                                            resetRandom();// line added by pr on 4thjan18
                                            $(".alert-danger").show();
                                            $(".alert-danger").html(jsonResponse.msg);
                                            showAlertBox(jsonResponse.msg) // line modified by pr on 22ndmar19

                                            $("#forward_modal").modal("hide");

                                            $("#approve_modal").modal("hide");

                                            var action = $("#action_hidden").val();

                                            //setAction(action) // line commented by pr on 23rdoct18


                                            // start, code added by pr on 23rdoct18

                                            var table = $('#example').DataTable();

                                            console.log(" just before the datatable reload call ");

                                            table.ajax.reload(null, false);

                                            // end, code added by pr on 23rdoct18


                                            // end, code added by pr on 5thjun18





                                        } else if (jsonResponse.isError == true)
                                        {
                                            resetRandom();// line added by pr on 4thjan18

                                            $(".alert-danger").hide();

                                            //alert(jsonResponse.msg);

                                            showAlertBox(jsonResponse.msg) // line modified by pr on 22ndmar19

                                        }

                                    }, 'json');

                        }

                    }
                });

//                $.ajax({
//                    type: "POST",
//                    url: "action",
//                    data:  {moduleEsign:moduleEsign},
//                    datatype: JSON,
//                    contentType: 'application/json; charset=utf-8',
//                    success: function (data) {
//                         alert(data.isSuccess)
//                    }, error: function () {
//                        console.log('error');
//                    }
//                });





            }
        </script>
    
<script src="js/vpn.js" type="text/javascript"></script>
<script src="js/CA.js" type="text/javascript"></script>
<script src="assets/plugins/jquery.serializeObject.js"></script>
<script type="text/javascript" src="js/vpb_uploader.js"></script>
  <script src="assets/plugins/jquery.min.js" type="text/javascript"></script>
  </head>
     

    
    <body onload="proceed()">

        fdsafdsfdsfds

    </body> 
</html>