package com.smartcom.api.controller;

import com.smartcom.api.model.Announcements;
import com.smartcom.api.model.Estate;
import com.smartcom.api.model.Recharge;
import com.smartcom.api.repository.AnnounceRepo;
import com.smartcom.api.service.MessagingService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class AnnounceController {
    @Autowired
    private AnnounceRepo announceRepo;

    @GetMapping(path = "admin/getAnnounce")
    public ResponseEntity<?> getAllAnnounce() {
        List announcements1 = announceRepo.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "announcements", announcements1)
        );
    }


    @PostMapping(path = {"/admin/announce"})
    public ResponseEntity<?> postAnnounce(@Valid @RequestBody Announcements announcements) throws MqttException {
        Announcements announcements1 = new Announcements();
        MessagingService messagingService = new MessagingService();
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp2 = Timestamp.valueOf(now);

        announcements1.setContent(announcements.getContent());
        announcements1.setTitle(announcements.getTitle());
        announcements1.setCreationDate(timestamp2);
        messagingService.announcementAlert(announcements.getTitle());
        announceRepo.save(announcements1);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false"));

    }

    @PutMapping(path = {"/admin/announceUpdate"})
    public ResponseEntity<?> announceUpdate(@Valid @RequestBody Announcements announcements) {
        if (this.announceRepo.findByAnnounceid(announcements.getAnnounceid()) == null) {
            return new ResponseEntity((Object) ("Unable to update as  Announcement id " + announcements.getAnnounceid() + " not found."), HttpStatus.NOT_FOUND);
        }
        Announcements announcements1 = new Announcements();
        Integer announce_id = announcements.getAnnounceid();
        String content = announcements.getContent();
        String title = announcements.getTitle();
        announceRepo.findByIDAndUpdate(announce_id, content, title);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false"));

    }

    @PutMapping(path = {"/admin/announceDelete"})
    public ResponseEntity<?> announceDelete(@Valid @RequestBody Announcements announcements) {
        if (this.announceRepo.findByAnnounceid(announcements.getAnnounceid()) == null) {
            return new ResponseEntity((Object) ("Unable to update as  Announcement id " + announcements.getAnnounceid() + " not found."), HttpStatus.NOT_FOUND);
        }
        Announcements announcements1 = new Announcements();
        Integer announce_id = announcements.getAnnounceid();

        announceRepo.findByAnnounceidAndDelete(announce_id);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false"));

    }


}
