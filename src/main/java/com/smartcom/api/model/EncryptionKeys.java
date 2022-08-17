package com.smartcom.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table
public class EncryptionKeys implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "encrypt_id", nullable = false)
    private Integer encryptID;

    private byte[] encryptionKey;
    private byte[] IV1;
    private byte[] IV2;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estate_id", nullable = false)
    private Estate estate;

}
