/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcom.api.serverService;

import com.smartcom.api.model.Devices;
import com.smartcom.api.repository.DeviceRepository;
import com.smartcom.api.service.CryptoMngr;
import org.json.JSONArray;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Joseph Lejowa
 */
@Service
public class HTTPServerClass {

    @Autowired
    DeviceRepository deviceRepository;

    public HTTPServerClass() {

        int port = 9000;
        try {

            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            System.out.println("server started at " + port);
            server.createContext("/", new RootHandler());
            server.createContext("/echoGet", new EchoGetHandler());
            server.createContext("/echoHeader", new EchoHeaderHandler());
            server.createContext("/echoPost", new EchoPostHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException ex) {
            Logger.getLogger(HTTPServerClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class RootHandler implements HttpHandler {

        @Override

        public void handle(HttpExchange he) throws IOException {
            String response = "<h1>Please specify path </h1>";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }


    public class EchoHeaderHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            Headers headers = he.getRequestHeaders();
            Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
            String response = "<h1>Please specify path for smartcom</h1>";
            //for (Map.Entry<String, List<String>> entry : entries)
            //    response += entry.toString() + "\n";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }

    public class EchoGetHandler implements HttpHandler {

        @Override

        public void handle(HttpExchange he) throws IOException {
            // parse request
            Map<String, Object> parameters = new HashMap<String, Object>();
            URI requestedUri = he.getRequestURI();
            String query = requestedUri.getRawQuery();
            try {
                parseQuery(query, parameters);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // send response
            String response = "";
            for (String key : parameters.keySet())
                response += key + " = " + parameters.get(key) + "\n";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.toString().getBytes());

            os.close();
        }

    }

    public class EchoPostHandler implements HttpHandler {
        StringBuilder result = new StringBuilder();

        @Override

        public void handle(HttpExchange he) throws IOException {
            // parse request
            Map<String, Object> configFileResponse = new HashMap<String, Object>();
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);

            result = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                result.append(responseLine.trim());
            }
            try {
                parseQuery(result.toString(), configFileResponse);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // send response
            String response = "";
            for (String key : configFileResponse.keySet())
                response += key + "=" + configFileResponse.get(key) + ",\n";
            //encrypt response
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }

    public void parseQuery(String query, Map<String, Object> configFileResponse) throws UnsupportedEncodingException, ParseException {
        //System.out.println(query + "\n");

        org.json.JSONObject json = new JSONObject(query);
        System.out.println(json.toString());
        if (query != null) {
            /*    JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(query);*/


            //convert java object to JSON format
            // String json = gson.toJson(query);
               /* JSONArray array = new JSONArray(jsonObj);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    //  System.out.println(object.getString("deviceID"));
                    //  System.out.println(object.getString("command"));
                    //  if(object.getString("deviceID"))

                }*/
            try {
                String id = json.getString("deviceID");
                Devices devices = deviceRepository.findByMacaddress(id);

                File myObj = new File(id + ".txt");
                //File file=new File("C:\\Users\\nimbu\\IdeaProjects\\api\\" + id +".txt");
                String data = "";
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    data += myReader.nextLine();
                    //   System.out.println(data);

                }
                String deviceID = devices.getMacaddress();
                System.out.println(deviceID);

                byte[] cipherText = CryptoMngr.encrypt(devices.getEstate().getEncryptionKeys().getEncryptionKey(), devices.getEstate().getEncryptionKeys().getIV1(), data.getBytes());
                byte[] decrypted = CryptoMngr.decrypt(devices.getEstate().getEncryptionKeys().getEncryptionKey(), devices.getEstate().getEncryptionKeys().getIV1(), cipherText);
                System.out.println("3. Decrypted Message : " + new String(decrypted));
                System.out.println(devices.getEstate().getEncryptionKeys().getEncryptionKey());

                System.out.println(data);
                configFileResponse.put("encrypt", "0");
                configFileResponse.put(id, data);

                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
