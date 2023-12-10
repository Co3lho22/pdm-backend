package fcup.pdm.myapp.dao;

import fcup.pdm.myapp.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * The MovieLinksDAO class provides methods for interacting with a relational database
 * to retrieve movie links based on movie ID and resolution.
 */
public class MovieLinksDAO {
    private static final Logger logger = LogManager.getLogger(AdminDAO.class);

    /**
     * Retrieves the movie link (URL) from the database for a given movie ID and resolution.
     *
     * @param movie_id   The unique identifier for the movie.
     * @param resolution The desired resolution.
     * @return The movie link URL for the specified movie ID and resolution, or null if not found.
     */
    public String getMovieLink(int movie_id, String resolution){
        String movie_link = null;
        String query = "SELECT link FROM MOVIE_LINKS WHERE movie_id = ? AND resolution = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, movie_id);
            ps.setString(2, resolution);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("Successfully got link for movieId = {} " +
                        "and with resolution = {}", movie_id, resolution);
                movie_link = rs.getString("link");
            }
        } catch (Exception e) {
            logger.error("Error getting the link for movieId = {} " +
                    "and with resolution = {}", movie_id, resolution, e);
        }
        return movie_link;
    }
}
