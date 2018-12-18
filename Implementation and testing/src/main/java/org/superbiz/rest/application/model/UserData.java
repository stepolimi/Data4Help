package org.superbiz.rest.application.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.List;

@Entity
public class UserData {

    @Id
    private int id;

    private String fiscalCode;
    private String name;
    private String surname;
    private Date dateOfBirth;
    private int height;
    private int weight;
    private Sex sex;
    private Status status;

    @OneToOne
    private Address address;

    @OneToOne
    private User user;

    @ManyToMany
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
