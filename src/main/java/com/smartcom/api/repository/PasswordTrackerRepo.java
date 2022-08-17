package com.smartcom.api.repository;

import com.smartcom.api.model.Devices;
import com.smartcom.api.model.LoginLog;
import com.smartcom.api.model.PasswordTracker;
import com.smartcom.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PasswordTrackerRepo extends JpaRepository<PasswordTracker, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE PasswordTracker t set t.state=:newState WHERE t.email = :email AND t.state=:state")
    void updateState(@Param("state") String state,
                     @Param("newState") String newState,
                     @Param("email") String email);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM password_tracker WHERE email =?;\n")
    void deleteLogs(String email);

    @Query("from PasswordTracker  e where  e.email = :email AND e.state =:state")
    PasswordTracker findAllByCurrentUsr(@Param("email") String email,
                                        @Param("state") String state);

}
