/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.controller.UserController
 *  com.smartcom.api.exception.BadRequestException
 *  com.smartcom.api.model.User
 *  com.smartcom.api.repository.UserRepository
 *  com.smartcom.api.repository.UserToEstateRepository
 *  com.smartcom.api.service.EmailService
 *  com.smartcom.api.service.UserService
 *  javax.validation.Valid
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.mail.SimpleMailMessage
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.smartcom.api.controller;

import com.smartcom.api.exception.BadRequestException;
import com.smartcom.api.model.Devices;
import com.smartcom.api.model.Estate;
import com.smartcom.api.model.Events;
import com.smartcom.api.model.User;
import com.smartcom.api.repository.*;
import com.smartcom.api.service.EmailService;
import com.smartcom.api.service.UserService;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600L)
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EncryptRepo encryptRepo;
    @Autowired
    private PasswordTrackerRepo passwordTrackerRepo;
    @Autowired
    private AttachedDevicesRepo attachedDevicesRepo;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EstateRepository estateRepository;
    @Autowired
    private UserToEstateRepository userToEstateRepository;
    @Autowired
    private NotifyRepo notifyRepo;
    @Autowired
    private EventsRepo eventsRepo;
    @Autowired
    private EmailService emailService;
    @Value(value = "${webServerUrl}")
    private String webServerUrl;
    private BadRequestException BadRequestException;

    @GetMapping(path = {"/users"})
    public List<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping(value = {"/users/{user_id}"})
    public ResponseEntity<?> getUser(@PathVariable(value = "user_id") Integer userid) {
        User user = this.userRepository.findByUserid(userid);
        if (user == null) {
            return new ResponseEntity((Object) ("No User found with this " + userid), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("user", user));
    }

    @PostMapping(path = {"/users/register"})
    public ResponseEntity<Object> register(@Valid @RequestBody User user) throws SQLIntegrityConstraintViolationException {
        if (this.userService.registerUser(user)) {
            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Registration Confirmation");
            registrationEmail.setText("To confirm your e-mail address, please click the link below:\n" + this.webServerUrl + "/users/confirm?token=" + user.getConfirmationToken());
            registrationEmail.setFrom("support@nimbusengineering.co.bw");
         // this.emailService.sendEmail(registrationEmail);
            return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("firstname", user.getFirstName(), "lastname", user.getLastName(), "error", "false", "id", user.getUserid(), "user", user));
        }
        return ResponseEntity.status((HttpStatus) HttpStatus.CREATED).build();
    }

    @PostMapping(path = {"/users/login"})
    public ResponseEntity<?> login(@Valid @RequestBody User user) {
        User userExists = this.userService.loginUser(user);
        //   this.userRepository.findAllByMember(userExists);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("user", userExists, "error", "false"));
    }

    @PutMapping(value = {"/updateEstateCount"})
    public ResponseEntity<?> updateUserCount(@RequestBody User user) {
        if (this.userRepository.findByUserid(user.getUserid()) == null) {
            return new ResponseEntity((Object) ("Unable to update as  User id " + user.getUserid() + " not found."), HttpStatus.NOT_FOUND);
        }
        this.userRepository.findByUseridAndUpdate(user.getUserid());
        return new ResponseEntity((Object) user, HttpStatus.OK);
    }

    @PutMapping(value = {"/updateEstateCountNegative"})
    public ResponseEntity<?> updateUserCountNegative(@RequestBody User user) {
        if (this.userRepository.findByUserid(user.getUserid()) == null) {
            return new ResponseEntity((Object) ("Unable to update as  User id " + user.getUserid() + " not found."), HttpStatus.NOT_FOUND);
        }
        this.userRepository.findByUseridAndDeleteEstate(user.getUserid());
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("user", user));
    }

    @PutMapping(value = {"/users/update"})
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        if (this.userRepository.findByUserid(user.getUserid()) == null) {
            return new ResponseEntity((Object) ("Unable to update as  User id " + user.getUserid() + " not found."), HttpStatus.NOT_FOUND);
        }
        Integer userid = user.getUserid();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        this.userRepository.findByUserUpdate(userid, firstName, lastName, email);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("user", user));
    }

    @PostMapping(path = {"/users/inviteUser"})
    public ResponseEntity<Object> InviteUser(@Valid @RequestBody User user) {
        if (this.userService.inviteUser(user)) {
            User resetUser = this.userService.resetUser(user);
            return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "user", user));
        }
        User userExists = this.userRepository.findByUsername(user.getUsername());
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "user", userExists));
    }

    @GetMapping(path = {"/users/confirm"})
    public String confirm(@RequestParam(value = "token") String token) {
        this.userService.confirmrUser(token);
        return "User confirmed.";
    }

    @PostMapping(path = {"/users/reset"})
    public void reset(@Valid @RequestBody User user) {
        User resetUser = this.userService.resetUser(user);
        if (resetUser != null) {
            System.out.println(resetUser.getPassword());
           /* SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Temporary Password Sent From " + this.webServerUrl);
            registrationEmail.setText("To access your account, please use this temporary password:  " + resetUser.getPassword() + ".\r\nNOTE: This email was sent from an automated system. Please do not reply.");
            registrationEmail.setFrom("support@nimbusengineering.co.bw");
            this.emailService.sendEmail(registrationEmail);*/
        }
    }

    @PostMapping(path = {"/users/delete"})
    public ResponseEntity<?> deleteUser(@Valid @RequestBody User user) {
        User user1 = userRepository.findByUserid(user.getUserid());
        if (user1 == null) {

            return new ResponseEntity((Object) ("Unable to delete as  User id " + user.getUserid() + " not found."), HttpStatus.NOT_FOUND);

        }
        userRepository.findByUseridAndDelete(user1.getUserid());
        passwordTrackerRepo.deleteLogs(user1.getEmail());
        List<Estate> estates = estateRepository.findAllByUserId(user1.getUserid());

        if (estates == null || estates.isEmpty()) {
            return new ResponseEntity((Object) ("User removed, No estates and devices found for " + user.getUserid()), HttpStatus.NOT_FOUND);

        }
        Integer estateid = 0;
        for (Estate estate : estates) {
            estateid = estate.getEstateid();
        }
        estateRepository.findByUserIDDelete(user1.getUserid());
        encryptRepo.deleteEncryption(estateid);
        List<Devices> devices = deviceRepository.findAllByEstateId(estateid);
        if (devices == null || devices.isEmpty()) {
            return new ResponseEntity((Object) ("User removed and estates but no devices found for " + user.getUserid()), HttpStatus.NOT_FOUND);

        }
        String mac = "";
        for (Devices devices1 : devices) {
            mac = devices1.getMacaddress();
            notifyRepo.findByDeviceIDAndDelete(mac);

        }
        attachedDevicesRepo.findByEstateIDAndDelete(estateid);
        deviceRepository.findByEstateIdAndDelete(estateid);
        List<Events> events = eventsRepo.findAllByEstateID(estateid);
        if (events == null || events.isEmpty()) {
            return new ResponseEntity((Object) ("User removed , estates and devices. There are no events found for " + user.getUserid()), HttpStatus.NOT_FOUND);

        }
        eventsRepo.deleteEventsByEstateID(estateid);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false"));

    }

    @PostMapping(path = {"/users/changepwd"})
    public User changePassword(@Valid @RequestBody User user) {
        return this.userService.changeUserPassword(user);
    }
}

