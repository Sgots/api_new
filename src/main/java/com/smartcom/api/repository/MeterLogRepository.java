/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.EnergyMapper
 *  com.smartcom.api.model.LogMapper
 *  com.smartcom.api.model.MeterLogs
 *  com.smartcom.api.model.MeterMapper
 *  com.smartcom.api.repository.MeterLogRepository
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.dao.DataAccessException
 *  org.springframework.jdbc.core.JdbcTemplate
 *  org.springframework.jdbc.core.RowMapper
 *  org.springframework.stereotype.Repository
 */
package com.smartcom.api.repository;

import com.smartcom.api.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MeterLogRepository {
    String mac;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<MeterLogs> findAllByConsumption(String id) {
        String sql = "SELECT  date(date),cost_today as PULA,month(date),MAX(energy_used_today) as kW FROM `dataresponse` WHERE macaddress =? AND date >= DATE(NOW()) - INTERVAL 7 DAY GROUP by date(date),month(date) ORDER BY date(date)";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{id}, (RowMapper) new MeterMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MeterLogs> findAllRemainingEnergy(String id) {
        String sql = "SELECT * \nFROM dataresponse WHERE macaddress  =?\nORDER BY date DESC\nLIMIT 1";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{id}, (RowMapper) new EnergyMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MeterLogs> findAllByMonth(String id) {

        String sql = "SELECT month(date),(MAX(total_energy) - MIN(total_energy))  as kW FROM `dataresponse` WHERE macaddress = ? GROUP by month(date )";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{id}, (RowMapper) new LogMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MeterLogs findAllByMonthCompare(String id) {

        String sql = "SELECT month(date),(MAX(total_energy) - MIN(total_energy))  as kW FROM `dataresponse` WHERE macaddress = ? GROUP by month(date ) ORDER BY month(date ) DESC LIMIT 1";
        try {

            return (MeterLogs) jdbcTemplate.queryForObject(sql, new Object[]{id}, new LogMapper());

        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    public MeterLogs findMeterEnergy(String id) {

        String sql = "SELECT month(date),(MAX(total_energy) - MIN(total_energy))  as kW FROM `dataresponse` WHERE macaddress = ? AND (device_typeid = 2 || device_typeid =3)GROUP by month(date ) ORDER BY month(date ) DESC LIMIT 1";
        try {

            return (MeterLogs) jdbcTemplate.queryForObject(sql, new Object[]{id}, new LogMapper());

        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<CompareDevices> deviceCompare(Integer id, Double meterValue) throws SQLException {

        String sql = "SELECT macaddress as deviceID, date(date),(energy_used_today/" + meterValue + " * 100) as today ,month(date),(MAX(total_energy) - MIN(total_energy)/100) as monthusage, MAX(time_stamp) FROM `dataresponse` WHERE estateid = ? and (device_typeid = 4 or device_typeid = 5) GROUP by deviceID";

        try {
            return this.jdbcTemplate.query(sql, new Object[]{id}, (RowMapper) new CompareLogger());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;

        }
    }

    public MaxEnergy sumEnergy(Integer id) throws SQLException {
        List<CompareDevices> compareDevices1 = null;

        String sql = "SELECT MAX(energy_used_today) as maxi FROM `dataresponse` WHERE estateid = ? and (device_typeid = 4 || device_typeid = 5) OR devGROUP by month(date)";

        try {
            return (MaxEnergy) this.jdbcTemplate.query(sql, new Object[]{id}, (RowMapper) new MaxLogger());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;

        }
    }


    /*  for (Devices devices1 : devices) {
          mac = devices1.getMacaddress();
      *//*    mac = events3.getDeviceID();
            deviceRepository.findByDeviceIdEvent(mac);*//*

        }

    }*/
    public List<MeterLogs> deviceCompareWeekly(String id) {

        String sql = "SELECT date(date),MAX(energy_used_today/100) as today ,month(date),(MAX(total_energy) - MIN(total_energy)/100) as monthusage FROM `dataresponse` WHERE macaddress = ? GROUP by month(date)";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{id}, (RowMapper) new CompareLogger());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MeterLogs> findAllByMonthInPula(String id) {

        String sql = "SELECT month(date),(MAX(total_energy) - MIN(total_energy)) as Pula FROM `dataresponse` WHERE macaddress = ? GROUP by month(date )";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{id}, (RowMapper) new LogMapperPula());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}

