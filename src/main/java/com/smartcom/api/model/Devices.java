/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.AuditModel
 *  com.smartcom.api.model.DeviceTypes
 *  com.smartcom.api.model.Devices
 *  com.smartcom.api.model.Estate
 *  javax.persistence.CascadeType
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.FetchType
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.JoinColumn
 *  javax.persistence.ManyToOne
 *  javax.persistence.OneToOne
 *  javax.persistence.Table
 *  org.hibernate.annotations.ColumnDefault
 */
package com.smartcom.api.model;

import com.smartcom.api.model.AuditModel;
import com.smartcom.api.model.DeviceTypes;
import com.smartcom.api.model.Estate;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "devices")
public class Devices
        extends AuditModel
        implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deviceid", nullable = false)
    private Integer deviceid;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "estate_id", nullable = false)
    private Estate estate = new Estate();
    @Column(name = "device_name", nullable = false)
    private String device_name = "";
    @Column(name = "delete_status", nullable = false)
    private boolean delete_status = false;
    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "device_type", referencedColumnName = "id")
    private DeviceTypes deviceTypes = new DeviceTypes();
    private String macaddress = "";
    @Column(name = "device_status", nullable = false)
    private Boolean active = false;
    @Column(name = "credit", nullable = false)
    private Integer credit = 20;
    @Column(name = "power", nullable = false)
    private Integer power = 20;
    @Column(name = "energy", nullable = false)
    private Integer energy = 20;
    @Column(name = "credit_action", nullable = false)
    private Integer creditAction = 1;
    @Column(name = "power_action", nullable = false)
    private Integer powerAction = 2;
    @Column(name = "energy_action", nullable = false)
    private Integer energyAction = 3;
    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;
    @ColumnDefault(value = "0")
    private Double remainingEnergy;
    @ColumnDefault(value = "0")
    private Double remainingCredit;
    @ColumnDefault(value = "0")
    private Double currentPower;

    public boolean isHasEvent() {
        return hasEvent;
    }

    public void setHasEvent(boolean hasEvent) {
        this.hasEvent = hasEvent;
    }

    @Column(name = "event", nullable = false)
    private boolean hasEvent;

    public Double getEnergyUsed() {
        return energyUsed;
    }

    public void setEnergyUsed(Double energyUsed) {
        this.energyUsed = energyUsed;
    }

    public Double getCostToday() {
        return costToday;
    }

    public void setCostToday(Double costToday) {
        this.costToday = costToday;
    }

    @ColumnDefault(value = "0.0")
    private Double energyUsed = 0.0;
    @ColumnDefault(value = "0.0")
    private Double costToday = 0.0;

    public Integer getDeviceid() {
        return this.deviceid;
    }

    public Estate getEstate() {
        return this.estate;
    }

    public String getDevice_name() {
        return this.device_name;
    }

    public boolean isDelete_status() {
        return this.delete_status;
    }

    public DeviceTypes getDeviceTypes() {
        return this.deviceTypes;
    }

    public String getMacaddress() {
        return this.macaddress;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Integer getCredit() {
        return this.credit;
    }

    public Integer getPower() {
        return this.power;
    }

    public Integer getEnergy() {
        return this.energy;
    }

    public Integer getCreditAction() {
        return this.creditAction;
    }

    public Integer getPowerAction() {
        return this.powerAction;
    }

    public Integer getEnergyAction() {
        return this.energyAction;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Double getRemainingEnergy() {
        return this.remainingEnergy;
    }

    public Double getRemainingCredit() {
        return this.remainingCredit;
    }

    public Double getCurrentPower() {
        return this.currentPower;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public void setEstate(Estate estate) {
        this.estate = estate;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public void setDelete_status(boolean delete_status) {
        this.delete_status = delete_status;
    }

    public void setDeviceTypes(DeviceTypes deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public void setCreditAction(Integer creditAction) {
        this.creditAction = creditAction;
    }

    public void setPowerAction(Integer powerAction) {
        this.powerAction = powerAction;
    }

    public void setEnergyAction(Integer energyAction) {
        this.energyAction = energyAction;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRemainingEnergy(Double remainingEnergy) {
        this.remainingEnergy = remainingEnergy;
    }

    public void setRemainingCredit(Double remainingCredit) {
        this.remainingCredit = remainingCredit;
    }

    public void setCurrentPower(Double currentPower) {
        this.currentPower = currentPower;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Devices)) {
            return false;
        }
        Devices other = (Devices) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        if (this.isDelete_status() != other.isDelete_status()) {
            return false;
        }
        if (this.isEnabled() != other.isEnabled()) {
            return false;
        }
        Integer this$deviceid = this.getDeviceid();
        Integer other$deviceid = other.getDeviceid();
        if (this$deviceid == null ? other$deviceid != null : !((Object) this$deviceid).equals(other$deviceid)) {
            return false;
        }
        Boolean this$active = this.getActive();
        Boolean other$active = other.getActive();
        if (this$active == null ? other$active != null : !((Object) this$active).equals(other$active)) {
            return false;
        }
        Integer this$credit = this.getCredit();
        Integer other$credit = other.getCredit();
        if (this$credit == null ? other$credit != null : !((Object) this$credit).equals(other$credit)) {
            return false;
        }
        Integer this$power = this.getPower();
        Integer other$power = other.getPower();
        if (this$power == null ? other$power != null : !((Object) this$power).equals(other$power)) {
            return false;
        }
        Integer this$energy = this.getEnergy();
        Integer other$energy = other.getEnergy();
        if (this$energy == null ? other$energy != null : !((Object) this$energy).equals(other$energy)) {
            return false;
        }
        Integer this$creditAction = this.getCreditAction();
        Integer other$creditAction = other.getCreditAction();
        if (this$creditAction == null ? other$creditAction != null : !((Object) this$creditAction).equals(other$creditAction)) {
            return false;
        }
        Integer this$powerAction = this.getPowerAction();
        Integer other$powerAction = other.getPowerAction();
        if (this$powerAction == null ? other$powerAction != null : !((Object) this$powerAction).equals(other$powerAction)) {
            return false;
        }
        Integer this$energyAction = this.getEnergyAction();
        Integer other$energyAction = other.getEnergyAction();
        if (this$energyAction == null ? other$energyAction != null : !((Object) this$energyAction).equals(other$energyAction)) {
            return false;
        }
        Double this$remainingEnergy = this.getRemainingEnergy();
        Double other$remainingEnergy = other.getRemainingEnergy();
        if (this$remainingEnergy == null ? other$remainingEnergy != null : !((Object) this$remainingEnergy).equals(other$remainingEnergy)) {
            return false;
        }
        Double this$remainingCredit = this.getRemainingCredit();
        Double other$remainingCredit = other.getRemainingCredit();
        if (this$remainingCredit == null ? other$remainingCredit != null : !((Object) this$remainingCredit).equals(other$remainingCredit)) {
            return false;
        }
        Double this$currentPower = this.getCurrentPower();
        Double other$currentPower = other.getCurrentPower();
        if (this$currentPower == null ? other$currentPower != null : !((Object) this$currentPower).equals(other$currentPower)) {
            return false;
        }
        Estate this$estate = this.getEstate();
        Estate other$estate = other.getEstate();
        if (this$estate == null ? other$estate != null : !this$estate.equals((Object) other$estate)) {
            return false;
        }
        String this$device_name = this.getDevice_name();
        String other$device_name = other.getDevice_name();
        if (this$device_name == null ? other$device_name != null : !this$device_name.equals(other$device_name)) {
            return false;
        }
        DeviceTypes this$deviceTypes = this.getDeviceTypes();
        DeviceTypes other$deviceTypes = other.getDeviceTypes();
        if (this$deviceTypes == null ? other$deviceTypes != null : !this$deviceTypes.equals((Object) other$deviceTypes)) {
            return false;
        }
        String this$macaddress = this.getMacaddress();
        String other$macaddress = other.getMacaddress();
        return !(this$macaddress == null ? other$macaddress != null : !this$macaddress.equals(other$macaddress));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Devices;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isDelete_status() ? 79 : 97);
        result = result * 59 + (this.isEnabled() ? 79 : 97);
        Integer $deviceid = this.getDeviceid();
        result = result * 59 + ($deviceid == null ? 43 : ((Object) $deviceid).hashCode());
        Boolean $active = this.getActive();
        result = result * 59 + ($active == null ? 43 : ((Object) $active).hashCode());
        Integer $credit = this.getCredit();
        result = result * 59 + ($credit == null ? 43 : ((Object) $credit).hashCode());
        Integer $power = this.getPower();
        result = result * 59 + ($power == null ? 43 : ((Object) $power).hashCode());
        Integer $energy = this.getEnergy();
        result = result * 59 + ($energy == null ? 43 : ((Object) $energy).hashCode());
        Integer $creditAction = this.getCreditAction();
        result = result * 59 + ($creditAction == null ? 43 : ((Object) $creditAction).hashCode());
        Integer $powerAction = this.getPowerAction();
        result = result * 59 + ($powerAction == null ? 43 : ((Object) $powerAction).hashCode());
        Integer $energyAction = this.getEnergyAction();
        result = result * 59 + ($energyAction == null ? 43 : ((Object) $energyAction).hashCode());
        Double $remainingEnergy = this.getRemainingEnergy();
        result = result * 59 + ($remainingEnergy == null ? 43 : ((Object) $remainingEnergy).hashCode());
        Double $remainingCredit = this.getRemainingCredit();
        result = result * 59 + ($remainingCredit == null ? 43 : ((Object) $remainingCredit).hashCode());
        Double $currentPower = this.getCurrentPower();
        result = result * 59 + ($currentPower == null ? 43 : ((Object) $currentPower).hashCode());
        Estate $estate = this.getEstate();
        result = result * 59 + ($estate == null ? 43 : $estate.hashCode());
        String $device_name = this.getDevice_name();
        result = result * 59 + ($device_name == null ? 43 : $device_name.hashCode());
        DeviceTypes $deviceTypes = this.getDeviceTypes();
        result = result * 59 + ($deviceTypes == null ? 43 : $deviceTypes.hashCode());
        String $macaddress = this.getMacaddress();
        result = result * 59 + ($macaddress == null ? 43 : $macaddress.hashCode());
        return result;
    }

    public String toString() {
        return "Devices(deviceid=" + this.getDeviceid() + ", estate=" + this.getEstate() + ", device_name=" + this.getDevice_name() + ", delete_status=" + this.isDelete_status() + ", deviceTypes=" + this.getDeviceTypes() + ", macaddress=" + this.getMacaddress() + ", active=" + this.getActive() + ", credit=" + this.getCredit() + ", power=" + this.getPower() + ", energy=" + this.getEnergy() + ", creditAction=" + this.getCreditAction() + ", powerAction=" + this.getPowerAction() + ", energyAction=" + this.getEnergyAction() + ", enabled=" + this.isEnabled() + ", remainingEnergy=" + this.getRemainingEnergy() + ", remainingCredit=" + this.getRemainingCredit() + ", currentPower=" + this.getCurrentPower() + ")";
    }
}

