package fcup.pdm.myapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {

    private static Connection connection;

    public static Connection getConnection() throws Exception {
        if (connection == null) {
            Properties props = new Properties();
            props.load(DBConnection.class.getClassLoader().getResourceAsStream("dbconfig.properties"));

            String dbUrl = props.getProperty("db.url");
            String dbUser = props.getProperty("db.user");
            String dbPassword = props.getProperty("db.password");

            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        }
        return connection;
    }
}

