package fcup.pdm.myapp.api;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        // Setting up the context for servlet
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        logger.info("Starting the service on port 8080.");
        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        // Adding Jersey Servlet
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Register resources
        String resources = MyResource.class.getCanonicalName() + ";" + UserResource.class.getCanonicalName();
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", resources);

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.stop();
            jettyServer.destroy();
        }
    }
}

