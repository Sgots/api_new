package com.smartcom.api.service;

import com.smartcom.api.config.ConfigReader;
import com.smartcom.api.repository.DeviceRepository;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.*;

public class MqttSubscriberTest implements IMqttMessageListener {

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

    private static final String broker = mqttServerUrl;
    private static final String topic = "#";
    private volatile boolean running = true;
   // MqttClient   subClient = getMqttClient();
   ExecutorService pool = Executors.newFixedThreadPool(10);

        class MessageHandler implements Runnable {
            MqttMessage message;
            String topic;

            public MessageHandler(String topic, MqttMessage message) throws SQLException {
                this.message = message;

            }


            public void run() {
                System.out.println("From topic: " + topic);
                org.json.JSONObject json = new org.json.JSONObject(message.toString());
                System.out.println("Message content: " + json);
                String command = json.getString("command");
                java.util.Date date = new java.util.Date();
                java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

                switch (command) {
                    case "dataResponse": {

                        UUID uuid = UUID.randomUUID();
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
                        Connection con = null;
                        PreparedStatement pstmt = null;
                        PreparedStatement pstmt2 = null;
                        PreparedStatement preparedStmt3 = null;

                        ResultSet rss;
                        ResultSet rss2;
                        try {
                            con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                            pstmt = con.prepareStatement("INSERT INTO dataresponse values (?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                            pstmt.setTimestamp(1, timestamp);
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
                            pstmt.executeUpdate();
                            pstmt2 = con.prepareStatement("SELECT month(date),MAX(total_energy)- MIN(total_energy) as kW FROM `dataresponse` WHERE macaddress =? GROUP by month(date )");
                            pstmt2.setString(1, device);
                            rss = pstmt2.executeQuery();
                            if (rss.next()) {
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        Integer estate = 0;
                        try {
                            PreparedStatement preparedStmt = con.prepareStatement("select * from devices where  macaddress=?");
                            preparedStmt.setString(1, device);

                            ResultSet rs = preparedStmt.executeQuery();
                            if (rs == null) {

                            }

                            while (rs.next()) {
                                estate = rs.getInt("estate_id");

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
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        finally {
                            try {
                                con.close();
                                assert pstmt != null;
                                pstmt.close();
                                assert pstmt2 != null;
                                pstmt2.close();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                            break;
                    }
                    case "rechargeResponse": {
                        String new_id = null;
                        PreparedStatement pstmt = null;
                        UUID uuid = UUID.randomUUID();
                        String uuidAsString = uuid.toString();
                        Double remainingCredit = json.getDouble("remainingCredit");
                        Double remainingEnergy = json.getDouble("remainingEnergy");
                        Double previousEnergy = json.getDouble("previousRemainingEnergy");
                        String reason = json.getString("reason");
                        String token = json.getString("rechargeToken");
                        String deviceID = json.getString("deviceID");
                        String status = json.getString("status");
                        Connection con = null;
                        try {
                            con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);


                            String querys = "select * from devices where  macaddress=" + deviceID + "";
                            try (Statement stmts = con.createStatement()) {
                                ResultSet rss = stmts.executeQuery(querys);


                                if (rss.next()) {
                                    estateid = rss.getInt("estate_id");

                                }
                            }
                            pstmt = con.prepareStatement("INSERT INTO  recharge_response values (?,?,?,?,?,?,?,?,?,?)");
                            pstmt.setString(1, uuidAsString);
                            pstmt.setTimestamp(3, timestamp);
                            pstmt.setDouble(5, previousEnergy);
                            pstmt.setString(6, reason);
                            pstmt.setString(7, token);

                            pstmt.setDouble(8, remainingCredit);
                            pstmt.setDouble(9, remainingEnergy);
                            pstmt.setString(4, deviceID);
                            pstmt.setString(10, status);
                            pstmt.setDouble(2, 0);

                            pstmt.executeUpdate();


                            if (status.equals("fail")) {

                                MessagingService messagingService = new MessagingService();
                                messagingService.rechargeNotificationFail(deviceID, estateid);
                            } else {
                                MessagingService messagingService = new MessagingService();
                                messagingService.rechargeNotification(deviceID, estateid);
                            }
                            String sql = "SELECT id,remaining_energy,previous_remaining_energy,(remaining_energy-previous_remaining_energy) as amount FROM `recharge_response` WHERE deviceid=" + deviceID + " ORDER BY date DESC LIMIT 1";
                            try (Statement stmts = con.createStatement()) {
                                ResultSet rss = stmts.executeQuery(sql);

                                if (rss.next()) {
                                    amount = rss.getDouble("amount");
                                    new_id = rss.getString("id");

                                }
                            }
                            System.out.println(amount);
                            System.out.println(new_id);
                            String str = timestamp.toString();
                            String query2 = "update recharge_response set amount=? WHERE id =?";
                            pstmt = con.prepareStatement(query2);
                            pstmt.setDouble(1, amount);
                            pstmt.setString(2, new_id);

                            pstmt.executeUpdate();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (MqttException e) {
                            throw new RuntimeException(e);
                        } finally {
                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            }
                            if (pstmt != null) {
                                try {
                                    pstmt.close();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }

                            }
                        }
                        break;
                    }
                    case "controlResponse": {
                        boolean booleanState = false;
                        PreparedStatement preparedStmt = null;
                        ResultSet rss = null;
                        Connection con = null;
                        try {
                            con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

                            String device = json.getString("deviceID");
                            String state = json.getString("state");
                            if (state.equals("ON")) {
                                booleanState = true;
                            }
                            String query2 = "update devices set device_status =? where macaddress= ?";
                            preparedStmt = con.prepareStatement(query2);
                            preparedStmt.setBoolean(1, booleanState);
                            preparedStmt.setString(2, device);

                            // execute the java preparedstatement
                            preparedStmt.executeUpdate();


                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } finally {
                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                    case "notificationsResponse": {
                        boolean booleanOnline = false;
                        PreparedStatement preparedStmt = null;
                        PreparedStatement preparedStmt2;
                        PreparedStatement preparedStmt3 = null;
                        ResultSet rss = null;
                        UUID uuid = UUID.randomUUID();
                        String uuidAsString = uuid.toString();
                        String device = json.getString("deviceID");
                        Integer creditThreshold = json.getInt("creditThresholdState");
                        Integer powerThreshold = json.getInt("powerThresholdState");
                        Integer energyThreshold = json.getInt("energyThresholdState");
                        String onlineState = json.getString("onlineState");
                        if (onlineState.equals("1")) {
                            booleanOnline = true;
                        }
                        Connection con = null;
                        try {
                            con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                            String query5 = "update devices set enabled= ? where macaddress= ?";
                            preparedStmt2 = con.prepareStatement(query5);
                            preparedStmt2.setBoolean(1, booleanOnline);
                            preparedStmt2.setString(2, device);
                            preparedStmt2.executeUpdate();
                            preparedStmt3 = con.prepareStatement("select credit_action, power_action, energy_action from devices where  macaddress=?");
                            preparedStmt3.setString(1, device);
                            rss = preparedStmt3.executeQuery();
                            while (rss.next()) {
                                credit_action1 = rss.getInt("credit_action");
                                power_action1 = rss.getInt("power_action");
                                energy_action1 = rss.getInt("energy_action");
                            }

                            preparedStmt3 = con.prepareStatement("select * from devices where  macaddress=?");
                            preparedStmt3.setString(1, device);
                            rss = preparedStmt3.executeQuery();
                            while (rss.next()) {
                                estateid = rss.getInt("estate_id");
                                deviceName = rss.getString("device_name");

                            }
                            preparedStmt3 = con.prepareStatement("select * from notifications where  device_id=?");


                            preparedStmt3.setString(1, device);
                            rss = preparedStmt3.executeQuery();
                            //rss = stmts.executeQuery(querys);


                            if (rss.next()) {
                                creditNotification = rss.getInt("credit_notification");
                                powerNotification = rss.getInt("power_notification");
                                energyNotification = rss.getInt("energy_notification");
                            } else {
                                preparedStmt = con.prepareStatement("INSERT INTO  notifications values (?, ?, ?, ?, ?)");
                                preparedStmt.setString(1, uuidAsString);
                                preparedStmt.setInt(5, 0);
                                preparedStmt.setInt(2, 0);
                                preparedStmt.setInt(4, 0);
                                preparedStmt.setString(3, device);
                                preparedStmt.executeUpdate();

                            }
                            if (creditThreshold.equals(creditNotification)) {

                            } else {
                                String query2 = "update notifications set credit_notification= ? where device_id= ?";
                                preparedStmt = con.prepareStatement(query2);
                                preparedStmt.setInt(1, creditThreshold);
                                preparedStmt.setString(2, device);

                                // execute the java preparedstatement
                                preparedStmt.executeUpdate();
                                if (creditThreshold.equals(1)) {
                                    DeviceService deviceService = new DeviceService();
                                    deviceService.CreditNotification(device, credit_action1, estateid, deviceName);
                                } else if (creditThreshold.equals(0)) {
                                    {
                                        DeviceService deviceService = new DeviceService();
                                        deviceService.CreditNotificationOff(device, credit_action1, estateid, deviceName);
                                    }
                                }
                                // code for threshold escaped
                            }
                            if (powerThreshold.equals(powerNotification)) {

                            } else {
                                String query2 = "update notifications set power_notification =? where device_id= ?";
                                preparedStmt = con.prepareStatement(query2);
                                preparedStmt.setInt(1, powerThreshold);
                                preparedStmt.setString(2, device);

                                // execute the java preparedstatement
                                preparedStmt.executeUpdate();
                                if (powerThreshold.equals(1)) {
                                    DeviceService deviceService = new DeviceService();
                                    deviceService.PowerNotification(device, power_action1, estateid, deviceName);
                                } else if (powerThreshold.equals(0)) {
                                    DeviceService deviceService = new DeviceService();
                                    deviceService.PowerNotificationOff(device, power_action1, estateid, deviceName);
                                }
                            }
                            if (energyThreshold.equals(energyNotification)) {

                            } else {
                                String query2 = "update notifications set energy_notification= ? where device_id= ?";
                                preparedStmt = con.prepareStatement(query2);
                                preparedStmt.setInt(1, energyThreshold);
                                preparedStmt.setString(2, device);

                                // execute the java preparedstatement
                                preparedStmt.executeUpdate();
                                if (energyThreshold.equals(1)) {
                                    DeviceService deviceService = new DeviceService();
                                    deviceService.EnergyNotification(device, energy_action1, estateid, deviceName);
                                } else if (energyThreshold.equals(0)) {
                                    DeviceService deviceService = new DeviceService();
                                    deviceService.EnergyNotificationOff(device, energy_action1, estateid, deviceName);
                                }
                            }


                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } finally {
                            if (con != null) {
                                try {
                                    con.close();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            }
                            break;
                        }
                    }
                    case "notification": {
                        UUID uuid = UUID.randomUUID();
                        String uuidAsString = uuid.toString();
                        String status = json.getString("status");

                        String device = json.getString("deviceID");

                        //Getting the connection
                        Connection con = null;
                        PreparedStatement pstmt = null;
                        try {
                            con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

                            pstmt = con.prepareStatement("INSERT INTO notifications_history values (?, ?, ?, ?, ?, ?)");
                            pstmt.setString(1, uuidAsString);
                            pstmt.setString(5, status);
                            pstmt.setString(3, device);
                            pstmt.setString(2, command);
                            pstmt.setTimestamp(6, timestamp);
                            pstmt.setBoolean(4, false);
                            pstmt.executeUpdate();

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                        if (pstmt != null) {
                            try {
                                pstmt.close();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

                        }
                        break;
                    }
                }                //process message
            }
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            pool.execute(new MessageHandler(topic,message));
        }


    }

