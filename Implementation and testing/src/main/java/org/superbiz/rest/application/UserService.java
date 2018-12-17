package org.superbiz.rest.application;

import org.superbiz.injection.jpa.model.Credential;

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
