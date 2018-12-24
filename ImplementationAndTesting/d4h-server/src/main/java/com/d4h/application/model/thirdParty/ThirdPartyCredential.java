package com.d4h.application.model.thirdParty;

import javax.persistence.*;

@Entity
public class ThirdPartyCredential {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String email;
    private String password;

    @OneToOne
    private ThirdParty thirdParty; // devo capire come verificare se registrazione o no

    public ThirdPartyCredential() {}

    public ThirdPartyCredential(String email, String password){
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
