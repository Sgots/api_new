package com.smartcom.api.model;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Timer;

@Data
@Entity
@Table
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer eventID;
    private String deviceID;
    private String eventName;
    private String startTime;
    private String startDate;
    private boolean durationBasedTimer;
    private String duration;
    private String endTime;
    private boolean isEventRecurring;
    private String recurringDays;
    private Integer estateID;


}
