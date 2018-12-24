package com.d4h.application.dao;

import com.d4h.application.dao.User.UserDataDao;
import com.d4h.application.model.user.UserData;
import org.junit.jupiter.api.Test;

import javax.naming.Context;

import static org.junit.jupiter.api.Assertions.*;

public class UserDataDaoTest extends DaoTestBase{
    @Test
    public void emptyGetUserDatas() throws Exception {
        final Context context = container.getContext();

        UserDataDao users = (UserDataDao) context.lookup("java:global/d4h-server/UserDataDao");

        assertEquals(0, users.getUserDatas().size());
    }

    @Test
    public void notEmptyGetUserDatas() throws Exception {
        final Context context = container.getContext();
        UserData user0 = new UserData();
        UserData user1 = new UserData();

        UserDataDao users = (UserDataDao) context.lookup("java:global/d4h-server/UserDataDao");

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

        UserDataDao users = (UserDataDao) context.lookup("java:global/d4h-server/UserDataDao");

        assertNull( users.getUserData("1"));
    }

    @Test
    public void notEmptyGetUserData() throws Exception {
        final Context context = container.getContext();
        UserData user = new UserData();

        UserDataDao users = (UserDataDao) context.lookup("java:global/d4h-server/UserDataDao");

        users.addUserData(user);

        assertNotNull( users.getUserData(user.getId()));
        users.deleteUserData(user);
    }
}
