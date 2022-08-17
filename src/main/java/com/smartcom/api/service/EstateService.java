package com.smartcom.api.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.smartcom.api.model.*;
import com.smartcom.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartcom.api.exception.BadRequestException;
import com.smartcom.api.model.User;
import com.smartcom.api.repository.UserRepository;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@Service
@EnableJpaAuditing
public class EstateService {
    @Autowired
    private SystemActivityLogRepo systemActivityLogRepo;
    @Autowired
    private EstateRepository estateRepository;
    @Autowired
    private EncryptRepo encryptRepo;
    private static int AES_128 = 128;

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public List<Estate> getAllEstate() {
        List<Estate> estates = estateRepository.findAll();
        for (Estate estate : estates) {
            estate.setEstateid(0);
        }
        return estates;
    }

    public boolean addEstate(Estate estate) throws NoSuchAlgorithmException {
        String estate_name = estate.getEstatename();

        String estate_address = estate.getEstateaddress();

        String intervals = estate.getMeterdatafreq();

        if (estate_name.isEmpty() && estate_address.isEmpty() && intervals.isEmpty()) {
            throw new BadRequestException("Fill all the fields.");
        }


        Estate estateExists = estateRepository.findByEstateaddress(estate.getEstateaddress());

        if (estateExists != null) {
            throw new BadRequestException(estate.getEstateaddress() + " already registered.");
        }

        // Disable user until they click on confirmation link in email
        estate.setEnabled(false);
        User user = estate.getUser();
        // estate.addUser(user);
        estate.setSavedevent(false);
        estateRepository.save(estate);
        System.out.println(estate.getUser().getEmail());

        KeyGenerator keyGenerator = KeyGenerator.getInstance(CryptoMngr.ALGORITHM);
        keyGenerator.init(AES_128);
        //Generate Key
        SecretKey key = keyGenerator.generateKey();
        //Initialization vector
        SecretKey IV = keyGenerator.generateKey();
        SecretKey IV2 = keyGenerator.generateKey();
        EncryptionKeys encryptionKeys = new EncryptionKeys();
        encryptionKeys.setEstate(estate);
        encryptionKeys.setEncryptionKey(key.getEncoded());
        encryptionKeys.setIV1(IV.getEncoded());
        encryptionKeys.setIV2(IV2.getEncoded());
        encryptRepo.save(encryptionKeys);
        return true;
    }

    public Estate inviteUser(Estate estate) {
        Estate estateExists = estateRepository.findByEstateid(estate.getEstateid());

        if (estateExists == null) {
            throw new BadRequestException("Invalid Estate id");
        }

        User user = estate.getUser();
        estateRepository.inviteUser(estateExists.getEstateid(), user.getUserid());
        //estateExists.addUser(user);
        System.out.println(user.getUserid());
        //userExists.setUsername(email);

        estateRepository.save(estateExists);


        return estateExists;
    }

    public Estate removeUser(Estate estate) {
        Estate estateExists = estateRepository.findByEstateid(estate.getEstateid());

        if (estateExists == null) {
            throw new BadRequestException("Invalid Estate id");
        }
        User user = estate.getUser();
        System.out.println(user.getUserid() + "//" + estateExists.getEstateid());
        estateRepository.removeUser(estateExists.getEstateid(), user.getUserid());
        estateRepository.save(estateExists);

        return estateExists;
    }


}
