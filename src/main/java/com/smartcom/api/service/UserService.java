package com.smartcom.api.service;

import java.net.ServerSocket;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smartcom.api.model.LoginLog;
import com.smartcom.api.model.PasswordTracker;
import com.smartcom.api.model.SystemLog;
import com.smartcom.api.model.User;
import com.smartcom.api.repository.LoginLogRepo;
import com.smartcom.api.repository.PasswordTrackerRepo;
import com.smartcom.api.repository.SystemActivityLogRepo;
import com.smartcom.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.smartcom.api.exception.BadRequestException;

@Service

public class UserService {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    private SystemActivityLogRepo systemActivityLogRepo;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private PasswordTrackerRepo passwordTrackerRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginLogRepo loginLogRepo;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private PasswordEncoder encoder;
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 7777;
    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    private static final String patterns = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

    private static final String numberPattern = "(?=.*[0-9])(?=\\S+$).{8,}";
    private static final String alphaPattern = "(?=.*[a-z])(?=\\S+$).{8,}";
    private static final String alphaCaps = "(?=.*[A-Z])(?=\\S+$).{8,}";
    private static final String specialChar = "(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    private static final String length = "(?=\\S+$).{8,}";

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setPassword("");
        }
        return users;
    }

    /*public User update(User user){

        Integer userid = user.getUserid();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        User user1 = userRepository.findByUserUpdate(userid,firstName, lastName, email);
        return user1;
    }*/
    public boolean registerUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String firstname = user.getFirstName();
        String lastname = user.getLastName();
        String email = user.getEmail();
        String password2 = user.getPassword2();
        user.setEstatecount(0);    //	Integer id =user.getUserid();
        if (password.isEmpty() && firstname.isEmpty() && lastname.isEmpty() && email.isEmpty() && username.isEmpty()) {
            throw new BadRequestException("fill all the fields");
        }

        if (!password.equals(password2)) {
            throw new BadRequestException("Password do not match");

        }
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);


        User userExists = userRepository.findByUsername(user.getUsername());
        User userExists2 = userRepository.findByEmail(user.getEmail());

        if (userExists != null || userExists2 != null) {

            throw new BadRequestException(user.getUsername() + " already registered.");
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches() == false) {
            throw new BadRequestException("Enter a valid email");

        }
        pattern = Pattern.compile(length);
        matcher = pattern.matcher(password);
        if (matcher.matches() == false) {
            throw new BadRequestException("Your password must contain 8 characters");

        }
        pattern = Pattern.compile(numberPattern);
        matcher = pattern.matcher(password);
        if (matcher.matches() == false) {

            throw new BadRequestException("Your password must contain a number");

        }
        pattern = Pattern.compile(alphaPattern);
        matcher = pattern.matcher(password);
        if (matcher.matches() == false) {
            throw new BadRequestException("Your password must contain a lower case alphabet");

        }
        pattern = Pattern.compile(alphaCaps);
        matcher = pattern.matcher(password);
        if (matcher.matches() == false) {
            throw new BadRequestException("Your password must contain an upper case alphabet");

        }
        pattern = Pattern.compile(specialChar);
        matcher = pattern.matcher(password);
        if (matcher.matches() == false) {
            throw new BadRequestException("Your password must contain a special character");

        }

        // Disable user until they click on confirmation link in email
        user.setEnabled(true);
        user.setRole("ROLE_USER");
        user.setUserid(user.getUserid());
        user.setPassword2("");
        // Generate random 36-character string token for confirmation link
        user.setConfirmationToken(UUID.randomUUID().toString());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

// 2018-08-03 03:50:17
        LocalDateTime.now().format(formatter);

        String dateFuture = LocalDateTime.now().plusMonths(6).format(formatter);
        LocalDateTime now = LocalDateTime.now();

        Timestamp timestamp = Timestamp.valueOf(dateFuture);
        Timestamp timestamp2 = Timestamp.valueOf(now);

        //System.out.println(newPassword);

        PasswordTracker passwordTracker = new PasswordTracker();
        passwordTracker.setExpiresAt(timestamp);
        passwordTracker.setCreatedAt(timestamp2);
        passwordTracker.setEmail(user.getEmail());
        passwordTracker.setState("Active");
        passwordTrackerRepo.save(passwordTracker);
        userRepository.save(user);

        return true;
    }

    public boolean inviteUser(User user) {
        String password = user.getPassword();
        String firstname = user.getFirstName();
        String lastname = user.getLastName();
        String email = user.getEmail();
        //	Integer id =user.getUserid();
        if (password.isEmpty() && firstname.isEmpty() && lastname.isEmpty() && email.isEmpty()) {
            throw new BadRequestException("fill all the fields");
        }

        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);
        if (user.getUsername().isEmpty()) {
            user.setUsername(user.getEmail());
        }

        User userExists = userRepository.findByUsername(user.getUsername());

        if (userExists == null) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches() == false) {
                throw new BadRequestException("Enter a valid email");

            }
            // Disable user until they click on confirmation link in email
            user.setEnabled(false);
            user.setRole("ROLE_MEMBER");
            user.setUserid(user.getUserid());
            user.setPassword2("");
            // Generate random 36-character string token for confirmation link
            user.setConfirmationToken(UUID.randomUUID().toString());
            userRepository.save(user);
            return true;
        } else {
            return false;
        }


    }

    public User resetUser(User user) {
     /*   if (user.getUsername().isEmpty()) {
            user.setUsername(user.getEmail());
        }*/
        User userExists = userRepository.findByEmail(user.getEmail());

        if (userExists == null) {
            throw new BadRequestException(user.getUsername() + " is not registered.");
        }

        if (userExists.getEmail().isEmpty()) {
            throw new BadRequestException(user.getUsername() + " does not have a valid email address.");
        }

        String password = generatePassword(10);
        String encodedPassword = encoder.encode(password);
        userExists.setPassword(encodedPassword);
        userExists.setTempPassword(true);

        userRepository.save(userExists);

        // return the user with plain password so that we can send it to the user's email.
        userExists.setPassword(password);

        return userExists;
    }
/*	public void updateUserEstate(User user){
	userRepository.findByUseridAndUpdate(user.getUserid());
	}*/


    //update my_table set my_field = my_field+1

    public User changeUserPassword(User user) {
        User userExists = userRepository.findByUsername(user.getUsername());

        if (userExists == null) {
            throw new BadRequestException(user.getUsername() + " is not registered.");
        }

        String oldPassword = user.getPassword();
        if (!encoder.matches(oldPassword, userExists.getPassword())) {
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp2 = Timestamp.valueOf(now);
            SystemLog systemLog = new SystemLog();
            systemLog.setEmail(user.getUsername());
            systemLog.setActivity("Failed to change password. Invalid current password");
            systemLog.setCreatedAt(timestamp2);
            systemActivityLogRepo.save(systemLog);
            throw new BadRequestException("Invalid current password.");
        }

        if (!userExists.getEnabled()) {
            throw new BadRequestException("The user is not enabled.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

// 2018-08-03 03:50:17
        LocalDateTime.now().format(formatter);

// 2018-09-03 03:50:17
        String dateFuture = LocalDateTime.now().plusMonths(6).format(formatter);
        LocalDateTime now = LocalDateTime.now();

        Timestamp timestamp = Timestamp.valueOf(dateFuture);
        Timestamp timestamp2 = Timestamp.valueOf(now);
        String newState = "Inactive";
        String state = "Active";
        passwordTrackerRepo.updateState(state, newState, userExists.getEmail());
        SystemLog systemLog = new SystemLog();
        systemLog.setEmail(user.getUsername());
        systemLog.setActivity("Changed password.");
        systemLog.setCreatedAt(timestamp2);
        systemActivityLogRepo.save(systemLog);
        String newPassword = user.getPassword2();
        //System.out.println(newPassword);
        String encodedPassword = encoder.encode(newPassword);
        userExists.setPassword(encodedPassword);
        userExists.setTempPassword(false);
        PasswordTracker passwordTracker = new PasswordTracker();
        passwordTracker.setCreatedAt(timestamp2);
        passwordTracker.setEmail(userExists.getEmail());
        passwordTracker.setExpiresAt(timestamp);
        passwordTracker.setState(state);
        passwordTrackerRepo.save(passwordTracker);
        userRepository.save(userExists);

        userExists.setPassword("");
        userExists.setUserid(0);
        return userExists;
    }

    public User confirmrUser(String token) {
        User user = userRepository.findByConfirmationToken(token);

        if (user == null) {
            throw new BadRequestException("Invalid token.");
        }
        // Token found
        user.setEnabled(true);
        user.setConfirmationToken("");

        // Save user
        userRepository.save(user);
        return user;
    }

    public User loginUser(User user) {
        User userExists = userRepository.findByUsername(user.getUsername());
        String state = "Active";
        LocalDateTime now = LocalDateTime.now();

        Timestamp timestamp = Timestamp.valueOf(now);
        if (userExists == null) {
            LoginLog loginLog = new LoginLog();
            loginLog.setActivity("Invalid username");
            loginLog.setEmail(user.getUsername());
            loginLog.setCreatedAt(timestamp);

            loginLogRepo.save(loginLog);
            throw new BadRequestException("Invalid user name.");

        }

        String password = user.getPassword();
        if (!encoder.matches(password, userExists.getPassword())) {
            LoginLog loginLog = new LoginLog();
            loginLog.setActivity("Invalid user name and password combination.");
            loginLog.setEmail(userExists.getEmail());
            loginLog.setCreatedAt(timestamp);

            loginLogRepo.save(loginLog);
            throw new BadRequestException("Invalid user name and password combination.");
        }


        PasswordTracker passwordTracker = passwordTrackerRepo.findAllByCurrentUsr(userExists.getEmail(), state);

        //compares ts1 with ts2
        int b3 = passwordTracker.getCreatedAt().compareTo(passwordTracker.getExpiresAt());
        if (b3 > 0) {
            LoginLog loginLog = new LoginLog();
            loginLog.setActivity("Login failed due to expired password.");
            loginLog.setEmail(userExists.getEmail());
            loginLog.setCreatedAt(timestamp);
            loginLogRepo.save(loginLog);
            throw new BadRequestException("Your password has expired");

        }

        LoginLog loginLog = new LoginLog();
        loginLog.setActivity("Successful login.");
        loginLog.setEmail(userExists.getEmail());
        loginLog.setCreatedAt(timestamp);
        loginLogRepo.save(loginLog);
        userExists.setPassword("");
        //	userExists.setUserid(0);
        return userExists;
    }

    public static String generatePassword(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public User updateUser(User user) {
        User userExists = userRepository.findByUserid(user.getUserid());

        if (userExists == null) {
            throw new BadRequestException("Invalid user name.");
        }

        String name = user.getFirstName();
        String lname = user.getLastName();
        String email = user.getEmail();

        userExists.setFirstName(name);
        userExists.setLastName(lname);
        userExists.setEmail(email);

        userRepository.save(userExists);

        return userExists;
    }
}
