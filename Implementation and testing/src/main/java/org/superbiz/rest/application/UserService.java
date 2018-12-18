package org.superbiz.rest.application;

import org.superbiz.rest.application.model.Credential;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/users")
public interface UserService {

    @POST
    @Consumes("application/json")
    Response registration(Credential credential);

    @GET
    String ciao();

}
