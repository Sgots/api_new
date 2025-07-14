package com.smartcom.api.repository;

import com.smartcom.api.model.Devices;
import com.smartcom.api.model.Events;
import com.smartcom.api.model.Meterdataresponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MeterRepo extends JpaRepository<Meterdataresponse, Integer> {
    @Query("from Meterdataresponse  e where e.macaddress=:macaddress GROUP BY e.date")
    List<Meterdataresponse> findAllByMac(@Param("macaddress") String macaddress, Pageable pageable);

    List<Meterdataresponse> findTop7ByMacaddressOrderByDateDesc(String mac_address);

    @Query(nativeQuery = true, value = "SELECT  *  FROM dataresponse where estateid=? and (device_typeid = 2 or device_typeid =3 ) ORDER BY date DESC LIMIT 1")
    Meterdataresponse meterEnergy(Integer estateid);

    @Query(nativeQuery = true, value = "SELECT  *  FROM dataresponse where macaddress=? and (device_typeid = 2 or device_typeid =3 ) ORDER BY date DESC LIMIT 1")
    Meterdataresponse meterData(String mac);
    @Query(nativeQuery = true, value = "SELECT month(date),(MAX(total_energy) - MIN(total_energy))  as kW FROM `dataresponse` WHERE macaddress = ? GROUP by month(date ) ORDER  BY DESC  LIMIT 1")
    Meterdataresponse monthlyUsage(String macaddress);


    @Query(nativeQuery = true, value = "SELECT  *  FROM dataresponse where estateid=? and device_typeid = 4 GROUP BY macaddress")
    List<Meterdataresponse> socketEnergy(Integer estateid);

}
