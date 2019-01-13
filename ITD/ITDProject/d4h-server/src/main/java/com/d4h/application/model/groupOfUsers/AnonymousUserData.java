package com.d4h.application.model.groupOfUsers;

import com.d4h.application.model.user.HealthParameters;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the health parameters of the users for a group of users data request.
 */
@Entity
public class AnonymousUserData {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToMany (cascade = CascadeType.ALL)
    private List<HealthParameters> healthParams = new ArrayList<>();

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

    public void addHealthParameters(List<HealthParameters> healthParameters) { this.healthParams.addAll(healthParameters); }

}
