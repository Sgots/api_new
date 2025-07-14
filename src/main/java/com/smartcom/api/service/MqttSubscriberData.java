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

public class MqttSubscriberData {

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
    private static final String topic = "+/+/meterData";
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

                org.json.JSONObject json = new org.json.JSONObject(mqttMessage.toString());



                String command = json.getString("command");
                java.util.Date date = new java.util.Date();
                Timestamp timestamp2 = new Timestamp(date.getTime());
            /*  String messages = mqttMessage.toString();
                JSONParser parser = new JSONParser();*/
                // JSONObject json = (JSONObject) parser.parse(messages);

                switch (command) {
                    case "dataResponse": {
                        if (json.getString("deviceTypeID").equals("3") || json.getString("deviceTypeID").equals("4")) {
                            System.out.println("From topic: " + s);

                            //      JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(mqttMessage.toString()));
                            System.out.println("Message content: " + json);
                            UUID uuid = UUID.randomUUID();
                            String uuidAsString = uuid.toString();
                            Double remainingCredit = json.getDouble("remainingCredit");
                            Double power = json.getDouble("power");
                            Double energyEst = json.getDouble("energyEstimatePerHour");
                            Double energyCost = json.getDouble("energyCostEstimatePerHour");
                            Double energyUsedToday = json.getDouble("energyUsedToday");
                            Double costPerKwh = json.getDouble("costPerKwh");
                            Double costToday = json.getDouble("costToday");
                            String deviceType = json.getString("deviceTypeID");
                            String timeStamp = json.getString("timestamp");
                            Double remainingEnergy = json.getDouble("remainingEnergy");
                            Double voltage = json.getDouble("voltage");
                            Double current = json.getDouble("current");
                            String device = json.getString("deviceID");
                            Double totalEnergy = json.getDouble("totalEnergy");

                            //Getting the connection
                            PreparedStatement pstmt = null;
                            PreparedStatement pstmt2 = null;
                            PreparedStatement preparedStmt3 = null;

                            ResultSet rss;
                            ResultSet rss2;
                            Meterdataresponse meterdataresponse = new Meterdataresponse();
                            meterdataresponse.setCommand(command);
                            meterdataresponse.setDate(timestamp2);
                            meterdataresponse.setCurrent(current);
                            meterdataresponse.setPower(power);
                            meterdataresponse.setCostToday(costToday);
                            meterdataresponse.setCostPerKwh(costPerKwh);
                            meterdataresponse.setEnergyCost(energyCost);
                            meterdataresponse.setEnergyEst(energyEst);
                            meterdataresponse.setDeviceTypeID(deviceType);
                            meterdataresponse.setTimeStamp(timeStamp);
                            meterdataresponse.setEnergyUsedToday(energyUsedToday);
                            meterdataresponse.setRemainingCredit(remainingCredit);
                            meterdataresponse.setRemainingEnergy(remainingEnergy);
                            meterdataresponse.setVoltage(voltage);
                            meterdataresponse.setMacaddress(device);
                            meterdataresponse.setTotalEnergy(totalEnergy);
                            try {
                                meterService.addDataresponse(meterdataresponse);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);

                            }
                            //  meterService.addDataresponse(meterdataresponse);

                       /* try {


                            pstmt = con.prepareStatement("INSERT INTO dataresponse values (?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");


                            //   int id = Integer.parseInt((String) record.get("ID"));
//
//                    int first_name = Integer.parseInt((String) json.get("current"));
//                   int last_name = Integer.parseInt((String) json.get("remainingEnergy"));
//                    int date = Integer.parseInt((String) json.get("remainingCredit"));
//                    int country = Integer.parseInt((String) json.get("power"));
                            //   pstmt.setDouble(1, current);
                            pstmt.setTimestamp(1, timestamp2);
                            pstmt.setString(6, deviceType);
                            pstmt.setDouble(5, current);
                            pstmt.setDouble(7, energyCost);
                            pstmt.setDouble(4, costToday);

                            pstmt.setDouble(3, costPerKwh);
                            pstmt.setDouble(17, voltage);
                            pstmt.setString(2, command);
                            pstmt.setDouble(8, energyEst);
                            pstmt.setDouble(9, energyUsedToday);
                            pstmt.setInt(10, 0);

                            pstmt.setString(11, device);
                            pstmt.setDouble(12, power);
                            pstmt.setDouble(13, remainingCredit);
                            pstmt.setDouble(14, remainingEnergy);
                            pstmt.setString(15, timeStamp);
                            pstmt.setDouble(16, totalEnergy);
                            //  pstmt.setInt(16, estate);
                            pstmt.executeUpdate();
                            pstmt2 = con.prepareStatement("SELECT month(date),MAX(total_energy)- MIN(total_energy) as kW FROM `dataresponse` WHERE macaddress =? GROUP by month(date )");
                            pstmt2.setString(1, device);      // Assign value to input parameter      2

                            rss = pstmt2.executeQuery();        // Get the result table from the query  3

                            if (rss.next()) {
                                // total_energy = rss.getDouble("month(date)");

                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        Integer estate = 0;
                        PreparedStatement preparedStmt = con.prepareStatement("select * from devices where  macaddress=?");

                        //  String querys1 = "select * from devices where  macaddress=?";
                        //  preparedStmt3 = con.prepareStatement(querys1);
                        preparedStmt.setString(1, device);

                        ResultSet rs = preparedStmt.executeQuery();
                        if (rs == null) {

                        }

                        while (rs.next()) {
                            estate = rs.getInt("estate_id");
                            //  deviceName = rss.getString("device_name");

                        }


                        String query2 = "update devices set remaining_credit= ?, remaining_energy=?, current_power=?, energy_used=?, cost_today=? where macaddress= ?";
                        pstmt = con.prepareStatement(query2);
                        pstmt.setDouble(1, remainingCredit);
                        pstmt.setDouble(2, remainingEnergy);
                        pstmt.setDouble(3, power);
                        pstmt.setDouble(4, energyUsedToday);
                        pstmt.setDouble(5, costToday);

                        pstmt.setString(6, device);
                        pstmt.executeUpdate();
                        String query3 = "update dataresponse set estateid= ? where macaddress= ?";
                        pstmt = con.prepareStatement(query3);
                        pstmt.setInt(1, estate);
                        pstmt.setString(2, device);
                        pstmt.executeUpdate();
                        Double sum = 0.0;
                        if (deviceType.equals("2") || deviceType.equals("3")) {
                            preparedStmt = con.prepareStatement("Select  sum(energy_used) as maxi FROM devices where estate_id =? and device_type=4");
                            preparedStmt.setInt(1, estate);

                            rs = preparedStmt.executeQuery();
                            if (rs == null) {

                            }

                            while (rs.next()) {
                                sum = rs.getDouble("maxi");
                                // estate = rs.getInt("estate_id");
                                //  deviceName = rss.getString("device_name");


                            }
                            String query0 = "update devices set remaining_credit= ?, remaining_energy=?, current_power=?, energy_used=?, cost_today=? where macaddress= ? and device_type=5";
                            pstmt = con.prepareStatement(query0);
                            pstmt.setDouble(1, remainingCredit);
                            pstmt.setDouble(2, remainingEnergy);
                            pstmt.setDouble(3, power);
                            pstmt.setDouble(4, energyUsedToday - sum);
                            pstmt.setDouble(5, costToday);

                            pstmt.setString(6, "Unknown");
                            pstmt.executeUpdate();
                        }

}

                        break;*/
                            break;
                        }
                    }
                }
            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("deliveryComplete");
            }

        });
        subClient.subscribe(topic);
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
