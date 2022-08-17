/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.DeviceRowMapper
 *  com.smartcom.api.model.Devices
 *  com.smartcom.api.model.Estate
 *  com.smartcom.api.model.EstateRowMapper
 *  com.smartcom.api.repository.DeviceToEstateRepository
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.dao.DataAccessException
 *  org.springframework.jdbc.core.JdbcTemplate
 *  org.springframework.jdbc.core.RowMapper
 *  org.springframework.stereotype.Repository
 */
package com.smartcom.api.repository;

import com.smartcom.api.model.DeviceRowMapper;
import com.smartcom.api.model.Devices;
import com.smartcom.api.model.Estate;
import com.smartcom.api.model.EstateRowMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceToEstateRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Devices> getDevices(Integer id) {
        String sql = "SELECT estate.estate_id, estate.user_id, estate.estate_name, estate.estate_address, devices.device_id, devices.device_name, devices.macaddress,devices.enabled, devices.device_type FROM estate INNER JOIN devices ON estate.estate_id=devices.estate_id INNER JOIN users ON estate.user_id=users.user_id where users.user_id =?";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{id}, (RowMapper) new DeviceRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Estate findByUserID(Integer id) {
        String sql = "SELECT * FROM Estate WHERE user_id =?";
        try {
            return (Estate) this.jdbcTemplate.queryForObject(sql, new Object[]{id}, (RowMapper) new EstateRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}

