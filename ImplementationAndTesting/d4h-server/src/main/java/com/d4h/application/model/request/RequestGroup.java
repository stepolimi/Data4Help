package com.d4h.application.model.request;

import com.d4h.application.model.groupOfUsers.GroupOfUsers;
import com.d4h.application.model.thirdParty.ThirdParty;
import com.d4h.application.model.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RequestGroup implements Request {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    private ThirdParty sender;

    @OneToOne
    private RequestAttributes attributes;

    @OneToOne
    private GroupOfUsers groupOfUsers;

    private String motivation;
    private boolean accepted;
    private boolean pending;

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public GroupOfUsers getGroupOfUsers() {
        return groupOfUsers;
    }

    public void setGroupOfUsers(GroupOfUsers groupOfUsers) {
        this.groupOfUsers = groupOfUsers;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setAccepted(boolean accepted) {this.accepted = accepted; }

    @Override
    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public String getMotivation() {
        return motivation;
    }

    @Override
    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public RequestAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(RequestAttributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public ThirdParty getSender() {
        return sender;
    }

    @Override
    public void setSender(ThirdParty sender) {
        this.sender = sender;
    }
}
