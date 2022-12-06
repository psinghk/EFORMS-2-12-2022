package utility;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.security.SecureRandom;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author created by pr on 19thjan18
 */
public class ResetCSRFRandom extends ActionSupport implements SessionAware
{
    Map session;    
    
    public void setSession(Map session) {
        this.session = session;
    }
    
    // start, code added by pr on 10thjan18

    private String random;

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    // end, code added by pr on 10thjan18
        
    public String resetCSRFRandom() 
    {
       // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inisde reset random coming from separate class");
        StringBuilder sb = new StringBuilder();
        String num = "0123456789";
        SecureRandom rmh = new SecureRandom();
        for (int i = 0; i < 32; i++) {
            sb.append(num.charAt(rmh.nextInt(num.length())));
        }
        String ran = sb.toString();
        session.put("CSRFRandom", ran);
//        String regNumber = (String) session.get("ref");
//        session.put("secretKey", regNumber+ran);

       // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside resetRandom func value of random num is " + ran + " session value is " + session.get("CSRFRandom"));

        random = ran;

        return SUCCESS;
    }
}
