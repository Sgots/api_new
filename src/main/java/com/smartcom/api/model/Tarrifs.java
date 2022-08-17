package com.smartcom.api.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
public class Tarrifs {
    @Id
    private String tarrifID;
    private String description;
    private String tarrifValue;

    public Tarrifs() {

    }
}
