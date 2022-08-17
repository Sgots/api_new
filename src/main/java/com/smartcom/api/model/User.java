package com.smartcom.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer userid;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private List<Estate> estate;

    public List<Estate> estate() {
        return estate;
    }

    public void setEstate(List<Estate> estate) {
        this.estate = estate;
    }


    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    private String firstName;

    private String lastName;
    @Column(name = "estate_count")
    @ColumnDefault("0")
    private Integer estatecount;
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @Column(name = "status", nullable = false)
    private boolean status;
    private String confirmationToken;

    private boolean isTempPassword;
    private String password2;


    public User() {
        //userid=0;
        this.userid = userid;
        username = "";
        password = "";
        password2 = "";
        firstName = "";
        lastName = "";
        email = "";
        role = "";
        enabled = false;
        status = false;
        confirmationToken = "";
        setTempPassword(false);
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEstatecount(Integer estatecount) {
        this.estatecount = estatecount;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getEstatecount() {
        return estatecount;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public boolean getStatus() {
        return status;
    }

    public void setEnabled(boolean value) {
        this.enabled = value;
    }

    public void setStatus(boolean value) {
        this.status = value;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public boolean isTempPassword() {
        return isTempPassword;
    }

    public void setTempPassword(boolean isTempPassword) {
        this.isTempPassword = isTempPassword;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof User)) {
            return false;
        }

        User u = (User) o;

        return u.getUsername().equals(this.getUsername()) && u.getEmail().equals(this.getEmail())
                && u.getRole().equals(this.getRole()) && (u.getEnabled() == this.getEnabled() && (u.getStatus() == this.getStatus()));
    }

}