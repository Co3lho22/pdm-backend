package fcup.pdm.myapp.model;

/**
 * The Role class represents a role entity with an ID and a name.
 */
public class Role {
    private int id;
    private String name;

    /**
     * Default constructor for the Role class.
     */
    public Role() {
    }

    /**
     * Set the ID of the role.
     *
     * @param id The unique identifier of the role.
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Get the ID of the role.
     *
     * @return The unique identifier of the role.
     */
    public int getId(){ return this.id; }

    /**
     * Set the name of the role.
     *
     * @param name The name of the role.
     */
    public void setName(String name){ this.name = name; }

    /**
     * Get the name of the role.
     *
     * @return The name of the role.
     */
    public String getName(){return this.name; }
}
