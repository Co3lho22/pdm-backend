package fcup.pdm.myapp.api;

import fcup.pdm.myapp.model.User;
import fcup.pdm.myapp.dao.UserDAO;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/register")
public class RegisterResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        // Implement user registration logic here
        // This might include saving the user to a database, password hashing, etc.
        // Return appropriate response, e.g., success or error message
        return Response.ok().entity("User registered successfully").build();
    }
}

