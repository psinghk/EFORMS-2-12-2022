package com.org.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserData {

    private String token, name, email, password, mobile, role, previousRoleAtLogin, telephone, userBo, alternate_email, returnString, uid, emailOtp, mobileOtp, countryCode, verifiedOtp, bo_check, aliasesInString, individualEmail, hodEmail, loginCaptcha, msg, emailAgainstMobileNumber, existMobileRequestByFlow, existMobileRequest, otp_generate_or_resend, on_behalf_email, userAuthenticatedForUpdateMobile, isexistInKavach;
    private String ssoAllowed;
    private String pariSessionout;
    private int noOfMobileOtpAttempts, noOfEmailAttempts;

    private String type;
    private boolean isNewUser, isNewUserforService, isEmailValidated, dashboardUsagePrivilege, authenticated, hasMultipleMobileNumbers, isNICEmployee, isInRoTable, emailOtpActive, mobileOtpActive, isHOG, isHOD, isMobileInLdap, resendmobile, resendemail, existInsupport, existInadmin, rolesAlreadyAssigned, loginCaptchaAuthenticated;
    //if old number does not exist..update mobile
    private String newcode, new_mobile;
    private List<String> roles, previousRolesAtLogin;
    private ArrayList<String> formsAllowed;
    private ImportantData impData;
    private HodData hodData;
    private UserOfficialData userOfficialData;
    private Set<String> Aliases;
    private List<String> aliasesInList;
    private int emailCountAgainstMobile;
    private List<String> rolesService, previousRolesServiceAtLogin; // 24thoct19
    private boolean blocked;

    private String nicDateOfBirth;
    private String nicDateOfRetirement;
    private String designation;
    private String displayName;

    public UserData() {
        isNewUser = true;
        hodData = new HodData();
        impData = new ImportantData();
        userOfficialData = new UserOfficialData();
    }

    public String getPreviousRoleAtLogin() {
        return previousRoleAtLogin;
    }

    public void setPreviousRoleAtLogin(String previousRoleAtLogin) {
        this.previousRoleAtLogin = previousRoleAtLogin;
    }

    public List<String> getPreviousRolesAtLogin() {
        return previousRolesAtLogin;
    }

    public void setPreviousRolesAtLogin(List<String> previousRolesAtLogin) {
        this.previousRolesAtLogin = previousRolesAtLogin;
    }

    public List<String> getPreviousRolesServiceAtLogin() {
        return previousRolesServiceAtLogin;
    }

    public void setPreviousRolesServiceAtLogin(List<String> previousRolesServiceAtLogin) {
        this.previousRolesServiceAtLogin = previousRolesServiceAtLogin;
    }

    
    public int getNoOfMobileOtpAttempts() {
        return noOfMobileOtpAttempts;
    }

    public void setNoOfMobileOtpAttempts(int noOfMobileOtpAttempts) {
        this.noOfMobileOtpAttempts = noOfMobileOtpAttempts;
    }

    public int getNoOfEmailAttempts() {
        return noOfEmailAttempts;
    }

    public void setNoOfEmailAttempts(int noOfEmailAttempts) {
        this.noOfEmailAttempts = noOfEmailAttempts;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // start, 24thoct19
    public List<String> getRolesService() {
        return rolesService;
    }

    public void setRolesService(List<String> rolesService) {
        this.rolesService = rolesService;
    }

    public String getMsg() {
        return msg;
    }

    // end, 24thoct19
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String fetchIndividualEmail() {
        return individualEmail;
    }

    public boolean isLoginCaptchaAuthenticated() {
        return loginCaptchaAuthenticated;
    }

    public void setLoginCaptchaAuthenticated(boolean loginCaptchaAuthenticated) {
        this.loginCaptchaAuthenticated = loginCaptchaAuthenticated;
    }

    public String getLoginCaptcha() {
        return loginCaptcha;
    }

    public void setLoginCaptcha(String loginCaptcha) {
        this.loginCaptcha = loginCaptcha;
    }

    public void setIndividualEmail(String individualEmail) {
        this.individualEmail = individualEmail;
    }

    public String getAliasesInString() {
        return aliasesInString;
    }

    public void setAliasesInString(String aliasesInString) {
        this.aliasesInString = aliasesInString;
    }

    public ArrayList<String> getFormsAllowed() {
        return formsAllowed;
    }

    public void setFormsAllowed(ArrayList<String> formsAllowed) {
        this.formsAllowed = formsAllowed;
    }

    public boolean isRolesAlreadyAssigned() {
        return rolesAlreadyAssigned;
    }

    public void setRolesAlreadyAssigned(boolean rolesAlreadyAssigned) {
        this.rolesAlreadyAssigned = rolesAlreadyAssigned;
    }

    public String getHodEmail() {
        return hodEmail;
    }

    public void setHodEmail(String hodEmail) {
        this.hodEmail = hodEmail;
    }

    public String getVerifiedOtp() {
        return verifiedOtp;
    }

    public void setVerifiedOtp(String verifiedOtp) {
        this.verifiedOtp = verifiedOtp;
    }

    public boolean isIsMobileInLdap() {
        return isMobileInLdap;
    }

    public void setIsMobileInLdap(boolean isMobileInLdap) {
        this.isMobileInLdap = isMobileInLdap;
    }

    public boolean isIsHOG() {
        return isHOG;
    }

    public void setIsHOG(boolean isHOG) {
        this.isHOG = isHOG;
    }

    public boolean isIsHOD() {
        return isHOD;
    }

    public void setIsHOD(boolean isHOD) {
        this.isHOD = isHOD;
    }

    public String getCountryCode() {
        if (countryCode == null) {
            return "+91";
        } else if (countryCode.isEmpty() || countryCode.equalsIgnoreCase("null")) {
            return "+91";
        } else {
            return countryCode;
        }
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        System.out.println("email setter");
        this.email = email;
    }

    public void setPassword(String password) {
        System.out.println("password setter");
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(boolean isNewUser) {
        this.isNewUser = isNewUser;
    }

    public boolean isIsEmailValidated() {
        return isEmailValidated;
    }

    public void setIsEmailValidated(boolean isEmailValidated) {
        System.out.println("Valid Email Address");
        this.isEmailValidated = isEmailValidated;
    }

    public HodData getHodData() {
        return hodData;
    }

    public void setHodData(HodData hodData) {
        this.hodData = hodData;
    }

    public boolean isDashboardUsagePrivilege() {
        return dashboardUsagePrivilege;
    }

    public void setDashboardUsagePrivilege(boolean dashboardUsagePrivilege) {
        this.dashboardUsagePrivilege = dashboardUsagePrivilege;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isHasMultipleMobileNumbers() {
        return hasMultipleMobileNumbers;
    }

    public void setHasMultipleMobileNumbers(boolean hasMultipleMobileNumbers) {
        this.hasMultipleMobileNumbers = hasMultipleMobileNumbers;
    }

    public UserOfficialData getUserOfficialData() {
        return userOfficialData;
    }

    public void setUserOfficialData(UserOfficialData userOfficialData) {
        this.userOfficialData = userOfficialData;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserBo() {
        return userBo;
    }

    public void setUserBo(String userBo) {
        this.userBo = userBo;
    }

    public String getAlternate_email() {
        return alternate_email;
    }

    public void setAlternate_email(String alternate_email) {
        this.alternate_email = alternate_email;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getReturnString() {
        return returnString;
    }

    public void setReturnString(String returnString) {
        this.returnString = returnString;
    }

    public ImportantData getImpData() {
        return impData;
    }

    public void setImpData(ImportantData impData) {
        this.impData = impData;
    }

    public boolean isIsNICEmployee() {
        return isNICEmployee;
    }

    public void setIsNICEmployee(boolean isNICEmployee) {
        this.isNICEmployee = isNICEmployee;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isIsInRoTable() {
        return isInRoTable;
    }

    public void setIsInRoTable(boolean isInRoTable) {
        this.isInRoTable = isInRoTable;
    }

    public String getEmailOtp() {
        return emailOtp;
    }

    public void setEmailOtp(String emailOtp) {
        this.emailOtp = emailOtp;
    }

    public String getMobileOtp() {
        return mobileOtp;
    }

    public void setMobileOtp(String mobileOtp) {
        this.mobileOtp = mobileOtp;
    }

    public String getBo_check() {
        return bo_check;
    }

    public void setBo_check(String bo_check) {
        this.bo_check = bo_check;
    }

    public boolean isResendmobile() {
        return resendmobile;
    }

    public void setResendmobile(boolean resendmobile) {
        this.resendmobile = resendmobile;
    }

    public boolean isResendemail() {
        return resendemail;
    }

    public void setResendemail(boolean resendemail) {
        this.resendemail = resendemail;
    }

    public boolean isExistInsupport() {
        return existInsupport;
    }

    public void setExistInsupport(boolean existInsupport) {
        this.existInsupport = existInsupport;
    }

    public boolean isExistInadmin() {
        return existInadmin;
    }

    public void setExistInadmin(boolean existInadmin) {
        this.existInadmin = existInadmin;
    }

    public Set<String> getAliases() {
        return Aliases;
    }

    public void setAliases(Set<String> Aliases) {
        this.Aliases = Aliases;
    }

    public List<String> getAliasesInList() {
        return aliasesInList;
    }

    public void setAliasesInList(List<String> aliasesInList) {
        this.aliasesInList = aliasesInList;
    }

    public boolean isEmailOtpActive() {
        return emailOtpActive;
    }

    public void setEmailOtpActive(boolean emailOtpActive) {
        this.emailOtpActive = emailOtpActive;
    }

    public boolean isMobileOtpActive() {
        return mobileOtpActive;
    }

    public void setMobileOtpActive(boolean mobileOtpActive) {
        this.mobileOtpActive = mobileOtpActive;
    }

    public String getNewcode() {
        return newcode;
    }

    public void setNewcode(String newcode) {
        this.newcode = newcode;
    }

    public String getNew_mobile() {
        return new_mobile;
    }

    public void setNew_mobile(String new_mobile) {
        this.new_mobile = new_mobile;
    }

    public String getEmailAgainstMobileNumber() {
        return emailAgainstMobileNumber;
    }

    public void setEmailAgainstMobileNumber(String emailAgainstMobileNumber) {
        this.emailAgainstMobileNumber = emailAgainstMobileNumber;
    }

    public int getEmailCountAgainstMobile() {
        return emailCountAgainstMobile;
    }

    public void setEmailCountAgainstMobile(int emailCountAgainstMobile) {
        this.emailCountAgainstMobile = emailCountAgainstMobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isIsNewUserforService() {
        return isNewUserforService;
    }

    public void setIsNewUserforService(boolean isNewUserforService) {
        this.isNewUserforService = isNewUserforService;
    }

    public String getSsoAllowed() {
        return ssoAllowed;
    }

    public void setSsoAllowed(String ssoAllowed) {
        this.ssoAllowed = ssoAllowed;
    }

    public String getPariSessionout() {
        return pariSessionout;
    }

    public void setPariSessionout(String pariSessionout) {
        this.pariSessionout = pariSessionout;
    }

    public String getExistMobileRequest() {
        return existMobileRequest;
    }

    public void setExistMobileRequest(String existMobileRequest) {
        this.existMobileRequest = existMobileRequest;
    }

    public String getOtp_generate_or_resend() {
        return otp_generate_or_resend;
    }

    public void setOtp_generate_or_resend(String otp_generate_or_resend) {
        this.otp_generate_or_resend = otp_generate_or_resend;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getOn_behalf_email() {
        return on_behalf_email;
    }

    public void setOn_behalf_email(String on_behalf_email) {
        this.on_behalf_email = on_behalf_email;
    }

    public String getUserAuthenticatedForUpdateMobile() {
        return userAuthenticatedForUpdateMobile;
    }

    public void setUserAuthenticatedForUpdateMobile(String userAuthenticatedForUpdateMobile) {
        this.userAuthenticatedForUpdateMobile = userAuthenticatedForUpdateMobile;
    }

    public String getIsexistInKavach() {
        return isexistInKavach;
    }

    public void setIsexistInKavach(String isexistInKavach) {
        this.isexistInKavach = isexistInKavach;
    }

    public String getExistMobileRequestByFlow() {
        return existMobileRequestByFlow;
    }

    public void setExistMobileRequestByFlow(String existMobileRequestByFlow) {
        this.existMobileRequestByFlow = existMobileRequestByFlow;
    }

    public String getNicDateOfBirth() {
        return nicDateOfBirth;
    }

    public void setNicDateOfBirth(String nicDateOfBirth) {
        this.nicDateOfBirth = nicDateOfBirth;
    }

    public String getNicDateOfRetirement() {
        return nicDateOfRetirement;
    }

    public void setNicDateOfRetirement(String nicDateOfRetirement) {
        this.nicDateOfRetirement = nicDateOfRetirement;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
