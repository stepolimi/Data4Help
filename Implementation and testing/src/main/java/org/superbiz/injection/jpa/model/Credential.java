package org.superbiz.injection.jpa.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Credential {
    @Id
    private int id;

    private String email;
    private String password;

    @OneToOne
    private User user; // devo capire come verificare se registrazione o no

    public Credential() {}

    public Credential(String email, String password){
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
