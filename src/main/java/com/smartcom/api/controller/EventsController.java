package com.smartcom.api.controller;

import com.smartcom.api.model.Announcements;
import com.smartcom.api.model.Devices;
import com.smartcom.api.model.Events;
import com.smartcom.api.repository.DeviceRepository;
import com.smartcom.api.repository.EstateRepository;
import com.smartcom.api.repository.EventsRepo;
import com.smartcom.api.service.MessagingService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class EventsController {

    @Autowired
    EventsRepo eventsRepo;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    EstateRepository estateRepository;
    Integer estateid;
    String mac;

    @GetMapping(path = "device/event/{device_id}")
    public ResponseEntity<?> getEvent(@PathVariable(value = "device_id") String device_id) {
        Events events = eventsRepo.findByDeviceID(device_id);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "event", events)
        );
    }

    @GetMapping(path = "device/event/config/{estate_id}")
    public ResponseEntity<?> getEvents(@PathVariable(value = "estate_id") Integer estate_id) throws MqttException {
        Devices devices = deviceRepository.findAllByEstateIdAndDevice_type2(estate_id);
        List<Events> events = eventsRepo.findAllByEstateID(estate_id);
        if (events != null || !events.isEmpty()) {
            for (Events events3 : events) {
                estateid = events3.getEstateID();
                mac = events3.getDeviceID();
                deviceRepository.findByDeviceIdEvent(mac);
            }

            MessagingService messagingService = new MessagingService();
            messagingService.publishEvent(events, estateid, devices.getMacaddress());
            estateRepository.findByEstateIdEventReverse(estateid);
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "deviceID", devices.getMacaddress(),
                "command", "timedEventsRequest",
                "events", events)
        );
    }

    @PutMapping(path = {"/delete/event/"})
    public ResponseEntity<?> eventsDelete(@Valid @RequestBody Events events) {
        Events events1 = eventsRepo.findByDeviceID(events.getDeviceID());
        deviceRepository.findByDeviceIdEventAndDel(events1.getDeviceID());

        eventsRepo.deleteEventsByDeviceID(events1.getDeviceID());
        estateRepository.findByEstateIdEvent(events1.getEstateID());
      /*  List<Events> events = eventsRepo.findAllByEstateID(estate_id);
        if(events!=null || !events.isEmpty()){
            for (Events events3 : events) {
                estateid = events3.getEstateID();
                mac = events3.getDeviceID();
                deviceRepository.findByDeviceIdEvent(mac);
            }*/


        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved")
        );
    }

    /*@PostMapping(path = {"/device/events/add"})
    public ResponseEntity<?> addEvent(@Valid @RequestBody List <Events> events) throws MqttException, InterruptedException {
     //Events events1 =  eventsRepo.findByDeviceID(events.getDeviceID());
   *//*  if(events1!=null){
         return new ResponseEntity((Object)("Event already exist"), HttpStatus.NOT_FOUND);

     }*//*

            Events events1 = new Events();
           // eventsRepo.deleteAll();

        for (Events events3 : events) {
            estateid = events3.getEstateID();
            mac = events3.getDeviceID();
          *//*  String days = events1.getRecurringDays().toString();
            days = days.replace("{","").replace("}","");
            String days2[] = days.split(",");
            System.out.println(Arrays.toString(days2));

            //System.out.println(days2);
            events1.setRecurringDays(Arrays.toString(days2).replace("{","").replace("}",""));
*//*
        }
      eventsRepo.deleteEventsByEstateID(estateid);
        Thread.sleep(1000);
        eventsRepo.saveAll(events);
        deviceRepository.findByDeviceIdEvent(mac);
        events1 = eventsRepo.findByEventID();

        MessagingService messagingService = new MessagingService();
messagingService.publishEvent(events,estateid);
        return ResponseEntity.status((HttpStatus)HttpStatus.OK).body(Map.of("error", "false"));

    }*/
    @PostMapping(path = {"/device/event/add"})
    public ResponseEntity<?> addOneEvent(@Valid @RequestBody Events events) throws MqttException, InterruptedException {
        //Events events1 =  eventsRepo.findByDeviceID(events.getDeviceID());
   /*  if(events1!=null){
         return new ResponseEntity((Object)("Event already exist"), HttpStatus.NOT_FOUND);

     }*/
        if (eventsRepo.findByDeviceID(events.getDeviceID()) != null) {
            eventsRepo.deleteEventsByDeviceID(events.getDeviceID());
        }
        Thread.sleep(1000);
        estateRepository.findByEstateIdEvent(events.getEstateID());
        eventsRepo.save(events);
        //   deviceRepository.findByDeviceIdEvent(mac);
        //  events1 = eventsRepo.findByEventID();

     /*   MessagingService messagingService = new MessagingService();
        messagingService.publishEvent(events,estateid);*/
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false"));

    }

    @GetMapping(path = "event/allEvents")
    public ResponseEntity<?> getAllEvents() {
        List events = eventsRepo.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "error", "false",
                "message", "retrieved",
                "events", events)
        );
    }

    @PutMapping(value = {"device/updateEvent"})
    public ResponseEntity<?> updateEvent(@RequestBody Events events) {
        Events events1 = eventsRepo.findByDeviceID(events.getDeviceID());
        events1.setRecurringDays(events.getRecurringDays());
        events1.setEventName(events.getEventName());
        events1.setDuration(events.getDuration());
        events1.setEventRecurring(events.isEventRecurring());
        events1.setStartDate(events.getStartDate());
        events1.setStartTime(events.getStartTime());
        events1.setDurationBasedTimer(events.isDurationBasedTimer());
        events1.setEndTime(events.getEndTime());
        estateRepository.findByEstateIdEvent(events1.getEstateID());
        eventsRepo.save(events1);
        return ResponseEntity.status((HttpStatus) HttpStatus.OK).body(Map.of("error", "false", "message", "retrieved"));


    }
}
