/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.DeviceRowMapper
 *  com.smartcom.api.model.Devices
 *  com.smartcom.api.model.User
 *  com.smartcom.api.model.UserRowMapper
 *  com.smartcom.api.repository.UserToEstateRepository
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.dao.DataAccessException
 *  org.springframework.jdbc.core.JdbcTemplate
 *  org.springframework.jdbc.core.RowMapper
 *  org.springframework.stereotype.Repository
 */
package com.smartcom.api.repository;

import com.smartcom.api.model.DeviceRowMapper;
import com.smartcom.api.model.Devices;
import com.smartcom.api.model.User;
import com.smartcom.api.model.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserToEstateRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public User getUser(Integer id) {
        String sql = "SELECT users.first_name ,users.last_name, users.confirmation_token, users.email, users.enabled, users.username, users.is_temp_password, users.password, users.password2, users.role, users.status,users.user_id, estate.estate_id, estate.user_id FROM users INNER JOIN estate ON estate.user_id=users.user_id where users.user_id =?";
        try {
            return (User) this.jdbcTemplate.queryForObject(sql, new Object[]{id}, (RowMapper) new UserRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Devices findByDeviceID(Integer id) {
        String sql = "SELECT * FROM devices WHERE deviceid =? ";
        try {
            return (Devices) this.jdbcTemplate.queryForObject(sql, new Object[]{id}, (RowMapper) new DeviceRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}

