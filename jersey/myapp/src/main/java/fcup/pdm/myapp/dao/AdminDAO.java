package fcup.pdm.myapp.dao;

import fcup.pdm.myapp.model.Movie;
import fcup.pdm.myapp.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    // Method to add a new movie
    public boolean addMovie(Movie movie, int genreId) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            // Insert movie
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

            // Retrieve generated movie ID
            int movieId;
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movieId = generatedKeys.getInt(1);
                } else {
                    connection.rollback();
                    return false;
                }
            }

            // Link movie with genre
            if (!linkMovieWithGenre(movieId, genreId)) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            closeResources(ps, connection);
        }
    }

    // Method to update an existing movie
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
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete a movie
    public boolean deleteMovie(int movieId) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            // Unlink movie from all genres
            String unlinkQuery = "DELETE FROM MOVIE_GENRES WHERE movie_id = ?";
            ps = connection.prepareStatement(unlinkQuery);
            ps.setInt(1, movieId);
            ps.executeUpdate();

            // Delete movie
            String deleteQuery = "DELETE FROM MOVIES WHERE id = ?";
            ps = connection.prepareStatement(deleteQuery);
            ps.setInt(1, movieId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            closeResources(ps, connection);
        }
    }

    // Method to add a genre
    public boolean addGenre(String genreName) {
        String query = "INSERT INTO GENRES (name) VALUES (?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, genreName);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to remove a genre
    public boolean removeGenre(int genreId) {
        String query = "DELETE FROM GENRES WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, genreId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Link a movie with a genre
    private boolean linkMovieWithGenre(int movieId, int genreId) {
        String query = "INSERT INTO MOVIE_GENRES (movie_id, genre_id) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, movieId);
            ps.setInt(2, genreId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Unlink a movie from a genre
    private boolean unlinkMovieFromGenre(int movieId, int genreId) {
        String query = "DELETE FROM MOVIE_GENRES WHERE movie_id = ? AND genre_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, movieId);
            ps.setInt(2, genreId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeUser(int userId) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            // Delete user favorites
            String deleteFavoritesQuery = "DELETE FROM USER_FAVORITES WHERE user_id = ?";
            ps = connection.prepareStatement(deleteFavoritesQuery);
            ps.setInt(1, userId);
            ps.executeUpdate();

            // Delete user authentication
            String deleteAuthQuery = "DELETE FROM USER_AUTH WHERE user_id = ?";
            ps = connection.prepareStatement(deleteAuthQuery);
            ps.setInt(1, userId);
            ps.executeUpdate();

            // Delete user
            String deleteUserQuery = "DELETE FROM USERS WHERE id = ?";
            ps = connection.prepareStatement(deleteUserQuery);
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            closeResources(ps, connection);
        }
    }

    private void closeResources(PreparedStatement ps, Connection connection) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
