package com.d4h.application.rest.impl;

import com.d4h.application.dao.request.RequestUserDao;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.user.*;
import com.d4h.application.dao.User.UsersDao;

import com.d4h.application.rest.UserService;
import org.json.JSONObject;

import javax.ejb.EJB;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.*;

public class UserServiceImpl implements UserService{
    @EJB
    UsersDao users;

    @EJB
    RequestUserDao requests;

    private Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    /**
     * Used to login an existing user.
     * @param credential credenls of the existing user.
     * @return the id of the user if the request has been successful.
     */
    @Override
    public Response login(UserCredential credential) {
        if(credential!= null) {
            String email = credential.getEmail();
            try {
                if (authenticate(email)) {
                    User user = users.getUserByMail(email);
                    JSONObject response = new JSONObject();
                    response.put("value",user.getId());
                    return Response.ok().type(MediaType.APPLICATION_JSON).entity(response).build();
                }
                return status(Response.Status.UNAUTHORIZED).build();
            }catch (Exception e){
                return status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return status(Response.Status.FORBIDDEN).build();
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
            //for testing
            System.out.println("mail: " + email);
            System.out.println("pass:" + credential.getPassword());
            try {
                if (!authenticate(email)) {
                    addUser(credential);

                    //for testing
                    JSONObject response = new JSONObject();
                    response.put("status", "ok");
                    response.put("value", credential.getId());
                    System.out.println(response);
                    System.out.println(Response.Status.ACCEPTED);
                    return Response.ok().type(MediaType.APPLICATION_JSON).entity(response).build();
                } else
                    return status(Response.Status.UNAUTHORIZED).build();

            } catch (Exception e) {
                return status(Response.Status.FORBIDDEN).build();
            }
        }
        return status(Response.Status.FORBIDDEN).build();
    }


    @Override
    public Response users() {
        try {
            return ok(users.getUsers(), "application/json").build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
    }

    /**
     * @param id is the id given during the registration to verify the user
     * @param userData is the json containing some user information
     * @return ok if the request has been successful
     *
     * Add user personal data to the user created during the registration.
     */
    @Override
    public Response insertPersonalData(@QueryParam("id") String id, UserData userData) {
        try {
            User user = users.getUserById(id);
            if (user != null) {
                users.addUserData(userData);
                userData.setUser(user);
                user.setUserData(userData);
                return ok().build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to add height and weight of users
     * @param id id of the user.
     * @param weight weight of the user.
     * @param height height of the user.
     *
     * @return ok if the request has been successful
     */

    @Override
    public Response insertWeightHeight(@QueryParam("id") String id, int weight, int height) {
        try {
            User user = users.getUserById(id);
            if (user != null) {
                UserData data = users.getUserDataByUser(user);
                data.setWeight(weight);
                data.setHeight(height);
                return ok().build();
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to insert the address of users.
     * @param id id of the user.
     * @param address address of the user.
     * @return ok if the request has been successful.
     */
    @Override
    public Response insertAddress(@QueryParam("id") String id, Address address) {
        try {
            User user = users.getUserById(id);
            if (user != null) {
                UserData data = users.getUserDataByUser(user);
                users.addAddress(address);
                address.setUser(user);
                data.setAddress(address);
                return ok().build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to add new health parameters data relative to a user,
     * @param id id of the user.
     * @param healthParameters health parameters to be added.
     * @return ok if the request has been successful.
     */
    @Override
    public Response addHealthParam(@QueryParam("id") String id, HealthParameters healthParameters) {
        try {
            User user = users.getUserById(id);
            if(user != null){
                users.addHealthParameters(healthParameters);
                healthParameters.setUser(user);
                healthParameters.setDate(new Date());
                user.addHealthParameters(healthParameters);
                return ok().build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to get user's health params of the past day.
     * @param id id of the user.
     * @return the health params of the past day if the request has been successful.
     */
    @Override
    public Response getDailyHealthParam(String id) {
        try {
            User user = users.getUserById(id);
            if(user != null){
                Long day = new Long(86400000);
                return ok(getPastHealthParams(id, day), "application/json").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to get user's health params of the past week.
     * @param id id of the user.
     * @return the health params of the past week if the request has been successful.
     */
    @Override
    public Response getWeeklyHealthParam(String id) {
        try {
            User user = users.getUserById(id);
            if(user != null){
                Long week = new Long(604800000);
                return ok(getPastHealthParams(id, week), "application/json").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to get user's health params of the past month.
     * @param id id of the user.
     * @return the health params of the past month if the request has been successful.
     */
    @Override
    public Response getMonthlyHealthParam(String id) {
        try {
            User user = users.getUserById(id);
            if(user != null){
                Long month = new Long(262974600);
                month = month*10;
                return ok(getPastHealthParams(id, month), "application/json").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to get existing pending requests.
     * @param id id of the user.
     * @return the pending requests.
     */
    @Override
    public Response getRequests(String id) {
        try {
            User user = users.getUserById(id);
            List<RequestUser> waitingRequests = new ArrayList<>();
            if(user != null){
                for(RequestUser requestUser: user.getRequests())
                    if(requestUser.isWaiting())
                        waitingRequests.add(requestUser);
                return ok(waitingRequests, "application/json").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to respond to a data request.
     * @param id id of the user.
     * @param requestId id of the request.
     * @param accepted true if the request has been accepted, false otherwise.
     * @return ok if the request has been successful.
     */
    @Override
    public Response respondRequest(String id, String requestId, boolean accepted) {
        try {
            User user = users.getUserById(id);
            RequestUser request = requests.getRequestUser(requestId);
            if(user != null){
                if(request != null) {
                    request.setWaiting(false);
                    request.setAccepted(accepted);
                    return ok().build();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    //--------------------------------Other Methods---------------------------------------------------------------------

    /**
     * @param email is the email given by the client during the registration
     * @return true if the email is already in the DB, it means that the email has already been registered;
     * false isn't in the DB.
     */
    private boolean authenticate(String email){
        try {
            List<UserCredential> usersCred = users.getUserCredentials();
            if (usersCred != null) {
                for (UserCredential userCred : usersCred) {
                    if (userCred.getEmail().equals(email))
                        return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO
        }
        return false;
    }

    /**
     * Used to add a new user and relative tables in the DB
     * @param credential credentials of the user to be added.
     * @throws Exception relative to the DB.
     */

    private void addUser(UserCredential credential) throws Exception{
        User user = new User();

        users.addUser(user);
        users.addUserCredential(credential);

        user.setCredential(credential);
        credential.setUser(user);
    }

    /**
     * Used to get health params of a user of the last period specified.
     * @param id id of the user.
     * @param period period of time.
     * @return a list of the health params of the user gathered in the specified period.
     * @throws Exception relative to the DB.
     */
    private List<HealthParameters> getPastHealthParams(String id, Long period) throws Exception{
        List<HealthParameters> healthParameters = users.getUserHealthParam(id);
        List<HealthParameters> targetHealthParam = new ArrayList<>();

        Date date = new Date();
        date.setTime(date.getTime() - period);
        for(HealthParameters healthParam: healthParameters)
            if(healthParam.getDate().after(date))
                targetHealthParam.add(healthParam);
        return targetHealthParam;
    }

}
