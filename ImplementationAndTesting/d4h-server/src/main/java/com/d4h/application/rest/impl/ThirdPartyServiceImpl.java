package com.d4h.application.rest.impl;

import com.d4h.application.dao.ThirdParty.ThirdPartyDao;
import com.d4h.application.dao.User.UsersDao;
import com.d4h.application.dao.request.RequestUserDao;
import com.d4h.application.model.request.RequestAttributes;
import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.services.RequestGroupService;
import com.d4h.application.model.services.RequestUserService;
import com.d4h.application.model.thirdParty.ThirdParty;
import com.d4h.application.model.thirdParty.ThirdPartyCredential;
import com.d4h.application.model.thirdParty.ThirdPartyData;
import com.d4h.application.rest.ThirdPartyService;
import org.json.JSONObject;

import javax.ejb.EJB;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.*;

public class ThirdPartyServiceImpl implements ThirdPartyService {
    @EJB
    ThirdPartyDao thirdParties;
    @EJB
    RequestUserDao requests;

    private Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

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
                    ThirdParty thirdParty = thirdParties.getThirdPartyByMail(email);
                    JSONObject response = new JSONObject();
                    response.put("value",thirdParty.getId());
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
                    addThirdParty(credential);

                    //for testing
                    JSONObject response = new JSONObject();
                    response.put("status", "ok");
                    response.put("value", credential.getId());
                    System.out.println(response);
                    return Response.ok().type(MediaType.APPLICATION_JSON).entity(response).build();
                } else
                    return status(Response.Status.UNAUTHORIZED).build();

            } catch (Exception e) {
                return status(Response.Status.FORBIDDEN).build();
            }
        }
        return status(Response.Status.FORBIDDEN).build();
    }

    /**
     * Used to inset personal data of third parties.
     * @param id id of the third party.
     * @param thirdPartyData data to be added.
     * @return ok if the request has been successful.
     */
    @Override
    public Response insertPersonalData(String id, ThirdPartyData thirdPartyData) {
        try {
            ThirdParty thirdParty = thirdParties.getThirdPartyById(id);
            if (thirdParty != null) {
                thirdParties.addThirdPartyData(thirdPartyData);
                thirdPartyData.setThirdParty(thirdParty);
                thirdParty.setData(thirdPartyData);
                return ok().build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to create a new request of user data.
     * @param id id of the requesting third party.
     * @param request request made by the third party.
     * @return ok if the request has been successful.
     */
    @Override
    public Response createUserRequest(String id, RequestUser request) {
        try {
            ThirdParty thirdParty = thirdParties.getThirdPartyById(id);
            if (thirdParty != null) {
                requests.addRequestUser(request);
                thirdParty.addUserRequest(request);
                request.setSender(thirdParty);
                request.setPending(true);
                request.setWaiting(true);
                RequestUserService.getService().setUser(request);
                return ok().build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }


    /**
     * Used to create a new request of group data.
     * @param id id of the requesting third party.
     * @param request request made by the third party,
     * @return ok is the request has been successful.
     */
    @Override
    public Response createGroupRequest(String id, RequestGroup request, RequestAttributes attributes) {
        try {
            ThirdParty thirdParty = thirdParties.getThirdPartyById(id);
            if (thirdParty != null) {
                requests.addRequestGroup(request);
                requests.addRequestAttributes(attributes);
                thirdParty.addGroupRequest(request);
                request.setSender(thirdParty);
                request.setAttributes(attributes);
                attributes.setRequestGroup(request);
                if(RequestGroupService.getService().evaluate(request)) {
                    request.setPending(true);
                    return ok(request.getId(),"application/json").build();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to get data about users of a request already accepted.
     * @param id id of the third party.
     * @param requestId id of the accepted request.
     * @return the data about users.
     */

    @Override
    public Response getGroupData(String id, String requestId) {
        try {
            ThirdParty thirdParty = thirdParties.getThirdPartyById(id);
            RequestGroup request = requests.getRequestGroup(requestId);
            if (thirdParty != null && request != null)
                if(request.isAccepted() ) {
                    if(request.isPending()) {
                        request.setPending(false);
                        return ok(request.getGroupOfUsers().getGroupUsersData(), "application/json").build();
                    }
                    //data already sent
                }
                //request not accepted
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to get data about a user of a request already accepted.
     * @param id id of the third party.
     * @param requestId id of the accepted request.
     * @return the data about user.
     */
    @Override
    public Response getUserData(String id, String requestId) {
        try {
            ThirdParty thirdParty = thirdParties.getThirdPartyById(id);
            RequestUser request = requests.getRequestUser(requestId);
            if (thirdParty != null && request != null)
                if(request.isWaiting()) {
                    if (request.isPending())
                        if (request.isAccepted()) {
                            request.setPending(false);
                            return ok(request.getAcquiredUserData(), "application/json").build();
                        }
                        //request not accepted
                    //data already sent
                }
                // request not yet approved
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to get users' data of requests already accepted.
     * @param id id of the third party.
     * @return the required data if the request has been successful.
     */

    @Override
    public Response getAcquiredUserData(String id) {
        try {
            ThirdParty thirdParty = thirdParties.getThirdPartyById(id);
            if (thirdParty != null)
                return ok(thirdParty.getAcquiredUserData(), "application/json").build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Used to get groups of users' data of requests already accepted.
     * @param id id of the third party.
     * @return the required data if the request has been successful.
     */
    @Override
    public Response getAcquiredGroupData(String id) {
        try {
            ThirdParty thirdParty = thirdParties.getThirdPartyById(id);
            if (thirdParty != null)
                return ok(thirdParty.getGroupUsersData(), "application/json").build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e, () -> "Error while calling users()");
            return serverError().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    //todo: al momento mando i dati come AcquiredUserData per richieste singole e GroupUserData per richiest di gruppi



    //----------------------------------------other methods-----------------------------------------------------

    /**
     * Used to verify if an email is already associated to an existing third party.
     * @param email email to be verified.
     * @return true if the email is already associated to a third party, false otherwise.
     */
    private boolean authenticate(String email){
        try {
            List<ThirdPartyCredential> thirdPartiesCredentials = thirdParties.getThirdPartiesCredentials();
            if (thirdPartiesCredentials != null) {
                for ( ThirdPartyCredential thirdPartyCredential : thirdPartiesCredentials) {
                    if (thirdPartyCredential.getEmail().equals(email))
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
     * Used to add a new third party.
     * @param credential credentials of the third party to be added.
     * @throws Exception relative to the DB requests.
     */
    private void addThirdParty(ThirdPartyCredential credential) throws Exception{
        ThirdParty thirdParty = new ThirdParty();

        thirdParties.addThirdParty(thirdParty);
        thirdParties.addThirdPartyCredential(credential);

        thirdParty.setCredential(credential);
        credential.setThirdParty(thirdParty);
    }

}
