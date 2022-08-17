package com.smartcom.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcom.api.SmartcomApplication;
import com.smartcom.api.controller.SimpleMqTTCallBack;
import com.smartcom.api.exception.BadRequestException;
import com.smartcom.api.model.*;
import com.smartcom.api.repository.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    boolean isCancelled = false;

    @Autowired
    private EstateRepository estateRepository;
    @Autowired
    private AttachedDevicesRepo attachedDevicesRepo;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private UserToEstateRepository userToEstateRepository;
    @Autowired
    private RechargeRepo rechargeRepo;
    ServerSocket server_socket;
    public int PORT = 7777;
    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public List<Devices> getAllDevices() {
        List<Devices> device = deviceRepository.findAll();
        for (Devices devices : device) {
            // Devices.setId(0);
        }
        return device;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getJsonFile(Devices device) throws IOException {
        Devices deviceExists = this.deviceRepository.findByDeviceid(device.getDeviceid());
        if (deviceExists == null) {
            throw new BadRequestException(device.getDeviceid() + " is not registered.");
        }
        String mac = device.getMacaddress();
        Object fis = null;
        String json = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(mac + ".txt"));) {
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = reader.readLine();
            }
            json = sb.toString();
        }
        return json;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean updateDevice(Devices device) throws IOException, MqttException {
        Devices devices = this.deviceRepository.findByDeviceid(device.getDeviceid());
        List<AttachedDevices> attached = attachedDevicesRepo.findAllByEstateID(devices.getEstate().getEstateid());
        Estate estate = estateRepository.findByEstateid(devices.getEstate().getEstateid());
        Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
        if (devices == null) {
            throw new BadRequestException(device.getDeviceid() + " is not registered.");
        }
        String deviceName = device.getDevice_name();
        Integer credit = device.getCredit();
        Integer creditAction = device.getCreditAction();
        Integer power = device.getPower();
        Integer powerAction = device.getPowerAction();
        Integer energy = device.getEnergy();
        Integer energyAction = device.getEnergyAction();
        devices.setCredit(credit);
        devices.setCreditAction(creditAction);
        devices.setEnergy(energy);
        devices.setEnergyAction(energyAction);
        devices.setPower(power);
        devices.setPowerAction(powerAction);
        devices.setDevice_name(deviceName);
        try {
            SmartcomApplication smartcomApplication = new SmartcomApplication();
            MessagingService messagingService = new MessagingService();
            SimpleMqTTCallBack simpleMqTTCallBack = new SimpleMqTTCallBack();
            String mess = "";
            String json = new ObjectMapper().writeValueAsString((Object) devices);
            byte[] key = estate.getEncryptionKeys().getEncryptionKey();
            byte[] iv1 = estate.getEncryptionKeys().getIV1();
            byte[] iv2 = estate.getEncryptionKeys().getIV2();

            String string = Base64.getEncoder().encodeToString(key);
            String string2 = Base64.getEncoder().encodeToString(iv1);
            String string3 = Base64.getEncoder().encodeToString(iv2);


            JSONObject jsonObject = new JSONObject();
            JSONObject attachedJson = new JSONObject();

            JSONObject topic = new JSONObject();
            JSONObject dev = new JSONObject();
            JSONObject set = new JSONObject();
            JSONObject ta = new JSONObject();
            JSONArray serverSet = new JSONArray();
            JSONArray topics = new JSONArray();
            JSONArray deviceSettions = new JSONArray();
            JSONArray attachedDevices = new JSONArray();
            attachedDevices.put(attached);
            JSONArray tariffs = new JSONArray();
            JSONObject encrypt = new JSONObject();
            JSONObject band = new JSONObject();
            topic.put("eventsTopic", devices.getEstate().getEstateid() + "/timedEventsTopic");

            topic.put("meterDataTopic", devices.getEstate().getEstateid() + "/" + devices.getMacaddress() + "/meterData");
            topic.put("rechargeTopic", devices.getEstate().getEstateid() + "/" + devices.getMacaddress() + "/recharge");
            topic.put("stateControlTopic", devices.getEstate().getEstateid() + "/" + devices.getMacaddress() + "/control/state");
            topic.put("configurationReadyTopic", devices.getEstate().getEstateid() + "/" + devices.getMacaddress() + "/configurationReady");
            topic.put("notificationTopic", devices.getEstate().getEstateid() + "/" + devices.getMacaddress() + "/notifications");

            set.put("serverConfigurationUrl", "http://18.228.18.217:9000/echoPost");
            set.put("mqttServerURL", "tcp://18.228.18.217:1883");
            set.put("mqttUsername", "admin");
            set.put("mqttPassword", "admin");
            dev.put("dataFrequency", estateExists.getMeterdatafreq());
            dev.put("devicePin", "1234");
            dev.put("energyThreshold", "20");
            dev.put("powerThreshold", "20");
            dev.put("creditThreshold", "20");
            encrypt.put("key", string);
            encrypt.put("iv1", string2);
            encrypt.put("iv2", string3);
            band.put("bandValue", "1");
            band.put("bandID", "B1");
            band.put("bandDescription", "Standard Rate");


            ta.put("tarrifID", "TOU7");
            ta.put("tariffDescription", "Medium Business");
            ta.put("tariffValue", "1.2");


            jsonObject.put("band", band);
            jsonObject.put("command", "configurationRequest");
            jsonObject.put("deviceID", devices.getMacaddress());
            jsonObject.put("deviceSettings", dev);
            jsonObject.put("topics", topic);
            jsonObject.put("deviceTypeID", devices.getDeviceTypes().getTypeid());
            jsonObject.put("estateID", devices.getEstate().getEstateid());
            jsonObject.put("encryptionSettings", encrypt);
            jsonObject.put("tariff", ta);
            jsonObject.put("serverSettings", set);
            jsonObject.put("attachedDevices", attached);
            if (messagingService.publish(devices)) {
                try {
                    File myObj = new File(devices.getMacaddress() + ".txt");
                    if (myObj.createNewFile()) {
                        System.out.println("File created: " + myObj.getName());
                    } else {
                        FileWriter myWriter = new FileWriter(devices.getMacaddress() + ".txt");
                        myWriter.write("");
                    }
                    System.out.println("File already exists.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                }
                FileWriter myWriter = new FileWriter(devices.getMacaddress() + ".txt");
                myWriter.write(String.valueOf((Object) jsonObject));
                myWriter.close();
                deviceRepository.save(devices);
            }
        } finally {
            return true;
        }
    }

    public Devices deleteDevice(Devices device) {
        Devices deviceExists = deviceRepository.findByDeviceid(device.getDeviceid());

        if (deviceExists == null) {
            throw new BadRequestException(device.getDeviceid() + " is not registered.");
        }

        deviceExists.setDelete_status(true);

        deviceRepository.save(deviceExists);

        return deviceExists;
    }

    public void CreditNotification(String deviceID, Integer action, Integer estateid, String deviceName) {
        MessagingService messagingService = new MessagingService();
        Devices devices = new Devices();
        if (action.equals(2)) {
            try {
                try {
                    messagingService.CreditNotif(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(3)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(4)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                    messagingService.CreditNotif(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void CreditNotificationOff(String deviceID, Integer action, Integer estateid, String deviceName) {
        MessagingService messagingService = new MessagingService();
        Devices devices = new Devices();
        if (action.equals(2)) {
            try {
                try {
                    messagingService.CreditNotifOff(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(3)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(4)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                    messagingService.CreditNotifOff(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void PowerNotification(String deviceID, Integer action, Integer estateid, String deviceName) {
        MessagingService messagingService = new MessagingService();
        Devices devices = new Devices();
        if (action.equals(2)) {
            try {
                try {
                    messagingService.PowerNotif(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(3)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(4)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                    messagingService.PowerNotif(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void PowerNotificationOff(String deviceID, Integer action, Integer estateid, String deviceName) {
        MessagingService messagingService = new MessagingService();
        Devices devices = new Devices();
        if (action.equals(2)) {
            try {
                try {
                    messagingService.PowerNotifOff(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(3)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(4)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                    messagingService.PowerNotifOff(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void EnergyNotification(String deviceID, Integer action, Integer estateid, String deviceName) {
        MessagingService messagingService = new MessagingService();
        Devices devices = new Devices();
        if (action.equals(2)) {
            try {
                try {
                    messagingService.EnergyNotif(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(3)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(4)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                    messagingService.EnergyNotif(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void EnergyNotificationOff(String deviceID, Integer action, Integer estateid, String deviceName) {
        MessagingService messagingService = new MessagingService();
        Devices devices = new Devices();
        if (action.equals(2)) {
            try {
                try {
                    messagingService.EnergyNotifOff(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(3)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (action.equals(4)) {
            try {
                try {
                    devices.setActive(Boolean.valueOf(false));
                    messagingService.publishState2(devices, devices);
                    messagingService.EnergyNotifOff(deviceID, estateid, deviceName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean recharge(Recharge recharge) throws MqttException {
        Devices devices = this.deviceRepository.findByDeviceid(recharge.getDeviceID());
        String rechargeToken = recharge.getToken();
        MessagingService messagingService = new MessagingService();
        if (devices != null) {
            if (messagingService.recharge(recharge, devices)) {
                return true;
            }
            throw new BadRequestException("Device not found");
        }
        return true;
    }

    public boolean rechargeWait(Recharge recharge) throws MqttException, InterruptedException {
        block3:
        {
            List rechargeHistoryModel;
            block2:
            {
                Devices devices = this.deviceRepository.findByDeviceid(recharge.getDeviceID());
                MessagingService messagingService = new MessagingService();
                rechargeHistoryModel = this.rechargeRepo.findByRechargeToken(recharge.getToken());
                if (rechargeHistoryModel == null) break block2;
                int i = 1;
                while (i < 14) {
                    System.out.println(++i);
                    Thread.sleep(3000);
                }
                break block3;
            }
            if (rechargeHistoryModel != null && !rechargeHistoryModel.isEmpty()) break block3;
            long start = 0L;
            int i = 1;
            long end = start + 10L;
            System.out.println(end);
            while (i < 14 && rechargeHistoryModel == null) {
                System.out.println(++i);
                Thread.sleep(3000);
            }
        }
        return true;
    }

    public RechargeHistoryModel rechargeResponse(RechargeHistoryModel rechargeHistoryModel) throws MqttException {
        return rechargeHistoryModel;
    }

    public boolean addAttachedDevices(Devices devices) throws InterruptedException {
        block10:
        {
            String Devices_name = devices.getDevice_name();
            String device_mac = devices.getMacaddress();
            if (Devices_name.isEmpty() && device_mac.isEmpty()) {
                throw new BadRequestException("Fill all the fields.");
            }
            Devices DevicesExists = this.deviceRepository.findByMacaddress(devices.getMacaddress());
            if (DevicesExists != null) {
                throw new BadRequestException(devices.getMacaddress() + " already registered.");
            }
            Devices unknownDevice = deviceRepository.findUnknown(devices.getEstate().getEstateid());
            if (unknownDevice == null) {
                Devices devices1 = new Devices();
                Estate estate = new Estate();
                DeviceTypes deviceTypes = new DeviceTypes();
                deviceTypes.setTypeid("5");
                devices1.setDevice_name("Unknown");
                devices1.setEstate(devices.getEstate());
                devices1.setDeviceTypes(deviceTypes);
                devices1.setMacaddress("Unknown");
                deviceRepository.save(devices1);
            }
            Estate estate = estateRepository.findByEstateid(devices.getEstate().getEstateid());
            Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
            this.deviceRepository.save(devices);
            AttachedDevices attachedDevices = new AttachedDevices();
            attachedDevices.setDeviceID(devices.getMacaddress());
            attachedDevices.setNotificationTopic(devices.getEstate().getEstateid() + "/" + devices.getMacaddress() + "/notifications");
            attachedDevices.setStateControlTopic(devices.getEstate().getEstateid() + "/" + devices.getMacaddress() + "/control/state");
            attachedDevices.setEstateID(devices.getEstate().getEstateid());
            attachedDevices.setDeviceTypeID(devices.getDeviceTypes().getTypeid());
            attachedDevices.setDeviceName(devices.getDevice_name());
            attachedDevicesRepo.save(attachedDevices);
            Devices master = deviceRepository.findAllByEstateIdAndDevice_type2(devices.getEstate().getEstateid());
            Devices DevicesExists2 = this.deviceRepository.findByMacaddress(devices.getMacaddress());
            // System.out.println(master.toString());
            Thread.sleep(1000);
            //System.out.println(DevicesExists2.toString());
            List<AttachedDevices> attached = attachedDevicesRepo.findAllByEstateID(devices.getEstate().getEstateid());
            System.out.println(attached.toString());

            try {
                byte[] key = estate.getEncryptionKeys().getEncryptionKey();
                byte[] iv1 = estate.getEncryptionKeys().getIV1();
                byte[] iv2 = estate.getEncryptionKeys().getIV2();

                String string = Base64.getEncoder().encodeToString(key);
                String string2 = Base64.getEncoder().encodeToString(iv1);
                String string3 = Base64.getEncoder().encodeToString(iv2);

                SmartcomApplication smartcomApplication = new SmartcomApplication();
                MessagingService messagingService = new MessagingService();
                SimpleMqTTCallBack simpleMqTTCallBack = new SimpleMqTTCallBack();
                String mess = "";
                //   String json = new ObjectMapper().writeValueAsString(devices);

                JSONObject jsonObject = new JSONObject();
                JSONObject attache = new JSONObject();

                JSONObject topic = new JSONObject();
                JSONObject dev = new JSONObject();
                JSONObject set = new JSONObject();
                JSONObject ta = new JSONObject();
                JSONArray serverSet = new JSONArray();
                JSONArray topics = new JSONArray();
                JSONArray deviceSettions = new JSONArray();
                //  JSONArray attachedDevices = new JSONArray();
                // attache.put(attached);
                JSONArray tariffs = new JSONArray();
                JSONObject encrypt = new JSONObject();
                JSONObject band = new JSONObject();
                topic.put("eventsTopic", master.getEstate().getEstateid() + "/timedEventsTopic");

                topic.put("meterDataTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/meterData");
                topic.put("rechargeTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/recharge");
                topic.put("stateControlTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/control/state");
                topic.put("configurationReadyTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/configurationReady");
                topic.put("notificationTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/notifications");

                set.put("serverConfigurationUrl", "http://18.228.18.217:9000/echoPost");
                set.put("mqttServerURL", "tcp://18.228.18.217:1883");
                set.put("mqttUsername", "admin");
                set.put("mqttPassword", "admin");
                dev.put("dataFrequency", estateExists.getMeterdatafreq());
                dev.put("devicePin", "1234");
                dev.put("energyThreshold", "20");
                dev.put("powerThreshold", "20");
                dev.put("creditThreshold", "20");
                encrypt.put("key", string);
                encrypt.put("iv1", string2);
                encrypt.put("iv2", string3);
                band.put("bandValue", "1");
                band.put("bandID", "B1");
                band.put("bandDescription", "Standard Rate");


                ta.put("tarrifID", "TOU7");
                ta.put("tariffDescription", "Medium Business");
                ta.put("tariffValue", "1.2");

                // tariffs.put(ta);
                jsonObject.put("band", band);
                jsonObject.put("command", "configurationRequest");
                jsonObject.put("deviceID", master.getMacaddress());
                jsonObject.put("deviceSettings", dev);
                jsonObject.put("topics", topic);
                jsonObject.put("deviceTypeID", master.getDeviceTypes().getTypeid());
                jsonObject.put("estateID", master.getEstate().getEstateid());
                jsonObject.put("encryptionSettings", encrypt);
                jsonObject.put("tariff", ta);
                jsonObject.put("serverSettings", set);
                jsonObject.put("attachedDevices", attachedDevicesRepo.findAllByEstateID(devices.getEstate().getEstateid()));
                if (!messagingService.publish(master)) break block10;
                try {
                    File myObj = new File(master.getMacaddress() + ".txt");
                    if (myObj.createNewFile()) {
                        System.out.println("File created: " + myObj.getName());
                    } else {
                        System.out.println("File already exists." + master.getMacaddress().toString());
                        FileWriter myWriter = new FileWriter(master.getMacaddress() + ".txt");
                        myWriter.write("");
                        myWriter.write(String.valueOf((Object) jsonObject));
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                FileWriter myWriter = new FileWriter(master.getMacaddress() + ".txt");
                myWriter.write(String.valueOf((Object) jsonObject));
                myWriter.close();
                Object socket = null;
                devices.setEnabled(false);
                devices.setEnergyUsed(0.0);
                devices.setCostToday(0.0);
                // this.deviceRepository.save(devices);
            } catch (MqttPersistenceException smartcomApplication) {
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        return true;

    }

    public boolean updateAttachedDevices(Devices device) throws IOException, MqttException {
        Devices devices = this.deviceRepository.findByDeviceid(device.getDeviceid());
        AttachedDevices attachedDevices = attachedDevicesRepo.findByDeviceID(device.getMacaddress());
        Estate estate = estateRepository.findByEstateid(device.getEstate().getEstateid());
        Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
        if (devices == null) {
            throw new BadRequestException(device.getDeviceid() + " is not registered.");
        }
        String deviceName = device.getDevice_name();
        Integer credit = device.getCredit();
        Integer creditAction = device.getCreditAction();
        Integer power = device.getPower();
        Integer powerAction = device.getPowerAction();
        Integer energy = device.getEnergy();
        Integer energyAction = device.getEnergyAction();
        devices.setCredit(credit);
        devices.setCreditAction(creditAction);
        devices.setEnergy(energy);
        devices.setEnergyAction(energyAction);
        devices.setPower(power);
        devices.setPowerAction(powerAction);
        devices.setDevice_name(deviceName);
        //    AttachedDevices attachedDevices = new AttachedDevices();
        // attachedDevices.setMacaddress(devices.getMacaddress());
        //  attachedDevices.setNotificationTopic(devices.getEstate().getEstateid()+"/"+devices.getMacaddress()+"/notifications");
        //  attachedDevices.setStateControlTopic(devices.getEstate().getEstateid()+"/"+devices.getMacaddress()+"/control/state");
        //attachedDevices.setEstateid(devices.getEstate().getEstateid());
        //   attachedDevices.setTypeID(devices.getDeviceTypes().getTypeid());
        attachedDevices.setCreditThreshold(credit);
        attachedDevices.setEnergyThreshold(energy);
        attachedDevices.setPowerThreshold(power);
        attachedDevicesRepo.save(attachedDevices);
        List<AttachedDevices> attached = attachedDevicesRepo.findAllByEstateID(devices.getEstate().getEstateid());

        Devices master = deviceRepository.findAllByEstateIdAndDevice_type2(devices.getEstate().getEstateid());
        try {
            SmartcomApplication smartcomApplication = new SmartcomApplication();
            MessagingService messagingService = new MessagingService();
            SimpleMqTTCallBack simpleMqTTCallBack = new SimpleMqTTCallBack();
            String mess = "";
            String json = new ObjectMapper().writeValueAsString((Object) devices);
            byte[] key = estate.getEncryptionKeys().getEncryptionKey();
            byte[] iv1 = estate.getEncryptionKeys().getIV1();
            byte[] iv2 = estate.getEncryptionKeys().getIV2();

            String string = Base64.getEncoder().encodeToString(key);
            String string2 = Base64.getEncoder().encodeToString(iv1);
            String string3 = Base64.getEncoder().encodeToString(iv2);


            JSONObject jsonObject = new JSONObject();
            JSONObject attachedJson = new JSONObject();

            JSONObject topic = new JSONObject();
            JSONObject dev = new JSONObject();
            JSONObject set = new JSONObject();
            JSONObject ta = new JSONObject();
            JSONArray serverSet = new JSONArray();
            JSONArray topics = new JSONArray();
            JSONArray deviceSettions = new JSONArray();
            //  JSONArray attachedDevices = new JSONArray();
            //    attachedDevices.put(attached);
            JSONArray tariffs = new JSONArray();
            JSONObject encrypt = new JSONObject();
            JSONObject band = new JSONObject();
            topic.put("meterDataTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/meterData");
            topic.put("eventsTopic", master.getEstate().getEstateid() + "/timedEventsTopic");

            topic.put("rechargeTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/recharge");
            topic.put("stateControlTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/control/state");
            topic.put("configurationReadyTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/configurationReady");
            topic.put("notificationTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/notifications");

            set.put("serverConfigurationUrl", "http://18.228.18.217:9000/echoPost");
            set.put("mqttServerURL", "tcp://18.228.18.217:1883");
            set.put("mqttUsername", "admin");
            set.put("mqttPassword", "admin");
            dev.put("dataFrequency", estateExists.getMeterdatafreq());
            dev.put("devicePin", "1234");
            dev.put("energyThreshold", "20");
            dev.put("powerThreshold", "20");
            dev.put("creditThreshold", "20");
            encrypt.put("key", string);
            encrypt.put("iv1", string2);
            encrypt.put("iv2", string3);
            band.put("bandValue", "1");
            band.put("bandID", "B1");
            band.put("bandDescription", "Standard Rate");


            ta.put("tarrifID", "TOU7");
            ta.put("tariffDescription", "Medium Business");
            ta.put("tariffValue", "1.2");


            jsonObject.put("band", band);
            jsonObject.put("command", "configurationRequest");
            jsonObject.put("deviceID", master.getMacaddress());
            jsonObject.put("deviceSettings", dev);
            jsonObject.put("topics", topic);
            jsonObject.put("deviceTypeID", master.getDeviceTypes().getTypeid());
            jsonObject.put("estateID", master.getEstate().getEstateid());
            jsonObject.put("encryptionSettings", encrypt);
            jsonObject.put("tariff", ta);
            jsonObject.put("serverSettings", set);
            jsonObject.put("attachedDevices", attached);
            if (messagingService.publish(master)) {
                try {
                    File myObj = new File(master.getMacaddress() + ".txt");
                    if (myObj.createNewFile()) {
                        System.out.println("File created: " + myObj.getName());
                    } else {
                        FileWriter myWriter = new FileWriter(master.getMacaddress() + ".txt");
                        myWriter.write("");
                    }
                    System.out.println("File already exists.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                }
                FileWriter myWriter = new FileWriter(master.getMacaddress() + ".txt");
                myWriter.write(String.valueOf((Object) jsonObject));
                myWriter.close();
                deviceRepository.save(devices);
            }
        } finally {
            return true;
        }
    }

    public boolean deletaAttcahed(Devices device) throws IOException, MqttException {
        Devices devices = this.deviceRepository.findByDeviceid(device.getDeviceid());
        AttachedDevices attachedDevices = attachedDevicesRepo.findByDeviceID(device.getMacaddress());
        Estate estate = estateRepository.findByEstateid(device.getEstate().getEstateid());
        Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
        if (devices == null) {
            throw new BadRequestException(device.getDeviceid() + " is not registered.");
        }
        deviceRepository.findByDeviceIdDelete(devices.getDeviceid());
        attachedDevicesRepo.findByAttachedDevice(devices.getMacaddress());


      /*  String deviceName = device.getDevice_name();
        Integer credit = device.getCredit();
        Integer creditAction = device.getCreditAction();
        Integer power = device.getPower();
        Integer powerAction = device.getPowerAction();
        Integer energy = device.getEnergy();
        Integer energyAction = device.getEnergyAction();
        devices.setCredit(credit);
        devices.setCreditAction(creditAction);
        devices.setEnergy(energy);
        devices.setEnergyAction(energyAction);
        devices.setPower(power);
        devices.setPowerAction(powerAction);
        devices.setDevice_name(deviceName);*/
        //    AttachedDevices attachedDevices = new AttachedDevices();
        // attachedDevices.setMacaddress(devices.getMacaddress());
        //  attachedDevices.setNotificationTopic(devices.getEstate().getEstateid()+"/"+devices.getMacaddress()+"/notifications");
        //  attachedDevices.setStateControlTopic(devices.getEstate().getEstateid()+"/"+devices.getMacaddress()+"/control/state");
        //attachedDevices.setEstateid(devices.getEstate().getEstateid());
        //   attachedDevices.setTypeID(devices.getDeviceTypes().getTypeid());
    /*    attachedDevices.setCreditThreshold(credit);
        attachedDevices.setEnergyThreshold(energy);
        attachedDevices.setPowerThreshold(power);
        attachedDevicesRepo.save(attachedDevices);*/
        List<AttachedDevices> attached = attachedDevicesRepo.findAllByEstateID(devices.getEstate().getEstateid());

        Devices master = deviceRepository.findAllByEstateIdAndDevice_type2(devices.getEstate().getEstateid());
        try {
            SmartcomApplication smartcomApplication = new SmartcomApplication();
            MessagingService messagingService = new MessagingService();
            SimpleMqTTCallBack simpleMqTTCallBack = new SimpleMqTTCallBack();
            String mess = "";
            String json = new ObjectMapper().writeValueAsString((Object) devices);
            byte[] key = estate.getEncryptionKeys().getEncryptionKey();
            byte[] iv1 = estate.getEncryptionKeys().getIV1();
            byte[] iv2 = estate.getEncryptionKeys().getIV2();

            String string = Base64.getEncoder().encodeToString(key);
            String string2 = Base64.getEncoder().encodeToString(iv1);
            String string3 = Base64.getEncoder().encodeToString(iv2);


            JSONObject jsonObject = new JSONObject();
            JSONObject attachedJson = new JSONObject();

            JSONObject topic = new JSONObject();
            JSONObject dev = new JSONObject();
            JSONObject set = new JSONObject();
            JSONObject ta = new JSONObject();
            JSONArray serverSet = new JSONArray();
            JSONArray topics = new JSONArray();
            JSONArray deviceSettions = new JSONArray();
            //  JSONArray attachedDevices = new JSONArray();
            //    attachedDevices.put(attached);
            JSONArray tariffs = new JSONArray();
            JSONObject encrypt = new JSONObject();
            JSONObject band = new JSONObject();
            topic.put("meterDataTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/meterData");
            topic.put("eventsTopic", master.getEstate().getEstateid() + "/timedEventsTopic");

            topic.put("rechargeTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/recharge");
            topic.put("stateControlTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/control/state");
            topic.put("configurationReadyTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/configurationReady");
            topic.put("notificationTopic", master.getEstate().getEstateid() + "/" + master.getMacaddress() + "/notifications");

            set.put("serverConfigurationUrl", "http://18.228.18.217:9000/echoPost");
            set.put("mqttServerURL", "tcp://18.228.18.217:1883");
            set.put("mqttUsername", "admin");
            set.put("mqttPassword", "admin");
            dev.put("dataFrequency", estateExists.getMeterdatafreq());
            dev.put("devicePin", "1234");
            dev.put("energyThreshold", "20");
            dev.put("powerThreshold", "20");
            dev.put("creditThreshold", "20");
            encrypt.put("key", string);
            encrypt.put("iv1", string2);
            encrypt.put("iv2", string3);
            band.put("bandValue", "1");
            band.put("bandID", "B1");
            band.put("bandDescription", "Standard Rate");


            ta.put("tarrifID", "TOU7");
            ta.put("tariffDescription", "Medium Business");
            ta.put("tariffValue", "1.2");


            jsonObject.put("band", band);
            jsonObject.put("command", "configurationRequest");
            jsonObject.put("deviceID", master.getMacaddress());
            jsonObject.put("deviceSettings", dev);
            jsonObject.put("topics", topic);
            jsonObject.put("deviceTypeID", master.getDeviceTypes().getTypeid());
            jsonObject.put("estateID", master.getEstate().getEstateid());
            jsonObject.put("encryptionSettings", encrypt);
            jsonObject.put("tariff", ta);
            jsonObject.put("serverSettings", set);
            jsonObject.put("attachedDevices", attached);
            if (messagingService.publish(master)) {
                try {
                    File myObj = new File(master.getMacaddress() + ".txt");
                    if (myObj.createNewFile()) {
                        System.out.println("File created: " + myObj.getName());
                    } else {
                        FileWriter myWriter = new FileWriter(master.getMacaddress() + ".txt");
                        myWriter.write("");
                    }
                    System.out.println("File already exists.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                }
                FileWriter myWriter = new FileWriter(master.getMacaddress() + ".txt");
                myWriter.write(String.valueOf((Object) jsonObject));
                myWriter.close();
                deviceRepository.save(devices);
            }
        } finally {
            return true;
        }
    }

    public void run(Devices devices) {
        Devices device = new Devices();
        int i = 0;
        while (!isCancelled && devices.getActive() != device.getActive() && i < 10) {
            device = this.deviceRepository.findByDeviceid(devices.getDeviceid());

            System.out.println("Thread is running with all its might!");

            try {
                Thread.sleep(1000);
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void cancel() {
        isCancelled = true;

    }

    public boolean addDevices(Devices devices) throws JsonProcessingException {
        block10:
        {
            String Devices_name = devices.getDevice_name();
            String device_mac = devices.getMacaddress();
            if (Devices_name.isEmpty() && device_mac.isEmpty()) {
                throw new BadRequestException("Fill all the fields.");
            }

            Devices DevicesExists = deviceRepository.findByMacaddress(devices.getMacaddress());

            //  Devices deviceExists = deviceRepository.findByDeviceid(devices.getDeviceid());
            //  User user = userRepository.findByUserid(estateExists.getUser().getUserid());
            if (DevicesExists != null) {
                throw new BadRequestException(devices.getMacaddress() + " already registered.");
            }
            Estate estate = estateRepository.findByEstateid(devices.getEstate().getEstateid());
            System.out.println(estate.getEstatename());
            //  Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());

            try {
                byte[] key = estate.getEncryptionKeys().getEncryptionKey();
                byte[] iv1 = estate.getEncryptionKeys().getIV1();
                byte[] iv2 = estate.getEncryptionKeys().getIV2();

                String string = Base64.getEncoder().encodeToString(key);
                String string2 = Base64.getEncoder().encodeToString(iv1);
                String string3 = Base64.getEncoder().encodeToString(iv2);

                SmartcomApplication smartcomApplication = new SmartcomApplication();
                MessagingService messagingService = new MessagingService();
                SimpleMqTTCallBack simpleMqTTCallBack = new SimpleMqTTCallBack();
                String mess = "";
                String json = new ObjectMapper().writeValueAsString(devices);

                JSONObject jsonObject = new JSONObject();
                JSONObject attache = new JSONObject();

                JSONObject topic = new JSONObject();
                JSONObject dev = new JSONObject();
                JSONObject set = new JSONObject();
                JSONObject ta = new JSONObject();
                JSONArray serverSet = new JSONArray();
                JSONArray topics = new JSONArray();
                JSONArray deviceSettions = new JSONArray();
                JSONArray attachedDevices = new JSONArray();
                // attache.put(attached);
                JSONArray tariffs = new JSONArray();
                JSONObject encrypt = new JSONObject();
                JSONObject band = new JSONObject();
                topic.put("eventsTopic", devices.getEstate().getEstateid() + "/timedEventsTopic");

                topic.put("meterDataTopic", devices.getEstate().getEstateid() + "/" + device_mac + "/meterData");
                topic.put("rechargeTopic", devices.getEstate().getEstateid() + "/" + device_mac + "/recharge");
                topic.put("stateControlTopic", devices.getEstate().getEstateid() + "/" + device_mac + "/control/state");
                topic.put("configurationReadyTopic", devices.getEstate().getEstateid() + "/" + device_mac + "/configurationReady");
                topic.put("notificationTopic", devices.getEstate().getEstateid() + "/" + device_mac + "/notifications");

                set.put("serverConfigurationUrl", "http://18.228.18.217:9000/echoPost");
                set.put("mqttServerURL", "tcp://18.228.18.217:1883");
                set.put("mqttUsername", "admin");
                set.put("mqttPassword", "admin");
                dev.put("dataFrequency", estate.getMeterdatafreq());
                dev.put("devicePin", "1234");
                dev.put("energyThreshold", "20");
                dev.put("powerThreshold", "20");
                dev.put("creditThreshold", "20");
                encrypt.put("key", string);
                encrypt.put("iv1", string2);
                encrypt.put("iv2", string3);
                band.put("bandValue", "1");
                band.put("bandID", "B1");
                band.put("bandDescription", "Standard Rate");


                ta.put("tarrifID", "TOU7");
                ta.put("tariffDescription", "Medium Business");
                ta.put("tariffValue", "1.2");

                // tariffs.put(ta);
                jsonObject.put("band", band);
                jsonObject.put("command", "configurationRequest");
                jsonObject.put("deviceID", device_mac);
                jsonObject.put("deviceSettings", dev);
                jsonObject.put("topics", topic);
                jsonObject.put("deviceTypeID", devices.getDeviceTypes().getTypeid());
                jsonObject.put("estateID", devices.getEstate().getEstateid());
                jsonObject.put("encryptionSettings", encrypt);
                jsonObject.put("tariff", ta);
                jsonObject.put("serverSettings", set);
                jsonObject.put("attachedDevices", attachedDevices);
                if (!messagingService.publish(devices)) break block10;
                try {
                    File myObj = new File(device_mac + ".txt");
                    if (myObj.createNewFile()) {
                        System.out.println("File created: " + myObj.getName());
                    } else {
                        System.out.println("File already exists.");
                        FileWriter myWriter = new FileWriter(device_mac + ".txt");
                        myWriter.write("");
                        myWriter.write(String.valueOf((Object) jsonObject));
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                FileWriter myWriter = new FileWriter(device_mac + ".txt");
                myWriter.write(String.valueOf((Object) jsonObject));
                myWriter.close();
                Object socket = null;
                devices.setEnabled(false);
                devices.setHasEvent(false);
                devices.setCostToday(0.0);
                devices.setEnergyUsed(0.0);
                this.deviceRepository.save(devices);
            } catch (MqttPersistenceException smartcomApplication) {
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}

