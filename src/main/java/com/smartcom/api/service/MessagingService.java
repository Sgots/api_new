/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.smartcom.api.model.Devices
 *  com.smartcom.api.model.Recharge
 *  com.smartcom.api.service.MessagingService
 *  org.eclipse.paho.client.mqttv3.MqttClient
 *  org.eclipse.paho.client.mqttv3.MqttException
 *  org.eclipse.paho.client.mqttv3.MqttMessage
 *  org.eclipse.paho.client.mqttv3.MqttPersistenceException
 *  org.springframework.stereotype.Service
 */
package com.smartcom.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smartcom.api.config.ConfigReader;
import com.smartcom.api.model.Devices;
import com.smartcom.api.model.Events;
import com.smartcom.api.model.Recharge;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


@Service
public class MessagingService {
    private static ConfigReader configReader = new ConfigReader();
    private static Properties properties = new Properties(configReader.init_prop());
    public  String mqttServerUrl = properties.getProperty("mqttServerUrl");

    public boolean EnergyNotif(String deviceID, Integer estateid, String deviceName) throws MqttPersistenceException, MqttException, JsonProcessingException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = estateid + "/" + deviceID + "/EnergyNotification";
        String payload = "{\"command\":\"notification\", \"deviceID\":\"" + deviceID + "\", \"status\":\"Energy usage high " + deviceName + "\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return false;
    }

    public boolean EnergyNotifOff(String deviceID, Integer estateid, String deviceName) throws MqttPersistenceException, MqttException, JsonProcessingException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = estateid + "/" + deviceID + "/EnergyNotification";
        String payload = "{\"command\":\"notification\", \"deviceID\":\"" + deviceID + "\", \"status\":\"Energy okay " + deviceName + "\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return false;
    }

    public boolean PowerNotif(String deviceID, Integer estateid, String deviceName) throws MqttPersistenceException, MqttException, JsonProcessingException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = estateid + "/" + deviceID + "/PowerNotification";
        String payload = "{\"command\":\"notification\", \"deviceID\":\"" + deviceID + "\", \"status\":\"Power usage high in " + deviceName + "\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return false;
    }

    public boolean PowerNotifOff(String deviceID, Integer estateid, String deviceName) throws MqttPersistenceException, MqttException, JsonProcessingException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = estateid + "/" + deviceID + "/PowerNotification";
        String payload = "{\"command\":\"notification\", \"deviceID\":\"" + deviceID + "\", \"status\":\"Power okay in " + deviceName + "\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return false;
    }

    public boolean CreditNotif(String deviceID, Integer estateid, String deviceName) throws MqttPersistenceException, MqttException, JsonProcessingException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = estateid + "/" + deviceID + "/CreditNotification";
        String payload = "{\"command\":\"notification\", \"deviceID\":\"" + deviceID + "\", \"status\":\"Credit low in " + deviceName + "\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return false;
    }

    public boolean CreditNotifOff(String deviceID, Integer estateid, String deviceName) throws MqttPersistenceException, MqttException, JsonProcessingException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = estateid + "/" + deviceID + "/CreditNotification";
        String payload = "{\"command\":\"notification\", \"deviceID\":\"" + deviceID + "\", \"status\":\"Credit okay in " + deviceName + "\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return false;
    }

    public boolean publish(Devices devices) throws MqttPersistenceException, MqttException, JsonProcessingException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = devices.getEstate().getEstateid() + "/" + devices.getMacaddress() + "/configurationReady";
        String payload = "{\"command\":\"configurationReady\", \"deviceID\":\"" + devices.getMacaddress() + "\", \"status\":\"1\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return true;
    }

    public boolean publishStateOFF(Devices devices, String mac) throws MqttException, JsonProcessingException {

        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = devices.getEstate().getEstateid() + "/" + mac + "/control/state";
        String payload = "{\"command\":\"controlRequest\", \"state\": \"OFF\",\"deviceID\": \"" + devices.getMacaddress() + "\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();

        return true;
    }

    public boolean publishStateOn(Devices devices, String mac) throws MqttException, JsonProcessingException {

        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = devices.getEstate().getEstateid() + "/" + mac + "/control/state";
        String payload = "{\"command\":\"controlRequest\", \"state\": \"ON\",\"deviceID\": \"" + devices.getMacaddress() + "\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();

        return true;
    }

    public boolean publishState2(Devices devices, Devices devices1) throws MqttException, JsonProcessingException {
        if (devices1.getActive()) {
            MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
            client.connect();
            String topic = devices.getEstate().getEstateid() + "/" + devices.getMacaddress() + "/control/state";
            String payload = "{\"command\":\"controlRequest\", \"state\": \"ON\",\"deviceID\": \"" + devices.getMacaddress() + "\" }";
            int qos = 0;
            boolean retained = false;
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(payload.getBytes());
            mqttMessage.setQos(qos);
            mqttMessage.setRetained(retained);
            client.publish(topic, mqttMessage);
            client.disconnect();
        } else if (!devices1.getActive()) {
            MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
            client.connect();
            String topic = devices.getEstate().getEstateid() + "/" + devices.getMacaddress() + "/control/state";
            String payload = "{\"command\":\"controlRequest\", \"state\": \"OFF\",\"deviceID\": \"" + devices.getMacaddress() + "\" }";
            int qos = 0;
            boolean retained = false;
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(payload.getBytes());
            mqttMessage.setQos(qos);
            mqttMessage.setRetained(retained);
            client.publish(topic, mqttMessage);
            client.disconnect();
        }
        return true;
    }

    public boolean recharge(Recharge recharge, Devices devices) throws MqttException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = devices.getEstate().getEstateid() + "/" + devices.getMacaddress() + "/recharge";
        String payload = "{\"command\":\"rechargeRequest\", \"rechargeToken\":'" + recharge.getToken() + "', \"deviceID\":'" + devices.getMacaddress() + "' }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return true;
    }

    public boolean rechargeNotification(String deviceID, Integer estateid) throws MqttException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = estateid + "/" + deviceID + "/notifications/recharge";
        String payload = "{\"command\":\"notification\", \"deviceID\":\"" + deviceID + "\", \"status\":\"Recharge succesful\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return true;
    }

    public boolean rechargeNotificationFail(String deviceID, Integer estateid) throws MqttException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = estateid + "/" + deviceID + "/notifications/recharge";
        String payload = "{\"command\":\"notification\", \"deviceID\":\"" + deviceID + "\", \"status\":\"Recharge failed\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return true;
    }

    public boolean announcementAlert(String title) throws MqttException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        String deviceID = "Announcement";
        client.connect();
        String topic = "announcement/alert";
        String payload = "{\"command\":\"notification\", \"deviceID\":\"" + deviceID + "\", \"status\":\"ALERT:" + title + "\" }";
        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return true;
    }

    public boolean publishEvent(List<Events> events, Integer estateid, String deviceID) throws MqttException {
        MqttClient client = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        client.connect();
        String topic = estateid + "/timedEventsTopic";
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject.put("command", "timedEventsRequest");
        // JSONArray jsonArray = new JSONArray();
      /*  for (Events events1 : events) {
            String days = events1.getRecurringDays().toString();
            days = days.replace("{","").replace("}","");
            String days2[] = days.split(",");
            System.out.println(Arrays.toString(days2));

            //System.out.println(days2);
        events1.setRecurringDays(Arrays.toString(days2).replace("{","").replace("}",""));

        }*/
        // jsonArray.put(events);
        jsonObject.put("events", events);
        jsonObject.put("deviceID", deviceID);

        int qos = 0;
        boolean retained = false;
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(jsonObject.toString().getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        client.publish(topic, mqttMessage);
        client.disconnect();
        return true;
    }

}

