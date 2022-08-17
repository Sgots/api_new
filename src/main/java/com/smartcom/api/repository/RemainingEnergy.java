/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.EnergyMapper
 *  com.smartcom.api.model.Meterdataresponse
 *  com.smartcom.api.repository.RemainingEnergy
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.dao.DataAccessException
 *  org.springframework.jdbc.core.JdbcTemplate
 *  org.springframework.jdbc.core.RowMapper
 *  org.springframework.stereotype.Repository
 */
package com.smartcom.api.repository;

import com.smartcom.api.model.EnergyMapper;
import com.smartcom.api.model.Meterdataresponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RemainingEnergy {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Meterdataresponse findByDeviceID(String id) {
        String sql = "SELECT * \nFROM meterdataresponse WHERE macaddress  =?\nORDER BY date DESC\nLIMIT 1";
        try {
            return (Meterdataresponse) this.jdbcTemplate.queryForObject(sql, new Object[]{id}, (RowMapper) new EnergyMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}

