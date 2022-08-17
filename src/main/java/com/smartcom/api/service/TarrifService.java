package com.smartcom.api.service;

import com.smartcom.api.model.Tarrifs;
import com.smartcom.api.repository.TarrifsRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TarrifService {

    @Autowired
    TarrifsRepo tarrifsRepo;

    public List<Tarrifs> getAllTarrifs() {
        List<Tarrifs> tarrifs = tarrifsRepo.findAll();
        for (Tarrifs tarrifs1 : tarrifs) {
            // Devices.setId(0);
        }
        return tarrifs;
    }
}
