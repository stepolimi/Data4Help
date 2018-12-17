package org.superbiz.injection.jpa.model;

import javax.persistence.*;

@Entity
public class User{

    @Id
    private int id;

    @OneToOne
    private Credential credential;

    @OneToOne
    private UserData userData;

    //@OneToMany
    //private HealthParameters healthParameters;

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

/*    public HealthParameters getHealthParameters() {
        return healthParameters;
    }

    public void setHealthParameters(HealthParameters healthParameters) {
        this.healthParameters = healthParameters;
    }
*/
}

