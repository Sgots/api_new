package com.smartcom.api.service;

import com.smartcom.api.model.Devices;
import com.smartcom.api.model.Meterdataresponse;
import com.smartcom.api.repository.DeviceRepository;
import com.smartcom.api.repository.MeterRepo;
import com.smartcom.api.repository.SystemActivityLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;

@Service
@EnableJpaAuditing
public class MeterService {

    @Autowired
    private MeterRepo meterRepo;
    @Autowired
    private DeviceRepository deviceRepository;

    public boolean addDataresponse(Meterdataresponse meterdataresponse){
        Devices devices = deviceRepository.findByMacaddress(meterdataresponse.getMacaddress());

        if(devices!=null) {
            meterdataresponse.setEstateID(devices.getEstate().getEstateid());
            meterRepo.save(meterdataresponse);

            Double energy_used = meterdataresponse.getEnergyUsedToday();
            Double remaining_credit = meterdataresponse.getRemainingCredit();
            Double remaining_energy = meterdataresponse.getRemainingEnergy();
            Double power = meterdataresponse.getPower();
            Double cost_today = meterdataresponse.getCostToday();
            String mac = meterdataresponse.getMacaddress();
            try {
                deviceRepository.updateDeviceDataresponse(mac, remaining_credit, remaining_energy, power, energy_used, cost_today);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else{
        }

            return true;

    }
}
