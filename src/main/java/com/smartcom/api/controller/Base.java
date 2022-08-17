/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.controller.Base
 *  com.smartcom.api.repository.UserRepository
 *  com.smartcom.api.repository.UserToEstateRepository
 *  com.smartcom.api.service.EmailService
 *  com.smartcom.api.service.EstateService
 *  com.smartcom.api.service.UserService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.smartcom.api.controller;

import com.smartcom.api.repository.UserRepository;
import com.smartcom.api.repository.UserToEstateRepository;
import com.smartcom.api.service.EmailService;
import com.smartcom.api.service.EstateService;
import com.smartcom.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Base {
    @Autowired
    private UserService userService;
    @Autowired
    private EstateService estateService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserToEstateRepository userToEstateRepository;
    @Autowired
    private EmailService emailService;
    @Value(value = "${webServerUrl}")
    private String webServerUrl;

    @RequestMapping(value = {"/"})
    public String Rest() {
        return "Hello";
    }
}

