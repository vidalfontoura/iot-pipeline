package com.relay42.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("login")
public class LoginRest {


    @POST
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {

        Date expiry = getExpiryDate(15);
        User user = authenticate(username, password);

        // Issue a token (can be a random String persisted to a database or a
        // JWT token)
        // The issued token must be associated to a user
        // Return the issued token
        String jwtString =
            TokenUtil.getJWTString(username, user.getRoles(),
                user.getVersion(), expiry, key);
        Token token = new com.sample.domain.Token();
        token.setAuthToken(jwtString);
        token.setExpires(expiry);

        return Response.ok(token).build();
    }
}
