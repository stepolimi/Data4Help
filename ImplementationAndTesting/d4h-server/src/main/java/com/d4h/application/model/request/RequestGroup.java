package com.d4h.application.model.request;

import com.d4h.application.model.groupOfUsers.GroupOfUsers;
import com.d4h.application.model.thirdParty.ThirdParty;

import javax.persistence.*;

@Entity
public class RequestGroup implements Request {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    private ThirdParty sender;

    @OneToOne (cascade = CascadeType.ALL)
    private RequestAttributes attributes;

    @OneToOne (cascade = CascadeType.ALL)
    private GroupOfUsers groupOfUsers;

    private boolean accepted;
    private boolean pending;
    private String thirdPartyId;
    private boolean subscribed;

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

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
