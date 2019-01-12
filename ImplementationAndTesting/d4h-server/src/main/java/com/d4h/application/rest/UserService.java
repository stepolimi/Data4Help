package com.d4h.application.rest;

import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.user.Address;
import com.d4h.application.model.user.HealthParameters;
import com.d4h.application.model.user.UserCredential;
import com.d4h.application.model.user.UserData;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/users")
public interface UserService {

    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    Response login(UserCredential credential);

    @POST
    @Path("/registration")
    @Consumes("application/json")
    @Produces("application/json")
    Response registration(UserCredential credential);

    @POST
    @Path("/insertPersonalData")
    @Consumes("application/json")
    Response insertPersonalData(UserData userData);

    @POST
    @Path("/insertWeightHeight")
    @Consumes("application/json")
    Response insertWeightHeight(UserData userData);

    @POST
    @Path("/addHealthParam")
    @Consumes("application/json")
    Response addHealthParam(HealthParameters healthParameters);

    @POST
    @Path("/getDailyHealthParam")
    @Consumes("application/json")
    @Produces("application/json")
    Response getDailyHealthParam(String userId);

    @POST
    @Path("/getWeeklyHealthParam")
    @Consumes("application/json")
    @Produces("application/json")
    Response getWeeklyHealthParam(String userId);

    @POST
    @Path("/getMonthlyHealthParam")
    @Consumes("application/json")
    @Produces("application/json")
    Response getMonthlyHealthParam(String userId);

    @POST
    @Path("/getYearHealthParam")
    @Consumes("application/json")
    @Produces("application/json")
    Response getYearHealthParam(String userId);

    @POST
    @Path("/getRequests")
    @Consumes("application/json")
    @Produces("application/json")
    Response getRequests(String userId);

    @POST
    @Path("/respondRequest")
    @Consumes("application/json")
    Response respondRequest(RequestUser requestUser);

}
