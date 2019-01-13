package com.d4h.application.model.groupOfUsers;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the group of users data.
 */
@Entity
public class GroupUsersData {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne (cascade = CascadeType.ALL)
    private GroupOfUsers group;

    @OneToMany (cascade = CascadeType.ALL)
    private List<AnonymousUserData> userData = new ArrayList<>();

    public void setGroup(GroupOfUsers group) {
        this.group = group;
    }

    public GroupOfUsers getGroup() {
        return group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<AnonymousUserData> getUserData() {
        return userData;
    }

    public void addUserData(AnonymousUserData userData) {
        this.userData.add(userData);
    }
}
