package com.d4h.application.dao;

import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.user.Wearable;
import org.junit.jupiter.api.Test;

import javax.naming.Context;

import static org.junit.jupiter.api.Assertions.*;

public class WearableDaoTest extends DaoTestBase{
    @Test
    public void emptyGetWearables() throws Exception {
        final Context context = container.getContext();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        assertEquals(0, users.getWearables().size());
    }

    @Test
    public void notEmptyGetWearables() throws Exception {
        final Context context = container.getContext();
        Wearable user0 = new Wearable();
        Wearable user1 = new Wearable();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        users.addWearable(user0);
        assertEquals(1, users.getWearables().size());
        users.addWearable(user1);
        assertEquals(2, users.getWearables().size());
        users.deleteWearable(user1);
        assertEquals(1, users.getWearables().size());
        users.deleteWearable(user0);
        assertEquals(0, users.getWearables().size());
    }

    @Test
    public void emptyGetWearable() throws Exception {
        final Context context = container.getContext();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        assertNull( users.getWearable("1"));
    }

    @Test
    public void notEmptyGetWearable() throws Exception {
        final Context context = container.getContext();
        Wearable user = new Wearable();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        users.addWearable(user);

        assertNotNull( users.getWearable(user.getId()));
        users.deleteWearable(user);
    }
}
