package com.smartcom.api.repository;

import com.smartcom.api.model.Announcements;
import com.smartcom.api.model.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnnounceRepo extends JpaRepository<Announcements, Integer> {
    Announcements findByAnnounceid(Integer announceid);

    @Transactional
    @Modifying
    @Query("FROM  Announcements  t where t.deleteStatus=false ORDER BY t.creationDate desc")
    List<Announcements> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE Announcements t set t.content=:content, t.title=:title WHERE t.announceid = :announce_id")
    void findByIDAndUpdate(@Param("announce_id") Integer announce_id,
                           @Param("title") String title,
                           @Param("content") String content);

    @Transactional
    @Modifying
    @Query("UPDATE Announcements t set t.deleteStatus = true WHERE t.announceid = :announceid")
    void findByAnnounceidAndDelete(Integer announceid);
}
