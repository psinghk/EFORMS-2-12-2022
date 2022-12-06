package com.org.connections;

import java.util.Base64;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.apache.struts2.ServletActionContext;

// inside new branch preeti_master_3rdsep2020
public class SingleApplicationContext {

    private final String ctxfactory = ServletActionContext.getServletContext().getInitParameter("ctx-factory");
    private final String providerurl = ServletActionContext.getServletContext().getInitParameter("provider-url");
    private final String nic_bo = ServletActionContext.getServletContext().getInitParameter("nic_bo");
    private final String binddn = ServletActionContext.getServletContext().getInitParameter("createdn");
//    private final String bindpass = ServletActionContext.getServletContext().getInitParameter("createpass");

    Base64.Decoder decoder = Base64.getDecoder();
	private final String bindpass = new String(
			decoder.decode(ServletActionContext.getServletContext().getInitParameter("createpass")));
	
    private Hashtable ht = new Hashtable();
    private static DirContext ctx = null;
    
    
    private SingleApplicationContext()
    {
        System.out.println("Inside SingleApplicationContext Constructor ...");
        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
        ht.put(Context.PROVIDER_URL, providerurl);
        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
        ht.put(Context.SECURITY_PRINCIPAL, binddn);
        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        
        try {
            ctx = new InitialDirContext(ht);
            System.out.println("CTX value : " + ctx);
        } catch (Exception e) {
            ctx = null;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception in LDAPConnection.java.get: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Final CTX : " + ctx);        
    }
    
 
    
    public static DirContext getLDAPInstance()
    {
        if( ctx == null )
        {
            new SingleApplicationContext();
        }
        
        System.out.println("SingleApplicationContext ctx value  inside getLDAPInstance is "+ctx);        
        
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
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception in SingleApplicationContext recreateInstance while closing the current connection : " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            ctx = null;
        }   
        
        return getLDAPInstance();        
    }    
}