package fcup.pdm.myapp.model;

/**
 * The Permission class represents a permission entity with an ID and a name.
 */
public class Permission {
    private int id;
    private String name;

    /**
     * Default constructor for the Permission class.
     */
    public Permission(){
    }

    /**
     * Set the ID of the permission.
     *
     * @param id The unique identifier of the permission.
     */
    public void setId(int id){ this.id = id; }

    /**
     * Get the ID of the permission.
     *
     * @return The unique identifier of the permission.
     */
    public int getId(){ return this.id; }

    /**
     * Set the name of the permission.
     *
     * @param name The name of the permission.
     */
    public void setName(String name){ this.name = name; }

    /**
     * Get the name of the permission.
     *
     * @return The name of the permission.
     */
    public String getName(){ return this.name; }
}
