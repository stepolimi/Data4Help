package com.d4h.application.service;

import com.d4h.application.dao.DaoTestBase;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.groupOfUsers.GroupOfUsers;
import com.d4h.application.model.request.AddressRange;
import com.d4h.application.model.request.RequestAttributes;
import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.services.RequestGroupService;
import com.d4h.application.model.thirdParty.ThirdParty;
import com.d4h.application.model.user.Address;
import com.d4h.application.model.user.User;
import com.d4h.application.model.user.UserData;
import org.junit.jupiter.api.Test;

import javax.naming.Context;

import java.util.ArrayList;
import java.util.List;

import static com.d4h.application.constants.Constants.REQUEST_THRESHOLD;
import static org.junit.jupiter.api.Assertions.*;

public class RequestGroupServiceTest extends DaoTestBase {

    @Test
    public void evaluate() throws Exception{
        final Context context = container.getContext();
        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");
        List<User> userList = new ArrayList<>();

        for(int i = 0; i<REQUEST_THRESHOLD - 1; i ++){
            User user = new User();
            userList.add(user);
            UserData userData= new UserData();
            Address address = new Address();

            user.setUserData(userData);
            userData.setUser(user);
            userData.setAddress(address);
            users.addAddress(address);
            users.addUser(user);
            users.addUserData(userData);

        }
        RequestGroup requestGroup = new RequestGroup();
        RequestAttributes requestAttributes = new RequestAttributes();
        AddressRange addressRange = new AddressRange();
        requestGroup.setAttributes(requestAttributes);
        requestAttributes.setAddressRange(addressRange);

        assertFalse(RequestGroupService.getService().evaluate(requestGroup,users));

        User user = new User();
        userList.add(user);
        UserData userData= new UserData();
        Address address = new Address();

        user.setUserData(userData);
        userData.setUser(user);
        userData.setAddress(address);
        users.addAddress(address);
        users.addUser(user);
        users.addUserData(userData);

        assertTrue(RequestGroupService.getService().evaluate(requestGroup,users));

        requestAttributes.setMinAge(10);
        user.getUserData().setYearOfBirth(2013);

        assertFalse(RequestGroupService.getService().evaluate(requestGroup,users));

        for(User user1: userList)
            users.deleteUser(user1);

    }


    @Test
    public void setGroupUsersData() throws Exception{
        final Context context = container.getContext();
        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        RequestGroup requestGroup = new RequestGroup();
        GroupOfUsers groupOfUsers = new GroupOfUsers();
        ThirdParty thirdParty = new ThirdParty();
        List<User> usersList = new ArrayList<>();
        usersList.add(new User());
        groupOfUsers.setUsers(usersList);
        requestGroup.setGroupOfUsers(groupOfUsers);
        requestGroup.setSender(thirdParty);

        assertEquals(0,users.getGroupUsersData().size());

        RequestGroupService.getService().setGroupUsersData(requestGroup,users);

        assertEquals(1,users.getGroupUsersData().size());

        users.deleteGroupUsersData(groupOfUsers.getGroupUsersData());
    }

    @Test
    public void getAcquiredGroupData(){

    }
}
