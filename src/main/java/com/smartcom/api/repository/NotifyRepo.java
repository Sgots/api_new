/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.NotificationsHistory
 *  com.smartcom.api.repository.NotifyRepo
 *  javax.transaction.Transactional
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Modifying
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.smartcom.api.repository;

import com.smartcom.api.model.NotificationsHistory;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepo
        extends JpaRepository<NotificationsHistory, Integer> {
    @Query(value = "from NotificationsHistory e where ( e.read_state=false) and (e.deviceID=:device_id OR e.deviceID='Announcement')  ORDER BY e.timestamp DESC")
    public List<NotificationsHistory> findAllByDeviceID(@Param(value = "device_id") String var1);

    @Transactional
    @Modifying
    @Query(value = "UPDATE NotificationsHistory t set t.read_state=:read  WHERE t.id= :id")
    public void findByIdAndUpdate(@Param(value = "id") String var1, @Param(value = "read") boolean var2);
    @Transactional
    @Modifying
    @Query(value = "UPDATE NotificationsHistory t set t.read_state=:read  WHERE t.deviceID= :id")
    public void findByDeviceIdAndUpdate(@Param(value = "id") String var1, @Param(value = "read") boolean var2);
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM notifications_history WHERE deviceid =?;\n")
    public void findByDeviceIDAndDelete(String deviceid);
}

