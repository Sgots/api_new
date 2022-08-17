/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.NotificationsHistory
 *  com.smartcom.api.model.NotifyMapper
 *  com.smartcom.api.repository.NewNotifyRepo
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.dao.DataAccessException
 *  org.springframework.jdbc.core.JdbcTemplate
 *  org.springframework.jdbc.core.RowMapper
 *  org.springframework.stereotype.Repository
 */
package com.smartcom.api.repository;

import com.smartcom.api.model.NotificationsHistory;
import com.smartcom.api.model.NotifyMapper;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class NewNotifyRepo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<NotificationsHistory> findAllByDeviceid(String id) throws SQLException {
        String sql = "SELECT * FROM notifications_history WHERE deviceid =? AND read_state = false ";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{id}, (RowMapper) new NotifyMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<NotificationsHistory> findAllById(String id) {
        String sql = "SELECT * FROM notifications_history WHERE deviceid =?";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{id}, (RowMapper) new NotifyMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}

