/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.connections.DbConnection;
import com.org.controller.Vpn_registration;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Gyan Pc
 */
public class UserTrackWifi{

    private String data;
    private InputStream fileInputStream;
    String genfileDownload;
    String ip = ServletActionContext.getRequest().getRemoteAddr();
    String printlog;
    Map session;

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    public String getGenfileDownload() {
        return genfileDownload;
    }

    public void setGenfileDownload(String genfileDownload) {
        this.genfileDownload = genfileDownload;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    
        
    public String wifimac(String refno, FileOutputStream file) {
        UserTrack userTrack = new UserTrack();
        PreparedStatement pst = null;
        Connection conSlave = null;
        ResultSet rs = null;
        //String refno = getData();
//        setData(refno);
//        session.put("ref", refno);
        String sql = "select * from wifi_registration where registration_no=?";
        pst = null;
        //String json = null;
       // Map<String, Object> prvwdetails = new HashMap<String, Object>();
        try {
//                    con = getConnection();
            conSlave = DbConnection.getSlaveConnection();
            pst = conSlave.prepareStatement(sql);
            pst.setString(1, refno);
            rs = pst.executeQuery();
            while (rs.next()) {
                try {
                    String final_dept;
                    /*switch (rs.getString("employment")) {
                                case "Central":
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "DEPT TAARARRRR " + rs.getString("department"));
                                    if (rs.getString("department").equalsIgnoreCase("other")) {
                                        final_dept = "1) Ministry/Department* : " + rs.getString("ministry") + " / " + rs.getString("other_dept");
                                    } else {
                                        final_dept = "1) Ministry/Department* : " + rs.getString("ministry") + " / " + rs.getString("department");
                                    }
                                    break;
                                case "Others":
                                case "Psu":
                                case "Const":
                                    if (rs.getString("organization").equalsIgnoreCase("other")) {
                                        final_dept = "1) Organization Name* : " + rs.getString("organization");
                                    } else {
                                        final_dept = "1) Organization Name* : " + rs.getString("organization");
                                    }
                                    break;
                                default:
                                    if (rs.getString("organization").equalsIgnoreCase("other")) {
                                        final_dept = "1) State/Department* : " + rs.getString("state") + " / " + rs.getString("other_dept");
                                    } else {
                                        final_dept = "1) State/Department* : " + rs.getString("state") + " / " + rs.getString("department");
                                    }
                                    break;
                            }*/

                    // above code modified by pr on 10thjul19
                    switch (rs.getString("employment")) {
                        case "Central":
                            if (rs.getString("department").equalsIgnoreCase("other")) {
                                final_dept = "1) Ministry/Department* : " + rs.getString("ministry") + " / " + rs.getString("other_dept");
                            } else {
                                final_dept = "1) Ministry/Department* : " + rs.getString("ministry") + " / " + rs.getString("department");
                            }
                            break;
                        case "Others":
                        case "Psu":
                        case "Const":
                        case "Nkn":
                            if (rs.getString("organization").equalsIgnoreCase("other")) {
                                final_dept = "1) Organization Name* : " + rs.getString("organization") + " / " + rs.getString("other_dept");  // other dept added by pr on 10thjul19
                            } else {
                                final_dept = "1) Organization Name* : " + rs.getString("organization");  // other department added by pr on 10thjul19
                            }
                            break;
                        default:
                            /*if (rs.getString("organization").equalsIgnoreCase("other")) {
                                        final_dept = "1) State/Department* : " + rs.getString("state") + " / " + rs.getString("other_dept");
                                    } else {
                                        final_dept = "1) State/Department* : " + rs.getString("state") + " / " + rs.getString("department");
                                    }*/

                            if (rs.getString("department").equalsIgnoreCase("other")) {
                                final_dept = "1) State/Department* : " + rs.getString("state") + " / " + rs.getString("other_dept");
                            } else {
                                final_dept = "1) State/Department* : " + rs.getString("state") + " / " + rs.getString("department");
                            }

                            break;
                    }

                    
                    Document document = new Document();
                    PdfWriter writer = PdfWriter.getInstance(document, file);
                    document.open();
                    Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
                    Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
                    Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
                    Paragraph emptypara = new Paragraph("\n");
                    Paragraph header = new Paragraph();
                    header.add(new Paragraph("Government of India", HeaderFont));
                    header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
                    header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
                    header.add(new Paragraph("WIFI Request Form", boldFont));
                    /*header.add(new Paragraph("(In case of MANUAL submission, the COMPLETED application form, duly signed by the concerned Project Coordinator"
                                + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
                    header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                            + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont));// line modified by pr on 18thjan19
                    header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
                    header.setAlignment(header.ALIGN_CENTER);
                    document.add(header);
                    document.add(emptypara);
                    Paragraph reg_num = new Paragraph("Registration number :" + refno, boldFont);
                    reg_num.setAlignment(reg_num.ALIGN_RIGHT);
                    document.add(reg_num);
                    document.add(emptypara);
                    Paragraph details = new Paragraph();
                    details.add(new Paragraph(final_dept, normalFont));
                    details.add(new Paragraph("2) Applicant Name* : " + rs.getString("auth_off_name"), normalFont));
                    details.add(new Paragraph("3) Applicant Email* : " + rs.getString("auth_email") + "       4) Applicant Mobile* : " + rs.getString("mobile"), normalFont));
                    details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + rs.getString("hod_name"), normalFont));
                    details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + rs.getString("hod_email") + "        7) Reporting/Nodal/Forwarding Officer Mobile* : " + rs.getString("hod_mobile") + "         Reporting/Nodal/Forwarding Officer Designation* : " + rs.getString("ca_desig"), normalFont));
                    document.add(details);
                    document.add(emptypara);
                    if (rs.getString("wifi_process").equals("request")) {
                        if (rs.getString("wifi_type").equals("nic")) {
                            PdfPTable iptable = new PdfPTable(2); // 4 columns.
                            PdfPCell cell1 = new PdfPCell(new Paragraph("MAC Address of the Device", boldFont));
                            PdfPCell cell2 = new PdfPCell(new Paragraph("Operating System of the Device", boldFont));
                            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                            iptable.addCell(cell1);
                            iptable.addCell(cell2);
                            iptable.addCell(new Paragraph(rs.getString("wifi_mac1"), normalFont));
                            iptable.addCell(new Paragraph(rs.getString("wifi_os1"), normalFont));
                            if (!rs.getString("wifi_mac2").equals("") && rs.getString("wifi_mac2") != null) {
                                iptable.addCell(new Paragraph(rs.getString("wifi_mac2"), normalFont));
                                iptable.addCell(new Paragraph(rs.getString("wifi_os2"), normalFont));
                            }
                            if (!rs.getString("wifi_mac3").equals("") && rs.getString("wifi_mac3") != null) {
                                iptable.addCell(new Paragraph(rs.getString("wifi_mac3"), normalFont));
                                iptable.addCell(new Paragraph(rs.getString("wifi_os3"), normalFont));
                            }
                            if (!rs.getString("wifi_mac4").equals("") && rs.getString("wifi_mac4") != null) {
                                iptable.addCell(new Paragraph(rs.getString("wifi_mac4"), normalFont));
                                iptable.addCell(new Paragraph(rs.getString("wifi_os4"), normalFont));
                            }
                            document.add(iptable);
                        } else {
                            PdfPTable iptable = new PdfPTable(2); // 4 columns.
                            PdfPCell cell1 = new PdfPCell(new Paragraph("WIFI Request For", boldFont));
                            PdfPCell cell2 = new PdfPCell(new Paragraph("Time Duration", boldFont));
                            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                            iptable.addCell(cell1);
                            iptable.addCell(cell2);
                            iptable.addCell(new Paragraph(rs.getString("wifi_request"), normalFont));
                            iptable.addCell(new Paragraph(rs.getString("wifi_time") + " " + rs.getString("wifi_duration"), normalFont));
                            document.add(iptable);
                        }
                    }
                    Paragraph footer = new Paragraph();
                    footer.add(new Paragraph(""));
                    footer.add(new Paragraph(""));
                    footer.add(new Paragraph(""));
                    footer.add(new Paragraph("Signature of the Applicant with date and seal", normalFont));
                    footer.add(new Paragraph(""));
                    footer.add(new Paragraph(""));
                    footer.add(new Paragraph(""));
                    footer.add(new Paragraph("  Signature of the Reporting/Nodal/Forwarding Officer with date and seal", normalFont));
                    footer.add(new Paragraph(""));
                    footer.add(new Paragraph(""));
                    footer.add(new Paragraph(""));
                    footer.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", normalFont));
                    footer.setAlignment(footer.ALIGN_RIGHT);
                    document.add(footer);
                    document.newPage();
                    Paragraph tnc = new Paragraph();
                    tnc.add(new Paragraph("Terms And Conditions", boldFont));
                    tnc.setAlignment(header.ALIGN_CENTER);
                    document.add(tnc);
                    document.add(emptypara);
                    Paragraph tncpoints = new Paragraph();
                    tncpoints.add(new Paragraph("1. Entries marked with asterisk  (*) are mandatory", normalFont));
                    tncpoints.add(new Paragraph("2. Only three devices allowed per user ID.", normalFont));
                    tncpoints.add(new Paragraph("3. For iPhone/iPad/MAC, write ios(version) in Operating System.", normalFont));
                    tncpoints.setAlignment(header.ALIGN_LEFT);
                    document.add(tncpoints);
                    document.add(emptypara);
                    // below function call added by pr on 3rdjan19, table created with status flow
                    userTrack.createStatusTable(refno, document, header, emptypara, normalFont, boldFont, rs.getString("auth_email"), rs.getString("mobile"), rs.getString("userip"));
                    document.close();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(printlog + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "PDF WIFI exception: " + e.getMessage());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "PDF WIFI exception: " + e.getMessage());
        }
        return SUCCESS;
    }
}
