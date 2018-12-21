package com.d4h.application.dao;

import org.junit.jupiter.api.Test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import static org.junit.jupiter.api.Assertions.*;

public class UsersDaoTest extends DaoTestBase {

    static EJBContainer container;

    @Test
    public void emptyGetUsers() throws Exception {
        final Context context = container.getContext();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        assertEquals(0, users.getUsers().size());
    }
}