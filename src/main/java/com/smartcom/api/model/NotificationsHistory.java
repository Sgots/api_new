/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonFormat
 *  com.smartcom.api.model.NotificationsHistory
 *  javax.persistence.Entity
 *  javax.persistence.Id
 *  javax.persistence.Table
 */
package com.smartcom.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class NotificationsHistory {
    @Id
    private String id;
    private String deviceID;
    private String status;
    private String command;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+02:00")
    private Date timestamp;
    private boolean read_state;

    public String getId() {
        return this.id;
    }

    public String getDeviceID() {
        return this.deviceID;
    }

    public String getStatus() {
        return this.status;
    }

    public String getCommand() {
        return this.command;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public boolean isRead_state() {
        return this.read_state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+02:00")
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setRead_state(boolean read_state) {
        this.read_state = read_state;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NotificationsHistory)) {
            return false;
        }
        NotificationsHistory other = (NotificationsHistory) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        if (this.isRead_state() != other.isRead_state()) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$deviceID = this.getDeviceID();
        String other$deviceID = other.getDeviceID();
        if (this$deviceID == null ? other$deviceID != null : !this$deviceID.equals(other$deviceID)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$command = this.getCommand();
        String other$command = other.getCommand();
        if (this$command == null ? other$command != null : !this$command.equals(other$command)) {
            return false;
        }
        Date this$timestamp = this.getTimestamp();
        Date other$timestamp = other.getTimestamp();
        return !(this$timestamp == null ? other$timestamp != null : !((Object) this$timestamp).equals(other$timestamp));
    }

    protected boolean canEqual(Object other) {
        return other instanceof NotificationsHistory;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isRead_state() ? 79 : 97);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $deviceID = this.getDeviceID();
        result = result * 59 + ($deviceID == null ? 43 : $deviceID.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $command = this.getCommand();
        result = result * 59 + ($command == null ? 43 : $command.hashCode());
        Date $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object) $timestamp).hashCode());
        return result;
    }

    public String toString() {
        return "NotificationsHistory(id=" + this.getId() + ", deviceID=" + this.getDeviceID() + ", status=" + this.getStatus() + ", command=" + this.getCommand() + ", timestamp=" + this.getTimestamp() + ", read_state=" + this.isRead_state() + ")";
    }
}

