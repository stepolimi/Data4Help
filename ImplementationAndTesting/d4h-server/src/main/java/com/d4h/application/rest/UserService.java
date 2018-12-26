package com.d4h.application.rest;

import com.d4h.application.model.user.UserCredential;
import com.d4h.application.model.user.UserData;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/users")
public interface UserService {

    @GET
    @Path("/login")
    @Consumes("application/json")
    Response login(UserCredential credential);

    @POST
    @Path("/registration")
    @Consumes("application/json")
    @Produces("application/json")
    Response registration(UserCredential credential);

    @GET
    Response users();

    @GET
    @Path("/ciao")
    String ciao();

    @PUT
    @Consumes("application/json")
    Response insertUserPersonalData(@QueryParam("id") String id, UserData userData);

}
