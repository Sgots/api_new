/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonFormat
 *  com.smartcom.api.model.RechargeHistoryModel
 *  javax.persistence.Entity
 *  javax.persistence.Id
 *  javax.persistence.Table
 */
package com.smartcom.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "recharge_response")
public class RechargeHistoryModel {
    @Id
    private String id;
    private Double remainingCredit;
    private Double remainingEnergy;
    private Double previousRemainingEnergy;
    private String rechargeToken;
    private String deviceID;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+02:00")
    private Date date;
    private String reason;
    private Double amount;


}

