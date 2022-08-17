/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.DeviceTypes
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.Table
 */
package com.smartcom.api.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "device_types")
public class DeviceTypes {
    @Id
    @Column(name = "id", nullable = false)
    private String typeid = "";
    private String type;

    public DeviceTypes() {
    }

    public DeviceTypes(String typeid, String type) {
        this.type = type;
        this.typeid = typeid;
    }

}

