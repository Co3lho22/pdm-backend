package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.AdminDAO;
import fcup.pdm.myapp.dao.UserDAO;
import fcup.pdm.myapp.model.Movie;
import fcup.pdm.myapp.model.MovieLink;
import fcup.pdm.myapp.model.User;
import fcup.pdm.myapp.util.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * This class defines RESTful web services for admin-related operations.
 */
@Path("/admin")
public class AdminResource {
    private static final Logger logger = LogManager.getLogger(AdminResource.class);
    private AdminDAO adminDAO = new AdminDAO();

    /**
     * Adds a new movie to the system.
     *
     * @param authHeader The authorization header containing a JWT token.
     * @param movie      The movie object to be added.
     * @return A response indicating whether the movie was added successfully or not.
     */
    @POST
    @Path("/addMovie")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovie(@HeaderParam("Authorization") String authHeader, Movie movie) {
        if (!isAuthorized(authHeader, AppConstants.PERMISSION_WRITE)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
        }

        if(!movie.isMovieCompleteToAdd()){
            logger.info("received a movie incomplete");
            return Response.status(Response.Status.BAD_REQUEST).entity("Movie is not complete").build();
        }

        if (adminDAO.addMovie(movie)) {
            logger.info("Movie added successfully: {}", movie.getTitle());
            return Response.ok().entity("Movie added successfully").build();
        } else {
            logger.error("Error adding movie with movie Title {} and genreId {} ", movie.getTitle(), movie.getGenresIds());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error adding movie").build();
        }
    }

    /**
     * Updates an existing movie in the system.
     *
     * @param authHeader The authorization header containing a JWT token.
     * @param movie      The updated movie object with new information.
     * @return A response indicating whether the movie was updated successfully or not.
     */
    @PUT
    @Path("/updateMovie")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMovie(@HeaderParam("Authorization") String authHeader, Movie movie) {
        if (!isAuthorized(authHeader, AppConstants.PERMISSION_WRITE)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
        }

        if(!movie.isMovieCompleteToUpdate()){
            logger.info("received a movie incomplete");
            return Response.status(Response.Status.BAD_REQUEST).entity("Movie is not complete").build();
        }

        if (adminDAO.updateMovie(movie)) {
            logger.info("Movie updated successfully: {}", movie.getTitle());
            return Response.ok().entity("Movie updated successfully").build();
        } else {
            logger.error("Error updating movie: {}", movie.getTitle());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating movie").build();
        }
    }

    /**
     * Updates the links associated with a movie in the system.
     *
     * @param authHeader The authorization header containing a JWT token.
     * @param movieId    The ID of the movie for which links are being updated.
     * @param newLinks   A list of MovieLink objects representing the new links to be associated
     *                   with the movie.
     * @return A response indicating whether the movie links were updated successfully or not.
     */
    @PUT
    @Path("/updateMovieLinks/{movieId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMovieLinks(@HeaderParam("Authorization") String authHeader,
                                     @PathParam("movieId") int movieId,
                                     List<MovieLink> newLinks) {
        if (!isAuthorized(authHeader, AppConstants.PERMISSION_WRITE)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
        }

        if (adminDAO.updateMovieLinks(movieId, newLinks)) {
            return Response.ok().entity("Movie links updated successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating movie links").build();
        }
    }

    /**
     * Adds a new user to the system.
     *
     * @param authHeader      The authorization header containing a JWT token.
     * @param user            The user class with the user metadata.
     * @return A response indicating whether the user was added successfully or not.
     */
    @POST
    @Path("/addUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(@HeaderParam("Authorization") String authHeader,
                            User user) {

        if (!isAuthorized(authHeader, AppConstants.PERMISSION_WRITE)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
        }

        UserDAO userDao = new UserDAO();
        user.setHashedPassword(PasswordUtil.hashPassword(user.getPassword()));

        if(userDao.userExists(user)){
            return Response.status(Response.Status.BAD_REQUEST).entity("User already exists").build();
        }

        if (adminDAO.addUser(user.getUsername(), user.getHashedPassword(), user.getEmail(), user.getCountry(),
                user.getPhone(), user.getRole().getName())) {
            logger.info("User added successfully with username: {}", user.getUsername());
            return Response.ok().entity("User added successfully").build();
        } else {
            logger.error("Error adding user: {}", user.getUsername());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error adding user").build();
        }
    }

    /**
     * Deletes a movie from the system based on its ID.
     *
     * @param authHeader The authorization header containing a JWT token.
     * @param movieId    The ID of the movie to be deleted.
     * @return A response indicating whether the movie was deleted successfully or not.
     */
    @DELETE
    @Path("/deleteMovie/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovie(@HeaderParam("Authorization") String authHeader, @PathParam("movieId") int movieId) {
        if (!isAuthorized(authHeader, AppConstants.PERMISSION_DELETE)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
        }

        if (adminDAO.deleteMovie(movieId)) {
            logger.info("Movie deleted successfully for movie ID: {}", movieId);
            return Response.ok().entity("Movie deleted successfully").build();
        } else {
            logger.error("Error deleting movie with ID: {}", movieId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting movie").build();
        }
    }

    /**
     * Adds a new genre to the system.
     *
     * @param authHeader The authorization header containing a JWT token.
     * @param genreName  The name of the genre to be added.
     * @return A response indicating whether the genre was added successfully or not.
     */
    @POST
    @Path("/addGenre")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenre(@HeaderParam("Authorization") String authHeader, String genreName) {
        if (!isAuthorized(authHeader, AppConstants.PERMISSION_WRITE)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
        }

        if (adminDAO.addGenre(genreName)) {
            logger.info("Genre added successfully: {}", genreName);
            return Response.ok().entity("Genre added successfully").build();
        } else {
            logger.error("Error adding genre: {}", genreName);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error adding genre").build();
        }
    }

    /**
     * Removes a genre from the system based on its ID.
     *
     * @param authHeader The authorization header containing a JWT token.
     * @param genreId    The ID of the genre to be removed.
     * @return A response indicating whether the genre was removed successfully or not.
     */
    @DELETE
    @Path("/removeGenre/{genreId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeGenre(@HeaderParam("Authorization") String authHeader, @PathParam("genreId") int genreId) {
        if (!isAuthorized(authHeader, AppConstants.PERMISSION_DELETE)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
        }

        if (adminDAO.removeGenre(genreId)) {
            logger.info("Genre removed successfully for genre ID: {}", genreId);
            return Response.ok().entity("Genre removed successfully").build();
        } else {
            logger.error("Error removing genre for genre ID: {}", genreId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error removing genre").build();
        }
    }

    /**
     * Removes a user from the system based on their ID.
     *
     * @param authHeader The authorization header containing a JWT token.
     * @param userId     The ID of the user to be removed.
     * @return A response indicating whether the user was removed successfully or not.
     */
    @DELETE
    @Path("/removeUser/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUser(@HeaderParam("Authorization") String authHeader, @PathParam("userId") int userId) {
        if (!isAuthorized(authHeader, AppConstants.PERMISSION_DELETE)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied").build();
        }

        if (adminDAO.removeUser(userId)) {
            logger.info("User removed successfully with ID: {}", userId);
            return Response.ok().entity("User removed successfully").build();
        } else {
            logger.error("Error removing user with ID: {}", userId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error removing user").build();
        }
    }

    /**
     * Checks if the user is authorized to perform a specific action.
     *
     * @param authHeader         The authorization header containing a JWT token.
     * @param requiredPermission The required permission for the action.
     * @return True if the user is authorized, false otherwise.
     */
    private boolean isAuthorized(String authHeader, String requiredPermission) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring("Bearer ".length()).trim();
        try {
            int userId = JwtUtil.getUserIdFromToken(token);
            if (RBACUtil.hasPermission(userId, requiredPermission)) {
                logger.info("User ID: {} is authorized for {}", userId, requiredPermission);
                return true;
            } else {
                logger.warn("User ID: {} is not authorized for {}", userId, requiredPermission);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error verifying authorization", e);
            return false;
        }
    }
}
