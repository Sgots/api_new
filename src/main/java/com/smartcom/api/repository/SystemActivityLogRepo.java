package com.smartcom.api.repository;

import com.smartcom.api.model.PasswordTracker;
import com.smartcom.api.model.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemActivityLogRepo extends JpaRepository<SystemLog, Integer> {
}
