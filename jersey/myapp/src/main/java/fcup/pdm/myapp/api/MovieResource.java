package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.MovieDAO;
import fcup.pdm.myapp.model.Movie;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * This class defines a RESTful web service for managing movies.
 */
@Path("/movie")
public class MovieResource {

    private static final Logger logger = LogManager.getLogger(MovieResource.class);
    MovieDAO movieDAO = new MovieDAO();

    /**
     * Retrieves a movie by its ID.
     *
     * @param id The ID of the movie to retrieve.
     * @return A response containing the movie if found or a "not found" response if the movie does not exist.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieById(@PathParam("id") int id) {
        try{
            Movie movie = movieDAO.getMovieById(id);
            if (movie != null) {
                logger.info("Get the movie with the id: {}", id);
                return Response.ok(movie).build();
            } else {
                logger.warn("Movie with the id {} does not exists", id);
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e){
            logger.error("Error getting movie with the id: {}", id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error on the server when " +
                    "tried to get movie with the id: " + id).build();
        }
    }

    /**
     * Retrieves a list of movies based on optional query parameters.
     *
     * @param minRating   The minimum movie rating.
     * @param maxRating   The maximum movie rating.
     * @param startDate   The start date for filtering by release date.
     * @param endDate     The end date for filtering by release date.
     * @param limit       The maximum number of movies to retrieve.
     * @return A response containing a list of movies that match the provided criteria.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies(@QueryParam("minRating") Float minRating,
                              @QueryParam("maxRating") Float maxRating,
                              @QueryParam("startDate") Date startDate,
                              @QueryParam("endDare") Date endDate,
                              @QueryParam("limit") Integer limit) {

        try {

            List<Movie> movies = movieDAO.getMovies(Optional.ofNullable(minRating),
                    Optional.ofNullable(maxRating),
                    Optional.ofNullable(startDate),
                    Optional.ofNullable(endDate),
                    Optional.ofNullable(limit));

            logger.info("Retrieved movies successfully");
            return Response.ok(movies).build();

        } catch (Exception e){
            logger.error("Error retrieving movies", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error on the server when " +
                    "tried to verify the login").build();
        }
    }
}
