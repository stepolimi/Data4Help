package com.d4h.application.dao;

import com.d4h.application.dao.User.UserCredentialDao;
import com.d4h.application.model.user.UserCredential;
import org.junit.jupiter.api.Test;

import javax.naming.Context;

import static org.junit.jupiter.api.Assertions.*;

public class UserCredentialDaoTest extends DaoTestBase{
    @Test
    public void emptyGetUserCredentials() throws Exception {
        final Context context = container.getContext();

        UserCredentialDao users = (UserCredentialDao) context.lookup("java:global/d4h-server/UserCredentialDao");

        assertEquals(0, users.getUserCredentials().size());
    }

    @Test
    public void notEmptyGetUserCredentials() throws Exception {
        final Context context = container.getContext();
        UserCredential user0 = new UserCredential();
        UserCredential user1 = new UserCredential();

        UserCredentialDao users = (UserCredentialDao) context.lookup("java:global/d4h-server/UserCredentialDao");

        users.addUserCredential(user0);
        assertEquals(1, users.getUserCredentials().size());
        users.addUserCredential(user1);
        assertEquals(2, users.getUserCredentials().size());
        users.deleteUserCredential(user1);
        assertEquals(1, users.getUserCredentials().size());
        users.deleteUserCredential(user0);
        assertEquals(0, users.getUserCredentials().size());
    }

    @Test
    public void emptyGetUserCredential() throws Exception {
        final Context context = container.getContext();

        UserCredentialDao users = (UserCredentialDao) context.lookup("java:global/d4h-server/UserCredentialDao");

        assertNull( users.getUserCredential("1"));
    }

    @Test
    public void notEmptyGetUserCredential() throws Exception {
        final Context context = container.getContext();
        UserCredential user = new UserCredential();

        UserCredentialDao users = (UserCredentialDao) context.lookup("java:global/d4h-server/UserCredentialDao");

        users.addUserCredential(user);

        assertNotNull( users.getUserCredential(user.getId()));
        users.deleteUserCredential(user);
    }
}
