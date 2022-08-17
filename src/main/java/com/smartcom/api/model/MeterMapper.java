/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.MeterLogs
 *  com.smartcom.api.model.MeterMapper
 *  org.springframework.jdbc.core.RowMapper
 */
package com.smartcom.api.model;

import com.smartcom.api.model.MeterLogs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class MeterMapper
        implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        MeterLogs meterLogs = new MeterLogs();
        meterLogs.setDate((Date) rs.getDate("date(date)"));
        meterLogs.setMonth(Long.valueOf(rs.getLong("month(date)")));
        meterLogs.setConsumption(Double.valueOf(rs.getDouble("kW")));
        meterLogs.setCostToday(Double.valueOf(rs.getDouble("PULA")));
        return meterLogs;
    }
}

