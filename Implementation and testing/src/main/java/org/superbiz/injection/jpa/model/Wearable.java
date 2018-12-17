package org.superbiz.injection.jpa.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Wearable {
    @Id
    private int id;

    private String name;

//    @ManyToMany
//    private UserData userData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }*/
}
