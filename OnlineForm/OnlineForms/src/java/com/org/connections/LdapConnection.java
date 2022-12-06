package com.org.connections;

import java.util.Base64;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author Ashwini
 */
public class LdapConnection {

    private final String ctxfactory = ServletActionContext.getServletContext().getInitParameter("ctx-factory");
    private final String providerurl = ServletActionContext.getServletContext().getInitParameter("provider-url");
    private final String nic_bo = ServletActionContext.getServletContext().getInitParameter("nic_bo");
    private final String binddn = ServletActionContext.getServletContext().getInitParameter("createdn");
    //private final String bindpass = ServletActionContext.getServletContext().getInitParameter("createpass");
     Base64.Decoder decoder = Base64.getDecoder();
	private final String bindpass = new String(
			decoder.decode(ServletActionContext.getServletContext().getInitParameter("createpass")));
    private Hashtable ht = new Hashtable();
    private DirContext ctx = null;

    public DirContext getContext() throws ClassNotFoundException {
        System.out.println("Inside context ...");
        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
        ht.put(Context.PROVIDER_URL, providerurl);
        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
        ht.put(Context.SECURITY_PRINCIPAL, binddn);
        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        // ht.put(Context.PROVIDER_URL, "ldap://192.168.1.42:389 "+ "ldap://192.168.1.6:389 ");
        try {
            ctx = new InitialDirContext(ht);
            System.out.println("CTX value : " + ctx);
        } catch (Exception e) {
            ctx = null;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception in LDAPConnection.java.get: " + e.getMessage());
        }
        System.out.println("Final CTX : " + ctx);
        return ctx;
    }

    public DirContext getConnection(String userid, String password) throws ClassNotFoundException {
        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
       
        ht.put(Context.PROVIDER_URL, providerurl);
        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
        ht.put(Context.SECURITY_PRINCIPAL, userid);
        ht.put(Context.SECURITY_CREDENTIALS, password);
        try {
            ctx = new InitialDirContext(ht);
        } catch (Exception e) {
            ctx = null;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception in LDAPConnection.java.get: " + e.getMessage());
        }
        return ctx;
    }

    public void closeConnection(DirContext con) {
        try {
            ctx.close();
        } catch (NamingException e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception in LDAPConnection.java.close: " + e.getMessage());
        }
    }
}
