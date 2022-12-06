/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.Base64;

/**
 *
 * @author Preeti
 */
public class Encrypt {
    
    public static String hidden = "";
    
    public static String decodeString(String text)
    {
        //System.out.println("CREATEUSER: " + " ds start :" + text);
        
        //Random rd = new Random();
        
        String decodedStr = text;
        
        Base64.Decoder decoder = Base64.getDecoder();
        
        if( text != null && !text.equals("") )
        {
            decodedStr = new String(decoder.decode(text));
        }
        
        //System.out.println("CREATEUSER: " + " ds end :" + text);
        
        return decodedStr;
    }

    public static String encodeString(String text)
    {
        //Random rd = new Random();
        
        String encodedStr = text;
        
        Base64.Encoder encoder = Base64.getEncoder();
        
        if( text != null && !text.equals("") )
        {        
            encodedStr = encoder.encodeToString(text.getBytes());
        }
        return encodedStr;
    } 
    
}
