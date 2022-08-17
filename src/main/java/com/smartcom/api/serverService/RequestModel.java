/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.serverService.RequestModel
 */
package com.smartcom.api.serverService;

public class RequestModel {
    String deviceID = "";
    String command = "";

    public String getDeviceID() {
        return this.deviceID;
    }

    public String getCommand() {
        return this.command;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}

