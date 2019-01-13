package com.d4h.application.model.groupOfUsers;

import com.d4h.application.model.request.RequestAttributesSent;
import com.d4h.application.model.user.HealthParametersSent;

import java.util.ArrayList;
import java.util.List;

public class GroupUsersDataSent {
    private List<HealthParametersSent> healthParametersSents = new ArrayList<>();

    private RequestAttributesSent requestAttributesSent;

    public RequestAttributesSent getRequestAttributesSent() {
        return requestAttributesSent;
    }

    public void setRequestAttributesSent(RequestAttributesSent requestAttributesSent) {
        this.requestAttributesSent = requestAttributesSent;
    }

    public List<HealthParametersSent> getHealthParametersSents() {
        return healthParametersSents;
    }

    public void addHealthParametersSents(HealthParametersSent healthParametersSent){
        this.healthParametersSents.add(healthParametersSent);
    }

    public void setHealthParametersSents(List<HealthParametersSent> healthParametersSents) {
        this.healthParametersSents = healthParametersSents;
    }
}
