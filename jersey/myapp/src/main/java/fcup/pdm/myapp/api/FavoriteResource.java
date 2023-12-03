package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.UserFavoritesDAO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Path("/favorite")
public class FavoriteResource {
    private static final Logger logger = LogManager.getLogger(FavoriteResource.class);
    private UserFavoritesDAO userFavoritesDAO = new UserFavoritesDAO();

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
