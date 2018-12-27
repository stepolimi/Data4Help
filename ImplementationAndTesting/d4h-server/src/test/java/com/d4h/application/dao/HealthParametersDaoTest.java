package com.d4h.application.dao;


import com.d4h.application.dao.User.HealthParametersDao;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.user.HealthParameters;
import org.junit.jupiter.api.Test;

import javax.naming.Context;

import static org.junit.jupiter.api.Assertions.*;

public class HealthParametersDaoTest extends DaoTestBase{
    @Test
    public void emptyGetHealthParameterss() throws Exception {
        final Context context = container.getContext();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        assertEquals(0, users.getHealthParameters().size());
    }

    @Test
    public void notEmptyGetHealthParameterss() throws Exception {
        final Context context = container.getContext();
        HealthParameters healthParameters0 = new HealthParameters();
        HealthParameters healthParameters1 = new HealthParameters();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        users.addHealthParameters(healthParameters0);
        assertEquals(1, users.getHealthParameters().size());
        users.addHealthParameters(healthParameters1);
        assertEquals(2, users.getHealthParameters().size());
        users.deleteHealthParameters(healthParameters1);
        assertEquals(1, users.getHealthParameters().size());
        users.deleteHealthParameters(healthParameters0);
        assertEquals(0, users.getHealthParameters().size());
    }

    @Test
    public void emptyGetHealthParameters() throws Exception {
        final Context context = container.getContext();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        assertNull( users.getHealthParameters("1"));
    }

    @Test
    public void notEmptyGetHealthParameters() throws Exception {
        final Context context = container.getContext();
        HealthParameters healthParameters = new HealthParameters();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        users.addHealthParameters(healthParameters);

        assertNotNull( users.getHealthParameters(healthParameters.getId()));
        users.deleteHealthParameters(healthParameters);
    }
}
