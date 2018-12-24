package com.d4h.application.model.user;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private UserCredential credential;

    @OneToOne
    private UserData userData;

    @OneToMany
    private List<HealthParameters> healthParameters = new ArrayList<>();


    public UserCredential getCredential() {
        return credential;
    }

    public void setCredential(UserCredential credential) {
        this.credential = credential;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<HealthParameters> getHealthParameters() { return healthParameters; }

    public void addHealthParameters(HealthParameters healthParameters) { this.healthParameters.add(healthParameters); }

    public void setHealthParameters(List<HealthParameters> healthParameters) { this.healthParameters = healthParameters; }
}

