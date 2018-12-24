package com.d4h.application.model.thirdParty;


import com.d4h.application.model.user.HealthParameters;
import com.d4h.application.model.user.UserData;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AcquiredUserData {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToMany
    private List<HealthParameters> healthParams = new ArrayList<>();

    @OneToOne
    private UserData userData;

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<HealthParameters> getHealthParams() {
        return healthParams;
    }

    public void addHealthParam(HealthParameters healthParam) {
        healthParams.add(healthParam);
    }

}
