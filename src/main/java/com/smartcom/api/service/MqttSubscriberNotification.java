package com.smartcom.api.service;

import com.smartcom.api.config.ConfigReader;
import com.smartcom.api.model.*;
import com.smartcom.api.repository.DeviceRepository;
import com.smartcom.api.repository.EstateRepository;
import com.smartcom.api.repository.NotifyRepo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.Properties;
import java.util.UUID;

@Service
@EnableJpaAuditing

public class MqttSubscriberNotification {

    @Autowired
    private MeterService meterService;


    @Autowired
    NotifyRepo notifyRepo;



    private static ConfigReader configReader = new ConfigReader();
    private static Properties properties = new Properties(configReader.init_prop());
    public static String mqttServerUrl = properties.getProperty("mqttServerUrl");
    public static String dbUsername = properties.getProperty("spring.datasource.username");
    public static String dbUrl = properties.getProperty("spring.datasource.url");

    public static String dbPassword = properties.getProperty("spring.datasource.password");
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
     private EstateRepository estateRepository;


    Integer credit_action1 = null;
    Integer power_action1 = null;
    Integer energy_action1 = null;
    Integer creditNotification = null;
    Integer powerNotification = null;
    Integer energyNotification = null;
    Integer estateid = null;
    Double total_energy = null;
    String deviceName = "";
    Double amount = null;
    private volatile boolean running = true;
    private static Connection con;
    public static Connection ConnectToDB() throws Exception {
        //Registering the Driver
        //Getting the connection
/*        String dbUrl = "jdbc:mysql://localhost/smartcom2";
        Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        System.out.println("Connection established......");*/
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(300);
        config.setMaxLifetime(600);
        HikariDataSource dataSource = new HikariDataSource(config);
         con = dataSource.getConnection();

        return con;
    }

    private static final String broker = mqttServerUrl;
    private static final String topic = "+/+/CreditNotification";
    private static final String topic1 = "+/+/PowerNotification";
    private static final String topic3 = "+/+/EnergyNotification";


    private static final String topic2 = "+/+/notifications";

    private static final String clientId = "AMAZON_CLIENT556";

    public void run() throws Exception {
        MqttClient subClient = getMqttClient();
         //  con = ConnectToDB();
        String value = UUID.randomUUID().toString();
        subClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                //     running = false;
                try {
                    run();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(throwable);

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws IOException, ParseException, JSONException, JSONException, MqttException, ClassNotFoundException, SQLException {
                System.out.println("From topic: " + s);

                //      JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(mqttMessage.toString()));
                org.json.JSONObject json = new org.json.JSONObject(mqttMessage.toString());
                System.out.println("Message content: " + json);

                String command = json.getString("command");
                java.util.Date date = new java.util.Date();
                Timestamp timestamp2 = new Timestamp(date.getTime());
            /*  String messages = mqttMessage.toString();
                JSONParser parser = new JSONParser();*/
                // JSONObject json = (JSONObject) parser.parse(messages);

                switch (command) {

                    case "notificationsResponse": {
                        UUID uuid = UUID.randomUUID();
                        String uuidAsString = uuid.toString();
                        Integer creditThreshold = json.getInt("creditThresholdState");
                        Integer powerThreshold = json.getInt("powerThresholdState");
                        Integer energyThreshold = json.getInt("energyThresholdState");
                        String onlineState = json.getString("onlineState");
                        String device = json.getString("deviceID");
                        boolean booleanOnline = false;
                        Notifications notifications = new Notifications();
                        notifications.setCredit_notification(creditThreshold);
                        notifications.setEnergy_notification(energyThreshold);
                        notifications.setPower_notification(powerThreshold);
                        notifications.setDevice_id(device);
                        notifications.setNotificationid(uuidAsString);
                        if (onlineState.equals("1")) {
                            booleanOnline = true;
                        }

                        deviceService.addNotifications(notifications, booleanOnline);


                        break;
                    }
                    case "notification": {
                        UUID uuid = UUID.randomUUID();
                        String uuidAsString = uuid.toString();
                        String status = json.getString("status");

                        String device = json.getString("deviceID");


                        //Getting the connection
                        PreparedStatement pstmt = null;






                        NotificationsHistory notificationsHistory = new NotificationsHistory();


                        notificationsHistory.setCommand(command);
                        notificationsHistory.setId(uuidAsString);
                        notificationsHistory.setStatus(status);
                        notificationsHistory.setTimestamp(timestamp2);
                        notificationsHistory.setRead_state(false);
                        notificationsHistory.setDeviceID(device);

                        notifyRepo.save(notificationsHistory);


                        break;
                    }
                }
            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("deliveryComplete");
            }

        });
        subClient.subscribe(topic);
        subClient.subscribe(topic1);
        subClient.subscribe(topic2);
        subClient.subscribe(topic3);




    }


    private static MqttClient getMqttClient() {
        try {
            MqttClient pubClient = new MqttClient(broker, MqttClient.generateClientId());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
           // connectOptions.setAutomaticReconnect(true);
            System.out.println("Connecting to broker: " + broker);
            pubClient.connect(connectOptions);
            return pubClient;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void terminate() {
        running = false;
    }
}
