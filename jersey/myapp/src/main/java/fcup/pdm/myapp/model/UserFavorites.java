package fcup.pdm.myapp.model;

public class UserFavorites {
    private int user_id;
    private int movie_id;

    public UserFavorites(){}

    public int getUser_id(){ return user_id; }

    public void setUser_id(int user_id){ this.user_id = user_id; }

    public int getMovie_id(){ return movie_id; }

    public void setMovie_id(int movie_id){ this.movie_id = movie_id; }
}
