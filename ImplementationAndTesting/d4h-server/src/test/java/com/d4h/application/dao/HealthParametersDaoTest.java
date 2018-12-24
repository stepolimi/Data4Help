package com.d4h.application.dao;


import com.d4h.application.dao.User.HealthParametersDao;
import com.d4h.application.model.user.HealthParameters;
import org.junit.jupiter.api.Test;

import javax.naming.Context;

import static org.junit.jupiter.api.Assertions.*;

public class HealthParametersDaoTest extends DaoTestBase{
    @Test
    public void emptyGetHealthParameterss() throws Exception {
        final Context context = container.getContext();

        HealthParametersDao healthParameterss = (HealthParametersDao) context.lookup("java:global/d4h-server/HealthParametersDao");

        assertEquals(0, healthParameterss.getHealthParameters().size());
    }

    @Test
    public void notEmptyGetHealthParameterss() throws Exception {
        final Context context = container.getContext();
        HealthParameters healthParameters0 = new HealthParameters();
        HealthParameters healthParameters1 = new HealthParameters();

        HealthParametersDao healthParameterss = (HealthParametersDao) context.lookup("java:global/d4h-server/HealthParametersDao");

        healthParameterss.addHealthParameters(healthParameters0);
        assertEquals(1, healthParameterss.getHealthParameters().size());
        healthParameterss.addHealthParameters(healthParameters1);
        assertEquals(2, healthParameterss.getHealthParameters().size());
        healthParameterss.deleteHealthParameters(healthParameters1);
        assertEquals(1, healthParameterss.getHealthParameters().size());
        healthParameterss.deleteHealthParameters(healthParameters0);
        assertEquals(0, healthParameterss.getHealthParameters().size());
    }

    @Test
    public void emptyGetHealthParameters() throws Exception {
        final Context context = container.getContext();

        HealthParametersDao healthParameterss = (HealthParametersDao) context.lookup("java:global/d4h-server/HealthParametersDao");

        assertNull( healthParameterss.getHealthParameters("1"));
    }

    @Test
    public void notEmptyGetHealthParameters() throws Exception {
        final Context context = container.getContext();
        HealthParameters healthParameters = new HealthParameters();

        HealthParametersDao healthParameterss = (HealthParametersDao) context.lookup("java:global/d4h-server/HealthParametersDao");

        healthParameterss.addHealthParameters(healthParameters);

        assertNotNull( healthParameterss.getHealthParameters(healthParameters.getId()));
        healthParameterss.deleteHealthParameters(healthParameters);
    }
}
