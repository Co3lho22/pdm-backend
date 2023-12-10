package fcup.pdm.myapp.dao;

import fcup.pdm.myapp.util.CassandraConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.util.UUID;

public class MovieLinksCassandraDAO {
    private static final Logger logger = LogManager.getLogger(AdminDAO.class);

    public String getMoviePathFromCassandra(int movie_id, String resolution){
        String query = "SELECT hls_url FROM movie_links WHERE movie_id = ? AND resolution = ?";
        try {
            PreparedStatement preparedStatement = CassandraConnection.getSession().prepare(query);
            ResultSet rs = CassandraConnection.getSession().execute(preparedStatement.bind(movie_id, resolution));

            Row row = rs.one();
            if (row != null) {
                logger.info("Successfully retrieved HLS URL for movieId = {} and resolution = {}", movie_id, resolution);
                return row.getString("hls_url");
            }
        } catch (Exception e) {
            logger.error("Error retrieving HLS URL for movieId = {} and resolution = {}", movie_id, resolution, e);
        }
        return null;
    }

    public boolean isMovieLinkInCassandra(String m3u8FilePath) {
        String query = "SELECT movie_id FROM movie_links WHERE hls_url = ?";
        try {
            PreparedStatement preparedStatement = CassandraConnection.getSession().prepare(query);
            ResultSet rs = CassandraConnection.getSession().execute(preparedStatement.bind(m3u8FilePath));

            if (rs.one() != null) {
                logger.info("hls_url exists with resolution = {}", m3u8FilePath);
                return true;
            } else {
                logger.info("hls_url does not exist with resolution = {}",m3u8FilePath);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error checking hls_url with resolution = {}", m3u8FilePath, e);
            return false;
        }
    }

    public boolean setMovieLinkInCassandra(int movie_id, String resolution, String m3u8FilePath){
        String query = "INSERT INTO movie_links (movie_id, resolution, hls_url) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = CassandraConnection.getSession().prepare(query);
            CassandraConnection.getSession().execute(preparedStatement.bind(movie_id, resolution, m3u8FilePath));

            logger.info("Successfully inserted movie link for movieId = {} with resolution = {}", movie_id, resolution);
            return true;
        } catch (Exception e) {
            logger.error("Error inserting movie link for movieId = {} with resolution = {}", movie_id, resolution, e);
            return false;
        }
    }

    public String getConversionStatus(int movieId, String resolution) {
        String query = "SELECT conversion_status FROM movie_links WHERE movie_id = ? AND resolution = ?";
        try {
            PreparedStatement preparedStatement = CassandraConnection.getSession().prepare(query);
            ResultSet rs = CassandraConnection.getSession().execute(preparedStatement.bind(movieId, resolution));

            Row row = rs.one();
            if (row != null) {
                logger.info("Successfully retrieved conversion status for movieId = {} and resolution = {}", movieId, resolution);
                return row.getString("conversion_status");
            }
        } catch (Exception e) {
            logger.error("Error retrieving conversion status for movieId = {} and resolution = {}", movieId, resolution, e);
        }
        return "pending";
    }

    public static void updateConversionStatus(int movieId, String resolution, String status) {
        String query = "UPDATE movie_links SET conversion_status = ? WHERE movie_id = ? AND resolution = ?";
        try {
            CqlSession session = CassandraConnection.getSession();
            PreparedStatement preparedStatement = session.prepare(query);
            session.execute(preparedStatement.bind(status, movieId, resolution));
            logger.info("Updated conversion status to {} for movieId = {} and resolution = {}", status, movieId, resolution);
        } catch (Exception e) {
            logger.error("Error updating conversion status for movieId = {} and resolution = {}", movieId, resolution, e);
        }
    }
}
