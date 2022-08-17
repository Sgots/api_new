package com.smartcom.api.controller;


import com.smartcom.api.model.Announcements;
import com.smartcom.api.model.LoginLog;
import com.smartcom.api.model.PasswordTracker;
import com.smartcom.api.model.SystemLog;
import com.smartcom.api.repository.AnnounceRepo;
import com.smartcom.api.repository.LoginLogRepo;
import com.smartcom.api.repository.PasswordTrackerRepo;
import com.smartcom.api.repository.SystemActivityLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SystemLogsController {
    @Autowired
    private SystemActivityLogRepo systemActivityLogRepo;
    @Autowired
    private PasswordTrackerRepo passwordTrackerRepo;
    @Autowired
    private LoginLogRepo loginLogRepo;

    @GetMapping(path = "admin/systemActivity")
    public List<SystemLog> getAllLogs() {
        List<SystemLog> systemLogs = systemActivityLogRepo.findAll();
        for (SystemLog systemLog : systemLogs) {
            systemLog.setSystemID(0);
        }
        return systemLogs;

    }

    @GetMapping(path = "admin/passwordTracker")
    public List<PasswordTracker> getAllLogs2() {
        List<PasswordTracker> passwordTrackers = passwordTrackerRepo.findAll();
        for (PasswordTracker passwordTracker : passwordTrackers) {
            passwordTracker.setTrackId(0);
        }
        return passwordTrackers;

    }

    @GetMapping(path = "admin/loginLogs")
    public List<LoginLog> getAllLogs3() {
        List<LoginLog> loginLogs = loginLogRepo.findAll();
        for (LoginLog loginLog : loginLogs) {
            loginLog.setLogId(0);
        }
        return loginLogs;

    }
}
