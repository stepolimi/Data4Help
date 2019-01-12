package com.d4h.application.model.groupOfUsers;

import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class GroupOfUsers {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne (cascade = CascadeType.ALL)
    private RequestGroup request;

    @OneToOne (cascade = CascadeType.ALL)
    private GroupUsersData groupUsersData;

    @ManyToMany
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public GroupUsersData getGroupUsersData() {
        return groupUsersData;
    }

    public void setGroupUsersData(GroupUsersData groupUsersData) {
        this.groupUsersData = groupUsersData;
    }

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
