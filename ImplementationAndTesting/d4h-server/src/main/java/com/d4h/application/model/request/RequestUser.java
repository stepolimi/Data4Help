package com.d4h.application.model.request;

import com.d4h.application.model.thirdParty.AcquiredUserData;
import com.d4h.application.model.thirdParty.ThirdParty;
import com.d4h.application.model.user.User;

import javax.persistence.*;

@Entity
public class RequestUser implements Request {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    private ThirdParty sender;

    @OneToOne
    private User user;

    @OneToOne
    private AcquiredUserData acquiredUserData;

    private String fiscalCode;
    private String description;
    private boolean accepted;
    private boolean pending;
    private boolean waiting;
    private String thirdPartyId;
    private String userId;
    private boolean subscribed;

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public AcquiredUserData getAcquiredUserData() {
        return acquiredUserData;
    }

    public void setAcquiredUserData(AcquiredUserData acquiredUserData) {
        this.acquiredUserData = acquiredUserData;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
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
