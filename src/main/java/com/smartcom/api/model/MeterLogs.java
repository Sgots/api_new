/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.Devices
 *  com.smartcom.api.model.MeterLogs
 */
package com.smartcom.api.model;

import com.smartcom.api.model.Devices;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
public class MeterLogs {
    private int id;
    private Double remainingEnergy;
    private Double power;
    private Double costToday;
    private Double voltage;
    private Devices devices;
    private Date date;
    private Double consumption;
    private Long month;


}

