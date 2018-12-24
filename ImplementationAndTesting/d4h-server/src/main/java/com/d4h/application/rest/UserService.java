package com.d4h.application.rest;

import com.d4h.application.model.user.UserCredential;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/users")
public interface UserService {

    @POST
    @Consumes("application/json")
    Response registration(UserCredential credential);

    @GET
    String ciao();

    @GET
    Response users();

}
