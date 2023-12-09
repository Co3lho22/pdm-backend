package fcup.pdm.myapp.api;

import fcup.pdm.myapp.model.MovieLink;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fcup.pdm.myapp.util.VideoConverter;
import fcup.pdm.myapp.dao.MovieLinksDAO;

/**
 * This class defines a RESTful web service for streaming movies.
 */
@Path("/stream")
public class StreamResource {
    private static final Logger logger = LogManager.getLogger(StreamResource.class);

    /**
     * Streams a movie based on the provided movie ID and resolution.
     *
     * @return A response containing the URL to the .m3u8 file for streaming.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response streamMovie(MovieLink movieLink) {
        int movieId = movieLink.getMovieId();
        String resolution = movieLink.getResolution();
        try {
            MovieLinksDAO movieLinksDAO = new MovieLinksDAO();
            String m3u8FilePath = movieLinksDAO.getMoviePathFromCassandra(movieId, resolution);

            if(m3u8FilePath != null){
                String jsonResponse = String.format("{\"movieId\":%d, \"streamUrl\":\"%s\"}",
                        movieId,
                        m3u8FilePath);
                return Response.ok().entity(jsonResponse).build();
            }else{
                String inputFilePath = movieLinksDAO.getMovieLink(movieId, resolution);

                m3u8FilePath = VideoConverter.convertToHLS(inputFilePath,
                        movieId,
                        resolution);

                if (m3u8FilePath != null) {
                    movieLinksDAO.setMovieLinkInCassandra(movieId, resolution, m3u8FilePath);

                    String jsonResponse = String.format("{\"movieId\":%d, \"streamUrl\":\"%s\"}",
                            movieId,
                            m3u8FilePath);
                    return Response.ok().entity(jsonResponse).build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Error converting video").build();
                }
            }
        } catch (Exception e) {
            logger.error("Error streaming movie with the id: {}", movieId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error on the server when tried to stream movie with the id: " +
                            movieId).build();
        }
    }
}
