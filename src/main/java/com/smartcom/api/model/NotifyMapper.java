/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.NotificationsHistory
 *  com.smartcom.api.model.NotifyMapper
 *  org.springframework.jdbc.core.RowMapper
 */
package com.smartcom.api.model;

import com.smartcom.api.model.NotificationsHistory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class NotifyMapper
        implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        NotificationsHistory notificationsHistory = new NotificationsHistory();
        notificationsHistory.setId(rs.getString("id"));
        notificationsHistory.setDeviceID(rs.getString("deviceid"));
        notificationsHistory.setRead_state(rs.getBoolean("read_state"));
        notificationsHistory.setStatus(rs.getString("status"));
        notificationsHistory.setTimestamp((Date) rs.getDate("timestamp"));
        return notificationsHistory;
    }
}

