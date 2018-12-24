package com.d4h.application.model.thirdParty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ThirdPartyData {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String name;
    private int fiscalCode;
    private int pIva;
    private TypeOfSociety type;

    public TypeOfSociety getType() {
        return type;
    }

    public void setType(TypeOfSociety type) {
        this.type = type;
    }

    public int getpIva() {
        return pIva;
    }

    public void setpIva(int pIva) {
        this.pIva = pIva;
    }

    public int getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(int fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
