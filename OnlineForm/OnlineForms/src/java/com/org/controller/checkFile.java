package com.org.controller;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.DnsBean;
import com.org.bean.EmailBean;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.service.checkFileService;
import com.org.utility.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;

public class checkFile extends ActionSupport implements SessionAware {

    File cert_file;
    File hardware_cert_file;
    String size;
    String cert_fileFileName;
    String hardware_cert_fileFileName;
    String error;
    checkFileService fileservice = null;
    String uploaded_filename, ref_num, stat_form_type;
    private InputStream fileInputStream;
    private String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    String req_type = "";
    String req_other_type = "";

    public checkFile() {
        fileservice = new checkFileService();
        if (fileservice == null) {
            fileservice = new checkFileService();
        }
    }

    public File getHardware_cert_file() {
        return hardware_cert_file;
    }

    public void setHardware_cert_file(File hardware_cert_file) {
        this.hardware_cert_file = hardware_cert_file;
    }

    public String getHardware_cert_fileFileName() {
        return hardware_cert_fileFileName;
    }

    public void setHardware_cert_fileFileName(String hardware_cert_fileFileName) {
        this.hardware_cert_fileFileName = hardware_cert_fileFileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }
    Map session;

    public void setSession(Map session) {
        this.session = session;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public String getReq_other_type() {
        return req_other_type;
    }

    public void setReq_other_type(String req_other_type) {
        this.req_other_type = req_other_type;
    }

    public File getCert_file() {
        return cert_file;
    }

    public void setCert_file(File cert_file) {
        this.cert_file = cert_file;
    }

    public String getCert_fileFileName() {
        return cert_fileFileName;
    }

    public void setCert_fileFileName(String cert_fileFileName) {
        this.cert_fileFileName = Jsoup.parse(cert_fileFileName).text();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = Jsoup.parse(error).text();
    }

    public String getUploaded_filename() {
        return uploaded_filename;
    }

    public void setUploaded_filename(String uploaded_filename) {
        this.uploaded_filename = Jsoup.parse(uploaded_filename).text();
    }

    public String getRef_num() {
        return ref_num;
    }

    public void setRef_num(String ref_num) {
        this.ref_num = ref_num;
    }

    public String getStat_form_type() {
        return stat_form_type;
    }

    public void setStat_form_type(String stat_form_type) {
        this.stat_form_type = stat_form_type;
    }

    File[] userfiles;
    String[] userfilesFileName;

    public File[] getUserfiles() {
        return userfiles;
    }

    public void setUserfiles(File[] userfiles) {
        this.userfiles = userfiles;
    }

    public String[] getUserfilesFileName() {
        return userfilesFileName;
    }

    public void setUserfilesFileName(String[] userfilesFileName) {
        this.userfilesFileName = userfilesFileName;
    }

    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isFilenameValid(String file) {
        try {
            String fileNameWithOutExt = FilenameUtils.removeExtension(cert_fileFileName);
            if (!fileNameWithOutExt.contains(".")) {
                if ((fileNameWithOutExt.matches("^[^*&%]+$")) && (cert_fileFileName.endsWith(".xls") || cert_fileFileName.endsWith(".XLS") || cert_fileFileName.endsWith(".xlsx") || cert_fileFileName.endsWith(".XLSX"))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isHardwareFilenamePDFValid(String file) {
        try {
            System.out.println("cert_fileFileName" + hardware_cert_fileFileName);
            String fileNameWithOutExt = FilenameUtils.removeExtension(hardware_cert_fileFileName);
            if (!fileNameWithOutExt.contains(".")) {
                if ((fileNameWithOutExt.matches("^[^*&%<>]+$")) && (hardware_cert_fileFileName.endsWith(".pdf") || hardware_cert_fileFileName.endsWith(".PDF"))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFilenameCSVValid(String file) {
        try {
            String fileNameWithOutExt = FilenameUtils.removeExtension(cert_fileFileName);
            if (!fileNameWithOutExt.contains(".")) {
                if ((fileNameWithOutExt.matches("^[^*&%]+$")) && (cert_fileFileName.endsWith(".csv") || cert_fileFileName.endsWith(".CSV"))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFilenamePDFValid(String file) {
        try {
            String fileNameWithOutExt = FilenameUtils.removeExtension(cert_fileFileName);
            if (!fileNameWithOutExt.contains(".")) {
                if ((fileNameWithOutExt.matches("^[^*&%<>]+$")) && (cert_fileFileName.endsWith(".pdf") || cert_fileFileName.endsWith(".PDF"))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFilenamePDFJPGValid(String s) {
        try {
            String fileNameWithOutExt = FilenameUtils.removeExtension(s);
            if (!fileNameWithOutExt.contains(".")) {
                if ((fileNameWithOutExt.matches("^[^*&%<>]+$")) && (s.endsWith(".pdf") || s.endsWith(".PDF") || s.endsWith(".jpg") || s.endsWith(".JPG") || s.endsWith(".jpeg") || s.endsWith(".JPEG") || s.endsWith(".png") || s.endsWith(".PNG"))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String checkWebcastFile() {
        System.out.println("session.get(\"webcast_uploaded_files\")" + session.get("webcast_uploaded_files"));
        if (session.get("webcast_uploaded_files") != null) {
            session.remove("webcast_uploaded_files");
        }
        try {
            Boolean fileError = false;
            for (int i = 0; i < userfiles.length; i++) {
                File uploadedFile = userfiles[i];
                String fileName = userfilesFileName[i];
                System.out.println("isFilenamePDFJPGValid(fileName)" + isFilenamePDFJPGValid(fileName));
                if (isFilenamePDFJPGValid(fileName) == true) {
                    System.out.println("111111111111111");
                    if (fileName.endsWith(".pdf") || fileName.endsWith(".PDF")) {
                        System.out.println("222222222222222222222");
                        fileError = fileservice.checkPDF(uploadedFile);
                    } else if (fileName.endsWith(".jpg") || fileName.endsWith(".JPG") || fileName.endsWith(".jpeg") || fileName.endsWith(".JPEG") || fileName.endsWith(".png")) {
                        System.out.println("333333333333333333333");
                        fileError = fileservice.checkImage(uploadedFile);
                    }
                } else {
                    System.out.println("4444444444444444444444444");
                    fileError = true;
                }
                if (fileError) {
                    System.out.println("555555555555555555555555555");
                    error = "Upload file in proper PDF/JPG format. Filename: " + fileName;
                    break;
                }
            }
            if (!fileError) {
                Map webcast_files = new HashMap();
                for (int i = 0; i < userfiles.length; i++) {
                    File uploadedFile = userfiles[i];
                    String fileName = userfilesFileName[i];
                    File destFile = new File(ServletActionContext.getServletContext().getInitParameter("signedPdfLocation") + fileName);
                    FileUtils.copyFile(uploadedFile, destFile);
                    webcast_files.put(fileName, ServletActionContext.getServletContext().getInitParameter("signedPdfLocation") + fileName);
                }
                session.put("webcast_uploaded_files", webcast_files);
            }

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
        cert_file = null;
        return SUCCESS;
    }

    public String checkPDF() {
        String email = "";
        if (cert_file != null && cert_file.isFile()) {
            try {
                if (isFilenamePDFValid(cert_file.getName()) == true) {
                    if (session != null && session.get("uservalues") != null) {
                        UserData userdata = (UserData) session.get("uservalues");
                        email = userdata.getEmail();
                    }
                    Date date = new Date();
                    DateFormat dt = new SimpleDateFormat("ddMMyyyyHHmmss");
                    String pdate = dt.format(date);
                    String destinationFileName = pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".pdf";
                    File destFile = new File(ServletActionContext.getServletContext().getInitParameter("signedPdfLocation"), pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".pdf");
                    FileUtils.copyFile(cert_file, destFile);
                    Boolean fileError = fileservice.checkPDF(destFile);
                    if (fileError) {
                        error = "Upload file in proper PDF format";
                        destFile.delete();
                    } else if (cert_fileFileName != null && !cert_fileFileName.isEmpty()) {
                        session.put("uploaded_filename", cert_fileFileName);
                        session.put("renamed_filepath", ServletActionContext.getServletContext().getInitParameter("signedPdfLocation") + destinationFileName);
                        System.out.println("uploaded file name in session:::::" + session.get("uploaded_filename"));
                    } else {
                        error = "Please try again!!! File could not be uploaded!";
                        cert_fileFileName = "";
                        cert_file.deleteOnExit();
                    }
                } else {
                    error = "Upload file in proper PDF format";
                    cert_fileFileName = "";
                }
            } catch (Exception e) {
                cert_file.deleteOnExit();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
            }
        } else {
            error = "Please try again!!! File could not be uploaded!";
            cert_fileFileName = "";
        }
        cert_file = null;
        return SUCCESS;
    }

    public String checkRelayHardwarePDF() {
        String email = "";
        if (hardware_cert_file != null && hardware_cert_file.isFile()) {
            //String SessionCSRFRandom = session.get("CSRFRandom").toString();  // audit by nikki
            System.out.println("cert_file.length()" + hardware_cert_file.length());
            System.out.println("hardware_cert_file.getName()" + hardware_cert_file.getName());
//       if (CSRFRandom.equals(SessionCSRFRandom) && !session.get("fileLength").equals(hardware_cert_file.length())) {
//            System.out.println("size not match");
//        }

            try {

                if ((hardware_cert_file.length() / 1048576) < 1.0) {
                    if (isHardwareFilenamePDFValid(hardware_cert_file.getName()) == true) {
                        if (session != null && session.get("uservalues") != null) {
                            UserData userdata = (UserData) session.get("uservalues");
                            email = userdata.getEmail();
                        }
                        Date date = new Date();
                        DateFormat dt = new SimpleDateFormat("ddMMyyyyHHmmss");
                        String pdate = dt.format(date);
                        System.out.println("file size is:::::::::::::" + size);

                        if (Long.parseLong(size) != hardware_cert_file.length()) {
                            error = "Upload file in proper PDF format";

                        } else {
                            String destinationFileName = pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".pdf";
                            File destFile = new File(ServletActionContext.getServletContext().getInitParameter("signedPdfLocation"), pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".pdf");
                            FileUtils.copyFile(hardware_cert_file, destFile);
                            Boolean fileError = fileservice.checkPDF(destFile);
                            if (fileError) {
                                error = "Upload file in proper PDF format";
                                destFile.delete();
                            } else if (hardware_cert_fileFileName != null && !hardware_cert_fileFileName.isEmpty()) {
                                session.put("hardware_uploaded_filename", hardware_cert_fileFileName);
                                session.put("hardware_renamed_filepath", ServletActionContext.getServletContext().getInitParameter("signedPdfLocation") + destinationFileName);
                                System.out.println("uploaded file name in session:::::" + session.get("hardware_uploaded_filename"));
                            } else {
                                error = "Please try again!!! File could not be uploaded!";
                                hardware_cert_fileFileName = "";
                                hardware_cert_file.deleteOnExit();
                            }
                        }

                    } else {
                        error = "Upload file in proper PDF format";
                        hardware_cert_fileFileName = "";
                    }
                } else {
                    System.out.println("file size is greater than 1 MB ");
                    error = "File size can not exceed 1 MB";
                    hardware_cert_fileFileName = "";
                }

            } catch (Exception e) {
                hardware_cert_file.deleteOnExit();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
            }
        } else {
            error = "Please try again!!! File could not be uploaded!";
            hardware_cert_fileFileName = "";
        }
        hardware_cert_file = null;
        return SUCCESS;
    }

    public String checkExcel() {
        String email = "";
        if (cert_file != null && cert_file.isFile()) {
            try {
                if (isFilenameValid(cert_file.getName()) == true) {
                    if (session != null && session.get("uservalues") != null) {
                        UserData userdata = (UserData) session.get("uservalues");
                        email = userdata.getEmail();
                    }
                    Date date = new Date();
                    DateFormat dt = new SimpleDateFormat("ddMMyyyyHHmmss");
                    String pdate = dt.format(date);
                    String destinationFileName = pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".xls";
                    File destFile = new File(ServletActionContext.getServletContext().getInitParameter("excelLocation"), pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".xls");
                    FileUtils.copyFile(cert_file, destFile);
                    Boolean fileError = fileservice.checkExcel(destFile);
                    if (fileError) {
                        error = "Upload file in .xls/.xlsx format with maxmimum 3000 rows.";
                        destFile.delete();
                    } else if (cert_fileFileName != null && !cert_fileFileName.isEmpty()) {
                        session.put("uploaded_filename", cert_fileFileName);
                        session.put("renamed_filepath", ServletActionContext.getServletContext().getInitParameter("excelLocation") + destinationFileName);
                    } else {
                        error = "Please try again!!! File could not be uploaded!";
                        cert_fileFileName = "";
                        cert_file.deleteOnExit();
                    }
                } else {
                    error = "Upload file in .xls/.xlsx or .csv format with maxmimum 3000 rows.";
                    cert_fileFileName = "";
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
            }
        } else {
            error = "Please try again!!! File could not be uploaded!";
            cert_fileFileName = "";
        }
        cert_file = null;
        return SUCCESS;
    }

    public String checkCSV() {
        String email = "";
        if (cert_file != null && cert_file.isFile()) {
            try {
                System.out.println("cert_file.getName()" + cert_file.getName());
                if (isFilenameCSVValid(cert_file.getName()) == true) {
                    if (session != null && session.get("uservalues") != null) {
                        UserData userdata = (UserData) session.get("uservalues");
                        email = userdata.getEmail();
                    }
                    Date date = new Date();
                    DateFormat dt = new SimpleDateFormat("ddMMyyyyHHmmss");
                    String pdate = dt.format(date);
                    String destinationFileName = pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".csv";
                    File destFile = new File(ServletActionContext.getServletContext().getInitParameter("excelLocation"), pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".csv");
                    FileUtils.copyFile(cert_file, destFile);
                    Boolean fileError = fileservice.checkCSV(destFile);
                    System.out.println("fileError" + fileError);
                    if (fileError) {
                        error = "Upload file in .csv format with maxmimum 3000 rows.";
                        destFile.delete();
                    } else {
//                        //02-may-2022
//                        destFile = new File(ServletActionContext.getServletContext().getInitParameter("multipleUploadPdfLocation"), pdate + ".csv");
//                        FileUtils.copyFile(cert_file, destFile);
//                        //02-may-2022
                        if (cert_fileFileName != null && !cert_fileFileName.isEmpty()) {
                            session.put("uploaded_filename", cert_fileFileName);
                            session.put("renamed_filepath", ServletActionContext.getServletContext().getInitParameter("excelLocation") + destinationFileName);
                        } else {
                            error = "Please try again!!! File could not be uploaded!";
                            cert_fileFileName = "";
                            cert_file.deleteOnExit();
                        }
                    }
                } else {
                    System.out.println("inside error in ckeck file");
                    error = "Upload file in .csv format with maxmimum 3000 rows.";
                    cert_fileFileName = "";
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
            }
        } else {
            error = "Please try again!!! File could not be uploaded!";
            cert_fileFileName = "";
        }
        cert_file = null;
        return SUCCESS;
    }

    public String checkCSVForBulk() {
        String email = "";
        if (cert_file != null && cert_file.isFile()) {
            try {
                System.out.println("cert_file.getName()" + cert_file.getName());
                if (isFilenameCSVValid(cert_file.getName()) == true) {
                    if (session != null && session.get("uservalues") != null) {
                        UserData userdata = (UserData) session.get("uservalues");
                        email = userdata.getEmail();
                    }
                    Date date = new Date();
                    DateFormat dt = new SimpleDateFormat("ddMMyyyyHHmmss");
                    String pdate = dt.format(date);
                    String destinationFileName = pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".csv";
                    File destFile = new File(ServletActionContext.getServletContext().getInitParameter("excelLocation"), pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".csv");
                    FileUtils.copyFile(cert_file, destFile);
                    Set<EmailBean> emailData = fileservice.checkCSVForBULK(destFile);
                    if (emailData.size() > 3000) {
                        error = "Upload file in .csv format with maxmimum 3000 rows.";
                        destFile.delete();
                    } else if (cert_fileFileName != null && !cert_fileFileName.isEmpty()) {
                        System.out.println("INSIDE FILE UPLOAD : Session created ");
                        session.put("uploaded_filename", cert_fileFileName);
                        session.put("renamed_filepath", ServletActionContext.getServletContext().getInitParameter("excelLocation") + destinationFileName);
                        session.put("bulkFileContent", emailData);
                    } else {
                        error = "Please try again!!! File could not be uploaded!";
                        cert_fileFileName = "";
                        cert_file.deleteOnExit();
                    }
                } else {
                    System.out.println("inside error in ckeck file");
                    error = "Please upload csv file only.";
                    cert_fileFileName = "";
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
            }
        } else {
            error = "Please try again!!! File could not be uploaded!";
            cert_fileFileName = "";
        }
        cert_file = null;
        return SUCCESS;
    }

    public String checkCSVForDNS() {
        String email = "";
        if (cert_file != null && cert_file.isFile()) {
            try {
                System.out.println("cert_file.getName()" + cert_file.getName());
                System.out.println("req_type.getName()" + getReq_type());
                System.out.println("req_other_type.getName()" + getReq_other_type());
                if (isFilenameCSVValid(cert_file.getName()) == true) {
                    if (session != null && session.get("uservalues") != null) {
                        UserData userdata = (UserData) session.get("uservalues");
                        email = userdata.getEmail();
                    }
                    Date date = new Date();
                    DateFormat dt = new SimpleDateFormat("ddMMyyyyHHmmss");
                    String pdate = dt.format(date);
                    String destinationFileName = pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".csv";
                    File destFile = new File(ServletActionContext.getServletContext().getInitParameter("excelLocation"), pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".csv");
                    FileUtils.copyFile(cert_file, destFile);
                    //Boolean fileError = fileservice.checkCSV(destFile);
                    Set<DnsBean> dnsdata = fileservice.checkCSVForDNS(destFile, req_type, req_other_type);
                    if (dnsdata.size() > 3000) {
                        error = "Upload file in .csv format with maxmimum 3000 rows.";
                        destFile.delete();
                    } else if (cert_fileFileName != null && !cert_fileFileName.isEmpty()) {
                        System.out.println("INSIDE FILE UPLOAD : Session created ");
                        session.put("uploaded_filename", cert_fileFileName);
                        session.put("renamed_filepath", ServletActionContext.getServletContext().getInitParameter("excelLocation") + destinationFileName);
                        session.put("fileContent", dnsdata);
                    } else {
                        error = "Please try again!!! File could not be uploaded!";
                        cert_fileFileName = "";
                        cert_file.deleteOnExit();
                    }
                } else {
                    System.out.println("inside error in ckeck file");
                    error = "Please upload csv file only.";
                    cert_fileFileName = "";
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
            }
        } else {
            error = "Please try again!!! File could not be uploaded!";
            cert_fileFileName = "";
        }
        cert_file = null;
        return SUCCESS;
    }

    public String checkCert() {
        String email = "";
        if (cert_file != null && cert_file.isFile()) {
            try {
                System.out.println("here in uploading function of sign cert");
                if (isFilenamePDFValid(cert_file.getName()) == true) {
                    if (session != null && session.get("uservalues") != null) {
                        UserData userdata = (UserData) session.get("uservalues");
                        email = userdata.getEmail();
                    }
                    Date date = new Date();
                    DateFormat dt = new SimpleDateFormat("ddMMyyyyHHmmss");
                    String pdate = dt.format(date);
                    String destinationFileName = pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".pdf";
                    File destFile = new File(ServletActionContext.getServletContext().getInitParameter("rawPdfLocation"), pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".pdf");
                    FileUtils.copyFile(cert_file, destFile);
                    Boolean fileError = fileservice.checkPDF(destFile);
                    if (fileError) {
                        error = "Upload file in proper PDF format";
                        destFile.delete();
                    } else if (cert_fileFileName != null && !cert_fileFileName.isEmpty()) {
                        System.out.println("cert_filename:::::::::::::" + cert_fileFileName);
                        session.put("uploaded_filename_cert", cert_fileFileName);
//                    session.put("renamed_filepath_cert", "/eForms/PDF/" + pdate + ".pdf");
                        session.put("renamed_filepath_cert", ServletActionContext.getServletContext().getInitParameter("allServicesPdfLocation") + destinationFileName);

                        System.out.println("renamed_filepath_cert:::::::::::::" + session.get("renamed_filepath_cert"));
                    } else {
                        error = "Please try again!!! File could not be uploaded!";
                        cert_fileFileName = "";
                        cert_file.deleteOnExit();
                    }
                } else {
                    error = "Upload file in proper PDF format";
                    cert_fileFileName = "";
                }
            } catch (Exception e) {
                cert_file.deleteOnExit();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
            }
        } else {
            error = "Please try again!!! File could not be uploaded!";
            cert_fileFileName = "";
        }
        cert_file = null;
        return SUCCESS;
    }

    public String DownloadFile() {
        System.out.println("cert_fileFileName: " + cert_fileFileName);
        String filename = "";

        String role = "";
        String roleString = "";
        if (session.get("admin_role") != null) {
            role = session.get("admin_role").toString();
        }
        if (role.contains("ca")) {
            roleString = "user";
        }
        if (role.contains("co")) {
            roleString = "ca";
        }
        if (role.contains("admin")) {
            roleString = "ca";
        }
        if (role.contains("admin")) {
            roleString = "ca";
        }
        if (role.contains("sup")) {
            roleString = "ca";
        }
        if (role.contains("user")) {
            roleString = "user";
        }
        if (cert_fileFileName != null && ref_num != null) {
            if (cert_fileFileName.equals("download_esigned")) {

                filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + "eSigned_" + roleString + "_" + ref_num.trim() + ".pdf";
                //filename="/eForms/PDF/06032018171454.pdf";   // to be changed with upper line
                uploaded_filename = "eSigned_" + roleString + "_" + ref_num.trim() + ".pdf";
                try {
                    fileInputStream = new FileInputStream(new File(filename));
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                    return "filenotfound";
                }
                System.out.println("filename" + filename + "roleString" + roleString + role);
            } else if (cert_fileFileName.equals("download_esigned_admin")) {

                filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + "eSigned_user_" + ref_num.trim() + ".pdf";
                //filename="/eForms/PDF/06032018171454.pdf";   // to be changed with upper line
                uploaded_filename = "eSigned_user_" + ref_num.trim() + ".pdf";
                try {
                    fileInputStream = new FileInputStream(new File(filename));
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                    return "filenotfound";
                }
            }
        } else {
            // THIS PART OF CODE LOOKS USELESS (Written by Ashwini)
            if (session.get("renamed_filepath") != null) {
                filename = session.get("renamed_filepath").toString();
            }
            System.out.println("inside else:::::::::" + filename);
            if (session.get("uploaded_filename") != null) {
                uploaded_filename = session.get("uploaded_filename").toString();
                try {
                    fileInputStream = new FileInputStream(new File(filename));
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                    return "filenotfound";
                }
            }
        }
        return SUCCESS;
    }

    public String DownloadFileRetired() {
        System.out.println("cert_fileFileName: " + cert_fileFileName);
        String filename = "";

        if (cert_fileFileName != null && ref_num != null) {
            filename = ServletActionContext.getServletContext().getInitParameter("retiredOfficersPdfLocation") + "eSigned_retired_user_" + ref_num.trim() + ".pdf";
            uploaded_filename = "eSigned_retired_user_" + ref_num.trim() + ".pdf";
            try {
                fileInputStream = new FileInputStream(new File(filename));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    public String DownloadExemptedFile() {
        // THIS PART OF CODE LOOKS USELESS (Written by Ashwini)
        System.out.println(" inside DownloadErrorFile ");
        String filename = "";
        if (session.get("hardware_renamed_filepath") != null && !session.get("hardware_renamed_filepath").equals("")) {
            filename = session.get("hardware_renamed_filepath").toString();
        }
        System.out.println("inside else:::::::::" + filename);
        if (session.get("hardware_uploaded_filename") != null && !session.get("hardware_renamed_filepath").equals("")) {
            uploaded_filename = session.get("hardware_uploaded_filename").toString();
            try {
                fileInputStream = new FileInputStream(new File(filename));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    public String Download_EmaiAct_pdfFile() {

        System.out.println(" inside Downloa_EmaiAct_pdfFile ");
        String filename = "";
        if (session.get("hardware_renamed_filepath") != null && !session.get("hardware_renamed_filepath").equals("")) {
            filename = session.get("hardware_renamed_filepath").toString();
        }
        System.out.println("inside else:::::::::" + filename);
        if (session.get("hardware_uploaded_filename") != null && !session.get("hardware_renamed_filepath").equals("")) {
            uploaded_filename = session.get("hardware_uploaded_filename").toString();
            try {
                fileInputStream = new FileInputStream(new File(filename));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    public String DownloadCertRelay() {
        System.out.println(" inside Download relay cert ");
        //String filename = "C:/eForms/PDF/14112018123549.pdf";
        String filename = "";
        System.out.println("inside else:::::::::" + filename);
        if (session.get("renamed_filepath_cert") != null) {
            uploaded_filename = session.get("renamed_filepath_cert").toString();
            try {
                fileInputStream = new FileInputStream(new File(filename));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    // start, code added by pr on 12thjun18
    // below function added by pr on 12thjun18
    public String DownloadValidFile() {
        System.out.println(" inside DownloadErrorFile ");
        String filename = "";
        filename = session.get("valid_filepath").toString();
        System.out.println("inside else:::::::::" + filename);
        if (session.get("valid_filepath") != null) {
            uploaded_filename = session.get("valid_filepath").toString();
            try {
                fileInputStream = new FileInputStream(new File(filename));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    // below function added by pr on 12thjun18
    public String DownloadErrorFile() {
        System.out.println(" inside DownloadValidFile ");
        String filename = "";
        filename = session.get("error_filepath").toString();
        System.out.println("inside else:::::::::" + filename);
        if (session.get("error_filepath") != null) {
            uploaded_filename = session.get("error_filepath").toString();
            try {
                fileInputStream = new FileInputStream(new File(filename));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    // below function added by pr on 13thjun18
    public String DownloadNotValidFile() {
        System.out.println(" inside DownloadErrorFile ");
        String filename = "";
        filename = session.get("notvalid_filepath").toString();
        System.out.println("inside else:::::::::" + filename);
        if (session.get("notvalid_filepath") != null) {
            uploaded_filename = session.get("notvalid_filepath").toString();
            try {
                fileInputStream = new FileInputStream(new File(filename));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    // end, code added by pr on 12thjun18
    public String DownloadFiles() {
        try {
            if (session.get("filetodownload") != null) {
                String filename = "";
                if (uploaded_filename.equals("valid")) {
                    filename = session.get("filetodownload").toString() + "-valid.csv";
                } else if (uploaded_filename.equals("not-valid")) {
                    filename = session.get("filetodownload").toString() + "-notvalid.csv";
                } else if (uploaded_filename.equals("error")) {
                    filename = session.get("filetodownload").toString() + "-error.csv";
                }
                if (uploaded_filename.equals("valid1")) {
                    filename = session.get("filetodownload").toString() + "-valid.xls";
                } else if (uploaded_filename.equals("not-valid1")) {
                    filename = session.get("filetodownload").toString() + "-notvalid.xls";
                } else if (uploaded_filename.equals("error1")) {
                    filename = session.get("filetodownload").toString() + "-error.xls";
                }
                uploaded_filename = filename.replace(ServletActionContext.getServletContext().getInitParameter("excelLocation"), "");
                try {
                    fileInputStream = new FileInputStream(new File(filename));
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                    return "filenotfound";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String DownloadBulkSampleFile() {
        if (session.get("rand") != null) {
            try {
                fileInputStream = new FileInputStream(new File(ServletActionContext.getServletContext().getInitParameter("excelLocation") + "Bulk-FileFormat.csv"));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    public String downloaDlistBulkSampleFile() {
        if (session.get("rand") != null) {
            System.out.println("Inside Dlist Bulk Formate File:");
            try {
                fileInputStream = new FileInputStream(new File(ServletActionContext.getServletContext().getInitParameter("excelLocation") + "DlistBulk-FileFormat.xlsx"));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    public String download_Retired_Manual() {
        System.out.println("Inside download_Retired_SampleFile File:");
        try {
            fileInputStream = new FileInputStream(new File(ServletActionContext.getServletContext().getInitParameter("excelLocation") + "Retired-Officers/Retired-DOREXT_Manual.pdf"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
            return "filenotfound";
        }
        return SUCCESS;
    }

    public String DownloadDnsSampleFile() {
        if (session.get("rand") != null) {
            try {
                fileInputStream = new FileInputStream(new File(ServletActionContext.getServletContext().getInitParameter("excelLocation") + "DNS-FileFormat.csv"));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    public String DownloadDnsModifySampleFile() {
        if (session.get("rand") != null) {
            try {
                fileInputStream = new FileInputStream(new File(ServletActionContext.getServletContext().getInitParameter("excelLocation") + "DNS-FileFormat-modify.csv"));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    public String DownloadPDF() {
        System.out.println("stat_form_type: " + stat_form_type + " in download pdf");
        System.out.println("ref_num: " + ref_num);
        String tblName = "";
        if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) {
            tblName = Constants.SMS_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.SINGLE_FORM_KEYWORD)) {
            tblName = Constants.SINGLE_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.BULK_FORM_KEYWORD)) {
            tblName = Constants.BULK_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.IP_FORM_KEYWORD)) {
            tblName = Constants.IP_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.NKN_BULK_FORM_KEYWORD)) {
            tblName = Constants.NKN_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.RELAY_FORM_KEYWORD)) {
            tblName = Constants.RELAY_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.LDAP_FORM_KEYWORD)) {
            tblName = Constants.LDAP_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.DIST_FORM_KEYWORD)) {
            tblName = Constants.DIST_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.BULKDIST_FORM_KEYWORD)) {
            tblName = Constants.BULKDIST_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.IMAP_FORM_KEYWORD)) {
            tblName = Constants.IMAP_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.GEM_FORM_KEYWORD)) {
            tblName = Constants.GEM_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.MOB_FORM_KEYWORD)) {
            tblName = Constants.MOB_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.DNS_FORM_KEYWORD)) {
            tblName = Constants.DNS_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) {
            tblName = Constants.WIFI_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_BULK_FORM_KEYWORD)) {
            tblName = Constants.VPN_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.DAONBOARDING_FORM_KEYWORD)) {
            tblName = Constants.DAONBOARDING_TABLE_NAME;
        }
        String qry = "SELECT rename_sign_cert,sign_cert FROM " + tblName + " WHERE registration_no = ?";
        String filename = "";
        // select query in sms table corresponding to the reg no
        try {
            con = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(qry);
            ps.setString(1, ref_num.trim());
            System.out.println("PSSSS: " + ps);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                filename = res.getString("rename_sign_cert");
                uploaded_filename = res.getString("sign_cert");
            }
            res.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside updateFormsObj DB connection: " + stat_form_type + " - " + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "filename: " + filename);
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "esigned: " + uploaded_filename);
        if (filename != null && uploaded_filename != null) {
            try {
                System.out.println("in download");
                fileInputStream = new FileInputStream(new File(filename));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    public String DownloadPDFCA() {
        //Connection con = null;
        System.out.println("stat_form_type: " + stat_form_type);
        System.out.println("ref_num: " + ref_num);
        String tblName = "";
        if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) {
            tblName = Constants.SMS_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.SINGLE_FORM_KEYWORD)) {
            tblName = Constants.SINGLE_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.BULK_FORM_KEYWORD)) {
            tblName = Constants.BULK_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.IP_FORM_KEYWORD)) {
            tblName = Constants.IP_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.NKN_BULK_FORM_KEYWORD)) {
            tblName = Constants.NKN_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.RELAY_FORM_KEYWORD)) {
            tblName = Constants.RELAY_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.LDAP_FORM_KEYWORD)) {
            tblName = Constants.LDAP_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.DIST_FORM_KEYWORD)) {
            tblName = Constants.DIST_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.BULKDIST_FORM_KEYWORD)) {
            tblName = Constants.BULKDIST_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.IMAP_FORM_KEYWORD)) {
            tblName = Constants.IMAP_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.GEM_FORM_KEYWORD)) {
            tblName = Constants.GEM_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.MOB_FORM_KEYWORD)) {
            tblName = Constants.MOB_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.DNS_FORM_KEYWORD)) {
            tblName = Constants.DNS_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) {
            tblName = Constants.WIFI_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_BULK_FORM_KEYWORD)) {
            tblName = Constants.VPN_TABLE_NAME;
        } else if (stat_form_type.equals(Constants.DAONBOARDING_FORM_KEYWORD)) {
            tblName = Constants.DAONBOARDING_TABLE_NAME;
        }
        String qry = "SELECT ca_rename_sign_cert,ca_sign_cert FROM " + tblName + " WHERE registration_no = ?";
        String filename = "";
        // select query in sms table corresponding to the reg no
        try {
            con = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(qry);
            ps.setString(1, ref_num.trim());
            System.out.println("PSSSS: " + ps);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                filename = res.getString("ca_rename_sign_cert");
                uploaded_filename = res.getString("ca_sign_cert");
            }
            res.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside updateFormsObj DB connection: " + stat_form_type + " - " + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "filename: " + filename);
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "esigned: " + uploaded_filename);
        if (filename != null && uploaded_filename != null) {
            try {
                System.out.println("in download");
                fileInputStream = new FileInputStream(new File(filename));
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

}
