package com.smartcom.api.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "announcements")
public class Announcements {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer announceid;
    private String title;
    private String content;
    private boolean deleteStatus;
    private Timestamp creationDate;

    public Announcements() {
        //id = 0;

        //user.setUserEstate(user.getUserEstate());


    }
}
