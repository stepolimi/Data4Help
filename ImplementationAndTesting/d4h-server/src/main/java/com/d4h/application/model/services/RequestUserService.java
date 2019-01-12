package com.d4h.application.model.services;

import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.request.RequestUserPending;
import com.d4h.application.model.request.RequestUserSent;
import com.d4h.application.model.thirdParty.AcquiredUserData;
import com.d4h.application.model.thirdParty.AcquiredUserDataSent;
import com.d4h.application.model.thirdParty.ThirdParty;
import com.d4h.application.model.user.HealthParameters;
import com.d4h.application.model.user.HealthParametersSent;
import com.d4h.application.model.user.User;
import com.d4h.application.model.user.UserData;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Singleton
public class RequestUserService {
   // @EJB
   // private UsersDao users;

    private static RequestUserService service;

    private RequestUserService(){}

    public static RequestUserService getService(){
        if(service == null)
            service = new RequestUserService();
        return service;
    }

    /**
     * Sets the user to the request.
     * @param request the request.
     */
    public boolean setRequest(RequestUser request, UsersDao users) throws Exception {
        User user = users.getUserByCode(request.getFiscalCode());
        if(user != null) {
            request.setUser(user);
            request.setPending(true);
            request.setWaiting(true);
            user.addRequest(request);
            return true;
        }
        return false;
    }

    /**
     * Sets the data concerning a request.
     * @param requestUser the request.
     * @param users usersDao to access the DB.
     * @throws Exception exception related to the DB.
     */
    public void setAcquiredData(RequestUser requestUser, UsersDao users) throws Exception {
        User user = requestUser.getUser();
        AcquiredUserData acquiredUserData = new AcquiredUserData();
        Long week = new Long(604800000);

        requestUser.getSender().addAcquiredUserData(acquiredUserData);
        requestUser.setAcquiredUserData(acquiredUserData);
        acquiredUserData.setUserData(user.getUserData());
        acquiredUserData.addHealthParameters(RequestService.getService().getPastHealthParams(user.getId(),week,users));
        users.addAcquiredUserData(acquiredUserData);
    }

    /**
     * Used to get the health parameters of the past week and the personal data of a user.
     * @param requestUser the request that contains the target user.
     * @return the data and health parameters requested.
     */
    public AcquiredUserDataSent getAcquiredData(RequestUser requestUser){
        AcquiredUserDataSent acquiredUserDataSent = new AcquiredUserDataSent();
        Long week = new Long(604800000);
        Long day = new Long(86400000);
        UserData userData = requestUser.getUser().getUserData();
        acquiredUserDataSent.setFiscalCode(userData.getFiscalCode());
        acquiredUserDataSent.setHeight(userData.getHeight());
        acquiredUserDataSent.setWeight(userData.getWeight());
        acquiredUserDataSent.setName(userData.getName());
        acquiredUserDataSent.setSurname(userData.getSurname());
        acquiredUserDataSent.setSex(userData.getSex());
        acquiredUserDataSent.setYearOfBirth(userData.getYearOfBirth());

        for(Long days = day; days <= week; days = days + day) {
            List<HealthParameters> targetHealthParameters = new ArrayList<>();
            Date date = new Date();
            Date date1 = new Date();
            date.setTime(date.getTime() - days);
            date1.setTime(date1.getTime() - days + day);

            for (HealthParameters healthParameter : requestUser.getAcquiredUserData().getHealthParams())
                if (healthParameter.getDate().after(date) && healthParameter.getDate().before(date1))
                    targetHealthParameters.add(healthParameter);

            acquiredUserDataSent.addHealthParametersSents(RequestService.getService().getHealthParametersSent(targetHealthParameters));
        }
        return acquiredUserDataSent;
    }

    /**
     * Used to check if there are requests to the user that are waiting to be accepted or refused.
     * @param user the user.
     * @return a list with the waiting requests.
     */
    public List<RequestUserSent> searchNewRequests(User user){
        List<RequestUserSent> waitingRequests = new ArrayList<>();
        for(RequestUser requestUser: user.getRequests())
            if(requestUser.isWaiting()) {
                RequestUserSent requestUserSent = new RequestUserSent();
                requestUserSent.setDescription(requestUser.getDescription());
                requestUserSent.setSenderName(requestUser.getSender().getData().getName());
                requestUserSent.setRequestId(requestUser.getId());
                waitingRequests.add(requestUserSent);
            }
        return waitingRequests;
    }

    /**
     * Used to check if there are requests accepted by users from which was not already taken data.
     * @param thirdParty the third party.
     * @return the list of the pending requests.
     */

    public List<RequestUserPending> searchNewThirdPartyRequests(ThirdParty thirdParty){
        List<RequestUserPending> waitingRequests = new ArrayList<>();
        for(RequestUser requestUser: thirdParty.getUserRequests())
            if(requestUser.isPending()) {
                RequestUserPending requestUserPending = new RequestUserPending();
                requestUserPending.setFiscalCode(requestUser.getFiscalCode());
                requestUserPending.setRequestId(requestUser.getId());
                waitingRequests.add(requestUserPending);
            }
        return waitingRequests;
    }


}
