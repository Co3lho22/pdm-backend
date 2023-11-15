package fcup.pdm.myapp.api;

import fcup.pdm.myapp.model.User;
import fcup.pdm.myapp.dao.UserDAO;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/register")
public class RegisterResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String registerUser(User user) {
        return "Hello, World!";
    }
}

