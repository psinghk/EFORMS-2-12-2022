/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author NIC
 */
public class TestDemo {
    public static void main(String[] args) throws Exception{
         
//        Set s = new TreeSet();
//        s.add("pk");
//         s.add("Dk");
//          s.add("mk");
//           s.add("Nk");
//            s.add("DSk");
//            System.out.println("Set Of Values   " + s.toString() );
        String [] s1 ={"pk@","nm@","N/A","N/A","N/A"};
        System.out.println("  print index");
        //int i;
          for (String string : s1) {
              //i=0;
             String pre= string.split("@")[1].trim().toLowerCase();
            System.out.println(" Testing values   ::"+pre);
            //i++;
        }
       //System.out.println("List Of Values   " + s.toString() );
    }
    
    
}
