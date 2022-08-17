/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.model.Recharge
 */
package com.smartcom.api.model;

public class Recharge {
    private String token;
    private Integer deviceID;

    public String getToken() {
        return this.token;
    }

    public Integer getDeviceID() {
        return this.deviceID;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Recharge)) {
            return false;
        }
        Recharge other = (Recharge) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        String this$token = this.getToken();
        String other$token = other.getToken();
        if (this$token == null ? other$token != null : !this$token.equals(other$token)) {
            return false;
        }
        Integer this$deviceID = this.getDeviceID();
        Integer other$deviceID = other.getDeviceID();
        return !(this$deviceID == null ? other$deviceID != null : !((Object) this$deviceID).equals(other$deviceID));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Recharge;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $token = this.getToken();
        result = result * 59 + ($token == null ? 43 : $token.hashCode());
        Integer $deviceID = this.getDeviceID();
        result = result * 59 + ($deviceID == null ? 43 : ((Object) $deviceID).hashCode());
        return result;
    }

    public String toString() {
        return "Recharge(token=" + this.getToken() + ", deviceID=" + this.getDeviceID() + ")";
    }
}

