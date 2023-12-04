package fcup.pdm.myapp.model;

/**
 * The Genre class represents a movie genre with an ID and a name.
 */
public class Genre {
    private int id;
    private String name;

    /**
     * Default constructor for the Genre class.
     */
    public Genre(){}

    /**
     * Get the ID of the genre.
     *
     * @return The unique identifier of the genre.
     */
    public int getId(){ return id; }

    /**
     * Set the ID of the genre.
     *
     * @param id The unique identifier of the genre.
     */
    public void setId(int id){ this.id = id; }

    /**
     * Get the name of the genre.
     *
     * @return The name of the genre.
     */
    public String getName(){ return name; }

    /**
     * Set the name of the genre.
     *
     * @param name The name of the genre.
     */
    public void setName(String name){ this.name = name; }
}
