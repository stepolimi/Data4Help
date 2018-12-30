package com.d4h.application.model.thirdParty;

import com.d4h.application.model.groupOfUsers.GroupUsersData;
import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.request.RequestUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ThirdParty {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private ThirdPartyCredential credential;

    @OneToOne
    private ThirdPartyData data;

    //todo: mettere acquiredUserData e groupUserData all'invio dei dati.
    @OneToMany
    private List<AcquiredUserData> acquiredUserData = new ArrayList<>();

    @OneToMany
    private List<GroupUsersData> groupUsersData = new ArrayList<>();

    @OneToMany
    private List<RequestGroup> groupRequests = new ArrayList<>();

    @OneToMany
    private List<RequestUser> userRequests = new ArrayList<>();

    public void setAcquiredUserData(List<AcquiredUserData> acquiredUserData) {
        this.acquiredUserData = acquiredUserData;
    }

    public void addAcquiredUserData(AcquiredUserData acquiredUserData) { this.acquiredUserData.add(acquiredUserData); }

    public void setGroupUsersData(List<GroupUsersData> groupUsersData) {
        this.groupUsersData = groupUsersData;
    }

    public List<RequestGroup> getGroupRequests() {
        return groupRequests;
    }

    public void setGroupRequests(List<RequestGroup> groupRequests) {
        this.groupRequests = groupRequests;
    }

    public List<RequestUser> getUserRequests() {
        return userRequests;
    }

    public void setUserRequests(List<RequestUser> userRequests) {
        this.userRequests = userRequests;
    }

    public List<GroupUsersData> getGroupUsersData() {
        return groupUsersData;
    }

    public List<AcquiredUserData> getAcquiredUserData() {
        return acquiredUserData;
    }

    public ThirdPartyData getData() {
        return data;
    }

    public void setData(ThirdPartyData data) {
        this.data = data;
    }

    public ThirdPartyCredential getCredential() {
        return credential;
    }

    public void setCredential(ThirdPartyCredential credential) {
        this.credential = credential;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addUserData(AcquiredUserData data){
        acquiredUserData.add(data);
    }

    public void addGroupUsers(GroupUsersData data){
        groupUsersData.add(data);
    }

    public void addUserRequest(RequestUser requestUser) { userRequests.add(requestUser); }

    public void addGroupRequest(RequestGroup requestGroup) { groupRequests.add(requestGroup); }
}
