/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.smartcom.api.controller.DeviceController
 *  com.smartcom.api.exception.BadRequestException
 *  com.smartcom.api.model.Devices
 *  com.smartcom.api.model.Meterdataresponse
 *  com.smartcom.api.model.NotificationsHistory
 *  com.smartcom.api.model.Recharge
 *  com.smartcom.api.repository.DeviceRepository
 *  com.smartcom.api.repository.DeviceToEstateRepository
 *  com.smartcom.api.repository.DeviceTypesRepo
 *  com.smartcom.api.repository.MeterRepo
 *  com.smartcom.api.repository.Meterdatarepository
 *  com.smartcom.api.repository.NewNotifyRepo
 *  com.smartcom.api.repository.NotifyRepo
 *  com.smartcom.api.repository.RechargeRepo
 *  com.smartcom.api.repository.RemainingEnergy
 *  com.smartcom.api.service.DeviceService
 *  com.smartcom.api.service.EmailService
 *  com.smartcom.api.service.MessagingService
 *  com.smartcom.api.service.Subscribe
 *  com.smartcom.api.service.UserService
 *  javax.validation.Valid
 *  org.eclipse.paho.client.mqttv3.MqttException
 *  org.json.JSONObject
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.data.domain.PageRequest
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.domain.Sort
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RestController
 */
package com.smartcom.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smartcom.api.exception.BadRequestException;
import com.smartcom.api.model.*;
import com.smartcom.api.repository.*;
import com.smartcom.api.service.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600L)
@RestController
public class DeviceController {
    boolean isCancelled = false;

    @Autowired
    UserToEstateRepository userToEstateRepository;
    @Autowired
    private EstateRepository estateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private SystemActivityLogRepo systemActivityLogRepo;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceTypesRepo deviceTypesRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private NewNotifyRepo notifyRepo;
    @Autowired
    private Meterdatarepository meterdatarepository;
    @Autowired
    private NotifyRepo notifyRepo2;
    @Autowired
    private RemainingEnergy remainingEnergy;
    @Autowired
    private DeviceToEstateRepository deviceToEstateRepository;
    @Autowired
    private MeterRepo meterRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private AttachedDevicesRepo attachedDevicesRepo;
    @Autowired
    private RechargeRepo rechargeRepo;
    @Value(value = "${webServerUrl}")
    private String webServerUrl;

    @GetMapping(path = {"/device"})
    public List<Devices> getAllDevices() {
        return this.deviceService.getAllDevices();
    }

    @PostMapping(path = {"/device/addDevice"})
    public ResponseEntity<Object> addDevice(@Valid @RequestBody Devices devices) throws SQLIntegrityConstraintViolationException, JsonProcessingException {
        if (this.deviceService.addDevices(devices)) {
            Devices deviceExists = deviceRepository.findByDeviceid(devices.getDeviceid());
            Estate estate = estateRepository.findByEstateid(deviceExists.getEstate().getEstateid());
            Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
            User user = userRepository.findByUserid(estateExists.getUser().getUserid());
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp2 = Timestamp.valueOf(now);
            SystemLog systemLog = new SystemLog();
            systemLog.setEmail(user.getEmail());
            systemLog.setActivity("Added device with ID " + devices.getMacaddress());
            systemLog.setCreatedAt(timestamp2);
            systemActivityLogRepo.save(systemLog);
            return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "device", devices));
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.CREATED).build();
    }

    @PostMapping(path = {"/device/addSlaves"})
    public ResponseEntity<Object> addSlaves(@Valid @RequestBody Devices devices) throws SQLIntegrityConstraintViolationException, JsonProcessingException, InterruptedException {
        if (this.deviceService.addAttachedDevices(devices)) {
            Devices deviceExists = deviceRepository.findByDeviceid(devices.getDeviceid());
            Estate estate = estateRepository.findByEstateid(deviceExists.getEstate().getEstateid());
            Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
            User user = userRepository.findByUserid(estateExists.getUser().getUserid());
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp2 = Timestamp.valueOf(now);
            SystemLog systemLog = new SystemLog();
            systemLog.setEmail(user.getEmail());
            systemLog.setActivity("Added device with ID " + devices.getMacaddress());
            systemLog.setCreatedAt(timestamp2);
            systemActivityLogRepo.save(systemLog);
            return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "device", devices));
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.CREATED).build();
    }

    @PostMapping(path = {"/device/recharge2"})
    public ResponseEntity<Object> rechargeAndWait(@Valid @RequestBody Recharge recharge) throws SQLIntegrityConstraintViolationException, JsonProcessingException, MqttException, InterruptedException {
        Integer pageNo = 0;
        Integer pageSize = 1;
        Devices devices = this.deviceRepository.findByDeviceid(recharge.getDeviceID());
        PageRequest paging = PageRequest.of((int) pageNo, (int) pageSize, (Sort) Sort.by((String[]) new String[]{"date"}).descending());
        List rechargeHistoryModel = this.rechargeRepo.findByDeviceID(devices.getMacaddress(), (Pageable) paging);
        if (this.deviceService.recharge(recharge)) {
            this.deviceService.rechargeWait(recharge);
        }
        System.out.println(rechargeHistoryModel);
        Integer pageNo1 = 0;
        Integer pageSize1 = 1;
        Devices devices1 = this.deviceRepository.findByDeviceid(recharge.getDeviceID());
        PageRequest paging1 = PageRequest.of((int) pageNo1, (int) pageSize1, (Sort) Sort.by((String[]) new String[]{"date"}).descending());
        List rechargeHistoryModel1 = this.rechargeRepo.findByDeviceID(devices.getMacaddress(), (Pageable) paging);
        System.out.println(rechargeHistoryModel1);
        boolean boolval = rechargeHistoryModel1.equals(rechargeHistoryModel);
        if (boolval) {
            return new ResponseEntity((Object) "Timed out ", HttpStatus.NOT_FOUND);
        }
        if (rechargeHistoryModel1.isEmpty()) {
            return new ResponseEntity((Object) "Timed out", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("rechargeResponse", rechargeHistoryModel1, "error", "false"));
    }

    @PutMapping(value = {"device/updateDevice"})
    public ResponseEntity<?> updateUser(@RequestBody Devices devices) {
        if (this.deviceRepository.findByDeviceid(devices.getDeviceid()) == null) {
            return new ResponseEntity((Object) ("Unable to update as  User id " + devices.getDeviceid() + " not found."), HttpStatus.NOT_FOUND);
        }
        Devices deviceExists = deviceRepository.findByDeviceid(devices.getDeviceid());
        Estate estate = estateRepository.findByEstateid(deviceExists.getEstate().getEstateid());
        Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
        User user = userRepository.findByUserid(estateExists.getUser().getUserid());
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp2 = Timestamp.valueOf(now);
        SystemLog systemLog = new SystemLog();
        systemLog.setEmail(user.getEmail());
        systemLog.setActivity("Updated device with ID " + devices.getMacaddress());
        systemLog.setCreatedAt(timestamp2);
        systemActivityLogRepo.save(systemLog);
        Integer deviceid = devices.getDeviceid();
        String device_name = devices.getDevice_name();
        Integer credit = devices.getCredit();
        Integer creditAction = devices.getCreditAction();
        Integer power = devices.getPower();
        Integer powerAction = devices.getPowerAction();
        Integer energy = devices.getEnergy();
        Integer energyAction = devices.getEnergyAction();
        this.deviceRepository.findByDeviceAndUpdate(deviceid, device_name, credit, creditAction, power, powerAction, energy, energyAction);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "device", this.deviceRepository.findByDeviceid(devices.getDeviceid())));
    }

    @PutMapping(path = {"/device/update"})
    public ResponseEntity<Object> updateDevice(@Valid @RequestBody Devices devices) throws SQLIntegrityConstraintViolationException, IOException, MqttException {
        if (this.deviceService.updateDevice(devices)) {
            Devices deviceExists = deviceRepository.findByDeviceid(devices.getDeviceid());
            Estate estate = estateRepository.findByEstateid(deviceExists.getEstate().getEstateid());
            Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
            User user = userRepository.findByUserid(estateExists.getUser().getUserid());
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp2 = Timestamp.valueOf(now);
            SystemLog systemLog = new SystemLog();
            systemLog.setEmail(user.getEmail());
            systemLog.setActivity("Updated device with ID " + devices.getMacaddress());
            systemLog.setCreatedAt(timestamp2);
            systemActivityLogRepo.save(systemLog);
            Object res = Map.of(
                    "error", "false",

                    "device", devices);

            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.CREATED).build();
    }

    @PutMapping(path = {"/device/updateAttached"})
    public ResponseEntity<Object> updateAttachedDevice(@Valid @RequestBody Devices devices) throws SQLIntegrityConstraintViolationException, IOException, MqttException {
        if (this.deviceService.updateAttachedDevices(devices)) {
            Devices deviceExists = deviceRepository.findByDeviceid(devices.getDeviceid());
            Estate estate = estateRepository.findByEstateid(deviceExists.getEstate().getEstateid());
            Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
            User user = userRepository.findByUserid(estateExists.getUser().getUserid());
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp2 = Timestamp.valueOf(now);
            SystemLog systemLog = new SystemLog();
            systemLog.setEmail(user.getEmail());
            systemLog.setActivity("Updated device with ID " + devices.getMacaddress());
            systemLog.setCreatedAt(timestamp2);
            systemActivityLogRepo.save(systemLog);
            Object res = Map.of(
                    "error", "false",

                    "device", devices);

            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.CREATED).build();
    }

    @PutMapping(value = {"device/delete"})
    public ResponseEntity<?> delete(@RequestBody Devices devices) throws InterruptedException, MqttException, IOException {
        if (this.deviceRepository.findByDeviceid(devices.getDeviceid()) == null) {
            return new ResponseEntity((Object) ("Unable to update as  User id " + devices.getDeviceid() + " not found."), HttpStatus.NOT_FOUND);
        }
        Devices deviceExists = deviceRepository.findByDeviceid(devices.getDeviceid());
        Estate estate = estateRepository.findByEstateid(deviceExists.getEstate().getEstateid());
        Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
        User user = userRepository.findByUserid(estateExists.getUser().getUserid());
        deviceRepository.findByDeviceIdDelete(devices.getDeviceid());
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp2 = Timestamp.valueOf(now);
        SystemLog systemLog = new SystemLog();
        systemLog.setEmail(user.getEmail());
        systemLog.setActivity("Deleted device with ID " + devices.getMacaddress());
        systemLog.setCreatedAt(timestamp2);
        systemActivityLogRepo.save(systemLog);
        deviceRepository.findByDeviceIdDelete(devices.getDeviceid());
        Thread.sleep(1000);
        Devices devices1 = deviceRepository.findByDeviceid(deviceExists.getDeviceid());
        attachedDevicesRepo.findByAttachedDevice(deviceExists.getMacaddress());
        deviceService.updateAttachedDevices(devices1);
        return new ResponseEntity((Object) devices, HttpStatus.OK);
    }

    @PutMapping(value = {"notifications/read"})
    public ResponseEntity<?> updateRead(@RequestBody NotificationsHistory notificationsHistory) {
        if (this.notifyRepo.findAllById(notificationsHistory.getDeviceID()) == null) {
            return new ResponseEntity((Object) ("Unable to update as  User id " + notificationsHistory.getDeviceID() + " not found."), HttpStatus.NOT_FOUND);
        }
        String id = notificationsHistory.getId();
        boolean read = true;
        this.notifyRepo2.findByIdAndUpdate(id, read);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "notifications", "updated"));
    }
    @PutMapping(value = {"notifications/readall"})
    public ResponseEntity<?> updateReadAll(@RequestBody NotificationsHistory notificationsHistory) {
        if (this.notifyRepo.findAllById(notificationsHistory.getDeviceID()) == null) {
            return new ResponseEntity((Object) ("Unable to update as  User id " + notificationsHistory.getDeviceID() + " not found."), HttpStatus.NOT_FOUND);
        }
        String id = notificationsHistory.getDeviceID();
        boolean read = true;
        this.notifyRepo2.findByDeviceIdAndUpdate(id, read);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "notifications", "updated"));
    }

    @GetMapping(value = {"/device/{device_id}"})
    public ResponseEntity<?> getDevice(@PathVariable(value = "device_id") Integer device_id) {
        Devices devices = this.deviceRepository.findByDeviceid(device_id);
        if (devices == null) {
            return new ResponseEntity((Object) ("No User found with this " + device_id), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "device", devices));
    }

    @GetMapping(value = "/smartdevice/{estateid}")
    public ResponseEntity<?> getSmartMeter(@PathVariable("estateid") Integer estate_id) {
        Devices devices = deviceRepository.findAllByEstateIdAndDevice_type(estate_id);


        if (devices == null) {
            return new ResponseEntity<String>("No User found with this " + estate_id, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "device", devices)
        );
    }

    @GetMapping(value = "/hub/{estateid}")
    public ResponseEntity<?> getHub(@PathVariable("estateid") Integer estate_id) {
        Devices devices = deviceRepository.findHub(estate_id);


        if (devices == null) {
            return new ResponseEntity<String>("No User found with this " + estate_id, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "device", devices)
        );
    }

    @GetMapping(value = {"/recharge/{device_id}"})
    public ResponseEntity<?> getRechargeHistory(@PathVariable(value = "device_id") String device_id) {
        Integer pageNo1 = 0;
        Integer pageSize1 = 10000;
        PageRequest paging = PageRequest.of((int) pageNo1, (int) pageSize1, (Sort) Sort.by((String[]) new String[]{"date"}).descending());
        List rechargeModel = this.rechargeRepo.findByDeviceID(device_id, (Pageable) paging);
        if (rechargeModel == null) {
            return new ResponseEntity((Object) ("No User found with this " + device_id), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "rechargeHistory", rechargeModel));
    }

    @GetMapping(value = {"/notifyHistory/{device_id}"})
    public ResponseEntity<?> getNotifyHistory(@PathVariable(value = "device_id") String deviceid) throws SQLException {

        List notificationsHistories = this.notifyRepo2.findAllByDeviceID(deviceid);
        if (notificationsHistories == null) {
            return new ResponseEntity((Object) ("No User found with this " + deviceid), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "notifications", notificationsHistories));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @GetMapping(value = "/config/{device_id}")
    public ResponseEntity<?> getConfigfile(@PathVariable("device_id") String device_id) throws Exception {
        Devices devices = deviceRepository.findByMacaddress(device_id);
        String mac = devices.getMacaddress();
        FileInputStream fis = null;
        //  File file=new File("root/"+mac+".txt");
        BufferedReader reader = new BufferedReader(new FileReader(mac + ".txt"));
        String json = "";
        try {
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = reader.readLine();
            }
            json = sb.toString();
        } finally {
            reader.close();
        }
        byte[] cipherText = CryptoMngr.encrypt(devices.getEstate().getEncryptionKeys().getEncryptionKey(), devices.getEstate().getEncryptionKeys().getIV1(), json.getBytes());
        byte[] decrypted = CryptoMngr.decrypt(devices.getEstate().getEncryptionKeys().getEncryptionKey(), devices.getEstate().getEncryptionKeys().getIV1(), cipherText);
        //  System.out.println("3. Decrypted Message : " + new String(decrypted));

        return ResponseEntity.status(HttpStatus.OK).body(decrypted);
    }

    @GetMapping(value = {"devices/type/"})
    public ResponseEntity<?> getDeviceTypes() {
        List deviceTypes = this.deviceTypesRepo.findAll();
        if (deviceTypes == null) {
            return new ResponseEntity((Object) "No User found ", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "types", deviceTypes));
    }

    @GetMapping(value = {"/device/user/{estate_id}"})
    public ResponseEntity<?> getDeviceWithEstate(@PathVariable(value = "estate_id") Integer estate_id) {
        List devices = this.deviceRepository.findAllByEstateId(estate_id);
        if (devices == null || devices.isEmpty()) {
            return ResponseEntity.status((HttpStatus) HttpStatus.NOT_FOUND).body(Map.of("message", "No devices", "error", "true"));
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "device", devices));
    }

    @GetMapping(value = {"/device/socket/{estate_id}"})
    public ResponseEntity<?> getSmartSocket(@PathVariable(value = "estate_id") Integer estate_id) {
        List<Devices> devices = deviceRepository.findAllSocketsByEstateIdAndDevice_type(estate_id);
        if (devices == null || devices.isEmpty()) {
            return ResponseEntity.status((HttpStatus) HttpStatus.NOT_FOUND).body(Map.of("message", "No devices", "error", "true"));
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "device", devices));
    }

    @GetMapping(value = {"/device/socketonly/{estate_id}"})
    public ResponseEntity<?> getSmartSocketOnly(@PathVariable(value = "estate_id") Integer estate_id) {
        List<Devices> devices = deviceRepository.findAllSockets(estate_id);
        if (devices == null || devices.isEmpty()) {
            return ResponseEntity.status((HttpStatus) HttpStatus.NOT_FOUND).body(Map.of("message", "No devices", "error", "true"));
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "device", devices));
    }


    @GetMapping(value = {"/energy/{device_id}"})
    public ResponseEntity<?> getDeviceEnergy(@PathVariable(value = "device_id") String deviceid) {
        Meterdataresponse meterdataresponse = this.remainingEnergy.findByDeviceID(deviceid);
        if (meterdataresponse == null) {
            return new ResponseEntity((Object) ("No User found with this " + deviceid), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved", "remainingEnergy", meterdataresponse));
    }

    @PostMapping(path = {"/device/recharge"})
    public boolean recharge(@Valid @RequestBody Recharge recharge) throws MqttException {
        return this.deviceService.recharge(recharge);
    }

    @PutMapping(path = {"/device/changeState"})
    public ResponseEntity<?> changeState(@Valid @RequestBody Devices device) throws MqttException, JsonProcessingException, InterruptedException {
        Devices devices = this.deviceRepository.findByDeviceid(device.getDeviceid());

        if (devices == null) {
            throw new BadRequestException(device.getDeviceid() + " is not registered.");
        }
        //  devices.setActive(device.getActive());
        MessagingService messagingService = new MessagingService();
        messagingService.publishState2(devices, device);


        int i = 0;
        boolean active = device.getActive();
        boolean active2 = devices.getActive();

        System.out.println("check" + active2);

        while (active != active2 && i < 11) {

            System.out.println("Thread is running with all its might!");
            System.out.println(active);
            System.out.println(active2);
            Thread.sleep(4000);
            Devices devices3 = userToEstateRepository.findByDeviceID(device.getDeviceid());
            active2 = devices3.getActive();
            i++;

        }
        if (i == 11) {
            return new ResponseEntity((Object) "Timed out ", HttpStatus.NOT_FOUND);
        }
        // this.deviceRepository.save(devices);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false"));
    }


    @GetMapping(value = "/devices/{user_id}")
    public ResponseEntity<?> getDevices(@PathVariable("user_id") Integer id) {

        List<Devices> devices = deviceToEstateRepository.getDevices(id);


        if (devices == null) {
            // return new ResponseEntity<String>("No User found with this " + id, HttpStatus.NOT_FOUND);
        }
        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (Devices n : devices) {

            JSONObject entity = new JSONObject();
            entity.put("device_id", n.getDeviceid());
            entity.put("device_name", n.getDevice_name());
            entity.put("macaddress", n.getMacaddress());

            entities.add(entity);
        }
        //   return new ResponseEntity<>(entities, HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "device", entities)
        );
    }
}

