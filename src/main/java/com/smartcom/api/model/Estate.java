package com.smartcom.api.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "estate")
public class Estate extends AuditModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "estate_id", nullable = false)
    private Integer estateid;
    @JsonIgnoreProperties(value = {"estate", "hibernateLazyInitializer"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

/*@JsonIgnore
    @ManyToMany(cascade = {CascadeType.REMOVE},fetch = FetchType.LAZY)
    @JoinTable(name = "user_estates", joinColumns = {@JoinColumn(name = "estate_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users = new HashSet<User>();*/


    @Column(name = "estate_name", nullable = false)
    private String estatename;

    @Column(name = "estate_address", nullable = false)
    private String estateaddress;

    @Column(name = "meterdataFreq", nullable = false)
    private String meterdatafreq;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @Column(name = "savedevent", nullable = false)
    private boolean savedevent;
    @Column(name = "delete_status", nullable = false)
    private boolean delete_status;
    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            mappedBy = "estate")
    private EncryptionKeys encryptionKeys;

    public Estate() {
        //id = 0;
        user = new User();

        estatename = "";
        meterdatafreq = "";
        estateaddress = "";
        enabled = false;
        //user.setUserEstate(user.getUserEstate());


    }
}
/*    public void addUser(User user) {
        this.users.add(user);
    }
}*/
