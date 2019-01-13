package com.d4h.application.model.user;

import com.d4h.application.model.request.RequestUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of a user, contains his credentials, data, health parameters and accepted requests.
 */
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne (cascade = CascadeType.ALL)
    private UserCredential credential;

    @OneToOne (cascade = CascadeType.ALL)
    private UserData userData;

    @OneToMany (cascade = CascadeType.ALL)
    private List<HealthParameters> healthParameters = new ArrayList<>();

    @OneToMany (cascade = CascadeType.ALL)
    private List<RequestUser> requests = new ArrayList<>();

    public List<RequestUser> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestUser> requests) {
        this.requests = requests;
    }

    public void addRequest(RequestUser requestUser){
        requests.add(requestUser);
    }

    public UserCredential getCredential() {
        return credential;
    }

    public void setCredential(UserCredential credential) {
        this.credential = credential;
    }

    public String getId() {
        return id;
    }

    public void setId(String userId) {
        this.id = userId;
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

