package fcup.pdm.myapp.util;

import fcup.pdm.myapp.util.AppConstants;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Properties;


/**
 * The CassandraConnection class manages connections to a Cassandra database using the DataStax Java driver.
 */
public class CassandraConnection {
    private static final Logger logger = LogManager.getLogger(DBConnection.class);
    private static CqlSession session;

    // Static initialization block to configure the Cassandra database connection.
    static {
        try {
            Properties props = new Properties();
            props.load(DBConnection.class.getClassLoader().getResourceAsStream("dbconfig.properties"));
            session = new CqlSessionBuilder()
                    .addContactPoint(new InetSocketAddress(props.getProperty("db_Cassandra.ip"),
                            Integer.parseInt(props.getProperty("db_Cassandra.port"))))
                    .withLocalDatacenter(props.getProperty("db_Cassandra.data_center")) // Replace with your data center name
                    .build();
            logger.info("Cassandra connection established successfully");

            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (session != null) {
                    try {
                        session.close();
                        logger.info("Cassandra session closed successfully");
                    } catch (Exception e) {
                        logger.error("Error closing Cassandra session", e);
                    }
                }
            }));
        } catch (Exception e) {
            logger.error("Error initializing datasourceCassandra session: " + e.getMessage());
            throw new RuntimeException("Error initializing Cassandra session", e);
        }
    }

    /**
     * Get the Cassandra database session.
     *
     * @return A CqlSession instance for interacting with the Cassandra database.
     */
    public static CqlSession getSession() {
        return session;
    }
}
