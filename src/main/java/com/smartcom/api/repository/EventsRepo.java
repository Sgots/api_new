package com.smartcom.api.repository;

import com.smartcom.api.model.Announcements;
import com.smartcom.api.model.Events;
import com.smartcom.api.model.Tarrifs;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EventsRepo extends JpaRepository<Events, Integer> {

    Events findByDeviceID(String deviceID);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM events WHERE deviceid =?;\n")
    void deleteEventsByDeviceID(String deviceid);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM events WHERE estateid =?;\n")
    void deleteEventsByEstateID(Integer estateid);

    @Query(nativeQuery = true, value = "SELECT * FROM events s ORDER BY eventID DESC LIMIT 1")
    Events findByEventID();

    List<Events> findAllByEstateID(Integer estateid);
    // Events deleteEventsByDeviceID(String deviceID);
}
