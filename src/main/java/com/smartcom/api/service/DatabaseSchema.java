/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.service.DatabaseSchema
 *  org.springframework.stereotype.Service
 */
package com.smartcom.api.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

@Service
public class DatabaseSchema {
    public void rechargeResponse() throws ClassNotFoundException, SQLException {
        String mysqlUrl = "jdbc:mysql://localhost/test";
        Connection con = DriverManager.getConnection(mysqlUrl, "root", "smartcom");
        Statement stmt = con.createStatement();
    }
}

