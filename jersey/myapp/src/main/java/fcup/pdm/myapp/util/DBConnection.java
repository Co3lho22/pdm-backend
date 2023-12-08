package fcup.pdm.myapp.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * The DBConnection class manages database connections using HikariCP connection pooling.
 */
public class DBConnection {

    private static HikariDataSource dataSource;
    private static final Logger logger = LogManager.getLogger(DBConnection.class);

    // Static initialization block to configure the database connection pool.
    static {
        try {
            Properties props = new Properties();
            props.load(DBConnection.class.getClassLoader().getResourceAsStream("dbconfig.properties"));

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db_MariaDB.url"));
            config.setUsername(props.getProperty("db_MariaDB.user"));
            config.setPassword(props.getProperty("db_MariaDB.password"));

            dataSource = new HikariDataSource(config);
            logger.info("Database connection pool initialized successfully");
        } catch (Exception e) {
            logger.error("Error initializing datasource", e);
            throw new RuntimeException("Error initializing datasource", e);
        }
    }


    /**
     * Get a database connection from the connection pool.
     *
     * @return A database connection.
     * @throws Exception If an error occurs while acquiring a connection.
     */
    public static Connection getConnection() throws Exception {
        try {
            Connection connection = dataSource.getConnection();
            logger.info("Database connection acquired successfully");
            return connection;
        } catch (Exception e) {
            logger.error("Error acquiring database connection", e);
            throw e;
        }
    }
}
