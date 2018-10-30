package com.barclayadunn.helloworld.resources

import com.barclayadunn.helloworld.core.User
import io.dropwizard.auth.Auth

import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/protected")
@Produces(MediaType.TEXT_PLAIN)
class ProtectedResource {

    @PermitAll
    @GET
    def showSecret(@Auth user: User): String = {
        String.format("Hey there, %s. You know the secret! %d", user.getName(), user.getId)
    }

    @RolesAllowed("ADMIN")
    @GET
    @Path("admin")
    def showAdminSecret(@Auth user: User): String = {
        String.format("Hey there, %s. It looks like you are an admin. %d", user.getName(), user.getId)
    }
}
