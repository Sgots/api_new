package com.smartcom.api.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MaxLogger implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        MaxEnergy maxEnergy = new MaxEnergy();
        maxEnergy.setMaxEnergy(rs.getDouble("maxi"));
        return maxEnergy;
    }

}
