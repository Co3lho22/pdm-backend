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

public class UserFavoritesDAO {
    private static final Logger logger = LogManager.getLogger(UserFavoritesDAO.class);
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
