/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.DeviceRowMapper
 *  com.smartcom.api.model.Devices
 *  com.smartcom.api.model.Estate
 *  org.springframework.jdbc.core.RowMapper
 */
package com.smartcom.api.model;

import com.smartcom.api.model.Devices;
import com.smartcom.api.model.Estate;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DeviceRowMapper
        implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Devices devices = new Devices();
        Estate estate = new Estate();
        devices.setDeviceid(Integer.valueOf(rs.getInt("deviceid")));
        devices.setDevice_name(rs.getString("device_name"));
        devices.setMacaddress(rs.getString("macaddress"));
        devices.setEnabled(rs.getBoolean("enabled"));
        devices.setActive(rs.getBoolean("device_status"));
        return devices;
    }
}

