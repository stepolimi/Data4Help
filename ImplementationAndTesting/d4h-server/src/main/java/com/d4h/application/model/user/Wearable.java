package com.d4h.application.model.user;

import javax.persistence.*;

@Entity
public class Wearable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String name;

    private WearableType type;

    @ManyToOne
    private UserData userData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WearableType getWearableType() {
        return type;
    }

    public void setWearableType(WearableType type) {
        this.type = type;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
