package fcup.pdm.myapp.dao;

import fcup.pdm.myapp.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MovieLinksDAO {
    private static final Logger logger = LogManager.getLogger(AdminDAO.class);

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
