<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Insert title here</title>

        <script type="text/javascript">
            function proceed() {

                document.getElementById("esignForm").submit();

            }
        </script>

    </head>
    <body onload="proceed()">

        <s:form method="post" id="esignForm" action="%{gatewayURL}">
            <s:hidden name="xml" id="xml" value="%{xmlContent}"></s:hidden>       
            <s:hidden name="clientrequestURL" value="https://eforms.nic.in/eSignResponsePageForRetired"></s:hidden>
            <s:hidden name="username" value="%{userName}"></s:hidden>
        </s:form>
        <s:hidden id="gatewayURL" value="%{gatewayURL}"></s:hidden>
    </body>   
</html>