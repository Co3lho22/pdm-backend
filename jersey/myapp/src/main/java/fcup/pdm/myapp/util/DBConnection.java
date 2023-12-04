package fcup.pdm.myapp.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public class DBConnection {

    private static HikariDataSource dataSource;
    private static final Logger logger = LogManager.getLogger(DBConnection.class);

    static {
        try {
            Properties props = new Properties();
            props.load(DBConnection.class.getClassLoader().getResourceAsStream("dbconfig.properties"));

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));

            dataSource = new HikariDataSource(config);
            logger.info("Database connection pool initialized successfully");
        } catch (Exception e) {
            logger.error("Error initializing datasource", e);
            throw new RuntimeException("Error initializing datasource", e);
        }
    }

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
