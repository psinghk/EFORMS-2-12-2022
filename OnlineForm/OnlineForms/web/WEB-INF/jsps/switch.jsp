<%@page import="com.org.bean.UserData"%>
<%@page import="admin.ForwardAction"%>
<%@page import="java.util.List"%>
<%@page import="com.org.bean.Forms"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.org.utility.Constants"%>
<%@page import="admin.UserTrack"%> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib  uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<%
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("X-Frame-Options", "DENY");
    response.addHeader("X-Content-Type-Options", "nosniff");
    response.addHeader("X-XSS-Protection", "1; mode=block");
    String url = request.getRequestURL().toString();
%>
<script>
    $(document).ready(function () {
        var admin_value = '<s:property value="#adminValue" />';
      if(admin_value == "admin") {
                                        window.location.href = "showLinkData?adminValue=admin";
                                    } else if (admin_value == "co") {
                                        window.location.href = "showLinkData?adminValue=co"
                                    } else if (admin_value == "ca") {
                                        window.location.href = "showLinkData?adminValue=ca"
                                    } else if (admin_value == "sup") {
                                        window.location.href = "showLinkData?adminValue=sup"
                                    } else {
                                        window.location.href = "showUserData";
                                    }
      
    });
</script>
    