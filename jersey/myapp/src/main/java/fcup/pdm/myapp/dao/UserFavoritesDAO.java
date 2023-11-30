package fcup.pdm.myapp.dao;

import fcup.pdm.myapp.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserFavoritesDAO {
    public boolean addFavorite(int userId, int movieId) {
        String query = "INSERT INTO USER_FAVORITES (user_id, movie_id) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setInt(2, movieId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> getFavoriteMovies(int userId) {
        List<Integer> favoriteMovies = new ArrayList<>();
        String query = "SELECT movie_id FROM USER_FAVORITES WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    favoriteMovies.add(rs.getInt("movie_id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return favoriteMovies;
    }

    public boolean removeFavorite(int userId, int movieId) {
        String query = "DELETE FROM USER_FAVORITES WHERE user_id = ? AND movie_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setInt(2, movieId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
