package fcup.pdm.myapp.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * This class configures the Jakarta EE application for the RESTful web service.
 */
@ApplicationPath("/*")
public class MyApplication extends Application {

    /**
     * Get a set of resource classes to be used by the Jakarta EE application.
     *
     * @return A set of resource classes.
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(RegisterResource.class);
        classes.add(LoginResource.class);
        classes.add(FavoriteResource.class);
        classes.add(MovieResource.class);
        classes.add(RefreshTokenResource.class);
        classes.add(AdminResource.class);
        classes.add(StreamResource.class);
        classes.add(UserResource.class);

        return classes;
    }

}

