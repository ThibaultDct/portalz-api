package fr.portalz.controllers;

import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import fr.portalz.business.entities.User;
import fr.portalz.services.UserService;

@Path("/api/v1/users")
@Tag(name = "Users", description = "Users related resources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getAllUsers",
            description = "Return all users registered in database",
            summary = "Get all users"
    )
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getUserByUserId",
            description = "Return a user based on given id",
            summary = "Get user by id"
    )
    public User getUserByUserId(@PathParam UUID id) {
        User query = userService.getByUserId(id);
        if (query == null){
            throw new WebApplicationException(Status.NOT_FOUND);
        } else {
            return query;
        }
    }

    @GET
    @Path("/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getUserByUsername",
            description = "Return a user based on given username",
            summary = "Get user by username"
    )
    public User getUserByUsername(@PathParam String username) {
        User query = userService.getByUsername(username);
        if (query == null){
            throw new WebApplicationException(Status.NOT_FOUND);
        } else {
            return query;
        }
    }

    @POST
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "addUser",
            description = "Create a new user",
            summary = "Add user"
    )
    public User addUser(User user){
        User pseudoQuery = userService.getByUsername(user.getUsername());
        User mailQuery = userService.getByMail(user.getMail());
        if (pseudoQuery == null || mailQuery == null){
            throw new WebApplicationException(Status.NOT_ACCEPTABLE);
        } else {
            return userService.add(user.getUsername(), user.getPassword(), user.getRole(), user.getMail());
        }
    }

    @PATCH
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "patchUserByUserId",
            description = "Patch a user with given id with given infos",
            summary = "Patch user by user id"
    )
    public User patchUserByUserId(@PathParam UUID id, User user) {
        User query = userService.patchUserById(id, user);
        if (query == null){
            throw new WebApplicationException(Status.NOT_FOUND);
        } else {
            return query;
        }
    }

}
