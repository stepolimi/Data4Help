package org.superbiz.rest.application;
import org.superbiz.rest.application.model.Credential;

import javax.ws.rs.core.Response;

public class UserServiceImpl implements UserService{

    @Override
    public Response registration(Credential credential) {

        return Response.ok(credential, "application/json").build();
    }

    @Override
    public String ciao(){
        return ("fu");
    }
}
