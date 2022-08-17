package com.smartcom.api.model;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table
@Entity
public class Banner extends AuditModel {


    public Banner() {

        super();

    }


    public Banner(String name, String type, byte[] picByte) {

        this.name = name;

        this.type = type;

        this.picByte = picByte;

    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")

    private String type;
    @Column(name = "picByte", length = 1000)

    private byte[] picByte;
    private boolean deleteStatus;

    public String getName() {

        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {

        return type;

    }

    public void setType(String type) {

        this.type = type;

    }

    public byte[] getPicByte() {

        return picByte;

    }

    public void setPicByte(byte[] picByte) {
        this.picByte = picByte;

    }


}
