package com.smartcom.api;


import com.smartcom.api.model.DeviceTypes;
import com.smartcom.api.model.Tarrifs;
import com.smartcom.api.repository.DeviceTypesRepo;
import com.smartcom.api.repository.TarrifsRepo;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LoadTestData {

    private final DeviceTypesRepo repository;
    private final TarrifsRepo tarrifsRepo;

    public LoadTestData(DeviceTypesRepo repository, TarrifsRepo tarrifsRepo) {
        this.tarrifsRepo = tarrifsRepo;
        this.repository = repository;
    }

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        loadDiveTypes();
        loadTarrifs();
    }

    private void loadDiveTypes() {

        repository.save(loadDeviceType("1", "IP Hub"));
        repository.save(loadDeviceType("2", "IP Smart Meter"));
        repository.save(loadDeviceType("3", "Smart Meter"));
        repository.save(loadDeviceType("4", "Smart Socket"));
        repository.save(loadDeviceType("5", "Unknown"));


    }

    private void loadTarrifs() {
        tarrifsRepo.save(loadTarrif("T0U2", "Government", "1.2"));
        tarrifsRepo.save(loadTarrif("T0U4", "Domestic Customers", "1.2"));
        tarrifsRepo.save(loadTarrif("T0U4", "Small Business", "1.2"));

        tarrifsRepo.save(loadTarrif("T0U7", "Medium Business", "1.2"));


    }

    private DeviceTypes loadDeviceType(String typeid, String type) {
        DeviceTypes deviceTypes = new DeviceTypes();
        deviceTypes.setType(type);
        deviceTypes.setTypeid(typeid);

        return deviceTypes;
    }

    private Tarrifs loadTarrif(String tarrifId, String desc, String value) {
        Tarrifs tarrifs = new Tarrifs();
        tarrifs.setTarrifID(tarrifId);
        tarrifs.setTarrifValue(value);
        tarrifs.setDescription(desc);

        return tarrifs;
    }

}
