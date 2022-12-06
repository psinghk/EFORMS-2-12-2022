package com.org.connections;

import static com.org.connections.SingleApplicationContext.getLDAPInstance;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.apache.struts2.ServletActionContext;
import static org.apache.struts2.ServletActionContext.getServletContext;
// inside new branch preeti_master_3rdsep2020
public class SingleApplicationBindContextSlave {

    private final String ctxfactory = getServletContext().getInitParameter("ctx-factory");
    private final String providerurlSlave = getServletContext().getInitParameter("provider-url-slave");
    private final String nic_bo = getServletContext().getInitParameter("nic_bo");
//    private final String binddn = ServletActionContext.getServletContext().getInitParameter("createdn");
//    private final String bindpass = ServletActionContext.getServletContext().getInitParameter("createpass");
    
    private String binddn = getServletContext().getInitParameter("bind-dn");
    private String bindpass = getServletContext().getInitParameter("bind-pass");
    
    private Hashtable ht = new Hashtable();
    private static DirContext ctx = null;
    
    
    private SingleApplicationBindContextSlave()
    {
        System.out.println("Inside SingleApplicationBindContext Constructor ...");
        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
        ht.put(Context.PROVIDER_URL, providerurlSlave);
        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
        ht.put(Context.SECURITY_PRINCIPAL, binddn);
        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        
        try {
            ctx = new InitialDirContext(ht);
            System.out.println("CTX value : " + ctx);
        } catch (Exception e) {
            ctx = null;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception in SingleApplicationBindContext : " + e.getMessage());
        }
        System.out.println("Final CTX : " + ctx);        
    }
    
    public static DirContext getLDAPInstance()
    {
        if( ctx == null )
        {
            new SingleApplicationBindContextSlave();
        }
        
        System.out.println("SingleApplicationBindContext ctx value  inside getLDAPInstance is "+ctx);        
        
        return ctx;        
    }
    
    // below method created by pr on 2ndsep2020
    public static DirContext recreateInstance()
    {
        try
        {
            if( ctx != null )
            {
                ctx.close();
            }            
        }
        catch(Exception e)
        {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception in SingleApplicationBindContext recreateInstance while closing the current connection : " + e.getMessage());
        }
        finally
        {
            ctx = null;
        }   
        
        return getLDAPInstance();        
    }    
}