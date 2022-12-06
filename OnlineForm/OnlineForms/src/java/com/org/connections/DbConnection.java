package com.org.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.struts2.ServletActionContext;

public class DbConnection {

    private static final String dbhost = ServletActionContext.getServletContext().getInitParameter("db-host");
    private static final String dbuser = ServletActionContext.getServletContext().getInitParameter("db-user");
    private static final String dbname = ServletActionContext.getServletContext().getInitParameter("db-name");
    private static final String dbpass = ServletActionContext.getServletContext().getInitParameter("db-pass");
    
    private static final String dbhostSlave = ServletActionContext.getServletContext().getInitParameter("db-host-slave");
    private static final String dbuserSlave = ServletActionContext.getServletContext().getInitParameter("db-user-slave");
    private static final String dbnameSlave = ServletActionContext.getServletContext().getInitParameter("db-name-slave");
    private static final String dbpassSlave = ServletActionContext.getServletContext().getInitParameter("db-pass-slave");

    private static Connection con = null;
    private static Connection con1 = null;
    
    private static Connection conSlave = null;
    private static Connection con1Slave = null;
    
     private static final String url = "jdbc:mysql://" + dbhost + "/" + dbname + "?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
    private static final String urlSlave = "jdbc:mysql://" + dbhostSlave + "/" + dbnameSlave + "?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
    private static final String url1 = "jdbc:mysql://" + dbhost + "/information_schema?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
    
//    private static final String url = "jdbc:mysql://" + dbhost + "/" + dbname + "?autoReconnect=true";
//    private static final String urlSlave = "jdbc:mysql://" + dbhostSlave + "/" + dbnameSlave + "?autoReconnect=true";
//    private static final String url1 = "jdbc:mysql://" + dbhost + "/information_schema?autoReconnect=true";

    private DbConnection(String url, String user, String pass) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exeption in MyConnection.java: " + e.getMessage());
        }
    }
    
     //this constructor is for slave  connection
    private DbConnection(String url, String user) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conSlave = (Connection) DriverManager.getConnection(url, user, dbpassSlave);
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exeption in MyConnection.java: " + e.getMessage());
        }
    }

    private DbConnection(String url1){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(url1, dbuser, dbpass);
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exeption in MyConnection.java: " + e.getMessage());
        }
    }
    public static Connection getConnection() throws ClassNotFoundException {

        //Class.forName("com.mysql.jdbc.Driver");
        try {
            if (con == null) {
                new DbConnection(url, dbuser, dbpass);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exeption in Con DbConnection.java.get: " + e.getMessage());
        }
        return con;
    }
    
    
    
    public static Connection getSlaveConnection() throws ClassNotFoundException {
        try {
            if (conSlave == null) {
                new DbConnection(urlSlave, dbuserSlave);
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exeption in Con Slave DbConnection.java.get: " + e.getMessage());
        }
        return conSlave;
    }

    public static Connection getConnectionForSchema() throws ClassNotFoundException {
       // Class.forName("com.mysql.jdbc.Driver");
        
        try {
            if (con1 == null) {
                new DbConnection(url1);
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exeption in Con1 DbConnection.java.get: " + e.getMessage());
            //e.printStackTrace();
        }
        return con1;
    }
}
