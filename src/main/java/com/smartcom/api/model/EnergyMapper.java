/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.EnergyMapper
 *  com.smartcom.api.model.Meterdataresponse
 *  org.springframework.jdbc.core.RowMapper
 */
package com.smartcom.api.model;

import com.smartcom.api.model.Meterdataresponse;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class EnergyMapper
        implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Meterdataresponse meterdataresponse = new Meterdataresponse();
        meterdataresponse.setRemainingEnergy(Double.valueOf(rs.getDouble("remaining_energy")));
        meterdataresponse.setDate(rs.getTimestamp("date"));
        return meterdataresponse;
    }
}

