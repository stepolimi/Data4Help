package com.d4h.application.model.services;

import com.d4h.application.dao.GroupOfUsers.GroupOfUsersDao;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.dao.request.RequestUserDao;
import com.d4h.application.model.groupOfUsers.AnonymousUserData;
import com.d4h.application.model.groupOfUsers.GroupOfUsers;
import com.d4h.application.model.groupOfUsers.GroupUsersData;
import com.d4h.application.model.groupOfUsers.GroupUsersDataSent;
import com.d4h.application.model.request.RequestAttributes;
import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.request.RequestUserSent;
import com.d4h.application.model.thirdParty.ThirdParty;
import com.d4h.application.model.user.HealthParameters;
import com.d4h.application.model.user.User;
import com.d4h.application.model.user.UserData;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.d4h.application.constants.Constants.REQUEST_THRESHOLD;

@Singleton
public class RequestGroupService {

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

    public boolean evaluate(RequestGroup request, UsersDao users) throws Exception {

        RequestAttributes attributes = request.getAttributes();
        List<UserData> data = getUsers(attributes, users);
        List<User> targetUsers = new ArrayList<>();
        if (data.size() == REQUEST_THRESHOLD) {
            GroupOfUsers groupOfUsers = new GroupOfUsers();
            request.setAccepted(true);
            request.setPending(true);
            for (UserData userData : data)
                targetUsers.add(users.getUserByUserData(userData));
            request.setGroupOfUsers(groupOfUsers);
            groupOfUsers.setUsers(targetUsers);
            groupOfUsers.setRequest(request);
            users.addGroupOfUsers(groupOfUsers);
            return true;
        }
        request.setAccepted(false);
        return false;
    }

    /**
     * Sets to the request the data of users.
     * @param request the request.
     * @throws Exception related to the access to DB.
     */
    public void setGroupUsersData(RequestGroup request, UsersDao users) throws Exception{
        GroupUsersData groupUsersData = new GroupUsersData();
        Long week = new Long(604800000);
        request.getGroupOfUsers().setGroupUsersData(groupUsersData);
        groupUsersData.setGroup(request.getGroupOfUsers());
        for(User user: request.getGroupOfUsers().getUsers()){
            AnonymousUserData anonymousUserData = new AnonymousUserData();
            anonymousUserData.addHealthParameters(RequestService.getService().getPastHealthParams(user.getId(),week,users));
            groupUsersData.addUserData(anonymousUserData);
            users.addAnonymousUserData(anonymousUserData);
        }
        request.getSender().addGroupUsersData(groupUsersData);
        users.addGroupUsersData(groupUsersData);
    }

    /**
     * Used to get the users concerning a request.
     * @param attributes attributes that the users need to concerns.
     * @return the list of users data.
     * @throws Exception about requests of the DB.
     */

    private List<UserData> getUsers(RequestAttributes attributes, UsersDao users) throws Exception{
        List<UserData> targetData = new ArrayList<>();
        List<UserData> data = users.getUserDatas();
        for(UserData userData: data)
            if((attributes.getMinHeight() == attributes.getMaxHeight() && attributes.getMaxHeight() == 0) || (userData.getHeight() >= attributes.getMinHeight() && userData.getHeight() <= attributes.getMaxHeight()))
                if((attributes.getMinWeight() == attributes.getMaxWeight() && attributes.getMaxWeight() == 0) || (userData.getWeight() >= attributes.getMinWeight() && userData.getWeight() <= attributes.getMaxWeight()))
                    if(attributes.getSex() == null || userData.getSex().equals(attributes.getSex())) {
                        Date todayDate = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy");
                        int year = Integer.parseInt(format.format(todayDate));
                        if ((attributes.getMinAge() == attributes.getMaxAge() && attributes.getMaxAge() == 0) || (year - userData.getYearOfBirth() <= attributes.getMaxAge() && year - userData.getYearOfBirth() >= attributes.getMinAge()))
                            if((attributes.getAddressRange().getState().equals("")) || (userData.getAddress().getState().equals(attributes.getAddressRange().getState())))
                                if((attributes.getAddressRange().getRegion().equals("")) || (userData.getAddress().getRegion().equals(attributes.getAddressRange().getRegion())))
                                targetData.add(userData);
                    }
        return targetData;
    }

    /**
     * Used to get health parameters of the past week and the attributes of a request.
     * @param requestGroup request that contains health parameters and attributes.
     * @return the data and attributes requested.
     */
    public GroupUsersDataSent getAcquiredGroupsData(RequestGroup requestGroup){
        GroupUsersDataSent groupUsersDataSent = new GroupUsersDataSent();
        Long week = new Long(604800000);
        Long day = new Long(86400000);
        groupUsersDataSent.setRequestAttributes(requestGroup.getAttributes());

        for(Long days = day; days <= week; days = days + day) {
            List<HealthParameters> targetHealthParameters = new ArrayList<>();
            Date date = new Date();
            Date date1 = new Date();
            date.setTime(date.getTime() - days);
            date1.setTime(date1.getTime() - days + day);

            for(AnonymousUserData anonymousUserData: requestGroup.getGroupOfUsers().getGroupUsersData().getUserData()) {
                for (HealthParameters healthParameter : anonymousUserData.getHealthParams())
                    if (healthParameter.getDate().after(date) && healthParameter.getDate().before(date1))
                        targetHealthParameters.add(healthParameter);
            }
            groupUsersDataSent.addHealthParametersSents(RequestService.getService().getHealthParametersSent(targetHealthParameters));
        }
        return groupUsersDataSent;
    }


}
