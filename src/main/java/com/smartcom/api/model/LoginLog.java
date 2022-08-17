package com.smartcom.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer logId;
    private Timestamp createdAt;
    private String activity;
    private String email;
}
