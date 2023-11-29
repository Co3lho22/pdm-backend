package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.FilmDAO;
import fcup.pdm.myapp.model.Film;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Path("/film")
public class FilmResource {

    FilmDAO filmDAO = new FilmDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFilm(Film film) {
        boolean isAdded = filmDAO.addFilm(film);
        if (isAdded) {
            return Response.status(Response.Status.CREATED).entity(film).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFilm(Film film) {
        boolean isUpdated = filmDAO.updateFilm(film);
        if (isUpdated) {
            return Response.ok(film).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteFilm(@PathParam("id") int id) {
        boolean isDeleted = filmDAO.deleteFilm(id);
        if (isDeleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilmById(@PathParam("id") int id) {
        Film film = filmDAO.getFilmById(id);
        if (film != null) {
            return Response.ok(film).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilms(@QueryParam("genre") String genre,
                                @QueryParam("minRating") Float minRating,
                                @QueryParam("maxRating") Float maxRating,
                                @QueryParam("startDate") Date startDate,
                                @QueryParam("endDare") Date endDate,
                                @QueryParam("limit") Integer limit) {

        List<Film> films = filmDAO.getFilms(Optional.ofNullable(genre),
                Optional.ofNullable(minRating),
                Optional.ofNullable(maxRating),
                Optional.ofNullable(startDate),
                Optional.ofNullable(endDate),
                Optional.ofNullable(limit));

        return Response.ok(films).build();
    }
}
