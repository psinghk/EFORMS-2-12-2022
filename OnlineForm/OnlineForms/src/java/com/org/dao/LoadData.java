
package com.org.dao;

import com.opensymphony.xwork2.ActionSupport;
import com.org.connections.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.struts2.ServletActionContext;


public class LoadData extends ActionSupport {

    private String ip = ServletActionContext.getRequest().getRemoteAddr(); 
    Connection con = null;
    Connection conSlave = null;
    
    public LoadData() {
    }
   

    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, Object> getState() {
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList state = new ArrayList();
        ArrayList state_code = new ArrayList();
        Map<String, Object> statelist = new HashMap<String, Object>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conSlave.prepareStatement("select DISTINCT(stname) from district order by stname ASC");
            rs = ps.executeQuery();

            while (rs.next()) {
                state.add(rs.getString("stname"));
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                 if (con != null) {
                   // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        statelist.put("state", state);
        

        return statelist;
    }

}
