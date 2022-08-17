/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.RechargeHistoryModel
 *  com.smartcom.api.service.DeviceService
 *  com.smartcom.api.service.SimpleMqTTCallBack
 *  org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
 *  org.eclipse.paho.client.mqttv3.MqttCallback
 *  org.eclipse.paho.client.mqttv3.MqttMessage
 *  org.json.JSONObject
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.smartcom.api.service;

import com.smartcom.api.model.RechargeHistoryModel;
import com.smartcom.api.service.DeviceService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleMqTTCallBack
        implements MqttCallback {
    @Autowired
    public RechargeHistoryModel rechargeHistoryModel;
    @Autowired
    public DeviceService deviceService;

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("Message received:\t" + new String(mqttMessage.getPayload()));
        JSONObject json = new JSONObject(mqttMessage.toString());
        while (json != null) {
            Double remainingCredit = json.getDouble("remainingCredit");
            Double remainingEnergy = json.getDouble("remainingEnergy");
            String deviceID = json.getString("deviceID");
            String status = json.getString("status");
            this.rechargeHistoryModel.setDeviceID(deviceID);
            this.rechargeHistoryModel.setStatus(status);
            this.rechargeHistoryModel.setRemainingCredit(remainingCredit);
            this.rechargeHistoryModel.setRemainingEnergy(remainingEnergy);
            this.deviceService.rechargeResponse(this.rechargeHistoryModel);
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}

