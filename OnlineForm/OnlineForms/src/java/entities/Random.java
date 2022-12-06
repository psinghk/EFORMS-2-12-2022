/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.apache.struts2.ServletActionContext;
/**
 *
 * @author nikki
 */
public class Random {
 
    public int random() {
        int response = 0;
        SecureRandom rand = new SecureRandom();
        int num1 = rand.nextInt(10);
        while (num1 == 0) {
            num1 = rand.nextInt(10);
        }
        int num2 = rand.nextInt(10);
        int num3 = rand.nextInt(10);
        int num4 = rand.nextInt(10);
        int num5 = rand.nextInt(10);
        int num6 = rand.nextInt(10);
        response = num1 * 100000 + num2 * 10000 + num3 * 1000 + num4 * 100 + num5 * 10 + num6;
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "OTP generated: " + response);
        return response;
    }
    public static  String csrf_random() {
        // RANDOM NUMBER
        SecureRandom random = new SecureRandom();
        String num = new BigInteger(130, random).toString(32);        
        return num;
    }
}
