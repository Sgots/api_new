package com.smartcom.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table
public class SystemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer systemID;
    private Timestamp createdAt;
    private String email;
    private String activity;
}
