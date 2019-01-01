package com.d4h.application.dao;

import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.user.User;
import com.d4h.application.model.user.UserData;
import org.junit.jupiter.api.Test;
import javax.naming.Context;
import static org.junit.jupiter.api.Assertions.*;

public class UsersDaoTest extends DaoTestBase {


    @Test
    public void emptyGetUsers() throws Exception {
        final Context context = container.getContext();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        assertEquals(0, users.getUsers().size());
    }

    @Test
    public void notEmptyGetUsers() throws Exception {
        final Context context = container.getContext();
        User user0 = new User();
        User user1 = new User();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        users.addUser(user0);
        assertEquals(1, users.getUsers().size());
        users.addUser(user1);
        assertEquals(2, users.getUsers().size());
        users.deleteUser(user1);
        assertEquals(1, users.getUsers().size());
        users.deleteUser(user0);
        assertEquals(0, users.getUsers().size());
    }

    @Test
    public void emptyGetUser() throws Exception {
        final Context context = container.getContext();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        assertNull( users.getUserById("1"));
    }

    @Test
    public void notEmptyGetUser() throws Exception {
        final Context context = container.getContext();
        User user = new User();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        users.addUser(user);

        assertNotNull( users.getUserById(user.getId()));
        users.deleteUser(user);
    }

    @Test
    public void getUserByCode() throws Exception{
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

        user0.setUserData(userData0);
        user1.setUserData(userData1);

        userData0.setFiscalCode("123");
        userData1.setFiscalCode("456");

        assertEquals(user0,users.getUserByCode("123"));
        assertEquals(user1,users.getUserByCode("456"));
        assertNull(users.getUserByCode("789"));
    }
}