package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.UserFavoritesDAO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * This class defines a RESTful web service for managing user favorite movies and genres.
 */
@Path("/favorite")
public class FavoriteResource {
    private static final Logger logger = LogManager.getLogger(FavoriteResource.class);
    private UserFavoritesDAO userFavoritesDAO = new UserFavoritesDAO();

    /**
     * Adds favorite movies for a user.
     *
     * @param userId    The ID of the user for whom to add favorite movies.
     * @param movieIds  A list of movie IDs to add as favorite movies for the user.
     * @return          A response indicating the success or failure of the operation.
     */
    @POST
    @Path("/movies/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFavoriteMovies(@PathParam("userId") int userId, List<Integer> movieIds) {
        try {
            boolean success = userFavoritesDAO.addFavoriteMovies(userId, movieIds);
            if (success) {
                logger.info("Add favorite movies for user {}", userId);
                return Response.ok().entity("Favorite movies added successfully").build();
            } else {
                logger.warn("Failed to add favorite movies to user {}", userId);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error adding favorite movies").build();
            }
        } catch (Exception e) {
            logger.error("Error adding favorite movies for user: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error occurred while adding favorite movies").build();
        }
    }

    /**
     * Retrieves favorite movies for a user.
     *
     * @param userId    The ID of the user for whom to retrieve favorite movies.
     * @return          A response containing a list of favorite movie IDs.
     */
    @GET
    @Path("/movies/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFavoriteMovies(@PathParam("userId") int userId) {
        try {
            List<Integer> favoriteMovies = userFavoritesDAO.getFavoriteMovies(userId);
            logger.info("Got favorite movies for user {}", userId);
            return Response.ok(favoriteMovies).build();
        } catch (Exception e) {
            logger.error("Error retrieving favorite movies for user: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error occurred while retrieving favorite movies").build();
        }
    }

    /**
     * Removes favorite movies for a user.
     *
     * @param userId    The ID of the user for whom to remove favorite movies.
     * @param movieIds  A list of movie IDs to remove from the user's favorite movies.
     * @return          A response indicating the success or failure of the operation.
     */
    @DELETE
    @Path("/movies/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFavoriteMovies(@PathParam("userId") int userId, List<Integer> movieIds) {
        try {
            boolean success = userFavoritesDAO.removeFavoriteMovies(userId, movieIds);
            if (success) {
                logger.info("Favorite movies for user {} removed successfully", userId);
                return Response.ok().entity("Favorite movies removed successfully").build();
            } else {
                logger.warn("Failed to remove favorite movies to user {}", userId);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error removing favorite movies").build();
            }
        } catch (Exception e) {
            logger.error("Error removing favorite movies for user: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error occurred while removing favorite movies").build();
        }
    }

    /**
     * Adds favorite genres for a user.
     *
     * @param userId    The ID of the user for whom to add favorite genres.
     * @param genreIds  A list of genre IDs to add as favorite genres for the user.
     * @return          A response indicating the success or failure of the operation.
     */
    @POST
    @Path("/genres/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFavoriteGenres(@PathParam("userId") int userId, List<Integer> genreIds) {
        try {
            boolean success = userFavoritesDAO.addFavoriteGenres(userId, genreIds);
            if (success) {
                logger.info("Add favorite genres for user {}", userId);
                return Response.ok().entity("Favorite genres added successfully").build();
            } else {
                logger.warn("Failed to add favorite genres to user {}", userId);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error adding favorite genres").build();
            }
        } catch (Exception e) {
            logger.error("Error adding favorite genres for user: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error occurred while adding favorite genres").build();
        }
    }

    /**
     * Retrieves favorite genres for a user.
     *
     * @param userId    The ID of the user for whom to retrieve favorite genres.
     * @return          A response containing a list of favorite genre IDs.
     */
    @GET
    @Path("/genres/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFavoriteGenres(@PathParam("userId") int userId) {
        try {
            List<Integer> favoriteGenres = userFavoritesDAO.getFavoriteGenres(userId);
            logger.info("Got favorite genres for user {}", userId);
            return Response.ok(favoriteGenres).build();
        } catch (Exception e) {
            logger.error("Error retrieving favorite genres for user: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error occurred while retrieving favorite genres").build();
        }
    }

    /**
     * Removes favorite genres for a user.
     *
     * @param userId    The ID of the user for whom to remove favorite genres.
     * @param genreIds  A list of genre IDs to remove from the user's favorite genres.
     * @return          A response indicating the success or failure of the operation.
     */
    @DELETE
    @Path("/genres/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFavoriteGenres(@PathParam("userId") int userId, List<Integer> genreIds) {
        try {
            boolean success = userFavoritesDAO.removeFavoriteGenres(userId, genreIds);
            if (success) {
                logger.info("Favorite genres for user {} removed successfully", userId);
                return Response.ok().entity("Favorite genres removed successfully").build();
            } else {
                logger.warn("Failed to remove favorite genres to user {}", userId);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error removing favorite genres").build();
            }
        } catch (Exception e) {
            logger.error("Error removing favorite genres for user: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error occurred while removing favorite genres").build();
        }
    }
}
