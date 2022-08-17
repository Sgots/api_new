/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.service.TestService
 */
package com.smartcom.api.service;

public class TestService {
    public TestService(String response) {
    }

    public boolean test(String response) {
        return response.equals("HTTP/1.1 200");
    }
}

