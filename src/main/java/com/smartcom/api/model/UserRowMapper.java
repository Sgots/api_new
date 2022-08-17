/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.Estate
 *  com.smartcom.api.model.User
 *  com.smartcom.api.model.UserRowMapper
 *  org.springframework.jdbc.core.RowMapper
 */
package com.smartcom.api.model;

import com.smartcom.api.model.Estate;
import com.smartcom.api.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper
        implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        Estate estate = new Estate();
        user.setUserid(Integer.valueOf(rs.getInt("user_id")));
        user.setPassword(rs.getString("password"));
        user.setPassword2(rs.getString("password2"));
        user.setEnabled(rs.getBoolean("enabled"));
        estate.setEstateid(Integer.valueOf(rs.getInt("estate_id")));
        user.setStatus(rs.getBoolean("Status"));
        user.setConfirmationToken(rs.getString("confirmation_token"));
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("Username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("first_name"));
        return user;
    }
}

