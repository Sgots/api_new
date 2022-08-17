/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.controller.SpringBootJdbcController
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.jdbc.core.JdbcTemplate
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.smartcom.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringBootJdbcController {
    @Autowired
    JdbcTemplate jdbc;

    @RequestMapping(value = {"/insert"})
    public String index() {
        this.jdbc.execute("insert into device_types(type)values('IP Hub'), ('IP Smart Meter'), ('Smart Meter'), ('Smart Socket')");
        return "data inserted Successfully";
    }
}

