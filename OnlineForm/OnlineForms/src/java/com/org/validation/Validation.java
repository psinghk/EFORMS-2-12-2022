package com.org.validation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class Validation {

    public boolean checkFormat(String type, String value) {
        String typeOfData = type.toLowerCase();
        boolean flag = false;
        switch (typeOfData) {
            case "email":
                flag = value.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$");
                break;
            case "mobile":
                flag = value.matches("^[+0-9]{10,13}$");
                break;
            case "title":
                flag = value.matches("^[a-zA-Z.]{2,6}$");
                break;
            case "name":
                flag = value.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-\\(\\)]+.[a-zA-z0-9]{2,4}$");
                break;
            case "telephone":
                flag = value.matches("^[+0-9]{10,13}$");
                break;
            case "employeecode":
                flag = value.matches("^[+0-9]{10,17}$");
                break;
            case "address":
                flag = value.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$");
                break;
            case "district":
                flag = value.matches("^[+0-9]{10,13}$");
                break;
            case "pincode":
                flag = value.matches("^[+0-9]{10,13}$");
                break;
            case "ministry":
                flag = value.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$");
                break;
            case "department":
                flag = value.matches("^[+0-9]{10,13}$");
                break;
            case "employment":
                flag = value.matches("^[+0-9]{10,13}$");
                break;
            case "purpose":
                flag = value.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$");
                break;
            case "url":
                flag = value.matches("^[+0-9]{10,13}$");
                break;
            case "ip":
                flag = value.matches("^[+0-9]{10,13}$");
                break;
            case "mac":
                flag = value.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$");
                break;
            case "pullkeyword":
                flag = value.matches("^[+0-9]{10,13}$");
                break;
            case "cname":
                flag = value.matches("^[+0-9]{10,13}$");
                break;
            default:
                System.out.println("Invalid type");
        }
        return flag;
    }

    public boolean isEmailFormatCorrect(String email) {
        if (email.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMobileFormatCorrect(String mobile) {
        if (mobile.matches("^[+0-9]{10,13}$")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean UpdateMobileValidation(String mobile, String code) {
        System.out.println("code::::::::" + code);
        boolean flag = false;
        code = code.trim();
        if (code.equals("+91")) {
            System.out.println("inisde 91 code");
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
        } else if (!fname.matches("^[a-zA-Z .,]{1,50}$")) {
            flag = true;
        }
        return flag;

    }

    public boolean telValidation(String tel1) {
        boolean flag = false;
        if (tel1.isEmpty()) {
            flag = true;
        } else if (!tel1.matches("^[0-9]{3,5}[-]([0-9]{6,15})$")) {
            flag = true;
        }
        return flag;

    }

    public boolean tel1Validation(String tel1) {
        boolean flag = false;
        if (!tel1.isEmpty()) {
            if (!tel1.matches("^[0-9]{3,5}[-]([0-9]{6,15})$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean employcodevalidation(String employcode) {
        boolean flag = false;
        if (!employcode.isEmpty()) {
            if (!employcode.matches("^[a-zA-Z0-9]{2,12}$")) {
                flag = true;
            }
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

    public boolean cityValidation(String city) {
        boolean flag = false;
        if (city.isEmpty()) {
            flag = true;
        } else if (!city.matches("^[a-zA-Z ]{1,50}$")) {
            flag = true;
        }
        return flag;
    }

    public boolean pinValidation(String pin) {
        boolean flag = false;
        if (pin.isEmpty()) {
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

                if (!desig.matches("^[a-zA-Z0-9 .,-_&]{2,50}$")) {
                    flag = true;
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
                int ydb = date1.getYear() - 66;
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
                    msg = "minimum age is 18 years and maximum age is 66 years";
                } else if (dcl == y60) {
                    if (m2 < m1) {
                        msg = "minimum age is 18 years and maximum age is 66 years";
                    } else if (m2 == m1) {
                        if (d2 < d1) {
                            msg = "minimum age is 18 years and maximum age is 66 years";
                        } else {
                            msg = "";
                        }
                    } else {

                    }
                } else if (dcl == y18) {
                    if (m2 > m1) {
                        msg = "minimum age is 18 years and maximum age is 66 years";
                    } else if (m2 == m1) {
                        if (d2 > d1) {
                            msg = "minimum age is 18 years and maximum age is 66 years";
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

//    public String dorValidation(String dor, String dob) throws ParseException {
//
//        String msg = "";
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "dor is:::" + dor);
//        if (dor.isEmpty()) {
//            msg = "Please Enter Date of Retirement";
//        } else if (!dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
//            msg = "Please select Date of Retirement in correct format";
//        } else //                    dor = yearrt + "-" + monthrt + "-" + dayrt;
//         if (dor.matches("([0-9]{2})[-][0-9]{2}[-][0-9]{4}")) {
//
//                Date d11 = null;
//
//                Date d12 = null;
//
//                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//                java.util.Date date = new java.util.Date();
//                format.setLenient(false);
//
//                String dateStart = dob;
//
//                String dateEnd = dor;
//
//                String month = dor.substring(3, 5);
//
//                //Integer month_new = Integer.parseInt(month);
//                String dob_month = dob.substring(3, 5);
//
//                //d2 = new Date();
//                try {
//                    d11 = format.parse(dateStart);
//                    d12 = format.parse(dateEnd);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                long diff = d12.getTime() - d11.getTime();
//                long diffSeconds = diff / 1000;
//                long diffMinutes = diff / (60 * 1000);
//                long diffHours = diff / (60 * 60 * 1000);
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Time in seconds: " + diffSeconds + " seconds.");
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Time in minutes: " + diffMinutes + " minutes.");
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Time in hours: " + diffHours + " hours.");
//
//                long days = (diffHours / 24);
//
//                long years = (days / 365);
//
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " days are " + days + " years are " + years);
//
//                if ((years) > 65 || ((years) == 65) && (Integer.parseInt(month) > Integer.parseInt(dob_month))) {
//                    msg = "year of retirementttttt can not exceed 65 years from the DOB year";
//                } else {
//                    msg = "";
//                }
//
//                String pdate = format.format(date);
//
//                java.util.Date date1 = format.parse(pdate);
//                java.util.Date date2 = format.parse(dor);
//                int yr60 = date1.getYear() + 47 + 1900;
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "y60 ret : " + yr60);
//                int yc = date2.getYear() + 1900;
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "date entered year : " + yc);
//                int yt = date1.getYear() + 1900;
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "current year : " + yt);
//                int mr1 = date1.getMonth() + 1;
//                int mr = date2.getMonth() + 1;
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "mr value is" + mr);
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "mr1 value is" + mr1);
//                int d1 = date1.getDate();
//                int d2 = date2.getDate();
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "d2 value is" + d2);
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "d1 value is" + d1);
//
//                /* String date_dor = dor.substring(0, 2);
//                    String month = dor.substring(3, 5);
//                    String year = dor.substring(6, 10);
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "month is::::"+month);
//                    String dateToCompare = new java.util.Date(year,month,date);
//                 */
//                if (yc < yt || yc > yr60) {
//
//                    msg = "Please enter Date Of retirement in correct format";
//
//                } else if (yc == yt) {
//                    if (mr < mr1) {
//
//                        msg = "year of retirement can not exceed 65 years from the DOB year";
//
//                    } else if (mr == mr1) {
//                        if (d2 < d1) {
//
//                            msg = "year of retirement can not exceed 65 years from the DOB year";
//
//                        }
//                    }
//                }
//
//            }
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "msg:::" + msg);
//        return msg;
//
//    }
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
                        if ((years) > 66 || ((years) == 66) && (month > dob_month)) {
                            msg = "year of retirement can not exceed 66 years from the DOB year";
                        } else {
                            msg = "";
                        }
                        String pdate = format.format(date);
                        java.util.Date date1 = format.parse(pdate);
                        java.util.Date date2 = format.parse(dor);
                        int yr60 = date1.getYear() + 48 + 1900;
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
                                msg = "year of retirement can not exceed 66 years from the DOB year";
                            } else if (mr == mr1) {
                                if (d2 < d1) {
                                    msg = "year of retirement can not exceed 66 years from the DOB year";
                                }
                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //String dob_month = dob.substring(3, 5);
                //d2 = new Date();


                /* String date_dor = dor.substring(0, 2);
                 String month = dor.substring(3, 5);
                 String year = dor.substring(6, 10);
                 System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "month is::::"+month);
                 String dateToCompare = new java.util.Date(year,month,date);
                 */
            }
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
                System.out.println("date in gem check::::" + pdate);
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
                            System.out.println("d1::::" + d11);
                            d12 = format.parse(dateEnd);
                            System.out.println("d12::::" + d12);
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
                        int yr60 = date1.getYear() + 48 + 1900;
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
                                msg = "year of retirement can not exceed 66 years from the DOB year";
                            } else if (mr == mr1) {
                                if (d2 < d1) {
                                    msg = "year of retirement can not exceed 66 years from the DOB year";
                                }
                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //String dob_month = dob.substring(3, 5);
                //d2 = new Date();


                /* String date_dor = dor.substring(0, 2);
                 String month = dor.substring(3, 5);
                 String year = dor.substring(6, 10);
                 System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "month is::::"+month);
                 String dateToCompare = new java.util.Date(year,month,date);
                 */
            }
        }
        return msg;

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
            System.out.println("list:::::::" + list);
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
                    System.out.println("contains");
                } else {
                    System.out.println("not contains");
                    System.out.println("inisde if list contain");
                    msg = "List Name should contain (hyphen(-) or dot[.]";
                }
            }
        } else {
            msg = "Please append @lsmgr.nic.in at the end";
        }
        System.out.println("msg::::::::::::::" + msg);
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
            if (!dnsurl.matches("^(?!:\\/\\/)([a-zA-Z0-9-]+\\.){0,5}[a-zA-Z0-9-][a-zA-Z0-9-]+\\.[a-zA-Z]{2,64}?$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean cnamevalidation(String cname) {
        boolean flag = false;
        if (!cname.isEmpty()) {
            if (!cname.matches("^(?!:\\/\\/)([a-zA-Z0-9-]+\\.){0,5}[a-zA-Z0-9-][a-zA-Z0-9-]+\\.[a-zA-Z]{2,64}?$")) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean accnameValidation(String accname) {
        boolean flag = false;
        if (accname.isEmpty()) {
            flag = true;
        } else if (!accname.isEmpty()) {
            if (!accname.matches("^[a-zA-Z0-9\\.\\-\\_]{5,15}$")) {
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

        } else if (!add1.matches("^[^<>&%]+$")) {
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
}
