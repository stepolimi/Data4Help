package com.d4h.application.rest.impl;

import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.services.RequestService;
import com.d4h.application.model.services.RequestUserService;
import com.d4h.application.model.user.*;
import com.d4h.application.rest.UserService;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

public class UserServiceImpl implements UserService{
    @EJB
    UsersDao users;

    private Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    /**
     * Used to login an existing user.
     * @param credential credentials of the existing user.
     * @return the id of the user if the request has been successful.
     */
    @Override
    public Response login(UserCredential credential) {
        if(credential!= null) {
            String email = credential.getEmail();
            try {
                if (authenticate(email)) {
                    if(authenticateUser(credential)) {
                        User user = users.getUserByMail(email);
                        return ok(user.getId(),"application/json").build();
                    }
                }
                return status(Response.Status.BAD_REQUEST).build();
            }catch (Exception e){
                return status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Used to register a new user.
     * @param credential credentials of the user to be added.
     * @return the id of the new user if the request has been successful.
     */
    @Override
    public Response registration(UserCredential credential) {
        if(credential != null) {
            String email = credential.getEmail();
            try {
                if (!authenticate(email)) {
                  //  String id = addUser(credential);
                    User user = new User();
                    user.setCredential(credential);
                    credential.setUser(user);
                    users.addUser(user);
                    users.addUserCredential(credential);

                    return ok(user.getId(),"application/json").build();
                } else
                    return status(Response.Status.BAD_REQUEST).build();

            } catch (Exception e) {
                return status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * @param userData is the json containing some user information
     * @return ok if the request has been successful
     *
     * Add user personal data to the user created during the registration.
     */
    @Override
    public Response insertPersonalData( UserData userData) {
        try {
            User user = users.getUserById(userData.getUserId());
            if (user != null) {
                userData.setUser(user);
                userData.getAddress().setUser(user);
                user.setUserData(userData);

                users.addUserData(userData);
                users.addAddress(userData.getAddress());
                return ok().build();
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Used to add height and weight of users.
     * @param userData to be updated.
     * @return ok if the request has been successful.
     */

    @Override
    public Response insertWeightHeight(UserData userData) {
        try {
            User user = users.getUserById(userData.getUserId());
            if (user != null) {
                UserData data = users.getUserDataByUser(user);
                data.setWeight(userData.getWeight());
                data.setHeight(userData.getHeight());
                users.updateDB();
                return ok().build();
            }

        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Used to add new health parameters data relative to a user,
     * @param healthParameters health parameters to be added.
     * @return ok if the request has been successful.
     */
    @Override
    public Response addHealthParam(HealthParameters healthParameters) {
        try {
            User user = users.getUserById(healthParameters.getUserId());
            if(user != null){
                healthParameters.setUser(user);
                healthParameters.setDate(new Date());
                user.addHealthParameters(healthParameters);
                users.addHealthParameters(healthParameters);
                return ok().build();
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Used to get user's health params of the past day.
     * @param userId id of the user.
     * @return the health params of the past day if the request has been successful.
     */
    @Override
    public Response getDailyHealthParam(String userId) {
        try {
            String id = getUserId(userId);
            User user = users.getUserById(id);
            if(user != null){
                Long day = new Long(86400000);
                List<HealthParameters> healthParameters = RequestService.getService().getPastHealthParams(id, day, users);
                HealthParametersSent healthParametersSent = RequestService.getService().getHealthParametersSent(healthParameters);
                healthParametersSent.setHeight(user.getUserData().getHeight());
                healthParametersSent.setWeight(user.getUserData().getWeight());
                return ok(healthParametersSent, "application/json").build();
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Used to get user's health params of the past week.
     * @param userId id of the user.
     * @return the health params of the past week if the request has been successful.
     */
    @Override
    public Response getWeeklyHealthParam(String userId) {
        try {
            String id = getUserId(userId);
            User user = users.getUserById(id);
            if(user != null){
                Long week = new Long(604800000);
                List<HealthParameters> healthParameters = RequestService.getService().getPastHealthParams(id, week, users);
                return ok(RequestService.getService().getHealthParametersSent(healthParameters), "application/json").build();
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Used to get user's health params of the past month.
     * @param userId id of the user.
     * @return the health params of the past month if the request has been successful.
     */
    @Override
    public Response getMonthlyHealthParam(String userId) {
        try {
            String id = getUserId(userId);
            User user = users.getUserById(id);
            if(user != null){
                Long month = new Long(262974600);
                month = month*10;
                List<HealthParameters> healthParameters = RequestService.getService().getPastHealthParams(id, month, users);
                return ok(RequestService.getService().getHealthParametersSent(healthParameters), "application/json").build();
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Used to get user's health params of the past year.
     * @param userId id of the user.
     * @return the health params of the past month if the request has been successful.
     */
    @Override
    public Response getYearHealthParam(String userId) {
        try {
            String id = getUserId(userId);
            User user = users.getUserById(id);
            if(user != null){
                Long year = new Long(262974600);
                year = year*10*12;
                List<HealthParameters> healthParameters = RequestService.getService().getPastHealthParams(id, year, users);
                return ok(RequestService.getService().getHealthParametersSent(healthParameters), "application/json").build();
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Used to get existing pending requests.
     * @param userId id of the user.
     * @return the pending requests.
     */
    @Override
    public Response getRequests(String userId) {
        try {
            String id = getUserId(userId);
            User user = users.getUserById(id);
            if(user != null)
                return ok(RequestUserService.getService().searchNewRequests(user), "application/json").build();

        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Used to respond to a data request.
     * @param requestUser id of the request.
     * @return ok if the request has been successful.
     */
    @Override
    public Response respondRequest(RequestUser requestUser) {
        try {
            User user = users.getUserById(requestUser.getUserId());
            RequestUser request = users.getRequestUser(requestUser.getId());
            if(user != null){
                if(request != null) {
                    request.setWaiting(false);
                    request.setPending(true);
                    request.setAccepted(requestUser.isAccepted());
                    users.updateDB();
                    return ok().build();
                }
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Response.Status.BAD_REQUEST).build();
    }

    //--------------------------------Other Methods---------------------------------------------------------------------

    /**
     * @param email is the email given by the client during the registration
     * @return true if the email is already in the DB, it means that the email has already been registered;
     * false isn't in the DB.
     * @throws Exception exception related to the DB.
     */
    private boolean authenticate(String email) throws Exception {
        List<UserCredential> usersCred = users.getUserCredentials();
        if (usersCred != null) {
            for (UserCredential userCred : usersCred) {
                if (userCred.getEmail().equals(email))
                    return true;
            }
        }

        return false;
    }

    /**
     * Used to check if the password associated with the mail of the user corresponds with the one given.
     * @param credential the credentials that contains email and password.
     * @return true if the password corresponds, false otherwise.
     * @throws Exception exception related to the DB.
     */
    private boolean authenticateUser(UserCredential credential) throws Exception{
        UserCredential userCredentialTarget = users.getUserCredentialByMail(credential.getEmail());
        if(userCredentialTarget!= null && userCredentialTarget.getEmail().equals(credential.getEmail()))
            return true;
        return false;
    }


    /**
     * Used to parse the Id of the user.
     * @param userId id to be parsed.
     * @return the id parsed.
     */

    private String getUserId(String userId){
        JsonObject jsonObject = Json.createReader(new StringReader(userId)).readObject();
        String id = jsonObject.get("id").toString();
        id = id.substring(1,id.length() - 1);
        return id;
    }

}
