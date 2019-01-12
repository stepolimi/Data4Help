package com.d4h.application.model.thirdParty;

import com.d4h.application.model.user.Address;

import javax.persistence.*;

@Entity
public class ThirdPartyData {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String name;
    private String pIva;
    private String typeSociety;
    private String thirdPartyId;

    @OneToOne
    private ThirdParty thirdParty;

    @OneToOne (cascade = CascadeType.ALL)
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public ThirdParty getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(ThirdParty thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getType() {
        return typeSociety;
    }

    public void setType(String type) {
        this.typeSociety = type;
    }

    public String getpIva() {
        return pIva;
    }

    public void setpIva(String pIva) {
        this.pIva = pIva;
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
