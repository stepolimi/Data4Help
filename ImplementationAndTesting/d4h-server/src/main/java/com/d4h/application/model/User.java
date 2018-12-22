package com.d4h.application.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id @GeneratedValue
    private int id;

    @OneToOne
    private Credential credential;

    @OneToOne
    private UserData userData;

    @OneToMany
    private List<HealthParameters> healthParameters;


    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<HealthParameters> getHealthParameters() { return healthParameters; }

    public void setHealthParameters(List<HealthParameters> healthParameters) { this.healthParameters = healthParameters; }
}

