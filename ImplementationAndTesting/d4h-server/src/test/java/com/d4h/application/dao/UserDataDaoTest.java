package com.d4h.application.dao;

import com.d4h.application.dao.User.UserDataDao;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.user.User;
import com.d4h.application.model.user.UserData;
import org.junit.jupiter.api.Test;

import javax.naming.Context;

import static org.junit.jupiter.api.Assertions.*;

public class UserDataDaoTest extends DaoTestBase{
    @Test
    public void emptyGetUserDatas() throws Exception {
        final Context context = container.getContext();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        assertEquals(0, users.getUserDatas().size());
    }

    @Test
    public void notEmptyGetUserDatas() throws Exception {
        final Context context = container.getContext();
        UserData user0 = new UserData();
        UserData user1 = new UserData();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        users.addUserData(user0);
        assertEquals(1, users.getUserDatas().size());
        users.addUserData(user1);
        assertEquals(2, users.getUserDatas().size());
        users.deleteUserData(user1);
        assertEquals(1, users.getUserDatas().size());
        users.deleteUserData(user0);
        assertEquals(0, users.getUserDatas().size());
    }

    @Test
    public void emptyGetUserData() throws Exception {
        final Context context = container.getContext();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        assertNull( users.getUserData("1"));
    }

    @Test
    public void notEmptyGetUserData() throws Exception {
        final Context context = container.getContext();
        UserData user = new UserData();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");
        users.addUserData(user);

        assertNotNull( users.getUserData(user.getId()));
        users.deleteUserData(user);
    }

    @Test
    public void noDuplicateFiscalCode() throws Exception{
        final Context context = container.getContext();
        User user0 = new User();
        User user1 = new User();
        UserData userData0 = new UserData();
        UserData userData1 = new UserData();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        users.addUser(user0);
        users.addUser(user1);

        users.addUserData(userData0);
        users.addUserData(userData1);


        userData0.setFiscalCode("1");
        userData1.setFiscalCode("2");

        userData0.setUser(user0);
        userData1.setUser(user1);

        user0.setUserData(userData0);
        user1.setUserData(userData1);


        System.out.println(userData0.getUser().getId());


        //usersData = (UserDataDao) context.lookup("java:global/d4h-server/UserDataDao");

        assertEquals("1",users.getUserData(userData0.getId()).getFiscalCode());
        assertEquals("2",users.getUserData(userData1.getId()).getFiscalCode());

        assertEquals(2,users.getUsers().size());
        for(User u: users.getUsers())
            System.out.println(u.getUserData());
        assertEquals(user0,users.getUserByCode("1"));

        users.deleteUser(user0);
        users.deleteUser(user1);
        users.deleteUserData(userData0);
        users.deleteUserData(userData1);
    }
}
