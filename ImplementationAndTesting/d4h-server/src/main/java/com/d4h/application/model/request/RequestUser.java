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
    private String motivation;
    private boolean accepted;
    private boolean pending;
    private boolean waiting;

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

    @Override
    public String getMotivation() {
        return motivation;
    }

    @Override
    public void setMotivation(String motivation) {
        this.motivation = motivation;
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
