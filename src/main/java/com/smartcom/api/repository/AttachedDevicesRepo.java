package com.smartcom.api.repository;

import com.smartcom.api.model.AttachedDevices;
import com.smartcom.api.model.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AttachedDevicesRepo
        extends JpaRepository<AttachedDevices, Integer> {
    @Query("from AttachedDevices e where e.estateID =:estateid and e.deleteStatus= false ")
    List<AttachedDevices> findAllByEstateID(@Param("estateid") Integer estateid);

    AttachedDevices findByAndAttachmentID(Integer device_id);

    AttachedDevices findByDeviceID(String mac);

    @Transactional
    @Modifying
    @Query("UPDATE AttachedDevices  t set t.deleteStatus= true WHERE t.deviceID = :deviceid")
    void findByAttachedDevice(String deviceid);

    @Transactional
    @Modifying
    @Query("UPDATE AttachedDevices  t set t.deleteStatus= true WHERE t.estateID = :estateID")
    void findByEstateIDAndDelete(Integer estateID);

}
