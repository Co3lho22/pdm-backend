package fcup.pdm.myapp.model;

public class Role {
    private int id;
    private String name;

    public Role() {
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){ return this.id; }

    public void setName(String name){ this.name = name; }

    public String getName(){return this.name; }
}