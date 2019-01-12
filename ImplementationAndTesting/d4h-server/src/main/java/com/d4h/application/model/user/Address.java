package com.d4h.application.model.user;

import com.d4h.application.model.thirdParty.ThirdParty;

import javax.persistence.*;

@Entity
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private int cap;
    private int number;
    private String street;
    private String city;
    private String region;
    private String state;

    @OneToOne (cascade = CascadeType.ALL)
    private User user;

    @OneToOne (cascade = CascadeType.ALL)
    private ThirdParty thirdParty;

    public ThirdParty getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(ThirdParty thirdParty) {
        this.thirdParty = thirdParty;
    }

    public int getCap() { return cap; }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public int getNum() {
        return number;
    }

    public void setNum(int num) {
        this.number = num;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
