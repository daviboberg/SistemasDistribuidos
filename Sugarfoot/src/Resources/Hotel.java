package Resources;

public class Hotel implements Resource{
    public int id;
    public String name;

    @Override
    public boolean equals(Resource resource) {
        return id == resource.getId();
    }

    @Override
    public int getId() {
        return this.id;
    }
}
