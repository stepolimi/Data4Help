package com.d4h.application.rest.impl;

import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.model.groupOfUsers.GroupUsersDataSent;
import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.request.RequestUserPending;
import com.d4h.application.model.services.RequestGroupService;
import com.d4h.application.model.services.RequestUserService;
import com.d4h.application.model.services.SubscribeService;
import com.d4h.application.model.thirdParty.AcquiredUserDataSent;
import com.d4h.application.model.thirdParty.ThirdParty;
import com.d4h.application.model.thirdParty.ThirdPartyCredential;
import com.d4h.application.model.thirdParty.ThirdPartyData;
import com.d4h.application.model.user.HealthParametersSent;
import com.d4h.application.rest.ThirdPartyService;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.List;

import static javax.ws.rs.core.Response.*;

public class ThirdPartyServiceImpl implements ThirdPartyService {
    @EJB
    UsersDao users;


    /**
     * Used to login an existing third party.
     * @param credential credentials of the existing third party.
     * @return the id of the third party if the request has been successful.
     */
    @Override
    public Response login(ThirdPartyCredential credential) {
        if(credential!= null) {
            String email = credential.getEmail();
            try {
                if (authenticate(email)) {
                    if(authenticateThirdParty(credential)) {
                        ThirdParty thirdParty = users.getThirdPartyByMail(email);
                        return Response.ok(thirdParty.getId(), "application/json").build();
                    }
                }
                return status(Status.BAD_REQUEST).build();
            }catch (Exception e){
                return status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return status(Status.BAD_REQUEST).build();
    }

    /**
     * Used to register a new third party.
     * @param credential credentials of the new third party.
     * @return the id of the third party if the request has been successful.
     */
    @Override
    public Response registration(ThirdPartyCredential credential) {
        if(credential != null) {
            String email = credential.getEmail();
            try {
                if (!authenticate(email)) {
                    String id = addThirdParty(credential);
                    return Response.ok(id, "application/json").build();
                } else
                    return status(Status.BAD_REQUEST).build();

            } catch (Exception e) {
                return status(Status.UNAUTHORIZED).build();
            }
        }
        return status(Status.BAD_REQUEST).build();
    }

    /**
     * Used to inset personal data of third parties.
     * @param thirdPartyData data to be added.
     * @return ok if the request has been successful.
     */
    @Override
    public Response insertPersonalData(ThirdPartyData thirdPartyData) {
        try {
            ThirdParty thirdParty = users.getThirdPartyById(thirdPartyData.getThirdPartyId());
            if (thirdParty != null) {
                thirdPartyData.setThirdParty(thirdParty);
                thirdParty.setData(thirdPartyData);
                thirdParty.getData().getAddress().setThirdParty(thirdParty);
                users.addThirdPartyData(thirdPartyData);
                users.addAddress(thirdParty.getData().getAddress());
                return ok().build();
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Status.BAD_REQUEST).build();
    }

    /**
     * Used to create a new request of user data.
     * @param request request made by the third party.
     * @return ok if the request has been successful.
     */
    @Override
    public Response createUserRequest(RequestUser request) {
        try {
            ThirdParty thirdParty = users.getThirdPartyById(request.getThirdPartyId());
            if (thirdParty != null) {
                thirdParty.addUserRequest(request);
                request.setSender(thirdParty);
                if(RequestUserService.getService().setRequest(request,users)) {
                    users.addRequestUser(request);
                    return ok(request.getId(), "application/json").build();
                }
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Status.BAD_REQUEST).build();
    }


    /**
     * Used to create a new request of group data.
     * @param request request made by the third party,
     * @return ok is the request has been successful.
     */
    @Override
    public Response createGroupRequest(RequestGroup request) {
        try {
            ThirdParty thirdParty = users.getThirdPartyById(request.getThirdPartyId());
            if (thirdParty != null) {
                thirdParty.addGroupRequest(request);
                request.setSender(thirdParty);
                request.getAttributes().setRequestGroup(request);
                users.addRequestGroup(request);
                users.addRequestAttributes(request.getAttributes());
                if(RequestGroupService.getService().evaluate(request,users)) {
                    return ok(request.getId(),"application/json").build();
                }
                return Response.status(Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Status.BAD_REQUEST).build();
    }

    /**
     * Used to get data about a group of users to whom a  third party is already subscribed.
     * @param requestGroup request.
     * @return the data about users.
     */

    @Override
    public Response getGroupData(RequestGroup requestGroup) {
        try {
            ThirdParty thirdParty = users.getThirdPartyById(requestGroup.getThirdPartyId());
            RequestGroup request = users.getRequestGroup(requestGroup.getId());

            if (thirdParty != null && request != null) {
                if (request.isAccepted() && request.isPending()) {
                    request.setPending(false);
                    RequestGroupService.getService().setGroupUsersData(request, users);
                    return ok(RequestGroupService.getService().getAcquiredGroupsData(request), "application/json").build();
                }
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Status.BAD_REQUEST).build();
    }

    /**
     * Used to get data about a user to whom a third party is already subscribed.
     * @param requestUser request.
     * @return the data about user.
     */
    @Override
    public Response getUserData(RequestUser requestUser) {
        try {
            ThirdParty thirdParty = users.getThirdPartyById(requestUser.getThirdPartyId());
            RequestUser request = users.getRequestUser(requestUser.getId());

            if (thirdParty != null && request != null)
                if(!request.isWaiting() && request.isPending() && request.isAccepted()) {
                    request.setPending(false);
                    RequestUserService.getService().setAcquiredData(request, users);
                    return ok(RequestUserService.getService().getAcquiredData(request), "application/json").build();
                }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Status.BAD_REQUEST).build();
    }

    /**
     * Used to get users' data of requests already accepted.
     * @param thirdPartyId id of the third party.
     * @return the required data if the request has been successful.
     */

    @Override
    public Response getSubscribedUserData(String thirdPartyId) {
        try {
            String id = getThirdPartyId(thirdPartyId);
            ThirdParty thirdParty = users.getThirdPartyById(id);
            if (thirdParty != null) {
                SubscribeService.getService().checkNewUserData(thirdParty, users);
                List<AcquiredUserDataSent> acquiredUserDataSents = SubscribeService.getService().getSubscribedUserData(thirdParty);
                return ok(acquiredUserDataSents, "application/json").build();
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Status.BAD_REQUEST).build();
    }

    /**
     * Used to get groups of users' data of requests already accepted.
     * @param thirdPartyId id of the third party.
     * @return the required data if the request has been successful.
     */
    @Override
    public Response getSubscribedGroupData(String thirdPartyId) {
        try {
            String id = getThirdPartyId(thirdPartyId);
            ThirdParty thirdParty = users.getThirdPartyById(id);
            if (thirdParty != null) {
                SubscribeService.getService().checkNewGroupData(thirdParty, users);
                List<GroupUsersDataSent> groupUsersDataSents = SubscribeService.getService().getSubscribedGroupData(thirdParty);
                return ok(groupUsersDataSents, "application/json").build();
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Status.BAD_REQUEST).build();
    }

    /**
     * Used to subscribe to an already accepted user request.
     * @param requestUser the request that has been already accepted.
     * @return ok if the request has been successful.
     */

    @Override
    public Response subscribeUser(RequestUser requestUser) {
        try {
            ThirdParty thirdParty = users.getThirdPartyById(requestUser.getThirdPartyId());
            RequestUser request = users.getRequestUser(requestUser.getId());
            if (thirdParty != null && request != null)
                if (request.isAccepted()) {
                    request.setSubscribed(true);
                    request.setPending(false);
                    users.updateDB();
                    return ok().build();
                }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Status.BAD_REQUEST).build();
    }

    /**
     * Used to subscribe to an already accepted group request.
     * @param requestGroup the request that has been already accepted.
     * @return ok if the request has been successful.
     */

    @Override
    public Response subscribeGroup(RequestGroup requestGroup) {
        try {
            ThirdParty thirdParty = users.getThirdPartyById(requestGroup.getThirdPartyId());
            RequestGroup request = users.getRequestGroup(requestGroup.getId());
            if (thirdParty != null && request != null)
                if(request.isAccepted() ) {
                    request.setSubscribed(true);
                    users.updateDB();
                    return ok().build();
                }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Status.BAD_REQUEST).build();
    }

    /**
     * Used to get the accepted user requests from which was not already taken data.
     * @param thirdPartyId the third party who made the request.
     * @return the list of the pending requests if the request has been successful.
     */
    @Override
    public Response getPendingRequests(String thirdPartyId) {
        try {
            String id = getThirdPartyId(thirdPartyId);
            ThirdParty thirdParty = users.getThirdPartyById(id);
            if (thirdParty != null) {
                List<RequestUserPending> requestUserPendings = RequestUserService.getService().searchNewThirdPartyRequests(thirdParty);
                return ok(requestUserPendings, "application/json").build();
            }
        } catch (Exception e) {
            return status(Response.Status.UNAUTHORIZED).build();
        }
        return status(Status.BAD_REQUEST).build();
    }


    //----------------------------------------other methods-----------------------------------------------------

    /**
     * Used to verify if an email is already associated to an existing third party.
     * @param email email to be verified.
     * @return true if the email is already associated to a third party, false otherwise.
     * @throws Exception exception related to the DB.
     */
    private boolean authenticate(String email) throws Exception {
        List<ThirdPartyCredential> thirdPartiesCredentials = users.getThirdPartiesCredentials();
        if (thirdPartiesCredentials != null) {
            for (ThirdPartyCredential thirdPartyCredential : thirdPartiesCredentials) {
                if (thirdPartyCredential.getEmail().equals(email))
                    return true;
            }
        }
        return false;
    }

    /**
     * Used to add a new third party.
     * @param credential credentials of the third party to be added.
     * @throws Exception relative to the DB requests.
     */
    private String addThirdParty(ThirdPartyCredential credential) throws Exception{
        ThirdParty thirdParty = new ThirdParty();

        thirdParty.setCredential(credential);
        credential.setThirdParty(thirdParty);
        users.addThirdParty(thirdParty);
        users.addThirdPartyCredential(credential);
        return thirdParty.getId();
    }

    /**
     * Used to check if the password associated with the mail of the third party corresponds with the one given.
     * @param credential the credentials that contains email and password.
     * @return true if the password corresponds, false otherwise.
     * @throws Exception exception related to the DB.
     */
    private boolean authenticateThirdParty(ThirdPartyCredential credential) throws Exception{
        ThirdPartyCredential thirdPartyCredential = users.getThirdPartyCredentialByMail(credential.getEmail());
        return thirdPartyCredential!= null && thirdPartyCredential.getEmail().equals(credential.getEmail());
    }

    /**
     * Used to parse the Id of the third party.
     * @param thirdPartyId id to be parsed.
     * @return the id parsed.
     */
    private String getThirdPartyId(String thirdPartyId){
        JsonObject jsonObject = Json.createReader(new StringReader(thirdPartyId)).readObject();
        String id = jsonObject.get("id").toString();
        return id.substring(1,id.length() - 1);
    }

}
