package com.d4h.application.model.thirdParty;

import com.d4h.application.model.user.HealthParametersSent;
import com.d4h.application.model.user.Sex;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the health parameters and personal data of a user to be sent to a third party due to a previous request.
 */
public class AcquiredUserDataSent {
    private List<HealthParametersSent> healthParametersSents = new ArrayList<>();

    private String fiscalCode;
    private String name;
    private String surname;
    private int yearOfBirth;
    private int height;
    private int weight;
    private Sex sex;

    public List<HealthParametersSent> getHealthParametersSents() {
        return healthParametersSents;
    }

    public void setHealthParametersSents(List<HealthParametersSent> healthParametersSents) {
        this.healthParametersSents = healthParametersSents;
    }

    public void addHealthParametersSents(HealthParametersSent healthParametersSent) {
        this.healthParametersSents.add(healthParametersSent);
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
