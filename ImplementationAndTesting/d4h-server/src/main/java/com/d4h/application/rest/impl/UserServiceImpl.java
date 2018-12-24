package com.d4h.application.rest.impl;
import com.d4h.application.model.user.UserCredential;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.rest.UserService;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    @EJB
    UsersDao users;

    Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public Response registration(UserCredential credential) {
        return Response.ok(credential, "application/json").build();
    }

    @Override
    public String ciao(){
        return ("fu");
    }

    @Override
    public Response users() {
        try {
            return Response.ok(users.getUsers(), "application/json").build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return Response.serverError().build();
        }
    }


}
