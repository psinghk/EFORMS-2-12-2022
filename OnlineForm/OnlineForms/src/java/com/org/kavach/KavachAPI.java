package com.org.kavach;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;

public class KavachAPI {

    private static final Pattern pat = Pattern.compile(".*\"access_token\"\\s*:\\s*\"([^\"]+)\".*");
    private static final String clientId = "NICDA5TA4PDZV";
    private static final String clientSecret = "X9I3OE11F05FL80QE1CZ";
    private static final String kavachurl = "https://gkavachda01.nic.in";

    private static final String tokenUrl = kavachurl + "/oauth/token";
    private static final String auth = clientId + ":" + clientSecret;
    private static final String authentication = Base64.getEncoder().encodeToString(auth.getBytes());
    private static final String content = "grant_type=client_credentials";
    private static final String key = "4UO7SA2TLSK6UM9R";

    public static String isUserKavachEnabled(String user_dn, String admin, String deviceDetailFlag) {
        String response = "";
        response = getUserCountryPolicy(user_dn, admin, deviceDetailFlag);
        System.out.println("user_dn " + user_dn + "admin = " + admin + "response " + response);
        if (response.contains("\"result\":\"Success\"")) {
            return "exist_in_kavach";
        } else {
            return "exist_not_in_kavach";
        }
    }

    public static String getUserCountryPolicy(String user_dn, String admin, String deviceDetailFlag) {

        String returnString;
        // sunny Commented, Uncomment for live
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("entryDn", user_dn);
            jsonObj.put("admin", admin);
            jsonObj.put("deviceDetailFlag", deviceDetailFlag);
//         String returnString = Encryptor1.decrypt(callOauthApi(kavachurl + "/api/show?access_token=" + getAccessToken(), Encryptor1.encrypt(jsonObj.toJSONString(), "4UO7SA2TLSK6UM9R")), "4UO7SA2TLSK6UM9R");
            returnString = Encryptor1.decrypt(callOauthApi(kavachurl + "/api/show?access_token=" + getAccessToken(),
                    Encryptor1.encrypt(jsonObj.toString(), "4UO7SA2TLSK6UM9R")), "4UO7SA2TLSK6UM9R");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            returnString = "";
            e.printStackTrace();
        }

        System.out.println("Before Calling API ::::");

        System.out.println("returnString in execute() Method:::::::::::;" + returnString);
        // String returnString =
        // "{\"result\":\"Success\",\"countryPolicy\":[{\"country\":\"Albania\",\"endDate\":\"2021-08-24
        // 11:00\",\"startDate\":\"2021-06-24
        // 10:52\"},{\"country\":\"Australia\",\"endDate\":\"2021-08-25
        // 11:00\",\"startDate\":\"2021-06-25 10:52\"}]}";
        return returnString;

    }

    public static String getAccessToken() {

        BufferedReader reader = null;
        HttpURLConnection connection = null;
        String returnValue = null;
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager(null)}, new SecureRandom());
            // ctx.init(null, new TrustManager[]{new DefaultTrustManager(null)}, new
            // SecureRandom());
            SSLContext.setDefault(ctx);
            HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession arg1) {
                    return true;
                }

            });
            System.out.println("authhhhhhhhh" + authentication);
            URL url = new URL(tokenUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic TklDREE1VEE0UERaVjpYOUkzT0UxMUYwNUZMODBRRTFDWg==");
            connection.setRequestProperty("Accept", "application/json");
            PrintStream os = new PrintStream(connection.getOutputStream());
            os.print(content);
            os.close();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            StringWriter out = new StringWriter(
                    connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            String response = out.toString();
            Matcher matcher = pat.matcher(response);
            if (matcher.matches() && matcher.groupCount() > 0) {
                returnValue = matcher.group(1);
            }
            System.out.println(">>>>>>>>>" + returnValue);
        } catch (Exception e) {
            System.out.println("Error :::::::::: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            connection.disconnect();
        }
        return returnValue;
    }

    public static String callOauthApi(String urlString, String payload) {
        // System.out.println("payload==" + payload);
        String result = null;
        URL url = null;

        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager(null)}, new SecureRandom());
            // ctx.init(null, new TrustManager[]{new DefaultTrustManager(null)}, new
            // SecureRandom());
            SSLContext.setDefault(ctx);
            url = new URL(urlString);
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession arg1) {
                    return true;
                }

            });
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            BufferedWriter httpsRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            httpsRequestBodyWriter.write("payload=" + payload);
            httpsRequestBodyWriter.close();

            // Reading from the HTTP response body
            Scanner httpsResponseScanner = new Scanner(con.getInputStream());
            while (httpsResponseScanner.hasNextLine()) {
                result = httpsResponseScanner.nextLine();
            }
            httpsResponseScanner.close();
        } catch (Exception e) {
            System.out.println("exceptionnnnnnnnnn" + e);
            e.printStackTrace();
        }

        System.out.println("result=====  " + result);
        return result;
    }

    public static class DefaultTrustManager implements X509TrustManager {

        public DefaultTrustManager(Object object) {
            // TODO Auto-generated constructor stub
        }

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}
