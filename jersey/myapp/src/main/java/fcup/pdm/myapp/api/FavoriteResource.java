package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.UserFavoritesDAO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/favorite")
public class FavoriteResource {
    private UserFavoritesDAO userFavoritesDAO = new UserFavoritesDAO();

    @POST
    @Path("/movies/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFavoriteMovies(@PathParam("userId") int userId, List<Integer> movieIds) {
        boolean success = userFavoritesDAO.addFavoriteMovies(userId, movieIds);
        if (success) {
            return Response.ok().entity("Favorite movies added successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error adding favorite movies").build();
        }
    }

    @GET
    @Path("/movies/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFavoriteMovies(@PathParam("userId") int userId) {
        List<Integer> favoriteMovies = userFavoritesDAO.getFavoriteMovies(userId);
        return Response.ok(favoriteMovies).build();
    }

    @DELETE
    @Path("/movies/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFavoriteMovies(@PathParam("userId") int userId, List<Integer> movieIds) {
        boolean success = userFavoritesDAO.removeFavoriteMovies(userId, movieIds);
        if (success) {
            return Response.ok().entity("Favorite movies removed successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error removing favorite movies").build();
        }
    }

    @POST
    @Path("/genres/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFavoriteGenres(@PathParam("userId") int userId, List<Integer> genreIds) {
        boolean success = userFavoritesDAO.addFavoriteGenres(userId, genreIds);
        if (success) {
            return Response.ok().entity("Favorite genres added successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error adding favorite genres").build();
        }
    }

    @GET
    @Path("/genres/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFavoriteGenres(@PathParam("userId") int userId) {
        List<Integer> favoriteGenres = userFavoritesDAO.getFavoriteGenres(userId);
        return Response.ok(favoriteGenres).build();
    }

    @DELETE
    @Path("/genres/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFavoriteGenres(@PathParam("userId") int userId, List<Integer> genreIds) {
        boolean success = userFavoritesDAO.removeFavoriteGenres(userId, genreIds);
        if (success) {
            return Response.ok().entity("Favorite genres removed successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error removing favorite genres").build();
        }
    }
}
