package com.smartcom.api.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LogMapperPula
        implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        MeterLogs meterLogs = new MeterLogs();
        meterLogs.setMonth(Long.valueOf(rs.getLong("month(date)")));
        meterLogs.setConsumption(Double.valueOf(rs.getDouble("Pula") * 1.2));
        return meterLogs;
    }
}