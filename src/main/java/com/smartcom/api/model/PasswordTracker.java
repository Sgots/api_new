package com.smartcom.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table
public class PasswordTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer trackId;
    private Timestamp createdAt;
    private Timestamp expiresAt;
    private String state;
    private String email;

}
