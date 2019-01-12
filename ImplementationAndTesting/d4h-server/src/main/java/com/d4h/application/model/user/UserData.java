package com.d4h.application.model.user;

import org.apache.openjpa.persistence.InverseLogical;
import org.apache.openjpa.persistence.jdbc.Unique;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class UserData {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Unique
    private String fiscalCode;

    private String name;
    private String surname;
    private int yearOfBirth;
    private int height;
    private int weight;
    private Sex sex;
    private Status status;
    private String userId;

    @OneToOne (cascade = CascadeType.ALL)
    private Address address;

    @OneToOne (cascade = CascadeType.ALL)
    private User user;

    @OneToMany (cascade = CascadeType.ALL)
    private List<Wearable> wearable;


    public String getFiscalCode() { return fiscalCode; }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public void setFiscalCode(String fiscalCode) { this.fiscalCode = fiscalCode; }

    public void setName(String name) { this.name = name; }

    public void setSurname(String surname) { this.surname = surname; }

    public int getHeight() { return height; }

    public void setHeight(int height) { this.height = height; }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public List<Wearable> getWearable() {
        return wearable;
    }

    public void setWearable(List<Wearable> wearable) {
        this.wearable = wearable;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
