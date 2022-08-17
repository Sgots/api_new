/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.Meterdataresponse
 *  com.smartcom.api.repository.Meterdatarepository
 *  org.springframework.stereotype.Repository
 */
package com.smartcom.api.repository;

import com.smartcom.api.model.Meterdataresponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.springframework.stereotype.Repository;

@Repository
public class Meterdatarepository {
    Timestamp timestamp;

    public Meterdataresponse findAndUpdate(String deviceID) throws SQLException {
        String mysqlUrl = "jdbc:mysql://localhost/test";
        Connection con = DriverManager.getConnection(mysqlUrl, "root", "");
        System.out.println("Connection established......");
        String querys = "select * from meterdataresponse where  macaddress=" + deviceID + " ORDER  BY date DESC LIMIT 1";
        try (Statement stmts = con.createStatement();) {
            ResultSet rss = stmts.executeQuery(querys);
            if (rss.next()) {
                this.timestamp = rss.getTimestamp("date");
            }
            String query2 = "update devices set date= ? where deviceid= ?";
            PreparedStatement preparedStmt = con.prepareStatement(query2);
            preparedStmt.setTimestamp(1, this.timestamp);
            preparedStmt.setString(2, deviceID);
            preparedStmt.executeUpdate();
        }
        Meterdataresponse meterdataresponse = new Meterdataresponse();
        meterdataresponse.setDate(this.timestamp);
        return meterdataresponse;
    }
}

