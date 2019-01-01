package com.d4h.application.model.services;

import com.d4h.application.dao.GroupOfUsers.GroupOfUsersDao;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.dao.request.RequestUserDao;
import com.d4h.application.model.groupOfUsers.AnonymousUserData;
import com.d4h.application.model.groupOfUsers.GroupOfUsers;
import com.d4h.application.model.groupOfUsers.GroupUsersData;
import com.d4h.application.model.request.RequestAttributes;
import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.user.HealthParameters;
import com.d4h.application.model.user.User;
import com.d4h.application.model.user.UserData;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.List;

import static com.d4h.application.constants.Constants.REQUEST_TRESHOLD;

@Singleton
public class RequestGroupService {
    @EJB
    private UsersDao users;

    @EJB
    private RequestUserDao requests;

    @EJB
    private GroupOfUsersDao groups;


    private static RequestGroupService service;

    private RequestGroupService(){}

    public static RequestGroupService getService(){
        if(service == null)
            service = new RequestGroupService();
        return service;
    }

    /**
     * Used to evaluate a request for group of users' data.
     * @param request the request to be evaluated.
     * @return true if the request has been accepted, false otherwise.
     */

    public boolean evaluate(RequestGroup request){
        try {
            RequestAttributes attributes = requests.getAttributesByRequest(request);
            List<UserData> data = getUsersData(attributes);
            List<User> targetUsers = new ArrayList<>();
            if(data.size() >= REQUEST_TRESHOLD) {
                GroupOfUsers groupOfUsers = new GroupOfUsers();
                GroupUsersData groupUsersData = new GroupUsersData();
                request.setAccepted(true);
                request.setPending(false);
                for(UserData userData: data)
                    targetUsers.add(users.getUserByUserData(userData));
                groups.addGroupOfUsers(groupOfUsers);
                groups.addGroupUsersData(groupUsersData);
                request.setGroupOfUsers(groupOfUsers);
                groupOfUsers.setUsers(targetUsers);
                groupOfUsers.setRequest(request);
                groupOfUsers.setGroupUsersData(groupUsersData);
                groupUsersData.setGroup(groupOfUsers);
                for(User user: targetUsers){
                    AnonymousUserData anonymousUserData = new AnonymousUserData();
                    groups.addAnonymousUserData(anonymousUserData);
                    anonymousUserData.addHealthParameters(users.getUserHealthParam(user.getId()));
                    groupUsersData.addUserData(anonymousUserData);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAccepted(false);
        return false;
    }

    /**
     * Used to get the users concerning a request.
     * @param attributes attributes that the users need to concerns.
     * @return the list of users data.
     * @throws Exception about requests of the DB.
     */

    private List<UserData> getUsersData(RequestAttributes attributes) throws Exception{
        List<UserData> targetData = new ArrayList<>();
        List<UserData> data = users.getUserDatas();
        for(UserData userData: data)
            if(userData.getHeight() >= attributes.getMinHeight() && userData.getHeight() <= attributes.getMaxHeight())
                if(userData.getWeight() >= attributes.getMinWeight() && userData.getWeight() <= attributes.getMaxWeight())
                    if(userData.getSex().equals(attributes.getSex()))
                 //           if(userData.getAddress())
                 //               if(userData.getDateOfBirth())
                                targetData.add(userData);
                        //todo
        return targetData;
    }
}
