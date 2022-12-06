/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.ssologin;

import com.google.gson.Gson;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.controller.LoginAction;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.SignUpService;
import com.org.utility.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Person;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author Satya
 */
public class SsoAuth extends ActionSupport implements SessionAware, ServletRequestAware {

    private UserData userValues;
    private Ldap ldap = null;
    String ip = ServletActionContext.getRequest().getRemoteAddr();
    private SessionMap<String, Object> sessionMap;
    LoginAction loginAction;
    private SignUpService signUpService = null;
    Map DAOGotMap = null;
    Map DAOGotFFMap = null;
    String localTokenId;
    String uid;
    String browserId;
    String userName;
    String sessionId;
    String mobileNo;
    HttpServletRequest request;
    String userJSONDec;

    HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    String response_string;
    Person person;

    //private String api_key = "tzcdo2Gm337yOPL1rJnEE1qvDFFpMWTX";
    //private String api_key="n4LmmzNH9q3C+Ny8F1WHs3yqhbfJfTkN";
    private String api_key = "I1JryyZnbozv9ukR787GT/Li0DX6nWIa";

    private Database db = null;
    String path;

    public SsoAuth() {
        ldap = new Ldap();
        userValues = new UserData();
        db = new Database();
        loginAction = new LoginAction();
        signUpService = new SignUpService();
    }

    public UserData getUserValues() {
        return userValues;
    }

    public void setUserValues(UserData userValues) {
        this.userValues = userValues;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocalTokenId() {
        return localTokenId;
    }

    public void setLocalTokenId(String localTokenId) {
        this.localTokenId = localTokenId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBrowserId() {
        return browserId;
    }

    public void setBrowserId(String browserId) {
        this.browserId = browserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUserJSONDec() {
        return userJSONDec;
    }

    public void setUserJSONDec(String userJSONDec) {
        this.userJSONDec = userJSONDec;
    }

    public String getResponse_string() {
        return response_string;
    }

    public void setResponse_string(String response_string) {
        this.response_string = response_string;
    }

    public String authUser() throws IOException {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                + "inside authuser ssologin----");

        // String response_string = "B33B279D96B565AA6BC8161D017244C2958B4D865A141B972C37F3C3FA97A262652010A85A2AE4594867850A52193E891378EB948BCE7B5B3FD386333D5AAC611A8DCFEB555F656FB3C380779A3B66A1279E959D98D88F4A78448C109611EC020D1C7D5C8B2757BFEC52355D346F3611DCE19E1F4DB1E41DDD7EEBFFD1201723997E7071B4224234D1BD68D1660A7300AA49D54C4BF36244C2D5B55A74ED7C06A4FF33B85976DFD05091BE981831EFC8CB3AEA125B8D9A5CCB875B123E2795F273248581DE1B8A46F2380E45EF8AB3AA90B1EF766398E067BC7AAA80CDE39194852D832008C576E9122D02EC7656E617C8F27E058DC72379DD555ED6B9D3CA613D4846CECBBEDB18B132F7A6454022DBB450C9E0579831787BFD1E6626F51F49C31007DF7C7747B63D8D75E84C3EF4FB33C113C71C0C042A14C7C0C3AEACD5C44934D6896524FD59B7354626EBA1FA8F0774D101F6152D8057F74A673F4640DBAD2648537A113EF93935480AC81AB08883EB22328371B54CB07CA99552514B5A7D20EAE75BCE2DB51FA9A63869DD888E8B5BD21F9592E735ADB9AE71FE9CA60914A918029B553FCEDFBC0A0491B33F290900CF608325F4EC06B5E38CE05B4ACAB34E33AE554D51444D4CDF6D113E2CA556E0D6EBFA483ACE02D415A3B628C66D6F43371C8DAF3D06E3A59032D67CC98424C69E2DEA1C8BB40568FA005A1A930F598A30FFAD415734F2ABF8A673156BF0512BED1B8BCAF53B38A9C149360408B3C20880C4187486229C1E1DB0CA6919DC51411D5B3A4501A415A7D1B0798BB15AB6D923CE096EF6D086B6C0FD0C33C881281BEBECCCEBF162338E70521C793B2AA40BAA1D2F0ED38204EE8FF704BF2D4FCDC74B6D0BFBD8398B7B65A14876391C37D53E5D7185AAFDDFF9525D62077B0EBACB80D20FE50918FA72DCD29E40EAF6CCC924FF5BE75FE6EEB17C958B986F925536B02F576F71AA325153C3187803C92461944E351F0B90653191F3723AE19EBB51FAAE38288F1C2FE0D7405A99D24E3F5D1C3FB3D6136D5A5BC7A981AD9AF346C862068B9C1123558F01C03E7207C2DBA4B3A639F154D84D22F6AFE8D8A4B784F8448ABF37CA38D20FC534935423DD";
        // String response_string="9FE7400797FCFB4FA0540401802B1878D62924524BD28EAB0F9FA33EC94F6079492496EDA812C25E5AD02CE467F7069004E677A9EA3997BCD253A46725A922B2AD923CE2009BDE4263799D8EDC30597998F0182B0D0289B3D1BC5F4F2E0FFEE2C8B5C0A31C10E2720C30E682FB9071272A06CA26E770EDCDAEB761882D2AAFA4456C432ED23086400B11E74AB0854FED88F5FFB9225DB283739DA32F512DE573E41D94EF9F7DEB88EDAD858EB740CA74ECC6C95B85FB19CB3C90EC49D72B20B17F5599B03816441F9B920596A99797703A2D913EA9CEDF31492CAED89EE51C1538AAD66FAD4FCAF404265841321FAF85898955FBB77396E90314D5FA4F1C4D048644AAEBF8C873B473EB56A6791318C179520EB28BFF06B16B5A6CAF435A59BB6AD90778890C4F1D9986AB86A5CEB283FA4241A0FBEC82A9794185CA7FF98D82F1AA28432FCC1964E0BC430F0FEAA5947A2A9C830122A85233FAF477516D6204ECD7504A540E9C353EE0F1C7CF27493F76F494C3C95A571347C359F79B5D0AA370684FE051071E940D5C0671552FEAF43D6FB442D5AE9941B4BAB1C48888E9529363F524B9947F4DF6957AEF4511A3E8FB648FC58213D89E24ED68A391CD69B38737F883D582828FBA8B2A650367A6A10E66354C14B484F82963483A7B9C8221783D6335CC55217A999F24CFF66238A06E71D4D74D082E6CA8253AD5E6AD721BA15FF1E41570D6F3AAB7DF914D4408543D938C86FBF54BD44FAFABAA22775E21A2DDC58332AEF3104708543284A97C519475A8F868096DB2307352A5D26B7F91CCBB4FBF69998B586C7E10BDCB17FA557AF9BBF64001EC4865B7136CA759CD33878D48FD0BFE0133019AAD47C2D090498AD1A2A3AD867ACEC3905A580B8ACAB9175E662767F153D0941CB270FF59A0914BAAD00636C7CCC35DEA777BE54DDF8DB1C0A696CD6D14F8F3923989A28642599F194EB155CDBAA081BAF79EC8EE8C207D287E24C6DE4764E45A631800C5E09F302800494758922820169A231D31F62F0224FFF8D89659FA19D75361ADEC74D68C213DCE99FE77DE2CC8E5713540C5A4A2F929E3A526D1DD2B1B2E26D1D8F4D8CFD1B94588335BDE225E69F75ADCD02771737309D9824A3D1A95F00FFD78C86676038FC5A730D6782561E8732A73CA8561E6D08A3CBEF78BF2B636078C2217B81856F57EAB703AEEC3270F49772C3E3688BF86AB384A3AC4565F7CC205D68DB2D292BC42BA26EED41A398B472633F058BA664A326F1F6AFCCAB4EB4C6FD1AE6C9A3FEF2841771D67FDA336F5B400D3E3";
        path = request.getRequestURI();
        sessionMap.put("mykey", "sso");
        System.out.println("Path in SSOoooo====" + path);
        response_string = request.getParameter("string");
        System.out.println("string=" + response_string);
        // String url = "https://parichay.staging.nic.in:8080/Accounts/openam/login/validateClientToken/" + response_string + "/efromsCO";
        //String url = "https://parichay.nic.in/Accounts/openam/login/validateClientToken/" + response_string + "/eforms";
        // String output = HTTP_URL_Response(url);
        //System.out.println(""+output);

        if (response_string == null) {
            System.out.println("inside response string...");
            //res.sendRedirect(res.encodeRedirectURL("https://parichay.staging.nic.in:8080/Accounts/Services?service=efromsCO"));
            //  res.sendRedirect(res.encodeRedirectURL("https://parichay.nic.in/Accounts/Services?service=eforms"));//fully commented dont open

            return "fetchresponseString";
            //   response_string="9FE7400797FCFB4FA0540401802B1878D62924524BD28EAB0F9FA33EC94F6079492496EDA812C25E5AD02CE467F7069004E677A9EA3997BCD253A46725A922B2AD923CE2009BDE4263799D8EDC30597998F0182B0D0289B3D1BC5F4F2E0FFEE2C8B5C0A31C10E2720C30E682FB9071272A06CA26E770EDCDAEB761882D2AAFA4456C432ED23086400B11E74AB0854FED88F5FFB9225DB283739DA32F512DE573E41D94EF9F7DEB88EDAD858EB740CA74ECC6C95B85FB19CB3C90EC49D72B20B17F5599B03816441F9B920596A99797703A2D913EA9CEDF31492CAED89EE51C1538AAD66FAD4FCAF404265841321FAF85898955FBB77396E90314D5FA4F1C4D048644AAEBF8C873B473EB56A6791318C179520EB28BFF06B16B5A6CAF435A59BB6AD90778890C4F1D9986AB86A5CEB283FA4241A0FBEC82A9794185CA7FF98D82F1AA28432FCC1964E0BC430F0FEAA5947A2A9C830122A85233FAF477516D6204ECD7504A540E9C353EE0F1C7CF27493F76F494C3C95A571347C359F79B5D0AA370684FE051071E940D5C0671552FEAF43D6FB442D5AE9941B4BAB1C48888E9529363F524B9947F4DF6957AEF4511A3E8FB648FC58213D89E24ED68A391CD69B38737F883D582828FBA8B2A650367A6A177F794EBE340C77935AEC3F71F28931E7FA66E55F5AC77FFACD01A694F4A6AF44FB9356820D4EB225566CAEA8E951BA6EDD22B2DB63061B0853F78324399753380BD0BC7A5BA53C3CB7CC7115B62CF5A09FD1C3A04FF8959EF127D19C1FCE5141B58E6BE8EA1F4A8E679E6B3BFEA9A3027247F31A9EF2DD1AEF1BA0B0F453A7A8A11DA6517D8B98E880BE827894C153CC0E541A147EDA1E21DCB4D7BA2AC0076C68011AE8B82E4996FCA0AA06F31FCF36895A6FDDB89F5DD7D06E6B19D981F773BFE7795A05CD725FA5843EC20786D81ED0723CE7413ED76150D95B93880ABB7BCEBAD67679DD168D81B49E5250A99507D287E24C6DE4764E45A631800C5E09F302800494758922820169A231D31F62F0224FFF8D89659FA19D75361ADEC74D68C213DCE99FE77DE2CC8E5713540C5A4A2F929E3A526D1DD2B1B2E26D1D8F4D8CFD1B94588335BDE225E69F75ADCD02771737309D9824A3D1A95F00FFD78C86689C676928B8BE430BCDDDB7893B35AD61E79CE04C4C098BD717B01C94FAF052E41DB88B18E041B7CB44AFC2DDA9F64357F838EDA53525007682EFB37FD9392ACCECC25FF8AEB9F6252196971EA2C3837";
        }

        try {
            //userJSONDec = SSODecodeString.decyText(response_string, api_key);
            try {
                userJSONDec = SSODecodeString.decyText(response_string, api_key);
                if (userJSONDec == null || userJSONDec.isEmpty()) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "failure parichay as userJSONDec is blank ..redirected");
                    return "tempered";
                }
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "failure parichay as userJSONDec is blank TEMPERED ..redirected");
                return "tempered";
            }
            //System.out.println(userJSONDec);

        } catch (Exception ex) {
            Logger.getLogger(SsoAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!userJSONDec.isEmpty()) {
            Gson g = new Gson();
            person = g.fromJson(userJSONDec, Person.class);
            System.out.println(person.getLocalTokenId());
            System.out.println("Request inside sso====" + request.getParameter("string"));

            localTokenId = person.getLocalTokenId();

            sessionMap.put("string", userJSONDec);
            String uemail = person.getEmail();
            uid = person.getEmail();
            browserId = person.getBrowserId();
            userName = person.getUserName();
            sessionId = person.getSessionId();
            mobileNo = person.getMobileNo();

            userValues.setEmail(person.getUserName());
            userValues.setMobile(mobileNo);

            //added for sessionout
//               boolean sessionout=false;
//            try {
//         
//                sessionout=db.fetchLoginDetailsSsoCallback(userValues.getEmail());
//                
//                if(sessionout){
//                    System.out.println("Sessionout parichay");
//                    
//                    db.updateLogoutDetailsForcefully(userValues.getEmail(),"logged out forcefully as sessionout");
//                  
//                    return "ssosessionout";
//                }
//            } catch (ParseException ex) {
//                 System.out.println("Sessionout parichay ----"+ex.getMessage());
//                Logger.getLogger(SsoAuth.class.getName()).log(Level.SEVERE, null, ex);
//            }
            boolean sessionout = false;
            Map<String, Object> personDetails = db.fetchFromParichayDataStorage(person.getEmail());
            boolean flag = false;
            if (!personDetails.isEmpty()) {
                flag = true;
                String oldLocaltokenId = personDetails.get("localTokenId").toString();
                String oldBrowserId = personDetails.get("browserId").toString();
                String oldSessionId = personDetails.get("sessionId").toString();
                //String url = "http://10.122.34.117:8081/Accounts/openam/login/isTokenValid?localTokenId=" + oldLocaltokenId + "&userName=" + userName + "&service=efromsCO&browserId=" + oldBrowserId + "&sessionId=" + oldSessionId;
                //String url = "https://parichay.pp.nic.in:8081/Accounts/openam/login/isTokenValid?localTokenId=" + oldLocaltokenId + "&userName=" + userName + "&service=eforms&browserId=" + oldBrowserId + "&sessionId=" + oldSessionId;              
                String url = "https://parichay.nic.in:8081/Accounts/openam/login/isTokenValid?localTokenId=" + oldLocaltokenId + "&userName=" + userName + "&service=eforms&browserId=" + oldBrowserId + "&sessionId=" + oldSessionId;
                System.out.println(url);
                String oldOutput = HTTP_URL_Response(url);
                Person oldPerson = g.fromJson(oldOutput, Person.class);
                String oldStatus = oldPerson.getStatus();
                String oldTokenValid = oldPerson.getTokenValid();

                if ((oldStatus.equalsIgnoreCase("success")) && (oldTokenValid.equalsIgnoreCase("true"))) {
                    try {
                        sessionout = db.fetchLoginDetailsSsoCallback(userValues.getEmail());
                        if (sessionout) {
                            System.out.println("Sessionout parichay");
                            db.updateLogoutDetailsForcefully(userValues.getEmail(), "logged out forcefully as sessionout");
                            return "ssosessionout";
                        }
                    } catch (ParseException ex) {
                        System.out.println("Sessionout parichay ----" + ex.getMessage());
                        Logger.getLogger(SsoAuth.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //Insert into parichay_data_storage
            int parichayInsert = db.insertParichayDataStorage(person);
            if (parichayInsert > 0) {
                System.out.println("Inserted into parichay table " + person.getEmail());
            }

            //FINISHED SSO SESSION CHECK
            if (!flag) {
                //Testcase check status
                //String url1 = "http://10.122.34.117:8081/Accounts/openam/login/isTokenValid?localTokenId=" + localTokenId + "&userName=" + userName + "&service=efromsCO&browserId=" + browserId + "&sessionId=" + sessionId;
                String url1 = "https://parichay.nic.in:8081/Accounts/openam/login/isTokenValid?localTokenId=" + localTokenId + "&userName=" + userName + "&service=eforms&browserId=" + browserId + "&sessionId=" + sessionId;
                //String url1 = "https://parichay.pp.nic.in:8081/Accounts/openam/login/isTokenValid?localTokenId=" + localTokenId + "&userName=" + userName + "&service=eforms&browserId=" + browserId + "&sessionId=" + sessionId;
                System.out.println(url1);
                String output = HTTP_URL_Response(url1);
                System.out.println("interceptor response from 10.122.34.117:8081" + output);
                Gson gs = new Gson();
                Person person = gs.fromJson(output, Person.class);
                String status = person.getStatus();
                String tokenValid = person.getTokenValid();

                if ((status.equalsIgnoreCase("failure")) && (tokenValid.equalsIgnoreCase("false"))) {
                    sessionMap.clear();
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "failure parichay as status false ..redirected" + userValues.isIsMobileInLdap());

//                  HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
//                  System.out.println("status is -->>-->> failure redirected to url "+ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) );
//                  res.sendRedirect("https://parichay.staging.nic.in:8080/Accounts/Services?service=efromsCO");
//                   //res.sendRedirect("https://parichay.nic.in/Accounts/Services?service=eforms");
                    return "failedredirect";
                }
            }

            //End of TestCase check status
            System.out.println(userJSONDec);

            userValues.setIsEmailValidated(true);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "If user does not belong to LDAP::::");
            Set<String> aliases = ldap.fetchAliases(userValues.getEmail());

            Set<String> aliasesNew = aliases.stream().collect(Collectors.toSet());  ////  why this line is here an object can be assigned directly into another

            for (String aliase : aliasesNew) {
                if (aliase.contains("+") || !aliase.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                    aliases.remove(aliase);
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "aliases::::" + aliases);

            userValues.setAliases(aliases);

            int sizeOfAliases = aliases.size();
            String commaSeparatedAliases = "";
            if (sizeOfAliases > 1) {
                for (String email : aliases) {
                    commaSeparatedAliases += "'" + email + "',";
                }
                commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
                userValues.setAliasesInString(commaSeparatedAliases);
            } else if (sizeOfAliases == 1) {
                userValues.setAliasesInString("'" + aliases.iterator().next() + "'");
            }

            for (String email : aliases) {
                if (!email.isEmpty()) {
                    if (!db.isNewUser(email)) {
                        userValues.setIsNewUser(false);
                        break;
                    }
                }
            }

            System.out.println("user is new or old:::::::::::##########" + userValues.isIsNewUser());

            sessionMap.put("email", userValues.getEmail());

        } else {
            sessionMap.put("error", "invalid response");
        }
        //verifyotp skipped and only setters use
        UserData user = null;
        boolean isRoleAssigned = false;
        if (sessionMap.get("uservalues") != null) {
            user = (UserData) sessionMap.get("uservalues");
            isRoleAssigned = (boolean) sessionMap.get("roleAssigned");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "USER From SESSION ::" + user);
        }

        assignRoles(userValues.getEmail(), userValues.isIsEmailValidated(), userValues.isIsNewUser(), userValues.getMobile(), userValues.getAliases());

        //testing
        // assignRoles(userValues.getEmail(), userValues.isIsEmailValidated(), userValues.isIsNewUser());
        //eo testing 
        if (userValues != null && userValues.getRoles() != null) {
            ArrayList roless = (ArrayList) userValues.getRoles();
            LinkedList<String> roleLL = new LinkedList<String>();

            if (roless.contains("admin")) {
                roleLL.add("admin");
            }
            if (roless.contains("co")) {
                roleLL.add("co");
            }
            if (roless.contains("sup")) {
                roleLL.add("sup");
            }
            if (roless.contains("ca")) {
                roleLL.add("ca");
            }
            String topRole = "";
            if (roleLL != null && roleLL.size() > 0) {
                int i = 1;
                for (Object rol : roleLL) {
                    // fetch the counts for each role and save in session variables
                    if (i == 1) {
                        topRole = rol.toString();
                        sessionMap.put("toprole", topRole);
                        // start, code added by pr on 5thsep19
                        // call the counts function for the top role in the main thread
                        UserData userdata = (UserData) sessionMap.get("uservalues");
                        Set aliasSet = (Set) userdata.getAliases();
                        ArrayList<String> newAr = new ArrayList<>();
                        newAr.addAll(aliasSet);
                        ArrayList email = newAr;
                        HashMap finalMap = null;
                        ArrayList filterForms = null;
                        // set the session values based on role
                        //settingSessionCount(role, email, sessionMap);
                        finalMap = (HashMap) signUpService.settingSessionCount(topRole, email);
                        if (finalMap != null) {
                            if (finalMap.get("counts") != null) {
                                DAOGotMap = (HashMap) finalMap.get("counts");
                            }
                            if (finalMap.get("forms") != null) {
                                DAOGotFFMap = (HashMap) finalMap.get("forms");
                            }
                        }
                        sessionMap.putAll(DAOGotMap);
                        sessionMap.putAll(DAOGotFFMap);
                        sessionMap.put("co_list_show", coListData());
                        sessionMap.put("showDnsTrackerLink", findValidUser());
                        System.out.println("co_list_show:::: " + coListData());
                        System.out.println(" Main thread inside run for role  " + topRole + " DAOGotMap values are " + DAOGotMap + " DAOGotFFMap got from dao is " + DAOGotFFMap + " sessionMap values are " + sessionMap);
                        // end, code added by pr on 5thsep19                                                
                    } else {
                        System.out.println(" roless size is greater than 0 rol value is " + rol + " nto dashboard ");
                        setRoleCountSession(rol.toString());

                    }
                    //}

                    i++;
                }
            }
        }

        return SUCCESS;
    }
    public boolean status;

    String HTTP_URL_Response(String http_url) {
        String line = "";

        try {

            System.out.println("SOUT " + http_url);
            HttpURLConnection huc = (HttpURLConnection) new URL(http_url).openConnection();
            //HttpURLConnection.setFollowRedirects(false);
            huc.setConnectTimeout(2 * 1000);
            huc.connect();

            try (InputStream response2 = huc.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response2));
                for (String tmp; (tmp = reader.readLine()) != null;) {
                    line = tmp;
                    System.out.println("HTTP Response " + line);
                }

                reader.close();
            } catch (Exception e) {
                System.out.println("Error In HTTTP URL");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("Error In HTTTP URL");
            e.printStackTrace();
        }

        return line;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.sessionMap = (SessionMap<String, Object>) session;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    //private Classes from loginAction
    private String coListData() {
        Connection conSlave = null;
        UserData userdata = (UserData) sessionMap.get("uservalues");
        String aliasescomma = userdata.getAliasesInString();
        //List<String> coListData = new ArrayList<>();

        PreparedStatement psco = null;
        ResultSet rsco = null;
        if (userdata.isIsNICEmployee()) {
            return "Data Found";
        } else {
            try {
                String sql = "SELECT count(DISTINCT `emp_coord_email`) FROM `employment_coordinator` WHERE emp_coord_email IN (" + aliasescomma + ")";
                conSlave = DbConnection.getSlaveConnection(); //29dec2021();
                psco = conSlave.prepareStatement(sql);
                rsco = psco.executeQuery();

                if (rsco.next()) {
                    if (rsco.getInt(1) > 0) {
                        return "Data Found";
                    } else {
                        psco = null;
                        rsco = null;
                        sql = "SELECT * FROM `coordinator_list_status` WHERE email_id IN (" + aliasescomma + ") AND `status` = 'a'";
                        conSlave = DbConnection.getSlaveConnection(); //29dec2021
                        psco = conSlave.prepareStatement(sql);
                        rsco = psco.executeQuery();

                        if (rsco.next()) {
                            if (rsco.getString("allow_ip") == null || rsco.getString("allow_ip") == ip || rsco.getString("allow_ip").equals("")) {
                                return "Data Found";
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Data Not Found";
        }
    }

    public void setRoleCountSession(String role) {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                + " ==  inside setRoleCountSession function ");

        //ArrayList email = null;
        if (DAOGotMap != null) {
            DAOGotMap.clear();
        }

        if (DAOGotFFMap != null) {
            DAOGotFFMap.clear();
        }

        UserData userdata = (UserData) sessionMap.get("uservalues");

        Set s = (Set) userdata.getAliases();
        System.out.println(" inside getEmailEquivalent get alias not null intialize set value is  " + s);
        ArrayList<String> newAr = new ArrayList<>();
        newAr.addAll(s);
        ArrayList email = newAr;

        System.out.println(" sessionmap values before calling run method " + sessionMap);

        try {

            int delay = 1; //msecs
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                public void run() {

                    HashMap finalMap = null;

                    ArrayList filterForms = null;

                    // set the session values based on role
                    //settingSessionCount(role, email, sessionMap);
                    System.out.println(" role is " + role + " email values are " + email);

                    //DAOGotMap = signUpService.settingSessionCount(role, email); 
                    finalMap = (HashMap) signUpService.settingSessionCount(role, email);

                    System.out.println(" finalMap got for role " + role + " is " + finalMap);

                    if (finalMap != null) {
                        if (finalMap.get("counts") != null) {
                            DAOGotMap = (HashMap) finalMap.get("counts");
                        }

                        if (finalMap.get("forms") != null) {
                            //filterForms = (ArrayList)  finalMap.get("forms");

                            DAOGotFFMap = (HashMap) finalMap.get("forms");

                        }
                    }

                    System.out.println(" inside run for role  " + role + " DAOGotMap values are " + DAOGotMap + " DAOGotFFMap value got from dao is " + DAOGotFFMap);

                    sessionMap.putAll(DAOGotMap);

                    sessionMap.putAll(DAOGotFFMap);

                    System.out.println(" inside run for role  " + role + " DAOGotMap values are " + DAOGotMap + " sessionMap values are " + sessionMap);

                }

            }, delay);

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAliasSingle catch method exception is " + e.getMessage());

            e.printStackTrace();
        }

        System.out.println(" after TRY after settingadminvalue  session values are below : sessionMap values are " + sessionMap);

        for (String key : sessionMap.keySet()) {
            System.out.println(" 2 key is " + key + " value is " + sessionMap.get(key));
        }

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside setRoleCountSession function ");

    }

    public Map<String, Object> userValuesFromLdap;
    public Map<String, Object> profile_values;

    public String assignRoles(String Email, boolean isIsEmailValidated, boolean isIsNewUser, String mobile, Set<String> aliases) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles::::");

        Map<String, String> map = new HashMap<>();
        Set<String> roles = new HashSet<>();
        List<String> rolesList = new ArrayList<>();
        List<String> rolesListService = new ArrayList<>(); // 22ndoct19
        Map<String, Object> hashMap = new HashMap<>();
        String rolesInString = "";
        ArrayList<String> employment_data = new ArrayList<>();
        userValues.setEmail(Email);
        userValues.setIsEmailValidated(isIsEmailValidated);
        userValues.setIsNewUser(isIsNewUser);
        userValues.setAliases(aliases);
        System.out.println("userValues.getEmail()" + userValues.getEmail());
        if (userValues.isIsEmailValidated()) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles user is authenticated from ldap::::");
            // THIS MAP WILL HAVE ONLY 3 KEYS LIKE bo, isUserNicEmployee and uid
            map = ldap.isUserNICEmployee(userValues.getEmail());

//            aliases = new HashSet<>();
//            if (userValues.isIsEmailValidated()) {
//                aliases = (Set<String>) getUserValuesFromLdap().get("aliases");
//            }
//            aliases.add(userValues.getEmail());   // added to verify for both mail and mail aliases
//            Set<String> aliasesNew = aliases.stream().collect(Collectors.toSet());
//            for (String aliase : aliasesNew) {
//                if (aliase.contains("+") || !aliase.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
//                    aliases.remove(aliase);
//                }
//            }
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles aliases are::::" + aliases);
//            userValues.setAliases(aliases);
//            int sizeOfAliases = aliases.size();
//            String commaSeparatedAliases = "";
//            if (sizeOfAliases > 1) {
//                for (String email : aliases) {
//                    commaSeparatedAliases += "'" + email + "',";
//                }
//                commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
//                userValues.setAliasesInString(commaSeparatedAliases);
//            } else if (sizeOfAliases == 1) {
//                userValues.setAliasesInString("'" + aliases.iterator().next() + "'");
//            }
            userValues.setUserBo(map.get("bo"));
            if (map.get("isNICEmployee").equals("yes")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles if user is nic employee::::");
                userValues.setIsNICEmployee(true);

//                for (String email : aliases) {
//                    if (!email.isEmpty()) {
//                        if (!db.isNewUser(email)) {
//                            userValues.setIsNewUser(false);
//                            break;
//                        }
//                    }
//                }
                userValues.setUid(map.get("uid"));
                if (userValues.isIsNewUser()) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles NEW USER::::");
                    map.clear();
                    map = db.fetchUserValuesFromOAD(userValues.getUid()); //Fetching user  values from RO table

                    if (map.size() > 0) {
                        userValues.setIsInRoTable(true);
                        initializeUserFromOAD(map);
                    } else {
                        System.out.println("if doesn not exist in OAD");
                        userValues.setIsInRoTable(false);
                        // Fetching complete details from LDAP and setting userValuesFromLdap bean
                        setUserValuesFromLdap(ldap.fetchLdapValues(userValues.getEmail()));
                        initializeUserFromLDAP();
                    }
                } else {
                    //String mob = userValues.getMobile();
                    // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles OLD USER::::");
                    userValues.setIsNewUser(false);
                    map.clear();
                    
                    for (String email : aliases) {
                        map = db.fetchUserValues(email, userValues.getMobile());
                        if (map.size() > 0) {
                            profile_values = (HashMap) signUpService.getUserValues(email, userValues.isIsEmailValidated(), userValues.isIsNICEmployee());
                            profile_values.put("mobile", mobile);
                            initializeUserByProfile(map);
                            userValues.setMobile(mobile);
                            employment_data = db.empCoordData((HashMap) map);
                            break;
                        }
                    }
                    
                    if (!mobile.contains("+")) {
                        userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
                    }
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles rolesInString::::" + rolesInString);
                }

                if (signUpService.isHog(userValues.getEmail())) {
                    userValues.setIsHOG(true);
                } else {
                    userValues.setIsHOG(false);
                }

                if (signUpService.isHod(userValues.getEmail())) {
                    userValues.setIsHOD(true);
                } else {
                    userValues.setIsHOD(false);
                }
                userValues.setHodEmail(db.fetchRo(userValues.getAliases()));
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles if user is not nic employee::::");
                userValues.setUid(map.get("uid"));
                boolean existEmail = true;
                userValues.setIsNICEmployee(false);

//                for (String email : aliases) {
//                    existEmail = db.isNewUser(email);
//                    if (!existEmail) {
//                        break;
//                    }
//                }
                if (userValues.isIsNewUser()) {
                    // Fetching complete details from LDAP and setting userValuesFromLdap bean
                    setUserValuesFromLdap(ldap.fetchLdapValues(userValues.getEmail()));
                    initializeUserFromLDAP();
                } //                if (existEmail) {
                //                    initializeUserFromLDAP();
                //                } 
                else {
                    userValues.setIsNewUser(false);
                    for (String email : aliases) {
                        map = db.fetchUserValues(email, userValues.getMobile());
                        if (map.size() > 0) {
                            profile_values = (HashMap) signUpService.getUserValues(email, userValues.isIsEmailValidated(), userValues.isIsNICEmployee());
                            profile_values.put("mobile", mobile);
                            initializeUserByProfile(map);
                            userValues.setMobile(mobile);
                            employment_data = db.empCoordData((HashMap) map);
                            break;
                        }
                    }
                }
            }

            hashMap = getRoles(userValues.getAliases());
            rolesInString = (String) hashMap.get("rolesInString");
            rolesList = (List<String>) hashMap.get("rolesInList");

            System.out.println("Roles : " + rolesInString);
            userValues.setRole(rolesInString);
            //rolesList.addAll(listOfString);
            userValues.setRoles(rolesList);
        } 
        userValues.setRolesAlreadyAssigned(true);
        userValues.setFormsAllowed(db.fetchAllowedFormsForAdmins(userValues.getAliases()));
        userValues.setPreviousRoleAtLogin(userValues.getRole());
        userValues.setPreviousRolesAtLogin(userValues.getRoles());
        userValues.setPreviousRolesServiceAtLogin(userValues.getRolesService());
        rolesInString = userValues.getRole();
        if (rolesInString.contains("sup") || rolesInString.contains("admin")) {
            userValues.setIndividualEmail(db.fetchIndividualEmailForSupporOrAdmin(userValues.getCountryCode() + userValues.getMobile()));
        }
        
        String comingFromUpdateMobile = "";
        if (sessionMap.get("comingFromUpdateMobile") == null) {

            String login_status = "Authentication Sucessfull";
            db.insertIntoAuditTable(userValues.getEmail(), userValues.getRole(), login_status);

        } else {
            comingFromUpdateMobile = "updateMobile";

        }
        String exist_in_kavach = "";
        if (sessionMap.get("exist_in_kavach") != null && sessionMap.get("exist_in_kavach").equals("true")) {
            exist_in_kavach = "true";
        }
        sessionMap.put("isGov", userValues.isIsEmailValidated());
        sessionMap.get("keyval");
        sessionMap.put("email", "");
        //sessionMap.clear();
        sessionMap.put("roleAssigned", userValues.isRolesAlreadyAssigned());
        sessionMap.put("uservalues", userValues);
        sessionMap.put("login", userValues.getEmail());
        sessionMap.put("whichModule", "online");
        sessionMap.put("update_without_oldmobile", "no");
        sessionMap.put("path", path);
        sessionMap.put("str", response_string);
        sessionMap.put("loginType", "sso");
        sessionMap.put("str", response_string);
        sessionMap.put("myIdSso", "sso");
        
        if (comingFromUpdateMobile.equals("updateMobile")) {
            sessionMap.put("comingFromUpdateMobile", comingFromUpdateMobile);
        }
        if (exist_in_kavach.equals("true")) {
            sessionMap.put("exist_in_kavach", exist_in_kavach);
        }

        System.out.println("inside login action profile_values" + profile_values);
        sessionMap.put("profile-values", profile_values);
        sessionMap.put("localTokenId", localTokenId);
        sessionMap.put("uid", uid);
        sessionMap.put("browserId", browserId);
        sessionMap.put("userName", userName);
        sessionMap.put("sessionId", sessionId);
        sessionMap.put("login_ip",ip);
        String co_data = "";
        Map co = new HashMap();
        int co_counter = 0;
        System.out.println("Counter:::: " + co_counter);
        for (String e : employment_data) {
            if (co_counter < 4) {
                co.clear();
                co = signUpService.getLdapValues(e);
                if (e.equals("support@nic.in") || e.equals("support@gov.in") || e.equals("smssupport@gov.in") || e.equals("support@nkn.in") || e.equals("smssupport@nic.in") || e.equals("vpnsupport@nic.in")) {
                    if (co_data.equals("")) {
                        co_data = "to Service Desk:&ensp;" + " (servicedesk.nic.in - <b>1800-111-555</b>)";
                    } else {
                        co_data = " or " + "to Service Desk:&ensp;" + " (servicedesk.nic.in - <b>1800-111-555</b>)";
                    }
                } else if (userValues.getUserOfficialData().getDepartment() != null && userValues.getUserOfficialData().getDepartment().toLowerCase().contains("nic")) {
                    co_data = "to Service Desk:&ensp;" + " (servicedesk.nic.in - <b>1800-111-555</b>)";
                } else if (co_data.equals("")) {
                    co_data = "your Coordinator/DA:&ensp;" + co.get("cn").toString() + " (" + e + " - " + co.get("ophone").toString() + ")";
                } else {
                    if (co_counter == 1 && co_counter < 2) {
                        co_data += ", ";
                    }
                    co_data += " or " + co.get("cn").toString() + " (" + e + " - " + co.get("ophone").toString() + ")";
                }
            } else {
                break;
            }
            co_counter++;
        }
        System.out.println("com.org.controller.LoginAction.assignRoles()::::::::::: " + co_data);
        if (co_data.equals("")) {
            co_data = "to Service Desk:&ensp;" + " (servicedesk.nic.in - <b>1800-111-555</b>)";
        }
        sessionMap.put("employment_data", co_data);
        System.out.println("UID###############" + userValues.getUid());
        return SUCCESS;

    }

    //initialization
    private void initializeUserFromOAD(Map<String, String> map) {
        Map<String, Object> hashMap = new HashMap<>();
        List<String> rolesList = new ArrayList<>();
        String rolesInString = "";
        userValues.setIsNewUser(true);
        userValues.setEmail(userValues.getEmail());
        if (!map.get("mobile").equals("")) {
            userValues.setMobile(map.get("mobile"));
        }
        userValues.setName(map.get("name"));
        userValues.setTelephone(map.get("ophone"));
        userValues.getUserOfficialData().setDesignation(map.get("designation"));
        userValues.getUserOfficialData().setEmployeeCode(map.get("emp_code"));
        userValues.getUserOfficialData().setOfficeAddress(map.get("address"));
        userValues.getUserOfficialData().setPostingCity(map.get("city"));
        userValues.getUserOfficialData().setPostingState(map.get("add_state"));
        userValues.getUserOfficialData().setOfficePhone(map.get("ophone"));
        userValues.getHodData().setName(map.get("hod_name"));
        userValues.getHodData().setEmail(map.get("hod_email"));
        userValues.getHodData().setMobile(map.get("hod_mobile"));
        userValues.getHodData().setDesignation(map.get("ca_desig"));
        userValues.getHodData().setTelephone(map.get("hod_telephone"));
        userValues.getHodData().setGovEmployee(ldap.emailValidate(map.get("hod_email")));
        if (!userValues.getMobile().contains("+")) {
            userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
        }

        userValues.getImpData().setStates(db.getStates());
        userValues.getUserOfficialData().setOrganizationCategory(map.get("employment"));
        userValues.getUserOfficialData().setMinistry(map.get("ministry"));
        userValues.getUserOfficialData().setDepartment("department");

    }

    private void initializeUserFromLDAP() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "initializeUserFromLDAP");

        Map<String, Object> hashMap = new HashMap<>();
        List<String> rolesList = new ArrayList<>();
        String rolesInString = "";
        userValues.setAuthenticated(true);
        userValues.setIsEmailValidated(true);
        userValues.setIsNewUser(true);
        userValues.setEmail((String) userValuesFromLdap.get("email"));
        if (userValuesFromLdap.get("email").equals("support@nic.in") == false && userValuesFromLdap.get("email").equals("support@gov.in") == false && userValuesFromLdap.get("email").equals("support@nkn.in") == false && userValuesFromLdap.get("email").equals("smssupport@gov.in") == false && userValuesFromLdap.get("email").equals("smssupport@nic.in") == false && userValuesFromLdap.get("email").equals("vpnsupport@nic.in") == false) {
            userValues.setMobile((String) userValuesFromLdap.get("mobile"));
        }
        userValues.setName((String) userValuesFromLdap.get("cn"));
        userValues.getUserOfficialData().setEmployeeCode((String) userValuesFromLdap.get("emp_code"));
        userValues.getUserOfficialData().setDesignation((String) userValuesFromLdap.get("desig"));
        userValues.getUserOfficialData().setOfficeAddress((String) userValuesFromLdap.get("address"));
        userValues.getUserOfficialData().setPostingCity((String) userValuesFromLdap.get("city"));
        userValues.getUserOfficialData().setPostingState((String) userValuesFromLdap.get("state"));
        userValues.getUserOfficialData().setOrganizationCategory((String) userValuesFromLdap.get("employment"));
        userValues.getUserOfficialData().setMinistry((String) userValuesFromLdap.get("ministry"));
        userValues.getUserOfficialData().setDepartment((String) userValuesFromLdap.get("department"));
        userValues.getUserOfficialData().setOfficePhone((String) userValuesFromLdap.get("ophone"));
        userValues.setTelephone((String) userValuesFromLdap.get("rphone"));

        userValues.getImpData().setStates(db.getStates());
        if (!userValues.getMobile().contains("+")) {
            userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
        }

    }

    private void initializeUserByProfile(Map<String, String> map) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "initializeUserByProfile");

        Map<String, Object> hashMap = new HashMap<>();
        List<String> rolesList = new ArrayList<>();
        String rolesInString = "";
        String email = userValues.getEmail();
        userValues.setEmail(userValues.getEmail());
        userValues.setIsNewUser(false);
        if (!(email.equals("support@nic.in") || email.equals("support@gov.in") || email.equals("smssupport@gov.in") || email.equals("support@nkn.in") || email.equals("smssupport@nic.in") || email.equals("vpnsupport@nic.in"))) {
            userValues.setMobile(map.get("mobile"));
        }
        userValues.setName(map.get("name"));
        userValues.setTelephone(map.get("ophone"));
        userValues.getUserOfficialData().setDesignation(map.get("designation"));
        userValues.getUserOfficialData().setEmployeeCode(map.get("emp_code"));
        userValues.getUserOfficialData().setOfficeAddress(map.get("address"));
        userValues.getUserOfficialData().setPostingCity(map.get("city"));
        userValues.getUserOfficialData().setPostingState(map.get("add_state"));
        userValues.getUserOfficialData().setPincode(map.get("pin"));
        userValues.getUserOfficialData().setDepartment(map.get("department"));
        userValues.getUserOfficialData().setMinistry(map.get("ministry"));
        userValues.getUserOfficialData().setState(map.get("state"));
        userValues.getUserOfficialData().setOrganizationCategory(map.get("organization"));
        userValues.getUserOfficialData().setEmployment(map.get("employment"));
        userValues.getUserOfficialData().setOfficePhone(map.get("ophone"));
        userValues.getUserOfficialData().setRphone(map.get("rphone"));
        userValues.getHodData().setName(map.get("hod_name"));
        userValues.getHodData().setEmail(map.get("hod_email"));
        userValues.getHodData().setMobile(map.get("hod_mobile"));
        userValues.getHodData().setDesignation(map.get("ca_desig"));
        userValues.getHodData().setTelephone(map.get("hod_telephone"));
        userValues.getUserOfficialData().setUnder_sec_email(map.get("under_sec_email"));
        userValues.getUserOfficialData().setUnder_sec_name(map.get("under_sec_name"));
        userValues.getUserOfficialData().setUnder_sec_tel(map.get("under_sec_tel"));
        userValues.getUserOfficialData().setUnder_sec_mobile(map.get("under_sec_mobile"));
        userValues.getUserOfficialData().setUnder_sec_desig(map.get("under_sec_desig"));
        userValues.getHodData().setGovEmployee(ldap.emailValidate(map.get("hod_email")));
        userValues.getImpData().setStates(db.getStates());
        if (userValues.getMobile() != null && !userValues.getMobile().contains("+")) {
            userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
        }

    }

    public Map<String, Object> getRoles(Set<String> aliases) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "getRoles");
        long startTime = System.currentTimeMillis();
        Set<String> roles = new HashSet<>();
        List<String> rolesList = new ArrayList<>();
        List<String> rolesServiceList = new ArrayList<>();// 24thoct19
        HashMap<String, Object> hashMap = new HashMap<>();
        String rolesInString = "";
        Set<String> rolesService = new HashSet<>(); //24thoct19
      

            roles = db.getRolesForSSO(userValues.getAliasesInString(), userValues.getMobile());

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " SSOAUTH roles1 in getroles before is " + roles);
            // start, added on 24thoct19
            if (roles.contains("email_co")) {
                // add email related forms in 
                rolesService.add(Constants.SINGLE_FORM_KEYWORD);
                rolesService.add(Constants.BULK_FORM_KEYWORD);
                rolesService.add(Constants.NKN_SINGLE_FORM_KEYWORD);
                rolesService.add(Constants.NKN_BULK_FORM_KEYWORD);
                rolesService.add(Constants.GEM_FORM_KEYWORD);
                rolesService.add(Constants.IMAP_FORM_KEYWORD);
                rolesService.add(Constants.MOB_FORM_KEYWORD);
                rolesService.add(Constants.LDAP_FORM_KEYWORD);
                rolesService.add(Constants.SMS_FORM_KEYWORD);
                rolesService.add(Constants.IP_FORM_KEYWORD);
                rolesService.add(Constants.DIST_FORM_KEYWORD);
                rolesService.add(Constants.BULKDIST_FORM_KEYWORD);
                rolesService.add(Constants.DNS_FORM_KEYWORD);
                rolesService.add(Constants.WIFI_FORM_KEYWORD);
                rolesService.add(Constants.WIFI_PORT_FORM_KEYWORD);
                rolesService.add(Constants.RELAY_FORM_KEYWORD);
                rolesService.add(Constants.CENTRAL_UTM_FORM_KEYWORD);
                rolesService.add(Constants.EMAILACTIVATE_FORM_KEYWORD);
                rolesService.add(Constants.EMAILDEACTIVATE_FORM_KEYWORD);
                rolesService.add(Constants.DOR_EXT_FORM_KEYWORD);
                rolesService.add(Constants.DAONBOARDING_FORM_KEYWORD);
            }

            if (roles.contains("vpn_co")) {
                // add email related forms in 
                rolesService.add(Constants.VPN_ADD_FORM_KEYWORD);
                rolesService.add(Constants.VPN_BULK_FORM_KEYWORD);
                rolesService.add(Constants.VPN_RENEW_FORM_KEYWORD);
                rolesService.add(Constants.VPN_SINGLE_FORM_KEYWORD);
                rolesService.add(Constants.VPN_SURRENDER_FORM_KEYWORD);
            }

            // end, added on 24thoct19
           
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " SSOAUTH after alias " + userValues.getAliasesInString() + rolesService);
    System.out.println("Start the process of fetch all allowedForm(registration number) of user on the basis of role:");
        if (roles.contains("admin") || roles.contains("sup")) {
            System.out.println("check for admin, support role");
            boolean flag = false;
            for (String email : aliases) {
                boolean checkFormsAllowed = db.areFormsAllowedAsAdmin(email);
                System.out.println("checkFormsAllowed = " + checkFormsAllowed);
                if (checkFormsAllowed) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                roles.remove("admin");
                roles.remove("sup");
            }
        }
        
        int countOfVpnREquests = fetchCountForVpnRequests("", "pending", "all", aliases);
        if (countOfVpnREquests == 0 && !roles.contains("email_co")) {
            roles.remove("co");
            roles.remove("vpn_co");
        } 
        if (roles.size() > 0) {
            for (String role : roles) {
                if (rolesInString.isEmpty()) {
                    rolesInString = role;
                } else {
                    rolesInString += "," + role;
                }
            }
        }
        rolesList.addAll(roles);
   //System.out.println(" rolesService before putting in arraylist is " + rolesService);
        rolesServiceList.addAll(rolesService);// 24thoct19

        hashMap.put("rolesInString", rolesInString);
        hashMap.put("rolesInList", rolesList);
        hashMap.put("rolesServiceInList", rolesServiceList);

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  SSOAUTH rolesInString = " + rolesInString+ " SSOAUTH rolesServiceInList = " + rolesServiceList+ " SSOAUTH rolesInList = " + rolesList);
        List<String> rolesListService = new ArrayList<>();
        rolesListService = (List<String>) hashMap.get("rolesServiceInList");
        userValues.setRolesService(rolesListService);

        // end, added on 24thoct19        
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside get roles getRoles rolesInString" + rolesInString + " rolesServiceList " + rolesServiceList);
        long endTime = System.currentTimeMillis();
        //System.out.println("TOTAL TIME  TAKEN : " + (endTime - startTime));
        System.out.println("End the process of fetch allowedForm based on role assigned:");
        return hashMap;
    }

    public String findValidUser() {
        String flag = "notshowlink";
        Connection conSlave = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserData userdata = (UserData) sessionMap.get("uservalues");
        String aliasescomma = userdata.getAliasesInString();
        System.out.println("com.org.dao.DnsTeamDao.findValidUser() LoginAliases:::: " + aliasescomma);
        try {
            conSlave = DbConnection.getSlaveConnection();
            String sql = "SELECT count(*) FROM domain_tracker WHERE authorize_email IN(" + aliasescomma + ") and allow_domain_tracker = 'y'";
            ps = conSlave.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    flag = "showlink";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("com.org.controller.LoginAction.findValidUser() FLAG::: " + flag);
        return flag;
    }

    public String myurl(String request) throws MalformedURLException, IOException {
        URL url = new URL(request);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * @return the userValuesFromLdap
     */
    public Map<String, Object> getUserValuesFromLdap() {
        return userValuesFromLdap;
    }

    /**
     * @param userValuesFromLdap the userValuesFromLdap to set
     */
    public void setUserValuesFromLdap(Map<String, Object> userValuesFromLdap) {
        this.userValuesFromLdap = userValuesFromLdap;
    }

    public Map<String, Object> getProfile_values() {
        return profile_values;
    }

    public void setProfile_values(Map<String, Object> profile_values) {
        this.profile_values = profile_values;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }
  public int fetchCountForVpnRequests(String search, String type, String formName, Set<String> aliases) {
        long startTime = System.currentTimeMillis();
        String pendingStr = "", rejectStr = "", pendingDate = "";
        PreparedStatement ps = null;
        ResultSet res1 = null;
        String csvForms = "";
        String field = "", tot_field = "";
        String email1 = "";
        String statusString = "";
        int cnt = 0;
        pendingStr = "coordinator_pending";
        rejectStr = "coordinator_rejected";
        field = "coordinator_email";
        tot_field = "coordinator_email";
        pendingDate = "coordinator_datetime";
        Connection conSlave = null;
        ResultSet rs = null;

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT count(*) FROM final_audit_track WHERE 1 ";
            int i = 1;
            for (String email : aliases) {
                if (i == 1) {
                    qry += " and ( ";
                } else {
                    qry += " OR ";
                }
                qry += "(( status = '" + pendingStr + "' and to_email  = '" + email.toString().trim() + "' ) || (coordinator_email = '" + email.toString().trim() + "' and co_id = 0 ))";
                ++i;
            }

            List<String> aliasesAsList = new ArrayList<>(aliases);
            Set<String> coordsIds = db.fetchCoIds(aliasesAsList);
            String sb = "";
            if (!coordsIds.isEmpty()) {
                for (String coordsId : coordsIds) {
                    sb += "'" + coordsId + "',";
                }
                // 24-feb-2022  COUNT QUERY EXCEPTION You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near ')))' at line 1
                sb = sb.replaceAll("\\s*,\\s*$", "");
                qry += " or ( status = '" + pendingStr + "' and co_id in (" + sb + "))";
            }
            qry += ") and registration_no like 'vpn%'";
            
            ps = conSlave.prepareStatement(qry);
                        
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " COUNT QUERY is " + ps);
           
            res1 = ps.executeQuery();
            if (res1.next()) {
                cnt = res1.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " COUNT QUERY EXCEPTION " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getCount EXCEPTION 1: " + e.getMessage());

                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getCount EXCEPTION 2: " + e.getMessage());

                }
            }
        }
        long endTime = System.currentTimeMillis();
        //System.out.println("TOTAL TIME  TAKEN : " + (endTime - startTime));
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  end of getcount function ");
        return cnt;
    }
}
