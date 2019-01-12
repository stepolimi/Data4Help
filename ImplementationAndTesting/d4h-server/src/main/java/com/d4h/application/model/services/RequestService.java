package com.d4h.application.model.services;

import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.user.HealthParameters;
import com.d4h.application.model.user.HealthParametersSent;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Singleton
public class RequestService {
    private static RequestService service;

    private RequestService(){}

    public static RequestService getService(){
        if(service == null)
            service = new RequestService();
        return service;
    }

    /**
     * Used to build an HealthParametersSent object.
     * @param targetHealthParam the health parameters that will be used to build the object.
     * @return the object built.
     */
    public HealthParametersSent getHealthParametersSent(List<HealthParameters> targetHealthParam){
        HealthParametersSent healthParametersSent = new HealthParametersSent();
        int tot = 0;

        if(targetHealthParam.get(0) != null) {
            healthParametersSent.setMaxHeartBeat(targetHealthParam.get(0).getHeartBeat());
            healthParametersSent.setMinHeartBeat(targetHealthParam.get(0).getHeartBeat());
            healthParametersSent.setMaxMaxPressure(targetHealthParam.get(0).getMaxPressure());
            healthParametersSent.setMinMinPressure(targetHealthParam.get(0).getMinPressure());
            healthParametersSent.setMaxTemperature(targetHealthParam.get(0).getTemperature());
            healthParametersSent.setMinTemperature(targetHealthParam.get(0).getTemperature());

            for (HealthParameters healthParameters1 : targetHealthParam) {
                tot = tot + healthParameters1.getHeartBeat();
                if (healthParameters1.getHeartBeat() > healthParametersSent.getMaxHeartBeat())
                    healthParametersSent.setMaxHeartBeat(healthParameters1.getHeartBeat());

                if (healthParameters1.getHeartBeat() < healthParametersSent.getMinHeartBeat())
                    healthParametersSent.setMinHeartBeat(healthParameters1.getHeartBeat());

                if (healthParameters1.getMaxPressure() > healthParametersSent.getMaxMaxPressure())
                    healthParametersSent.setMaxMaxPressure(healthParameters1.getMaxPressure());

                if (healthParameters1.getMinPressure() < healthParametersSent.getMinMinPressure())
                    healthParametersSent.setMinMinPressure(healthParameters1.getMinPressure());

                if (healthParameters1.getTemperature() > healthParametersSent.getMaxTemperature())
                    healthParametersSent.setMaxTemperature(healthParameters1.getTemperature());

                if (healthParameters1.getTemperature() < healthParametersSent.getMinTemperature())
                    healthParametersSent.setMinTemperature(healthParameters1.getTemperature());
            }
            healthParametersSent.setAvgHeartBeat(tot/targetHealthParam.size());
        }

        return healthParametersSent;
    }


    /**
     * Used to get health params of a user of the last period specified.
     * @param id id of the user.
     * @param period period of time.
     * @return a list of the health params of the user gathered in the specified period.
     * @throws Exception relative to the DB.
     */
    public List<HealthParameters> getPastHealthParams(String id, Long period, UsersDao users) throws Exception{
        List<HealthParameters> healthParameters = users.getUserHealthParam(id);
        List<HealthParameters> targetHealthParam = new ArrayList<>();

        System.out.println(healthParameters.size());
        Date date = new Date();
        date.setTime(date.getTime() - period);
        for(HealthParameters healthParam: healthParameters)
            if(healthParam.getDate().after(date))
                targetHealthParam.add(healthParam);
        return targetHealthParam;
    }
}
