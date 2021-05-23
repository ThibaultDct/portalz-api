package fr.portalz.controllers;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import fr.portalz.business.entities.User;
import fr.portalz.services.UserService;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
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
    public User getUserByUsername(@PathParam String username) {
        User query = userService.getByUsername(username);
        if (query == null){
            throw new WebApplicationException(Status.NOT_FOUND);
        } else {
            return query;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User addUser(User user){
        return userService.add(user.getUsername(), user.getPassword(), user.getRole(), user.getMail());
    }

}
