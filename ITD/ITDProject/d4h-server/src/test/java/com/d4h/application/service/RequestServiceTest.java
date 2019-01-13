package com.d4h.application.service;

import com.d4h.application.dao.DaoTestBase;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.services.RequestService;
import com.d4h.application.model.user.HealthParameters;
import com.d4h.application.model.user.HealthParametersSent;
import com.d4h.application.model.user.User;
import org.junit.jupiter.api.Test;

import javax.naming.Context;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestServiceTest extends DaoTestBase {


    @Test
    public void getHealthParametersSent(){
        List<HealthParameters> healthParameters = new ArrayList<>();
        healthParameters.add(new HealthParameters());
        healthParameters.add(new HealthParameters());

        healthParameters.get(0).setTemperature(30);
        healthParameters.get(1).setTemperature(40);

        healthParameters.get(0).setHeartBeat(80);
        healthParameters.get(1).setHeartBeat(100);

        healthParameters.get(0).setMaxPressure(100);
        healthParameters.get(1).setMaxPressure(120);

        healthParameters.get(0).setMinPressure(60);
        healthParameters.get(1).setMinPressure(80);

        HealthParametersSent healthParametersSent = RequestService.getService().getHealthParametersSent(healthParameters);
        assertEquals(40,healthParametersSent.getMaxTemperature());
        assertEquals(30,healthParametersSent.getMinTemperature());
        assertEquals(80,healthParametersSent.getMinHeartBeat());
        assertEquals(90,healthParametersSent.getAvgHeartBeat());
        assertEquals(100,healthParametersSent.getMaxHeartBeat());
        assertEquals(120,healthParametersSent.getMaxMaxPressure());
        assertEquals(60,healthParametersSent.getMinMinPressure());
    }

    @Test
    public void getPastHealthParameters()  throws Exception{
        final Context context = container.getContext();
        UsersDao users = (UsersDao) context.lookup("java:global/d4h-server/UsersDao");

        List<HealthParameters> healthParametersList;
        Long week = new Long(604800000);
        Long day = new Long(86400000);
        HealthParameters healthParameters = new HealthParameters();
        HealthParameters healthParameters1 = new HealthParameters();
        healthParameters.setDate(new Date());
        Date date = new Date();
        date.setTime(date.getTime() - week);
        healthParameters1.setDate(date);

        User user = new User();
        user.addHealthParameters(healthParameters);
        user.addHealthParameters(healthParameters1);
        healthParameters.setUser(user);
        healthParameters1.setUser(user);
        users.addUser(user);
        users.addHealthParameters(healthParameters);
        users.addHealthParameters(healthParameters1);

        healthParametersList = RequestService.getService().getPastHealthParams(user.getId(),day + day,users);

        assertEquals(1,healthParametersList.size());

        healthParametersList = RequestService.getService().getPastHealthParams(user.getId(),week + day,users);

        assertEquals(2, healthParametersList.size());

        users.deleteHealthParameters(healthParameters);
        users.deleteHealthParameters(healthParameters1);
        users.deleteUser(user);
    }
}
