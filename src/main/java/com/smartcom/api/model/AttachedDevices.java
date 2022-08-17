package com.smartcom.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "attached")
public class AttachedDevices extends AuditModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attached_id", nullable = false)
    @JsonIgnore
    private Integer attachmentID;
    private String deviceID;
    private String deviceName;
    private String deviceTypeID;
    @Column(name = "credit", nullable = false)
    private Integer creditThreshold = 20;
    @Column(name = "power", nullable = false)
    private Integer powerThreshold = 20;
    @Column(name = "energy", nullable = false)
    private Integer energyThreshold = 20;
    private String stateControlTopic;
    private String notificationTopic;
    private Integer estateID;
    private boolean deleteStatus;


}
