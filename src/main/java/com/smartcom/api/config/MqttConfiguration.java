/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.config.MqttConfiguration
 *  org.eclipse.paho.client.mqttv3.IMqttClient
 *  org.eclipse.paho.client.mqttv3.MqttClient
 *  org.eclipse.paho.client.mqttv3.MqttConnectOptions
 *  org.eclipse.paho.client.mqttv3.MqttException
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.boot.context.properties.ConfigurationProperties
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 */
package com.smartcom.api.config;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "mqtt")
    public MqttConnectOptions mqttConnectOptions() {
        return new MqttConnectOptions();
    }

  /*  @Bean
    public IMqttClient mqttClient(@Value(value="1") String clientId, @Value(value="18.228.18.217") String hostname, @Value(value="1883") int port) throws MqttException {
        MqttClient mqttClient = new MqttClient("tcp://" + hostname + ":" + port, clientId);
        mqttClient.connect(this.mqttConnectOptions());
        return mqttClient;
    }*/
}

