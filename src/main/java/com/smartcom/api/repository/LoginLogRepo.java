package com.smartcom.api.repository;

import com.smartcom.api.model.Estate;
import com.smartcom.api.model.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepo extends JpaRepository<LoginLog, Integer> {
}
