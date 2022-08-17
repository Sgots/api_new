package com.smartcom.api.controller;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.smartcom.api.model.*;
import com.smartcom.api.repository.*;
import com.smartcom.api.service.EmailService;
import com.smartcom.api.service.EstateService;
import com.smartcom.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(maxAge = 3600) // https://spring.io/guides/gs/rest-service-cors/
@RestController
public class EstateController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private EstateService estateService;
    @Autowired
    private EstateRepository estateRepository;
    @Autowired
    private SystemActivityLogRepo systemActivityLogRepo;
    @Autowired
    private EncryptRepo encryptRepo;
    @Autowired
    private DeviceToEstateRepository deviceToEstateRepository;
    @Autowired
    private EmailService emailService;
    @Value("${webServerUrl}")
    private String webServerUrl;

    @GetMapping(path = "/estate")
    public List<Estate> getAllEstate() {
        return estateService.getAllEstate();
    }

    @GetMapping(value = "/estate/{estate_id}")
    public ResponseEntity<?> getEstate(@PathVariable("estate_id") Integer estateid) {
        Estate estate = estateRepository.findByEstateid(estateid);


        if (estate == null) {
            return new ResponseEntity<String>("No User found with this " + estateid, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",

                "estate", estate)
        );
    }

    @GetMapping(value = "/estates/{user_id}")
    public ResponseEntity<?> getEstateByUser(@PathVariable("user_id") Integer userid) {
        Estate estate = estateRepository.findByUserID(userid);


        if (estate == null) {
            return new ResponseEntity<String>("No User found with this " + userid, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",

                "estate", estate)
        );
    }

    @GetMapping(value = "/estateKeys/{estate_id}")
    public ResponseEntity<?> getEstateKeys(@PathVariable("estate_id") Integer userid) {
        //  Estate estate = estateRepository.findByEstateid(userid);
        EncryptionKeys encryptionKeys = encryptRepo.findByEncryptID(userid);

       /* if (estate == null) {
            return new ResponseEntity<String>("No User found with this "+ userid, HttpStatus.NOT_FOUND);
        }*/
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",

                "estate", encryptionKeys)
        );
    }

    @GetMapping(value = "/estateUser/{user_id}")
    public ResponseEntity<?> getEstates(@PathVariable("user_id") Integer userid) {

        List<Estate> estates = estateRepository.findAllByUserId(userid);


        if (estates == null) {
            return new ResponseEntity<String>("No User found with this " + userid, HttpStatus.NOT_FOUND);
        }

        //   return new ResponseEntity<>(entities, HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "estates", estates)
        );
    }

    /*	@PostMapping(path = "/users/register")
        public void register(@Valid @RequestBody User user) {

            if (userService.registerUser(user)) {

                SimpleMailMessage registrationEmail = new SimpleMailMessage();
                registrationEmail.setTo(user.getEmail());
                registrationEmail.setSubject("Registration Confirmation");
                registrationEmail.setText("To confirm your e-mail address, please click the link below:\n" + webServerUrl
                        + "/users/confirm?token=" + user.getConfirmationToken());
                registrationEmail.setFrom("noreply@domain.com");

                emailService.sendEmail(registrationEmail);
            }
        }*/
    @PutMapping(value = "estate/updateKeys")
    public ResponseEntity<?> updateKeys(@RequestBody Estate estate) {
        if (estateRepository.findByEstateid(estate.getEstateid()) == null) {
            return new ResponseEntity<String>("Unable to update as  User id " + estate.getEstateid() + " not found.",
                    HttpStatus.NOT_FOUND);
        }
        Integer estateid = estate.getEstateid();
        byte[] key = estate.getEncryptionKeys().getEncryptionKey();
        byte[] iv1 = estate.getEncryptionKeys().getIV1();
        byte[] iv2 = estate.getEncryptionKeys().getIV2();
        encryptRepo.findByEstateAndUpdate(estateid, key, iv1, iv2);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "estate", estate));
    }

    @PutMapping(value = "estate/updateEstate")
    public ResponseEntity<?> updateUser(@RequestBody Estate estate) {
        if (estateRepository.findByEstateid(estate.getEstateid()) == null) {
            return new ResponseEntity<String>("Unable to update as  User id " + estate.getEstateid() + " not found.",
                    HttpStatus.NOT_FOUND);
        }
        Integer estateid = estate.getEstateid();
        String estatename = estate.getEstatename();
        String estateaddress = estate.getEstateaddress();
        estateRepository.findByEstateAndUpdate(estateid, estatename, estateaddress);
        SystemLog systemLog = new SystemLog();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

// 2018-08-03 03:50:17
        Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
        User user = userRepository.findByUserid(estateExists.getUser().getUserid());
// 2018-09-03 03:50:17
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp2 = Timestamp.valueOf(now);

        systemLog.setEmail(user.getEmail());
        systemLog.setActivity("Updated estate " + estateid);
        systemLog.setCreatedAt(timestamp2);
        systemActivityLogRepo.save(systemLog);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "estate", estate));
    }

    @PutMapping(value = "estate/delete")
    public ResponseEntity<?> deleteEstate(@RequestBody Estate estate) {
        if (estateRepository.findByEstateid(estate.getEstateid()) == null) {
            return new ResponseEntity<String>("Unable to update as  User id " + estate.getEstateid() + " not found.",
                    HttpStatus.NOT_FOUND);
        }
        Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
        User user = userRepository.findByUserid(estate.getUser().getUserid());
        estateRepository.findByEstateIdDelete(estate.getEstateid());
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp2 = Timestamp.valueOf(now);
        SystemLog systemLog = new SystemLog();
        systemLog.setEmail(user.getEmail());
        systemLog.setActivity("Deleted estate " + estate.getEstateid());
        systemLog.setCreatedAt(timestamp2);
        systemActivityLogRepo.save(systemLog);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "estate", estate));
    }

    @PutMapping(path = "/estate/invite")
    public Estate Invite(@Valid @RequestBody Estate estate) {
        return estateService.inviteUser(estate);
    }

    @DeleteMapping(path = "/estate/removeUser")
    public Estate removeUser(@Valid @RequestBody Estate estate) {
        return estateService.removeUser(estate);
    }

    /* @GetMapping(value = "/estateUsers/{user_id}")
     public ResponseEntity<Object> getUserEstate(@PathVariable("user_id") Integer userid) {
         //List<Estate> estate = estateRepository.findAllByCurrentUser(userid);
       //  List<Estate> estate1 = estateRepository.findAllByUsers(userRepository.findByUserid(userid));


    *//*     if (estate1 == null) {
            // return new ResponseEntity<String>("No User found with this "+ userid, HttpStatus.NOT_FOUND);
        }*//*
        return  ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",

                "estate", estate1)
        );
    }*/
    @PostMapping(path = "/estate/addEstate")
    public ResponseEntity<Object> addEstate(@Valid @RequestBody Estate estate) throws SQLIntegrityConstraintViolationException, NoSuchAlgorithmException {
        if (estateService.addEstate(estate) == true) {
            Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());
            User user = userRepository.findByUserid(estateExists.getUser().getUserid());
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp2 = Timestamp.valueOf(now);
            SystemLog systemLog = new SystemLog();
            systemLog.setEmail(user.getEmail());
            systemLog.setActivity("Added estate with ID " + estate.getEstateid());
            systemLog.setCreatedAt(timestamp2);
            systemActivityLogRepo.save(systemLog);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "error", "false",

                    "estate", estate)
            );
            //	return new ResponseEntity<String>("Duplicate Entry "+ user.getId(), HttpStatus.IM_USED);
        }

        //userRepository.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}