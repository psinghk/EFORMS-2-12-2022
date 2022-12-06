package com.org.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.nic.eoffice.esign.CreateEsignRequest;
import org.nic.eoffice.esign.esign_enum.AuthMode;
import org.nic.eoffice.esign.util.EsignUtil;
import org.nic.eoffice.esign.model.EsignRequest;
import org.nic.eoffice.esign.model.EsignRequestResponse;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import org.apache.struts2.ServletActionContext;

public class EsignUtilDao{

    public int getTransactionId() {

        String query = "SELECT id from esign;";
        int i = 0;
        PreparedStatement ps = null;
        try {
            Connection con = DbConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                i = rs.getInt("id");
                String query1 = "update esign set id =?";
                ps = con.prepareStatement(query1);
                ps.setInt(1, i + 1);
                int k = ps.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EsignUtilDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public EsignRequest getEsignFileRequest(String inputFileName, String outputFilename, String pageNo,
            String cordinates, String uid, UserData userValues, String txn) {
        String unSignedPath = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation");
        String signedPath = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation");
        EsignRequest esignReqResp = new EsignRequest();
        esignReqResp.setAspId("NICI-001");
        esignReqResp.setAadhaar("");
        esignReqResp.setSignerConsent("Y");
        esignReqResp.setContentType("file");
        esignReqResp.setAuthMode(AuthMode.OTP);
        //esignReqResp.setResponseUrl("https://nic-esign2gateway.nic.in/esign/response?rs=https://eforms.nic.in/ESignResponsePage");
        esignReqResp.setResponseUrl("https://nic-esign2gateway.nic.in/eSign21/response?rs=https://eforms.nic.in/ESignResponsePage");
        esignReqResp.setEsignVersion("2.1");
        esignReqResp.setTxn(txn);
        esignReqResp.setReason("Approved"); // reason to sign PDF
        //String signerName = profile_values.get("cn").toString(); // For Testing
        String signerName = userValues.getName() != null ? userValues.getName():""; // For Testing
        esignReqResp.setUserName(new String[]{signerName});// Name of signer

        inputFileName = unSignedPath + inputFileName;
        outputFilename = signedPath + outputFilename;

        String[] inputFileNames = new String[]{inputFileName};
        String[] outputFileNames = new String[]{outputFilename};

        inputFileNames = EsignUtil.removeNullFromStringArray(inputFileNames);
        outputFileNames = EsignUtil.removeNullFromStringArray(outputFileNames);
        esignReqResp.setDocInfo(new String[]{"test pdf doc info"});
        esignReqResp.setInputFilesPath(inputFileNames);
        esignReqResp.setOutputFilesPath(outputFileNames);
        if (pageNo != null && !pageNo.isEmpty()) {
            esignReqResp.setPageNo(new String[]{pageNo});
        }
        if (cordinates != null && !cordinates.isEmpty()) {
            esignReqResp.setFileCoordinate(new String[]{cordinates});
        }
        return esignReqResp;
    }

    public EsignRequest getEsignFileRequestForRetiredOffcials(String inputFileName, String outputFilename, String pageNo,
            String cordinates, String uid, String name, String txn) {
        String unSignedPath = ServletActionContext.getServletContext().getInitParameter("retiredOfficersPdfLocation");
        String signedPath = ServletActionContext.getServletContext().getInitParameter("retiredOfficersPdfLocation");
        EsignRequest esignReqResp = new EsignRequest();
        esignReqResp.setAspId("NICI-001");
        esignReqResp.setAadhaar("");
        esignReqResp.setSignerConsent("Y");
        esignReqResp.setContentType("file");
        esignReqResp.setAuthMode(AuthMode.OTP);
        //esignReqResp.setResponseUrl("https://nic-esign2gateway.nic.in/esign/response?rs=https://eforms.nic.in/ESignResponsePage");
        esignReqResp.setResponseUrl("https://nic-esign2gateway.nic.in/eSign21/response?rs=https://eforms.nic.in/eSignResponsePageForRetired");
        esignReqResp.setEsignVersion("2.1");
        esignReqResp.setTxn(txn);
        esignReqResp.setReason("Approved"); // reason to sign PDF
        //String signerName = profile_values.get("cn").toString(); // For Testing
        String signerName = name; // For Testing
        esignReqResp.setUserName(new String[]{signerName});// Name of signer

        inputFileName = unSignedPath + inputFileName;
        outputFilename = signedPath + outputFilename;

        String[] inputFileNames = new String[]{inputFileName};
        String[] outputFileNames = new String[]{outputFilename};

        inputFileNames = EsignUtil.removeNullFromStringArray(inputFileNames);
        outputFileNames = EsignUtil.removeNullFromStringArray(outputFileNames);
        esignReqResp.setDocInfo(new String[]{"test pdf doc info"});
        esignReqResp.setInputFilesPath(inputFileNames);
        esignReqResp.setOutputFilesPath(outputFileNames);
        if (pageNo != null && !pageNo.isEmpty()) {
            esignReqResp.setPageNo(new String[]{pageNo});
        }
        if (cordinates != null && !cordinates.isEmpty()) {
            esignReqResp.setFileCoordinate(new String[]{cordinates});
        }
        return esignReqResp;
    }

    public EsignRequestResponse getRequestXML(EsignRequest esignRequest) {
        EsignRequestResponse esignRequestResponse = null;
        try {
            CreateEsignRequest createEsignRequest = new CreateEsignRequest();
            esignRequestResponse = createEsignRequest.getRequestXml(esignRequest);
            return esignRequestResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
