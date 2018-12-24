package com.d4h.application.model.thirdParty;

import com.d4h.application.model.groupOfUsers.GroupUsersData;

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

    @OneToMany
    private List<AcquiredUserData> acquiredUserData = new ArrayList<>();

    @OneToMany
    private List<GroupUsersData> groupUsersData = new ArrayList<>();

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
}
