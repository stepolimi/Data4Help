package com.d4h.application.model.thirdParty;

import com.d4h.application.model.groupOfUsers.GroupUsersData;
import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.request.RequestUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of a third party, contains his personal data, credentials, acquired health parameters and requests done.
 */
@Entity
public class ThirdParty {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne (cascade = CascadeType.ALL)
    private ThirdPartyCredential credential;

    @OneToOne (cascade = CascadeType.ALL)
    private ThirdPartyData data;

    @OneToMany (cascade = CascadeType.ALL)
    private List<AcquiredUserData> acquiredUserData = new ArrayList<>();

    @OneToMany (cascade = CascadeType.ALL)
    private List<GroupUsersData> groupUsersData = new ArrayList<>();

    @OneToMany (cascade = CascadeType.ALL)
    private List<RequestGroup> groupRequests = new ArrayList<>();

    @OneToMany (cascade = CascadeType.ALL)
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

    public void addGroupUsersData(GroupUsersData data){
        groupUsersData.add(data);
    }

    public void addUserRequest(RequestUser requestUser) { userRequests.add(requestUser); }

    public void addGroupRequest(RequestGroup requestGroup) { groupRequests.add(requestGroup); }

    public void removeAcquiredUserData(AcquiredUserData acquiredUserData) { this.acquiredUserData.remove(acquiredUserData); }

    public void removeGroupUsersData(GroupUsersData groupUsersData) { this.groupUsersData.remove(groupUsersData); }
}
