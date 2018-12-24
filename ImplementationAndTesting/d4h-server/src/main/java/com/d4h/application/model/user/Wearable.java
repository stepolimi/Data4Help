package com.d4h.application.model.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Wearable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String name;

//    @ManyToMany
//    private AcquiredUserData userData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public AcquiredUserData getAcquiredUserData() {
        return userData;
    }

    public void setUserData(AcquiredUserData userData) {
        this.userData = userData;
    }*/
}
