
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author SHIVAM
 */
public class Check {

    Connection con = null;
    private static final String dbhost = "localhost:3306";
    private static final String dbuser = "root";
    private static final String dbname = "onlineform";
    private static final String dbpass = "@Forms@123";

    private static final String dbhostSlave = "localhost:3306";
    private static final String dbuserSlave = "root";
    private static final String dbnameSlave = "onlineform";
    private static final String dbpassSlave = "@Forms@123";

    private static final String url = "jdbc:mysql://" + dbhost + "/" + dbname + "?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";

    public static void main(String[] args) {

        Check check = new Check();
        check.fetchDepartment();

    }

    public String fetchDepartment() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        Connection conSlave = null;
        String category = "";
        String min = "";
        String dept = "";
        String department = "";
        String ministry = "";
        String employment = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conSlave = (Connection) DriverManager.getConnection(url, dbuser, dbpass);
            ps = conSlave.prepareStatement("select DISTINCT(department),ministry from test1");

            rs = ps.executeQuery();
            while (rs.next()) {

                dept = rs.getString("department");
                min = rs.getString("ministry");

                ps1 = conSlave.prepareStatement("select DISTINCT(emp_dept),emp_min_state_org,emp_category from employment_coordinator where emp_dept like ?");
                ps1.setString(1, "%"+dept);
               
                rs1 = ps1.executeQuery();
                if (rs1.next()) {
                    
                    if (!rs1.getString("emp_min_state_org").equalsIgnoreCase(min));
                    {
                        ps = conSlave.prepareStatement("insert into test2 (ministry_profile,department_profile,ministry,department) VALUES(?,?,?,?)");
                        ps.setString(1, min);
                        ps.setString(2, dept);
                        ps.setString(3, rs1.getString("emp_min_state_org"));
                        ps.setString(4, rs1.getString("emp_dept"));
                        
                        ps.executeUpdate();

                    }

                }

            }

        } catch (Exception e) {

        }

        return "test";
    }

//     public String fetchMinistry() {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        PreparedStatement ps1 = null;
//        ResultSet rs1 = null;
//        Connection conSlave = null;
//        String category = "";
//        String min = "";
//        String dept = "";
//        String department = "";
//        String ministry = "";
//        String employment = "";
//        try {
//
//            Class.forName("com.mysql.jdbc.Driver");
//            conSlave = (Connection) DriverManager.getConnection(url, dbuser, dbpass);
//            ps = conSlave.prepareStatement("select emp_min_state_org,emp_dept,emp_category from employment_coordinator");
//            rs = ps.executeQuery();
//
//            while (rs.next()) {
//                
//                min = rs.getString("emp_min_state_org");
//                dept = rs.getString("emp_dept");
//                employment = rs.getString("emp_category");
//                /*if (employment.equals("State")) {
//                    int i = 0;
//
//                    ps1 = conSlave.prepareStatement("select DISTINCT(department),state from user_profile where state=? and department NOT IN (select DISTINCT(emp_dept) from employment_coordinator where emp_min_state_org =?) group by department");
//
//                    //ps1 = conSlave.prepareStatement("select DISTINCT(emp_dept),emp_min_state_org from employment_coordinator where emp_min_state_org =? and emp_dept NOT IN (select DISTINCT(department) from user_profile where ministry=? group by department)");
//                    ps1.setString(1, min);
//                    ps1.setString(2, min);
//
//                    System.out.println("PS1:::::::::"+ps1);
//                    rs1 = ps1.executeQuery();
//
//                    if (rs1.next()) {
//
//                        min = rs1.getString("state");
//                        dept = rs1.getString("department");
//                        i++;
//
//                    }
//
//                    if (i > 0) {
//
//
//                        ps = conSlave.prepareStatement("insert into test1 (ministry,department) VALUES(?,?)");
//                        ps.setString(1, min);
//                        ps.setString(2, dept);
//                        ps.executeUpdate();
//
//                    }
//
//                }*/
//
//                if (employment.equals("UT")) {
//                    int i = 0;
//
//                    ps1 = conSlave.prepareStatement("select DISTINCT(department),ministry from user_profile where ministry=? and department NOT IN (select DISTINCT(emp_dept) from employment_coordinator where emp_min_state_org =?) group by department");
//
//                    //ps1 = conSlave.prepareStatement("select DISTINCT(emp_dept),emp_min_state_org from employment_coordinator where emp_min_state_org =? and emp_dept NOT IN (select DISTINCT(department) from user_profile where ministry=? group by department)");
//                    ps1.setString(1, min);
//                    ps1.setString(2, min);
//
//                    System.out.println("PS1:::::::::" + ps1);
//                    rs1 = ps1.executeQuery();
//
//                    if (rs1.next()) {
//
//                        min = rs1.getString("ministry");
//                        dept = rs1.getString("department");
//                        i++;
//                        System.out.println("ministry" + ministry + "department" + department);
//                    }
//
//                    if (i > 0) {
//
//                        System.out.println("department" + dept);
//                        ps = conSlave.prepareStatement("insert into test1 (ministry,department) VALUES(?,?)");
//                        ps.setString(1, min);
//                        ps.setString(2, dept);
//                        ps.executeUpdate();
//
//                    }
//
//                }
//            }
//
//                 /*ps1 = conSlave.prepareStatement("select DISTINCT(organization) from user_profile where employment='Const' and organization NOT IN (select DISTINCT(emp_min_state_org) from employment_coordinator where emp_category ='Const') group by organization ");
//                rs1 = ps1.executeQuery();
//
//                while (rs1.next()) {
//                    ps = conSlave.prepareStatement("insert into test1 (ministry,department) VALUES(?,?)");
//                    ps.setString(1, "Const");
//                    ps.setString(2, rs1.getString("organization"));
//                    System.out.println("PS::::::" + ps);
//                    ps.executeUpdate();
//                }
//
//                ps1 = conSlave.prepareStatement("select DISTINCT(organization) from user_profile where employment='Nkn' and organization NOT IN (select DISTINCT(emp_min_state_org) from employment_coordinator where emp_category ='Nkn') group by organization");
//                rs1 = ps1.executeQuery();
//
//                while (rs1.next()) {
//                    ps = conSlave.prepareStatement("insert into test1 (ministry,department) VALUES(?,?)");
//                    ps.setString(1, "Nkn");
//                    ps.setString(2, rs1.getString("organization"));
//                    System.out.println("PS::::::" + ps);
//                    ps.executeUpdate();
//                }
//
//                ps1 = conSlave.prepareStatement("select DISTINCT(organization) from user_profile where employment='Others' and organization NOT IN (select DISTINCT(emp_min_state_org) from employment_coordinator where emp_category ='Others') group by organization");
//                rs1 = ps1.executeQuery();
//
//                while (rs1.next()) {
//                    ps = conSlave.prepareStatement("insert into test1 (ministry,department) VALUES(?,?)");
//                    ps.setString(1, "Others");
//                    ps.setString(2, rs1.getString("organization"));
//                    System.out.println("PS::::::" + ps);
//                    ps.executeUpdate();
//                }
//
//                ps1 = conSlave.prepareStatement("select DISTINCT(organization) from user_profile where employment='Project' and organization NOT IN (select DISTINCT(emp_min_state_org) from employment_coordinator where emp_category ='Project') group by organization");
//                rs1 = ps1.executeQuery();
//
//                while (rs1.next()) {
//                    ps = conSlave.prepareStatement("insert into test1 (ministry,department) VALUES(?,?)");
//                    ps.setString(1, "Project");
//                    ps.setString(2, rs1.getString("organization"));
//                    System.out.println("PS::::::" + ps);
//                    ps.executeUpdate();
//                }
//
//                ps1 = conSlave.prepareStatement("select DISTINCT(organization) from user_profile where employment='Psu' and organization NOT IN (select DISTINCT(emp_min_state_org) from employment_coordinator where emp_category ='Psu') group by organization");
//                rs1 = ps1.executeQuery();
//
//                while (rs1.next()) {
//                    ps = conSlave.prepareStatement("insert into test1 (ministry,department) VALUES(?,?)");
//                    ps.setString(1, "Psu");
//                    ps.setString(2, rs1.getString("organization"));
//                    System.out.println("PS::::::" + ps);
//                    ps.executeUpdate();
//                }*/
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//
//
//        }
//
//        return "test";
//
//    }
//    public String fetchMinistry() {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        PreparedStatement ps1 = null;
//        ResultSet rs1 = null;
//        Connection conSlave = null;
//        String category = "";
//        String min = "";
//        String dept = "";
//        String department = "";
//        String ministry = "";
//        String employment = "";
//        try {
//
//            Class.forName("com.mysql.jdbc.Driver");
//            conSlave = (Connection) DriverManager.getConnection(url, dbuser, dbpass);
//            ps = conSlave.prepareStatement("select emp_min_state_org,emp_dept,emp_category from employment_coordinator");
//            rs = ps.executeQuery();
//
//            while (rs.next()) {
//
//                min = rs.getString("emp_min_state_org");
//                dept = rs.getString("emp_dept");
//                employment = rs.getString("emp_category");
////                if (employment.equals("Central")) {
//                int i = 0;
//
//                ps1 = conSlave.prepareStatement("select DISTINCT(department) from user_profile where ministry=? and department NOT IN (select DISTINCT(emp_dept),emp_min_state_org from employment_coordinator where emp_min_state_org =?) group by department");
//
//                //ps1 = conSlave.prepareStatement("select DISTINCT(emp_dept),emp_min_state_org from employment_coordinator where emp_min_state_org =? and emp_dept NOT IN (select DISTINCT(department) from user_profile where ministry=? group by department)");
//                ps1.setString(1, min);
//                ps1.setString(2, min);
//                rs1 = ps1.executeQuery();
//
//                if (rs1.next()) {
//                    min = rs1.getString("emp_min_state_org");
//                    dept = rs1.getString("emp_dept");
//                    i++;
//                    //System.out.println("ministry" + ministry + "department" + department);
//                }
//
//                if (i > 0) {
//
//                    System.out.println("department" + dept);
//                    ps = conSlave.prepareStatement("insert into test1 (ministry,department) VALUES(?,?)");
//                    ps.setString(1, min);
//                    ps.setString(2, dept);
//                    ps.executeUpdate();
//
//                }
//
//            }
//
////                 if (employment.equals("State")) {
////                    int i = 0;
////                    ps1 = conSlave.prepareStatement("select DISTINCT(emp_dept),emp_min_state_org from employment_coordinator where emp_min_state_org =? and emp_dept NOT IN (select DISTINCT(department) from user_profile where state=? group by department)");
////                    ps1.setString(1, min);
////                    ps1.setString(2, min);
////                    rs1 = ps1.executeQuery();
////
////                    if (rs1.next()) {
////                        min = rs1.getString("emp_min_state_org");
////                        dept = rs1.getString("emp_dept");
////                        i++;
////                        //System.out.println("ministry" + ministry + "department" + department);
////                    }
////
////                    if (i > 0) {
////
////                        System.out.println("department" + dept);
////                        ps = conSlave.prepareStatement("insert into test (ministry,department) VALUES(?,?)");
////                        ps.setString(1, min);
////                        ps.setString(2, dept);
////                        ps.executeUpdate();
////
////                    }
////
////                }
//            //if (employment.equals("Psu")) {
//            //int i = 0;
////            ps1 = conSlave.prepareStatement("select DISTINCT(emp_min_state_org),emp_category from employment_coordinator where emp_category ='Const' and emp_min_state_org NOT IN (select DISTINCT(organization) from user_profile where employment='Const' group by organization)");
////            rs1 = ps1.executeQuery();
////
////            while (rs1.next()) {
////                ps = conSlave.prepareStatement("insert into test (ministry,department) VALUES(?,?)");
////                ps.setString(1, rs1.getString("emp_category"));
////                ps.setString(2, rs1.getString("emp_min_state_org"));
////                System.out.println("PS::::::" + ps);
////                ps.executeUpdate();
////            }
////
////            ps1 = conSlave.prepareStatement("select DISTINCT(emp_min_state_org),emp_category from employment_coordinator where emp_category ='Nkn' and emp_min_state_org NOT IN (select DISTINCT(organization) from user_profile where employment='Nkn' group by organization)");
////            rs1 = ps1.executeQuery();
////
////            while (rs1.next()) {
////                ps = conSlave.prepareStatement("insert into test (ministry,department) VALUES(?,?)");
////                ps.setString(1, rs1.getString("emp_category"));
////                ps.setString(2, rs1.getString("emp_min_state_org"));
////                System.out.println("PS::::::" + ps);
////                ps.executeUpdate();
////            }
////
////            ps1 = conSlave.prepareStatement("select DISTINCT(emp_min_state_org),emp_category from employment_coordinator where emp_category ='Others' and emp_min_state_org NOT IN (select DISTINCT(organization) from user_profile where employment='Others' group by organization)");
////            rs1 = ps1.executeQuery();
////
////            while (rs1.next()) {
////                ps = conSlave.prepareStatement("insert into test (ministry,department) VALUES(?,?)");
////                ps.setString(1, rs1.getString("emp_category"));
////                ps.setString(2, rs1.getString("emp_min_state_org"));
////                System.out.println("PS::::::" + ps);
////                ps.executeUpdate();
////            }
////
////            ps1 = conSlave.prepareStatement("select DISTINCT(emp_min_state_org),emp_category from employment_coordinator where emp_category ='Project' and emp_min_state_org NOT IN (select DISTINCT(organization) from user_profile where employment='Project' group by organization)");
////            rs1 = ps1.executeQuery();
////
////            while (rs1.next()) {
////                ps = conSlave.prepareStatement("insert into test (ministry,department) VALUES(?,?)");
////                ps.setString(1, rs1.getString("emp_category"));
////                ps.setString(2, rs1.getString("emp_min_state_org"));
////                System.out.println("PS::::::" + ps);
////                ps.executeUpdate();
////            }
////            
////             ps1 = conSlave.prepareStatement("select DISTINCT(emp_min_state_org),emp_category from employment_coordinator where emp_category ='UT' and emp_min_state_org NOT IN (select DISTINCT(organization) from user_profile where employment='UT' group by organization)");
////            rs1 = ps1.executeQuery();
////
////            while (rs1.next()) {
////                ps = conSlave.prepareStatement("insert into test (ministry,department) VALUES(?,?)");
////                ps.setString(1, rs1.getString("emp_category"));
////                ps.setString(2, rs1.getString("emp_min_state_org"));
////                System.out.println("PS::::::" + ps);
////                ps.executeUpdate();
////            }
//            //if (i > 0) {
//            //}
//            //}
//            //}
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//
//            try {
//                conSlave.close();
//            } catch (SQLException ex) {
//                Logger.getLogger(Check.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        return "test";
//
//    }
}
