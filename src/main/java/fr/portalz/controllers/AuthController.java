package fr.portalz.controllers;

import fr.portalz.business.dto.UserDTO;
import fr.portalz.business.entities.User;
import fr.portalz.services.UserService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/v1/auth")
@Tag(name = "Authentication", description = "Authentication related resources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    UserService userService;

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(UserDTO dto) throws Exception {
        try {
            User authenticated = userService.basicAuthentication(dto);
            return Response.ok(authenticated).build();
        } catch (WebApplicationException wae) {
            return Response.status(wae.getResponse().getStatusInfo()).build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserDTO dto) {
        try {
            User registered = userService.basicRegistration(dto);
            return Response.ok(registered).build();
        } catch (WebApplicationException wae) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}
