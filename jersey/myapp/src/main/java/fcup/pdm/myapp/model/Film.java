package fcup.pdm.myapp.model;

import java.util.Date;

public class Film {
    private int id;
    private String title;
    private String genre;
    private int duration;  // In minutes
    private float rating;
    private Date release_date;
    private String description;


    public Film() {}


    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int getId(){ return id; }

    public void setId(int id){ this.id = id; }

    public String getGenre(){ return genre;}

    public void setGenre(String genre){ this.genre = genre; }

    public int getDuration(){ return duration; }

    public void setDuration(int duration){ this.duration = duration; }

    public float getRating(){ return rating; }

    public void setRating( float rating){ this.rating = rating; }

    public Date getRelease_date(){ return release_date; }

    public void setRelease_date(Date release_date){ this.release_date = release_date; }

    public String getDescription(){ return description; }

    public void setDescription(String description){ this.description = description; }

}
