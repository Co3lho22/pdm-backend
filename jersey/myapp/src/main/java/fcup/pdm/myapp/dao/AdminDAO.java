package fcup.pdm.myapp.dao;

import fcup.pdm.myapp.model.Movie;
import fcup.pdm.myapp.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This class provides data access methods for administrative operations.
 */
public class AdminDAO {
    private static final Logger logger = LogManager.getLogger(AdminDAO.class);

    /**
     * Adds a new movie and links it to a genre.
     *
     * @param movie   The movie object to be added.
     * @param genreId The ID of the genre to link the movie to.
     * @return True if the movie was added and linked successfully; otherwise, false.
     */
    public boolean addMovie(Movie movie, int genreId) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            String movieQuery = "INSERT INTO MOVIES (title, duration, rating, " +
                    "release_date, description) VALUES (?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(movieQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDuration());
            ps.setFloat(3, movie.getRating());
            ps.setDate(4, new java.sql.Date(movie.getRelease_date().getTime()));
            ps.setString(5, movie.getDescription());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                connection.rollback();
                return false;
            }

            int movieId;
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movieId = generatedKeys.getInt(1);
                } else {
                    connection.rollback();
                    return false;
                }
            }

            if (!linkMovieWithGenre(movieId, genreId)) {
                connection.rollback();
                return false;
            }

            connection.commit();

            logger.info("Movie added successfully with title: {}", movie.getTitle());
            return true;
        } catch (Exception e) {
            logger.error("Error adding movie: {}", movie.getTitle(), e);

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            closeResources(ps, connection);
        }
    }

    /**
     * Updates movie information.
     *
     * @param movie The updated movie object.
     * @return True if the movie was updated successfully; otherwise, false.
     */
    public boolean updateMovie(Movie movie) {
        String query = "UPDATE MOVIES SET title = ?, duration = ?, " +
                "rating = ?, release_date = ?, description = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDuration());
            ps.setFloat(3, movie.getRating());
            ps.setDate(4, new java.sql.Date(movie.getRelease_date().getTime()));
            ps.setString(5, movie.getDescription());
            ps.setInt(6, movie.getId());

            int rowsAffected = ps.executeUpdate();

            logger.info("Movie updated successfully with ID: {}", movie.getId());
            return rowsAffected > 0;
        } catch (Exception e) {
            logger.error("Error updating movie with ID: {}", movie.getId(), e);
            return false;
        }
    }

    /**
     * Deletes a movie by its ID and unlinks it from genres.
     *
     * @param movieId The ID of the movie to delete.
     * @return True if the movie was deleted successfully; otherwise, false.
     */
    public boolean deleteMovie(int movieId) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            String unlinkQuery = "DELETE FROM MOVIE_GENRES WHERE movie_id = ?";
            ps = connection.prepareStatement(unlinkQuery);
            ps.setInt(1, movieId);
            ps.executeUpdate();

            String deleteQuery = "DELETE FROM MOVIES WHERE id = ?";
            ps = connection.prepareStatement(deleteQuery);
            ps.setInt(1, movieId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            logger.info("Movie deleted successfully with ID: {}", movieId);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting movie with ID: {}", movieId, e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            closeResources(ps, connection);
        }
    }

    /**
     * Adds a new genre to the database.
     *
     * @param genreName The name of the genre to be added.
     * @return True if the genre was added successfully; otherwise, false.
     */
    public boolean addGenre(String genreName) {
        String query = "INSERT INTO GENRES (name) VALUES (?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, genreName);
            int rowsAffected = ps.executeUpdate();

            logger.info("Genre added successfully with name: {}", genreName);
            return rowsAffected > 0;
        } catch (Exception e) {
            logger.error("Error adding genre: {}", genreName, e);
            return false;
        }
    }

    /**
     * Removes a genre from the database by its ID.
     *
     * @param genreId The ID of the genre to be removed.
     * @return True if the genre was removed successfully; otherwise, false.
     */
    public boolean removeGenre(int genreId) {
        String query = "DELETE FROM GENRES WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, genreId);
            int rowsAffected = ps.executeUpdate();

            logger.info("Genre removed successfully with ID: {}", genreId);
            return rowsAffected > 0;
        } catch (Exception e) {
            logger.error("Error removing genre with ID: {}", genreId, e);
            return false;
        }
    }

    /**
     * Links a movie with a genre in the database.
     *
     * @param movieId The ID of the movie to be linked.
     * @param genreId The ID of the genre to link the movie to.
     * @return True if the movie was linked to the genre successfully; otherwise, false.
     */
    private boolean linkMovieWithGenre(int movieId, int genreId) {
        String query = "INSERT INTO MOVIE_GENRES (movie_id, genre_id) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, movieId);
            ps.setInt(2, genreId);
            int rowsAffected = ps.executeUpdate();

            logger.info("Linked movie with id={} with genre with id={} successfully", movieId, genreId);
            return rowsAffected > 0;
        } catch (Exception e) {
            logger.error("Error linking movie with id={} with genre with id={}", movieId, genreId, e);
            return false;
        }
    }

    /**
     * Unlinks a movie from a genre in the database.
     *
     * @param movieId The ID of the movie to be unlinked.
     * @param genreId The ID of the genre to unlink the movie from.
     * @return True if the movie was unlinked from the genre successfully; otherwise, false.
     */
    private boolean unlinkMovieFromGenre(int movieId, int genreId) {
        String query = "DELETE FROM MOVIE_GENRES WHERE movie_id = ? AND genre_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, movieId);
            ps.setInt(2, genreId);
            int rowsAffected = ps.executeUpdate();

            logger.info("Unlinked movie with id={} with genre with id={} successfully", movieId, genreId);
            return rowsAffected > 0;
        } catch (Exception e) {
            logger.error("Error unlinking movie with id={} with genre with id={}", movieId, genreId, e);
            return false;
        }
    }

    /**
     * Removes a user from the database by their ID and deletes related data.
     *
     * @param userId The ID of the user to be removed.
     * @return True if the user was removed successfully; otherwise, false.
     */
    public boolean removeUser(int userId) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            String deleteFavoritesMoviesQuery = "DELETE FROM USER_FAVORITES_MOVIES WHERE user_id = ?";
            ps = connection.prepareStatement(deleteFavoritesMoviesQuery);
            ps.setInt(1, userId);
            ps.executeUpdate();

            String deleteFavoritesGenresQuery = "DELETE FROM USER_FAVORITES_GENRES WHERE user_id = ?";
            ps = connection.prepareStatement(deleteFavoritesGenresQuery);
            ps.setInt(1, userId);
            ps.executeUpdate();

            String deleteAuthQuery = "DELETE FROM USER_AUTH WHERE user_id = ?";
            ps = connection.prepareStatement(deleteAuthQuery);
            ps.setInt(1, userId);
            ps.executeUpdate();

            String deleteUserQuery = "DELETE FROM USERS WHERE id = ?";
            ps = connection.prepareStatement(deleteUserQuery);
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            logger.info("Removed user successfully with userID: {}", userId);
            return true;
        } catch (Exception e) {
            logger.error("Error removing user with userID: {}", userId, e);

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            closeResources(ps, connection);
        }
    }

    /**
     * Closes the prepared statement and database connection.
     *
     * @param ps         The prepared statement to close.
     * @param connection The database connection to close.
     */
    private void closeResources(PreparedStatement ps, Connection connection) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ex) {
                logger.error("Error closing prepared statement", ex);
            }
        }
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException ex) {
                logger.error("Error closing connection", ex);
            }
        }
    }
}
