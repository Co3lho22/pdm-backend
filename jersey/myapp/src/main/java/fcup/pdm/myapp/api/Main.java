package fcup.pdm.myapp.api;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * This is the main class that starts the Jetty server for the application.
 */
public class Main {

    /**
     * The main method that starts the Jetty server and configures the Jersey Servlet for REST API handling.
     *
     * @param args Command-line arguments (not used in this application).
     * @throws Exception If there's an error while starting the server.
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        server.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter(
			"jakarta.ws.rs.Application", MyApplication.class.getCanonicalName());

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
}

