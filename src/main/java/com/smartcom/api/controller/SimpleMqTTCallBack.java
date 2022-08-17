/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.controller.SimpleMqTTCallBack
 *  com.smartcom.api.model.User
 *  com.smartcom.api.service.UserService
 *  org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
 *  org.eclipse.paho.client.mqttv3.MqttCallback
 *  org.eclipse.paho.client.mqttv3.MqttMessage
 */
package com.smartcom.api.controller;

import com.smartcom.api.model.User;
import com.smartcom.api.service.UserService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SimpleMqTTCallBack
        implements MqttCallback {
    private static String mess = "";

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        User user = new User();
        if (mqttMessage.equals("Over")) {
            UserService userService = new UserService();
            user.setStatus(true);
            userService.registerUser(user);
            return;
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}

