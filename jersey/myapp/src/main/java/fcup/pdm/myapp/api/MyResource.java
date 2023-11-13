package fcup.pdm.myapp.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/myresource")
public class MyResource {

    private static final Logger logger = LogManager.getLogger(MyResource.class);  // <-- Initialize the logger

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        logger.info("Endpoint /myresource was called.");  // <-- Log statement
        return "Got it!";
    }
}

