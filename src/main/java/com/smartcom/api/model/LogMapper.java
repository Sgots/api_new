/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.LogMapper
 *  com.smartcom.api.model.MeterLogs
 *  org.springframework.jdbc.core.RowMapper
 */
package com.smartcom.api.model;

import com.smartcom.api.model.MeterLogs;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class LogMapper
        implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        MeterLogs meterLogs = new MeterLogs();
        meterLogs.setMonth(Long.valueOf(rs.getLong("month(date)")));
        meterLogs.setConsumption(Double.valueOf(rs.getDouble("kW")));
        return meterLogs;
    }
}

