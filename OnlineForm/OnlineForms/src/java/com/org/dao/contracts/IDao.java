/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.org.dao.contracts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Nicsi
 */
public interface IDao {
    public List<Map<String,String>> select(String tableName, HashMap<String,String> hm);
    public List<Map<String,String>> select(String tableName);
    public boolean insert(String tableName, HashMap<String,String> hm);
    public boolean update(String tableName, HashMap<String,String> hm);
    public boolean delete(String tableName, HashMap<String,String> hm);
}
