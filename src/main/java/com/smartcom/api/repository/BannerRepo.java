package com.smartcom.api.repository;

import com.smartcom.api.model.Banner;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface BannerRepo extends JpaRepository<Banner, Integer> {
    Optional<Banner> findByName(String name);

    List<Banner> findAll();

    // Banner findById(Integer id);
    @Transactional
    @Modifying
    @Query("UPDATE Banner t set t.deleteStatus = true WHERE t.id = :id")
    void findByBanneridAndDelete(Integer id);

}
