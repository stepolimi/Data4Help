package com.d4h.application.rest;

import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.thirdParty.ThirdPartyCredential;
import com.d4h.application.model.thirdParty.ThirdPartyData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("thirdParties")
public interface ThirdPartyService {
    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    Response login(ThirdPartyCredential credential);

    @POST
    @Path("/registration")
    @Consumes("application/json")
    @Produces("application/json")
    Response registration(ThirdPartyCredential credential);


    @POST
    @Path("/insertPersonalData")
    @Consumes("application/json")
    Response insertPersonalData(ThirdPartyData thirdPartyData);

    @POST
    @Path("/createUserRequest")
    @Consumes("application/json")
    Response createUserRequest(RequestUser request);

    @POST
    @Path("/createGroupRequest")
    @Consumes("application/json")
    Response createGroupRequest(RequestGroup request);

    @POST
    @Path("/getGroupData")
    @Consumes("application/json")
    @Produces("application/json")
    Response getGroupData(RequestGroup request);

    @POST
    @Path("/getUserData")
    @Consumes("application/json")
    @Produces("application/json")
    Response getUserData(RequestUser request);

    @POST
    @Path("/getSubscribedUserData")
    @Consumes("application/json")
    @Produces("application/json")
    Response getSubscribedUserData(String thirdPartyId);

    @POST
    @Path("/getSubscribedGroupData")
    @Consumes("application/json")
    @Produces("application/json")
    Response getSubscribedGroupData(String thirdPartyId);

    @POST
    @Path("/subscribeUser")
    @Consumes("application/json")
    Response subscribeUser(RequestUser request);

    @POST
    @Path("/subscribeGroup")
    @Consumes("application/json")
    Response subscribeGroup(RequestGroup request);

    @POST
    @Path("/getPendingRequests")
    @Consumes("application/json")
    @Produces("application/json")
    Response getPendingRequests(String thirdPartyId);

}
