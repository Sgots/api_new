package com.smartcom.api.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompareLogger implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        CompareDevices compareDevices = new CompareDevices();
        // compareDevices.setMonthlyUsagePercentage(rs.getDouble("monthusage"));
        compareDevices.setTodayUsagePercentage(rs.getDouble("today"));
        compareDevices.setDeviceID(rs.getString("deviceID"));
        //   compareDevices.setWeeklyUsagePercentage(rs.getDouble("weekly"));

        return compareDevices;
    }
}
