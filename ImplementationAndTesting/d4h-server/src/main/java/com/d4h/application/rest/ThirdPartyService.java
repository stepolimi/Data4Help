package com.d4h.application.rest;

import com.d4h.application.model.request.RequestAttributes;
import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.thirdParty.ThirdPartyCredential;
import com.d4h.application.model.thirdParty.ThirdPartyData;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("thirdParties")
public interface ThirdPartyService {
    @GET
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    Response login(ThirdPartyCredential credential);

    @PUT
    @Path("/registration")
    @Consumes("application/json")
    @Produces("application/json")
    Response registration(ThirdPartyCredential credential);


    @PUT
    @Path("insertPersonalData")
    @Consumes("application/json")
    Response insertPersonalData(@QueryParam("id") String id, ThirdPartyData thirdPartyData);

    @POST
    @Path("createUserRequest")
    @Consumes("application/json")
    Response createUserRequest(@QueryParam("id") String id, RequestUser request);

    @POST
    @Path("createGroupRequest")
    @Consumes("application/json")
    Response createGroupRequest(@QueryParam("id") String id, RequestGroup request, RequestAttributes attributes);

    @GET
    @Path("getGroupData")
    @Consumes("application/json")
    @Produces("application/json")
    Response getGroupData(@QueryParam("id") String id, String requestId);

    @GET
    @Path("getUserData")
    @Consumes("application/json")
    @Produces("application/json")
    Response getUserData(@QueryParam("id") String id, String requestId);

    @GET
    @Path("getAcquiredUserData")
    @Consumes("application/java")
    @Produces("application/java")
    Response getAcquiredUserData(@QueryParam("id") String id);

    @GET
    @Path("getAcquiredGroupData")
    @Consumes("application/java")
    @Produces("application/java")
    Response getAcquiredGroupData(@QueryParam("id") String id);
}
