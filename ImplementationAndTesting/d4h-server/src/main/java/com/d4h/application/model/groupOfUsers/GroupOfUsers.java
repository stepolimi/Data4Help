package com.d4h.application.model.groupOfUsers;

import com.d4h.application.model.request.RequestGroup;

import javax.persistence.*;

@Entity
public class GroupOfUsers {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private RequestGroup request;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RequestGroup getRequest() {
        return request;
    }

    public void setRequest(RequestGroup request) {
        this.request = request;
    }
}
