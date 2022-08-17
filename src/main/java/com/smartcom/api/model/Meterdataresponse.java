/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.Meterdataresponse
 *  javax.persistence.Entity
 *  javax.persistence.Id
 *  javax.persistence.Table
 */
package com.smartcom.api.model;

import lombok.Data;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "dataresponse")
public class Meterdataresponse {
    @Id
    private Timestamp date = null;
    private Double current = null;
    private Double remainingEnergy;
    private Double remainingCredit;
    private Double totalEnergy;
    private Double power;
    private String deviceTypeID;
    private String macaddress;
    private String command;
    private Double voltage;
    private Double energyEst;
    private Double energyCost;
    private Double energyUsedToday;
    private Double costPerKwh;
    private Double costToday;
    private String timeStamp;
    private Integer estateID;

}

