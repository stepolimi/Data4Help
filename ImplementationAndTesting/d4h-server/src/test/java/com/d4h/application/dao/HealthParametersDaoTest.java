package com.d4h.application.dao;


import com.d4h.application.dao.User.HealthParametersDao;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.user.HealthParameters;
import com.d4h.application.model.user.User;
import org.junit.jupiter.api.Test;

import javax.naming.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        assertNull( users.getHealthParam("1"));
    }

    @Test
    public void notEmptyGetHealthParameters() throws Exception {
        final Context context = container.getContext();
        HealthParameters healthParameters = new HealthParameters();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        users.addHealthParameters(healthParameters);

        assertNotNull( users.getHealthParam(healthParameters.getId()));
        users.deleteHealthParameters(healthParameters);
    }

    @Test
    public void getHealthParamsOfMonth() throws Exception{
        final Context context = container.getContext();
        HealthParameters healthParams0 = new HealthParameters();
        HealthParameters healthParams1 = new HealthParameters();

        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");


        List<HealthParameters> targetHealthParam = new ArrayList<>();
        Long month = new Long(262974600);
        month = month*10;

        healthParams0.setDate(new Date());
        users.addHealthParameters(healthParams0);

        Date date1 = new Date();
        date1.setTime(new Date().getTime() - month*2);
        healthParams1.setDate(date1);
        users.addHealthParameters(healthParams1);


        Date date = new Date();
        date.setTime(date.getTime() - month);
        System.out.println("target: " + date.getTime());

        List<HealthParameters> healthParameters = users.getHealthParameters();
        for(HealthParameters healthParam: healthParameters){
            System.out.println("date: " + healthParam.getDate().getTime());
            if(healthParam.getDate().after(date))
                targetHealthParam.add(healthParam);
        }
        assertEquals(1,targetHealthParam.size());

        targetHealthParam.clear();
        date.setTime(date.getTime() - month*2);
        for(HealthParameters healthParam: healthParameters){
            if(healthParam.getDate().after(date))
                targetHealthParam.add(healthParam);
        }

        assertEquals(2, targetHealthParam.size());
        users.deleteHealthParameters(healthParams0);
        users.deleteHealthParameters(healthParams1);
    }
}
