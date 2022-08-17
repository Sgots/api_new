package com.smartcom.api.controller;

import com.smartcom.api.model.Announcements;
import com.smartcom.api.model.Devices;
import com.smartcom.api.model.Tarrifs;
import com.smartcom.api.repository.TarrifsRepo;
import com.smartcom.api.service.TarrifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TarrifController {
    @Autowired
    TarrifService tarrifService;
    @Autowired
    TarrifsRepo tarrifsRepo;

    @GetMapping(path = {"admin/getTarrifs"})
    public List<Tarrifs> getAllTarrifs() {
        return tarrifService.getAllTarrifs();
    }

    @PostMapping(path = {"/admin/addTarrif"})
    public ResponseEntity<?> postTarrif(@Valid @RequestBody Tarrifs tarrifs) {
        Tarrifs tarrifs1 = new Tarrifs();
        tarrifs1.setTarrifID(tarrifs.getTarrifID());
        tarrifs1.setTarrifValue(tarrifs.getTarrifValue());
        tarrifs1.setDescription(tarrifs.getDescription());
        tarrifsRepo.save(tarrifs1);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false"));

    }

    @PutMapping(path = {"/admin/updateTarrif"})
    public ResponseEntity<?> updateTarrif(@Valid @RequestBody Tarrifs tarrifs) {
        if (tarrifsRepo.findByTarrifID(tarrifs.getTarrifID()) == null) {
            return new ResponseEntity((Object) ("Unable to update as  Tarrif id " + tarrifs.getTarrifID() + " not found."), HttpStatus.NOT_FOUND);
        }

        String tarrifID = tarrifs.getTarrifID();
        String description = tarrifs.getDescription();
        String tarrifValue = tarrifs.getTarrifValue();
        tarrifsRepo.findByIDAndUpdate(tarrifID, description, tarrifValue);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false"));

    }
}
