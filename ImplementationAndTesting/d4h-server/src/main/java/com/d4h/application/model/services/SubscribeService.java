package com.d4h.application.model.services;

import com.d4h.application.dao.GroupOfUsers.GroupOfUsersDao;
import com.d4h.application.dao.ThirdParty.ThirdPartyDao;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.dao.request.RequestUserDao;
import com.d4h.application.model.groupOfUsers.GroupUsersData;
import com.d4h.application.model.groupOfUsers.GroupUsersDataSent;
import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.thirdParty.AcquiredUserData;
import com.d4h.application.model.thirdParty.AcquiredUserDataSent;
import com.d4h.application.model.thirdParty.ThirdParty;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SubscribeService {
    private static SubscribeService service;

 //   @EJB
 //   private UsersDao users;

    private SubscribeService(){}

    public static SubscribeService getService(){
        if(service == null)
            service = new SubscribeService();
        return service;
    }

    /**
     * Checks if the third party is subscribed to some users and update his acquiredUserData in those cases.
     * @param thirdParty the third party to check.
     * @param users Dao to access the DB.
     * @throws Exception related to the access to the DB.
     */
    public void checkNewUserData(ThirdParty thirdParty, UsersDao users) throws Exception{
        for(RequestUser requestUser: thirdParty.getUserRequests()){
            if(requestUser.isSubscribed()){
                AcquiredUserData acquiredUserData = requestUser.getAcquiredUserData();
                thirdParty.removeAcquiredUserData(acquiredUserData);
                users.deleteAcquiredUserData(acquiredUserData);
                RequestUserService.getService().setAcquiredData(requestUser, users);
            }
        }

    }
    /**
     * Checks if the third party is subscribed to some groups of users and update his groupUsersData in those cases.
     * @param thirdParty the third party to check.
     * @param users Dao to access the DB.
     * @throws Exception related to the access to the DB.
     */
    public void checkNewGroupData(ThirdParty thirdParty, UsersDao users) throws Exception{
        for(RequestGroup requestGroup: thirdParty.getGroupRequests()){
            if(requestGroup.isSubscribed()){
                GroupUsersData groupUsersData = requestGroup.getGroupOfUsers().getGroupUsersData();
                thirdParty.removeGroupUsersData(groupUsersData);
                users.deleteGroupUsersData(groupUsersData);
                RequestGroupService.getService().setGroupUsersData(requestGroup, users);
            }
        }

    }

    /**
     * Used to get new data relative to the subscribed users.
     * @param thirdParty the third party.
     * @return the list of new data.
     */
    public List<AcquiredUserDataSent> getSubscribedUserData(ThirdParty thirdParty){
        List<AcquiredUserDataSent> acquiredUserDataSents = new ArrayList<>();
        for(RequestUser requestUser: thirdParty.getUserRequests()) {
            if (requestUser.isSubscribed()) {
                acquiredUserDataSents.add(RequestUserService.getService().getAcquiredData(requestUser));
            }
        }
        return acquiredUserDataSents;
    }

    public List<GroupUsersDataSent> getSubscribedGroupData(ThirdParty thirdParty){
        List<GroupUsersDataSent> groupUsersDataSents = new ArrayList<>();
        for(RequestGroup requestGroup: thirdParty.getGroupRequests()){
            if(requestGroup.isSubscribed()){
                groupUsersDataSents.add(RequestGroupService.getService().getAcquiredGroupsData(requestGroup));
            }
        }
        return groupUsersDataSents;
    }
}
