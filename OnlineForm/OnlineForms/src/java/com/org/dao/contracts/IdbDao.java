/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.org.dao.contracts;

import java.sql.Connection;

/**
 *
 * @author Nicsi
 */
public interface IdbDao extends IDao{
    public Connection getConnection();
}
