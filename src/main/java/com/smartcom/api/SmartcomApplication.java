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

import com.smartcom.api.serverService.HTTPServerClass;
import com.smartcom.api.service.MqttSubscriber;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SmartcomApplication.class, (String[]) args);
        HTTPServerClass server = new HTTPServerClass();
        MqttSubscriber mqttSubscriber = new MqttSubscriber();
        mqttSubscriber.run();
        SmartcomApplication smartcomApplication = new SmartcomApplication();
    }

}

