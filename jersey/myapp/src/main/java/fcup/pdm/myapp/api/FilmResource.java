package fcup.pdm.myapp.api;

import fcup.pdm.myapp.model.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/film")
public class FilmResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String GetFilm(User user) {
        return "Hello, World!";
    }
}
