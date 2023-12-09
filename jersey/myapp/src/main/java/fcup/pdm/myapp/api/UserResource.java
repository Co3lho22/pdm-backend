package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.UserDAO;
import fcup.pdm.myapp.model.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The UserResource class provides RESTful API endpoints for managing user data.
 */
@Path("/user")
public class UserResource {
    private static final Logger logger = LogManager.getLogger(AdminResource.class);

    /**
     * Updates user data via settings.
     *
     * @param user The User object containing updated user data.
     * @return A Response object with status and message indicating the result of the update operation.
     */
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserDataViaSettings(User user){
        try{
            UserDAO userDAO = new UserDAO();
            if (userDAO.updateUserDataSetting(user)) {
                String jsonResponse = "{\"message\":\"User data updated successfully\"}";
                return Response.ok().entity(jsonResponse).build();
            } else {
                String jsonResponse = "{\"message\":\"Error updating user data\"}";
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse).build();
            }
        }catch (Exception e){
            logger.error("Error updating data via setting for user: {}", user.getUsername());

            String jsonResponse = "{\"message\":\"Error updating user data\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse).build();

        }
    }

}
