package validation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Nancy
 */
public class validation {

    public boolean EmailValidation(String email) {
        boolean flag = false;
        if (email.isEmpty()) {
            flag = true;
        } else if (!email.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean WebcastMobileValidation(String mobile) {
        boolean flag = false;
        if (mobile.isEmpty()) {
            flag = true;
        } else {

            if (!mobile.matches("^[+0-9]{10}$")) {

                flag = true;
            }
        }
        return flag;
    }

    public boolean MobileValidation(String mobile) {
        boolean flag = false;
        if (mobile.isEmpty()) {
            flag = true;
        } else {
            if (mobile.startsWith("+91") && !mobile.matches("^[+0-9]{13}$")) {
                flag = true;
            } else {
                if (!mobile.matches("^[+0-9]{8,15}$") && !mobile.startsWith("+91")) {

                    flag = true;
                }
            }
        }
        return flag;
    }

    public boolean applicantMobileValidation(String mobile) {
        boolean flag = false;
        if (mobile.isEmpty()) {
            flag = true;
        } else {
            if (!mobile.matches("^[+0-9]{13}$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean UpdateMobileValidation(String mobile, String code) {
        boolean flag = false;
        code = code.trim();
        if (code.equals("+91")) {
            if (mobile.isEmpty()) {
                flag = true;
            } else if (!mobile.matches("^[0-9]{10}$")) {
                flag = true;
            }
        } else if (mobile.isEmpty()) {
            flag = true;
        } else if (!mobile.matches("^[0-9]{8,12}$")) {
            flag = true;
        }
        return flag;
    }

    public String titleValidation(String title) {
        String msg = "";
        if (title.isEmpty()) {
            msg = "Please select title";
        } else if (!title.matches("^[a-zA-Z.]{2,6}$")) {
            msg = "Please Enter title in correct format";
        }
        return msg;
    }

    public boolean nameValidation(String fname) {
        boolean flag = false;
        if (fname.isEmpty() || fname.trim().equals("")) {
            flag = true;
        } else if (!fname.matches("^[a-zA-Z .,()]{1,50}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean mnameValidation(String fname) {
        boolean flag = false;
        if (!fname.isEmpty() && !fname.trim().equals("")) {
            if (!fname.matches("^[a-zA-Z .,]{1,50}$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean nameSmsValidation(String fname) {
        boolean flag = false;
        if (fname.isEmpty() || fname.trim().equals("")) {
            flag = true;
        } else if (!fname.matches("^[a-zA-Z0-9 .,]{1,50}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean telValidation(String tel1) {
        boolean flag = false;
        if (!tel1.isEmpty()) {
            if (!tel1.matches("^[+0-9]{3,5}[-]([0-9]{6,15})$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean roTelValidation(String tel1) {
        boolean flag = false;
        if (tel1.isEmpty()) {
            return true;
        }
        if (!tel1.matches("^[+0-9]{3,5}[-]([0-9]{6,15})$")) {
            flag = true;
        }
        return flag;
    }

    public boolean tel1Validation(String tel1) {
        boolean flag = false;
        if (!tel1.isEmpty()) {
            if (!tel1.matches("^[+0-9]{3,5}[-]([0-9]{6,15})$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean employcodevalidation(String employcode) {
        boolean flag = false;
        if (!employcode.isEmpty()) {
            if (!employcode.matches("^[a-zA-_Z0-9]{2,17}$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean dnsemploycodevalidation(String employcode) {
        boolean flag = false;
        if (employcode.isEmpty()) {
            flag = true;
        } else if (!employcode.matches("^[a-zA-_Z0-9]{2,17}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean addValidation(String add1) {
        boolean flag = false;
        if (add1.isEmpty()) {
            flag = true;
        } else if (!add1.matches("^[a-zA-Z#0-9\\s,.\\-\\/\\(\\)]{2,100}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean banknameValidation(String add1) {
        boolean flag = false;
        if (add1.isEmpty()) {
            flag = false;
        } else if (!add1.matches("^[a-zA-Z#0-9\\s,.\\-\\/\\(\\)]{2,100}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean vpnRemarksValidation(String add1) {
        boolean flag = false;
        if (!add1.isEmpty()) {
            if (!add1.matches("^[a-zA-Z#0-9\\s,.\\-\\/\\(\\)]{2,100}$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean cityValidation(String city) {
        boolean flag = false;
        if (city == null || city.isEmpty()) {
            flag = true;
        } else if (!city.matches("^[a-zA-Z*#& 0-9\\s,.\\-\\/\\(\\)]{1,100}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean pinValidation(String pin) {
        boolean flag = false;
        if (pin == null || pin.isEmpty()) {
            flag = true;
        } else if (!pin.matches("^[0-9]{6}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean EmploymentValidation(String employ) {
        boolean flag = false;
        if (employ.isEmpty()) {
            flag = true;
        } else if (!employ.matches("^[a-zA-Z]{1,10}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean MinistryValidation(String min) {
        boolean flag = false;
        if (min.isEmpty()) {
            flag = true;
        } else if (!min.matches("^[a-zA-Z#0-9\\s,'.\\-\\/\\&\\_(\\)]{2,100}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean purposeValidation(String usage) {
        boolean flag = false;
        if (!usage.isEmpty()) {
            if (!usage.matches("^[a-zA-Z .,]{1,50}$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean serviceValidation(String service) {
        boolean flag = false;
        if (service.isEmpty()) {
            flag = true;
        }
        return flag;
    }

    public boolean pullurlValidation(String pull_url) {
        boolean flag = false;
        pull_url = pull_url.toLowerCase();
        if (pull_url.isEmpty()) {
            flag = true;
        } else if (!pull_url.matches("^(?:(?:(?:https?|ftp):)?\\/\\/)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})).?)(?::\\d{2,5})?(?:[/?#]\\S*)?$")) {
            flag = true;
        }
        return flag;
    }

    public boolean pullkeywordValidation(String pull_keyword) {
        boolean flag = false;
        pull_keyword = pull_keyword.toUpperCase();
        if (pull_keyword.isEmpty()) {
            flag = true;
        } else if (!pull_keyword.matches("^[a-zA-Z0-9]{1,15}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean baseipValidation(String baseip) {
        boolean flag = false;
        if (baseip == null || baseip.isEmpty()) {
            flag = true;
        } else if (!baseip.matches("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
            flag = true;
            //} else if (baseip.startsWith("0") || baseip.equals("0.0.0.0") || baseip.equals("127.0.0.1") || baseip.equals("255.255.255.255") || baseip.endsWith("255")) {
        } else if (baseip.startsWith("0") || baseip.equals("0.0.0.0") || baseip.equals("127.0.0.1") || baseip.equals("255.255.255.255")) {
            flag = true;
        }
        return flag;
    }

    public boolean vpnValidation(String baseip) {
        boolean flag = false;
        if (baseip == null || baseip.isEmpty()) {
            flag = true;
        } else if (!baseip.matches("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
            flag = true;
        } else if (baseip.startsWith("0") || baseip.equals("0.0.0.0") || baseip.equals("127.0.0.1") || baseip.equals("255.255.255.255")) {
            flag = true;
        }
        return flag;
    }

    public boolean serviceipValidation(String serviceip) {
        boolean flag = false;
        if (!serviceip.isEmpty()) {
            if (!serviceip.matches("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
                flag = true;
                //} else if (serviceip.startsWith("0") || serviceip.equals("0.0.0.0") || serviceip.equals("127.0.0.1") || serviceip.equals("255.255.255.255") || serviceip.endsWith("255")) {
            } else if (serviceip.startsWith("0") || serviceip.equals("0.0.0.0") || serviceip.equals("127.0.0.1") || serviceip.equals("255.255.255.255")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean ipstagingvalidation(String ipstaging) {
        boolean flag = false;
        if (!ipstaging.isEmpty()) {
            if (!ipstaging.matches("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
                flag = true;

                //} else if (ipstaging.startsWith("0") || ipstaging.startsWith("0") || ipstaging.equals("0.0.0.0") || ipstaging.equals("127.0.0.1") || ipstaging.equals("255.255.255.255") || ipstaging.endsWith("255")) {
            } else if (ipstaging.startsWith("0") || ipstaging.startsWith("0") || ipstaging.equals("0.0.0.0") || ipstaging.equals("127.0.0.1") || ipstaging.equals("255.255.255.255")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean domesticTrafficvalidation(String domestictraff) {
        boolean flag = false;
        if (domestictraff.isEmpty()) {
            flag = true;
        } else if (!domestictraff.matches("^[0-9]{4,9}$")) {
            flag = true;
        } else {
            int a = Integer.parseInt(domestictraff);
            int tr = a * 1;
            if (tr < 1000) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean intertrafvalidation(String intertraff) {
        boolean flag = false;
        if (!intertraff.isEmpty()) {
            if (!intertraff.matches("^[0-9]{4,9}$")) {
                flag = true;
            } else {
                int a = Integer.parseInt(intertraff);
                int tr = a * 1;
                if (tr < 1000) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    public boolean desigValidation(String desig) {
        boolean flag = false;
        boolean numeric = desig.matches("-?\\d+(\\.\\d+)?");
        if (desig.isEmpty()) {
            flag = true;
        } else {
            if (numeric) {
                flag = true;
            } else {
                if (!desig.matches("^[a-zA-Z0-9 .,\\-\\_\\&]{2,100}$")) {
                    flag = true;
                } else {
                }
            }
        }
        return flag;
    }

    public boolean scodeValidation(String scode) {
        boolean flag = false;
        if (scode.isEmpty()) {
            flag = true;
        } else if (!scode.matches("^[0-9]{3,10}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean senderidValidation(String sid) {
        boolean flag = false;
        if (sid.isEmpty()) {
            flag = true;
        } else if (sid.matches("^[0-9]{4,7}$") || sid.matches("^[a-zA-Z]{6}$")) {

        } else {
            flag = true;
        }
        return flag;
    }

    public boolean notblankValidation(String url) {
        boolean flag = false;
        if (url.isEmpty()) {
            flag = true;
        }
        return flag;
    }

    public boolean macvalidation(String mac_address) {
        boolean flag = false;
        if (mac_address.isEmpty()) {
            flag = true;
        } else if (!mac_address.matches("^(([0-9A-Fa-f]{2}[:]){5}([0-9A-Fa-f]{2}))|(([0-9A-Fa-f]{2}[\\-]){5}([0-9A-Fa-f]{2}))$")) {
            flag = true;
        }
        if (mac_address.matches("^(([0-9A-Fa-f]{2}[:]){5}([0-9A-Fa-f]{2}))|(([0-9A-Fa-f]{2}[\\-]){5}([0-9A-Fa-f]{2}))$")) {
            if (mac_address.substring(1, 2).equals("2") || mac_address.substring(1, 2).equals("6") || mac_address.substring(1, 2).equalsIgnoreCase("a") || mac_address.substring(1, 2).equalsIgnoreCase("e")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean numericValidation(String duration, String dur) {
        boolean flag = false;
        if (dur.equals("months")) {
            if (!duration.equals("") && duration.matches("^[0-9]{1}$")) {
                int time = Integer.parseInt(duration);
                if (time <= 3) {

                } else {
                    flag = true;
                }
            } else {
                flag = true;
            }
        }
        return flag;
    }

    public boolean numericdayValidation(String duration, String dur) {
        boolean flag = false;
        if (duration.isEmpty()) {
            flag = true;
        } else if (!duration.isEmpty()) {
            if (!duration.matches("^[0-9]{0,3}$")) {
                flag = true;
            } else {
                int days = Integer.parseInt(duration);
                if (days <= 90) {

                } else {
                    flag = true;
                }
            }
        }
        return flag;
    }

    public String dobValidation(String dob) throws ParseException {
        String msg = "";
        if (!dob.isEmpty()) {
            if (!dob.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
                msg = "Please select Date of Birth in correct format";
            } else {
                java.util.Date date = new java.util.Date();
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                formatter.setLenient(false);

                String pdate = formatter.format(date);
                java.util.Date date1 = formatter.parse(pdate);
                java.util.Date date2 = formatter.parse(dob);

                int ydob = date1.getYear() - 18;
                int y18 = ydob + 1900;

                //int ydb = date1.getYear() - 66;
                int ydb = date1.getYear() - 67; // line modified by pr on 2ndaug18

                int y60 = ydb + 1900;
                int ydc = date2.getYear();
                int dcl = ydc + 1900;
                int me1 = date1.getMonth();
                int m1 = me1 + 1;
                int me2 = date2.getMonth();
                int m2 = me2 + 1;
                int d1 = date1.getDate();
                int d2 = date2.getDate();

                if (dcl > y18 || dcl < y60) {
                    //msg = "minimum age is 18 years and maximum age is 66 years";

                    msg = "minimum age is 18 years and maximum age is 67 years"; // line modified by pr on 2ndaug18

                } else if (dcl == y60) {
                    if (m2 < m1) {

                        //msg = "minimum age is 18 years and maximum age is 66 years";
                        msg = "minimum age is 18 years and maximum age is 67 years"; // line modified by pr on 2ndaug18

                    } else if (m2 == m1) {
                        if (d2 < d1) {

                            //msg = "minimum age is 18 years and maximum age is 66 years";
                            msg = "minimum age is 18 years and maximum age is 67 years"; // line modified by pr on 2ndaug18

                        } else {
                            msg = "";
                        }
                    } else {

                    }
                } else if (dcl == y18) {
                    if (m2 > m1) {

                        //msg = "minimum age is 18 years and maximum age is 66 years";
                        msg = "minimum age is 18 years and maximum age is 67 years"; // line modified by pr on 2ndaug18

                    } else if (m2 == m1) {
                        if (d2 > d1) {

                            //msg = "minimum age is 18 years and maximum age is 66 years";
                            msg = "minimum age is 18 years and maximum age is 67 years";  // line modified by pr on 2ndaug18

                        } else {
                            msg = "";
                        }
                    } else {
                        msg = "";
                    }
                }
            }
        } else {
            msg = "Please Enter Date of Birth";
        }
        return msg;
    }

    public String dorValidation(String dor, String dob) throws ParseException {
        String msg = "";
        if (dor.isEmpty()) {
            msg = "Please Enter Date of Retirement";
        } else if (!dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
            msg = "Please select Date of Retirement in correct format";
        } else //                    dor = yearrt + "-" + monthrt + "-" + dayrt;
        {
            dor = dor.trim();
            if (dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
                Date d11 = null;
                Date d12 = null;

                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                java.util.Date date = new java.util.Date();
                format.setLenient(false);
                String dateStart = dob;
                String dateEnd = dor;
                int month = 0;
                int dob_month = 0;
                try {
                    Calendar cal = Calendar.getInstance();
                    Date dr = format.parse(dor);
                    cal.setTime(dr);
                    month = cal.get(Calendar.MONTH);
                    //String month = dor.substring(3, 5);
                    //Integer month_new = Integer.parseInt(month);
                    if (!dob.isEmpty()) {
                        Date db = format.parse(dob);
                        cal.setTime(db);
                        dob_month = cal.get(Calendar.MONTH);
                        try {

                            d11 = format.parse(dateStart);
                            d12 = format.parse(dateEnd);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff = d12.getTime() - d11.getTime();
                        long diffSeconds = diff / 1000;
                        long diffMinutes = diff / (60 * 1000);
                        long diffHours = diff / (60 * 60 * 1000);
                        long days = (diffHours / 24);
                        long years = (days / 365);
                        //if ((years) > 65 || ((years) == 65) && (Integer.parseInt(month) > Integer.parseInt(dob_month))) {
                        //if ((years) > 66 || ((years) == 66) && (month > dob_month)) 
                        if ((years) > 67 || ((years) == 67) && (month > dob_month)) // line modified by pr on 2ndaug18
                        {
                            msg = "year of retirement can not exceed 67 years from the DOB year"; // 66 -> 67 by pr on 2ndaug18
                        } else {
                            msg = "";
                        }
                        String pdate = format.format(date);
                        java.util.Date date1 = format.parse(pdate);
                        java.util.Date date2 = format.parse(dor);

                        //int yr60 = date1.getYear() + 48 + 1900; 
                        int yr60 = date1.getYear() + 49 + 1900;  // to add in the current year to make it 67 , modified by pr on 2ndaug18

                        int yc = date2.getYear() + 1900;
                        int yt = date1.getYear() + 1900;
                        int mr1 = date1.getMonth() + 1;
                        int mr = date2.getMonth() + 1;
                        int d1 = date1.getDate();
                        int d2 = date2.getDate();
                        if (yc < yt || yc > yr60) {
                            msg = "Please enter Date Of retirement in correct format";

                        } else if (yc == yt) {
                            if (mr < mr1) {
                                msg = "year of retirement can not exceed 67 years from the DOB year"; // 66 -> 67 done by pr on 2ndaug18
                            } else if (mr == mr1) {
                                if (d2 < d1) {
                                    msg = "year of retirement can not exceed 67 years from the DOB year"; // 66 -> 67 done by pr on 2ndaug18
                                }
                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return msg;

    }

    public String activatedorValidation(String dor) throws ParseException {
        String msg = "";
        if (dor.isEmpty()) {
            msg = "Please Enter Date of Retirement";
        } else if (!dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
            msg = "Please select Date of Retirement in correct format";
        }
        return msg;
    }

    public String gemdorValidation(String dor) throws ParseException {
        String msg = "";
        if (dor.isEmpty()) {
            msg = "Please Enter Date of Retirement";
        } else if (!dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
            msg = "Please select Date of Retirement in correct format";
        } else //                    dor = yearrt + "-" + monthrt + "-" + dayrt;
        {
            dor = dor.trim();
            if (dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
                Date d11 = null;
                Date d12 = null;

                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                java.util.Date date = new java.util.Date();
                String pdate = format.format(date);
                //String dob = "";
                format.setLenient(false);
                String dateStart = pdate;
                String dateEnd = dor;
                int month = 0;
                int dob_month = 0;
                try {
                    Calendar cal = Calendar.getInstance();
                    Date dr = format.parse(dor);
                    cal.setTime(dr);
                    month = cal.get(Calendar.MONTH);
                    String dor_month = dor.substring(3, 5);
                    //Integer month_new = Integer.parseInt(month);
                    if (!pdate.isEmpty()) {
                        //Date db = format.parse(pdate);
                        ///cal.setTime(db);
                        //dob_month = pdate.get(Calendar.MONTH);
                        try {

                            d11 = format.parse(dateStart);
                            d12 = format.parse(dateEnd);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff = d12.getTime() - d11.getTime();
                        long diffSeconds = diff / 1000;
                        long diffMinutes = diff / (60 * 1000);
                        long diffHours = diff / (60 * 60 * 1000);
                        long days = (diffHours / 24);
                        long years = (days / 365);
                        // System.out.println("years:::::::"+years);
                        //if ((years) > 65 || ((years) == 65) && (Integer.parseInt(month) > Integer.parseInt(dob_month))) {
                        if ((years) > 10 || ((years) == 10)) {
                            msg = "year of retirement can not exceed 10 years from the current Date";
                        } else {
                            msg = "";
                        }
                        //String pdate = format.format(date);
                        java.util.Date date1 = format.parse(pdate);
                        java.util.Date date2 = format.parse(dor);

                        //int yr60 = date1.getYear() + 48 + 1900;
                        int yr60 = date1.getYear() + 49 + 1900; // line modified by pr on 2ndaug18 to make dor 67

                        int yc = date2.getYear() + 1900;
                        int yt = date1.getYear() + 1900;
                        int mr1 = date1.getMonth() + 1;
                        int mr = date2.getMonth() + 1;
                        int d1 = date1.getDate();
                        int d2 = date2.getDate();
                        if (yc < yt || yc > yr60) {
                            msg = "Please enter Date Of retirement in correct format";
                        } else if (yc == yt) {
                            if (mr < mr1) {
                                //msg = "year of retirement can not exceed 66 years from the DOB year";
                                msg = "year of retirement can not exceed 67 years from the DOB year"; // line modified by pr on 2ndaug18

                            } else if (mr == mr1) {
                                if (d2 < d1) {
                                    //msg = "year of retirement can not exceed 66 years from the DOB year";
                                    msg = "year of retirement can not exceed 67 years from the DOB year"; // line modified by pr on 2ndaug18
                                }
                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return msg;

    }

    //method added by sahil on 10 august for contractual date validation 
    public String dorValidationCustom(String dor) throws ParseException {
        String msg = "";
        if (dor.isEmpty()) {
            msg = "Please Enter Date of Account Expiry";
        } else if (!dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
            msg = "Please select Date of Account Expiry in correct format";
        } else //                    dor = yearrt + "-" + monthrt + "-" + dayrt;
        {
            dor = dor.trim();
            if (dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
                Date d11 = null;
                Date d12 = null;

                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                java.util.Date date = new java.util.Date();
                String pdate = format.format(date);
                //String dob = "";
                format.setLenient(false);
                String dateStart = pdate;
                String dateEnd = dor;
                int month = 0;
                int dob_month = 0;
                try {
                    Calendar cal = Calendar.getInstance();
                    Date dr = format.parse(dor);
                    cal.setTime(dr);
                    month = cal.get(Calendar.MONTH);
                    String dor_month = dor.substring(3, 5);
                    //Integer month_new = Integer.parseInt(month);
                    if (!pdate.isEmpty()) {
                        //Date db = format.parse(pdate);
                        ///cal.setTime(db);
                        //dob_month = pdate.get(Calendar.MONTH);
                        try {

                            d11 = format.parse(dateStart);
                            d12 = format.parse(dateEnd);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff = d12.getTime() - d11.getTime();
                        long diffSeconds = diff / 1000;
                        long diffMinutes = diff / (60 * 1000);
                        long diffHours = diff / (60 * 60 * 1000);
                        long days = (diffHours / 24);
                        long years = (days / 365);
                        // System.out.println("years:::::::"+years);
                        //if ((years) > 65 || ((years) == 65) && (Integer.parseInt(month) > Integer.parseInt(dob_month))) {
                        if ((years) > 1) {
                            msg = "year of retirement can not exceed 1 years from the current Date";
                        } else {
                            msg = "";
                        }
                        //String pdate = format.format(date);
                        java.util.Date date1 = format.parse(pdate);
                        java.util.Date date2 = format.parse(dor);

                        //int yr60 = date1.getYear() + 48 + 1900;
                        int yr60 = date1.getYear() + 49 + 1900; // line modified by pr on 2ndaug18 to make dor 67

                        int yc = date2.getYear() + 1900;
                        int yt = date1.getYear() + 1900;
                        int mr1 = date1.getMonth() + 1;
                        int mr = date2.getMonth() + 1;
                        int d1 = date1.getDate();
                        int d2 = date2.getDate();
                        if (yc < yt || yc > yr60) {
                            msg = "Please enter Date Of Account Expiry in correct format";
                        } else if (yc == yt) {
                            if (mr < mr1) {
                                //msg = "year of retirement can not exceed 66 years from the DOB year";
                                msg = "year of retirement can not exceed 67 years from the DOB year"; // line modified by pr on 2ndaug18

                            } else if (mr == mr1) {
                                if (d2 < d1) {
                                    //msg = "year of retirement can not exceed 66 years from the DOB year";
                                    msg = "year of retirement can not exceed 67 years from the DOB year"; // line modified by pr on 2ndaug18
                                }
                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return msg;

    }

    // method added by sahil on 10 th august 2021
    public String dorValidationCustom(String dor, String dob) throws ParseException {
        String msg = "";
        if (dor.isEmpty()) {
            msg = "Please Enter Date of Account Expiry";
        } else if (!dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
            msg = "Please select Date of Account Expiry in correct format";
        } else //                    dor = yearrt + "-" + monthrt + "-" + dayrt;
        {
            dor = dor.trim();
            if (dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
                Date d11 = null;
                Date d12 = null;

                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dob);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                // DateFormat format = new SimpleDateFormat("dd-MM-yyyy").parse(dob);
                //  java.util.Date date = new java.util.Date();
                String pdate = format.format(date);
                System.out.println("Pdate:::::::" + pdate);
                //String dob = "";
                format.setLenient(false);
                String dateStart = pdate;
                String dateEnd = dor;
                int month = 0;
                int dob_month = 0;
                try {
                    Calendar cal = Calendar.getInstance();
                    Date dr = format.parse(dor);
                    cal.setTime(dr);
                    month = cal.get(Calendar.MONTH);
                    String dor_month = dor.substring(3, 5);
                    //Integer month_new = Integer.parseInt(month);
                    if (!pdate.isEmpty()) {
                        //Date db = format.parse(pdate);
                        ///cal.setTime(db);
                        //dob_month = pdate.get(Calendar.MONTH);
                        try {

                            d11 = format.parse(dateStart);
                            d12 = format.parse(dateEnd);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff = d12.getTime() - d11.getTime();
                        long years = -1;
                        if (diff < 0) {
                            msg = "Please check date of retirement. It can not be less than Date of birth";
                        } else {
                            long diffSeconds = diff / 1000;
                            long diffMinutes = diff / (60 * 1000);
                            long diffHours = diff / (60 * 60 * 1000);
                            long days = (diffHours / 24);
                            years = (days / 365);
                            // System.out.println("years:::::::"+years);
                            //if ((years) > 65 || ((years) == 65) && (Integer.parseInt(month) > Integer.parseInt(dob_month))) {

                            if ((years) > 60) {
                                msg = "year of retirement can not exceed 60 years from the Date of birth";
                            } else {
                                msg = "";
                            }
                        }
                        if (msg.isEmpty()) {
                            //String pdate = format.format(date);
                            java.util.Date date1 = format.parse(pdate);
                            java.util.Date date2 = format.parse(dor);

                            //int yr60 = date1.getYear() + 48 + 1900;
                            // int yr60 = date1.getYear() + 49 + 1900; // line modified by pr on 2ndaug18 to make dor 67
                            int yc = date2.getYear() + 1900;
                            int yt = date1.getYear() + 1900;
                            int mr1 = date1.getMonth() + 1;
                            int mr = date2.getMonth() + 1;
                            int d1 = date1.getDate();
                            int d2 = date2.getDate();
                            if (years == 60) {
//                                if (mr < mr1) {
//                                    msg = "year of retirement can not exceed 60 years from the Date of birth";
//                                }
                                if (mr > mr1) {
                                    msg = "year of retirement can not exceed 60 years from the Date of birth";
                                }
                                if ((mr - mr1) == 0) {
                                    if (d2 <= 31) {
                                        msg = "";
                                    } else {
                                        msg = "year of retirement can not exceed 60 years from the Date of birth";

                                    }
                                }
                            }

                            if (yc < yt) {
                                msg = "Please enter Date Of Account Expiry in correct format";
                            } else if (yc == yt) {
                                if (mr < mr1) {
                                    //msg = "year of retirement can not exceed 66 years from the DOB year";
                                    msg = "year of retirement can not exceed 67 years from the DOB year"; // line modified by pr on 2ndaug18

                                } else if (mr == mr1) {
                                    if (d2 < d1) {
                                        //msg = "year of retirement can not exceed 66 years from the DOB year";
                                        msg = "year of retirement can not exceed 67 years from the DOB year"; // line modified by pr on 2ndaug18
                                    }
                                }
                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return msg;

    }

    // method added by sahil on 10 th august 2021
    public String dorValidationCustomForDoR(String dor, String prevDor) throws ParseException {
        String msg = "";
        if (dor.isEmpty()) {
            msg = "Please Enter Date of Account Expiry";
        } else if (!dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
            msg = "Please select Date of Account Expiry in correct format";
        } else //                    dor = yearrt + "-" + monthrt + "-" + dayrt;
        {
            dor = dor.trim();
            if (dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
                Date d11 = null;
                Date d12 = null;

                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(prevDor);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                // DateFormat format = new SimpleDateFormat("dd-MM-yyyy").parse(dob);
                //  java.util.Date date = new java.util.Date();
                String pdate = format.format(date);
                System.out.println("Pdate:::::::" + pdate);
                //String dob = "";
                format.setLenient(false);
                String dateStart = pdate;
                String dateEnd = dor;
                int month = 0;
                int dob_month = 0;
                try {
                    Calendar cal = Calendar.getInstance();
                    Date dr = format.parse(dor);
                    cal.setTime(dr);
                    month = cal.get(Calendar.MONTH);
                    String dor_month = dor.substring(3, 5);
                    //Integer month_new = Integer.parseInt(month);
                    if (!pdate.isEmpty()) {
                        //Date db = format.parse(pdate);
                        ///cal.setTime(db);
                        //dob_month = pdate.get(Calendar.MONTH);
                        try {

                            d11 = format.parse(dateStart);
                            d12 = format.parse(dateEnd);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff = d12.getTime() - d11.getTime();
                        long years = -1;
                        if (diff < 0) {
                            msg = "Please check date of retirement. It can not be less than previous account expiry date!!!";
                        } else {
                            long diffSeconds = diff / 1000;
                            long diffMinutes = diff / (60 * 1000);
                            long diffHours = diff / (60 * 60 * 1000);
                            long days = (diffHours / 24);
                            years = (days / 365);
                            // System.out.println("years:::::::"+years);
                            //if ((years) > 65 || ((years) == 65) && (Integer.parseInt(month) > Integer.parseInt(dob_month))) {

                            if ((years) > 1) {
                                //msg = "year of retirement can not exceed 60 years from the Date of birth";
                                msg = "";
                            } else {
                                msg = "";
                            }
                        }
                        if (msg.isEmpty()) {
                            //String pdate = format.format(date);
                            java.util.Date date1 = format.parse(pdate);
                            java.util.Date date2 = format.parse(dor);

                            //int yr60 = date1.getYear() + 48 + 1900;
                            // int yr60 = date1.getYear() + 49 + 1900; // line modified by pr on 2ndaug18 to make dor 67
                            int yc = date2.getYear() + 1900;
                            int yt = date1.getYear() + 1900;
                            int mr1 = date1.getMonth() + 1;
                            int mr = date2.getMonth() + 1;
                            int d1 = date1.getDate();
                            int d2 = date2.getDate();
                            if (years == 60) {
//                                if (mr < mr1) {
//                                    msg = "year of retirement can not exceed 60 years from the Date of birth";
//                                }
                                if (mr > mr1) {
                                    msg = "year of retirement can not exceed 60 years from the Date of birth";
                                }
                                if ((mr - mr1) == 0) {
                                    if (d2 <= 31) {
                                        msg = "";
                                    } else {
                                        msg = "year of retirement can not exceed 60 years from the Date of birth";

                                    }
                                }
                            }

                            if (yc < yt) {
                                msg = "Please enter Date Of Account Expiry in correct format";
                            } else if (yc == yt) {
                                if (mr < mr1) {
                                    //msg = "year of retirement can not exceed 66 years from the DOB year";
                                    msg = "year of retirement can not exceed 67 years from the DOB year"; // line modified by pr on 2ndaug18

                                } else if (mr == mr1) {
                                    if (d2 < d1) {
                                        //msg = "year of retirement can not exceed 66 years from the DOB year";
                                        msg = "year of retirement can not exceed 67 years from the DOB year"; // line modified by pr on 2ndaug18
                                    }
                                }
                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return msg;

    }


//    public String dorValidationCustomRetired(String newDor, String preDor) throws ParseException {
//        String msg = "";
//        if (newDor.isEmpty()) {
//            msg = "Please Enter Date of Account Expiry";
//        } else if (!newDor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
//            msg = "Please select Date of Account Expiry in correct format";
//        } else if (compare2Dates(newDor, preDor) > 0) {
//            msg = "Previous account expiry date can not be more than new account expiry date";
//        } else {
//            newDor = newDor.trim();
//            if (newDor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
//                Date d11 = null;
//                Date d12 = null;
//
//                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(preDor);
//                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//                String pdate = format.format(date);
//                System.out.println("Pdate:::::::" + pdate);
//                format.setLenient(false);
//                String dateStart = pdate;
//                String dateEnd = newDor;
//                try {
//                    Calendar cal = Calendar.getInstance();
//                    Date dr = format.parse(newDor);
//                    cal.setTime(dr);
//                    if (!pdate.isEmpty()) {
//                        try {
//                            d11 = format.parse(dateStart);
//                            d12 = format.parse(dateEnd);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        long diff = d12.getTime() - d11.getTime();
//                        long diffHours = diff / (60 * 60 * 1000);
//                        long days = (diffHours / 24);
//                        long years = (days / 365);
//
//                        if ((years) > 1) {
//                            msg = "Account expiry date cann not exceed 1 year from current date";
//                        } else {
//                            String currentDate = format.format(LocalDateTime.now());
//                            d11 = format.parse(currentDate);
//                            d12 = format.parse(dateStart);
//                            diff = d12.getTime() - d11.getTime();
//                            diffHours = diff / (60 * 60 * 1000);
//                            days = (diffHours / 24);
//                            if(days > 30){
//                                msg = "Please come when only 30 days are left for inactivation of your account.";
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return msg;
//    }
    public String dorValidationCustomRetired(String newDor, String preDor) throws ParseException {
        String msg = "";
        LocalDateTime currentDate1 = LocalDateTime.now();
        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String currentDateInString = currentDate1.format(format2);
//        if (newDor.isEmpty()) {
//            msg = "Please Enter Date of Account Expiry";
//        } else if (!newDor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
//            msg = "Please select Date of Account Expiry in correct format";
//        }
//        else if (compare2Dates(newDor, preDor) < 0) {
//            msg = "Previous account expiry date can not be more than new account expiry date";
//        } 
//        else if (compare2Dates(currentDateInString, preDor) > 0) {
//            msg = "Your account has already been expired. To extend, please contact https://servicedesk.nic.in or call at Toll Free number 1800111555";
//        } else {
        //newDor = newDor.trim();
        //if (newDor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
        //Date d11 = null;
        //Date d12 = null;

        if (compare2Dates(currentDateInString, preDor) > 0) {
            //msg = "Your account has already been expired. To extend, please contact https://servicedesk.nic.in or call at Toll Free number 1800111555";
            return "";
        }

        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(preDor);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String pdate = format.format(date);
        System.out.println("Pdate:::::::" + pdate);
        format.setLenient(false);
        //String dateStart = pdate;
        //String dateEnd = newDor;

        YearMonth currentMonth = YearMonth.now();
        if (!currentMonth.equals(YearMonth.from(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())))) {
            return "Your account is not expiring this month!! Kindly revisit this portal in the month in which your account will be expiring";
        }

//                try {
//                    Calendar cal = Calendar.getInstance();
//                    Date dr = format.parse(newDor);
//                    cal.setTime(dr);
//                    if (!pdate.isEmpty()) {
//                        try {
//                            d11 = format.parse(dateStart);
//                            d12 = format.parse(dateEnd);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        long diff = d12.getTime() - d11.getTime();
//                        double diffHours = diff / (60 * 60 * 1000);
//                        double days = (diffHours / 24);
//                        double years = (days / 365);
//
//                        if ((years) > 1) {
////                            msg = "Account expiry date cann not exceed 1 year from current date";
//                        } else {
//                            //String currentDate = format.format(LocalDateTime.now());
//
//                            LocalDateTime now = LocalDateTime.now();
//                            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//                            String currentDate = now.format(format1);
//
//                            d11 = format.parse(currentDate);
//                            d12 = format.parse(dateStart);
//                            diff = d12.getTime() - d11.getTime();
//                            diffHours = diff / (60 * 60 * 1000);
//                            days = (diffHours / 24);
//                            if (days > 30) {
//                                msg = "Please come when only 30 days are left for inactivation of your account.";
//                            }
//                        }
//
//                        String str_newDor[] = newDor.split("-");
//                        String str_preDor[] = preDor.split("-");
//                        String newDor_month = str_newDor[1];
//                        String preDor_month = str_preDor[1];
//
//                        int newDor_month1 = Integer.parseInt(newDor_month);
//                        int preDor_month1 = Integer.parseInt(preDor_month);
//
//                        if (preDor_month1 > newDor_month1) {
//                           // if(preDor_month1 != 12 && newDor_month1 ! =  )
//                            msg = "You can activate your account in next month.";
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
        //}
        //}
        return msg;
    }
    
     public String dorValidationCustomRetiring(String preDor) throws ParseException {
        String msg = "";
        LocalDateTime currentDate1 = LocalDateTime.now();
        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String currentDateInString = currentDate1.format(format2);

        if (compare2Dates(currentDateInString, preDor) > 0) {
            return "Your account has already been expired. To extend, please contact https://servicedesk.nic.in or call at Toll Free number 1800111555";
        }

        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(preDor);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String pdate = format.format(date);
        System.out.println("Pdate:::::::" + pdate);
        format.setLenient(false);
        
        YearMonth currentMonth = YearMonth.now();
        if (!currentMonth.equals(YearMonth.from(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())))) {
            return "Your account is not expiring this month!! Kindly revisit this portal in the month in which your account will be expiring";
        }
        return msg;
    }

    public String dorValidationCustomServing(String newDor, String preDor) throws ParseException {
        String msg = "";
        LocalDateTime currentDate1 = LocalDateTime.now();
        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String currentDateInString = currentDate1.format(format2);

        if (compare2Dates(currentDateInString, preDor) > 0) {
            return "Your account has already been expired. To extend, please contact https://servicedesk.nic.in or call at Toll Free number 1800111555";
        }

        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(preDor);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String pdate = format.format(date);
        System.out.println("Pdate:::::::" + pdate);
        format.setLenient(false);

        YearMonth currentMonth = YearMonth.now();
        if (!currentMonth.equals(YearMonth.from(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())))) {
            return "Your account is not expiring this month!! Kindly revisit this portal in the month in which your account will be expiring";
        }

        return msg;
    }

    public int compare2Dates(String d1, String d2) throws ParseException {
        Date newDor = new SimpleDateFormat("dd-MM-yyyy").parse(d1);
        Date preDor = new SimpleDateFormat("dd-MM-yyyy").parse(d2);
        System.out.println("The date 1 is: " + d1);
        System.out.println("The date 2 is: " + d2);
        return newDor.compareTo(preDor);
        // return preDor.compareTo(newDor);
    }

    public boolean controllingministryValidation(String fname) {
        boolean flag = false;
        if (fname.isEmpty()) {
            flag = true;
        } else if (!fname.matches("^[a-zA-Z ,&.]{3,100}$")) {
            flag = true;
        }
        return flag;

    }

    public boolean roleValidation(String fname) {
        boolean flag = false;
        if (fname.isEmpty()) {
            flag = true;
        } else if (!fname.matches("^[a-zA-Z_]{1,100}$")) {
            flag = true;
        }
        return flag;

    }

    public boolean checkradioValidation(String list_mod) {
        boolean flag = false;
        String msg = "";
        if ((!list_mod.equals("")) && (list_mod.equalsIgnoreCase("yes") || list_mod.equalsIgnoreCase("no"))) {
            flag = false;
        } else {
            flag = true;

        }
        return flag;
    }

    public String listtempValidation(String list_temp, String valid_date) {
        String msg = "";
        if ((!list_temp.equals("")) && (list_temp.equals("yes") || list_temp.equals("no"))) {
            if (list_temp.equals("yes")) {
                if (valid_date == null || "".equals(valid_date)) {
                    valid_date = "";
                    //  valid_date = ESAPI.encoder().encodeForHTMLAttribute(valid_date);
                    msg = "Please enter List vailidty Date";
                } else if (!valid_date.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
                    //  valid_date = ESAPI.encoder().encodeForHTMLAttribute(valid_date);
                    msg = "Please enter List vailidty Date in correct format";
                } else {
                    try {
                        java.util.Date date = new java.util.Date();
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        formatter.setLenient(false);
                        String pdate = formatter.format(date);
                        java.util.Date date1 = formatter.parse(pdate);
                        java.util.Date date2 = formatter.parse(valid_date);

                        int yr60 = date1.getYear() + 42 + 1900;
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "y60 ret : " + yr60);
                        int yc = date2.getYear() + 1900;
                        int yt = date1.getYear() + 1900;
                        int mr1 = date1.getMonth() + 1;
                        int mr = date2.getMonth() + 1;
                        int d1 = date1.getDate();
                        int d2 = date2.getDate();
                        if (!valid_date.matches("[0-9]{2}[-][0-9]{2}[-]([0-9]{4})")) {
                            //  valid_date = ESAPI.encoder().encodeForHTMLAttribute(valid_date);
                            msg = "Please Select List Validity date in correct format";

                        } else if (yc < yt) {

                            // valid_date = ESAPI.encoder().encodeForHTMLAttribute(valid_date);
                            msg = "List Validity Date must be equal or greater then present date";

                        } else if (yc == yt) {
                            if (mr < mr1) {
                                //  valid_date = ESAPI.encoder().encodeForHTMLAttribute(valid_date);
                                msg = "List Validity Date must be equal or greater then present date";

                            } else if (mr == mr1) {
                                if (d2 >= d1) {
                                } else {
                                    //  valid_date = ESAPI.encoder().encodeForHTMLAttribute(valid_date);
                                    msg = "List Validity Date must be equal or greater then present date";

                                }
                            }

                        }
                    } catch (Exception e) {
                        msg = "Please Select List Validity date in correct format";

                        e.printStackTrace();
                    }

                }
            }

        } else {
            list_temp = "";
            msg = "Please tick, Is list temporary";

        }
        return msg;
    }

    public String listValidation(String list1) {
        String msg = "";
        if (list1.contains("@") && list1.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
            String[] t = list1.split("@");
            String list = t[0];
            String listend = t[1];
            if (list.isEmpty()) {
                msg = "Please Enter List Name";
            } else if (list.length() < 6 || list.length() > 20) {
                msg = "List Name contain min length is 6, max length is 20";
            } else if (list.contains("_")) {
                msg = "List Name can't contain underscore(_)";
            } else if (list.startsWith("-") || list.startsWith(".") || list.endsWith("-") || list.endsWith(".")) {
                msg = "List Name can't start or end with hyphen(-) or dot[.]";
            } else {
                boolean uflag = false;
                for (int l = 0; l < list.length(); l++) {
                    char a = list.charAt(l);
                    if (a == '.') {
                        char b = list.charAt(l + 1);
                        char c = '-';
                        if (a == b || b == c) {
                            uflag = true;
                        }
                    }
                    if (a == '-') {
                        char b = list.charAt(l + 1);
                        char c = '.';
                        if (a == b || b == c) {
                            uflag = true;
                        }
                    }
                }
                if (uflag) {
                    msg = "Please enter List Name in correct format";
                } else if (!list.matches("^[a-zA-Z0-9.-]{6,20}$")) {
                    msg = "Please enter List Name in correct format";
                }
                if (!listend.equalsIgnoreCase("lsmgr.nic.in")) {
                    msg = "Please append @lsmgr.nic.in at the end";
                }

                if (list.contains("-") || list.contains(".")) {
                } else {
                    msg = "List Name should contain (hyphen(-) or dot[.]";
                }
            }
        } else {
            msg = "Please append @lsmgr.nic.in at the end";
        }
        return msg;
    }

    public boolean dnsipvalidation(String dnsip) {
        boolean flag = false;
        if (dnsip.isEmpty()) {
            flag = true;
        } else if (dnsip.matches("((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))")) {
            //if (dnsip.startsWith("0") || dnsip.equals("0.0.0.0") || dnsip.equals("127.0.0.1") || dnsip.equals("255.255.255.255") || dnsip.endsWith("255")) {
            if (dnsip.startsWith("0") || dnsip.equals("0.0.0.0") || dnsip.equals("127.0.0.1") || dnsip.equals("255.255.255.255")) {
                flag = true;
            }
        } else {
            flag = true;
        }
        return flag;
    }

    public boolean dnsmodifyipvalidation(String dnsip) {
        boolean flag = false;
        if (!dnsip.isEmpty()) {
            if (dnsip.matches("((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))")) {
                //if (dnsip.startsWith("0") || dnsip.equals("0.0.0.0") || dnsip.equals("127.0.0.1") || dnsip.equals("255.255.255.255") || dnsip.endsWith("255")) {
                if (dnsip.startsWith("0") || dnsip.equals("0.0.0.0") || dnsip.equals("127.0.0.1") || dnsip.equals("255.255.255.255")) {
                    flag = true;
                }
            } else {
                flag = true;
            }
        }
        return flag;
    }

    public boolean dnsurlvalidation(String dnsurl) {
        boolean flag = false;
        if (dnsurl.isEmpty()) {
            flag = true;
        } else if (!dnsurl.isEmpty()) {
            if (!dnsurl.matches("^(?!:\\/\\/)([a-zA-Z0-9-\\_]+\\.){0,5}[a-zA-Z0-9-\\_][a-zA-Z0-9-\\_]+\\.[a-zA-Z]{2,64}?$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean cnamevalidation(String cname) {
        boolean flag = false;
        if (!cname.isEmpty()) {
            if (!cname.matches("^(?!:\\/\\/)([a-zA-Z0-9-\\_]+\\.){0,5}[a-zA-Z0-9-\\_][a-zA-Z0-9-\\_]+\\.[a-zA-Z]{2,64}?$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean cnamevalidation(String cname, String req) {
        boolean flag = false;
        if (req.equals("req_cname")) {
            if (cname.isEmpty()) {
                flag = true;
            } else if (!cname.isEmpty()) {
                if (!cname.matches("^(?!:\\/\\/)([a-zA-Z0-9-\\_]+\\.){0,5}[a-zA-Z0-9-\\_][a-zA-Z0-9-\\_]+\\.[a-zA-Z]{2,64}?$")) {
                    flag = true;
                }
            }

        } else {
            if (!cname.isEmpty()) {
                if (!cname.matches("^(?!:\\/\\/)([a-zA-Z0-9-\\_]+\\.){0,5}[a-zA-Z0-9-\\_][a-zA-Z0-9-\\_]+\\.[a-zA-Z]{2,64}?$")) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    public boolean accnameValidation(String accname) {
        boolean flag = false;
        if (accname.isEmpty()) {
            flag = true;
        } else if (!accname.isEmpty()) {
            if (!accname.matches("^[a-zA-Z0-9\\.\\-\\_]{5,25}$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean institueIdValidation(String insid) {
        boolean flag = false;
        if (insid.isEmpty()) {
        } else if (!insid.matches("^[a-zA-Z0-9 ,.-]{1,50}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean portValidation(String port) {
        boolean flag = false;
        if (port.isEmpty()) {
            flag = true;
        } else if (!port.matches("^[0-9]{1,10}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean dnsServiceValidation(String add1) {
        boolean flag = false;
        if (add1.isEmpty()) {
            flag = true;
        } else if (!add1.matches("^[a-zA-Z#0-9\\s,.\\_\\-\\/\\(\\)]{2,100}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean dnsCnameValidation(String add1) {
        boolean flag = false;
        if (add1.isEmpty()) {
            flag = true;
        } else if (!add1.matches("^[a-zA-Z#0-9\\,.\\_\\-\\/\\(\\)]{2,100}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean dnstxtValidation(String add1) {
        boolean flag = false;
        if (add1.isEmpty()) {
            flag = true;

        } else if (!add1.matches("^[^<>&%]{2,300}+$")) {
            flag = true;
        }
        return flag;
    }

    public boolean osValidation(String os) {
        boolean flag = false;
        boolean numeric = os.matches("-?\\d+(\\.\\d+)?");
        if (os.isEmpty()) {

            flag = true;
        } else {
            if (numeric) {
                flag = true;
            } else {

                if (!os.matches("^[a-zA-Z0-9 .,-_&]{2,50}$")) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    public boolean addstateValidation(String user_state) {
        boolean flag = false;
        if (user_state == null || user_state.isEmpty()) {
            flag = true;

        } else if (!user_state.matches("^[a-zA-Z0-9\\.\\-\\_ ]{1,100}$")) {
            flag = true;
        }
        return flag;
    }

    ///////// NIKKI //////////
    public boolean hinditextValidation(String text) {
        boolean flag = false;
        if (text.isEmpty()) {
            flag = true;

        } else if (!text.matches("[\\pL\\p{Ll}\\p{Lu}\\p{Lt}\\p{Lm}\\p{Lo}\\p{Mn}\\p{Mc}\\p{Me}\\p{Z}\\p{Zs}\\p{Zl}\\p{Zp}\\p{S}\\p{Sm}\\p{Sc}\\p{Sk}\\p{So}\\p{Nd}\\p{Nl}\\p{P}\\p{Pd}\\p{Ps}\\p{Pe}\\p{Pi}\\p{Pf}\\p{Pc}\\p{Po}\\p{C}\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}\\p{Cs}\\p{No} \\pM\\p{javaWhitespace}\\p{No}\\p{N}]+")) {
            flag = true;
        }

        return flag;
    }

    public boolean eventTypeValidation(String type) {
        boolean flag = false;
        if (type.isEmpty()) {
            flag = true;
        } else if (!type.matches("^[a-zA-Z ]{1,50}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean remarksValidation(String type) {
        boolean flag = false;
        if (type == null || type.isEmpty()) {
            flag = false;
        } else if (!type.matches("^[a-zA-Z0-9\\.\\-\\&\\(\\) ]{1,100}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean chequeValidation(String pin) {
        boolean flag = false;
        if (pin == null || pin.isEmpty()) {
            flag = false;
        } else if (!pin.matches("^[a-zA-Z0-9]{6,30}$")) {
            flag = true;
        }
        return flag;
    }

    public String chequeDateValidation(String cheque_date) throws ParseException {
        String msg = "";
        if (!cheque_date.isEmpty()) {

            if (!cheque_date.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
                msg = "Please select Date in correct format";
            } else {
                try {
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    formatter.setLenient(false);
                    Date cheque_date_parsed = formatter.parse(cheque_date);

                    Calendar cheque = Calendar.getInstance();
                    Calendar future = Calendar.getInstance();
                    Calendar past = Calendar.getInstance();

                    cheque.setTime(cheque_date_parsed);
                    future.add(Calendar.MONTH, 3);
                    past.add(Calendar.MONTH, -3);

                    if (cheque.after(future)) {
                        msg = "Date should be within 3 months.";
                    } else if (cheque.before(past)) {
                        msg = "Only today or with in next 30 day's dates are allowed.";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            //msg = "Please Enter Cheque Date ";

        }
        return msg;
    }

    public String dnsDateValidation(String cheque_date) {
        String msg = "";
        if (!cheque_date.isEmpty()) {
            if (!cheque_date.matches("([0-9]{2})[/][0-9]{2}[/][0-9]{4}")) {
                msg = "Please select Date in correct format i.e. dd/MM/yyyy";
            } else {
                try {
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    formatter.setLenient(false);
                    Date cheque_date_parsed = formatter.parse(cheque_date);

                    Calendar cheque = Calendar.getInstance();
                    Calendar future = Calendar.getInstance();
                    Calendar current = Calendar.getInstance();

                    cheque.setTime(cheque_date_parsed);
                    future.add(Calendar.MONTH, 1);
                    //past.add(Calendar.MONTH, -3);

                    if (cheque.after(future)) {
                        msg = "Migration date must be within 1 month.";
                    } else if (cheque.before(current)) {
                        msg = "You can not enter past date.";
                    }
                } catch (Exception e) {
                    msg = "Please enter the date in correct format i.e. dd/MM/yyyy";
                    //e.printStackTrace();
                }
            }
        } else {
            //msg = "Please Enter Cheque Date ";

        }
        return msg;
    }

    public boolean confSessionValidation(String no) {
        boolean flag = false;
        if (no.isEmpty()) {
            flag = true;
        } else if (!no.matches("^[0-9]{1,2}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean institueIdValidationnotnull(String insid) {
        boolean flag = false;
        if (insid.isEmpty()) {
            flag = true;
        } else if (!insid.matches("^[a-zA-Z0-9 ,.-]{1,50}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean vpnurlValidation(String port) {

        boolean flag = false;
        if (!port.isEmpty()) {
            if (port.matches("^(?:(?:(?:https?|ftp):)?\\/\\/)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})).?)(?::\\d{2,5})?(?:[/?#]\\S*)?$")) {
                flag = false;
            } else if (port.matches("^((http:\\/\\/|https:\\/\\/)([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\/[a-z]{1,})*$")) {
                String ip = port.substring(port.indexOf("//") + 2, port.length());
                //if (ip.startsWith("0") || ip.equals("0.0.0.0") || ip.equals("127.0.0.1") || ip.equals("255.255.255.255") || ip.endsWith("255")) {
                if (ip.startsWith("0") || ip.equals("0.0.0.0") || ip.equals("127.0.0.1") || ip.equals("255.255.255.255")) {
                    flag = true;
                } else {
                    flag = false;
                }
            } else {
                flag = true;
            }
        }
        return flag;
    }

    public boolean firewallipvalidation(String dnsip) {
        boolean flag = false;
        if (dnsip.isEmpty()) {
            flag = true;
        } else if (dnsip.matches("((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))")) {
        } else {
            String[] parts;
            String part1 = "";
            String part2 = "";
            if (dnsip.contains("-")) {
                parts = dnsip.split("-");
                part1 = parts[0]; // 004
                part2 = parts[1];
            }
            if (dnsip.contains("/")) {
                parts = dnsip.split("/");
                part1 = parts[0]; // 004
                part2 = parts[1];
            }

            if (!part2.equals("")) {

                if (part1.matches("((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))") && (part2.matches("((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))") && (dnsip.contains("-")))) {

                    //if (part1.startsWith("0") || part1.equals("0.0.0.0") || part1.equals("127.0.0.1") || part1.equals("255.255.255.255") || part1.endsWith("255")) {
                    if (part1.startsWith("0") || part1.equals("0.0.0.0") || part1.equals("127.0.0.1") || part1.equals("255.255.255.255")) {
                        flag = true;
                    }
                    //if (part2.startsWith("0") || part2.equals("0.0.0.0") || part2.equals("127.0.0.1") || part2.equals("255.255.255.255") || part2.endsWith("255")) {
                    if (part2.startsWith("0") || part2.equals("0.0.0.0") || part2.equals("127.0.0.1") || part2.equals("255.255.255.255")) {
                        flag = true;
                    }
                } else if (part1.matches("((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))") && (part2.matches("^[0-9]{1,10}$") && (!dnsip.contains("/")))) {
                    flag = false;

                } else if (part1.matches("((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))") && Integer.parseInt(part2) <= 32) {
                    flag = false;

                } else {
                    flag = true;
                }
            } else if (part1.equals("any") || part1.equals("all")) {
            } else {
                flag = true;
            }

            if (part2.equals("")) {
                if (part1.matches("((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))")) {
                    //if (part1.startsWith("0") || part1.equals("0.0.0.0") || part1.equals("127.0.0.1") || part1.equals("255.255.255.255") || part1.endsWith("255")) {
                    if (part1.startsWith("0") || part1.equals("0.0.0.0") || part1.equals("127.0.0.1") || part1.equals("255.255.255.255")) {
                        flag = true;
                    }
                } else if (dnsip.equals("any") || dnsip.equals("all")) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
        }
        return flag;
    }

    public boolean firewallportValidation(String port) {
        boolean flag = false;
        if (port.isEmpty()) {
            flag = true;
        } else if (port.equals("all") || port.equals("any")) {

        } else {
            String[] parts;
            String part1 = "";
            String part2 = "";
            if (port.contains("-")) {
                parts = port.split("-");
                part1 = parts[0]; // 004
                part2 = parts[1];
                if (!part1.matches("^[0-9]{1,10}$") && !part2.matches("^[0-9]{1,10}$")) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }

    public boolean numericdayValidation_firewall(String duration, String dur) {
        boolean flag = false;
//        if (duration.isEmpty()) {
//            flag = true;
//        } else 
        if (!duration.isEmpty()) {
            if (duration.matches("^[0-9]{0,3}$")) {
                int days = Integer.parseInt(duration);
                if (days <= 730 && days >= 1) {
                } else {
                    flag = true;
                }
            } else if (duration.equalsIgnoreCase("Default")) {

            } else {
                flag = true;
            }
        }
        return flag;
    }

    public String event_startValidation(String event_start) {
        String msg = "NA";
        if (!event_start.isEmpty()) {
            try {
                String event_start_date = event_start.substring(0, event_start.indexOf(" "));
                String event_start_time = event_start.substring(event_start.indexOf(" ") + 1, event_start.length());
                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                formatter.setLenient(false);
                Date cheque_date_parsed = formatter.parse(event_start_date.trim());
                Calendar today = Calendar.getInstance();
                today.roll(Calendar.DATE, -1);
                Calendar cheque = Calendar.getInstance();
                cheque.setTime(cheque_date_parsed);

                if (cheque.compareTo(today) > 0) {
                    System.out.println("Date 1 occurs after Date 2");
                } else if (cheque.compareTo(today) < 0) {
                    msg = "Please select event start datetime in correct format";
                    System.out.println("Date 1 occurs before Date 2");
                } else if (!event_start_time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
                    msg = "Please select event start datetime in correct format";
                }
                System.out.println("inside try msg@@@@@@@@@@@@@@" + msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            msg = "Please select event start datetime in correct format";
        }

        System.out.println("msg@@@@@@@@@@@@@@" + msg);
        return msg;
    }

    public String event_endValidation(String event_start, String event_end) {
        String msg = "NA";
        if (!event_end.isEmpty()) {
            try {

                String event_start_date = event_start.substring(0, event_start.indexOf(" "));
                String event_end_date = event_end.substring(0, event_end.indexOf(" "));
                String event_end_time = event_end.substring(event_end.indexOf(" ") + 1, event_end.length());
                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                formatter.setLenient(false);
                Date end_parsed = formatter.parse(event_end_date);
                Date start_parsed = formatter.parse(event_start_date.trim());

                Calendar today = Calendar.getInstance();
                today.roll(Calendar.DATE, -1);
                Calendar cheque = Calendar.getInstance();
                Calendar start = Calendar.getInstance();
                start.roll(Calendar.DATE, -1);

                start.setTime(start_parsed);
                cheque.setTime(end_parsed);
                System.out.println("cheque.compareTo(start)" + cheque.compareTo(start));
                if (cheque.compareTo(start) < 0) {
                    msg = "Please select event end datetime in correct format";
                } else {
                    if (cheque.compareTo(today) > 0) {
                        System.out.println("Date 1 occurs after Date 2");
                    } else if (cheque.compareTo(today) < 0) {
                        msg = "Please select event end datetime in correct format";
                        System.out.println("Date 1 occurs before Date 2");
                    } else if (!event_end_time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
                        msg = "Please select event end datetime in correct format";
                    }
                }
//               

            } catch (Exception e) {
                e.printStackTrace();
                msg = "Please select event end datetime in correct format";
            }

        } else {
            msg = "Please select event end datetime in correct format";
        }
        return msg;
    }

    public boolean confscheduleValidation(String add1) {
        boolean flag = false;
        if (add1.isEmpty()) {
            flag = true;
        } else if (!add1.matches("^[a-zA-Z#0-9\\s,.\\-\\/\\(\\)]{1,100}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean webcastamountvalidation(String amount) {
        boolean flag = false;
        if (!amount.isEmpty()) {

            if (amount.matches("^[0-9]{1,9}$")) {
                int amount1 = Integer.parseInt(amount);
                if (amount1 > 0) {

                } else {
                    flag = true;
                }
            } else {
                flag = true;
            }

        }
        return flag;
    }

    public boolean vcidvalidation(String vc_id) {
        boolean flag = false;
        if (vc_id.isEmpty()) {
            flag = true;
        } else if (!vc_id.isEmpty()) {
            if (!vc_id.matches("^[a-zA-Z0-9]{1,6}$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean relayUrlValidation(String port) {

        boolean flag = false;
        if (!port.isEmpty()) {
            if (port.matches("^(?:(?:(?:https?|ftp):)?\\/\\/)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})).?)(?::\\d{2,5})?(?:[/?#]\\S*)?$")) {
                flag = false;
            } else if (port.matches("^((http:\\/\\/|https:\\/\\/)([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\/[a-z]{1,})*$")) {
                String ip = port.substring(port.indexOf("//") + 2, port.length());
                //if (ip.startsWith("0") || ip.equals("0.0.0.0") || ip.equals("127.0.0.1") || ip.equals("255.255.255.255") || ip.endsWith("255")) {
                if (ip.startsWith("0") || ip.equals("0.0.0.0") || ip.equals("127.0.0.1") || ip.equals("255.255.255.255")) {
                    flag = true;
                } else {
                    flag = false;
                }
            } else {
                flag = true;
            }
        }
        return flag;
    }

    //for mobile
    public String dorValidationMobile(String dor, String dob) throws ParseException {
        String msg = "";
        if (dor.isEmpty()) {
            msg = "Please Enter Date of Account Expiry";
        } else if (!dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
            msg = "Please select Date of Account Expiry in correct format";
        } else //                    dor = yearrt + "-" + monthrt + "-" + dayrt;
        {
            dor = dor.trim();
            if (dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
                Date d11 = null;
                Date d12 = null;

                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dob);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                // DateFormat format = new SimpleDateFormat("dd-MM-yyyy").parse(dob);
                //  java.util.Date date = new java.util.Date();
                String pdate = format.format(date);
                System.out.println("Pdate:::::::" + pdate);
                //String dob = "";
                format.setLenient(false);
                String dateStart = pdate;
                String dateEnd = dor;
                int month = 0;
                int dob_month = 0;
                try {
                    Calendar cal = Calendar.getInstance();
                    Date dr = format.parse(dor);
                    cal.setTime(dr);
                    month = cal.get(Calendar.MONTH);
                    String dor_month = dor.substring(3, 5);
                    //Integer month_new = Integer.parseInt(month);
                    if (!pdate.isEmpty()) {
                        //Date db = format.parse(pdate);
                        ///cal.setTime(db);
                        //dob_month = pdate.get(Calendar.MONTH);
                        try {

                            d11 = format.parse(dateStart);
                            d12 = format.parse(dateEnd);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff = d12.getTime() - d11.getTime();
                        long diffSeconds = diff / 1000;
                        long diffMinutes = diff / (60 * 1000);
                        long diffHours = diff / (60 * 60 * 1000);
                        long days = (diffHours / 24);
                        long years = (days / 365);
                        // System.out.println("years:::::::"+years);
                        //if ((years) > 65 || ((years) == 65) && (Integer.parseInt(month) > Integer.parseInt(dob_month))) {

                        if ((years) > 60) {
                            msg = "year of retirement can not exceed 60 years from the Date of birth";
                        } else {
                            msg = "";
                        }
                        if (msg.isEmpty()) {
                            //String pdate = format.format(date);
                            java.util.Date date1 = format.parse(pdate);
                            java.util.Date date2 = format.parse(dor);

                            //int yr60 = date1.getYear() + 48 + 1900;
                            // int yr60 = date1.getYear() + 49 + 1900; // line modified by pr on 2ndaug18 to make dor 67
                            int yc = date2.getYear() + 1900;
                            int yt = date1.getYear() + 1900;
                            int mr1 = date1.getMonth() + 1;
                            int mr = date2.getMonth() + 1;
                            int d1 = date1.getDate();
                            int d2 = date2.getDate();
                            if (years == 60) {
//                                if (mr < mr1) {
//                                    msg = "year of retirement can not exceed 60 years from the Date of birth";
//                                }
                                if (mr > mr1) {
                                    msg = "year of retirement can not exceed 60 years from the Date of birth";
                                }
                                if ((mr - mr1) == 0) {
                                    if (d2 <= 31) {
                                        msg = "";
                                    } else {
                                        msg = "year of retirement can not exceed 60 years from the Date of birth";

                                    }
                                }
                            }

                            if (yc < yt) {
                                msg = "Please enter Date Of Account Expiry in correct format";
                            } else if (yc == yt) {
                                if (mr < mr1) {
                                    //msg = "year of retirement can not exceed 66 years from the DOB year";
                                    msg = "year of retirement can not exceed 67 years from the DOB year"; // line modified by pr on 2ndaug18

                                } else if (mr == mr1) {
                                    if (d2 < d1) {
                                        //msg = "year of retirement can not exceed 66 years from the DOB year";
                                        msg = "year of retirement can not exceed 67 years from the DOB year"; // line modified by pr on 2ndaug18
                                    }
                                }
                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return msg;

    }

    public boolean macvalidationRandomized(String mac_address) {
        boolean flag = false;

        if (mac_address.matches("^(([0-9A-Fa-f]{2}[:]){5}([0-9A-Fa-f]{2}))|(([0-9A-Fa-f]{2}[\\-]){5}([0-9A-Fa-f]{2}))$")) {
            if (mac_address.substring(1, 2).equals("2") || mac_address.substring(1, 2).equals("6") || mac_address.substring(1, 2).equalsIgnoreCase("a") || mac_address.substring(1, 2).equalsIgnoreCase("e")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean RolesValidation(String role) {

        ArrayList list = new ArrayList();
        list.add("coordinator_pending");
        list.add("mail-admin_pending");
        list.add("da_pending");

        boolean flag = false;
        if (!list.contains(role)) {
            flag = true;
        }
        return flag;
    }

}
