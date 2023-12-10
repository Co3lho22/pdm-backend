package fcup.pdm.myapp.api;

import fcup.pdm.myapp.dao.MovieLinksCassandraDAO;
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

    @POST
    @Path("/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response streamStatus(MovieLink movieLink) {
        int movieId = movieLink.getMovieId();
        String resolution = movieLink.getResolution();
        try {
            MovieLinksCassandraDAO  movieLinksCassandra = new MovieLinksCassandraDAO();
            String status = movieLinksCassandra.getConversionStatus(movieId, resolution);

            String jsonResponse = String.format("{\"status\":\"%s\"}", status);
            return Response.ok().entity(jsonResponse).build();
        }catch (Exception e){
            logger.error("Was not able to return " +
                    "the stream status for the movie with id: {} and resolution: {}", movieId, resolution, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error on the server when tried get the stream status for movie with the id: " + movieId).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response streamMovie(MovieLink movieLink) {
        int movieId = movieLink.getMovieId();
        String resolution = movieLink.getResolution();
        try {
            MovieLinksCassandraDAO  movieLinksCassandra = new MovieLinksCassandraDAO();
            String m3u8FilePath = movieLinksCassandra.getMoviePathFromCassandra(movieId, resolution);

            if (m3u8FilePath != null) {
                String jsonResponse = String.format("{\"movieId\":%d, \"streamUrl\":\"%s\"}", movieId, m3u8FilePath);
                return Response.ok().entity(jsonResponse).build();
            } else {
                MovieLinksDAO movieLinksDAO = new MovieLinksDAO();
                String inputFilePath = movieLinksDAO.getMovieLink(movieId, resolution);

                VideoConverter.convertToHLS(inputFilePath, movieId, resolution, new VideoConverter.ConversionCallback() {
                    @Override
                    public void onSuccess(String outputFilePath) {
                        movieLinksCassandra.setMovieLinkInCassandra(movieId, resolution, outputFilePath);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        logger.error("Video conversion failed for movieId: " + movieId +
                                " with resolution: " + resolution, e);
                    }
                });

                // Return a response indicating that conversion has started
                return Response.accepted().entity("Conversion started").build();
            }
        } catch (Exception e) {
            logger.error("Error streaming movie with the id: {}", movieId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error on the server when tried to stream movie with the id: " + movieId).build();
        }
    }


//    /**
//     * Streams a movie based on the provided movie ID and resolution.
//     *
//     * @return A response containing the URL to the .m3u8 file for streaming.
//     */
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response streamMovie(MovieLink movieLink) {
//        int movieId = movieLink.getMovieId();
//        String resolution = movieLink.getResolution();
//        try {
//            MovieLinksDAO movieLinksDAO = new MovieLinksDAO();
//            String m3u8FilePath = movieLinksDAO.getMoviePathFromCassandra(movieId, resolution);
//
//            if(m3u8FilePath != null){
//                String jsonResponse = String.format("{\"movieId\":%d, \"streamUrl\":\"%s\"}",
//                        movieId,
//                        m3u8FilePath);
//                return Response.ok().entity(jsonResponse).build();
//            }else{
//                String inputFilePath = movieLinksDAO.getMovieLink(movieId, resolution);
//
//                m3u8FilePath = VideoConverter.convertToHLS(inputFilePath,
//                        movieId,
//                        resolution);
//
//                if (m3u8FilePath != null) {
//                    movieLinksDAO.setMovieLinkInCassandra(movieId, resolution, m3u8FilePath);
//
//                    String jsonResponse = String.format("{\"movieId\":%d, \"streamUrl\":\"%s\"}",
//                            movieId,
//                            m3u8FilePath);
//                    return Response.ok().entity(jsonResponse).build();
//                } else {
//                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                            .entity("Error converting video").build();
//                }
//            }
//        } catch (Exception e) {
//            logger.error("Error streaming movie with the id: {}", movieId, e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Error on the server when tried to stream movie with the id: " +
//                            movieId).build();
//        }
//    }
}
