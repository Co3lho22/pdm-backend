package fcup.pdm.myapp.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/*")
public class MyApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(RegisterResource.class);
        classes.add(LoginResource.class);
        classes.add(FavGenreResource.class);
        classes.add(MovieResource.class);
        classes.add(RefreshTokenResource.class);

        return classes;
    }

}

