package com.org.controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.HttpParameters;
import org.owasp.esapi.ESAPI;

public class LoggingInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;
    private static Pattern[] patterns = new Pattern[]{
        // Script fragments
        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
        // Script fragments
        Pattern.compile("alert.*(.*?).*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS),
        Pattern.compile("lt.*script.*gt", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS),
        Pattern.compile("lt.*script", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS),
        Pattern.compile("script.*gt", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS),
        // src='...'
        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("src=*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // lonely script tags
        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // eval(...)
        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // expression(...)
        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // javascript:...
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        // vbscript:...
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        // a href
        Pattern.compile("<a .*href=.*", Pattern.CASE_INSENSITIVE),
        //        // img
        //        Pattern.compile("<img .*", Pattern.CASE_INSENSITIVE),

        // a href
        Pattern.compile("href=.*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // onload(...)=...
        Pattern.compile("on.*(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // iframe
        Pattern.compile("<iframe .*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // object
        Pattern.compile("<object .*", Pattern.CASE_INSENSITIVE),
        // iframe
        Pattern.compile("//evil", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // style
        Pattern.compile("style=*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),};

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        System.out.println("INSIDE LOGGING INTERCEPTOR");
        final ActionContext context = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        //String requestVal = request.getParameter("data");
        HttpParameters parameters = context.getParameters();

        if (parameters != null && !parameters.isEmpty()) {
                   System.out.println("INSIDE LOGGING INTERCEPTOR : parameter not empty");
            String data = "";
//            for (Object entry : request.getParameterMap().entrySet()) {
//                
////           
//                String name = (String)entry.getKey();
//                data = (String) entry.getValue()[0];
////                data = data.replace("<", "&lt;");
////                data = data.replace(">", "&gt;");
//                data = StringUtils.replaceAll(data, "<", "&lt;");
//                data = StringUtils.replaceAll(data, ">", "&gt;"); 
//                data = StringUtils.replaceAll(data, "&#x2b", "+");
//                String str = "";
//                if (data.contains(";") || data.startsWith("+") && data.contains(".") || data.contains("-") || data.contains("@")) {
//                    str = data;
//                } else {
//                    str = ESAPI.encoder().encodeForHTML(data);
//                }
//
////                if (data != null || stripXSS(data)) {
////                    return "exception";
////                }
//                ActionContext.getContext().getValueStack().setValue(name, str);
//
//
//                // ...
//            }
        }

        if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {
            String[] str = request.getQueryString().split("&");
            System.out.println("INSIDE LOGGING INTERCEPTOR : str[] "+str);
            for (String str1 : str) {
                String[] str2 = str1.split("=");
                if (str2.length == 2) {
                    //System.out.println("first string" + str2[0] + "    " + "second string" + str2[1]);
                    if (str2[1] != null) {
                        if (str2[1].contains("<")) {
                            str[1] = StringUtils.replaceAll(str2[1], "<", "&lt;");
                        }
                        if (str2[1].contains(">")) {
                            str[1] = StringUtils.replaceAll(str2[1], ">", "&gt;");
                        }
                        String encStr = ESAPI.encoder().encodeForHTML(str2[1]);
                          System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "logging interceptor second if: " + encStr);
           
                        ActionContext.getContext().getValueStack().setValue(str2[0], encStr);
                    }

                } else {
                    System.out.println("Error");
                }
            }

        }
        

        if (StringUtils.containsIgnoreCase(request.getRequestURL().toString(), "<script") || StringUtils.containsIgnoreCase(request.getRequestURL().toString(), "< script")
                || StringUtils.containsIgnoreCase(request.getRequestURL().toString(), "%3cscript") || StringUtils.containsIgnoreCase(request.getRequestURL().toString(), "%3c script") || StringUtils.containsIgnoreCase(request.getRequestURL().toString(), "window")) {
                 System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "third if: exception");
           
            return "exception";
        }
          if (StringUtils.containsIgnoreCase(request.getQueryString(), "<script") || StringUtils.containsIgnoreCase(request.getQueryString(), "< script")
                || StringUtils.containsIgnoreCase(request.getQueryString(), "%3cscript") || StringUtils.containsIgnoreCase(request.getQueryString(), "%3c script") || StringUtils.containsIgnoreCase(request.getQueryString(), "window")) {
              
              System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "fourth if: exception");
            return "exception";
        }
        return invocation.invoke();

    }

    public static void main(String[] arg) {
        String s = "\"<s>\"";
        //System.out.println(StringEscapeUtils.escapeHtml(s));
        //System.out.println(ESAPI.encoder().encodeForHTML(ESAPI.encoder().encodeForHTML(StringEscapeUtils.escapeHtml(s))));
    }

    private Boolean stripXSS(String value) {
        Boolean flag = false;
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            value = value.replaceAll("\0", "");
            if (StringUtils.containsIgnoreCase(value, "<script") || StringUtils.containsIgnoreCase(value, "< script")
                    || StringUtils.containsIgnoreCase(value, "%3cscript") || StringUtils.containsIgnoreCase(value, "%3c script")
                    || StringUtils.containsIgnoreCase(value, "script>") || StringUtils.containsIgnoreCase(value, "script >")
                    || StringUtils.containsIgnoreCase(value, "script%3c") || StringUtils.containsIgnoreCase(value, "scr%")
                    || StringUtils.containsIgnoreCase(value, "script+")) {
                flag = true;
            }

            if (flag) {
                return flag;
            }
            // Avoid null characters
            // Remove all sections that match a pattern
            value = value.replaceAll("\0", "");
            for (Pattern scriptPattern : patterns) {
                if (scriptPattern.matcher(value).find()) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
