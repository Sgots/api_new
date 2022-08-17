/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.controller.MonitoringController
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.servlet.mvc.method.RequestMappingInfo
 *  org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
 */
package com.smartcom.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestController
@RequestMapping(value = {"/monitoring/"})
public class MonitoringController {
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @GetMapping(value = {"endpoints"})
    public ResponseEntity<List<String>> getEndpoints() {
        return new ResponseEntity(this.requestMappingHandlerMapping.getHandlerMethods().keySet().stream().map(RequestMappingInfo::toString).collect(Collectors.toList()), HttpStatus.OK);
    }
}

