package fcup.pdm.myapp;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class MyApplication extends Application {
    // You can register resources and providers here if needed.
}

