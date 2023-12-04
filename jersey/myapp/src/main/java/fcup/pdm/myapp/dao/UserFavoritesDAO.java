package fcup.pdm.myapp.dao;

import fcup.pdm.myapp.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The UserFavoritesDAO class provides methods to interact with the database for user favorites-related operations.
 * It handles adding, retrieving, and removing favorite movies and genres for a user.
 */
public class UserFavoritesDAO {
    private static final Logger logger = LogManager.getLogger(UserFavoritesDAO.class);

    /**
     * Adds a list of favorite movies for a user in the database.
     *
     * @param userId   The ID of the user for whom to add favorite movies.
     * @param movieIds The list of movie IDs to add as favorites for the user.
     * @return true if the favorite movies are added successfully; false otherwise.
     */
    public boolean addFavoriteMovies(int userId, List<Integer> movieIds) {
        String query = "INSERT INTO USER_FAVORITES_MOVIES (user_id, movie_id) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            for( Integer movieId : movieIds) {
                ps.setInt(1, userId);
                ps.setInt(2, movieId);
                ps.addBatch();
            }

            int[] rowsAffected = ps.executeBatch();
            boolean success = Arrays.stream(rowsAffected).sum() == movieIds.size();
            logger.info("Added favorite movies for user ID: {} | Success: {}", userId, success);
            return success;
        } catch (Exception e) {
            logger.error("Error adding favorite movies for user ID: {}", userId, e);
            return false;
        }
    }

    /**
     * Retrieves a list of favorite movie IDs for a user from the database.
     *
     * @param userId The ID of the user for whom to retrieve favorite movies.
     * @return A list of movie IDs that are favorites for the user.
     */
    public List<Integer> getFavoriteMovies(int userId) {
        List<Integer> favoriteMovies = new ArrayList<>();
        String query = "SELECT movie_id FROM USER_FAVORITES_MOVIES WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    favoriteMovies.add(rs.getInt("movie_id"));
                }
            }
            logger.info("Retrieved favorite movies for user ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error retrieving favorite movies for user ID: {}", userId, e);
        }
        return favoriteMovies;
    }

    /**
     * Removes a list of favorite movies for a user from the database.
     *
     * @param userId   The ID of the user for whom to remove favorite movies.
     * @param movieIds The list of movie IDs to remove as favorites for the user.
     * @return true if the favorite movies are removed successfully; false otherwise.
     */
    public boolean removeFavoriteMovies(int userId, List<Integer> movieIds) {
        String query = "DELETE FROM USER_FAVORITES_MOVIES WHERE user_id = ? AND movie_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            for(Integer movieId : movieIds) {
                ps.setInt(1, userId);
                ps.setInt(2, movieId);
                ps.addBatch();
            }

            int[] rowsAffected = ps.executeBatch();
            boolean success = Arrays.stream(rowsAffected).sum() == movieIds.size();
            logger.info("Removed favorite movies for user ID: {} | Success: {}", userId, success);
            return success;
        } catch (Exception e) {
            logger.error("Error removing favorite movies for user ID: {}", userId, e);
            return false;
        }
    }

    /**
     * Adds a list of favorite genres for a user in the database.
     *
     * @param userId   The ID of the user for whom to add favorite genres.
     * @param genreIds The list of genre IDs to add as favorites for the user.
     * @return true if the favorite genres are added successfully; false otherwise.
     */
    public boolean addFavoriteGenres(int userId, List<Integer> genreIds) {
        String query = "INSERT INTO USER_FAVORITES_GENRES (user_id, genre_id) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            for( Integer genreId : genreIds){
                ps.setInt(1, userId);
                ps.setInt(2, genreId);
                ps.addBatch();
            }

            int[] rowsAffected = ps.executeBatch();
            boolean success = Arrays.stream(rowsAffected).sum() == genreIds.size();
            logger.info("Added favorite genres for user ID: {} | Success: {}", userId, success);
            return success;
        } catch (Exception e) {
            logger.error("Error adding favorite genres for user ID: {}", userId, e);
            return false;
        }
    }

    /**
     * Retrieves a list of favorite genre IDs for a user from the database.
     *
     * @param userId The ID of the user for whom to retrieve favorite genres.
     * @return A list of genre IDs that are favorites for the user.
     */
    public List<Integer> getFavoriteGenres(int userId) {
        List<Integer> favoriteMovies = new ArrayList<>();
        String query = "SELECT genre_id FROM USER_FAVORITES_GENRES WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    favoriteMovies.add(rs.getInt("genres_id"));
                }
            }
            logger.info("Retrieved favorite genres for user ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error retrieving favorite genres for user ID: {}", userId, e);
        }
        return favoriteMovies;
    }

    /**
     * Removes a list of favorite genres for a user from the database.
     *
     * @param userId   The ID of the user for whom to remove favorite genres.
     * @param genreIds The list of genre IDs to remove as favorites for the user.
     * @return true if the favorite genres are removed successfully; false otherwise.
     */
    public boolean removeFavoriteGenres(int userId, List<Integer> genreIds) {
        String query = "DELETE FROM USER_FAVORITES_GENRES WHERE user_id = ? AND genres_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            for(Integer genreId : genreIds) {
                ps.setInt(1, userId);
                ps.setInt(2, genreId);
                ps.addBatch();
            }

            int[] rowsAffected = ps.executeBatch();
            boolean success = Arrays.stream(rowsAffected).sum() == genreIds.size();
            logger.info("Removed favorite genres for user ID: {} | Success: {}", userId, success);
            return success;
        } catch (Exception e) {
            logger.error("Error removing favorite genres for user ID: {}", userId, e);
            return false;
        }
    }



}
