/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author DJ
 */
public class audit_status {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException {

        try {
            audit_status t = new audit_status();
            String s = t.audit("sampark.gov.in");
        } catch (IllegalAccessException ex) {
            Logger.getLogger(audit_status.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(audit_status.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // for ignoreing certificate
    private static TrustManager[] get_trust_mgr() {
        TrustManager[] certs = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String t) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String t) {
            }
        }};
        return certs;
    }

    // for response printing
    private static String print_content(HttpsURLConnection con) {
        String res = "";
        if (con != null) {

            try {

                System.out.println("****** Content of the URL ********");
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null) {

                    res += input;
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    // for calling API
    public static String audit(String empno) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        URL url;
        String ret_val = "";
        //String http = "https://personnel.nic.in/webservice_ldap/emdmaster.php?processid=EmployeeMaster&var=" + empno;
        String http = "https://personnel.nic.in/wcarws/wcar_data_service.php?format=json&url=" + empno;
        try {

            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
            TrustManager[] trust_mgr = get_trust_mgr();
            ssl_ctx.init(null,
                    trust_mgr,
                    new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx
                    .getSocketFactory());

            url = new URL(http);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            //con.setRequestMethod("HEAD");
            con.setConnectTimeout(10000); //set timeout to 10 seconds
            String res = print_content(con);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            InputSource is;
            try {
                builder = factory.newDocumentBuilder();
                is = new InputSource(new StringReader(res));
                Document doc = builder.parse(is);

                System.out.println("output ::::::" + doc);
                //NodeList list = doc.getElementsByTagName("reporting_off_email");
                //NodeList name = doc.getElementsByTagName("reporting_off_name");
                //System.out.println(list.item(0).getTextContent() + "," + name.item(0).getTextContent());
                //ret_val = list.item(0).getTextContent() + "~" + name.item(0).getTextContent();
            } catch (ParserConfigurationException e) {
            } catch (SAXException e) {
            } catch (IOException e) {
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return ret_val;
    }

}