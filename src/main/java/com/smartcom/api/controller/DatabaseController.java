/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.controller.DatabaseController
 *  com.smartcom.api.service.DatabaseSchema
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.smartcom.api.controller;

import com.smartcom.api.service.DatabaseSchema;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {
    @RequestMapping(value = {"/rechargeDB"})
    public void Rest() throws SQLException, ClassNotFoundException {
        DatabaseSchema databaseSchema = new DatabaseSchema();
        databaseSchema.rechargeResponse();
    }
}

