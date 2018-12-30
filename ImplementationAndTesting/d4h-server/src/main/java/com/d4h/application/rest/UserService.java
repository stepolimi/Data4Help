package com.d4h.application.rest;

import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.user.Address;
import com.d4h.application.model.user.HealthParameters;
import com.d4h.application.model.user.UserCredential;
import com.d4h.application.model.user.UserData;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/users")
public interface UserService {

    @GET
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    Response login(UserCredential credential);

    @PUT
    @Path("/registration")
    @Consumes("application/json")
    @Produces("application/json")
    Response registration(UserCredential credential);

    @GET
    Response users();

    @PUT
    @Path("insertPersonalData")
    @Consumes("application/json")
    Response insertPersonalData(@QueryParam("id") String id, UserData userData);

    @PUT
    @Path("insertWeightHeight")
    @Consumes("application/json")
    Response insertWeightHeight(@QueryParam("id") String id, int weight, int height);

    @PUT
    @Path("insertAddress")
    @Consumes("application/json")
    Response insertAddress(@QueryParam("id") String id, Address address);

    @PUT
    @Path("insertHealthParam")
    @Consumes("application/json")
    Response addHealthParam(@QueryParam("id") String id, HealthParameters healthParameters);

    @GET
    @Path("getDailyHealthParam")
    @Consumes("application/json")
    @Produces("application/json")
    Response getDailyHealthParam(@QueryParam("id") String id);

    @GET
    @Path("getWeeklyHealthParam")
    @Consumes("application/json")
    @Produces("application/json")
    Response getWeeklyHealthParam(@QueryParam("id") String id);

    @GET
    @Path("getMonthlyHealthParam")
    @Consumes("application/json")
    @Produces("application/json")
    Response getMonthlyHealthParam(@QueryParam("id") String id);

    @GET
    @Path("getRequests")
    @Consumes("application/json")
    @Produces("application/json")
    Response getRequests(@QueryParam("id") String id);

    @POST
    @Path("respondRequest")
    @Consumes("application/json")
    Response respondRequest(@QueryParam("id") String id, String requestId, boolean accepted);

}
