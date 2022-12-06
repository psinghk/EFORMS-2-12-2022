package com.org.dao;

import com.org.bean.UserOfficialData;
import com.org.kavach.KavachAPI;
import com.org.services.Authenticate;
import com.org.utility.Constants;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Person;

public class Database implements Authenticate {

    private DbDao dbDao;

    public Database() {
        dbDao = new DbDao();
    }

     public ArrayList<String> setAllowedRegistrationNumbers(String aliasesInString, List<String> roles, ArrayList<String> formsAllowed, Set<String>  aliases) {
        return dbDao.setAllowedRegistrationNumbers(aliasesInString, roles, formsAllowed, aliases);
    }
     
     public Boolean isRegistrationNumberAllowed(String aliasesInString, List<String> roles, ArrayList<String> formsAllowed, Set<String>  aliases, String regNumber) {
        return dbDao.isRegistrationNumberAllowed(aliasesInString, roles, formsAllowed, aliases, regNumber);
    }
     
      public ArrayList<String> setAllowedRegistrationNumbersFromStatusTable(String aliasesInString, List<String> roles, ArrayList<String> formsAllowed, Set<String>  aliases) {
        return dbDao.setAllowedRegistrationNumbersFromStatusTable(aliasesInString, roles, formsAllowed, aliases);
    }

    public boolean isHog(String email) {
        return true;
    }

    @Override
    public boolean authenticate(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isNewUser(String email) {
        boolean flag = true;
        int count = dbDao.countRows(Constants.PROFILE_TABLE, email);
        if (count > 0) {
            flag = false;
        }
        return flag;
    }

    public Map<String, String> fetchUserValues(String email, String mobile) {
        return dbDao.fetchUserProfile(email, mobile);
    }

    public Map<String, String> fetchUserValuesFromEmpCode(String empcode) {
        return dbDao.fetchUserProfileFromOADOnEmpCode(empcode);
    }

    public Map<String, String> fetchUserValuesFromOAD(String uid) {
        return dbDao.fetchUserProfileFromOAD(uid);
    }

    public List<String> getStates() {
        return dbDao.getStates();
    }

    public Set<String> getRoles(String email, String mobile, Set<String> roles, boolean isIsEmailValidated) {
        return dbDao.getRoles(email, mobile, roles, isIsEmailValidated);
    }
    
    
     public Set<String> getRolesForSSO(String aliases, String mobile) {
        return dbDao.getRolesForSSO(aliases, mobile);
    }

    public String getMobile(String email) {
        return dbDao.fetchMobileFromProfile(email);

    }

    public boolean insertNewOtp(int otp_mobile, int otp_email, String mobile, String email) {
        return dbDao.insertOtp(otp_mobile, otp_email, mobile, email);
    }
    
    public Map<String, String> fetchOtp(String mobile, String email) {
        return dbDao.fetchOtp(email, mobile);
    }

    public String verifyOTP(String email, String mobile, String email_otp, String mobile_otp, boolean isEmailOtpActive, boolean isMobileOtpActive) {
        System.out.println("EMAIL : " + email);

        //Map<String, String> map = dbDao.fetchOtp(email,mobile);
        String flag = "";
        flag = dbDao.verifyOTP(email, mobile, email_otp, mobile_otp, isEmailOtpActive, isMobileOtpActive);

        System.out.println("flag in database :::::::" + flag);
        return flag;
    }

    public void insertIntoAuditTable(String email, String role, String login_status) {
        dbDao.insertLoginDetails(email, role, login_status);
    }

    public void insertIntoDigitalTable(String email, String role) {
        dbDao.insertIntoDigitalTable(email, role);
    }

    public boolean fetchAuditTable(String email) {
        return dbDao.fetchAuditTable(email);
    }

    public void updateAuditTableForLogout(String email, String logout_status) {
        dbDao.updateLogoutDetails(email, logout_status);
    }

    public boolean areFormsAllowedAsAdmin(String email) {
        return dbDao.checkMailAdminFormsAllowed(email);
    }

    public boolean isEmailOtpActive(String email) {
        return dbDao.isEmailOtpActive(email);
    }

    public boolean isMobileOtpActive(String mobile, String countryCode) {
        return dbDao.isMobileOtpActive(mobile, countryCode);
    }

    public String resendMobile(String email, String mobile) {
        return dbDao.resendMobile(email, mobile);
    }
//resendOtpForLogin
      public String resendOtpForLogin(String email, String mobile) {
        return dbDao.resendOtpForLogin(email, mobile);
    }
    public String resendEmail(String email, String mobile) {
        return dbDao.resendEmail(mobile,email);
    }

    public String checkSupportEmail(String mobile) {
        return dbDao.checkSupportEmail(mobile);
    }

    public String checkAdminEmail(String mobile) {
        return dbDao.checkAdminEmail(mobile);
    }

    public ArrayList<String> fetchAllowedFormsForAdmins(Set<String> aliases) {
        return dbDao.fetchAllowedFormsForAdmins(aliases);
    }

    public String fetchIndividualEmailForSupporOrAdmin(String mobile) {
        return dbDao.fetchIndividualEmail(mobile);
    }

    public Map getCaValues(String email) {
        return dbDao.getCaValues(email);
    }

    public boolean updateSummaryTable(String role, List<String> listOfCounts, Map<String, Boolean> mapOfCounts, Set<String> aliases) {
        return dbDao.updateSummaryTable(role, listOfCounts, mapOfCounts, aliases);
    }

    public boolean insertIntoEmpCoordForVPN(String coordinator) {
        return dbDao.insertIntoEmpCoordForVPN(coordinator);
    }

    public ArrayList<String> empCoordData(HashMap map) {
        return dbDao.empCoordData(map);
    }

    public String fetchRo(Set<String> email) {
        return dbDao.fetchRo(email);
    }

    public Set<String> fetchCoIds(List<String> aliases) {
        return dbDao.fetchCoIds(aliases);
    }
    
    public Set<String> fetchDaIds(List<String> aliases) {
        return dbDao.fetchDaIds(aliases);
    }

    public Set<String> findIntersection(List<Set<String>> ids) {
        return dbDao.findIntersection(ids);
    }

    public String insertIntoCoordsIdTable(String to_email) {
        return dbDao.insertIntoCoordsIdTable(to_email);
    }
    
    public String insertIntoDaIdTable(String to_email) {
        return dbDao.insertIntoDaIdTable(to_email);
    }

    public boolean updateFinalAuditTrack(String registration_no, String commaSeperatedString) {
        return dbDao.updateFinalAuditTrack(registration_no, commaSeperatedString);
    }

    public boolean updateFinalAuditTrackForDa(String registration_no, String commaSeperatedString) {
        return dbDao.updateFinalAuditTrackForDa(registration_no, commaSeperatedString);
    }
    
    public String fetchPunjabNodalOfficers(String district) {
        return dbDao.fetchPunjabNodalOfficers(district);
    }
    
    public String fetchDelhiNodalOfficers(String employment, String ministry, String department) {
        return dbDao.fetchDelhiNodalOfficers(employment, ministry, department);
    }

    public String fetchPunjabDA(String employment, String ministry, String department) {
        return dbDao.fetchPunjabDA(employment, ministry, department);
    }

    public boolean isReservedKeyWord(String uid) {
        return dbDao.isReservedKeyWord(uid);
    }

    public Set<String> fetchBoFromEmpCoord(Map<String, String> profile) {
        return dbDao.fetchBoFromEmpCoord(profile);
    }

//     public String resendMobileForUpdateMobile(String mobile,String new_mobile, String email) {
//        return dbDao.resendMobileForUpdateMobile(mobile,new_mobile, email);
//    }
    public boolean insertOtpAttempts(String emailid, String mobile, String otp, String ip, String status) {
        return dbDao.insertOtpAttempts(emailid, mobile, otp, ip, status);

    }

    public String insertNewOtpInUpdateMobile(int newcode, String mobile, String country_code, String new_mobile, String email,String existInKavach) {
        return dbDao.insertNewOtpInUpdateMobile(newcode, mobile, country_code, new_mobile, email,existInKavach);
    }

    public boolean updateOtpOnBehalf(int otp_mob, String OnBehalfemail, String email) {
        return dbDao.updateOtpOnBehalf(otp_mob, OnBehalfemail, email);
    }

    public boolean updateOtpInUpdateMobile(String new_mobile, String email) {
        return dbDao.updateOtpInUpdateMobile(new_mobile, email);
    }
    
    public boolean updateResendOtpAttempts(String new_mobile, String email, String forWhich) {
        return dbDao.updateResendOtpAttempts(new_mobile, email, forWhich);
    }

//      public boolean insertIntoUpdatemobileTable(String email,String new_mobile,String newcode,String status) {
//        return dbDao.insertIntoUpdatemobileTable(email,new_mobile,newcode,status);
//    }
    public Map<String, String> fetchOtpForUpdateMobile(String email, String mobile) {
        return dbDao.fetchOtpForUpdateMobile(email, mobile);
    }
    
    public Map<String, String> fetchOtpForCheckingResendAttempts(String email, String mobile) {
         return dbDao.fetchOtpForCheckingResendAttempts(email, mobile);
    }

    public Map<String, String> fetchOtpForOnBehalfEmail(String OnBehalfemail, String email) {
        return dbDao.fetchOtpForOnBehalfEmail(OnBehalfemail, email);
    }

    //ADDED for SSO
    public boolean fetchLoginDetailsSso(String emailid) throws ParseException {
        Map<String, Object> details = dbDao.fetchLoginDetailsSso(emailid);
        boolean sessionout = false;

        if (details.isEmpty()) {
            sessionout = true;
        }
        return sessionout;
    }

    public boolean MobileRequestAlreadyExist(String email) {
        return dbDao.MobileRequestAlreadyExist(email);
    }
    
      public boolean MobileRequestAlreadyPending(String email) {
        return dbDao.MobileRequestAlreadyPending(email);
    }

    public boolean insertMobileTrail(String emailid, String ip, String attemptcounts, String status, String useragent) {
        return dbDao.insertMobileTrail(emailid, ip, attemptcounts, status, useragent);

    }

    public boolean insertMobileTrailAttempts(String emailid, String ip, String attemptcounts, String status, String useragent) {
        return dbDao.insertMobileTrailAttempts(emailid, ip, attemptcounts, status, useragent);

    }

    public boolean updateMobileTrail(String emailid, String ip, String attemptcounts, String status) {
        return dbDao.updateMobileTrailAttempts(emailid, ip, attemptcounts, status);

    }

    public Map<String, Object> fetchTrailMobileUpdate(String emailid) {
        return dbDao.fetchTrailMobileUpdate(emailid);
    }

    public String fetchTrailMobileUpdateBlockOneHour(String emailid, String ip) {

        return dbDao.fetchTrailMobileUpdateOneHour(emailid, ip);
    }

    public String fetchOtpSaveAttemptsOneHour(String emailid, String ip) {

        return dbDao.fetchOtpSaveAttemptsOneHour(emailid, ip);
    }
     public String checkEmailForUpdateMobile(String email) {
        return dbDao.checkEmailForUpdateMobile(email);
    }
      public String checkBoForUpdateMobile(String bo) {
        return dbDao.checkBoForUpdateMobile(bo);
    }
      
       //11-apr-2022
    public String existInKavachOLD(String bo) {
        return dbDao.existInKavach(bo);
    }

    public String existInKavach(String email) {
        //return "exist_in_kavach";   // for testing purpose added by rahul 13June2022 , It must be Uncommented on live
        Ldap ldap = new Ldap();
        String dn = ldap.fetchDn(email);
        return KavachAPI.isUserKavachEnabled(dn, email, "false");
    }
    //11-apr-2022
      
      //added for sso session out
       public boolean fetchLoginDetailsSsoCallback(String emailid) throws ParseException { 
        Map<String, Object> details = dbDao.fetchLoginDetailsSsoCallback(emailid);
        boolean sessionout = false;
       if(!details.isEmpty() && details.get("status")!=null && details.get("status").toString().equals("sessionoutfalse")){
             sessionout=false;
       } else {
            sessionout=true;
       }
       
//        if (details.isEmpty()) {
//            sessionout = true;
//        }
        return sessionout;
    }

//added for sso call back
         public void updateLogoutDetailsForcefully(String email, String logout_status) {
        dbDao.updateLogoutDetailsForcefully(email, logout_status);
    }
         
          public int insertParichayDataStorage(Person person) {
        return dbDao.insertParichayDataStorage(person);
    }

    public Map<String, Object> fetchFromParichayDataStorage(String email) {

        return dbDao.fetchFromParichayDataStorage(email);

    }

    public Map<String, String> fetchEmpDepAndEmpMinVpnCordinator(String email) {

        return dbDao.fetchEmpDepAndEmpMinVpnCordinator(email);

    }
    public List<Map<String, String>> fetchFromEmpCordinator(String email, String empType) {

        return dbDao.fetchFromEmpCordinator(email,empType);

    }
    
    public List<FinalAuditDetails> fetchAllPendingRequstsfromFinalAuditTrack(Map<String, String> radioEmpCatMinDept,String email, String status) {

        return dbDao.fetchAllPendingRequstsfromFinalAuditTrack(radioEmpCatMinDept,email, status);

    }
    
    public ArrayList<String> fetchCoordinatorEmailFromCoordinatorEmailForMoveRequests(String coord_email,String emp_type) {
        return dbDao.fetchCoordinatorEmailFromCoordinatorEmailForMoveRequests(coord_email,emp_type);
    }

    

    public int fetchCountOfWrongPasswordAttempts(Set<String> aliases, String mobile) {
         return dbDao.fetchCountOfWrongPasswordAttempts(aliases, mobile); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isUserBlockedDueToWrongPasswordAttempts(Set<String> aliases, String mobile) {
       return dbDao.isUserBlockedDueToWrongPasswordAttempts(aliases, mobile); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateBlockUserTable(String email, String message) {
       dbDao.updateBlockUserTable(email,message); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isNoOfOtpAttemptsExceeds(String email) {
       return dbDao.isNoOfOtpAttemptsExceeds(email);
    }

    public boolean isUserBlockedDueToWrongOtpAttempts(Set<String> aliases,String mobile) {
      return dbDao.isUserBlockedDueToWrongOtpAttempts(aliases,mobile);
    }

    public boolean isUserBlockDuetoWrongEmailOtpOrMobileOtp(Set<String> aliases) {
        return dbDao.isUserBlockDuetoWrongEmailOtpOrMobileOtp(aliases);
    }

    public int fetchEmailResendAttempts(String email, String mobile) {
      return dbDao.fetchEmailResendAttempts(email,mobile);
    }

    public String fetchCoordinatorOrDaEmailByBo(String userBo) {
        return dbDao.fetchCoordinatorOrDaEmailByBo(userBo);
    }
}
