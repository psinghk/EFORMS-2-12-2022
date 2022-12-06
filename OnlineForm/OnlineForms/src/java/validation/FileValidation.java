package validation;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.opencsv.CSVReader;
import com.org.bean.DnsBean;
import com.org.bean.EmailBean;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import ognl.OgnlException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import validation.validation;

/**
 *
 * @author nikki
 */
public class FileValidation {

    public Boolean checkPDF(File destFile) {
        boolean wrongfileflag = false;
        try {

            boolean safeState = false;
            InputStream inputstream = new FileInputStream(destFile);
            //inputstream = new FileInputStream(new File("D:\\abc.pdf"));
            PdfReader reader = new PdfReader(inputstream);
            if (reader == null) {
                wrongfileflag = true;
            }
            int n = reader.getNumberOfPages();
            String page = PdfTextExtractor.getTextFromPage(reader, 1);
            // Check 1:
            // Detect if the document contains any JavaScript code
            String jsCode = reader.getJavaScript();
            if (jsCode == null) {
                PdfDictionary root = reader.getCatalog();
                PdfDictionary names = root.getAsDict(PdfName.NAMES);
                PdfArray namesArray = null;
                if (names != null) {
                    PdfDictionary embeddedFiles = names.getAsDict(PdfName.EMBEDDEDFILES);
                    namesArray = embeddedFiles.getAsArray(PdfName.NAMES);
                }
                // Get safe state from number of embedded files
                safeState = ((namesArray == null) || namesArray.isEmpty());
            }

            if (!safeState) {
                wrongfileflag = true;
            }
            inputstream.close();
        } catch (IOException e) {
            wrongfileflag = true;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } catch (Exception e) {
            wrongfileflag = true;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } catch (NoClassDefFoundError e) {
            wrongfileflag = true;
            System.out.println("NoClassDefFoundError " + e.getMessage());
        }
        return wrongfileflag;
    }

    public Boolean checkExcel(File destFile) {
        boolean wrongfileflag = false;
        try {
            InputStream inputstream = new FileInputStream(destFile);
            org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(inputstream);
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            int RowCount = sheet.getPhysicalNumberOfRows();
            if (RowCount > 3000) {
                wrongfileflag = true;
            }
            inputstream.close();
        } catch (Exception e) {
            wrongfileflag = true;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
        return wrongfileflag;
    }

    public Boolean checkCSV(File destFile) {
        boolean wrongfileflag = false;
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(destFile));
            String[] line1;
            int length = 0;
            while ((line1 = reader.readNext()) != null) {
                length++;
            }
            if (length > 3000) {
                wrongfileflag = true;
            }
            reader.close();
        } catch (IOException e) {
            wrongfileflag = true;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
        return wrongfileflag;
    }

    public Set<EmailBean> checkCSVForBULK(File destFile) {
        CSVReader reader = null;
        Set<EmailBean> emailData = new HashSet<>();
        try {
            reader = new CSVReader(new FileReader(destFile));
            String[] line;
            int counter = 0;
            while ((line = reader.readNext()) != null) {
                counter++;
                if (line.length == 1 && line[0].isEmpty()) {
                } else {
                    if (line[0] != null && !line[0].contains("First Name") && !line[0].isEmpty()) {
                        emailData.add(populateEmailBean(line, counter));
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
        System.out.println("FILE DATA DURING VALIDATION ::: " + emailData);
        return emailData;
    }

    public Set<DnsBean> checkCSVForDNS(File destFile, String req_type, String req_other_type) {
        CSVReader reader = null;
        Set<DnsBean> dnsData = new HashSet<>();
        try {
            reader = new CSVReader(new FileReader(destFile));
            String[] line;
            int counter = 0;
            while ((line = reader.readNext()) != null) {
                counter++;
                if (line.length == 1 && line[0].isEmpty()) {
                } else {
                    if (!isLineEmpty(line, req_type, req_other_type)) {
                        if (line.length > 1 && line[0].contains("Domain U")) {
                        } else {
                            //DnsBean dnsBean = populateDnsBean(line, req_type, req_other_type);
                            dnsData.add(populateDnsBean(line, req_type, req_other_type, counter));
                            //System.out.println("FILE DATA DURING VALIDATION ::: " + dnsBean);
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
        System.out.println("FILE DATA DURING VALIDATION ::: " + dnsData);
        return dnsData;
    }

    public Boolean checkImage(File destFile) {
        boolean wrongfileflag = false;
        try {
            InputStream inputstream = new FileInputStream(destFile);
            Image image = null;
            try {
                image = ImageIO.read(inputstream);
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
            }
            if (image == null) {
                wrongfileflag = true;
            }
            inputstream.close();
        } catch (Exception e) {
            wrongfileflag = true;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
        return wrongfileflag;
    }

    private boolean isLineEmpty(String[] line, String req_type, String req_other_type) {
        boolean flag = false;
        switch (req_type) {
            case "req_new":
            case "req_delete":
                switch (req_other_type) {
                    case "cname":
                    case "mx":
                    case "txt":
                    case "spf":
                    case "dmarc":
                    case "srv":
                    case "ptr":
                        if (line.length == 4) {
                            if (line[0].isEmpty() && line[1].isEmpty() && line[2].isEmpty() && line[3].isEmpty()) {
                                flag = true;
                            }
                        }
                        break;
                    case "":
                        if (line.length == 5) {
                            if (line[0].isEmpty() && line[1].isEmpty() && line[2].isEmpty() && line[3].isEmpty() && line[4].isEmpty()) {
                                flag = true;
                            }
                        }
                        break;
                }
                break;
            case "req_modify":
                switch (req_other_type) {
                    case "cname":
                    case "mx":
                    case "txt":
                    case "spf":
                    case "dmarc":
                    case "srv":
                    case "ptr":
                        if (line.length == 5) {
                            if (line[0].isEmpty() && line[1].isEmpty() && line[2].isEmpty() && line[3].isEmpty() && line[4].isEmpty()) {
                                flag = true;
                            }
                        }
                        break;
                    case "":
                        if (line.length == 6) {
                            if (line[0].isEmpty() && line[1].isEmpty() && line[2].isEmpty() && line[3].isEmpty() && line[4].isEmpty() && line[5].isEmpty()) {
                                flag = true;
                            }
                        }
                        break;
                }
                break;
        }
        return flag;
    }

    private DnsBean populateDnsBean(String[] line, String req_type, String req_other_type, int counter) {
        DnsBean dnsData = new DnsBean();
        switch (req_type) {
            case "req_new":
            case "req_delete":
                dnsData.setReq_(req_type);
                dnsData.setReq_other_add(req_other_type);
                dnsData.setId(counter);
                switch (req_other_type) {
                    case "cname":
                        if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setCname_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                            dnsData.setMigration_date(line[3]);
                        } else if (line.length == 3) {
                            dnsData.setDomain(line[0]);
                            dnsData.setCname_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                        }
                        break;
                    case "mx":
                        if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setMx_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                            dnsData.setMigration_date(line[3]);
                        } else if (line.length == 3) {
                            dnsData.setDomain(line[0]);
                            dnsData.setMx_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                        }
                        break;
                    case "txt":
                        if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setTxt_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                            dnsData.setMigration_date(line[3]);
                        } else if (line.length == 3) {
                            dnsData.setDomain(line[0]);
                            dnsData.setTxt_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                        }
                        break;
                    case "spf":
                        if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setSpf_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                            dnsData.setMigration_date(line[3]);
                        } else if (line.length == 3) {
                            dnsData.setDomain(line[0]);
                            dnsData.setSpf_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                        }
                        break;
                    case "dmarc":
                        if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setDmarc_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                            dnsData.setMigration_date(line[3]);
                        } else if (line.length == 3) {
                            dnsData.setDomain(line[0]);
                            dnsData.setDmarc_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                        }
                        break;
                    case "srv":
                        if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setSrv_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                            dnsData.setMigration_date(line[3]);
                        } else if (line.length == 3) {
                            dnsData.setDomain(line[0]);
                            dnsData.setSrv_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                        }
                        break;
                    case "ptr":
                        if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setPtr_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                            dnsData.setMigration_date(line[3]);
                        } else if (line.length == 3) {
                            dnsData.setDomain(line[0]);
                            dnsData.setPtr_txt(line[1]);
                            dnsData.setDns_loc(line[2]);
                        }
                        break;
                    case "":
                        if (line.length == 5) {
                            dnsData.setDomain(line[0]);
                            dnsData.setCname(line[1]);
                            dnsData.setNew_ip(line[2]);
                            dnsData.setDns_loc(line[3]);
                            dnsData.setMigration_date(line[4]);
                        } else if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setCname(line[1]);
                            dnsData.setNew_ip(line[2]);
                            dnsData.setDns_loc(line[3]);
                        }
                        break;
                }
                break;
            case "req_modify":
                dnsData.setReq_(req_type);
                dnsData.setReq_other_add(req_other_type);
                dnsData.setId(counter);
                switch (req_other_type) {
                    case "cname":
                        if (line.length == 5) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_cname_txt(line[1]);
                            dnsData.setCname_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                            dnsData.setMigration_date(line[4]);
                        } else if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_cname_txt(line[1]);
                            dnsData.setCname_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                        }
                        break;
                    case "mx":
                        if (line.length == 5) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_mx_txt(line[1]);
                            dnsData.setMx_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                            dnsData.setMigration_date(line[4]);
                        } else if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_mx_txt(line[1]);
                            dnsData.setMx_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                        }
                        break;
                    case "txt":
                        if (line.length == 5) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_txt_txt(line[1]);
                            dnsData.setTxt_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                            dnsData.setMigration_date(line[4]);
                        } else if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_txt_txt(line[1]);
                            dnsData.setTxt_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                        }
                        break;
                    case "spf":
                        if (line.length == 5) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_spf_txt(line[1]);
                            dnsData.setSpf_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                            dnsData.setMigration_date(line[4]);
                        } else if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_spf_txt(line[1]);
                            dnsData.setSpf_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                        }
                        break;
                    case "dmarc":
                        if (line.length == 5) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_dmarc_txt(line[1]);
                            dnsData.setDmarc_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                            dnsData.setMigration_date(line[4]);
                        } else if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_dmarc_txt(line[1]);
                            dnsData.setDmarc_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                        }
                        break;
                    case "srv":
                        if (line.length == 5) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_srv_txt(line[1]);
                            dnsData.setSrv_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                            dnsData.setMigration_date(line[4]);
                        } else if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_srv_txt(line[1]);
                            dnsData.setSrv_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                        }
                        break;
                    case "ptr":
                        if (line.length == 5) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_ptr_txt(line[1]);
                            dnsData.setPtr_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                            dnsData.setMigration_date(line[4]);
                        } else if (line.length == 4) {
                            dnsData.setDomain(line[0]);
                            dnsData.setOld_ptr_txt(line[1]);
                            dnsData.setPtr_txt(line[2]);
                            dnsData.setDns_loc(line[3]);
                        }
                        break;
                    case "":
                        if (line.length == 6) {
                            dnsData.setDomain(line[0]);
                            dnsData.setCname(line[1]);
                            dnsData.setOld_ip(line[2]);
                            dnsData.setNew_ip(line[3]);
                            dnsData.setDns_loc(line[4]);
                            dnsData.setMigration_date(line[5]);
                        } else if (line.length == 5) {
                            dnsData.setDomain(line[0]);
                            dnsData.setCname(line[1]);
                            dnsData.setOld_ip(line[2]);
                            dnsData.setNew_ip(line[3]);
                            dnsData.setDns_loc(line[4]);
                        }
                        break;
                }
                break;
        }
        return dnsData;
    }

    private EmailBean populateEmailBean(String[] line1, int counter) {
        EmailBean formbean = new EmailBean();
        formbean.setId(counter);
        formbean.setFname(line1[0].replaceAll("[^a-zA-Z0-9]", " "));
        formbean.setLname(line1[1].replaceAll("[^a-zA-Z0-9]", " "));
        formbean.setDesignation(line1[2].replaceAll("[^a-zA-Z0-9_/(/)-/[/]:]", " "));
        formbean.setDepartment(line1[3].replaceAll("[^a-zA-Z0-9,/-/(/)/&]", " "));
        formbean.setState(line1[4].replaceAll("[^a-zA-Z0-9/&/-]", " "));
        formbean.setCountry_code(line1[5].replaceAll("[^0-9]", " "));
        formbean.setMobile(line1[6].replaceAll("[^0-9]", " "));
        formbean.setDor(line1[7].replaceAll("[^0-9/-]", " "));
        formbean.setUid(line1[8].replaceAll("[^a-zA-Z0-9-/._]", " "));
        formbean.setMail(line1[9].replaceAll("[^a-zA-Z0-9-/._@]", " "));
        if (line1.length == 11) {
            formbean.setDob(line1[10]);
        } else if (line1.length == 12) {
            formbean.setDob(line1[10]);
            formbean.setEmpcode(line1[11]);
        }
        return formbean;

    }

}
