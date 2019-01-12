package com.d4h.application.model.groupOfUsers;

import com.d4h.application.model.request.RequestAttributes;
import com.d4h.application.model.user.HealthParameters;
import com.d4h.application.model.user.HealthParametersSent;

import java.util.ArrayList;
import java.util.List;

public class GroupUsersDataSent {
    private List<HealthParametersSent> healthParametersSents = new ArrayList<>();

    private RequestAttributes requestAttributes;

    public RequestAttributes getRequestAttributes() {
        return requestAttributes;
    }

    public void setRequestAttributes(RequestAttributes requestAttributes) {
        this.requestAttributes = requestAttributes;
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
