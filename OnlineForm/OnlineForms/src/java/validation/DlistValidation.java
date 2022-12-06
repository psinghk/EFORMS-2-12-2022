/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validation;

import java.util.HashMap;

/**
 *
 * @author Rahul 
 */
public class DlistValidation 
 {
    HashMap values = new HashMap();
    
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
    
      public boolean dnstxtValidation(String add1) {
        boolean flag = false;
        if (add1.isEmpty()) {
            flag = true;

        } else if (!add1.matches("^[^<>&%]{2,300}+$")) {
            flag = true;
        }
        return flag;
    }

   
    
    public HashMap IsMemberAllowed(String isMemberAllowed) {
        String valid = "false";
        String errorMsg = "";
        values.clear();
        if (isMemberAllowed == null || isMemberAllowed.equals("")) {
            errorMsg = "IsMemberAllowed can not be blank.";
        } else {
            if (!isMemberAllowed.matches("^[a-z|A-Z|]+[a-z|A-Z|.|\\s]*")) {
                errorMsg = "Enter IsMemberAllowed in correct format.";
            } else {
                valid = "true";
            }
        }
        values.put("valid", valid);
        values.put("errorMsg", errorMsg);
        return values;
    }
    public HashMap IsListTemp(String isListTemp) {
        String valid = "false";
        String errorMsg = "";
        values.clear();
        if (isListTemp == null || isListTemp.equals("")) {
            errorMsg = "IsListTemp can not be blank.";
        } else {
            if (!isListTemp.matches("^[a-z|A-Z|]+[a-z|A-Z|.|\\s]*")) {
                errorMsg = "Enter IsListTemp in correct format.";
            } else {
                valid = "true";
            }
        }
        values.put("valid", valid);
        values.put("errorMsg", errorMsg);
        return values;
    }
    public HashMap MailAcceptance(String mailAcceptance) {
        String valid = "false";
        String errorMsg = "";
        values.clear();
        if (mailAcceptance == null || mailAcceptance.equals("")) {
            errorMsg = "MailAcceptance can not be blank.";
        } else {
            if (!mailAcceptance.matches("^[a-z|A-Z|]+[a-z|A-Z|.|\\s]*")) {
                errorMsg = "Enter MailAcceptance in correct format.";
            } else {
                valid = "true";
            }
        }
        values.put("valid", valid);
        values.put("errorMsg", errorMsg);
        return values;
    }
    public HashMap TotalMembersCount(String totalMembersCount) {
        String valid = "false";
        String errorMsg = "";
        values.clear();
        if (totalMembersCount == null || totalMembersCount.equals("")) {
            errorMsg = "Total Members Count can not be blank.";
        } else {
            if (!totalMembersCount.matches("^[0-9]{2,5}$")) {
                errorMsg = "Enter Total Members Count in correct format.";
            } else {
                valid = "true";
            }
        }
        values.put("valid", valid);
        values.put("errorMsg", errorMsg);
        return values;
    } 
    
    public HashMap OwnerName(String ownerName) {
        String valid = "false";
        String errorMsg = "";
        values.clear();
        if (ownerName == null || ownerName.equals("")) {
            errorMsg = "Owner Name can not be blank.";
        } else {
            if (!ownerName.matches("^[a-z|A-Z|]+[a-z|A-Z|.|\\s]*")) {
                errorMsg = "Enter OwnerName in correct format.";
            } else {
                valid = "true";
            }
        }
        values.put("valid", valid);
        values.put("errorMsg", errorMsg);
        return values;
    }
        public HashMap Mobile(String mobile) {
        String valid = "false";
        String errorMsg = "";
        values.clear();
        String mob = "";
        if (mobile == null || mobile.equals("")) {
            errorMsg = "Mobile can not be blank.";
        } else {
            try {
                long mm = Double.valueOf(mobile).longValue();
                mob = "+"  + mm;
                values.put("mob", mob);
            } catch (Exception e) {
                //all.add("Mobile number is not valid. Where row is:" + count + " and column is:6");
            }
            if ( !mob.matches("^[+0-9]{13}$")) {
                errorMsg = "Please enter valid mobile number with country code[e.g: 919999999999], limit[12 digits].";
            } else if (!mob.matches("^[+0-9]{8,15}$")) {
                errorMsg = "Please enter valid mobile number with country code [e.g: 123456789],  limit[8,14 digits].";
            } else {
                valid = "true";
            }
//                if (!mob.matches("^[0-9]{12}$")) {
//                    errorMsg = "Mobile number is not valid.";
//                } else {
//                    valid = "true";
//                }
        }
        values.put("valid", valid);
        values.put("errorMsg", errorMsg);
        return values;
    }

    
    public HashMap ModeratorName(String moderatorName) {
        String valid = "false";
        String errorMsg = "";
        values.clear();
        if (moderatorName == null || moderatorName.equals("")) {
            errorMsg = "Moderator Name can not be blank.";
        } else {
            if (!moderatorName.matches("^[a-z|A-Z|]+[a-z|A-Z|.|\\s]*")) {
                errorMsg = "Enter Moderator Name in correct format.";
            } else {
                valid = "true";
            }
        }
        values.put("valid", valid);
        values.put("errorMsg", errorMsg);
        return values;
    }
  
    public boolean webcastamountvalidation(String amount) {
        boolean flag = false;
        String regex = "[0-9]*+[.]+[0-9]*";
        if (!amount.isEmpty()) {
            if (amount.matches(regex)) {

            } else {
                flag = true;
            }
        } else {
            flag = true;
        }
        
        return flag;
    }
  
   public HashMap OwnerAdmin(String ownerAdmin) {
        String valid = "false";
        String errorMsg = "";
        values.clear();
        if (ownerAdmin == null || ownerAdmin.equals("")) {
            errorMsg = "Owner Admin can not be blank.";
        } else {
            if (!ownerAdmin.matches("^[a-z|A-Z|]+[a-z|A-Z|.|\\s]*")) {
                errorMsg = "Enter Owner Admin in correct format.";
            } else {
                valid = "true";
            }
        }
        values.put("valid", valid);
        values.put("errorMsg", errorMsg);
        return values;
    }
   public HashMap ModeratorAdmin(String moderatorAdmin) {
        String valid = "false";
        String errorMsg = "";
        values.clear();
        if (moderatorAdmin == null || moderatorAdmin.equals("")) {
            errorMsg = "Moderator Admin can not be blank.";
        } else {
            if (!moderatorAdmin.matches("^[a-z|A-Z|]+[a-z|A-Z|.|\\s]*")) {
                errorMsg = "Enter Moderator Admin in correct format.";
            } else {
                valid = "true";
            }
        }
        values.put("valid", valid);
        values.put("errorMsg", errorMsg);
        return values;
    }
   
}

