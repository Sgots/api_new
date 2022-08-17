/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.Notifications
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.Id
 *  javax.persistence.Table
 */
package com.smartcom.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notifications {
    @Id
    @Column(name = "id", nullable = false)
    private String notificationid;
    private String device_id;
    private Integer credit_notification;
    private Integer power_notification;
    private Integer energy_notification;

    public String getNotificationid() {
        return this.notificationid;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public Integer getCredit_notification() {
        return this.credit_notification;
    }

    public Integer getPower_notification() {
        return this.power_notification;
    }

    public Integer getEnergy_notification() {
        return this.energy_notification;
    }

    public void setNotificationid(String notificationid) {
        this.notificationid = notificationid;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setCredit_notification(Integer credit_notification) {
        this.credit_notification = credit_notification;
    }

    public void setPower_notification(Integer power_notification) {
        this.power_notification = power_notification;
    }

    public void setEnergy_notification(Integer energy_notification) {
        this.energy_notification = energy_notification;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Notifications)) {
            return false;
        }
        Notifications other = (Notifications) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        Integer this$credit_notification = this.getCredit_notification();
        Integer other$credit_notification = other.getCredit_notification();
        if (this$credit_notification == null ? other$credit_notification != null : !((Object) this$credit_notification).equals(other$credit_notification)) {
            return false;
        }
        Integer this$power_notification = this.getPower_notification();
        Integer other$power_notification = other.getPower_notification();
        if (this$power_notification == null ? other$power_notification != null : !((Object) this$power_notification).equals(other$power_notification)) {
            return false;
        }
        Integer this$energy_notification = this.getEnergy_notification();
        Integer other$energy_notification = other.getEnergy_notification();
        if (this$energy_notification == null ? other$energy_notification != null : !((Object) this$energy_notification).equals(other$energy_notification)) {
            return false;
        }
        String this$notificationid = this.getNotificationid();
        String other$notificationid = other.getNotificationid();
        if (this$notificationid == null ? other$notificationid != null : !this$notificationid.equals(other$notificationid)) {
            return false;
        }
        String this$device_id = this.getDevice_id();
        String other$device_id = other.getDevice_id();
        return !(this$device_id == null ? other$device_id != null : !this$device_id.equals(other$device_id));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Notifications;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $credit_notification = this.getCredit_notification();
        result = result * 59 + ($credit_notification == null ? 43 : ((Object) $credit_notification).hashCode());
        Integer $power_notification = this.getPower_notification();
        result = result * 59 + ($power_notification == null ? 43 : ((Object) $power_notification).hashCode());
        Integer $energy_notification = this.getEnergy_notification();
        result = result * 59 + ($energy_notification == null ? 43 : ((Object) $energy_notification).hashCode());
        String $notificationid = this.getNotificationid();
        result = result * 59 + ($notificationid == null ? 43 : $notificationid.hashCode());
        String $device_id = this.getDevice_id();
        result = result * 59 + ($device_id == null ? 43 : $device_id.hashCode());
        return result;
    }

    public String toString() {
        return "Notifications(notificationid=" + this.getNotificationid() + ", device_id=" + this.getDevice_id() + ", credit_notification=" + this.getCredit_notification() + ", power_notification=" + this.getPower_notification() + ", energy_notification=" + this.getEnergy_notification() + ")";
    }
}

