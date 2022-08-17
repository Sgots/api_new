/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.Estate
 *  com.smartcom.api.model.EstateRowMapper
 *  org.springframework.jdbc.core.RowMapper
 */
package com.smartcom.api.model;

import com.smartcom.api.model.Estate;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class EstateRowMapper
        implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Estate estate = new Estate();
        estate.setEstateid(Integer.valueOf(rs.getInt("estate_id")));
        estate.setEstatename(rs.getString("estate_name"));
        estate.setEstateaddress(rs.getString("estate_address"));
        estate.setEnabled(rs.getBoolean("Enabled"));
        return estate;
    }
}

