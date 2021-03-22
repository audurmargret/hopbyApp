package is.hi.hbv601g.hopby;


public class Hobby {

    private long id;
    private String name;

    public Hobby(){
    }

    public Hobby(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
