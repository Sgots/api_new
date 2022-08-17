/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.controller.MeterController
 *  com.smartcom.api.repository.MeterLogRepository
 *  com.smartcom.api.repository.MeterRepo
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.PageRequest
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.domain.Sort
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RestController
 */
package com.smartcom.api.controller;

import com.smartcom.api.model.*;
import com.smartcom.api.repository.DeviceRepository;
import com.smartcom.api.repository.MeterLogRepository;
import com.smartcom.api.repository.MeterRepo;

import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeterController {
    @Autowired
    private MeterLogRepository meterLogRepository;
    @Autowired
    private MeterRepo meterRepo;
    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping(value = {"/logs/{meter_id}"})
    public ResponseEntity<?> getLogs(@PathVariable(value = "meter_id") String meter_id) {
        List meterLogs = this.meterLogRepository.findAllByConsumption(meter_id);
        if (meterLogs == null || meterLogs.isEmpty()) {
            return new ResponseEntity((Object) ("No User found with this " + meter_id), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "meterLogs", meterLogs));
    }

    @GetMapping(value = {"/remains/{meter_id}"})
    public ResponseEntity<?> getEnergy(@PathVariable(value = "meter_id") String meter_id) {
        List meterLogs = this.meterLogRepository.findAllRemainingEnergy(meter_id);
        if (meterLogs == null || meterLogs.isEmpty()) {
            return new ResponseEntity((Object) ("No User found with this " + meter_id), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "meterLogs", meterLogs));
    }

    @GetMapping(value = {"/logs/month/{meter_id}"})
    public ResponseEntity<?> getmonthlyLogs(@PathVariable(value = "meter_id") String meter_id) {
        List meterLogs = this.meterLogRepository.findAllByMonth(meter_id);
        if (meterLogs == null) {
            return new ResponseEntity((Object) ("No User found with this " + meter_id), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error",
                "false", "message", "retrieved"
                , "meterLogs", meterLogs));
    }

    @GetMapping(value = {"/logs/compare/{estate_id}"})
    public ResponseEntity<?> getDeviceCompare(@PathVariable(value = "estate_id") Integer estate_id) throws SQLException {
        //  List meterLogs = this.meterLogRepository.deviceCompare(estate_id);
        List<Devices> devices = deviceRepository.findAllSockets(estate_id);

        if (devices == null) {
            return new ResponseEntity((Object) ("No User found with this " + estate_id), HttpStatus.NOT_FOUND);
        }
        Devices devices4 = deviceRepository.meterEnergy(estate_id);
        Double meterValue = devices4.getEnergyUsed();
        List<Devices> devices1 = deviceRepository.energyUsedToday(estate_id);
        List compareDevices = this.meterLogRepository.deviceCompare(estate_id, meterValue);
        Double cal = 0.0;
        for (Devices devices2 : devices1) {
            cal = (devices2.getEnergyUsed() / meterValue) * 100;
            System.out.println(cal);
            devices2.setEnergyUsed(cal);
        }

        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error",
                "false", "message", "retrieved"
                , "meterLogs", devices1));
    }
    @GetMapping(value = {"/logs/compare/month/{estate_id}"})
    public ResponseEntity<?> getDeviceCompareMonth(@PathVariable(value = "estate_id") Integer estate_id) throws SQLException {
        //  List meterLogs = this.meterLogRepository.deviceCompare(estate_id);
        List<Devices> devices = deviceRepository.findAllSockets(estate_id);

        if (devices == null) {
            return new ResponseEntity((Object) ("No User found with this " + estate_id), HttpStatus.NOT_FOUND);
        }
       List<Meterdataresponse> meterdataresponse = meterRepo.socketEnergy(estate_id);
        Devices devices4 = deviceRepository.meterEnergy(estate_id);

        MeterLogs meterLogs4 = meterLogRepository.findMeterEnergy(devices4.getMacaddress());
        Double meterValue = meterLogs4.getConsumption();
        Double cal = 0.0;
        Double sumOfSockets = 0.0;
        ArrayList<Double> list = null;
        System.out.println("before loop"+meterdataresponse.toString());
        for (Meterdataresponse meterdataresponse1 : meterdataresponse) {
            System.out.println("in loop"+meterdataresponse.toString());

            MeterLogs meterLogs = meterLogRepository.findAllByMonthCompare(meterdataresponse1.getMacaddress());
            cal = (meterLogs.getConsumption() / meterValue) * 100;
            list = new ArrayList<Double>();
            list.add(meterLogs.getConsumption());
            System.out.println(meterLogs.toString());
            meterdataresponse1.setEnergyUsedToday(cal);
        }
        for(int i=0; i<list.size(); i++){
            sumOfSockets = sumOfSockets + list.get(i);

        }
      Double unknownEnergy =  meterValue - sumOfSockets ;
        Meterdataresponse meterdataresponse4 = new Meterdataresponse();
        meterdataresponse4.setEnergyUsedToday((unknownEnergy/meterValue) * 100);
        meterdataresponse4.setMacaddress("Unknown");
meterdataresponse.add(meterdataresponse4);
       // devices2.setEstate(devices4.);

        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error",
                "false", "message", "retrieved"
                , "meterLogs", meterdataresponse));
    }
    @GetMapping(value = {"/logs/month/pula/{meter_id}"})
    public ResponseEntity<?> getmonthlyPulaLogs(@PathVariable(value = "meter_id") String meter_id) {
        List<MeterLogs> meterLogs = this.meterLogRepository.findAllByMonthInPula(meter_id);
        if (meterLogs == null) {
            return new ResponseEntity((Object) ("No User found with this " + meter_id), HttpStatus.NOT_FOUND);
        }
        Double cons =0.0;
     /*   for (MeterLogs meterLogs1 : meterLogs) {

            cons = (meterLogs1.getConsumption() * 1.2);
            meterLogs1.setConsumption(cons);
        }*/
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error",
                "false", "message", "retrieved"
                , "meterLogs", meterLogs));
    }

    @GetMapping(value = {"/logs/daily/{meter_id}"})
    public ResponseEntity<?> getdailyLogs(@PathVariable(value = "meter_id") String meter_id) {
        List meterLogs = meterRepo.findTop7ByMacaddressOrderByDateDesc(meter_id);
        if (meterLogs == null) {
            return new ResponseEntity((Object) ("No User found with this " + meter_id), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body
                (Map.of("error", "false", "message",
                        "retrieved",
                        "meterLogs", meterLogs));
    }

    @GetMapping(value = {"/meterdataresponse/{macaddress}"})
    public ResponseEntity<?> getMeter(@PathVariable(value = "macaddress") String macaddress) throws SQLException {
        Integer pageNo = 0;
        Integer pageSize = 1;
        PageRequest paging = PageRequest.of((int) pageNo, (int) pageSize, (Sort) Sort.by((String[]) new String[]{"date"}).descending());
        List meterdataresponse1 = this.meterRepo.findAllByMac(macaddress, (Pageable) paging);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "deviceSS", meterdataresponse1));
    }
   /* @GetMapping(value={"/smartscoketsUsage/{macaddress}"})
    public ResponseEntity<?> getSmartUsage(@PathVariable(value="macaddress") String macaddress) throws SQLException {
        Integer pageNo = 0;
        Integer pageSize = 1;
        PageRequest paging = PageRequest.of((int)pageNo, (int)pageSize, (Sort)Sort.by((String[])new String[]{"date"}).descending());
        List meterdataresponse1 = this.meterRepo.findAllSockets(macaddress, (Pageable)paging);
        return ResponseEntity.status((HttpStatus)HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "deviceSS", meterdataresponse1));
    }*/
}

