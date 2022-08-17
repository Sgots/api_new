package com.smartcom.api.repository;

import com.smartcom.api.model.Devices;
import com.smartcom.api.model.Tarrifs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TarrifsRepo extends JpaRepository<Tarrifs, Integer> {
    Tarrifs findByTarrifID(String tarrifID);

    @Transactional
    @Modifying
    @Query("UPDATE Tarrifs t set t.description=:description, t.tarrifValue=:tarrifValue WHERE t.tarrifID = :tarrifID")
    void findByIDAndUpdate(@Param("tarrifID") String tarrifID,
                           @Param("description") String description,
                           @Param("tarrifValue") String tarrifValue);
}
