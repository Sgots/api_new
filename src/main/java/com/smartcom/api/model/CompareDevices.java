package com.smartcom.api.model;

import lombok.Data;

@Data
public class CompareDevices {
    private String deviceID;
    private Double todayUsagePercentage;
    private Double weeklyUsagePercentage;
    private Double monthlyUsagePercentage;

}
