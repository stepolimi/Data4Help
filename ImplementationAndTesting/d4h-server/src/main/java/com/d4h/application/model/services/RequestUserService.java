package com.d4h.application.model.services;

import com.d4h.application.dao.ThirdParty.ThirdPartyDao;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.dao.request.RequestUserDao;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.thirdParty.AcquiredUserData;
import com.d4h.application.model.user.HealthParameters;
import com.d4h.application.model.user.User;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.List;


@Singleton
public class RequestUserService {
    @EJB
    private UsersDao users;

    @EJB
    private RequestUserDao requests;

    @EJB
    private ThirdPartyDao thirdParties;

    private static RequestUserService service;

    private RequestUserService(){}

    public static RequestUserService getService(){
        if(service == null)
            service = new RequestUserService();
        return service;
    }

    public void setUser(RequestUser request){
        try {
            User user = users.getUserByCode(request.getFiscalCode());
            request.setUser(user);
            user.addRequest(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public AcquiredUserData getHealthParams(RequestUser requestUser){
        User user = requestUser.getUser();
        AcquiredUserData acquiredUserData = new AcquiredUserData();
        try {
            thirdParties.addAcquiredUserData(acquiredUserData);
            requestUser.getSender().addAcquiredUserData(acquiredUserData);
            requestUser.setAcquiredUserData(acquiredUserData);
            acquiredUserData.setUserData(user.getUserData());
            acquiredUserData.addHealthParameters(user.getHealthParameters());
            return acquiredUserData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
