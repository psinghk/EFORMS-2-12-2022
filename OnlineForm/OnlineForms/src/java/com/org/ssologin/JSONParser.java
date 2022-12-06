/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.ssologin;

//import org.json.simple.*;

import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;




//import org.json.simple.parser.*;


/**
 *
 * @author mkumar1
 */
public class JSONParser {
    
    public String getAttribute(String jsonString, String attributeName) {

        if (!jsonString.equalsIgnoreCase("")) {
            JSONObject jo = (JSONObject) JSONSerializer.toJSON(jsonString);
            //JSONObject jo1 = (JSONObject) JSONSerializer.toJSON(jo.get("User"));
             //System.out.println("Value Of :"+jsonString+" is: "+jo);
             
            String attributeValue = jo.getString(attributeName).toString();
           
            return attributeValue;
        } else {
            return null;
        }
    }
    
    public String[] getJSONArray(String abc) {

        abc = abc.substring(1, abc.length() - 1);
        String[] test = abc.split(",");
        List<String> xyz = new ArrayList<String>();

        for (int i = 0; i < test.length; i++) {
            String string = test[i];
            string = string.substring(1, string.length() - 1);
            xyz.add(string);
        }
        String[] arr = (String[]) xyz.toArray(new String[xyz.size()]);
        return arr;
    }

    public JSONObject getJSONObject(String string) {
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(string);
        return jsonObject;

    }

}
