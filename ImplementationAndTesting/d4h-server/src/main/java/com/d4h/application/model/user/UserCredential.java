package com.d4h.application.model.user;

import javax.persistence.*;

/**
 * Credentials of a user, includes e-mail and password.
 */
@Entity
public class UserCredential {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String email;
    private String password;

    @OneToOne (cascade = CascadeType.ALL)
    private User user; // devo capire come verificare se registrazione o no

    public UserCredential() {}

    public UserCredential(String email, String password){
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(String id) {
        this.id = id;
    }
}
