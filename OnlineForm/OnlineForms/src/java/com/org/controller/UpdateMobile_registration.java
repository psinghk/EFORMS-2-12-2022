/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.HodData;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.dao.esignDao;
import com.org.service.MobileService;
import com.org.service.ProfileService;
import com.org.service.UpdateMobileService;
import com.org.service.UpdateService;
import entities.LdapQuery;
import entities.Random;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import validation.validation;

/**
 *
 * @author dhiren
 */
public class UpdateMobile_registration extends ActionSupport implements SessionAware {

    Map session;
    FormBean form_details;
    UpdateMobileService updatemobileservice = null;
    UpdateService updateService = null;// line added by pr on 1stfeb18
    Map profile_values = null;
    validation valid = null;
    Map<String, Object> error = null;
    public String action_type, data, ReturnString, email, mobile, on_behalf_email, on_behalf_of, fname, relation, newmobile, prefixMob, reason, reason_txt;
    ProfileService profileService = null;
    Map hodDetails = null;
    esignDao esignDao = null;
    // start, code added by pr on 22ndjan18
    String CSRFRandom;
    private Database db = null;
    Map<String, String> map = null;
    private UserData userValues;
    private Ldap ldap = null;
    private String check;

    public UpdateMobile_registration() {
        valid = new validation();
        error = new HashMap();
        map = new HashMap();
        profile_values = new HashMap();
        esignDao = new esignDao();
        db = new Database();
        ldap = new Ldap();
        userValues = new UserData();
        if (userValues == null) {
            userValues = new UserData();
        }
        if (updatemobileservice == null) {
            updatemobileservice = new UpdateMobileService();
        }
        if (esignDao == null) {
            esignDao = new esignDao();
        }
        if (valid == null) {
            valid = new validation();
        }
        if (error == null) {
            error = new HashMap();
        }

        if (map == null) {
            map = new HashMap();
        }
        if (hodDetails == null) {
            hodDetails = new HashMap();
        }
        if (profileService == null) {
            profileService = new ProfileService();
        }
        if (profile_values == null) {
            profile_values = new HashMap();
        }
        if (ldap == null) {
            ldap = new Ldap();
        }

    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = Jsoup.parse(CSRFRandom).text();
    }

    // end, code added by pr on 22ndjan18
    public void setSession(Map session) {
        this.session = session;
    }

    public FormBean getForm_details() {
        return form_details;
    }

    public void setForm_details(FormBean form_details) {
        this.form_details = form_details;
    }

    public String getReturnString() {
        return ReturnString;
    }

    public void setReturnString(String ReturnString) {
        this.ReturnString = Jsoup.parse(ReturnString).text();
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = Jsoup.parse(action_type).text();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = Jsoup.parse(data).text();
    }

    public String update_mobile_tab1() {
        String result = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            db = new Database();
            System.out.println("form_details.getUser_email()" + form_details.getUser_email());
            String oldMobile = ldap.fetchMobileFromLdap(form_details.getUser_email());
            form_details.setMobile_number(mobile);
            boolean title_error = valid.nameValidation(form_details.getInitials());
            boolean fname_error = valid.nameValidation(form_details.getFname());
            boolean mname_error = valid.mnameValidation(form_details.getMname());
            boolean lname_error = valid.nameValidation(form_details.getLname());
            boolean reason_error = valid.addValidation(form_details.getReason());
            String type = "";
            if (oldMobile.contains(form_details.getNew_mobile())) {
                type = "profile";
            } else {
                type = "mobile";
            }

            String msg = valid.dorValidationMobile(form_details.getNicDateOfRetirement(), form_details.getNicDateOfBirth());
            if (!msg.isEmpty()) {
                error.put("dor_err", msg);
            }
            if (form_details.getNicDateOfBirth().isEmpty()) {
                error.put("dob_err", "Date of birth cannot be left empty");
            }
            if (form_details.getNicDateOfRetirement().isEmpty()) {
                error.put("dob_err", "Date of Retirement cannot be left empty");
            }

            if (form_details.getNicDateOfRetirement().isEmpty()) {
                error.put("des_err", "Desination cannot be left empty");
            }

            if (form_details.getDisplayName().isEmpty()) {
                error.put("dis_err", "Display name cannot be left empty");
            }

            Map<String, String> map = db.fetchOtpForUpdateMobile(form_details.getUser_email(), form_details.getNew_mobile());
            Set<String> aliases = ldap.fetchAliases(form_details.getUser_email());
            Set<String> emailsAgainstMobile = ldap.fetchPrimaryEmailsAgainstMobile("+91" + form_details.getNew_mobile());
            email=form_details.getUser_email();
            if (type.equalsIgnoreCase("mobile")) {
                if (email.toLowerCase().contains("@jcn.nic.in") && emailsAgainstMobile.size() > 4) {
                    String emailAgainstMobileInString = emailsAgainstMobile.toString();
                    error.put("mobile_error", "There are already 5 email addresses (" + emailAgainstMobileInString + ") associated with this mobile number.So, the same mobile number can't be updated.");//text change 20thmay2020
                } else {
                    if (emailsAgainstMobile.size() > 3 || emailsAgainstMobile.size() == 3) {
                        String emailAgainstMobileInString = emailsAgainstMobile.toString();
                        error.put("mobile_error", "There are already 3 email addresses (" + emailAgainstMobileInString + ") associated with this mobile number.So, the same mobile number can't be updated.");//text change 20thmay2020
                    }
                }
            }

            if (error.isEmpty()) {

                if (emailsAgainstMobile.size() < 6) {

                    if (session.get("exist_in_kavach") == null) {
                        if (session.get("newMobileNumber") != null && session.get("newMobileNumber").toString().equals("+91" + form_details.getNew_mobile())) {
                            if (form_details.getReason().equals("other")) {
                                boolean reason_txt_error = valid.addValidation(form_details.getReason_txt());
                                if (reason_txt_error == true) {
                                    error.put("reason_txt_error", "Enter Reason [Only characters,digits,whitespaces and [. , - # / ( ) ],limit[100] allowed]");
                                }
                            }

                            if (title_error == true) {
                                error.put("title_error", "Please select correct initials");
                            }
                            if (fname_error == true) {
                                error.put("fname_error", "Enter First Name [Only characters,dot(.) and whitespaces allowed]");
                            }
                            if (mname_error == true) {
                                error.put("mname_error", "Enter Middle Name [Only characters,dot(.) and whitespaces allowed]");
                            }
                            if (lname_error == true) {
                                error.put("lname_error", "Enter Last Name [Only characters,dot(.) and whitespaces allowed]");
                            }
                            if (reason_error == true) {
                                error.put("reason_error", "Please select reason");
                            }
                            if (form_details.getRadioOnBehalf().equals("On Behalf")) {
                                boolean onbehalf_email_error = valid.EmailValidation(form_details.getOn_behalf_email());
                                if (onbehalf_email_error == true) {
                                    error.put("onbehalf_email_error", "Enter Relation [Only characters,dot(.) and whitespaces allowed]");
                                }
                                boolean relation_with_error = valid.nameValidation(form_details.getRelationWithOwner());
                                if (relation_with_error == true) {
                                    error.put("relation_with_error", "Please select relation with owner");
                                }
                                if (form_details.getOtherRelation().equals("other")) {
                                    boolean relationtxt_with_error = valid.addValidation(form_details.getOtherRelation());
                                    if (relationtxt_with_error == true) {
                                        error.put("relationtxt_with_error", "Enter Your Official Address [Only characters,digits,whitespaces and [. , - # / ( ) ],limit[100] allowed]");
                                    }
                                }

                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Update Mobile controller tab 0: " + map);
                                if (!map.isEmpty()) {

                                    session.put("ref_num", map.get("registration_no"));
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Update Mobile controller tab 1: " + session.get("ref_num"));
                                }

                                if (!ldap.emailValidate(form_details.getOn_behalf_email())) {
                                    error.put("nongov_error", "Please Enter Your NIC/Gov Address");

                                }
                                if (session.get("on_behalf_otp") == null || form_details.getNewcodeonbehalf() == null || session.get("on_behalf_otp").equals("") || form_details.getNewcodeonbehalf().equals("")) {
                                    error.put("otp_onbehalf_error", "Please enter otp");
                                }
                                if (session.get("on_behalf_otp") != null && (!session.get("on_behalf_otp").toString().equals(form_details.getNewcodeonbehalf()))) {
                                    error.put("otp_onbehalf_error", "Please enter correct otp");

                                }
                                if (!form_details.getOn_behalf_email().equals("")) {
                                    for (String aliase : aliases) {
                                        if (aliase.contains(form_details.getOn_behalf_email())) {
                                            error.put("nongov_error", "This email address belongs to the owner whose mobile number is being updated. Please enter your email address");

                                        }
                                    }
                                } else {
                                    error.put("otp_onbehalf_error", "Please Enter Your NIC/Gov Address");
                                }

                            }

                            // end validate profile 20th march
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for Mobile controller tab 2: " + error);
                            if (!error.isEmpty()) {
                                data = "";
                                CSRFRandom = "";
                                action_type = "";
                            } else {
                                session.put("userName", form_details.getUser_name());
                                session.put("form_type", "updatemobile");
                                session.put("admin_role", "user");
                                session.put("check", "esign");
                                session.put("newMobileForUpdate", "+91" + form_details.getNew_mobile());
                                session.put("newcode", form_details.getNewcode());
                                session.put("useremail", form_details.getUser_email());
                                session.put("update_without_oldmobile", "yes");
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Update Mobile controller tab####: " + session.get("newMobileForUpdate"));
                                session.put("ref_num", map.get("registration_no"));
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Update Mobile controller tab 2: " + session.get("ref_num"));
                                result = updatemobileservice.update_mobile_tab1(form_details);
                                session.put("updatemobile_details", form_details);
                                System.out.println("result" + result);
                            }
                        }
                    }
                }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception for Mobile controller tab 1: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
        }
        ReturnString = result;
        return SUCCESS;
    }

    public String consentMobile() {
        String returnString = "";
        if (check.equals("esign")) {
            GeneratePDF_mobile();
            returnString = "PDF";
        }
        return SUCCESS;
    }

    public String GeneratePDF_mobile() {
        form_details = (FormBean) session.get("updatemobile_details");
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String ref_num = (String) session.get("ref_num");
            file = new FileOutputStream(ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf");
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Mobile Update Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont));// line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            document.add(emptypara);
            Paragraph details = new Paragraph();

            details.add(new Paragraph("1) Applicant Name* : " + form_details.getFname(), normalFont));
            details.add(new Paragraph("2) Applicant Email* : " + form_details.getUser_email(), normalFont));
            details.add(new Paragraph("3) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("4) New Mobile Number* : " + form_details.getCountry_code() + form_details.getNew_mobile(), normalFont));
            document.add(details);

            document.add(details);
            document.add(emptypara);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Reporting/Nodal/Forwarding Officer with date and seal", normalFont));
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
            tncpoints.add(new Paragraph("1. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("2. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("3. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("4. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("5. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("6. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("7. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

}
