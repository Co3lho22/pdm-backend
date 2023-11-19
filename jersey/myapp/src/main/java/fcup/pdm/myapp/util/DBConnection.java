package fcup.pdm.myapp.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public class DBConnection {

    private static HikariDataSource dataSource;

    static {
        try {
            Properties props = new Properties();
            props.load(DBConnection.class.getClassLoader().getResourceAsStream("dbconfig.properties"));

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing datasource", e);
        }
    }

    public static Connection getConnection() throws Exception {
        return dataSource.getConnection();
    }
}
