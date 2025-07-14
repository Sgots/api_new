/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.SmartcomApplication
 *  com.smartcom.api.serverService.HTTPServerClass
 *  com.smartcom.api.service.MqttSubscriber
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.boot.web.servlet.support.SpringBootServletInitializer
 *  org.springframework.web.bind.annotation.GetMapping
 */
package com.smartcom.api;

import com.smartcom.api.config.ConfigReader;
import com.smartcom.api.serverService.HTTPServerClass;
import com.smartcom.api.service.DeviceService;
import com.smartcom.api.service.MqttSubscriber;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import com.smartcom.api.service.MqttSubscriberPool;
import com.smartcom.api.service.MqttSubscriberTest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Component
public class SmartcomApplication
        extends SpringBootServletInitializer {
    ServerSocket server_socket;
    public int PORT = 7777;
    Socket socket;
    PrintWriter print_writer;
    BufferedReader buffered_reader;
    String clientSentence;

    @GetMapping(value = {"/"})
    public String Rest() {
        return "Hello";
    }

    @Value(value = "${mqttServerUrl}")
    private static String broker;

@Autowired
private static MqttSubscriberPool mqttSubscriberPool;

    private static ConfigReader configReader = new ConfigReader();
    private static Properties properties = new Properties(configReader.init_prop());
    public static String mqttServerUrl = properties.getProperty("mqttServerUrl");
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SmartcomApplication.class, (String[]) args);
        HTTPServerClass server = new HTTPServerClass();
        DeviceService deviceService = new DeviceService();
        Thread.sleep(1000);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:8080/start/3")
                .build();
        Response response = client.newCall(request).execute();

  /*      URL url = new URL("http://localhost:8080/start/3");
        URLConnection connection = url.openConnection();
        connection.connect();*/
     //   deviceService.runStartUpCmd();

     /*   MqttClient pubClient = new MqttClient(mqttServerUrl, MqttClient.generateClientId());
        MqttConnectOptions connOpt = new MqttConnectOptions();
        connOpt.setAutomaticReconnect(true);
        connOpt.setCleanSession(true);
        System.out.println("Connecting to broker: " + mqttServerUrl);
        pubClient.connect(connOpt);
        pubClient.subscribe("#", new MqttSubscriberTest());
        SmartcomApplication smartcomApplication = new SmartcomApplication();*/

    }

}

