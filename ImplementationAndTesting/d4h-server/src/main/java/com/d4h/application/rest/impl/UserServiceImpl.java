package com.d4h.application.rest.impl;

import com.d4h.application.dao.User.UserCredentialDao;
import com.d4h.application.model.user.User;
import com.d4h.application.model.user.UserCredential;
import com.d4h.application.dao.User.UsersDao;

import com.d4h.application.model.user.UserData;
import com.d4h.application.rest.UserService;

import javax.ejb.EJB;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.*;

public class UserServiceImpl implements UserService{

    //@Context
    //SecurityContext sctx;

    @EJB
    UsersDao users;

    private Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public Response login(UserCredential credential) {
        return ok(credential, "application/json").build();

    }

    @Override
    public Response registration(UserCredential credential) {
        String email = credential.getEmail();
        System.out.println("mail: " + email);
        try {
            // authenticate the user using the credentials provided
            if(!authenticate(email)){

                User user = new User();
                user.setCredential(credential);
                credential.setUser(user);

                users.addUserCredential(credential);
                users.addUser(user);

                System.out.println(ok(credential.getId()).build());
                return ok(credential.getId()).build();
            }
            else
                return status(Status.UNAUTHORIZED).build();

        } catch (Exception e) {
            return status(Status.FORBIDDEN).build();
        }
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

    @Override
    public String ciao(){ return ("ciao");}


    /**
     * @param id is the id given during the registration to verify the user
     * @param userData is the json containing some user information
     * @return ok if the request has been successful
     *
     * Add user personal data to the user created during the registration.
     */
    @Override
    public Response insertUserPersonalData(@QueryParam("id") String id, UserData userData) {
        if(checkId(id)){
            try {
                return ok().build();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e, () -> "Error while calling users()");
                return serverError().build();
            }
        }
        return Response.status(Status.UNAUTHORIZED).build();
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
            if (!usersCred.isEmpty()) {
                for (UserCredential userCred : usersCred)
                    if (userCred.getEmail().equals(email))
                        return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO
        }
        return false;
    }

    private boolean checkId(String userId){
        try {
            List<UserCredential> userCredentialList = users.getUserCredentials();
            for(UserCredential userCred : userCredentialList){
                if(userCred.getId().equals(userId))
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO
        }
        return false;
    }

}
